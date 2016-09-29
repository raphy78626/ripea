<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib tagdir="/WEB-INF/tags/ripea" prefix="rip"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<c:set var="potModificarContingut" value="${false}"/>
<c:if test="${contingut.node}"><c:set var="potModificarContingut" value="${empty contingut.metaNode or contingut.metaNode.usuariActualWrite}"/></c:if>
<c:set var="htmlIconaCarpeta6em"><span class="fa-stack" style="font-size:.6em"><i class="fa fa-folder fa-stack-2x"></i><i class="fa fa-clock-o fa-stack-1x fa-inverse"></i></span></c:set>
<html>
<head>
	<title>
		<c:choose>
			<c:when test="${contingut.escriptori}">
				<c:choose>
					<c:when test="${paginaExpedients}"><c:set var="titleIconClass" value="list-alt"/>&nbsp;<spring:message code="contingut.titol.expedients"/></c:when>
					<c:otherwise><c:set var="titleIconClass" value="desktop"/>&nbsp;<spring:message code="contingut.titol.escriptori"/></c:otherwise>
				</c:choose>
			</c:when>
			<c:when test="${contingut.expedient}"><c:set var="titleIconClass" value="briefcase"/>&nbsp;${contingut.nom}</c:when>
			<c:when test="${contingut.carpeta}"><c:set var="titleIconClass" value="folder"/>&nbsp;${contingut.nom}</c:when>
			<c:when test="${contingut.document}"><c:set var="titleIconClass" value="file"/>&nbsp;${contingut.nom}</c:when>
			<c:when test="${contingut.bustia}"><c:set var="titleIconClass" value="inbox"/>&nbsp;${contingut.nom}</c:when>
		</c:choose>
	</title>
	<c:if test="${not empty titleIconClass}"><meta name="title-icon-class" content="fa fa-${titleIconClass}"/></c:if>
	<meta name="subtitle" content="${serveiPerTitol}"/>
	<script src="<c:url value="/webjars/datatables.net/1.10.11/js/jquery.dataTables.min.js"/>"></script>
	<script src="<c:url value="/webjars/datatables.net-bs/1.10.11/js/dataTables.bootstrap.min.js"/>"></script>
	<link href="<c:url value="/webjars/datatables.net-bs/1.10.11/css/dataTables.bootstrap.min.css"/>" rel="stylesheet"></link>
	<script src="<c:url value="/webjars/jsrender/1.0.0-rc.70/jsrender.min.js"/>"></script>
	<script src="<c:url value="/js/webutil.common.js"/>"></script>
	<script src="<c:url value="/js/webutil.datatable.js"/>"></script>
	<script src="<c:url value="/js/webutil.modal.js"/>"></script>
	<%--link href="<c:url value="/css/DT_bootstrap.css"/>" rel="stylesheet">
	<script src="<c:url value="/js/jquery.dataTables.js"/>"></script>
	<script src="<c:url value="/js/DT_bootstrap.js"/>"></script>
	<script src="<c:url value="/js/jsrender.min.js"/>"></script>
	<script src="<c:url value="/js/ripea.datatable.js"/>"></script>
	<script src="<c:url value="/js/ripea.modal.js"/>"></script--%>
	<script src="<c:url value="/js/clamp.js"/>"></script>
	<script src="<c:url value="/js/jquery-ui-1.10.3.custom.min.js"/>"></script>
<style>
.tab-content {
	margin-top: .8em;
}
#contenidor-contingut {
	margin-left: 0;
	margin-right: -11px;
}
#contenidor-contingut li.element-contingut {
	margin: 0 0 0px 0;
	padding: 0 10px 0 0;
	min-height: 140px;
	display: -moz-inline-stack;
	display: inline-block;
	vertical-align: top;
	zoom: 1;
	*display: inline;
	_height: 140px;
}
#contenidor-contingut .thumbnail {
	margin-bottom: 0 !important;
	border: 1px solid #f9f9f9;
}
#contenidor-contingut .thumbnail:hover {
	border: 1px solid #428bca;
	background-color: #f5f5f5;
	cursor: pointer;
}
#contenidor-contingut .thumbnail h4 {
	margin-top: 4px;
}
#contenidor-contingut .thumbnail a {
	text-decoration: none;
}
#contenidor-contingut .caption p {
}
#contenidor-contingut .caption .dropdown-menu {
	text-align: left;
}
#contenidor-contingut .caption .dropdown-menu li {
	width: 100%;
	margin: 0;
	padding: 0;
}

#contenidor-info h3 {
	font-weight: bold;
	margin-top: 0;
	border-bottom: 1px solid #e3e3e3;
	padding-bottom: .6em;
}
#contenidor-info h4 {
	font-weight: bold;
	margin-top: 0;
	border-bottom: 1px solid #f5f5f5;
	padding-bottom: .6em;
}
#contenidor-info dt {
	color: #999;
	font-size: small;
	font-style: italic;
	font-weight: normal;
}
#contenidor-info dd {
	font-size: medium;
	font-weight: bold;
	margin-bottom: 0.4em;
}
#contingut-botons {
	margin-bottom: .8em;
}
h4.interessats {
	padding-bottom: 0 !important;
	margin-bottom: 4px !important;
}
ul.interessats {
	padding-left: 1em !important;
}
.element-target .thumbnail {
	/*border: 1px solid #cacaca !important;*/
}
.element-hover .thumbnail {
	border: 1px solid #428bca !important;
	background-color: #f5f5f5;
}
.right {
	float: right;
}
.brep {
	padding-right: 5px;
    padding-left: 6px;
}
</style>
<c:if test="${edicioOnlineActiva and contingut.document and not empty contingut.escriptoriPare}">
	<script src="http://www.java.com/js/deployJava.js"></script>
