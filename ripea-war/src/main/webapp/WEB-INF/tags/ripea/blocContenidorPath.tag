<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib tagdir="/WEB-INF/tags/ripea" prefix="rip"%>
<%@ attribute name="contenidor" required="true" rtexprvalue="true" type="java.lang.Object"%>
<c:if test="${not contenidor.escriptori}">
	<ol class="breadcrumb">
		<c:forEach var="contenidorPath" items="${contenidor.path}" varStatus="status">
			<li>
				<c:choose>
					<c:when test="${contenidorPath.escriptori}"><a href="${contenidorPath.id}"><span class="fa fa-desktop"></span> <spring:message code="contenidor.contingut.path.escriptori"/></a></c:when>
					<c:otherwise>
						<c:choose>
							<c:when test="${contenidorPath.expedient}"><a href="<c:url value="/contenidor/${contenidorPath.id}"/>"><span class="fa fa-briefcase" title="<spring:message code="contenidor.contingut.icona.expedient"/>"></span> ${contenidorPath.nom}</a></c:when>
							<c:when test="${contenidorPath.carpeta}"><a href="<c:url value="/contenidor/${contenidorPath.id}"/>"><rip:blocIconaCarpeta carpeta="${contenidorPath}" petita="${true}"/> ${contenidorPath.nom}</a></c:when>
							<c:when test="${contenidorPath.document}"><a href="<c:url value="/contenidor/${contenidorPath.id}"/>"><span class="fa fa-file" title="<spring:message code="contenidor.contingut.icona.document"/>"></span> ${contenidorPath.nom}</a></c:when>
							<c:when test="${contenidorPath.arxiv}">
								<c:choose>
									<c:when test="${status.first}"><a href="<c:url value="/arxiuUser/unitat/${contenidorPath.unitatCodi}"/>"><span class="fa fa-sitemap" title="<spring:message code="contenidor.contingut.icona.unitat"/>"></span> ${contenidorPath.nom}</a></c:when>
									<c:otherwise><a href="<c:url value="/arxiuUser/${contenidorPath.id}/expedient"/>"><span class="fa fa-archive" title="<spring:message code="contenidor.contingut.icona.arxiu"/>"></span> ${contenidorPath.nom}</a></c:otherwise>
								</c:choose>
							</c:when>
							<c:when test="${contenidorPath.bustia}">
								<c:choose>
									<c:when test="${status.first}"><span class="fa fa-sitemap" title="<spring:message code="contenidor.contingut.icona.unitat"/>"></span> ${contenidorPath.nom}</c:when>
									<c:otherwise><span class="fa fa-inbox" title="<spring:message code="contenidor.contingut.icona.bustia"/>"></span> ${contenidorPath.nom}</c:otherwise>
								</c:choose>
							</c:when>
						</c:choose>
					</c:otherwise>
				</c:choose>
			</li>
		</c:forEach>
		<li class="active">
			<c:choose>
				<c:when test="${contenidor.expedient}"><span class="fa fa-briefcase" title="<spring:message code="contenidor.contingut.icona.expedient"/>"></span></c:when>
				<c:when test="${contenidor.carpeta}"><rip:blocIconaCarpeta carpeta="${contenidor}" petita="${true}"/></c:when>
				<c:when test="${contenidor.document}"><span class="fa fa-file" title="<spring:message code="contenidor.contingut.icona.document"/>"></span></c:when>
				<c:when test="${contenidor.arxiv}"><span class="fa fa-archive" title="<spring:message code="contenidor.contingut.icona.arxiu"/>"></span></c:when>
				<c:when test="${contenidor.bustia}"><span class="fa fa-inbox" title="<spring:message code="contenidor.contingut.icona.bustia"/>"></span></c:when>
			</c:choose>
			${contenidor.nom}
		</li>
	</ol>
</c:if>
