package de.nrw.hbz.lzv.services.plugin.callas.model.pilot;

import java.util.Properties;

public class Parameter {

  
  private Properties paramProp  = new Properties();
  
  public Parameter(){
    setDefaultParameter();
  }
  
  private void setDefaultParameter(){
    
    // set Processing Params
    paramProp.setProperty("returnOnlyValidPdfA", "true");
    paramProp.setProperty("quickProcessing", "true");
    paramProp.setProperty("analyseOnly", "false");
    
    paramProp.setProperty("compliancyLevel", "1b");
    
    
    //set Reporting Params
    paramProp.setProperty("reportTrigger", "ALWAYS");
    paramProp.setProperty("reportLang", "DE");
    paramProp.setProperty("htmlReport", "true");
    paramProp.setProperty("htmlOpenResult", "true");
    paramProp.setProperty("htmlNoIcons", "false");
    paramProp.setProperty("xmlReport", "false");
    paramProp.setProperty("mhtReport", "false");
  }
  
  
}

