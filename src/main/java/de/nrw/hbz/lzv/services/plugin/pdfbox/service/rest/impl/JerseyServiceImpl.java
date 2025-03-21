package de.nrw.hbz.lzv.services.plugin.pdfbox.service.rest.impl;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import de.nrw.hbz.lzv.services.file.util.FileUtil;
import de.nrw.hbz.lzv.services.plugin.callaspilot.service.impl.PilotRunner;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;


/**
 * Class JerseyServiceImpl
 * 
 * <p><em>Title: </em></p>
 * <p>Description: </p>
 * 
 * @author aquast, email
 * creation date: 26.07.2013
 *
 */
@Path("callas/")
public class JerseyServiceImpl {

	// Initiate Logger for JerseyServiceImpl
	private static Logger log = LogManager.getLogger(JerseyServiceImpl.class);

	
  @POST
  @Path("validate")
  @Consumes({ MediaType.MULTIPART_FORM_DATA })
  @Produces({ MediaType.TEXT_HTML })
  public String validatePdfA(@FormDataParam("file") InputStream fileInputStream,
      @FormDataParam("FileMD") FormDataContentDisposition ContentDisposition) {

    File file = FileUtil.saveTmpFile(fileInputStream, "callas");
		String paramString = null;
		//create a unique temporary file prefix
		String fileName = file.getAbsolutePath();
		
		PilotRunner pRunner = new PilotRunner();
		pRunner.executePdfATool(paramString, file.getAbsolutePath());
		
		return "<html> " + "<title>" + "Access to converted file" + "</title>"
		        + "<body><h1>" + "Converted file:" + "</h1>" +
		        	"<ul>" +
		        	"<li>Result: " + fileName + "</li>" +
		        	"<li>From :" + "</li>" +
		        	"</ul></body>" + "</html> ";

	}
			  
}
