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
  public final static String FILESIZE = "filesize";
  public final static String PAGES = "pages";
  public final static String PAGES_PDFAPILOT = "pagenumber";
  public final static String PAGESIZE = "pagesize";
  public final static String CREATION_DATE = "creationDate";
  public final static String CREATION_DATE_PDFAPILOT = "created";
  public final static String MODIFICATION_DATE = "modificationDate";
  public final static String MODIFICATION_DATE_PDFAPILOT = "modified";
  public final static String KEYWORDS = "keywords";
  public final static String SUBJECT = "subject";

  public PdfInfoModel(){
    setInfoLabel();
    setKeys();
  }
  
  private static LinkedHashMap<String,String> setInfoLabel() {
    
    infoLabel = new LinkedHashMap<>();
    infoLabel.put(TITLE, "Titel");
    infoLabel.put(AUTHOR, "Autor");
    infoLabel.put(CREATOR, "Erstellt mit");
    infoLabel.put(PRODUCER,"PDF erstellt mit");
    infoLabel.put(FILESIZE, "Dateigröße");
    infoLabel.put(PAGES, "Seitenanzahl");
    infoLabel.put(PAGES_PDFAPILOT, "Seitenanzahl");
    infoLabel.put(PAGESIZE, "Seitengröße");
    infoLabel.put(CREATION_DATE, "Erstellungsdatum");
    infoLabel.put(CREATION_DATE_PDFAPILOT, "Erstellungsdatum");
    infoLabel.put(KEYWORDS, "Schlagwörter");
    infoLabel.put(SUBJECT, "Betreff");
    infoLabel.put(MODIFICATION_DATE, "Änderungsdatum");
    infoLabel.put(MODIFICATION_DATE_PDFAPILOT, "Änderungsdatum");
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
    infoKeys.add(FILESIZE);
    infoKeys.add(PAGES);
    infoKeys.add(PAGES_PDFAPILOT);
    infoKeys.add(PAGESIZE);
    infoKeys.add(CREATION_DATE);
    infoKeys.add(CREATION_DATE_PDFAPILOT);
    infoKeys.add(KEYWORDS);
    infoKeys.add(SUBJECT);
    infoKeys.add(MODIFICATION_DATE);
    infoKeys.add(MODIFICATION_DATE_PDFAPILOT);
    return infoKeys; 
  }

  public static Set<String> getKeys() {
    return infoKeys;
  }

}
