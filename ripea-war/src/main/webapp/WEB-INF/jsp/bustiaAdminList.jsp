<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib tagdir="/WEB-INF/tags/ripea" prefix="rip"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<c:set var="unitatCodiUrlPrefix" value=""/>
<c:choose>
	<c:when test="${not empty unitatCodi}">
		<c:set var="unitatCodiPerUrl" value="${unitatCodi}"/>
		<c:set var="unitatCodiUrlPrefix" value="../../"/>
	</c:when>
	<c:otherwise><c:set var="unitatCodiPerUrl" value="null"/></c:otherwise>
</c:choose>
<html>
<head>
	<title><spring:message code="bustia.list.titol"/></title>
	<link href="<c:url value="/css/DT_bootstrap.css"/>" rel="stylesheet">
	<script src="<c:url value="/js/jquery.dataTables.js"/>"></script>
	<script src="<c:url value="/js/DT_bootstrap.js"/>"></script>
	<script src="<c:url value="/js/jsrender.min.js"/>"></script>
	<script src="<c:url value="/js/ripea.datatable.js"/>"></script>
	<script src="<c:url value="/js/ripea.modal.js"/>"></script>
	<link href="<c:url value="/css/jstree.min.css"/>" rel="stylesheet">
	<script src="<c:url value="/js/jstree.min.js"/>"></script>
	<script src="<c:url value="/js/jquery.dataTables.fnReloadAjax.js"/>"></script>
<script>
$(document).ready(function() {
	$("#taulaDades").ripeaDataTable({
		ajaxSourceUrl: "<c:url value="/bustiaAdmin/unitat/${unitatCodiPerUrl}/datatable"/>",
		localeUrl: "<c:url value="/js/dataTables-locales/dataTables_locale_ca.txt"/>",
		alertesRefreshUrl: "<c:url value="/nodeco/util/alertes"/>"
	});
});
function changedCallback(e, data) {
	var baseUrl = '${unitatCodiUrlPrefix}bustiaAdmin/unitat/' + data.node.id;
	$('#tableButtonsTemplate').html('<p style="text-align:right"><a class="btn btn-default" href="' + baseUrl + '/new" data-rdt-link-modal="true"><span class="fa fa-plus"></span>&nbsp;<spring:message code="bustia.list.boto.nova.bustia"/></a></p>');
	$('#taulaDades').dataTable().fnReloadAjax(baseUrl  + '/datatable');
	$('#taulaLlistat').css('visibility', '');
	
}
</script>
</head>
<body>
	<div class="row">
		<div class="col-md-6">
			<rip:arbre id="arbreUnitats" arbre="${arbreUnitatsOrganitzatives}" atributId="codi" atributNom="denominacio" changedCallback="changedCallback" seleccionatId="${unitatCodi}"/>
		</div>
		<div id="taulaLlistat" class="col-md-6"<c:if test="${empty unitatCodi}"> style="visibility:hidden"</c:if>>
			<table id="taulaDades" class="table table-striped table-bordered" data-rdt-button-template="tableButtonsTemplate" data-rdt-paginable="false">
				<thead>
					<tr>
						<th data-rdt-property="id" width="4%" data-rdt-visible="false">#</th>
						<th data-rdt-property="nom" data-rdt-sorting="desc"><spring:message code="bustia.list.columna.nom"/></th>
						<th data-rdt-property="perDefecte" data-rdt-template="cellPerDefecteTemplate">
							<spring:message code="bustia.list.columna.per.defecte"/>
							<script id="cellPerDefecteTemplate" type="text/x-jsrender">
								{{if perDefecte}}<span class="fa fa-check"></span>{{/if}}
							</script>
						</th>
						<th data-rdt-property="activa" data-rdt-template="cellActivaTemplate">
							<spring:message code="bustia.list.columna.activa"/>
							<script id="cellActivaTemplate" type="text/x-jsrender">
								{{if activa}}<span class="fa fa-check"></span>{{/if}}
							</script>
						</th>
						<th data-rdt-property="permisosCount" data-rdt-template="cellPermisosCountTemplate" data-rdt-sortable="false" width="10%">
							<script id="cellPermisosCountTemplate" type="text/x-jsrender">
								<a href="${unitatCodiUrlPrefix}bustiaAdmin/{{:id}}/permis" class="btn btn-default"><span class="fa fa-file-alt"></span>&nbsp;<spring:message code="bustia.list.boto.permisos"/>&nbsp;<span class="badge">{{:permisosCount}}</span></a>
							</script>
						</th>
						<th data-rdt-property="id" data-rdt-template="cellAccionsTemplate" data-rdt-sortable="false" width="10%">
							<script id="cellAccionsTemplate" type="text/x-jsrender">
								<div class="dropdown">
									<button class="btn btn-primary" data-toggle="dropdown"><span class="fa fa-cog"></span>&nbsp;<spring:message code="comu.boto.accions"/>&nbsp;<span class="caret"></span></button>
									<ul class="dropdown-menu">
										<li><a href="${unitatCodiUrlPrefix}bustiaAdmin/{{:id}}" data-rdt-link-modal="true"><span class="fa fa-pencil"></span>&nbsp;&nbsp;<spring:message code="comu.boto.modificar"/></a></li>
										<li><a href="${unitatCodiUrlPrefix}bustiaAdmin/{{:id}}/default" data-rdt-link-ajax="true"><span class="fa fa-check-square-o"></span>&nbsp;&nbsp;<spring:message code="bustia.list.accio.per.defecte"/></a></li>
										{{if !activa}}
										<li><a href="${unitatCodiUrlPrefix}bustiaAdmin/{{:id}}/enable" data-rdt-link-ajax="true"><span class="fa fa-check"></span>&nbsp;&nbsp;<spring:message code="comu.boto.activar"/></a></li>
										{{else}}
										<li><a href="${unitatCodiUrlPrefix}bustiaAdmin/{{:id}}/disable" data-rdt-link-ajax="true"><span class="fa fa-times"></span>&nbsp;&nbsp;<spring:message code="comu.boto.desactivar"/></a></li>
										{{/if}}
										<li><a href="${unitatCodiUrlPrefix}bustiaAdmin/{{:id}}/delete" data-rdt-link-ajax="true" data-rdt-link-confirm="<spring:message code="bustia.list.confirmacio.esborrar"/>"><span class="fa fa-trash-o"></span>&nbsp;&nbsp;<spring:message code="comu.boto.esborrar"/></a></li>
									</ul>
								</div>
							</script>
						</th>
					</tr>
				</thead>
			</table>
			<script id="tableButtonsTemplate" type="text/x-jsrender">
				<p style="text-align:right"><a class="btn btn-default" href="${unitatCodiUrlPrefix}bustiaAdmin/unitat/${unitatCodiPerUrl}/new" data-rdt-link-modal="true"><span class="fa fa-plus"></span>&nbsp;<spring:message code="bustia.list.boto.nova.bustia"/></a></p>
			</script>
		</div>
	</div>
</body>