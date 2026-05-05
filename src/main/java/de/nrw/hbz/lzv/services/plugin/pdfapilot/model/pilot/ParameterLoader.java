/**
 * 
 */
package de.nrw.hbz.lzv.services.plugin.pdfapilot.model.pilot;

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

  private static final Properties pdfaPilotProps = new Properties();

  public static void loadPdfaPilotProperties() {
    try (InputStream is = ParameterLoader.class.getResourceAsStream("/pdfaPilot.properties")) {

      if (is != null) {
        pdfaPilotProps.load(is);
      }

    } catch (Exception e) {
      throw new RuntimeException("Failed to load pdfaPilot.properties", e);
    }
  }

  static {
    loadPdfaPilotProperties();
  }

  public static String getProgramPath() {
    return pdfaPilotProps.getProperty("pdfapilot.path");
  }

  public static String getDefaultLevel() {
    return pdfaPilotProps.getProperty("pdfapilot.level");
  }

  public static List<String> getAnalyzerFlags() {
    List<String> flags = new ArrayList<>();

    if (Boolean.parseBoolean(pdfaPilotProps.getProperty("pdfapilot.quickpdfinfo"))) {
      flags.add("--quickpdfinfo");
    }

    return flags;
  }

  public static List<String> getCreatorFlags() {
    List<String> flags = new ArrayList<>();

    if (Boolean.parseBoolean(pdfaPilotProps.getProperty("pdfapilot.quick"))) {
      flags.add("--quick");
    }
    if (!Boolean.parseBoolean(pdfaPilotProps.getProperty("pdfapilot.quick"))
        && Boolean.parseBoolean(pdfaPilotProps.getProperty("pdfapilot.report"))) {
      if (pdfaPilotProps.getProperty("pdfapilot.report.type") != null
          && pdfaPilotProps.getProperty("pdfapilot.report.generate") != null
          && pdfaPilotProps.getProperty("pdfapilot.report.path") != null) {
        flags.add("--report=" + pdfaPilotProps.getProperty("pdfapilot.report.type") + ","
            + pdfaPilotProps.getProperty("pdfapilot.report.generate") + "," + "PATH="
            + pdfaPilotProps.getProperty("pdfapilot.report.path"));
      }
    }
    if (Boolean.parseBoolean(pdfaPilotProps.getProperty("pdfapilot.noprogress"))) {
      flags.add("--noprogress");
    }
    if (Boolean.parseBoolean(pdfaPilotProps.getProperty("pdfapilot.substitute"))) {
      flags.add("--substitute");
    }
    if (Boolean.parseBoolean(pdfaPilotProps.getProperty("pdfapilot.onlypdfa"))) {
      flags.add("--onlypdfa");
    }
    if (pdfaPilotProps.getProperty("pdfapilot.fontfolder") != null) {
      flags.add("--fontfolder=" + pdfaPilotProps.getProperty("pdfapilot.fontfolder"));
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

  public void setOperation(LinkedHashMap<String, String> operationalParameter) {

    Iterator<String> opIt = operationalParameter.keySet().iterator();

    while (opIt.hasNext()) {
      parameterBuffer.append(operationalParameter.get(opIt.next()));
    }
  }

}
