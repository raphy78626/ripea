<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib tagdir="/WEB-INF/tags/ripea" prefix="rip"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<rip:blocIconaContingutNoms/>
	
<c:url value="/unitatajax/unitat" var="urlConsultaInicial"/>
<c:url value="/unitatajax/unitats" var="urlConsultaLlistat"/>

<html>
<head>
	<title><spring:message code="bustia.list.titol"/></title>
	<script src="<c:url value="/webjars/datatables.net/1.10.11/js/jquery.dataTables.min.js"/>"></script>
	<script src="<c:url value="/webjars/datatables.net-bs/1.10.11/js/dataTables.bootstrap.min.js"/>"></script>
	<link href="<c:url value="/webjars/datatables.net-bs/1.10.11/css/dataTables.bootstrap.min.css"/>" rel="stylesheet"></link>
	<link href="<c:url value="/webjars/select2/4.0.6-rc.1/dist/css/select2.min.css"/>" rel="stylesheet"/>
	<link href="<c:url value="/webjars/select2-bootstrap-theme/0.1.0-beta.4/dist/select2-bootstrap.min.css"/>" rel="stylesheet"/>
	<script src="<c:url value="/webjars/select2/4.0.6-rc.1/dist/js/select2.full.min.js"/>"></script>
	<script src="<c:url value="/webjars/select2/4.0.6-rc.1/dist/js/i18n/${requestLocale}.js"/>"></script>
	<script src="<c:url value="/webjars/jsrender/1.0.0-rc.70/jsrender.min.js"/>"></script>
	<script src="<c:url value="/js/webutil.common.js"/>"></script>
	<script src="<c:url value="/js/webutil.datatable.js"/>"></script>
	<script src="<c:url value="/js/webutil.modal.js"/>"></script>
	<link href="<c:url value="/webjars/jstree/3.2.1/dist/themes/default/style.min.css"/>" rel="stylesheet">
	<script src="<c:url value="/webjars/jstree/3.2.1/dist/jstree.min.js"/>"></script>
<script>


function changedCallback(e, data) {
	$('#panellInfo').css('visibility', '');
	$('#panellInfo').css('display', 'none');
	$(".datatable-dades-carregant").css("display", "block");

	var bustiaId = data.node.id;
	
	var permisUrl = "bustiaAdmin/" + bustiaId + "/permis";
	$('#permis-boto-nou').attr('href', permisUrl + '/new');
	$('#permisos').webutilDatatable('refresh-url', permisUrl  + '/datatable');


	var bustiaUrl = "bustiaAdminOrganigrama/" + bustiaId;

	var bustiaNomSel = $('#nom', $('#panellInfo'));
	var unitatSel = $('#unitatId', $('#panellInfo'));
 
	$.ajax({
		type: 'GET',
		url: bustiaUrl,
		success: function(resposta) {

			bustiaNomSel.val(resposta.nom);
			var newOption = new Option(resposta.unitatOrganitzativa.nom, resposta.unitatOrganitzativa.id, false, true);
			unitatSel.append(newOption).trigger('change');
			
			$('#id', $('#panellInfo')).val(resposta.id);
			$('#pareId', $('#panellInfo')).val(resposta.pare.id);


			var isActiva = resposta.activa;
			if(isActiva) {
				$('#activarBtn').hide();
				$('#desactivarBtn').show();
				} else {
					$('#activarBtn').show();
					$('#desactivarBtn').hide();
				}
		},
	 	complete: function() {		
			$('#panellInfo').css('display', 'block');
			$(".datatable-dades-carregant").css("display", "none");
		}

	});

};


function deleteBustia() {
	  if (confirm('<spring:message code="contingut.confirmacio.esborrar.node"/>')) {
		  location.href="bustiaAdminOrganigrama/" + $('#id').val() + "/delete";		
	  } 
}

function marcarPerDefecte() {
	location.href="bustiaAdminOrganigrama/" + $('#id').val() + "/default";
}

