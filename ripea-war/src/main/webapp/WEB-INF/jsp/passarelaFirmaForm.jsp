<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib tagdir="/WEB-INF/tags/ripea" prefix="rip"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<c:set var="titol"><spring:message code="contenidor.document.passarelafirma.titol"/></c:set>
<html>
<head>
	<title>${titol}</title>
	<rip:modalHead titol="${titol}" buttonContainerId="botons"/>
	<link href="<c:url value="/css/datepicker.css"/>" rel="stylesheet"/>
	<script src="<c:url value="/js/bootstrap-datepicker.js"/>"></script>
	<script src="<c:url value="/js/datepicker-locales/bootstrap-datepicker.${idioma}.js"/>"></script>
	<link href="<c:url value="/css/select2.css"/>" rel="stylesheet"/>
	<link href="<c:url value="/css/select2-bootstrap.css"/>" rel="stylesheet"/>
	<script src="<c:url value="/js/select2.min.js"/>"></script>
	<script src="<c:url value="/js/select2-locales/select2_locale_${idioma}.js"/>"></script>
</head>
<body>
	<c:if test="${documentVersio.arxiuNom != documentVersio.portafirmesConversioArxiuNom}">
		<div class="alert well-sm alert-warning alert-dismissable">
			<span class="fa fa-exclamation-triangle"></span>
			<spring:message code="contenidor.document.passarelafirma.conversio.avis"/>
			<a data-rdt-link-modal="true" class="btn btn-xs btn-default pull-right" href="pdf">
				<spring:message code="contenidor.document.passarelafirma.conversio.boto.previsualitzar"/>
			</a>
		</div>
	</c:if>
	<c:set var="formAction"><rip:modalUrl value="/contenidor/${document.id}/document/${document.id}/versio/${documentVersio.versio}/firmaPassarela"/></c:set>
	<form:form action="${formAction}" method="post" cssClass="form-horizontal" commandName="passarelaFirmaEnviarCommand" role="form">
		<rip:inputText name="motiu" textKey="contenidor.document.passarelafirma.camp.motiu" required="true"/>
		<rip:inputText name="lloc" textKey="contenidor.document.passarelafirma.camp.lloc" required="true"/>
		<div id="modal-botons" class="well">
			<button type="submit" class="btn btn-success"><span class="fa fa-play"></span> <spring:message code="contenidor.document.passarelafirma.iniciar"/></button>
			<a href="<c:url value="/contenidor/${document.id}"/>" class="btn btn-default modal-tancar"><spring:message code="comu.boto.cancelar"/></a>
		</div>
	</form:form>
</body>
</html>
