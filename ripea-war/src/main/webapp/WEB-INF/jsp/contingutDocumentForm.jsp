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
	<script src="<c:url value="/js/webutil.common.js"/>"></script>
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
	$('#metaNodeId').on('change', function() {
		if ($(this).val()) {
			$.get("../metaDocument/" +  $(this).val())
			.done(function(data) {
				if (data.plantillaNom) {
					$('#info-plantilla-si').removeClass('hidden');
					$('#info-plantilla-no').addClass('hidden');
					$('#info-plantilla-si a').attr('href', '../metaDocument/' + data.id + '/plantilla');
				} else {
					$('#info-plantilla-si').addClass('hidden');
					$('#info-plantilla-no').removeClass('hidden');
				}
				webutilModalAdjustHeight();
			})
			.fail(function() {
				alert("<spring:message code="contingut.document.form.alert.plantilla"/>");
			});
		} else {
			$('#info-plantilla-no').removeClass('hidden');
			$('#info-plantilla-si').addClass('hidden');
		}
	});
	$('input[type=radio][name=origen]').on('change', function() {
		if ($(this).val() == 'DISC') {
			$('#input-origen-arxiu').removeClass('hidden');
			$('#input-origen-escaner').addClass('hidden');
			$('input[name=origen][value=DISC]').parent().addClass('active');
		} else {
			$('#input-origen-escaner').removeClass('hidden');
			$('#input-origen-arxiu').addClass('hidden');
			$('input[name=origen][value=ESCANER]').parent().addClass('active');
		}
		webutilModalAdjustHeight();
	});
	$('a[data-toggle="tab"]').on('shown.bs.tab', function(e) {
		var $tab = $($(e.target).attr('href'));
		if ($tab.attr('id') == 'digital') {
			$('#documentTipus').val('DIGITAL');
		} else if ($tab.attr('id') == 'fisic') {
			$('#documentTipus').val('FISIC');
		}
	});
	$(document).on('submit','form#documentCommand', function() {
		var action = $(this).attr('action');
		var lastSlashIndex = action.lastIndexOf('/');
		var actionProcessed = action.substring(0, lastSlashIndex);
		if ($('#documentTipus').val() == 'DIGITAL') {
			var $btn = $(this).find("button[type=submit]:focus");
			if ($btn.length == 0) {
				if (action.endsWith("new")) {
					actionProcessed += '/digital/new';
				} else {
					actionProcessed += '/digital/update';
				}
			} else {
				actionProcessed += '/escaneig/inici';
			}
		} else if ($('#documentTipus').val() == 'FISIC') {
			if (action.endsWith("new")) {
				actionProcessed += '/fisic/new';
			} else {
				actionProcessed += '/fisic/update';
			}
		}
		$(this).attr('action', actionProcessed);
		return true;
	});
	$('#metaNodeId').trigger('change');
	$('input[type=radio][name=origen][value=${documentCommand.origen}]').trigger('change');
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
				<div id="info-plantilla-si" class="alert well-sm alert-info hidden">
					<span class="fa fa-info-circle"></span>
					<spring:message code="contingut.document.form.info.plantilla.si"/>
					<a href="#" class="btn btn-xs btn-default pull-right"><spring:message code="comu.boto.descarregar"/></a>
				</div>
				<div id="info-plantilla-no" class="alert well-sm alert-info hidden">
					<span class="fa fa-info-circle"></span>
					<spring:message code="contingut.document.form.info.plantilla.no"/>
				</div>
				<rip:inputRadio name="origen" textKey="contingut.document.form.camp.origen" botons="true" optionItems="${digitalOrigenOptions}" optionValueAttribute="value" optionTextKeyAttribute="text"/>
				<div id="input-origen-arxiu" class="hidden">
					<rip:inputFile name="arxiu" textKey="contingut.document.form.camp.arxiu" required="${empty documentCommand.id}"/>
				</div>
				<div id="input-origen-escaner" class="hidden">
					<rip:inputFixed name="escanejatTempId" textKey="contingut.document.form.camp.escaneig">
						<rip:inputHidden name="escanejatTempId"/>
						<div class="input-group">
							<input type="text" class="form-control" disabled="disabled" value="${escanejat.nom}"/>
							<span class="input-group-btn">
								<button class="btn btn-default" name="hola" type="submit"><span class="fa fa-print"></span> <spring:message code="contingut.document.form.boto.escaneig"/></button>
							</span>
	    				</div>
					</rip:inputFixed>
				</div>
			</div>
			<div role="tabpanel" class="tab-pane<c:if test="${documentCommand.documentTipus == 'FISIC'}"> active</c:if>" id="fisic">
				<rip:inputTextarea name="ubicacio" textKey="contingut.document.form.camp.ubicacio" required="true"/>
			</div>
		</div>
		<div id="modal-botons" class="well">
			<button type="submit" class="btn btn-success"><span class="fa fa-save"></span> <spring:message code="comu.boto.guardar"/></button>
		</div>
	</form:form>
</body>
</html>
