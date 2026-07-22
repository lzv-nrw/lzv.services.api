/**
 * 
 */
package de.nrw.hbz.lzv.services.plugin.pdfapilot.service.impl;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.stream.Stream;

import de.nrw.hbz.lzv.services.model.json.impl.PdfInfo;
import de.nrw.hbz.lzv.services.model.json.model.PdfInfoModel;
import de.nrw.hbz.lzv.services.util.TimePrefix;

/**
 * 
 */
public class PdfInfoProvider {

  private String stout = null;
  private PdfInfo pdfInfo = null;

  public PdfInfoProvider(String stout) {
    this.stout = stout;
    setPdfInfo();
  }

  public void setPdfInfo() {

    pdfInfo = new PdfInfo();
    Stream<String> resultLines = stout.lines();

    Iterator<String> rlIt = resultLines.iterator();

    while (rlIt.hasNext()) {
      String line = rlIt.next();
      if (line.startsWith("Info")) {
        String[] split = line.split("\t", -1);
        LinkedHashMap<String, String> infoLabels = PdfInfoModel.getInfoLabel();
        if (infoLabels.containsKey(split[1].toLowerCase())) {
          String key = split[1].toLowerCase();
          String value = split.length > 2 ? split[2] : "";
          if (!value.equals("<no entry>") && !value.equals("")) {
            if (key.equals("created") || key.equals("modified")) {
              String dateString = value;
              dateString = dateString.replaceFirst("([+-]\\d{2})'(\\d{2})'", "$1:$2");
              DateTimeFormatter formatter = DateTimeFormatter.ofPattern("'D:'yyyyMMddHHmmssXXX");
              OffsetDateTime odt = OffsetDateTime.parse(dateString, formatter);
              Calendar calendar = GregorianCalendar.from(odt.toZonedDateTime());

              pdfInfo.setInfoElement(key, TimePrefix.setFormat(calendar));
            } else {
              pdfInfo.setInfoElement(key, value);
            }
          }
        } else {
          pdfInfo.setInfoElement(split[1], split[2]);
        }
      }
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
