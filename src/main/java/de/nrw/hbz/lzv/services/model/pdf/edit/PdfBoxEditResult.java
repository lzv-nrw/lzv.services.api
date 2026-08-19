package de.nrw.hbz.lzv.services.model.pdf.edit;

import java.util.AbstractMap;
import java.util.Map;

public class PdfBoxEditResult {

	private Map.Entry<String, String> oldMetadataPair = new AbstractMap.SimpleEntry<>(null, null);
	private Map.Entry<String, String> newMetadataPair = new AbstractMap.SimpleEntry<>(null, null);

	private String fileOutputLocation = null;
	private String loadedFileName = null;

	/**
	 * @return the metadata key value pair for editing
	 */
	public Map.Entry<String, String> getOldMetadataPair() {
		return oldMetadataPair;
	}

	/**
	 * @param pair the pair to set
	 */
	public void setOldMetadatatPair(Map.Entry<String, String> oldMetadataPair) {
		this.oldMetadataPair = oldMetadataPair;
	}

	/**
	 * @return the metadata key value pair for editing
	 */
	public Map.Entry<String, String> getNewMetadataPair() {
		return newMetadataPair;
	}

	/**
	 * @param pair the pair to set
	 */
	public void setNewMetadatatPair(Map.Entry<String, String> newMetadataPair) {
		this.newMetadataPair = newMetadataPair;
	}

	/**
	 * @return the fileOutputLocation
	 */
	public String getFileOutputLocation() {
		return fileOutputLocation;
	}

	/**
	 * @param fileOutputLocation the fileOutputLocation to set
	 */
	public void setFileOutputLocation(String fileOutputLocation) {
		this.fileOutputLocation = fileOutputLocation;
	}

	/**
	 * @return the loadedFileName
	 */
	public String getLoadedFileName() {
		return loadedFileName;
	}

	/**
	 * @param loadedFileName the loadedFileName to set
	 */
	public void setLoadedFileName(String loadedFileName) {
		this.loadedFileName = loadedFileName;
	}

}
