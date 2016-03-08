
function webutilContextPath() {
	return '/ripea';
}
function webutilModalTancarPath() {
	return webutilContextPath() + '/modal/tancar';
}
function webutilRefreshMissatges() {
	$('#contingut-missatges').load(webutilContextPath() + "/nodeco/missatges");
}

function webutilModalAdjustHeight() {
	$html = $(document.documentElement);
	if ($(window.frameElement).length) {
		$iframe = $(window.frameElement);
		var modalobj = $iframe.parent().parent().parent();
		var taraModal = $('.modal-header', modalobj).outerHeight() + $('.modal-footer', modalobj).outerHeight();
		var maxBodyHeight = $(window.top).height() - taraModal - 62;
		var height = $html.height();
		if (height > maxBodyHeight) {
			$(iframe).height(maxBodyHeight + 'px');
			$('.modal-body', modalobj).css('height', maxBodyHeight + 'px');
			$(iframe).contents().find("body").css('height', maxBodyHeight + 'px');
		} else {
			$iframe.parent().css('height', height + 'px');
			$iframe.css('min-height', height + 'px');
			$iframe.closest('div.modal-body').height(height + 'px');
		}
	}
}

function webutilUrlAmbPrefix(url, prefix) {
	var absolutePath;
	if (url.indexOf('/') != 0)
		absolutePath = window.location.pathname.substring(0, window.location.pathname.lastIndexOf('/') + 1) + url;
	else
		absolutePath = url;
	var prefixSenseBarra = prefix;
	if (prefixSenseBarra.indexOf('/') == 0)
		prefixSenseBarra = prefixSenseBarra.substring(1);
	if (prefixSenseBarra.indexOf('/') == prefixSenseBarra.length - 1)
		prefixSenseBarra = prefixSenseBarra.substring(0, prefixSenseBarra.length - 1);
	return absolutePath.substring(0, webutilContextPath().length) + '/' + prefixSenseBarra + absolutePath.substring(webutilContextPath().length);
}

$(document).ajaxError(function(event, jqxhr, ajaxSettings, thrownError) {
	var message = "Error AJAX: [" + jqxhr.status + "] " + thrownError;
	/*var statusErrorMap = {
			'400': "Server understood the request, but request content was invalid.",
			'401': "Unauthorized access.",
			'403': "Forbidden resource can't be accessed.",
			'500': "Internal server error.",
			'503': "Service unavailable."
	};
	if (jqxhr.status) {
		message = statusErrorMap[jqxhr.status];
		if (!message) {
			message = "Unknown Error: (" + jqxhr.status + ", " + thrownError + ")";
		}
	} else if (thrownError == 'parsererror') {
		message = "Error.\nParsing JSON Request failed.";
	} else if (thrownError == 'timeout') {
		message = "Request Time out.";
	} else if (thrownError == 'abort') {
		message = "Request was aborted by the server";
	} else {
		message = "Unknown Error: (" + jqxhr.status + ", " + thrownError + ")";
	}*/
	alert(message);
});

