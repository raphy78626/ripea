<%@page import="java.util.Iterator"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib tagdir="/WEB-INF/tags/ripea" prefix="rip"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<c:set var="titol"><spring:message code="registre.log.titol"/></c:set>

<html>
<head>
	<title>${titol}</title>
	<rip:modalHead titol="${titol}" buttonContainerId="botons"/>
</head>
<body>
	<c:choose>
		<c:when test="${not empty moviments}">
			<table class="table table-striped table-bordered">
				<thead>
					<tr>
						<th><spring:message code="registre.log.columna.data"/></th>
						<th><spring:message code="registre.log.columna.usuari"/></th>
						<th><spring:message code="registre.log.columna.origen"/></th>
						<th><spring:message code="registre.log.columna.desti"/></th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="moviment" items="${moviments}">
						<tr>
							<td><fmt:formatDate value="${moviment.data}" pattern="dd/MM/yyyy HH:mm:ss"/></td>
							<td>${moviment.remitent.nom}</td>
							<td>
								<c:choose>
									<c:when test="${moviment.origen.expedient}"><spring:message code="contenidor.tipus.enum.EXPEDIENT"/></c:when>
									<c:when test="${moviment.origen.carpeta}"><spring:message code="contenidor.tipus.enum.CARPETA"/></c:when>
									<c:when test="${moviment.origen.document}"><spring:message code="contenidor.tipus.enum.DOCUMENT"/></c:when>
									<c:when test="${moviment.origen.escriptori}"><spring:message code="contenidor.tipus.enum.ESCRIPTORI"/></c:when>
									<c:when test="${moviment.origen.arxiv}"><spring:message code="contenidor.tipus.enum.ARXIU"/></c:when>
									<c:when test="${moviment.origen.bustia}"><spring:message code="contenidor.tipus.enum.BUSTIA"/></c:when>
								</c:choose>#${moviment.origen.id}
							</td>
							<td>
								<c:choose>
									<c:when test="${moviment.desti.expedient}"><spring:message code="contenidor.tipus.enum.EXPEDIENT"/></c:when>
									<c:when test="${moviment.desti.carpeta}"><spring:message code="contenidor.tipus.enum.CARPETA"/></c:when>
									<c:when test="${moviment.desti.document}"><spring:message code="contenidor.tipus.enum.DOCUMENT"/></c:when>
									<c:when test="${moviment.desti.escriptori}"><spring:message code="contenidor.tipus.enum.ESCRIPTORI"/></c:when>
									<c:when test="${moviment.desti.arxiv}"><spring:message code="contenidor.tipus.enum.ARXIU"/></c:when>
									<c:when test="${moviment.desti.bustia}"><spring:message code="contenidor.tipus.enum.BUSTIA"/></c:when>
								</c:choose>#${moviment.desti.id}
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</c:when>
		<c:otherwise>
			<div class="alert alert-info well-sm" role="alert"><spring:message code="registre.log.no.moviments"/></div>
		</c:otherwise>
	</c:choose>
	<div id="modal-botons" class="well">
		<a href="<c:url value="/contenidor/${contenidor.id}"/>" class="btn btn-default modal-tancar"><spring:message code="comu.boto.tancar"/></a>
	</div>
</body>
</html>
