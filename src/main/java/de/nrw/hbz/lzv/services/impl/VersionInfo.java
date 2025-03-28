package de.nrw.hbz.lzv.services.impl;

import java.util.Hashtable;

public abstract class VersionInfo {

  // private static String name = "main";
  private static String versionString = null;

  private static Hashtable<String, VersionInfo> subClasses = new Hashtable<>(); 
   
  public static VersionInfo getInstance(String name) {
    init();
    //String path = "de.nrw.hbz.lzv.services.plugin." + name + ".service.impl.VersionInfo";
    VersionInfo versInfo = subClasses.get(name);
    //TODO: implement a reflection based version of getInstance 
    
    return versInfo;
  }
  
  
  public abstract String getPluginName();

  public String getVersionString() {
    return versionString;
  };
  
  private static void init() {
    subClasses.put("pdfapilot", new de.nrw.hbz.lzv.services.plugin.pdfapilot.service.impl.VersionInfo());
    subClasses.put("verapdf", new de.nrw.hbz.lzv.services.plugin.verapdf.service.impl.VersionInfo());
    subClasses.put("pdfbox", new de.nrw.hbz.lzv.services.plugin.pdfbox.service.impl.VersionInfo());
  }
}
