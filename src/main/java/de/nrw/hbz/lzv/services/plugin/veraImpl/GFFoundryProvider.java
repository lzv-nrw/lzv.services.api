/**
 * 
 */
package de.nrw.hbz.lzv.services.plugin.veraImpl;

import org.verapdf.gf.foundry.VeraGreenfieldFoundryProvider;

/**
 * 
 */
public class GFFoundryProvider {

  public static void setFoundry() {
    VeraGreenfieldFoundryProvider.initialise();
  }
}
