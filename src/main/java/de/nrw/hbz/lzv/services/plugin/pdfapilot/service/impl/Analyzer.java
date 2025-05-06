/**
 * 
 */
package de.nrw.hbz.lzv.services.plugin.pdfapilot.service.impl;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Stream;

import de.nrw.hbz.lzv.services.model.pdf.model.Compliance;
import de.nrw.hbz.lzv.services.model.pdf.model.Version;

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
    String stripPrefLabel = prefLabel.substring(6,8).toUpperCase();
    
    if(Compliance.labelExists(stripPrefLabel)){
      complianceMap.put("prefLabel", stripPrefLabel);
      complianceMap.put("@id", Compliance.getComplianceUrl(stripPrefLabel));
    }
    
    pdfMd.put("File", fileName);
    pdfMd.put("PDF/A compliance", complianceMap);

    // return pdfMd.toString();
  }

  @Override
  public String getHtml() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String getJson() {
    // TODO Auto-generated method stub
    return null;
  }


}
