<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib tagdir="/WEB-INF/tags/ripea" prefix="rip"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<c:set var="idioma"><%=org.springframework.web.servlet.support.RequestContextUtils.getLocale(request).getLanguage()%></c:set>
<%
pageContext.setAttribute(
		"contenidorTipusEnumOptions",
		es.caib.ripea.war.helper.EnumHelper.getOptionsForEnum(
				es.caib.ripea.core.api.dto.ContenidorTipusEnumDto.class,
				"contenidor.tipus.enum."));
pageContext.setAttribute(
		"contenidorAdminOpcionsEsborratEnumOptions",
		es.caib.ripea.war.helper.EnumHelper.getOptionsForEnum(
				es.caib.ripea.war.command.ContenidorFiltreCommand.ContenidorFiltreOpcionsEsborratEnum.class,
				"contenidor.admin.opcions.esborrat.enum."));
%>
<html>
<head>
	<title><spring:message code="contenidor.admin.titol"/></title>
	<link href="<c:url value="/css/DT_bootstrap.css"/>" rel="stylesheet">
	<script src="<c:url value="/js/jquery.dataTables.js"/>"></script>
	<script src="<c:url value="/js/DT_bootstrap.js"/>"></script>
	<script src="<c:url value="/js/jsrender.min.js"/>"></script>
	<script src="<c:url value="/js/ripea.datatable.js"/>"></script>
	<script src="<c:url value="/js/ripea.modal.js"/>"></script>
	<link href="<c:url value="/css/datepicker.css"/>" rel="stylesheet"/>
	<script src="<c:url value="/js/bootstrap-datepicker.js"/>"></script>
	<script src="<c:url value="/js/datepicker-locales/bootstrap-datepicker.${idioma}.js"/>"></script>
	<link href="<c:url value="/css/select2.css"/>" rel="stylesheet"/>
	<link href="<c:url value="/css/select2-bootstrap.css"/>" rel="stylesheet"/>
	<script src="<c:url value="/js/select2.min.js"/>"></script>
	<script src="<c:url value="/js/select2-locales/select2_locale_${idioma}.js"/>"></script>
