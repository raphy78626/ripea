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
	<div id="modal-botons" class="well">
		<a href="<c:url value="/contenidor/${contenidor.id}"/>" class="btn btn-default" data-modal-cancel="true"><spring:message code="comu.boto.tancar"/></a>
	</div>
</body>
</html>
