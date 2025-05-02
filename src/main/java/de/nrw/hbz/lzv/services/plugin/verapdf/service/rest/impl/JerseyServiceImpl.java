/**
 * 
 */
package de.nrw.hbz.lzv.services.plugin.verapdf.service.rest.impl;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.ResponseBuilder;
import jakarta.ws.rs.core.StreamingOutput;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.io.BufferedWriter;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import de.nrw.hbz.lzv.services.impl.Analyzer;
import de.nrw.hbz.lzv.services.impl.FileController;
import de.nrw.hbz.lzv.services.impl.PdfACreator;
import de.nrw.hbz.lzv.services.impl.VersionInfo;
import de.nrw.hbz.lzv.services.model.json.impl.PdfModelImpl;
import de.nrw.hbz.lzv.services.model.pdfa.result.PdfaPilotResult;
import de.nrw.hbz.lzv.services.plugin.verapdf.service.impl.ServiceImpl;
import de.nrw.hbz.lzv.services.template.HtmlTemplate;
import de.nrw.hbz.lzv.services.util.file.FileUtil;

/**
 * Implementation of Restful Endpoints
 */
@Path("/")
public class JerseyServiceImpl {

  // Initiate Logger for JerseyServiceImpl
  private static Logger logger = LogManager.getLogger(JerseyServiceImpl.class);
  private FileController fileController = new FileController();

  public JerseyServiceImpl() {
    logger.info("Jersey Service startet");
  }

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

  @GET
  @Path("version/pdfapilot")
  @Produces({ MediaType.APPLICATION_JSON })
  public String getPdfaPilotVersionJson() {
    StringBuffer sbVersion = new StringBuffer();
    sbVersion.append("[{\"plugin\" : \"PDFA-Validation with Callas pdfaPilot\",");
    sbVersion.append("\"serviceInfo\" : {");
    sbVersion.append("\"pdfaPilot Version\" : " + "\"");
    sbVersion.append(VersionInfo.getInstance("verapdf").getVersionString() + "\"");
    sbVersion.append("}}]");

    String version = sbVersion.toString();
    return version;
  }

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

  @GET
  @Path("version/pdfbox")
  @Produces({ MediaType.APPLICATION_JSON })
  public String getPdfBoxVersionJson() {
    StringBuffer sbVersion = new StringBuffer();
    sbVersion.append("[{\"plugin\" : \"PDFA-Validation with Apache PDFbox\",");
    sbVersion.append("\"serviceInfo\" : {");
    sbVersion.append("\"pdfaPilot Version\" : " + "\"");
    sbVersion.append(VersionInfo.getInstance("pdfbox").getVersionString() + "\"");
    sbVersion.append("}}]");

    String version = sbVersion.toString();
    return version;
  }

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

    String result = "<p class=\"error\">Datei konnte nicht verarbeitet werden</p>";
    ServiceImpl sImpl = new ServiceImpl();
    result = sImpl.validatePDF(fileInputStream);

    StringBuffer htmlResult = new StringBuffer(HtmlTemplate.getHtmlHead());
    htmlResult.append("<h1>Ergebnis der Prüfung</h1>\n<p>Dateiname: " + fileName + "</p>\n" + result);
    htmlResult.append("<p><a href=\"/lzv-jsp/verapdf/upload\">Weitere PDF-Datei prüfen</a>");
    htmlResult.append(HtmlTemplate.getHtmlFoot());

