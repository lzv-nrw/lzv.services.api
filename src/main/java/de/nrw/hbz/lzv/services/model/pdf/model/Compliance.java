/**
 * 
 */
package de.nrw.hbz.lzv.services.model.pdf.model;

import java.util.LinkedHashMap;

/**
 * 
 */
public class Compliance {
        
    static LinkedHashMap<String,String> complianceFormat = setComplianceFormat();
    static LinkedHashMap<String,String> complianceLabel = setComplianceLabel();

    final static String pronomUrl = "https://www.nationalarchives.gov.uk/pronom/";
    
    final static String _1A = "1a";
    final static String _1B = "1b";
    final static String _2A = "2a";
    final static String _2U = "2u";
    final static String _2B = "2b";
    final static String _3A = "3a";
    final static String _3U = "3u";
    final static String _3B = "3b";
    final static String _4 = "4";
    final static String _4E = "4e";
    final static String _4F = "4f";
    
       
    private static LinkedHashMap<String,String> setComplianceLabel() {
      LinkedHashMap<String,String> complianceLabel = new LinkedHashMap<>(); 
      complianceLabel.put("1A", _1A);
      complianceLabel.put("1B", _1B);
      complianceLabel.put("2A", _2A);
      complianceLabel.put("2U", _2U);
      complianceLabel.put("2B", _2B);
      complianceLabel.put("3A", _3A);
      complianceLabel.put("3U", _3U);
      complianceLabel.put("3B", _3B);
      complianceLabel.put("4", _4);
      complianceLabel.put("4E", _4E);
      complianceLabel.put("4F", _4F);
      return complianceLabel;
    }

    private static LinkedHashMap<String,String> setComplianceFormat() {

      LinkedHashMap<String,String> complianceFormat = new LinkedHashMap<>(); 
      complianceFormat.put( _1A, "fmt/95");
      complianceFormat.put( _1B, "fmt/354");
      complianceFormat.put( _2A, "fmt/476");
      complianceFormat.put( _2U, "fmt/478");
      complianceFormat.put( _2B, "fmt/477");
      complianceFormat.put( _3A, "fmt/479");
      complianceFormat.put( _3U, "fmt/481");
      complianceFormat.put( _3B, "fmt/480");
      complianceFormat.put( _4,  "fmt/1910");
      complianceFormat.put( _4E, "fmt/1911");
      complianceFormat.put( _4F, "fmt/1912");
      return complianceFormat;
    }
    
    /**
     * check for supported version of PDF 
     * @param label
     * @return true if prefLabel exists and is supported 
     * by lzv.service.api
     */
    public static boolean labelExists(String label) {
      boolean isLabel = false;
      if(complianceLabel.containsValue(label)){
        isLabel = true;
      }
      return isLabel;
    }

    
    /**
     * Convenience method to return Pronom Format for PDF/A flavour
     * @param key name of ComplianceFormat constant
     * @return String value of ComplianceFormat
     */
    public static String getComplianceFormat(String key) {
      String cl = complianceFormat.get(key);
      return cl;
    }

    /**
     * Convenience method to return PDF/A flavour label
     * @param key name of ComplianceLabel constant
     * @return String value of ComplianceLabel
     */
    public static String getComplianceLabel(String key) {
      String cl = complianceLabel.get(key);
      return cl;
    }

    /**
     * Convenience method to return Pronom Url for PDF/A flavour
     * @param key name of ComplianceFormat constant
     * @return String value of Pronom Format URL as String
     */
    public static String getComplianceUrl(String key) {
      String clUrl = pronomUrl + complianceFormat.get(key);
      return clUrl;
    }


}
