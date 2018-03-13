/**
 * 
 */
package es.caib.ripea.core.service.ws.bustia;

import javax.annotation.Resource;
import javax.jws.WebParam;
import javax.jws.WebService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import es.caib.ripea.core.api.dto.DocumentNtiTipoFirmaEnumDto;
import es.caib.ripea.core.api.exception.ValidationException;
import es.caib.ripea.core.api.registre.Firma;
import es.caib.ripea.core.api.registre.RegistreAnnex;
import es.caib.ripea.core.api.registre.RegistreAnotacio;
import es.caib.ripea.core.api.registre.RegistreTipusEnum;
import es.caib.ripea.core.api.service.BustiaService;
import es.caib.ripea.core.api.service.ReglaService;
import es.caib.ripea.core.api.service.ws.BustiaV1WsService;

/**
 * Implementació dels mètodes per al servei d'enviament de
 * continguts a bústies.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@Component
@WebService(
		name = "BustiaV1",
		serviceName = "BustiaV1Service",
		portName = "BustiaV1ServicePort",
		endpointInterface = "es.caib.ripea.core.api.service.ws.BustiaV1WsService",
		targetNamespace = "http://www.caib.es/ripea/ws/v1/bustia")
public class BustiaV1WsServiceImpl implements BustiaV1WsService {

	@Resource
	private ReglaService reglaService;
	@Resource
	private BustiaService bustiaService;



	@Override
	public void enviarAnotacioRegistreEntrada(
			String entitat,
			String unitatAdministrativa,
			RegistreAnotacio registreEntrada) {
		String registreNumero = (registreEntrada != null) ? registreEntrada.getIdentificador() : null;
		logger.debug(
				"Processant enviament d'anotació de registre d'entrada al servei web de bústia (" +
				"entitat:" + entitat + ", " +
				"unitatAdministrativa:" + unitatAdministrativa + ", " +
				"numero:" + registreNumero + ")");
		validarAnotacioRegistre(registreEntrada);
		bustiaService.registreAnotacioCrear(
				entitat,
				RegistreTipusEnum.ENTRADA,
				unitatAdministrativa,
				registreEntrada);
	}

	@Override
	public void enviarDocument(
			@WebParam(name="entitat") String entitat,
			@WebParam(name="unitatAdministrativa") String unitatAdministrativa,
			@WebParam(name="referenciaDocument") String referenciaDocument) {
		logger.debug(
				"Processant enviament de document al servei web de bústia (" +
				"unitatCodi:" + entitat + ", " +
				"unitatAdministrativa:" + unitatAdministrativa + ", " +
				"referenciaDocument:" + referenciaDocument + ")");
		throw new ValidationException(
				"Els enviaments de tipus DOCUMENT encara no estan suportats");
	}

	@Override
	public void enviarExpedient(
			@WebParam(name="entitat") String entitat,
			@WebParam(name="unitatAdministrativa") String unitatAdministrativa,
			@WebParam(name="referenciaExpedient") String referenciaExpedient) {
		logger.debug(
				"Processant enviament d'expedient al servei web de bústia (" +
				"unitatCodi:" + entitat + ", " +
				"unitatAdministrativa:" + unitatAdministrativa + ", " +
				"referenciaExpedient:" + referenciaExpedient + ")");
		throw new ValidationException(
				"Els enviaments de tipus EXPEDIENT encara no estan suportats");
	}



	private void validarAnotacioRegistre(
			RegistreAnotacio registreEntrada) {
		if (registreEntrada.getNumero() == null) {
			throw new ValidationException(
					"Es obligatori especificar un valor pel camp 'numero'");
		}
		if (registreEntrada.getData() == null) {
			throw new ValidationException(
					"Es obligatori especificar un valor pel camp 'data'");
		}
		if (registreEntrada.getIdentificador() == null) {
			throw new ValidationException(
					"Es obligatori especificar un valor pel camp 'identificador'");
		}
		if (registreEntrada.getExtracte() == null) {
			throw new ValidationException(
					"Es obligatori especificar un valor pel camp 'extracte'");
		}
		if (registreEntrada.getOficinaCodi() == null) {
			throw new ValidationException(
					"Es obligatori especificar un valor pel camp 'oficinaCodi'");
		}
		if (registreEntrada.getLlibreCodi() == null) {
			throw new ValidationException(
					"Es obligatori especificar un valor pel camp 'llibreCodi'");
		}
		if (registreEntrada.getAssumpteTipusCodi() == null) {
			throw new ValidationException(
					"Es obligatori especificar un valor pel camp 'assumpteTipusCodi'");
		}
		if (registreEntrada.getIdiomaCodi() == null) {
			throw new ValidationException(
					"Es obligatori especificar un valor pel camp 'idiomaCodi'");
		}
		
		if (registreEntrada.getAnnexos() != null && registreEntrada.getAnnexos().size() > 0)
			for (RegistreAnnex annex : registreEntrada.getAnnexos())
				validarAnnex(annex);
	}

	/** Valida que l'annex:
	 * Tingui el nom informat.
	 * Valida les seves firmes.
	 * @param annex
	 * @throws ValidationException
	 */
	private void validarAnnex(RegistreAnnex annex) throws ValidationException{
		if (annex.getFitxerNom() == null) {
			throw new ValidationException(
					"Es obligatori especificar un valor pel camp 'fitxerNom' per l'annex");
		}
		if (annex.getFirmes() != null && annex.getFirmes().size() > 0)
			for (Firma firma : annex.getFirmes())
				validaFirma(annex, firma);

		
	}

	/** Valida la firma. Valida:
	 * El tipus de firma ha d'estar reconegut.
	 * Si el tipus és TF04 l'annex ha de tenir el contingut informat.
	 * Si el tipus és TF05 la firma ha de tenir el contingut informat
	 * @param annex
	 * @param firma
	 */
	private void validaFirma(RegistreAnnex annex, Firma firma) {
		if (firma.getTipus() == null)
			throw new ValidationException(
					"Es obligatori especificar un valor pel camp 'tipus' de la firma");
		DocumentNtiTipoFirmaEnumDto firmaTipus = null;
		try {
			firmaTipus = DocumentNtiTipoFirmaEnumDto.valueOf(firma.getTipus());
		} catch(Exception e) {
			throw new ValidationException(
					"El tipus de firma '" + firma.getTipus() + "' no es reconeix com a vàlid.");
		}
		// Validacions segons el tipus de firma
		if (DocumentNtiTipoFirmaEnumDto.TF04.equals(firmaTipus)) {
			if (annex.getFitxerContingut() == null)
				throw new ValidationException(
						"El contingut de l'annex ha d'estar informat quan conté una firma del tipus TF04");
			if (firma.getContingut() == null)
				throw new ValidationException(
						"El contingut de la firma ha d'estar informat pel tipus TF04");
		} else if (DocumentNtiTipoFirmaEnumDto.TF05.equals(firmaTipus)) {
			if (firma.getContingut() == null)
				throw new ValidationException(
						"El contingut de la firma ha d'estar informat pel tipus TF05");
		}		
	}

	private static final Logger logger = LoggerFactory.getLogger(BustiaV1WsServiceImpl.class);

}
