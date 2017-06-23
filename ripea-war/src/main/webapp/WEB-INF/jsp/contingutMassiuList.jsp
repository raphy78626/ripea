<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib tagdir="/WEB-INF/tags/ripea" prefix="rip"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<c:set var="idioma"><%=org.springframework.web.servlet.support.RequestContextUtils.getLocale(request).getLanguage()%></c:set>
<rip:blocIconaContingutNoms/>
<html>
<head>
	<title>${titolMassiu}</title>
	<script src="<c:url value="/webjars/datatables.net/1.10.11/js/jquery.dataTables.min.js"/>"></script>
	<script src="<c:url value="/webjars/datatables.net-bs/1.10.11/js/dataTables.bootstrap.min.js"/>"></script>
	<link href="<c:url value="/webjars/datatables.net-bs/1.10.11/css/dataTables.bootstrap.min.css"/>" rel="stylesheet"></link>
	<link href="<c:url value="/webjars/datatables.net-bs/1.10.11/css/dataTables.bootstrap.min.css"/>" rel="stylesheet"></link>
	<script src="<c:url value="/webjars/datatables.net-select/1.1.2/js/dataTables.select.min.js"/>"></script>
	<link href="<c:url value="/webjars/datatables.net-select-bs/1.1.2/css/select.bootstrap.min.css"/>" rel="stylesheet"></link>
	<link href="<c:url value="/webjars/select2/4.0.1/dist/css/select2.min.css"/>" rel="stylesheet"/>
	<link href="<c:url value="/webjars/select2-bootstrap-theme/0.1.0-beta.4/dist/select2-bootstrap.min.css"/>" rel="stylesheet"/>
	<script src="<c:url value="/webjars/select2/4.0.1/dist/js/select2.min.js"/>"></script>
	<script src="<c:url value="/webjars/select2/4.0.1/dist/js/i18n/${requestLocale}.js"/>"></script>
	<link href="<c:url value="/webjars/bootstrap-datepicker/1.6.1/dist/css/bootstrap-datepicker.min.css"/>" rel="stylesheet"/>
	<script src="<c:url value="/webjars/bootstrap-datepicker/1.6.1/dist/js/bootstrap-datepicker.min.js"/>"></script>
	<script src="<c:url value="/webjars/bootstrap-datepicker/1.6.1/dist/locales/bootstrap-datepicker.${requestLocale}.min.js"/>"></script>
	<script src="<c:url value="/webjars/jsrender/1.0.0-rc.70/jsrender.min.js"/>"></script>
	<script src="<c:url value="/js/webutil.common.js"/>"></script>
	<script src="<c:url value="/js/webutil.datatable.js"/>"></script>
	<script src="<c:url value="/js/webutil.modal.js"/>"></script>
	
<style>
	table.dataTable tbody > tr.selected, table.dataTable tbody > tr > .selected {
		background-color: #fcf8e3;
		color: #666666;
	}
	table.dataTable thead > tr.selectable > :first-child, table.dataTable tbody > tr.selectable > :first-child {
		cursor: pointer;
	}
</style>
<script>
	$(document).ready(function() {
		$('#taulaDades').on('selectionchange.dataTable', function (e, accio, ids) {
			$.get(
					accio,
					{ids: ids},
					function(data) {
						$("#seleccioCount").html(data);
					}
			);
		});
		$('#taulaDades').on('draw.dt', function () {
			$('#seleccioAll').on('click', function() {
				$.get(
						"select",
						function(data) {
							$("#seleccioCount").html(data);
							$('#taulaDades').webutilDatatable('refresh');
						}
				);
				return false;
			});
			$('#seleccioNone').on('click', function() {
				$.get(
						"deselect",
						function(data) {
							$("#seleccioCount").html(data);
							$('#taulaDades').webutilDatatable('select-none');
							$('#taulaDades').webutilDatatable('refresh');
						}
				);
				return false;
			});
		});
	});
</script>

</head>
<body>
	
	<form:form action="" method="post" cssClass="well" commandName="contingutMassiuFiltreCommand">
		<div class="row">
			<div class="col-md-4">
				<rip:inputSelect name="tipusElement"  optionEnum="ContingutTipusEnumDto" placeholderKey="execucions.massives.list.filtre.tipuselement" emptyOption="true" inline="true" disabled="${contingutMassiuFiltreCommand.bloquejarTipusElement}" netejar="${not contingutMassiuFiltreCommand.bloquejarTipusElement}"/>
			</div>
			<div class="col-md-4">
				<rip:inputSelect name="tipusExpedient" optionItems="${metaExpedients}" optionValueAttribute="id" optionTextAttribute="nom" optionMinimumResultsForSearch="3" emptyOption="true" placeholderKey="execucions.massives.list.filtre.tipusexpedient" inline="true" disabled="${contingutMassiuFiltreCommand.bloquejarMetaExpedient}"/>
			</div>
			<div class="col-md-4">
				<rip:inputSelect name="tipusDocument" optionItems="${metaDocuments}" optionValueAttribute="id" optionTextAttribute="nom" optionMinimumResultsForSearch="3" emptyOption="true" placeholderKey="execucions.massives.list.filtre.tipusdocument" inline="true" disabled="${contingutMassiuFiltreCommand.bloquejarMetaDocument}"/>
			</div>
		</div>
		<div class="row">
			<div class="col-md-4">
				<rip:inputText name="nom" inline="true" placeholderKey="execucions.massives.list.filtre.nom"/>
			</div>
