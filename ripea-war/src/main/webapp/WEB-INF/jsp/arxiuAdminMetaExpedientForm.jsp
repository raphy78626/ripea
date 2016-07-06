<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib tagdir="/WEB-INF/tags/ripea" prefix="rip"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<%
pageContext.setAttribute(
		"idioma",
		org.springframework.web.servlet.support.RequestContextUtils.getLocale(request).getLanguage());
pageContext.setAttribute(
		"principalTipusEnumOptions",
		es.caib.ripea.war.helper.HtmlSelectOptionHelper.getOptionsForEnum(
				es.caib.ripea.core.api.dto.PrincipalTipusEnumDto.class,
				"principal.tipus.enum."));
%>
	<c:set var="titol"><spring:message code="arxiu.metaexpedient.form.titol.crear"/></c:set>
<html>
<head>
	<title>${titol}</title>
	<link href="<c:url value="/webjars/select2/4.0.1/dist/css/select2.min.css"/>" rel="stylesheet"/>
	<link href="<c:url value="/webjars/select2-bootstrap-theme/0.1.0-beta.4/dist/select2-bootstrap.min.css"/>" rel="stylesheet"/>
	<script src="<c:url value="/webjars/select2/4.0.1/dist/js/select2.min.js"/>"></script>
	<script src="<c:url value="/webjars/select2/4.0.1/dist/js/i18n/${idioma}.js"/>"></script>
	<rip:modalHead titol="${titol}" buttonContainerId="botons"/>
<script>
	$(document).ready(function() {
		$("#modal-botons button[type='submit']").on('click', function() {
			$("form#arxiuMetaExpedientCommand *:disabled").attr('readonly', 'readonly');
			$("form#arxiuMetaExpedientCommand *:disabled").removeAttr('disabled');
		});
	});
</script>
</head>
<body>

	<c:set var="formAction"><rip:modalUrl value="/arxiuAdmin/${arxiu.id}/metaExpedient"/></c:set>
	<form:form action="${formAction}" method="post" cssClass="form-horizontal" commandName="arxiuMetaExpedientCommand">
		<rip:inputSelect name="metaExpedientId" textKey="arxiu.metaexpedient.form.camp.metaexpedient" required="true" optionItems="${metaExpedients}" optionValueAttribute="id" optionTextAttribute="nom"/>
		<div id="modal-botons" class="well">
			<button type="submit" class="btn btn-success"><span class="fa fa-save"></span>&nbsp;<spring:message code="comu.boto.guardar"/></button>
			<a href="<c:url value="/arxiuAdmin/${arxiu.id}/metaExpedient"/>" class="btn btn-default modal-tancar"><spring:message code="comu.boto.cancelar"/></a>
		</div>
		<div style="height: 300px;"></div>
	</form:form>

</body>
</html>
