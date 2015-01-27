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
	<title><spring:message code="metaexpedient.permis.titol"/></title>
	<meta name="subtitle" content="${metaExpedient.nom}"/>
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
		ajaxSourceUrl: "<c:url value="/metaExpedient/${metaExpedient.id}/permis/datatable"/>",
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
				<th data-rdt-property="principalTipus" data-rdt-template="cellPrincipalTipusTemplate">
					<spring:message code="metaexpedient.permis.columna.tipus"/>
					<script id="cellPrincipalTipusTemplate" type="text/x-jsrender">
						{{:~eval('principalTipusText["' + principalTipus + '"]')}}
					</script>
				</th>
				<th data-rdt-property="principalNom" data-rdt-sorting="desc"><spring:message code="metaexpedient.permis.columna.principal"/></th>
				<th data-rdt-property="create" data-rdt-template="cellPermisCreateTemplate">
					<spring:message code="metaexpedient.permis.columna.creacio"/>
					<script id="cellPermisCreateTemplate" type="text/x-jsrender">
						{{if create}}<span class="fa fa-check"></span>{{/if}}
					</script>
				</th>
				<th data-rdt-property="read" data-rdt-template="cellPermisReadTemplate">
					<spring:message code="metaexpedient.permis.columna.consulta"/>
					<script id="cellPermisReadTemplate" type="text/x-jsrender">
						{{if read}}<span class="fa fa-check"></span>{{/if}}
					</script>
				</th>
				<th data-rdt-property="write" data-rdt-template="cellPermisWriteTemplate">
					<spring:message code="metaexpedient.permis.columna.modificacio"/>
					<script id="cellPermisWriteTemplate" type="text/x-jsrender">
						{{if write}}<span class="fa fa-check"></span>{{/if}}
					</script>
				</th>
				<th data-rdt-property="delete" data-rdt-template="cellPermisDeleteTemplate">
					<spring:message code="metaexpedient.permis.columna.eliminacio"/>
					<script id="cellPermisDeleteTemplate" type="text/x-jsrender">
						{{if delete}}<span class="fa fa-check"></span>{{/if}}
					</script>
				</th>
				<th data-rdt-property="id" data-rdt-template="cellAccionsTemplate" data-rdt-sortable="false" width="10%">
					<script id="cellAccionsTemplate" type="text/x-jsrender">
						<div class="dropdown">
							<button class="btn btn-primary" data-toggle="dropdown"><span class="fa fa-cog"></span>&nbsp;<spring:message code="comu.boto.accions"/>&nbsp;<span class="caret"></span></button>
							<ul class="dropdown-menu">
								<li><a href="../../metaExpedient/${metaExpedient.id}/permis/{{:id}}" data-rdt-link-modal="true"><span class="fa fa-pencil"></span>&nbsp;&nbsp;<spring:message code="comu.boto.modificar"/></a></li>
								<li><a href="../../metaExpedient/${metaExpedient.id}/permis/{{:id}}/delete" data-rdt-link-ajax="true" data-rdt-link-confirm="<spring:message code="metaexpedient.permis.confirmacio.esborrar"/>"><span class="fa fa-trash-o"></span>&nbsp;&nbsp;<spring:message code="comu.boto.esborrar"/></a></li>
							</ul>
						</div>
					</script>
				</th>
			</tr>
		</thead>
	</table>
	<script id="tableButtonsTemplate" type="text/x-jsrender">
		<p style="text-align:right"><a class="btn btn-default" href="../../metaExpedient/${metaExpedient.id}/permis/new" data-rdt-link-modal="true"><span class="fa fa-plus"></span>&nbsp;<spring:message code="metaexpedient.permis.boto.nou.permis"/></a></p>
	</script>
	<a href="<c:url value="/metaExpedient"/>" class="btn btn-default pull-right"><span class="fa fa-arrow-left"></span>&nbsp;<spring:message code="comu.boto.tornar"/></a>
	<div class="clearfix"></div>
</body>
</html>
