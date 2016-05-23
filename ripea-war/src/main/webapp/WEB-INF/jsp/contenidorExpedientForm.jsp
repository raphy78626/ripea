<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib tagdir="/WEB-INF/tags/ripea" prefix="rip"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<c:choose>
	<c:when test="${empty expedientCommand.id}"><c:set var="titol"><spring:message code="contenidor.expedient.form.titol.crear"/></c:set></c:when>
	<c:otherwise><c:set var="titol"><spring:message code="contenidor.expedient.form.titol.modificar"/></c:set></c:otherwise>
</c:choose>
<html>
<head>
	<title>${titol}</title>
	<rip:modalHead titol="${titol}" buttonContainerId="botons"/>
	<link href="<c:url value="/css/select2.css"/>" rel="stylesheet"/>
	<link href="<c:url value="/css/select2-bootstrap.css"/>" rel="stylesheet"/>
	<script src="<c:url value="/js/select2.min.js"/>"></script>
<script>
	$(document).ready(function() {
		$("#metaNodeId").change(function() {
			// Buida les opcions del select2
			$('#arxiuId').select2('val', '', true);
			$('#arxiuId option[value!=""]').remove();
			var metaExpedientId = $(this).val();
			if(metaExpedientId != null) {
				// Obté la llista d'arxius per a aquell node contenidor
				var baseUrl = "<c:url value="/metaExpedient/"/>" + metaExpedientId + "/arxiu/findAll";
				$.get(baseUrl)
					.done(function(data) {
						for (var i = 0; i < data.length; i++) {
							$('#arxiuId').append('<option value="' + data[i].id + '">' + data[i].nom + '</option>');
						}	
						$('#arxiuId').change();
					})
					.fail(function() {
						alert("<spring:message code="error.jquery.ajax"/>");
					});
			} else {
				$('#arxiuId').change();
			}
		})
		$("#metaNodeId").change();
	});
</script>
</head>
<body>
	<c:choose>
		<c:when test="${empty expedientCommand.id}"><c:set var="formAction"><rip:modalUrl value="/contenidor/${expedientCommand.pareId}/expedient/new"/></c:set></c:when>
		<c:otherwise><c:set var="formAction"><rip:modalUrl value="/contenidor/${expedientCommand.pareId}/expedient/update"/></c:set></c:otherwise>
	</c:choose>
	<form:form action="${formAction}" method="post" cssClass="form-horizontal" commandName="expedientCommand">
		<form:hidden path="id"/>
		<form:hidden path="entitatId"/>
		<form:hidden path="pareId"/>
		<rip:inputSelect name="metaNodeId" textKey="contenidor.expedient.form.camp.metanode" required="true" optionItems="${metaExpedients}" optionValueAttribute="id" optionTextAttribute="nom"/>
		<rip:inputSelect name="arxiuId" textKey="contenidor.expedient.form.camp.arxiu" required="true" optionItems="${arxius}" optionValueAttribute="id" optionTextAttribute="nomAmbUnitat"/>
		<rip:inputText name="nom" textKey="contenidor.expedient.form.camp.nom" required="true"/>
		<rip:inputText name="any" textKey="contenidor.expedient.form.camp.any" required="true"/>
		<div id="modal-botons" class="well">
			<button type="submit" class="btn btn-success"><span class="fa fa-save"></span> <spring:message code="comu.boto.guardar"/></button>
			<a href="<c:url value="/contenidor/${expedientCommand.pareId}"/>" class="btn btn-default modal-tancar"><spring:message code="comu.boto.cancelar"/></a>
		</div>
	</form:form>
</body>
</html>
