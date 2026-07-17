/**
 * 
 */
package de.nrw.hbz.lzv.services.plugin.pdfbox.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.json.JSONObject;

import de.nrw.hbz.lzv.services.model.json.impl.PdfInfo;
import de.nrw.hbz.lzv.services.plugin.pdfbox.service.impl.PdfInfoProvider;
import de.nrw.hbz.lzv.services.template.HtmlTemplate;

/**
 * 
 */
public class Analyzer extends de.nrw.hbz.lzv.services.impl.Analyzer {


  @Override
  public void analyze(File file, String fileName) {

    this.fileName = fileName;
    PDDocument pdDoc;
    try {
      pdDoc = Loader.loadPDF(file);
      pdfInfo = getPdfInfo(pdDoc);
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    // TODO Auto-generated method stub
  }

  /**
   * get the information stored in the PDF information part
   * 
   * @return
   */
  private PdfInfo getPdfInfo(PDDocument pdDocument) {
    PdfInfoProvider infoProvider = new PdfInfoProvider(pdDocument.getDocumentInformation());
    return infoProvider.getPdfInfo();
  }

  @Override
  public String getHtml() {

    resultBuffer.append(HtmlTemplate.getHtmlHead());

    resultBuffer.append("<h1>Ergebnis der Prüfung mit PDFBox</h1>\n");
    resultBuffer.append("<h2>Analysierte Datei: " + fileName + "</h2>");
    if (pdfInfo == null || pdfInfo.getJSONObject().isEmpty()) {
      resultBuffer
      .append("<h3>PDF Informationen</h3><ul>\n<li>Keine Informationen verfügbar oder extrahierbar</li>\n</ul>\n");
    } else {
      resultBuffer.append(pdfInfo.toHtml());
    }
    resultBuffer.append("<p><i class=\"fa-solid fa-magnifying-glass\"></i><a href=\"/lzv-jsp/pdfbox/upload\">Weitere PDF-Validierung</a>");
    resultBuffer.append(HtmlTemplate.getHtmlFoot());

    return resultBuffer.toString();
  }

  @Override
  public String getJson() {
    JSONObject resultJson = new JSONObject();

    resultJson.put("file", fileName);
    if (pdfInfo != null) {
      resultJson.put("pdfInfo", pdfInfo.getJSONObject());
    } else {
      resultJson.put("pdfInfo", new JSONObject());
    }
    return resultJson.toString(3);
  }

}
