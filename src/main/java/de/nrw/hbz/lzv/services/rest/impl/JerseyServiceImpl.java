/**
 * 
 */
package de.nrw.hbz.lzv.services.rest.impl;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.ResponseBuilder;
import jakarta.ws.rs.core.StreamingOutput;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import de.nrw.hbz.lzv.services.impl.Analyzer;
import de.nrw.hbz.lzv.services.impl.FileController;
import de.nrw.hbz.lzv.services.impl.PdfACreator;
import de.nrw.hbz.lzv.services.impl.PdfMdEditor;
import de.nrw.hbz.lzv.services.impl.VersionInfo;
import de.nrw.hbz.lzv.services.model.pdf.edit.MetadataToUpdate;
import de.nrw.hbz.lzv.services.template.HtmlTemplate;
import de.nrw.hbz.lzv.services.util.file.FileUtil;

/**
 * Implementation of Restful endpoints for the different plugins (tools)
 * integrated
 * 
 */
@Path("/")
public class JerseyServiceImpl {

	// Initiate Logger for JerseyServiceImpl
	private static Logger logger = LogManager.getLogger(JerseyServiceImpl.class);
	private FileController fileController = new FileController();

	public JerseyServiceImpl() {
		logger.info("Jersey Service startet");
	}

	/**
	 * /** provide RestFul endpoint for listing the tools in use
	 *
	 * @return version information as HTML
	 */
	@GET
	@Path("tools")
	@Produces({ MediaType.TEXT_HTML })
	public String getToolsHTML() {

		StringBuffer sbVersion = new StringBuffer();
		sbVersion.append(HtmlTemplate.getHtmlHead());
		sbVersion.append("<ul>");
		sbVersion.append("<li><b>veraPDF:</b> Version ");
		sbVersion.append(VersionInfo.getInstance("verapdf").getVersionString() + "</li>");
		sbVersion.append("<li><b>pdfaPilot:</b> Version ");
		sbVersion.append(VersionInfo.getInstance("pdfapilot").getVersionString() + "</li>");
		sbVersion.append("<li><b>Apache PDFBox:</b> Version ");
		sbVersion.append(VersionInfo.getInstance("pdfbox").getVersionString() + "</li>");
		sbVersion.append("</ul>");
		sbVersion.append(HtmlTemplate.getHtmlFoot());

		String version = sbVersion.toString();
		return version;
	}

	/**
	 * provide RestFul endpoint for listing the tools in use
	 *
	 * @return version information as JSON
	 */
	@GET
	@Path("tools")
	@Produces({ MediaType.APPLICATION_JSON })
	public String getToolsJson() {
		String veraPdfVersion = VersionInfo.getInstance("verapdf").getVersionString();
		String pdfaPilotVersion = VersionInfo.getInstance("pdfapilot").getVersionString();
		String pdfBoxVersion = VersionInfo.getInstance("pdfbox").getVersionString();

		if (veraPdfVersion == null) {
			veraPdfVersion = "not available";
		}
		if (pdfaPilotVersion == null) {
			pdfaPilotVersion = "not available";
		}
		if (pdfBoxVersion == null) {
			pdfBoxVersion = "not available";
		}

		org.json.JSONArray tools = new org.json.JSONArray();

		tools.put(new org.json.JSONObject().put("name", "veraPDF").put("version", veraPdfVersion.trim())
				.put("website", "https://verapdf.org").put("github", "https://github.com/verapdf"));
		tools.put(new org.json.JSONObject().put("name", "pdfaPilot").put("version", pdfaPilotVersion.trim()).put("website",
				"https://callassoftware.com/de/products/pdfapilot"));
		tools.put(new org.json.JSONObject().put("name", "PDFBox").put("version", pdfBoxVersion.trim())
				.put("website", "https://pdfbox.apache.org").put("github", "https://github.com/apache/pdfbox"));

		return tools.toString();
	}

