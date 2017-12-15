<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib tagdir="/WEB-INF/tags/ripea" prefix="rip"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<html>
<head>
	<title><spring:message code="permis.list.titol"/></title>
	<script src="<c:url value="/webjars/datatables.net/1.10.11/js/jquery.dataTables.min.js"/>"></script>
	<script src="<c:url value="/webjars/datatables.net-bs/1.10.11/js/dataTables.bootstrap.min.js"/>"></script>
	<link href="<c:url value="/webjars/datatables.net-bs/1.10.11/css/dataTables.bootstrap.min.css"/>" rel="stylesheet"></link>
	<script src="<c:url value="/webjars/jsrender/1.0.0-rc.70/jsrender.min.js"/>"></script>
	<script src="<c:url value="/js/webutil.common.js"/>"></script>
	<script src="<c:url value="/js/webutil.datatable.js"/>"></script>
	<script src="<c:url value="/js/webutil.modal.js"/>"></script>
</head>
<body>
	<div class="text-right" data-toggle="botons-titol">
		<a class="btn btn-default" href="permis/new" data-toggle="modal" data-datatable-id="permisos"><span class="fa fa-plus"></span>&nbsp;<spring:message code="permis.list.boto.nou.permis"/></a>
	</div>
	<table id="permisos" data-toggle="datatable" data-url="<c:url value="/permis/datatable"/>" data-search-enabled="false" data-paging-enabled="false" data-default-order="1" data-default-dir="asc" class="table table-striped table-bordered">
		<thead>
			<tr>
				<th data-col-name="principalTipus" data-renderer="enum(PrincipalTipusEnumDto)"><spring:message code="permis.list.columna.tipus"/></th>
				<th data-col-name="principalNom"><spring:message code="entitat.permis.columna.principal"/></th>
				<th data-col-name="administration" data-template="#cellAdministrationTemplate">
					<spring:message code="permis.list.columna.administracio"/>
					<script id="cellAdministrationTemplate" type="text/x-jsrender">
						{{if administration}}<span class="fa fa-check"></span>{{/if}}
					</script>
				</th>
				<th data-col-name="read" data-template="#cellReadTemplate">
					<spring:message code="permis.list.columna.usuari"/>
					<script id="cellReadTemplate" type="text/x-jsrender">
						{{if read}}<span class="fa fa-check"></span>{{/if}}
					</script>
				</th>
				<th data-col-name="id" data-template="#cellAccionsTemplate" data-orderable="false" width="10%">
					<script id="cellAccionsTemplate" type="text/x-jsrender">
						<div class="dropdown">
							<button class="btn btn-primary" data-toggle="dropdown"><span class="fa fa-cog"></span>&nbsp;<spring:message code="comu.boto.accions"/>&nbsp;<span class="caret"></span></button>
							<ul class="dropdown-menu">
								<li><a href="permis/{{:id}}" data-toggle="modal"><span class="fa fa-pencil"></span>&nbsp;&nbsp;<spring:message code="comu.boto.modificar"/></a></li>
								<li><a href="permis/{{:id}}/delete" data-toggle="ajax" data-confirm="<spring:message code="entitat.permis.confirmacio.esborrar"/>"><span class="fa fa-trash-o"></span>&nbsp;&nbsp;<spring:message code="comu.boto.esborrar"/></a></li>
							</ul>
						</div>
					</script>
				</th>
			</tr>
		</thead>
	</table>
</body>
</html>
