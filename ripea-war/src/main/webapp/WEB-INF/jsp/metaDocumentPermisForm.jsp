<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib tagdir="/WEB-INF/tags/ripea" prefix="rip"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<%
pageContext.setAttribute(
		"principalTipusEnumOptions",
		es.caib.ripea.war.helper.HtmlSelectOptionHelper.getOptionsForEnum(
				es.caib.ripea.core.api.dto.PrincipalTipusEnumDto.class,
				"principal.tipus.enum."));
%>

<c:choose>
	<c:when test="${empty permisCommand.id}"><c:set var="titol"><spring:message code="metadocument.permis.form.titol.crear"/></c:set></c:when>
	<c:otherwise><c:set var="titol"><spring:message code="metadocument.permis.form.titol.modificar"/></c:set></c:otherwise>
</c:choose>
<html>
<head>
	<title>${titol}</title>
	<rip:modalHead titol="${titol}" buttonContainerId="botons"/>
<script>
	$(document).ready(function() {
		$("#modal-botons button[type='submit']").on('click', function() {
			$("form#permisCommand *:disabled").attr('readonly', 'readonly');
			$("form#permisCommand *:disabled").removeAttr('disabled');
		});
	});
</script>
</head>
<body>

	<c:set var="formAction"><rip:modalUrl value="/metaDocument/${metaDocument.id}/permis"/></c:set>
	<form:form action="${formAction}" method="post" cssClass="form-horizontal" commandName="permisCommand">
		<form:hidden path="id"/>
		<rip:inputSelect name="principalTipus" textKey="metadocument.permis.form.camp.tipus" disabled="${not empty permisCommand.id}" optionItems="${principalTipusEnumOptions}" optionValueAttribute="value" optionTextKeyAttribute="text"/>
		<rip:inputText name="principalNom" textKey="metadocument.permis.form.camp.principal" disabled="${not empty permisCommand.id}"/>
		<rip:inputCheckbox name="create" textKey="metadocument.permis.form.camp.creacio"/>
		<rip:inputCheckbox name="read" textKey="metadocument.permis.form.camp.consulta"/>
		<rip:inputCheckbox name="write" textKey="metadocument.permis.form.camp.modificacio"/>
		<rip:inputCheckbox name="delete" textKey="metadocument.permis.form.camp.eliminacio"/>
		<div id="modal-botons" class="well">
			<button type="submit" class="btn btn-success"><span class="fa fa-save"></span> <spring:message code="comu.boto.guardar"/></button>
			<a href="<c:url value="/metaDocument/${metaDocument.id}/permis"/>" class="btn btn-default modal-tancar"><spring:message code="comu.boto.cancelar"/></a>
		</div>
	</form:form>

</body>
</html>
