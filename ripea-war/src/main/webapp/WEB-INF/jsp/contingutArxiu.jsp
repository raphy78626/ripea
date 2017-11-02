<%@page import="java.util.Iterator"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib tagdir="/WEB-INF/tags/ripea" prefix="rip"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<c:set var="titol"><spring:message code="contingut.arxiu.titol"/></c:set>
<html>
<head>
	<title>${titol}</title>
	<rip:modalHead/>
</head>
<body>
	<c:choose>
		<c:when test="${not empty arxiuDetall}">
			<ul class="nav nav-tabs" role="tablist">
				<li role="presentation" class="active"><a href="#info" aria-controls="info" role="tab" data-toggle="tab"><spring:message code="contingut.arxiu.tab.info"/></a></li>
				<c:if test="${not empty arxiuDetall.fills}">
					<li role="presentation"><a href="#fills" aria-controls="fills" role="tab" data-toggle="tab"><spring:message code="contingut.arxiu.tab.fills"/> <span class="badge badge-default">${fn:length(arxiuDetall.fills)}</span></a></li>
				</c:if>
				<c:if test="${not empty arxiuDetall.firmes}">
					<li role="presentation"><a href="#firmes" aria-controls="firmes" role="tab" data-toggle="tab"><spring:message code="contingut.arxiu.tab.firmes"/> <span class="badge badge-default">${fn:length(arxiuDetall.firmes)}</span></a></li>
				</c:if>
				<c:if test="${not empty arxiuDetall.metadadesAddicionals}">
					<li role="presentation"><a href="#metadades" aria-controls="metadades" role="tab" data-toggle="tab"><spring:message code="contingut.arxiu.tab.metadades"/> <span class="badge badge-default">${fn:length(arxiuDetall.metadadesAddicionals)}</span></a></li>
				</c:if>
			</ul>
			<br/>
			<div class="tab-content">
				<div role="tabpanel" class="tab-pane active" id="info">
					<table class="table table-striped table-bordered">
						<tbody>
							<tr>
								<td><strong><spring:message code="contingut.arxiu.camp.identificador"/></strong></td>
								<td>${arxiuDetall.identificador}</td>
							</tr>
							<tr>
								<td><strong><spring:message code="contingut.arxiu.camp.nom"/></strong></td>
								<td>${arxiuDetall.nom}</td>
							</tr>
							<c:if test="${not empty arxiuDetall.serieDocumental}">
								<tr>
									<td><strong><spring:message code="contingut.arxiu.camp.serie.doc"/></strong></td>
									<td>${arxiuDetall.serieDocumental}</td>
								</tr>
							</c:if>
						</tbody>
					</table>
					<c:if test="${not empty arxiuDetall.contingutTipusMime or not empty arxiuDetall.contingutArxiuNom}">
						<div class="panel panel-default">
							<div class="panel-heading"><h4 style="margin:0"><strong><spring:message code="contingut.arxiu.grup.contingut"/></strong></h4></div>
							<table class="table table-striped table-bordered">
							<tbody>
								<c:if test="${not empty arxiuDetall.contingutTipusMime}">
									<tr>
										<td><strong><spring:message code="contingut.arxiu.camp.contingut.tipus.mime"/></strong></td>
										<td>${arxiuDetall.contingutTipusMime}</td>
									</tr>
								</c:if>
								<c:if test="${not empty arxiuDetall.contingutArxiuNom}">
									<tr>
										<td><strong><spring:message code="contingut.arxiu.camp.contingut.arxiu.nom"/></strong></td>
										<td>${arxiuDetall.contingutArxiuNom}</td>
									</tr>
								</c:if>
							</tbody>
							</table>
						</div>
					</c:if>
					<c:if test="${not empty arxiuDetall.eniIdentificador}">
						<div class="panel panel-default">
							<div class="panel-heading"><h4 style="margin:0"><strong><spring:message code="contingut.arxiu.grup.metadades"/></strong></h4></div>
							<table class="table table-striped table-bordered">
							<tbody>
								<tr>
									<td><strong><spring:message code="contingut.arxiu.camp.eni.versio"/></strong></td>
									<td>${arxiuDetall.eniVersio}</td>
								</tr>
								<tr>
									<td><strong><spring:message code="contingut.arxiu.camp.eni.identificador"/></strong></td>
									<td>${arxiuDetall.eniIdentificador}</td>
								</tr>
								<c:if test="${not empty arxiuDetall.eniOrgans}">
									<tr>
										<td><strong><spring:message code="contingut.arxiu.camp.eni.organs"/></strong></td>
										<td>
											<c:forEach var="organ" items="${arxiuDetall.eniOrgans}" varStatus="status">
												${organ}<c:if test="${not status.last}">,</c:if>
											</c:forEach>
										</td>
									</tr>
								</c:if>
								<c:if test="${not empty arxiuDetall.eniDataObertura}">
									<tr>
										<td><strong><spring:message code="contingut.arxiu.camp.eni.data.obertura"/></strong></td>
										<td><fmt:formatDate value="${arxiuDetall.eniDataObertura}" pattern="dd/MM/yyyy HH:mm:ss"/></td>
									</tr>
								</c:if>
								<c:if test="${not empty arxiuDetall.eniClassificacio}">
									<tr>
										<td><strong><spring:message code="contingut.arxiu.camp.eni.classificacio"/></strong></td>
										<td>${arxiuDetall.eniClassificacio}</td>
									</tr>
								</c:if>
								<c:if test="${not empty arxiuDetall.eniEstat}">
									<tr>
										<td><strong><spring:message code="contingut.arxiu.camp.eni.estat"/></strong></td>
										<td><spring:message code="expedient.estat.enum.${arxiuDetall.eniEstat}"/></td>
									</tr>
								</c:if>
								<c:if test="${not empty arxiuDetall.eniDataCaptura}">
									<tr>
										<td><strong><spring:message code="contingut.arxiu.camp.eni.data.captura"/></strong></td>
										<td><fmt:formatDate value="${arxiuDetall.eniDataCaptura}" pattern="dd/MM/yyyy HH:mm:ss"/></td>
									</tr>
								</c:if>
								<c:if test="${not empty arxiuDetall.eniOrigen}">
									<tr>
										<td><strong><spring:message code="contingut.arxiu.camp.eni.origen"/></strong></td>
										<td><spring:message code="document.nti.origen.enum.${arxiuDetall.eniOrigen}"/></td>
									</tr>
								</c:if>
								<c:if test="${not empty arxiuDetall.eniEstatElaboracio}">
									<tr>
										<td><strong><spring:message code="contingut.arxiu.camp.eni.estat.elab"/></strong></td>
										<td><spring:message code="document.nti.estela.enum.${arxiuDetall.eniEstatElaboracio}"/></td>
									</tr>
								</c:if>
								<c:if test="${not empty arxiuDetall.eniTipusDocumental}">
									<tr>
										<td><strong><spring:message code="contingut.arxiu.camp.eni.tipus.doc"/></strong></td>
										<td><spring:message code="document.nti.tipdoc.enum.${arxiuDetall.eniTipusDocumental}"/></td>
									</tr>
								</c:if>
								<c:if test="${not empty arxiuDetall.eniFormat}">
									<tr>
										<td><strong><spring:message code="contingut.arxiu.camp.eni.format.nom"/></strong></td>
										<td>${arxiuDetall.eniFormat}</td>
									</tr>
								</c:if>
								<c:if test="${not empty arxiuDetall.eniExtensio}">
									<tr>
										<td><strong><spring:message code="contingut.arxiu.camp.eni.format.ext"/></strong></td>
										<td>${arxiuDetall.eniExtensio}</td>
									</tr>
								</c:if>
								<c:if test="${not empty arxiuDetall.eniInteressats}">
									<tr>
										<td><strong><spring:message code="contingut.arxiu.camp.eni.interessats"/></strong></td>
										<td>
											<c:forEach var="interessat" items="${arxiuDetall.eniInteressats}" varStatus="status">
												${interessat}<c:if test="${not status.last}">,</c:if>
											</c:forEach>
										</td>
									</tr>
								</c:if>
								<c:if test="${not empty arxiuDetall.eniDocumentOrigenId}">
									<tr>
										<td><strong><spring:message code="contingut.arxiu.camp.eni.doc.orig.id"/></strong></td>
										<td>${arxiuDetall.eniDocumentOrigenId}</td>
									</tr>
								</c:if>
							</tbody>
							</table>
						</div>
					</c:if>
					<%--table class="table table-bordered">
						<tbody>
							<c:if test="${not empty arxiuInfo.eniVersio}">
								<tr>
									<td><strong><spring:message code="contingut.arxiu.camp.eni.versio"/></strong></td>
									<td>${arxiuInfo.eniVersio}</td>
								</tr>
							</c:if>
							<c:if test="${not empty arxiuInfo.eniIdentificador}">
								<tr>
									<td><strong><spring:message code="contingut.arxiu.camp.eni.identificador"/></strong></td>
									<td>${arxiuInfo.eniIdentificador}</td>
								</tr>
							</c:if>
							<c:if test="${not empty arxiuInfo.eniOrigen}">
								<tr>
									<td><strong><spring:message code="contingut.arxiu.camp.eni.origen"/></strong></td>
									<td><spring:message code="document.nti.origen.enum.${arxiuInfo.eniOrigen}"/></td>
								</tr>
							</c:if>
							<c:choose>
								<c:when test="${contingut.expedient}">
									<tr>
										<td><strong><spring:message code="contingut.arxiu.camp.eni.data.obertura"/></strong></td>
										<td><fmt:formatDate value="${arxiuInfo.eniDataObertura}" pattern="dd/MM/yyyy HH:mm:ss"/></td>
									</tr>
									<tr>
										<td><strong><spring:message code="contingut.arxiu.camp.eni.classificacio"/></strong></td>
										<td>${arxiuInfo.eniClassificacio}</td>
									</tr>
									<tr>
										<td><strong><spring:message code="contingut.arxiu.camp.eni.estat"/></strong></td>
										<td><spring:message code="expedient.estat.enum.${arxiuInfo.eniEstat}"/></td>
									</tr>
								</c:when>
								<c:when test="${contingut.document}">
									<tr>
										<td><strong><spring:message code="contingut.arxiu.camp.eni.data.captura"/></strong></td>
										<td><fmt:formatDate value="${arxiuInfo.eniDataCaptura}" pattern="dd/MM/yyyy HH:mm:ss"/></td>
									</tr>
									<tr>
										<td><strong><spring:message code="contingut.arxiu.camp.eni.estat.elab"/></strong></td>
										<td><spring:message code="document.nti.estela.enum.${arxiuInfo.eniEstatElaboracio}"/></td>
									</tr>
									<tr>
										<td><strong><spring:message code="contingut.arxiu.camp.eni.tipus.doc"/></strong></td>
										<td><spring:message code="document.nti.tipdoc.enum.${arxiuInfo.eniTipusDocumental}"/></td>
									</tr>
									<c:if test="${not empty arxiuInfo.eniFormatNom}">
										<tr>
											<td><strong><spring:message code="contingut.arxiu.camp.eni.format.nom"/></strong></td>
											<td>${arxiuInfo.eniFormatNom}</td>
										</tr>
									</c:if>
									<c:if test="${not empty arxiuInfo.eniFormatExtensio}">
										<tr>
											<td><strong><spring:message code="contingut.arxiu.camp.eni.format.ext"/></strong></td>
											<td>${arxiuInfo.eniFormatExtensio}</td>
										</tr>
									</c:if>
								</c:when>
							</c:choose>
							<c:if test="${not empty arxiuInfo.eniOrgans}">
								<tr>
									<td><strong><spring:message code="contingut.arxiu.camp.eni.organs"/></strong></td>
									<td>
										<c:forEach var="organ" items="${arxiuInfo.eniOrgans}" varStatus="status">
											${organ}<c:if test="${not status.last}">,</c:if>
										</c:forEach>
									</td>
								</tr>
							</c:if>
							<c:if test="${not empty arxiuInfo.eniInteressats}">
								<tr>
									<td><strong><spring:message code="contingut.arxiu.camp.eni.interessats"/></strong></td>
									<td>
										<c:forEach var="interessat" items="${arxiuInfo.eniInteressats}" varStatus="status">
											${interessat}<c:if test="${not status.last}">,</c:if>
										</c:forEach>
									</td>
								</tr>
							</c:if>
							<c:if test="${not empty arxiuInfo.eniDocumentOrigenId}">
								<tr>
									<td><strong><spring:message code="contingut.arxiu.camp.eni.doc.orig.id"/></strong></td>
									<td>${arxiuInfo.eniDocumentOrigenId}</td>
								</tr>
							</c:if>
							<c:if test="${not empty arxiuInfo.eniFirmaTipus}">
								<tr>
									<td><strong><spring:message code="contingut.arxiu.camp.eni.firma.tipus"/></strong></td>
									<td>
										<c:forEach var="tipus" items="${arxiuInfo.eniFirmaTipus}" varStatus="status">
											${tipus}<c:if test="${not status.last}">,</c:if>
										</c:forEach>
									</td>
								</tr>
							</c:if>
							<c:if test="${not empty arxiuInfo.eniFirmaCsv}">
								<tr>
									<td><strong><spring:message code="contingut.arxiu.camp.eni.firma.csv"/></strong></td>
									<td>
										<c:forEach var="csv" items="${arxiuInfo.eniFirmaCsv}" varStatus="status">
											${csv}<c:if test="${not status.last}">,</c:if>
										</c:forEach>
									</td>
								</tr>
							</c:if>
							<c:if test="${not empty arxiuInfo.eniFirmaCsvDefinicio}">
								<tr>
									<td><strong><spring:message code="contingut.arxiu.camp.eni.firma.csvdef"/></strong></td>
									<td>
										<c:forEach var="csvdef" items="${arxiuInfo.eniFirmaCsvDefinicio}" varStatus="status">
											${csvdef}<c:if test="${not status.last}">,</c:if>
										</c:forEach>
									</td>
								</tr>
							</c:if>
						</tbody>
					</table--%>
				</div>
				<c:if test="${not empty arxiuDetall.fills}">
					<div role="tabpanel" class="tab-pane" id="fills">
						<table class="table table-striped table-bordered">
							<c:forEach var="fill" items="${arxiuDetall.fills}" varStatus="status">
								<tr>
									<td width="10%">${fill.tipus}</td>
									<td>${fill.nom}</td>
								</tr>
							</c:forEach>
						</table>
					</div>
				</c:if>
				<c:if test="${not empty arxiuDetall.firmes}">
					<div role="tabpanel" class="tab-pane" id="firmes">
						<c:forEach var="firma" items="${arxiuDetall.firmes}" varStatus="status">
							<div class="panel panel-default">
								<div class="panel-heading"><h4 style="margin:0"><strong><spring:message code="contingut.arxiu.grup.firma"/> ${firma.tipus}</strong></h4></div>
								<table class="table table-striped table-bordered">
								<tbody>
									<c:if test="${not empty firma.perfil}">
										<tr>
											<td><strong><spring:message code="contingut.arxiu.camp.firma.perfil"/></strong></td>
											<td>${firma.perfil}</td>
										</tr>
									</c:if>
									<c:if test="${not empty firma.fitxerNom}">
										<tr>
											<td><strong><spring:message code="contingut.arxiu.camp.firma.arxiu"/></strong></td>
											<td>${firma.fitxerNom}</td>
										</tr>
									</c:if>
									<c:if test="${not empty firma.tipusMime}">
										<tr>
											<td><strong><spring:message code="contingut.arxiu.camp.firma.tipus.mime"/></strong></td>
											<td>${firma.tipusMime}</td>
										</tr>
									</c:if>
									<c:if test="${firma.tipus == 'CSV'}">
										<tr>
											<td><strong><spring:message code="contingut.arxiu.camp.firma.csv"/></strong></td>
											<td>${firma.contingutComString}</td>
										</tr>
									</c:if>
									<c:if test="${not empty firma.csvRegulacio}">
										<tr>
											<td><strong><spring:message code="contingut.arxiu.camp.firma.csv.reg"/></strong></td>
											<td>${firma.csvRegulacio}</td>
										</tr>
									</c:if>
								</tbody>
								</table>
							</div>
						</c:forEach>
					</div>
				</c:if>
				<c:if test="${not empty arxiuDetall.metadadesAddicionals}">
					<div role="tabpanel" class="tab-pane" id="metadades">
						<table class="table table-striped table-bordered">
							<c:forEach var="metadada" items="${arxiuDetall.metadadesAddicionals}" varStatus="status">
								<tr>
									<td width="20%"><strong>${metadada.key}</strong></td>
									<td>${metadada.value}</td>
								</tr>
							</c:forEach>
						</table>
					</div>
				</c:if>
				<%--c:if test="${not empty arxiuInfo.aspectes}">
					<div role="tabpanel" class="tab-pane" id="aspectes">
						<ul class="list-group">
							<c:forEach var="aspecte" items="${arxiuInfo.aspectes}" varStatus="status">
							  <li class="list-group-item">${aspecte}</li>
							</c:forEach>
						</ul>
					</div>
				</c:if>
				<c:if test="${not empty arxiuInfo.continguts}">
					<div role="tabpanel" class="tab-pane" id="continguts">
						<table class="table table-bordered">
							<c:forEach var="contingut" items="${arxiuInfo.continguts}" varStatus="status">
								<tr>
									<td>${contingut.tipus}</td>
									<td>${contingut.contentType}</td>
								</tr>
							</c:forEach>
						</table>
					</div>
				</c:if>
				<c:if test="${not empty arxiuInfo.codiFontPeticio || not empty arxiuInfo.codiFontResposta}">
					<div role="tabpanel" class="tab-pane" id="json">
						<div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
							<div class="panel panel-default">
								<div class="panel-heading">
									<h3 class="panel-title">
										<a role="button" data-toggle="collapse" data-parent="#accordion" href="#collapsePeticio" aria-expanded="false" aria-controls="collapsePeticio">
											<spring:message code="contingut.arxiu.json.peticio"/>
										</a>
									</h3>
								</div>
								<div id="collapsePeticio" class="panel-collapse collapse in" role="tabpanel" aria-labelledby="collapsePeticio">
									<div class="panel-body">
										<pre id="codiFontPeticio"></pre>
									</div>
								</div>
							</div>
							<div class="panel panel-default">
								<div class="panel-heading">
									<h3 class="panel-title">
										<a class="collapsed" role="button" data-toggle="collapse" data-parent="#accordion" href="#collapseResposta" aria-expanded="false" aria-controls="collapseResposta">
											<spring:message code="contingut.arxiu.json.resposta"/>
										</a>
									</h3>
								</div>
								<div id="collapseResposta" class="panel-collapse collapse" role="tabpanel" aria-labelledby="collapseResposta">
									<div class="panel-body">
										<pre id="codiFontResposta"></pre>
									</div>
								</div>
							</div>
						</div>
					</div>
				</c:if--%>
			</div>
		</c:when>
		<c:otherwise>
			<div class="alert alert-warning well-sm" role="alert">
				<span class="fa fa-info-circle"></span>
				<spring:message code="contingut.arxiu.no.alta"/>
			</div>
			<br/>
			<%--div style="text-align:center">
				<a href="#" class="btn btn-success">
					<span class="fa fa-download"></span>
					Emmagatzemar a dins l'arxiu digital
				</a>
			</div--%>
			<br/>
		</c:otherwise>
	</c:choose>
	
	<div id="modal-botons" class="well">
		<a href="<c:url value="/contenidor/${contenidor.id}"/>" class="btn btn-default" data-modal-cancel="true"><spring:message code="comu.boto.tancar"/></a>
	</div>
</body>
</html>
