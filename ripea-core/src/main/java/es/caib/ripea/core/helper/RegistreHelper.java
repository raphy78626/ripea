/**
 * 
 */
package es.caib.ripea.core.helper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import es.caib.ripea.core.api.dto.UnitatOrganitzativaDto;
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
import es.caib.ripea.core.entity.EntitatEntity;
import es.caib.ripea.core.entity.FirmaEntity;
import es.caib.ripea.core.entity.RegistreAnnexEntity;
import es.caib.ripea.core.entity.RegistreEntity;
import es.caib.ripea.core.entity.RegistreInteressatEntity;
import es.caib.ripea.core.entity.ReglaEntity;

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
//		annex.setFirmaMode(annexEntity.getFirmaMode());
//		annex.setFirmaFitxerNom(annexEntity.getFirmaFitxerNom());
//		annex.setFirmaFitxerTamany(annexEntity.getFirmaFitxerTamany());
//		annex.setFirmaFitxerTipusMime(annexEntity.getFitxerTipusMime());
//		annex.setFirmaFitxerArxiuUuid(annexEntity.getFirmaFitxerArxiuUuid());
//		annex.setFirmaCsv(annexEntity.getFirmaCsv());
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
				registre,
				registreAnnex.getFitxerContingut()).
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
	
	private FirmaEntity toFirmaEntity(
			Firma firma,
			RegistreAnnexEntity annex) {
		FirmaEntity firmaEntity = FirmaEntity.getBuilder(
				firma.getTipus(), 
				firma.getPerfil(), 
				firma.getFitxerNom(), 
				firma.getContingut(),
				firma.getTipusMime(), 
				firma.getCsvRegulacio(), 
				false, // no és una autofirma de Ripea
				annex).build();
		
		return firmaEntity;
	}
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void processarErrorAsincron(RegistreEntity anotacio, Exception e) {
		alertaHelper.crearAlerta(
				anotacio.getId(),
				messageHelper.getMessage(
						"alertes.segon.pla.distribuir.anotacions.error",
						new Object[] {anotacio.getId()}),
				e);
		Date dataProcesOk = new Date();
		anotacio.updateProces(
				dataProcesOk,
				RegistreProcesEstatEnum.ERROR,
				e.getMessage());
		anotacio.updateDistribucioAsincrona(
				null, 
				e.getMessage());
	}

}
