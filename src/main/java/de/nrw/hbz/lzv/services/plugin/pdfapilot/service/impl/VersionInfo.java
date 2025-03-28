/**
 * 
 */
package de.nrw.hbz.lzv.services.plugin.pdfapilot.service.impl;

/**
 * 
 */
public class VersionInfo extends de.nrw.hbz.lzv.services.impl.VersionInfo {

  public VersionInfo() {
    
  }
  
  public final static String PLUGIN_NAME = "pdfapilot";
  
  @Override
  public String getVersionString() {
    PilotRunner pRunner = new PilotRunner();
    
    pRunner.executePdfATool(" --version");
    
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
