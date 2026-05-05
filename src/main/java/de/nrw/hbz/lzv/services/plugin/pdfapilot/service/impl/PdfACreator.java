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
      if (line.startsWith("Fix")) {
        String[] split = line.split("\\t");
        pdfaRes.addFixMessage(split[1]);
      }
      if (line.startsWith("Summary")) {
        String[] split = line.split("\\t", 2);
        if (split.length > 1) {
          pdfaRes.addSummaryMessage(split[1]);
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
        pdfaRes.addErrorMessage(line.replace("Hit PDFA", "Fehlerhinweis:"));
      }

      pdfaRes.setStout(stout);
      pdfaRes.setErrOut(errOut);

    }

    return pdfaRes;
  }

  @Override
  public String getHtml() {
    resultBuffer.append(HtmlTemplate.getHtmlHead());

    resultBuffer.append("<h1>Ergebnis der PDF/A-Erzeugung</h1>\n");

    if (pdfaRes != null) {

      resultBuffer.append("<p>Operations:</p>\n<ul>\n");
      for (int i = 0; i < pdfaRes.getFixList().size(); i++) {
        resultBuffer.append("<li>").append(pdfaRes.getFixList().get(i)).append("</li>");
      }
      resultBuffer.append("</ul>\n");

      resultBuffer.append("<p>Summary:</p>\n<ul>\n");
      for (int i = 0; i < pdfaRes.getSummaryList().size(); i++) {
        resultBuffer.append("<li>").append(pdfaRes.getSummaryList().get(i)).append("</li>");
      }
      resultBuffer.append("</ul>\n");

      if (pdfaRes.getErrorList() != null && !pdfaRes.getErrorList().isEmpty()) {
        resultBuffer.append("<p>Errors:</p>\n<ul>\n");
        for (int i = 0; i < pdfaRes.getErrorList().size(); i++) {
          resultBuffer.append("<li>").append(pdfaRes.getErrorList().get(i)).append("</li>");
        }
        resultBuffer.append("</ul>\n");
      }

      if (pdfaRes.getReportOutputLocation() != null) {
        resultBuffer.append("<p class='error'><a href=\"/lzv-api/download?fileName=" + pdfaRes.getReportOutputLocation()
            + "&origFileName=report_"
            + pdfaRes.getLoadedFileName().replace(".pdf",
                "_pdf." + pdfaRes.getReportOutputLocation()
                    .substring(pdfaRes.getReportOutputLocation().lastIndexOf('.') + 1))
            + "\">Report herunterladen</a></p>");
      }

      if (pdfaRes.getFileOutputLocation() != null) {
        resultBuffer.append("<p class='error'><a href=\"/lzv-api/download?fileName=" + pdfaRes.getFileOutputLocation()
            + "&origFileName=" + pdfaRes.getLoadedFileName() + "\">PDF/A Datei herunterladen</a></p>");
      }
    }

    resultBuffer.append("<p><a href=\"/lzv-jsp/pdfapilot/createpdfa\">Weiteres PDF umwandeln</a></p>");

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
