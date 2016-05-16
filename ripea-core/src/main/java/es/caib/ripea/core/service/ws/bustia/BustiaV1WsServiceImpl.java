/**
 * 
 */
package es.caib.ripea.core.service.ws.bustia;

import javax.annotation.Resource;
import javax.jws.WebParam;
import javax.jws.WebService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import es.caib.regweb3.ws.api.v3.AnexoWs;
import es.caib.regweb3.ws.api.v3.DatosInteresadoWs;
import es.caib.regweb3.ws.api.v3.InteresadoWs;
import es.caib.regweb3.ws.api.v3.RegistroEntradaWs;
import es.caib.ripea.core.api.dto.RegistreAnnexFirmaModeEnumDto;
import es.caib.ripea.core.api.dto.RegistreAnnexOrigenEnumDto;
import es.caib.ripea.core.api.dto.RegistreDocumentTipusEnumDto;
import es.caib.ripea.core.api.dto.RegistreDocumentValidesaEnumDto;
import es.caib.ripea.core.api.dto.RegistreDocumentacioFisicaTipusEnumDto;
import es.caib.ripea.core.api.dto.RegistreInteressatCanalEnumDto;
import es.caib.ripea.core.api.dto.RegistreInteressatDocumentTipusEnumDto;
import es.caib.ripea.core.api.dto.RegistreInteressatTipusEnumDto;
import es.caib.ripea.core.api.dto.RegistreTipusEnumDto;
import es.caib.ripea.core.api.dto.RegistreTransportTipusEnumDto;
import es.caib.ripea.core.api.dto.UnitatOrganitzativaDto;
import es.caib.ripea.core.api.exception.ValidationException;
import es.caib.ripea.core.api.service.ws.BustiaV1WsService;
import es.caib.ripea.core.entity.BustiaEntity;
import es.caib.ripea.core.entity.EntitatEntity;
import es.caib.ripea.core.entity.RegistreAnnexEntity;
import es.caib.ripea.core.entity.RegistreEntity;
import es.caib.ripea.core.entity.RegistreInteressatEntity;
import es.caib.ripea.core.helper.PluginHelper;
import es.caib.ripea.core.helper.UnitatOrganitzativaHelper;
import es.caib.ripea.core.repository.BustiaRepository;
import es.caib.ripea.core.repository.EntitatRepository;
import es.caib.ripea.core.repository.RegistreRepository;

