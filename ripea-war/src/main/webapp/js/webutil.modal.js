// Basat en http://stefangabos.ro/jquery/jquery-plugin-boilerplate-revisited/
(function($) {

	$.webutilModal = function(element, options) {
		var defaults = {
			adjustHeight: true,
			maximized: false,
			refreshMissatges: true,
			reloadPage: false,
			refreshDatatable: false,
			elementBotons: "#modal-botons",
			elementForm: "#modal-form",
			elementTancarData: "modal-cancel"
		}
		var $element = $(element), element = element;
		var plugin = this;
		plugin.settings = {}
		plugin.serverParams = [];
		plugin.init = function() {
			plugin.settings = $.extend(defaults, $element.data(), options);
			$element.click(function(event) {
				var elementPerEvaluar = $element;
				if (elementPerEvaluar.prop("tagName") == 'TR' && event.target.tagName != 'TD') {
					elementPerEvaluar = $(event.target.tagName);
				}
				var obrirNovaFinestra = false;
				if ((elementPerEvaluar.attr('href') || elementPerEvaluar.data('href')) && elementPerEvaluar.data('toggle') == 'modal') {
					obrirNovaFinestra = true;
				}
				if (obrirNovaFinestra) {
					var href = elementPerEvaluar.attr('href');
					if (!href)
						href = elementPerEvaluar.data('href');
					if (event.which != 2) {
						var dataTableId = plugin.settings.datatableId;
						if (!dataTableId && elementPerEvaluar.closest('.dataTables_wrapper')) {
							dataTableId = $('table.dataTable', elementPerEvaluar.closest('.dataTables_wrapper')).attr('id');
						}
						if (dataTableId) {
							plugin.settings.refreshDatatable = true;
						}
						var modalDivId = (plugin.settings.modalId) ? plugin.settings.modalId : "modal_" + (new Date().getTime());
						var modalData = '';
						if (plugin.settings.maximized) {
							modalData += ' data-maximized="true"';
						}
						if ($('#' + modalDivId).length == 0 ) {
							$('body').append(
								'<div id="' + modalDivId + '"' + modalData + '>' +
								'	<div class="modal" tabindex="-1" role="dialog" aria-labelledby="" aria-hidden="true">' +
								'		<div class="modal-dialog modal-lg">' +
								'			<div class="modal-content">' +
								'				<div class="modal-header">' +
								'					<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>' +
								'					<h4 class="modal-title"></h4>' +
								'				</div>' +
								'				<div class="modal-body" style="padding:0">' +
								'					<iframe frameborder="0" height="10" width="100%"></iframe>' +
								'				</div>' +
								'			</div>' +
								'		</div>' +
								'	</div>' +
								'</div>');
							elementPerEvaluar.data("modal-id", modalDivId);
							$('#' + modalDivId).webutilModalShow({
								adjustHeight: plugin.settings.adjustHeight,
								maximized: plugin.settings.maximized,
								refreshMissatges: plugin.settings.refreshMissatges,
								refreshDatatable: plugin.settings.refreshDatatable,
								reloadPage: plugin.settings.reloadPage,
								elementBotons: plugin.settings.elementBotons,
								elementForm: plugin.settings.elementForm,
								elementTancarData: plugin.settings.elementTancarData,
								contentUrl: webutilUrlAmbPrefix(href, '/modal'),
								dataTableId: dataTableId
							});
						} else {
							$('#' + modalDivId).webutilModalShow({
								adjustHeight: plugin.settings.adjustHeight,
								maximized: plugin.settings.maximized,
								refreshMissatges: plugin.settings.refreshMissatges,
								refreshDatatable: plugin.settings.refreshDatatable,
								reloadPage: plugin.settings.reloadPage,
								elementBotons: plugin.settings.elementBotons,
								elementForm: plugin.settings.elementForm,
								elementTancarData: plugin.settings.elementTancarData,
								dataTableId: dataTableId
							});
						}
					} else {
						window.open(href, '_blank');
					}
					return false;
				}
			});
		}
		// Mètodes públics
		plugin.adjustHeight = function() {
			var modalId = $(element).data("modal-id");
			var $modalElement = $('#' + modalId);
			var iframe = $('iframe', $modalElement);
			var height = $modalElement.contents().find("html").height();
			$(iframe).parent().css('height', height + 17 + 'px');
			$(iframe).css('min-height', height + 'px');
			$modalElement.height(height + 17 + 'px');
		};
		// Mètodes privats
		// Inicialització del plugin
        plugin.init();
	}

	$.fn.webutilModalShow = function(settings) {
		return this.filter("div").each(function() {
			var modalobj = $('div.modal', this);
			if (!modalobj.data('modal-configurada')) {
				var iframe = $('iframe', modalobj);
				modalobj.on('show.bs.modal', function () {
					iframe.empty();
					if (settings.height)
						iframe.css('height', '' + settings.height + 'px');
					iframe.attr("src", settings.contentUrl);
					iframe.load(function() {
						// Copiar el titol de la modal
						var titol = $(this).contents().find("title").html();
						$('.modal-header h4', $(this).parent().parent()).html(titol);
						// Afegir padding-top al body
						$('body', $(iframe).contents()).css('padding-top', '12px');
						// Copiar botons
						var dataBotons = $('body', $(iframe).contents()).data('modal-botons');
						var modalBotons = (dataBotons) ? $(dataBotons, $(iframe).contents()) : $(settings.elementBotons, $(iframe).contents());
						if (modalBotons.length) {
							$('.modal-body', $(iframe).parent().parent()).after('<div class="modal-footer"></div>');
							$('.btn', modalBotons).each(function(index) {
								var element = $(this);
								var clon = element.clone();
								if (element.data(settings.elementTancarData)) {
									clon.on('click', function () {
										$(iframe).parent().parent().parent().parent().data(settings.elementTancarData, 'true');
										$(iframe).parent().parent().parent().parent().modal('hide');
										return false;
									});
								} else {
									clon.on('click', function () {
										if ($(this).prop("tagName") == 'A') {
											$(iframe).attr('src', $(this).attr('href'));
										} else {
											element.click();
										}
										return false;
									});
								}
								$('.modal-footer', $(iframe).parent().parent()).append(clon);
							});
							modalBotons.hide();
						}
						// Ajustar alçada per canvi de pipella
						var $pipelles = $('.nav-tabs', $(iframe).contents());
						if ($pipelles.length) {
							$('a[data-toggle="tab"]', $(iframe).contents()).on('click', function (e) {
								webutilModalAdjustHeight($(iframe));
							});
						}
						// Evaluar URL del formulari
						var dataForm = $('body', $(iframe).contents()).data('modal-form');
						var modalForm = (dataForm) ? $(dataForm, $(iframe).contents()) : $(settings.elementForm, $(iframe).contents());
						if (modalForm.length) {
							modalForm.attr('action', webutilUrlAmbPrefix(modalForm.attr('action'), '/modal'));
						}
						if (settings.maximized) {
							// Maximitzar
							$('.modal-dialog', modalobj).css('width', '98%');
							$('.modal-dialog', modalobj).css('top', '10px');
							$('.modal-dialog', modalobj).css('margin', 'auto');
							iframe.attr('height', '100%');
							var contentHeight = $(iframe).contents().find("html").outerHeight();
							var modalobj = $(iframe).parent().parent().parent();
							var taraModal = $('.modal-header', modalobj).outerHeight() + $('.modal-footer', modalobj).outerHeight();
							var maxBodyHeight = $(window.top).height() - taraModal - 20;
							$(iframe).height(maxBodyHeight + 'px');
							$('.modal-body', modalobj).css('height', maxBodyHeight + 'px');
							$(iframe).contents().find("body").css('height', maxBodyHeight + 'px');
						} else if (settings.adjustHeight) {
							var modalobj = $(iframe).parent().parent().parent();
							var taraModal = $('.modal-header', modalobj).outerHeight() + $('.modal-footer', modalobj).outerHeight();
							var maxBodyHeight = $(window.top).height() - taraModal - 62;
							var height = $(this).contents().find("html").height();
							if (height > maxBodyHeight) {
								$(iframe).height(maxBodyHeight + 'px');
								$('.modal-body', modalobj).css('height', maxBodyHeight + 'px');
								$(iframe).contents().find("body").css('height', maxBodyHeight + 'px');
							} else {
								$(iframe).parent().css('height', height + 'px');
								$(iframe).css('min-height', height + 'px');
								$(iframe).closest('div.modal-body').height(height + 'px');
							}
						}
					});
				});
				iframe.on('load', function () {
					$('.modal-footer', modalobj).remove();
					var pathname = this.contentDocument.location.pathname;
					if (pathname == webutilModalTancarPath()) {
						if (settings.reloadPage) {
							modalobj.on('hide.bs.modal', function() {
								window.location.reload(true);
							});
						} else {
							if (settings.refreshMissatges) {
								webutilRefreshMissatges();
							}
							if (settings.refreshDatatable) {
								$('#' + settings.dataTableId).webutilDatatable('refresh');
							}
						}
						$('button.close', $(this).closest('.modal-dialog')).trigger('click');
					}
				});
				modalobj.data('modal-configurada', true);
			}
			$('.modal-body iframe *', modalobj).remove();
			modalobj.modal('show');
		});
	};

	$.fn.webutilModal = function(options) {
		var pluginName = 'webutilModal';
        return this.each(function() {
            if (undefined == $(this).data(pluginName)) {
                var plugin = new $.webutilModal(this, options);
                $(this).data(pluginName, plugin);
            } else if (options && typeof options !== 'object') {
            }
        });
    }

	$.fn.webutilModalEval = function() {
		$('[data-toggle="modal"]', this).each(function() {
			if (!$(this).attr('data-modal-eval')) {
				$(this).webutilModal();
				$(this).attr('data-modal-eval', 'true');
			}
		});
	}

	$(document).ready(function() {
		$('[data-toggle="modal"]', $(this)).each(function() {
			if (!$(this).attr('data-modal-eval')) {
				$(this).webutilModal();
				$(this).attr('data-modal-eval', 'true');
			}
		});
	});

}(jQuery));
