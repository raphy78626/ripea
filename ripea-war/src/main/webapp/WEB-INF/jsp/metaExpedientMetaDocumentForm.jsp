<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib tagdir="/WEB-INF/tags/ripea" prefix="rip"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<c:choose>
	<c:when test="${empty metaNodeMetaDadaCommand.id}"><c:set var="titol"><spring:message code="metaexpedient.metadocument.form.titol.crear"/></c:set></c:when>
	<c:otherwise><c:set var="titol"><spring:message code="metaexpedient.metadocument.form.titol.modificar"/></c:set></c:otherwise>
</c:choose>
<%
pageContext.setAttribute(
		"multiplicitatEnumOptions",
		es.caib.ripea.war.helper.EnumHelper.getOptionsForEnum(
				es.caib.ripea.core.api.dto.MultiplicitatEnumDto.class,
				"multiplicitat.enum."));
%>

<html>
<head>
	<title>${titol}</title>
<c:if test="${empty metaExpedientMetaDocumentCommand.id}">
	<link href="<c:url value="/css/select2.css"/>" rel="stylesheet"/>
	<link href="<c:url value="/css/select2-bootstrap.css"/>" rel="stylesheet"/>
	<script src="<c:url value="/js/select2.min.js"/>"></script>
<script>
$(document).ready(function() {
	$("#metaDocumentId").select2({
		placeholder: "<spring:message code="metaexpedient.metadocument.form.placeholder.metadocument"/>",
		width: '100%'
	}).on('select2-open', function() {
		var iframe = $('.modal-body iframe', window.parent.document);
		var height = $('html').height() + $(".select2-drop").height() - 60;
		iframe.height(height + 'px');
	}).on('select2-close', function() {
		var iframe = $('.modal-body iframe', window.parent.document);
		var height = $('html').height();
		iframe.height(height + 'px');
	});
});
</script>
</c:if>
	<rip:modalHead titol="${titol}" buttonContainerId="botons"/>
</head>
<body>

	<c:set var="formAction"><rip:modalUrl value="/metaExpedient/${metaExpedient.id}/metaDocument/save"/></c:set>
	<form:form action="${formAction}" method="post" cssClass="form-horizontal" commandName="metaExpedientMetaDocumentCommand">
		<form:hidden path="id"/>
		<c:choose>
			<c:when test="${empty metaExpedientMetaDocumentCommand.id}">
				<rip:inputSelect name="metaDocumentId" textKey="metaexpedient.metadocument.form.camp.metadocument" optionItems="${metaDocuments}" optionValueAttribute="id" optionTextAttribute="nom"/>
			</c:when>
			<c:otherwise>
				<form:hidden path="metaDocumentId"/>
				<rip:inputFixed textKey="metaexpedient.metadocument.form.camp.codi">${metaExpedientMetaDocument.metaDocument.codi}</rip:inputFixed>
				<rip:inputFixed textKey="metaexpedient.metadocument.form.camp.nom">${metaExpedientMetaDocument.metaDocument.nom}</rip:inputFixed>
			</c:otherwise>
		</c:choose>
		<rip:inputSelect name="multiplicitat" textKey="metaexpedient.metadocument.form.camp.multiplicitat" optionItems="${multiplicitatEnumOptions}" optionValueAttribute="value" optionTextKeyAttribute="text"/>
		<div id="modal-botons" class="well">
			<button type="submit" class="btn btn-success"><span class="fa fa-save"></span>&nbsp;<spring:message code="comu.boto.guardar"/></button>
			<a href="<c:url value="/metaExpedient/${metaExpedient.id}/metaDada"/>" class="btn btn-default modal-tancar"><spring:message code="comu.boto.cancelar"/></a>
		</div>
	</form:form>

</body>
</html>
