<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib tagdir="/WEB-INF/tags/ripea" prefix="rip"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<c:set var="titol"><spring:message code="bustia.pendent.registre.rebutjar.titol"/></c:set>
<html>
<head>
	<title>${titol}</title>
	<rip:modalHead titol="${titol}"/>
</head>
<body>
	<form:form action="" class="form-horizontal" commandName="registreRebutjarCommand">
		<form:hidden path="registreId"/>
		<rip:inputTextarea name="motiu" textKey="bustia.pendent.registre.rebutjar.motiu" required="true"/>
		<div id="modal-botons" class="well">
			<button type="submit" class="btn btn-success"><span class="fa fa-trash-o"></span> <spring:message code="comu.boto.rebutjar"/></button>
			<a href="<c:url value="/contenidor/${contenidorOrigen.pare.id}"/>" class="btn btn-default modal-tancar"><spring:message code="comu.boto.cancelar"/></a>
		</div>
	</form:form>
</body>
</html>