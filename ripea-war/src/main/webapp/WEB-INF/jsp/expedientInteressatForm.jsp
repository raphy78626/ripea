<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib tagdir="/WEB-INF/tags/ripea" prefix="rip"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<c:set var="titol"><spring:message code="interessat.form.titol"/></c:set>
<html>
<head>
	<title>${titol}</title>
	<rip:modalHead titol="${titol}" buttonContainerId="botons-ciutada"/>
	<rip:modalHead titol="${titol}" buttonContainerId="botons-administracio"/>
<style>
.tab-pane {
	margin-top: 1em;
}
</style>
<script type="text/javascript">
var interessatNoms = [];
var interessatLlinatges = [];
<c:forEach var="interessat" items="${interessats}">
interessatNoms['${interessat.id}'] = "${interessat.nom}";
<c:if test="${interessat.ciutada}">
	interessatLlinatges['${interessat.id}'] = "${interessat.llinatges}";
</c:if>
</c:forEach>
$(document).ready(function() {
	$('form').submit(function() {
		$('form input').removeAttr('disabled');
		return true;
	});
	$('select#id').change(function() {
		if (this.value == '') {
			$('input#nom').val('');
			$('input#nom').removeAttr('disabled');
			$('input#llinatges').val('');
			$('input#llinatges').removeAttr('disabled');
		} else {
			$('input#nom').val(interessatNoms[this.value]);
			$('input#nom').attr('disabled', 'disabled');
			$('input#llinatges').val(interessatLlinatges[this.value]);
			$('input#llinatges').attr('disabled', 'disabled');
		}
	});
	$('select#id').change();
});
</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li<c:if test="${interessatCommand.ciutada}"> class="active"</c:if>><a href="#ciutada" data-toggle="tab">Ciutadà</a></li>
		<li<c:if test="${interessatCommand.administracio}"> class="active"</c:if>><a href="#administracio" data-toggle="tab">Administració</a></li>
	</ul>
	<div class="tab-content">
		<div class="tab-pane<c:if test="${interessatCommand.ciutada}"> active</c:if>" id="ciutada">
			<c:choose>
				<c:when test="${interessatCommand.ciutada}"><c:set var="commandName" value="interessatCommand"/></c:when>
				<c:otherwise><c:set var="commandName" value="emptyCommand"/></c:otherwise>
			</c:choose>
			<c:choose>
				<c:when test="${interessatCommand.comprovat}"><c:set var="formAction"><rip:modalUrl value="/expedient/${expedientId}/interessatCiutada"/></c:set></c:when>
				<c:otherwise><c:set var="formAction"><rip:modalUrl value="/expedient/${expedientId}/interessatComprovarCiutada"/></c:set></c:otherwise>
			</c:choose>
			<form:form action="${formAction}" method="post" cssClass="form-horizontal" commandName="${commandName}">
				<form:hidden path="entitatId"/>
				<form:hidden path="comprovat"/>
				<input type="hidden" name="tipus" value="<%=es.caib.ripea.war.command.InteressatCommand.TIPUS_CIUTADA%>"/>
				<rip:inputText name="nif" textKey="interessat.form.camp.nif" required="true"/>
				<c:choose>
					<c:when test="${interessatCommand.ciutada and interessatCommand.comprovat}">
						<c:if test="${fn:length(interessats) > 0}">
							<rip:inputSelect name="id" textKey="interessat.form.camp.id" required="true" optionItems="${interessats}" optionValueAttribute="id" optionTextAttribute="llinatgesComaNom" emptyOption="true" emptyOptionTextKey="interessat.form.camp.id.sense.valor"/>
						</c:if>
						<rip:inputText name="nom" textKey="interessat.form.camp.nom" required="true"/>
						<rip:inputText name="llinatges" textKey="interessat.form.camp.llinatges" required="true"/>
						<div id="botons-ciutada" class="well">
							<button type="submit" class="btn btn-success"><span class="fa fa-save"></span> <spring:message code="comu.boto.guardar"/></button>
							<a href="<c:url value="/contenidor/${expedientId}"/>" class="btn btn-default btn-tancar"><spring:message code="comu.boto.cancelar"/></a>
						</div>
					</c:when>
					<c:otherwise>
						<div id="botons-administracio" class="well">
							<button type="submit" class="btn btn-info"><span class="fa fa-question-sign"></span> <spring:message code="comu.boto.consultar"/></button>
							<a href="<c:url value="/contenidor/${expedientId}"/>" class="btn btn-default btn-tancar"><spring:message code="comu.boto.cancelar"/></a>
						</div>
					</c:otherwise>
				</c:choose>
				
			</form:form>
		</div>
		<div class="tab-pane<c:if test="${interessatCommand.administracio}"> active</c:if>" id="administracio">
			<c:choose>
				<c:when test="${interessatCommand.administracio}"><c:set var="commandName" value="interessatCommand"/></c:when>
				<c:otherwise><c:set var="commandName" value="emptyCommand"/></c:otherwise>
			</c:choose>
			<c:choose>
				<c:when test="${interessatCommand.comprovat}"><c:set var="formAction"><rip:modalUrl value="/expedient/${expedientId}/interessatAdministracio"/></c:set></c:when>
				<c:otherwise><c:set var="formAction"><rip:modalUrl value="/expedient/${expedientId}/interessatComprovarAdministracio"/></c:set></c:otherwise>
			</c:choose>
			<form:form action="${formAction}" method="post" cssClass="form-horizontal" commandName="${commandName}">
				<form:hidden path="entitatId"/>
				<form:hidden path="comprovat"/>
				<input type="hidden" name="tipus" value="<%=es.caib.ripea.war.command.InteressatCommand.TIPUS_ADMINISTRACIO%>"/>
				<rip:inputText name="identificador" textKey="interessat.form.camp.identificador" required="true"/>
				<c:choose>
					<c:when test="${interessatCommand.administracio and interessatCommand.comprovat}">
						<c:if test="${fn:length(interessats) > 0}">
							<rip:inputSelect name="id" textKey="interessat.form.camp.id" required="true" optionItems="${interessats}" optionValueAttribute="id" optionTextAttribute="nom" emptyOption="true" emptyOptionTextKey="interessat.form.camp.id.sense.valor"/>
						</c:if>
						<rip:inputText name="nom" textKey="interessat.form.camp.nom" required="true"/>
						<div id="botons-administracio" class="well">
							<button type="submit" class="btn btn-success"><span class="fa fa-save"></span> <spring:message code="comu.boto.guardar"/></button>
							<a href="<c:url value="/contenidor/${expedientId}"/>" class="btn btn-default btn-tancar"><spring:message code="comu.boto.cancelar"/></a>
						</div>
					</c:when>
					<c:otherwise>
						<div id="botons-administracio" class="well">
							<button type="submit" class="btn btn-info"><span class="fa fa-question-sign"></span> <spring:message code="comu.boto.consultar"/></button>
							<a href="<c:url value="/contenidor/${expedientId}"/>" class="btn btn-default btn-tancar"><spring:message code="comu.boto.cancelar"/></a>
						</div>
					</c:otherwise>
				</c:choose>
			</form:form>
		</div>
	</div>
</body>
</html>
