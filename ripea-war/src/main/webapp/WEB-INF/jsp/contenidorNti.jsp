<%@page import="java.util.Iterator"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib tagdir="/WEB-INF/tags/ripea" prefix="rip"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<c:set var="titol"><spring:message code="contenidor.nti.titol"/></c:set>

<html>
<head>
	<title>${titol}</title>
	<rip:modalHead titol="${titol}" buttonContainerId="botons"/>
</head>
<body>
	<c:if test="${contenidor.expedient}">
		<dl class="dl-horizontal">
			<dt><spring:message code="contenidor.nti.camp.versio"/></dt>
			<dd>${contenidor.ntiVersionUrl}</dd>
			<dt><spring:message code="contenidor.nti.camp.identificador"/></dt>
			<dd>${contenidor.ntiIdentificador}</dd>
			<dt><spring:message code="contenidor.nti.camp.organ"/></dt>
			<dd>${contenidor.ntiOrgano}</dd>
			<dt><spring:message code="contenidor.nti.camp.data"/></dt>
			<dd>${contenidor.ntiFechaApertura}</dd>
			<dt><spring:message code="contenidor.nti.camp.classificacio"/></dt>
			<dd>${contenidor.ntiClasificacion}</dd>
			<dt><spring:message code="contenidor.nti.camp.estat"/></dt>
			<dd>${contenidor.ntiEstado}</dd>
		</dl>
	</c:if>
	<div id="modal-botons" class="well">
		<a href="<c:url value="/contenidor/${contenidor.id}"/>" class="btn btn-default modal-tancar"><spring:message code="comu.boto.tancar"/></a>
	</div>
</body>
</html>
