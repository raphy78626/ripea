<%@page import="java.util.Iterator"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib tagdir="/WEB-INF/tags/ripea" prefix="rip"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<c:set var="titol"><spring:message code="registre.detalls.titol"/></c:set>
<html>
<head>
	<title>${titol}</title>
	<rip:modalHead/>
<style>
.tab-content {
    margin-top: 0.8em;
}
.icona-doc {
	color: #666666
}
.file-dt {
	margin-top: 9px;
}
.file-dd {
	margin-top: 3px;
}
</style>
</head>
<body>
	<ul class="nav nav-tabs" role="tablist">
		<li class="active" role="presentation"><a href="#informacio" aria-controls="informacio" role="tab" data-toggle="tab"><spring:message code="registre.detalls.pipella.informacio"/></a>
		</li>
		<li role="presentation">
			<a href="#interessats" aria-controls="interessats" role="tab" data-toggle="tab"><spring:message code="registre.detalls.pipella.interessats"/>&nbsp;<span class="badge">${fn:length(registre.interessats)}</span></a>
		</li>
		<li role="presentation">
			<a href="#annexos" aria-controls="annexos" role="tab" data-toggle="tab"><spring:message code="registre.detalls.pipella.annexos"/>&nbsp;<span class="badge">${fn:length(registre.annexos)}</span></a>
		</li>
		<c:if test="${registre.procesEstat != 'NO_PROCES'}">
			<li role="presentation">
				<a href="#proces" aria-controls="proces" role="tab" data-toggle="tab">
					<spring:message code="registre.detalls.pipella.proces"/>
					<c:if test="${registre.procesEstat == 'ERROR'}"><span class="fa fa-warning text-danger"></span></c:if>
				</a>
			</li>
		</c:if>
	</ul>
	<div class="tab-content">
		<div class="tab-pane active in" id="informacio" role="tabpanel">
			<dl class="dl-horizontal">
				<dt><spring:message code="registre.detalls.camp.tipus"/></dt><dd><spring:message code="registre.anotacio.tipus.enum.${registre.registreTipus}"/></dd>
				<dt><spring:message code="registre.detalls.camp.numero"/></dt><dd>${registre.identificador}</dd>
				<dt><spring:message code="registre.detalls.camp.data"/></dt><dd><fmt:formatDate value="${registre.data}" pattern="dd/MM/yyyy HH:mm:ss"/></dd>
				<c:if test="${registre.registreTipus == 'ENTRADA'}">
					<dt><spring:message code="registre.detalls.camp.desti"/></dt><dd>${registre.unitatAdministrativa}</dd>				
				</c:if>
				<c:if test="${registre.registreTipus == 'SORTIDA'}">
					<dt><spring:message code="registre.detalls.camp.origen"/></dt><dd>${registre.unitatAdministrativa}</dd>
				</c:if>
				<dt><spring:message code="registre.detalls.camp.entitat"/></dt><dd>${registre.entitatDescripcio} (${registre.entitatCodi})</dd>
				<dt><spring:message code="registre.detalls.camp.oficina"/></dt><dd>${registre.oficinaDescripcio} (${registre.oficinaCodi})</dd>
				<dt><spring:message code="registre.detalls.camp.data.origen"/></dt><dd><fmt:formatDate value="${registre.dataOrigen}" pattern="dd/MM/yyyy HH:mm:ss"/></dd>
				<dt><spring:message code="registre.detalls.camp.oficina.origen"/></dt><dd>${registre.oficinaOrigenDescripcio} (${registre.oficinaOrigenCodi})</dd>
				<dt><spring:message code="registre.detalls.camp.llibre"/></dt><dd>${registre.llibreDescripcio} (${registre.llibreCodi})</dd>
				<dt><spring:message code="registre.detalls.camp.extracte"/></dt><dd>${registre.extracte}</dd>
				<dt><spring:message code="registre.detalls.camp.assumpte.tipus"/></dt><dd>${registre.assumpteTipusDescripcio} (${registre.assumpteTipusCodi})</dd>
				<c:if test="${not empty registre.assumpteCodi}">
					<dt><spring:message code="registre.detalls.camp.assumpte.codi"/></dt><dd>${registre.assumpteCodi}</dd>
				</c:if>
				<dt><spring:message code="registre.detalls.camp.idioma"/></dt><dd>${registre.idiomaDescripcio} (${registre.idiomaCodi})</dd>
				<c:if test="${not empty registre.transportTipusCodi}">
					<dt><spring:message code="registre.detalls.camp.transport.tipus"/></dt><dd>${registre.transportTipusDescripcio} (${registre.transportTipusCodi})</dd>
				</c:if>
				<c:if test="${not empty registre.transportNumero}">
					<dt><spring:message code="registre.detalls.camp.transport.numero"/></dt><dd>${registre.transportNumero}</dd>
				</c:if>
				<c:if test="${not empty registre.usuariCodi}">
					<dt><spring:message code="registre.detalls.camp.usuari"/></dt><dd>${registre.usuariNom} (${registre.usuariCodi})</dd>
				</c:if>
				<c:if test="${not empty registre.aplicacioCodi}">
					<dt><spring:message code="registre.detalls.camp.aplicacio"/></dt><dd>${registre.aplicacioCodi} ${registre.aplicacioVersio}</dd>
				</c:if>
				<c:if test="${not empty registre.expedientNumero}">
					<dt><spring:message code="registre.detalls.camp.expedient"/></dt><dd>${registre.expedientNumero}</dd>
				</c:if>
				<c:if test="${not empty registre.observacions}">
					<dt><spring:message code="registre.detalls.camp.observacions"/></dt><dd>${registre.observacions}</dd>
				</c:if>
				<dt><spring:message code="registre.detalls.camp.ripea.alta"/></dt><dd><fmt:formatDate value="${registre.createdDate}" pattern="dd/MM/yyyy HH:mm:ss"/></dd>
			</dl>
		</div>
		<div class="tab-pane" id="interessats" role="tabpanel">
			<table class="table table-striped table-bordered">
				<thead>
					<tr>
						<th><spring:message code="registre.detalls.camp.interessat.tipus"/></th>
						<th><spring:message code="registre.detalls.camp.interessat.document"/></th>
						<th><spring:message code="registre.detalls.camp.interessat.nom"/></th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="interessat" items="${registre.interessats}">
						<tr>
							<td><spring:message code="registre.interessat.tipus.enum.${interessat.tipus}"/></td>
							<td>${interessat.documentTipus}: ${interessat.documentNum}</td>
							<c:choose>
								<c:when test="${interessat.tipus == 'PERSONA_FIS'}">
									<td>${interessat.nom} ${interessat.llinatge1} ${interessat.llinatge2}</td>
								</c:when>
								<c:otherwise>
									<td>${interessat.raoSocial}</td>
								</c:otherwise>
							</c:choose>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
		<div class="tab-pane" id="annexos" role="tabpanel">
			<div class="panel-group" id="annexos_accordion" role="tablist" aria-multiselectable="true">
			
			<c:forEach var="annex" items="${annexos}">
			  <div class="panel panel-default">
			    <div class="panel-heading" role="tab" id="heading-${annex.titol}">
			      <h4 class="panel-title">
			        <a role="button" data-toggle="collapse" data-parent="#annexos_accordion" href="#collapse-${annex.titol}" aria-expanded="false" aria-controls="collapse-${annex.titol}">
			          ${annex.titol}
			        </a>
			      </h4>
			    </div>
			    <div id="collapse-${annex.titol}" class="panel-collapse collapse" role="tabpanel" aria-labelledby="heading-${annex.titol}">
			      <div class="panel-body">
			      	<div class="row">
		        		<div class="col-xs-6">
			        		<dl class="dl-horizontal">
								<dt><spring:message code="registre.annex.detalls.camp.titol"/></dt><dd>${annex.titol}</dd>
								<dt><spring:message code="registre.annex.detalls.camp.dataCaptura"/></dt><dd><fmt:formatDate value="${annex.dataCaptura}" pattern="dd/MM/yyyy HH:mm:ss"/></dd>
								<dt><spring:message code="registre.annex.detalls.camp.localitzacio"/></dt><dd>${annex.localitzacio}</dd>
								<dt><spring:message code="registre.annex.detalls.camp.origenCiutadaAdmin"/></dt><dd>${annex.origenCiutadaAdmin}</dd>
								<dt><spring:message code="registre.annex.detalls.camp.observacions"/></dt><dd>${annex.observacions}</dd>
							</dl>
		        		</div>
		        		<div class="col-xs-6">
			        		<dl class="dl-horizontal">
								<dt><spring:message code="registre.annex.detalls.camp.firmaMode"/></dt><dd>${annex.firmaMode}</dd>
								<dt><spring:message code="registre.annex.detalls.camp.firmaCsv"/></dt><dd>${annex.firmaCsv}</dd>
								<dt><spring:message code="registre.annex.detalls.camp.validacioOCSP"/></dt><dd>${annex.validacioOCSP}</dd>
								
								<c:if test="${annex.ambDocument}">
									<dt class="file-dt"><spring:message code="registre.annex.detalls.camp.fitxerNom"/></dt>
									<dd class="file-dd">
										${annex.fitxerNom}
										<a href="${registre.id}/annex/${annex.id}/arxiu/DOCUMENT" class="btn btn-default">
				        					<span class="fa fa-download icona-doc"  title="Descarregar document"></span> <spring:message code="comu.boto.descarregar"/>
										</a>
									</dd>
								</c:if>
								<c:if test="${annex.ambFirma}">
									<dt class="file-dt"><spring:message code="registre.annex.detalls.camp.firmaFitxerNom"/></dt>
									<dd class="file-dd">
										${annex.firmaFitxerNom}
										<a href="${registre.id}/annex/${annex.id}/arxiu/FIRMA" class="btn btn-default">
				        					<span class="fa fa-download icona-doc"  title="Descarregar firma"></span> <spring:message code="comu.boto.descarregar"/>
										</a>
									</dd>
								</c:if>
							</dl>
		        		</div>
		        	</div>
			      </div>
			    </div>
			  </div>
			</c:forEach>
			  
			</div>
		</div>
		<div class="tab-pane" id="proces" role="tabpanel">
			<c:if test="${registre.procesEstat == 'ERROR'}">
				<div class="alert well-sm alert-danger alert-dismissable">
					<span class="fa fa-exclamation-triangle"></span>
					<spring:message code="registre.detalls.info.errors"/>
					<a href="${registre.id}/reintentar" class="btn btn-xs btn-default pull-right"><span class="fa fa-refresh"></span> <spring:message code="registre.detalls.accio.reintentar"/></a>
				</div>
			</c:if>
			<dl class="dl-horizontal">
				<dt><spring:message code="registre.detalls.camp.proces.estat"/></dt>
				<dd>${registre.procesEstat}</dd>
				<dt><spring:message code="registre.detalls.camp.proces.data"/></dt>
				<dd><fmt:formatDate value="${registre.procesData}" pattern="dd/MM/yyyy HH:mm:ss"/></dd>
				<dt><spring:message code="registre.detalls.camp.proces.intents"/></dt>
				<dd>${registre.procesIntents}</dd>
			</dl>
			<c:if test="${registre.procesEstat == 'ERROR'}">
				<pre style="height:300px">${registre.procesError}</pre>
			</c:if>
		</div>
	</div>
	<div id="modal-botons" class="well">
		<a href="<c:url value="/contenidor/${contenidor.id}"/>" class="btn btn-default modal-tancar" data-modal-cancel="true"><spring:message code="comu.boto.tancar"/></a>
	</div>
</body>
</html>
