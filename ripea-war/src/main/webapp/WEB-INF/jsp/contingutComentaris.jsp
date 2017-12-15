<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib tagdir="/WEB-INF/tags/ripea" prefix="rip"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<c:set var="titol"><spring:message code="contingut.comentaris.titol"/></c:set>
<html>
<head>
	<title>${titol}: ${contingut.nom}</title>
	<rip:modalHead/>
	
	<style>
		.enviar-comentari {
			width: 84%
		}
		.comentari-bocata {
			width: 85%;
		}
		.comentari-autor {
			color: #999;
		}
		div.comentari-autor {
			margin-bottom: 6px;
		}
		.comentari-propi {
			background-color: #dfeecf;
		}
		#comentaris_content {
		 	overflow: hidden;
		    height: 500px;
		    max-height: 500px;
		    overflow-y: scroll;
		    margin-bottom: 15px;
		}
	</style>
	
	<script src="<c:url value="/js/webutil.common.js"/>"></script>
	<script type="text/javascript">
		$(document).ready(function() {
			$('button.enviar-comentari').click(function() {
				var text = $("#comentari_text").val();
				if (text != undefined && text != "") {
					enviarLlistarComentaris(text);
				}
			});
			
			enviarLlistarComentaris("");
		});
		
		function enviarLlistarComentaris(text) {
			$.ajax({
				type: 'POST',
				url: "<c:url value="/contingut/${contingut.id}/comentaris/publicar"/>",
				data: {
					text: text
				},
				success: function(data) {
					pintarComentaris(data);
					$("#comentari_text").val("");
					webutilModalAdjustHeight();
				},
				error: function() {
					console.log("error enviant comentari..");
				}
			});
		}
		
		function pintarComentaris(comentaris) {
			$("#comentaris_content").empty();
			for (var comentariIndex in comentaris) {
				var comentari = comentaris[comentariIndex];
				
				var fons = "";
				var pull = "left";
				var propi = comentari.createdBy.codi == "${usuariActual.codi}"; 
				if (propi) {
					pull = "right"
					fons = "comentari-propi";
				}
					
				var comentariHtml = '<div class="well comentari-bocata pull-' + pull + ' ' + fons + '">';
				if (!propi)
					comentariHtml += '<div class="comentari-autor"><strong>' + comentari.createdBy.nom + '</strong></div>';
				
				comentariHtml +='<p>' + comentari.text + '</p>' +
				'<small class="pull-right comentari-autor">' + comentari.createdDateAmbFormat + '</small>' +
				'</div>';
				$("#comentaris_content").append(comentariHtml);
			}
			scrollFinal();
		}
		
		function scrollFinal() {
			var contenidor = $('#comentaris_content');
			var height = contenidor[0].scrollHeight;
			contenidor.scrollTop(height);
		}
	</script>
	
</head>
<body>
	<div id="comentaris_content" class="col-xs-12">
	</div>
	
	<div class="col-xs-10">
		<input id="comentari_text" class="form-control" placeholder="<spring:message code="contingut.comentaris.text.placeholder"/>" maxlength="1024"/>
	</div>
	<div class="col-xs-2">
		<button class="btn btn-success enviar-comentari"><span class="fa fa-paper-plane-o"></span>&nbsp;<spring:message code="comu.boto.enviar"/></button>
	</div>
	
	<div class="col-xs-12" style="height:10px">
	</div>

	<div id="modal-botons" class="well">
		<a href="<c:url value="/bustiaUser"/>" class="btn btn-default" data-modal-cancel="true"><spring:message code="comu.boto.tancar"/></a>
	</div>
</body>
</html>
