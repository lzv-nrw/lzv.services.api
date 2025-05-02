/**
 * 
 */
package de.nrw.hbz.lzv.services.plugin.verapdf.service.impl;

import org.verapdf.ReleaseDetails;

/**
 * 
 */
public class VersionInfo extends de.nrw.hbz.lzv.services.impl.VersionInfo {

  public VersionInfo() {
    
  }
  
  public final static String PLUGIN_NAME = "verapdf";
  
  @Override
  public String getVersionString() {
    ReleaseDetails rDetails = ReleaseDetails.getInstance();
    return rDetails.getVersion();
  }

  @Override
  public String getPluginName() {
    return PLUGIN_NAME;
    
  }
  
  
  

}
