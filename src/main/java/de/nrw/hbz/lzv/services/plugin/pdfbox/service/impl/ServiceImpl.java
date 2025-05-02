/**
 * 
 */
package de.nrw.hbz.lzv.services.plugin.pdfbox.service.impl;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.util.filetypedetector.FileType;
import org.apache.pdfbox.util.filetypedetector.FileTypeDetector;

import de.nrw.hbz.lzv.services.model.pdf.model.Version;
import de.nrw.hbz.lzv.services.util.TimePrefix;


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

  /** 
   * Read version information and metadata available in PDF
   * @param file the PDF
   * @return PDF's metadata as Map 
   */
  public Map<String, Object> getPdfMD(File file, String origFileName) {
    Map<String,Object> pdfMd = new HashMap<>();
    Map<String,String> versionMap = new HashMap<>();

    pdfMd.put("File", origFileName);

    try {
        PDDocument pdDoc = Loader.loadPDF(file);
                
        // get PDF Version
        String prefLabel = Float.toString(pdDoc.getVersion());
        logger.info(prefLabel);
        logger.info(Version.getVersionFormat(prefLabel));
        if(Version.labelExists(prefLabel)) {
          versionMap.put("prefLabel", prefLabel);
          versionMap.put("@id", Version.getVersionUrl(prefLabel));
          pdfMd.put("Version", versionMap);
        }
        
        // check for PDF/A readiness
        if(!Version.getVersionIsPdfaApplicable(prefLabel)) {
          pdfMd.put("PdfACompliancy", "none");
        }
        
        // check for encryption
        if(pdDoc.isEncrypted()) {
          pdfMd.put("Encrypted", "true");
        } else {
          pdfMd.put("Encrypted", "false");          
        }
        
        // get more MD from PDDocumentInformation
        PDDocumentInformation docInf = pdDoc.getDocumentInformation();

        LinkedHashMap<String,String> mdMap = getInfo(docInf);
        Set<String> mdKeys = mdMap.keySet();
        Iterator<String> mdIt = mdKeys.iterator();
        
        while(mdIt.hasNext()) {
          String key = mdIt.next();
          pdfMd.put(key, mdMap.get(key));
          
        }

        // get more MD from PDDocument itself
        LinkedHashMap<String,String> pdMap = getInfo(pdDoc);

        Set<String> pdKeys = pdMap.keySet();
        Iterator<String> pdIt = pdKeys.iterator();
        
        while(pdIt.hasNext()) {
          String key = pdIt.next();
          pdfMd.put(key, pdMap.get(key));
          
        }

        try {
          pdDoc.close();
        } catch (IOException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
        
      } catch (IOException e) {
        // TODO Auto-generated catch block
        
        if(e.getClass().getName().equals("org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException")) {
          pdfMd.put("Reason" , "PDF is ENCRYPTED");
        } else {
          pdfMd.put("Reason" , "See Error");          
        }
        pdfMd.put("Error", e.getMessage());
        pdfMd.put("ExceptionType", e.getClass().getName());
        
      } 

      return pdfMd;
  }
  
  
  @Deprecated
  public String getPdfVersion(File file) {

    String result = "<p class=\"error\">File is not a PDF</p>";
        // + "<p><a href=\"/lzv-api/verapdf/format\">Datei-Format zu ermitteln?</a></p>";
 
    String versionNumber = "unkown";
    
    StringBuffer resultSb = new StringBuffer();
    
    try {
      PDDocument pdDoc = Loader.loadPDF(file);
      versionNumber = Float.toString(pdDoc.getVersion());
      resultSb.append("<ul>\n");
      resultSb.append("<li>PDF is of Version: " + versionNumber + "</li>\n");

      if(pdDoc.isEncrypted()) {
        resultSb.append("<li class=\"error\">PDF is encrypted</li>\n");
        // TODO: check out what we can do with this information
        // PDEncryption encrypt = pdDoc.getEncryption();
        // encrypt.
      } else {
        resultSb.append("<li class=\"success\">PDF is NOT encrypted</li>\n");
      }


      PDDocumentInformation docInf = pdDoc.getDocumentInformation();

      LinkedHashMap<String,String> infoList = getInfo(docInf);
      
      Set<String> infoKeys = infoList.keySet();
      Iterator<String> ilIt = infoKeys.iterator();
      
      while(ilIt.hasNext()) {
        String key = ilIt.next();
        resultSb.append("<li>" + key + ": " + infoList.get(key) + "</li>\n");
        
      }
      
      LinkedHashMap<String,String> pdList = getInfo(pdDoc);

      Set<String> pdKeys = pdList.keySet();
      Iterator<String> pdIt = pdKeys.iterator();
      
      while(pdIt.hasNext()) {
        String key = pdIt.next();
        resultSb.append("<li>" + key + ": " + pdList.get(key) + "</li>\n");
        
      }

      resultSb.append("</ul>\n");
      result = resultSb.toString();
    
    } catch (IOException e) {
      e.printStackTrace();
    }    
    
    return result;
  }
  
  
  /**
   * Editing the information (metadata) provided by PDDocumentInformation Object.
   * Currently only editing of Title and Author is implemented. Modification Date
   * will set to the editing date in addition.
   * 
   * @param pdfFile
   * @param key
   * @param value
   * @return absolute path of a temp file created with edited data
   */
  public File editPDFInfo(File pdfFile, String key, String value) {
    
    try {
      PDDocument pdDoc = new PDDocument();
      pdDoc = Loader.loadPDF(pdfFile);
      PDDocumentInformation pdfInfo = pdDoc.getDocumentInformation();
      PDDocumentInformation editPdfInfo = setPdfInfoValue(pdfInfo, key, value);
      pdDoc.setDocumentInformation(editPdfInfo);
      
      File editPdf = File.createTempFile("edit_", ".pdf");
      pdDoc.save(editPdf);
            
      pdDoc.close();
      return editPdf;
      
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } 
    return null;

  }
  
  
  
  /**
   * Methods allows to set metadata within Pdf
   * @param docInf
   * @param key
   * @param value
   */
  private PDDocumentInformation setPdfInfoValue(PDDocumentInformation docInf, String key, String value) {
      // TODO: find a better solution for thisImplement  
      if(key.equals("Title")) {
        docInf.setTitle(value);
      }
      if(key.equals("Author")) {
        docInf.setAuthor(value);
      }
      
      docInf.setModificationDate(Calendar.getInstance());
      
      return docInf;
  }
  
  /**
   * Provides descriptive and technical metadata from PDF document 
   * @param docInf PDDocumentInformation (see PDFbox)
   * @return a sorted Map<k,v> with metadata accessed from PDF document information 
   */
  private LinkedHashMap<String,String> getInfo(PDDocumentInformation docInf) {
    LinkedHashMap<String,String> pdfInf = new LinkedHashMap<>();
    pdfInf.put("Title", docInf.getTitle());
    pdfInf.put("Author", docInf.getAuthor());
    pdfInf.put("Creator", docInf.getCreator());
    pdfInf.put("Producer", docInf.getProducer());
    
    if(docInf.getCreationDate() != null) {
      pdfInf.put("Creation Date", TimePrefix.setFormat(docInf.getCreationDate()));
    }
    
    if(docInf.getModificationDate() != null) {
      docInf.getModificationDate();
      pdfInf.put("Modification Date", TimePrefix.setFormat(docInf.getModificationDate()));
    }
    return pdfInf;
    
  }
  
  /**
   * Provides technical metadata from the PDF document
   * 
   * @param pdDoc PDDocument (see PDFbox)
   * @return a sorted Map<k,v> with technical information from the PDF document 
   */
  private LinkedHashMap<String,String> getInfo(PDDocument pdDoc) {
    LinkedHashMap<String,String> pdfDocInf = new LinkedHashMap<>();
    pdfDocInf.put("Pages", Integer.toString(pdDoc.getNumberOfPages()));

    return pdfDocInf;
    
  }
}