	/**
	 * provide RestFul endpoint for exposing Version of the tool in use
	 *
	 * @return version information as HTML
	 */
	@GET
	@Path("version/verapdf")
	@Produces({ MediaType.TEXT_HTML })
	public String getVeraPdfVersionHTML() {

		StringBuffer sbVersion = new StringBuffer();
		sbVersion.append(HtmlTemplate.getHtmlHead());
		// sbVersion.append("<h1>PDFA-Validierung mit veraPDF</h1>");
		sbVersion.append("<ul>");
		sbVersion.append("<li>Version der verwendeten veraPDF-Libraries: ");
		sbVersion.append(VersionInfo.getInstance("verapdf").getVersionString() + "</li>");
		// ServiceImpl.getVersion() + "</li>");
		// sbVersion.append("<li>Verzeichnis-Pfad der Applikation: " +
		// System.getProperty("user.dir") + "</li>");
		// sbVersion.append("<li>Derzeitiger Pfad: " + new File("").getAbsolutePath() +
		// "</li>");
		sbVersion.append("</ul>");
		sbVersion.append(HtmlTemplate.getHtmlFoot());

		String version = sbVersion.toString();
		return version;
	}

	/**
	 * provide RestFul endpoint for exposing Version of the tool in use
	 *
	 * @return version information as JSON
	 */
	@GET
	@Path("version/verapdf")
	@Produces({ MediaType.APPLICATION_JSON })
	public String getVeraPdfVersionJson() {
		StringBuffer sbVersion = new StringBuffer();
		sbVersion.append("[{\"plugin\" : \"PDFA-Validation with veraPDF\",");
		sbVersion.append("\"serviceInfo\" : {");
		sbVersion.append("\"veraPDF Version\" : " + "\"");
		sbVersion.append(VersionInfo.getInstance("verapdf").getVersionString() + "\"");
		// ServiceImpl.getVersion() + "\"");
		sbVersion.append("}}]");

		String version = sbVersion.toString();
		return version;
	}

	/**
	 * provide RestFul endpoint for exposing Version of the tool in use
	 *
	 * @return version information as JSON
	 */
	@GET
	@Path("version/pdfapilot")
	@Produces({ MediaType.APPLICATION_JSON })
	public String getPdfaPilotVersionJson() {
		StringBuffer sbVersion = new StringBuffer();
		sbVersion.append("[{\"plugin\" : \"PDFA-Validation with Callas pdfaPilot\",");
		sbVersion.append("\"serviceInfo\" : {");
		sbVersion.append("\"pdfaPilot Version\" : " + "\"");
		sbVersion.append(VersionInfo.getInstance("pdfapilot").getVersionString().replace("\n", "") + "\"");
		sbVersion.append("}}]");

		String version = sbVersion.toString();
		return version;
	}

	/**
	 * provide RestFul endpoint for exposing Version of the tool in use
	 *
	 * @return version information as HTML
	 */
	@GET
	@Path("version/pdfapilot")
	@Produces({ MediaType.TEXT_HTML })
	public String getPdfaPilotVersionHTML() {

		StringBuffer sbVersion = new StringBuffer();
		sbVersion.append(HtmlTemplate.getHtmlHead());
		// sbVersion.append("<h1>PDFA-Validierung mit veraPDF</h1>");
		sbVersion.append("<ul>");
		sbVersion.append("<li>Version der verwendeten pdfaPilot-Libraries: ");
		sbVersion.append(VersionInfo.getInstance("pdfapilot").getVersionString() + "</li>");
		sbVersion.append("</ul>");
		sbVersion.append(HtmlTemplate.getHtmlFoot());

		String version = sbVersion.toString();
		return version;
	}

	/**
	 * provide RestFul endpoint for exposing Version of the tool in use
	 *
	 * @return version information as JSON
	 */
	@GET
	@Path("version/pdfbox")
	@Produces({ MediaType.APPLICATION_JSON })
	public String getPdfBoxVersionJson() {
		StringBuffer sbVersion = new StringBuffer();
		sbVersion.append("[{\"plugin\" : \"PDFA-Validation with Apache PDFbox\",");
		sbVersion.append("\"serviceInfo\" : {");
		sbVersion.append("\"Apache PDFbox Version\" : " + "\"");
		sbVersion.append(VersionInfo.getInstance("pdfbox").getVersionString() + "\"");
		sbVersion.append("}}]");

		String version = sbVersion.toString();
		return version;
	}

