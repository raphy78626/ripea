<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<html>
<head>
	<title><spring:message code="contenidor.document.passarelafirma.titol"/></title>
</head>
<body>
	<div class="lead" style="margin-bottom: 10px; text-align: center;">
		<h3>
			Selecció del mòdul de Firma
		</h3>
		<h5>
			Seleccioni el mòdul de firma amb el que vol firmar
		</h5><br/>
		<c:if test="fn:length(companies) eq 1">
			<h6>
				Si no voleu que aparegui aquesta pantalla quan només hi ha un
				plugin, llavors anau a la <br /> classe <b>org.fundaciobit.plugins.signatureweb.exemple.controller.SignatureModuleController</b><br />
				i editau el camp estatic stepSelectionWhenOnlyOnePlugin i assignau-li un valor true;
			</h6><br/>
		</c:if>
		<div class="well" style="max-width: 400px; margin: 0 auto 10px;">
			<c:forEach items="${plugins}" var="plugin">
				<a href="../showsignaturemodule/${plugin.pluginID}/${signaturesSetId}" class="btn btn-large btn-block btn-primary">
					<b>${plugin.nom}</b><br />
					<small><i>${plugin.descripcioCurta}</i></small>
				</a>
			</c:forEach>
		</div>
	</div>
</body>
</html>
