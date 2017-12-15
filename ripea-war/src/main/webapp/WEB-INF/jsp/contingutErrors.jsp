<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib tagdir="/WEB-INF/tags/ripea" prefix="rip"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<c:choose>
	<c:when test="${contingut.expedient}"><c:set var="titol"><spring:message code="contingut.errors.titol.expedient"/></c:set></c:when>
	<c:when test="${contingut.document}"><c:set var="titol"><spring:message code="contingut.errors.titol.document"/></c:set></c:when>
</c:choose>
<html>
<head>
	<title>${titol}</title>
	<rip:modalHead/>
</head>
<body>
	<c:set var="hiHaMetaDades" value="${false}"/>
	<c:set var="hiHaMetaDocuments" value="${false}"/>
	<c:forEach var="error" items="${errors}">
		<c:if test="${error.errorMetaDada}"><c:set var="hiHaMetaDades" value="${true}"/></c:if>
		<c:if test="${error.errorMetaDocument}"><c:set var="hiHaMetaDocuments" value="${true}"/></c:if>
	</c:forEach>
	<c:if test="${hiHaMetaDades}">
		<h4><span class="fa fa-exclamation-triangle text-warning"></span>&nbsp;<spring:message code="contingut.errors.falten.metadades"/></h4>
		<ul class="list-group">
			<c:forEach var="error" items="${errors}">
				<c:if test="${error.errorMetaDada}">
					<li class="list-group-item">${error.metaDada.nom}</li>
				</c:if>
			</c:forEach>
		</ul>
	</c:if>
	<c:if test="${hiHaMetaDocuments}">
		<h4><span class="fa fa-exclamation-triangle text-warning"></span>&nbsp;<spring:message code="contingut.errors.falten.metadocuments"/></h4>
		<ul class="list-group">
			<c:forEach var="error" items="${errors}">
				<c:if test="${error.errorMetaDocument}">
					<li class="list-group-item">${error.metaDocument.nom}</li>
				</c:if>
			</c:forEach>
		</ul>
	</c:if>
	<div id="modal-botons" class="well">
		<a href="<c:url value="/contingut/${contingut.id}"/>" class="btn btn-default" data-modal-cancel="true"><spring:message code="comu.boto.tancar"/></a>
	</div>
</body>
</html>
