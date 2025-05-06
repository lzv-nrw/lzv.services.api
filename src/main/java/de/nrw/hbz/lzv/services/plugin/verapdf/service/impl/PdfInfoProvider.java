/**
 * 
 */
package de.nrw.hbz.lzv.services.plugin.verapdf.service.impl;

import org.verapdf.metadata.fixer.entity.InfoDictionary;

import de.nrw.hbz.lzv.services.model.json.impl.PdfInfo;

/**
 * 
 */
public class PdfInfoProvider {
  
  private InfoDictionary infoDict = null;
  private PdfInfo pdfInfo = null;
  
  
  public PdfInfoProvider(InfoDictionary infoDict) {
    this.infoDict = infoDict;
  }

  public void setPdfInfo() {
    
    pdfInfo.setPdfTitle(infoDict.getTitle());
    pdfInfo.setPdfAuthor(infoDict.getAuthor());
    pdfInfo.setPdfCreator(infoDict.getCreator());
    pdfInfo.setPdfProducer(infoDict.getProducer());
    pdfInfo.setPdfCreationDate(infoDict.getCreationDate());
    pdfInfo.setPdfModificationDate(infoDict.getModificationDate());
    pdfInfo.setPdfKeywords(infoDict.getKeywords());
    pdfInfo.setPdfSubject(infoDict.getSubject());
  }

  /**
   * Get a plug-in independent object representing PDF Information
   * @return PdfInfo
   */
  public PdfInfo getPdfInfo() {
    return pdfInfo;
  }
}
