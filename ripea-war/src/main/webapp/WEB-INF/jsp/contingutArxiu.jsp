<%@page import="java.util.Iterator"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib tagdir="/WEB-INF/tags/ripea" prefix="rip"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<c:set var="titol"><spring:message code="contingut.arxiu.titol"/></c:set>

<html>
<head>
	<title>${titol}</title>
	<rip:modalHead/>
<script type="text/javascript">
$(document).ready(function() {
	$("#codiJson").text(JSON.stringify(${arxiuInfo.json}, null, 4));
});
</script>
</head>
<body>
	<ul class="nav nav-tabs" role="tablist">
		<li role="presentation" class="active"><a href="#info" aria-controls="info" role="tab" data-toggle="tab"><spring:message code="contingut.arxiu.tab.info"/></a></li>
		<c:if test="${not empty arxiuInfo.fills}">
			<li role="presentation"><a href="#fills" aria-controls="fills" role="tab" data-toggle="tab"><spring:message code="contingut.arxiu.tab.fills"/> <span class="badge badge-default">${fn:length(arxiuInfo.fills)}</span></a></li>
		</c:if>
		<c:if test="${not empty arxiuInfo.metadades}">
			<li role="presentation"><a href="#metadades" aria-controls="metadades" role="tab" data-toggle="tab"><spring:message code="contingut.arxiu.tab.metadades"/> <span class="badge badge-default">${fn:length(arxiuInfo.metadades)}</span></a></li>
		</c:if>
		<c:if test="${not empty arxiuInfo.aspectes}">
			<li role="presentation"><a href="#aspectes" aria-controls="aspectes" role="tab" data-toggle="tab"><spring:message code="contingut.arxiu.tab.aspectes"/> <span class="badge badge-default">${fn:length(arxiuInfo.aspectes)}</span></a></li>
		</c:if>
		
		<c:if test="${not empty arxiuInfo.json}">
			<li role="presentation"><a href="#json" aria-controls="json" role="tab" data-toggle="tab"><spring:message code="contingut.arxiu.tab.json"/></a></li>
		</c:if>
	</ul>
	<br/>
	<div class="tab-content">
		<div role="tabpanel" class="tab-pane active" id="info">
			<dl class="dl-horizontal">
				<dt><spring:message code="contingut.arxiu.camp.node.id"/></dt>
				<dd>${arxiuInfo.nodeId}</dd>
				<dt><spring:message code="contingut.arxiu.camp.nom"/></dt>
				<dd>${arxiuInfo.nom}</dd>
				<c:if test="${not empty arxiuInfo.descripcio}">
					<dt><spring:message code="contingut.arxiu.camp.descripcio"/></dt>
					<dd>${arxiuInfo.descripcio}</dd>
				</c:if>
				<c:if test="${not empty arxiuInfo.serieDocumental}">
					<dt><spring:message code="contingut.arxiu.camp.serie.doc"/></dt>
					<dd>${arxiuInfo.serieDocumental}</dd>
				</c:if>
				<c:if test="${not empty arxiuInfo.aplicacio}">
					<dt><spring:message code="contingut.arxiu.camp.aplicacio"/></dt>
					<dd>${arxiuInfo.aplicacio}</dd>
				</c:if>
				<c:if test="${not empty arxiuInfo.eniVersio}">
					<dt><spring:message code="contingut.arxiu.camp.eni.versio"/></dt>
					<dd>${arxiuInfo.eniVersio}</dd>
				</c:if>
				<c:if test="${not empty arxiuInfo.eniIdentificador}">
					<dt><spring:message code="contingut.arxiu.camp.eni.identificador"/></dt>
					<dd>${arxiuInfo.eniIdentificador}</dd>
				</c:if>
				<c:if test="${not empty arxiuInfo.eniOrigen}">
					<dt><spring:message code="contingut.arxiu.camp.eni.origen"/></dt>
					<dd><spring:message code="document.nti.origen.enum.${arxiuInfo.eniOrigen}"/></dd>
				</c:if>
				<c:choose>
					<c:when test="${contingut.expedient}">
						<dt><spring:message code="contingut.arxiu.camp.eni.data.captura"/></dt>
						<dd>
							<fmt:formatDate value="${arxiuInfo.eniDataObertura}" pattern="dd/MM/yyyy HH:mm:ss"/>
						</dd>
						<dt><spring:message code="contingut.arxiu.camp.eni.classificacio"/></dt>
						<dd>${arxiuInfo.eniClassificacio}</dd>
						<dt><spring:message code="contingut.arxiu.camp.eni.estat"/></dt>
						<dd><spring:message code="expedient.estat.enum.${arxiuInfo.eniEstat}"/></dd>
					</c:when>
					<c:when test="${contingut.document}">
						<dt><spring:message code="contingut.arxiu.camp.eni.data.obertura"/></dt>
						<dd>
							<fmt:formatDate value="${arxiuInfo.eniDataCaptura}" pattern="dd/MM/yyyy HH:mm:ss"/>
						</dd>
						<dt><spring:message code="contingut.arxiu.camp.eni.estat.elab"/></dt>
						<dd><spring:message code="document.nti.estela.enum.${arxiuInfo.eniEstatElaboracio}"/></dd>
						<dt><spring:message code="contingut.arxiu.camp.eni.tipus.doc"/></dt>
						<dd><spring:message code="document.nti.tipdoc.enum.${arxiuInfo.eniTipusDocumental}"/></dd>
						<dt><spring:message code="contingut.arxiu.camp.eni.format.nom"/></dt>
						<dd>${arxiuInfo.eniFormatNom}</dd>
						<dt><spring:message code="contingut.arxiu.camp.eni.format.ext"/></dt>
						<dd>${arxiuInfo.eniFormatExtensio}</dd>
					</c:when>
				</c:choose>
				<c:if test="${not empty arxiuInfo.eniOrgans}">
					<dt><spring:message code="contingut.arxiu.camp.eni.organs"/></dt>
					<dd>
						<c:forEach var="organ" items="${arxiuInfo.eniOrgans}" varStatus="status">
							${organ}<c:if test="${not status.last}">,</c:if>
						</c:forEach>
					</dd>
				</c:if>
				<c:if test="${not empty arxiuInfo.eniInteressats}">
					<dt><spring:message code="contingut.arxiu.camp.eni.interessats"/></dt>
					<dd>
						<c:forEach var="interessat" items="${arxiuInfo.eniInteressats}" varStatus="status">
							${interessat}<c:if test="${not status.last}">,</c:if>
						</c:forEach>
					</dd>
				</c:if>
				<c:if test="${not empty arxiuInfo.eniDocumentOrigenId}">
					<dt><spring:message code="contingut.arxiu.camp.eni.doc.orig.id"/></dt>
					<dd>${arxiuInfo.eniDocumentOrigenId}</dd>
				</c:if>
				<c:if test="${not empty arxiuInfo.eniFirmaTipus}">
					<dt><spring:message code="contingut.arxiu.camp.eni.firma.tipus"/></dt>
					<dd>
						<c:forEach var="tipus" items="${arxiuInfo.eniFirmaTipus}" varStatus="status">
							${tipus}<c:if test="${not status.last}">,</c:if>
						</c:forEach>
					</dd>
				</c:if>
				<c:if test="${not empty arxiuInfo.eniFirmaCsv}">
					<dt><spring:message code="contingut.arxiu.camp.eni.firma.csv"/></dt>
					<dd>
						<c:forEach var="csv" items="${arxiuInfo.eniFirmaCsv}" varStatus="status">
							${csv}<c:if test="${not status.last}">,</c:if>
						</c:forEach>
					</dd>
				</c:if>
				<c:if test="${not empty arxiuInfo.eniFirmaCsvDefinicio}">
					<dt><spring:message code="contingut.arxiu.camp.eni.firma.csvdef"/></dt>
					<dd>
						<c:forEach var="csvdef" items="${arxiuInfo.eniFirmaCsvDefinicio}" varStatus="status">
							${csvdef}<c:if test="${not status.last}">,</c:if>
						</c:forEach>
					</dd>
				</c:if>
			</dl>
		</div>
		<c:if test="${not empty arxiuInfo.fills}">
			<div role="tabpanel" class="tab-pane" id="fills">
				<table class="table table-bordered">
					<c:forEach var="fill" items="${arxiuInfo.fills}" varStatus="status">
						<tr>
							<td width="10%">${fill.tipus}</td>
							<td>${fill.nom}</td>
						</tr>
					</c:forEach>
				</table>
			</div>
		</c:if>
		<c:if test="${not empty arxiuInfo.metadades}">
			<div role="tabpanel" class="tab-pane" id="metadades">
				<table class="table table-bordered">
					<c:forEach var="metadada" items="${arxiuInfo.metadades}" varStatus="status">
						<tr>
							<td><strong>${metadada.key}</strong></td>
							<td>${metadada.value}</td>
						</tr>
					</c:forEach>
				</table>
			</div>
		</c:if>
		<c:if test="${not empty arxiuInfo.aspectes}">
			<div role="tabpanel" class="tab-pane" id="aspectes">
				<ul class="list-group">
					<c:forEach var="aspecte" items="${arxiuInfo.aspectes}" varStatus="status">
					  <li class="list-group-item">${aspecte}</li>
					</c:forEach>
				</ul>
			</div>
		</c:if>
		<c:if test="${not empty arxiuInfo.json}">
			<div role="tabpanel" class="tab-pane" id="json">
				<pre id="codiJson" style="height:300px; margin: 12px"></pre>
			</div>
		</c:if>
	</div>
	<div id="modal-botons" class="well">
		<a href="<c:url value="/contenidor/${contenidor.id}"/>" class="btn btn-default" data-modal-cancel="true"><spring:message code="comu.boto.tancar"/></a>
	</div>
</body>
</html>
