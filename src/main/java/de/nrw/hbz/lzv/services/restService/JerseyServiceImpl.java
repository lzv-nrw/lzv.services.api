/**
 * 
 */
package de.nrw.hbz.lzv.services.restService;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.io.File;
import java.io.InputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import de.nrw.hbz.lzv.services.fileUtil.FileUtil;
import de.nrw.hbz.lzv.services.plugin.veraImpl.ServiceImpl;

/**
 * Implementation of Restful Endpoints
 */
@Path("/")
public class JerseyServiceImpl {

  // Initiate Logger for JerseyServiceImpl
  private static Logger log = LogManager.getLogger(JerseyServiceImpl.class);

  public JerseyServiceImpl() {
    log.info("Jersey Service startet");
  }

  @POST
  @Path("validate")
  @Consumes({ MediaType.MULTIPART_FORM_DATA })
  @Produces({ MediaType.TEXT_HTML })
  public String validatePdfA(@FormDataParam("file") InputStream fileInputStream,
      @FormDataParam("FileMD") FormDataContentDisposition ContentDisposition) {

    String result = "<p class=\"error\">Datei konnte nicht verarbeitet werden</p>";
    ServiceImpl sImpl = new ServiceImpl();
    result = sImpl.validatePDF(fileInputStream);

    StringBuffer htmlResult = new StringBuffer(getHtmlHead());
    htmlResult.append("<h1>Ergebnis der Prüfung</h1>" + result);
    htmlResult.append("<p><a href=\"upload\">Weitere PDF-Datei prüfen</a>");
    htmlResult.append(getHtmlFoot());

    return htmlResult.toString();

  }

  @GET
  @Path("version")
  @Produces({ MediaType.TEXT_HTML })
  public String getVersionHTML() {

    StringBuffer sbVersion = new StringBuffer();
    sbVersion.append(getHtmlHead());
    sbVersion.append("<h1>PDFA-Validierung mit veraPDF</h1>");
    sbVersion.append("<ul>");
    sbVersion.append("<li>Version der verwendeten veraPDF-Libraries: " + ServiceImpl.getVersion() + "</li>");
    sbVersion.append("<li>Verzeichnis-Pfad der Applikation: " + System.getProperty("user.dir") + "</li>");
    sbVersion.append("<li>Derzeitiger Pfad: " + new File("").getAbsolutePath() + "</li>");
    sbVersion.append("</ul>");
    sbVersion.append(getHtmlFoot());

    String version = sbVersion.toString();
    return version;
  }

  @GET
  @Path("version")
  @Produces({ MediaType.APPLICATION_JSON })
  public String getVersionJson() {
    StringBuffer sbVersion = new StringBuffer();
    sbVersion.append("[{\"plugin\" : \"PDFA-Validation with veraPDF\",");
    sbVersion.append("\"serviceInfo\" : {");
    sbVersion.append("\"veraPDF Version\" : " + "\"" + ServiceImpl.getVersion() + "\"");
    sbVersion.append("}}]");

    String version = sbVersion.toString();
    return version;
  }

  /**
   * @return a HTML-Document Head
   */
  public static String getHtmlHead() {
    StringBuffer headSb = new StringBuffer();
    headSb.append("<html>\n" + "<head>\n" + "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n"
        + "<link rel=\"stylesheet\" href=\"css\" />\n" + "<title>hbz lzv services</title>\n" + "</head>\n<body>\n"
        + "<div class=\"main\">");

    return headSb.toString();
  }

  /**
   * @return a HTML-Document Foot
   */
  public static String getHtmlFoot() {
    StringBuffer footSb = new StringBuffer();
    footSb.append("</div>\n<div class=\"footer\">");
    footSb.append("<hr/><a class=\"fanker\" href=\"about\">Über</a><a class=\"fanker\" href=\"version\">veraPDF-Version</a>");
    footSb.append("</body>\n</html>");

    return footSb.toString();
  }

}
