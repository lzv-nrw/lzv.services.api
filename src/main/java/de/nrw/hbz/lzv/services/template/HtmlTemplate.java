/**
 * 
 */
package de.nrw.hbz.lzv.services.template;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

import de.nrw.hbz.lzv.services.plugin.verapdf.template.MenuTemplate;

/**
 * 
 */
public class HtmlTemplate {

  private static Hashtable<String, String> menu = new Hashtable<>();
  private static MenuTemplate vera = new MenuTemplate();
  
  /**
   * @return a HTML-Document Head
   */
  public static String getHtmlHead() {
    StringBuffer headSb = new StringBuffer();
    headSb.append("<html>\n" + "<head>\n" + "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n"
        + "<link rel=\"stylesheet\" href=\"/lzv-jsp/css/default.css\" />\n" 
        + "<title>hbz lzv services</title>\n" + "</head>\n<body>\n");
    headSb.append("<div class=\"head\">");
    
    headSb.append(getMenuEntry());
    
    headSb.append("</div></div><div class=\"main\"><hr/>");

    return headSb.toString();
  }

  /**
   * @return a HTML-Document Foot
   */
  public static String getHtmlFoot() {
    StringBuffer footSb = new StringBuffer();
    footSb.append("</div>\n<div class=\"footer\">");
    footSb.append("<hr/><a class=\"fanker\" href=\"/lzv-jsp/about\">Über</a>");
    footSb.append("</body>\n</html>");

    return footSb.toString();
  }

  public static void appendMenu(String menuKey, String menuCode) {
    menu.put(menuKey,menuCode);
  }
  
  private static String getMenuEntry() {
    StringBuffer menuSb = new StringBuffer();
    Enumeration<String> mEnum = menu.keys();
    while(mEnum.hasMoreElements()) {
      String key = mEnum.nextElement();
         menuSb.append(menu.get(key));
    }
   return menuSb.toString(); 
  }
}
