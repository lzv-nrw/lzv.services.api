/**
 * 
 */
package de.nrw.hbz.lzv.services.model.json.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import de.nrw.hbz.lzv.services.model.json.model.PdfInfoModel;

import org.json.JSONArray;

/**
 * 
 */
public class PdfACreation {

  private static final Logger log = LogManager.getLogger(PdfACreation.class);
  private JSONObject pdfACreationNode = null;
  private String flavour = "undefined";
  private JSONArray fixingMessage = new JSONArray();
  private JSONArray summaryMessage = new JSONArray();
  
  public PdfACreation() {
    pdfACreationNode = new JSONObject();
  }
  
  public JSONObject getPdfACCreation() {
    
    pdfACreationNode.put("fixings", fixingMessage);
    pdfACreationNode.put("summary", summaryMessage);
    return pdfACreationNode;    
  }

  public void setSummary(ArrayList<String> summaryMap) {

    summaryMessage = new JSONArray();
    summaryMap.forEach((value) -> summaryMessage.put(value));
    
  }
  
  public void setFixings(ArrayList<String> fixingList) {
    
    fixingMessage = new JSONArray();
    fixingList.forEach((value) -> fixingMessage.put(value));
  }
  
  public void setDownloadLink(String downloadUrl) {
    if(downloadUrl != null && !downloadUrl.isEmpty()) {
      pdfACreationNode.put("downloadUrl", downloadUrl);      
    }
    else {
      log.info("Download URL is not provided due to error in Pdf A generation");
      pdfACreationNode.put("info", "Generation of PDF A, with version  " + flavour + " failed");
    }
  }
  
  
  /**
   * @return PdfModel as html fragment
   */
  public String toHtml() {
    String listStart = "<p>Output provided by Callas pdfa-pilot</p><ul>\n";
    String listEnd = "</ul>\n";
    StringBuffer htmlBuffer = new StringBuffer();
    
    htmlBuffer.append(listStart);

    
    for (int i = 0; i < summaryMessage.length(); i++) {
      htmlBuffer.append("<li>" + summaryMessage.get(i) + "</li>\n");
    }
    
    for (int i = 0; i < fixingMessage.length(); i++) {
      htmlBuffer.append("<li>" + fixingMessage.get(i) + "</li>\n");
    }
    
    htmlBuffer.append(listEnd);
    return htmlBuffer.toString();
  }
  
  public String toJson() {
    return pdfACreationNode.toString(3);
  }
  
  public JSONObject getJSONObject() {
    return pdfACreationNode;
  }
  
 


}
