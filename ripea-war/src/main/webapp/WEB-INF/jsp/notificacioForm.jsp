<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib tagdir="/WEB-INF/tags/ripea" prefix="rip"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<c:choose>
	<c:when test="${empty documentNotificacioCommand.id}"><c:set var="titol"><spring:message code="notificacio.form.titol.crear"/></c:set></c:when>
	<c:otherwise><c:set var="titol"><spring:message code="notificacio.form.titol.modificar"/></c:set></c:otherwise>
</c:choose>
<html>
<head>
	<title>${titol}</title>
	<link href="<c:url value="/webjars/select2/4.0.6-rc.1/dist/css/select2.min.css"/>" rel="stylesheet"/>
	<link href="<c:url value="/webjars/select2-bootstrap-theme/0.1.0-beta.4/dist/select2-bootstrap.min.css"/>" rel="stylesheet"/>
	<script src="<c:url value="/webjars/select2/4.0.6-rc.1/dist/js/select2.min.js"/>"></script>
	<script src="<c:url value="/webjars/select2/4.0.6-rc.1/dist/js/i18n/${requestLocale}.js"/>"></script>
	<script src="<c:url value="/js/webutil.common.js"/>"></script>
	<rip:modalHead/>
<script>
$(document).ready(function() {
	$('#tipus').on('change', function() {
		if ($(this).val() == 'MANUAL') {
			$('ul.nav-tabs li a[href="#avisofici"]').removeAttr('data-toggle');
			$('ul.nav-tabs li').addClass('disabled');
			$('ul.nav-tabs li a[href="#annexos"]').removeAttr('data-toggle');
			$('ul.nav-tabs li').addClass('disabled');
			$('input#avisTitol').prop('disabled', true);
			$('textarea#avisText').prop('disabled', true);
			$('textarea#avisTextSms').prop('disabled', true);
			$('input#oficiTitol').prop('disabled', true);
			$('textarea#oficiText').prop('disabled', true);
			$('select#estat').prop('disabled', false);
		} else {
			$('ul.nav-tabs li a[href="#avisofici"]').attr('data-toggle', 'tab');
			$('ul.nav-tabs li').removeClass('disabled');
			$('ul.nav-tabs li a[href="#annexos"]').attr('data-toggle', 'tab');
			$('ul.nav-tabs li').removeClass('disabled');
			$('input#avisTitol').prop('disabled', false);
			$('textarea#avisText').prop('disabled', false);
			$('textarea#avisTextSms').prop('disabled', false);
			$('input#oficiTitol').prop('disabled', false);
			$('textarea#oficiText').prop('disabled', false);
			$('select#estat').prop('disabled', true);
			$('select#estat').val('PENDENT');
		}
	});
	$('#tipus').trigger('change');
});
</script>
</head>
<body>
	<c:choose>
		<c:when test="${empty documentNotificacioCommand.id}"><c:set var="formAction"><rip:modalUrl value="/document/${documentNotificacioCommand.documentId}/notificar"/></c:set></c:when>
		<c:otherwise><c:set var="formAction"><rip:modalUrl value="/expedient/${expedientId}/notificacio/${documentNotificacioCommand.id}"/></c:set></c:otherwise>
	</c:choose>
	<form:form action="${formAction}" method="post" cssClass="form-horizontal" commandName="documentNotificacioCommand" role="form">
		<c:if test="${empty documentNotificacioCommand.id || documentNotificacioCommand.tipus == 'ELECTRONICA'}">
			<ul class="nav nav-tabs" role="tablist">
				<li role="presentation" class="active"><a href="#dades" aria-controls="dades" role="tab" data-toggle="tab"><spring:message code="notificacio.form.camp.tab.dades"/></a></li>
				<li role="presentation"><a href="#avisofici" aria-controls="avisofici" role="tab" data-toggle="tab"><spring:message code="notificacio.form.camp.tab.avisofici"/></a></li>
				<li role="presentation"><a href="#annexos" aria-controls="annexos" role="tab" data-toggle="tab"><spring:message code="notificacio.form.camp.tab.annexos"/></a></li>
			</ul>
			<br/>
		</c:if>
		<rip:inputHidden name="id"/>
		<rip:inputHidden name="documentId"/>
		<div class="tab-content">
			<div role="tabpanel" class="tab-pane active" id="dades">
				<c:choose>
					<c:when test="${empty documentNotificacioCommand.id}">
						<rip:inputSelect name="tipus" textKey="notificacio.form.camp.tipus" optionItems="${notificacioTipusEnumOptions}" optionValueAttribute="value" optionTextKeyAttribute="text" required="true"/>
					</c:when>
					<c:otherwise>
						<rip:inputHidden name="tipus"/>
					</c:otherwise>
				</c:choose>
				<rip:inputSelect name="estat" textKey="notificacio.form.camp.estat" optionItems="${notificacioEstatEnumOptions}" optionValueAttribute="value" optionTextKeyAttribute="text" required="true"/>
				<rip:inputSelect name="interessatId" textKey="notificacio.form.camp.destinatari" optionItems="${interessats}" optionValueAttribute="id" optionTextAttribute="identificador" placeholderKey="notificacio.form.camp.destinatari"/>
				<rip:inputText name="assumpte" textKey="notificacio.form.camp.assumpte" required="true"/>
				<rip:inputTextarea name="observacions" textKey="notificacio.form.camp.observacions"/>
			</div>
			<div role="tabpanel" class="tab-pane" id="avisofici">
				<rip:inputText name="avisTitol" textKey="notificacio.form.camp.avis.titol"/>
				<rip:inputTextarea name="avisText" textKey="notificacio.form.camp.avis.text"/>
				<rip:inputTextarea name="avisTextSms" textKey="notificacio.form.camp.avis.textsms"/>
				<rip:inputText name="oficiTitol" textKey="notificacio.form.camp.ofici.titol"/>
				<rip:inputTextarea name="oficiText" textKey="notificacio.form.camp.ofici.text"/>
			</div>
			<div role="tabpanel" class="tab-pane" id="annexos">
				<rip:inputSelect name="annexos" textKey="notificacio.form.camp.annexos" optionItems="${annexos}" emptyOption="true" optionValueAttribute="id" optionTextAttribute="nom" placeholderKey="notificacio.form.camp.annexos"/>
			</div>
		</div>
		<c:choose>
			<c:when test="${empty document}"><c:set var="urlTancar"><c:url value="/contingut/${expedientId}"/></c:set></c:when>
			<c:otherwise><c:set var="urlTancar"><c:url value="/contingut/${document.id}"/></c:set></c:otherwise>
		</c:choose>
		<div id="modal-botons" class="well">
			<button type="submit" class="btn btn-success"><span class="fa fa-floppy-o"></span> <spring:message code="comu.boto.guardar"/></button>
			<a href="${urlTancar}" class="btn btn-default" data-modal-cancel="true"><spring:message code="comu.boto.cancelar"/></a>
		</div>
	</form:form>
</body>
</html>
