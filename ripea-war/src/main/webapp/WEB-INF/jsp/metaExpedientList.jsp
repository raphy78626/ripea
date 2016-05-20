<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib tagdir="/WEB-INF/tags/ripea" prefix="rip"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<html>
<head>
	<title><spring:message code="metaexpedient.list.titol"/></title>
	<link href="<c:url value="/css/DT_bootstrap.css"/>" rel="stylesheet">
	<script src="<c:url value="/js/jquery.dataTables.js"/>"></script>
	<script src="<c:url value="/js/DT_bootstrap.js"/>"></script>
	<script src="<c:url value="/js/jsrender.min.js"/>"></script>
	<script src="<c:url value="/js/ripea.datatable.js"/>"></script>
	<script src="<c:url value="/js/ripea.modal.js"/>"></script>
<script>
$(document).ready(function() {
	$("#taulaDades").ripeaDataTable({
		ajaxSourceUrl: "<c:url value="/metaExpedient/datatable"/>",
		localeUrl: "<c:url value="/js/dataTables-locales/dataTables_locale_ca.txt"/>",
		alertesRefreshUrl: "<c:url value="/nodeco/util/alertes"/>"
	});
});
</script>
</head>
<body>
	<table id="taulaDades" class="table table-striped table-bordered" data-rdt-button-template="tableButtonsTemplate">
		<thead>
			<tr>
				<th data-rdt-property="id" width="4%" data-rdt-visible="false">#</th>
				<th data-rdt-property="pareId" width="4%" data-rdt-visible="false">#</th>
				<th data-rdt-property="codi"><spring:message code="metaexpedient.list.columna.codi"/></th>
				<th data-rdt-property="nom" data-rdt-sorting="desc"><spring:message code="metaexpedient.list.columna.nom"/></th>
				<th data-rdt-property="actiu" data-rdt-template="cellActiuTemplate">
					<spring:message code="metaexpedient.list.columna.actiu"/>
					<script id="cellActiuTemplate" type="text/x-jsrender">
						{{if actiu}}<span class="fa fa-check"></span>{{/if}}
					</script>
				</th>
				<th data-rdt-property="metaDocumentsCount" data-rdt-template="cellMetaDocumentsTemplate" data-rdt-sortable="false" width="10%">
					<script id="cellMetaDocumentsTemplate" type="text/x-jsrender">
						<a href="metaExpedient/{{:id}}/metaDocument" class="btn btn-default"><span class="fa fa-file-alt"></span>&nbsp;<spring:message code="metaexpedient.list.boto.meta.documents"/>&nbsp;<span class="badge">{{:metaDocumentsCount}}</span></a>
					</script>
				</th>
				<th data-rdt-property="metaDadesCount" data-rdt-template="cellMetaDadesTemplate" data-rdt-sortable="false" width="10%">
					<script id="cellMetaDadesTemplate" type="text/x-jsrender">
						<a href="metaExpedient/{{:id}}/metaDada" class="btn btn-default"><span class="fa fa-file-alt"></span>&nbsp;<spring:message code="metaexpedient.list.boto.meta.dades"/>&nbsp;<span class="badge">{{:metaDadesCount}}</span></a>
					</script>
				</th>
				<th data-rdt-property="arxiusCount" data-rdt-template="cellArxiusTemplate" data-rdt-sortable="false" width="10%">
					<script id="cellArxiusTemplate" type="text/x-jsrender">
						<a href="metaExpedient/{{:id}}/arxiu" class="btn btn-default"><span class="fa fa-file-alt"></span>&nbsp;<spring:message code="metaexpedient.list.boto.arxius"/>&nbsp;<span class="badge">{{:arxiusCount}}</span></a>
					</script>
				</th>
				<th data-rdt-property="permisosCount" data-rdt-template="cellPermisosTemplate" data-rdt-sortable="false" width="10%">
					<script id="cellPermisosTemplate" type="text/x-jsrender">
						<a href="metaExpedient/{{:id}}/permis" class="btn btn-default"><span class="fa fa-file-alt"></span>&nbsp;<spring:message code="metaexpedient.list.boto.permisos"/>&nbsp;<span class="badge">{{:permisosCount}}</span></a>
					</script>
				</th>
				<th data-rdt-property="id" data-rdt-template="cellAccionsTemplate" data-rdt-sortable="false" width="10%">
					<script id="cellAccionsTemplate" type="text/x-jsrender">
						<div class="dropdown">
							<button class="btn btn-primary" data-toggle="dropdown"><span class="fa fa-cog"></span>&nbsp;<spring:message code="comu.boto.accions"/>&nbsp;<span class="caret"></span></button>
							<ul class="dropdown-menu">
								<li><a href="metaExpedient/{{:id}}" data-rdt-link-modal="true"><span class="fa fa-pencil"></span>&nbsp;&nbsp;<spring:message code="comu.boto.modificar"/></a></li>
								{{if !actiu}}
								<li><a href="metaExpedient/{{:id}}/enable" data-rdt-link-ajax="true"><span class="fa fa-check"></span>&nbsp;&nbsp;<spring:message code="comu.boto.activar"/></a></li>
								{{else}}
								<li><a href="metaExpedient/{{:id}}/disable" data-rdt-link-ajax="true"><span class="fa fa-times"></span>&nbsp;&nbsp;<spring:message code="comu.boto.desactivar"/></a></li>
								{{/if}}
								<li><a href="metaExpedient/{{:id}}/delete" data-rdt-link-ajax="true" data-rdt-link-confirm="<spring:message code="metaexpedient.list.confirmacio.esborrar"/>"><span class="fa fa-trash-o"></span>&nbsp;&nbsp;<spring:message code="comu.boto.esborrar"/></a></li>
							</ul>
						</div>
					</script>
				</th>
			</tr>
		</thead>
	</table>
	<script id="tableButtonsTemplate" type="text/x-jsrender">
		<p style="text-align:right"><a class="btn btn-default" href="metaExpedient/new" data-rdt-link-modal="true"><span class="fa fa-plus"></span>&nbsp;<spring:message code="metaexpedient.list.boto.nou"/></a></p>
	</script>
</body>