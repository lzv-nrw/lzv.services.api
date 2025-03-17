/**
 * 
 */
package de.nrw.hbz.lzv.services.plugin.veraPDF.restServiceImpl;

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

import de.nrw.hbz.lzv.services.plugin.veraPDF.serviceImpl.ServiceImpl;
import de.nrw.hbz.lzv.services.template.HtmlTemplate;

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

    StringBuffer htmlResult = new StringBuffer(HtmlTemplate.getHtmlHead());
    htmlResult.append("<h1>Ergebnis der Prüfung</h1>" + result);
    htmlResult.append("<p><a href=\"upload\">Weitere PDF-Datei prüfen</a>");
    htmlResult.append(HtmlTemplate.getHtmlFoot());

    return htmlResult.toString();

  }

  @GET
  @Path("version")
  @Produces({ MediaType.TEXT_HTML })
  public String getVersionHTML() {

    StringBuffer sbVersion = new StringBuffer();
    sbVersion.append(HtmlTemplate.getHtmlHead());
    sbVersion.append("<h1>PDFA-Validierung mit veraPDF</h1>");
    sbVersion.append("<ul>");
    sbVersion.append("<li>Version der verwendeten veraPDF-Libraries: " + ServiceImpl.getVersion() + "</li>");
    sbVersion.append("<li>Verzeichnis-Pfad der Applikation: " + System.getProperty("user.dir") + "</li>");
    sbVersion.append("<li>Derzeitiger Pfad: " + new File("").getAbsolutePath() + "</li>");
    sbVersion.append("</ul>");
    sbVersion.append(HtmlTemplate.getHtmlFoot());

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

}
