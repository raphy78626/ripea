<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib tagdir="/WEB-INF/tags/ripea" prefix="rip"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%
pageContext.setAttribute(
		"edicioOnlineActiva",
		new Boolean(false));
pageContext.setAttribute(
		"registreAccioEnumOptions",
		es.caib.ripea.war.helper.HtmlSelectOptionHelper.getOptionsForEnum(
				es.caib.ripea.core.api.dto.RegistreAccioEnumDto.class,
				"registre.anotacio.accio.enum."));
/*pageContext.setAttribute(
		"registreTipusEnumOptions",
		es.caib.ripea.war.helper.HtmlSelectOptionHelper.getOptionsForEnum(
				es.caib.ripea.core.api.dto.RegistreTipusEnumDto.class,
				"registre.anotacio.tipus.enum."));*/
%>
<c:set var="potModificarContingut" value="${false}"/>
<c:if test="${contenidor.node}"><c:set var="potModificarContingut" value="${empty contenidor.metaNode or contenidor.metaNode.usuariActualWrite}"/></c:if>
<c:set var="htmlIconaCarpeta6em"><span class="fa-stack" style="font-size:.6em"><i class="fa fa-folder fa-stack-2x"></i><i class="fa fa-clock-o fa-stack-1x fa-inverse"></i></span></c:set>
<html>
<head>
	<title>
		<c:choose>
			<c:when test="${contenidor.escriptori}">
				<c:choose>
					<c:when test="${paginaExpedients}"><c:set var="titleIconClass" value="list-alt"/>&nbsp;<spring:message code="contenidor.contingut.titol.expedients"/></c:when>
					<c:otherwise><c:set var="titleIconClass" value="desktop"/>&nbsp;<spring:message code="contenidor.contingut.titol.escriptori"/></c:otherwise>
				</c:choose>
			</c:when>
			<c:when test="${contenidor.expedient}"><c:set var="titleIconClass" value="briefcase"/>&nbsp;${contenidor.nom}</c:when>
			<c:when test="${contenidor.carpeta}"><c:set var="titleIconClass" value="folder"/>&nbsp;${contenidor.nom}</c:when>
			<c:when test="${contenidor.document}"><c:set var="titleIconClass" value="file"/>&nbsp;${contenidor.nom}</c:when>
			<c:when test="${contenidor.bustia}"><c:set var="titleIconClass" value="inbox"/>&nbsp;${contenidor.nom}</c:when>
		</c:choose>
	</title>
	<c:if test="${not empty titleIconClass}"><meta name="title-icon-class" content="fa fa-${titleIconClass}"/></c:if>
	<meta name="subtitle" content="${serveiPerTitol}"/>
	<link href="<c:url value="/css/DT_bootstrap.css"/>" rel="stylesheet">
	<script src="<c:url value="/js/jquery.dataTables.js"/>"></script>
	<script src="<c:url value="/js/DT_bootstrap.js"/>"></script>
	<script src="<c:url value="/js/jsrender.min.js"/>"></script>
	<script src="<c:url value="/js/ripea.datatable.js"/>"></script>
	<script src="<c:url value="/js/ripea.modal.js"/>"></script>
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
</style>
<c:if test="${edicioOnlineActiva and contenidor.document and not empty contenidor.escriptoriPare}">
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
			alert("<spring:message code="contenidor.contingut.applet.no.trobat"/>");
		}
		return false;
	});
});
</script>
</c:if>
<script>
var registreAccioText = new Array();
<c:forEach var="option" items="${registreAccioEnumOptions}">
registreAccioText["${option.value}"] = "<spring:message code="${option.text}"/>";
</c:forEach>
var registreTipusText = new Array();
<c:forEach var="option" items="${registreTipusEnumOptions}">
registreTipusText["${option.value}"] = "<spring:message code="${option.text}"/>";
</c:forEach>
$(document).ready(function() {
	<c:if test="${contenidor.carpeta and contenidor.tipus == 'ESBORRANY'}">
		$('.container-main .panel-heading h2 span.fa').replaceWith('<rip:blocIconaCarpeta carpeta="${contenidor}" petita="${true}"/>');
	</c:if>
	$('#contenidor-contingut li').mouseover(function() {
		$('a.btn', this).removeClass('hidden');
	});
	$('#contenidor-contingut li').mouseout(function() {
		$('a.btn', this).addClass('hidden');
	});
	$('#contenidor-info li a.confirm-delete').click(function() {
		return confirm('<spring:message code="contenidor.contingut.confirmacio.esborrar.node"/>');
	});
	$('#contenidor-contingut li a.confirm-delete').click(function() {
		return confirm('<spring:message code="contenidor.contingut.confirmacio.esborrar.node"/>');
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
		return confirm('<spring:message code="contenidor.contingut.confirmacio.esborrar.interessat"/>');
	});
	$("#taulaDades").ripeaDataTable({
		ajaxSourceUrl: "<c:url value="/contenidor/${contenidor.id}/dada/datatable"/>",
		drawCallback: function() {
			if ($('#taulaDades tbody tr td:eq(0)').hasClass('dataTables_empty'))
				$('#dades-count').html('0');
			else
				$('#dades-count').html($('#taulaDades tbody tr').length);
		},
		serverParamsCallback: function() {
			$('#dataTables_new').html('');
			<c:if test="${potModificarContingut}">$.ajax({
				type: "GET",
				url: "../contenidor/${contenidor.id}/llistaMetaDadesCrear",
				timeout: 20000,
				async: false,
				success: function(data) {
					if (data.length > 0)
						$('#dataTables_new').html('<p style="text-align:right"><a href="../contenidor/${contenidor.id}/dada/new" class="btn btn-default" data-rdt-link-modal="true"><span class="fa fa-plus"></span>&nbsp;<spring:message code="contenidor.contingut.boto.nova.dada"/></a></p>');
				},
				error: function (jqXHR, exception) {
					if (jqXHR.status === 0) {
		                alert('Not connected.\n Verify Network.');
		            } else if (jqXHR.status == 404) {
		                alert('Requested page not found. [404]');
		            } else if (jqXHR.status == 500) {
		                alert('Internal Server Error [500].');
		            } else if (exception === 'parsererror') {
		                alert('Requested JSON parse failed.');
		            } else if (exception === 'timeout') {
		                alert('Time out error.');
		            } else if (exception === 'abort') {
		                alert('Ajax request aborted.');
		            } else {
		                alert('Uncaught Error.\n' + jqXHR.responseText);
		            }
				}
		    });</c:if>
		},
		localeUrl: "<c:url value="/js/dataTables-locales/dataTables_locale_ca.txt"/>",
		alertesRefreshUrl: "<c:url value="/nodeco/util/alertes"/>"
	});
	$("#taulaRegistres").ripeaDataTable({
		ajaxSourceUrl: "<c:url value="/contenidor/${contenidor.id}/registre/datatable"/>",
		localeUrl: "<c:url value="/js/dataTables-locales/dataTables_locale_ca.txt"/>",
		alertesRefreshUrl: "<c:url value="/nodeco/util/alertes"/>"
	});
	$('#botons-crear-contingut a').ripeaEvalLink({
		refrescarAlertes: false,
		refrescarPagina: true,
	});
	$('#contenidor-info a').ripeaEvalLink({
		refrescarAlertes: false,
		refrescarPagina: true,
	});
	$('.botons-accions-element a').ripeaEvalLink({
		refrescarAlertes: false,
		refrescarPagina: true,
	});
	$('#botons-errors-validacio a').ripeaEvalLink({
		refrescarAlertes: false,
		refrescarPagina: true,
	});
	$('#document-versions a').ripeaEvalLink({
		refrescarAlertes: false,
		refrescarPagina: true,
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
	<rip:blocContenidorPath contenidor="${contenidor}"/>
	<div class="row">
		<c:set var="contingutClass">col-md-12</c:set>
		<c:if test="${contenidor.expedient or contenidor.carpeta or contenidor.document}">
			<c:set var="contingutClass">col-md-9</c:set>
			<div class="col-md-3">
				<%--                    --%>
				<%-- Columna informació --%>
				<%--                    --%>
				<div id="contenidor-info" class="well">
					<h3>
						<spring:message code="contenidor.contingut.info.titol"/>
						<c:if test="${contenidor.expedient}">
							<a href="../contenidor/${contenidor.id}/nti" class="btn btn-info btn-xs" data-rdt-link-modal="true">NTI</a>
						</c:if>
					</h3>
					<dl>
						<c:if test="${contenidor.expedient}">
							<dt><spring:message code="contenidor.contingut.info.numero"/></dt>
							<dd>${contenidor.sequencia}/${contenidor.any}</dd>
						</c:if>
						<c:if test="${contenidor.carpeta or contenidor.node and not empty contenidor.metaNode}">
							<dt><spring:message code="contenidor.contingut.info.tipus"/></dt>
							<c:choose>
								<c:when test="${contenidor.expedient or contenidor.document}">
									<dd>${contenidor.metaNode.nom}</dd>
								</c:when>
								<c:otherwise>
									<dd><spring:message code="carpeta.tipus.enum.${contenidor.tipus}"/></dd>
								</c:otherwise>
							</c:choose>
						</c:if>
						<dt><spring:message code="contenidor.contingut.info.createl"/></dt>
						<dd><fmt:formatDate value="${contenidor.createdDate}" pattern="dd/MM/yyyy HH:mm"/></dd>
						<dt><spring:message code="contenidor.contingut.info.creatper"/></dt>
						<dd>${contenidor.createdBy.nom}</dd>
						<c:if test="${contenidor.expedient}">
							<dt><spring:message code="contenidor.contingut.info.arxiu"/></dt>
							<dd>${contenidor.arxiu.nom}</dd>
							<dt><spring:message code="contenidor.contingut.info.estat"/></dt>
							<dd>
								<c:choose>
									<c:when test="${contenidor.obert}"><spring:message code="contenidor.contingut.info.estat.obert"/></c:when>
									<c:otherwise><spring:message code="contenidor.contingut.info.estat.tancat"/></c:otherwise>
								</c:choose>
							</dd>
						</c:if>
						<c:if test="${contenidor.document}">
							<dt><spring:message code="contenidor.contingut.info.data"/></dt>
							<dd><fmt:formatDate value="${contenidor.data}" pattern="dd/MM/yyyy"/></dd>
							<dt><spring:message code="contenidor.contingut.info.versio"/></dt>
							<dd>${contenidor.darreraVersio.versio}</dd>
						</c:if>
					</dl>
					<c:if test="${contenidor.expedient}">
						<h4 class="interessats">
							<spring:message code="contenidor.contingut.info.interessats"/>
							<c:if test="${potModificarContingut}"><a href="../expedient/${contenidor.id}/interessat/new" class="btn btn-link btn-sm" title="<spring:message code="comu.boto.afegir"/>"><span class="fa fa-plus"></span></a></c:if>
						</h4>
						<c:if test="${not empty interessats}">
							<ul class="list-unstyled interessats">
								<c:forEach var="interessat" items="${interessats}">
									<li>
										<c:if test="${interessat.ciutada}">
											<span class="fa fa-user" title="<spring:message code="contenidor.contingut.info.interessat.tipus.ciutada"/>"></span>&nbsp;<span title="NIF: ${interessat.nif}">${interessat.nom} ${interessat.llinatges}</span>
										</c:if>
										<c:if test="${interessat.administracio}">
											<span class="fa fa-institution" title="<spring:message code="contenidor.contingut.info.interessat.tipus.administracio"/>"></span>&nbsp;<span title="ID: ${interessat.identificador}">${interessat.nom}</span>
										</c:if>
										<c:if test="${potModificarContingut}"><a href="../expedient/${contenidor.id}/interessat/${interessat.id}/delete" class="hidden interessat-esborrar" title="<spring:message code="comu.boto.esborrar"/>"><span class="fa fa-minus"></span></a></c:if>
										<div class="clearfix"></div>
									</li>
								</c:forEach>
							</ul>
						</c:if>
					</c:if>
					<rip:blocContenidorAccions id="botons-accions-info" contenidor="${contenidor}" modeLlistat="true" mostrarObrir="false"/>
				</div>
				<%--                     --%>
				<%-- /Columna informació --%>
				<%--                     --%>
			</div>
		</c:if>
		<div class="${contingutClass}">
			<c:if test="${contenidor.node and not contenidor.valid}">
				<div id="botons-errors-validacio" class="alert well-sm alert-warning alert-dismissable">
					<span class="fa fa-exclamation-triangle"></span>
					<c:choose>
						<c:when test="${contenidor.expedient}"><spring:message code="contenidor.contingut.errors.expedient"/></c:when>
						<c:when test="${contenidor.document}"><spring:message code="contenidor.contingut.errors.document"/></c:when>
					</c:choose>
					<a href="../contenidor/${contenidor.id}/errors" class="btn btn-xs btn-default pull-right" data-rdt-link-modal="true"><spring:message code="contenidor.contingut.errors.mesinfo"/></a>
				</div>
			</c:if>
			<%--          --%>
			<%-- Pipelles --%>
			<%--          --%>
			<ul class="nav nav-tabs">
				<c:choose>
					<c:when test="${contenidor.document}">
						<li class="active"><a href="#contingut" data-toggle="tab">
							<spring:message code="contenidor.contingut.tab.versions"/>&nbsp;<span class="badge">${fn:length(documentVersions)}</span></a>
						</li>
					</c:when>
					<c:otherwise>
						<li class="active"><a href="#contingut" data-toggle="tab">
							<spring:message code="contenidor.contingut.tab.contingut"/>&nbsp;<span class="badge">${contenidor.fillsCount}</span></a>
						</li>
					</c:otherwise>
				</c:choose>
				<c:if test="${contenidor.node}">
					<li>
						<a href="#dades" data-toggle="tab"><spring:message code="contenidor.contingut.tab.dades"/>&nbsp;<span class="badge" id="dades-count">${contenidor.dadesCount}</span></a>
					</li>
				</c:if>
				<c:if test="${contenidor.expedient}">
					<li>
						<a href="#registres" data-toggle="tab"><spring:message code="contenidor.contingut.tab.registres"/>&nbsp;<span class="badge" id="registres-count">${contenidor.registresCount}</span></a>
					</li>
					<li>
						<a href="#nouvinguts" data-toggle="tab"><spring:message code="contenidor.contingut.tab.nouvinguts"/>&nbsp;<span class="badge" id="nouvinguts-count">${fn:length(nouvinguts)}</span></a>
					</li>
				</c:if>
			</ul>
			<%--           --%>
			<%-- /Pipelles --%>
			<%--           --%>
			<div class="tab-content">
				<c:choose>
					<c:when test="${contenidor.document}">
						<%--          --%>
						<%-- Document --%>
						<%--          --%>
						<div class="tab-pane active in" id="contingut">
							<div id="document-versions" class="panel-group" id="accordion">
								<c:forEach var="versio" items="${documentVersions}" varStatus="status">
									<div class="panel panel-default">
										<div class="panel-heading">
											<h4 class="panel-title">
												<a class="accordion-toggle" data-toggle="collapse" data-parent="#accordion" href="#collapse_${versio.id}">
													<spring:message code="contenidor.contingut.versions.versio"/> ${versio.versio}
												</a>
											</h4>
										</div>
										<div id="collapse_${versio.id}" class="panel-collapse collapse<c:if test="${status.first}"> in</c:if>">
											<div class="panel-body">
												<dl class="dl-horizontal">
													<dt><spring:message code="contenidor.contingut.versions.arxiu"/>:</dt>
													<dd>${versio.arxiuNom}</dd>
													<dt><spring:message code="contenidor.contingut.versions.createl"/>:</dt>
													<dd><fmt:formatDate value="${versio.createdDate}" pattern="dd/MM/yyyy HH:mm"/></dd>
													<c:if test="${versio.firmaIntentat}">
														<dt><spring:message code="contenidor.contingut.versions.firma.estat"/>:</dt>
														<dd>
															<c:choose>
																<c:when test="${versio.firmaError}">
																	<span class="label label-danger" title="${versio.portafirmesEnviamentDarrer.errorDescripcio}"><span class="fa fa-warning"></span>&nbsp;ERROR</span>
																</c:when>
																<c:otherwise><spring:message code="document.firma.estat.enum.${versio.firmaEstat}"/></c:otherwise>
															</c:choose>
														</dd>
													</c:if>
													<c:if test="${versio.custodiat}">
														<dt><spring:message code="contenidor.contingut.versions.custodia.url"/>:</dt>
														<dd><a href="${versio.custodiaUrl}" target="_blank">${versio.custodiaUrl}</a> <span class="fa fa-external-link"></span></dd>
													</c:if>
												</dl>
												<div class="btn-toolbar pull-right">
													<c:if test="${not empty contenidor.escriptoriPare and status.first}">
														<a href="../webdav${contenidor.pathAsString}/${contenidor.nom}/${contenidor.darreraVersio.arxiuNom}" class="btn btn-default btn-document-modificar"><span class="fa fa-pencil"></span>&nbsp;<spring:message code="contenidor.contingut.boto.link.dav"/></a>
													</c:if>
													<c:if test="${edicioOnlineActiva}">
														<c:if test="${not empty contenidor.escriptoriPare and status.first}">
															<a href="../webdav${contenidor.pathAsString}/${contenidor.nom}/${contenidor.darreraVersio.arxiuNom}" class="btn btn-default btn-document-modificar"><span class="fa fa-pencil"></span>&nbsp;<spring:message code="contenidor.contingut.boto.editar.office"/></a>
														</c:if>
													</c:if>
													<a href="../contenidor/${contenidor.id}/document/${contenidor.id}/versio/${versio.versio}/descarregar" class="btn btn-default"><span class="fa fa-download"></span>&nbsp;<spring:message code="comu.boto.descarregar"/></a>
													<c:if test="${not empty contenidor.metaNode and (contenidor.metaNode.firmaPortafirmesActiva or contenidor.metaNode.firmaPassarelaActiva)}">
														<div class="btn-group">
  															<button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown" aria-expanded="false">
																<spring:message code="contenidor.contingut.boto.firma.accions"/> <span class="caret"></span>
															</button>
															<ul class="dropdown-menu" role="menu">
																<c:if test="${contenidor.metaNode.firmaPortafirmesActiva}">
																	<c:choose>
																		<c:when test="${not versio.custodiat}">
																			<c:if test="${not versio.firmaEstatBloquejarEnviaments}">
																				<li><a href="../contenidor/${contenidor.id}/document/${contenidor.id}/versio/${versio.versio}/portafirmes/upload" data-rdt-link-modal="true"><span class="fa fa-send"></span>&nbsp;<spring:message code="contenidor.contingut.boto.portafirmes.enviar"/></a></li>
																			</c:if>
																			<c:if test="${versio.firmaEstatPendent}">
																				<li><a href="../contenidor/${contenidor.id}/document/${contenidor.id}/versio/${versio.versio}/portafirmes/cancel" data-rdt-link-confirm="<spring:message code="contenidor.contingut.boto.portafirmes.cancelar.confirm"/>"><span class="fa fa-times"></span>&nbsp;<spring:message code="contenidor.contingut.boto.portafirmes.cancelar"/></a></li>
																			</c:if>
																			<c:if test="${versio.firmaEstatBloquejarEnviaments}">
																				<li><a href="#" data-toggle="modal" data-target="#pfirma-info-${versio.id}"><span class="fa fa-info-circle"></span>&nbsp;<spring:message code="contenidor.contingut.boto.portafirmes.info"/></a></li>
																			</c:if>
																			<c:if test="${versio.firmaError}">
																				<li><a href="../contenidor/${contenidor.id}/document/${contenidor.id}/versio/${versio.versio}/custodia/reintentar"><span class="fa fa-repeat"></span>&nbsp;<spring:message code="contenidor.contingut.boto.custodia.reintentar"/></a></li>
																			</c:if>
																		</c:when>
																		<c:otherwise>
																			<li><a href="../contenidor/${contenidor.id}/document/${contenidor.id}/versio/${versio.versio}/custodia/esborrar"><span class="fa fa-remove"></span>&nbsp;<spring:message code="contenidor.contingut.boto.custodia.esborrar"/></a></li>
																		</c:otherwise>
																	</c:choose>
																</c:if>
																<c:if test="${contenidor.metaNode.firmaPassarelaActiva and not versio.firmaEstatBloquejarEnviaments}">
																	<c:choose>
																		<c:when test="${not versio.custodiat}">
																			<li><a href="../contenidor/${contenidor.id}/document/${contenidor.id}/versio/${versio.versio}/firmaPassarela" data-rdt-link-modal="true"><span class="fa fa-edit"></span>&nbsp;<spring:message code="contenidor.contingut.boto.firma.passarela"/></a></li>
																		</c:when>
																		<c:otherwise>
																			<li><a href="../contenidor/${contenidor.id}/document/${contenidor.id}/versio/${versio.versio}/custodia/esborrar"><span class="fa fa-remove"></span>&nbsp;<spring:message code="contenidor.contingut.boto.custodia.esborrar"/></a></li>
																		</c:otherwise>
																	</c:choose>
																</c:if>
															</ul>
															<c:if test="${contenidor.metaNode.firmaPortafirmesActiva and versio.firmaEstatBloquejarEnviaments}">
																<div id="pfirma-info-${versio.id}" class="modal fade">
																	<div class="modal-dialog">
																		<div class="modal-content">
																			<div class="modal-header">
																				<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only"><spring:message code="comu.boto.tancar"/></span></button>
																				<h4 class="modal-title"><spring:message code="contenidor.contingut.boto.portafirmes.info.modal.titol"/></h4>
																			</div>
																			<div class="modal-body">
																				<c:if test="${versio.firmaError}">
																					<div class="alert well-sm alert-danger">
																						<span class="fa fa-exclamation-triangle"></span>
																						${versio.portafirmesEnviamentDarrer.errorDescripcio}
																					</div>
																				</c:if>
																				<dl class="dl-horizontal">
																					<dt><spring:message code="contenidor.contingut.boto.portafirmes.info.modal.motiu"/>:</dt>
																					<dd>${versio.portafirmesEnviamentDarrer.motiu}</dd>
																					<dt><spring:message code="contenidor.contingut.boto.portafirmes.info.modal.prioritat"/>:</dt>
																					<dd>${versio.portafirmesEnviamentDarrer.prioritat}</dd>
																					<dt><spring:message code="contenidor.contingut.boto.portafirmes.info.modal.enviament"/>:</dt>
																					<dd><fmt:formatDate value="${versio.portafirmesEnviamentDarrer.dataEnviament}" pattern="dd/MM/yyyy HH:mm:ss"/></dd>
																					<dt><spring:message code="contenidor.contingut.boto.portafirmes.info.modal.caducitat"/>:</dt>
																					<dd><fmt:formatDate value="${versio.portafirmesEnviamentDarrer.dataCaducitat}" pattern="dd/MM/yyyy"/></dd>
																					<dt><spring:message code="contenidor.contingut.boto.portafirmes.info.modal.id"/>:</dt>
																					<dd>${versio.portafirmesEnviamentDarrer.portafirmesId}</dd>
																					<dt><spring:message code="contenidor.contingut.boto.portafirmes.info.modal.estat"/>:</dt>
																					<dd>${versio.portafirmesEnviamentDarrer.portafirmesEstat}</dd>
																					<c:if test="${versio.portafirmesEnviamentDarrer.callbackCount gt 0}">
																						<dt><spring:message code="contenidor.contingut.boto.portafirmes.info.modal.rebuts"/>:</dt>
																						<dd>${versio.portafirmesEnviamentDarrer.callbackCount}</dd>
																						<dt><spring:message code="contenidor.contingut.boto.portafirmes.info.modal.darrer"/>:</dt>
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
									<a href="../contenidor/${contenidor.id}/canviVista/icones" class="btn btn-default<c:if test="${vistaIcones}"> active</c:if>">
										<span class="fa fa-th"></span>
									</a>
									<a href="../contenidor/${contenidor.id}/canviVista/llistat" class="btn btn-default<c:if test="${vistaLlistat}"> active</c:if>">
										<span class="fa fa-th-list"></span>
									</a>
								</div>
								<c:if test="${(contenidor.escriptori or not empty contenidor.escriptoriPare) and (contenidor.escriptori or contenidor.carpeta or ((contenidor.expedient or contenidor.document) and potModificarContingut))}">
									<div id="botons-crear-contingut" class="btn-group">
										<button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown"><span class="fa fa-plus"></span>&nbsp;<spring:message code="contenidor.contingut.boto.crear.contingut"/>&nbsp;<span class="caret"></span></button>
										<ul class="dropdown-menu text-left" role="menu">
											<li><a href="../contenidor/${contenidor.id}/carpeta/new" data-rdt-link-modal="true"><span class="fa fa-folder"></span>&nbsp;&nbsp;<spring:message code="contenidor.contingut.boto.crear.carpeta"/>...</a></li>
											<c:if test="${contenidor.crearDocumentsGenerics or not empty metaDocuments}">
												<li><a href="../contenidor/${contenidor.id}/document/new" data-rdt-link-modal="true"><span class="fa fa-file"></span>&nbsp;&nbsp;<spring:message code="contenidor.contingut.boto.crear.document"/>...</a></li>
											</c:if>
											<c:if test="${contenidor.crearExpedients and not empty metaExpedients}">
												<li><a href="../contenidor/${contenidor.id}/expedient/new" data-rdt-link-modal="true"><span class="fa fa-briefcase"></span>&nbsp;<spring:message code="contenidor.contingut.boto.crear.expedient"/>...</a></li>
											</c:if>
										</ul>
									</div>
								</c:if>
  							</div>
							<rip:blocContenidorContingut contenidor="${contenidor}" mostrarExpedients="${true}" mostrarNoExpedients="${true}"/>
							<%--                    --%>
							<%-- /Pipella contingut --%>
							<%--                    --%>
						</div>
					</c:otherwise>
				</c:choose>
				<c:if test="${contenidor.node}">
					<div class="tab-pane" id="dades">
						<%--               --%>
						<%-- Pipella dades --%>
						<%--               --%>
						<table id="taulaDades" class="table table-striped table-bordered" data-rdt-paginable="false">
							<thead>
								<tr>
									<th data-rdt-property="id" data-rdt-visible="false" width="4%">#</th>
									<th data-rdt-property="metaDada.nom" data-rdt-sortable="false"><spring:message code="contenidor.contingut.dades.columna.dada"/></th>
									<th data-rdt-property="valorMostrar" data-rdt-sortable="false"><spring:message code="contenidor.contingut.dades.columna.valor"/></th>
									<c:if test="${not empty contenidor.escriptoriPare and potModificarContingut}">
										<th data-rdt-property="id" data-rdt-sortable="false" data-rdt-template="cellAccionsTemplate" width="10%">
											<script id="cellAccionsTemplate" type="text/x-jsrender">
												<div class="dropdown">
													<button class="btn btn-primary" data-toggle="dropdown"><span class="fa fa-cog"></span>&nbsp;<spring:message code="comu.boto.accions"/>&nbsp;<span class="caret"></span></button>
													<ul class="dropdown-menu">
														<li><a href="../contenidor/${contenidor.id}/dada/{{:id}}" data-rdt-link-modal="true"><span class="fa fa-pencil"></span>&nbsp;&nbsp;<spring:message code="comu.boto.modificar"/></a></li>
														<li><a href="../contenidor/${contenidor.id}/dada/{{:id}}/delete" data-rdt-link-ajax="true" data-rdt-link-confirm="<spring:message code="contenidor.contingut.confirmacio.esborrar.dada"/>"><span class="fa fa-trash-o"></span>&nbsp;&nbsp;<spring:message code="comu.boto.esborrar"/></a></li>
													</ul>
												</div>
											</script>
										</th>
									</c:if>
								</tr>
							</thead>
						</table>
						<script id="tableButtonsTemplate" type="text/x-jsrender">
							<p style="text-align:right"><a href="../contenidor/${contenidor.id}/dada/new" class="btn btn-default" data-rdt-link-modal="true"><span class="fa fa-plus"></span>&nbsp;<spring:message code="contenidor.contingut.boto.nova.dada"/></a></p>
						</script>
						<%--                --%>
						<%-- /Pipella dades --%>
						<%--                --%>
					</div>
				</c:if>
				<c:if test="${contenidor.expedient}">
					<div class="tab-pane" id="nouvinguts">
						<%--                    --%>
						<%-- Pipella nouvinguts --%>
						<%--                    --%>
						
						<%--                     --%>
						<%-- /Pipella nouvinguts --%>
						<%--                     --%>
					</div>
					<div class="tab-pane" id="registres">
						<%--                   --%>
						<%-- Pipella registres --%>
						<%--                   --%>
						<table id="taulaRegistres" class="table table-bordered table-striped" data-rdt-paginable="false">
							<thead>
								<tr>
									<th data-rdt-property="id" data-rdt-visible="false">#</th>
									<th data-rdt-property="accio" data-rdt-template="cellAccioTemplate" data-rdt-sortable="false" width="10%">
										<spring:message code="bustia.pendent.registre.columna.accio"/>
										<script id="cellAccioTemplate" type="text/x-jsrender">
											{{:~eval('registreAccioText["' + accio + '"]')}}
										</script>
									</th>
									<th data-rdt-property="tipus" data-rdt-template="cellTipusTemplate" data-rdt-sortable="false" width="10%">
										<spring:message code="bustia.pendent.registre.columna.tipus"/>
										<script id="cellTipusTemplate" type="text/x-jsrender">
											{{:~eval('registreTipusText["' + tipus + '"]')}}
										</script>
									</th>
									<th data-rdt-property="numero" data-rdt-sortable="false" width="10%"><spring:message code="bustia.pendent.registre.columna.numero"/></th>
									<th data-rdt-property="data" data-rdt-type="datetime" data-rdt-sortable="false" width="20%"><spring:message code="bustia.pendent.registre.columna.data"/></th>
									<th data-rdt-property="assumpteResum" data-rdt-sortable="false" width="40%"><spring:message code="bustia.pendent.registre.columna.assumpte"/></th>
									<th data-rdt-property="id" data-rdt-sortable="false" data-rdt-template="cellAccionsRegistreTemplate" width="10%">
										<script id="cellAccionsRegistreTemplate" type="text/x-jsrender">
											<div class="dropdown">
												<button class="btn btn-primary" data-toggle="dropdown"><span class="fa fa-cog"></span>&nbsp;<spring:message code="comu.boto.accions"/>&nbsp;<span class="caret"></span></button>
												<ul class="dropdown-menu">
													<li><a href="../contenidor/${contenidor.id}/registre/{{:id}}" data-rdt-link-modal="true"><span class="fa fa-info-circle"></span>&nbsp;&nbsp;<spring:message code="comu.boto.detalls"/></a></li>
													<li><a href="../contenidor/${contenidor.id}/registre/{{:id}}/log" data-rdt-link-modal="true"><span class="fa fa-list"></span>&nbsp;<spring:message code="comu.boto.historial"/></a></li>
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
				</c:if>
			</div>
		</div>
	</div>
</body>
</html>
