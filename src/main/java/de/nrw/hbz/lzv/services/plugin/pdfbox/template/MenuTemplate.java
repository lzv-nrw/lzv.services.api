/**
 * 
 */
package de.nrw.hbz.lzv.services.plugin.pdfbox.template;

import java.util.ArrayList;

import de.nrw.hbz.lzv.services.template.HtmlTemplate;

/**
 * 
 */
 public class MenuTemplate {

   static final String NAME = "PDFbox";

    public MenuTemplate() {
      initMenu();
    }
    
    private void initMenu() {
      HtmlTemplate.appendMenu(MenuTemplate.NAME, setMenuPlug(setMenuEntry()));     
    }
    
    /**
     * Create Plugin specific menu-entries
     */
    private  ArrayList<String> setMenuEntry() {
      ArrayList<String> menuEntry = new ArrayList<>();
      menuEntry.add("<a href=\"/lzv-jsp/pdfbox/upload\">PDF-Validierung</a>");
      menuEntry.add("<a href=\"/lzv-api/pdf/version\">pdfBox-Version</a>");
      return menuEntry;
    }
    
    private String setMenuPlug(ArrayList menuEntries) {
      StringBuffer menuPlug = new StringBuffer();
      menuPlug = new StringBuffer();
      menuPlug.append("<div class=\"dropdown\"><div class=\"menu\"><img class=\"menu\" src=\"/lzv-jsp/images/pdfbox-favicon.ico\" />"
          + "veraPDF<div class=\"submenu\"><ul>");
          for(int i=0; i< menuEntries.size(); i++) {
            menuPlug.append("<li>" + menuEntries.get(i) + "</li>");
          }
          menuPlug.append("</ul></div></div>");
          return menuPlug.toString();
    }
    
  
}