<!-- 			<div class="col-md-4"> -->
<%-- 				<rip:inputSelect name="metaDada" optionItems="${metaDades}" optionValueAttribute="id" optionTextAttribute="nom" optionMinimumResultsForSearch="3" emptyOption="true" placeholderKey="execucions.massives.list.filtre.metadada" inline="true" disabled="${not empty contingutMassiuFiltreCommand.bloquejarTipusElement}"/> --%>
<!-- 			</div> -->
			<div class="col-md-2">
				<rip:inputDate name="dataInici" inline="true" placeholderKey="execucions.massives.list.filtre.datainici"/>
			</div>
			<div class="col-md-2">
				<rip:inputDate name="dataFi" inline="true" placeholderKey="execucions.massives.list.filtre.datafi"/>
			</div>
			<div class="col-md-4 pull-right">
				<div class="pull-right">
					<button type="submit" name="accio" value="netejar" class="btn btn-default"><spring:message code="comu.boto.netejar"/></button>
					<button type="submit" name="accio" value="filtrar" class="btn btn-primary"><span class="fa fa-filter"></span> <spring:message code="comu.boto.filtrar"/></button>
				</div>
			</div>
		</div>
	</form:form>

	<script id="botonsTemplate" type="text/x-jsrender">
		<div class="btn-group pull-right">
			<button type="button" id="seleccioAll" title="<spring:message code="expedient.list.user.seleccio.tots"/>" class="btn btn-default"><span class="fa fa-check-square-o"></span></a>
			<button type="button" id="seleccioNone" title="<spring:message code="expedient.list.user.seleccio.cap"/>" class="btn btn-default"><span class="fa fa-square-o"></span></a>
			<button type="button" class="btn btn-default" href="./crear/portafirmes" data-toggle="modal" data-refresh-pagina="true">
				<span id="seleccioCount" class="badge">${fn:length(seleccio)}</span> ${botoMassiu}
			</button>
		</div>
	</script>
		
	<table id="taulaDades" 
		data-toggle="datatable" 
		data-url="<c:url value="/massiu/datatable"/>"
		data-filter="#contingutMassiuFiltreCommand"
		class="table table-bordered table-striped" 
		data-default-order="3" 
		data-default-dir="desc"
		data-botons-template="#botonsTemplate"
		data-selection-enabled="true"
		style="width:100%">
		<thead>
			<tr>
				<th data-col-name="id" data-visible="false"></th>
				<th data-col-name="tipus" data-orderable="true" width="15%" data-template="#cellTipusTemplate">
					<spring:message code="execucions.massives.list.column.tipuselement"/>
					<script id="cellTipusTemplate" type="text/x-jsrender">
						{{if tipus == 'DOCUMENT'}}
							<span class="fa fa-file-text-o"></span> <spring:message code="contingut.tipus.enum.DOCUMENT"/>
						{{/if}}
					</script>
				</th>
				<th data-col-name="path" data-template="#cellPathTemplate" data-orderable="false">
					<spring:message code="execucions.massives.list.column.ubicacio"/>
					<script id="cellPathTemplate" type="text/x-jsrender">
						{{for path}}/
							{{if escriptori}}<span class="fa ${iconaEscriptori}" title="<spring:message code="contingut.path.escriptori"/>"></span>
							{{else expedient}}<span class="fa ${iconaExpedient}" title="<spring:message code="contingut.icona.expedient"/>"></span>
							{{else carpeta}}<span class="fa ${iconaCarpeta}" title="<spring:message code="contingut.icona.carpeta"/>"></span>
							{{else document}}<span class="fa ${iconaDocument}" title="<spring:message code="contingut.icona.document"/>"></span>
							{{else arxiv}}{{if #getIndex() == 0}}<span class="fa ${iconaUnitat}" title="<spring:message code="contingut.icona.unitat"/>"></span>{{else}}<span class="fa ${iconaArxiu}" title="<spring:message code="contingut.icona.arxiu"/>"></span>{{/if}}
							{{else bustia}}{{if #getIndex() == 0}}<span class="fa ${iconaUnitat}" title="<spring:message code="contingut.icona.unitat"/>"></span>{{else}}<span class="fa ${iconaBustia}" title="<spring:message code="contingut.icona.bustia"/>"></span>{{/if}}{{/if}}
							{{if escriptori}}{{:createdBy.nom}}{{else}}{{:nom}}{{/if}}
						{{/for}}
					</script>
				</th>
				<th data-col-name="nom" data-ordenable="true"><spring:message code="execucions.massives.list.column.nom"/></th>
				<th data-col-name="createdDate" data-ordenable="true" data-converter="datetime" width="15%"><spring:message code="execucions.massives.list.column.datacreacio"/></th>
				<th data-col-name="nomPropietariEscriptoriPare" data-orderable="false" width="20%"><spring:message code="expedient.list.user.columna.agafatper"/></th>
			</tr>
		</thead>
	</table>
	
</body>
</html>