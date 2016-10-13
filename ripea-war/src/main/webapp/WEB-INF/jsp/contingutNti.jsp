<%@page import="java.util.Iterator"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib tagdir="/WEB-INF/tags/ripea" prefix="rip"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<c:set var="titol"><spring:message code="contingut.nti.titol"/></c:set>

<html>
<head>
	<title>${titol}</title>
	<rip:modalHead/>
</head>
<body>
	<c:if test="${contingut.expedient}">
		<dl class="dl-horizontal">
			<dt><spring:message code="contingut.nti.camp.versio"/></dt>
			<dd>${contingut.ntiVersionUrl}</dd>
			<dt><spring:message code="contingut.nti.camp.identificador"/></dt>
			<dd>${contingut.ntiIdentificador}</dd>
			<dt><spring:message code="contingut.nti.camp.organ"/></dt>
			<dd>${contingut.ntiOrgano}</dd>
			<dt><spring:message code="contingut.nti.camp.data"/></dt>
			<dd>
				<fmt:formatDate value="${contingut.ntiFechaApertura}" pattern="yyyyMMdd'T'HH:mm:ss"/>
				<%--fmt:formatDate value="${contingut.ntiFechaApertura}" pattern="dd/MM/yyyy HH:mm:ss"/--%>
			</dd>
			<dt><spring:message code="contingut.nti.camp.classificacio"/></dt>
			<dd>${contingut.ntiClasificacion}</dd>
			<dt><spring:message code="contingut.nti.camp.estat"/></dt>
			<dd>${contingut.ntiEstado}</dd>
		</dl>
	</c:if>
	<c:if test="${contingut.document}">
		<dl class="dl-horizontal">
			<dt><spring:message code="contingut.nti.camp.versio"/></dt>
			<dd>${contingut.ntiVersion}</dd>
			<dt><spring:message code="contingut.nti.camp.identificador"/></dt>
			<dd>${contingut.ntiIdentificador}</dd>
			<dt><spring:message code="contingut.nti.camp.organ"/></dt>
			<dd>${contingut.ntiOrgano}</dd>
			<dt><spring:message code="contingut.nti.camp.datcap"/></dt>
			<dd>
				<fmt:formatDate value="${contingut.dataCaptura}" pattern="yyyyMMdd'T'HH:mm:ss"/>
				<%--fmt:formatDate value="${contingut.dataCaptura}" pattern="dd/MM/yyyy HH:mm:ss"/--%>
			</dd>
			<dt><spring:message code="contingut.nti.camp.origen"/></dt>
			<dd><spring:message code="document.nti.origen.enum.${contingut.ntiOrigen}"/></dd>
			<dt><spring:message code="contingut.nti.camp.estela"/></dt>
			<dd><spring:message code="document.nti.estela.enum.${contingut.ntiEstadoElaboracion}"/></dd>
			<dt><spring:message code="contingut.nti.camp.tipdoc"/></dt>
			<dd><spring:message code="document.nti.tipdoc.enum.${contingut.ntiTipoDocumental}"/></dd>
			<c:if test="${not empty contingut.ntiIdDocumentoOrigen}">
				<dt><spring:message code="contingut.nti.camp.iddocori"/></dt>
				<dd>${contingut.ntiIdDocumentoOrigen}</dd>
			</c:if>
			<c:if test="${not empty contingut.ntiTipoFirma}">
				<dt><spring:message code="contingut.nti.camp.tipfir"/></dt>
				<dd>${contingut.ntiTipoFirma}</dd>
			</c:if>
			<c:if test="${not empty contingut.ntiCsv}">
				<dt><spring:message code="contingut.nti.camp.csv"/></dt>
				<dd>${contingut.ntiCsv}</dd>
			</c:if>
			<c:if test="${not empty contingut.ntiCsvRegulacion}">
				<dt><spring:message code="contingut.nti.camp.csvreg"/></dt>
				<dd>${contingut.ntiCsvRegulacion}</dd>
			</c:if>
			
		</dl>
	</c:if>
	<div id="modal-botons" class="well">
		<a href="<c:url value="/contenidor/${contenidor.id}"/>" class="btn btn-default" data-modal-cancel="true"><spring:message code="comu.boto.tancar"/></a>
	</div>
</body>
</html>
