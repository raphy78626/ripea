<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib tagdir="/WEB-INF/tags/ripea" prefix="rip"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<c:choose>
	<c:when test="${empty metaExpedientCommand.id}"><c:set var="titol"><spring:message code="metaexpedient.form.titol.crear"/></c:set></c:when>
	<c:otherwise><c:set var="titol"><spring:message code="metaexpedient.form.titol.modificar"/></c:set></c:otherwise>
</c:choose>
<html>
<head>
	<title>${titol}</title>
	<link href="<c:url value="/webjars/select2/4.0.1/dist/css/select2.min.css"/>" rel="stylesheet"/>
	<link href="<c:url value="/webjars/select2-bootstrap-theme/0.1.0-beta.4/dist/select2-bootstrap.min.css"/>" rel="stylesheet"/>
	<script src="<c:url value="/webjars/select2/4.0.1/dist/js/select2.min.js"/>"></script>
	<script src="<c:url value="/webjars/select2/4.0.1/dist/js/i18n/${requestLocale}.js"/>"></script>
	<script src="<c:url value="/js/webutil.common.js"/>"></script>
	<rip:modalHead/>
</head>
<body>
	<c:set var="formAction"><rip:modalUrl value="/metaExpedient"/></c:set>
	<form:form action="${formAction}" method="post" cssClass="form-horizontal" commandName="metaExpedientCommand">
		<ul class="nav nav-tabs" role="tablist">
			<li role="presentation" class="active"><a href="#dades" aria-controls="dades" role="tab" data-toggle="tab"><spring:message code="metaexpedient.form.camp.tab.dades"/></a></li>
			<li role="presentation"><a href="#notificacions" aria-controls="notificacions" role="tab" data-toggle="tab"><spring:message code="metaexpedient.form.camp.tab.notificacions"/></a></li>
		</ul>
		<form:hidden path="id"/>
		<form:hidden path="entitatId"/>
		<br/>
		<div class="tab-content">
			<div role="tabpanel" class="tab-pane active" id="dades">
				<rip:inputText name="codi" textKey="metaexpedient.form.camp.codi" required="true"/>
				<rip:inputText name="nom" textKey="metaexpedient.form.camp.nom" required="true"/>
				<rip:inputTextarea name="descripcio" textKey="metaexpedient.form.camp.descripcio"/>
				<rip:inputText name="classificacioSia" textKey="metaexpedient.form.camp.classificacio.sia" required="true"/>
				<rip:inputText name="classificacioDocumental" textKey="metaexpedient.form.camp.classificacio.doc" required="true"/>
			</div>
			<div role="tabpanel" class="tab-pane" id="notificacions">
				<rip:inputText name="unitatAdministrativa" textKey="metaexpedient.form.camp.unitat.adm"/>
				<rip:inputCheckbox name="notificacioActiva" textKey="metaexpedient.form.camp.notificacio.activa"/>
				<rip:inputText name="notificacioOrganCodi" textKey="metaexpedient.form.camp.notificacio.organ"/>
				<rip:inputText name="notificacioLlibreCodi" textKey="metaexpedient.form.camp.notificacio.llibre"/>
				<rip:inputText name="notificacioAvisTitol" textKey="metaexpedient.form.camp.notificacio.avis.titol"/>
				<rip:inputTextarea name="notificacioAvisText" textKey="metaexpedient.form.camp.notificacio.avis.text"/>
				<rip:inputTextarea name="notificacioAvisTextSms" textKey="metaexpedient.form.camp.notificacio.avis.textsms"/>
				<rip:inputText name="notificacioOficiTitol" textKey="metaexpedient.form.camp.notificacio.ofici.titol"/>
				<rip:inputTextarea name="notificacioOficiText" textKey="metaexpedient.form.camp.notificacio.ofici.text"/>
			</div>
		</div>
		<div id="modal-botons">
			<button type="submit" class="btn btn-success"><span class="fa fa-save"></span> <spring:message code="comu.boto.guardar"/></button>
			<a href="<c:url value="/metaExpedient"/>" class="btn btn-default" data-modal-cancel="true"><spring:message code="comu.boto.cancelar"/></a>
		</div>
	</form:form>
</body>
</html>