<script type="text/javascript">
var officeExecAttributes = {
		id: 'officeExecApplet',
		code: 'es.caib.ripea.applet.OfficeExecApplet.class',
		archive: '<c:url value="/applet/ripea-applet.jar"/>',
		width: 1,
		height: 1};
var officeExecParameters = {};
$(document).ready(function() {
	$('.btn-document-modificar').click(function() {
		if (typeof officeExecApplet == 'undefined') {
			var intercepted = '';
			var dwBackup = document.write;
			document.write = function(arg) {intercepted += arg};
			deployJava.runApplet(
					officeExecAttributes,
					officeExecParameters,
					'1.5');
			document.write = dwBackup;
			$(document.body).append(intercepted);
		}
		if (officeExecApplet.openWithOffice) {
			officeExecApplet.openWithOffice(this.href)
			officeExecTimeout = function(){
				var exitCode = officeExecApplet.getExitValue();
				if (exitCode == 0) {
					location.reload(true);
				} else {
					setTimeout(officeExecTimeout, 500);
				}
			}
			setTimeout(officeExecTimeout, 500);
		} else {
			alert("<spring:message code="contingut.applet.no.trobat"/>");
		}
		return false;
	});
});
</script>
</c:if>
<script>
var registreTipusText = new Array();
<c:forEach var="option" items="${registreTipusEnumOptions}">
registreTipusText["${option.value}"] = "<spring:message code="${option.text}"/>";
</c:forEach>
var interessatTipusText = new Array();
<c:forEach var="option" items="${interessatTipusEnumOptions}">
interessatTipusText["${option.value}"] = "<spring:message code="${option.text}"/>";
</c:forEach>
var enviamentEstatText = new Array();
<c:forEach var="option" items="${enviamentEstatEnumOptions}">
enviamentEstatText["${option.value}"] = "<spring:message code="${option.text}"/>";
</c:forEach>
$(document).ready(function() {
	<c:if test="${contingut.carpeta and contingut.tipus == 'ESBORRANY'}">
		$('.container-main .panel-heading h2 span.fa').replaceWith('<rip:blocIconaCarpeta carpeta="${contingut}" petita="${true}"/>');
	</c:if>
	$('#contenidor-contingut li').mouseover(function() {
		$('a.btn', this).removeClass('hidden');
	});
	$('#contenidor-contingut li').mouseout(function() {
		$('a.btn', this).addClass('hidden');
	});
	$('#contenidor-info li a.confirm-delete').click(function() {
		return confirm('<spring:message code="contingut.confirmacio.esborrar.node"/>');
	});
	$('#contenidor-contingut li a.confirm-delete').click(function() {
		return confirm('<spring:message code="contingut.confirmacio.esborrar.node"/>');
	});
	$('li.element-contingut .caption p').each(function() {
		$clamp(this, {
			clamp: 2,
			useNativeClamp: true
		});
	});
	$('.table-hover > tbody > tr > td:not(:last-child)').css('cursor','pointer');
	$('.table-hover > tbody > tr > td:not(:last-child)').click(function(event) {
		event.stopPropagation();
		window.location.href = $('a:first', $(this).parent()).attr('href');
	});
	$('ul.interessats li').hover(function() {
		$('a', this).removeClass('hidden');
	},
	function() {
		$('a', this).addClass('hidden');
	});
	$('a.interessat-esborrar').click(function() {
		return confirm('<spring:message code="contingut.confirmacio.esborrar.interessat"/>');
	});
	$('#taulaDades').on('draw.dt', function (e, settings) {
		var api = new $.fn.dataTable.Api(settings);
		$('#dades-count').html(api.page.info().recordsTotal);
	});
	$('#taulaInteressats').on('draw.dt', function (e, settings) {
		var api = new $.fn.dataTable.Api(settings);
		$('#interessats-count').html(api.page.info().recordsTotal);
	});
	$('#taulaEnviaments').on('draw.dt', function (e, settings) {
		var api = new $.fn.dataTable.Api(settings);
		$('#enviaments-count').html(api.page.info().recordsTotal);
	});
	$('.element-draggable').draggable({
		containment: 'parent',
		helper: 'clone',
		revert: true,
		revertDuration: 200,
		opacity: 0.50,
		zIndex: 100,
		start: function() {
			$('div.element-noclick', this).addClass('noclick');
			$('div.element-noclick', this).tooltip('hide');
			$('div.element-noclick', this).tooltip('disable');
		},
		stop: function() {
			$('div.element-noclick', this).tooltip('enable');
		}
	});
	$('.element-droppable').droppable({
		accept: '.element-draggable',
		tolerance: 'pointer',
		activeClass: 'element-target',
		hoverClass: 'element-hover',
		drop: function(event, ui) {
			var origenId = ui.draggable.data('contenidor-id');
			var destiId = $(this).data('contenidor-id');
			window.location = origenId + "/moure/" + destiId;
		}
	});
});
</script>
</head>
<body>
	<rip:blocContenidorPath contingut="${contingut}"/>
	<div class="row">
		<c:set var="contingutClass">col-md-12</c:set>
		<c:if test="${contingut.expedient or contingut.carpeta or contingut.document}">
			<c:set var="contingutClass">col-md-9</c:set>
			<div class="col-md-3">
				<%--                    --%>
				<%-- Columna informació --%>
				<%--                    --%>
				<div id="contenidor-info" class="well">
					<h3>
						<spring:message code="contingut.info.informacio"/>
						<c:if test="${contingut.expedient}">
							<a href="../contingut/${contingut.id}/nti" class="btn btn-info btn-xs" data-toggle="modal">NTI</a>
						</c:if>
					</h3>
					<dl>
						<c:if test="${contingut.expedient}">
							<dt><spring:message code="contingut.info.numero"/></dt>
							<dd>${contingut.sequencia}/${contingut.any}</dd>
						</c:if>
						<c:if test="${contingut.carpeta or contingut.node and not empty contingut.metaNode}">
							<dt><spring:message code="contingut.info.tipus"/></dt>
							<c:choose>
								<c:when test="${contingut.expedient or contingut.document}">
									<dd>${contingut.metaNode.nom}</dd>
								</c:when>
								<c:otherwise>
									<dd><spring:message code="carpeta.tipus.enum.${contingut.tipus}"/></dd>
								</c:otherwise>
							</c:choose>
						</c:if>
						<dt><spring:message code="contingut.info.createl"/></dt>
						<dd><fmt:formatDate value="${contingut.createdDate}" pattern="dd/MM/yyyy HH:mm"/></dd>
						<dt><spring:message code="contingut.info.creatper"/></dt>
						<dd>${contingut.createdBy.nom}</dd>
						<c:if test="${contingut.expedient}">
							<dt><spring:message code="contingut.info.arxiu"/></dt>
							<dd>${contingut.arxiu.nom}</dd>
							<dt><spring:message code="contingut.info.estat"/></dt>
							<dd><spring:message code="expedient.estat.enum.${contingut.estat}"/></dd>
						</c:if>
						<c:if test="${contingut.document}">
							<dt><spring:message code="contingut.info.data"/></dt>
							<dd><fmt:formatDate value="${contingut.data}" pattern="dd/MM/yyyy"/></dd>
							<c:if test="${contingut.documentTipus != 'FISIC'}">
								<dt><spring:message code="contingut.info.versio"/></dt>
								<dd>${contingut.versioDarrera.versio}</dd>
							</c:if>
						</c:if>
					</dl>
					<rip:blocContenidorAccions id="botons-accions-info" contingut="${contingut}" modeLlistat="true" mostrarObrir="false"/>
				</div>
				<%--                     --%>
				<%-- /Columna informació --%>
				<%--                     --%>
			</div>
		</c:if>
		<div class="${contingutClass}">
			<c:if test="${contingut.node and not contingut.valid}">
				<div id="botons-errors-validacio" class="alert well-sm alert-warning alert-dismissable">
					<span class="fa fa-exclamation-triangle"></span>
					<c:choose>
						<c:when test="${contingut.expedient}"><spring:message code="contingut.errors.expedient"/></c:when>
						<c:when test="${contingut.document}"><spring:message code="contingut.errors.document"/></c:when>
					</c:choose>
					<a href="../contingut/${contingut.id}/errors" class="btn btn-xs btn-default pull-right" data-toggle="modal"><spring:message code="contingut.errors.mesinfo"/></a>
				</div>
			</c:if>
			<%--          --%>
			<%-- Pipelles --%>
			<%--          --%>
			<ul class="nav nav-tabs">
				<c:choose>
					<c:when test="${contingut.document}">
						<c:choose>
							<c:when test="${contingut.documentTipus != 'FISIC'}">
								<li class="active"><a href="#contingut" data-toggle="tab">
									<spring:message code="contingut.tab.versions"/>&nbsp;<span class="badge">${fn:length(documentVersions)}</span></a>
								</li>
							</c:when>
							<c:otherwise>
								<li class="active"><a href="#ubicacio" data-toggle="tab">
									<spring:message code="contingut.tab.ubicacio"/></a>
								</li>
							</c:otherwise>
						</c:choose>			
					</c:when>
					<c:otherwise>
						<li class="active"><a href="#contingut" data-toggle="tab">
							<spring:message code="contingut.tab.contingut"/>&nbsp;<span class="badge">${contingut.fillsNoRegistresCount}</span></a>
						</li>
					</c:otherwise>
				</c:choose>
				<c:if test="${contingut.node}">
					<li>
						<a href="#dades" data-toggle="tab"><spring:message code="contingut.tab.dades"/>&nbsp;<span class="badge" id="dades-count">${contingut.dadesCount}</span></a>
					</li>
				</c:if>
				<c:if test="${contingut.expedient}">
					<li>
						<a href="#registres" data-toggle="tab"><spring:message code="contingut.tab.registres"/>&nbsp;<span class="badge" id="registres-count">${contingut.fillsRegistresCount}</span></a>
					</li>
					<li>
						<a href="#interessats" data-toggle="tab"><spring:message code="contingut.tab.interessats"/>&nbsp;<span class="badge" id="interessats-count">${interessatsCount}</span></a>
					</li>
					<li>
						<a href="#enviaments" data-toggle="tab"><spring:message code="contingut.tab.enviaments"/>&nbsp;<span class="badge" id="enviaments-count">${enviamentsCount}</span></a>
					</li>
				</c:if>
			</ul>
			<%--           --%>
			<%-- /Pipelles --%>
			<%--           --%>
			<div class="tab-content">
				<c:choose>
					<c:when test="${contingut.document}">
						<%--          --%>
						<%-- Document --%>
						<%--          --%>
						<c:choose>
							<c:when test="${contingut.documentTipus != 'FISIC'}">
								<div class="tab-pane active in" id="contingut">
									<div id="document-versions" class="panel-group" id="accordion">
										<c:forEach var="versio" items="${documentVersions}" varStatus="status">
											<div class="panel panel-default">
												<div class="panel-heading">
													<h4 class="panel-title">
														<a class="accordion-toggle" data-toggle="collapse" data-parent="#accordion" href="#collapse_${versio.id}">
															<spring:message code="contingut.versions.versio"/> ${versio.versio}
														</a>
													</h4>
												</div>
												<div id="collapse_${versio.id}" class="panel-collapse collapse<c:if test="${status.first}"> in</c:if>">
													<div class="panel-body">
														<dl class="dl-horizontal">
															<dt><spring:message code="contingut.versions.arxiu.nom"/>:</dt>
															<dd>${versio.arxiuNom}</dd>
															<dt><spring:message code="contingut.versions.arxiu.tipus"/>:</dt>
															<dd>${versio.arxiuContentType}</dd>
															<dt><spring:message code="contingut.versions.arxiu.tamany"/>:</dt>
															<dd>${versio.arxiuContentLength} bytes</dd>
															<dt><spring:message code="contingut.versions.createl"/>:</dt>
															<dd><fmt:formatDate value="${versio.createdDate}" pattern="dd/MM/yyyy HH:mm"/></dd>
															<c:if test="${versio.firmaIntentat}">
																<dt><spring:message code="contingut.versions.firma.estat"/>:</dt>
																<dd>
																	<c:choose>
																		<c:when test="${versio.firmaError}">
																			<span class="label label-danger" title="${versio.portafirmesEnviamentDarrer.errorDescripcio}"><span class="fa fa-warning"></span>&nbsp;ERROR</span>
																		</c:when>
																		<c:otherwise><spring:message code="document.firma.estat.enum.${versio.firmaEstat}"/></c:otherwise>
																	</c:choose>
																</dd>
															</c:if>
															<%--c:if test="${versio.custodiat}">
																<dt><spring:message code="contingut.versions.custodia.url"/>:</dt>
																<dd><a href="${versio.custodiaUrl}" target="_blank">${versio.custodiaUrl}</a> <span class="fa fa-external-link"></span></dd>
															</c:if--%>
														</dl>
														<div class="btn-toolbar pull-right">
															<c:if test="${not empty contingut.escriptoriPare and status.first}">
																<a href="../webdav${contingut.pathAsString}/${contingut.nom}/${contingut.versioDarrera.arxiuNom}" class="btn btn-default btn-document-modificar"><span class="fa fa-pencil"></span>&nbsp;<spring:message code="contingut.boto.link.dav"/></a>
															</c:if>
															<c:if test="${edicioOnlineActiva}">
																<c:if test="${not empty contingut.escriptoriPare and status.first}">
																	<a href="../webdav${contingut.pathAsString}/${contingut.nom}/${contingut.versioDarrera.arxiuNom}" class="btn btn-default btn-document-modificar"><span class="fa fa-pencil"></span>&nbsp;<spring:message code="contingut.boto.editar.office"/></a>
																</c:if>
															</c:if>
															<a href="../contingut/${contingut.id}/document/${contingut.id}/descarregar/${versio.versio}" class="btn btn-default"><span class="fa fa-download"></span>&nbsp;<spring:message code="comu.boto.descarregar"/></a>
															<c:if test="${not empty contingut.metaNode and (contingut.metaNode.firmaPortafirmesActiva or contingut.metaNode.firmaPassarelaActiva)}">
																<div class="btn-group">
		  															<button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown" aria-expanded="false">
																		<spring:message code="contingut.boto.firma.accions"/> <span class="caret"></span>
																	</button>
																	<ul class="dropdown-menu" role="menu">
																		<%--c:if test="${contingut.metaNode.firmaPortafirmesActiva}">
																			<c:choose>
																				<c:when test="${not versio.custodiat}">
																					<c:if test="${not versio.firmaEstatBloquejarEnviaments}">
																						<li><a href="../contingut/${contingut.id}/document/${contingut.id}/portafirmes/upload" data-toggle="modal"><span class="fa fa-send"></span>&nbsp;<spring:message code="contingut.boto.portafirmes.enviar"/></a></li>
																					</c:if>
																					<c:if test="${versio.firmaEstatPendent}">
																						<li><a href="../contingut/${contingut.id}/document/${contingut.id}/portafirmes/cancel" data-confirm="<spring:message code="contingut.boto.portafirmes.cancelar.confirm"/>"><span class="fa fa-times"></span>&nbsp;<spring:message code="contingut.boto.portafirmes.cancelar"/></a></li>
																					</c:if>
																					<c:if test="${versio.firmaEstatBloquejarEnviaments}">
																						<li><a href="#" data-toggle="modal" data-target="#pfirma-info-${versio.id}"><span class="fa fa-info-circle"></span>&nbsp;<spring:message code="contingut.boto.portafirmes.info"/></a></li>
																					</c:if>
																					<c:if test="${versio.firmaError}">
																						<li><a href="../contingut/${contingut.id}/document/${contingut.id}/custodia/reintentar"><span class="fa fa-repeat"></span>&nbsp;<spring:message code="contingut.boto.custodia.reintentar"/></a></li>
																					</c:if>
																				</c:when>
																				<c:otherwise>
																					<li><a href="../contingut/${contingut.id}/document/${contingut.id}/custodia/esborrar"><span class="fa fa-remove"></span>&nbsp;<spring:message code="contingut.boto.custodia.esborrar"/></a></li>
																				</c:otherwise>
																			</c:choose>
																		</c:if--%>
																		<c:if test="${contingut.metaNode.firmaPassarelaActiva and not versio.firmaEstatBloquejarEnviaments}">
																			<%--c:choose>
																				<c:when test="${not versio.custodiat}">
																					<li><a href="../document/${contingut.id}/firmaPassarela" data-toggle="modal"><span class="fa fa-edit"></span>&nbsp;<spring:message code="contingut.boto.firma.passarela"/></a></li>
																				</c:when>
																				<c:otherwise>
																					<li><a href="../document/${contingut.id}/custodia/esborrar"><span class="fa fa-remove"></span>&nbsp;<spring:message code="contingut.boto.custodia.esborrar"/></a></li>
																				</c:otherwise>
																			</c:choose--%>
																		</c:if>
																	</ul>
																	<c:if test="${contingut.metaNode.firmaPortafirmesActiva and versio.firmaEstatBloquejarEnviaments}">
																		<div id="pfirma-info-${versio.id}" class="modal fade">
																			<div class="modal-dialog">
																				<div class="modal-content">
																					<div class="modal-header">
																						<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only"><spring:message code="comu.boto.tancar"/></span></button>
																						<h4 class="modal-title"><spring:message code="contingut.boto.portafirmes.info.modal.titol"/></h4>
																					</div>
																					<div class="modal-body">
																						<c:if test="${versio.firmaError}">
																							<div class="alert well-sm alert-danger">
																								<span class="fa fa-exclamation-triangle"></span>
																								${versio.portafirmesEnviamentDarrer.errorDescripcio}
																							</div>
																						</c:if>
																						<dl class="dl-horizontal">
																							<dt><spring:message code="contingut.boto.portafirmes.info.modal.motiu"/>:</dt>
																							<dd>${versio.portafirmesEnviamentDarrer.motiu}</dd>
																							<dt><spring:message code="contingut.boto.portafirmes.info.modal.prioritat"/>:</dt>
																							<dd>${versio.portafirmesEnviamentDarrer.prioritat}</dd>
																							<dt><spring:message code="contingut.boto.portafirmes.info.modal.enviament"/>:</dt>
																							<dd><fmt:formatDate value="${versio.portafirmesEnviamentDarrer.dataEnviament}" pattern="dd/MM/yyyy HH:mm:ss"/></dd>
																							<dt><spring:message code="contingut.boto.portafirmes.info.modal.caducitat"/>:</dt>
																							<dd><fmt:formatDate value="${versio.portafirmesEnviamentDarrer.dataCaducitat}" pattern="dd/MM/yyyy"/></dd>
																							<dt><spring:message code="contingut.boto.portafirmes.info.modal.id"/>:</dt>
																							<dd>${versio.portafirmesEnviamentDarrer.portafirmesId}</dd>
																							<dt><spring:message code="contingut.boto.portafirmes.info.modal.estat"/>:</dt>
																							<dd>${versio.portafirmesEnviamentDarrer.portafirmesEstat}</dd>
																							<c:if test="${versio.portafirmesEnviamentDarrer.callbackCount gt 0}">
																								<dt><spring:message code="contingut.boto.portafirmes.info.modal.rebuts"/>:</dt>
																								<dd>${versio.portafirmesEnviamentDarrer.callbackCount}</dd>
																								<dt><spring:message code="contingut.boto.portafirmes.info.modal.darrer"/>:</dt>
																								<dd><fmt:formatDate value="${versio.portafirmesEnviamentDarrer.callbackDarrer}" pattern="dd/MM/yyyy HH:mm:ss"/></dd>
																							</c:if>
																						</dl>
																					</div>
																				</div>
																			</div>
																		</div>
																	</c:if>
																</div>
															</c:if>
														</div>
													</div>
												</div>
											</div>
										</c:forEach>
									</div>
								</div>
							</c:when>
							<c:otherwise>
								<div class="tab-pane active in" id="ubicacio">
										Aquest document només està disponible en format físic i es troba a la següent ubicació:
										<br/><br/>
										<pre>${contingut.ubicacio}</pre> 
									
								</div>
							</c:otherwise>
						</c:choose>	
						<%--           --%>
						<%-- /Document --%>
						<%--           --%>
					</c:when>
					<c:otherwise>
						<div class="tab-pane active in" id="contingut">
							<%--                   --%>
							<%-- Pipella contingut --%>
							<%--                   --%>
							<div class="text-right" id="contingut-botons">
								<div class="btn-group">
									<a href="../contingut/${contingut.id}/canviVista/icones" class="btn btn-default<c:if test="${vistaIcones}"> active</c:if>">
										<span class="fa fa-th"></span>
									</a>
									<a href="../contingut/${contingut.id}/canviVista/llistat" class="btn btn-default<c:if test="${vistaLlistat}"> active</c:if>">
										<span class="fa fa-th-list"></span>
									</a>
								</div>
								<c:if test="${(contingut.escriptori or not empty contingut.escriptoriPare) and (contingut.escriptori or contingut.carpeta or ((contingut.expedient or contingut.document) and potModificarContingut))}">
									<div id="botons-crear-contingut" class="btn-group">
										<button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown"><span class="fa fa-plus"></span>&nbsp;<spring:message code="contingut.boto.crear.contingut"/>&nbsp;<span class="caret"></span></button>
										<ul class="dropdown-menu text-left" role="menu">
											<li><a href="../contingut/${contingut.id}/carpeta/new" data-toggle="modal" data-reload-page="true"><span class="fa fa-folder"></span>&nbsp;&nbsp;<spring:message code="contingut.boto.crear.carpeta"/>...</a></li>
											<li><a href="../contingut/${contingut.id}/document/new" data-toggle="modal" data-reload-page="true"><span class="fa fa-file"></span>&nbsp;&nbsp;<spring:message code="contingut.boto.crear.document"/>...</a></li>
											<c:if test="${contingut.crearExpedients and not empty metaExpedients}">
												<li><a href="../contingut/${contingut.id}/expedient/new" data-toggle="modal" data-reload-page="true"><span class="fa fa-briefcase"></span>&nbsp;<spring:message code="contingut.boto.crear.expedient"/>...</a></li>
											</c:if>
										</ul>
									</div>
								</c:if>
  							</div>
							<rip:blocContenidorContingut contingut="${contingut}" mostrarExpedients="${true}" mostrarNoExpedients="${true}"/>
							<%--                    --%>
							<%-- /Pipella contingut --%>
							<%--                    --%>
						</div>
					</c:otherwise>
				</c:choose>
				<c:if test="${contingut.node}">
					<div class="tab-pane" id="dades">
						<%--               --%>
						<%-- Pipella dades --%>
						<%--               --%>
						<table id="taulaDades" data-toggle="datatable" data-url="<c:url value="/contingut/${contingut.id}/dada/datatable"/>" data-paging-enabled="false" data-botons-template="#taulaDadesBotonsTemplate" class="table table-striped table-bordered" style="width:100%">
							<thead>
								<tr>
									<th data-col-name="id" data-visible="false" width="4%">#</th>
									<th data-col-name="metaDada.nom" data-orderable="false"><spring:message code="contingut.dades.columna.dada"/></th>
									<th data-col-name="valorMostrar" data-orderable="false"><spring:message code="contingut.dades.columna.valor"/></th>
									<c:if test="${not empty contingut.escriptoriPare and potModificarContingut}">
										<th data-col-name="id" data-orderable="false" data-template="#cellAccionsTemplate" width="10%">
											<script id="cellAccionsTemplate" type="text/x-jsrender">
												<div class="dropdown">
													<button class="btn btn-primary" data-toggle="dropdown"><span class="fa fa-cog"></span>&nbsp;<spring:message code="comu.boto.accions"/>&nbsp;<span class="caret"></span></button>
													<ul class="dropdown-menu">
														<li><a href="../contingut/${contingut.id}/dada/{{:id}}" data-toggle="modal"><span class="fa fa-pencil"></span>&nbsp;&nbsp;<spring:message code="comu.boto.modificar"/></a></li>
														<li><a href="../contingut/${contingut.id}/dada/{{:id}}/delete" data-toggle="ajax" data-confirm="<spring:message code="contingut.confirmacio.esborrar.dada"/>"><span class="fa fa-trash-o"></span>&nbsp;&nbsp;<spring:message code="comu.boto.esborrar"/></a></li>
													</ul>
												</div>
											</script>
										</th>
									</c:if>
								</tr>
							</thead>
						</table>
						<script id="taulaDadesBotonsTemplate" type="text/x-jsrender">
							<p style="text-align:right"><a href="../contingut/${contingut.id}/dada/new" class="btn btn-default" data-toggle="modal"><span class="fa fa-plus"></span>&nbsp;<spring:message code="contingut.boto.nova.dada"/></a></p>
						</script>
						<%--                --%>
						<%-- /Pipella dades --%>
						<%--                --%>
					</div>
				</c:if>
				<c:if test="${contingut.expedient}">
					<div class="tab-pane" id="registres">
						<%--                   --%>
						<%-- Pipella registres --%>
						<%--                   --%>
						<table id="taulaRegistres" data-toggle="datatable" data-url="<c:url value="/contingut/${contingut.id}/registre/datatable"/>" data-paging-enabled="false" class="table table-bordered table-striped" style="width:100%">
							<thead>
								<tr>
									<th data-col-name="id" data-visible="false">#</th>
									<th data-col-name="registreTipus" data-template="#cellTipusTemplate" data-orderable="false" width="10%">
										<spring:message code="contingut.registre.columna.tipus"/>
										<script id="cellTipusTemplate" type="text/x-jsrender">
											{{:~eval('registreTipusText["' + registreTipus + '"]')}}
										</script>
									</th>
									<th data-col-name="identificador" data-orderable="false" width="10%"><spring:message code="contingut.registre.columna.identificador"/></th>
									<th data-col-name="data" data-converter="datetime" data-orderable="false" width="20%"><spring:message code="contingut.registre.columna.data"/></th>
									<th data-col-name="extracte" data-orderable="false" width="40%"><spring:message code="contingut.registre.columna.extracte"/></th>
									<th data-col-name="id" data-orderable="false" data-template="#cellAccionsRegistreTemplate" width="10%">
										<script id="cellAccionsRegistreTemplate" type="text/x-jsrender">
											<div class="dropdown">
												<button class="btn btn-primary" data-toggle="dropdown"><span class="fa fa-cog"></span>&nbsp;<spring:message code="comu.boto.accions"/>&nbsp;<span class="caret"></span></button>
												<ul class="dropdown-menu">
													<li><a href="../contingut/${contingut.id}/registre/{{:id}}" data-toggle="modal"><span class="fa fa-info-circle"></span>&nbsp;<spring:message code="comu.boto.detalls"/></a></li>
													<li><a href="../contingut/{{:id}}/log" data-toggle="modal"><span class="fa fa-list"></span>&nbsp;<spring:message code="comu.boto.historial"/></a></li>
												</ul>
											</div>
										</script>
									</th>
								</tr>
							</thead>
						</table>
						<%--                    --%>
						<%-- /Pipella registres --%>
						<%--                    --%>
					</div>
					<div class="tab-pane" id="interessats">
						<%--                     --%>
						<%-- Pipella interessats --%>
						<%--                     --%>
						<table id="taulaInteressats" data-toggle="datatable" data-url="<c:url value="/contingut/${contingut.id}/interessat/datatable"/>" data-paging-enabled="false" data-botons-template="#taulaInteressatsBotonsTemplate" class="table table-striped table-bordered" style="width:100%">
							<thead>
								<tr>
									<th data-col-name="id" data-visible="false">#</th>
									<th data-col-name="representantId" data-visible="false">#</th>
									<th data-col-name="tipus" data-template="#cellTipusInteressatTemplate" data-orderable="false" width="15%">
										<spring:message code="contingut.interessat.columna.tipus"/>
										<script id="cellTipusInteressatTemplate" type="text/x-jsrender">
											{{:~eval('interessatTipusText["' + tipus + '"]')}}
										</script>
									</th>
									<th data-col-name="documentNum" data-orderable="false" width="15%"><spring:message code="contingut.interessat.columna.document"/></th>
									<th data-col-name="identificador" data-orderable="false" width="35%"><spring:message code="contingut.interessat.columna.identificador"/></th>
									<th data-col-name="representantIdentificador" data-orderable="false" width="25%"><spring:message code="contingut.interessat.columna.representant"/>
