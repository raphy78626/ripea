<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib tagdir="/WEB-INF/tags/ripea" prefix="rip"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%
pageContext.setAttribute(
		"registreAccioEnumOptions",
		es.caib.ripea.war.helper.HtmlSelectOptionHelper.getOptionsForEnum(
				es.caib.ripea.core.api.dto.RegistreAccioEnumDto.class,
				"registre.anotacio.accio.enum."));
pageContext.setAttribute(
		"registreTipusEnumOptions",
		es.caib.ripea.war.helper.HtmlSelectOptionHelper.getOptionsForEnum(
				es.caib.ripea.core.api.dto.RegistreTipusEnumDto.class,
				"registre.anotacio.tipus.enum."));
%>
<html>
<head>
	<title><spring:message code="bustia.pendent.titol"/></title>
	<link href="<c:url value="/css/DT_bootstrap.css"/>" rel="stylesheet">
	<script src="<c:url value="/js/jquery.dataTables.js"/>"></script>
	<script src="<c:url value="/js/DT_bootstrap.js"/>"></script>
	<script src="<c:url value="/js/jsrender.min.js"/>"></script>
	<script src="<c:url value="/js/ripea.datatable.js"/>"></script>
	<script src="<c:url value="/js/ripea.modal.js"/>"></script>
