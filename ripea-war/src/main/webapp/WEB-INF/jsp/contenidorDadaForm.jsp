<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib tagdir="/WEB-INF/tags/ripea" prefix="rip"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<c:choose>
	<c:when test="${empty dadaCommand.id}"><c:set var="titol"><spring:message code="contenidor.dada.form.titol.crear"/></c:set></c:when>
	<c:otherwise><c:set var="titol"><spring:message code="contenidor.dada.form.titol.modificar"/></c:set></c:otherwise>
</c:choose>
<html>
<head>
	<title>${titol}</title>
	<rip:modalHead titol="${titol}" buttonContainerId="botons"/>
	<script src="<c:url value="/js/jquery.numeric.js"/>"></script>
	<link href="<c:url value="/css/datepicker.css"/>" rel="stylesheet"/>
	<script src="<c:url value="/js/bootstrap-datepicker.js"/>"></script>
	<script src="<c:url value="/js/datepicker-locales/bootstrap-datepicker.${idioma}.js"/>"></script>
<script>
var metaDadaTipus = [];
<c:forEach var="metaDada" items="${metaDades}">
metaDadaTipus["${metaDada.id}"] = "${metaDada.tipus}";
</c:forEach>
$(document).ready(function() {
	$('#valorSencer').numeric({
		decimal: "¶",
		negative: true
	});
	$('#valorFlotant').numeric({
		decimal: ",",
		negative: true
	});
	$('#valorImport').numeric({
		decimal: ",",
		negative: true
	});
	$('#metaDadaId').change(function() {
		$('.dada-valor').addClass("hide");
		$('#dada-valor-' + metaDadaTipus[$(this).val()]).removeClass("hide");
	});
	$('input#valorData').datepicker({
		format: 'dd/mm/yyyy',
		weekStart: 1,
		autoclose: true,
		language: '${idioma}'
	}).on('show', function() {
		var iframe = $('.modal-body iframe', window.parent.document);
		var height = $('html').height() + 250;
		iframe.height(height + 'px');
	}).on('hide', function() {
		var iframe = $('.modal-body iframe', window.parent.document);
		var height = $('html').height();
		iframe.height(height + 'px');
	});
	$('#metaDadaId').trigger('change');
});
</script>
</head>
<body>

	<c:set var="formAction"><rip:modalUrl value="/contenidor/${dadaCommand.nodeId}/dada/new"/></c:set>
	<form:form action="${formAction}" method="post" cssClass="form-horizontal" commandName="dadaCommand">
		<form:hidden path="id"/>
		<form:hidden path="entitatId"/>
		<form:hidden path="nodeId"/>
		<rip:inputSelect name="metaDadaId" textKey="contenidor.dada.form.camp.metadada" required="true" optionItems="${metaDades}" optionValueAttribute="id" optionTextAttribute="nom"/>
		<div id="dada-valor-TEXT" class="dada-valor hide">
			<rip:inputText name="valorText" textKey="contenidor.dada.form.camp.valor" required="true"/>
		</div>
		<div id="dada-valor-DATA" class="dada-valor hide">
			<rip:inputDate name="valorData" textKey="contenidor.dada.form.camp.valor" required="true"/>
		</div>
		<div id="dada-valor-SENCER" class="dada-valor hide">
			<rip:inputText name="valorSencer" textKey="contenidor.dada.form.camp.valor" required="true"/>
		</div>
		<div id="dada-valor-FLOTANT" class="dada-valor hide">
			<rip:inputText name="valorFlotant" textKey="contenidor.dada.form.camp.valor" required="true"/>
		</div>
		<div id="dada-valor-IMPORT" class="dada-valor hide">
			<rip:inputText name="valorImport" textKey="contenidor.dada.form.camp.valor" required="true"/>
		</div>
		<div id="dada-valor-BOOLEA" class="dada-valor hide">
			<rip:inputCheckbox name="valorBoolea" textKey="contenidor.dada.form.camp.valor"/>
		</div>
		<div id="modal-botons" class="well">
			<button type="submit" class="btn btn-success"><span class="fa fa-save"></span> <spring:message code="comu.boto.guardar"/></button>
			<a href="<c:url value="/contenidor/${dadaCommand.nodeId}"/>" class="btn btn-default modal-tancar"><spring:message code="comu.boto.cancelar"/></a>
		</div>
	</form:form>

</body>
</html>
