/**
 * 
 */
package de.nrw.hbz.lzv.services.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import de.nrw.hbz.lzv.services.configuration.util.PropertiesLoader;

/**
 * 
 */
public class Configuration {

  private PropertiesLoader propLoader = new PropertiesLoader();
  
  private Properties readLogProperties() throws IOException {
    Properties logProps = new Properties();
        InputStream propStream = this.getClass().getResourceAsStream("log4j.properties");
        if (propStream == null) {
             throw new IOException("failed to load log4j.properties: file not found");
            }else{
                logProps.load(propStream);
                System.out.println("read log4j-configuration");
            }
        return logProps;       
  }

}
