/**
 * 
 */
package es.caib.ripea.core.helper;

import java.util.Date;

import javax.annotation.Resource;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import es.caib.ripea.core.api.dto.DocumentNotificacioDto;
import es.caib.ripea.core.api.dto.LogObjecteTipusEnumDto;
import es.caib.ripea.core.api.dto.LogTipusEnumDto;
import es.caib.ripea.core.entity.DocumentNotificacioEntity;
import es.caib.ripea.core.entity.ExpedientEntity;
import es.caib.ripea.core.entity.InteressatEntity;
import es.caib.ripea.core.service.ExpedientEnviamentServiceImpl;
import es.caib.ripea.plugin.ciutada.CiutadaExpedientInformacio;
import es.caib.ripea.plugin.ciutada.CiutadaNotificacioEstat;
import es.caib.ripea.plugin.ciutada.CiutadaNotificacioEstat.ZonaperJustificantEstat;
import es.caib.ripea.plugin.ciutada.CiutadaNotificacioResultat;
import es.caib.ripea.plugin.notib.NotibNotificacioResultat;

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
	@Resource
	private ConversioTipusHelper conversioTipusHelper;



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
			if (entregadaORebutjada) {
				DocumentNotificacioDto dto = conversioTipusHelper.convertir(
						notificacio,
						DocumentNotificacioDto.class);
				LogTipusEnumDto logTipus;
				if (ZonaperJustificantEstat.ENTREGADA.equals(notificacioEstat.getEstat())) {
					recepcio = notificacioEstat.getData();
					logTipus = LogTipusEnumDto.NOTIFICACIO_ENTREGADA;
					
				} else {
					logTipus = LogTipusEnumDto.NOTIFICACIO_REBUTJADA;
				}
				contingutLogHelper.log(
						expedient,
						LogTipusEnumDto.MODIFICACIO,
						notificacio,
						LogObjecteTipusEnumDto.NOTIFICACIO,
						logTipus,
						dto.getDestinatariNomSencerRepresentantAmbDocument(),
						notificacio.getAssumpte(),
						false,
						false);
				contingutLogHelper.log(
						notificacio.getDocument(),
						LogTipusEnumDto.MODIFICACIO,
						notificacio,
						LogObjecteTipusEnumDto.NOTIFICACIO,
						logTipus,
						dto.getDestinatariNomSencerRepresentantAmbDocument(),
						notificacio.getAssumpte(),
						false,
						false);
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

	/*****************************/
	/**** NOTIFICACIONS NOTIB ****/
	/*****************************/
	public boolean notibNotificacioEnviar(
			DocumentNotificacioEntity notificacio,
			InteressatEntity destinatari) {
		ExpedientEntity expedient = notificacio.getExpedient();
			try {
				
				NotibNotificacioResultat notificacioResultat = pluginHelper.notibNotificacioEnviar(
						expedient, 
						destinatari, 
						notificacio.getConcepte(), 
						notificacio.getOficiTitol(), 
						notificacio.getOficiText(), 
						notificacio.getAvisTitol(), 
						notificacio.getAvisText(), 
						notificacio.getAvisTextSms(), 
						notificacio.getIdioma(), 
						true, 
						notificacio.getDocument());
				
				notificacio.updateReferenciaEnviament(
						true,
						false,
						null,
						notificacioResultat.getReferencia());
				return true;
			} catch (Exception ex) {
				Throwable rootCause = ExceptionUtils.getRootCause(ex);
				if (rootCause == null) rootCause = ex;
				notificacio.updateEnviament(
						true,
						true,
						ExceptionUtils.getStackTrace(rootCause),
						null);
				
				logger.error("Error enviant la notificació: " + ex.getMessage());
				
				return false;
			}
	}
	
	/*************************/
	private static final Logger logger = LoggerFactory.getLogger(ExpedientHelper.class);
}
