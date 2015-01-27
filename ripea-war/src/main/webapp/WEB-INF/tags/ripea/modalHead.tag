<%@ attribute name="titol" required="true"%>
<%@ attribute name="buttonContainerId" required="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%><%
if (es.caib.ripea.war.helper.ModalHelper.isModal(request)) {%>
<meta name="decorator" content="senseCapNiPeus"/>
<%--c:if test="${not empty buttonContainerId}">
<script>
	$(document).ready(function() {
		var titol = "${titol}";
		window.parent.modalCanviarTitol(
				window.frameElement,
				titol);
		window.parent.modalCopiarBotons(
				window.frameElement,
				$('#${buttonContainerId} .btn'));
		$('#${buttonContainerId}').hide();
	});
	$(window).load(function() {
		setTimeout(function() {
			window.parent.modalAjustarTamany(
					window.frameElement,
					$('html').height());
		},
		100);
	});
</script>
</c:if--%>
<%}%>
