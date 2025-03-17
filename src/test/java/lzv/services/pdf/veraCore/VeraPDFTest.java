/**
 * 
 */
package lzv.services.pdf.veraCore;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import de.nrw.hbz.lzv.services.plugin.veraImpl.ServiceImpl;


/**
 * 
 */
public class VeraPDFTest {
  
  private static Logger log = LogManager.getLogger(VeraPDFTest.class);

  /**
   * @param args
   */
  public static void main(String[] args) {
    
    VeraPDFTest vpt = new VeraPDFTest();
    vpt.getVeraPDFVersion();
    ServiceImpl.listFoundries();
    vpt.validate();
    

  }
  
  @Test
  public void getVeraPDFVersion() {
   System.out.println(ServiceImpl.getVersion());
   assertNotNull(ServiceImpl.getVersion());
  }
  
  @Test
  public void validate() {
    String result1 = null;
    String result2 = null;
    
    File pdfa_1b = new File("src/test/resources/pdfa_1b.pdf");
    if (pdfa_1b.exists()) {
      System.out.println("Found test file at " + pdfa_1b.getAbsolutePath());
      ServiceImpl serviceImpl = new ServiceImpl();
      serviceImpl.setPDFFile(pdfa_1b);
      result1 = serviceImpl.validatePDF();
      System.out.println(result1);
    } else {
      System.out.println("no file found at " + pdfa_1b.getAbsolutePath());
    }  
    
    File pdf = new File("src/test/resources/pdf.pdf");
    if (pdf.exists()) {
      System.out.println("Found test file at " + pdf.getAbsolutePath());
      ServiceImpl serviceImpl = new ServiceImpl();
      serviceImpl.setPDFFile(pdf);
      result2 = serviceImpl.validatePDF();
      System.out.println(result2);
    } else {
      System.out.println("no file found at " + pdf.getAbsolutePath());
    }  
    // assertEquals(result1, "PDF is compliant to PDF/A, Version 1b");
    // assertEquals(result2, "PDF is NOT compliant to any PDF/A flavour");
  }

}