<script>
$(document).ready(function() {
	$("#taulaDades").ripeaDataTable({
		ajaxSourceUrl: "<c:url value="/contenidorAdmin/datatable"/>",
		localeUrl: "<c:url value="/js/dataTables-locales/dataTables_locale_ca.txt"/>",
		alertesRefreshUrl: "<c:url value="/nodeco/util/alertes"/>"
	});
	$('#tipus').on('change', function() {
		var tipus = $(this).val();
		$('#metaNodeId').select2('val', '', true);
		$('#metaNodeId option[value!=""]').remove();
		var metaNodeRefresh = function(data) {
			for (var i = 0; i < data.length; i++) {
				$('#metaNodeId').append('<option value="' + data[i].id + '">' + data[i].nom + '</option>');
			}
		};
		if (tipus == 'EXPEDIENT') {
			$.get("<c:url value="/metaExpedient/findAll"/>")
			.done(metaNodeRefresh)
			.fail(function() {
				alert("<spring:message code="error.jquery.ajax"/>");
			});
		} else if (tipus == 'DOCUMENT') {
			$.get("<c:url value="/metaDocument/findAll"/>")
			.done(metaNodeRefresh)
			.fail(function() {
				alert("<spring:message code="error.jquery.ajax"/>");
			});
		}
	});
	$('#tipus').trigger('change');
});
</script>
</head>
<body>
	<form:form action="" method="post" cssClass="well" commandName="contenidorFiltreCommand">
		<div class="row">
			<div class="col-md-7">
				<rip:inputText name="nom" inline="true" placeholderKey="contenidor.admin.filtre.nom"/>
			</div>
			<div class="col-md-2">
				<rip:inputSelect name="tipus" optionItems="${contenidorTipusEnumOptions}" optionValueAttribute="value" optionTextKeyAttribute="text" emptyOption="true" placeholderKey="contenidor.admin.filtre.tipus" inline="true"/>
			</div>
			<div class="col-md-3">
				<rip:inputSelect name="metaNodeId" optionItems="${metaNodes}" optionValueAttribute="id" optionTextAttribute="nom" emptyOption="true" placeholderKey="contenidor.admin.filtre.metanode" inline="true"/>
			</div>
		</div>
		<div class="row">
			<div class="col-md-2">
				<rip:inputDate name="dataCreacioInici" inline="true" placeholderKey="contenidor.admin.filtre.data.inici"/>
			</div>
			<div class="col-md-2">
				<rip:inputDate name="dataCreacioFi" inline="true" placeholderKey="contenidor.admin.filtre.data.fi"/>
			</div>
			<div class="col-md-3">
				<rip:inputSelect name="opcionsEsborrat" optionItems="${contenidorAdminOpcionsEsborratEnumOptions}" optionValueAttribute="value" optionTextKeyAttribute="text" emptyOption="false" placeholderKey="contenidor.admin.filtre.esborrat" inline="true"/>
			</div>
			<div class="col-md-5 pull-right">
				<div class="pull-right">
					<button type="submit" name="accio" value="netejar" class="btn btn-default"><spring:message code="comu.boto.netejar"/></button>
					<button type="submit" name="accio" value="filtrar" class="btn btn-primary"><span class="fa fa-filter"></span> <spring:message code="comu.boto.filtrar"/></button>
				</div>
			</div>
		</div>
	</form:form>
	<table id="taulaDades" class="table table-bordered table-striped" data-rdt-filtre-form-id="contenidorFiltreCommand">
		<thead>
			<tr>
				<th data-rdt-property="id" data-rdt-visible="false">#</th>
				<th data-rdt-property="escriptori" data-rdt-visible="false">#</th>
				<th data-rdt-property="escriptori" data-rdt-visible="false">#</th>
				<th data-rdt-property="expedient" data-rdt-visible="false">#</th>
				<th data-rdt-property="carpeta" data-rdt-visible="false">#</th>
				<th data-rdt-property="document" data-rdt-visible="false">#</th>
				<th data-rdt-property="arxiv" data-rdt-visible="false">#</th>
				<th data-rdt-property="bustia" data-rdt-visible="false">#</th>
				<th data-rdt-property="esborrat" data-rdt-visible="false">#</th>
				<th data-rdt-property="nom" data-rdt-template="cellNomTemplate" width="25%">
					<spring:message code="contenidor.admin.columna.nom"/>
					<script id="cellNomTemplate" type="text/x-jsrender">
						{{if expedient}}<span class="fa fa-briefcase"></span>{{else document}}<span class="fa fa-file"></span>{{else carpeta}}<span class="fa fa-folder"></span>{{/if}}
						{{:nom}}
						{{if esborrat}}<span class="fa fa-trash-o pull-right" title="<spring:message code="contenidor.admin.columna.esborrat"/>"></span>{{/if}}
					</script>
				</th>
				<th data-rdt-property="metaNode.nom" width="15%"><spring:message code="contenidor.admin.columna.metanode"/></th>
				<th data-rdt-property="createdBy.nom" width="10%"><spring:message code="contenidor.admin.columna.creat.per"/></th>
				<th data-rdt-property="createdDate" data-rdt-type="datetime" data-rdt-sorting="desc" width="10%"><spring:message code="contenidor.admin.columna.creat.el"/></th>
				<th data-rdt-property="path" data-rdt-template="cellPathTemplate" data-rdt-sortable="false" width="30%">
					<spring:message code="contenidor.admin.columna.situacio"/>
					<script id="cellPathTemplate" type="text/x-jsrender">
						{{for path}}/
							{{if escriptori}}<span class="fa fa-desktop" title="<spring:message code="contenidor.contingut.path.escriptori"/>"></span>
							{{else expedient}}<span class="fa fa-briefcase" title="<spring:message code="contenidor.contingut.icona.expedient"/>"></span>
							{{else carpeta}}<span class="fa fa-folder" title="<spring:message code="contenidor.contingut.icona.carpeta"/>"></span>
							{{else document}}<span class="fa fa-file" title="<spring:message code="contenidor.contingut.icona.document"/>"></span>
							{{else arxiv}}{{if #getIndex() == 0}}<span class="fa fa-sitemap" title="<spring:message code="contenidor.contingut.icona.unitat"/>"></span>{{else}}<span class="fa fa-archive" title="<spring:message code="contenidor.contingut.icona.arxiu"/>"></span>{{/if}}
							{{else bustia}}{{if #getIndex() == 0}}<span class="fa fa-sitemap" title="<spring:message code="contenidor.contingut.icona.unitat"/>"></span>{{else}}<span class="fa fa-inbox" title="<spring:message code="contenidor.contingut.icona.bustia"/>"></span>{{/if}}{{/if}}
							{{if escriptori}}{{:createdBy.nom}}{{else}}{{:nom}}{{/if}}
							<%--{{if #getIndex() != #get("array").data.length - 1}}, {{/if}}--%>
						{{/for}}
					</script>
				</th>
				<th data-rdt-property="id" data-rdt-template="cellAccionsTemplate" data-rdt-sortable="false" width="10%">
					<script id="cellAccionsTemplate" type="text/x-jsrender">
						<div class="dropdown">
							<button class="btn btn-primary" data-toggle="dropdown"><span class="fa fa-cog"></span>&nbsp;<spring:message code="comu.boto.accions"/>&nbsp;<span class="caret"></span></button>
							<ul class="dropdown-menu">
								<li><a href="contenidorAdmin/{{:id}}/info" data-rdt-link-ajax="true"><span class="fa fa-info-circle"></span>&nbsp;&nbsp;<spring:message code="contenidor.admin.boto.detalls"/></a></li>
								{{if esborrat}}
								<li><a href="contenidorAdmin/{{:id}}/undelete" data-rdt-link-ajax="true"><span class="fa fa-undo"></span>&nbsp;&nbsp;<spring:message code="contenidor.admin.boto.recuperar"/></a></li>
								<li><a href="contenidorAdmin/{{:id}}/delete" data-rdt-link-ajax="true" data-rdt-link-confirm="<spring:message code="contenidor.admin.confirmacio.esborrar"/>"><span class="fa fa-trash-o"></span>&nbsp;&nbsp;<spring:message code="comu.boto.esborrar"/></a></li>
								{{/if}}
							</ul>
						</div>
					</script>
				</th>
			</tr>
		</thead>
	</table>
</body>