<%@page import="java.util.Iterator"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib tagdir="/WEB-INF/tags/ripea" prefix="rip"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<html>
<head>
	<title>Detalls de l'anotació de registre</title>
	<rip:modalHead titol="${titol}" buttonContainerId="botons"/>
<style>
.tab-content {
    margin-top: 0.8em;
}
</style>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="#informacio" data-toggle="tab"><spring:message code="contenidor.registre.pipella.informacio"/></a>
		</li>
		<c:if test="${true}">
			<li><a href="#interessats" data-toggle="tab"><spring:message code="contenidor.registre.pipella.interessats"/>&nbsp;<span class="badge">${fn:length(registre.interessats)}</span></a>
			</li>
		</c:if>
		<c:if test="${true}">
			<li><a href="#annexos" data-toggle="tab"><spring:message code="contenidor.registre.pipella.annexos"/>&nbsp;<span class="badge">${fn:length(registre.annexos)}</span></a>
			</li>
		</c:if>
	</ul>
	<div class="tab-content">
		<div class="tab-pane active in" id="informacio">
			<dl class="dl-horizontal">
				<dt><spring:message code="contenidor.registre.camp.tipus"/></dt><dd><spring:message code="registre.anotacio.tipus.enum.${registre.tipus}"/></dd>
				<dt><spring:message code="contenidor.registre.camp.numero"/></dt><dd>${registre.numero}</dd>
				<dt><spring:message code="contenidor.registre.camp.data"/></dt><dd><fmt:formatDate value="${registre.data}" pattern="dd/MM/yyyy HH:mm:ss"/></dd>
				<dt><spring:message code="contenidor.registre.camp.assumpte"/></dt><dd>${registre.assumpteResum}</dd>
				<dt><spring:message code="contenidor.registre.camp.origen"/></dt><dd><c:choose><c:when test="${not empty registre.entitatNom}">${registre.entitatNom}</c:when><c:otherwise>${registre.entitatCodi}</c:otherwise></c:choose></dd>
			</dl>
		</div>
		<div class="tab-pane" id="interessats">
			
		</div>
		<div class="tab-pane" id="annexos">
			
		</div>
	</div>
	<div id="modal-botons" class="well">
		<a href="<c:url value="/contenidor/${contenidor.id}"/>" class="btn btn-default modal-tancar"><spring:message code="comu.boto.tancar"/></a>
	</div>
</body>
</html>
