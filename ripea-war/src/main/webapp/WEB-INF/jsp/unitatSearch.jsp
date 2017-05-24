<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib tagdir="/WEB-INF/tags/ripea" prefix="rip"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

	<c:set var="titol"><spring:message code="unitat.search.titol"/></c:set>

<html>
<head>
	<title>${titol}</title>
	<script src="<c:url value="/webjars/datatables.net/1.10.11/js/jquery.dataTables.min.js"/>"></script>
	<script src="<c:url value="/webjars/datatables.net-bs/1.10.11/js/dataTables.bootstrap.min.js"/>"></script>
	<link href="<c:url value="/webjars/datatables.net-bs/1.10.11/css/dataTables.bootstrap.min.css"/>" rel="stylesheet"></link>
	<link href="<c:url value="/webjars/select2/4.0.1/dist/css/select2.min.css"/>" rel="stylesheet"/>
	<link href="<c:url value="/webjars/select2-bootstrap-theme/0.1.0-beta.4/dist/select2-bootstrap.min.css"/>" rel="stylesheet"/>
	<script src="<c:url value="/webjars/select2/4.0.1/dist/js/select2.full.min.js"/>"></script>
	<script src="<c:url value="/webjars/select2/4.0.1/dist/js/i18n/${requestLocale}.js"/>"></script>
	<script src="<c:url value="/webjars/jsrender/1.0.0-rc.70/jsrender.min.js"/>"></script>
	<script src="<c:url value="/js/webutil.common.js"/>"></script>
	<script src="<c:url value="/js/webutil.datatable.js"/>"></script>
	<script src="<c:url value="/js/webutil.modal.js"/>"></script>
	
	<rip:modalHead/>
</head>
<body>
	<c:set var="formAction"><rip:modalUrl value="/unitatajax/unitats"/></c:set>
	<form:form action="${formAction}" method="post" cssClass="form-horizontal" commandName="unitatsFiltreCommand" role="form">
		
		<div class="col-xs-6">
			<rip:inputText name="denominacio" textKey="unitat.search.filtre.denominacio"/>
		</div>
		<div class="col-xs-6">
			<rip:inputText name="codi" textKey="unitat.search.filtre.codi"/>
		</div>
		<div class="col-xs-6">
<%-- 			<rip:inputText name="nivellAdministracio" textKey="unitat.search.filtre.nivell"/> --%>
			<rip:inputSelect name="nivellAdministracio" textKey="unitat.search.filtre.nivell" optionItems="${nivellsAdmin}" optionValueAttribute="descripcio" optionTextKeyAttribute="codi" disabled="true"/>
		</div>
		<div class="col-xs-6">
<%-- 			<rip:inputText name="comunitat" textKey="unitat.search.filtre.comunitat"/> --%>
			<rip:inputSelect name="comunitat" textKey="unitat.search.filtre.comunitat" optionItems="${comunitats}" optionValueAttribute="nom" optionTextKeyAttribute="codi" disabled="true"/>
		</div>
		<div class="col-xs-6">
<%-- 			<rip:inputText name="provincia" textKey="unitat.search.filtre.provincia"/> --%>
			<rip:inputSelect name="provincia" textKey="unitat.search.filtre.provincia" optionItems="${provincies}" optionValueAttribute="nom" optionTextKeyAttribute="codi" disabled="true"/>
		</div>
		<div class="col-xs-6">
<%-- 			<rip:inputText name="localitat" textKey="unitat.search.filtre.localitat"/> --%>
			<rip:inputSelect name="localitat" textKey="unitat.search.filtre.localitat" optionItems="${localitats}" optionValueAttribute="nom" optionTextKeyAttribute="codi"/>
		</div>
		<div class="col-xs-6">
			<rip:inputCheckbox name="unitatArrel" textKey="unitat.search.filtre.unitat.arrel"/>
		</div>
		<div class="col-md-6 pull-right">
			<div class="pull-right">
				<button type="submit" name="accio" value="netejar" class="btn btn-default"><spring:message code="comu.boto.netejar"/></button>
				<button type="submit" name="accio" value="filtrar" class="btn btn-primary"><span class="fa fa-filter"></span> <spring:message code="comu.boto.filtrar"/></button>
			</div>
		</div>
		
		<div id="modal-botons">
			<a href="<c:url value="/bustiaAdmin"/>" class="btn btn-default" data-modal-cancel="true"><spring:message code="comu.boto.cancelar"/></a>
		</div>
	</form:form>
	
<!-- 	<div class="col-xs-12"> -->
<!-- 		<a href="" onclick="triarRegistre('123213')" class="btn btn-success" data-toggle="modal"><span class="fa fa-hand-o-right"></span></a> -->
<!-- 	</div> -->
	
	<div class="col-xs-12">
	<table 
		id="unitats" 
		data-toggle="datatable" 
		data-url="<c:url value="/unitatajax/unitats/datatable"/>" 
		data-filter="#unitatsFiltreCommand" 
		data-default-order="1" 
		data-default-dir="asc" 
		data-editable="false"
		data-paging-enabled="false"
		class="table table-bordered table-striped">
		<thead>
			<tr>
				<th data-col-name="denominacion"><spring:message code="unitat.search.col.denominacio"/></th>
				<th data-col-name="raiz"><spring:message code="unitat.search.col.arrel"/></th>
				<th data-col-name="localidad"><spring:message code="unitat.search.col.localitat"/></th>
				<th data-col-name="codigo" data-template="#cellAccionsTemplate" data-orderable="false" width="10%">
					<script id="cellAccionsTemplate" type="text/x-jsrender">
						<a href="" onclick="triarRegistre('{{:codigo}}')" class="btn btn-success" data-toggle="modal"><span class="fa fa-hand-o-right"></span></a>
					</script>
				</th>
			</tr>
		</thead>
	</table>
	</div>
	
	<script>
		function triarRegistre(codigo) {
			webutilModalRetornarValor(codigo);
		}
	</script>
</body>
</html>
