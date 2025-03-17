/**
 * 
 */
package lzv.services.pdf.restService;

import java.io.File;
import java.io.InputStream;

import org.junit.Test;
import static org.junit.Assert.assertNotNull;

import de.nrw.hbz.lzv.services.fileUtil.FileUtil;

/**
 * 
 */
public class FileUtilTest {

  /**
   * @param args
   */
  public static void main(String[] args) {
    
    FileUtilTest fup = new FileUtilTest();
    fup.saveFile();
  }
  
  
  
  @Test
  public void saveFile() {
    InputStream pis = FileUtil.loadFile(new File("src/test/resources/pdf.pdf"));
    File tmpFile = FileUtil.saveTmpFile(pis, "pdf.pdf");
    System.out.println(tmpFile.getAbsolutePath());
    assertNotNull(tmpFile.getAbsolutePath());
  }

  
}
