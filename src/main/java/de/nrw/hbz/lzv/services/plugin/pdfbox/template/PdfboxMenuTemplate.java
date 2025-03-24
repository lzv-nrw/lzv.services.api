/**
 * 
 */
package de.nrw.hbz.lzv.services.plugin.pdfbox.template;

import java.util.ArrayList;

import de.nrw.hbz.lzv.services.model.MenuTemplate;
import de.nrw.hbz.lzv.services.template.HtmlTemplate;

/**
 * 
 */
 public class PdfboxMenuTemplate extends MenuTemplate {

   static final String NAME = "PDFbox";

    public PdfboxMenuTemplate() {
      initMenu();
    }
    
    @Override
    public void initMenu() {
      HtmlTemplate.appendMenu(PdfboxMenuTemplate.NAME, setMenuPlug(setMenuEntry()));     
    }
    
    /**
     * Create Plugin specific menu-entries
     */
    private  ArrayList<String> setMenuEntry() {
      ArrayList<String> menuEntry = new ArrayList<>();
      menuEntry.add("<a href=\"/lzv-jsp/pdfbox/upload\">PDF-Versionserkennung</a>");
      return menuEntry;
    }
    
    private String setMenuPlug(ArrayList menuEntries) {
      StringBuffer menuPlug = new StringBuffer();
      menuPlug = new StringBuffer();
      menuPlug.append("<div class=\"dropdown\"><div class=\"menu\"><img class=\"menu\" src=\"/lzv-jsp/images/pdfbox-favicon.ico\" />"
          + "<p>PDFbox</p><div class=\"submenu\"><ul>");
          for(int i=0; i< menuEntries.size(); i++) {
            menuPlug.append("<li>" + menuEntries.get(i) + "</li>");
          }
          menuPlug.append("</ul></div></div></div>");
          return menuPlug.toString();
    }
    
  
}
