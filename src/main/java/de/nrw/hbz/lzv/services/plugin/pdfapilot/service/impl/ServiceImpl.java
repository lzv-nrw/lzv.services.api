/**
 * 
 */
package de.nrw.hbz.lzv.services.plugin.pdfapilot.service.impl;

/**
 * 
 */
public class ServiceImpl {
 
  public static String createPdfA() {
    
    
    
    PilotRunner pRunner = new PilotRunner();
    
    
    pRunner.executePdfATool(" --version");
    
    return pRunner.getStoutStr();
    
    
  }
}
