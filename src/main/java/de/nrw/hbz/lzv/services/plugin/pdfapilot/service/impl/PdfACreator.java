/**
 * 
 */
package de.nrw.hbz.lzv.services.plugin.pdfapilot.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
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
import de.nrw.hbz.lzv.services.util.file.FileUtil;

/**
 * 
 */
public class PdfACreator extends de.nrw.hbz.lzv.services.impl.PdfACreator {

  private static Logger log = LogManager.getLogger(PdfACreator.class);
  public final static String PLUGIN_NAME = "pdfapilot";

  protected PdfaPilotResult pdfaRes = null;

  @Override
  public PdfaPilotResult createPdfa(File file, String fileName, String flavour) {

    pdfaRes = new PdfaPilotResult();
    pdfaRes.setLoadedFileName(fileName);

    // Prepare command for ProcessBuilder
    ArrayList<String> cmdList = new ArrayList<>();
    cmdList.add(ParameterLoader.getProgramPath());

    for (String flag : ParameterLoader.getCreatorFlags()) {
      cmdList.add(flag);
    }

    String compliance = ParameterLoader.getDefaultLevel();
    File convertedFile = null;
    try {
      convertedFile = File.createTempFile(compliance + "_", file.getName());
    } catch (IOException e) {
      log.error("Unable to create temp file", e.getMessage());
    }
    cmdList.add("--outputfile=" + convertedFile.getAbsolutePath());
    cmdList.add(file.getAbsolutePath());

    ArrayList<String> flavourList = null;
    if (Compliance.labelExists(flavour)) {
      flavourList = new ArrayList<>();
      compliance = flavour;
      flavourList.add("--level=" + compliance);
    } else {
      flavourList = new ArrayList<>();
      Set<String> keys = Compliance.getAllComplianceLabels().keySet();
      for (String key : keys) {
        flavourList.add("--level=" + key);
      }
    }

    PilotRunner pRunner = new PilotRunner();

    for(String fl : flavourList) {
      cmdList.add(fl);
      pRunner = new PilotRunner();
      pRunner.executePdfATool(cmdList);
      cmdList.removeLast();
      if (pRunner.getExitStateStr().equals("0")) {
        break;
      }
    }

    String stout = pRunner.getStoutStr();
    if (stout == null) {
      stout = "Summary \t  run for test only";
    }

    String errOut = pRunner.getErrStr();
    Stream<String> errLines = stout.lines();
    Iterator<String> errIt = errLines.iterator();

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

          message = unifiedMessage(message);
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

    errLines = stout.lines();
    errIt = errLines.iterator();

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

    FileUtil.scheduledDelete(pdfaRes.getFileOutputLocation());

    return pdfaRes;
  }

  /**
   * @param Message
   * @return String with replacements
   */
  private String unifiedMessage(String Message) {
    String message = Message;
    message = message.replace("Corrections", "Korrekturen:").replace("Errors", "Fehler:")
        .replace("Warnings", "Warnungen:").replace("Infos", "Informationen:");

    return message;
  }

  /**
   * method generates HTML Output of the PDF/A creation results
   */
  @Override
  public String getHtml() {
    resultBuffer.append(HtmlTemplate.getHtmlHead());

    resultBuffer.append("<h1>Ergebnis der PDF/A-Erzeugung mit pdfaPilot</h1>\n");
    resultBuffer.append("<h2>Datei zur Konvertierung: " + pdfaRes.getLoadedFileName() + "</h2>\n");
    if (pdfaRes != null) {

      if (pdfaRes.getFileOutputLocation() != null) {
        resultBuffer.append(
            "<h3 style=\"color: darkgreen;)\">Konvertierung erfolgreich <i class=\"fa-solid fa-check\"></i></h3>");
      } else {
        resultBuffer
            .append("<h3 style=\"color: red;)\">Konvertierung fehlgeschlagen <i class=\"fa-solid fa-xmark\"></i></h3>");
      }
      resultBuffer.append("<h3>Durchgeführte Maßnahmen:</h3>\n<ul>\n");
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

  /**
   * method generates JSON Output of the the the PDF/A creation results
   */
  @Override
  public String getJson() {
    if (pdfaRes == null) {
      return "{}";
    }

    JSONObject resultJson = new JSONObject();

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