(function($) {

	$.fn.webutilClonarElementAmbInputs = function(clonId, clonValor) {
		var $contingutOrigen = $(this);
		$contingutOrigen.webutilDestroyInputComponents();
		var $contingutClonat = $contingutOrigen.clone(true);
		$contingutOrigen.webutilEvalInputComponents();
		$contingutClonat.webutilNetejarInputs();
		$contingutClonat.webutilNetejarErrorsCamps();
		var $inputClonat = ($contingutClonat.is(':input')) ? $contingutClonat : $(':input', $contingutClonat);
		$inputClonat.attr('id', clonId);
		if ($inputClonat.attr('type') == 'checkbox') {
			$inputClonat.prop('checked', (clonValor == true));
		} else {
			$inputClonat.css('width', '100%');
			//$contingutClonat.limitEvalInputComponents();
			$inputClonat.val(clonValor);
		}
		return $contingutClonat;
	};
	$.fn.webutilDestroyInputComponents = function() {
	};
	$.fn.webutilEvalInputComponents = function() {
	};

	$.fn.webutilMostrarErrorsCamps = function(errors) {
		var focused = false;
		for (var i = 0; i < errors.length; i++) {
			var $input = $(':input[name="' + errors[i].camp + '"]', this);
			$input.attr('title', errors[i].missatge);
			$input.closest('.form-group').addClass('has-error has-feedback');
			$input.closest('.form-group').append('<span class="fa fa-warning form-control-feedback" aria-hidden="true" style="top:6px"/>');
			if (!focused) {
				$input.focus();
				focused = true;
			}
		}
	}
	$.fn.webutilNetejarErrorsCamps = function() {
		$(':input', this).each(function() {
			$(this).attr('title', '');
			$(this).closest('.form-group').removeClass('has-error has-feedback');
			$('span.form-control-feedback', $(this).closest('.form-group')).remove();
		});
	}

	$.fn.webutilNetejarInputs = function(options) {
		$(this).find('input:text, input:password, input:file, select, textarea').val('');
		$(this).find('input:radio, input:checkbox').removeAttr('checked').removeAttr('selected');
		$(this).find('select.select2-hidden-accessible').val(null).trigger("change");
	}

	$.fn.webutilConfirm = function() {
		$(this).click(function(e) {
			if (confirm($(this).data('confirm'))) {
				return true
			} else {
				e.stopImmediatePropagation();
				return false;
			}
		});
	}
	$.fn.webutilConfirmEval = function() {
		$('[data-confirm]', this).each(function() {
			if (!$(this).attr('data-confirm-eval')) {
				$(this).webutilConfirm();
				$(this).attr('data-confirm-eval', 'true');
			}
		});
	}

	$.fn.webutilAjax = function() {
		$(this).on('click', function() {
			var $element = $(this);
			$.ajax({
				type: "GET",
				url: webutilUrlAmbPrefix($element.attr("href"), '/ajax'),
				timeout: 10000,
				success: function() {
					webutilRefreshMissatges();
					if ($element.closest('.dataTables_wrapper')) {
						var $dataTable = $('table.dataTable', $element.closest('.dataTables_wrapper'));
						$dataTable.webutilDatatable('refresh');
					}
				}
		    });
			return false;
		});
	}
	$.fn.webutilAjaxEval = function() {
		$('[data-toggle="ajax"]', $(this)).each(function() {
			if (!$(this).attr('data-ajax-eval')) {
				$(this).webutilAjax();
				$(this).attr('data-ajax-eval', 'true');
			}
		});
	}

	$.fn.webutilMultifield = function() {
		var $multifield = $(this);
		$multifield.css('display', 'none');
		var multifieldAfegirClick = function(inputValor) {
			var $clon = $multifield.clone();
			$clon.css('display', '');
			$clon.attr('data-multifield-clon', 'true');
			var $buttonAfegir = $('<button class="btn btn-default pull-right"><span class="fa fa-plus"></span></button>');
			$('div', $clon).append($buttonAfegir);
			var $fieldInput = $('div :input:first', $clon);
			$fieldInput.addClass('pull-left');
			$fieldInput.css('width', '93%');
			if (inputValor) {
				$fieldInput.val(inputValor);
			}
			var $puntInsercio = $multifield;
			var $next;
			var esPrimer = true;
			do {
				$next = $puntInsercio.next('div[data-multifield-clon="true"]');
				if ($next.length) {
					esPrimer = false;
					$puntInsercio = $next;
					$puntInsercio.css('margin-bottom', '4px');
					$('button', $puntInsercio).unbind('click');
					$('button span', $puntInsercio).removeClass('fa-plus').addClass('fa-trash-o');
					$('button', $puntInsercio).on('click', multifieldEliminarClick);
				}
			} while ($next.length);
			if (!esPrimer) {
				$('label', $clon).text('');
			}
			$puntInsercio.after($clon);
			$buttonAfegir.on('click', function() {
				multifieldAfegirClick();
				webutilModalAdjustHeight();
				return false;
			});
		}
		var multifieldEliminarClick = function() {
			$(this).closest('div[data-multifield-clon="true"]').remove();
			$('label', $multifield.next('div[data-multifield-clon="true"]')).text($('label', $multifield).text());
			webutilModalAdjustHeight();
			return false;
		}
		var valor = $(':input:first', $multifield).val();
		var separador = ',';
		if (valor && valor.indexOf(separador) != -1) {
			var parts = valor.split(separador);
			var i;
			for (i = 0; i < parts.length; i++) {
				multifieldAfegirClick(parts[i]);
			}
		} else {
			multifieldAfegirClick();
		}
	}
	$.fn.webutilMultifieldEval = function() {
		$('[data-toggle="multifield"]', $(this)).each(function() {
			if (!$(this).attr('data-multifield-eval')) {
				$(this).webutilMultifield();
				$(this).attr('data-multifield-eval', 'true');
			}
		});
	}

	$(document).ready(function() {
		$('[data-confirm]', $(this)).each(function() {
			if (!$(this).attr('data-confirm-eval')) {
				$(this).webutilConfirm();
				$(this).attr('data-confirm-eval', 'true');
			}
		});
		$('[data-toggle="ajax"]', $(this)).each(function() {
			if (!$(this).attr('data-ajax-eval')) {
				$(this).webutilAjax();
				$(this).attr('data-ajax-eval', 'true');
			}
		});
		$('[data-toggle="multifield"]', $(this)).each(function() {
			if (!$(this).attr('data-multifield-eval')) {
				$(this).webutilMultifield();
				$(this).attr('data-multifield-eval', 'true');
			}
		});
	});

}(jQuery));
