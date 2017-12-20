/**
 * 
 */
package es.caib.ripea.core.helper;

import java.util.Date;

import javax.annotation.Resource;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.fundaciobit.plugins.utils.Base64;
import org.springframework.stereotype.Component;

import es.caib.ripea.core.api.dto.DocumentNotificacioDto;
import es.caib.ripea.core.api.dto.FitxerDto;
import es.caib.ripea.core.api.dto.LogObjecteTipusEnumDto;
import es.caib.ripea.core.api.dto.LogTipusEnumDto;
import es.caib.ripea.core.entity.DocumentEntity;
import es.caib.ripea.core.entity.DocumentNotificacioEntity;
import es.caib.ripea.core.entity.ExpedientEntity;
import es.caib.ripea.core.entity.InteressatEntity;
import es.caib.ripea.plugin.ciutada.CiutadaExpedientInformacio;
import es.caib.ripea.plugin.ciutada.CiutadaNotificacioEstat;
import es.caib.ripea.plugin.ciutada.CiutadaNotificacioEstat.ZonaperJustificantEstat;
import es.caib.ripea.plugin.ciutada.CiutadaNotificacioResultat;
import es.caib.ripea.plugin.notificacio.NotificacioDocument;
import es.caib.ripea.plugin.notificacio.NotificacioEntregaDeh;
import es.caib.ripea.plugin.notificacio.NotificacioEntregaPostal;
import es.caib.ripea.plugin.notificacio.NotificacioEntregaPostalTipusEnum;
import es.caib.ripea.plugin.notificacio.NotificacioEnviament;
import es.caib.ripea.plugin.notificacio.NotificacioEnviamentEstatEnum;
import es.caib.ripea.plugin.notificacio.NotificacioInformacioEnviament;
import es.caib.ripea.plugin.notificacio.NotificacioPagadorCie;
import es.caib.ripea.plugin.notificacio.NotificacioPagadorPostal;
import es.caib.ripea.plugin.notificacio.NotificacioParametresSeu;
import es.caib.ripea.plugin.notificacio.NotificacioPersona;
import es.caib.ripea.plugin.notificacio.NotificacioServeiTipusEnum;

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
	private DocumentHelper documentHelper;
	@Resource
	private ContingutLogHelper contingutLogHelper;
	@Resource
	private ConversioTipusHelper conversioTipusHelper;


	//////////////////////////////////////////////////////////
	///	Sistra											//////
	//////////////////////////////////////////////////////////
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
						notificacio.getSeuOficiTitol(),
						notificacio.getSeuOficiText(),
						notificacio.getSeuAvisTitol(),
						notificacio.getSeuAvisText(),
						notificacio.getSeuAvisTextMobil(),
						null,//notificacio.getSeuIdioma(),
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
					notificacio.getNotificaReferencia());
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
	
	
	//////////////////////////////////////////////////////////
	///	Notib											//////
	//////////////////////////////////////////////////////////
	public boolean notibNotificacioEnviar(
			DocumentNotificacioEntity notificacio,
			InteressatEntity destinatari) {
		try {
			FitxerDto fitxer = documentHelper.getFitxerAssociat(
					notificacio.getDocument(),
					null);
			String referencia = pluginHelper.notibNotificacioEnviar(
					ConversioTipusHelper.dateToXMLGregorianCalendar(
							notificacio.getCaducitat()),
					notificacio.getAssumpte(),
					notificacio.getObservacions(),
					new NotificacioDocument(
							fitxer.getNom(),
							Base64.encode(fitxer.getContingut()),
							notificacio.isDocumentGenerarCsv(),
							notificacio.getDocumentHash(),
							null,
							notificacio.isDocumentNormalitzat(),
							null),
					notificacio.getEmisorDir3Codi(),
					ConversioTipusHelper.dateToXMLGregorianCalendar(
							notificacio.getEnviamentDataProgramada()),
					notificacio.getEnviamentTipus(),
					new NotificacioEnviament(
							new NotificacioPersona(
									notificacio.getDestinatariNom(),
									notificacio.getDestinatariLlinatge1(),
									notificacio.getDestinatariLlinatge2(),
									notificacio.getDestinatariNif(),
									notificacio.getDestinatariEmail(),
									notificacio.getDestinatariTelefon()),
							new NotificacioEntregaDeh(
									notificacio.getDehNif(), 
									notificacio.getDehObligat()),
							new NotificacioEntregaPostal(
									destinatari.getDomiciliApartatCorreus(),
									destinatari.getDomiciliBloc(),
									destinatari.getDomiciliCie(),
									destinatari.getDomiciliCodiPostal(),
									destinatari.getDomiciliComplement(),
									destinatari.getDomiciliEscala(),
									null,
									null,
									destinatari.getDomiciliLinea1(),
									destinatari.getDomiciliLinea2(),
									destinatari.getDomiciliMunicipiCodiIne(),
									destinatari.getDomiciliNumeracioNumero(),
									null,
									destinatari.getDomiciliPaisCodiIso(),
									destinatari.getDomiciliPlanta(),
									destinatari.getDomiciliPoblacio(),
									destinatari.getDomiciliPorta(),
									destinatari.getDomiciliPortal(),
									destinatari.getDomiciliProvinciaCodi(),
									destinatari.getDomiciliNumeracioPuntKm(),
									NotificacioEntregaPostalTipusEnum.valueOf(
											destinatari.getDomiciliTipusEnum()),
									destinatari.getDomiciliViaNom(),
									destinatari.getDomiciliViaTipus()),
							null,
							NotificacioServeiTipusEnum.valueOf(
									notificacio.getServeiTipus().name()),
							new NotificacioPersona(
									notificacio.getTitularNom(),
									notificacio.getTitularLlinatge1(),
									notificacio.getTitularLlinatge2(),
									notificacio.getTitularNif(),
									notificacio.getTitularEmail(),
									notificacio.getTitularTelefon())),
					new NotificacioPagadorCie(
							ConversioTipusHelper.dateToXMLGregorianCalendar(
									notificacio.getPagadorCieDataVigencia()),
							notificacio.getPagadorCieCodiDir3()),
					new NotificacioPagadorPostal(
							ConversioTipusHelper.dateToXMLGregorianCalendar(
									notificacio.getPagadorCorreusDataVigencia()),
							notificacio.getPagadorCorreusContracteNum(),
							notificacio.getPagadorCorreusCodiDir3(),
							notificacio.getPagadorCorreusCodiClientFacturacio()),
					new NotificacioParametresSeu(
							notificacio.getSeuAvisText(),
							notificacio.getSeuAvisTextMobil(),
							notificacio.getSeuAvisTitol(),
							notificacio.getSeuExpedientIdentificadorEni(),
							notificacio.getSeuExpedientSerieDocumental(),
							notificacio.getSeuExpedientTitol(),
							notificacio.getSeuExpedientUnitatOrganitzativa(),
							notificacio.getSeuIdioma().name(),
							notificacio.getSeuOficiText(),
							notificacio.getSeuOficiTitol(),
							notificacio.getSeuRegistreLlibre(),
							notificacio.getSeuRegistreOficina()),
					notificacio.getProcedimentCodiSia(),
					notificacio.getRetardPostal());
			
			notificacio.updateEnviament(
					true,
					false,
					null,
					referencia);
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
	}

	public boolean notibNotificacioComprovarEstat(
			DocumentNotificacioEntity notificacio) {
		ExpedientEntity expedient = notificacio.getExpedient();
		try {
			NotificacioInformacioEnviament notificacioEstat = pluginHelper.notibNotificacioComprovarEstat(
					notificacio.getNotificaReferencia());
			boolean entregadaORebutjada =
					NotificacioEnviamentEstatEnum.ENTREGADA_OP.equals(notificacioEstat.getEstat()) ||
					NotificacioEnviamentEstatEnum.REBUTJADA.equals(notificacioEstat.getEstat());
			Date recepcio = null;
			if (entregadaORebutjada) {
				DocumentNotificacioDto dto = conversioTipusHelper.convertir(
						notificacio,
						DocumentNotificacioDto.class);
				LogTipusEnumDto logTipus;
				if (NotificacioEnviamentEstatEnum.ENTREGADA_OP.equals(notificacioEstat.getEstat())) {
					recepcio = notificacioEstat.getEstatData().toGregorianCalendar().getTime();
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
	
}
