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
	<script src="<c:url value="/webjars/datatables.net/1.10.11/js/jquery.dataTables.min.js"/>"></script>
	<script src="<c:url value="/webjars/datatables.net-bs/1.10.11/js/dataTables.bootstrap.min.js"/>"></script>
	<link href="<c:url value="/webjars/datatables.net-bs/1.10.11/css/dataTables.bootstrap.min.css"/>" rel="stylesheet"></link>
	<script src="<c:url value="/webjars/jsrender/1.0.0-rc.70/jsrender.min.js"/>"></script>
	<script src="<c:url value="/js/webutil.common.js"/>"></script>
	<script src="<c:url value="/js/webutil.datatable.js"/>"></script>
	<script src="<c:url value="/js/webutil.modal.js"/>"></script>
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
	/*$("#taulaDadesContingut").ripeaDataTable({
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
	});*/
});
</script>
</head>
<body>
	<rip:blocContenidorPath contenidor="${bustia}"/>
	<table id="taulaDadesContingut" data-toggle="datatable" data-url="<c:url value="/bustiaUser/${bustia.id}/pendent/datatable"/>" class="table table-bordered table-striped">
		<thead>
			<tr>
				<th data-col-name="id" data-visible="false"></th>
				<th data-col-name="error" data-visible="false"></th>
				<th data-col-name="tipus" data-visible="false"><spring:message code="bustia.pendent.columna.tipus"/></th>
				<th data-col-name="recepcioData" data-converter="datetime" data-orderable="false" width="15%"><spring:message code="bustia.pendent.columna.recepcio.data"/></th>
				<th data-col-name="descripcio" data-template="#contingutTemplate" data-orderable="false" width="30%">
					<spring:message code="bustia.pendent.columna.descripcio"/>
					<script id="contingutTemplate" type="text/x-jsrender">
						{{if tipus == 'REGISTRE'}}<span class="fa fa-book" title="<spring:message code="bustia.pendent.tipus.enum.REGISTRE"/>"></span>{{else tipus == 'EXPEDIENT'}}<span class="fa fa-briefcase" title="<spring:message code="bustia.pendent.tipus.enum.EXPEDIENT"/>"></span>{{else tipus == 'DOCUMENT'}}<span class="fa fa-file" title="<spring:message code="bustia.pendent.tipus.enum.DOCUMENT"/>"></span>{{/if}}
						{{:descripcio}}
						{{if error}}<span class="fa fa-warning text-danger pull-right" title="<spring:message code="bustia.pendent.registre.estat.error"/>"></span>{{/if}}
					</script>
				</th>
				<th data-col-name="numero" data-orderable="false" width="15%"><spring:message code="bustia.pendent.columna.numero"/></th>
				<th data-col-name="remitent" data-orderable="false" width="15%"><spring:message code="bustia.pendent.columna.remitent"/></th>
				<th data-col-name="comentari" data-orderable="false" width="15%"><spring:message code="bustia.pendent.columna.comentari"/></th>
				<th data-col-name="id" data-orderable="false" data-template="#cellAccionsContingutTemplate" width="10%">
					<script id="cellAccionsContingutTemplate" type="text/x-jsrender">
						<div class="dropdown">
							<button class="btn btn-primary" data-toggle="dropdown"><span class="fa fa-cog"></span>&nbsp;<spring:message code="comu.boto.accions"/>&nbsp;<span class="caret"></span></button>
							<ul class="dropdown-menu">
								{{if tipus == 'REGISTRE'}}
									<li><a href="../../contenidor/${bustia.id}/registre/{{:id}}" data-toggle="modal"><span class="fa fa-info-circle"></span>&nbsp;<spring:message code="comu.boto.detalls"/></a></li>
								{{else}}
									<li><a href="../../contenidor/{{:id}}"><span class="fa fa-folder-open-o"></span>&nbsp;&nbsp;<spring:message code="comu.boto.obrir"/></a></li>
								{{/if}}
								<li><a href="../../contenidor/{{:id}}/log" data-toggle="modal"><span class="fa fa-list"></span>&nbsp;<spring:message code="comu.boto.historial"/></a></li>
								<li><a href="../../bustiaUser/${bustia.id}/pendent/{{:tipus}}/{{:id}}/nouexp" data-toggle="modal"><span class="fa fa-plus"></span>&nbsp;&nbsp;<spring:message code="bustia.pendent.accio.nou.expedient"/>...</a></li>
								<li><a href="../../bustiaUser/${bustia.id}/pendent/{{:tipus}}/{{:id}}/addexp" data-toggle="modal"><span class="fa fa-sign-in"></span>&nbsp;&nbsp;<spring:message code="bustia.pendent.accio.afegir.expedient"/>...</a></li>
								<li><a href="../../bustiaUser/${bustia.id}/pendent/{{:tipus}}/{{:id}}/reenviar" data-toggle="modal"><span class="fa fa-envelope"></span>&nbsp;&nbsp;<spring:message code="bustia.pendent.accio.reenviar"/>...</a></li>
								<%--li><a href="../../bustiaUser/${bustia.id}/pendent/contingut/{{:id}}/agafar" data-toggle="modal"><span class="fa fa-thumbs-o-up"></span>&nbsp;&nbsp;<spring:message code="bustia.pendent.accio.agafar"/></a></li--%>
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