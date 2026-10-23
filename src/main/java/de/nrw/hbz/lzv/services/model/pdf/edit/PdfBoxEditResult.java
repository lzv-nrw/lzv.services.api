package de.nrw.hbz.lzv.services.model.pdf.edit;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PdfBoxEditResult {

	private List<Map.Entry<String, String>> oldMetadataPairs = new ArrayList<>();
	private List<Map.Entry<String, String>> newMetadataPairs = new ArrayList<>();

	private String fileOutputLocation = null;
	private String loadedFileName = null;

	/**
	 * @return the list of old metadata key-value pairs
	 */
	public List<Map.Entry<String, String>> getOldMetadataPairs() {
		return oldMetadataPairs;
	}

	/**
	 * Adds an old metadata key-value pair to the list.
	 *
	 * @param key   the metadata key
	 * @param value the metadata value
	 */
	public void addOldMetadataPair(String key, String value) {
		oldMetadataPairs.add(new AbstractMap.SimpleEntry<>(key, value));
	}

	/**
	 * @return the list of new metadata key-value pairs
	 */
	public List<Map.Entry<String, String>> getNewMetadataPairs() {
		return newMetadataPairs;
	}

	/**
	 * Adds a new metadata key-value pair to the list.
	 *
	 * @param key   the metadata key
	 * @param value the metadata value
	 */
	public void addNewMetadataPair(String key, String value) {
		newMetadataPairs.add(new AbstractMap.SimpleEntry<>(key, value));
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
