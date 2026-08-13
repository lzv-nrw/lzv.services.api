/**
 * 
 */
package de.nrw.hbz.lzv.services.plugin.pdfapilot.service.impl;

import java.util.ArrayList;

import de.nrw.hbz.lzv.services.plugin.pdfapilot.model.pilot.ParameterLoader;

/**
 * 
 */
public class VersionInfo extends de.nrw.hbz.lzv.services.impl.VersionInfo {

  public VersionInfo() {

    ParameterLoader.loadPdfaPilotProperties();
  }
  
  public final static String PLUGIN_NAME = "pdfapilot";
  
  @Override
  public String getVersionString() {
    PilotRunner pRunner = new PilotRunner();

    ArrayList<String> cmdList = new ArrayList<String>();
    cmdList.add(ParameterLoader.getParameter("pdfapilot.version"));
    pRunner.executePdfATool(cmdList);
    
    StringBuffer runnerSb = new StringBuffer();
    runnerSb.append("StandardOut: " + pRunner.getStoutStr() + "\n");
    runnerSb.append("ExitState: " + pRunner.getExitStateStr() + "\n");
    return pRunner.getStoutStr();
  }

  @Override
  public String getPluginName() {
    // TODO Auto-generated method stub
    return PLUGIN_NAME;
  }
  
  
  

}
