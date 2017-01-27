<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib tagdir="/WEB-INF/tags/ripea" prefix="rip"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<html>
<head>
	<title><spring:message code="metadocument.metadada.titol"/></title>
	<meta name="subtitle" content="${metaDocument.nom}"/>
	<script src="<c:url value="/webjars/datatables.net/1.10.11/js/jquery.dataTables.min.js"/>"></script>
	<script src="<c:url value="/webjars/datatables.net-bs/1.10.11/js/dataTables.bootstrap.min.js"/>"></script>
	<link href="<c:url value="/webjars/datatables.net-bs/1.10.11/css/dataTables.bootstrap.min.css"/>" rel="stylesheet"></link>
	<script src="<c:url value="/webjars/jsrender/1.0.0-rc.70/jsrender.min.js"/>"></script>
	<script src="<c:url value="/webjars/Sortable/1.4.2/Sortable.min.js"/>"></script>
	<script src="<c:url value="/js/webutil.common.js"/>"></script>
	<script src="<c:url value="/js/webutil.datatable.js"/>"></script>
	<script src="<c:url value="/js/webutil.modal.js"/>"></script>
</head>
<body>
	<div class="text-right" data-toggle="botons-titol">
		<a class="btn btn-default" href="metaDada/new" data-toggle="modal" data-datatable-id="metadades"><span class="fa fa-plus"></span>&nbsp;<spring:message code="metadocument.metadada.boto.afegir"/></a>
	</div>
	<table id="metadades" data-toggle="datatable" data-url="<c:url value="/metaDocument/${metaDocument.id}/metaDada/datatable"/>" data-drag-enabled="true" data-info-type="search" data-default-order="1" data-default-dir="asc" class="table table-striped table-bordered">
		<thead>
			<tr>
				<th data-col-name="metaDada.codi"><spring:message code="metadocument.metadada.columna.codi"/></th>
				<th data-col-name="metaDada.nom"><spring:message code="metadocument.metadada.columna.nom"/></th>
				<th data-col-name="metaDada.tipus" data-renderer="enum(MetaDadaTipusEnumDto)" data-orderable="false"><spring:message code="metadocument.metadada.columna.tipus"/></th>
				<th data-col-name="multiplicitat" data-renderer="enum(MultiplicitatEnumDto)" data-orderable="false"><spring:message code="metadocument.metadada.columna.multiplicitat"/></th>
				<th data-col-name="id" data-template="#cellAccionsTemplate" data-rdt-sortable="false" width="10%">
					<script id="cellAccionsTemplate" type="text/x-jsrender">
						<div class="dropdown">
							<button class="btn btn-primary" data-toggle="dropdown"><span class="fa fa-cog"></span>&nbsp;<spring:message code="comu.boto.accions"/>&nbsp;<span class="caret"></span></button>
							<ul class="dropdown-menu">
								<li><a href="../../metaDocument/${metaDocument.id}/metaDada/{{:id}}" data-toggle="modal"><span class="fa fa-pencil"></span>&nbsp;&nbsp;<spring:message code="comu.boto.modificar"/></a></li>
								<li><a href="../../metaDocument/${metaDocument.id}/metaDada/{{:id}}/delete" data-toggle="ajax" data-confirm="<spring:message code="metadocument.metadada.confirmacio.esborrar"/>"><span class="fa fa-trash-o"></span>&nbsp;&nbsp;<spring:message code="comu.boto.esborrar"/></a></li>
							</ul>
						</div>
					</script>
				</th>
			</tr>
		</thead>
	</table>
	<a href="<c:url value="/metaDocument"/>" class="btn btn-default pull-right"><span class="fa fa-arrow-left"></span> <spring:message code="comu.boto.tornar"/></a>
</body>