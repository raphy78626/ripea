<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib tagdir="/WEB-INF/tags/ripea" prefix="rip"%>
<%@ attribute name="contenidor" required="true" rtexprvalue="true" type="java.lang.Object"%>
<%@ attribute name="mostrarExpedients" required="true" rtexprvalue="true" type="java.lang.Boolean"%>
<%@ attribute name="mostrarNoExpedients" required="true" rtexprvalue="true" type="java.lang.Boolean"%>
<c:choose>
	<c:when test="${mostrarExpedients and mostrarNoExpedients}"><c:set var="fills" value="${contenidor.fillsNoRegistres}"/></c:when>
	<c:when test="${mostrarExpedients and not mostrarNoExpedients}"><c:set var="fills" value="${contenidor.fillsExpedients}"/></c:when>
	<c:when test="${not mostrarExpedients and mostrarNoExpedients}"><c:set var="fills" value="${contenidor.fillsNoExpedients}"/></c:when>
</c:choose>
<c:set var="htmlIconaCarpeta"><span class="fa-stack"><i class="fa fa-folder fa-stack-2x"></i><i class="fa fa-clock-o fa-stack-1x fa-inverse"></i></span></c:set>
<c:set var="htmlIconaCarpeta6em"><span class="fa-stack" style="font-size:.6em"><i class="fa fa-folder fa-stack-2x"></i><i class="fa fa-clock-o fa-stack-1x fa-inverse"></i></span></c:set>
<c:choose>
	<c:when test="${vistaIcones}">
		<%--              --%>
		<%-- Vista icones --%>
		<%--              --%>
		<ul id="contenidor-contingut" class="list-inline row">
			<c:forEach var="fill" items="${fills}">
				<c:if test="${fill.carpeta or empty fill.metaNode or fill.metaNode.usuariActualRead}">
					<li class="col-md-2 element-contingut element-draggable<c:if test="${not fill.document}"> element-droppable</c:if>" data-contenidor-id="${fill.id}">
						<div id="info-fill-${fill.id}" class="thumbnail element-noclick">
							<div class="text-center">
								<c:choose>
									<c:when test="${fill.expedient}"><span class="fa fa-briefcase fa-2x"></span></c:when>
									<c:when test="${fill.carpeta}"><rip:blocIconaCarpeta carpeta="${fill}" petita="${false}"/></c:when>
									<c:when test="${fill.document}"><span class="fa fa-file fa-2x"></span></c:when>
								</c:choose>
							</div>
							<div class="caption">
								<p class="text-center">
									<c:if test="${fill.node and not fill.valid}"><span class="fa fa-exclamation-triangle text-warning"></span></c:if>
									${fill.nom}
								</p>
								<rip:blocContenidorAccions id="accions-fill-${fill.id}" className="botons-accions-element" modeLlistat="false" contenidor="${fill}"/>
							</div>
						</div>
						<script>
							var tooltipTitle = '<dl class="text-left" style="min-width:120px">';
							<c:if test="${fill.expedient or fill.document}">
							tooltipTitle += '<dt><spring:message code="contenidor.contingut.info.titol"/></dt>';
							</c:if>
							<c:if test="${not fill.expedient and not fill.document}">
							tooltipTitle += '<dt><spring:message code="contenidor.contingut.info.nom"/></dt>';
							</c:if>
							tooltipTitle += '<dd>${fn:escapeXml(fill.nom)}</dd>';
							<c:if test="${fill.expedient}">
							tooltipTitle += '<dt><spring:message code="contenidor.contingut.info.numero"/></dt>';
							tooltipTitle += '<dd>${fill.sequencia}/${fill.any}</dd>';
							</c:if>
							<c:if test="${fill.carpeta or fill.node and not empty fill.metaNode}">
							tooltipTitle += '<dt><spring:message code="contenidor.contingut.info.tipus"/></dt>';
							<c:choose>
								<c:when test="${fill.carpeta}">
							tooltipTitle += '<dd><spring:message code="carpeta.tipus.enum.${fill.tipus}"/></dd>';
								</c:when>
								<c:when test="${fill.expedient or fill.document}">
							tooltipTitle += '<dd>${fn:escapeXml(fill.metaNode.nom)}</dd>';
								</c:when>
							</c:choose>
							</c:if>
							tooltipTitle += '<dt><spring:message code="contenidor.contingut.info.createl"/></dt>' +
									'<dd><fmt:formatDate value="${fill.createdDate}" pattern="dd/MM/yyyy HH:mm"/></dd>' +
									'<dt><spring:message code="contenidor.contingut.info.creatper"/></dt>' +
									'<dd>${fn:escapeXml(fill.createdBy.nom)}</dd>';
							<c:if test="${fill.expedient}">
							tooltipTitle += '<dt><spring:message code="contenidor.contingut.info.arxiu"/></dt>' +
									'<dd>${fn:escapeXml(fill.arxiu.nom)}</dd>' +
									'<dt><spring:message code="contenidor.contingut.info.estat"/></dt>' +
									'<dd><spring:message code="expedient.estat.enum.${fill.estat}"/></dd>';
							</c:if>
							<c:if test="${fill.document}">
							tooltipTitle += '<dt><spring:message code="contenidor.contingut.info.data"/></dt>' +
									'<dd><fmt:formatDate value="${fill.data}" pattern="dd/MM/yyyy"/></dd>' +
									'<dt><spring:message code="contenidor.contingut.info.versio"/></dt>' +
									'<dd>${fill.darreraVersio.versio}</dd>';
							</c:if>
							tooltipTitle += '</dl>';
							$('#info-fill-${fill.id}').tooltip({
								trigger: 'hover',
								placement: 'auto',
								delay: { show: 1000, hide: 100 },
								html: true,
								title: tooltipTitle
							});
							$('#info-fill-${fill.id}').click(function(e) {
								if ($(this).hasClass('noclick')) {
									$(this).removeClass('noclick');
								} else {
									if ($('#accions-fill-${fill.id}').has(e.target).length == 0) {
										$('#info-fill-${fill.id}').tooltip('destroy');
										window.location = $('#info-fill-${fill.id} a:first').attr('href');
									}
								}
							});
							$('#info-fill-${fill.id} li a').click(function(e) {
								e.stopPropagation();
							});
						</script>
					</li>
				</c:if>
			</c:forEach>
		</ul>
		<%--               --%>
		<%-- /Vista icones --%>
		<%--               --%>
	</c:when>
	<c:when test="${vistaLlistat and fn:length(fills) > 0}">
		<%--               --%>
		<%-- Vista llistat --%>
		<%--               --%>
		<table class="table table-striped table-bordered table-hover">
			<thead>
				<tr>
					<th><spring:message code="contenidor.contingut.info.nom"/></th>
					<th><spring:message code="contenidor.contingut.info.tipus"/></th>
					<th><spring:message code="contenidor.contingut.info.createl"/></th>
					<th><spring:message code="contenidor.contingut.info.creatper"/></th>
					<th width="10%">&nbsp;</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="fill" items="${fills}">
					<tr id="info-fill-${fill.id}" class="element-drag-drop">
						<td>
							<c:choose>
								<c:when test="${fill.expedient}"><span class="fa fa-briefcase" title="<spring:message code="contenidor.contingut.icona.expedient"/>"></span></c:when>
								<c:when test="${fill.carpeta}"><rip:blocIconaCarpeta carpeta="${fill}" petita="${true}"/></c:when>
								<c:when test="${fill.document}"><span class="fa fa-file" title="<spring:message code="contenidor.contingut.icona.document"/>"></span></c:when>
							</c:choose>
							<c:if test="${fill.node and not fill.valid}">&nbsp;<span class="fa fa-exclamation-triangle text-warning"></span></c:if>
							&nbsp;${fill.nom}
						</td>
						<td>
							<c:choose>
								<c:when test="${fill.carpeta}"><spring:message code="carpeta.tipus.enum.${fill.tipus}"/></c:when>
								<c:otherwise>${fill.metaNode.nom}</c:otherwise>
							</c:choose>
						</td>
						<td><fmt:formatDate value="${fill.createdDate}" pattern="dd/MM/yyyy HH:mm"/></td>
						<td>${fill.createdBy.nom}</td>
						<td>
							<rip:blocContenidorAccions className="botons-accions-element" modeLlistat="true" contenidor="${fill}"/>
						</td>
					</tr>
					<script>
						var tooltipTitle = '<dl class="text-left" style="min-width:120px">' +
								'<dt><spring:message code="contenidor.contingut.info.nom"/></dt>' +
								'<dd>${fn:escapeXml(fill.nom)}</dd>' +
								'<dt><spring:message code="contenidor.contingut.info.tipus"/></dt>';
						<c:choose>
							<c:when test="${fill.expedient or fill.document}">
						tooltipTitle += '<dd>${fn:escapeXml(fill.metaNode.nom)}</dd>';
							</c:when>
							<c:when test="${fill.carpeta}">
						tooltipTitle += '<dd><spring:message code="carpeta.tipus.enum.${fill.tipus}"/></dd>';
							</c:when>
						</c:choose>
						tooltipTitle += '<dt><spring:message code="contenidor.contingut.info.createl"/></dt>' +
								'<dd><fmt:formatDate value="${fill.createdDate}" pattern="dd/MM/yyyy HH:mm"/></dd>' +
								'<dt><spring:message code="contenidor.contingut.info.creatper"/></dt>' +
								'<dd>${fn:escapeXml(fill.createdBy.nom)}</dd>';
						<c:if test="${fill.expedient}">
						tooltipTitle += '<dt><spring:message code="contenidor.contingut.info.arxiu"/></dt>' +
								'<dd>${fn:escapeXml(fill.arxiu.nom)}</dd>' +
								'<dt><spring:message code="contenidor.contingut.info.estat"/></dt>' +
								'<dd><spring:message code="expedient.estat.enum.${fill.estat}"/></dd>';
						</c:if>
						<c:if test="${fill.document}">
						tooltipTitle += '<dt><spring:message code="contenidor.contingut.info.data"/></dt>' +
								'<dd><fmt:formatDate value="${fill.data}" pattern="dd/MM/yyyy"/></dd>' +
								'<dt><spring:message code="contenidor.contingut.info.versio"/></dt>' +
								'<dd>${fill.darreraVersio.versio}</dd>';
						</c:if>
						tooltipTitle += '</dl>';
						$('#info-fill-${fill.id} > td:not(:last-child)').tooltip({
							trigger: 'hover',
							placement: 'auto',
							delay: { show: 1000, hide: 100 },
							html: true,
							title: tooltipTitle,
							container: 'body'
						});
					</script>
				</c:forEach>
			</tbody>
		</table>
		<%--                --%>
		<%-- /Vista llistat --%>
		<%--                --%>
	</c:when>
</c:choose>