package de.nrw.hbz.lzv.services.plugin.pdfbox.service.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.Calendar;
import java.util.List;

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
import org.apache.xmpbox.type.BadFieldValueException;
import org.apache.xmpbox.xml.DomXmpParser;
import org.apache.xmpbox.xml.XmpSerializer;
import org.json.JSONObject;

import de.nrw.hbz.lzv.services.model.json.impl.PdfInfo;
import de.nrw.hbz.lzv.services.model.json.model.PdfInfoModel;
import de.nrw.hbz.lzv.services.model.pdf.edit.MetadataToUpdate;
import de.nrw.hbz.lzv.services.model.pdf.edit.PdfBoxEditResult;
import de.nrw.hbz.lzv.services.template.HtmlTemplate;

/**
 * Service for editing PDF Info Dictionary and XMP metadata.
 *
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
	public PdfBoxEditResult editPdfMd(File file, String fileName, List<MetadataToUpdate> metadataUpdates) {

		pdfEditRes = new PdfBoxEditResult();

		pdfEditRes.setLoadedFileName(fileName);

		logger.info("Filename: " + pdfEditRes.getLoadedFileName());
		logger.info("File: " + file.getAbsolutePath());

		try (PDDocument pdDoc = Loader.loadPDF(file)) {

			String[] keys = { "title", "author", "subject", "keywords", "creator", "producer" };

			for (String key : keys) {
				pdfEditRes.addOldMetadataPair(key, getPdfInfo(pdDoc).getJSONObject().optString(key.toLowerCase(), ""));
			}

			for (MetadataToUpdate metadataUpdate : metadataUpdates) {
				updateMetadata(pdDoc, metadataUpdate);
			}

			for (String key : keys) {
				pdfEditRes.addNewMetadataPair(key, getPdfInfo(pdDoc).getJSONObject().optString(key.toLowerCase(), ""));
			}

			File editedFile = saveEditedPdf(pdDoc);

			String outputFilePath = editedFile.getAbsolutePath();
			pdfEditRes.setFileOutputLocation(outputFilePath);
			logger.info("PDF with updated metadata successfully saved to: " + outputFilePath);

			int fileDeleteTime = 120;
			DELETE_EXECUTOR.schedule(() -> {
				try {

					File outputFile = new File(outputFilePath);

					if (outputFile.exists() && !outputFile.delete()) {
						logger.info("Cannot delete temp file " + outputFilePath);
					}
				} catch (Exception e) {
					logger.info("Error deleting the temp file " + outputFilePath, e);
				}
			}, fileDeleteTime, TimeUnit.MINUTES);

		} catch (Exception e) {
			logger.info("Error while editing PDF metadata for file: " + fileName, e);
			pdfEditRes.setFileOutputLocation(null);
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
	 * Updates the PDF Info Dictionary and XMP metadata.
	 *
	 * @param pdDoc            the PDF document
	 * @param metadataToUpdate the metadata to update
	 * @throws Exception if metadata processing fails
	 */
	private void updateMetadata(PDDocument pdDoc, MetadataToUpdate metadataToUpdate) throws Exception {

		String key = metadataToUpdate.getKey();

		if (key == null || key.isBlank()) {

			throw new IllegalArgumentException("Metadata key must not be empty");
		}

		updatePdfInfo(pdDoc, metadataToUpdate);
		updateXmpMetadata(pdDoc, metadataToUpdate);
	}

	/**
	 * Updates the PDF Info Dictionary metadata.
	 *
	 * @param pdDoc            the PDF document
	 * @param metadataToUpdate the metadata to update
	 */
	private void updatePdfInfo(PDDocument pdDoc, MetadataToUpdate metadataToUpdate) {

		pdDoc.setDocumentInformation(setPdfInfo(pdDoc.getDocumentInformation(), metadataToUpdate));
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
	private PDDocumentInformation setPdfInfo(PDDocumentInformation documentInfo, MetadataToUpdate metadataToUpdate) {

		String key = metadataToUpdate.getKey();
		String value = metadataToUpdate.getValue();

		BiConsumer<PDDocumentInformation, String> setter = PDF_INFO_SETTERS.get(key);

		if (setter == null) {
			throw new IllegalArgumentException("Unknown PDF metadata key: " + key);
		}
		setter.accept(documentInfo, value);
		documentInfo.setModificationDate(Calendar.getInstance());
		return documentInfo;
	}

	/**
	 * Updates the XMP metadata.
	 *
	 * @param pdDoc            the PDF document
	 * @param metadataToUpdate the metadata to update
	 * @throws Exception if XMP processing fails
	 */
	private void updateXmpMetadata(PDDocument pdDoc, MetadataToUpdate metadataToUpdate) throws Exception {

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

		PDDocumentInformation info = pdDoc.getDocumentInformation();

		Map<String, String> values = Map.of("Title", info.getTitle() == null ? "" : info.getTitle(), "Author",
				info.getAuthor() == null ? "" : info.getAuthor(), "Subject", info.getSubject() == null ? "" : info.getSubject(),
				"Keywords", info.getKeywords() == null ? "" : info.getKeywords(), "Creator",
				info.getCreator() == null ? "" : info.getCreator(), "Producer",
				info.getProducer() == null ? "" : info.getProducer());

		for (Map.Entry<String, String> entry : values.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();

			if (!value.isBlank()) {
				String current = getXmpValue(xmp, key);

				if (current == null || current.isBlank()) {
					setXMP(xmp, new MetadataToUpdate(key, value));
				}
			}
		}

		setXMP(xmp, metadataToUpdate);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		XmpSerializer serializer = new XmpSerializer();

		serializer.serialize(xmp, baos, true);

		PDMetadata newMetadata = new PDMetadata(pdDoc, new ByteArrayInputStream(baos.toByteArray()));

		pdDoc.getDocumentCatalog().setMetadata(newMetadata);
	}

	/**
	 * Retrieves the current value of XMP metadata field
	 * 
	 * @param xmp the XMP metadata
	 * @param key the metadata key (Title, Author, Subject, Keywords, Creator or
	 *            Producer)
	 * @return the current XMP value, or {@code null} if the schema or field does
	 *         not exist or the key is unknown
	 */
	private String getXmpValue(XMPMetadata xmp, String key) {
		switch (key) {
		case "Title": {
			DublinCoreSchema dc = xmp.getDublinCoreSchema();
			try {
				return dc != null ? dc.getTitle() : null;
			} catch (BadFieldValueException e) {
				e.printStackTrace();
			}
		}
		case "Author": {
			DublinCoreSchema dc = xmp.getDublinCoreSchema();
			return dc != null && dc.getCreators() != null && !dc.getCreators().isEmpty() ? String.join(", ", dc.getCreators())
					: null;
		}
		case "Subject": {
			DublinCoreSchema dc = xmp.getDublinCoreSchema();
			try {
				return dc != null ? dc.getDescription() : null;
			} catch (BadFieldValueException e) {
				e.printStackTrace();
			}
		}
		case "Keywords": {
			AdobePDFSchema pdf = xmp.getAdobePDFSchema();
			return pdf != null ? pdf.getKeywords() : null;
		}
		case "Producer": {
			AdobePDFSchema pdf = xmp.getAdobePDFSchema();
			return pdf != null ? pdf.getProducer() : null;
		}
		case "Creator": {
			XMPBasicSchema basic = xmp.getXMPBasicSchema();
			return basic != null ? basic.getCreatorTool() : null;
		}
		default:
			return null;
		}
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
	private XMPMetadata setXMP(XMPMetadata xmp, MetadataToUpdate metadataToUpdate) {

		String key = metadataToUpdate.getKey();
		String value = metadataToUpdate.getValue();

		XMPBasicSchema xmpBasic = xmp.getXMPBasicSchema();

		if (PDF_XMP_DC_SETTERS.get(key) != null) {

			DublinCoreSchema dc = xmp.getDublinCoreSchema();

			if (dc == null) {
				dc = xmp.createAndAddDublinCoreSchema();
			}

			PDF_XMP_DC_SETTERS.get(key).accept(dc, value);

		} else if (PDF_XMP_ADOBE_PDF_SETTERS.get(key) != null) {

			AdobePDFSchema pdf = xmp.getAdobePDFSchema();

			if (pdf == null) {
				pdf = xmp.createAndAddAdobePDFSchema();
			}

			PDF_XMP_ADOBE_PDF_SETTERS.get(key).accept(pdf, value);

		} else if (PDF_XMP_BASIC_SETTERS.get(key) != null) {

			if (xmpBasic == null) {
				xmpBasic = xmp.createAndAddXMPBasicSchema();
			}

			PDF_XMP_BASIC_SETTERS.get(key).accept(xmpBasic, value);

		} else {
			throw new IllegalArgumentException("Unknown PDF XMP metadata key: " + key);
		}

		if (xmpBasic == null) {
			xmpBasic = xmp.createAndAddXMPBasicSchema();
		}

		xmpBasic.setModifyDate(Calendar.getInstance());

		return xmp;

	}

	/**
	 * Saves the edited PDF to a file.
	 *
	 * @param pdDoc the PDF document
	 * @return the edited PDF file
	 * @throws Exception if the file cannot be created or saved
	 */
	private File saveEditedPdf(PDDocument pdDoc) throws Exception {

		File editedFile = File.createTempFile("edited_", ".pdf");

		pdDoc.save(editedFile);

		return editedFile;
	}

	@Override
	public String getHtml() {
		resultBuffer.append(HtmlTemplate.getHtmlHead());

		resultBuffer.append("<h1>Ergebnis der PDF-Metadatenbearbeitung mit PDFBox</h1>\n");
		resultBuffer.append("<h2>zur Bearbeitung ausgewählte Datei: " + pdfEditRes.getLoadedFileName() + "</h2>\n");

		if (pdfEditRes.getFileOutputLocation() != null) {
			resultBuffer
					.append("<h3 style=\"color: darkgreen;\">Bearbeitung erfolgreich <i class=\"fa-solid fa-check\"></i></h3>\n");
		} else {
			resultBuffer
					.append("<h3 style=\"color: red;\">Bearbeitung fehlgeschlagen <i class=\"fa-solid fa-xmark\"></i></h3>\n");
		}
		if (pdfEditRes.getOldMetadataPairs() != null && pdfEditRes.getNewMetadataPairs() != null) {
			resultBuffer.append("<h3>Alte Metadaten:</h3>\n<ul>\n");
			for (Map.Entry<String, String> oldPair : pdfEditRes.getOldMetadataPairs()) {

				String label = PdfInfoModel.getInfoLabel().getOrDefault(oldPair.getKey().toLowerCase(), oldPair.getKey());

				resultBuffer.append("<li>").append(label).append(": ").append(oldPair.getValue()).append("</li>\n");
			}

			resultBuffer.append("</ul>\n");
			resultBuffer.append("<h3>Neue Metadaten:</h3>\n<ul>\n");
			for (Map.Entry<String, String> newPair : pdfEditRes.getNewMetadataPairs()) {

				String label = PdfInfoModel.getInfoLabel().getOrDefault(newPair.getKey().toLowerCase(), newPair.getKey());

				resultBuffer.append("<li>").append(label).append(": ").append(newPair.getValue()).append("</li>\n");
			}

			resultBuffer.append("</ul>\n");
		}
		if (pdfEditRes.getFileOutputLocation() != null) {
			resultBuffer.append(
					"<p class='moreMarginTop'><i class=\"fa-solid fa-download\"></i><a href=\"/lzv-api/downloadedit?fileName="
							+ pdfEditRes.getFileOutputLocation() + "&origFileName=" + pdfEditRes.getLoadedFileName()
							+ "\">PDF-Datei mit aktualisierten Metadaten herunterladen</a> (Link 120 Minuten gültig)</p>");
		}

		resultBuffer.append(
				"<p class='moreMarginTop'><i class=\"fa-regular fa-pen-to-square\"></i><a href=\"/lzv-jsp/pdfbox/editMd\">Metadaten des nächsten PDF bearbeiten\n</a></p>");

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

		JSONObject oldMetadata = new JSONObject();

		for (Map.Entry<String, String> pair : pdfEditRes.getOldMetadataPairs()) {
			oldMetadata.put(pair.getKey(), pair.getValue());
		}

		resultJson.put("oldMetadata", oldMetadata);

		JSONObject newMetadata = new JSONObject();

		for (Map.Entry<String, String> pair : pdfEditRes.getNewMetadataPairs()) {
			newMetadata.put(pair.getKey(), pair.getValue());
		}

		resultJson.put("newMetadata", newMetadata);

		if (pdfEditRes.getFileOutputLocation() != null) {
			resultJson.put("fileOutputLocation", "/lzv-api/downloadedit?fileName=" + pdfEditRes.getFileOutputLocation()
					+ "&origFileName=" + pdfEditRes.getLoadedFileName());
		}
		return resultJson.toString(3);
	}

}
