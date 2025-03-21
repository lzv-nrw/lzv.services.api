/**
 * 
 */
package de.nrw.hbz.lzv.services.model;

import java.util.Hashtable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.nrw.hbz.lzv.services.plugin.callas.template.CallasMenuTemplate;
import de.nrw.hbz.lzv.services.plugin.pdfbox.template.PdfboxMenuTemplate;
import de.nrw.hbz.lzv.services.plugin.verapdf.template.VeraMenuTemplate;

/**
 * 
 */
public abstract class MenuTemplate {

  final static Logger logger = LogManager.getLogger(MenuTemplate.class);

  public static final String VERAPDF = "veraPDF";
  public static final String PDFBOX = "PDFbox";
  public static final String PDFAPILOT = "pdfaPilot";
  
  private static VeraMenuTemplate vera = new VeraMenuTemplate();
  private static PdfboxMenuTemplate pdfbox = new PdfboxMenuTemplate();
  private static CallasMenuTemplate callas = new CallasMenuTemplate();
  
  
  private static Hashtable<String, MenuTemplate> templates = new Hashtable<>(); 
  
  public abstract void initMenu();
  
  public static MenuTemplate initTemplate(String templateName) {
    
    return getTemplates().get(templateName);
  }
  
  public static Hashtable<String, MenuTemplate> getTemplates() {
    templates.put(VERAPDF, vera);
    templates.put(PDFBOX, pdfbox);
    templates.put(PDFAPILOT, callas);
    
    return templates;
    
  }
}
