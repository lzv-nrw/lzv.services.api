/**
 * 
 */
package de.nrw.hbz.lzv.services.plugin.verapdf.service.rest.impl;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

import java.io.File;
import java.io.InputStream;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import de.nrw.hbz.lzv.services.impl.Analyzer;
import de.nrw.hbz.lzv.services.impl.VersionInfo;
import de.nrw.hbz.lzv.services.model.json.impl.PdfModelImpl;
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
  private String[] fileNames = new String[10];

  public JerseyServiceImpl() {
    logger.info("Jersey Service startet");
  }

  @GET
  @Path("version/verapdf")
  @Produces({ MediaType.TEXT_HTML })
  public String getVeraPdfVersionHTML() {

    StringBuffer sbVersion = new StringBuffer();
    sbVersion.append(HtmlTemplate.getHtmlHead());
    //sbVersion.append("<h1>PDFA-Validierung mit veraPDF</h1>");
    sbVersion.append("<ul>");
    sbVersion.append("<li>Version der verwendeten veraPDF-Libraries: "); 
    sbVersion.append(VersionInfo.getInstance("verapdf").getVersionString()  + "</li>");
    //ServiceImpl.getVersion() + "</li>");
    //sbVersion.append("<li>Verzeichnis-Pfad der Applikation: " + System.getProperty("user.dir") + "</li>");
    //sbVersion.append("<li>Derzeitiger Pfad: " + new File("").getAbsolutePath() + "</li>");
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
    sbVersion.append(VersionInfo.getInstance("verapdf").getVersionString()  + "\"");
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
    sbVersion.append(VersionInfo.getInstance("verapdf").getVersionString()  + "\"");
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
    //sbVersion.append("<h1>PDFA-Validierung mit veraPDF</h1>");
    sbVersion.append("<ul>");
    sbVersion.append("<li>Version der verwendeten pdfaPilot-Libraries: "); 
    sbVersion.append(VersionInfo.getInstance("pdfapilot").getVersionString()  + "</li>");
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
    sbVersion.append(VersionInfo.getInstance("pdfbox").getVersionString()  + "\"");
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
    //sbVersion.append("<h1>PDFA-Validierung mit veraPDF</h1>");
    sbVersion.append("<ul>");
    sbVersion.append("<li>Version der verwendeten Apache PDFbox-Libraries: "); 
    sbVersion.append(VersionInfo.getInstance("pdfbox").getVersionString()  + "</li>");
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
    if(contentDisposition != null) {
      fileName = contentDisposition.getFileName();
    }
    
    String result = "<p class=\"error\">Datei konnte nicht verarbeitet werden</p>";
    ServiceImpl sImpl = new ServiceImpl();
    result = sImpl.validatePDF(fileInputStream);

    StringBuffer htmlResult = new StringBuffer(HtmlTemplate.getHtmlHead());
    htmlResult.append("<h1>Ergebnis der Prüfung</h1>\n<p>Dateiname: " + fileName +"</p>\n" + result);
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
    if(contentDisposition != null) {
      fileName = contentDisposition.getFileName();
    }
        
    File file = FileUtil.saveTempFile(fileInputStream, "pdfbox.pdf");
    
    de.nrw.hbz.lzv.services.plugin.pdfbox.service.impl.ServiceImpl serviceImpl = new de.nrw.hbz.lzv.services.plugin.pdfbox.service.impl.ServiceImpl();
    Map<String,Object> resultMap = serviceImpl.getPdfMD(file, fileName);
    
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
    if(contentDisposition != null) {
      fileName = contentDisposition.getFileName();
    }
        
    File file = FileUtil.saveTempFile(fileInputStream, "pdfbox.pdf");
    
    de.nrw.hbz.lzv.services.plugin.pdfbox.service.impl.ServiceImpl serviceImpl = 
        new de.nrw.hbz.lzv.services.plugin.pdfbox.service.impl.ServiceImpl();
    
    Map<String,Object> resultMap = serviceImpl.getPdfMD(file, fileName);
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
    if(contentDisposition != null) {
      fileName = contentDisposition.getFileName();
    }
        
    File file = FileUtil.saveTempFile(fileInputStream, "pdfapilot.pdf");
    
    Analyzer pdfaPilotAnalyzer = Analyzer.getInstance("pdfapilot");
    Map<String,Object> resultMap = pdfaPilotAnalyzer.analyze(file, fileName);
    
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
    if(contentDisposition != null) {
      fileName = contentDisposition.getFileName();
    }
        
    File file = FileUtil.saveTempFile(fileInputStream, "pdfapilot.pdf");
    
    Analyzer pdfaPilotAnalyzer = Analyzer.getInstance("pdfapilot");
    Map<String,Object> resultMap = pdfaPilotAnalyzer.analyze(file, fileName);
    
    StringBuffer pilotSb = new StringBuffer();
    // pilotSb.append("{'pdfaPilot validation result :");
    pilotSb.append(new PdfModelImpl(resultMap).toString());
    return pilotSb.toString();

  }

  @POST
  @Path("format")
  @Consumes({ MediaType.MULTIPART_FORM_DATA })
  @Produces({ MediaType.TEXT_HTML })
  public String format(@FormDataParam("file") InputStream fileInputStream,
      @FormDataParam("file") FormDataContentDisposition contentDisposition) {
    
    String fileName = null;
    if(contentDisposition != null) {
      fileName = contentDisposition.getFileName();
    }
    
    de.nrw.hbz.lzv.services.plugin.pdfbox.service.impl.ServiceImpl serviceImpl = new de.nrw.hbz.lzv.services.plugin.pdfbox.service.impl.ServiceImpl();
    String result = serviceImpl.getFileFormat(fileInputStream);
    
    StringBuffer htmlResult = new StringBuffer(HtmlTemplate.getHtmlHead());
    htmlResult.append("<h1>Ergebnis der Prüfung</h1>\n<p>Dateiname: " + fileName +"</p>\n" + result);

    htmlResult.append("<p><a href=\"/lzv-jsp/pdfbox/upload\">Weitere PDF-Version ermitteln</a></p>");
    
    htmlResult.append(HtmlTemplate.getHtmlFoot());
    return htmlResult.toString();

  }
  
  @POST
  @Path("editMD/pdfbox")
  @Consumes({ MediaType.MULTIPART_FORM_DATA })
  @Produces({ MediaType.TEXT_HTML })

  public File editMDPdfBox(@FormDataParam("file") InputStream fileInputStream,
      @FormDataParam("file") FormDataContentDisposition contentDisposition,
      @QueryParam("field") String key, @QueryParam("value") String value) {
    
    String fileName = "-";
    if(contentDisposition != null) {
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
  @Path("getFileUrl")
  @Consumes({ MediaType.MULTIPART_FORM_DATA })
  @Produces({ MediaType.TEXT_HTML })
  public String getFileUrl() {
    // TODO: implement missing code
    
    // logger.info(new File(fileName).getAbsolutePath());
  
  return null;
  }  

}
