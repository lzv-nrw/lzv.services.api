/**
 * 
 */
package de.nrw.hbz.lzv.services.model.json.model;

/**
 * 
 */
public class PdfTitle {
  
  private String title = null;
  private static String name = "title";
  
  
  public static String getName() {
    return name;
  }
  
  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }
  
  
}
