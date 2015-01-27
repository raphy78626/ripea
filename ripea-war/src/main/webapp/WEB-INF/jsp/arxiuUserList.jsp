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
	<title><spring:message code="arxiu.list.user.titol"/></title>
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
		ajaxSourceUrl: "<c:url value="/arxiuUser/unitat/${unitatCodiPerUrl}/datatable"/>",
		localeUrl: "<c:url value="/js/dataTables-locales/dataTables_locale_ca.txt"/>",
		alertesRefreshUrl: "<c:url value="/nodeco/util/alertes"/>"
	});
});
function changedCallback(e, data) {
	var baseUrl = '${unitatCodiUrlPrefix}arxiuUser/unitat/' + data.node.id;
	$('#taulaDades').dataTable().fnReloadAjax(baseUrl  + '/datatable');
	$('#taulaLlistat').css('visibility', '');
	
}
</script>
<%--script>
	function rowCallback(nRow, aData, iDisplayIndex, iDisplayIndexFull) {
		var baseUrl = '<c:if test="${not empty unitatCodi}">../../</c:if>arxiuUser';
		var rowId = aData[0];
		var numColumnes = $("td", nRow).size();
		$("td:eq(" + (numColumnes - 1) + ")", nRow).html(
				'<a href="' + baseUrl + '/' + rowId + '/expedient" class="btn btn-default"><span class="fa fa-briefcase"></span>&nbsp;<spring:message code="arxiu.list.boto.expedients"/>&nbsp;<span class="badge">' + aData[2] + '</span></a>');
	}
	function changedCallback(e, data) {
		var novaUrl = '<c:url value="/arxiuUser/unitat/"/>' + data.node.id + '/datatable';
		$('#taulaDades').dataTable().fnReloadAjax(novaUrl);
	}
</script>
	<c:choose>
		<c:when test="${not empty unitatCodi}"><c:url value="/arxiuUser/unitat/${unitatCodi}/datatable" var="dataTableAjaxSourceUrl"/></c:when>
		<c:otherwise><c:url value="/arxiuUser/unitat/null/datatable" var="dataTableAjaxSourceUrl"/></c:otherwise>
	</c:choose>
	<rip:dataTable tableId="taulaDades" paginate="false" ajaxSourceUrl="${dataTableAjaxSourceUrl}" rowCallback="rowCallback" ajaxRefrescarTaula="true" ajaxRefrescarAlertes="true"/--%>
</head>
<body>
	<div class="row">
		<div class="col-md-4">
			<rip:arbre id="arbreUnitats" arbre="${arbreUnitatsOrganitzatives}" atributId="codi" atributNom="denominacio" changedCallback="changedCallback" seleccionatId="${unitatCodi}"/>
		</div>
		<div id="taulaLlistat" class="col-md-8"<c:if test="${empty unitatCodi}"> style="visibility:hidden"</c:if>>
			<table id="taulaDades" class="table table-bordered table-striped" data-rdt-paginable="false">
				<thead>
					<tr>
						<th data-rdt-property="id" width="4%" data-rdt-visible="false">#</th>
						<th data-rdt-property="nom" data-rdt-sorting="desc"><spring:message code="bustia.list.columna.nom"/></th>
						<th data-rdt-property="expedientsCount" data-rdt-template="cellExpedientsCountTemplate" data-rdt-sortable="false" width="10%">
							<script id="cellExpedientsCountTemplate" type="text/x-jsrender">
								<a href="${unitatCodiUrlPrefix}arxiuUser/{{:id}}/expedient" class="btn btn-default"><span class="fa fa-briefcase"></span>&nbsp;<spring:message code="arxiu.list.boto.expedients"/>&nbsp;<span class="badge">{{:expedientsCount}}</span></a>
							</script>
						</th>
					</tr>
				</thead>
			</table>
		</div>
	</div>
</body>