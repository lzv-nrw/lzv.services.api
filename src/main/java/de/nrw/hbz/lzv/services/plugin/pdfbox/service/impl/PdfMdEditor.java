package de.nrw.hbz.lzv.services.plugin.pdfbox.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.AbstractMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.Calendar;

import javax.xml.transform.TransformerException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.pdmodel.common.PDMetadata;
import org.apache.xmpbox.XMPMetadata;
import org.apache.xmpbox.schema.AdobePDFSchema;
import org.apache.xmpbox.schema.DublinCoreSchema;
import org.apache.xmpbox.schema.XMPBasicSchema;
import org.apache.xmpbox.xml.DomXmpParser;
import org.apache.xmpbox.xml.XmpSerializer;
import org.json.JSONObject;

import de.nrw.hbz.lzv.services.model.json.impl.PdfInfo;
import de.nrw.hbz.lzv.services.model.json.model.PdfInfoModel;
import de.nrw.hbz.lzv.services.model.pdf.edit.PdfBoxEditResult;
import de.nrw.hbz.lzv.services.template.HtmlTemplate;

/**
 * Service for editing PDF Info Dictionary and XMP metadata.
 */
public class PdfMdEditor extends de.nrw.hbz.lzv.services.impl.PdfMdEditor {

	private static Logger logger = LogManager.getLogger(PdfMdEditor.class);
	private static final ScheduledExecutorService DELETE_EXECUTOR = Executors.newSingleThreadScheduledExecutor();

	protected PdfBoxEditResult pdfEditRes = null;

	/**
	 * Maps PDF metadata keys to their corresponding PdfInfo setters
	 * 
	 */
	private static final Map<String, BiConsumer<PDDocumentInformation, String>> PDF_INFO_SETTERS = Map.of("Title",
			PDDocumentInformation::setTitle, "Author", PDDocumentInformation::setAuthor, "Subject",
			PDDocumentInformation::setSubject, "Keywords", PDDocumentInformation::setKeywords, "Creator",
			PDDocumentInformation::setCreator, "Producer", PDDocumentInformation::setProducer);

	/**
	 * Maps PDF metadata keys to their corresponding XMP DC setters
	 * 
	 */
	private static final Map<String, BiConsumer<DublinCoreSchema, String>> PDF_XMP_DC_SETTERS = Map.of("Title",
			DublinCoreSchema::setTitle, "Author", (dc, value) -> {
				if (dc.getCreators() != null) {
					for (String creator : dc.getCreators()) {
						dc.removeCreator(creator);
					}
				}
				dc.addCreator(value);
			}, "Subject", DublinCoreSchema::setDescription);

	/**
	 * Maps PDF metadata keys to their corresponding XMP Adobe PDF setter methods.
	 * 
	 */
	private static final Map<String, BiConsumer<AdobePDFSchema, String>> PDF_XMP_ADOBE_PDF_SETTERS = Map.of("Keywords",
			AdobePDFSchema::setKeywords, "Producer", AdobePDFSchema::setProducer);

	/**
	 * Maps PDF metadata keys to their corresponding XMP Basic setter methods.
	 * 
	 */
	private static final Map<String, BiConsumer<XMPBasicSchema, String>> PDF_XMP_BASIC_SETTERS = Map.of("Creator",
			XMPBasicSchema::setCreatorTool);

