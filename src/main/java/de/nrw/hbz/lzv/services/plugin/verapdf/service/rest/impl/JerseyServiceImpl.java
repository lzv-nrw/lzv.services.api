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

import de.nrw.hbz.lzv.services.model.json.impl.PdfModelImpl;
import de.nrw.hbz.lzv.services.plugin.verapdf.service.impl.ServiceImpl;
import de.nrw.hbz.lzv.services.template.HtmlTemplate;
import de.nrw.hbz.lzv.services.util.file.FileUtil;

/**
 * Implementation of Restful Endpoints
 */
// @ApplicationPath("verapdf/")
@Path("/verapdf")
public class JerseyServiceImpl {

  // Initiate Logger for JerseyServiceImpl
  private static Logger logger = LogManager.getLogger(JerseyServiceImpl.class);

  public JerseyServiceImpl() {
    logger.info("Jersey Service startet");
  }

  @POST
  @Path("/validate")
  @Consumes({ MediaType.MULTIPART_FORM_DATA })
  @Produces({ MediaType.TEXT_HTML })
  public String validatePdfA(@FormDataParam("file") InputStream fileInputStream,
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

  @GET
  @Path("/version")
  @Produces({ MediaType.TEXT_HTML })
  public String getVersionHTML() {

    StringBuffer sbVersion = new StringBuffer();
    sbVersion.append(HtmlTemplate.getHtmlHead());
    //sbVersion.append("<h1>PDFA-Validierung mit veraPDF</h1>");
    sbVersion.append("<ul>");
    sbVersion.append("<li>Version der verwendeten veraPDF-Libraries: " + 
    ServiceImpl.getVersion() + "</li>");
    //sbVersion.append("<li>Verzeichnis-Pfad der Applikation: " + System.getProperty("user.dir") + "</li>");
    //sbVersion.append("<li>Derzeitiger Pfad: " + new File("").getAbsolutePath() + "</li>");
    sbVersion.append("</ul>");
    sbVersion.append(HtmlTemplate.getHtmlFoot());

    String version = sbVersion.toString();
    return version;
  }

  @GET
  @Path("/version")
  @Produces({ MediaType.APPLICATION_JSON })
  public String getVersionJson() {
    StringBuffer sbVersion = new StringBuffer();
    sbVersion.append("[{\"plugin\" : \"PDFA-Validation with veraPDF\",");
    sbVersion.append("\"serviceInfo\" : {");
    sbVersion.append("\"veraPDF Version\" : " + "\"" + 
    ServiceImpl.getVersion() + "\"");
    sbVersion.append("}}]");

    String version = sbVersion.toString();
    return version;
  }

  @POST
  @Path("/pversion")
  @Consumes({ MediaType.MULTIPART_FORM_DATA })
  @Produces({ MediaType.TEXT_HTML })
  public String pversion(@FormDataParam("file") InputStream fileInputStream,
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
  @Path("/pversion")
  @Consumes({ MediaType.MULTIPART_FORM_DATA })
  @Produces({ MediaType.APPLICATION_JSON })
  public String pversionJson(@FormDataParam("file") InputStream fileInputStream,
      @FormDataParam("file") FormDataContentDisposition contentDisposition) {

    String fileName = "unknown";
    if(contentDisposition != null) {
      fileName = contentDisposition.getFileName();
    }
        
    File file = FileUtil.saveTempFile(fileInputStream, "pdfbox.pdf");
    
    de.nrw.hbz.lzv.services.plugin.pdfbox.service.impl.ServiceImpl serviceImpl = 
        new de.nrw.hbz.lzv.services.plugin.pdfbox.service.impl.ServiceImpl();
    
    String result = serviceImpl.getPdfMD(file, fileName).toString();
    return result;

  }

  @POST
  @Path("/format")
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
  @Path("/editMD")
  @Consumes({ MediaType.MULTIPART_FORM_DATA })
  @Produces({ MediaType.TEXT_HTML })
  public File editMD(@FormDataParam("file") InputStream fileInputStream,
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
  @Path("/getFileUrl")
  @Consumes({ MediaType.MULTIPART_FORM_DATA })
  @Produces({ MediaType.TEXT_HTML })
  public String getFileUrl() {
    // TODO: implement missing code
    
    // logger.info(new File(fileName).getAbsolutePath());
  
  return null;
  }  

}
