<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib tagdir="/WEB-INF/tags/ripea" prefix="rip"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<c:set var="potModificarContingut" value="${false}"/>
<c:if test="${contingut.node}"><c:set var="potModificarContingut" value="${empty contingut.metaNode or contingut.metaNode.usuariActualWrite}"/></c:if>
<c:set var="htmlIconaCarpeta6em"><span class="fa-stack" style="font-size:.6em"><i class="fa fa-folder fa-stack-2x"></i><i class="fa fa-clock-o fa-stack-1x fa-inverse"></i></span></c:set>
<rip:blocIconaContingutNoms/>
<html>
<head>
	<title>
		<c:choose>
			<c:when test="${contingut.escriptori}">
				<c:choose>
					<c:when test="${paginaExpedients}">&nbsp;<spring:message code="contingut.titol.expedients"/></c:when>
					<c:otherwise>&nbsp;<spring:message code="contingut.titol.escriptori"/></c:otherwise>
				</c:choose>
			</c:when>
			<c:when test="${contingut.expedient}">&nbsp;${contingut.nom}</c:when>
			<c:when test="${contingut.carpeta}">&nbsp;${contingut.nom}</c:when>
			<c:when test="${contingut.document}">&nbsp;${contingut.nom}</c:when>
			<c:when test="${contingut.bustia}">&nbsp;${contingut.nom}</c:when>
		</c:choose>
	</title>
	<c:set var="titleIconClass"><rip:blocIconaContingut contingut="${contingut}" nomesIconaNom="true"/></c:set>
	<c:set var="titleIconClass" value="${fn:trim(titleIconClass)}"/>
	<c:if test="${not empty titleIconClass}"><meta name="title-icon-class" content="fa ${titleIconClass}"/></c:if>
	<meta name="subtitle" content="${serveiPerTitol}"/>
	<script src="<c:url value="/webjars/datatables.net/1.10.11/js/jquery.dataTables.min.js"/>"></script>
	<script src="<c:url value="/webjars/datatables.net-bs/1.10.11/js/dataTables.bootstrap.min.js"/>"></script>
	<link href="<c:url value="/webjars/datatables.net-bs/1.10.11/css/dataTables.bootstrap.min.css"/>" rel="stylesheet"></link>
	<link href="<c:url value="/webjars/bootstrap-datepicker/1.6.1/dist/css/bootstrap-datepicker.min.css"/>" rel="stylesheet"/>
	<script src="<c:url value="/webjars/bootstrap-datepicker/1.6.1/dist/js/bootstrap-datepicker.min.js"/>"></script>
	<script src="<c:url value="/webjars/bootstrap-datepicker/1.6.1/dist/locales/bootstrap-datepicker.${requestLocale}.min.js"/>"></script>
	<script src="<c:url value="/webjars/autoNumeric/1.9.30/autoNumeric.js"/>"></script>
	<script src="<c:url value="/webjars/jsrender/1.0.0-rc.70/jsrender.min.js"/>"></script>
	<script src="<c:url value="/js/webutil.common.js"/>"></script>
	<script src="<c:url value="/js/webutil.datatable.js"/>"></script>
	<script src="<c:url value="/js/webutil.modal.js"/>"></script>
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
	border: 2px solid #f9f9f9;
}
#contenidor-contingut .thumbnail:hover {
	border: 2px solid #ddd;
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
.element-hover .thumbnail {
	border: 2px solid #ddd !important;
	background-color: #f5f5f5;
}
.right {
	float: right;
}
#nodeDades .form-group {
	margin-bottom: 6px;
}
#nodeDades input.form-control {
	width: 322px;
}
#nodeDades input.multiple {
	width: 280px; !important
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
var notificacioEstatText = new Array();
<c:forEach var="option" items="${notificacioEstatEnumOptions}">
notificacioEstatText["${option.value}"] = "<spring:message code="${option.text}"/>";
</c:forEach>
var publicacioEstatText = new Array();
<c:forEach var="option" items="${publicacioEstatEnumOptions}">
publicacioEstatText["${option.value}"] = "<spring:message code="${option.text}"/>";
</c:forEach>
$(document).ready(function() {
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
	var nodeDadesInputChange = function() {
		var $pare = $(this).parent();
		$pare.removeClass('has-success');
		$pare.removeClass('has-warning');
		$pare.removeClass('has-error');
		$pare.addClass('has-warning has-feedback');
		$(this).next().removeClass().addClass('glyphicon glyphicon-pencil form-control-feedback');
		$(this).attr('title', 'Valor modificat pendent de guardar');
	}
	$('#nodeDades input').change(nodeDadesInputChange);
	$('form#nodeDades').submit(function() {
		$.post(
				'../ajax/contingutDada/${contingut.id}/save',
				$('#nodeDades').serialize(),
				function (data) {
					if (data.estatOk) {
						$('#nodeDades input').each(function() {
							var $pare = $(this).parent();
							if ($pare.hasClass('has-warning') || $pare.hasClass('has-error')) {
								$pare.removeClass('has-success');
								$pare.removeClass('has-warning');
								$pare.removeClass('has-error');
								$pare.addClass('has-success has-feedback');
								$(this).next().removeClass().addClass('glyphicon glyphicon-ok form-control-feedback');
								$(this).attr('title', 'Valor guardat correctament');
							} else {
								$pare.removeClass('has-success');
								$pare.removeClass('has-feedback');
								$(this).next().removeClass();
								$(this).removeAttr('title');
							}
						});
						$.get(
								'../ajax/contingutDada/${contingut.id}/count',
								function (data) {
									$('#dades-count').html(data);
								});
					} else {
						$('#nodeDades input').each(function() {
							for (var i = 0; i < data.errorsCamps.length; i++) {
								var error = data.errorsCamps[i];
								if (error.camp == $(this).attr('name')) {
									var $pare = $(this).parent();
									$pare.removeClass('has-success');
									$pare.removeClass('has-warning');
									$pare.removeClass('has-error');
									$pare.addClass('has-error has-feedback');
									$(this).next().removeClass().addClass('glyphicon glyphicon-warning-sign form-control-feedback');
									$(this).attr('title', error.missatge);
									break;
								}
							}
						});
					}
					webutilRefreshMissatges();
				});
		return false;
	});
	$('form#nodeDades td .form-group').on('clone.multifield', function(event, clon) {
		$('input', clon).change(nodeDadesInputChange);
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
						<c:if test="${pluginArxiuActiu}">
							<a href="../contingut/${contingut.id}/arxiu" class="btn btn-info btn-xs" data-toggle="modal">Arxiu</a>
						</c:if>
					</h3>
					<dl>
						<dt>
							<c:choose>
								<c:when test="${contingut.expedient or contingut.document}">
									<spring:message code="contingut.info.titol"/>
								</c:when>
								<c:otherwise>
									<spring:message code="contingut.info.nom"/>
								</c:otherwise>
							</c:choose>
						</dt>
						<dd>${contingut.nom}</dd>
						<dt><spring:message code="contingut.info.tipus"/></dt>
						<dd><spring:message code="contingut.tipus.enum.${contingut.tipus}"/></dd>
						<c:if test="${contingut.expedient}">
							<c:if test="${not empty contingut.metaNode}">
								<dt><spring:message code="contingut.info.meta.expedient"/></dt>
								<dd>${contingut.metaNode.nom}</dd>
							</c:if>
							<dt><spring:message code="contingut.info.numero"/></dt>
							<dd>${contingut.sequencia}/${contingut.any}</dd>
							<dt><spring:message code="contingut.info.arxiu"/></dt>
							<dd>${contingut.arxiu.nom}</dd>
							<dt><spring:message code="contingut.info.estat"/></dt>
							<dd><spring:message code="expedient.estat.enum.${contingut.estat}"/></dd>
						</c:if>
						<c:if test="${contingut.document}">
							<c:if test="${not empty contingut.metaNode}">
								<dt><spring:message code="contingut.info.meta.document"/></dt>
								<dd>${contingut.metaNode.nom}</dd>
							</c:if>
							<dt><spring:message code="contingut.info.data"/></dt>
							<dd><fmt:formatDate value="${contingut.data}" pattern="dd/MM/yyyy"/></dd>
							<dt><spring:message code="contingut.info.estat"/></dt>
							<dd><spring:message code="document.estat.enum.${contingut.estat}"/></dd>
							<c:if test="${contingut.versioCount gt 0}">
								<dt><spring:message code="contingut.info.versio"/></dt>
								<dd>${contingut.versioDarrera}</dd>
							</c:if>
						</c:if>
					</dl>
					<c:if test="${contingut.expedient or contingut.document}">
						<a href="#informacioEni" class="btn btn-default btn-xs pull-right" role="button" data-toggle="collapse" aria-expanded="false" aria-controls="informacioEni" style="margin-top:-3.5em">
						<spring:message code="contingut.info.mes"/> ...
						</a>
						<div class="collapse" id="informacioEni">
							<dl>
								<c:if test="${contingut.expedient}">
									<dt><spring:message code="contingut.info.nti.identificador"/></dt>
									<dd style="overflow:hidden;text-overflow:ellipsis" title="${contingut.ntiIdentificador}">${contingut.ntiIdentificador}</dd>
									<dt><spring:message code="contingut.info.nti.organ"/></dt>
									<dd>${contingut.ntiOrganoDescripcio}</dd>
									<dt><spring:message code="contingut.info.nti.data.obertura"/></dt>
									<dd><fmt:formatDate value="${contingut.ntiFechaApertura}" pattern="dd/MM/yyyy HH:mm:ss"/></dd>
									<dt><spring:message code="contingut.info.nti.classificacio"/></dt>
									<dd>${contingut.ntiClasificacionSia}</dd>
								</c:if>
								<c:if test="${contingut.document}">
									<dt><spring:message code="contingut.info.nti.identificador"/></dt>
									<dd style="overflow:hidden;text-overflow:ellipsis" title="${contingut.ntiIdentificador}">${contingut.ntiIdentificador}</dd>
									<dt><spring:message code="contingut.info.nti.organ"/></dt>
									<dd>${contingut.ntiOrganoDescripcio}</dd>
									<dt><spring:message code="contingut.info.nti.data.captura"/></dt>
									<dd><fmt:formatDate value="${contingut.dataCaptura}" pattern="dd/MM/yyyy"/></dd>
									<dt><spring:message code="contingut.info.nti.origen"/></dt>
									<dd><spring:message code="document.nti.origen.enum.${contingut.ntiOrigen}"/></dd>
									<dt><spring:message code="contingut.info.nti.estat.elab"/></dt>
									<dd><spring:message code="document.nti.estela.enum.${contingut.ntiEstadoElaboracion}"/></dd>
									<dt><spring:message code="contingut.info.nti.tipus.doc"/></dt>
									<dd><spring:message code="document.nti.tipdoc.enum.${contingut.ntiTipoDocumental}"/></dd>
									<c:if test="${not empty contingut.ntiIdDocumentoOrigen}">
										<dt><spring:message code="contingut.info.nti.doc.origen.id"/></dt>
										<dd>${contingut.ntiIdDocumentoOrigen}</dd>
									</c:if>
								</c:if>
							</dl>
						</div>
					</c:if>
					<c:if test="${not empty relacionats}">
						<h4 id="expedient-info-relacionats" style="padding-bottom: 0 !important;margin-bottom: 4px !important; border-bottom: 1px solid #e3e3e3">
							<spring:message code="contingut.info.relacionats"/>
						</h4>
						<ul class="list-unstyled">
							<c:forEach var="expedientRelacionat" items="${relacionats}">
								<c:if test="${!expedientRelacionat.esborrat}">
									<li>
										<span class="fa ${iconaExpedientObert}"></span>
										<a href="${expedientRelacionat.id}">
											[${expedientRelacionat.sequencia}/${expedientRelacionat.any}] 
											${expedientRelacionat.nom} 
										</a>
										<c:if test="${potModificarContingut}">
											<a href="../expedient/${contingut.id}/relacio/${expedientRelacionat.id}/delete" class="btn btn-default btn-xs" data-confirm="<spring:message code="contingut.info.relacio.esborrar.confirm"/>" style="float: right;">
												<span class="fa fa-trash-o"></span>
											</a> 
										</c:if>
									</li>
								</c:if>
							</c:forEach>
						</ul>
					</c:if>
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
				<li class="active"><a href="#contingut" data-toggle="tab">
					<spring:message code="contingut.tab.contingut"/>&nbsp;<span class="badge">${contingut.fillsNoRegistresCount}</span></a>
				</li>
				<c:if test="${contingut.document or contingut.expedient}">
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
				<c:if test="${contingut.document and fn:length(contingut.versions) gt 0}">
					<li>
						<a href="#versions" data-toggle="tab"><spring:message code="contingut.tab.versions"/>&nbsp;<span class="badge" id="versions-count">${fn:length(contingut.versions)}</span></a>
					</li>
				</c:if>
			</ul>
			<%--           --%>
			<%-- /Pipelles --%>
			<%--           --%>
			<div class="tab-content">
				<div class="tab-pane active in" id="contingut">
					<%--                   --%>
					<%-- Pipella contingut --%>
					<%--                   --%>
					<c:choose>
						<c:when test="${contingut.document}">
							<table class="table table-bordered">
								<tbody>
									<tr>
										<td><strong><spring:message code="contingut.document.camp.tipus"/></strong></td>
										<c:if test="${contingut.documentTipus == 'DIGITAL'}">
											<td><span class="label label-default"><span class="fa fa-save"></span> <spring:message code="document.tipus.enum.DIGITAL"/></span></td>
										</c:if>
										<c:if test="${contingut.documentTipus == 'FISIC'}">
											<td><span class="label label-default"><span class="fa fa-book"></span> <spring:message code="document.tipus.enum.FISIC"/></span></td>
										</c:if>
									</tr>
									<c:choose>
										<c:when test="${contingut.documentTipus != 'FISIC'}">
											<tr>
												<td>
													<c:choose>
														<c:when test="${not contingut.custodiat}">
															<strong><spring:message code="contingut.document.camp.nom"/></strong>
														</c:when>
														<c:otherwise>
															<strong><spring:message code="contingut.document.camp.nom.original"/></strong>
														</c:otherwise>
													</c:choose>
												</td>
												<td>${contingut.fitxerNom}</td>
											</tr>
											<tr>
												<td>
													<c:choose>
														<c:when test="${not contingut.custodiat}">
															<strong><spring:message code="contingut.document.camp.content.type"/></strong>
														</c:when>
														<c:otherwise>
															<strong><spring:message code="contingut.document.camp.content.type.original"/></strong>
														</c:otherwise>
													</c:choose>
												</td>
												<td>${contingut.fitxerContentType}</td>
											</tr>
										</c:when>
										<c:otherwise>
											<tr>
												<td><strong><spring:message code="contingut.document.camp.ubicacio"/></strong></td>
												<td>${contingut.ubicacio}</td>
											</tr>
										</c:otherwise>
									</c:choose>
								</tbody>
							</table>
							<c:if test="${contingut.custodiat}">
								<div class="panel panel-default">
									<div class="panel-heading">
										<h3 class="panel-title">
											<span class="fa fa-bookmark" title="Document firmat"></span>
											<spring:message code="contingut.document.info.firma"/>
										</h3>
									</div>
									<table class="table table-bordered">
										<tbody>
											<tr>
												<td><strong><spring:message code="contingut.document.camp.firma.tipus"/></strong></td>
												<td>
													<c:if test="${not empty contingut.ntiTipoFirma}">
														<spring:message code="document.nti.tipfir.enum.${contingut.ntiTipoFirma}"/>
													</c:if>
												</td>
											</tr>
											<tr>
												<td><strong><spring:message code="contingut.document.camp.firma.csv"/></strong></td>
												<td>${contingut.ntiCsv}</td>
											</tr>
											<tr>
												<td><strong><spring:message code="contingut.document.camp.firma.csv.regulacio"/></strong></td>
												<td>${contingut.ntiCsvRegulacion}</td>
											</tr>
											<%--tr>
												<td><strong><spring:message code="contingut.document.camp.firma.custodia.data"/></strong></td>
												<td><fmt:formatDate value="${contingut.custodiaData}" pattern="dd/MM/yyyy HH:mm:ss"/></td>
											</tr--%>
										</tbody>
									</table>
								</div>
							</c:if>
							<c:if test="${contingut.documentTipus != 'FISIC'}">
								<c:choose>
									<c:when test="${not contingut.custodiat}">
										<a href="../contingut/${contingut.id}/document/${contingut.id}/descarregar" class="btn btn-default pull-right"><span class="fa fa-download"></span>&nbsp;<spring:message code="comu.boto.descarregar"/></a>
									</c:when>
									<c:otherwise>
										<a href="../contingut/${contingut.id}/document/${contingut.id}/descarregar" class="btn btn-default pull-right"><span class="fa fa-download"></span>&nbsp;<spring:message code="contingut.document.descarregar.firmat"/></a>
									</c:otherwise>
								</c:choose>
							</c:if>
						</c:when>
						<c:otherwise>
							<div class="text-right" id="contingut-botons">
								<div class="btn-group">
									<a href="../contingut/${contingut.id}/canviVista/icones" class="btn btn-default<c:if test="${vistaIcones}"> active</c:if>">
										<span class="fa fa-th"></span>
									</a>
									<a href="../contingut/${contingut.id}/canviVista/llistat" class="btn btn-default<c:if test="${vistaLlistat}"> active</c:if>">
										<span class="fa fa-th-list"></span>
									</a>
								</div>
								<c:if test="${contingut.escriptori}">
									<div id="boto-accions-massives" class="btn-group">
										<button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
											<span class="fa fa-cogs"></span>&nbsp;
											<spring:message code="contingut.boto.accions.massives"/>&nbsp;
											<span class="caret"></span>
										</button>
										<ul class="dropdown-menu text-left" role="menu">
											<li>
												<a href="<c:url value="/massiu/portafirmes"/>">
													<span class="fa fa-envelope"></span>
													<spring:message code="contingut.boto.accio.massiva.portafirmes"/>
												</a>
											</li>
										</ul>
									</div>
								</c:if>
								<c:if test="${(contingut.escriptori or not empty contingut.escriptoriPare) and (contingut.escriptori or contingut.carpeta or ((contingut.expedient or contingut.document) and potModificarContingut))}">
									<div id="botons-crear-contingut" class="btn-group">
										<button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown"><span class="fa fa-plus"></span>&nbsp;<spring:message code="contingut.boto.crear.contingut"/>&nbsp;<span class="caret"></span></button>
										<ul class="dropdown-menu text-left" role="menu">
											<c:if test="${contingut.crearExpedients and not empty metaExpedients}">
												<li><a href="../contingut/${contingut.id}/expedient/new" data-toggle="modal" data-refresh-pagina="true"><span class="fa ${iconaExpedientTancat}"></span>&nbsp;<spring:message code="contingut.boto.crear.expedient"/>...</a></li>
											</c:if>
											<li><a href="../contingut/${contingut.id}/document/new" data-toggle="modal" data-refresh-pagina="true"><span class="fa ${iconaDocument}"></span>&nbsp;&nbsp;<spring:message code="contingut.boto.crear.document"/>...</a></li>
											<li><a href="../contingut/${contingut.id}/carpeta/new" data-toggle="modal" data-refresh-pagina="true"><span class="fa ${iconaCarpeta}"></span>&nbsp;&nbsp;<spring:message code="contingut.boto.crear.carpeta"/>...</a></li>
										</ul>
									</div>
								</c:if>
							</div>
							<rip:blocContenidorContingut contingut="${contingut}" mostrarExpedients="${true}" mostrarNoExpedients="${true}"/>
						</c:otherwise>
					</c:choose>
					<%--                    --%>
					<%-- /Pipella contingut --%>
					<%--                    --%>
				</div>
				<c:if test="${contingut.node}">
					<div class="tab-pane" id="dades">
						<%--               --%>
						<%-- Pipella dades --%>
						<%--               --%>
						<c:choose>
							<c:when test="${not empty metaDades}">
								<form:form id="nodeDades" commandName="dadesCommand" cssClass="form-inline">
									<button type="submit" class="btn btn-default pull-right" style="margin-bottom: 6px"><span class="fa fa-save"></span> <spring:message code="comu.boto.guardar"/></button>
									<table class="table table-striped table-bordered" style="width:100%">
									<thead>
										<tr>
											<th><spring:message code="contingut.dades.columna.dada"/></th>
											<th width="340"><spring:message code="contingut.dades.columna.valor"/></th>
										</tr>
									</thead>
									<tbody>
										<c:forEach var="metaNodeMetaDada" items="${metaDades}">
											<c:set var="dadaValor"></c:set>
											<c:forEach var="dada" items="${contingut.dades}">
												<c:if test="${dada.metaDada.codi == metaNodeMetaDada.metaDada.codi}">
													<c:set var="dadaValor">${dada.valorMostrar}</c:set>
												</c:if>
											</c:forEach>
											<c:set var="isMultiple" value="${metaNodeMetaDada.multiplicitat == 'M_0_N' or metaNodeMetaDada.multiplicitat == 'M_1_N'}"/>
											<c:set var="multipleClass" value=""/>
											<c:if test="${isMultiple}"><c:set var="multipleClass" value=" multiple"/></c:if>
											<tr>
												<td>${metaNodeMetaDada.metaDada.nom}</td>
												<td>
													<div class="form-group"<c:if test="${isMultiple}"> data-toggle="multifield" data-nou="true"</c:if>>
														<label class="hidden" for="${metaNodeMetaDada.metaDada.codi}"></label>
														<div>
															<c:choose>
																<c:when test="${metaNodeMetaDada.metaDada.tipus == 'DATA'}">
																	<form:input path="${metaNodeMetaDada.metaDada.codi}" id="${metaNodeMetaDada.metaDada.codi}" data-toggle="datepicker" data-idioma="${requestLocale}" cssClass="form-control text-right${multipleClass}"></form:input>
																</c:when>
																<c:when test="${metaNodeMetaDada.metaDada.tipus == 'IMPORT'}">
																	<form:input path="${metaNodeMetaDada.metaDada.codi}" id="${metaNodeMetaDada.metaDada.codi}" data-toggle="autonumeric" data-a-dec="," data-a-sep="." data-m-dec="2" class="form-control text-right${multipleClass}"></form:input>
																</c:when>
																<c:when test="${metaNodeMetaDada.metaDada.tipus == 'SENCER'}">
																	<form:input path="${metaNodeMetaDada.metaDada.codi}" id="${metaNodeMetaDada.metaDada.codi}" data-toggle="autonumeric" data-a-dec="," data-a-sep="" data-m-dec="0" class="form-control text-right${multipleClass}"></form:input>
																</c:when>
																<c:when test="${metaNodeMetaDada.metaDada.tipus == 'FLOTANT'}">
																	<form:input path="${metaNodeMetaDada.metaDada.codi}" id="${metaNodeMetaDada.metaDada.codi}" data-toggle="autonumeric" data-a-dec="," data-a-sep="" data-m-dec="10" data-a-pad="false" class="form-control text-right${multipleClass}"></form:input>
																</c:when>
																<c:when test="${metaNodeMetaDada.metaDada.tipus == 'BOOLEA'}">
																	<form:checkbox path="${metaNodeMetaDada.metaDada.codi}" id="${metaNodeMetaDada.metaDada.codi}" name="${metaNodeMetaDada.metaDada.codi}"></form:checkbox>
																</c:when>
																<c:otherwise>
																	<form:input path="${metaNodeMetaDada.metaDada.codi}" id="${metaNodeMetaDada.metaDada.codi}" cssClass="form-control${multipleClass}"></form:input>
																</c:otherwise>
															</c:choose>
															<span class="" aria-hidden="true"></span>
														</div>
													</div>
												</td>
											</tr>
										</c:forEach>
									</tbody>
									</table>
									<button type="submit" class="btn btn-default pull-right" style="margin-top: -14px"><span class="fa fa-save"></span> <spring:message code="comu.boto.guardar"/></button>
								</form:form>
							</c:when>
							<c:otherwise>
							</c:otherwise>
						</c:choose>
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
									<th data-col-name="id" data-orderable="false" data-template="#cellAccionsInteressatTemplate" width="10%">
										<script id="cellAccionsInteressatTemplate" type="text/x-jsrender">
											<div class="dropdown">
												<button class="btn btn-primary" data-toggle="dropdown"><span class="fa fa-cog"></span>&nbsp;<spring:message code="comu.boto.accions"/>&nbsp;<span class="caret"></span></button>
												<ul class="dropdown-menu">
													<li><a href="../expedient/${contingut.id}/interessat/{{:id}}" data-toggle="modal"><span class="fa fa-pencil"></span>&nbsp;&nbsp;<spring:message code="comu.boto.modificar"/></a></li>
													<li><a href="../expedient/${contingut.id}/interessat/{{:id}}/delete" data-toggle="ajax" data-confirm="<spring:message code="contingut.confirmacio.esborrar.interessat"/>"><span class="fa fa-trash-o"></span>&nbsp;&nbsp;<spring:message code="comu.boto.esborrar"/></a></li>
													{{if tipus != '<%=es.caib.ripea.core.api.dto.InteressatTipusEnumDto.ADMINISTRACIO%>'}}
														<li class="divider" role="separator"></li>
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
						<table
							id="taulaEnviaments"
							data-toggle="datatable"
							data-url="<c:url value="/expedient/${contingut.id}/enviament/datatable"/>"
							data-paging-enabled="false"
							data-agrupar="5"
							class="table table-bordered table-striped"
							style="width:100%">
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
									<th data-col-name="destinatari" data-orderable="false" data-visible="false" width="20%">
										<spring:message code="contingut.enviament.columna.destinatari"/>
									</th>
									<th data-col-name="estat" data-template="#cellEnviamentEstatTemplate" data-orderable="false" width="10%">
										<spring:message code="contingut.enviament.columna.estat"/>
										<script id="cellEnviamentEstatTemplate" type="text/x-jsrender">
											{{if notificacio}}
												{{if estat == 'PENDENT'}}
													<span class="label label-warning"><span class="fa fa-clock-o"></span> {{:~eval('notificacioEstatText["' + estat + '"]')}}</span>
												{{else estat == 'ENVIAT_OK'}}
													<span class="label label-info"><span class="fa fa-envelope-o"></span> {{:~eval('notificacioEstatText["' + estat + '"]')}}</span>
												{{else estat == 'ENVIAT_ERROR'}}
													<span class="label label-danger"><span class="fa fa-warning"></span> {{:~eval('notificacioEstatText["' + estat + '"]')}}</span>
												{{else estat == 'PROCESSAT_OK'}}
													<span class="label label-success"><span class="fa fa-check"></span> {{:~eval('notificacioEstatText["' + estat + '"]')}}</span>
												{{else estat == 'PROCESSAT_REBUTJAT'}}
													<span class="label label-default"><span class="fa fa-times"></span> {{:~eval('notificacioEstatText["' + estat + '"]')}}</span>
												{{else estat == 'PROCESSAT_ERROR'}}
													<span class="label label-danger"><span class="fa fa-warning"></span> {{:~eval('notificacioEstatText["' + estat + '"]')}}</span>
												{{/if}}
											{{else publicacio}}
												{{if estat == 'PENDENT'}}
													<span class="label label-warning"><span class="fa fa-clock-o"></span> {{:~eval('publicacioEstatText["' + estat + '"]')}}</span>
												{{else estat == 'ENVIAT_OK'}}
													<span class="label label-info"><span class="fa fa-envelope-o"></span> {{:~eval('publicacioEstatText["' + estat + '"]')}}</span>
												{{else estat == 'PROCESSAT_REBUTJAT'}}
													<span class="label label-default"><span class="fa fa-times"></span> {{:~eval('publicacioEstatText["' + estat + '"]')}}</span>
												{{else estat == 'PUBLICAT'}}
													<span class="label label-danger"><span class="fa fa-check"></span> {{:~eval('publicacioEstatText["' + estat + '"]')}}</span>
												{{/if}}
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
				<c:if test="${contingut.document and contingut.versioCount gt 0}">
					<div class="tab-pane" id="versions">
						<%--                  --%>
						<%-- Pipella versions --%>
						<%--                  --%>
						<div class="tab-pane" id="contingut">
							<div id="document-versions" class="panel-group" id="accordion">
								<table class="table table-bordered table-striped">
								<thead>
									<tr>
										<th>Id</th>
										<th>Data</th>
										<c:if test="${contingut.documentTipus != 'FISIC'}">
											<th width="1%"></th>
										</c:if>
									</tr>
								</thead>
								<tbody>
								<c:forEach var="versio" items="${contingut.versions}">
									<tr>
										<td>${versio.id}</td>
										<td><fmt:formatDate value="${versio.data}" pattern="dd/MM/yyyy HH:mm:ss"/></td>
										<c:if test="${contingut.documentTipus != 'FISIC'}">
											<td>
												<a href="../contingut/${contingut.id}/document/${contingut.id}/versio/${versio.id}/descarregar" class="btn btn-default">
													<span class="fa fa-download"></span>&nbsp;
													<spring:message code="comu.boto.descarregar"/>
												</a>
											</td>
										</c:if>
									</tr>
								</c:forEach>
								</tbody>
								</table>
							</div>
						</div>
						<%--                   --%>
						<%-- /Pipella versions --%>
						<%--                   --%>
					</div>
				</c:if>
			</div>
		</div>
	</div>
</body>
</html>
