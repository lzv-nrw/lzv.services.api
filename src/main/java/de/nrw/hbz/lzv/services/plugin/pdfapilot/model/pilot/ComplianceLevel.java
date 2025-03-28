/**
 * 
 */
package de.nrw.hbz.lzv.services.plugin.pdfapilot.model.pilot;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;

/**
 * 
 */
public class ComplianceLevel {
  
  final static LinkedHashMap<String,String> complianceLevel = new LinkedHashMap<>();
  final static String _1A = "1a";
  final static String _1B = "1b";
  final static String _2A = "2a";
  final static String _2U = "2u";
  final static String _2B = "2b";
  final static String _3A = "3a";
  final static String _3U = "3u";
  final static String _3B = "3b";

  public ComplianceLevel() {
    setComplianceLevelSet();
  }
  
  private void setComplianceLevelSet() {

    complianceLevel.put("_1A", _1A);
    complianceLevel.put("_1B", _1B);
    complianceLevel.put("_2A", _2A);
    complianceLevel.put("_2U", _2U);
    complianceLevel.put("_2B", _2B);
    complianceLevel.put("_3A", _3A);
    complianceLevel.put("_3U", _3U);
    complianceLevel.put("_3B", _3B);
    
  }
  
  /**
   * Convenience method to return ComplianceLevel for PDF/A generation
   * @param key name of ComplianceLevel constant
   * @return String value of ComplianceLevel
   */
  public static String getComplianceLevel(String key) {
    String cl = complianceLevel.get(key);
    return cl;
  }
  
  


}
