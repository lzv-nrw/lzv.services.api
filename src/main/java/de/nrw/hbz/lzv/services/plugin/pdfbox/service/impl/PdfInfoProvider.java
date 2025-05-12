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
    setPdfInfo();
  }

  public void setPdfInfo() {
    
    pdfInfo = new PdfInfo();
    pdfInfo.setPdfTitle(pdDocInfo.getTitle());
    pdfInfo.setPdfAuthor(pdDocInfo.getAuthor());
    pdfInfo.setPdfCreator(pdDocInfo.getCreator());
    pdfInfo.setPdfProducer(pdDocInfo.getProducer());
    if(pdDocInfo.getCreationDate() != null) {
      pdfInfo.setPdfCreationDate(TimePrefix.setFormat(pdDocInfo.getCreationDate()));      
    }
    if(pdDocInfo.getModificationDate() != null) {
    pdfInfo.setPdfModificationDate(TimePrefix.setFormat(pdDocInfo.getModificationDate()));
    } else {
      pdfInfo.setPdfModificationDate(TimePrefix.getTimePrefix());
    }
    
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
