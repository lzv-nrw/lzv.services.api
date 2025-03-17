<%@ page language="java" contentType="text/html"
	pageEncoding="UTF-8"%>

<%=de.nrw.hbz.lzv.services.restService.JerseyServiceImpl.getHtmlHead() %>

	<h1>LZV-Services für PDF-Dateien</h1>
	<p>Folgende Services stehen zur Verfügung</p>
	<ul>
	<li><a href="about">Diese Informationsseite zum Webservice</a>
	<li><a href="version">Anzeige der Version verwendeten Version der veraPDF-Library</a>
	<li><a href="upload">PDF-Datei zum Validieren mit veraPDF hochladen</a>
	</ul>

<%=de.nrw.hbz.lzv.services.restService.JerseyServiceImpl.getHtmlFoot() %>
