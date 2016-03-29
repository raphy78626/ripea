/**
 * 
 */
package es.caib.ripea.core.service.ws.bustia;

import javax.annotation.Resource;
import javax.jws.WebService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import es.caib.ripea.core.api.dto.RegistreAnnexFirmaModeEnumDto;
import es.caib.ripea.core.api.dto.RegistreAnnexOrigenEnumDto;
import es.caib.ripea.core.api.dto.RegistreDocumentTipusEnumDto;
import es.caib.ripea.core.api.dto.RegistreDocumentValidesaEnumDto;
import es.caib.ripea.core.api.dto.RegistreInteressatCanalEnumDto;
import es.caib.ripea.core.api.dto.RegistreInteressatDocumentTipusEnumDto;
import es.caib.ripea.core.api.dto.RegistreInteressatTipusEnumDto;
import es.caib.ripea.core.api.dto.RegistreTipusEnumDto;
import es.caib.ripea.core.api.dto.RegistreTransportTipusEnumDto;
import es.caib.ripea.core.entity.RegistreAnnexEntity;
import es.caib.ripea.core.entity.RegistreEntity;
import es.caib.ripea.core.entity.RegistreInteressatEntity;
import es.caib.ripea.core.helper.PluginHelper;
import es.caib.ripea.core.repository.RegistreRepository;
import es.caib.ripea.plugin.registre.RegistreAnnex;
import es.caib.ripea.plugin.registre.RegistreAnotacio;
import es.caib.ripea.plugin.registre.RegistreInteressat;

/**
 * Implementació dels mètodes per al servei de bústia de RIPEA.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@Component
@WebService(
		name = "Bustia",
		serviceName = "BustiaService",
		portName = "BustiaServicePort",
		endpointInterface = "es.caib.ripea.core.service.ws.bustia.BustiaWs",
		targetNamespace = "http://www.caib.es/ripea/ws/bustia")
public class BustiaWsImpl implements BustiaWs {

	@Resource
	private RegistreRepository registreRepository;

	@Resource
	private PluginHelper pluginHelper;



	@Override
	public void enviarContingut(
			String entitat,
			String unitatAdministrativa,
			BustiaContingutTipus tipus,
			String referencia) {
		logger.debug("Processant enviament al servei web de bústia (" +
				"unitatCodi:" + entitat + ", " +
				"unitatAdministrativa:" + unitatAdministrativa + ", " +
				"tipus:" + tipus + ", " +
				"referencia:" + referencia + ")");
		if (BustiaContingutTipus.EXPEDIENT.equals(tipus)) {
			throw new BustiaException(
					"Els enviaments de tipus EXPEDIENT encara no estan suportats");
		} else if (BustiaContingutTipus.DOCUMENT.equals(tipus)) {
			throw new BustiaException(
					"Els enviaments de tipus DOCUMENT encara no estan suportats");
		} else if (BustiaContingutTipus.REGISTRE_ENTRADA.equals(tipus)) {
			RegistreAnotacio anotacio = pluginHelper.registreEntradaConsultar(referencia);
			saveRegistreEntity(anotacio);
		}
	}



	private RegistreEntity saveRegistreEntity(RegistreAnotacio anotacio) {
		RegistreEntity entity = RegistreEntity.getBuilder(
				RegistreTipusEnumDto.ENTRADA,
				anotacio.getNumero(),
				anotacio.getData(),
				anotacio.getIdentificador(),
				anotacio.getOficina(),
				anotacio.getLlibre(),
				anotacio.getAssumpteTipus(),
				anotacio.getIdioma(),
				anotacio.getUsuariNom(),
				anotacio.getDocumentacioFisica()).
		extracte(anotacio.getExtracte()).
		assumpteCodi(anotacio.getAssumpteCodi()).
		assumpteReferencia(anotacio.getAssumpteReferencia()).
		assumpteNumExpedient(anotacio.getAssumpteNumExpedient()).
		transportTipus(RegistreTransportTipusEnumDto.valorAsEnum(anotacio.getTransportTipus())).
		transportNumero(anotacio.getTransportNumero()).
		usuariContacte(anotacio.getUsuariContacte()).
		aplicacioCodi(anotacio.getAplicacioCodi()).
		aplicacioVersio(anotacio.getAplicacioVersio()).
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
		return saved;
	}

	private RegistreInteressatEntity toInteressatEntity(
			RegistreInteressat registreInteressat,
			RegistreEntity registre) {
		RegistreInteressatTipusEnumDto interessatTipus = RegistreInteressatTipusEnumDto.valorAsEnum(registreInteressat.getTipus());
		RegistreInteressatEntity.Builder interessatBuilder;
		switch (interessatTipus) {
		case PERSONA_FIS:
			interessatBuilder = RegistreInteressatEntity.getBuilder(
					interessatTipus,
					RegistreInteressatDocumentTipusEnumDto.valorAsEnum(registreInteressat.getDocumentTipus()),
					registreInteressat.getDocumentNum(),
					registreInteressat.getNom(),
					registreInteressat.getLlinatge1(),
					registreInteressat.getLlinatge2(),
					registre);
			break;
		default: // PERSONA_JUR o ADMINISTRACIO
			interessatBuilder = RegistreInteressatEntity.getBuilder(
					interessatTipus,
					RegistreInteressatDocumentTipusEnumDto.valorAsEnum(registreInteressat.getDocumentTipus()),
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
				RegistreInteressatCanalEnumDto.valorAsEnum(
						registreInteressat.getCanalPreferent())).
		observacions(registreInteressat.getObservacions()).
		build();
		if (registreInteressat.getRepresentant() != null) {
			RegistreInteressat representant = registreInteressat.getRepresentant();
			interessatEntity.updateRepresentant(
					RegistreInteressatTipusEnumDto.valorAsEnum(representant.getTipus()),
					RegistreInteressatDocumentTipusEnumDto.valorAsEnum(representant.getDocumentTipus()),
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
					RegistreInteressatCanalEnumDto.valorAsEnum(representant.getCanalPreferent()));
		}
		return interessatEntity;
	}

	private RegistreAnnexEntity toAnnexEntity(
			RegistreAnnex registreAnnex,
			RegistreEntity registre) {
		RegistreAnnexEntity annexEntity = RegistreAnnexEntity.getBuilder(
				registreAnnex.getTitol(),
				registreAnnex.getFitxerNom(),
				registreAnnex.getFitxerTamany(),
				registreAnnex.getNtiTipoDocumental(),
				RegistreDocumentTipusEnumDto.valorAsEnum(registreAnnex.getTipus()),
				RegistreAnnexOrigenEnumDto.valorAsEnum(registreAnnex.getOrigen()),
				registreAnnex.getDataCaptura(),
				registre).
				fitxerTipusMime(registreAnnex.getFitxerTipusMime()).
				validesa(RegistreDocumentValidesaEnumDto.valorAsEnum(registreAnnex.getValidesa())).
				observacions(registreAnnex.getObservacions()).
				firmaMode(RegistreAnnexFirmaModeEnumDto.valorAsEnum(registreAnnex.getFirmaMode())).
				build();
		return annexEntity;
	}

	private static final Logger logger = LoggerFactory.getLogger(BustiaWsImpl.class);

}
