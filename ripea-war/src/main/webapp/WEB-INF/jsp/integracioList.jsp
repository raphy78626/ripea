<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib tagdir="/WEB-INF/tags/ripea" prefix="rip"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<html>
<head>
	<title><spring:message code="integracio.list.titol"/></title>
	<link href="<c:url value="/css/DT_bootstrap.css"/>" rel="stylesheet">
	<script src="<c:url value="/js/jquery.dataTables.js"/>"></script>
	<script src="<c:url value="/js/DT_bootstrap.js"/>"></script>
	<script src="<c:url value="/js/jsrender.min.js"/>"></script>
	<script src="<c:url value="/js/ripea.datatable.js"/>"></script>
	<script src="<c:url value="/js/ripea.modal.js"/>"></script>
<script>
$(document).ready(function() {
	$("#taulaDades").ripeaDataTable({
		ajaxSourceUrl: "<c:url value="/integracio/datatable"/>",
		localeUrl: "<c:url value="/js/dataTables-locales/dataTables_locale_ca.txt"/>",
		alertesRefreshUrl: "<c:url value="/nodeco/util/alertes"/>"
	});
});
</script>
</head>
<body>
	<ul class="nav nav-tabs" role="tablist">
		<c:forEach var="integracio" items="${integracions}">
			<li<c:if test="${integracio.codi == codiActual}"> class="active"</c:if>><a href="<c:url value="/integracio/${integracio.codi}"/>">${integracio.nom}</a></li>
		</c:forEach>
	</ul>
	<br/>
	<table id="taulaDades" class="table table-striped table-bordered">
		<thead>
			<tr>
				<th data-rdt-property="excepcioMessage" data-rdt-visible="false"></th>
				<th data-rdt-property="excepcioStacktrace" data-rdt-visible="false"></th>
				<th data-rdt-property="data" data-rdt-sortable="false" data-rdt-type="datetime"><spring:message code="integracio.list.columna.data"/></th>
				<th data-rdt-property="descripcio" data-rdt-sortable="false"><spring:message code="integracio.list.columna.descripcio"/></th>
				<th data-rdt-property="tipus" data-rdt-sortable="false"><spring:message code="integracio.list.columna.tipus"/></th>
				<th data-rdt-property="estat" data-rdt-template="cellEstatTemplate" data-rdt-sortable="false">
					<spring:message code="integracio.list.columna.estat"/>
					<script id="cellEstatTemplate" type="text/x-jsrender">
						{{if estat == 'OK'}}
							<span class="label label-success"><span class="fa fa-check"></span>&nbsp;{{:estat}}</span>
						{{else}}
							<span class="label label-danger" title="{{:excepcioMessage}}"><span class="fa fa-warning"></span>&nbsp;{{:estat}}</span>
						{{/if}}
					</script>
				</th>
				<th data-rdt-property="index" data-rdt-template="cellAccionsTemplate" data-rdt-sortable="false" width="10%">
					<script id="cellAccionsTemplate" type="text/x-jsrender">
						<a href="../integracio/${codiActual}/{{:index}}" class="btn btn-default" data-rdt-link-modal="true"><span class="fa fa-info-circle"></span>&nbsp;&nbsp;<spring:message code="contenidor.admin.boto.detalls"/></a>
					</script>
				</th>
			</tr>
		</thead>
	</table>
</body>