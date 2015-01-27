<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib tagdir="/WEB-INF/tags/ripea" prefix="rip"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<c:set var="idioma"><%=org.springframework.web.servlet.support.RequestContextUtils.getLocale(request).getLanguage()%></c:set>
<html>
<head>
	<title><spring:message code="arxiu.expedient.list.titol"/></title>
	<meta name="subtitle" content="${arxiu.nom}"/>
	<link href="<c:url value="/css/DT_bootstrap.css"/>" rel="stylesheet">
	<script src="<c:url value="/js/jquery.dataTables.js"/>"></script>
	<script src="<c:url value="/js/DT_bootstrap.js"/>"></script>
	<script src="<c:url value="/js/jsrender.min.js"/>"></script>
	<script src="<c:url value="/js/ripea.datatable.js"/>"></script>
	<script src="<c:url value="/js/ripea.modal.js"/>"></script>
	<link href="<c:url value="/css/datepicker.css"/>" rel="stylesheet"/>
	<script src="<c:url value="/js/bootstrap-datepicker.js"/>"></script>
	<script src="<c:url value="/js/datepicker-locales/bootstrap-datepicker.${idioma}.js"/>"></script>
	<link href="<c:url value="/css/select2.css"/>" rel="stylesheet"/>
	<link href="<c:url value="/css/select2-bootstrap.css"/>" rel="stylesheet"/>
	<script src="<c:url value="/js/select2.min.js"/>"></script>
	<script src="<c:url value="/js/select2-locales/select2_locale_${idioma}.js"/>"></script>
<script>
$(document).ready(function() {
	$("#taulaDades").ripeaDataTable({
		ajaxSourceUrl: "<c:url value="/arxiuAdmin/${arxiu.id}/expedient/datatable"/>",
		localeUrl: "<c:url value="/js/dataTables-locales/dataTables_locale_ca.txt"/>",
		alertesRefreshUrl: "<c:url value="/nodeco/util/alertes"/>"
	});
});
</script>
</head>
<body>
	<form:form action="" method="post" cssClass="form-inline well" commandName="expedientFiltreCommand">
		<rip:inputText name="nom" inline="true" placeholderKey="arxiu.expedient.list.placeholder.nom"/>
		<rip:inputSelect name="metaExpedientId" optionItems="${metaExpedients}" optionValueAttribute="id" optionTextAttribute="nom" emptyOption="true" placeholderKey="arxiu.expedient.list.placeholder.metaexp" inline="true"/>
		<rip:inputDate name="dataCreacioInici" inline="true" placeholderKey="arxiu.expedient.list.placeholder.creacio.inici"/>
		<rip:inputDate name="dataCreacioFi" inline="true" placeholderKey="arxiu.expedient.list.placeholder.creacio.fi"/>
		<div class="pull-right">
			<button type="submit" name="accio" value="netejar" class="btn btn-default"><spring:message code="comu.boto.netejar"/></button>
			<button type="submit" name="accio" value="filtrar" class="btn btn-primary"><span class="fa fa-filter"></span> <spring:message code="comu.boto.filtrar"/></button>
		</div>
	</form:form>
	<table id="taulaDades" class="table table-bordered table-striped" data-rdt-filtre-form-id="expedientFiltreCommand">
		<thead>
			<tr>
				<th data-rdt-property="id" data-rdt-visible="false">#</th>
				<th data-rdt-property="codiPropietariEscriptoriPare" data-rdt-visible="false"></th>
				<th data-rdt-property="nom" width="35%"><spring:message code="arxiu.expedient.list.columna.nom"/></th>
				<th data-rdt-property="metaNode.nom" width="20%"><spring:message code="arxiu.expedient.list.columna.tipus"/></th>
				<th data-rdt-property="createdDate" data-rdt-type="datetime" data-rdt-sorting="desc" width="15%"><spring:message code="arxiu.expedient.list.columna.createl"/></th>
				<th data-rdt-property="nomPropietariEscriptoriPare" data-sortable="false" width="20%"><spring:message code="arxiu.expedient.list.columna.agafatper"/></th>
				<th data-rdt-property="id" data-rdt-template="cellAccionsTemplate" data-rdt-sortable="false" width="10%">
					<script id="cellAccionsTemplate" type="text/x-jsrender">
						{{if obert}}
							<div class="dropdown">
								<button class="btn btn-primary" data-toggle="dropdown"><span class="fa fa-cog"></span>&nbsp;<spring:message code="comu.boto.accions"/>&nbsp;<span class="caret"></span></button>
								<ul class="dropdown-menu">
									{{if !codiPropietariEscriptoriPare}}
									<li><a href="../../arxiuAdmin/${arxiu.id}/expedient/{{:id}}/agafar" data-rdt-link-modal="true"><span class="fa fa-lock"></span>&nbsp;&nbsp;<spring:message code="comu.boto.agafar"/></a></li>
									{{else}}
									<li><a href="../../arxiuAdmin/${arxiu.id}/expedient/{{:id}}/alliberar" data-rdt-link-ajax="true"><span class="fa fa-unlock"></span>&nbsp;&nbsp;<spring:message code="comu.boto.alliberar"/></a></li>
									{{/if}}
								</ul>
							</div>
						{{/if}}
					</script>
				</th>
			</tr>
		</thead>
	</table>
	<a href="<c:url value="/arxiuAdmin/unitat/${arxiu.unitatCodi}"/>" class="btn btn-default pull-right"><span class="fa fa-arrow-left"></span>&nbsp;<spring:message code="comu.boto.tornar"/></a>
</body>