<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib tagdir="/WEB-INF/tags/ripea" prefix="rip"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<html>
<head>
	<title><spring:message code="contingut.admin.info.titol"/></title>
	<rip:modalHead/>
</head>
<body>
	<dl class="dl-horizontal">
		<dt><spring:message code="contingut.admin.info.camp.contingut"/></dt>
		<dd>
			<c:choose>
				<c:when test="${contingut.escriptori}"><span class="fa fa-desktop" title="<spring:message code="contenidor.contingut.path.escriptori"/>"></span></c:when>
				<c:when test="${contingut.expedient}"><span class="fa fa-briefcase" title="<spring:message code="contenidor.contingut.icona.expedient"/>"></span></c:when>
				<c:when test="${contingut.carpeta}"><span class="fa fa-folder" title="<spring:message code="contenidor.contingut.icona.carpeta"/>"></span></c:when>
				<c:when test="${contingut.document}"><span class="fa fa-file" title="<spring:message code="contenidor.contingut.icona.document"/>"></span></c:when>
				<c:when test="${contingut.arxiv}"><span class="fa fa-archive" title="<spring:message code="contenidor.contingut.icona.arxiu"/>"></span></c:when>
				<c:when test="${contingut.bustia}"><span class="fa fa-inbox" title="<spring:message code="contenidor.contingut.icona.bustia"/>"></span></c:when>
				<c:when test="${contingut.registre}"><span class="fa fa-book" title="<spring:message code="contenidor.contingut.icona.registre"/>"></span></c:when>
			</c:choose>
			${contingut.nom}
		</dd>
		<c:if test="${contingut.expedient}">
			<dt><spring:message code="contingut.admin.info.camp.numero"/></dt>
			<dd>${contingut.numero}</dd>
			<dt><spring:message code="contingut.admin.info.camp.estat"/></dt>
			<dd><spring:message code="expedient.estat.enum.${contingut.estat}"/></dd>
		</c:if>
		<c:if test="${contingut.expedient or contingut.document and not empty contingut.metaNode}">
			<dt><spring:message code="contingut.admin.info.camp.metanode"/></dt>
			<dd>${contingut.metaNode.nom}</dd>
		</c:if>
		<dt><spring:message code="contingut.admin.info.camp.usuari.creacio"/></dt>
		<dd>${contingut.createdBy.nom}</dd>
		<dt><spring:message code="contingut.admin.info.camp.data.creacio"/></dt>
		<dd><fmt:formatDate value="${contingut.createdDate}" pattern="dd/MM/yyyy HH:mm:ss"/></dd>
		<dt><spring:message code="contingut.admin.info.camp.usuari.modificacio"/></dt>
		<dd>${contingut.lastModifiedBy.nom}</dd>
		<dt><spring:message code="contingut.admin.info.camp.data.modificacio"/></dt>
		<dd><fmt:formatDate value="${contingut.lastModifiedDate}" pattern="dd/MM/yyyy HH:mm:ss"/></dd>
	</dl>
	<div id="modal-botons" class="well">
		<a href="<c:url value="/contenidor/${contenidor.id}"/>" class="btn btn-default" data-modal-cancel="true"><spring:message code="comu.boto.tancar"/></a>
	</div>
</body>