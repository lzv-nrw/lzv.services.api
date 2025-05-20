/**
 * 
 */
package de.nrw.hbz.lzv.services.model.json.impl;

import java.util.Iterator;
import java.util.LinkedHashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import de.nrw.hbz.lzv.services.model.json.model.PdfDocumentInformation;
import de.nrw.hbz.lzv.services.model.json.model.PdfInfoModel;

/**
 * 
 */
public class PdfInfo implements PdfDocumentInformation {

  private static Logger logger = LogManager.getLogger(PdfInfo.class);
  private JSONObject pdfInfo = new JSONObject();

  LinkedHashMap<String, Object> pdfInfoMap = new LinkedHashMap<>();

  @Override
  public void setPdfAuthor(String author) {
    pdfInfo.put(PdfInfoModel.AUTHOR, author);
  }

  @Override
  public void setPdfCreator(String creationTool) {
    pdfInfo.put(PdfInfoModel.CREATOR, creationTool);
  }

  @Override
  public void setPdfProducer(String productionTool) {
    pdfInfo.put(PdfInfoModel.PRODUCER, productionTool);
  }

  @Override
  public void setPdfCreationDate(String creationDate) {
    pdfInfo.put(PdfInfoModel.CREATION_DATE, creationDate);
    
  }

  @Override
  public void setPdfPages(String pages) {
    pdfInfo.put(PdfInfoModel.PAGES, pages);
  }

  @Override
  public void setPdfTitle(String title) {
    pdfInfo.put(PdfInfoModel.TITLE, title);
  }

  @Override
  public void setPdfModificationDate(String modificationDate) {
    pdfInfo.put(PdfInfoModel.MODIFICATION_DATE, modificationDate);
  }

  @Override
  public void setPdfKeywords(String keywords) {
    pdfInfo.put(PdfInfoModel.KEYWORDS, keywords);
    
  }

  @Override
  public void setPdfSubject(String subject) {
    pdfInfo.put(PdfInfoModel.SUBJECT, subject);
  }
  
  @Override
  public void setInfoElement(String key, String value) { 
    if(PdfInfoModel.getKeys().contains(key)) {
      pdfInfo.put(key, value);
    }
  }
  
  
  /**
   * @return PdfModel as html fragment
   */
  @Override
  public String toHtml() {
    String listStart = "<p>PDF Information</p><ul>\n";
    String listEnd = "</ul>\n";
    StringBuffer htmlBuffer = new StringBuffer();
    
    htmlBuffer.append(listStart);

    Iterator<String> pdfInfoIt = pdfInfo.keys();
    while(pdfInfoIt.hasNext()) {
      String key = pdfInfoIt.next();
     
      if(!pdfInfo.isNull(key)) {

        logger.debug(key);
        htmlBuffer.append("<li>" + PdfInfoModel.getInfoLabel().get(key) + ": " + pdfInfo.get(key) + "</li>\n");        
      }
      
    }
    
    htmlBuffer.append(listEnd);
    return htmlBuffer.toString();
  }
  
  @Override
  public String toJson() {
    return pdfInfo.toString(3);
  }
  
  @Override
  public JSONObject getJSONObject() {
    return pdfInfo;
  }
  
 
  
}
