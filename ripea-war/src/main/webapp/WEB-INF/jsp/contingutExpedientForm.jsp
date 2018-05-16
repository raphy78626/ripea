<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib tagdir="/WEB-INF/tags/ripea" prefix="rip"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<c:choose>
	<c:when test="${empty expedientCommand.id}"><c:set var="titol"><spring:message code="contingut.expedient.form.titol.crear"/></c:set></c:when>
	<c:otherwise><c:set var="titol"><spring:message code="contingut.expedient.form.titol.modificar"/></c:set></c:otherwise>
</c:choose>
<html>
<head>
	<title>${titol}</title>
	<rip:modalHead/>
	<link href="<c:url value="/webjars/select2/4.0.6-rc.1/dist/css/select2.min.css"/>" rel="stylesheet"/>
	<link href="<c:url value="/webjars/select2-bootstrap-theme/0.1.0-beta.4/dist/select2-bootstrap.min.css"/>" rel="stylesheet"/>
	<script src="<c:url value="/webjars/select2/4.0.6-rc.1/dist/js/select2.min.js"/>"></script>
	<script src="<c:url value="/js/webutil.common.js"/>"></script>
<script>
	$(document).ready(function() {
		$("#metaNodeId").change(function() {
			$('#arxiuId').select2(
					'val',
					'',
					true);
			$('#arxiuId option[value!=""]').remove();
			var metaExpedientId = $(this).val();
			if (metaExpedientId != null) {
				var baseUrl = "<c:url value="/userajax/metaExpedient/"/>" + metaExpedientId + "/arxius";
				$.get(baseUrl).
					done(function(data) {
						for (var i = 0; i < data.length; i++) {
							$('#arxiuId').append('<option value="' + data[i].id + '">' + data[i].nom + '</option>');
						}
						$('#arxiuId').change();
					}).
					fail(function() {
						alert("<spring:message code="error.jquery.ajax"/>");
					});
			} else {
				$('#arxiuId').change();
			}
		})
		$("#metaNodeId").trigger('change');
	});
</script>
</head>
<body>
	<c:choose>
		<c:when test="${empty expedientCommand.id}"><c:set var="formAction"><rip:modalUrl value="/contingut/${expedientCommand.pareId}/expedient/new"/></c:set></c:when>
		<c:otherwise><c:set var="formAction"><rip:modalUrl value="/contingut/${expedientCommand.pareId}/expedient/update"/></c:set></c:otherwise>
	</c:choose>
	<form:form action="${formAction}" method="post" cssClass="form-horizontal" commandName="expedientCommand">
		<form:hidden path="id"/>
		<form:hidden path="entitatId"/>
		<form:hidden path="pareId"/>
		<rip:inputText name="nom" textKey="contingut.expedient.form.camp.nom" required="true"/>
		<rip:inputSelect name="metaNodeId" textKey="contingut.expedient.form.camp.metanode" required="true" optionItems="${metaExpedients}" optionValueAttribute="id" optionTextAttribute="nom"/>
		<rip:inputSelect name="arxiuId" textKey="contingut.expedient.form.camp.arxiu" required="true" optionItems="${arxius}" optionValueAttribute="id" optionTextAttribute="nomAmbUnitat"/>
		<rip:inputText name="any" textKey="contingut.expedient.form.camp.any" required="true"/>
		<div id="modal-botons" class="well">
			<button type="submit" class="btn btn-success"><span class="fa fa-save"></span> <spring:message code="comu.boto.guardar"/></button>
			<a href="<c:url value="/contingut/${expedientCommand.pareId}"/>" class="btn btn-default" data-modal-cancel="true"><spring:message code="comu.boto.cancelar"/></a>
		</div>
	</form:form>
</body>
</html>
