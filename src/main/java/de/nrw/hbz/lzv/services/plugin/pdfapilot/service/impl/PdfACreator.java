/**
 * 
 */
package de.nrw.hbz.lzv.services.plugin.pdfapilot.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.nrw.hbz.lzv.services.model.pdf.model.Compliance;
import de.nrw.hbz.lzv.services.model.pdfa.result.PdfaPilotResult;

/**
 * 
 */
public class PdfACreator extends de.nrw.hbz.lzv.services.impl.PdfACreator{

  private static Logger log = LogManager.getLogger(PdfACreator.class);
  public final static String PLUGIN_NAME = "pdfapilot";

  @Override
  public PdfaPilotResult createPdfa(File file, String fileName, String flavour) {
    
    PdfaPilotResult pdfaRes = new PdfaPilotResult();
    pdfaRes.setLoadedFileName(fileName);
    
    String compliance = "2b";
    
    if(Compliance.labelExists(flavour)) {
      compliance = flavour;
    }

    File convertedFile = null;
    
    try {
      convertedFile = File.createTempFile(compliance + "_", file.getName());
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }    
        
    PilotRunner pRunner = new PilotRunner();    
    String executeString = " --quick --noprogress "
        + "--substitute --fontfolder=/opt/pdfapilot/fontfolder"
        + " --onlypdfa --level=" + compliance 
        + " --outputfile=" + convertedFile.getAbsolutePath() + " " + file.getAbsolutePath();
    pRunner.executePdfATool(executeString);
    
    pdfaRes.setExecuteString(executeString);
    
    String stout = pRunner.getStoutStr();
    if(stout == null) {
      stout = "Summary \t  run for test only";
    } 
    
    String errOut = pRunner.getErrStr();
    
    Stream<String> resultLines  = stout.lines();
    Iterator<String> rlIt = resultLines.iterator();
    
    while(rlIt.hasNext()) {
      String line = rlIt.next();
      if(line.startsWith("Fix")) {
        String[] split = line.split("\t");
        pdfaRes.addFixMessage(split[1]);
      }
      if(line.startsWith("Summary")){
        String[] split = line.split("\t");
        pdfaRes.addSummaryMessage(line.substring(split[0].length()));
      }
      if(line.startsWith("Output")){
        String[] split = line.split("\t");
        pdfaRes.setFileOutputLocation(split[1]);
      }
      
    }

      
      Stream<String> errLines  = stout.lines();
      Iterator<String> errIt = errLines.iterator();

      while(errIt.hasNext()) {
        String line = errIt.next();
        if(line.contains("Hit")){
        pdfaRes.addErrorMessage(line.replace("Hit PDFA", "Fehlerhinweis:"));
      }
      
      
      pdfaRes.setStout(stout);
      pdfaRes.setErrOut(errOut);
      
    }
    
    return pdfaRes;
  }

}
