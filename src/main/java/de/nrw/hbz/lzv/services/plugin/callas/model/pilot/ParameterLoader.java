/**
 * 
 */
package de.nrw.hbz.lzv.services.plugin.callas.model.pilot;

import java.util.Iterator;
import java.util.LinkedHashMap;

/**
 * 
 */
public class ParameterLoader {

  private String clKey = null;
  private String lang = null;
  private StringBuffer parameterBuffer = new StringBuffer();
  
  
  
  
  private String createParameterString() {
    parameterBuffer.append(ComplianceLevel.getComplianceLevel(clKey));
    parameterBuffer.append(ReportLanguage.getLanguage(lang));
    
    return null;
  }
  
  public void setComplianceLevel(String key) {
    clKey = " --level=" + key;
  }

  public void setLanguage(String key) {
    lang = " --language=" + key;
  }

  public void setOperation(LinkedHashMap<String,String> operationalParameter) {
    
    Iterator<String> opIt = operationalParameter.keySet().iterator();
    
    while(opIt.hasNext()) {
      parameterBuffer.append(operationalParameter.get(opIt.next()));
    }
  }
  
  
  
}
