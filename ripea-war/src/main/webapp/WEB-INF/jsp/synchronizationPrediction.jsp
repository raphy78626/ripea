<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib tagdir="/WEB-INF/tags/ripea" prefix="rip"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<c:choose>
	<c:when test="${empty bustiaCommand.id}"><c:set var="titol"><spring:message code="unitat.synchronize.dialog.header"/></c:set></c:when>
	<c:otherwise><c:set var="titol"><spring:message code="bustia.form.titol.modificar"/></c:set></c:otherwise>
</c:choose>
<html>
<head>
	<title>${titol}</title>
	<link href="<c:url value="/webjars/select2/4.0.6-rc.1/dist/css/select2.min.css"/>" rel="stylesheet"/>
	<link href="<c:url value="/webjars/select2-bootstrap-theme/0.1.0-beta.4/dist/select2-bootstrap.min.css"/>" rel="stylesheet"/>
	<script src="<c:url value="/webjars/select2/4.0.6-rc.1/dist/js/select2.min.js"/>"></script>
	<script src="<c:url value="/webjars/select2/4.0.6-rc.1/dist/js/i18n/${requestLocale}.js"/>"></script>
	<script src="<c:url value="/js/webutil.common.js"/>"></script>
	<script src="<c:url value="/js/webutil.modal.js"/>"></script>
	<rip:modalHead/>
</head>
	<body>

	

	<div class="panel panel-danger">
		<div class="panel-heading">
			<span class="fa fa-warning text-danger"></span>

		</div>
		<div class="panel-body">

			SPLITS: <br/>
			<c:forEach var="splitMap" items="${splitMap}">
				<c:set var="key" value="${splitMap.key}" />
				<c:set var="values" value="${splitMap.value}" />
				${key.codi}  -->
				<c:forEach var="value" items="${values}">
					${value.codi},
				</c:forEach>
			</c:forEach>
			
			<br/>MERGE: <br/>
			<c:forEach var="mergeMap" items="${mergeMap}">
				<c:set var="key" value="${mergeMap.key}" />
				<c:set var="values" value="${mergeMap.value}" />
				<c:forEach var="value" items="${values}">
					${value.codi},
				</c:forEach>
				: ${key.codi} 
			</c:forEach>
			
			<br/>SUBSTITUCION: <br/>
			<c:forEach var="substMap" items="${substMap}">
				<c:set var="key" value="${substMap.key}" />
				<c:set var="values" value="${substMap.value}" />
				<c:forEach var="value" items="${values}">
					${value.codi},
				</c:forEach>
				: ${key.codi} 
			</c:forEach>
			
			


		</div>
	</div>





	<div id="modal-botons">
			<a href="<c:url value="/unitatOrganitzativa/saveSynchronize"/>" class="btn btn-success">
				<span class="fa fa-save"></span> <spring:message code="comu.boto.guardar"/>
			</a> 
					
			<a href="<c:url value="/unitatOrganitzativa"/>" class="btn btn-default" data-modal-cancel="true">
				<spring:message code="comu.boto.cancelar" />
			</a>
		</div>
	

	
	</body>
</html>
