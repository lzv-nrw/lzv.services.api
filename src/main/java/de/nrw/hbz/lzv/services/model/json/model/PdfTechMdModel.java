package de.nrw.hbz.lzv.services.model.json.model;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;

/**
 * 
 */
public class PdfTechMdModel {

  private static LinkedHashMap<String, String> techMDLabel = setTechMDLabel();
  private static Set<String> techMDKeys = setKeys();
  
  public final static String VERSION = "version";

  public PdfTechMdModel(){
    setTechMDLabel();
    setKeys();
  }
  
  private static LinkedHashMap<String,String> setTechMDLabel() {
    
    techMDLabel = new LinkedHashMap<>();
    techMDLabel.put(VERSION, "Version");
    return techMDLabel; 
  }
  
  public static LinkedHashMap<String, String> getInfoLabel() {
    return techMDLabel;
  }
  
  private static Set<String> setKeys() {
    
    techMDKeys = new HashSet<>();
    techMDKeys.add(VERSION);
    return techMDKeys; 
  }

  public static Set<String> getKeys() {
    return techMDKeys;
  }

}
