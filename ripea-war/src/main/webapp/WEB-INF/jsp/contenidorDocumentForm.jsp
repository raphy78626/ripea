<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib tagdir="/WEB-INF/tags/ripea" prefix="rip"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<c:choose>
	<c:when test="${empty documentCommand.id}"><c:set var="titol"><spring:message code="contenidor.document.form.titol.crear"/></c:set></c:when>
	<c:otherwise><c:set var="titol"><spring:message code="contenidor.document.form.titol.modificar"/></c:set></c:otherwise>
</c:choose>
<c:set var="idioma"><%=org.springframework.web.servlet.support.RequestContextUtils.getLocale(request).getLanguage()%></c:set>

<html>
<head>
	<title>${titol}</title>
	<link href="<c:url value="/css/datepicker.css"/>" rel="stylesheet"/>
	<link href="<c:url value="/css/select2.css"/>" rel="stylesheet"/>
	<link href="<c:url value="/css/select2-bootstrap.css"/>" rel="stylesheet"/>
	<script src="<c:url value="/js/select2.min.js"/>"></script>
	<link href="<c:url value="/css/jasny-bootstrap.min.css"/>" rel="stylesheet">
	<script src="<c:url value="/js/jasny-bootstrap.min.js"/>"></script>
	<script src="<c:url value="/js/bootstrap-datepicker.js"/>"></script>
	<script src="<c:url value="/js/datepicker-locales/bootstrap-datepicker.${idioma}.js"/>"></script>
	<rip:modalHead titol="${titol}" buttonContainerId="botons"/>
<script>
$(document).ready(function() {
	$("#metaNodeId").on('change', function() {
		if ($(this).val()) {
			$.get("../metaDocument/" +  $(this).val())
			.done(function(data) {
				if (data.plantillaNom) {
					$('#plantilla-buida').addClass('hidden');
					$('#plantilla-descarregar').removeClass('hidden');
					$('#plantilla-descarregar a').attr('href', '../metaDocument/' + data.id + '/plantilla');
				} else {
					$('#plantilla-buida').removeClass('hidden');
					$('#plantilla-descarregar').addClass('hidden');
				}
				var iframe = $('.modal-body iframe', window.parent.document);
				var height = $('html').height();
				iframe.height(height + 'px');
			})
			.fail(function() {
				alert("<spring:message code="contenidor.document.form.alert.plantilla"/>");
			});
		}
	});
});
</script>
</head>
<body>
	<c:choose>
		<c:when test="${empty documentCommand.id}"><c:set var="formAction"><rip:modalUrl value="/contenidor/${documentCommand.pareId}/document/new"/></c:set></c:when>
		<c:otherwise><c:set var="formAction"><rip:modalUrl value="/contenidor/${documentCommand.pareId}/document/update"/></c:set></c:otherwise>
	</c:choose>
	<form:form action="${formAction}" method="post" cssClass="form-horizontal" commandName="documentCommand" enctype="multipart/form-data">
		<form:hidden path="id"/>
		<form:hidden path="entitatId"/>
		<form:hidden path="pareId"/>
		<rip:inputSelect name="metaNodeId" textKey="contenidor.document.form.camp.metanode" required="true" optionItems="${metaDocuments}" optionValueAttribute="id" optionTextAttribute="nom" emptyOption="true" emptyOptionTextKey="contenidor.document.form.camp.metanode.buit"/>
		<rip:inputFixed textKey="contenidor.document.form.camp.plantilla">
			<div id="plantilla-buida"><spring:message code="contenidor.document.form.camp.plantilla.sense"/></div>
			<div id="plantilla-descarregar" class="hidden"><a href="#" class="btn btn-info btn-xs"><span class="fa fa-file"></span>&nbsp;Descarregar</a></div>
		</rip:inputFixed>
		<rip:inputText name="nom" textKey="contenidor.document.form.camp.nom" required="true"/>
		<rip:inputFile name="arxiu" textKey="contenidor.document.form.camp.arxiu" required="${empty documentCommand.id}"/>
		<rip:inputDate name="data" textKey="contenidor.document.form.camp.data" required="true"/>
		<div id="modal-botons" class="well">
			<button type="submit" class="btn btn-success"><span class="fa fa-save"></span> <spring:message code="comu.boto.guardar"/></button>
			<a href="<c:url value="/contenidor/${documentCommand.pareId}"/>" class="btn btn-default modal-tancar"><spring:message code="comu.boto.cancelar"/></a>
		</div>
	</form:form>
<%--script>
	$('input#data').datepicker({
		format: 'dd/mm/yyyy',
		weekStart: 1,
		autoclose: true,
		language: '${idioma}'
	}).on('show', function() {
		var iframe = $('.modal-body iframe', window.parent.document);
		var height = $('html').height() + 190;
		iframe.height(height + 'px');
	}).on('hide', function() {
		var iframe = $('.modal-body iframe', window.parent.document);
		var height = $('html').height();
		iframe.height(height + 'px');
	});
</script--%>
</body>
</html>
