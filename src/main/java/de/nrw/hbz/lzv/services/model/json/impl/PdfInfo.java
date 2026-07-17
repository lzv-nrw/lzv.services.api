/**
 * 
 */
package de.nrw.hbz.lzv.services.model.json.impl;

import java.util.LinkedHashMap;
import java.util.Map;

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

  private final LinkedHashMap<String, Object> pdfInfoMap = new LinkedHashMap<>();

  @Override
  public void setPdfAuthor(String author) {
    pdfInfoMap.put(PdfInfoModel.AUTHOR, author);
  }

  @Override
  public void setPdfCreator(String creationTool) {
    pdfInfoMap.put(PdfInfoModel.CREATOR, creationTool);
  }

  @Override
  public void setPdfProducer(String productionTool) {
    pdfInfoMap.put(PdfInfoModel.PRODUCER, productionTool);
  }

  @Override
  public void setPdfCreationDate(String creationDate) {
    pdfInfoMap.put(PdfInfoModel.CREATION_DATE, creationDate);

  }

  @Override
  public void setPdfPages(String pages) {
    pdfInfoMap.put(PdfInfoModel.PAGES, pages);
  }

  @Override
  public void setPdfTitle(String title) {
    pdfInfoMap.put(PdfInfoModel.TITLE, title);
  }

  @Override
  public void setPdfModificationDate(String modificationDate) {
    pdfInfoMap.put(PdfInfoModel.MODIFICATION_DATE, modificationDate);
  }

  @Override
  public void setPdfKeywords(String keywords) {
    pdfInfoMap.put(PdfInfoModel.KEYWORDS, keywords);

  }

  @Override
  public void setPdfSubject(String subject) {
    pdfInfoMap.put(PdfInfoModel.SUBJECT, subject);
  }

  @Override
  public void setInfoElement(String key, String value) {
    if (PdfInfoModel.getKeys().contains(key)) {
      pdfInfoMap.put(key, value);
    }
  }

  /**
   * @return PdfModel as html fragment
   */
  @Override
  public String toHtml() {
    String listStart = "<h3>PDF Informationen</h3><ul>\n";
    String listEnd = "</ul>\n";
    StringBuffer htmlBuffer = new StringBuffer();

    htmlBuffer.append(listStart);

    for (Map.Entry<String, Object> entry : pdfInfoMap.entrySet()) {

      if (entry.getValue() != null) {
        logger.debug(entry.getKey());
        htmlBuffer
            .append("<li>" + PdfInfoModel.getInfoLabel().get(entry.getKey()) + ": " + entry.getValue() + "</li>\n");
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

    pdfInfo = new JSONObject(pdfInfoMap);

    return pdfInfo;
  }

}
