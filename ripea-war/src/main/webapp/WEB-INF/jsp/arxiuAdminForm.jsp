<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib tagdir="/WEB-INF/tags/ripea" prefix="rip"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<c:choose>
	<c:when test="${empty arxiuCommand.id}"><c:set var="titol"><spring:message code="arxiu.form.titol.crear"/></c:set></c:when>
	<c:otherwise><c:set var="titol"><spring:message code="arxiu.form.titol.modificar"/></c:set></c:otherwise>
</c:choose>
<html>
<head>
	<title>${titol}</title>
	<rip:modalHead titol="${titol}" buttonContainerId="botons"/>
</head>
<body>

	<c:set var="formAction"><rip:modalUrl value="/arxiuAdmin"/></c:set>
	<form:form action="${formAction}" method="post" cssClass="form-horizontal" commandName="arxiuCommand" role="form">
		<form:hidden path="id"/>
		<form:hidden path="pareId"/>
		<rip:inputText name="nom" textKey="arxiu.form.camp.nom" required="true"/>
		<form:hidden path="unitatCodi"/>
		<div id="modal-botons" class="well">
			<button type="submit" class="btn btn-success"><span class="fa fa-save"></span> <spring:message code="comu.boto.guardar"/></button>
			<a href="<c:url value="/entitat"/>" class="btn btn-default modal-tancar"><spring:message code="comu.boto.cancelar"/></a>
		</div>
	</form:form>

</body>
</html>
