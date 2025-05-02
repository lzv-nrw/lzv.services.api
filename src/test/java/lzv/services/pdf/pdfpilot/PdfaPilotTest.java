/**
 * 
 */
package lzv.services.pdf.pdfpilot;

import static org.junit.Assert.assertTrue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import de.nrw.hbz.lzv.services.impl.VersionInfo;


/**
 * 
 */
public class PdfaPilotTest {
  
  private static Logger log = LogManager.getLogger(PdfaPilotTest.class);

  /**
   * @param args
   */
  public static void main(String[] args) {
    
    log.info("Teste Jetzt");
    PdfaPilotTest ppt = new PdfaPilotTest();
    ppt.getPluginName();
    
    

  }
  
  @Test
  public void getPdfaPilotVersion() {
   VersionInfo versInfo = VersionInfo.getInstance("pdfapilot");
   System.out.println(versInfo.getPluginName());
   //assertNotNull(ServiceImpl.getVersion());
  }
  
  @Test
  public void getVersionInfoInstance() {
   
    //System.out.println(new VersionInfo().getPluginName());
   //assertNotNull(ServiceImpl.getVersion());
  }

  @Test
  public void getPluginName() {
   VersionInfo versInfo = VersionInfo.getInstance("pdfapilot");
   System.out.println(versInfo.getPluginName());
   assertTrue(versInfo.getPluginName().equals("pdfapilot"));
  }

  
}