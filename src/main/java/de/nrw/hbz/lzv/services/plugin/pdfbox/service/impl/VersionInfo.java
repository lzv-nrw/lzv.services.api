/**
 * 
 */
package de.nrw.hbz.lzv.services.plugin.pdfbox.service.impl;

/**
 * 
 */
public class VersionInfo extends de.nrw.hbz.lzv.services.impl.VersionInfo {

  public VersionInfo() {
    
  }
  
  public final static String PLUGIN_NAME = "pdfbox";
  
  @Override
  public String getVersionString() {

    return "Apache PDFbox Version 3.0.4";
  }

  @Override
  public String getPluginName() {
    // TODO Auto-generated method stub
    return PLUGIN_NAME;
  }
  
  
  

}
