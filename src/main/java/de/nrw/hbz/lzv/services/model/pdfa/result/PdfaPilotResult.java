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
  private ArrayList<String> errorList = new ArrayList<>();
  
  private String fileOutputLocation = null;
  private String loadedFileName = null;
  private String stout = null;
  private String errOut = null;
  private String executeString = null;
  
  
  
  
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
   * @param fixMessage the fixMessage to add
   */
  public void addErrorMessage(String errorMessage) {
    this.errorList.add(errorMessage);
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
   * @return the errorList
   */
  public ArrayList<String> getErrorList() {
    return errorList;
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
   * @return the errOut
   */
  public String getErrOut() {
    return errOut;
  }

  /**
   * @return the executeString
   */
  public String getExecuteString() {
    return executeString;
  }

  /**
   * @param executeString the executeString to set
   */
  public void setExecuteString(String executeString) {
    this.executeString = executeString;
  }

  /**
   * @param errOut the errOut to set
   */
  public void setErrOut(String errOut) {
    this.errOut = errOut;
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
   * @param summaryList the summaryList to set
   */
  public void setErrorList(ArrayList<String> errorList) {
    this.errorList = errorList;
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
