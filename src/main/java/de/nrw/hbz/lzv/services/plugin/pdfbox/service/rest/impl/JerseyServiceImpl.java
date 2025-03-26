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

import de.nrw.hbz.lzv.services.plugin.callas.service.impl.PilotRunner;
import de.nrw.hbz.lzv.services.plugin.pdfbox.service.impl.ServiceImpl;
import de.nrw.hbz.lzv.services.util.file.FileUtil;
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
@Path("/pdfbox")
public class JerseyServiceImpl {

	// Initiate Logger for JerseyServiceImpl
	private static Logger logger = LogManager.getLogger(JerseyServiceImpl.class);

	
  @POST
  @Path("/pversion")
  @Consumes({ MediaType.MULTIPART_FORM_DATA })
  @Produces({ MediaType.TEXT_HTML })
  public String pversion(@FormDataParam("file") InputStream fileInputStream,
      @FormDataParam("FileMD") FormDataContentDisposition ContentDisposition) {

    File file = FileUtil.saveTempFile(fileInputStream, "pdfbox");
    
    ServiceImpl serviceImpl = new ServiceImpl();
    String pdfVersion = serviceImpl.getPdfVersion(file);
    
    logger.debug("Found version: " + pdfVersion + " for file " + ContentDisposition.getFileName());
    return "<h1>Die getestete PDF-Datei " + ContentDisposition.getFileName() + 
        " hat die Version: " + pdfVersion + "</h1>";

	}
			  
}