<%-- 
									<th data-rdt-property="representantIdentificador" data-rdt-sortable="false" data-rdt-template="cellAccionsRepresentantTemplate" width="25%"><spring:message code="contingut.interessat.columna.representant"/>
										<script id="cellAccionsRepresentantTemplate" type="text/x-jsrender">
											{{if tipus != '<%=es.caib.ripea.core.api.dto.InteressatTipusEnumDto.ADMINISTRACIO%>'}}
												{{if representantId}}
													{{:representantIdentificador}}
													<div class="dropdown right">
														<button class="btn btn-success brep" data-toggle="dropdown"><span class="fa fa-cog"></span>&nbsp;<span class="caret"></span></button>
														<ul class="dropdown-menu">
															<li><a href="../expedient/${contenidor.id}/interessat/{{:id}}/representant/{{:representantId}}" data-rdt-link-modal="true"><span class="fa fa-pencil"></span>&nbsp;&nbsp;<spring:message code="contingut.interessat.modificar.prepresentant"/></a></li>
															<li><a href="../expedient/${contenidor.id}/interessat/{{:id}}/representant/{{:representantId}}/delete" data-rdt-link-ajax="true" data-rdt-link-confirm="<spring:message code="contingut.confirmacio.esborrar.representant"/>"><span class="fa fa-trash-o"></span>&nbsp;&nbsp;<spring:message code="contingut.interessat.borrar.representant"/></a></li>
														</ul>
													</div>	
												{{else}}
													<a class="btn btn-success right" href="../expedient/${contenidor.id}/interessat/{{:id}}/representant/new" data-rdt-link-modal="true" title="<spring:message code="contingut.interessat.nou.prepresentant"/>"><span class="fa fa-plus"></span></a>
												{{/if}}
											{{/if}}
										</script>
									</th>