    return htmlResult.toString();

  }

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

    de.nrw.hbz.lzv.services.plugin.pdfbox.service.impl.ServiceImpl serviceImpl = new de.nrw.hbz.lzv.services.plugin.pdfbox.service.impl.ServiceImpl();
    Map<String, Object> resultMap = serviceImpl.getPdfMD(file, fileName);

    StringBuffer htmlResult = new StringBuffer(HtmlTemplate.getHtmlHead());
    htmlResult.append("<h1>Ergebnis der Prüfung</h1>\n");
    htmlResult.append(new PdfModelImpl(resultMap).toHtml());
    htmlResult.append("<p><a href=\"/lzv-jsp/pdfbox/upload\">Weitere PDF-Version ermitteln</a>");

    htmlResult.append(HtmlTemplate.getHtmlFoot());
    return htmlResult.toString();

  }

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

    de.nrw.hbz.lzv.services.plugin.pdfbox.service.impl.ServiceImpl serviceImpl = new de.nrw.hbz.lzv.services.plugin.pdfbox.service.impl.ServiceImpl();

    Map<String, Object> resultMap = serviceImpl.getPdfMD(file, fileName);
    String result = new PdfModelImpl(resultMap).toString();
    return result;

  }

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
    Map<String, Object> resultMap = pdfaPilotAnalyzer.analyze(file, fileName);

    StringBuffer htmlResult = new StringBuffer(HtmlTemplate.getHtmlHead());
    htmlResult.append("<h1>Ergebnis der Prüfung</h1>\n");
    htmlResult.append(new PdfModelImpl(resultMap).toHtml());
    htmlResult.append("<p><a href=\"/lzv-jsp/pdfapilot/upload\">Weitere PDF-Validierung</a>");
    // htmlResult.append(fileName);

    htmlResult.append(HtmlTemplate.getHtmlFoot());
    return htmlResult.toString();

  }

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
    Map<String, Object> resultMap = pdfaPilotAnalyzer.analyze(file, fileName);

    StringBuffer pilotSb = new StringBuffer();
    // pilotSb.append("{'pdfaPilot validation result :");
    pilotSb.append(new PdfModelImpl(resultMap).toString());
    return pilotSb.toString();

  }

  @POST
  @Path("format/pdfbox")
  @Consumes({ MediaType.MULTIPART_FORM_DATA })
  @Produces({ MediaType.TEXT_HTML })
  public String format(@FormDataParam("file") InputStream fileInputStream,
      @FormDataParam("file") FormDataContentDisposition contentDisposition) {

    String fileName = null;
    if (contentDisposition != null) {
      fileName = contentDisposition.getFileName();
    }

    de.nrw.hbz.lzv.services.plugin.pdfbox.service.impl.ServiceImpl serviceImpl = new de.nrw.hbz.lzv.services.plugin.pdfbox.service.impl.ServiceImpl();
    String result = serviceImpl.getFileFormat(fileInputStream);

    StringBuffer htmlResult = new StringBuffer(HtmlTemplate.getHtmlHead());
    htmlResult.append("<h1>Ergebnis der Prüfung</h1>\n<p>Dateiname: " + fileName + "</p>\n" + result);

    htmlResult.append("<p><a href=\"/lzv-jsp/pdfbox/upload\">Weitere PDF-Version ermitteln</a></p>");

    htmlResult.append(HtmlTemplate.getHtmlFoot());
    return htmlResult.toString();

  }

  @POST
  @Path("editmd/pdfbox")
  @Consumes({ MediaType.MULTIPART_FORM_DATA })
  @Produces({ MediaType.TEXT_HTML })
  public File editMDPdfBox(@FormDataParam("file") InputStream fileInputStream,
      @FormDataParam("file") FormDataContentDisposition contentDisposition, @FormParam("field") String key,
      @FormParam("value") String value) {

    String fileName = "-";
    if (contentDisposition != null) {
      fileName = contentDisposition.getFileName();
    }
    File origFile = FileUtil.saveTempFile(fileInputStream, fileName);

    de.nrw.hbz.lzv.services.plugin.pdfbox.service.impl.ServiceImpl serviceImpl = new de.nrw.hbz.lzv.services.plugin.pdfbox.service.impl.ServiceImpl();
    File editedFile = serviceImpl.editPDFInfo(origFile, key, value);

    FileUtil.copyFile(editedFile, fileName);
    File resultFile = new File(fileName);
    logger.info(new File(fileName).getAbsolutePath());

    return resultFile;
  }

  @POST
  @Path("convert/pdfapilot")
  @Consumes({ MediaType.MULTIPART_FORM_DATA })
  @Produces({ MediaType.TEXT_HTML })
  public String createPdfA(@FormDataParam("file") InputStream fileInputStream,
      @FormDataParam("file") FormDataContentDisposition contentDisposition, @FormParam("flavour") String flavour) {

    String fileName = "unknown";
    if (contentDisposition != null) {
      fileName = contentDisposition.getFileName();

    }

    File file = FileUtil.saveTempFile(fileInputStream, "pdfapilot.pdf");

    PdfACreator pdfaPilotCreator = PdfACreator.getInstance("pdfapilot");
    PdfaPilotResult result = pdfaPilotCreator.createPdfa(file, fileName, flavour);

    StringBuffer htmlResult = new StringBuffer(HtmlTemplate.getHtmlHead());

    htmlResult.append("<h3>Ergebnis der PDF/A-Erzeugung</h3>\n" + "<p>Operations:</p>\n" + "<ul>\n");

    for (int i = 0; i < result.getFixList().size(); i++) {
      htmlResult.append("<li>" + result.getFixList().get(i) + "</li>");
    }

    htmlResult.append("</ul>\n");

    htmlResult.append("<p>Summary:</p>\n" + "<ul>\n");

    for (int i = 0; i < result.getSummaryList().size(); i++) {
      htmlResult.append("<li>" + result.getSummaryList().get(i) + "</li>");
    }

    htmlResult.append("</ul>\n");

    if (result.getErrorList() != null && result.getErrorList().size() > 0) {
      htmlResult.append("<p>Error:</p>\n" + "<ul>\n");

      for (int i = 0; i < result.getErrorList().size(); i++) {
        htmlResult.append("<li>" + result.getErrorList().get(i) + "</li>");
      }

      htmlResult.append("</ul>\n");
      htmlResult.append("<p>" + result.getExecuteString() + "</p>");
    }

    // htmlResult.append(result.getStout().replace("\n", "<br/>"));

    if (result.getFileOutputLocation() != null) {
      htmlResult.append("<p class='error'><a href=\"/lzv-api/download?fileName=" + result.getFileOutputLocation() + "&origFileName="
          + fileName + "\">PDF/A Datei herunterladen</a></p>");
    }

    htmlResult.append("<p><a href=\"/lzv-jsp/pdfapilot/createpdfa\">Weiteres PDF umwandeln</a></p>");

    htmlResult.append(HtmlTemplate.getHtmlFoot());

    return htmlResult.toString();
  }

  @GET
  @Path("download")
  @Consumes({ MediaType.MULTIPART_FORM_DATA })
  @Produces("application/pdf")
  public Response downloadPdf(@QueryParam("fileName") String fileName,
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
    response.header("Content-Disposition", "attachment; filename=" + origFileName.replace("pdf", "pdfa.pdf"));

    file.delete();
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
