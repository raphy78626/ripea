<%@page import="es.caib.ripea.war.helper.EnumHelper"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib tagdir="/WEB-INF/tags/ripea" prefix="rip"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<%
java.util.List<EnumHelper.HtmlOption> tipusEnum = EnumHelper.getOptionsForEnum(
		es.caib.ripea.core.api.dto.InteressatTipusEnumDto.class,
		"interessat.tipus.enum.");
Boolean esRepresentant = (Boolean)request.getAttribute("esRepresentant");
if (esRepresentant != null && esRepresentant) {
	tipusEnum.remove(2);
}
pageContext.setAttribute(
		"tipusEnumOptions",
		tipusEnum);
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

<c:set var="titol">
	<c:choose>
		<c:when test="${esRepresentant}">
			<c:choose>
				<c:when test="${not empty interessatCommand.id}"><spring:message code="representant.form.edit.titol"/></c:when>
				<c:otherwise><spring:message code="representant.form.titol"/></c:otherwise>
			</c:choose>
		</c:when>
		<c:otherwise>
			<c:choose>
				<c:when test="${not empty interessatCommand.id}"><spring:message code="interessat.form.edit.titol"/></c:when>
				<c:otherwise><spring:message code="interessat.form.titol"/></c:otherwise>
			</c:choose>
		</c:otherwise>
	</c:choose>
