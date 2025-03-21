/**
 * 
 */
package de.nrw.hbz.lzv.services.template;

import java.util.Enumeration;
import java.util.Hashtable;

import de.nrw.hbz.lzv.services.model.MenuTemplate;

/**
 * 
 */
public class HtmlTemplate {

  private static Hashtable<String, String> menu = new Hashtable<>();
  // private static Hashtable<String, MenuTemplate> templates = new Hashtable<>();
  // private static MenuTemplate vera = MenuTemplate.initTemplate(MenuTemplate.VERAPDF);
    
  /**
   * @return a HTML-Document Head
   */
  public static String getHtmlHead() {
    MenuTemplate.initTemplate(MenuTemplate.VERAPDF);
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

  /**
   * Method has to be implemented from MenuTemplate of each Plugin 
   * in order to provide the appropriate Menu Entry for the Plugin 
   * @param menuKey Menu Name  
   * @param menuCode html code returned from Plugin MenuTemplate 
   */
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
