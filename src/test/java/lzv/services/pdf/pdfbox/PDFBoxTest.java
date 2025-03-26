/**
 * 
 */
package lzv.services.pdf.pdfbox;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.InputStream;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import de.nrw.hbz.lzv.services.model.json.impl.PdfModelImpl;
import de.nrw.hbz.lzv.services.plugin.pdfbox.service.impl.ServiceImpl;
import de.nrw.hbz.lzv.services.util.file.FileUtil;


/**
 * 
 */
public class PDFBoxTest {
  
  private static Logger logger = LogManager.getLogger(PDFBoxTest.class);

  /**
   * @param args
   */
  public static void main(String[] args) {
   
    logger.debug("Starte PDFBoxTest als Hauptklasse");
    PDFBoxTest vpt = new PDFBoxTest();
    //vpt.getPDFVersion();
    //vpt.editInfo();
    vpt.getPdfMdAsJson();
    //vpt.getPdfMdAsHtml();
    

  }
  
  @Test
  public void getPDFVersion() {
   
   ServiceImpl boxService = new ServiceImpl();
   
   File pdf = new File("src/test/resources/pdf.pdf");
   String test = boxService.getPdfVersion(pdf);
   logger.info("Ergebnis: " + test);
   File pdfa = new File("src/test/resources/pdfa_1b.pdf");
   test = boxService.getPdfVersion(pdfa);
   logger.info("Ergebnis: " + test);
   
   assertNotNull(test);
  }
  
  @Test
  public void editInfo() {
    ServiceImpl boxService = new ServiceImpl();
    File pdf = new File("src/test/resources/pdf.pdf");
    String fileName = boxService.editPDFInfo(pdf, "title", "Dies ist ein PDF zum Testen").getAbsolutePath();
    logger.info(fileName);
    assertTrue(fileName.endsWith(".pdf"));
    
  }
  
  @Test
  public void getPdfMdAsJson() {
    ServiceImpl boxService = new ServiceImpl();
    File pdf = new File("src/test/resources/pdf1.4-encrypted.pdf");
    Map<String,Object> resultMap = boxService.getPdfMD(pdf , pdf.getName());
    PdfModelImpl pmi = new PdfModelImpl(resultMap);
    logger.info(pmi.toString());
    
  }
  
  @Test
  public void getPdfMdAsHtml() {
    ServiceImpl boxService = new ServiceImpl();
    File pdf = new File("src/test/resources/DNS-Betrieb.pdf");
    Map<String,Object> resultMap = boxService.getPdfMD(pdf , pdf.getName());
    PdfModelImpl pmi = new PdfModelImpl(resultMap);
    logger.info(pmi.toHtml());

    pdf = new File("src/test/resources/pdf1.4-encrypted.pdf");
    resultMap = boxService.getPdfMD(pdf , pdf.getName());
    pmi = new PdfModelImpl(resultMap);
    logger.info(pmi.toHtml());
    
  }

  
}
