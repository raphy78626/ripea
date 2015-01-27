<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib tagdir="/WEB-INF/tags/ripea" prefix="rip"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%
pageContext.setAttribute(
		"metadadaTipusEnumOptions",
		es.caib.ripea.war.helper.HtmlSelectOptionHelper.getOptionsForEnum(
				es.caib.ripea.core.api.dto.MetaDadaTipusEnumDto.class,
				"metadada.tipus.enum."));
pageContext.setAttribute(
		"multiplicitatEnumOptions",
		es.caib.ripea.war.helper.HtmlSelectOptionHelper.getOptionsForEnum(
				es.caib.ripea.core.api.dto.MultiplicitatEnumDto.class,
				"multiplicitat.enum."));
%>
<html>
<head>
	<title><spring:message code="metadocument.metadada.titol"/></title>
	<meta name="subtitle" content="${metaDocument.nom}"/>
	<link href="<c:url value="/css/DT_bootstrap.css"/>" rel="stylesheet">
	<script src="<c:url value="/js/jquery.dataTables.js"/>"></script>
	<script src="<c:url value="/js/DT_bootstrap.js"/>"></script>
	<script src="<c:url value="/js/jsrender.min.js"/>"></script>
	<script src="<c:url value="/js/ripea.datatable.js"/>"></script>
	<script src="<c:url value="/js/ripea.modal.js"/>"></script>
	<script src="<c:url value="/js/jquery-ui-1.10.3.custom.min.js"/>"></script>
<script>
var metadadaTipusText = new Array();
<c:forEach var="option" items="${metadadaTipusEnumOptions}">
metadadaTipusText["${option.value}"] = "<spring:message code="${option.text}"/>";
</c:forEach>
var multiplicitatText = new Array();
<c:forEach var="option" items="${multiplicitatEnumOptions}">
multiplicitatText["${option.value}"] = "<spring:message code="${option.text}"/>";
</c:forEach>
$(document).ready(function() {
	$("#taulaDades").ripeaDataTable({
		ajaxSourceUrl: "<c:url value="/metaDocument/${metaDocument.id}/metaDada/datatable"/>",
		localeUrl: "<c:url value="/js/dataTables-locales/dataTables_locale_ca.txt"/>",
		alertesRefreshUrl: "<c:url value="/nodeco/util/alertes"/>"
	});
});
</script>
</head>
<body>
	<table id="taulaDades" class="table table-striped table-bordered" data-rdt-paginable="false" data-rdt-ordenable="true" data-rdt-button-template="tableButtonsTemplate">
		<thead>
			<tr>
				<th data-rdt-property="id" width="4%" data-rdt-visible="false">#</th>
				<th data-rdt-property="metaDada.codi"><spring:message code="metadocument.metadada.columna.codi"/></th>
				<th data-rdt-property="metaDada.nom"><spring:message code="metadocument.metadada.columna.nom"/></th>
				<th data-rdt-property="metaDada.tipus" data-rdt-template="cellMetadadaTipusTemplate">
					<spring:message code="metadocument.metadada.columna.tipus"/>
					<script id="cellMetadadaTipusTemplate" type="text/x-jsrender">
						{{:~eval('metadadaTipusText["' + metaDada_tipus + '"]')}}
					</script>
				</th>
				<th data-rdt-property="multiplicitat" data-rdt-template="cellMultiplicitatTemplate">
					<spring:message code="metadocument.metadada.columna.multiplicitat"/>
					<script id="cellMultiplicitatTemplate" type="text/x-jsrender">
						{{:~eval('multiplicitatText["' + multiplicitat + '"]')}}
					</script>
				</th>
				<th data-rdt-property="id" data-rdt-template="cellAccionsTemplate" data-rdt-sortable="false" width="10%">
					<script id="cellAccionsTemplate" type="text/x-jsrender">
						<div class="dropdown">
							<button class="btn btn-primary" data-toggle="dropdown"><span class="fa fa-cog"></span>&nbsp;<spring:message code="comu.boto.accions"/>&nbsp;<span class="caret"></span></button>
							<ul class="dropdown-menu">
								<li><a href="../../metaDocument/${metaDocument.id}/metaDada/{{:id}}" data-rdt-link-modal="true"><span class="fa fa-pencil"></span>&nbsp;&nbsp;<spring:message code="comu.boto.modificar"/></a></li>
								<li><a href="../../metaDocument/${metaDocument.id}/metaDada/{{:id}}/delete" data-rdt-link-ajax="true" data-rdt-link-confirm="<spring:message code="metadocument.metadada.confirmacio.esborrar"/>"><span class="fa fa-trash-o"></span>&nbsp;&nbsp;<spring:message code="comu.boto.esborrar"/></a></li>
							</ul>
						</div>
					</script>
				</th>
			</tr>
		</thead>
	</table>
	<script id="tableButtonsTemplate" type="text/x-jsrender">
		<p style="text-align:right"><a class="btn btn-default" href="../../metaDocument/${metaDocument.id}/metaDada/new" data-rdt-link-modal="true"><span class="fa fa-plus"></span>&nbsp;<spring:message code="metadocument.metadada.boto.afegir"/></a></p>
	</script>
	<a href="<c:url value="/metaDocument"/>" class="btn btn-default pull-right"><span class="fa fa-arrow-left"></span> <spring:message code="comu.boto.tornar"/></a>
</body>