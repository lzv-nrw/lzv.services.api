/**
 * 
 */
package de.nrw.hbz.lzv.services.configuration.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * 
 */
public class PropertiesLoader {
  
  public PropertiesLoader() {
    loadBaseConfiguration();
  }

  // Initiate Logger for JerseyServiceImpl
  private static Logger log = LogManager.getLogger(PropertiesLoader.class);

  private static Properties baseConfiguration = new Properties();
  private Properties properties = new Properties();
  
  public Properties getProperties(String fileName) {
    
    return properties;
  }
  
  private void loadProperties(InputStream pIst) throws IOException {
    Enumeration<Object> key = baseConfiguration.keys();
    
    while(key.hasMoreElements()) {
      
    }
    properties.load(pIst); 
  }
  
  private void loadBaseConfiguration() {
    InputStream baseCIs = this.getClass().getResourceAsStream("application.properties");
    try {
      baseConfiguration.load(baseCIs);
    } catch (IOException e) {
      log.error("Can't load basic configuration");
    }
  }
}
