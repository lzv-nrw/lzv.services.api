/**
 * 
 */
package de.nrw.hbz.lzv.services.plugin.verapdf.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.verapdf.core.EncryptedPdfException;
import org.verapdf.core.ModelParsingException;
import org.verapdf.core.ValidationException;
import org.verapdf.metadata.fixer.entity.PDFDocument;
import org.verapdf.pdfa.Foundries;
import org.verapdf.pdfa.PDFAParser;
import org.verapdf.pdfa.PDFAValidator;
import org.verapdf.pdfa.VeraPDFFoundry;
import org.verapdf.pdfa.flavours.PDFAFlavour;
import org.verapdf.pdfa.results.ValidationResult;
import org.verapdf.pdfa.validation.validators.ValidatorFactory;

import de.nrw.hbz.lzv.services.model.json.impl.PdfACompliance;
import de.nrw.hbz.lzv.services.model.json.impl.PdfInfo;
import de.nrw.hbz.lzv.services.plugin.verapdf.provider.GFFoundryProvider;
import de.nrw.hbz.lzv.services.template.HtmlTemplate;

/**
 * 
 */
public class Analyzer extends de.nrw.hbz.lzv.services.impl.Analyzer {

  final static Logger log = LogManager.getLogger(Analyzer.class);

  private PDFAValidator validator = null;
  private static PDFAParser pdfParser = null;
  private StringBuffer resultBuffer = new StringBuffer();
   
  public Analyzer() {
    GFFoundryProvider.setFoundry();  
  }
  
  
  @Override
  public void analyze(File file, String fileName) {
    VeraPDFFoundry vpf = Foundries.defaultInstance();

    this.fileName = fileName;
    try {
      FileInputStream fileInputStream = new FileInputStream(file);
      pdfParser = vpf.createParser(fileInputStream);
      PDFDocument pdfDocument  = pdfParser.getPDFDocument();

      // get PDF information in generic form
      pdfInfo = getPdfInfo(pdfDocument);

      // validate for PDF/A
      log.info("Check for these PDF/A flavours : ");
      validateAllFlavours(fileInputStream);

    } catch (FileNotFoundException | ModelParsingException | EncryptedPdfException e) {
      StringBuffer eResult = new StringBuffer();
      eResult.append("<p class=\"error\">Something went wrong</p>");
      eResult.append("<p>" + e.getMessage() + "</p><p>");
      // return result = eResult.toString();
    }
    

   
  }
  
  private void validateAllFlavours(InputStream fileInputStream) {
    Set<String> pdfaFlavours = PDFAFlavour.getFlavourIds();
    Iterator<String> pfIt = pdfaFlavours.iterator();

    pdfACompl = new PdfACompliance();
    pdfACompl.setIsPdfACompliant(false);
    
    while (pfIt.hasNext()) {
      String flavour = pfIt.next();
      if (!flavour.equals("0") && !flavour.startsWith("w")) {
        log.info("\t" + flavour);
        validator = ValidatorFactory.createValidator(PDFAFlavour.fromString(flavour), true);
        ValidationResult vResult = validate(fileInputStream);
        
        if (vResult.isCompliant()) {
          pdfACompl.setIsPdfACompliant(true);
          pdfACompl.setCompliance(pdfParser.getFlavour().getId());
        }
      }
    }
  }
    

  /**
   * @param fileInputStream
   * @return
   */
  private ValidationResult validate(InputStream fileInputStream) {
    ValidationResult vResult = null;
    try {
      //pdfStream = fileInputStream;
      vResult = validator.validate(pdfParser);

    } catch (ValidationException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return vResult;
  }

  /**
   * get the information stored in the PDF information part  
   * @return
   */
  private PdfInfo getPdfInfo(PDFDocument pdfDocument){
    PdfInfoProvider infoProvider = new PdfInfoProvider(pdfDocument.getInfoDictionary()); 
    return infoProvider.getPdfInfo();
  }
  
  /**
   * set the flavour aka PDF/A level you wish to analyze PDF against 
   * @param flavour
   */
  public void setFlavour(String flavour) {
    validator = ValidatorFactory.createValidator(PDFAFlavour.fromString(flavour), true);
  }


  @Override
  public String getHtml() {
    
    resultBuffer.append(HtmlTemplate.getHtmlHead());

    resultBuffer.append("<h1>Ergebnis der Prüfung</h1>\n");
    resultBuffer.append("<p>" + fileName + "</p>");
    resultBuffer.append(pdfInfo.toHtml());
    resultBuffer.append(pdfACompl.toHtml());

    resultBuffer.append("<p><a href=\"/lzv-jsp/pdfapilot/upload\">Weitere PDF-Validierung</a>");
    resultBuffer.append(HtmlTemplate.getHtmlFoot());

    return resultBuffer.toString();
  }


  @Override
  public String getJson() {
    JSONObject resultJson = new JSONObject();
    
    resultJson.put("file", fileName);
    resultJson.put("pdfInfo", pdfInfo);
    resultJson.put("pdfACompliance", pdfACompl);
    return resultJson.toString(3);
  }

  

}
