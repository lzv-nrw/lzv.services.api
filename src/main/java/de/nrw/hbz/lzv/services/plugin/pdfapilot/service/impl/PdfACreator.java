/**
 * 
 */
package de.nrw.hbz.lzv.services.plugin.pdfapilot.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;
import java.util.LinkedHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import de.nrw.hbz.lzv.services.model.pdf.model.Compliance;
import de.nrw.hbz.lzv.services.model.pdfa.result.PdfaPilotResult;
import de.nrw.hbz.lzv.services.plugin.pdfapilot.model.pilot.ParameterLoader;
import de.nrw.hbz.lzv.services.template.HtmlTemplate;

/**
 * 
 */
public class PdfACreator extends de.nrw.hbz.lzv.services.impl.PdfACreator {

  private static Logger log = LogManager.getLogger(PdfACreator.class);
  public final static String PLUGIN_NAME = "pdfapilot";
  private static final ScheduledExecutorService DELETE_EXECUTOR = Executors.newSingleThreadScheduledExecutor();

  protected PdfaPilotResult pdfaRes = null;

  @Override
  public PdfaPilotResult createPdfa(File file, String fileName, String flavour) {

    pdfaRes = new PdfaPilotResult();
    pdfaRes.setLoadedFileName(fileName);

    String compliance = ParameterLoader.getDefaultLevel();

    if (Compliance.labelExists(flavour)) {
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

    if (flavour.equals("auto")) {
      log.info("PdfARunner calls pdfaPilot with flavour " + flavour);
      LinkedHashMap<String, String> labels = Compliance.getAllComplianceLabels();

      int index = 0;
      for (String level : labels.keySet()) {

        StringBuilder cmd = new StringBuilder();

        for (String flag : ParameterLoader.getCreatorFlags()) {
          cmd.append(" ").append(flag);
        }

        cmd.append(" --level=").append(level);
        cmd.append(" --outputfile=").append(convertedFile.getAbsolutePath());
        cmd.append(" ").append(file.getAbsolutePath());

        String executeString = cmd.toString();

        log.info("PdfARunner calls pdfaPilot with" + executeString);

        pRunner.executePdfATool(executeString);

        pdfaRes.setExecuteString(executeString);

        String stout = pRunner.getStoutStr();
        if (stout == null) {
          stout = "Summary \t  run for test only";
        }

        String errOut = pRunner.getErrStr();
        Stream<String> errLines = stout.lines();
        Iterator<String> errIt = errLines.iterator();

        if (!stout.contains("Hit") || index == (labels.size() - 1)) {
          Stream<String> resultLines = stout.lines();
          Iterator<String> rlIt = resultLines.iterator();
          if(index == (labels.size() - 1)) {
            pdfaRes.addSummaryMessage("Alle Flavours ausprobiert");
          }
          while (rlIt.hasNext()) {
            String line = rlIt.next();
            if (line.startsWith("Fix")) {
              String[] split = line.split("\\t");
              pdfaRes.addFixMessage(split[1]);
            }
            if (line.startsWith("Summary")) {
              String[] split = line.split("\\t", 2);
              if (split.length > 1) {
                String message = split[1];

                if (message.contains("Corrections")) {
                  message = message.replace("Corrections", "Korrekturen:");
                }

                if (message.contains("Errors")) {
                  message = message.replace("Errors", "Fehler:");
                }

                if (message.contains("Warnings")) {
                  message = message.replace("Warnings", "Warnungen:");
                }

                if (message.contains("Infos")) {
                  message = message.replace("Infos", "Informationen:");
                }
                pdfaRes.addSummaryMessage(message);
              }
            }
            if (line.startsWith("Output")) {
              String[] split = line.split("\\t");
              pdfaRes.setFileOutputLocation(split[1]);
            }
            if (line.startsWith("Report")) {
              String[] split = line.split("\\t");
              pdfaRes.setReportOutputLocation(split[1]);
            }
          }

          while (errIt.hasNext()) {
            String line = errIt.next();
            if (line.contains("Hit")) {
              pdfaRes.addErrorMessage(line.replace("Hit\\s+PDFA", ""));
            }
            if (line.contains("Error") && !line.contains("Errors")) {
              pdfaRes.addErrorMessage(line.replaceAll("Error\\s+", "").replaceAll("/.*\\.pdf.*", ""));
            }
            pdfaRes.setStout(stout);
            pdfaRes.setErrOut(errOut);
          }
          break;
        }
        index++;
      }
      return pdfaRes;
    }

    StringBuilder cmd = new StringBuilder();

    for (String flag : ParameterLoader.getCreatorFlags()) {
      cmd.append(" ").append(flag);
    }

    cmd.append(" --level=").append(compliance);

    cmd.append(" --outputfile=").append(convertedFile.getAbsolutePath());
    cmd.append(" ").append(file.getAbsolutePath());

    String executeString = cmd.toString();

    log.info("PdfARunner calls pdfaPilot with" + executeString);

    pRunner.executePdfATool(executeString);

    pdfaRes.setExecuteString(executeString);

    String stout = pRunner.getStoutStr();
    if (stout == null) {
      stout = "Summary \t  run for test only";
    }

    String errOut = pRunner.getErrStr();

    Stream<String> resultLines = stout.lines();
    Iterator<String> rlIt = resultLines.iterator();

    while (rlIt.hasNext()) {
      String line = rlIt.next();
      if (line.startsWith("Fix") && !line.contains("FixFailure")) {
        String[] split = line.split("\\t");
        pdfaRes.addFixMessage(split[1]);
      }
      if (line.startsWith("Summary")) {
        String[] split = line.split("\\t", 2);
        if (split.length > 1) {
          String message = split[1];

          if (message.contains("Corrections")) {
            message = message.replace("Corrections", "Korrekturen:");
          }

          if (message.contains("Errors")) {
            message = message.replace("Errors", "Fehler:");
          }

          if (message.contains("Warnings")) {
            message = message.replace("Warnings", "Warnungen:");
          }

          if (message.contains("Infos")) {
            message = message.replace("Infos", "Informationen:");
          }
          pdfaRes.addSummaryMessage(message);
        }
      }
      if (line.startsWith("Output")) {
        String[] split = line.split("\\t");
        pdfaRes.setFileOutputLocation(split[1]);
      }
      if (line.startsWith("Report")) {
        String[] split = line.split("\\t");
        pdfaRes.setReportOutputLocation(split[1]);
      }
    }

    Stream<String> errLines = stout.lines();
    Iterator<String> errIt = errLines.iterator();

    while (errIt.hasNext()) {
      String line = errIt.next();
      if (line.contains("Hit")) {
        pdfaRes.addErrorMessage(line.replaceAll("Hit\\s+PDFA", ""));
      }
      if (line.contains("Error") && !line.contains("Errors")) {
        pdfaRes.addErrorMessage(line.replaceAll("Error\\s+", "").replaceAll("/.*\\.pdf.*", ""));
      }
      pdfaRes.setStout(stout);
      pdfaRes.setErrOut(errOut);
    }
    convertedFile.delete();

    int fileDeleteTime = ParameterLoader.getFileDeleteTime();
    DELETE_EXECUTOR.schedule(() -> {
      try {

        File outputFile = new File(pdfaRes.getFileOutputLocation());

        if (outputFile.exists()) {
          if (!outputFile.delete()) {
            log.warn("Cannot delete temop file " + pdfaRes.getFileOutputLocation());
          }
        }
      } catch (Exception e) {
        log.error("Error deleting the temp file " + pdfaRes.getFileOutputLocation(), e);
      }
    }, fileDeleteTime, TimeUnit.MINUTES);

    return pdfaRes;
  }

  @Override
  public String getHtml() {
    resultBuffer.append(HtmlTemplate.getHtmlHead());

    resultBuffer.append("<h1>Ergebnis der PDF/A-Erzeugung mit pdfaPilot</h1>\n");
    resultBuffer.append("<h2>Datei zur Konvertierung: " + pdfaRes.getLoadedFileName() + "</h2>\n");
    if (pdfaRes != null) {

      resultBuffer.append("<h3>Bearbeitungsschritte:</h3>\n<ul>\n");
      for (int i = 0; i < pdfaRes.getFixList().size(); i++) {
        resultBuffer.append("<li>").append(pdfaRes.getFixList().get(i)).append("</li>");
      }
      resultBuffer.append("</ul>\n");

      resultBuffer.append("<h3>Zusammenfassung:</h3>\n<ul>\n");
      for (int i = 0; i < pdfaRes.getSummaryList().size(); i++) {
        resultBuffer.append("<li>").append(pdfaRes.getSummaryList().get(i)).append("</li>");
      }
      resultBuffer.append("</ul>\n");

      if (pdfaRes.getErrorList() != null && !pdfaRes.getErrorList().isEmpty()) {
        resultBuffer.append("<h3>Fehlerhinweise:</h3>\n<ul>\n");
        for (int i = 0; i < pdfaRes.getErrorList().size(); i++) {
          resultBuffer.append("<li>").append(pdfaRes.getErrorList().get(i)).append("</li>");
        }
        resultBuffer.append("</ul>\n");
      }

      if (pdfaRes.getReportOutputLocation() != null) {
        resultBuffer.append("<p><i class=\"fa-regular fa-file-lines\"></i><a href=\"/lzv-api/download?fileName="
            + pdfaRes.getReportOutputLocation() + "&origFileName=report_"
            + pdfaRes.getLoadedFileName().replace(".pdf",
                "_pdf." + pdfaRes.getReportOutputLocation()
                    .substring(pdfaRes.getReportOutputLocation().lastIndexOf('.') + 1))
            + "\">Report herunterladen</a></p>");
      }

      if (pdfaRes.getFileOutputLocation() != null) {
        resultBuffer.append("<p><i class=\"fa-solid fa-download\"></i><a href=\"/lzv-api/download?fileName="
            + pdfaRes.getFileOutputLocation() + "&origFileName=" + pdfaRes.getLoadedFileName()
            + "\">PDF/A Datei herunterladen</a> (Link " + ParameterLoader.getFileDeleteTime() + " Minuten gültig)</p>");
      }
    }

    resultBuffer.append(
        "<p><i class=\"fa-solid fa-repeat\"></i><a href=\"/lzv-jsp/pdfapilot/createpdfa\">Weitere PDF Umwandlung</a></p>");

    resultBuffer.append(HtmlTemplate.getHtmlFoot());

    return resultBuffer.toString();
  }

  @Override
  public String getJson() {
    if (pdfaRes == null) {
      return "{}";
    }

    JSONObject resultJson = new org.json.JSONObject();

    resultJson.put("file", pdfaRes.getLoadedFileName());

    resultJson.put("fixList", pdfaRes.getFixList());

    JSONObject summaryObj = new JSONObject();
    for (String entry : pdfaRes.getSummaryList()) {
      String[] parts = entry.split("\\t");
      if (parts.length == 2) {
        summaryObj.put(parts[0], Integer.parseInt(parts[1]));
      }
    }
    resultJson.put("summaryList", summaryObj);

    if (pdfaRes.getErrorList() != null && !pdfaRes.getErrorList().isEmpty()) {
      List<String> cleanedErrorsList = new ArrayList<>();
      for (String error : pdfaRes.getErrorList()) {
        String cleanedError = error.replace("\t", " ");
        cleanedErrorsList.add(cleanedError);
      }
      resultJson.put("errorList", cleanedErrorsList);
    }

    if (pdfaRes.getReportOutputLocation() != null) {
      resultJson.put("reportOutputLocation", "/lzv-api/download?fileName=" + pdfaRes.getReportOutputLocation()
          + "&origFileName=report_" + pdfaRes.getLoadedFileName().replace(".pdf", "_pdf."
              + pdfaRes.getReportOutputLocation().substring(pdfaRes.getReportOutputLocation().lastIndexOf('.') + 1)));
    }

    if (pdfaRes.getFileOutputLocation() != null) {
      resultJson.put("fileOutputLocation", "/lzv-api/download?fileName=" + pdfaRes.getFileOutputLocation()
          + "&origFileName=" + pdfaRes.getLoadedFileName());
    }

    return resultJson.toString(3);
  }

}
