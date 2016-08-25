<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ attribute name="id" required="false" rtexprvalue="true"%>
<%@ attribute name="className" required="false" rtexprvalue="true"%>
<%@ attribute name="contenidor" required="true" rtexprvalue="true" type="java.lang.Object"%>
<%@ attribute name="modeLlistat" required="true" rtexprvalue="true"%>
<%@ attribute name="mostrarObrir" required="false" rtexprvalue="true"%>
<div <c:if test="${not empty id}">id="${id}" </c:if>class="dropdown<c:if test="${not modeLlistat}"> text-center</c:if><c:if test="${not empty className}"> ${className}</c:if>">
	<button data-toggle="dropdown" class="btn btn-primary dropdown-toggle<c:if test="${not modeLlistat}"> btn-xs</c:if>"><span class="fa fa-cog"></span><c:if test="${modeLlistat}">&nbsp;<spring:message code="comu.boto.accions"/></c:if>&nbsp;<span class="caret caret-white"></span></button>
	<ul class="dropdown-menu">
		<c:if test="${empty mostrarObrir or mostrarObrir}">
			<li><a href="${contenidor.id}"><span class="fa fa-folder-open-o"></span>&nbsp;<spring:message code="comu.boto.obrir"/></a></li>
		</c:if>
		<c:if test="${not empty contenidor.escriptoriPare}">
			<c:if test="${contenidor.expedient}">
				<c:if test="${contenidor.metaNode.usuariActualWrite or empty contenidor.metaNode}">
					<li><a href="../contenidor/${contenidor.pare.id}/expedient/${contenidor.id}" data-rdt-link-modal="true"><span class="fa fa-pencil"></span>&nbsp;<spring:message code="comu.boto.modificar"/>...</a></li>
					<li><a href="../contenidor/${contenidor.pare.id}/expedient/${contenidor.id}/relacionar" data-rdt-link-modal="true"><span class="fa fa-link"></span>&nbsp;<spring:message code="comu.boto.relacionar"/>...</a></li>
					<li><a href="../contenidor/${contenidor.pare.id}/expedient/${contenidor.id}/acumular" data-rdt-link-modal="true"><span class="fa fa-sign-in"></span>&nbsp;<spring:message code="comu.boto.acumular"/>...</a></li>
				</c:if>
				<li><a href="../contenidor/${contenidor.pare.id}/expedient/${contenidor.id}/alliberar"><span class="fa fa-unlock"></span>&nbsp;<spring:message code="comu.boto.alliberar"/></a></li>
				<c:if test="${contenidor.estat == 'OBERT'}">
					<c:choose>
						<c:when test="${contenidor.valid}">
							<li><a href="../contenidor/${contenidor.pare.id}/expedient/${contenidor.id}/tancar" data-rdt-link-modal="true"><span class="fa fa-check"></span>&nbsp;<spring:message code="comu.boto.tancar"/>...</a></li>
						</c:when>
						<c:otherwise>
							<li><a href="#" onclick="alert('<spring:message code="contenidor.expedient.tancar.error.no.valid"/>');return false;"><span class="fa fa-check"></span>&nbsp;<spring:message code="comu.boto.tancar"/>...</a></li>
						</c:otherwise>
					</c:choose>
				</c:if>
				<c:if test="${contenidor.metaNode.usuariActualWrite or empty contenidor.metaNode}">
					<li><a href="../contenidor/${contenidor.pare.id}/expedient/${contenidor.id}/acumular" data-rdt-link-modal="true"><span class="fa fa-sign-in"></span>&nbsp;<spring:message code="comu.boto.acumular"/>...</a></li>
					<li><a href="../contenidor/${contenidor.pare.id}/expedient/${contenidor.id}/disgregar" data-rdt-link-modal="true"><span class="fa fa-sign-out"></span>&nbsp;<spring:message code="comu.boto.disgregar"/>...</a></li>
				</c:if>
			</c:if>
			<c:if test="${contenidor.carpeta}">
				<li><a href="../contenidor/${contenidor.pare.id}/carpeta/${contenidor.id}" data-rdt-link-modal="true"><span class="fa fa-pencil"></span>&nbsp;<spring:message code="comu.boto.modificar"/>...</a></li>
			</c:if>
			<c:if test="${contenidor.document}">
				<li><a href="../contenidor/${contenidor.pare.id}/document/${contenidor.id}" data-rdt-link-modal="true"><span class="fa fa-pencil"></span>&nbsp;<spring:message code="comu.boto.modificar"/>...</a></li>
				<li><a href="../contenidor/${contenidor.pare.id}/document/${contenidor.id}/descarregar"><span class="fa fa-download"></span>&nbsp;<spring:message code="comu.boto.descarregar"/></a></li>
			</c:if>
			<c:if test="${not contenidor.expedient}">
				<li><a href="../contenidor/${contenidor.id}/moure" data-rdt-link-modal="true"><span class="fa fa-arrows"></span>&nbsp;<spring:message code="comu.boto.moure"/>...</a></li>
				<li><a href="../contenidor/${contenidor.id}/copiar" data-rdt-link-modal="true"><span class="fa fa-copy"></span>&nbsp;<spring:message code="comu.boto.copiar"/>...</a></li>
				<c:if test="${empty contenidor.expedientPare}">
					<li><a href="../contenidor/${contenidor.id}/enviar" data-rdt-link-modal="true"><span class="fa fa-send"></span>&nbsp;<spring:message code="comu.boto.enviara"/>...</a></li>
				</c:if>
			</c:if>
		</c:if>
		<li><a href="../contenidor/${contenidor.id}/log" data-rdt-link-modal="true"><span class="fa fa-list"></span>&nbsp;<spring:message code="comu.boto.historial"/></a></li>
		<c:if test="${not empty contenidor.escriptoriPare}">
			<c:if test="${contenidor.carpeta or empty contenidor.metaNode or contenidor.metaNode.usuariActualDelete}">
				<li><a href="${contenidor.id}/delete" data-rdt-link-confirm="<spring:message code="contenidor.contingut.confirmacio.esborrar.node"/>"><span class="fa fa-trash-o"></span>&nbsp;<spring:message code="comu.boto.esborrar"/></a></li>
			</c:if>
		</c:if>
	</ul>
</div>
