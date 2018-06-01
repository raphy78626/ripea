/**
 * 
 */
package es.caib.ripea.core.helper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import es.caib.plugins.arxiu.api.ContingutArxiu;
import es.caib.plugins.arxiu.api.Document;
import es.caib.plugins.arxiu.api.FirmaPerfil;
import es.caib.ripea.core.api.dto.ArxiuFirmaDto;
import es.caib.ripea.core.api.dto.ArxiuFirmaPerfilEnumDto;
import es.caib.ripea.core.api.dto.ArxiuFirmaTipusEnumDto;
import es.caib.ripea.core.api.dto.BackofficeTipusEnumDto;
import es.caib.ripea.core.api.dto.DocumentNtiTipoFirmaEnumDto;
import es.caib.ripea.core.api.dto.FitxerDto;
import es.caib.ripea.core.api.dto.ReglaTipusEnumDto;
import es.caib.ripea.core.api.dto.UnitatOrganitzativaDto;
import es.caib.ripea.core.api.exception.AplicarReglaException;
import es.caib.ripea.core.api.exception.BustiaServiceException;
import es.caib.ripea.core.api.exception.ValidationException;
import es.caib.ripea.core.api.registre.Firma;
import es.caib.ripea.core.api.registre.RegistreAnnex;
import es.caib.ripea.core.api.registre.RegistreAnnexElaboracioEstatEnum;
import es.caib.ripea.core.api.registre.RegistreAnnexNtiTipusDocumentEnum;
import es.caib.ripea.core.api.registre.RegistreAnnexOrigenEnum;
import es.caib.ripea.core.api.registre.RegistreAnnexSicresTipusDocumentEnum;
import es.caib.ripea.core.api.registre.RegistreAnotacio;
import es.caib.ripea.core.api.registre.RegistreInteressat;
import es.caib.ripea.core.api.registre.RegistreInteressatCanalEnum;
import es.caib.ripea.core.api.registre.RegistreInteressatDocumentTipusEnum;
import es.caib.ripea.core.api.registre.RegistreInteressatTipusEnum;
import es.caib.ripea.core.api.registre.RegistreProcesEstatEnum;
import es.caib.ripea.core.api.registre.RegistreTipusEnum;
import es.caib.ripea.core.entity.BustiaEntity;
import es.caib.ripea.core.entity.EntitatEntity;
import es.caib.ripea.core.entity.RegistreAnnexEntity;
import es.caib.ripea.core.entity.RegistreAnnexFirmaEntity;
import es.caib.ripea.core.entity.RegistreEntity;
import es.caib.ripea.core.entity.RegistreInteressatEntity;
import es.caib.ripea.core.entity.ReglaEntity;
import es.caib.ripea.core.repository.RegistreRepository;

