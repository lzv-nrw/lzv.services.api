package de.nrw.hbz.lzv.services.model.json.model;

import java.util.LinkedHashMap;


public interface PdfInfo {

	public LinkedHashMap<String, Object> getPdfInfo();
	
	public PdfTitle getPdfTitle();
	
	public PdfAuthor getPdfAuthor();
	
	public PdfCreator getPdfCreator();
	
	public PdfProducer getProducer();
	
	public PdfCreationDate getPdfCreationDate();
	
	public PdfPages getPdfPages();
	
	
	
}
