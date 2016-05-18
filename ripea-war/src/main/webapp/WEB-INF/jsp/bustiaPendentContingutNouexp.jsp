<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib tagdir="/WEB-INF/tags/ripea" prefix="rip"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<c:set var="titol"><spring:message code="bustia.pendent.contingut.nouexp.titol"/></c:set>
<html>
<head>
	<title>${titol}</title>
	<rip:modalHead titol="${titol}"/>
	<link href="<c:url value="/css/select2.css"/>" rel="stylesheet"/>
	<link href="<c:url value="/css/select2-bootstrap.css"/>" rel="stylesheet"/>
	<script src="<c:url value="/js/select2.min.js"/>"></script>
</head>
<body>
	<form:form action="" method="post" cssClass="form-horizontal" commandName="expedientCommand">
		<form:hidden path="entitatId"/>
		<form:hidden path="pareId"/>
		<form:hidden path="contingutId"/>
		<rip:inputSelect name="metaNodeId" textKey="contenidor.expedient.form.camp.metanode" required="true" optionItems="${metaExpedients}" optionValueAttribute="id" optionTextAttribute="nom" disabled="${not empty expedientCommand.id}"/>
		<rip:inputSelect name="arxiuId" textKey="contenidor.expedient.form.camp.arxiu" required="true" optionItems="${arxius}" optionValueAttribute="id" optionTextAttribute="nomAmbUnitat"/>
		<rip:inputText name="nom" textKey="contenidor.expedient.form.camp.nom" required="true"/>
		<div id="modal-botons" class="well">
			<button type="submit" class="btn btn-success"><span class="fa fa-save"></span> <spring:message code="comu.boto.guardar"/></button>
			<a href="<c:url value="/contenidor/${expedientCommand.pareId}"/>" class="btn btn-default modal-tancar"><spring:message code="comu.boto.cancelar"/></a>
		</div>
	</form:form>
</body>
</html>