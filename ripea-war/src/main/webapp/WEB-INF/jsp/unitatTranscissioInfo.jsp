<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib tagdir="/WEB-INF/tags/ripea" prefix="rip"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<html>
<head>
	<title><spring:message code="unitat.transcissioInfo.unitatObsoleta"/></title>
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

			<span class="fa fa-warning text-danger"></span> <spring:message code="unitat.obsoleta"/>
		</div>
		<div class="panel-body">
			<div class="row">
				<label class="col-xs-4 text-right"><spring:message code="unitat.transcissioInfo.tipusTranscissio"/></label>
				<div class="col-xs-8">
					<c:if test="${unitatOrganitzativaDto.tipusTranscissio == 'DIVISIO'}">
						<spring:message code="unitat.tipusTranscissio.DIVISIO"/>
					</c:if>
					<c:if test="${unitatOrganitzativaDto.tipusTranscissio == 'FUSIO'}">
						<spring:message code="unitat.tipusTranscissio.FUSIO"/>
					</c:if>
					<c:if test="${unitatOrganitzativaDto.tipusTranscissio == 'SUBSTITUCIO'}">
						<spring:message code="unitat.tipusTranscissio.SUBSTITUCIO"/>
					</c:if>				
				</div>
			</div>
			<div class="row">
				<label class="col-xs-4 text-right"><spring:message code="unitat.transcissioInfo.novesUnitats"/></label>
				<div class="col-xs-8">
					<ul style="padding-left: 17px;">
						<c:forEach items="${unitatOrganitzativaDto.novaList}"
							var="newUnitat" varStatus="loop">
							<li>${newUnitat.denominacio} (${newUnitat.codi})</li>
						</c:forEach>
					</ul>
				</div>
			</div>
			<div class="row">
				<label class="col-xs-4 text-right"><spring:message code="unitat.transcissioInfo.bustiesAfectades"/></label>
				<div class="col-xs-8">
					<ul style="padding-left: 17px;">
						<c:forEach items="${bustiesOfOldUnitat}" var="bustia"
							varStatus="loop">
							<li>${bustia.nom}</li>
						</c:forEach>
					</ul>
				</div>
			</div>
			<c:if test="${unitatOrganitzativaDto.tipusTranscissio == 'FUSIO'}">
			<div class="row">
				<label class="col-xs-4 text-right"><spring:message code="unitat.transcissioInfo.altresUnitatsFusionades"/></label>
				<div class="col-xs-8">
					<ul style="padding-left: 17px;">
						<c:forEach items="${unitatOrganitzativaDto.altresUnitatsFusionades}" var="unitatMap"
							varStatus="loop">
								<li>${unitatMap.value} (${unitatMap.key}) </li>		
						</c:forEach>
					</ul>
				</div>
			</div>						
			</c:if>
		</div>
	</div>


</body>
</html>
