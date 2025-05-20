package de.nrw.hbz.lzv.services.model.json.model;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;

/**
 * 
 */
public class PdfInfoModel {

  private static LinkedHashMap<String, String> infoLabel = setInfoLabel();
  private static Set<String> infoKeys = setKeys();
  
  public final static String TITLE = "title";
  public final static String AUTHOR = "author";
  public final static String CREATOR = "creator";
  public final static String PRODUCER = "producer";
  public final static String PAGES = "pages";
  public final static String PAGESIZE = "pages";
  public final static String CREATION_DATE = "creationDate";
  public final static String MODIFICATION_DATE = "modificationDate";
  public final static String KEYWORDS = "keywords";
  public final static String SUBJECT = "subject";

  public PdfInfoModel(){
    setInfoLabel();
    setKeys();
  }
  
  private static LinkedHashMap<String,String> setInfoLabel() {
    
    infoLabel = new LinkedHashMap<>();
    infoLabel.put(TITLE, "Title");
    infoLabel.put(AUTHOR, "Author");
    infoLabel.put(CREATOR, "Creator");
    infoLabel.put(PRODUCER, "Production Tool");
    infoLabel.put(PAGES , "Pages");
    infoLabel.put(PAGESIZE , "PageSize");
    infoLabel.put(CREATION_DATE, "Creation Date");
    infoLabel.put(MODIFICATION_DATE, "Modification Date");
    infoLabel.put(KEYWORDS, "Keywords");
    infoLabel.put(SUBJECT, "Subject");
    return infoLabel; 
  }
  
  public static LinkedHashMap<String, String> getInfoLabel() {
    return infoLabel;
  }
  
  private static Set<String> setKeys() {
    
    infoKeys = new HashSet<>();
    infoKeys.add(TITLE);
    infoKeys.add(AUTHOR);
    infoKeys.add(CREATOR);
    infoKeys.add(PRODUCER);
    infoKeys.add(PAGES);
    infoKeys.add(CREATION_DATE);
    infoKeys.add(MODIFICATION_DATE);
    infoKeys.add(KEYWORDS);
    infoKeys.add(SUBJECT);
    return infoKeys; 
  }

  public static Set<String> getKeys() {
    return infoKeys;
  }

}
