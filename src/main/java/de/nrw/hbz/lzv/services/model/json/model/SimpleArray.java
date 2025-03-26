package de.nrw.hbz.lzv.services.model.json.model;

/**
 * Interface defines some methods required to create and modify an simple array of string 
 * 
 * @author aquast
 *
 */
public interface SimpleArray {
  
  /**
   * add new item to Array
   */
  public void addItem(String item);
  
  /**
   * @return get item as String
   */
  public String getItem(int i);
  
  /**
   * @return
   */
  public int size();
}