	/**
	 * provide RestFul endpoint for exposing Version of the tool in use
	 *
	 * @return version information as HTML
	 */
	@GET
	@Path("version/pdfbox")
	@Produces({ MediaType.TEXT_HTML })
	public String getPdfBoxVersionHTML() {

		StringBuffer sbVersion = new StringBuffer();
		sbVersion.append(HtmlTemplate.getHtmlHead());
		// sbVersion.append("<h1>PDFA-Validierung mit veraPDF</h1>");
		sbVersion.append("<ul>");
		sbVersion.append("<li>Version der verwendeten Apache PDFbox-Libraries: ");
		sbVersion.append(VersionInfo.getInstance("pdfbox").getVersionString() + "</li>");
		sbVersion.append("</ul>");
		sbVersion.append(HtmlTemplate.getHtmlFoot());

		String version = sbVersion.toString();
		return version;
	}

	/**
	 * provide RestFul endpoint for PDF analyzing with pdfBOX
	 *
	 * @param fileInputStream
	 * @param contentDisposition
	 * @return validation result as HTML
	 */
	@POST
	@Path("validate/verapdf")
	@Consumes({ MediaType.MULTIPART_FORM_DATA })
	@Produces({ MediaType.TEXT_HTML })
	public String validateVeraPdfHtml(@FormDataParam("file") InputStream fileInputStream,
			@FormDataParam("file") FormDataContentDisposition contentDisposition) {

		String fileName = "-";
		if (contentDisposition != null) {
			fileName = contentDisposition.getFileName();
		}

		File file = FileUtil.saveTempFile(fileInputStream, "verapdf.pdf");

		Analyzer veraPdfAnalyzer = Analyzer.getInstance("verapdf");
		veraPdfAnalyzer.analyze(file, fileName);

		file.delete();
		return veraPdfAnalyzer.getHtml();
	}

	/**
	 * provide RestFul endpoint for PDF analyzing with veraPDF
	 *
	 * @param fileInputStream
	 * @param contentDisposition
	 * @return validation result as json
	 */
	@POST
	@Path("validate/verapdf")
	@Consumes({ MediaType.MULTIPART_FORM_DATA })
	@Produces({ MediaType.APPLICATION_JSON })
	public String validateVeraPdfJson(@FormDataParam("file") InputStream fileInputStream,
			@FormDataParam("file") FormDataContentDisposition contentDisposition) {

		String fileName = "-";
		if (contentDisposition != null) {
			fileName = contentDisposition.getFileName();
		}

		File file = FileUtil.saveTempFile(fileInputStream, "verapdf.pdf");

		Analyzer veraPdfAnalyzer = Analyzer.getInstance("verapdf");
		veraPdfAnalyzer.analyze(file, fileName);

		file.delete();
		return veraPdfAnalyzer.getJson();

	}

	/**
	 * provide RestFul endpoint for PDF analyzing with pdfBOX
	 *
	 * @param fileInputStream
	 * @param contentDisposition
	 * @return validation result as HTML
	 */
	@POST
	@Path("validate/pdfbox")
	@Consumes({ MediaType.MULTIPART_FORM_DATA })
	@Produces({ MediaType.TEXT_HTML })
	public String validatePdfBoxHtml(@FormDataParam("file") InputStream fileInputStream,
			@FormDataParam("file") FormDataContentDisposition contentDisposition) {

		String fileName = "unknown";
		if (contentDisposition != null) {
			fileName = contentDisposition.getFileName();
		}

		File file = FileUtil.saveTempFile(fileInputStream, "pdfbox.pdf");

		Analyzer pdfBoxAnalyzer = Analyzer.getInstance("pdfbox");
		pdfBoxAnalyzer.analyze(file, fileName);

		file.delete();
		return pdfBoxAnalyzer.getHtml();

	}

