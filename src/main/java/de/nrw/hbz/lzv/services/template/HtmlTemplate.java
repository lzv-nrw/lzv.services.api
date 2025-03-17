/**
 * 
 */
package de.nrw.hbz.lzv.services.template;

/**
 * 
 */
public class HtmlTemplate {

  /**
   * @return a HTML-Document Head
   */
  public static String getHtmlHead() {
    StringBuffer headSb = new StringBuffer();
    headSb.append("<html>\n" + "<head>\n" + "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n"
        + "<link rel=\"stylesheet\" href=\"css\" />\n" + "<title>hbz lzv services</title>\n" + "</head>\n<body>\n"
        + "<div class=\"main\">");

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

}
