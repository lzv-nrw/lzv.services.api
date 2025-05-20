/**
 * 
 */
package lzv.services.pdf.model;

import de.nrw.hbz.lzv.services.model.pdf.model.Compliance;

/**
 * 
 */
public class ModelTest {

  /**
   * @param args
   */
  public static void main(String[] args) {
    
    ModelTest md = new ModelTest();
    md.getCompliance();
  
  }
  
  public void getCompliance() {
    System.out.println("Ergebnis: " + Compliance.labelExists("1A"));
    System.out.println(Compliance.getComplianceUrl("1a"));
    
  }
  

}