	/**
	 * provide RestFul endpoint for PDF analyzing with pdfBOX
	 *
	 * @param fileInputStream
	 * @param contentDisposition
	 * @return validation result as json
	 */
	@POST
	@Path("validate/pdfbox")
	@Consumes({ MediaType.MULTIPART_FORM_DATA })
	@Produces({ MediaType.APPLICATION_JSON })
	public String validatePdfBoxJson(@FormDataParam("file") InputStream fileInputStream,
			@FormDataParam("file") FormDataContentDisposition contentDisposition) {

		String fileName = "unknown";
		if (contentDisposition != null) {
			fileName = contentDisposition.getFileName();
		}
		File file = FileUtil.saveTempFile(fileInputStream, "pdfbox.pdf");

		Analyzer pdfBoxAnalyzer = Analyzer.getInstance("pdfbox");
		pdfBoxAnalyzer.analyze(file, fileName);

		file.delete();
		return pdfBoxAnalyzer.getJson();

	}

	/**
	 * provide RestFul endpoint for PDF analyzing with pdfaPilot
	 *
	 * @param fileInputStream
	 * @param contentDisposition
	 * @return validation result as HTML
	 */
	@POST
	@Path("validate/pdfapilot")
	@Consumes({ MediaType.MULTIPART_FORM_DATA })
	@Produces({ MediaType.TEXT_HTML })
	public String validatePdfaPilotHtml(@FormDataParam("file") InputStream fileInputStream,
			@FormDataParam("file") FormDataContentDisposition contentDisposition) {

		String fileName = "unknown";
		if (contentDisposition != null) {
			fileName = contentDisposition.getFileName();
		}

		File file = FileUtil.saveTempFile(fileInputStream, "pdfapilot.pdf");

		Analyzer pdfaPilotAnalyzer = Analyzer.getInstance("pdfapilot");
		pdfaPilotAnalyzer.analyze(file, fileName);
		file.delete();
		return pdfaPilotAnalyzer.getHtml();
	}

	/**
	 * provide RestFul endpoint for PDF analyzing with pdfaPilot
	 *
	 * @param fileInputStream
	 * @param contentDisposition
	 * @return validation result as json
	 */
	@POST
	@Path("validate/pdfapilot")
	@Consumes({ MediaType.MULTIPART_FORM_DATA })
	@Produces({ MediaType.APPLICATION_JSON })
	public String validatePdfaPilotJson(@FormDataParam("file") InputStream fileInputStream,
			@FormDataParam("file") FormDataContentDisposition contentDisposition) {

		String fileName = "unknown";
		if (contentDisposition != null) {
			fileName = contentDisposition.getFileName();
		}

		File file = FileUtil.saveTempFile(fileInputStream, "pdfapilot.pdf");

		Analyzer pdfaPilotAnalyzer = Analyzer.getInstance("pdfapilot");
		pdfaPilotAnalyzer.analyze(file, fileName);

		StringBuffer pilotSb = new StringBuffer();
		// pilotSb.append("{'pdfaPilot validation result :");
		pilotSb.append(pdfaPilotAnalyzer.getJson());
		file.delete();
		return pilotSb.toString();

	}

