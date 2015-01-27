<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib tagdir="/WEB-INF/tags/ripea" prefix="rip"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<%
pageContext.setAttribute(
		"multiplicitatEnumOptions",
		es.caib.ripea.war.helper.HtmlSelectOptionHelper.getOptionsForEnum(
				es.caib.ripea.core.api.dto.MultiplicitatEnumDto.class,
				"multiplicitat.enum."));
%>

<c:choose>
	<c:when test="${empty metaExpedientCommand.id}"><c:set var="titol"><spring:message code="metadocument.form.titol.crear"/></c:set></c:when>
	<c:otherwise><c:set var="titol"><spring:message code="metadocument.form.titol.modificar"/></c:set></c:otherwise>
</c:choose>
<html>
<head>
	<title>${titol}</title>
	<link href="<c:url value="/css/jasny-bootstrap.min.css"/>" rel="stylesheet">
	<script src="<c:url value="/js/jasny-bootstrap.min.js"/>"></script>
	<rip:modalHead titol="${titol}" buttonContainerId="botons"/>
</head>
<body>

	<c:set var="formAction"><rip:modalUrl value="/metaDocument"/></c:set>
	<form:form action="${formAction}" method="post" cssClass="form-horizontal" commandName="metaDocumentCommand" enctype="multipart/form-data">
		<form:hidden path="id"/>
		<form:hidden path="entitatId"/>
		<rip:inputText name="codi" textKey="metadocument.form.camp.codi" required="true"/>
		<rip:inputText name="nom" textKey="metadocument.form.camp.nom" required="true"/>
		<rip:inputTextarea name="descripcio" textKey="metadocument.form.camp.descripcio"/>
		<rip:inputCheckbox name="globalExpedient" textKey="metadada.form.camp.global.expedient"/>
		<rip:inputSelect name="globalMultiplicitat" textKey="metadada.form.camp.global.multiplicitat" optionItems="${multiplicitatEnumOptions}" optionValueAttribute="value" optionTextKeyAttribute="text"/>
		<rip:inputCheckbox name="globalReadOnly" textKey="metadada.form.camp.global.readonly"/>
		<rip:inputText name="custodiaPolitica" textKey="metadocument.form.camp.custodia.politica"/>
		<c:choose>
			<c:when test="${isPortafirmesDocumentTipusSuportat}">
				<rip:inputSelect name="portafirmesDocumentTipus" textKey="metadocument.form.camp.portafirmes.document.tipus" optionItems="${portafirmesDocumentTipus}" optionValueAttribute="id" optionTextAttribute="nom" emptyOption="true"/>
			</c:when>
			<c:otherwise>
				<rip:inputText name="portafirmesDocumentTipus" textKey="metadocument.form.camp.portafirmes.document.tipus"/>
			</c:otherwise>
		</c:choose>
		<rip:inputText name="portafirmesFluxId" textKey="metadocument.form.camp.portafirmes.flux.id"/>
		<rip:inputText name="signaturaTipusMime" textKey="metadocument.form.camp.signatura.tipus.mime"/>
		<rip:inputFile name="plantilla" textKey="metadocument.form.camp.plantilla"/>
		<div id="modal-botons" class="well">
			<button type="submit" class="btn btn-success"><span class="fa fa-save"></span>&nbsp;<spring:message code="comu.boto.guardar"/></button>
			<a href="<c:url value="/metaDocument"/>" class="btn btn-default modal-tancar"><spring:message code="comu.boto.cancelar"/></a>
		</div>
	</form:form>

</body>
</html>
