/**
 * 
 */
package de.nrw.hbz.lzv.services.plugin.pdfbox.service.impl;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.cos.COSStream;
import org.apache.pdfbox.io.ScratchFile;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.filetypedetector.FileType;
import org.apache.pdfbox.util.filetypedetector.FileTypeDetector;


/**
 * 
 */
public class ServiceImpl {
  
  private static Logger logger = LogManager.getLogger(ServiceImpl.class);
      
  private InputStream pdfStream = null;
  private BufferedInputStream pdfBis = null;

  public String getFileFormat(InputStream fileInputStream) {
    logger.info("Delegate file Format detection");
    
    BufferedInputStream bis = new BufferedInputStream(fileInputStream);
    pdfBis = bis;
    
    String result = "<p class=\"success\">File is of " + getFileFormat() + " Format</p>";
  
    return result;
  }
  
  /**
   * Method doesn't recognize PDF. It only recognizes Image Formats etc.
   * @return a File Format as capitalized suffix e.g. PNG  
   */
  public String getFileFormat() {
    FileType fType = null;
    logger.info("run file Format detection");
    try {
      fType = FileTypeDetector.detectFileType(pdfBis);
    } catch (IOException e) {
      // TODO Auto-generated catch block
      logger.error(e.getMessage());
    }
    
    return fType.toString();
  }

  public String getPdfVersion(File file) {

    String result = "<p class=\"error\">File is not a PDF</p>";
        // + "<p><a href=\"/lzv-api/verapdf/format\">Datei-Format zu ermitteln?</a></p>";
 
    String versionNumber = "unkown";
    PDDocument pdDoc = new PDDocument();

    try {
      pdDoc = Loader.loadPDF(file);
      versionNumber = Float.toString(pdDoc.getVersion());
      result = "<p class=\"success\">PDF is of Version :" + versionNumber + "</p>";

    
    } catch (IOException e) {
      e.printStackTrace();
    }    
    try {
      pdDoc.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return result;
  }

  
}