	/**
	 * provide RestFul endpoint for editing PdfInfo with pdfaBOX
	 *
	 * @param fileInputStream
	 * @param contentDisposition
	 * @param key
	 * @param value
	 * @return pdf with edited metadata result as HTML
	 */
	@POST
	@Path("editmd/pdfbox")
	@Consumes({ MediaType.MULTIPART_FORM_DATA })
	@Produces({ MediaType.TEXT_HTML })
	public String editMDPdfBoxHtml(@FormDataParam("file") InputStream fileInputStream,
			@FormDataParam("file") FormDataContentDisposition contentDisposition, @FormDataParam("title_new") String title,
			@FormDataParam("author_new") String author, @FormDataParam("subject_new") String subject,
			@FormDataParam("keywords_new") String keywords, @FormDataParam("creator_new") String creator,
			@FormDataParam("producer_new") String producer) {

		String fileName = "-";
		if (contentDisposition != null) {
			fileName = contentDisposition.getFileName();
		}
		File file = FileUtil.saveTempFile(fileInputStream, "pdfbox.pdf");
		List<MetadataToUpdate> updates = new ArrayList<>();

		addUpdate(updates, "Title", title);
		addUpdate(updates, "Author", author);
		addUpdate(updates, "Subject", subject);
		addUpdate(updates, "Keywords", keywords);
		addUpdate(updates, "Creator", creator);
		addUpdate(updates, "Producer", producer);

		PdfMdEditor pdfBoxEditor = PdfMdEditor.getInstance("pdfbox");
		pdfBoxEditor.editPdfMd(file, fileName, updates);

		logger.info(new File(fileName).getAbsolutePath());
		file.delete();
		return pdfBoxEditor.getHtml();
	}

	private void addUpdate(List<MetadataToUpdate> updates, String key, String value) {

		if (value != null && !value.isBlank()) {
			updates.add(new MetadataToUpdate(key, value.trim()));
		}
	}

	/**
	 * provide RestFul endpoint for editing PdfInfo with pdfaBOX
	 *
	 * @param fileInputStream
	 * @param contentDisposition
	 * @param key
	 * @param value
	 * @return pdf with edited metadata result as json
	 */
	@POST
	@Path("editmd/pdfbox")
	@Consumes({ MediaType.MULTIPART_FORM_DATA })
	@Produces({ MediaType.APPLICATION_JSON })
	public String editMDPdfBoxJson(@FormDataParam("file") InputStream fileInputStream,
			@FormDataParam("file") FormDataContentDisposition contentDisposition, @FormDataParam("title_new") String title,
			@FormDataParam("author_new") String author, @FormDataParam("subject_new") String subject,
			@FormDataParam("keywords_new") String keywords, @FormDataParam("creator_new") String creator,
			@FormDataParam("producer_new") String producer) {

		String fileName = "-";
		if (contentDisposition != null) {
			fileName = contentDisposition.getFileName();
		}
		File file = FileUtil.saveTempFile(fileInputStream, "pdfbox.pdf");

		List<MetadataToUpdate> updates = new ArrayList<>();

		addUpdate(updates, "Title", title);
		addUpdate(updates, "Author", author);
		addUpdate(updates, "Subject", subject);
		addUpdate(updates, "Keywords", keywords);
		addUpdate(updates, "Creator", creator);
		addUpdate(updates, "Producer", producer);

		PdfMdEditor pdfBoxEditor = PdfMdEditor.getInstance("pdfbox");
		pdfBoxEditor.editPdfMd(file, fileName, updates);

		logger.info(new File(fileName).getAbsolutePath());

		file.delete();
		return pdfBoxEditor.getJson();
	}

	/**
	 * provide RestFul endpoint for creating PDF/A with pdfaPilot
	 *
	 * @param fileInputStream
	 * @param contentDisposition
	 * @param flavour
	 * @return
	 */
	@POST
	@Path("convert/pdfapilot")
	@Consumes({ MediaType.MULTIPART_FORM_DATA })
	@Produces({ MediaType.TEXT_HTML })
	public String createPdfAHtml(@FormDataParam("file") InputStream fileInputStream,
			@FormDataParam("file") FormDataContentDisposition contentDisposition, @FormParam("flavour") String flavour) {

		String fileName = "unknown";
		if (contentDisposition != null) {
			fileName = contentDisposition.getFileName();

		}

		File file = FileUtil.saveTempFile(fileInputStream, "pdfapilot.pdf");
		PdfACreator pdfaPilotCreator = PdfACreator.getInstance("pdfapilot");
		pdfaPilotCreator.createPdfa(file, fileName, flavour);
		file.delete();
		return pdfaPilotCreator.getHtml();
	}

