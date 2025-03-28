/**
 * 
 */
package de.nrw.hbz.lzv.services.model.json.impl;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import de.nrw.hbz.lzv.services.model.json.model.SimpleObject;

/**
 * 
 */
public class AbstractSimpleObject implements SimpleObject {

  Map<String,String> simpleObj = new HashMap<>();
  
  private String name = null; 
  private String id = null; 
  private String prefLabel = null; 

  
  public String getId() {
    return id;
  }

  public String getPrefLabel() {
    return prefLabel;
  }

  public void setId(String id) {
    this.id = id;
    
  }

  public void setPrefLabel(String prefLabel) {
    this.prefLabel = prefLabel;
    
  }

  public String getType() {
    return null;
  }

  public void setType(String Type) {

  }
  
  public String getName() {
    return name;
  }
  
  
  public Map<String,String> getMap() {
    simpleObj.put("prefLabel", prefLabel);
    simpleObj.put("@id", id);
    return simpleObj;
  }


}
