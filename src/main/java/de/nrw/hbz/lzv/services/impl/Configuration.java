/**
 * 
 */
package de.nrw.hbz.lzv.services.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.nrw.hbz.lzv.services.util.configuration.PropertiesLoader;

/**
 * 
 */
public class Configuration {

  final static Logger logger = LogManager.getLogger(Configuration.class);

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
