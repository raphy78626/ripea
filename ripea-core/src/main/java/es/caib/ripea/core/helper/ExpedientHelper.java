/**
 * 
 */
package es.caib.ripea.core.helper;

import java.util.Date;

import javax.annotation.Resource;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.stereotype.Component;

import es.caib.ripea.core.api.dto.DocumentEstatEnumDto;
import es.caib.ripea.core.api.dto.LogTipusEnumDto;
import es.caib.ripea.core.api.exception.SistemaExternException;
import es.caib.ripea.core.entity.DocumentEntity;
import es.caib.ripea.core.entity.DocumentNotificacioEntity;
import es.caib.ripea.core.entity.DocumentPortafirmesEntity;
import es.caib.ripea.core.entity.ExpedientEntity;
import es.caib.ripea.core.entity.InteressatEntity;
import es.caib.ripea.plugin.ciutada.CiutadaExpedientInformacio;
import es.caib.ripea.plugin.ciutada.CiutadaNotificacioEstat;
import es.caib.ripea.plugin.ciutada.CiutadaNotificacioEstat.ZonaperJustificantEstat;
import es.caib.ripea.plugin.ciutada.CiutadaNotificacioResultat;
import es.caib.ripea.plugin.portafirmes.PortafirmesDocument;
import es.caib.ripea.plugin.portafirmes.PortafirmesPrioritatEnum;

