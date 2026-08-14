/**
 * 
 */
package de.nrw.hbz.lzv.services.plugin.pdfapilot.model.pilot;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Properties;

/**
 * 
 */
public class ParameterLoader {

  private static final String PROPERTIES_FILE = "/pdfaPilot.properties";

  private static final Properties pdfaPilotProps = new Properties();

  public static void loadPdfaPilotProperties() {
    try (InputStream is = ParameterLoader.class.getResourceAsStream(PROPERTIES_FILE)) {

      if (is != null) {
        pdfaPilotProps.load(is);
      }

    } catch (IOException e) {
      throw new IllegalStateException("Failed to load pdfaPilot.properties", e);
    }
  }

  static {
    loadPdfaPilotProperties();
  }

  public static String getProgramPath() {
    return pdfaPilotProps.getProperty("pdfapilot.path");
  }

  public static int getFileDeleteTime() {
    return Integer.parseInt(pdfaPilotProps.getProperty("pdfapilot.fileDeleteTime"));
  }

  public static String getDefaultLevel() {
    return pdfaPilotProps.getProperty("pdfapilot.level");
  }

  public static List<String> getAnalyzerFlags() {

    String analyzer = "pdfapilot.analyzer.";
    List<String> flags = new ArrayList<>();

    if (Boolean.parseBoolean(pdfaPilotProps.getProperty(analyzer + "quickpdfinfo"))) {
      flags.add("--quickpdfinfo");
    }

    return flags;
  }

  public static List<String> getCreatorFlags() {

    String creator = "pdfapilot.creator.";
    String report = "pdfapilot.report.";
    
    List<String> flags = new ArrayList<>();

    if (Boolean.parseBoolean(pdfaPilotProps.getProperty(creator + "quick"))) {
      flags.add("--quick");
    }
    if (!Boolean.parseBoolean(pdfaPilotProps.getProperty(creator + "quick"))
        && Boolean.parseBoolean(pdfaPilotProps.getProperty(report))) {
      if (pdfaPilotProps.getProperty(report + "type") != null
          && pdfaPilotProps.getProperty(report + "generate") != null
          && pdfaPilotProps.getProperty(report + "path") != null) {
        flags.add("--report=" + pdfaPilotProps.getProperty(report + "type") + ","
            + pdfaPilotProps.getProperty(report + "generate") + "," + "PATH="
            + pdfaPilotProps.getProperty(report + "path"));
      }
    }
    if (Boolean.parseBoolean(pdfaPilotProps.getProperty(creator + "noprogress"))) {
      flags.add("--noprogress");
    }
    if (Boolean.parseBoolean(pdfaPilotProps.getProperty(creator + "substitute"))) {
      flags.add("--substitute");
    }
    if (Boolean.parseBoolean(pdfaPilotProps.getProperty(creator + "onlypdfa"))) {
      flags.add("--onlypdfa");
    }
    if (pdfaPilotProps.getProperty(creator + "fontfolder") != null) {
      flags.add("--fontfolder=" + pdfaPilotProps.getProperty(creator + "fontfolder"));
    }
    if (pdfaPilotProps.getProperty(creator + "language") != null) {
      flags.add("--language=" + pdfaPilotProps.getProperty(creator + "language"));
    }

    return flags;
  }

  private String clKey = null;
  private String lang = null;
  private StringBuffer parameterBuffer = new StringBuffer();

  private String createParameterString() {
    parameterBuffer.append(ReportLanguage.getLanguage(lang));

    return null;
  }

  public void setComplianceLevel(String key) {
    clKey = " --level=" + key;
  }

  public void setLanguage(String key) {
    lang = " --language=" + key;
  }

  /**
   * method sets operational parameters provides as HashMap
   * @param operationalParameter
   */
  public void setOperation(LinkedHashMap<String, String> operationalParameter) {

    Iterator<String> opIt = operationalParameter.keySet().iterator();

    while (opIt.hasNext()) {
      parameterBuffer.append(operationalParameter.get(opIt.next()));
    }
  }
  
  /**
   * method return any parameter value from pdfaPilot.properties
   * @param key
   * @return parameter value
   */
  public static String getParameter(String key) {
    return pdfaPilotProps.getProperty(key);
  }

}
