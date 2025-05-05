/**
 * 
 */
package de.nrw.hbz.lzv.services.model.json.model;

import de.nrw.hbz.lzv.services.util.TimePrefix;

/**
 * 
 */
public class PdfCreationDate {
  
  private String creationDate = null;
  
  /**
   * @return the creationDate
   */
  public String getCreationDate() {
    return creationDate;
  }

  /**
   * @param creationDate the creationDate to set
   */
  public void setCreationDate(String creationDate) {
    this.creationDate = creationDate;
  }

  /**
   * set current creationDate
   */
  public void setCreationDate() {
    this.creationDate = TimePrefix.getTimePrefix();
  }

  
}
