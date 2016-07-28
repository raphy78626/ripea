<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib tagdir="/WEB-INF/tags/ripea" prefix="rip"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<c:set var="idioma"><%=org.springframework.web.servlet.support.RequestContextUtils.getLocale(request).getLanguage()%></c:set>
<html>
<head>
	<title><spring:message code="bustia.user.list.titol"/></title>
	<script src="<c:url value="/webjars/datatables.net/1.10.11/js/jquery.dataTables.min.js"/>"></script>
	<script src="<c:url value="/webjars/datatables.net-bs/1.10.11/js/dataTables.bootstrap.min.js"/>"></script>
	<link href="<c:url value="/webjars/datatables.net-bs/1.10.11/css/dataTables.bootstrap.min.css"/>" rel="stylesheet"></link>
	<script src="<c:url value="/webjars/jsrender/1.0.0-rc.70/jsrender.min.js"/>"></script>
	<script src="<c:url value="/js/webutil.common.js"/>"></script>
	<script src="<c:url value="/js/webutil.datatable.js"/>"></script>
	<script src="<c:url value="/js/webutil.modal.js"/>"></script>
<script>
$(document).ready(function() {
});
</script>
</head>
<body>
	<table id="taulaDades" 
			data-toggle="datatable" 
			data-url="<c:url value="/bustiaUser/datatable"/>" 
			class="table table-bordered table-striped" 
			data-info-type="search" 
			data-default-order="1" 
			data-default-dir="asc" 
			style="width:100%">
		<thead>
			<tr>
				<th data-col-name="id" data-visible="false">Id</th>
				<th data-col-name="nom"><spring:message code="bustia.list.columna.nom"/></th>
				<th data-col-name="pareNom"><spring:message code="bustia.list.columna.unitat"/></th>
				<th data-col-name="pendentCount" data-template="#cellFillsCountTemplate" data-orderable="false" width="10%">
					<script id="cellFillsCountTemplate" type="text/x-jsrender">
						<a href="${unitatCodiUrlPrefix}bustiaUser/{{:id}}/pendent" class="btn btn-default"><span class="fa fa-briefcase"></span>&nbsp;<spring:message code="bustia.user.list.pendent"/>&nbsp;<span class="badge">{{:pendentCount}}</span></a>
					</script>
				</th>
			</tr>
		</thead>
	</table>
</body>
</html>