	/**
	 * provide RestFul endpoint for creating PDF/A with pdfaPilot
	 *
	 * @param fileInputStream
	 * @param contentDisposition
	 * @param flavour
	 * @return
	 */
	@POST
	@Path("convert/pdfapilot")
	@Consumes({ MediaType.MULTIPART_FORM_DATA })
	@Produces({ MediaType.APPLICATION_JSON })
	public String createPdfAJson(@FormDataParam("file") InputStream fileInputStream,
			@FormDataParam("file") FormDataContentDisposition contentDisposition, @FormParam("flavour") String flavour) {

		String fileName = "unknown";
		if (contentDisposition != null) {
			fileName = contentDisposition.getFileName();

		}

		File file = FileUtil.saveTempFile(fileInputStream, "pdfapilot.pdf");

		PdfACreator pdfaPilotCreator = PdfACreator.getInstance("pdfapilot");
		pdfaPilotCreator.createPdfa(file, fileName, flavour);
		file.delete();
		return pdfaPilotCreator.getJson();
	}

	/**
	 * provide RestFul endpoint for downloading PDF/A
	 *
	 * @param fileInputStream
	 * @param contentDisposition
	 * @param flavour
	 * @return
	 */
	@GET
	@Path("downloadpdfa")
	@Consumes({ MediaType.MULTIPART_FORM_DATA })
	@Produces("application/pdf")
	public Response downloadPdfa(@QueryParam("fileName") String fileName,
			@QueryParam("origFileName") String origFileName) {

		File file = new File(fileName);
		InputStream iS = FileUtil.loadFile(file);

		if (origFileName == null) {
			origFileName = file.getName();
		}

		FileUtil fileUtil = new FileUtil();
		StreamingOutput fileStream = fileUtil.getStreamingOutput(iS);

		logger.info("StreamingOutput: " + iS.toString());

		ResponseBuilder response = Response.ok(fileStream, MediaType.APPLICATION_OCTET_STREAM);
		response.header("Content-Disposition", "attachment; filename=" + origFileName.replace(".pdf", "_pdfa.pdf"));

		return response.build();
	}

	@GET
	@Path("downloadedit")
	@Consumes({ MediaType.MULTIPART_FORM_DATA })
	@Produces("application/pdf")
	public Response downloadEditedPdf(@QueryParam("fileName") String fileName,
			@QueryParam("origFileName") String origFileName) {

		File file = new File(fileName);
		InputStream iS = FileUtil.loadFile(file);

		if (origFileName == null) {
			origFileName = file.getName();
		}

		FileUtil fileUtil = new FileUtil();
		StreamingOutput fileStream = fileUtil.getStreamingOutput(iS);

		logger.info("StreamingOutput: " + iS.toString());

		ResponseBuilder response = Response.ok(fileStream, MediaType.APPLICATION_OCTET_STREAM);
		response.header("Content-Disposition", "attachment; filename=" + origFileName.replace(".pdf", "_edited.pdf"));

		return response.build();
	}

	@POST
	@Path("loadfile")
	@Consumes({ MediaType.MULTIPART_FORM_DATA })
	@Produces({ MediaType.TEXT_HTML })
	public void uploadFile(@FormDataParam("file") InputStream fileInputStream,
			@FormDataParam("file") FormDataContentDisposition contentDisposition, @FormParam("field") String key,
			@FormParam("value") String value) {
		// TODO: implement missing code
		// logger.info(new File(fileName).getAbsolutePath());

		String fileName = "-";
		if (contentDisposition != null) {
			fileName = contentDisposition.getFileName();
		}

		File loadedFile = FileUtil.saveTempFile(fileInputStream, fileName);
		fileController.setLoadedFile(loadedFile);
	}

	@POST
	@Path("getFileUrl")
	@Consumes({ MediaType.MULTIPART_FORM_DATA })
	@Produces({ MediaType.TEXT_HTML })
	public String getFileUrl() {
		// TODO: implement missing code

		// logger.info(new File(fileName).getAbsolutePath());

		return null;
	}

}
