<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib tagdir="/WEB-INF/tags/ripea" prefix="rip"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<c:set var="titol"><spring:message code="bustia.pendent.contingut.addexp.titol"/></c:set>
<html>
<head>
	<title>${titol}</title>
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
	<rip:modalHead/>
	<script type="text/javascript">
		
		$( document ).ready(function() {
		    
			$( document ).ajaxComplete(function() {
				webutilModalAdjustHeight();
			});
			
		});
	
	</script>
</head>
<body>	
	<table id="taulaExpedients" 
			data-toggle="datatable" 
			data-url="<c:url value="/bustiaUser/addexp/datatable"/>" 
			class="table table-bordered table-striped" 
			style="width:100%">
		<thead>
			<tr>
				<th data-col-name="numero" data-orderable="false"><spring:message code="expedient.list.user.columna.numero"/></th>
				<th data-col-name="nom" data-orderable="false" width="30%"><spring:message code="expedient.list.user.columna.titol"/></th>
				<th data-col-name="arxiu.nom" data-orderable="false" width="15%"><spring:message code="expedient.list.user.columna.arxiu"/></th>
				<th data-col-name="estat" data-orderable="false" data-template="#cellEstatTemplate" width="10%">
					<spring:message code="expedient.list.user.columna.estat"/>
					<script id="cellEstatTemplate" type="text/x-jsrender">
						{{if estat == 'OBERT'}}
							<span class="label label-default"><span class="fa fa-folder-open"></span> <spring:message code="expedient.estat.enum.OBERT"/></span>
						{{else}}
							<span class="label label-success"><span class="fa fa-folder"></span> <spring:message code="expedient.estat.enum.TANCAT"/></span>
						{{/if}}
					</script>
				</th>
				<th data-col-name="agafatPer.nom" data-orderable="false" width="20%"><spring:message code="expedient.list.user.columna.agafatper"/></th>
			</tr>
		</thead>
	</table>
</body>
</html>