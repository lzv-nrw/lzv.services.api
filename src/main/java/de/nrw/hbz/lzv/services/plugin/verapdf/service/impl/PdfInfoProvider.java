/**
 * 
 */
package de.nrw.hbz.lzv.services.plugin.verapdf.service.impl;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.verapdf.metadata.fixer.entity.InfoDictionary;

import de.nrw.hbz.lzv.services.model.json.impl.PdfInfo;
import de.nrw.hbz.lzv.services.util.TimePrefix;

/**
 * 
 */
public class PdfInfoProvider {

  private InfoDictionary infoDict = null;
  private PdfInfo pdfInfo = null;

  public PdfInfoProvider(InfoDictionary infoDict) {
    this.infoDict = infoDict;
    setPdfInfo();
  }

  public void setPdfInfo() {
    pdfInfo = new PdfInfo();
    pdfInfo.setPdfTitle(infoDict.getTitle());
    pdfInfo.setPdfAuthor(infoDict.getAuthor());
    pdfInfo.setPdfCreator(infoDict.getCreator());
    pdfInfo.setPdfProducer(infoDict.getProducer());
    pdfInfo.setPdfKeywords(infoDict.getKeywords());
    pdfInfo.setPdfSubject(infoDict.getSubject());
    if (infoDict.getCreationDate() != null) {
      String dateString = infoDict.getCreationDate();
      dateString = dateString.replaceFirst("([+-]\\d{2})'(\\d{2})'", "$1:$2");
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("'D:'yyyyMMddHHmmssXXX");
      OffsetDateTime odt = OffsetDateTime.parse(dateString, formatter);
      Calendar calendar = GregorianCalendar.from(odt.toZonedDateTime());

      pdfInfo.setPdfCreationDate(TimePrefix.setFormat(calendar));
    }
    if (infoDict.getModificationDate() != null) {
      String dateString = infoDict.getModificationDate();

      dateString = dateString.replaceFirst("([+-]\\d{2})'(\\d{2})'", "$1:$2");
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("'D:'yyyyMMddHHmmssXXX");
      OffsetDateTime odt = OffsetDateTime.parse(dateString, formatter);
      Calendar calendar = GregorianCalendar.from(odt.toZonedDateTime());

      pdfInfo.setPdfModificationDate(TimePrefix.setFormat(calendar));
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
