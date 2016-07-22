<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib tagdir="/WEB-INF/tags/ripea" prefix="rip"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<c:choose>
	<c:when test="${empty metaNodeMetaDadaCommand.id}"><c:set var="titol"><spring:message code="metadocument.metadada.form.titol.crear"/></c:set></c:when>
	<c:otherwise><c:set var="titol"><spring:message code="metadocument.metadada.form.titol.modificar"/></c:set></c:otherwise>
</c:choose>
<%
pageContext.setAttribute(
		"metaDadaTipusEnumOptions",
		es.caib.ripea.war.helper.EnumHelper.getOptionsForEnum(
				es.caib.ripea.core.api.dto.MetaDadaTipusEnumDto.class,
				"metadada.tipus.enum."));
pageContext.setAttribute(
		"multiplicitatEnumOptions",
		es.caib.ripea.war.helper.EnumHelper.getOptionsForEnum(
				es.caib.ripea.core.api.dto.MultiplicitatEnumDto.class,
				"multiplicitat.enum."));
%>

<html>
<head>
	<title>${titol}</title>
<c:if test="${empty metaNodeMetaDadaCommand.id}">
	<link href="<c:url value="/css/select2.css"/>" rel="stylesheet"/>
	<link href="<c:url value="/css/select2-bootstrap.css"/>" rel="stylesheet"/>
	<script src="<c:url value="/js/select2.min.js"/>"></script>
<script>
$(document).ready(function() {
	$("#metaDadaId").select2({
		placeholder: "<spring:message code="metadocument.metadada.form.placeholder.metadada"/>",
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

	<c:set var="formAction"><rip:modalUrl value="/metaDocument/${metaDocument.id}/metaDada/save"/></c:set>
	<form:form action="${formAction}" method="post" cssClass="form-horizontal" commandName="metaNodeMetaDadaCommand">
		<form:hidden path="id"/>
		<c:choose>
			<c:when test="${empty metaNodeMetaDadaCommand.id}">
				<rip:inputSelect name="metaDadaId" textKey="metadocument.metadada.form.camp.metadada" optionItems="${metaDades}" optionValueAttribute="id" optionTextAttribute="nom"/>
			</c:when>
			<c:otherwise>
				<form:hidden path="metaDadaId"/>
				<rip:inputFixed textKey="metadocument.metadada.form.camp.codi">${metaNodeMetaDada.metaDada.codi}</rip:inputFixed>
				<rip:inputFixed textKey="metadocument.metadada.form.camp.nom">${metaNodeMetaDada.metaDada.nom}</rip:inputFixed>
				<rip:inputFixed textKey="metadocument.metadada.form.camp.tipus"><spring:message code="metadada.tipus.enum.${metaNodeMetaDada.metaDada.tipus}"/></rip:inputFixed>
			</c:otherwise>
		</c:choose>
		<rip:inputSelect name="multiplicitat" textKey="metadocument.metadada.form.camp.multiplicitat" disabled="${not empty permisCommand.id}" optionItems="${multiplicitatEnumOptions}" optionValueAttribute="value" optionTextKeyAttribute="text"/>
		<div id="modal-botons" class="well">
			<button type="submit" class="btn btn-success"><span class="fa fa-save"></span>&nbsp;<spring:message code="comu.boto.guardar"/></button>
			<a href="<c:url value="/metaDocument/${metaDocument.id}/metaDada"/>" class="btn btn-default modal-tancar"><spring:message code="comu.boto.cancelar"/></a>
		</div>
	</form:form>

</body>
</html>
