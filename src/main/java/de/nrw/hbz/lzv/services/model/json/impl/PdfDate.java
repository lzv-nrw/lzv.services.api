/**
 * 
 */
package de.nrw.hbz.lzv.services.model.json.impl;

import de.nrw.hbz.lzv.services.util.TimePrefix;

/**
 * 
 */
public class PdfDate {
  
  private static String date = null;

  public PdfDate() {
    setDate();
  }
  /**
   * @return the modificationDate
   */
  public static String getDate() {
    return date;
  }

  /**
   * @param modificationDate the modificationDate to set
   */
  public static void setDate(String Date) {
    date = Date;
  }

  /**
   * set current modificationDate
   */
  public static void setDate() {
    date = TimePrefix.getTimePrefix();
  }

  
}
