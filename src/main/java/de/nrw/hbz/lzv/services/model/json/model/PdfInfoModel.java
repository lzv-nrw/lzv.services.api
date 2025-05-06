package de.nrw.hbz.lzv.services.model.json.model;

import java.util.LinkedHashMap;

/**
 * 
 */
public class PdfInfoModel {

  private static LinkedHashMap<String, String> infoLabel = null; 
  
  public final static String TITLE = "title";
  public final static String AUTHOR = "author";
  public final static String CREATOR = "creator";
  public final static String PRODUCER = "producer";
  public final static String PAGES = "pages";
  public final static String CREATION_DATE = "creationDate";
  public final static String MODIFICATION_DATE = "modificationDate";
  public final static String KEYWORDS = "keywords";
  public final static String SUBJECT = "subject";

  public PdfInfoModel(){
    setInfoLabel();
  }
  
  private static LinkedHashMap<String,String> setInfoLabel() {
    
    LinkedHashMap<String,String> infoLabel = new LinkedHashMap<>();
    infoLabel.put(TITLE, "Title");
    infoLabel.put(AUTHOR, "Author");
    infoLabel.put(CREATOR, "Creator");
    infoLabel.put(PRODUCER, "Production Tool");
    infoLabel.put(PAGES , "Pages");
    infoLabel.put(CREATION_DATE, "Creation Date");
    infoLabel.put(MODIFICATION_DATE, "Modification Date");
    infoLabel.put(KEYWORDS, "Keywords");
    infoLabel.put(SUBJECT, "Subject");
    return infoLabel; 
  }
  
  public static LinkedHashMap<String, String> getInfoLabel() {
    return infoLabel;
  }
  

}
