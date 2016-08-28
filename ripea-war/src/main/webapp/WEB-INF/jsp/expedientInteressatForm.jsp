<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib tagdir="/WEB-INF/tags/ripea" prefix="rip"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<%
pageContext.setAttribute(
		"tipusEnumOptions",
		es.caib.ripea.war.helper.EnumHelper.getOptionsForEnum(
				es.caib.ripea.core.api.dto.InteressatTipusEnumDto.class,
				"interessat.tipus.enum."));
pageContext.setAttribute(
		"documentTipusEnumOptions",
		es.caib.ripea.war.helper.EnumHelper.getOptionsForEnum(
				es.caib.ripea.core.api.dto.InteressatDocumentTipusEnumDto.class,
				"interessat.document.tipus.enum."));
pageContext.setAttribute(
		"idiomaEnumOptions",
		es.caib.ripea.war.helper.EnumHelper.getOptionsForEnum(
				es.caib.ripea.core.api.dto.IndiomaEnumDto.class,
				"idioma.enum."));
%>

<c:set var="titol"><spring:message code="interessat.form.titol"/></c:set>
<html>
<head>
	<title>${titol}</title>
	<link href="<c:url value="/css/select2.css"/>" rel="stylesheet"/>
	<link href="<c:url value="/css/select2-bootstrap.css"/>" rel="stylesheet"/>
	<script src="<c:url value="/js/select2.min.js"/>"></script>
	<rip:modalHead titol="${titol}" buttonContainerId="botons"/>
<style>
.tab-pane {
	margin-top: 1em;
}
body {
	font-size: 13px;
}
.control-label {
	padding-right: 5px !important;
}
</style>
<script type="text/javascript">
var interessatNoms = [];
var interessatLlinatges = [];
<c:forEach var="interessat" items="${interessats}">
interessatNoms['${interessat.id}'] = "${interessat.nom}";
<c:if test="${interessat.personaFisica}">
	interessatLlinatges['${interessat.id}'] = "${interessat.llinatges}";