	/**
	 * Edits a PDF metadata value in both the PDF Info Dictionary and the XMP
	 * metadata.
	 * 
	 * @param the input PDF file
	 * @param the original file name
	 * @param the metadata key to be changed
	 * @param the new metadata value
	 */
	@Override
	public PdfBoxEditResult editPdfMd(File file, String fileName, String key, String value) {

		pdfEditRes = new PdfBoxEditResult();

		pdfEditRes.setLoadedFileName(fileName);
		PDDocument pdDoc;
		File editedFile = null;

		logger.info("Filename: " + pdfEditRes.getLoadedFileName());
		logger.info("File: " + file.getAbsolutePath());

		try {
			pdDoc = Loader.loadPDF(file);
			pdfInfo = getPdfInfo(pdDoc);
			pdfEditRes.setOldMetadatatPair(
					new AbstractMap.SimpleEntry<>(key, pdfInfo.getJSONObject().optString(key.toLowerCase())));

			editedFile = File.createTempFile("edited_", ".pdf");
			logger.info("Output file: " + editedFile.getAbsolutePath());

			pdfEditRes.setNewMetadatatPair(new AbstractMap.SimpleEntry<>(key, value));
			pdDoc.setDocumentInformation(setPdfInfo(pdDoc.getDocumentInformation(), key, value));

			PDMetadata metadata = pdDoc.getDocumentCatalog().getMetadata();

			XMPMetadata xmp;

			if (metadata != null) {

				try (InputStream is = metadata.exportXMPMetadata()) {
					DomXmpParser parser = new DomXmpParser();
					xmp = parser.parse(is);
				}

			} else {
				xmp = XMPMetadata.createXMPMetadata();
			}

			setXMP(xmp, key, value);

			ByteArrayOutputStream baos = new ByteArrayOutputStream();

			XmpSerializer serializer = new XmpSerializer();
			serializer.serialize(xmp, baos, true);

			PDMetadata newMetadata = new PDMetadata(pdDoc, new java.io.ByteArrayInputStream(baos.toByteArray()));

			pdDoc.getDocumentCatalog().setMetadata(newMetadata);

			pdDoc.save(editedFile);
			pdfEditRes.setFileOutputLocation(editedFile.getAbsolutePath());
			logger.info("Edited PDF successfully saved to: " + editedFile.getAbsolutePath());

			int fileDeleteTime = 120;
			DELETE_EXECUTOR.schedule(() -> {
				try {

					File outputFile = new File(pdfEditRes.getFileOutputLocation());

					if (outputFile.exists()) {
						if (!outputFile.delete()) {
							logger.warn("Cannot delete temp file " + pdfEditRes.getFileOutputLocation());
						}
					}
				} catch (Exception e) {
					logger.error("Error deleting the temp file " + pdfEditRes.getFileOutputLocation(), e);
				}
			}, fileDeleteTime, TimeUnit.MINUTES);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return pdfEditRes;
	}

	/**
	 * get the information stored in the PDF information part
	 * 
	 * @return
	 */
	private PdfInfo getPdfInfo(PDDocument pdDocument) {
		PdfInfoProvider infoProvider = new PdfInfoProvider(pdDocument);
		return infoProvider.getPdfInfo();
	}

	/**
	 * Sets a PDF Info Dictionary metadata value.
	 * 
	 * @param documentInfo the PDF document information
	 * @param key          the metadata key
	 * @param value        the new metadata value
	 *
	 * @return the modified document information
	 */
	private PDDocumentInformation setPdfInfo(PDDocumentInformation documentInfo, String key, String value) {
		logger.info("Original PdfInfo: " + documentInfo.getCOSObject().toString());

		BiConsumer<PDDocumentInformation, String> setter = PDF_INFO_SETTERS.get(key);

		if (setter == null) {
			throw new IllegalArgumentException("Unknown PDF metadata key: " + key);
		}
		setter.accept(documentInfo, value);
		documentInfo.setModificationDate(Calendar.getInstance());
		logger.info("Edited PdfInfo: " + documentInfo.getCOSObject().toString());
		return documentInfo;
	}

	/**
	 * Sets a XMP metadata value.
	 * 
	 * @param xmp   the XMP metadata
	 * @param key   the metadata key
	 * @param value the new metadata value
	 * 
	 * @return the modified PdfInfo object
	 */
	private XMPMetadata setXMP(XMPMetadata xmp, String key, String value) {

		logXmp(xmp, "XMP original");

		DublinCoreSchema dc = xmp.getDublinCoreSchema();

		if (dc == null) {
			dc = xmp.createAndAddDublinCoreSchema();
		}

		AdobePDFSchema pdf = xmp.getAdobePDFSchema();

		if (pdf == null) {
			pdf = xmp.createAndAddAdobePDFSchema();
		}

		XMPBasicSchema xmpBasic = xmp.getXMPBasicSchema();

		if (xmpBasic == null) {
			xmpBasic = xmp.createAndAddXMPBasicSchema();
		}

		BiConsumer<DublinCoreSchema, String> dcSetter = PDF_XMP_DC_SETTERS.get(key);

		if (dcSetter != null) {
			dcSetter.accept(dc, value);
		} else {

			BiConsumer<AdobePDFSchema, String> pdfSetter = PDF_XMP_ADOBE_PDF_SETTERS.get(key);

			if (pdfSetter != null) {
				pdfSetter.accept(pdf, value);
			} else {

				BiConsumer<XMPBasicSchema, String> basicSetter = PDF_XMP_BASIC_SETTERS.get(key);

				if (basicSetter != null) {
					basicSetter.accept(xmpBasic, value);
					xmpBasic.setModifyDate(Calendar.getInstance());

				} else {
					throw new IllegalArgumentException("Unknown PDF XMP metadata key: " + key);
				}
			}
		}

		logXmp(xmp, "XMP edited");

		return xmp;

	}

	/**
	 * Serializes the XMP metadata and writes it to the logger.
	 *
	 * <p>
	 * Serialization errors are logged instead of being propagated because this
	 * method is only used for diagnostic logging.
	 * </p>
	 *
	 * @param xmp     the XMP metadata to serialize
	 * @param message the message used as a prefix for the log entry
	 */
	private void logXmp(XMPMetadata xmp, String message) {

		ByteArrayOutputStream out = new ByteArrayOutputStream();

		XmpSerializer serializer = new XmpSerializer();

		try {
			serializer.serialize(xmp, out, true);

			logger.info(message + ": " + out.toString(StandardCharsets.UTF_8));

		} catch (TransformerException e) {
			logger.error("Could not serialize XMP metadata for logging", e);
		}
	}

	@Override
	public String getHtml() {
		resultBuffer.append(HtmlTemplate.getHtmlHead());

		resultBuffer.append("<h1>Ergebnis der PDF-Metadatenbearbeitung mit PDFBox</h1>\n");
		resultBuffer.append("<h2>Datei zur Bearbeitung: " + pdfEditRes.getLoadedFileName() + "</h2>\n");

		if (pdfEditRes.getFileOutputLocation() != null) {
			resultBuffer.append(
					"<h3 style=\"color: darkgreen;)\">Bearbeitung erfolgreich <i class=\"fa-solid fa-check\"></i></h3>\n");
		} else {
			resultBuffer
					.append("<h3 style=\"color: red;)\">Bearbeitung fehlgeschlagen <i class=\"fa-solid fa-xmark\"></i></h3>\n");
		}
		if (pdfInfo != null && !pdfInfo.getJSONObject().isEmpty()) {
			resultBuffer.append("<h3>Alte Metadaten:</h3>\n<ul>\n");
			resultBuffer.append("<li>")
					.append(PdfInfoModel.getInfoLabel().get(pdfEditRes.getOldMetadataPair().getKey().toLowerCase())).append(": ")
					.append(pdfEditRes.getOldMetadataPair().getValue()).append("</li>\n</ul>\n");
			resultBuffer.append("<h3>Neue Metadaten:</h3>\n<ul>\n");
			resultBuffer.append("<li>")
					.append(PdfInfoModel.getInfoLabel().get(pdfEditRes.getNewMetadataPair().getKey().toLowerCase())).append(": ")
					.append(pdfEditRes.getNewMetadataPair().getValue()).append("</li>\n</ul>\n");

		}
		if (pdfEditRes.getFileOutputLocation() != null) {
			resultBuffer.append("<p><i class=\"fa-solid fa-download\"></i><a href=\"/lzv-api/downloadedit?fileName="
					+ pdfEditRes.getFileOutputLocation() + "&origFileName=" + pdfEditRes.getLoadedFileName()
					+ "\">Editierte PDF Datei herunterladen</a> (Link 120 Minuten gültig)</p>");
		}

		resultBuffer.append(
				"<p><i class=\"fa-regular fa-pen-to-square\"></i><a href=\"/lzv-jsp/pdfbox/editMd\">Weitere PDF Metadaten-Bearbeitung</a></p>");

		resultBuffer.append(HtmlTemplate.getHtmlFoot());

		return resultBuffer.toString();
	}

	@Override
	public String getJson() {
		if (pdfEditRes == null) {
			return "{}";
		}

		JSONObject resultJson = new JSONObject();

		resultJson.put("file", pdfEditRes.getLoadedFileName());

		JSONObject oldMetadataPair = new JSONObject();
		oldMetadataPair.put("key", pdfEditRes.getOldMetadataPair().getKey());
		oldMetadataPair.put("value", pdfEditRes.getOldMetadataPair().getValue());
		resultJson.put("oldMetadataPair", oldMetadataPair);

		JSONObject newMetadataPair = new JSONObject();
		newMetadataPair.put("key", pdfEditRes.getNewMetadataPair().getKey());
		newMetadataPair.put("value", pdfEditRes.getNewMetadataPair().getValue());
		resultJson.put("newMetadataPair", newMetadataPair);

		if (pdfEditRes.getFileOutputLocation() != null) {
			resultJson.put("fileOutputLocation", "/lzv-api/downloadedit?fileName=" + pdfEditRes.getFileOutputLocation()
					+ "&origFileName=" + pdfEditRes.getLoadedFileName());
		}
		return resultJson.toString(3);
	}

}
