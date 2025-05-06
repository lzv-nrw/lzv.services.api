/**
 * 
 */
package de.nrw.hbz.lzv.services.plugin.pdfbox.service.impl;

import org.apache.pdfbox.pdmodel.PDDocumentInformation;

import de.nrw.hbz.lzv.services.model.json.impl.PdfInfo;
import de.nrw.hbz.lzv.services.util.TimePrefix;

/**
 * 
 */
public class PdfInfoProvider {
  
  private PDDocumentInformation pdDocInfo = null;
  private PdfInfo pdfInfo = null;
  
  
  public PdfInfoProvider(PDDocumentInformation docInf) {
    this.pdDocInfo = docInf;
  }

  public void setPdfInfo() {
    
    pdfInfo.setPdfTitle(pdDocInfo.getTitle());
    pdfInfo.setPdfAuthor(pdDocInfo.getAuthor());
    pdfInfo.setPdfCreator(pdDocInfo.getCreator());
    pdfInfo.setPdfProducer(pdDocInfo.getProducer());
    pdfInfo.setPdfCreationDate(TimePrefix.setFormat(pdDocInfo.getCreationDate()));
    pdfInfo.setPdfModificationDate(TimePrefix.setFormat(pdDocInfo.getModificationDate()));
    pdfInfo.setPdfKeywords(pdDocInfo.getKeywords());
    pdfInfo.setPdfSubject(pdDocInfo.getSubject());
  }

  /**
   * Get a plug-in independent object representing PDF Information
   * @return PdfInfo
   */
  public PdfInfo getPdfInfo() {
    return pdfInfo;
  }
}
