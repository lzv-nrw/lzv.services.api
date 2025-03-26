/**
 * 
 */
package de.nrw.hbz.lzv.services.model.json.impl;

import de.nrw.hbz.lzv.services.model.json.model.SimpleObject;

/**
 * Implementation of PdfACompliance-Model
 * uses default SimpleObject structure
 * derived from lobid and to.science
 */
public class PdfACompliance extends AbstractSimpleObject implements SimpleObject {

  private String name = "compliance";
  private String id = null; 
  private String prefLabel = null; 
  private String type = null; 
  
}
