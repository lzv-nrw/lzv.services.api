/**
 * 
 */
package de.nrw.hbz.lzv.services.model.pdf.model;

import java.util.LinkedHashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 
 */
public class Version {

    private static Logger logger = LogManager.getLogger(Version.class);

    static LinkedHashMap<String,String> versionFormat = setVersionFormat();
    static LinkedHashMap<String,String> versionLabel = setVersionLabel();
    static LinkedHashMap<String,Boolean> pdfaApplicable = setPdfaApplicable();

    final static String pronomUrl = "https://www.nationalarchives.gov.uk/pronom/";
    
    final static String _1_0 = "1.0";
    final static String _1_1 = "1.1";
    final static String _1_2 = "1.2";
    final static String _1_3 = "1.3";
    final static String _1_4 = "1.4";
    final static String _1_5 = "1.5";
    final static String _1_6 = "1.6";
    final static String _1_7 = "1.7";
    final static String _2 = "2";
       
    public Version() {
      setVersionLabel();
      setVersionFormat();
      setPdfaApplicable();
    }
    
    private static LinkedHashMap<String,String> setVersionLabel() {
      
      logger.info("hier");
      LinkedHashMap<String,String> versionLabel = new LinkedHashMap<>();
      versionLabel.put("1.0", _1_0);
      versionLabel.put("1.1", _1_1);
      versionLabel.put("1.2", _1_2);
      versionLabel.put("1.3", _1_3);
      versionLabel.put("1.4", _1_4);
      versionLabel.put("1.5", _1_5);
      versionLabel.put("1.6", _1_6);
      versionLabel.put("1.7", _1_7);
      versionLabel.put("2", _2);
      logger.info(versionLabel.get(_1_0));
      return versionLabel; 
    }

    private static LinkedHashMap<String,String> setVersionFormat() {
      LinkedHashMap<String,String> versionFormat = new LinkedHashMap<>();
      versionFormat.put("1.0", "fmt/14");
      versionFormat.put("1.1", "fmt/15");
      versionFormat.put("1.2", "fmt/16");
      versionFormat.put("1.3", "fmt/17");
      versionFormat.put("1.4", "fmt/18");
      versionFormat.put("1.5", "fmt/19");
      versionFormat.put("1.6", "fmt/20");
      versionFormat.put("1.7", "fmt/176");
      versionFormat.put("2.0", "fmt/1129");
      return versionFormat;
      
    }
    
    /**
     * check if version is applicable to PDF/A
     * the versions 1.4, 1.7 and 2.0 are basic formats for PDF/A only
     */
    private static LinkedHashMap<String,Boolean> setPdfaApplicable() {

      LinkedHashMap<String,Boolean> pdfaApplicable = new LinkedHashMap<>();
      pdfaApplicable.put("1.0", false);
      pdfaApplicable.put("1.1", false);
      pdfaApplicable.put("1.2", false);
      pdfaApplicable.put("1.3", false);
      pdfaApplicable.put("1.4", true);
      pdfaApplicable.put("1.5", false);
      pdfaApplicable.put("1.6", false);
      pdfaApplicable.put("1.7", true);
      pdfaApplicable.put("2.0", true);
      return pdfaApplicable;
      
    }

    /**
     * check for supported version of PDF 
     * @param label
     * @return true if prefLabel exists and is supported 
     * by lzv.service.api
     */
    public static boolean labelExists(String label) {
      boolean isLabel = false;
      if(versionLabel.containsKey(label)){
        isLabel = true;
      }
      return isLabel;
    }
    
    /**
     * Convenience method to return Pronom Format for PDF version
     * @param key name of VersionFormat constant
     * @return String value of VersionFormat
     */
    public static Boolean getVersionIsPdfaApplicable(String key) {
      Boolean ap = false;
      ap = pdfaApplicable.get(key);
      return ap;
    }

    /**
     * Convenience method to return Pronom Format for PDF version
     * @param key name of VersionFormat constant
     * @return String value of VersionFormat
     */
    public static String getVersionFormat(String key) {
      logger.info(key + "; " + versionFormat.size());
      String cl = versionFormat.get(key);
      return cl;
    }

    /**
     * Convenience method to return PDF version label
     * @param key name of VersionLabel constant
     * @return String value of VersionLabel
     */
    public static String getVersionLabel(String key) {
      String cl = versionLabel.get(key);
      return cl;
    }

    /**
     * Convenience method to return Pronom Url for PDF version
     * @param key name of VersionFormat constant
     * @return String value of Pronom Format URL as String
     */
    public static String getVersionUrl(String key) {
      String clUrl = pronomUrl + versionFormat.get(key);
      return clUrl;
    }


}
