/**
 * 
 */
package de.nrw.hbz.lzv.services.plugin.pdfbox.service.impl;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 
 */
public class Analyzer extends de.nrw.hbz.lzv.services.impl.Analyzer {

  private static Logger logger = LogManager.getLogger(ServiceImpl.class);
  
  private InputStream pdfStream = null;
  private BufferedInputStream pdfBis = null;

  @Override
  public void analyze(File file, String fileName) {
    // TODO Auto-generated method stub
  }

  @Override
  public String getHtml() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String getJson() {
    // TODO Auto-generated method stub
    return null;
  }

  
}
