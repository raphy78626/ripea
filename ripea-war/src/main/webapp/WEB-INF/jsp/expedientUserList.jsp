<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib tagdir="/WEB-INF/tags/ripea" prefix="rip"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<c:set var="idioma"><%=org.springframework.web.servlet.support.RequestContextUtils.getLocale(request).getLanguage()%></c:set>
<%
pageContext.setAttribute(
		"expedientUsernOpcionsEstatEnumOptions",
		es.caib.ripea.war.helper.EnumHelper.getOptionsForEnum(
				es.caib.ripea.war.command.ExpedientFiltreCommand.ExpedientFiltreOpcionsEstatEnum.class,
				"expedient.user.opcions.estat.enum."));
%>
<html>
<head>
	<title><spring:message code="expedient.list.user.titol"/></title>
	<script src="<c:url value="/webjars/datatables.net/1.10.11/js/jquery.dataTables.min.js"/>"></script>
	<script src="<c:url value="/webjars/datatables.net-bs/1.10.11/js/dataTables.bootstrap.min.js"/>"></script>
	<link href="<c:url value="/webjars/datatables.net-bs/1.10.11/css/dataTables.bootstrap.min.css"/>" rel="stylesheet"></link>
	<script src="<c:url value="/webjars/jsrender/1.0.0-rc.70/jsrender.min.js"/>"></script>
	<script src="<c:url value="/js/webutil.common.js"/>"></script>
	<script src="<c:url value="/js/webutil.datatable.js"/>"></script>
	<script src="<c:url value="/js/webutil.modal.js"/>"></script>
	<link href="<c:url value="/css/datepicker.css"/>" rel="stylesheet"/>
	<script src="<c:url value="/js/bootstrap-datepicker.js"/>"></script>
	<script src="<c:url value="/js/datepicker-locales/bootstrap-datepicker.${idioma}.js"/>"></script>
	<link href="<c:url value="/css/select2.css"/>" rel="stylesheet"/>
	<link href="<c:url value="/css/select2-bootstrap.css"/>" rel="stylesheet"/>
	<script src="<c:url value="/js/select2.min.js"/>"></script>
	<script src="<c:url value="/js/select2-locales/select2_locale_${idioma}.js"/>"></script>
