<%@page import="java.util.Iterator"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib tagdir="/WEB-INF/tags/ripea" prefix="rip"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<c:set var="titol"><spring:message code="contenidor.moure.titol"/></c:set>
<html>
<head>
	<title>${titol}</title>
	<rip:modalHead titol="${titol}" buttonContainerId="botons"/>
</head>
<body>
	<form:form action="" class="form-horizontal" commandName="contenidorMoureCopiarEnviarCommand">
		<form:hidden path="contenidorOrigenId"/>
		<rip:inputFixed textKey="contenidor.moure.camp.origen">
			<c:choose>
				<c:when test="${contenidorOrigen.expedient}"><span class="fa fa-briefcase"></span></c:when>
				<c:when test="${contenidorOrigen.carpeta}"><rip:blocIconaCarpeta carpeta="${contenidorOrigen}" petita="${true}"/></c:when>
				<c:when test="${contenidorOrigen.document}"><span class="fa fa-file"></span></c:when>
			</c:choose>
			${contenidorOrigen.nom}
		</rip:inputFixed>
		<rip:inputFileChooser name="contenidorDestiId" contenidorOrigen="${contenidorOrigen}" textKey="contenidor.moure.camp.desti" required="true"/>
		<div id="modal-botons" class="well">
			<button type="submit" class="btn btn-success"><span class="fa fa-save"></span> <spring:message code="comu.boto.moure"/></button>
			<a href="<c:url value="/contenidor/${contenidorOrigen.pare.id}"/>" class="btn btn-default modal-tancar"><spring:message code="comu.boto.cancelar"/></a>
		</div>
	</form:form>
</body>
</html>
