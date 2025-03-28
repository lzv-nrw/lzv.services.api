/**
 * 
 */
package lzv.services.pdf.rest.service;

import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.text.ParseException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.junit.Test;

import de.nrw.hbz.lzv.services.plugin.verapdf.service.rest.impl.JerseyServiceImpl;
import de.nrw.hbz.lzv.services.util.file.FileUtil;

/**
 * 
 */
public class RestServicesTest {

  final static Logger logger = LogManager.getLogger(RestServicesTest.class);

  /**
   * @param args
   */
  public static void main(String[] args) {
    
    logger.info("Hier bin ich");
    RestServicesTest rst = new RestServicesTest();
    rst.validate();
    rst.editPdf();
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
  
  @Test
  public void editPdf() {
    // TODO: learn how to set FormDataContentDisposition correctly
    JerseyServiceImpl jSI = new JerseyServiceImpl();
    FormDataContentDisposition contDisp;

    File testFile = new File("src/test/resources/pdfa_1b.pdf");
    try {
      contDisp = new FormDataContentDisposition("form-data;name=pdfa_1b.pdf;filename=pdfa_1b.pdf");
      File editedFile =  jSI.editMD(FileUtil.loadFile(testFile), contDisp, "Author", "aquast");
      
      logger.info(editedFile.getAbsolutePath());
    } catch (ParseException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    
  }

}
