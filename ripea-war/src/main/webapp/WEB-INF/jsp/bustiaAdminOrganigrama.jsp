<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib tagdir="/WEB-INF/tags/ripea" prefix="rip"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<rip:blocIconaContingutNoms/>
<html>
<head>
	<title><spring:message code="bustia.list.titol"/></title>
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
	<link href="<c:url value="/webjars/jstree/3.2.1/dist/themes/default/style.min.css"/>" rel="stylesheet">
	<script src="<c:url value="/webjars/jstree/3.2.1/dist/jstree.min.js"/>"></script>
</head>
<body>
	<form:form action="" method="get" cssClass="well" commandName="bustiaFiltreCommand">
		<div class="row">
			<div class="col-md-5">
				<rip:inputText name="nom" inline="true" placeholderKey="bustia.list.filtre.nom"/>
			</div>
			<div class="col-md-3">
				<rip:inputText name="unitatCodi" inline="true" placeholderKey="bustia.list.filtre.unitat.codidir3"/>
			</div>
			<div class="col-md-4 pull-right">
				<div class="pull-right">
					<button type="submit" name="accio" value="netejar" class="btn btn-default"><spring:message code="comu.boto.netejar"/></button>
					<button type="submit" name="accio" value="filtrar" class="btn btn-primary"><span class="fa fa-filter"></span> <spring:message code="comu.boto.filtrar"/></button>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-md-6">
				<p style="text-align:right"><a id="bustia-boto-nova" class="btn btn-default" href="${unitatCodiUrlPrefix}bustiaAdmin/new" data-toggle="modal"><span class="fa fa-plus"></span>&nbsp;<spring:message code="bustia.list.boto.nova.bustia"/></a></p>
				<rip:arbre id="arbreUnitatsOrganitzatives" atributId="codi" atributNom="denominacio" arbre="${arbreUnitatsOrganitzatives}" fulles="${busties}" fullesAtributId="id" fullesAtributNom="nom" fullesAtributPare="unitatCodi" fullesIcona="fa fa-inbox fa-lg" isArbreSeleccionable="${false}" isFullesSeleccionable="${true}" isOcultarCounts="${true}"/>
			</div>
			<div class="col-md-6">
				<div class="panel panel-default">
					<div class="panel-heading">
						<h2><spring:message code="bustia.form.titol.modificar"/></h2>
					</div>
					<div class="panel-body">
						<form:form action="${formAction}" method="post" commandName="bustiaCommand" role="form">
							<form:hidden path="id"/>
							<form:hidden path="pareId"/>
							<div class="col-md-12">
								<rip:inputText name="unitatCodi" textKey="bustia.form.camp.unitat" required="true"/>
							</div>
							<div class="col-md-12">
								<rip:inputText name="nom" textKey="bustia.form.camp.nom" required="true"/>
							</div>
							<div class="row">
								<div class="col-md-push-2 col-md-10">
									<button type="submit" class="btn btn-success"><span class="fa fa-save"></span> <spring:message code="comu.boto.guardar"/></button>
								</div>
							</div>						
						</form:form>
					</div>
				</div>
			</div>
		</div>
	</form:form>
</body>