/**
 * 
 */
package de.nrw.hbz.lzv.services.plugin.pdfapilot.service.impl;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Stream;

import org.json.JSONObject;

import de.nrw.hbz.lzv.services.model.json.impl.PdfACompliance;
import de.nrw.hbz.lzv.services.model.json.impl.PdfInfo;
import de.nrw.hbz.lzv.services.model.pdf.model.Compliance;
import de.nrw.hbz.lzv.services.model.pdf.model.Version;
import de.nrw.hbz.lzv.services.template.HtmlTemplate;

/**
 * 
 */
public class Analyzer extends de.nrw.hbz.lzv.services.impl.Analyzer{

  public final static String PLUGIN_NAME = "pdfapilot";

  @Override
  public void analyze(File file, String fileName) {
    
    Map<String,Object> pdfMd = new HashMap<>();
    Map<String,String> versionMap = new HashMap<>();
    Map<String,String> complianceMap = new HashMap<>();

    PilotRunner pRunner = new PilotRunner();
    
    pRunner.executePdfATool(" --quickpdfinfo " + file.getAbsolutePath() );
    
    String stout = pRunner.getStoutStr();
    
    pdfInfo = getPdfInfo(stout);
    
    pdfACompl = new PdfACompliance();
    
    
    
    Stream<String> resultLines  = stout.lines();
    Iterator<String> rlIt = resultLines.iterator();
    
    
    
    while(rlIt.hasNext()) {
      String line = rlIt.next();
      if(line.startsWith("Info")) {
        String[] split = line.split("\t");
        pdfMd.put(split[1], split[2]);
      }
      
    }
    
    String prefLabel = (String) pdfMd.get("Version");
    if(Version.labelExists(prefLabel)){
      versionMap.put("prefLabel", prefLabel);
      versionMap.put("@id", Version.getVersionUrl(prefLabel));
      pdfMd.put("Version", versionMap);

    }
    
    prefLabel = (String) pdfMd.get("PDFA");
    String stripPrefLabel = prefLabel.substring(6,8);
    
    if(Compliance.labelExists(stripPrefLabel)){
      complianceMap.put("prefLabel", stripPrefLabel);
      complianceMap.put("@id", Compliance.getComplianceUrl(stripPrefLabel));
    }
    
    pdfMd.put("file", fileName);
    pdfMd.put("PDF/A compliance", complianceMap);

    // return pdfMd.toString();
  }
  
  /**
   * get the information stored in the PDF information part  
   * @return
   */
  private PdfInfo getPdfInfo(String stout){
    PdfInfoProvider infoProvider = new PdfInfoProvider(stout); 
    return infoProvider.getPdfInfo();
  }


  @Override
  public String getHtml() {
    resultBuffer.append(HtmlTemplate.getHtmlHead());

    resultBuffer.append("<h1>Ergebnis der Prüfung</h1>\n");
    resultBuffer.append("<p>" + fileName + "</p>");
    resultBuffer.append(pdfInfo.toHtml());
    resultBuffer.append(pdfACompl.toHtml());

    resultBuffer.append("<p><a href=\"/lzv-jsp/pdfapilot/upload\">Weitere PDF-Validierung</a>");
    resultBuffer.append(HtmlTemplate.getHtmlFoot());

    return resultBuffer.toString();
  }

  @Override
  public String getJson() {
    JSONObject resultJson = new JSONObject();
    
    resultJson.put("file", fileName);
    resultJson.append("pdfInfo", pdfInfo.getJSONObject());
    resultJson.append("pdfACompliance", pdfACompl.getJSONObject());
    return resultJson.toString(3);
  }


}