</c:set>
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

	var munOrgan = '';
	var netejar = <c:out value="${empty interessatCommand.id and empty netejar}"/>;
	
	$('form').submit(function() {
		$('form input').removeAttr('disabled');
		return true;
	});
 	$('select#tipus').change(function() {
 		munOrgan = '';
 		var lPais = $("label[for='pais']");
		var lProvincia = $("label[for='provincia']");
		var lMunicipi = $("label[for='municipi']");
		var lAdresa = $("label[for='adresa']");
		var lCodiPostal = $("label[for='codiPostal']");

		$('#pais').select2("readonly", false);
		$('#provincia').select2("readonly", false);
		$('#municipi').select2("readonly", false);
		$('#codiPostal').prop("readonly", false);
		$('#adresa').prop("readonly", false);
		$('#documentTipus').select2("readonly", false);
		$('#documentNum').prop("readonly", false);

		// Netejar
		if (netejar) {
			$('input#documentNum').val('');
			$('input#nom').val('');
			$('input#llinatge1').val('');
			$('input#llinatge2').val('');
			$('input#raoSocial').val('');
			$('select#organCodi').val('');
			$('select#organCodi').change();
			$('input#email').val('');
			$('input#telefon').val('');
			$('textarea#observacions').val('');
			$('#pais').val("724");
			$('#pais').change();
// 			$('#provincia').prop("disabled", true);
// 			$('#municipi').prop("disabled", true);
 		}
 		netejar = true;
		
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
			$('select#documentTipus').prop( "disabled", false );
			$('select#documentTipus').closest(".form-group").removeClass('ocult');
			$('input#documentNum').prop( "disabled", false );
			$('input#documentNum').closest(".form-group").removeClass('ocult');
			
			$('input#raoSocial').prop( "disabled", true );
			$('input#raoSocial').closest(".form-group").addClass('ocult');
			
			$('select#organCodi').prop( "disabled", false );
			$('select#organCodi').closest(".form-group").removeClass('ocult');

			lPais.text("<spring:message code="interessat.form.camp.pais"/>")
			lProvincia.text("<spring:message code="interessat.form.camp.provincia"/>")
			lMunicipi.text("<spring:message code="interessat.form.camp.municipi"/>")
			lAdresa.text("<spring:message code="interessat.form.camp.adresa"/>")
			lCodiPostal.text("<spring:message code="interessat.form.camp.codiPostal"/>")

			$('#pais').select2("readonly", true);
			$('#provincia').select2("readonly", true);
			$('#municipi').select2("readonly", true);
			$('#codiPostal').prop("readonly", true);
			$('#adresa').prop("readonly", true);
			$('#documentTipus').select2("readonly", true);
			$('#documentTipus').val("NIF");
			$('#documentTipus').change();
			$('#documentNum').prop("readonly", true);

// 			$('select#organCodi').val('');
// 			$('select#organCodi').change();
					
 		}
 		var iframe = $('.modal-body iframe', window.parent.document);
		var height = $('html').height();
		iframe.height(height + 'px');
 	});
 	if ($("#id").val() != '') {
		$('select#tipus').prop( "readonly", true );
	}
 	$('input#documentNum').on('keydown', function(evt) {
		$(this).val(function (_, val) {
			return val.toUpperCase();
		});
	});
 	$('select#organCodi').change(function() {
 		munOrgan = '';
 	 	if ($(this).val() != "") {
	 		$.ajax({
				type: 'GET',
				url: "<c:url value="/expedient/organ/"/>" + $(this).val(),
				success: function(data) {
					$('#pais').val(data.codiPais);
					$('#pais').change();
// 					$('#provincia').select2("enable", (data.codiPais == '724'));
					$('#provincia').val(data.codiProvincia);
					$('#provincia').change();
// 					$('#municipi').select2("enable", (data.codiPais == '724'));
		 	 		$('#municipi').val(data.localitat);
		 	 		munOrgan = data.localitat;
		 	 		$('#municipi').change();
		 	 		$('#codiPostal').val(data.codiPostal);
		 	 		$('#adresa').val(data.adressa);
		 	 		$('#documentNum').val(data.nifCif);
				}
			});
 	 	} else {
 	 		$('#pais').val("");
 	 		$('#pais').change();
 	 		$('#provincia').val("");
 	 		$('#provincia').change();
 	 		$('#municipi').val("");
 	 		$('#municipi').change();
 	 		$('#codiPostal').val("");
 	 		$('#adresa').val("");
 	 		$('#documentNum').val("");
 	 	}
 	});
 	$('select#pais').change(function() {
 		if ($(this).val() == '724') {
 			$('#provincia').select2("enable", true);
			$('#municipi').select2("enable", true);
		} else {
			$('#provincia').val("");
 	 		$('#provincia').change();
			$('#provincia').select2("enable", false);
			$('#municipi').val("");
 	 		$('#municipi').change();
			$('#municipi').select2("enable", false);
		}
 	});
 	$('select#provincia').change(function(valor) {
 		if ($(this).val() != '') {
 			$.ajax({
				type: 'GET',
				url: "<c:url value="/expedient/municipis/"/>" + $(this).val(),
				success: function(data) {
					var selMunicipi = $('#municipi');
					selMunicipi.empty();
					selMunicipi.append("<option value=\"\"></option>");
					if (data && data.length > 0) {
						var items = [];
						$.each(data, function(i, val) {
							items.push({
								"id": val.codi,
								"text": val.nom
							});
							selMunicipi.append("<option value=\"" + val.codi + "\">" + val.nom + "</option>");
						});
					}
					var select2Options = {theme: 'bootstrap', allowClear: true, minimumResultsForSearch: "6"};
					selMunicipi.select2("destroy");
					selMunicipi.select2(select2Options);
					if (munOrgan != '')
						selMunicipi.val(munOrgan);
					selMunicipi.trigger("change");
				}
			});
 	 	}
 	});
	
	$('select#tipus').change();
// 	$('#pais').change();
});
</script>
</head>
<body>

	<c:choose>
		<c:when test="${not empty esRepresentant}">
			<c:set var="formAction"><rip:modalUrl value="/expedient/${expedientId}/interessat/${interessatId}/representant"/></c:set>
		</c:when>
		<c:otherwise>
			<c:set var="formAction"><rip:modalUrl value="/expedient/${expedientId}/interessat"/></c:set>
		</c:otherwise>
	</c:choose>
	
	<form:form action="${formAction}" method="post" cssClass="form-horizontal" commandName="interessatCommand">
		<form:hidden path="entitatId"/>
		<form:hidden path="id"/>
		<input type="hidden" id="id"/>
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
<%-- 			<div class="col-xs-6"><rip:inputText name="municipi" textKey="interessat.form.camp.municipi" required="true"/></div> --%>
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
