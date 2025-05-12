/**
 * 
 */
package de.nrw.hbz.lzv.services.plugin.pdfapilot.service.impl;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.stream.Stream;

import de.nrw.hbz.lzv.services.model.json.impl.PdfInfo;
import de.nrw.hbz.lzv.services.model.json.model.PdfInfoModel;

/**
 * 
 */
public class PdfInfoProvider {
  
  private String stout = null;
  private PdfInfo pdfInfo = null;
  
  
  public PdfInfoProvider(String stout) {
    this.stout = stout;
  }
  

  public void setPdfInfo() {
    
    Stream<String> resultLines = stout.lines();
    Iterator<String> rlIt = resultLines.iterator();
    
    while(rlIt.hasNext()) {
      String line = rlIt.next();
      if(line.startsWith("Info")) {
        String[] split = line.split("\t");
        
        LinkedHashMap<String,String> infoLabels = PdfInfoModel.getInfoLabel();
        if(infoLabels.containsKey(split[1])) {
          String key = infoLabels.get(split[1]);
          pdfInfo.setInfoElement(key, split[2]);
          
        }
      }
    }
  }

  /**
   * Get a plug-in independent object representing PDF Information
   * @return PdfInfo
   */
  public PdfInfo getPdfInfo() {
    return pdfInfo;
  }
}
