/**
 * 
 */
package de.nrw.hbz.lzv.services.impl;

import java.io.File;
import java.util.Iterator;
import java.util.LinkedHashMap;

/**
 * 
 */
public class FileController {
  
  private static LinkedHashMap<String,File> fileHandler = new LinkedHashMap<>();
  
  public void setLoadedFile(File loadedFile) {
    
    fileHandler.put(loadedFile.getName(), loadedFile);
        
  }
  
  public String getLoadedFile(String fileName) {
    return fileHandler.get(fileName).getAbsolutePath();
  }

  public void deleteFile(String fType) {
    if(fileHandler.containsKey(fType)) {
      fileHandler.get(fType).delete();
      fileHandler.remove(fType);
    }
  }
  
  public void deleteFiles() {
    
    Iterator<String> fhIt = fileHandler.keySet().iterator();
    
    while(fhIt.hasNext()) {
      String key = fhIt.next();
      fileHandler.get(key).delete();
    }
    fileHandler = null;
  }
}