/**
 * Mètodes comuns per a aplicar regles.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@Component
public class RegistreHelper {

	@Resource
	private UnitatOrganitzativaHelper unitatOrganitzativaHelper;
	@Resource
	private AlertaHelper alertaHelper;
	@Resource
	private MessageHelper messageHelper;
	@Resource
	private BustiaHelper bustiaHelper;
	@Resource
	private PluginHelper pluginHelper;
	@Resource
	private ReglaHelper reglaHelper;
	
	@Resource
	private RegistreRepository registreRepository;

	public RegistreAnotacio fromRegistreEntity(
			RegistreEntity entity) {
		RegistreAnotacio anotacio = new RegistreAnotacio();
		anotacio.setNumero(entity.getNumero());
		anotacio.setData(entity.getData());
		anotacio.setIdentificador(entity.getIdentificador());
		anotacio.setEntitatCodi(entity.getEntitatCodi());
		anotacio.setEntitatDescripcio(entity.getEntitatDescripcio());
		anotacio.setOficinaCodi(entity.getOficinaCodi());
		anotacio.setOficinaDescripcio(entity.getOficinaDescripcio());
		anotacio.setDataOrigen(entity.getDataOrigen());
		anotacio.setOficinaOrigenCodi(entity.getOficinaOrigenCodi());
		anotacio.setOficinaOrigenDescripcio(entity.getOficinaOrigenDescripcio());
		anotacio.setLlibreCodi(entity.getLlibreCodi());
		anotacio.setLlibreDescripcio(entity.getLlibreDescripcio());
		anotacio.setExtracte(entity.getExtracte());
		anotacio.setAssumpteTipusCodi(entity.getAssumpteTipusCodi());
		anotacio.setAssumpteTipusDescripcio(entity.getAssumpteTipusDescripcio());
		anotacio.setAssumpteCodi(entity.getAssumpteCodi());
		anotacio.setAssumpteDescripcio(entity.getAssumpteDescripcio());
		anotacio.setReferencia(entity.getReferencia());
		anotacio.setExpedientNumero(entity.getExpedientNumero());
		anotacio.setIdiomaCodi(entity.getIdiomaCodi());
		anotacio.setIdiomaDescripcio(entity.getIdiomaDescripcio());
		anotacio.setTransportTipusCodi(entity.getTransportTipusCodi());
		anotacio.setTransportTipusDescripcio(entity.getTransportTipusDescripcio());
		anotacio.setTransportNumero(entity.getTransportNumero());
		anotacio.setUsuariCodi(entity.getUsuariCodi());
		anotacio.setUsuariNom(entity.getUsuariNom());
		anotacio.setUsuariContacte(entity.getUsuariContacte());
		anotacio.setAplicacioCodi(entity.getAplicacioCodi());
		anotacio.setAplicacioVersio(entity.getAplicacioVersio());
		anotacio.setDocumentacioFisicaCodi(entity.getDocumentacioFisicaCodi());
		anotacio.setDocumentacioFisicaDescripcio(entity.getDocumentacioFisicaDescripcio());
		anotacio.setObservacions(entity.getObservacions());
		anotacio.setExposa(entity.getExposa());
		anotacio.setSolicita(entity.getSolicita());
		if (!entity.getInteressats().isEmpty()) {
			List<RegistreInteressat> interessats = new ArrayList<RegistreInteressat>();
			for (RegistreInteressatEntity interessat: entity.getInteressats()) {
				interessats.add(
						fromInteressatEntity(
								interessat));
			}
			anotacio.setInteressats(interessats);
		}
		if (!entity.getAnnexos().isEmpty()) {
			List<RegistreAnnex> annexos = new ArrayList<RegistreAnnex>();
			for (RegistreAnnexEntity annex: entity.getAnnexos()) {
				annexos.add(
						fromAnnexEntity(
								annex,
								anotacio));
			}
			anotacio.setAnnexos(annexos);
		}
		return anotacio;
	}

	public RegistreEntity toRegistreEntity(
			EntitatEntity entitat,
			RegistreTipusEnum tipus,
			String unitatAdministrativa,
			RegistreAnotacio anotacio,
			ReglaEntity regla) {
		// Obté la unitat organitzativa per guardar la descripció a l'anotació del registre
		UnitatOrganitzativaDto unitat = unitatOrganitzativaHelper.findPerEntitatAndCodi(
				entitat.getCodi(),
				unitatAdministrativa);
		// Construeix l'anotació
		RegistreEntity entity = RegistreEntity.getBuilder(
				entitat,
				tipus,
				unitatAdministrativa,
				unitat != null? unitat.getDenominacio() : null,
				anotacio.getNumero(),
				anotacio.getData(),
				anotacio.getIdentificador(),
				anotacio.getExtracte(),
				anotacio.getOficinaCodi(),
				anotacio.getLlibreCodi(),
				anotacio.getAssumpteTipusCodi(),
				anotacio.getIdiomaCodi(),
				(regla != null || (anotacio.getAnnexos() != null && !anotacio.getAnnexos().isEmpty())) ? RegistreProcesEstatEnum.PENDENT : RegistreProcesEstatEnum.NO_PROCES,
				null).
		entitatCodi(anotacio.getEntitatCodi()).
		entitatDescripcio(anotacio.getEntitatDescripcio()).
		oficinaDescripcio(anotacio.getOficinaDescripcio()).
		llibreDescripcio(anotacio.getLlibreDescripcio()).
		assumpteTipusDescripcio(anotacio.getAssumpteTipusDescripcio()).
		assumpteCodi(anotacio.getAssumpteCodi()).
		assumpteDescripcio(anotacio.getAssumpteDescripcio()).
		referencia(anotacio.getReferencia()).
		expedientNumero(anotacio.getExpedientNumero()).
		numeroOrigen(anotacio.getNumeroOrigen()).
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
		regla(regla).
		oficinaOrigen(anotacio.getDataOrigen(), anotacio.getOficinaOrigenCodi(), anotacio.getOficinaOrigenDescripcio()).
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
		
		if (anotacio.getJustificant() != null && anotacio.getJustificant().getFitxerArxiuUuid() != null) {
			entity.updateJustificantUuid(anotacio.getJustificant().getFitxerArxiuUuid());
		}
		
		return entity;
	}
	
	public byte[] getAnnexArxiuContingut(String nomArxiu) {
		String pathName = PropertiesHelper.getProperties().getProperty("es.caib.ripea.bustia.contingut.documents.dir");
		
		Path path = Paths.get(pathName + "/" + nomArxiu);
		try {
			byte[] data = Files.readAllBytes(path);
			return data;
		} catch (IOException e) {
			return null;
		}
	}

	public List<ArxiuFirmaDto> convertirFirmesAnnexToArxiuFirmaDto(
			RegistreAnnexEntity annex,
			byte[] firmaRipeaContingut) {
		List<ArxiuFirmaDto> firmes = null;
		if (annex.getFirmes() != null) {
			firmes = new ArrayList<ArxiuFirmaDto>();
			for (RegistreAnnexFirmaEntity annexFirma: annex.getFirmes()) {
				byte[] firmaContingut = null;
				
				if (annexFirma.getGesdocFirmaId() != null) {
					ByteArrayOutputStream baos_fir = new ByteArrayOutputStream();
					pluginHelper.gestioDocumentalGet(
							annexFirma.getGesdocFirmaId(), 
						PluginHelper.GESDOC_AGRUPACIO_ANOTACIONS_REGISTRE_FIR_TMP, 
						baos_fir);
					firmaContingut = baos_fir.toByteArray();
				} else if(firmaRipeaContingut != null) {
					firmaContingut = firmaRipeaContingut;
				}
				
				ArxiuFirmaDto firma = new ArxiuFirmaDto();
				if ("TF01".equalsIgnoreCase(annexFirma.getTipus())) {
					firma.setTipus(ArxiuFirmaTipusEnumDto.CSV);
				} else if ("TF02".equalsIgnoreCase(annexFirma.getTipus())) {
					firma.setTipus(ArxiuFirmaTipusEnumDto.XADES_DET);
				} else if ("TF03".equalsIgnoreCase(annexFirma.getTipus())) {
					firma.setTipus(ArxiuFirmaTipusEnumDto.XADES_ENV);
				} else if ("TF04".equalsIgnoreCase(annexFirma.getTipus())) {
					firma.setTipus(ArxiuFirmaTipusEnumDto.CADES_DET);
				} else if ("TF05".equalsIgnoreCase(annexFirma.getTipus())) {
					firma.setTipus(ArxiuFirmaTipusEnumDto.CADES_ATT);
				} else if ("TF06".equalsIgnoreCase(annexFirma.getTipus())) {
					firma.setTipus(ArxiuFirmaTipusEnumDto.PADES);
				} else if ("TF07".equalsIgnoreCase(annexFirma.getTipus())) {
					firma.setTipus(ArxiuFirmaTipusEnumDto.SMIME);
				} else if ("TF08".equalsIgnoreCase(annexFirma.getTipus())) {
					firma.setTipus(ArxiuFirmaTipusEnumDto.ODT);
				} else if ("TF09".equalsIgnoreCase(annexFirma.getTipus())) {
					firma.setTipus(ArxiuFirmaTipusEnumDto.OOXML);
				}
				firma.setPerfil(
						ArxiuFirmaPerfilEnumDto.valueOf(annexFirma.getPerfil()));
				firma.setFitxerNom(annexFirma.getFitxerNom());
				firma.setTipusMime(annexFirma.getTipusMime());
				firma.setCsvRegulacio(annexFirma.getCsvRegulacio());
				firma.setAutofirma(annexFirma.isAutofirma());
				firma.setContingut(firmaContingut);
				firmes.add(firma);
			}
		}
		return firmes;
	}



	private RegistreInteressat fromInteressatEntity(
			RegistreInteressatEntity interessatEntity) {
		RegistreInteressat interessat = new RegistreInteressat();
		if (interessatEntity.getTipus() != null)
			interessat.setTipus(interessatEntity.getTipus().getValor());
		if (interessatEntity.getDocumentTipus() != null)
			interessat.setDocumentTipus(interessatEntity.getDocumentTipus().getValor());
		interessat.setDocumentNum(interessatEntity.getDocumentNum());
		interessat.setNom(interessatEntity.getNom());
		interessat.setLlinatge1(interessatEntity.getLlinatge1());
		interessat.setLlinatge2(interessatEntity.getLlinatge2());
		interessat.setRaoSocial(interessatEntity.getRaoSocial());
		interessat.setPais(interessatEntity.getPais());
		interessat.setProvincia(interessatEntity.getProvincia());
		interessat.setMunicipi(interessatEntity.getMunicipi());
		interessat.setAdresa(interessatEntity.getAdresa());
		interessat.setCodiPostal(interessatEntity.getCodiPostal());
		interessat.setEmail(interessatEntity.getEmail());
		interessat.setTelefon(interessatEntity.getTelefon());
		interessat.setEmailHabilitat(interessatEntity.getEmailHabilitat());
		if (interessatEntity.getCanalPreferent() != null)
			interessat.setCanalPreferent(interessatEntity.getCanalPreferent().getValor());
		interessat.setObservacions(interessatEntity.getObservacions());
		if (interessatEntity.getRepresentant() != null) {
			interessat.setRepresentant(
					fromInteressatEntity(interessatEntity.getRepresentant()));
		}
		return interessat;
	}

	private RegistreAnnex fromAnnexEntity(
			RegistreAnnexEntity annexEntity,
			RegistreAnotacio registre) {
		RegistreAnnex annex = new RegistreAnnex();
		annex.setId(annexEntity.getId());
		annex.setTitol(annexEntity.getTitol());
		annex.setFitxerNom(annexEntity.getFitxerNom());
		annex.setFitxerTamany(annexEntity.getFitxerTamany());
		annex.setFitxerTipusMime(annexEntity.getFitxerTipusMime());
		annex.setFitxerArxiuUuid(annexEntity.getFitxerArxiuUuid());
		annex.setEniDataCaptura(annexEntity.getDataCaptura());
		annex.setLocalitzacio(annexEntity.getLocalitzacio());
		if (annexEntity.getOrigenCiutadaAdmin() != null)
			annex.setEniOrigen(annexEntity.getOrigenCiutadaAdmin().getValor());
		if (annexEntity.getNtiTipusDocument() != null)
			annex.setEniTipusDocumental(annexEntity.getNtiTipusDocument().getValor());
		if (annexEntity.getSicresTipusDocument() != null)
			annex.setSicresTipusDocument(annexEntity.getSicresTipusDocument().getValor());
		if (annexEntity.getNtiElaboracioEstat() != null)
			annex.setEniEstatElaboracio(annexEntity.getNtiElaboracioEstat().getValor());
		annex.setObservacions(annexEntity.getObservacions());
		annex.setTimestamp(annexEntity.getTimestamp());
		annex.setValidacioOCSP(annexEntity.getValidacioOCSP());
		return annex;
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
				registreAnnex.getFitxerTamany(),
				registreAnnex.getFitxerArxiuUuid(),
				registreAnnex.getEniDataCaptura(),
				RegistreAnnexOrigenEnum.valorAsEnum(registreAnnex.getEniOrigen()),
				RegistreAnnexNtiTipusDocumentEnum.valorAsEnum(registreAnnex.getEniTipusDocumental()),
				RegistreAnnexSicresTipusDocumentEnum.valorAsEnum(registreAnnex.getSicresTipusDocument()),
				registre).
				fitxerTipusMime(registreAnnex.getFitxerTipusMime()).
				localitzacio(registreAnnex.getLocalitzacio()).
				ntiElaboracioEstat(RegistreAnnexElaboracioEstatEnum.valorAsEnum(registreAnnex.getEniEstatElaboracio())).
				observacions(registreAnnex.getObservacions()).
				build();
		if (registreAnnex.getFirmes() != null && registreAnnex.getFirmes().size() > 0) {
			for (Firma firma: registreAnnex.getFirmes()) {
				annexEntity.getFirmes().add(
						toFirmaEntity(
								firma,
								annexEntity));
			}
		}
		return annexEntity;
	}

	private RegistreAnnexFirmaEntity toFirmaEntity(
			Firma firma,
			RegistreAnnexEntity annex) {
		RegistreAnnexFirmaEntity firmaEntity = RegistreAnnexFirmaEntity.getBuilder(
				firma.getTipus(),
				firma.getPerfil(),
				firma.getFitxerNom(),
				firma.getTipusMime(),
				firma.getCsvRegulacio(),
				false,
				annex).build();
		return firmaEntity;
	}
	
	//procés i distribució d'anotacions
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void distribuirAnotacioPendent(Long anotacioId) {
		
		RegistreEntity anotacio = registreRepository.findOne(anotacioId);
		
		BustiaEntity bustia = bustiaHelper.findBustiaDesti(
				anotacio.getEntitat(),
				anotacio.getUnitatAdministrativa());
		
		boolean processarAnnexos = false;
		ContingutArxiu expedientCreat = null;
		if (anotacio.getAnnexos() != null && anotacio.getAnnexos().size() > 0 && anotacio.getExpedientArxiuUuid() == null) {
			expedientCreat = crearExpedientArxiuTemporal(anotacio, bustia);
			anotacio.updateExpedientArxiuUuid(expedientCreat.getIdentificador());
			registreRepository.saveAndFlush(anotacio);
			processarAnnexos = true;
		}
		
		if (anotacio.getRegla() != null) {
			if (!reglaHelper.reglaAplicar(anotacio)) {
				if (expedientCreat != null)
					eliminarContingutExistent(expedientCreat.getIdentificador());
				throw new AplicarReglaException("Error aplicant regla en segon pla per a l'anotació " + anotacio.getId());
			}
		} 
		

		if (processarAnnexos)
			processarAnnexosRegistre(anotacio, bustia, expedientCreat);
		
		// si s'ha utilitzat el plugin de gestió documental, s'intentaran esborrar els fitxers guardats
		esborrarDocsTemporals(anotacio);
		
		//aquí haurem de actualtizar estat OK
		Date dataProcesOk = new Date();
		anotacio.updateProces(
				dataProcesOk,
				RegistreProcesEstatEnum.PROCESSAT, 
				null);
		registreRepository.saveAndFlush(anotacio);
	}
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void actualitzarEstatError(
			Long pendentId,
			Exception ex) {
		
		String error = ex.getMessage() + ": " + ExceptionUtils.getRootCauseMessage(ex) ;
		
		RegistreEntity pendent = registreRepository.findOne(pendentId);
		ReglaEntity regla = pendent.getRegla();
		if (regla != null && ReglaTipusEnumDto.BACKOFFICE.equals(regla.getTipus())) {
			Integer intentsRegla = regla.getBackofficeIntents();
			if (intentsRegla == null) {
				pendent.updateProces(
						new Date(),
						RegistreProcesEstatEnum.ERROR,
						error);
			} else {
				Integer intentsPendent = pendent.getProcesIntents();
				if (intentsPendent != null && intentsPendent.intValue() >= intentsRegla.intValue() - 1) {
					pendent.updateProces(
							new Date(),
							RegistreProcesEstatEnum.ERROR,
							error);
				} else {
					pendent.updateProces(
							new Date(),
							RegistreProcesEstatEnum.PENDENT,
							error);
				}
			}
		} else {
			pendent.updateProces(
					new Date(),
					RegistreProcesEstatEnum.ERROR,
					error);
		}
	}
	
	private void esborrarDocsTemporals(RegistreEntity anotacioEntity) {
		if (anotacioEntity.getAnnexos() != null && anotacioEntity.getAnnexos().size() > 0) {
			for (RegistreAnnexEntity annex: anotacioEntity.getAnnexos()) {
				if (annex.getGesdocDocumentId() != null) {
					pluginHelper.gestioDocumentalDelete(
							annex.getGesdocDocumentId(), 
							PluginHelper.GESDOC_AGRUPACIO_ANOTACIONS_REGISTRE_DOC_TMP);
					annex.updateGesdocDocumentId(null);
				}
				
				for(RegistreAnnexFirmaEntity firma: annex.getFirmes()) {
					if (firma.getGesdocFirmaId() != null) {
						pluginHelper.gestioDocumentalDelete(
								firma.getGesdocFirmaId(), 
								PluginHelper.GESDOC_AGRUPACIO_ANOTACIONS_REGISTRE_FIR_TMP);
						firma.updateGesdocFirmaId(null);
					}
				}
			}
		}
	}
	
	private ContingutArxiu crearExpedientArxiuTemporal(RegistreEntity anotacio, BustiaEntity bustia) {
		
		ContingutArxiu expedientCreat = pluginHelper.arxiuExpedientPerAnotacioCrear(
				anotacio, 
				bustia);
		
		return expedientCreat;
	}
	
	private void processarAnnexosRegistre(
			RegistreEntity anotacioEntity, 
			BustiaEntity bustia, 
			ContingutArxiu expedientCreat) {
		boolean isExpedientFinal = false;
		try {
			for (RegistreAnnexEntity annex: anotacioEntity.getAnnexos()) {
				
				guardarDocumentAnnex (
						annex, 
						bustia, 
						expedientCreat);
				
				isExpedientFinal = true;
				
				// Si l'anotació té una regla sistra llavors intenta extreure la informació dels identificadors
				// del tràmit i del procediment de l'annex
				if (anotacioEntity.getRegla() != null 
						&& anotacioEntity.getRegla().getTipus() == ReglaTipusEnumDto.BACKOFFICE
						&& anotacioEntity.getRegla().getBackofficeTipus() == BackofficeTipusEnumDto.SISTRA) {
					if (annex.getFitxerNom().equals("DatosPropios.xml") || annex.getFitxerNom().equals("Asiento.xml"))
						processarAnnexSistra(anotacioEntity, annex);
				}
			}
		} catch (Exception ex) {
			try {
				if (isExpedientFinal)
					pluginHelper.arxiuExpedientTemporalTancar(anotacioEntity);
				else
					eliminarContingutExistent(anotacioEntity.getExpedientArxiuUuid());
			} catch (Exception ex2) {
				logger.error(
						"Error al eliminar o tancar l'expedient creat l'arxiu digital",
						ex2);
			}
			throw new BustiaServiceException(
					"Error al processar els annexos de l'anotació de registre",
					ex);
		}
	}
	
	private void eliminarContingutExistent(
			String expedientUuid) {
		pluginHelper.arxiuExpedientEsborrarPerUuid(expedientUuid);
	}
	
	
	/*
	 * Mètode privat per obrir el document annex de tipus sistra i extreure'n
	 * informació per a l'anotació de registre. La informació que es pot extreure
	 * depén del document:
	 * - Asiento.xml: ASIENTO_REGISTRAL.DATOS_ASUNTO.IDENTIFICADOR_TRAMITE (VARCHAR2(20))
	 * - DatosPropios.xml: DATOS_PROPIOS.INSTRUCCIONES.IDENTIFICADOR_PROCEDIMIENTO (VARCHAR2(100))
	 * 
	 * @param anotacio 
	 * 			Anotació del registre
	 * @param annex
	 * 			Document annex amb el contingut per a llegir.
	 */
	private void processarAnnexSistra(
			RegistreEntity anotacio,
			RegistreAnnexEntity annex) {
		try {
			byte[] annexContingut = null;
			if (annex.getGesdocDocumentId() != null) {
				ByteArrayOutputStream baos_doc = new ByteArrayOutputStream();
				pluginHelper.gestioDocumentalGet(
					annex.getGesdocDocumentId(), 
					PluginHelper.GESDOC_AGRUPACIO_ANOTACIONS_REGISTRE_DOC_TMP, 
					baos_doc);
				annexContingut = baos_doc.toByteArray();
				annex.updateGesdocDocumentId(null);
			}
			org.w3c.dom.Document doc = XmlHelper.getDocumentFromContent(annexContingut);
			if (annex.getFitxerNom().equals("DatosPropios.xml")) {
				String identificadorProcediment = XmlHelper.getNodeValue(
						doc.getDocumentElement(), "INSTRUCCIONES.IDENTIFICADOR_PROCEDIMIENTO");
				anotacio.updateIdentificadorProcedimentSistra(identificadorProcediment);
			} else if (annex.getFitxerNom().equals("Asiento.xml")) {
				String identificadorTramit = XmlHelper.getNodeValue(
						doc.getDocumentElement(), "DATOS_ASUNTO.IDENTIFICADOR_TRAMITE");
				anotacio.updateIdentificadorTramitSistra(identificadorTramit);
			}		
		} catch (Exception e) {
			logger.error(
					"Error processant l'annex per l'anotació amb regla backoffice SISTRA " + annex.getFitxerNom(),
					e);
		}
	}

	private void guardarDocumentAnnex(
			RegistreAnnexEntity annex,
			BustiaEntity bustia,
			ContingutArxiu expedientCreat) throws IOException {
		byte[] contingut = null;
		if (annex.getGesdocDocumentId() != null) {
			ByteArrayOutputStream baos_doc = new ByteArrayOutputStream();
			pluginHelper.gestioDocumentalGet(
				annex.getGesdocDocumentId(), 
				PluginHelper.GESDOC_AGRUPACIO_ANOTACIONS_REGISTRE_DOC_TMP, 
				baos_doc);
			
			contingut = baos_doc.toByteArray();
		} else {
			Document arxiuDocument = pluginHelper.arxiuDocumentConsultar(
					bustia,
					annex.getFitxerArxiuUuid(),
					null,
					true);
			if (arxiuDocument.getContingut() != null) {
				contingut = arxiuDocument.getContingut().getContingut();
			} else {
				throw new ValidationException(
						"No s'ha trobat cap contingut per l'annex (" +
						"uuid=" + annex.getFitxerArxiuUuid() + ")");
			}
		}
		
		FitxerDto fitxer = new FitxerDto();
		fitxer.setNom(annex.getFitxerNom());
		fitxer.setContingut(contingut);
		fitxer.setContentType(annex.getFitxerTipusMime());
		fitxer.setTamany(contingut.length);

		byte[] firmaRipeaContingut = null;
		if (pluginHelper.isRegistreSignarAnnexos() && annex.getFirmes().size() == 0) {
			// Ripea signa amb el plugin de signatures els annexos sense firmes
			firmaRipeaContingut = pluginHelper.signaturaSignarCadesDetached(
					annex,
					contingut);
			String tipus = DocumentNtiTipoFirmaEnumDto.TF04.toString();
			String perfil = FirmaPerfil.BES.toString();
			String fitxerNom = annex.getFitxerNom();
			String tipusMime = "csig";
			String csvRegulacio = null;
			RegistreAnnexFirmaEntity annexFirma = RegistreAnnexFirmaEntity.getBuilder(
					tipus,
					perfil,
					fitxerNom + "_cades_det.csig",
					tipusMime,
					csvRegulacio,
					true, // firmat des de RIPEA
					annex).build();
			annex.getFirmes().add(annexFirma);
		}

		String uuidDocument = pluginHelper.arxiuDocumentAnnexCrear(
				annex,
				bustia,
				fitxer,
				convertirFirmesAnnexToArxiuFirmaDto(annex, firmaRipeaContingut),
				expedientCreat);
		annex.updateFitxerArxiuUuid(uuidDocument);
	}

	private static final Logger logger = LoggerFactory.getLogger(RegistreHelper.class);
}
