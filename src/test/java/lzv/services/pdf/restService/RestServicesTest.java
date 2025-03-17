/**
 * 
 */
package lzv.services.pdf.restService;

import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.text.ParseException;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.junit.Test;

import de.nrw.hbz.lzv.services.fileUtil.FileUtil;
import de.nrw.hbz.lzv.services.plugin.veraPDF.restServiceImpl.JerseyServiceImpl;

/**
 * 
 */
public class RestServicesTest {

  /**
   * @param args
   */
  public static void main(String[] args) {
    
    RestServicesTest rst = new RestServicesTest();
    rst.validate();
  }

  @Test
  public void versionTest() {
    JerseyServiceImpl jSI = new JerseyServiceImpl();
    String result = jSI.getVersionJson();
    assertNotNull(result);
    
  }
  
  @Test
  public void validate() {
    // TODO: learn how to set FormDataContentDisposition correctly
    JerseyServiceImpl jSI = new JerseyServiceImpl();
    FormDataContentDisposition contDisp;
    try {
      File testFile = new File("src/test/resources/pdfa_1b.pdf");
      
      contDisp = new FormDataContentDisposition("form-data;name=pdfa_1b.pdf;filename=pdfa_1b.pdf");
      String result = jSI.validatePdfA(FileUtil.loadFile(testFile), contDisp);
      System.out.println(result);
    } catch (ParseException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
  
}
