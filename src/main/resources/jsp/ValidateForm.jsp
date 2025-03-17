<%@ page language="java" contentType="text/html"
	pageEncoding="UTF-8"%>

<%=de.nrw.hbz.lzv.services.restService.JerseyServiceImpl.getHtmlHead() %>

	<h1>PDF Upload zur Nutzung des lzv-Services veraPDF-Validierung</h1>
		<div class="verapdf-form validation">
			<form action="validate" method="post" enctype="multipart/form-data">
        		<p>PDF wählen</p>
				<input class="file-upload" type="file" name="file">
				</input>
				<input type="submit" value="Abschicken"></input>
			</form>
		</div>

<%=de.nrw.hbz.lzv.services.restService.JerseyServiceImpl.getHtmlFoot() %>
