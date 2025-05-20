/**
 * 
 */
package de.nrw.hbz.lzv.services.model.json.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import de.nrw.hbz.lzv.services.model.json.model.PdfAComplianceModel;
import de.nrw.hbz.lzv.services.model.pdf.model.Compliance;


/**
 * 
 */
public class PdfACompliance {

  private static Logger logger = LogManager.getLogger(PdfACompliance.class);
  private JSONObject pdfACompl = new JSONObject();

  public void setIsPdfACompliant(boolean compliance) {
    logger.debug("found is compliant: " + Boolean.toString(compliance));
    pdfACompl.put("pdfACompliance", Boolean.toString(compliance));
  }
  
  /**
   * @param level prefLabel of PDF/A level aka flavour
   */
  public void setCompliance(String level) {
    PdfAComplianceModel complianceModel = new PdfAComplianceModel();
    complianceModel.setId(Compliance.getComplianceFormat(level));
    complianceModel.setPrefLabel(level);
    pdfACompl.put("complianceLevel", complianceModel.getMap());
  }
  
  /**
   * @return PdfModel as html fragment
   */
  public String toHtml() {
    String listStart = "<p>PDF/A Compliance Information</p><ul>\n";
    String listEnd = "</ul>\n";
    StringBuffer htmlBuffer = new StringBuffer();

    htmlBuffer.append(listStart);

    if(pdfACompl.get("pdfACompliance").toString().equals("true")) {
      
      String levelLabel = (String) pdfACompl.getJSONObject("complianceLevel").get("prefLabel");
      String formatUrl = Compliance.getComplianceUrl(levelLabel);
      htmlBuffer.append("<li class=\"success\">PDF is compliant to PDF/A, Version " + 
      "<a target='_blank' href='" + formatUrl + "' >" + levelLabel + "</li>");
    } else {

      htmlBuffer.append("<li class=\"error\">PDF is NOT compliant to any PDF/A level</li>");      
    }
    
    htmlBuffer.append(listEnd);
    return htmlBuffer.toString();
  }
  
  public String toJson() {
    return pdfACompl.toString(3);
  }
  
  public JSONObject getJSONObject() {
    return pdfACompl;
  }


}