<script>
$(document).ready(function() {
});
</script>
</head>
<body>
	<form:form action="" method="post" cssClass="well" commandName="expedientFiltreCommand">
		<div class="row">
			<div class="col-md-2">
				<rip:inputText name="numero" inline="true" placeholderKey="expedient.list.user.placeholder.numero"/>
			</div>
			<div class="col-md-2">
				<rip:inputText name="nom" inline="true" placeholderKey="expedient.list.user.placeholder.nom"/>
			</div>
			<div class="col-md-3">
				<rip:inputSelect name="metaExpedientId" optionItems="${metaExpedients}" optionValueAttribute="id" optionTextAttribute="nom" emptyOption="true" placeholderKey="expedient.list.user.placeholder.tipusExpedient" inline="true"/>
			</div>
			<div class="col-md-3">
				<rip:inputSelect name="arxiuId" optionItems="${arxius}" optionValueAttribute="id" optionTextAttribute="nom" emptyOption="true" placeholderKey="expedient.list.user.placeholder.arxiu" inline="true"/>
			</div>
			<div class="col-md-2">
				<rip:inputSelect name="estatFiltre" optionItems="${expedientUsernOpcionsEstatEnumOptions}" optionValueAttribute="value" optionTextKeyAttribute="text" placeholderKey="expedient.list.user.placeholder.estat" inline="true"/>
			</div>
		</div>
		<div class="row">
			<div class="col-md-9">
				<div class="row">
					<div class="col-md-3">
						<rip:inputDate name="dataCreacioInici" inline="true" placeholderKey="expedient.list.user.placeholder.creacio.inici"/>
					</div>
					<div class="col-md-3">
						<rip:inputDate name="dataCreacioFi" inline="true" placeholderKey="expedient.list.user.placeholder.creacio.fi"/>
					</div>
					<div class="col-md-3">
						<rip:inputDate name="dataTancatInici" inline="true" placeholderKey="expedient.list.user.placeholder.tancat.inici"/>
					</div>
					<div class="col-md-3">
						<rip:inputDate name="dataTancatFi" inline="true" placeholderKey="expedient.list.user.placeholder.tancat.fi"/>
					</div>
				</div>
			</div>
			<div class="col-md-3 pull-right">
				<div class="pull-right">
					<button type="submit" name="accio" value="filtrar" class="btn btn-primary" style="display:none;"></button>
					<button type="submit" name="accio" value="netejar" class="btn btn-default"><spring:message code="comu.boto.netejar"/></button>
					<button type="submit" name="accio" value="filtrar" class="btn btn-primary"><span class="fa fa-filter"></span> <spring:message code="comu.boto.filtrar"/></button>
				</div>
			</div>
		</div>
	</form:form>
	<table id="taulaDades" 
			data-toggle="datatable" 
			data-url="<c:url value="/expedient/datatable"/>" 
			class="table table-bordered table-striped" 
			data-default-order="7" 
			data-default-dir="desc"
			style="width:100%">
		<thead>
			<tr>
				<th data-col-name="id" data-visible="false">Id</th>
				<th data-col-name="codiPropietariEscriptoriPare" data-visible="false"></th>
				<th data-col-name="metaNode.usuariActualWrite" data-visible="false"></th>
				<th data-col-name="sequenciaAny"><spring:message code="expedient.list.user.columna.numero"/></th>
				<th data-col-name="nom" width="30%"><spring:message code="expedient.list.user.columna.nom"/></th>
				<th data-col-name="metaNode.nom" width="15%"><spring:message code="expedient.list.user.columna.tipus"/></th>
				<th data-col-name="arxiu.nom" width="15%"><spring:message code="expedient.list.user.columna.arxiu"/></th>
				<th data-col-name="createdDate" data-type="datetime" data-converter="datetime" width="15%"><spring:message code="expedient.list.user.columna.createl"/></th>
				<th data-col-name="estat" data-template="#cellEstatTemplate" width="10%">
					<spring:message code="expedient.list.user.columna.estat"/>
					<script id="cellEstatTemplate" type="text/x-jsrender">
						{{if estat == 'OBERT'}}<span class="fa fa-folder-open" title="Obert"></span>{{else}}<span class="fa fa-folder" title="Tancat">{{/if}}
					</script>
				</th>
				<th data-col-name="nomPropietariEscriptoriPare" data-orderable="false" width="20%"><spring:message code="expedient.list.user.columna.agafatper"/></th>
				<th data-col-name="id" data-template="#cellAccionsTemplate" data-orderable="false" width="10%">
					<script id="cellAccionsTemplate" type="text/x-jsrender">
						<div class="dropdown">
							<button class="btn btn-primary" data-toggle="dropdown"><span class="fa fa-cog"></span>&nbsp;<spring:message code="comu.boto.accions"/>&nbsp;<span class="caret"></span></button>
							<ul class="dropdown-menu">
								<li><a href="contenidor/{{:id}}"><span class="fa fa-folder-open-o"></span>&nbsp;&nbsp;<spring:message code="comu.boto.obrir"/></a></li>
								{{if estat == 'OBERT'}}
									{{if metaNode.usuariActualWrite}}
										{{if !codiPropietariEscriptoriPare}}
										<li><a href="expedient/{{:id}}/agafar" data-toggle="ajax"><span class="fa fa-lock"></span>&nbsp;&nbsp;<spring:message code="comu.boto.agafar"/></a></li>
										{{else codiPropietariEscriptoriPare != '${pageContext.request.userPrincipal.name}'}}
										<li><a href="expedient/{{:id}}/agafar" data-confirm="<spring:message code="expedient.list.user.agafar.confirm.1"/> {{:nomPropietariEscriptoriPare}}. <spring:message code="expedient.list.user.agafar.confirm.2"/>" data-toggle="ajax"><span class="fa fa-unlock"></span>&nbsp;&nbsp;<spring:message code="comu.boto.agafar"/></a></li>
										{{/if}}
									{{/if}}
									{{if codiPropietariEscriptoriPare == '${pageContext.request.userPrincipal.name}'}}
									<li><a href="expedient/{{:id}}/alliberar" data-toggle="ajax"><span class="fa fa-unlock"></span>&nbsp;&nbsp;<spring:message code="comu.boto.alliberar"/></a></li>
									{{/if}}
								{{/if}}
							</ul>
						</div>
					</script>
				</th>				
				
			</tr>
		</thead>
	</table>
</body>