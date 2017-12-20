<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib tagdir="/WEB-INF/tags/ripea" prefix="rip"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@page import="es.caib.ripea.war.helper.EnumHelper"%>

<%
java.util.List<EnumHelper.HtmlOption> enviamentTipus = EnumHelper.getOptionsForEnum(
		es.caib.ripea.core.api.dto.NotificacioEnviamentTipusEnumDto.class,
		"notificacio.enviament.tipus.enum.");
pageContext.setAttribute(
		"enviamentTipus",
		enviamentTipus);

java.util.List<EnumHelper.HtmlOption> serveiTipus = EnumHelper.getOptionsForEnum(
		es.caib.ripea.core.api.dto.ServeiTipusEnumDto.class,
		"notificacio.servei.tipus.enum.");
pageContext.setAttribute(
		"serveiTipus",
		serveiTipus);

java.util.List<EnumHelper.HtmlOption> seuIdioma = EnumHelper.getOptionsForEnum(
		es.caib.ripea.core.api.dto.InteressatIdiomaEnumDto.class,
		"notificacio.seu.idioma.enum.");
pageContext.setAttribute(
		"seuIdioma",
		seuIdioma);
%>

<c:choose>
	<c:when test="${empty documentNotificacioCommand.id}"><c:set var="titol"><spring:message code="notificacio.form.titol.crear"/></c:set></c:when>
	<c:otherwise><c:set var="titol"><spring:message code="notificacio.form.titol.modificar"/></c:set></c:otherwise>
