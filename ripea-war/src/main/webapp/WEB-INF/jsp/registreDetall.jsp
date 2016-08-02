<%@page import="java.util.Iterator"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib tagdir="/WEB-INF/tags/ripea" prefix="rip"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<c:set var="titol"><spring:message code="registre.titol"/></c:set>
<html>
<head>
	<title>${titol}</title>
	<rip:modalHead titol="${titol}" buttonContainerId="botons"/>
<style>
.tab-content {
    margin-top: 0.8em;
}
</style>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="#informacio" data-toggle="tab"><spring:message code="registre.pipella.informacio"/></a>
		</li>
		<li>
			<a href="#interessats" data-toggle="tab"><spring:message code="registre.pipella.interessats"/>&nbsp;<span class="badge">${fn:length(registre.interessats)}</span></a>
		</li>
		<li>
			<a href="#annexos" data-toggle="tab"><spring:message code="registre.pipella.annexos"/>&nbsp;<span class="badge">${fn:length(registre.annexos)}</span></a>
		</li>
	</ul>
	<div class="tab-content">
		<div class="tab-pane active in" id="informacio">
			<dl class="dl-horizontal">
				<dt><spring:message code="registre.camp.tipus"/></dt><dd><spring:message code="registre.anotacio.tipus.enum.${registre.registreTipus}"/></dd>
				<dt><spring:message code="registre.camp.numero"/></dt><dd>${registre.identificador}</dd>
				<dt><spring:message code="registre.camp.data"/></dt><dd><fmt:formatDate value="${registre.data}" pattern="dd/MM/yyyy HH:mm:ss"/></dd>
				<c:if test="${registre.registreTipus == 'ENTRADA'}">
					<dt><spring:message code="registre.camp.desti"/></dt><dd>${registre.unitatAdministrativa}</dd>				
				</c:if>
				<c:if test="${registre.registreTipus == 'SORTIDA'}">
					<dt><spring:message code="registre.camp.origen"/></dt><dd>${registre.unitatAdministrativa}</dd>
				</c:if>
				<dt><spring:message code="registre.camp.entitat"/></dt><dd>${registre.entitatDescripcio} (${registre.entitatCodi})</dd>
				<dt><spring:message code="registre.camp.oficina"/></dt><dd>${registre.oficinaDescripcio} (${registre.oficinaCodi})</dd>
				<dt><spring:message code="registre.camp.llibre"/></dt><dd>${registre.llibreDescripcio} (${registre.llibreCodi})</dd>
				<dt><spring:message code="registre.camp.extracte"/></dt><dd>${registre.extracte}</dd>
				<dt><spring:message code="registre.camp.assumpte.tipus"/></dt><dd>${registre.assumpteTipusDescripcio} (${registre.assumpteTipusCodi})</dd>
				<dt><spring:message code="registre.camp.idioma"/></dt><dd>${registre.idiomaDescripcio} (${registre.idiomaCodi})</dd>
				<c:if test="${not empty registre.transportTipusCodi}">
					<dt><spring:message code="registre.camp.transport.tipus"/></dt><dd>${registre.transportTipusDescripcio} (${registre.transportTipusCodi})</dd>
				</c:if>
				<c:if test="${not empty registre.transportNumero}">
					<dt><spring:message code="registre.camp.transport.numero"/></dt><dd>${registre.transportNumero}</dd>
				</c:if>
				<c:if test="${not empty registre.usuariCodi}">
					<dt><spring:message code="registre.camp.usuari"/></dt><dd>${registre.usuariNom} (${registre.usuariCodi})</dd>
				</c:if>
				<c:if test="${not empty registre.aplicacioCodi}">
					<dt><spring:message code="registre.camp.aplicacio"/></dt><dd>${registre.aplicacioVersio} (${registre.aplicacioCodi})</dd>
				</c:if>
				<c:if test="${not empty registre.expedientNumero}">
					<dt><spring:message code="registre.camp.expedient"/></dt><dd>${registre.expedientNumero}</dd>
				</c:if>
				<c:if test="${not empty registre.observacions}">
					<dt><spring:message code="registre.camp.observacions"/></dt><dd>${registre.observacions}</dd>
				</c:if>
				<dt><spring:message code="registre.camp.ripea.alta"/></dt><dd><fmt:formatDate value="${registre.createdDate}" pattern="dd/MM/yyyy HH:mm:ss"/></dd>
			</dl>
		</div>
		<div class="tab-pane" id="interessats">
			<table class="table table-striped table-bordered">
				<thead>
					<tr>
						<th><spring:message code="registre.camp.interessat.tipus"/></th>
						<th><spring:message code="registre.camp.interessat.document"/></th>
						<th><spring:message code="registre.camp.interessat.nom"/></th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="interessat" items="${registre.interessats}">
						<tr>
							<td><spring:message code="registre.interessat.tipus.enum.${interessat.tipus}"/></td>
							<td>${interessat.documentTipus}: ${interessat.documentNum}</td>
							<c:choose>
								<c:when test="${interessat.tipus == 'PERSONA_FIS'}">
									<td>${interessat.nom} ${interessat.llinatge1} ${interessat.llinatge2}</td>
								</c:when>
								<c:otherwise>
									<td>${interessat.raoSocial}</td>
								</c:otherwise>
							</c:choose>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
		<div class="tab-pane" id="annexos">
			
		</div>
	</div>
	<div id="modal-botons" class="well">
		<a href="<c:url value="/contenidor/${contenidor.id}"/>" class="btn btn-default modal-tancar"><spring:message code="comu.boto.tancar"/></a>
	</div>
</body>
</html>
