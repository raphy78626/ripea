<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%><%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%><%@ attribute name="carpeta" required="true" rtexprvalue="true" type="java.lang.Object"%><%@ attribute name="petita" required="false" rtexprvalue="true" type="java.lang.Boolean"%><c:set var="iconaTipusEsborrany" value="clock-o"/><c:set var="textStyle6em"><c:if test="${petita}"> style="font-size:.55em"</c:if></c:set><c:set var="textStyle2x"><c:if test="${not petita}"> fa-2x</c:if></c:set><c:set var="htmlIconaCarpeta"><span class="fa-stack"${textStyle6em} title="<spring:message code="contenidor.contingut.icona.carpeta"/>"><i class="fa fa-folder fa-stack-2x"></i><i class="fa fa-${iconaTipusEsborrany} fa-stack-1x fa-inverse"></i></span></c:set><c:choose><c:when test="${carpeta.tipus == 'ESBORRANY'}">${htmlIconaCarpeta}</c:when><c:otherwise><span class="fa fa-folder ${textStyle2x}" title="<spring:message code="contenidor.contingut.icona.carpeta"/>"></span></c:otherwise></c:choose>