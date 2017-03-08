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
		<li role="presentation"><a href="#json" aria-controls="dades_nti" role="tab" data-toggle="tab"><spring:message code="contingut.arxiu.tab.json"/></a></li>
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
				<dt><spring:message code="contingut.arxiu.camp.eni.versio"/></dt>
				<dd>${arxiuInfo.eniVersio}</dd>
				<dt><spring:message code="contingut.arxiu.camp.eni.identificador"/></dt>
				<dd>${arxiuInfo.eniIdentificador}</dd>
				<dt><spring:message code="contingut.arxiu.camp.eni.origen"/></dt>
				<dd>${arxiuInfo.eniOrigen}</dd>
				<c:choose>
					<c:when test="${contingut.expedient}">
						<dt><spring:message code="contingut.arxiu.camp.eni.data.captura"/></dt>
						<dd>
							<fmt:formatDate value="${arxiuInfo.eniDataInicial}" pattern="yyyyMMdd'T'HH:mm:ss"/>
						</dd>
						<dt><spring:message code="contingut.arxiu.camp.eni.classificacio"/></dt>
						<dd>${arxiuInfo.eniClassificacio}</dd>
						<dt><spring:message code="contingut.arxiu.camp.eni.estat"/></dt>
						<dd>${arxiuInfo.eniEstat}</dd>
					</c:when>
					<c:when test="${contingut.document}">
						<dt><spring:message code="contingut.arxiu.camp.eni.data.obertura"/></dt>
						<dd>
							<fmt:formatDate value="${arxiuInfo.eniDataInicial}" pattern="yyyyMMdd'T'HH:mm:ss"/>
						</dd>
						<dt><spring:message code="contingut.arxiu.camp.eni.estat.elab"/></dt>
						<dd>${arxiuInfo.eniEstatElaboracio}</dd>
						<dt><spring:message code="contingut.arxiu.camp.eni.tipus.doc"/></dt>
						<dd>${arxiuInfo.eniTipusDocumental}</dd>
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
				<c:if test="${not empty arxiuInfo.serieDocumental}">
					<dt><spring:message code="contingut.arxiu.camp.serie.doc"/></dt>
					<dd>${arxiuInfo.serieDocumental}</dd>
				</c:if>
				<c:if test="${not empty arxiuInfo.suport}">
					<dt><spring:message code="contingut.arxiu.camp.suport"/></dt>
					<dd>${arxiuInfo.suport}</dd>
				</c:if>
			</dl>
		</div>
		<div role="tabpanel" class="tab-pane" id="json">
			<pre id="codiJson" style="height:300px; margin: 12px"></pre>
		</div>
	</div>
	<div id="modal-botons" class="well">
		<a href="<c:url value="/contenidor/${contenidor.id}"/>" class="btn btn-default" data-modal-cancel="true"><spring:message code="comu.boto.tancar"/></a>
	</div>
</body>
</html>
