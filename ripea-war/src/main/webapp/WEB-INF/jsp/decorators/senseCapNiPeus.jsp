<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib tagdir="/WEB-INF/tags/ripea" prefix="rip" %>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator"%>

<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="utf-8">
	<title><decorator:title default="Benvinguts" /></title>
	<meta name="viewport" content="width=device-width, initial-scale=1.0"/>
	<meta name="description" content=""/>
	<meta name="author" content=""/>
	<!-- Estils CSS -->
	<link href="<c:url value="/css/bootstrap.min.css"/>" rel="stylesheet">
	<link href="<c:url value="/css/font-awesome.min.css"/>" rel="stylesheet">
    <link href="<c:url value="/css/estils.css"/>" rel="stylesheet">
	<link rel="shortcut icon" href="<c:url value="/img/favicon.png"/>" type="image/x-icon" />
	<script src="<c:url value="/js/jquery-1.10.2.min.js"/>"></script>
	<!-- Llibreria per a compatibilitat amb HTML5 -->
	<!--[if lt IE 9]>
      <script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
    <![endif]-->
    <script src="<c:url value="/js/bootstrap.min.js"/>"></script>
	<decorator:head />
</head>
<body>
	<div class="container">
		<div id="contingut-alertes"><rip:alertes/></div>
		<decorator:body />
	</div>
    <script src="<c:url value="/js/bootstrap.min.js"/>"></script>
</body>
</html>
