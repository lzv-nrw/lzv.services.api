/**
 * 
 */
package lzv.services.pdf.pdfbox;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.InputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import de.nrw.hbz.lzv.services.file.util.FileUtil;
import de.nrw.hbz.lzv.services.plugin.pdfbox.service.impl.ServiceImpl;


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
    vpt.getPDFVersion();
    vpt.editInfo();
    

  }
  
  @Test
  public void getPDFVersion() {
   ServiceImpl boxService = new ServiceImpl();
   File pdf = new File("src/test/resources/pdf.pdf");
   String test = boxService.getPdfVersion(pdf);
   logger.info("Ergebnis: " + test);
   File pdfa = new File("src/test/resources/pdfa_1b.pdf");
   test = boxService.getPdfVersion(pdfa);
   // System.out.println("Huhu: " + test);
   logger.info("Ergebnis: " + test);
   // assertNotNull(ServiceImpl.getVersion());
   assertNotNull(test);
  }
  
  @Test
  public void editInfo() {
    ServiceImpl boxService = new ServiceImpl();
    File pdf = new File("src/test/resources/pdf.pdf");
    String fileName = boxService.editPDFInfo(pdf, "title", "Dies ist ein PDF zum Testen");
    logger.info(fileName);
    
  }
  

}
