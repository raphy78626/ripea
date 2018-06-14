<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib tagdir="/WEB-INF/tags/ripea" prefix="rip"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<c:set var="titol"><spring:message code="unitat.arbre.titol"/></c:set>
<html>
<head>
	<title>${titol}</title>
	<rip:modalHead titol="${titol}"/>
	<link href="<c:url value="/css/jstree.min.css"/>" rel="stylesheet">
	<script src="<c:url value="/js/jstree.min.js"/>"></script>
</head>
<body>
	<rip:arbre id="arbreUnitats" arbre="${arbreUnitatsOrganitzatives}" atributId="codi" atributNom="codiAndNom" changedCallback="changedCallback" />
</body>
</html>