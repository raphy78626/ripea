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

import es.caib.ripea.core.api.exception.ValidationException;
import es.caib.ripea.core.api.registre.RegistreAnotacio;
import es.caib.ripea.core.api.registre.RegistreTipusEnum;
import es.caib.ripea.core.api.service.BustiaService;
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
	BustiaService bustiaService;

	/*@Resource
	private EntitatRepository entitatRepository;
	@Resource
	private RegistreRepository registreRepository;
	@Resource
	private BustiaRepository bustiaRepository;
	@Resource
	private PluginHelper pluginHelper;
	@Resource
	private CacheHelper cacheHelper;
	@Resource
	private UnitatOrganitzativaHelper unitatOrganitzativaHelper;*/



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
		bustiaService.registreAnotacioCrear(
				entitat,
				RegistreTipusEnum.ENTRADA,
				unitatAdministrativa,
				registreEntrada);
		/*EntitatEntity entitatEntity = entitatRepository.findByCodi(entitat);
		if (entitatEntity == null) {
			throw new ValidationException(
					"No existeix cap entitat amb el codi especificat (" +
					"codi=" + entitat + ")");
		}
		UnitatOrganitzativaDto unitat = unitatOrganitzativaHelper.findPerEntitatAndCodi(
				entitat,
				unitatAdministrativa);
		if (unitat == null) {
			throw new ValidationException(
					"No existeix cap unitat administrativa amb el codi especificat (" +
					"codi=" + unitatAdministrativa + ")");
		}
		RegistreEntity registreRepetit = registreRepository.findByTipusAndUnitatAdministrativaAndNumeroAndDataAndOficinaCodiAndLlibreCodi(
				RegistreTipusEnum.ENTRADA.getValor(),
				unitatAdministrativa,
				registreEntrada.getNumero(),
				registreEntrada.getData(),
				registreEntrada.getOficinaCodi(),
				registreEntrada.getLlibreCodi());
		if (registreRepetit != null) {
			throw new ValidationException(
					"Aquesta anotació ja ha estat donada d'alta a l'aplicació (" +
					"tipus=" + RegistreTipusEnum.ENTRADA.getValor() + ", " +
					"unitatAdministrativa=" + unitatAdministrativa + ", " +
					"numero=" + registreEntrada.getNumero() + ", " +
					"data=" + registreEntrada.getData() + ", " +
					"oficina=" + registreEntrada.getOficinaCodi() + ", " +
					"llibre=" + registreEntrada.getLlibreCodi() + ")");
		}
		saveRegistreEntity(
				RegistreTipusEnum.ENTRADA,
				unitatAdministrativa,
				registreEntrada,
				findAndCreateIfNotExistsBustiaPerDefectePerUnitatAdministrativa(
						entitatEntity,
						unitat));*/
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



	/*private BustiaEntity findAndCreateIfNotExistsBustiaPerDefectePerUnitatAdministrativa(
			EntitatEntity entitat,
			UnitatOrganitzativaDto unitat) {
		String unitatOrganitzativaCodi = unitat.getCodi();
		BustiaEntity bustia = bustiaRepository.findByEntitatAndUnitatCodiAndPerDefecteTrue(
				entitat,
				unitatOrganitzativaCodi);
		// Si la bústia no existeix la crea
		if (bustia == null) {
			// Cerca la bústia superior
			BustiaEntity bustiaPare = bustiaRepository.findByEntitatAndUnitatCodiAndPareNull(
					entitat,
					unitatOrganitzativaCodi);
			// Si la bústia superior no existeix la crea
			if (bustiaPare == null) {
				bustiaPare = bustiaRepository.save(
						BustiaEntity.getBuilder(
								entitat,
								unitat.getDenominacio(),
								unitatOrganitzativaCodi,
								null).build());
			}
			// Crea la nova bústia
			BustiaEntity entity = BustiaEntity.getBuilder(
					entitat,
					unitat.getDenominacio() + " (default)",
					unitatOrganitzativaCodi,
					bustiaPare).build();
			entity.updatePerDefecte(true);
			bustia = bustiaRepository.save(entity);
		}
		return bustia;
	}

	private RegistreEntity saveRegistreEntity(
			RegistreTipusEnum tipus,
			String unitatAdministrativa,
			RegistreAnotacio anotacio,
			BustiaEntity bustia) {
		RegistreEntity entity = RegistreEntity.getBuilder(
				tipus,
				unitatAdministrativa,
				anotacio.getNumero(),
				anotacio.getData(),
				anotacio.getIdentificador(),
				anotacio.getOficinaCodi(),
				anotacio.getLlibreCodi(),
				anotacio.getAssumpteTipusCodi(),
				anotacio.getIdiomaCodi(),
				bustia).
		entitatCodi(anotacio.getEntitatCodi()).
		entitatDescripcio(anotacio.getEntitatDescripcio()).
		oficinaDescripcio(anotacio.getOficinaDescripcio()).
		llibreDescripcio(anotacio.getLlibreDescripcio()).
		extracte(anotacio.getExtracte()).
		assumpteTipusDescripcio(anotacio.getAssumpteTipusDescripcio()).
		assumpteCodi(anotacio.getAssumpteCodi()).
		assumpteDescripcio(anotacio.getAssumpteDescripcio()).
		referencia(anotacio.getReferencia()).
		expedientNumero(anotacio.getExpedientNumero()).
		idiomaDescripcio(anotacio.getIdiomaDescripcio()).
		transportTipusCodi(anotacio.getTransportTipusCodi()).
		transportTipusDescripcio(anotacio.getTransportTipusDescripcio()).
		transportNumero(anotacio.getTransportNumero()).
		usuariCodi(anotacio.getUsuariCodi()).
		usuariNom(anotacio.getUsuariNom()).
		usuariContacte(anotacio.getUsuariContacte()).
		aplicacioCodi(anotacio.getAplicacioCodi()).
		aplicacioVersio(anotacio.getAplicacioVersio()).
		documentacioFisicaCodi(anotacio.getDocumentacioFisicaCodi()).
		documentacioFisicaDescripcio(anotacio.getDocumentacioFisicaDescripcio()).
		observacions(anotacio.getObservacions()).
		exposa(anotacio.getExposa()).
		solicita(anotacio.getSolicita()).
		build();
		if (anotacio.getInteressats() != null) {
			for (RegistreInteressat registreInteressat: anotacio.getInteressats()) {
				entity.getInteressats().add(
						toInteressatEntity(
								registreInteressat,
								entity));
			}
		}
		if (anotacio.getAnnexos() != null) {
			for (RegistreAnnex registreAnnex: anotacio.getAnnexos()) {
				entity.getAnnexos().add(
						toAnnexEntity(
								registreAnnex,
								entity));
			}
		}
		RegistreEntity saved = registreRepository.save(entity);
		cacheHelper.evictElementsPendentsBustia(
				bustia.getEntitat(),
				bustia);
		return saved;
	}

	private RegistreInteressatEntity toInteressatEntity(
			RegistreInteressat registreInteressat,
			RegistreEntity registre) {
		RegistreInteressatTipusEnum interessatTipus = RegistreInteressatTipusEnum.valorAsEnum(registreInteressat.getTipus());
		RegistreInteressatEntity.Builder interessatBuilder;
		switch (interessatTipus) {
		case PERSONA_FIS:
			interessatBuilder = RegistreInteressatEntity.getBuilder(
					interessatTipus,
					RegistreInteressatDocumentTipusEnum.valorAsEnum(registreInteressat.getDocumentTipus()),
					registreInteressat.getDocumentNum(),
					registreInteressat.getNom(),
					registreInteressat.getLlinatge1(),
					registreInteressat.getLlinatge2(),
					registre);
			break;
		default: // PERSONA_JUR o ADMINISTRACIO
			interessatBuilder = RegistreInteressatEntity.getBuilder(
					interessatTipus,
					RegistreInteressatDocumentTipusEnum.valorAsEnum(registreInteressat.getDocumentTipus()),
					registreInteressat.getDocumentNum(),
					registreInteressat.getRaoSocial(),
					registre);
			break;
		}
		RegistreInteressatEntity interessatEntity = interessatBuilder.
		pais(registreInteressat.getPais()).
		provincia(registreInteressat.getProvincia()).
		municipi(registreInteressat.getMunicipi()).
		adresa(registreInteressat.getAdresa()).
		codiPostal(registreInteressat.getCodiPostal()).
		email(registreInteressat.getEmail()).
		telefon(registreInteressat.getTelefon()).
		emailHabilitat(registreInteressat.getEmailHabilitat()).
		canalPreferent(
				RegistreInteressatCanalEnum.valorAsEnum(
						registreInteressat.getCanalPreferent())).
		observacions(registreInteressat.getObservacions()).
		build();
		if (registreInteressat.getRepresentant() != null) {
			RegistreInteressat representant = registreInteressat.getRepresentant();
			interessatEntity.updateRepresentant(
					RegistreInteressatTipusEnum.valorAsEnum(representant.getTipus()),
					RegistreInteressatDocumentTipusEnum.valorAsEnum(representant.getDocumentTipus()),
					representant.getDocumentNum(),
					representant.getNom(),
					representant.getLlinatge1(),
					representant.getLlinatge2(),
					representant.getRaoSocial(),
					representant.getPais(),
					representant.getProvincia(),
					representant.getMunicipi(),
					representant.getAdresa(),
					representant.getCodiPostal(),
					representant.getEmail(),
					representant.getTelefon(),
					representant.getEmailHabilitat(),
					RegistreInteressatCanalEnum.valorAsEnum(representant.getCanalPreferent()));
		}
		return interessatEntity;
	}

	private RegistreAnnexEntity toAnnexEntity(
			RegistreAnnex registreAnnex,
			RegistreEntity registre) {
		RegistreAnnexEntity annexEntity = RegistreAnnexEntity.getBuilder(
				registreAnnex.getTitol(),
				registreAnnex.getFitxerNom(),
				(registreAnnex.getFitxerContingut() != null) ? registreAnnex.getFitxerContingut().length : -1,
				registreAnnex.getFitxerGestioDocumentalId(),
				registreAnnex.getDataCaptura(),
				RegistreAnnexOrigenEnum.valorAsEnum(registreAnnex.getOrigenCiutadaAdmin()),
				RegistreAnnexNtiTipusDocumentEnum.valorAsEnum(registreAnnex.getNtiTipusDocument()),
				RegistreAnnexSicresTipusDocumentEnum.valorAsEnum(registreAnnex.getSicresTipusDocument()),
				registre).
				fitxerTipusMime(registreAnnex.getFitxerTipusMime()).
				localitzacio(registreAnnex.getLocalitzacio()).
				ntiElaboracioEstat(RegistreAnnexElaboracioEstatEnum.valorAsEnum(registreAnnex.getNtiElaboracioEstat())).
				firmaCsv(registreAnnex.getFirmaCsv()).
				observacions(registreAnnex.getObservacions()).
				build();
		return annexEntity;
	}*/

	private static final Logger logger = LoggerFactory.getLogger(BustiaV1WsServiceImpl.class);

}
