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
    if (pdDocInfo.getKeywords() != null && !pdDocInfo.getKeywords().isBlank()) {
      pdfInfo.setPdfKeywords(pdDocInfo.getKeywords());
    }
    if (pdDocInfo.getSubject() != null && !pdDocInfo.getSubject().isBlank()) {
      pdfInfo.setPdfSubject(pdDocInfo.getSubject());
    }
    if (pdDocInfo.getCreationDate() != null) {
      pdfInfo.setPdfCreationDate(TimePrefix.setFormat(pdDocInfo.getCreationDate()));
    }
    if (pdDocInfo.getModificationDate() != null) {
      pdfInfo.setPdfModificationDate(TimePrefix.setFormat(pdDocInfo.getModificationDate()));
    }
  }

  /**
   * Get a plug-in independent object representing PDF Information
   * 
   * @return PdfInfo
   */
  public PdfInfo getPdfInfo() {
    return pdfInfo;
  }
}