/**
 * Implementació dels mètodes per al servei d'enviament de
 * continguts a bústies.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@Service
@WebService(
		name = "BustiaV1",
		serviceName = "BustiaV1Service",
		portName = "BustiaV1ServicePort",
		endpointInterface = "es.caib.ripea.core.api.service.ws.BustiaV1WsService",
		targetNamespace = "http://www.caib.es/ripea/ws/v1/bustia")
public class BustiaV1WsServiceImpl implements BustiaV1WsService {

	@Resource
	private EntitatRepository entitatRepository;
	@Resource
	private RegistreRepository registreRepository;
	@Resource
	private BustiaRepository bustiaRepository;

	@Resource
	private PluginHelper pluginHelper;
	@Resource
	private UnitatOrganitzativaHelper unitatOrganitzativaHelper;



	@Override
	public void enviarAnotacioRegistreEntrada(
			String entitat,
			String unitatAdministrativa,
			RegistroEntradaWs registroEntrada) {
		String registreNumero = (registroEntrada != null) ? registroEntrada.getNumeroRegistroFormateado() : null;
		logger.debug(
				"Processant enviament d'anotació de registre d'entrada al servei web de bústia (" +
				"entitat:" + entitat + ", " +
				"unitatAdministrativa:" + unitatAdministrativa + ", " +
				"registreNumero:" + registreNumero + ")");
		EntitatEntity entitatEntity = entitatRepository.findByCodi(entitat);
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
		RegistreEntity registreRepetit = registreRepository.findByTipusAndUnitatAdministrativaAndNumeroAndDataAndOficinaAndLlibre(
				RegistreTipusEnumDto.ENTRADA.getValor(),
				unitatAdministrativa,
				registroEntrada.getNumero(),
				registroEntrada.getFecha(),
				registroEntrada.getOficina(),
				registroEntrada.getLibro());
		if (registreRepetit != null) {
			throw new ValidationException(
					"Aquesta anotació ja ha estat donada d'alta a l'aplicació (" +
					"tipus=" + RegistreTipusEnumDto.ENTRADA.getValor() + ", " +
					"unitatAdministrativa=" + unitatAdministrativa + ", " +
					"numero=" + registroEntrada.getNumeroRegistroFormateado() + ", " +
					"data=" + registroEntrada.getFecha() + ", " +
					"oficina=" + registroEntrada.getOficina() + ", " +
					"llibre=" + registroEntrada.getLibro() + ")");
		}
		saveRegistreEntity(
				registroEntrada,
				findAndCreateIfNotExistsBustiaPerDefectePerUnitatAdministrativa(
						entitatEntity,
						unitat));
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



	private BustiaEntity findAndCreateIfNotExistsBustiaPerDefectePerUnitatAdministrativa(
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
			RegistroEntradaWs registroEntrada,
			BustiaEntity bustia) {
		RegistreDocumentacioFisicaTipusEnumDto documentacioFisica = null;
		if (registroEntrada.getDocFisica() != null) {
			documentacioFisica = RegistreDocumentacioFisicaTipusEnumDto.valorAsEnum(
					registroEntrada.getDocFisica().toString());
		}
		RegistreEntity entity = RegistreEntity.getBuilder(
				RegistreTipusEnumDto.ENTRADA,
				registroEntrada.getDestino(),
				registroEntrada.getNumero(),
				registroEntrada.getFecha(),
				registroEntrada.getNumeroRegistroFormateado(),
				registroEntrada.getOficina(),
				registroEntrada.getLibro(),
				registroEntrada.getTipoAsunto(),
				registroEntrada.getIdioma(),
				registroEntrada.getCodigoUsuario(),
				bustia).
		extracte(registroEntrada.getExtracto()).
		assumpteCodi(registroEntrada.getCodigoAsunto()).
		assumpteReferencia(registroEntrada.getRefExterna()).
		assumpteNumExpedient(registroEntrada.getNumExpediente()).
		transportTipus(
				RegistreTransportTipusEnumDto.valorAsEnum(
						registroEntrada.getTipoTransporte())).
		transportNumero(registroEntrada.getNumTransporte()).
		usuariContacte(registroEntrada.getContactoUsuario()).
		aplicacioCodi(registroEntrada.getAplicacion()).
		documentacioFisica(documentacioFisica).
		aplicacioVersio(registroEntrada.getVersion()).
		observacions(registroEntrada.getObservaciones()).
		exposa(registroEntrada.getExpone()).
		solicita(registroEntrada.getSolicita()).
		build();
		if (registroEntrada.getInteresados() != null) {
			for (InteresadoWs interesado: registroEntrada.getInteresados()) {
				entity.getInteressats().add(
						toInteressatEntity(
								interesado,
								entity));
			}
		}
		if (registroEntrada.getAnexos() != null) {
			for (AnexoWs anexo: registroEntrada.getAnexos()) {
				entity.getAnnexos().add(
						toAnnexEntity(
								anexo,
								entity));
			}
		}
		RegistreEntity saved = registreRepository.save(entity);
		return saved;
	}

	private RegistreInteressatEntity toInteressatEntity(
			InteresadoWs interesado,
			RegistreEntity registre) {
		DatosInteresadoWs interesadoDatos = interesado.getInteresado();
		RegistreInteressatTipusEnumDto interessatTipus = null;
		if (interesadoDatos.getTipoInteresado() != null) {
			interessatTipus = RegistreInteressatTipusEnumDto.valorAsEnum(
					interesadoDatos.getTipoInteresado().toString());
		}
		RegistreInteressatEntity.Builder interessatBuilder;
		switch (interessatTipus) {
		case PERSONA_FIS:
			interessatBuilder = RegistreInteressatEntity.getBuilder(
					interessatTipus,
					RegistreInteressatDocumentTipusEnumDto.valorAsEnum(interesadoDatos.getTipoDocumentoIdentificacion()),
					interesadoDatos.getDocumento(),
					interesadoDatos.getNombre(),
					interesadoDatos.getApellido1(),
					interesadoDatos.getApellido2(),
					registre);
			break;
		default: // PERSONA_JUR o ADMINISTRACIO
			interessatBuilder = RegistreInteressatEntity.getBuilder(
					interessatTipus,
					RegistreInteressatDocumentTipusEnumDto.valorAsEnum(interesadoDatos.getTipoDocumentoIdentificacion()),
					interesadoDatos.getDocumento(),
					interesadoDatos.getRazonSocial(),
					registre);
			break;
		}
		String pais = null;
		if (interesadoDatos.getPais() != null)
			pais = interesadoDatos.getPais().toString();
		String provincia = null;
		if (interesadoDatos.getProvincia() != null)
			provincia = interesadoDatos.getProvincia().toString();
		String municipi = null;
		if (interesadoDatos.getLocalidad() != null)
			municipi = interesadoDatos.getLocalidad().toString();
		String canal = null;
		if (interesadoDatos.getCanal() != null)
			canal = interesadoDatos.getCanal().toString();
		RegistreInteressatEntity interessatEntity = interessatBuilder.
		pais(pais).
		provincia(provincia).
		municipi(municipi).
		adresa(interesadoDatos.getDireccion()).
		codiPostal(interesadoDatos.getCp()).
		email(interesadoDatos.getEmail()).
		telefon(interesadoDatos.getTelefono()).
		emailHabilitat(interesadoDatos.getDireccionElectronica()).
		canalPreferent(
				RegistreInteressatCanalEnumDto.valorAsEnum(canal)).
		observacions(interesadoDatos.getObservaciones()).
		build();
		if (interesado.getRepresentante() != null) {
			DatosInteresadoWs representanteDatos = interesado.getRepresentante();
			interessatTipus = null;
			if (representanteDatos.getTipoInteresado() != null) {
				interessatTipus = RegistreInteressatTipusEnumDto.valorAsEnum(
						representanteDatos.getTipoInteresado().toString());
			}
			pais = null;
			if (representanteDatos.getPais() != null)
				pais = representanteDatos.getPais().toString();
			provincia = null;
			if (representanteDatos.getProvincia() != null)
				provincia = representanteDatos.getProvincia().toString();
			municipi = null;
			if (representanteDatos.getLocalidad() != null)
				municipi = representanteDatos.getLocalidad().toString();
			canal = null;
			if (representanteDatos.getCanal() != null)
				canal = representanteDatos.getCanal().toString();
			interessatEntity.updateRepresentant(
					interessatTipus,
					RegistreInteressatDocumentTipusEnumDto.valorAsEnum(representanteDatos.getTipoDocumentoIdentificacion()),
					representanteDatos.getDocumento(),
					representanteDatos.getNombre(),
					representanteDatos.getApellido1(),
					representanteDatos.getApellido2(),
					representanteDatos.getRazonSocial(),
					pais,
					provincia,
					municipi,
					representanteDatos.getDireccion(),
					representanteDatos.getCp(),
					representanteDatos.getEmail(),
					representanteDatos.getTelefono(),
					representanteDatos.getDireccionElectronica(),
					RegistreInteressatCanalEnumDto.valorAsEnum(canal));
		}
		return interessatEntity;
	}

	private RegistreAnnexEntity toAnnexEntity(
			AnexoWs anexo,
			RegistreEntity registre) {
		String origen = null;
		if (anexo.getOrigenCiudadanoAdmin() != null)
			origen = anexo.getOrigenCiudadanoAdmin().toString();
		RegistreAnnexEntity annexEntity = RegistreAnnexEntity.getBuilder(
				anexo.getTitulo(),
				anexo.getNombreFicheroAnexado(),
				(anexo.getFicheroAnexado() != null) ? anexo.getFicheroAnexado().length : -1,
				anexo.getTipoDocumental(),
				RegistreDocumentTipusEnumDto.valorAsEnum(anexo.getTipoDocumento()),
				RegistreAnnexOrigenEnumDto.valorAsEnum(origen),
				anexo.getFechaCaptura(),
				registre).
				fitxerTipusMime(anexo.getTipoMIMEFicheroAnexado()).
				validesa(RegistreDocumentValidesaEnumDto.valorAsEnum(anexo.getValidezDocumento())).
				observacions(anexo.getObservaciones()).
				firmaMode(RegistreAnnexFirmaModeEnumDto.valorAsEnum(anexo.getModoFirma())).
				build();
		return annexEntity;
	}

	private static final Logger logger = LoggerFactory.getLogger(BustiaV1WsServiceImpl.class);

}
