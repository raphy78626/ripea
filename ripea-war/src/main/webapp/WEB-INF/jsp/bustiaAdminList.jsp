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
	<script src="<c:url value="/webjars/datatables.net/1.10.11/js/jquery.dataTables.min.js"/>"></script>
	<script src="<c:url value="/webjars/datatables.net-bs/1.10.11/js/dataTables.bootstrap.min.js"/>"></script>
	<link href="<c:url value="/webjars/datatables.net-bs/1.10.11/css/dataTables.bootstrap.min.css"/>" rel="stylesheet"></link>
	<script src="<c:url value="/webjars/jsrender/1.0.0-rc.70/jsrender.min.js"/>"></script>
	<link href="<c:url value="/webjars/jstree/3.2.1/dist/themes/default/style.min.css"/>" rel="stylesheet">
	<script src="<c:url value="/webjars/jstree/3.2.1/dist/jstree.min.js"/>"></script>
	<script src="<c:url value="/js/webutil.common.js"/>"></script>
	<script src="<c:url value="/js/webutil.datatable.js"/>"></script>
	<script src="<c:url value="/js/webutil.modal.js"/>"></script>
<script>
function changedCallback(e, data) {
	var baseUrl = '${unitatCodiUrlPrefix}bustiaAdmin/unitat/' + data.node.id;
	$('#bustia-boto-nova').attr('href', baseUrl + '/new');
	$('#busties').webutilDatatable('refresh-url', baseUrl  + '/datatable');
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
			<table id="busties" data-toggle="datatable" data-url="<c:url value="/bustiaAdmin/unitat/${unitatCodiPerUrl}/datatable"/>" data-search-enabled="false" data-paging-enabled="false" data-default-order="1" data-default-dir="asc" data-botons-template="#botonsTemplate" class="table table-striped table-bordered" style="width:100%">
				<thead>
					<tr>
						<th data-col-name="id" width="4%" data-visible="false">#</th>
						<th data-col-name="nom"><spring:message code="bustia.list.columna.nom"/></th>
						<th data-col-name="perDefecte" data-template="#cellPerDefecteTemplate">
							<spring:message code="bustia.list.columna.per.defecte"/>
							<script id="cellPerDefecteTemplate" type="text/x-jsrender">
								{{if perDefecte}}<span class="fa fa-check"></span>{{/if}}
							</script>
						</th>
						<th data-col-name="activa" data-template="#cellActivaTemplate">
							<spring:message code="bustia.list.columna.activa"/>
							<script id="cellActivaTemplate" type="text/x-jsrender">
								{{if activa}}<span class="fa fa-check"></span>{{/if}}
							</script>
						</th>
						<th data-col-name="permisosCount" data-template="#cellPermisosCountTemplate" data-orderable="false" width="10%">
							<script id="cellPermisosCountTemplate" type="text/x-jsrender">
								<a href="${unitatCodiUrlPrefix}bustiaAdmin/{{:id}}/permis" class="btn btn-default"><span class="fa fa-file-alt"></span>&nbsp;<spring:message code="bustia.list.boto.permisos"/>&nbsp;<span class="badge">{{:permisosCount}}</span></a>
							</script>
						</th>
						<th data-col-name="id" data-template="#cellAccionsTemplate" data-orderable="false" width="10%">
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
			<script id="botonsTemplate" type="text/x-jsrender">
				<p style="text-align:right"><a id="bustia-boto-nova" class="btn btn-default" href="${unitatCodiUrlPrefix}bustiaAdmin/unitat/${unitatCodiPerUrl}/new" data-toggle="modal"><span class="fa fa-plus"></span>&nbsp;<spring:message code="bustia.list.boto.nova.bustia"/></a></p>
			</script>
		</div>
	</div>
</body>