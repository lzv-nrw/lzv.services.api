/**
 * 
 */
package de.nrw.hbz.lzv.services.plugin.veraPDF.template;

import java.util.ArrayList;
import java.util.Hashtable;

import de.nrw.hbz.lzv.services.template.HtmlTemplate;

/**
 * 
 */
public class VeraPDFTemplate {

  HtmlTemplate hTemplate = new HtmlTemplate();
  Hashtable<String, ArrayList<String>> menu = new Hashtable<>();
  ArrayList<String> menuEntry = new ArrayList<>();

  public VeraPDFTemplate() {
    initMenu();
  }
  /**
   * Create Plugin specific menu-entries
   */
  private void initMenu() {
    menuEntry.add("<a href=\"upload\">PDF-Validierung</a>");
    menuEntry.add("<a href=\"version\">veraPDF-Version</a>");
    menu.put("veraPDF", menuEntry);
    hTemplate.appendMenu(menu);
  }
  
  
}
