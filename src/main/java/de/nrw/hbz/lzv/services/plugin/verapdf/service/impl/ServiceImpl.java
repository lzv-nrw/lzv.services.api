/**
 * 
 */
package de.nrw.hbz.lzv.services.plugin.verapdf.service.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Iterator;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.verapdf.ReleaseDetails;
import org.verapdf.core.EncryptedPdfException;
import org.verapdf.core.ModelParsingException;
import org.verapdf.core.ValidationException;
import org.verapdf.model.coslayer.CosDocument;
import org.verapdf.pdfa.Foundries;
import org.verapdf.pdfa.PDFAParser;
import org.verapdf.pdfa.PDFAValidator;
import org.verapdf.pdfa.VeraPDFFoundry;
import org.verapdf.pdfa.flavours.PDFAFlavour;
import org.verapdf.pdfa.results.ValidationResult;
import org.verapdf.pdfa.validation.validators.ValidatorFactory;

import de.nrw.hbz.lzv.services.plugin.verapdf.provider.GFFoundryProvider;
import de.nrw.hbz.lzv.services.util.file.FileUtil;

/**
 * 
 */
public class ServiceImpl {

  private PDFAValidator validator = null;
  private static PDFAParser pdfParser = null;
  private InputStream pdfStream = null;
  private File pdfFile = null;

  final static Logger log = LogManager.getLogger(ServiceImpl.class);

  public ServiceImpl() {
    GFFoundryProvider.setFoundry();
  }

  public void setFlavour(String flavour) {
    validator = ValidatorFactory.createValidator(PDFAFlavour.fromString(flavour), true);
  }

  public void setPDFFile(File file) {
    pdfFile = file;
    pdfStream = FileUtil.loadFile(file);
  }

  public static void listFoundries() {
    Set<URI> uri = Foundries.getProviderIds();
    Iterator<URI> uriIt = uri.iterator();

    while (uriIt.hasNext()) {
      log.info(uriIt.next().toString());

    }

  }

  /**
   * @return
   */
  public String validatePDF() {

    StringBuffer bResult = new StringBuffer();
    bResult.append("Datei-Pfad: " + pdfFile.getAbsolutePath() + "</p><p>");
    bResult.append("Ergebnis : " + validatePDF(pdfStream) + "</p>");
    
    return bResult.toString();
  }

  /**
   * @param fileInputStream
   * @return
   */
  public String validatePDF(InputStream fileInputStream) {
    String result = "<p class=\"error\">PDF is NOT compliant to any PDF/A flavour</p>";
    Set<String> pdfaFlavours = PDFAFlavour.getFlavourIds();
    Iterator<String> pfIt = pdfaFlavours.iterator();

    VeraPDFFoundry vpf = Foundries.defaultInstance();
    try {
      pdfParser = vpf.createParser(fileInputStream);
    } catch (ModelParsingException | EncryptedPdfException e) {
      StringBuffer eResult = new StringBuffer();
      eResult.append("<p class=\"error\">Something went wrong</p>");
      eResult.append("<p>" + e.getMessage() + "</p><p>");
      return result = eResult.toString();
    }

    log.info("Check for these PDF/A flavours : ");

    while (pfIt.hasNext()) {
      String flavour = pfIt.next();
      if (!flavour.equals("0") && !flavour.startsWith("w")) {
        log.info("\t" + flavour);
        validator = ValidatorFactory.createValidator(PDFAFlavour.fromString(flavour), true);
        ValidationResult vResult = validate(fileInputStream);
        
        if (vResult.isCompliant()) {
          result = "<p class=\"success\">PDF is compliant to PDF/A, Version " + pdfParser.getFlavour().getId() + "</p>";
        }

      }
    }

    return result;
  }

  private ValidationResult validate(InputStream fileInputStream) {
    ValidationResult vResult = null;
    try {
      pdfStream = fileInputStream;
      vResult = validator.validate(pdfParser);

    } catch (ValidationException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return vResult;
  }

}
