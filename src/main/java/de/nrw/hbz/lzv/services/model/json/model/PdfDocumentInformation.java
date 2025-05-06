package de.nrw.hbz.lzv.services.model.json.model;

import org.json.JSONObject;


public interface PdfDocumentInformation {

	/**
	 * @return pdfInfo 
	 */
	public JSONObject getPdfInfo();
		
  public void setPdfTitle(String title);

  public void setPdfAuthor(String author);
	
  public void setPdfCreator(String creationTool);

  public void setPdfProducer(String productionTool);

  public void setPdfCreationDate(String creationDate);

  public void setPdfModificationDate(String modificationDate);

  public void setPdfPages(String pages);

  public void setPdfKeywords(String keywords);

  public void setPdfSubject(String subject);
  
  public String toHtml();
	
}
