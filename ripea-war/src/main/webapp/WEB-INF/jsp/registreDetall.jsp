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
body {
	min-height: 400px;
}
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
tr.odd {
	background-color: #f9f9f9;
}
tr.detall {
/* 	background-color: cornsilk; */
}
tr.clicable {
	cursor: pointer;
}
</style>
<script type="text/javascript">
	$(document).ready(function() {
		$(".desplegable").click(function(){
			$(this).find("span").toggleClass("fa-caret-up");
			$(this).find("span").toggleClass("fa-caret-down");
		});
	});
</script>
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
				<dt><spring:message code="registre.detalls.camp.llibre"/></dt><dd>${registre.llibreDescripcio} (${registre.llibreCodi})</dd>
				<dt><spring:message code="registre.detalls.camp.extracte"/></dt><dd>${registre.extracte}</dd>
				<dt><spring:message code="registre.detalls.camp.assumpte.tipus"/></dt><dd>${registre.assumpteTipusDescripcio} (${registre.assumpteTipusCodi})</dd>
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
			<c:choose>
				<c:when test="${not empty registre.interessats}">
					<table class="table table-bordered">
						<thead>
							<tr>
								<th style="width: 150px;"><spring:message code="registre.detalls.camp.interessat.tipus"/></th>
								<th style="width: 150px;"><spring:message code="registre.detalls.camp.interessat.document"/></th>
								<th><spring:message code="registre.detalls.camp.interessat.nom"/></th>
								<th style="width: 50px;"></th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="interessat" items="${registre.interessats}" varStatus="status">
								<tr <c:if test="${status.index%2 == 0}">class="odd"</c:if>>
									<td>
										<spring:message code="registre.interessat.tipus.enum.${interessat.tipus}"/>
									</td>
									<td>${interessat.documentTipus}: ${interessat.documentNum}</td>
									<c:choose>
										<c:when test="${interessat.tipus == 'PERSONA_FIS'}">
											<td>${interessat.nom} ${interessat.llinatge1} ${interessat.llinatge2}</td>
										</c:when>
										<c:otherwise>
											<td>${interessat.raoSocial}</td>
										</c:otherwise>
									</c:choose>
									<td>
										<button type="button" class="btn btn-default desplegable" href="#detalls_${status.index}" data-toggle="collapse" aria-expanded="false" aria-controls="detalls_${status.index}">
											<span class="fa fa-caret-down"></span>
										</button>
									</td>
								</tr>
								<tr class="collapse detall" id="detalls_${status.index}">
									<td colspan="4">
										<div class="row">
											<div class="col-xs-6">
												<dl class="dl-horizontal">
													<dt><spring:message code="interessat.form.camp.pais"/></dt><dd>${interessat.pais}</dd>
													<dt><spring:message code="interessat.form.camp.provincia"/></dt><dd>${interessat.provincia}</dd>											
													<dt><spring:message code="interessat.form.camp.municipi"/></dt><dd>${interessat.municipi}</dd>
													<dt><spring:message code="interessat.form.camp.adresa"/></dt><dd>${interessat.adresa}</dd>
													<dt><spring:message code="interessat.form.camp.codiPostal"/></dt><dd>${interessat.codiPostal}</dd>
												</dl>
											</div>
											<div class="col-xs-6">
												<dl class="dl-horizontal">
													<dt><spring:message code="interessat.form.camp.email"/></dt><dd>${interessat.email}</dd>
													<dt><spring:message code="interessat.form.camp.telefon"/></dt><dd>${interessat.telefon}</dd>
													<dt><spring:message code="registre.interessat.detalls.camp.emailHabilitat"/></dt><dd>${interessat.emailHabilitat}</dd>
													<dt><spring:message code="registre.interessat.detalls.camp.canalPreferent"/></dt><dd><c:if test="${not empty interessat.canalPreferent}"><spring:message code="registre.interessat.detalls.camp.canalPreferent.${interessat.canalPreferent}"/></c:if></dd>
													<dt><spring:message code="interessat.form.camp.observacions"/></dt><dd>${interessat.observacions}</dd>
												</dl>
											</div>
											<div class="col-xs-12">
												<dl class="dl-horizontal">
													<dt><spring:message code="registre.interessat.detalls.camp.representant"/></dt><dd>
														<c:if test="${not empty interessat.representant}">
															<c:choose>
																<c:when test="${interessat.representant.tipus == 'PERSONA_FIS'}">
																	${interessat.representant.nom} ${interessat.representant.llinatge1} ${interessat.representant.llinatge2}
																</c:when>
																<c:otherwise>
																	<td>${interessat.representant.raoSocial}</td>
																</c:otherwise>
															</c:choose>
														</c:if>
													</dd>
												</dl>
											</div>
										</div>
									</td>						
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</c:when>
				<c:otherwise>
					<div class="row col-xs-12">
						<div class="well">
							<spring:message code="registre.interessat.buit"/>
						</div>
					</div>
				</c:otherwise>
			</c:choose>
		</div>
		<div class="tab-pane" id="annexos" role="tabpanel">
			<c:choose>
				<c:when test="${not empty annexos}">
					<table class="table table-bordered">
						<thead>
							<tr>
								<th><spring:message code="registre.annex.detalls.camp.titol"/></th>
								<th style="width: 50px;"></th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="annex" items="${annexos}" varStatus="status">
								<tr <c:if test="${status.index%2 == 0}">class="odd"</c:if>>
									<td>${annex.titol}</td>
									<td>
										<button type="button" class="btn btn-default desplegable" href="#annex_${status.index}" data-toggle="collapse" aria-expanded="false" aria-controls="annex_${status.index}">
											<span class="fa fa-caret-down"></span>
										</button>
									</td>
								</tr>
								<tr class="collapse annex" id="annex_${status.index}">
									<td colspan="2">
										<div class="row">
							        		<div class="col-xs-6">
								        		<dl class="dl-horizontal">
													<dt title="<spring:message code="registre.annex.detalls.camp.titol"/>"><spring:message code="registre.annex.detalls.camp.titol"/></dt><dd>${annex.titol}</dd>
													<dt title="<spring:message code="registre.annex.detalls.camp.dataCaptura"/>"><spring:message code="registre.annex.detalls.camp.dataCaptura"/></dt><dd><fmt:formatDate value="${annex.dataCaptura}" pattern="dd/MM/yyyy HH:mm:ss"/></dd>
													<dt title="<spring:message code="registre.annex.detalls.camp.localitzacio"/>"><spring:message code="registre.annex.detalls.camp.localitzacio"/></dt><dd>${annex.localitzacio}</dd>
													<dt title="<spring:message code="registre.annex.detalls.camp.origenCiutadaAdmin"/>"><spring:message code="registre.annex.detalls.camp.origenCiutadaAdmin"/></dt><dd>${annex.origenCiutadaAdmin}</dd>
													<dt title="<spring:message code="registre.annex.detalls.camp.ntiTipusDocument"/>"><spring:message code="registre.annex.detalls.camp.ntiTipusDocument"/></dt><dd><c:if test="${not empty annex.ntiTipusDocument}"><spring:message code="registre.annex.detalls.camp.ntiTipusDocument.${annex.ntiTipusDocument}"/></c:if></dd>
													<dt title="<spring:message code="registre.annex.detalls.camp.sicresTipusDocument"/>"><spring:message code="registre.annex.detalls.camp.sicresTipusDocument"/></dt><dd><c:if test="${not empty annex.sicresTipusDocument}"><spring:message code="registre.annex.detalls.camp.sicresTipusDocument.${annex.sicresTipusDocument}"/></c:if></dd>
													<dt title="<spring:message code="registre.annex.detalls.camp.ntiElaboracioEstat"/>"><spring:message code="registre.annex.detalls.camp.ntiElaboracioEstat"/></dt><dd><c:if test="${not empty annex.ntiElaboracioEstat}"><spring:message code="registre.annex.detalls.camp.ntiElaboracioEstat.${annex.ntiElaboracioEstat}"/></c:if></dd>
													<dt title="<spring:message code="registre.annex.detalls.camp.observacions"/>"><spring:message code="registre.annex.detalls.camp.observacions"/></dt><dd>${annex.observacions}</dd>
												</dl>
							        		</div>
							        		<div class="col-xs-6">
								        		<dl class="dl-horizontal">
													<c:if test="${annex.ambDocument}">
														<dt title="<spring:message code="registre.annex.detalls.camp.firmaFitxerNom"/>" class="file-dt"><spring:message code="registre.annex.detalls.camp.fitxerNom"/></dt>
														<dd class="file-dd">
															${annex.fitxerNom}
															<a href="${registre.id}/annex/${annex.id}/arxiu/DOCUMENT" class="btn btn-default">
									        					<span class="fa fa-download icona-doc"  title="Descarregar document"></span> <spring:message code="comu.boto.descarregar"/>
															</a>
														</dd>
														<dt title="<spring:message code="registre.annex.detalls.camp.tamany"/>"><spring:message code="registre.annex.detalls.camp.tamany"/></dt><dd>${annex.fitxerTamany} bytes</dd>
													</c:if>
													<dt title="<spring:message code="registre.annex.detalls.camp.firmaMode"/>"><spring:message code="registre.annex.detalls.camp.firmaMode"/></dt><dd>${annex.firmaMode}</dd>
													<dt title="<spring:message code="registre.annex.detalls.camp.firmaCsv"/>"><spring:message code="registre.annex.detalls.camp.firmaCsv"/></dt><dd>${annex.firmaCsv}</dd>
													<dt title="<spring:message code="registre.annex.detalls.camp.validacioOCSP"/>"><spring:message code="registre.annex.detalls.camp.validacioOCSP"/></dt><dd>${annex.validacioOCSP}</dd>
													<c:if test="${annex.ambFirma}">
														<dt title="<spring:message code="registre.annex.detalls.camp.firmaFitxerNom"/>" class="file-dt"><spring:message code="registre.annex.detalls.camp.firmaFitxerNom"/></dt>
														<dd class="file-dd">
															${annex.firmaFitxerNom}
															<a href="${registre.id}/annex/${annex.id}/arxiu/FIRMA" class="btn btn-default">
									        					<span class="fa fa-download icona-doc"  title="Descarregar firma"></span> <spring:message code="comu.boto.descarregar"/>
															</a>
														</dd>
														<dt title="<spring:message code="registre.annex.detalls.camp.tamany"/>"><spring:message code="registre.annex.detalls.camp.tamany"/></dt><dd>${annex.firmaFitxerTamany} bytes</dd>
													</c:if>
												</dl>
							        		</div>
							        	</div>
									</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</c:when>
				<c:otherwise>
					<div class="row col-xs-12">
						<div class="well">
							<spring:message code="registre.annex.buit"/>
						</div>
					</div>
				</c:otherwise>
			</c:choose>
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
