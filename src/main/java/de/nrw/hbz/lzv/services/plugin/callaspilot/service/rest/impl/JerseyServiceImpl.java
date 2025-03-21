/**
 * JerseyServiceImpl.java - This file is part of the DiPP Project by hbz
 * Library Service Center North Rhine Westfalia, Cologne 
 *
 * -----------------------------------------------------------------------------
 *
 * <p><b>License and Copyright: </b>The contents of this file are subject to the
 * D-FSL License Version 1.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License
 * at <a href="http://www.dipp.nrw.de/dfsl/">http://www.dipp.nrw.de/dfsl/.</a></p>
 *
 * <p>Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific language governing rights and limitations under the License.</p>
 *
 * <p>Portions created for the Fedora Repository System are Copyright &copy; 2002-2005
 * by The Rector and Visitors of the University of Virginia and Cornell
 * University. All rights reserved."</p>
 *
 * -----------------------------------------------------------------------------
 *
 */
package de.nrw.hbz.lzv.services.plugin.callaspilot.service.rest.impl;

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