<script>
var accioText = new Array();
<c:forEach var="option" items="${registreAccioEnumOptions}">
accioText["${option.value}"] = "<spring:message code="${option.text}"/>";
</c:forEach>
var tipusText = new Array();
<c:forEach var="option" items="${registreTipusEnumOptions}">
tipusText["${option.value}"] = "<spring:message code="${option.text}"/>";
</c:forEach>
$(document).ready(function() {
	$("#taulaDadesContingut").ripeaDataTable({
		ajaxSourceUrl: "<c:url value="/bustiaUser/${bustia.id}/pendent/contingut/datatable"/>",
		drawCallback: function() {
			if ($('#taulaDadesContingut tbody tr td:eq(0)').hasClass('dataTables_empty'))
				$('#contingut-count').html('0');
			else
				$('#contingut-count').html($('#taulaDadesContingut tbody tr').length);
			$.get("<c:url value="/bustiaUser/${bustia.id}/pendent/count"/>", function(data) {
				$('#bustia-pendent-count').html(data);
			});
		},
		localeUrl: "<c:url value="/js/dataTables-locales/dataTables_locale_ca.txt"/>",
		alertesRefreshUrl: "<c:url value="/nodeco/util/alertes"/>"
	});
	$("#taulaDadesRegistre").ripeaDataTable({
		ajaxSourceUrl: "<c:url value="/bustiaUser/${bustia.id}/pendent/registre/datatable"/>",
		drawCallback: function() {
			if ($('#taulaDadesRegistre tbody tr td:eq(0)').hasClass('dataTables_empty'))
				$('#registre-count').html('0');
			else
				$('#registre-count').html($('#taulaDadesRegistre tbody tr').length);
			$.get("<c:url value="/bustiaUser/${bustia.id}/pendent/count"/>", function(data) {
				$('#bustia-pendent-count').html(data);
			});
		},
		localeUrl: "<c:url value="/js/dataTables-locales/dataTables_locale_ca.txt"/>",
		alertesRefreshUrl: "<c:url value="/nodeco/util/alertes"/>"
	});
});
</script>
</head>
<body>
	<rip:blocContenidorPath contenidor="${bustia}"/>
	<ul class="nav nav-tabs">
		<li class="active"><a href="#contingut" data-toggle="tab"><spring:message code="bustia.pendent.pipella.contingut"/>&nbsp;<span id="contingut-count" class="badge">&nbsp;</span></a></li>
		<li><a href="#registre" data-toggle="tab"><spring:message code="bustia.pendent.pipella.registre"/>&nbsp;<span id="registre-count" class="badge">&nbsp;</span></a></li>
	</ul>
	<br/>
	<div class="tab-content">
		<div class="tab-pane fade in active" id="contingut">
			<table id="taulaDadesContingut" class="table table-bordered table-striped" data-rdt-paginable="false">
				<thead>
					<tr>
						<th data-rdt-property="id" data-rdt-visible="false">#</th>
						<th data-rdt-property="expedient" data-rdt-visible="false">#</th>
						<th data-rdt-property="carpeta" data-rdt-visible="false">#</th>
						<th data-rdt-property="document" data-rdt-visible="false">#</th>
						<th data-rdt-property="node" data-rdt-visible="false">#</th>
						<th data-rdt-property="darrerMovimentData" data-rdt-type="datetime" data-rdt-sortable="false" width="15%"><spring:message code="bustia.pendent.contingut.columna.data"/></th>
						<th data-rdt-property="darrerMovimentUsuari.nom" data-rdt-sortable="false" width="15%"><spring:message code="bustia.pendent.contingut.columna.remitent"/></th>
						<th data-rdt-property="nom" data-rdt-template="cellNomTemplate" data-sortable="false" width="30%">
							<spring:message code="bustia.pendent.contingut.columna.element"/>
							<script id="cellNomTemplate" type="text/x-jsrender">
								{{if expedient}}<span class="fa fa-briefcase" title="<spring:message code="contenidor.contingut.icona.expedient"/>"></span>{{/if}}
								{{if carpeta}}<span class="fa fa-folder" title="<spring:message code="contenidor.contingut.icona.carpeta"/>"></span>{{/if}}
								{{if document}}<span class="fa fa-file" title="<spring:message code="contenidor.contingut.icona.document"/>"></span>{{/if}}
								{{:nom}}
							</script>
						</th>
						<th data-rdt-property="darrerMovimentComentari" data-rdt-sortable="false" width="30%"><spring:message code="bustia.pendent.contingut.columna.comentari"/></th>
						<th data-rdt-property="id" data-rdt-sortable="false" data-rdt-template="cellAccionsContingutTemplate" width="10%">
							<script id="cellAccionsContingutTemplate" type="text/x-jsrender">
								<div class="dropdown">
									<button class="btn btn-primary" data-toggle="dropdown"><span class="fa fa-cog"></span>&nbsp;<spring:message code="comu.boto.accions"/>&nbsp;<span class="caret"></span></button>
									<ul class="dropdown-menu">
										<li><a href="../../contenidor/{{:id}}"><span class="fa fa-folder-open-o"></span>&nbsp;&nbsp;<spring:message code="bustia.pendent.contingut.accio.info"/></a></li>
										<li><a href="../../bustiaUser/${bustia.id}/pendent/contingut/{{:id}}/nouexp" data-rdt-link-modal="true"><span class="fa fa-plus"></span>&nbsp;&nbsp;<spring:message code="bustia.pendent.contingut.accio.nou.expedient"/>...</a></li>
										<li><a href="../../bustiaUser/${bustia.id}/pendent/contingut/{{:id}}/addexp" data-rdt-link-modal="true"><span class="fa fa-sign-in"></span>&nbsp;&nbsp;<spring:message code="bustia.pendent.contingut.accio.afegir.expedient"/>...</a></li>
										<li><a href="../../bustiaUser/${bustia.id}/pendent/contingut/{{:id}}/reenviar" data-rdt-link-modal="true"><span class="fa fa-envelope"></span>&nbsp;&nbsp;<spring:message code="bustia.pendent.contingut.accio.reenviar"/>...</a></li>
										<li><a href="../../bustiaUser/${bustia.id}/pendent/contingut/{{:id}}/agafar" data-rdt-link-ajax="true"><span class="fa fa-thumbs-o-up"></span>&nbsp;&nbsp;<spring:message code="bustia.pendent.contingut.accio.agafar"/></a></li>
									</ul>
								</div>
							</script>
						</th>
					</tr>
				</thead>
			</table>
		</div>
		<div class="tab-pane fade" id="registre">
			<table id="taulaDadesRegistre" class="table table-bordered table-striped" data-rdt-paginable="false">
				<thead>
					<tr>
						<th data-rdt-property="id" data-rdt-visible="false">#</th>
						<th data-rdt-property="tipus" data-rdt-template="cellTipusTemplate" data-rdt-sortable="false" width="10%">
							<spring:message code="bustia.pendent.registre.columna.tipus"/>
							<script id="cellTipusTemplate" type="text/x-jsrender">
								{{:~eval('tipusText["' + tipus + '"]')}}
							</script>
						</th>
						<th data-rdt-property="identificador" data-rdt-sortable="false" width="10%"><spring:message code="bustia.pendent.registre.columna.identificador"/></th>
						<th data-rdt-property="data" data-rdt-type="datetime" data-rdt-sortable="false" width="20%"><spring:message code="bustia.pendent.registre.columna.data"/></th>
						<th data-rdt-property="extracte" data-rdt-sortable="false" width="40%"><spring:message code="bustia.pendent.registre.columna.extracte"/></th>
						<th data-rdt-property="id" data-rdt-sortable="false" data-rdt-template="cellAccionsRegistreTemplate" width="10%">
							<script id="cellAccionsRegistreTemplate" type="text/x-jsrender">
								<div class="dropdown">
									<button class="btn btn-primary" data-toggle="dropdown"><span class="fa fa-cog"></span>&nbsp;<spring:message code="comu.boto.accions"/>&nbsp;<span class="caret"></span></button>
									<ul class="dropdown-menu">
										<li><a href="../../bustiaUser/${bustia.id}/pendent/registre/{{:id}}" data-rdt-link-modal="true"><span class="fa fa-info-circle"></span>&nbsp;&nbsp;<spring:message code="bustia.pendent.registre.accio.info"/>...</a></li>
										<li><a href="../../bustiaUser/${bustia.id}/pendent/registre/{{:id}}/nouexp" data-rdt-link-modal="true"><span class="fa fa-plus"></span>&nbsp;&nbsp;<spring:message code="bustia.pendent.registre.accio.nou.expedient"/>...</a></li>
										<li><a href="../../bustiaUser/${bustia.id}/pendent/registre/{{:id}}/addexp" data-rdt-link-modal="true"><span class="fa fa-sign-in"></span>&nbsp;&nbsp;<spring:message code="bustia.pendent.registre.accio.afegir.expedient"/>...</a></li>
										<li><a href="../../bustiaUser/${bustia.id}/pendent/registre/{{:id}}/reenviar" data-rdt-link-modal="true"><span class="fa fa-envelope"></span>&nbsp;&nbsp;<spring:message code="bustia.pendent.registre.accio.reenviar"/>...</a></li>
										<li><a href="../../bustiaUser/${bustia.id}/pendent/registre/{{:id}}/rebutjar" data-rdt-link-modal="true"><span class="fa fa-trash-o"></span>&nbsp;&nbsp;<spring:message code="bustia.pendent.registre.accio.rebutjar"/>...</a></li>
									</ul>
								</div>
							</script>
						</th>
					</tr>
				</thead>
			</table>
		</div>
	</div>
	<a href="<c:url value="/bustiaUser/unitat/${bustia.unitatCodi}"/>" class="btn btn-default pull-right"><span class="fa fa-arrow-left"></span>&nbsp;<spring:message code="comu.boto.tornar"/></a>
</body>
</html>