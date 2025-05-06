/**
 * 
 */
package de.nrw.hbz.lzv.services.impl;

import java.io.File;
import java.util.Hashtable;
import java.util.Map;

/**
 * 
 */
public abstract class Analyzer {
  private static Hashtable<String, Analyzer> subClasses = new Hashtable<>(); 
  
  public static Analyzer getInstance(String name) {
    init();
    //String path = "de.nrw.hbz.lzv.services.plugin." + name + ".service.impl.VersionInfo";
    Analyzer analyzer = subClasses.get(name);
    //TODO: implement a reflection based version of getInstance 
    
    return analyzer;
  }

  private static void init() {
    subClasses.put("pdfapilot", new de.nrw.hbz.lzv.services.plugin.pdfapilot.service.impl.Analyzer());
    subClasses.put("verapdf", new de.nrw.hbz.lzv.services.plugin.verapdf.service.impl.Analyzer());
    subClasses.put("pdfbox", new de.nrw.hbz.lzv.services.plugin.pdfbox.service.impl.Analyzer());

  }
  
  public abstract void analyze(File file, String fileName);

  public abstract String getHtml();

  public abstract String getJson();

}
