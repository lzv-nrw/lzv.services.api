/**
 * TimePrefix.java - This file is part of the DiPP Project by hbz
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
package de.nrw.hbz.lzv.services.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.logging.log4j.LogManager;
  import org.apache.logging.log4j.Logger;

/**
 * Class TimePrefix
 * 
 * <p><em>Title: Time Stamp for directories or Files </em></p>
 * <p>Description: Class returns the actual time stamp which can be used for 
 * creation of unique directory and file names</p>
 * 
 * @author aquast, email
 * creation date: 29.07.2013
 *
 */
public class TimePrefix {

	// Initiate Logger for TimePrefix
	private static Logger log = LogManager.getLogger(TimePrefix.class);

	/**
	 * <p><em>Title: </em></p>
	 * <p>Description: simple Method that returns a time stamp used to 
	 * create unique identifiers</p>
	 * 
	 * @return 
	 */
	public static String getTimePrefix(){
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd'T'kkmmssSSS'Z'");
		Calendar cal = Calendar.getInstance();
		log.debug(dateFormat.format(cal.getTime()));
		return dateFormat.format(cal.getTime());
	}
		
	 public static String setFormat(Calendar calendar) {
	    SimpleDateFormat dateFormat = new SimpleDateFormat("EEE' 'dd'. 'MMM' 'yyyy' 'kk':'mm':'ss' 'z");
	    return dateFormat.format(calendar.getTime());
	 }	    

}
