<%@page import="java.util.Iterator"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib tagdir="/WEB-INF/tags/ripea" prefix="rip"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<c:set var="titol"><spring:message code="contingut.log.titol"/></c:set>
<html>
<head>
	<title>${titol}</title>
	<rip:modalHead/>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active">
			<a data-toggle="tab" href="#accions">
				<span class="fa fa-cog"></span>
				<spring:message code="comu.boto.accions"/>
			</a>
		</li>
		<li>
			<a data-toggle="tab" href="#moviments">
				<span class="fa fa-retweet"></span>
				<spring:message code="comu.boto.moviments"/>
			</a>
		</li>
		<li>
			<a data-toggle="tab" href="#auditoria">
				<span class="fa fa-eye"></span>
				<spring:message code="contingut.log.pipella.auditoria"/>
			</a>
		</li>
	</ul>
	<br/>
	<div class="tab-content">
		<div class="tab-pane active in" id="accions">
			<table class="table table-striped table-bordered">
				<thead>
					<tr>
						<th><spring:message code="contingut.log.columna.data"/></th>
						<th><spring:message code="contingut.log.columna.usuari"/></th>
						<th><spring:message code="contingut.log.columna.accio"/></th>
						<th><spring:message code="contingut.log.columna.detalls"/></th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="log" items="${logs}">
						<tr>
							<td><fmt:formatDate value="${log.data}" pattern="dd/MM/yyyy HH:mm:ss"/></td>
							<td>${log.usuari.nom}</td>
							<td><spring:message code="log.tipus.enum.${log.tipus}"/></td>
							<td><c:if test="${not empty log.objecteId}">
								<spring:message code="log.objecte.tipus.enum.${log.objecteTipus}"/>#${log.objecteId}<br/>
								<spring:message code="log.tipus.enum.${log.objecteLogTipus}"/>
							</c:if></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
		<div class="tab-pane" id="moviments">
			<c:if test="${not empty moviments}">
				<table class="table table-striped table-bordered">
					<thead>
						<tr>
							<th><spring:message code="contingut.log.columna.data"/></th>
							<th><spring:message code="contingut.log.columna.usuari"/></th>
							<th>Origen</th>
							<th>Desti</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="moviment" items="${moviments}">
							<tr>
								<td><fmt:formatDate value="${moviment.data}" pattern="dd/MM/yyyy HH:mm:ss"/></td>
								<td>${moviment.remitent.nom}</td>
								<td>
									<c:if test="${not empty moviment.origen}">
										<c:choose>
											<c:when test="${moviment.origen.expedient}"><spring:message code="contingut.tipus.enum.EXPEDIENT"/></c:when>
											<c:when test="${moviment.origen.carpeta}"><spring:message code="contingut.tipus.enum.CARPETA"/></c:when>
											<c:when test="${moviment.origen.document}"><spring:message code="contingut.tipus.enum.DOCUMENT"/></c:when>
											<c:when test="${moviment.origen.escriptori}"><spring:message code="contingut.tipus.enum.ESCRIPTORI"/></c:when>
											<c:when test="${moviment.origen.arxiv}"><spring:message code="contingut.tipus.enum.ARXIU"/></c:when>
											<c:when test="${moviment.origen.bustia}"><spring:message code="contingut.tipus.enum.BUSTIA"/></c:when>
										</c:choose>#${moviment.origen.id}
									</c:if>
								</td>
								<td>
									<c:choose>
										<c:when test="${moviment.desti.expedient}"><spring:message code="contingut.tipus.enum.EXPEDIENT"/></c:when>
										<c:when test="${moviment.desti.carpeta}"><spring:message code="contingut.tipus.enum.CARPETA"/></c:when>
										<c:when test="${moviment.desti.document}"><spring:message code="contingut.tipus.enum.DOCUMENT"/></c:when>
										<c:when test="${moviment.desti.escriptori}"><spring:message code="contingut.tipus.enum.ESCRIPTORI"/></c:when>
										<c:when test="${moviment.desti.arxiv}"><spring:message code="contingut.tipus.enum.ARXIU"/></c:when>
										<c:when test="${moviment.desti.bustia}"><spring:message code="contingut.tipus.enum.BUSTIA"/></c:when>
									</c:choose>#${moviment.desti.id}
								</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</c:if>
		</div>
		<div class="tab-pane" id="auditoria">
			<dl class="dl-horizontal">
				<dt><spring:message code="contingut.log.camp.usuari.creacio"/></dt>
				<dd>${contingut.createdBy.nom}</dd>
				<dt><spring:message code="contingut.log.camp.data.creacio"/></dt>
				<dd><fmt:formatDate value="${contingut.createdDate}" pattern="dd/MM/yyyy HH:mm:ss"/></dd>
				<dt><spring:message code="contingut.log.camp.usuari.modificacio"/></dt>
				<dd>${contingut.lastModifiedBy.nom}</dd>
				<dt><spring:message code="contingut.log.camp.data.modificacio"/></dt>
				<dd><fmt:formatDate value="${contingut.lastModifiedDate}" pattern="dd/MM/yyyy HH:mm:ss"/></dd>
			</dl>
		</div>
	</div>
	<div id="modal-botons" class="well">
		<a href="<c:url value="/contingut/${contingut.id}"/>" class="btn btn-default" data-modal-cancel="true"><spring:message code="comu.boto.tancar"/></a>
	</div>
</body>
</html>
