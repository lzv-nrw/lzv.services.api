package de.nrw.hbz.lzv.services.impl;

import java.io.File;
import java.util.Hashtable;

import de.nrw.hbz.lzv.services.model.json.impl.PdfACompliance;
import de.nrw.hbz.lzv.services.model.json.impl.PdfInfo;
import de.nrw.hbz.lzv.services.model.pdf.edit.PdfBoxEditResult;

/**
 * 
 */
public abstract class PdfMdEditor {

	private static Hashtable<String, PdfMdEditor> subClasses = new Hashtable<>();

	public PdfInfo pdfInfo = null;
	public PdfACompliance pdfACompl = null;

	public StringBuffer resultBuffer = new StringBuffer();
	public String fileName = null;

	public static PdfMdEditor getInstance(String name) {
		init();
		// String path = "de.nrw.hbz.lzv.services.plugin." + name +
		// ".service.impl.VersionInfo";
		PdfMdEditor PdfMdEditor = subClasses.get(name);
		// TODO: implement a reflection based version of getInstance

		return PdfMdEditor;
	}

	private static void init() {
		subClasses.put("pdfbox", new de.nrw.hbz.lzv.services.plugin.pdfbox.service.impl.PdfMdEditor());

	}

	public abstract PdfBoxEditResult editPdfMd(File file, String fileName, String key, String value);

	public abstract String getHtml();

	public abstract String getJson();

}
