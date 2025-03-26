/**
 * 
 */
package de.nrw.hbz.lzv.services.plugin.callas.model.pilot;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;

/**
 * 
 */
public class ReportLanguage {
  
  final static LinkedHashMap<String,String> language = new LinkedHashMap<>();
  final static String DE = "DE";
  final static String EN = "EN";
  final static String FR = "FR";
  final static String ES = "ES";
  final static String IT = "IT";
  final static String JA = "JA";

  public ReportLanguage() {
    setLanguageMap();
  }
  
  private void setLanguageMap() {

    language.put("DE", DE);
    language.put("EN", EN);
    language.put("ES", ES);
    language.put("FR", FR);
    language.put("IT", IT);
    language.put("JA", JA);
  }
  
  /**
   * Convenience method to return ComplianceLevel for PDF/A generation
   * @param key name of ComplianceLevel constant
   * @return String value of ComplianceLevel
   */
  public static String getLanguage(String key) {
    String lang = language.get(key);
    return lang;
  }
  
  


}
