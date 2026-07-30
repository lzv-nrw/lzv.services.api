/**
 * 
 */
package de.nrw.hbz.lzv.services.plugin.pdfbox.service.impl;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;

import de.nrw.hbz.lzv.services.model.json.impl.PdfInfo;
import de.nrw.hbz.lzv.services.util.TimePrefix;

/**
 * 
 */
public class PdfInfoProvider {

  private PDDocumentInformation pdDocInfo = null;
  private PdfInfo pdfInfo = null;
  private String pdfVersion = null;

  public PdfInfoProvider(PDDocument pdDocument) {
    this.pdDocInfo = pdDocument.getDocumentInformation();
    this.pdfVersion = Float.toString(pdDocument.getVersion());
    setPdfInfo();
  }

  public void setPdfInfo() {

    pdfInfo = new PdfInfo();
    if (pdDocInfo.getTitle() != null && !pdDocInfo.getTitle().isBlank()) {
      pdfInfo.setPdfTitle(pdDocInfo.getTitle());
    }
    if (pdDocInfo.getAuthor() != null && !pdDocInfo.getAuthor().isBlank()) {
      pdfInfo.setPdfAuthor(pdDocInfo.getAuthor());
    }
    if (pdDocInfo.getCreator() != null && !pdDocInfo.getCreator().isBlank()) {
      pdfInfo.setPdfCreator(pdDocInfo.getCreator());
    }
    if (pdDocInfo.getProducer() != null && !pdDocInfo.getProducer().isBlank()) {
      pdfInfo.setPdfProducer(pdDocInfo.getProducer());
    }
    pdfInfo.setPdfVersion(pdfVersion);
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
