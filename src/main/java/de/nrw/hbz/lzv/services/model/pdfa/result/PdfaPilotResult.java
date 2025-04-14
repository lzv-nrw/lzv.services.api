/**
 * 
 */
package de.nrw.hbz.lzv.services.model.pdfa.result;

import java.util.ArrayList;

/**
 * 
 */
public class PdfaPilotResult {

  private ArrayList<String> fixList = new ArrayList<>();
  private ArrayList<String> summaryList = new ArrayList<>();
  private String fileOutputLocation = null;
  private String loadedFileName = null;
  private String stout = null;
  
  
  
  
  /**
   * @param fixMessage the fixMessage to add
   */
  public void addFixMessage(String fixMessage) {
    this.fixList.add(fixMessage);
  }
  
  /**
   * @param fixMessage the fixMessage to add
   */
  public void addSummaryMessage(String summaryMessage) {
    this.summaryList.add(summaryMessage);
  }

  /**
   * @return the fixList
   */
  public ArrayList<String> getFixList() {
    return fixList;
  }
  /**
   * @return the loadedFileName
   */
  public String getLoadedFileName() {
    return loadedFileName;
  }

  /**
   * @return the summaryList
   */
  public ArrayList<String> getSummaryList() {
    return summaryList;
  }
  /**
   * @return the fileOutputLocation
   */
  public String getFileOutputLocation() {
    return fileOutputLocation;
  }
  /**
   * @return the stout
   */
  public String getStout() {
    return stout;
  }

  /**
   * @param stout the stout to set
   */
  public void setStout(String stout) {
    this.stout = stout;
  }

  /**
   * @param fixList the fixList to set
   */
  public void setFixList(ArrayList<String> fixList) {
    this.fixList = fixList;
  }
  /**
   * @param summaryList the summaryList to set
   */
  public void setSummaryList(ArrayList<String> summaryList) {
    this.summaryList = summaryList;
  }
  /**
   * @param fileOutputLocation the fileOutputLocation to set
   */
  public void setFileOutputLocation(String fileOutputLocation) {
    this.fileOutputLocation = fileOutputLocation;
  }
  
  /**
   * @param loadedFileName the loadedFileName to set
   */
  public void setLoadedFileName(String loadedFileName) {
    this.loadedFileName = loadedFileName;
  }
  
  
}
