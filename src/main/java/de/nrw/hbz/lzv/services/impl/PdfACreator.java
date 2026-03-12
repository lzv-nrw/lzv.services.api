/**
 * 
 */
package de.nrw.hbz.lzv.services.impl;

import java.io.File;
import java.util.Hashtable;

import de.nrw.hbz.lzv.services.model.json.impl.PdfACompliance;
import de.nrw.hbz.lzv.services.model.json.impl.PdfInfo;
import de.nrw.hbz.lzv.services.model.pdfa.result.PdfaPilotResult;

/**
 * 
 */
public abstract class PdfACreator {
  private static Hashtable<String, PdfACreator> subClasses = new Hashtable<>(); 

  public PdfInfo pdfInfo = null;
  public PdfACompliance pdfACompl = null;

  public StringBuffer resultBuffer = new StringBuffer();
  public String fileName = null;

  public static PdfACreator getInstance(String name) {
    init();
    //String path = "de.nrw.hbz.lzv.services.plugin." + name + ".service.impl.VersionInfo";
    PdfACreator pdfACreator = subClasses.get(name);
    //TODO: implement a reflection based version of getInstance 
    
    return pdfACreator;
  }

  private static void init() {
    subClasses.put("pdfapilot", new de.nrw.hbz.lzv.services.plugin.pdfapilot.service.impl.PdfACreator());

  }

  public abstract PdfaPilotResult createPdfa(File file, String fileName, String flavour);

  public abstract String getHtml();

  public abstract String getJson();

}