--%>
									<th data-col-name="id" data-orderable="false" data-template="#cellAccionsInteressatTemplate" width="10%">
										<script id="cellAccionsInteressatTemplate" type="text/x-jsrender">
											<div class="dropdown">
												<button class="btn btn-primary" data-toggle="dropdown"><span class="fa fa-cog"></span>&nbsp;<spring:message code="comu.boto.accions"/>&nbsp;<span class="caret"></span></button>
												<ul class="dropdown-menu">
													<li><a href="../expedient/${contingut.id}/interessat/{{:id}}" data-toggle="modal"><span class="fa fa-pencil"></span>&nbsp;&nbsp;<spring:message code="comu.boto.modificar"/></a></li>
													<li><a href="../expedient/${contingut.id}/interessat/{{:id}}/delete" data-toggle="ajax" data-confirm="<spring:message code="contingut.confirmacio.esborrar.interessat"/>"><span class="fa fa-trash-o"></span>&nbsp;&nbsp;<spring:message code="comu.boto.esborrar"/></a></li>
													{{if tipus != '<%=es.caib.ripea.core.api.dto.InteressatTipusEnumDto.ADMINISTRACIO%>'}}
														<li><hr/></li>
														{{if representantId}}
															<li><a href="../expedient/${contingut.id}/interessat/{{:id}}/representant/{{:representantId}}" data-toggle="modal"><span class="fa fa-pencil"></span>&nbsp;&nbsp;<spring:message code="contingut.interessat.modificar.prepresentant"/></a></li>
															<li><a href="../expedient/${contingut.id}/interessat/{{:id}}/representant/{{:representantId}}/delete" data-toggle="ajax" data-confirm="<spring:message code="contingut.confirmacio.esborrar.representant"/>"><span class="fa fa-trash-o"></span>&nbsp;&nbsp;<spring:message code="contingut.interessat.borrar.representant"/></a></li>
														{{else}}
															<li><a href="../expedient/${contingut.id}/interessat/{{:id}}/representant/new" data-toggle="modal"><span class="fa fa-plus"></span>&nbsp;&nbsp;<spring:message code="contingut.interessat.nou.prepresentant"/></a></li>														
														{{/if}}
													{{/if}}
												</ul>
											</div>
										</script>
									</th>
								</tr>
							</thead>
						</table>
						<script id="taulaInteressatsBotonsTemplate" type="text/x-jsrender">
							<c:if test="${potModificarContingut}">
								<p style="text-align:right"><a href="../expedient/${contingut.id}/interessat/new" class="btn btn-default" data-toggle="modal"><span class="fa fa-plus"></span>&nbsp;<spring:message code="contingut.boto.nou.interessat"/></a></p>
							</c:if>
						</script>
						<%--                      --%>
						<%-- /Pipella interessats --%>
						<%--                      --%>
					</div>
					<div class="tab-pane" id="enviaments">
						<%--                    --%>
						<%-- Pipella enviaments --%>
						<%--                    --%>
						<table id="taulaEnviaments" data-toggle="datatable" data-url="<c:url value="/expedient/${contingut.id}/enviament/datatable"/>" data-paging-enabled="false" class="table table-bordered table-striped" style="width:100%">
							<thead>
								<tr>
									<th data-col-name="notificacio" data-visible="false"></th>
									<th data-col-name="publicacio" data-visible="false"></th>
									<th data-col-name="tipus" data-orderable="false" data-template="#cellEnviamentTipusTemplate" width="15%">
										<spring:message code="contingut.enviament.columna.tipus"/>
										<script id="cellEnviamentTipusTemplate" type="text/x-jsrender">
											{{if notificacio}}
												{{if tipus == 'MANUAL'}}
													<spring:message code="contingut.enviament.notificacio.man"/>
												{{else}}
													<spring:message code="contingut.enviament.notificacio.elec"/>
												{{/if}}
											{{else publicacio}}
												<spring:message code="contingut.enviament.publicacio"/>
											{{/if}}
										</script>
									</th>
									<th data-col-name="dataEnviament" data-converter="datetime" data-orderable="false" width="20%"><spring:message code="contingut.enviament.columna.data"/></th>
									<th data-col-name="assumpte" data-orderable="false" width="25%"><spring:message code="contingut.enviament.columna.assumpte"/></th>
									<th data-col-name="destinatari" data-orderable="false" data-template="#cellEnviamentDestiTemplate" width="20%">
										<spring:message code="contingut.enviament.columna.destinatari"/>
										<script id="cellEnviamentDestiTemplate" type="text/x-jsrender">
											{{if notificacio}}
												{{:destinatari}}
											{{else publicacio}}
												{{:tipus}}
											{{/if}}
										</script>
									</th>
									<th data-col-name="estat" data-template="#cellEnviamentEstatTemplate" data-orderable="false" width="10%">
										<spring:message code="contingut.enviament.columna.estat"/>
										<script id="cellEnviamentEstatTemplate" type="text/x-jsrender">
											{{if estat == 'PENDENT'}}
												<span class="label label-warning"><span class="fa fa-clock-o"></span> {{:~eval('enviamentEstatText["' + estat + '"]')}}</span>
											{{else estat == 'ENVIAT_OK'}}
												<span class="label label-info"><span class="fa fa-envelope-o"></span> {{:~eval('enviamentEstatText["' + estat + '"]')}}</span>
											{{else estat == 'ENVIAT_ERROR'}}
												<span class="label label-danger"><span class="fa fa-warning"></span> {{:~eval('enviamentEstatText["' + estat + '"]')}}</span>
											{{else estat == 'PROCESSAT_OK'}}
												<span class="label label-success"><span class="fa fa-check"></span> {{:~eval('enviamentEstatText["' + estat + '"]')}}</span>
											{{else estat == 'PROCESSAT_REBUTJAT'}}
												<span class="label label-default"><span class="fa fa-times"></span> {{:~eval('enviamentEstatText["' + estat + '"]')}}</span>
											{{else estat == 'PROCESSAT_ERROR'}}
												<span class="label label-danger"><span class="fa fa-warning"></span> {{:~eval('enviamentEstatText["' + estat + '"]')}}</span>
											{{/if}}
										</script>
									</th>
									<th data-col-name="id" data-orderable="false" data-template="#cellEnviamentAccionsTemplate" width="10%">
										<script id="cellEnviamentAccionsTemplate" type="text/x-jsrender">
											<div class="dropdown">
												<button class="btn btn-primary" data-toggle="dropdown"><span class="fa fa-cog"></span>&nbsp;<spring:message code="comu.boto.accions"/>&nbsp;<span class="caret"></span></button>
												<ul class="dropdown-menu">
													<li><a href="../expedient/${contingut.id}/{{if notificacio}}notificacio{{else}}publicacio{{/if}}/{{:id}}/info" data-toggle="modal"><span class="fa fa-info-circle"></span>&nbsp;&nbsp;<spring:message code="comu.boto.detalls"/></a></li>
													{{if notificacio && tipus == 'MANUAL'}}
														<li><a href="../expedient/${contingut.id}/notificacio/{{:id}}" data-toggle="modal"><span class="fa fa-pencil"></span>&nbsp;&nbsp;<spring:message code="comu.boto.modificar"/></a></li>
														<li><a href="../expedient/${contingut.id}/notificacio/{{:id}}/delete" data-toggle="ajax" data-confirm="<spring:message code="contingut.confirmacio.esborrar.notificacio"/>"><span class="fa fa-trash-o"></span>&nbsp;&nbsp;<spring:message code="comu.boto.esborrar"/></a></li>
													{{else publicacio}}
														<li><a href="../expedient/${contingut.id}/publicacio/{{:id}}" data-toggle="modal"><span class="fa fa-pencil"></span>&nbsp;&nbsp;<spring:message code="comu.boto.modificar"/></a></li>
														<li><a href="../expedient/${contingut.id}/publicacio/{{:id}}/delete" data-toggle="ajax" data-confirm="<spring:message code="contingut.confirmacio.esborrar.publicacio"/>"><span class="fa fa-trash-o"></span>&nbsp;&nbsp;<spring:message code="comu.boto.esborrar"/></a></li>
													{{/if}}
												</ul>
											</div>
										</script>
									</th>
								</tr>
							</thead>
						</table>
						<%--                     --%>
						<%-- /Pipella enviaments --%>
						<%--                     --%>
					</div>
				</c:if>
			</div>
		</div>
	</div>
</body>
</html>