/**
 * Mètodes comuns per a gestionar expedients.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@Component
public class ExpedientHelper {

	@Resource
	private PluginHelper pluginHelper;
	@Resource
	private ContingutLogHelper contingutLogHelper;



	public boolean ciutadaNotificacioEnviar(
			DocumentNotificacioEntity notificacio,
			InteressatEntity destinatari) {
		ExpedientEntity expedient = notificacio.getExpedient();
		if (!expedient.isSistraPublicat()) {
			try {
				CiutadaExpedientInformacio expedientInfo = pluginHelper.ciutadaExpedientCrear(
						expedient,
						destinatari);
				expedient.updateSistra(
						true,
						expedient.getMetaExpedient().getUnitatAdministrativa(),
						expedientInfo.getClau());
			} catch (Exception ex) {
				Throwable rootCause = ExceptionUtils.getRootCause(ex);
				if (rootCause == null) rootCause = ex;
				notificacio.updateEnviament(
						true,
						true,
						ExceptionUtils.getStackTrace(rootCause),
						null);
			}
		}
		if (expedient.isSistraPublicat()) {
			try {
				CiutadaNotificacioResultat notificacioResultat = pluginHelper.ciutadaNotificacioEnviar(
						expedient,
						destinatari,
						notificacio.getOficiTitol(),
						notificacio.getOficiText(),
						notificacio.getAvisTitol(),
						notificacio.getAvisText(),
						notificacio.getAvisTextSms(),
						notificacio.getIdioma(),
						true,
						notificacio.getAnnexos());
				notificacio.updateEnviament(
						true,
						false,
						null,
						notificacioResultat.getRegistreNumero());
				return true;
			} catch (Exception ex) {
				Throwable rootCause = ExceptionUtils.getRootCause(ex);
				if (rootCause == null) rootCause = ex;
				notificacio.updateEnviament(
						true,
						true,
						ExceptionUtils.getStackTrace(rootCause),
						null);
				return false;
			}
		} else {
			return false;
		}
	}

	public boolean ciutadaNotificacioComprovarEstat(
			DocumentNotificacioEntity notificacio) {
		ExpedientEntity expedient = notificacio.getExpedient();
		try {
			CiutadaNotificacioEstat notificacioEstat = pluginHelper.ciutadaNotificacioComprovarEstat(
					expedient,
					notificacio.getRegistreNumero());
			boolean entregadaORebutjada = !ZonaperJustificantEstat.PENDENT.equals(notificacioEstat.getEstat());
			Date recepcio = null;
			if (ZonaperJustificantEstat.ENTREGADA.equals(notificacioEstat.getEstat())) {
				recepcio = notificacioEstat.getData();
			}
			notificacio.updateProcessament(
					true,
					false,
					null,
					recepcio,
					entregadaORebutjada);
			return true;
		} catch (Exception ex) {
			Throwable rootCause = ExceptionUtils.getRootCause(ex);
			if (rootCause == null) rootCause = ex;
			notificacio.updateProcessament(
					true,
					true,
					ExceptionUtils.getStackTrace(rootCause),
					null,
					false);
			return false;
		}
	}

	public boolean portafirmesEnviar(
			DocumentPortafirmesEntity documentPortafirmes) {
		DocumentEntity document = documentPortafirmes.getDocument();
		boolean estampatCorrecte = true;
		if (pluginHelper.portafirmesEnviarDocumentEstampat() && document.getCustodiaUrl() == null) {
			try {
				String custodiaUrl = pluginHelper.custodiaReservarUrl(document);
				document.updateInformacioCustodia(
						document.getCustodiaData(),
						document.getCustodiaId(),
						custodiaUrl);
			} catch (Exception ex) {
				Throwable rootCause = ExceptionUtils.getRootCause(ex);
				if (rootCause == null) rootCause = ex;
				documentPortafirmes.updateEnviament(
						true,
						true,
						ExceptionUtils.getStackTrace(rootCause),
						null);
				estampatCorrecte = false;
			}
		}
		if (estampatCorrecte) {
			try {
				String portafirmesId = pluginHelper.portafirmesUpload(
						document,
						documentPortafirmes.getAssumpte(),
						PortafirmesPrioritatEnum.valueOf(documentPortafirmes.getPrioritat().name()),
						documentPortafirmes.getDataCaducitat(),
						documentPortafirmes.getDocumentTipus(),
						documentPortafirmes.getResponsables(),
						documentPortafirmes.getFluxTipus(),
						documentPortafirmes.getFluxId(),
						null);
				documentPortafirmes.updateEnviament(
						true,
						false,
						null,
						portafirmesId);
				return true;
			} catch (Exception ex) {
				Throwable rootCause = ExceptionUtils.getRootCause(ex);
				if (rootCause == null) rootCause = ex;
				documentPortafirmes.updateEnviament(
						true,
						true,
						ExceptionUtils.getStackTrace(rootCause),
						null);
				return false;
			}
		} else {
			return false;
		}
	}

	public boolean portafirmesProcessarFirma(
			DocumentPortafirmesEntity documentPortafirmes) {
		DocumentEntity document = documentPortafirmes.getDocument();
		document.updateEstat(
				DocumentEstatEnumDto.FIRMAT);
		PortafirmesDocument portafirmesDocument = null;
		try {
			portafirmesDocument = pluginHelper.portafirmesDownload(
					documentPortafirmes);
		} catch (SistemaExternException ex) {
			Throwable rootCause = ExceptionUtils.getRootCause(ex);
			if (rootCause == null) rootCause = ex;
			documentPortafirmes.updateProcessament(
					true,
					true,
					ExceptionUtils.getStackTrace(rootCause),
					false);
			return false;
		}
		if (portafirmesDocument.isCustodiat()) {
			// El document ja ha estat custodiat pel portafirmes
			document.updateInformacioCustodia(
					new Date(),
					portafirmesDocument.getCustodiaId(),
					portafirmesDocument.getCustodiaUrl());
			documentPortafirmes.updateProcessament(
					true,
					false,
					null,
					false);
			return true;
		} else {
			// Envia el document a custòdia
			try {
				String custodiaDocumentId = pluginHelper.custodiaEnviarDocumentFirmat(
						document,
						document.getMetaDocument().getPortafirmesCustodiaTipus(),
						portafirmesDocument.getArxiuNom(),
						portafirmesDocument.getArxiuContingut());
				document.updateInformacioCustodia(
						new Date(),
						portafirmesDocument.getCustodiaId(),
						document.getCustodiaUrl());
				documentPortafirmes.updateProcessament(
						true,
						false,
						null,
						false);
				document.updateEstat(
						DocumentEstatEnumDto.CUSTODIAT);
				contingutLogHelper.log(
						documentPortafirmes.getDocument(),
						LogTipusEnumDto.CUSTODIA,
						null,
						null,
						custodiaDocumentId,
						null,
						true,
						true);
				return true;
			} catch (SistemaExternException ex) {
				Throwable rootCause = ExceptionUtils.getRootCause(ex);
				if (rootCause == null) rootCause = ex;
				documentPortafirmes.updateProcessament(
						true,
						true,
						ExceptionUtils.getStackTrace(rootCause),
						false);
				return false;
			}
		}
	}
	

}
