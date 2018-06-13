<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib tagdir="/WEB-INF/tags/ripea" prefix="rip"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<c:choose>
	<c:when test="${empty reglaCommand.id}"><c:set var="titol"><spring:message code="regla.form.titol.crear"/></c:set></c:when>
	<c:otherwise><c:set var="titol"><spring:message code="regla.form.titol.modificar"/></c:set></c:otherwise>
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
<script type="text/javascript">
$(document).ready(function() {
	$('#tipus').on('change', function () {
		$('div#camps_tipus_EXP_COMU').css('display', 'none');
		$('div#camps_tipus_EXP_CREAR').css('display', 'none');
		$('div#camps_tipus_EXP_AFEGIR').css('display', 'none');
		$('div#camps_tipus_BUSTIA').css('display', 'none');
		$('div#camps_tipus_BACKOFFICE').css('display', 'none');
		$('div#camps_tipus_' + $(this).val()).css('display', '');
		if ($(this).val().indexOf("EXP_") == 0) {
			$('div#camps_tipus_EXP_COMU').css('display', '');
		}
	});
	$('#tipus').trigger('change');	
	$('#backofficeTipus').change(function(){
		if ($(this).val() == 'SISTRA')
			$('#backofficeTempsEntreIntentsBlock').show();
		else
			$('#backofficeTempsEntreIntentsBlock').hide();
	});
});
</script>
</head>
<body>


	<c:if test="${reglaDto.unitatOrganitzativa.tipusTransicio != null}">

		<div class="panel panel-danger">
			<div class="panel-heading">
				<span class="fa fa-warning text-danger"></span>
				<spring:message code="regla.list.unitatObsoleta" />
			</div>
			<div class="panel-body">
				<div class="row">
					<label class="col-xs-4 text-right"><spring:message
							code="regla.form.tipusTransicio" /></label>
					<div class="col-xs-8">
						<c:if
							test="${reglaDto.unitatOrganitzativa.tipusTransicio == 'DIVISIO'}">
							<spring:message code="unitat.tipusTransicio.DIVISIO" />
						</c:if>
						<c:if
							test="${reglaDto.unitatOrganitzativa.tipusTransicio == 'FUSIO'}">
							<spring:message code="unitat.tipusTransicio.FUSIO" />
						</c:if>
						<c:if
							test="${reglaDto.unitatOrganitzativa.tipusTransicio == 'SUBSTITUCIO'}">
							<spring:message code="unitat.tipusTransicio.SUBSTITUCIO" />
						</c:if>
					</div>
				</div>
				<div class="row">
					<label class="col-xs-4 text-right"><spring:message
							code="regla.form.novesUnitats" /></label>
					<div class="col-xs-8">
						<ul style="padding-left: 17px;">
							<c:forEach items="${reglaDto.unitatOrganitzativa.novaList}"
								var="newUnitat" varStatus="loop">
								<li>${newUnitat.denominacio} (${newUnitat.codi})</li>
							</c:forEach>
						</ul>
					</div>
				</div>
				<c:if test="${!empty reglesOfOldUnitatWithoutCurrent}">
					<div class="row">
						<label class="col-xs-4 text-right"><spring:message
								code="regla.form.altresReglesAfectades" /></label>
						<div class="col-xs-8">
							<ul style="padding-left: 17px;">
								<c:forEach items="${reglesOfOldUnitatWithoutCurrent}"
									var="regla" varStatus="loop">
									<li>${regla.nom}</li>
								</c:forEach>
							</ul>
						</div>
					</div>
				</c:if>
				<c:if
					test="${reglaDto.unitatOrganitzativa.tipusTransicio == 'FUSIO'}">
					<div class="row">
						<label class="col-xs-4 text-right"><spring:message
								code="unitat.transicioInfo.altresUnitatsFusionades" /></label>
						<div class="col-xs-8">
							<ul style="padding-left: 17px;">
								<c:forEach
									items="${reglaDto.unitatOrganitzativa.altresUnitatsFusionades}"
									var="unitatMap" varStatus="loop">
									<li>${unitatMap.value} (${unitatMap.key})</li>
								</c:forEach>
							</ul>
						</div>
					</div>
				</c:if>
			</div>
		</div>
	</c:if>




	<c:set var="formAction"><rip:modalUrl value="/regla"/></c:set>
	<ul class="nav nav-tabs" role="tablist">
		<li role="presentation" class="active"><a href="#comunes" aria-controls="comunes" role="tab" data-toggle="tab"><spring:message code="regla.form.pipella.comunes"/></a></li>
		<li role="presentation"><a href="#especifiques" aria-controls="especifiques" role="tab" data-toggle="tab"><spring:message code="regla.form.pipella.especifiques"/></a></li>
	</ul>
	<br/>
	<form:form action="${formAction}" method="post" cssClass="form-horizontal" commandName="reglaCommand" role="form">
		<form:hidden path="id"/>
		<div class="tab-content">
			<div role="tabpanel" class="tab-pane active" id="comunes">
				<rip:inputSelect name="tipus" textKey="regla.form.camp.tipus" optionItems="${reglaTipusEnumOptions}" optionValueAttribute="value" optionTextKeyAttribute="text" required="true"/>
				<rip:inputText name="nom" textKey="regla.form.camp.nom" required="true"/>
				<rip:inputTextarea name="descripcio" textKey="regla.form.camp.descripcio"/>
				<rip:inputText name="assumpteCodi" textKey="regla.form.camp.assumpte.codi" required="true"/>
