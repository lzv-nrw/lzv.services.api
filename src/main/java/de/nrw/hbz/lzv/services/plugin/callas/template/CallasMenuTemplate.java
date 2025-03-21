/**
 * 
 */
package de.nrw.hbz.lzv.services.plugin.callas.template;

import java.util.ArrayList;

import de.nrw.hbz.lzv.services.model.MenuTemplate;
import de.nrw.hbz.lzv.services.plugin.verapdf.template.VeraMenuTemplate;
import de.nrw.hbz.lzv.services.template.HtmlTemplate;

/**
 * 
 */
public class CallasMenuTemplate extends MenuTemplate {

  static final String NAME = "pdfaPilot";
  
  public CallasMenuTemplate() {
    initMenu();
  }
  
  @Override
  public void initMenu() {
    HtmlTemplate.appendMenu(CallasMenuTemplate.NAME, setMenuPlug(setMenuEntry()));    
  }
  
  /**
   * Create Plugin specific menu-entries
   */
  private  ArrayList<String> setMenuEntry() {
    ArrayList<String> menuEntry = new ArrayList<>();
    menuEntry.add("<a href=\"/lzv-jsp/callas/upload\">PDF-Validierung</a>");
    menuEntry.add("<a href=\"/lzv-api/callas/version\">pdfaPilot-Version</a>");
    return menuEntry;
  }
  
  private String setMenuPlug(ArrayList menuEntries) {
    StringBuffer menuPlug = new StringBuffer();
    menuPlug = new StringBuffer();
    menuPlug.append("<div class=\"dropdown\"><div class=\"menu\"><img class=\"menu\" src=\"/lzv-jsp/images/callas-favicon.ico\" />"
        + "pdfaPilot<div class=\"submenu\"><ul>");
        for(int i=0; i< menuEntries.size(); i++) {
          menuPlug.append("<li>" + menuEntries.get(i) + "</li>");
        }
        menuPlug.append("</ul></div></div></div>");
        return menuPlug.toString();
  }
  
  
}