</c:if>
</c:forEach>
$(document).ready(function() {

	$('form').submit(function() {
		$('form input').removeAttr('disabled');
		return true;
	});
 	$('select#tipus').change(function() {
 		var lPais = $("label[for='pais']");
		var lProvincia = $("label[for='provincia']");
		var lMunicipi = $("label[for='municipi']");
		var lAdresa = $("label[for='adresa']");
		var lCodiPostal = $("label[for='codiPostal']");

		if ($('select#pais').val() == '724') {
			$('select#provincia').prop("readonly", false);
		} else {
			$('select#provincia').val("");
			$('select#provincia').prop("readonly", true);
		}
		
 		if (this.value == '<%=es.caib.ripea.core.api.dto.InteressatTipusEnumDto.PERSONA_FISICA%>') {
			$('input#nom').prop( "disabled", false );
			$('input#nom').closest(".form-group").removeClass('ocult');
			$('input#llinatge1').prop( "disabled", false );
			$('input#llinatge1').closest(".form-group").removeClass('ocult');
			$('input#llinatge2').prop( "disabled", false );
			$('input#llinatge2').closest(".form-group").removeClass('ocult');
			$('select#documentTipus').prop( "disabled", false );
			$('select#documentTipus').closest(".form-group").removeClass('ocult');
			$('input#documentNum').prop( "disabled", false );
			$('input#documentNum').closest(".form-group").removeClass('ocult');
			
			$('input#raoSocial').prop( "disabled", true );
			$('input#raoSocial').closest(".form-group").addClass('ocult');
			
			$('select#organCodi').prop( "disabled", true );
			$('select#organCodi').closest(".form-group").addClass('ocult');

			lPais.text("<spring:message code="interessat.form.camp.pais"/>" + " *")
			lProvincia.text("<spring:message code="interessat.form.camp.provincia"/>" + " *")
			lMunicipi.text("<spring:message code="interessat.form.camp.municipi"/>" + " *")
			lAdresa.text("<spring:message code="interessat.form.camp.adresa"/>" + " *")
			lCodiPostal.text("<spring:message code="interessat.form.camp.codiPostal"/>" + " *")
 		} else if (this.value == '<%=es.caib.ripea.core.api.dto.InteressatTipusEnumDto.PERSONA_JURIDICA%>') {
 			$('input#nom').prop( "disabled", true );
			$('input#nom').closest(".form-group").addClass('ocult');
			$('input#llinatge1').prop( "disabled", true );
			$('input#llinatge1').closest(".form-group").addClass('ocult');
			$('input#llinatge2').prop( "disabled", true );
			$('input#llinatge2').closest(".form-group").addClass('ocult');
			$('select#documentTipus').prop( "disabled", false );
			$('select#documentTipus').closest(".form-group").removeClass('ocult');
			$('input#documentNum').prop( "disabled", false );
			$('input#documentNum').closest(".form-group").removeClass('ocult');
			
			$('input#raoSocial').prop( "disabled", false );
			$('input#raoSocial').closest(".form-group").removeClass('ocult');
			
			$('select#organCodi').prop( "disabled", true );
			$('select#organCodi').closest(".form-group").addClass('ocult');

			lPais.text("<spring:message code="interessat.form.camp.pais"/>" + " *")
			lProvincia.text("<spring:message code="interessat.form.camp.provincia"/>" + " *")
			lMunicipi.text("<spring:message code="interessat.form.camp.municipi"/>" + " *")
			lAdresa.text("<spring:message code="interessat.form.camp.adresa"/>" + " *")
			lCodiPostal.text("<spring:message code="interessat.form.camp.codiPostal"/>" + " *")
 	 	} else {
 	 		$('input#nom').prop( "disabled", true );
			$('input#nom').closest(".form-group").addClass('ocult');
			$('input#llinatge1').prop( "disabled", true );
			$('input#llinatge1').closest(".form-group").addClass('ocult');
			$('input#llinatge2').prop( "disabled", true );
			$('input#llinatge2').closest(".form-group").addClass('ocult');
			$('select#documentTipus').prop( "disabled", true );
			$('select#documentTipus').closest(".form-group").addClass('ocult');
			$('input#documentNum').prop( "disabled", true );
			$('input#documentNum').closest(".form-group").addClass('ocult');
			
			$('input#raoSocial').prop( "disabled", true );
			$('input#raoSocial').closest(".form-group").addClass('ocult');
			
			$('select#organCodi').prop( "disabled", false );
			$('select#organCodi').closest(".form-group").removeClass('ocult');

			lPais.text("<spring:message code="interessat.form.camp.pais"/>")
			lProvincia.text("<spring:message code="interessat.form.camp.provincia"/>")
			lMunicipi.text("<spring:message code="interessat.form.camp.municipi"/>")
			lAdresa.text("<spring:message code="interessat.form.camp.adresa"/>")
			lCodiPostal.text("<spring:message code="interessat.form.camp.codiPostal"/>")
 		}
 		var iframe = $('.modal-body iframe', window.parent.document);
		var height = $('html').height();
		iframe.height(height + 'px');
 	});
 	$('select#tipus').change();
 	if ($("#id").val() != '') {
		$('select#tipus').prop( "readonly", true );
	}
 	$('input#documentNum').on('keydown', function(evt) {
		$(this).val(function (_, val) {
			return val.toUpperCase();
		});
	});
 	$('select#organCodi').change(function() {
 	 	if ($(this).val() != "") {
	 		$.ajax({
				type: 'GET',
				url: "<c:url value="/expedient/organ/"/>" + $(this).val(),
				success: function(data) {
					debugger;
					$('#pais').val(data.codiPais);
					$('#pais').change();
					$('#provincia').prop("disabled", (data.codiPais != '724'));
					$('#provincia').val(data.codiProvincia);
					$('#provincia').change();
		 	 		$('#municipi').val(data.localitat);
		 	 		$('#municipi').change();
		 	 		$('#codiPostal').val(data.codiPostal);
		 	 		$('#adresa').val(data.adressa);
				}
			});
 	 	} else {
 	 		$('#provincia').val("");
 	 		$('#provincia').change();
 	 		$('#municipi').val("");
 	 		$('#municipi').change();
 	 		$('#codiPostal').val("");
 	 		$('#adresa').val("");
 	 	}
 	});
 	$('select#pais').change(function() {
 		if ($(this).val() == '724') {
			$('select#provincia').prop("disabled", false);
		} else {
			$('select#provincia').val("");
			$('select#provincia').prop("disabled", true);
		}
 	});
 	$('select#provincia').change(function() {

 	});
});
</script>
</head>
<body>

	<c:set var="formAction"><rip:modalUrl value="/expedient/${expedientId}/interessat"/></c:set>
	
	<form:form action="${formAction}" method="post" cssClass="form-horizontal" commandName="interessatCommand">
		<form:hidden path="entitatId"/>
		<form:hidden path="id"/>
		<rip:inputSelect name="tipus" textKey="interessat.form.camp.tipus" optionItems="${tipusEnumOptions}" optionTextKeyAttribute="text" optionValueAttribute="value" labelSize="2" />
		<div class="row">
			<div class="col-xs-6"><rip:inputSelect name="documentTipus" textKey="interessat.form.camp.document.tipus" optionItems="${documentTipusEnumOptions}" optionTextKeyAttribute="text" optionValueAttribute="value" /></div>
			<div class="col-xs-6"><rip:inputText name="documentNum" textKey="interessat.form.camp.document.numero" required="true"/></div>
		</div>
		<rip:inputText name="nom" textKey="interessat.form.camp.nom" required="true" labelSize="2"/>
		<div class="row">
			<div class="col-xs-6"><rip:inputText name="llinatge1" textKey="interessat.form.camp.llinatge1" required="true"/></div>
			<div class="col-xs-6"><rip:inputText name="llinatge2" textKey="interessat.form.camp.llinatge2"/></div>
		</div>	
		<rip:inputText name="raoSocial" textKey="interessat.form.camp.raoSocial" required="true" labelSize="2"/>
		<rip:inputSelect name="organCodi" textKey="interessat.form.camp.organCodi" optionItems="${unitatsOrganitzatives}" optionTextAttribute="denominacio" optionValueAttribute="codi" emptyOption="true" required="true" optionMinimumResultsForSearch="6" labelSize="2"/>
		<div class="row">
			<div class="col-xs-6"><rip:inputSelect name="pais" textKey="interessat.form.camp.pais" optionItems="${paisos}" optionTextAttribute="nom" optionValueAttribute="codi_numeric" emptyOption="true" optionMinimumResultsForSearch="6" required="true"/></div>
			<div class="col-xs-6"><rip:inputSelect name="provincia" textKey="interessat.form.camp.provincia" optionItems="${provincies}" optionTextAttribute="nom" optionValueAttribute="codi" emptyOption="true" optionMinimumResultsForSearch="6" required="true"/></div>
			<div class="col-xs-6"><rip:inputSelect name="municipi" textKey="interessat.form.camp.municipi" optionItems="${municipis}" optionTextAttribute="nom" optionValueAttribute="codi" emptyOption="true" optionMinimumResultsForSearch="6" required="true"/></div>
			<div class="col-xs-6"><rip:inputText name="codiPostal" textKey="interessat.form.camp.codiPostal" required="true"/></div>
		</div>
		<rip:inputTextarea name="adresa" textKey="interessat.form.camp.adresa" required="true" labelSize="2"/>
		<div class="row">
			<div class="col-xs-6"><rip:inputText name="email" textKey="interessat.form.camp.email"/></div>
			<div class="col-xs-6"><rip:inputText name="telefon" textKey="interessat.form.camp.telefon"/></div>
		</div>
		<rip:inputTextarea name="observacions" textKey="interessat.form.camp.observacions" labelSize="2"/>
		<div class="row">
			<div class="col-xs-6"><rip:inputSelect name="notificacioIdioma" textKey="interessat.form.camp.idioma" optionItems="${idiomaEnumOptions}" optionTextKeyAttribute="text" optionValueAttribute="value" /></div>
			<div class="col-xs-6"><rip:inputCheckbox name="notificacioAutoritzat" textKey="interessat.form.camp.autoritzat" labelSize="10"/></div>
		</div>
		
		<div id="modal-botons" class="well">
			<button type="submit" class="btn btn-success"><span class="fa fa-save"></span> <spring:message code="comu.boto.guardar"/></button>
 			<a href="<c:url value="/contenidor/${expedientId}"/>" class="btn btn-default modal-tancar"><spring:message code="comu.boto.cancelar"/></a>
		</div>
	</form:form>

</body>
</html>
