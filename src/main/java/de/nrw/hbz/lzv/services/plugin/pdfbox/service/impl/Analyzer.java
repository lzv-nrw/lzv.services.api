/**
 * 
 */
package de.nrw.hbz.lzv.services.plugin.pdfbox.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

  private static Logger logger = LogManager.getLogger(ServiceImpl.class);
  
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
     * @return
     */
    private PdfInfo getPdfInfo(PDDocument pdDocument){
      PdfInfoProvider infoProvider = new PdfInfoProvider(pdDocument.getDocumentInformation()); 
      return infoProvider.getPdfInfo();
    }


  @Override
  public String getHtml() {

    resultBuffer.append(HtmlTemplate.getHtmlHead());

    resultBuffer.append("<h1>Ergebnis der Prüfung</h1>\n");
    resultBuffer.append("<p>" + fileName + "</p>");
    resultBuffer.append(pdfInfo.toHtml());

    resultBuffer.append("<p><a href=\"/lzv-jsp/pdfbox/upload\">Weitere PDF-Validierung</a>");
    resultBuffer.append(HtmlTemplate.getHtmlFoot());

    return resultBuffer.toString();
  }


  @Override
  public String getJson() {
    JSONObject resultJson = new JSONObject();
 
    resultJson.put("file", fileName);
    resultJson.put("pdfInfo", pdfInfo.getJSONObject());
    return resultJson.toString(3);
  }
  
}
