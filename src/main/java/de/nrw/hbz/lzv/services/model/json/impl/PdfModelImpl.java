/**
 * 
 */
package de.nrw.hbz.lzv.services.model.json.impl;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.json.JSONObject;
import de.nrw.hbz.lzv.services.model.pdf.model.Version;

/**
 * 
 */
public class PdfModelImpl {

  private static Logger logger = LogManager.getLogger(PdfModelImpl.class);
  private JSONObject pdfModel = new JSONObject();
  private PdfVersion pdfVersion = new PdfVersion();
  private PdfACompliance pdfCompliance = new PdfACompliance();
  
  /**
   * Constructor takes Map of PDFbox detected values into PdfModel
   * @param pdfMd
   */
  public PdfModelImpl(Map<String,Object> pdfMd) {
    pdfModel.put("PDFbox Validation", pdfMd);    
  }
  
  /**
   * Add PDFbox detected literals to Map 
   * @param key
   * 
   * @param value
   */
  public void setLiteral(String key, String value ) {
    pdfModel.put(key, value);
  }

  /**
   * Add PDFbox detected PDF version to PdfModel's PdfVersion object
   * and append Pronom Format-URL
   *  
   * @param versionKey
   */
  public void setVersion(String versionKey) {

    if(Version.labelExists(versionKey)) {
      pdfVersion.setPrefLabel(versionKey);
      pdfVersion.setId(Version.getVersionUrl(versionKey));
      pdfModel.put(pdfVersion.getName(), pdfVersion.getMap());
    }
  }
  
  /**
   * Set PdfACompliancy to none if PDF's version is not capable for PDF/A
   * e.g. Versions 1.0 to 1.3 and Versions 1.5 to 1.6
   *   
   * @param complianceKey
   */
  public void setCompliance(String complianceKey) {

    if(Version.labelExists(complianceKey)) {
      pdfCompliance.setPrefLabel(complianceKey);
      pdfCompliance.setId(Version.getVersionUrl(complianceKey));
      pdfModel.put(pdfCompliance.getName(), pdfVersion.getMap());
    }
  }

  /**
   * Set (modification) Date to PdfModelImpl 
   * @param key
   */
  public void setDate(String key) {
    pdfModel.put(key, PdfDate.getDate());
  }
  
  
  /**
   * 
   * @return PdfModel as Json
   */
  @Override
  public String toString() {
    return pdfModel.toString(1);
  }
  
  /**
   * @return PdfModel as html fragment
   */
  public String toHtml() {
    String listStart = "<ul>\n";
    String listEnd = "</ul>\n";
    StringBuffer htmlBuffer = new StringBuffer();
    
    JSONObject pdfMd = pdfModel.getJSONObject("PDFbox Validation");
    
    if(pdfMd.has("Error")) {
      htmlBuffer.append("<p>File: " + pdfMd.get("File") +  "</p>\n<ul>");
      htmlBuffer.append("<li class='error'>Application cannot validate File!</li>\n");
      htmlBuffer.append("<li class='error'>Reason: " + pdfMd.get("Reason") +  "</li>\n");
      htmlBuffer.append("<li class='error'>Error: " + pdfMd.get("Error") +  "</li>\n");
      htmlBuffer.append("<li class='error'>Exception Type is: " + pdfMd.get("ExceptionType") +  "</li>\n");
      htmlBuffer.append("</ul>");
      return htmlBuffer.toString();
    }
    
    JSONObject pdfVersion = pdfMd.getJSONObject("Version");
    pdfMd.remove("Version");

    
    Hashtable<String, String> encryptionResult = new Hashtable<>();
    encryptionResult.put("false", "<li class='success'>PDF is NOT encrypted</li>\n");
    encryptionResult.put("true", "<li class='error'>PDF is ENCRYPTED</li>\n");

    Hashtable<String, String> pdfaCompliancyResult = new Hashtable<>();
    pdfaCompliancyResult.put("none", "<li class='error'>PDF not a PDF/A</li>\n");

    htmlBuffer.append("<p>File: " + pdfMd.get("File") +  "</p>\n");
    pdfMd.remove("File");
    
    htmlBuffer.append(listStart);
    htmlBuffer.append("<li>PDF is of Version: <a href='" + pdfVersion.get("@id") + "'>" + pdfVersion.get("prefLabel") +  "</a></li>\n");
    htmlBuffer.append(encryptionResult.get(pdfMd.get("Encrypted")));
    pdfMd.remove("Encrypted");
    
    if(pdfMd.has("PdfACompliancy")) {
      htmlBuffer.append(pdfaCompliancyResult.get(pdfMd.get("PdfACompliancy")));
      pdfMd.remove("PdfACompliancy");
    }
 
    // Take special care of some descriptive MD
    if(!pdfMd.has("Title")) {
      htmlBuffer.append("<li>Title: null</li>\n");

    } else {
      htmlBuffer.append("<li>Title: " + pdfMd.get("Title"));
      pdfMd.remove("Title");
    }

    if(!pdfMd.has("Author")) {
      htmlBuffer.append("<li>Author: null</li>\n");

    } else {
      htmlBuffer.append("<li>Author: " + pdfMd.get("Author"));
      pdfMd.remove("Author");
    }

    // Use Iterator for all remaining fields
    Iterator<String> pdfInfoIt = pdfMd.keySet().iterator();
    
    
    while(pdfInfoIt.hasNext()) {
      String key = pdfInfoIt.next();
      
      // In first step iterate over literals only
      logger.debug(key);
      htmlBuffer.append("<li>" + key + ": " + pdfMd.get(key) + "</li>\n");
      
    }
    
    htmlBuffer.append(listEnd);
    return htmlBuffer.toString();
  }
  
}
