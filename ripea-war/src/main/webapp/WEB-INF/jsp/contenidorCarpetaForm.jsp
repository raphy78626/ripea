<%@page import="java.util.Iterator"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib tagdir="/WEB-INF/tags/ripea" prefix="rip"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%
java.util.List<es.caib.ripea.war.helper.HtmlSelectOptionHelper.HtmlOption> options = es.caib.ripea.war.helper.HtmlSelectOptionHelper.getOptionsForEnum(
		es.caib.ripea.core.api.dto.CarpetaTipusEnumDto.class,
		"carpeta.tipus.enum.");
java.util.Iterator<es.caib.ripea.war.helper.HtmlSelectOptionHelper.HtmlOption> it = options.iterator();
while (it.hasNext()) {
	es.caib.ripea.war.helper.HtmlSelectOptionHelper.HtmlOption option = it.next();
	if (option.getValue().equals("NOUVINGUT"))
		it.remove();
}
pageContext.setAttribute(
		"carpetaTipusEnumOptions",
		options);
%>
<c:choose>
	<c:when test="${empty carpetaCommand.id}"><c:set var="titol"><spring:message code="contenidor.carpeta.form.titol.crear"/></c:set></c:when>
	<c:otherwise><c:set var="titol"><spring:message code="contenidor.carpeta.form.titol.modificar"/></c:set></c:otherwise>
</c:choose>
<html>
<head>
	<title>${titol}</title>
	<rip:modalHead titol="${titol}" buttonContainerId="botons"/>
</head>
<body>
	<c:choose>
		<c:when test="${empty carpetaCommand.id}"><c:set var="formAction"><rip:modalUrl value="/contenidor/${carpetaCommand.pareId}/carpeta/new"/></c:set></c:when>
		<c:otherwise><c:set var="formAction"><rip:modalUrl value="/contenidor/${carpetaCommand.pareId}/carpeta/update"/></c:set></c:otherwise>
	</c:choose>
	<form:form action="${formAction}" method="post" cssClass="form-horizontal" commandName="carpetaCommand">
		<form:hidden path="id"/>
		<form:hidden path="entitatId"/>
		<form:hidden path="pareId"/>
		<rip:inputSelect name="tipus" textKey="contenidor.carpeta.form.camp.tipus" optionItems="${carpetaTipusEnumOptions}" optionValueAttribute="value" optionTextKeyAttribute="text"/>
		<rip:inputText name="nom" textKey="contenidor.carpeta.form.camp.nom" required="true"/>
		<div id="modal-botons" class="well">
			<button type="submit" class="btn btn-success"><span class="fa fa-save"></span> <spring:message code="comu.boto.guardar"/></button>
			<a href="<c:url value="/contenidor/${carpetaCommand.pareId}"/>" class="btn btn-default modal-tancar"><spring:message code="comu.boto.cancelar"/></a>
		</div>
	</form:form>
</body>
</html>
