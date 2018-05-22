<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib tagdir="/WEB-INF/tags/ripea" prefix="rip"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<rip:blocIconaContingutNoms/>
<html>
<head>
	<title><spring:message code="unitat.list.titol"/></title>
	<script src="<c:url value="/webjars/datatables.net/1.10.11/js/jquery.dataTables.min.js"/>"></script>
	<script src="<c:url value="/webjars/datatables.net-bs/1.10.11/js/dataTables.bootstrap.min.js"/>"></script>
	<link href="<c:url value="/webjars/datatables.net-bs/1.10.11/css/dataTables.bootstrap.min.css"/>" rel="stylesheet"></link>
	<link href="<c:url value="/webjars/select2/4.0.6-rc.1/dist/css/select2.min.css"/>" rel="stylesheet"/>
	<link href="<c:url value="/webjars/select2-bootstrap-theme/0.1.0-beta.4/dist/select2-bootstrap.min.css"/>" rel="stylesheet"/>
	<script src="<c:url value="/webjars/select2/4.0.6-rc.1/dist/js/select2.min.js"/>"></script>
	<script src="<c:url value="/webjars/select2/4.0.6-rc.1/dist/js/i18n/${requestLocale}.js"/>"></script>
	<script src="<c:url value="/webjars/jsrender/1.0.0-rc.70/jsrender.min.js"/>"></script>
	<script src="<c:url value="/js/webutil.common.js"/>"></script>
	<script src="<c:url value="/js/webutil.datatable.js"/>"></script>
	<script src="<c:url value="/js/webutil.modal.js"/>"></script>
</head>
<body>
	<div class="text-right" data-toggle="botons-titol">
		<a href="<c:url value="/unitatOrganitzativa/synchronize"/>" class="btn btn-default"><span class="fa fa-refresh"></span> <spring:message code="unitat.list.boto.synchronize"/></a>
	</div>

		<form:form action="" method="post" cssClass="well" commandName="unitatOrganitzativaFiltreCommand">
			<div class="row">
				<div class="col-md-4">
					<rip:inputText name="codi" inline="true" placeholderKey="unitat.list.filtre.codi"/>
				</div>
				<div class="col-md-4">
					<rip:inputText name="denominacio" inline="true" placeholderKey="unitat.list.filtre.denominacio"/>
				</div>
				<div class="col-md-4 pull-right">
					<div class="pull-right">
						<button type="submit" name="accio" value="netejar" class="btn btn-default"><spring:message code="comu.boto.netejar"/></button>
						<button type="submit" name="accio" value="filtrar" class="btn btn-primary"><span class="fa fa-filter"></span> <spring:message code="comu.boto.filtrar"/></button>
					</div>
				</div>
			</div>
		</form:form>

		<table id="unitatsOrganitzatives" data-toggle="datatable" data-url="<c:url value="/unitatOrganitzativa/datatable"/>" data-filter="#unitatOrganitzativaFiltreCommand" data-default-order="1" data-default-dir="asc" class="table table-bordered table-striped">
		<thead>
			<tr>
				<th data-col-name="id" data-visible="false">#</th>
				
				<th data-col-name="codi"><spring:message code="unitat.list.columna.codi"/></th>
				
				<th data-col-name="denominacio"><spring:message code="unitat.list.columna.denominacio"/></th>
				
				<th data-col-name="nifCif"><spring:message code="unitat.list.columna.nifCif"/></th>
				
				<th data-col-name="codiUnitatSuperior"><spring:message code="unitat.list.columna.codiUnitatSuperior"/></th>
				
				<th data-col-name="codiUnitatArrel"><spring:message code="unitat.list.columna.codiUnitatArrel"/></th>
				
				<th data-col-name="obsoleta"><spring:message code="unitat.list.columna.obolseta"/></th>
			</tr>
		</thead>
	</table>
	
</body>