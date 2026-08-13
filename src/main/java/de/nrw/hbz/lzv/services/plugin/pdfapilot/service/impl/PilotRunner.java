package de.nrw.hbz.lzv.services.plugin.pdfapilot.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.nrw.hbz.lzv.services.model.pdfa.result.PdfaPilotResult;
import de.nrw.hbz.lzv.services.plugin.pdfapilot.model.pilot.ParameterLoader;

/**
 * Class PilotRunner
 * 
 * <p>
 * <em>Title: </em>
 * </p>
 * <p>
 * Description:
 * </p>
 * 
 * @author aquast, email creation date: 29.07.2013
 *
 */
public class PilotRunner {

  // Initiate Logger for PilotRunner
  private static Logger log = LogManager.getLogger(PilotRunner.class);

  private String exitStateStr = null;
  private String stoutStr = null;
  private String errStr = null;

  /**
   * Method creates a Process running the pdfaPilot command with all parameters
   * given as ArrayList. Executes the shell command
   * 
   * @param cmdParams
   * 
   */
  public void executePdfATool(ArrayList<String> cmdParams) {

    StringBuffer lineBuffer = new StringBuffer("run for test only");
    StringBuffer errLineBuffer = new StringBuffer("run for test run");
    int exitState = -1;
    
    Iterator<String> cmdIt = cmdParams.iterator();
        
    while (cmdIt.hasNext()) {
      String value = cmdIt.next();
      // cmdString.append(value);
      log.info("Command Parameter: " + value);
    }
        
        
    try {
      ProcessBuilder procBuilder = new ProcessBuilder();
      procBuilder.command(cmdParams);
      procBuilder.directory(null);
      Process proc = procBuilder.start();
      exitState = proc.waitFor();
      InputStream stout = proc.getInputStream();
      InputStream err = proc.getErrorStream();

      InputStreamReader isr = new InputStreamReader(stout);
      BufferedReader br = new BufferedReader(isr);
      String line = null;

      lineBuffer = new StringBuffer();
      while ((line = br.readLine()) != null) {
        lineBuffer.append(line + "\n");
      }

      InputStreamReader eIR = new InputStreamReader(stout);
      BufferedReader eBR = new BufferedReader(eIR);
      String errLine = null;
      errLineBuffer = new StringBuffer();
      while ((errLine = eBR.readLine()) != null) {
        errLineBuffer.append(errLine + "\n");
      }

      log.info("STOUT: " + lineBuffer.toString());
      log.info("Exit State: " + exitState);
      stoutStr = lineBuffer.toString();
      errStr = errLineBuffer.toString();
      exitStateStr = Integer.toString(exitState);

    } catch (Exception Exc) {
      log.error(Exc);
    }

  }

  /**
   * @return Error messages as string
   */
  public String getErrStr() {
    return errStr;
  }

  /**
   * @return Standard Output messages as string
   */
  public String getStoutStr() {
    return stoutStr;
  }

  /**
   * @return numeric Exit State value as string
   */
  public String getExitStateStr() {
    return exitStateStr;
  }

}
