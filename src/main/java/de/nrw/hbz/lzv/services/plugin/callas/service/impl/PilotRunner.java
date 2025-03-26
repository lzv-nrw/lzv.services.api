package de.nrw.hbz.lzv.services.plugin.callas.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Class PilotRunner
 * 
 * <p><em>Title: </em></p>
 * <p>Description: </p>
 * 
 * @author aquast, email
 * creation date: 29.07.2013
 *
 */
public class PilotRunner {

	// Initiate Logger for PilotRunner
	private static Logger log = LogManager.getLogger(PilotRunner.class);
	
	private String exitStateStr= null; 	
	private String stoutStr = null;
	
	/**
	 * <p><em>Title: </em></p>
	 * <p>Description: Method creates the command line string with all parameters given. 
	 * Then executes the shell command </p>
	 * 
	 * @param paramString
	 * @param fileName 
	 */
	public void executePdfATool(String paramString, String fileName){
		// call to execute PDFA-Tool
		
		// Complete execute String 
		String programPath = new String("/opt/pdfapilot/callas_pdfaPilot_CLI_4_x64/pdfaPilot"); 
		String defaultParams = new String("--noprogress --nohits --substitute  " 
				 + "--linkpath=https://pdfa.hbz-nrw.de/pdfa/reporttemplate "
				 + "--fontfolder=/opt/pdfapilot/fontfolder --cachefolder=temp"
				);
		String executeString = new String(programPath + " " 
				+ defaultParams 
				+ paramString 
				+ " --outputfile=" + Configuration.getResultDirPath() + fileName 
				+ " " + Configuration.getTempDirPath() + fileName); 

		log.debug("The execute String: " + executeString);
		try{
			//Process proc = java.lang.Runtime.getRuntime().exec("echo " + executeString);
			Process proc = java.lang.Runtime.getRuntime().exec(executeString);
			int exitState = proc.waitFor();
			InputStream stout = proc.getInputStream();
            InputStreamReader isr = new InputStreamReader(stout);
            BufferedReader br = new BufferedReader(isr);
            String line = null;
            StringBuffer lineBuffer = new StringBuffer();
            while ((line = br.readLine()) != null){
                lineBuffer.append(line + "\n");
            }
            log.debug("STOUT: " + lineBuffer.toString());
            log.info("Exit State: " + exitState);
            stoutStr = lineBuffer.toString();
            exitStateStr = Integer.toString(exitState);
		}catch(Exception Exc){
			log.error(Exc);
		}
		
		//unlink temp file
		if(new File(Configuration.getTempDirPath() + fileName).isFile()){
			new File(Configuration.getTempDirPath() + fileName).delete();
		}
		// TODO: das Ausführen des PDFA Tools kann etwas dauern... was mache
		// ich um festzustellen, dass das Tool seine Arbeit beendet hat? 		
	}

	public String getStoutStr(){
		return stoutStr;
	}

	public String getExitStateStr(){
		return exitStateStr;
	}
}
