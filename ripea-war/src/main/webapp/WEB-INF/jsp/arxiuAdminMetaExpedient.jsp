<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib tagdir="/WEB-INF/tags/ripea" prefix="rip"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%
pageContext.setAttribute(
		"principalTipus",
		es.caib.ripea.core.api.dto.PrincipalTipusEnumDto.values());
%>
<html>
<head>
	<title><spring:message code="arxiu.metaexpedient.titol"/></title>
	<meta name="subtitle" content="${arxiu.nom}"/>
	<link href="<c:url value="/css/DT_bootstrap.css"/>" rel="stylesheet">
	<script src="<c:url value="/js/jquery.dataTables.js"/>"></script>
	<script src="<c:url value="/js/DT_bootstrap.js"/>"></script>
	<script src="<c:url value="/js/jsrender.min.js"/>"></script>
	<script src="<c:url value="/js/ripea.datatable.js"/>"></script>
	<script src="<c:url value="/js/ripea.modal.js"/>"></script>
<script>
var principalTipusText = new Array();
<c:forEach var="principalTipus" items="${principalTipus}">
principalTipusText["${principalTipus}"] = "<spring:message code="principal.tipus.enum.${principalTipus}"/>";
</c:forEach>
$(document).ready(function() {
	$("#taulaDades").ripeaDataTable({
		ajaxSourceUrl: "<c:url value="/arxiuAdmin/${arxiu.id}/metaExpedient/datatable"/>",
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
				<th data-rdt-property="codi"><spring:message code="arxiu.metaexpedient.list.columna.codi"/></th>
				<th data-rdt-property="nom" data-rdt-sorting="desc"><spring:message code="arxiu.metaexpedient.list.columna.nom"/></th>
				<th data-rdt-property="actiu" data-rdt-template="cellActiuTemplate">
					<spring:message code="arxiu.metaexpedient.list.columna.actiu"/>
					<script id="cellActiuTemplate" type="text/x-jsrender">
						{{if actiu}}<span class="fa fa-check"></span>{{/if}}
					</script>
				</th>			
				<th data-rdt-property="id" data-rdt-template="cellAccionsTemplate" data-rdt-sortable="false" width="10%">
					<script id="cellAccionsTemplate" type="text/x-jsrender">
						<a class="btn btn-default" href="../../arxiuAdmin/${arxiu.id}/metaExpedient/{{:id}}/delete" data-rdt-link-ajax="true" data-rdt-link-confirm="<spring:message code="arxiu.metaexpedient.confirmacio.deslligar"/>"><span class="fa fa-trash-o"></span>&nbsp;&nbsp;<spring:message code="comu.boto.deslligar"/></a>
					</script>
				</th>
			</tr>
		</thead>
	</table>
	<script id="tableButtonsTemplate" type="text/x-jsrender">
		<p style="text-align:right"><a class="btn btn-default" href="../../arxiuAdmin/${arxiu.id}/metaExpedient/new" data-rdt-link-modal="true"><span class="fa fa-plus"></span>&nbsp;<spring:message code="arxiu.metaexpedient.boto.nova.relacio.metaexpedient"/></a></p>
	</script>
	<a href="<c:url value="/arxiuAdmin/unitat/${arxiu.unitatCodi}"/>" class="btn btn-default pull-right"><span class="fa fa-arrow-left"></span>&nbsp;<spring:message code="comu.boto.tornar"/></a>
	<div class="clearfix"></div>
	<rip:modalDefinir modalId="perform" refrescarAlertes="true" refrescarTaula="true" refrescarTaulaId="taulaDades"/>
</body>
</html>
