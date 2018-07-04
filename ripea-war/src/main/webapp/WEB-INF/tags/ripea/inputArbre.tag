<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib tagdir="/WEB-INF/tags/ripea" prefix="rip"%>
<%@ attribute name="name" required="true" rtexprvalue="true"%>
<%@ attribute name="required" required="false" rtexprvalue="true"%>
<%@ attribute name="text" required="false" rtexprvalue="true"%>
<%@ attribute name="textKey" required="false" rtexprvalue="true"%>
<%@ attribute name="arbre" required="true" type="java.lang.Object"%>
<%@ attribute name="fulles" type="java.lang.Object"%>
<%@ attribute name="fullesAtributId"%>
<%@ attribute name="fullesAtributNom"%>
<%@ attribute name="fullesAtributPare"%>
<%@ attribute name="fullesIcona"%>
<%@ attribute name="fullesAtributInfo"%>
<%@ attribute name="fullesAtributInfoKey"%>
<%@ attribute name="isArbreSeleccionable" type="java.lang.Boolean"%>
<%@ attribute name="isFullesSeleccionable" type="java.lang.Boolean"%>
<%@ attribute name="isOcultarCounts" type="java.lang.Boolean"%>
<%@ attribute name="isSeleccioMultiple" type="java.lang.Boolean"%>
<%@ attribute name="labelSize" required="false" rtexprvalue="true"%>
<c:set var="campPath" value="${name}"/>
<c:set var="campErrors"><form:errors path="${campPath}"/></c:set>
<c:set var="campLabelText"><c:choose><c:when test="${not empty textKey}"><spring:message code="${textKey}"/></c:when><c:when test="${not empty text}">${text}</c:when><c:otherwise>${campPath}</c:otherwise></c:choose><c:if test="${required}">*</c:if></c:set>
<c:set var="campLabelSize"><c:choose><c:when test="${not empty labelSize}">${labelSize}</c:when><c:otherwise>4</c:otherwise></c:choose></c:set>
<c:set var="campInputSize">${12 - campLabelSize}</c:set>
<c:set var="fullesAtributInfoText"><c:if test="${not empty fullesAtributInfoKey}"><spring:message code="${fullesAtributInfoKey}"/></c:if></c:set>
<c:set var="isSeleccioMultiple" value="${(empty isSeleccioMultiple) ? false : isSeleccioMultiple}" />
<div class="form-group<c:if test="${not empty campErrors}"> has-error</c:if>">
	<label class="control-label col-xs-${campLabelSize}" for="${campPath}">${campLabelText}</label>
	<div class="col-xs-${campInputSize}">
		<div class="input-group" style="width:100%">
			<spring:bind path="${campPath}">
				<input type="hidden" id="${campPath}" name="${campPath}"/>
				<rip:arbre id="arbreUnitats_${campPath}" seleccionatId="${status.value}" arbre="${arbre}" atributId="codi" atributNom="denominacio" changedCallback="changedCallback_${campPath}" fulles="${fulles}" fullesIcona="${fullesIcona}" fullesAtributId="${fullesAtributId}" fullesAtributNom="${fullesAtributNom}" fullesAtributPare="${fullesAtributPare}" isArbreSeleccionable="${isArbreSeleccionable}" isFullesSeleccionable="${isFullesSeleccionable}" isOcultarCounts="${isOcultarCounts}" isError="${not empty campErrors}" fullesAtributInfo="${fullesAtributInfo}" fullesAtributInfoText="${fullesAtributInfoText}"/>
			</spring:bind>
			<c:if test="${not empty campErrors}"><p class="help-block"><span class="fa fa-exclamation-triangle"></span>&nbsp;<form:errors path="${campPath}"/></p></c:if>
		</div>
	</div>
</div>
<script>
	function changedCallback_${campPath}(e, data) {
		if(${isSeleccioMultiple}) {
			var i, j, r = [];
		    for(i = 0, j = data.selected.length; i < j; i++) {
		      r.push(data.instance.get_node(data.selected[i]).id);
		    }
			$('#${campPath}').val(r);
		} else {
			$('#${campPath}').val(data.node.id);
		}
	}
</script>
