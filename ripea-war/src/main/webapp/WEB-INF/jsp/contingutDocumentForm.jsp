<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib tagdir="/WEB-INF/tags/ripea" prefix="rip"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<c:choose>
	<c:when test="${empty documentCommand.id}"><c:set var="titol"><spring:message code="contingut.document.form.titol.crear"/></c:set></c:when>
	<c:otherwise><c:set var="titol"><spring:message code="contingut.document.form.titol.modificar"/></c:set></c:otherwise>
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
	<rip:modalHead/>
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
				alert("<spring:message code="contingut.document.form.alert.plantilla"/>");
			});
		}
	});
	$('a[data-toggle="tab"]').on('shown.bs.tab', function(e) {
		var $tab = $($(e.target).attr('href'));
		if ($tab.attr('id') == 'digital') {
			$('#documentTipus').val('DIGITAL');
		} else {
			$('#documentTipus').val('FISIC');
		}
	});
	$(document).on('submit','form#documentCommand', function() {
		var action = $(this).attr('action');
		var lastSlashIndex = action.lastIndexOf('/');
		var actionProcessed = action.substring(0, lastSlashIndex);
		if ($('#documentTipus').val() == 'DIGITAL') {
			if (action.endsWith("new")) {
				actionProcessed += '/digital/new';
			} else {
				actionProcessed += '/digital/update';
			}
		} else {
			if (action.endsWith("new")) {
				actionProcessed += '/fisic/new';
			} else {
				actionProcessed += '/fisic/update';
			}
		}
		$(this).attr('action', actionProcessed);
		return true;
	});
});
</script>
</head>
<body>
	<c:choose>
		<c:when test="${empty documentCommand.id}"><c:set var="formAction"><rip:modalUrl value="/contingut/${documentCommand.pareId}/document/new"/></c:set></c:when>
		<c:otherwise><c:set var="formAction"><rip:modalUrl value="/contingut/${documentCommand.pareId}/document/update"/></c:set></c:otherwise>
	</c:choose>
	<form:form action="${formAction}" method="post" cssClass="form-horizontal" commandName="documentCommand" enctype="multipart/form-data">
		<form:hidden path="id"/>
		<form:hidden path="entitatId"/>
		<form:hidden path="pareId"/>
		<form:hidden path="documentTipus"/>
		<rip:inputText name="nom" textKey="contingut.document.form.camp.nom" required="true"/>
		<rip:inputDate name="data" textKey="contingut.document.form.camp.data" required="true"/>
		<rip:inputSelect name="metaNodeId" textKey="contingut.document.form.camp.metanode" optionItems="${metaDocuments}" optionValueAttribute="id" optionTextAttribute="nom" emptyOption="true" emptyOptionTextKey="contingut.document.form.camp.metanode.buit"/>
		<c:if test="${empty documentCommand.id}">
			<ul class="nav nav-tabs" role="tablist">
				<c:if test="${empty documentCommand.id || documentCommand.documentTipus == 'DIGITAL'}">
					<li role="presentation"<c:if test="${documentCommand.documentTipus == 'DIGITAL'}"> class="active"</c:if>><a href="#digital" aria-controls="digital" role="tab" data-toggle="tab"><spring:message code="contingut.document.form.tab.digital"/></a></li>
				</c:if>
				<c:if test="${empty documentCommand.id || documentCommand.documentTipus == 'FISIC'}">
					<li role="presentation"<c:if test="${documentCommand.documentTipus == 'FISIC'}"> class="active"</c:if>><a href="#fisic" aria-controls="fisic" role="tab" data-toggle="tab"><spring:message code="contingut.document.form.tab.fisic"/></a></li>
				</c:if>
			</ul>
			<br/>
		</c:if>
		<div class="tab-content">
			<div role="tabpanel" class="tab-pane<c:if test="${documentCommand.documentTipus == 'DIGITAL'}"> active</c:if>" id="digital">
				<rip:inputFixed textKey="contingut.document.form.camp.plantilla">
					<div id="plantilla-buida"><spring:message code="contingut.document.form.camp.plantilla.sense"/></div>
					<div id="plantilla-descarregar" class="hidden"><a href="#" class="btn btn-info btn-xs"><span class="fa fa-file"></span>&nbsp;Descarregar</a></div>
				</rip:inputFixed>
				<rip:inputFile name="arxiu" textKey="contingut.document.form.camp.arxiu" required="${empty documentCommand.id}"/>
			</div>
			<div role="tabpanel" class="tab-pane<c:if test="${documentCommand.documentTipus == 'FISIC'}"> active</c:if>" id="fisic">
				<rip:inputTextarea name="ubicacio" textKey="contingut.document.form.camp.ubicacio" required="true"/>
			</div>
		</div>
		<div id="modal-botons" class="well">
			<button type="submit" class="btn btn-success"><span class="fa fa-save"></span> <spring:message code="comu.boto.guardar"/></button>
			<a href="<c:url value="/contingut/${documentCommand.pareId}"/>" class="btn btn-default" data-modal-cancel="true"><spring:message code="comu.boto.cancelar"/></a>
		</div>
	</form:form>
</body>
</html>
