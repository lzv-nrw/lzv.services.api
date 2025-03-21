/**
 * 
 */
package de.nrw.hbz.lzv.services.plugin.verapdf.template;

import java.util.ArrayList;

import de.nrw.hbz.lzv.services.model.MenuTemplate;
import de.nrw.hbz.lzv.services.template.HtmlTemplate;

/**
 * 
 */
public class VeraMenuTemplate extends MenuTemplate{

  static final String NAME = "veraPDF";

  public VeraMenuTemplate() {
    initMenu();
  }
  
  @Override
  public void initMenu() {
    HtmlTemplate.appendMenu(VeraMenuTemplate.NAME, setMenuPlug(setMenuEntry()));    
  }
  
  /**
   * Create Plugin specific menu-entries
   */
  private  ArrayList<String> setMenuEntry() {
    ArrayList<String> menuEntry = new ArrayList<>();
    menuEntry.add("<a href=\"/lzv-jsp/verapdf/upload\">PDF-Validierung</a>");
    menuEntry.add("<a href=\"/lzv-api/verapdf/version\">veraPDF-Version</a>");
    return menuEntry;
  }
  
  private String setMenuPlug(ArrayList<String> menuEntries) {
    StringBuffer menuPlug = new StringBuffer();
    menuPlug = new StringBuffer();
    menuPlug.append("<div class=\"dropdown\"><div class=\"menu\"><img class=\"menu\" src=\"/lzv-jsp/images/verapdf-favicon-32x32.png\" />"
        + "veraPDF<div class=\"submenu\"><ul>");
        for(int i=0; i< menuEntries.size(); i++) {
          menuPlug.append("<li>" + menuEntries.get(i) + "</li>");
        }
        menuPlug.append("</ul></div></div></div>");
        return menuPlug.toString();
  }
  
  
}
