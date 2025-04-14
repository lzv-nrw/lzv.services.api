/**
 * 
 */
package de.nrw.hbz.lzv.services.impl;

import java.io.File;
import java.util.Hashtable;
import java.util.Map;

import de.nrw.hbz.lzv.services.model.pdfa.result.PdfaPilotResult;

/**
 * 
 */
public abstract class PdfACreator {
  private static Hashtable<String, PdfACreator> subClasses = new Hashtable<>(); 
  
  public static PdfACreator getInstance(String name) {
    init();
    //String path = "de.nrw.hbz.lzv.services.plugin." + name + ".service.impl.VersionInfo";
    PdfACreator analyzer = subClasses.get(name);
    //TODO: implement a reflection based version of getInstance 
    
    return analyzer;
  }

  private static void init() {
    subClasses.put("pdfapilot", new de.nrw.hbz.lzv.services.plugin.pdfapilot.service.impl.PdfACreator());

  }
  
  public abstract PdfaPilotResult createPdfa(File file, String fileName, String flavour);
  
}
