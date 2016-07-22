<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib tagdir="/WEB-INF/tags/ripea" prefix="rip"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

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

<c:choose>
	<c:when test="${empty metaDadaCommand.id}"><c:set var="titol"><spring:message code="metadada.form.titol.crear"/></c:set></c:when>
	<c:otherwise><c:set var="titol"><spring:message code="metadada.form.titol.modificar"/></c:set></c:otherwise>
</c:choose>
<html>
<head>
	<title>${titol}</title>
	<rip:modalHead titol="${titol}" buttonContainerId="botons"/>
<script>
	var metaDadaTipusText = new Array();
	<c:forEach var="tipus" items="${metaDadaTipus}">
	metaDadaTipusText["${tipus}"] = "<spring:message code="metadada.tipus.enum.${tipus}"/>";
	</c:forEach>
</script>
</head>
<body>

	<c:set var="formAction"><rip:modalUrl value="/metaDada"/></c:set>
	<form:form action="${formAction}" method="post" cssClass="form-horizontal" commandName="metaDadaCommand">
		<form:hidden path="id"/>
		<form:hidden path="entitatId"/>
		<rip:inputText name="codi" textKey="metadada.form.camp.codi" required="true"/>
		<rip:inputText name="nom" textKey="metadada.form.camp.nom" required="true"/>
		<rip:inputTextarea name="descripcio" textKey="metadada.form.camp.descripcio"/>
		<rip:inputSelect name="tipus" textKey="metadada.form.camp.tipus" optionItems="${metaDadaTipusEnumOptions}" optionValueAttribute="value" optionTextKeyAttribute="text"/>
		<rip:inputCheckbox name="globalExpedient" textKey="metadada.form.camp.global.expedient"/>
		<rip:inputCheckbox name="globalDocument" textKey="metadada.form.camp.global.document"/>
		<rip:inputSelect name="globalMultiplicitat" textKey="metadada.form.camp.global.multiplicitat" optionItems="${multiplicitatEnumOptions}" optionValueAttribute="value" optionTextKeyAttribute="text"/>
		<rip:inputCheckbox name="globalReadOnly" textKey="metadada.form.camp.global.readonly"/>
		<div id="modal-botons" class="well">
			<button type="submit" class="btn btn-success"><span class="fa fa-save"></span> <spring:message code="comu.boto.guardar"/></button>
			<a href="<c:url value="/metaDada"/>" class="btn btn-default modal-tancar"><spring:message code="comu.boto.cancelar"/></a>
		</div>
	</form:form>

</body>
</html>
