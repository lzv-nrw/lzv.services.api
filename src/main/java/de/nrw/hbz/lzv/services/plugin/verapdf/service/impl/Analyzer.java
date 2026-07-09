/**
 * 
 */
package de.nrw.hbz.lzv.services.plugin.verapdf.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
import org.verapdf.pdfa.flavours.PDFFlavours;
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
  private StringBuffer resultBuffer = new StringBuffer();

  public Analyzer() {
    GFFoundryProvider.setFoundry();
  }

  @Override
  public void analyze(File file, String fileName) {

    this.fileName = fileName;

    pdfACompl = new PdfACompliance();
    pdfACompl.setIsPdfACompliant(false);
    VeraPDFFoundry vpf = Foundries.defaultInstance();

    try (PDFAParser pdfParser = vpf.createParser(file)) {

      PDFDocument pdfDocument = pdfParser.getPDFDocument();

      if (pdfDocument.getInfoDictionary() != null) {
        pdfInfo = getPdfInfo(pdfDocument);
      } else {
        pdfInfo = new PdfInfo();
      }

      validateAllFlavours(pdfParser, vpf);

    } catch (IOException | ModelParsingException | EncryptedPdfException e) {
      StringBuffer eResult = new StringBuffer();
      eResult.append("<p class=\"error\">Something went wrong</p>");
      eResult.append("<p>" + e.getMessage() + "</p><p>");
      // return result = eResult.toString();
    }

  }

  private void validateAllFlavours(PDFAParser pdfParser, VeraPDFFoundry vpf) {
    List<PDFAFlavour> detectedFlavours = pdfParser.getFlavours();
    log.info("Detected flavours: {}", detectedFlavours);

    List<PDFAFlavour> pdfaFlavours = new ArrayList<>();

    pdfACompl = new PdfACompliance();
    pdfACompl.setIsPdfACompliant(false);

    for (PDFAFlavour flavour : detectedFlavours) {

      // Only PDF/A
      if (PDFFlavours.isFlavourFamily(flavour, PDFAFlavour.SpecificationFamily.PDF_A)) {

        pdfaFlavours.add(flavour);
      }
    }

    if (pdfaFlavours.isEmpty()) {
      return;
    }

    validator = vpf.createValidator(pdfaFlavours);

    List<ValidationResult> results;
    try {
      results = validator.validateAll(pdfParser);

      for (int i = 0; i < results.size(); i++) {

        ValidationResult result = results.get(i);
        PDFAFlavour flavour = pdfaFlavours.get(i);

        log.info("{} {}", flavour.getId(), result.isCompliant());

        if (result.isCompliant()) {
          pdfACompl.setIsPdfACompliant(true);
          pdfACompl.setCompliance(flavour.getId());
          break;
        }
      }
    } catch (ValidationException e) {
      log.info("Something went wrong");
      e.printStackTrace();
    }
  }

  /**
   * get the information stored in the PDF information part
   *
   * @return
   */
  private PdfInfo getPdfInfo(PDFDocument pdfDocument) {
    PdfInfoProvider infoProvider = new PdfInfoProvider(pdfDocument.getInfoDictionary());
    return infoProvider.getPdfInfo();
  }

  /**
   * set the flavour aka PDF/A level you wish to analyze PDF against
   *
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
    if (pdfInfo != null) {
      resultBuffer.append(pdfInfo.toHtml());
    }
    resultBuffer.append(pdfACompl.toHtml());

    resultBuffer.append("<p><a href=\"/lzv-jsp/verapdf/upload\">Weitere PDF-Validierung</a>");
    resultBuffer.append(HtmlTemplate.getHtmlFoot());

    return resultBuffer.toString();
  }

  @Override
  public String getJson() {
    JSONObject resultJson = new JSONObject();

    resultJson.put("file", fileName);
    if (pdfInfo != null) {
      resultJson.put("pdfInfo", pdfInfo.getJSONObject());
    }
    resultJson.put("pdfACompliance", pdfACompl.getJSONObject());
    return resultJson.toString(3);
  }

}