</c:choose>
<html>
<head>
	<title>${titol}</title>
	<link href="<c:url value="/webjars/select2/4.0.1/dist/css/select2.min.css"/>" rel="stylesheet"/>
	<link href="<c:url value="/webjars/select2-bootstrap-theme/0.1.0-beta.4/dist/select2-bootstrap.min.css"/>" rel="stylesheet"/>
	<script src="<c:url value="/webjars/select2/4.0.1/dist/js/select2.min.js"/>"></script>
	<script src="<c:url value="/webjars/select2/4.0.1/dist/js/i18n/${requestLocale}.js"/>"></script>
	<script src="<c:url value="/js/webutil.common.js"/>"></script>
	<script src="<c:url value="/webjars/bootstrap-datepicker/1.6.1/dist/js/bootstrap-datepicker.min.js"/>"></script>
	<script src="<c:url value="/webjars/bootstrap-datepicker/1.6.1/dist/locales/bootstrap-datepicker.${requestLocale}.min.js"/>"></script>
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
		<c:when test="${empty documentNotificacioCommand.id}"><c:set var="formAction"><rip:modalUrl value="/document/${document.id}/notificar"/></c:set></c:when>
		<c:otherwise><c:set var="formAction"><rip:modalUrl value="/expedient/${expedientId}/notificacio/${documentNotificacioCommand.id}"/></c:set></c:otherwise>
	</c:choose>
	<form:form action="${formAction}" method="post" cssClass="form-horizontal" commandName="documentNotificacioCommand" role="form">
		<c:if test="${empty documentNotificacioCommand.id || documentNotificacioCommand.tipus == 'ELECTRONICA'}">
			<ul class="nav nav-tabs" role="tablist">
				<li role="presentation" class="active"><a href="#tab-informacio-general" aria-controls="tabinformacio-general" role="tab" data-toggle="tab"><spring:message code="notificacio.form.camp.tab.informacio.general"/></a></li>
				<li role="presentation"><a href="#tab-document" aria-controls="tab-document" role="tab" data-toggle="tab"><spring:message code="notificacio.form.camp.tab.document"/></a></li>
				<li role="presentation"><a href="#tab-enviament" aria-controls="tab-enviament" role="tab" data-toggle="tab"><spring:message code="notificacio.form.camp.tab.enviament"/></a></li>
				<li role="presentation"><a href="#tab-pagadors" aria-controls="tab-pagadors" role="tab" data-toggle="tab"><spring:message code="notificacio.form.camp.tab.organismes.pagadors"/></a></li>
				<li role="presentation"><a href="#tab-seu" aria-controls="tab-seu" role="tab" data-toggle="tab"><spring:message code="notificacio.form.camp.tab.seu"/></a></li>

				<%-- <li role="presentation"><a href="#dades" aria-controls="dades" role="tab" data-toggle="tab"><spring:message code="notificacio.form.camp.tab.dades"/></a></li>
				<li role="presentation"><a href="#avisofici" aria-controls="avisofici" role="tab" data-toggle="tab"><spring:message code="notificacio.form.camp.tab.avisofici"/></a></li>
				<li role="presentation"><a href="#annexos" aria-controls="annexos" role="tab" data-toggle="tab"><spring:message code="notificacio.form.camp.tab.annexos"/></a></li> --%>
			</ul>
			<br/>
		</c:if>
		<rip:inputHidden name="id"/>
		<div class="tab-content">
			<div role="tabpanel" class="tab-pane active" id="tab-informacio-general">
				<c:choose>
					<c:when test="${empty documentNotificacioCommand.id}">
						<div class="row">
							<div class="col-xs-12"><rip:inputSelect name="tipus" textKey="notificacio.form.camp.tipus" optionItems="${notificacioTipusEnumOptions}" optionValueAttribute="value" optionTextKeyAttribute="text" required="true" labelSize="2"/></div>
						</div>
					</c:when>
					<c:otherwise>
						<rip:inputHidden name="tipus"/>
					</c:otherwise>
				</c:choose>
				<div class="row">
					<div class="col-xs-6"><rip:inputSelect name="enviamentTipus" textKey="notificacio.form.camp.tipus.enviament" optionItems="${enviamentTipus}" optionTextKeyAttribute="text" optionValueAttribute="value" /></div>
					<div class="col-xs-6"><rip:inputText name="emisorDir3Codi" textKey="notificacio.form.camp.dir3.emissor"/></div>
				</div>
				<div class="row">
					<div class="col-xs-12"><rip:inputText name="assumpte" textKey="notificacio.form.camp.concepte" labelSize="2"/></div>
				</div>
				<div class="row">
					<div class="col-xs-12"><rip:inputTextarea name="observacions" textKey="notificacio.form.camp.descripcio" labelSize="2"/></div>
				</div>
				<div class="row">
					<div class="col-xs-6"><rip:inputDate name="caducitat" textKey="notificacio.form.camp.data.caducitat"/></div>
					<div class="col-xs-6"><rip:inputDate name="enviamentDataProgramada" textKey="notificacio.form.camp.data.enviament.programat"/></div>
				</div>
				<div class="row">
					<div class="col-xs-6"><rip:inputText name="procedimentCodiSia" textKey="notificacio.form.camp.procediment.codi.sia"/></div>
					<div class="col-xs-6"><rip:inputText name="retardPostal" textKey="notificacio.form.camp.retard.postal"/></div>
				</div>
			</div>
			<div role="tabpanel" class="tab-pane" id="tab-document">
				<div class="row">
					<div class="col-xs-12">
					<div class="form-group">
						<label class="control-label col-xs-2"><spring:message code="notificacio.form.camp.document.nom"/></label>
						<div class="col-xs-10">
							<input class="form-control" id="nomDocument" disabled="true" value="${document.nom}"/>
						</div>
					</div>
					</div>
				</div>
				<div class="row">
					<div class="col-xs-12"><rip:inputText name="documentHash" textKey="notificacio.form.camp.document.hash" labelSize="2"/></div>
				</div>
				<div class="row">
					<div class="col-xs-6"><rip:inputCheckbox name="documentGenerarCsv" textKey="notificacio.form.camp.document.generar.csv"/></div>
					<div class="col-xs-6"><rip:inputCheckbox name="documentNormalitzat" textKey="notificacio.form.camp.document.normalitzat"/></div>
				</div>
			</div>
			<div role="tabpanel" class="tab-pane" id="tab-enviament">
				<div class="row"><div class="col-xs-12"><h4><spring:message code="notificacio.form.camp.enviament.servei"/></h4></div></div>
				<div class="row">
					<div class="col-xs-12"><rip:inputSelect name="serveiTipus" textKey="notificacio.form.camp.enviament.servei.tipus" optionItems="${serveiTipus}" optionTextKeyAttribute="text" optionValueAttribute="value" labelSize="2" /></div>
				</div>
				<div class="row"><div class="col-xs-12"><h4><spring:message code="notificacio.form.camp.enviament.titular"/></h4></div></div>
				<div class="row">
					<div class="col-xs-6"><rip:inputText name="titularNom" textKey="notificacio.form.camp.enviament.titular.nom"/></div>
					<div class="col-xs-6"><rip:inputText name="titularNif" textKey="notificacio.form.camp.enviament.titular.nif"/></div>
				</div>
				<div class="row">
					<div class="col-xs-6"><rip:inputText name="titularLlinatge1" textKey="notificacio.form.camp.enviament.titular.llinatge1"/></div>
					<div class="col-xs-6"><rip:inputText name="titularLlinatge2" textKey="notificacio.form.camp.enviament.titular.llinatge2"/></div>
				</div>
				<div class="row">
					<div class="col-xs-6"><rip:inputText name="titularEmail" textKey="notificacio.form.camp.enviament.titular.email"/></div>
					<div class="col-xs-6"><rip:inputText name="titularTelefon" textKey="notificacio.form.camp.enviament.titular.telefon"/></div>
				</div>
				<div class="row"><div class="col-xs-12"><h4><spring:message code="notificacio.form.camp.enviament.destinatari"/></h4></div></div>
				<div class="row">
					<div class="col-xs-12"><rip:inputSelect name="interessatId" textKey="notificacio.form.camp.destinatari" optionItems="${interessats}" optionValueAttribute="id" optionTextAttribute="identificador" placeholderKey="notificacio.form.camp.destinatari" labelSize="2"/></div>
				</div>
				<div class="row"><div class="col-xs-12"><h4><spring:message code="notificacio.form.camp.enviament.entrega.deh"/></h4></div></div>
				<div class="row">
					<div class="col-xs-6"><rip:inputText name="dehNif" textKey="notificacio.form.camp.enviament.deh.nif"/></div>
					<div class="col-xs-6"><rip:inputCheckbox name="dehObligat" textKey="notificacio.form.camp.enviament.deh.obligat"/></div>
				</div>
			</div>
			<div role="tabpanel" class="tab-pane" id="tab-pagadors">
				<div class="row"><div class="col-xs-12"><h4><spring:message code="notificacio.form.camp.pagador.cie"/></h4></div></div>
				<div class="row">
					<div class="col-xs-6"><rip:inputText name="pagadorCieCodiDir3" textKey="notificacio.form.camp.pagador.cie.codi.dir3"/></div>
					<div class="col-xs-6"><rip:inputDate name="pagadorCieDataVigencia" textKey="notificacio.form.camp.pagador.cie.data.vigencia"/></div>
				</div>
				<div class="row"><div class="col-xs-12"><h4><spring:message code="notificacio.form.camp.pagador.postal"/></h4></div></div>
				<div class="row">
					<div class="col-xs-6"><rip:inputText name="pagadorCorreusCodiDir3" textKey="notificacio.form.camp.pagador.postal.codi.dir3"/></div>
					<div class="col-xs-6"><rip:inputDate name="pagadorCorreusDataVigencia" textKey="notificacio.form.camp.pagador.postal.data.vigencia"/></div>
				</div>
				<div class="row">
					<div class="col-xs-6"><rip:inputText name="pagadorCorreusContracteNum" textKey="notificacio.form.camp.pagador.postal.numero.contracte"/></div>
					<div class="col-xs-6"><rip:inputText name="pagadorCorreusCodiClientFacturacio" textKey="notificacio.form.camp.pagador.postal.codi.client.facturacio"/></div>
				</div>
			</div>
			<div role="tabpanel" class="tab-pane" id="tab-seu">
				<div class="row"><div class="col-xs-12"><h4><spring:message code="notificacio.form.camp.seu.avis"/></h4></div></div>
				<div class="row">
					<div class="col-xs-12"><rip:inputText name="seuAvisTitol" textKey="notificacio.form.camp.seu.avis.titol" labelSize="2"/></div>
				</div>
				<div class="row">
					<div class="col-xs-6"><rip:inputText name="seuAvisText" textKey="notificacio.form.camp.seu.avis.text"/></div>
					<div class="col-xs-6"><rip:inputText name="seuAvisTextMobil" textKey="notificacio.form.camp.seu.avis.text.mobil"/></div>
				</div>
				
				<div class="row"><div class="col-xs-12"><h4><spring:message code="notificacio.form.camp.seu.expdient"/></h4></div></div>
				<div class="row">
					<div class="col-xs-6"><rip:inputText name="seuExpedientTitol" textKey="notificacio.form.camp.seu.expdient.titol"/></div>
					<div class="col-xs-6"><rip:inputText name="seuExpedientSerieDocumental" textKey="notificacio.form.camp.seu.expdient.serie.documental"/></div>
				</div>
				<div class="row">
					<div class="col-xs-6"><rip:inputText name="seuExpedientUnitatOrganitzativa" textKey="notificacio.form.camp.seu.expdient.unitat.organitzativa"/></div>
					<div class="col-xs-6"><rip:inputText name="seuExpedientIdentificadorEni" textKey="notificacio.form.camp.seu.identificador.eni"/></div>
				</div>
				
				<div class="row"><div class="col-xs-12"><h4><spring:message code="notificacio.form.camp.seu.idioma"/></h4></div></div>
				<div class="row">
					<div class="col-xs-12"><rip:inputSelect name="seuIdioma" textKey="notificacio.form.camp.seu.idioma" optionItems="${seuIdioma}" optionTextKeyAttribute="text" optionValueAttribute="value" labelSize="2" /></div>
				</div>
				
				<div class="row"><div class="col-xs-12"><h4><spring:message code="notificacio.form.camp.seu.ofici"/></h4></div></div>
				<div class="row">
					<div class="col-xs-6"><rip:inputText name="seuOficiTitol" textKey="notificacio.form.camp.seu.ofici.titol"/></div>
					<div class="col-xs-6"><rip:inputText name="seuOficiText" textKey="notificacio.form.camp.seu.ofici.text"/></div>
				</div>
				
				<div class="row"><div class="col-xs-12"><h4><spring:message code="notificacio.form.camp.seu.registre"/></h4></div></div>
				<div class="row">
					<div class="col-xs-6"><rip:inputText name="seuRegistreLlibre" textKey="notificacio.form.camp.seu.registre.llibre"/></div>
					<div class="col-xs-6"><rip:inputText name="seuRegistreOficina" textKey="notificacio.form.camp.seu.registre.oficina"/></div>
				</div>
			</div>
			
			
			<%-- <div role="tabpanel" class="tab-pane" id="dades">
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
				<rip:inputText name="seuAvisTitol" textKey="notificacio.form.camp.avis.titol"/>
				<rip:inputTextarea name="seuAvisText" textKey="notificacio.form.camp.avis.text"/>
				<rip:inputTextarea name="seuAvisTextMobil" textKey="notificacio.form.camp.avis.textsms"/>
				<rip:inputText name="seuOficiTitol" textKey="notificacio.form.camp.ofici.titol"/>
				<rip:inputTextarea name="seuOficiText" textKey="notificacio.form.camp.ofici.text"/>
			</div>
			<div role="tabpanel" class="tab-pane" id="annexos">
				<rip:inputSelect name="annexos" textKey="notificacio.form.camp.annexos" optionItems="${annexos}" emptyOption="true" optionValueAttribute="id" optionTextAttribute="nom" placeholderKey="notificacio.form.camp.annexos"/>
			</div> --%>
			
			
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
