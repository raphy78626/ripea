<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib tagdir="/WEB-INF/tags/ripea" prefix="rip"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%
pageContext.setAttribute(
		"registreTipusEnumOptions",
		es.caib.ripea.war.helper.EnumHelper.getOptionsForEnum(
				es.caib.ripea.core.api.registre.RegistreTipusEnum.class,
				"registre.anotacio.tipus.enum."));
%>
<html>
<head>
	<title><spring:message code="bustia.pendent.titol"/></title>
	<link href="<c:url value="/css/DT_bootstrap.css"/>" rel="stylesheet">
	<script src="<c:url value="/js/jquery.dataTables.js"/>"></script>
	<script src="<c:url value="/js/DT_bootstrap.js"/>"></script>
	<script src="<c:url value="/js/jsrender.min.js"/>"></script>
	<script src="<c:url value="/js/ripea.datatable.js"/>"></script>
	<script src="<c:url value="/js/ripea.modal.js"/>"></script>
<script>
var accioText = new Array();
<c:forEach var="option" items="${registreAccioEnumOptions}">
accioText["${option.value}"] = "<spring:message code="${option.text}"/>";
</c:forEach>
var tipusText = new Array();
<c:forEach var="option" items="${registreTipusEnumOptions}">
tipusText["${option.value}"] = "<spring:message code="${option.text}"/>";
</c:forEach>
$(document).ready(function() {
	$("#taulaDadesContingut").ripeaDataTable({
		ajaxSourceUrl: "<c:url value="/bustiaUser/${bustia.id}/pendent/datatable"/>",
		drawCallback: function() {
			if ($('#taulaDadesContingut tbody tr td:eq(0)').hasClass('dataTables_empty'))
				$('#contingut-count').html('0');
			else
				$('#contingut-count').html($('#taulaDadesContingut tbody tr').length);
			$.get("<c:url value="/bustiaUser/${bustia.id}/pendent/count"/>", function(data) {
				$('#bustia-pendent-count').html(data);
			});
		},
		localeUrl: "<c:url value="/js/dataTables-locales/dataTables_locale_ca.txt"/>",
		alertesRefreshUrl: "<c:url value="/nodeco/util/alertes"/>"
	});
});
</script>
</head>
<body>
	<rip:blocContenidorPath contenidor="${bustia}"/>
	<table id="taulaDadesContingut" class="table table-bordered table-striped" data-rdt-paginable="false">
		<thead>
			<tr>
				<th data-rdt-property="id" data-rdt-visible="false">#</th>
				<th data-rdt-property="tipus" data-rdt-sortable="false" width="15%"><spring:message code="bustia.pendent.columna.tipus"/></th>
				<th data-rdt-property="recepcioData" data-rdt-type="datetime" data-rdt-sortable="false" width="15%"><spring:message code="bustia.pendent.columna.recepcio.data"/></th>
				<th data-rdt-property="numero" data-rdt-sortable="false" width="15%"><spring:message code="bustia.pendent.columna.numero"/></th>
				<th data-rdt-property="descripcio" data-rdt-sortable="false" width="15%"><spring:message code="bustia.pendent.columna.contingut"/></th>
				<th data-rdt-property="remitent" data-rdt-sortable="false" width="15%"><spring:message code="bustia.pendent.columna.remitent"/></th>
				<th data-rdt-property="comentari" data-rdt-sortable="false" width="15%"><spring:message code="bustia.pendent.columna.comentari"/></th>
				<th data-rdt-property="id" data-rdt-sortable="false" data-rdt-template="cellAccionsContingutTemplate" width="10%">
					<script id="cellAccionsContingutTemplate" type="text/x-jsrender">
						<div class="dropdown">
							<button class="btn btn-primary" data-toggle="dropdown"><span class="fa fa-cog"></span>&nbsp;<spring:message code="comu.boto.accions"/>&nbsp;<span class="caret"></span></button>
							<ul class="dropdown-menu">
								{{if tipus == 'REGISTRE_ENTRADA'}}
									<li><a href="../../contenidor/${bustia.id}/registre/{{:id}}" data-rdt-link-modal="true"><span class="fa fa-info-circle"></span>&nbsp;<spring:message code="comu.boto.detalls"/></a></li>
								{{else}}
									<li><a href="../../contenidor/{{:id}}"><span class="fa fa-folder-open-o"></span>&nbsp;&nbsp;<spring:message code="comu.boto.obrir"/></a></li>
								{{/if}}
								<li><a href="../../bustiaUser/${bustia.id}/pendent/{{:tipus}}/{{:id}}/nouexp" data-rdt-link-modal="true"><span class="fa fa-plus"></span>&nbsp;&nbsp;<spring:message code="bustia.pendent.accio.nou.expedient"/>...</a></li>
								<li><a href="../../bustiaUser/${bustia.id}/pendent/{{:tipus}}/{{:id}}/addexp" data-rdt-link-modal="true"><span class="fa fa-sign-in"></span>&nbsp;&nbsp;<spring:message code="bustia.pendent.accio.afegir.expedient"/>...</a></li>
								<li><a href="../../bustiaUser/${bustia.id}/pendent/{{:tipus}}/{{:id}}/reenviar" data-rdt-link-modal="true"><span class="fa fa-envelope"></span>&nbsp;&nbsp;<spring:message code="bustia.pendent.accio.reenviar"/>...</a></li>
								<%--li><a href="../../bustiaUser/${bustia.id}/pendent/contingut/{{:id}}/agafar" data-rdt-link-ajax="true"><span class="fa fa-thumbs-o-up"></span>&nbsp;&nbsp;<spring:message code="bustia.pendent.accio.agafar"/></a></li--%>
							</ul>
						</div>
					</script>
				</th>
			</tr>
		</thead>
	</table>
	<a href="<c:url value="/bustiaUser"/>" class="btn btn-default pull-right"><span class="fa fa-arrow-left"></span>&nbsp;<spring:message code="comu.boto.tornar"/></a>
</body>
</html>