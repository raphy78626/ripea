<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib tagdir="/WEB-INF/tags/ripea" prefix="rip"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<c:set var="titol"><spring:message code="arxiu.expedient.agafar.form.titol"/></c:set>
<html>
<head>
	<title>${titol}</title>
	<rip:modalHead titol="${titol}" buttonContainerId="botons"/>
	<link href="<c:url value="/css/select2.css"/>" rel="stylesheet"/>
	<link href="<c:url value="/css/select2-bootstrap.css"/>" rel="stylesheet"/>
	<script src="<c:url value="/js/select2.min.js"/>"></script>
	<script src="<c:url value="/js/select2-locales/select2_locale_${idioma}.js"/>"></script>
</head>
<body>
	<c:set var="formAction"><rip:modalUrl value="/arxiuAdmin/${arxiuExpedientAgafarCommand.arxiuId}/expedient/${arxiuExpedientAgafarCommand.expedientId}/agafar"/></c:set>
	<form:form action="${formAction}" method="post" cssClass="form-horizontal" commandName="arxiuExpedientAgafarCommand" role="form">
		<form:hidden path="arxiuId"/>
		<form:hidden path="expedientId"/>
		<c:url value="/userajax/usuari" var="urlConsultaInicial"/>
		<c:url value="/userajax/usuaris" var="urlConsultaLlistat"/>
		<rip:inputSuggest name="usuariCodi" textKey="arxiu.expedient.agafar.form.camp.usuari" urlConsultaInicial="${urlConsultaInicial}" urlConsultaLlistat="${urlConsultaLlistat}" placeholderKey="esborrat.list.filtre.placeholder.usuari"/>
		<div id="modal-botons" class="well">
			<button type="submit" class="btn btn-success"><span class="fa fa-lock"></span> <spring:message code="comu.boto.agafar"/></button>
			<a href="<c:url value="/arxiuAdmin/${arxiu.id}/expedient"/>" class="btn btn-default modal-tancar"><spring:message code="comu.boto.cancelar"/></a>
		</div>
	</form:form>
</body>
</html>
