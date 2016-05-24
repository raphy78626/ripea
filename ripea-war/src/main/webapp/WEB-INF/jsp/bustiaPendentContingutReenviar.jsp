<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib tagdir="/WEB-INF/tags/ripea" prefix="rip"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<c:set var="titol"><spring:message code="bustia.pendent.contingut.reenviar.titol"/></c:set>
<html>
<head>
	<title>${titol}</title>
	<rip:modalHead titol="${titol}"/>
	<link href="<c:url value="/css/jstree.min.css"/>" rel="stylesheet">
	<script src="<c:url value="/js/jstree.min.js"/>"></script>
</head>
<body>
	<form:form action="" class="form-horizontal" commandName="contenidorMoureCopiarEnviarCommand">
		<form:hidden path="contenidorOrigenId"/>
		<rip:inputFixed textKey="contenidor.enviar.camp.origen">
			<c:choose>
				<c:when test="${contingutPendent.tipus == EXPEDIENT}"><span class="fa fa-briefcase"></span></c:when>
				<c:when test="${contingutPendent.tipus == CARPETA}"><rip:blocIconaCarpeta carpeta="${contenidorOrigen}" petita="${true}"/></c:when>
				<c:when test="${contingutPendent.tipus == DOCUMENT}"><span class="fa fa-file"></span></c:when>
				<c:when test="${contingutPendent.tipus == REGISTRE_ENTRADA}"><span class="fa fa-file"></span></c:when>
			</c:choose>
			${contenidorOrigen.nom}
		</rip:inputFixed>
		<rip:inputArbre name="contenidorDestiId" textKey="contenidor.enviar.camp.desti" arbre="${arbreUnitatsOrganitzatives}" required="true" fulles="${busties}" fullesAtributId="id" fullesAtributNom="nom" fullesAtributPare="unitatCodi" fullesIcona="fa fa-inbox fa-lg" isArbreSeleccionable="${false}" isFullesSeleccionable="${true}" isOcultarCounts="${true}"/>
		<rip:inputTextarea name="comentariEnviar" textKey="contenidor.enviar.camp.comentari"/>
		<div id="modal-botons" class="well">
			<button type="submit" class="btn btn-success"><span class="fa fa-envelope"></span> <spring:message code="comu.boto.enviar"/></button>
			<a href="<c:url value="/contenidor/${contenidorOrigen.pare.id}"/>" class="btn btn-default modal-tancar"><spring:message code="comu.boto.cancelar"/></a>
		</div>
	</form:form>
</body>
</html>
