/**
 * 
 */
package de.nrw.hbz.lzv.services.template;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

import de.nrw.hbz.lzv.services.plugin.verapdf.template.VeraPDFTemplate;

/**
 * 
 */
public class HtmlTemplate {

  private static Hashtable<String, ArrayList<String>> menu = new Hashtable<>();
  private static VeraPDFTemplate vpt = new VeraPDFTemplate();
  
  /**
   * @return a HTML-Document Head
   */
  public static String getHtmlHead() {
    StringBuffer headSb = new StringBuffer();
    headSb.append("<html>\n" + "<head>\n" + "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n"
        + "<link rel=\"stylesheet\" href=\"css\" />\n" + "<title>hbz lzv services</title>\n" + "</head>\n<body>\n");
    headSb.append("<div class=\"head\">");
    
    headSb.append(getMenuEntry("veraPDF"));
    
    headSb.append("</div></div><div class=\"main\"><hr/>");

    return headSb.toString();
  }

  /**
   * @return a HTML-Document Foot
   */
  public static String getHtmlFoot() {
    StringBuffer footSb = new StringBuffer();
    footSb.append("</div>\n<div class=\"footer\">");
    footSb.append("<hr/><a class=\"fanker\" href=\"about\">Über</a><a class=\"fanker\" href=\"version\">veraPDF-Version</a>");
    footSb.append("</body>\n</html>");

    return footSb.toString();
  }

  public void appendMenu(Hashtable<String,ArrayList<String>> pluginMenu) {
    menu.putAll(pluginMenu);
  }
  
  private static String getMenuEntry(String entry) {
    StringBuffer menuSb = new StringBuffer();
    Enumeration<String> mEnum = menu.keys();
    while(mEnum.hasMoreElements()) {
      String key = mEnum.nextElement();
      if(key.equals(entry)){
         menuSb.append("<div class=\"dropdown\"><div class=\"menu\"><img class=\"menu\" src=\"https://docs.verapdf.org/favicon-32x32.png\" />"
        + "veraPDF<div class=\"submenu\"><ul>");
        ArrayList<String> subMenu = menu.get(key);
        for(int i=0; i< subMenu.size(); i++) {
          menuSb.append("<li>" + subMenu.get(i) + "</li>");
        }
        menuSb.append("</ul></div></div>");
      }
    }
   return menuSb.toString(); 
  }
}
