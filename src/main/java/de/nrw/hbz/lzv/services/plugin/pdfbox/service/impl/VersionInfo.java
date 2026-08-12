/**
 * 
 */
package de.nrw.hbz.lzv.services.plugin.pdfbox.service.impl;

import org.apache.pdfbox.util.Version;


/**
 * 
 */
public class VersionInfo extends de.nrw.hbz.lzv.services.impl.VersionInfo {

  public VersionInfo() {
    
  }
  
  public final static String PLUGIN_NAME = "pdfbox";
  
  @Override
  public String getVersionString() {

    return Version.getVersion();
  }

  @Override
  public String getPluginName() {
    // TODO Auto-generated method stub
    return PLUGIN_NAME;
  }
  
  
  

}