function activar() {
	location.href="bustiaAdminOrganigrama/" + $('#id').val() + "/enable";
}

function desactivar() {
	location.href="bustiaAdminOrganigrama/" + $('#id').val() + "/disable";
}





</script>
</head>
<body>
	<form:form action="" method="post" cssClass="well" commandName="bustiaFiltreOrganigramaCommand">
		<div class="row">
			<div class="col-md-5">
				<rip:inputText name="nomFiltre" inline="true" placeholderKey="bustia.list.filtre.nom"/>
			</div>
			<div class="col-md-3">
				<rip:inputSuggest
					name="unitatIdFiltre" 
					urlConsultaInicial="${urlConsultaInicial}" 
					urlConsultaLlistat="${urlConsultaLlistat}" 
					inline="true"
					placeholderKey="bustia.form.camp.unitat"
					suggestValue="id"
					suggestText="nom"/>
			</div>
			<div class="col-md-4 pull-right">
				<div class="pull-right">
					<button type="submit" name="accio" value="netejar" class="btn btn-default"><spring:message code="comu.boto.netejar"/></button>
					<button type="submit" name="accio" value="filtrar" class="btn btn-primary"><span class="fa fa-filter"></span> <spring:message code="comu.boto.filtrar"/></button>
				</div>
			</div>
		</div>
	</form:form>
	<div class="row">
		<div class="col-md-5">
 			<c:set var="fullesAtributInfoText"><spring:message code="contingut.enviar.info.bustia.defecte"/></c:set> 
			<p style="text-align:right"><a id="bustia-boto-nova" class="btn btn-default" href="bustiaAdminOrganigrama/new" 
				data-toggle="modal" data-refresh-pagina="true"><span class="fa fa-plus"></span>&nbsp;<spring:message code="bustia.list.boto.nova.bustia"/></a></p>
			<rip:arbre id="arbreUnitatsOrganitzatives" atributId="codi" atributNom="denominacio" arbre="${arbreUnitatsOrganitzatives}" fulles="${busties}" fullesAtributId="id" fullesAtributNom="nom" 
				fullesAtributPare="unitatCodi" fullesAtributInfo="perDefecte" fullesAtributInfoText="${fullesAtributInfoText}"  fullesIcona="fa fa-inbox fa-lg" 
				changedCallback="changedCallback" isArbreSeleccionable="${false}" isFullesSeleccionable="${true}" isOcultarCounts="${true}"/>
		</div>
		<div class="col-md-7" id="panellInfo"<c:if test="${empty unitatCodi}"> style="visibility:hidden"</c:if>>
			<div class="panel panel-default">
				<div class="panel-heading">
					<h2><spring:message code="bustia.form.titol.modificar"/><small><%-- ${bustia.nom} --%></small></h2>
				</div>
				<div class="panel-body">
					<c:set var="formAction"><rip:modalUrl value="/bustiaAdminOrganigrama/modify"/></c:set>
					<form:form action="${formAction}" method="post" commandName="bustiaCommand" role="form">
						<form:hidden path="id"/>
						<form:hidden path="pareId"/>

						<rip:inputSuggest 
							name="unitatId" 
							urlConsultaInicial="${urlConsultaInicial}" 
							urlConsultaLlistat="${urlConsultaLlistat}" 
							inline="false"
							placeholderKey="bustia.form.camp.unitat"
							suggestValue="id"
							suggestText="nom"
							textKey="bustia.form.camp.unitat"
							required="true" />
						<br/>
						<br/>
						<rip:inputText name="nom" textKey="bustia.form.camp.nom" required="true"/>
						<br/>
						<div class="panel panel-default" style="margin-top: 45px;">
							<div class="panel-heading">
								<h2><spring:message code="bustia.permis.titol"/><small><%-- ${permis.nom} --%></small></h2>
							</div>
							<div class="panel-body">
								<div class="text-right boto-nou-permis-organigrama" data-toggle="botons-titol">
									<a class="btn btn-default" id="permis-boto-nou" href="" data-toggle="modal" data-datatable-id="permisos"><span class="fa fa-plus"></span>&nbsp;<spring:message code="permis.list.boto.nou.permis"/></a>
								</div>
								<table id="permisos" data-toggle="datatable" data-url="<c:url value="/permis/datatable"/>" data-search-enabled="false" data-paging-enabled="false" data-default-order="1" data-default-dir="asc" class="table table-striped table-bordered">
									<thead>
										<tr>
											<th data-col-name="principalTipus" data-renderer="enum(PrincipalTipusEnumDto)"><spring:message code="permis.list.columna.tipus"/></th>
											<th data-col-name="principalNom"><spring:message code="entitat.permis.columna.principal"/></th>
											<th data-col-name="administration" data-template="#cellAdministrationTemplate">
												<spring:message code="permis.list.columna.administracio"/>
												<script id="cellAdministrationTemplate" type="text/x-jsrender">
														{{if administration}}<span class="fa fa-check"></span>{{/if}}
												</script>
											</th>
											<th data-col-name="read" data-template="#cellReadTemplate">
												<spring:message code="permis.list.columna.usuari"/>
												<script id="cellReadTemplate" type="text/x-jsrender">
														{{if read}}<span class="fa fa-check"></span>{{/if}}
												</script>
											</th>
											<th data-col-name="id" data-template="#cellAccionsTemplate" data-orderable="false" width="10%">
												<script id="cellAccionsTemplate" type="text/x-jsrender">
														<div class="dropdown">
															<button class="btn btn-primary" data-toggle="dropdown"><span class="fa fa-cog"></span>&nbsp;<spring:message code="comu.boto.accions"/>&nbsp;<span class="caret"></span></button>
															<ul class="dropdown-menu">
																<li><a href="permis/{{:id}}" data-toggle="modal"><span class="fa fa-pencil"></span>&nbsp;&nbsp;<spring:message code="comu.boto.modificar"/></a></li>
																<li><a href="permis/{{:id}}/delete" data-toggle="ajax" data-confirm="<spring:message code="entitat.permis.confirmacio.esborrar"/>"><span class="fa fa-trash-o"></span>&nbsp;&nbsp;<spring:message code="comu.boto.esborrar"/></a></li>
															</ul>
														</div>
												</script>
											</th>
										</tr>
									</thead>
								</table>
							</div>
						</div>
						<div class="row">
							<div class="col-md-4">
								<button type="button" onclick="marcarPerDefecte()" class="btn btn-default"><span class="fa fa-check-square-o"></span> <spring:message code="bustia.list.accio.per.defecte"/></button>
							</div>
						
							<div class="col-md-2">
								<button id="activarBtn" type="button" onclick="activar()" style="display: none;" class="btn btn-default"><span class="fa fa-check"></span> <spring:message code="comu.boto.activar"/></button>
								<button id="desactivarBtn" type="button" onclick="desactivar()" style="display: none;" class="btn btn-default"><span class="fa fa-times"></span> <spring:message code="comu.boto.desactivar"/></button>
							</div>
						
							<div class="col-md-3" style="margin-left: 15px;">
								<button type="button" class="btn btn-default" onclick="deleteBustia()"><span class="fa fa-trash-o"></span> <spring:message code="contingut.admin.boto.esborrar"/></button>
							</div>
						
							<div class="col-md-2">
								<button type="submit" class="btn btn-success"><span class="fa fa-save"></span> <spring:message code="comu.boto.guardar"/></button>
							</div>
						</div>						
					</form:form>
				</div>
			</div>
		</div>
		<div class="col-md-7 datatable-dades-carregant" style="display: none; text-align: center; margin-top: 100px;">
			<span class="fa fa-circle-o-notch fa-spin fa-3x"></span>
		</div>
	</div>

</body>