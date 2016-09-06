<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib tagdir="/WEB-INF/tags/ripea" prefix="rip"%>
<%@ attribute name="contingut" required="true" rtexprvalue="true" type="java.lang.Object"%>
<c:if test="${not contingut.escriptori}">
	<ol class="breadcrumb">
		<c:forEach var="contingutPath" items="${contingut.path}" varStatus="status">
			<li>
				<c:choose>
					<c:when test="${contingutPath.escriptori}"><a href="${contingutPath.id}"><span class="fa fa-desktop"></span> <spring:message code="contingut.path.escriptori"/></a></c:when>
					<c:otherwise>
						<c:choose>
							<c:when test="${contingutPath.expedient}"><a href="<c:url value="/contingut/${contingutPath.id}"/>"><span class="fa fa-briefcase" title="<spring:message code="contingut.icona.expedient"/>"></span> ${contingutPath.nom}</a></c:when>
							<c:when test="${contingutPath.carpeta}"><a href="<c:url value="/contingut/${contingutPath.id}"/>"><rip:blocIconaCarpeta carpeta="${contingutPath}" petita="${true}"/> ${contingutPath.nom}</a></c:when>
							<c:when test="${contingutPath.document}"><a href="<c:url value="/contingut/${contingutPath.id}"/>"><span class="fa fa-file" title="<spring:message code="contingut.icona.document"/>"></span> ${contingutPath.nom}</a></c:when>
							<c:when test="${contingutPath.arxiv}">
								<c:choose>
									<c:when test="${status.first}"><span class="fa fa-sitemap" title="<spring:message code="contenidor.contingut.icona.unitat"/>"></span> ${contingutPath.nom}</c:when>
									<c:otherwise><span class="fa fa-archive" title="<spring:message code="contenidor.contingut.icona.arxiu"/>"></span> ${contingutPath.nom}</c:otherwise>
								</c:choose>
							</c:when>
							<c:when test="${contingutPath.bustia}">
								<c:choose>
									<c:when test="${status.first}"><span class="fa fa-sitemap" title="<spring:message code="contingut.icona.unitat"/>"></span> ${contingutPath.nom}</c:when>
									<c:otherwise><span class="fa fa-inbox" title="<spring:message code="contingut.icona.bustia"/>"></span> ${contingutPath.nom}</c:otherwise>
								</c:choose>
							</c:when>
						</c:choose>
					</c:otherwise>
				</c:choose>
			</li>
		</c:forEach>
		<li class="active">
			<c:choose>
				<c:when test="${contingut.expedient}"><span class="fa fa-briefcase" title="<spring:message code="contingut.icona.expedient"/>"></span></c:when>
				<c:when test="${contingut.carpeta}"><rip:blocIconaCarpeta carpeta="${contingut}" petita="${true}"/></c:when>
				<c:when test="${contingut.document}"><span class="fa fa-file" title="<spring:message code="contingut.icona.document"/>"></span></c:when>
				<c:when test="${contingut.arxiv}"><span class="fa fa-archive" title="<spring:message code="contingut.icona.arxiu"/>"></span></c:when>
				<c:when test="${contingut.bustia}"><span class="fa fa-inbox" title="<spring:message code="contingut.icona.bustia"/>"></span></c:when>
			</c:choose>
			${contingut.nom}
		</li>
	</ol>
</c:if>
