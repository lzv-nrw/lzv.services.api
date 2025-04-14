/**
 * 
 */
package lzv.services.pdf.pdfapilot;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import de.nrw.hbz.lzv.services.impl.PdfACreator;
import de.nrw.hbz.lzv.services.model.pdfa.result.PdfaPilotResult;

/**
 * 
 */
public class TestPdfaPilot {

  private static Logger logger = LogManager.getLogger(TestPdfaPilot.class);

  /**
   * @param args
   */
  public static void main(String[] args) {

    TestPdfaPilot tpap = new TestPdfaPilot();
    tpap.getExecuteString();
    
  }
  
  @Test
  public void getExecuteString() {
    
    PdfACreator pdfaPC = PdfACreator.getInstance("pdfapilot");
    PdfaPilotResult result = pdfaPC.createPdfa(new File("src/test/resources/pdf.pdf"), "pdf.pdf" , "3a");
    //logger.info(result);
    //assertEquals("run for test only", result.getSummaryList().get(0).toString());
    
  }

}