<%-- 				<rip:inputText name="unitatCodi" textKey="regla.form.camp.unitat"/> --%>
				<c:url value="/unitatajax/unitat" var="urlConsultaInicial"/>
				<c:url value="/unitatajax/unitats" var="urlConsultaLlistat"/>
				<rip:inputSuggest 
					name="unitatId" 
					textKey="bustia.form.camp.unitat"
					urlConsultaInicial="${urlConsultaInicial}" 
					urlConsultaLlistat="${urlConsultaLlistat}" 
					inline="false" 
					placeholderKey="bustia.form.camp.unitat"
					suggestValue="id"
					suggestText="nom" />
			</div>
			<div role="tabpanel" class="tab-pane" id="especifiques">
				<div id="camps_tipus_EXP_COMU">
					<rip:inputSelect name="metaExpedientId" textKey="regla.form.camp.metaexpedient" optionItems="${metaExpedients}" optionValueAttribute="id" optionTextAttribute="nom" required="true"/>
				</div>
				<div id="camps_tipus_EXP_CREAR">
					<rip:inputSelect name="arxiuId" textKey="regla.form.camp.arxiu" optionItems="${arxius}" optionValueAttribute="id" optionTextAttribute="nom" required="true"/>
				</div>
				<div id="camps_tipus_EXP_AFEGIR">
				</div>
				<div id="camps_tipus_BUSTIA">
					<rip:inputSelect name="bustiaId" textKey="regla.form.camp.bustia" optionItems="${busties}" optionValueAttribute="id" optionTextAttribute="nom" required="true"/>
				</div>
				<div id="camps_tipus_BACKOFFICE">
					<rip:inputSelect name="backofficeTipus" textKey="regla.form.camp.backoffice.tipus" optionItems="${backofficeTipusEnumOptions}" optionValueAttribute="value" optionTextKeyAttribute="text" required="true"/>
					<rip:inputText name="backofficeUrl" textKey="regla.form.camp.backoffice.url" required="true"/>
					<rip:inputText name="backofficeUsuari" textKey="regla.form.camp.backoffice.usuari"/>
					<rip:inputText name="backofficeContrasenya" textKey="regla.form.camp.backoffice.contrasenya"/>
					<rip:inputText name="backofficeIntents" textKey="regla.form.camp.backoffice.intents"/>
					<block id="backofficeTempsEntreIntentsBlock" style='display:${reglaCommand.backofficeTipus == "SISTRA" ? "inline" : "none"}'>
						<rip:inputText name="backofficeTempsEntreIntents" textKey="regla.form.camp.backoffice.temps.entre.intents" comment="regla.form.camp.backoffice.temps.entre.intents.info"/>
					</block>
				</div>
			</div>
		</div>
		<div id="modal-botons">
			<button type="submit" class="btn btn-success"><span class="fa fa-save"></span> <spring:message code="comu.boto.guardar"/></button>
			<a href="<c:url value="/regla"/>" class="btn btn-default" data-modal-cancel="true"><spring:message code="comu.boto.cancelar"/></a>
		</div>
	</form:form>
</body>
</html>
