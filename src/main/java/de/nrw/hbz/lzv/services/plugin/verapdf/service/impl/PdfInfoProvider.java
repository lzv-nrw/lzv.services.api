/**
 * 
 */
package de.nrw.hbz.lzv.services.plugin.verapdf.service.impl;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.verapdf.core.ModelParsingException;
import org.verapdf.gf.model.impl.cos.GFCosDocument;
import org.verapdf.metadata.fixer.entity.InfoDictionary;
import org.verapdf.pdfa.PDFAParser;

import de.nrw.hbz.lzv.services.model.json.impl.PdfInfo;
import de.nrw.hbz.lzv.services.util.TimePrefix;

/**
 * 
 */
public class PdfInfoProvider {

  private InfoDictionary infoDict = null;
  private PdfInfo pdfInfo = null;
  private String pdfVersion = null;

  public PdfInfoProvider(PDFAParser pdfParser) {
    this.infoDict = pdfParser.getPDFDocument().getInfoDictionary();
    try {
      GFCosDocument root = (GFCosDocument) pdfParser.getRoot();
      this.pdfVersion = String.format("%.1f", root.getheaderVersion()).replace(',', '.');
    } catch (ModelParsingException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    setPdfInfo();
  }

  public void setPdfInfo() {
    pdfInfo = new PdfInfo();
    if (infoDict != null) {
      pdfInfo.setPdfTitle(infoDict.getTitle());
    }
    if (infoDict != null) {
      pdfInfo.setPdfAuthor(infoDict.getAuthor());
    }
    if (infoDict != null) {
      pdfInfo.setPdfCreator(infoDict.getCreator());
    }
    if (infoDict != null) {
      pdfInfo.setPdfProducer(infoDict.getProducer());
    }
    pdfInfo.setPdfVersion(pdfVersion);
    if (infoDict != null) {
      pdfInfo.setPdfKeywords(infoDict.getKeywords());
    }
    if (infoDict != null) {
      pdfInfo.setPdfSubject(infoDict.getSubject());
    }
    if (infoDict != null && infoDict.getCreationDate() != null) {
      String dateString = infoDict.getCreationDate();
      dateString = dateString.replaceFirst("([+-]\\d{2})'(\\d{2})'", "$1:$2");
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("'D:'yyyyMMddHHmmssXXX");
      OffsetDateTime odt = OffsetDateTime.parse(dateString, formatter);
      Calendar calendar = GregorianCalendar.from(odt.toZonedDateTime());

      pdfInfo.setPdfCreationDate(TimePrefix.setFormat(calendar));
    }
    if (infoDict != null && infoDict.getModificationDate() != null) {
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
