/**
 * 
 */
package es.caib.ripea.core.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.caib.ripea.core.api.dto.DocumentDto;
import es.caib.ripea.core.api.dto.DocumentEstatEnumDto;
import es.caib.ripea.core.api.dto.DocumentTipusEnumDto;
import es.caib.ripea.core.api.dto.DocumentVersioDto;
import es.caib.ripea.core.api.dto.FitxerDto;
import es.caib.ripea.core.api.dto.LogTipusEnumDto;
import es.caib.ripea.core.api.dto.MultiplicitatEnumDto;
import es.caib.ripea.core.api.dto.PortafirmesEnviamentDto;
import es.caib.ripea.core.api.dto.PortafirmesEstatEnumDto;
import es.caib.ripea.core.api.dto.PortafirmesPrioritatEnumDto;
import es.caib.ripea.core.api.exception.NotFoundException;
import es.caib.ripea.core.api.exception.SistemaExternException;
import es.caib.ripea.core.api.exception.ValidationException;
import es.caib.ripea.core.api.service.DocumentService;
import es.caib.ripea.core.entity.ContingutEntity;
import es.caib.ripea.core.entity.DocumentEntity;
import es.caib.ripea.core.entity.DocumentPortafirmesEntity;
import es.caib.ripea.core.entity.DocumentVersioEntity;
import es.caib.ripea.core.entity.EntitatEntity;
import es.caib.ripea.core.entity.ExpedientEntity;
import es.caib.ripea.core.entity.MetaDocumentEntity;
import es.caib.ripea.core.entity.MetaExpedientMetaDocumentEntity;
import es.caib.ripea.core.helper.CacheHelper;
import es.caib.ripea.core.helper.ContingutHelper;
import es.caib.ripea.core.helper.ContingutLogHelper;
import es.caib.ripea.core.helper.ConversioTipusHelper;
import es.caib.ripea.core.helper.DocumentHelper;
import es.caib.ripea.core.helper.EntityComprovarHelper;
import es.caib.ripea.core.helper.ExpedientHelper;
import es.caib.ripea.core.helper.PermisosHelper;
import es.caib.ripea.core.helper.PluginHelper;
import es.caib.ripea.core.helper.UsuariHelper;
import es.caib.ripea.core.repository.DocumentNotificacioRepository;
import es.caib.ripea.core.repository.DocumentPortafirmesRepository;
import es.caib.ripea.core.repository.DocumentPublicacioRepository;
import es.caib.ripea.core.repository.DocumentRepository;
import es.caib.ripea.core.repository.DocumentVersioRepository;
import es.caib.ripea.core.repository.EntitatRepository;
import es.caib.ripea.core.repository.InteressatRepository;
import es.caib.ripea.core.repository.MetaDocumentRepository;
import es.caib.ripea.core.repository.MetaExpedientMetaDocumentRepository;
import es.caib.ripea.plugin.portafirmes.PortafirmesDocument;
import es.caib.ripea.plugin.portafirmes.PortafirmesPrioritatEnum;

/**
 * Implementació dels mètodes per a gestionar documents.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@Service
public class DocumentServiceImpl implements DocumentService {

	@Resource
	private EntitatRepository entitatRepository;
	@Resource
	private MetaDocumentRepository metaDocumentRepository;
	@Resource
	private DocumentRepository documentRepository;
	@Resource
	private DocumentVersioRepository documentVersioRepository;
	@Resource
	private MetaExpedientMetaDocumentRepository metaExpedientMetaDocumentRepository;
	@Resource
	private InteressatRepository interessatRepository;
	@Resource
	private DocumentPortafirmesRepository documentPortafirmesRepository;
	@Resource
	private DocumentNotificacioRepository documentNotificacioRepository;
	@Resource
	private DocumentPublicacioRepository documentPublicacioRepository;

	@Resource
	private ConversioTipusHelper conversioTipusHelper;
	@Resource
	private PermisosHelper permisosHelper;
	@Resource
	private ContingutHelper contingutHelper;
	@Resource
	private ExpedientHelper expedientHelper;
	@Resource
	private DocumentHelper documentHelper;
	@Resource
	private PluginHelper pluginHelper;
	@Resource
	private UsuariHelper usuariHelper;
	@Resource
	private CacheHelper cacheHelper;
	@Resource
	private EntityComprovarHelper entityComprovarHelper;
	@Resource
	private ContingutLogHelper contingutLogHelper;



	@Transactional
	@Override
	public DocumentDto create(
			Long entitatId,
			Long contingutId,
			DocumentTipusEnumDto tipus,
			Long metaDocumentId,
			String nom,
			Date data,
			String arxiuNom,
			String arxiuContentType,
			byte[] arxiuContingut,
			String ubicacio) {
		logger.debug("Creant nou document (" +
				"entitatId=" + entitatId + ", " +
				"contingutId=" + contingutId + ", " +
				"tipus=" + tipus + ", " +
				"metaDocumentId=" + metaDocumentId + ", " +
				"nom=" + nom + ", " +
				"data=" + data + ", " +
				"arxiuNom=" + arxiuNom + ", " +
				"arxiuContentType=" + arxiuContentType + ", " +
				"arxiuContingut=" + arxiuContingut + ", " +
				"ubicacio=" + ubicacio + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				true,
				false,
				false);
		ContingutEntity contingut = entityComprovarHelper.comprovarContingut(
				entitat,
				contingutId,
				null);
		// Comprova el meta-document
		MetaDocumentEntity metaDocument = null;
		if (metaDocumentId != null) {
			metaDocument = entityComprovarHelper.comprovarMetaDocument(
					entitat,
					metaDocumentId,
					true);
		}
		// Comprova que el contenidor arrel és l'escriptori de l'usuari actual
		contingutHelper.comprovarContingutArrelEsEscriptoriUsuariActual(
				entitat,
				contingut);
		// Comprova l'accés al path del contenidor pare
		contingutHelper.comprovarPermisosPathContingut(
				contingut,
				true,
				false,
				false,
				true);
		// Comprova que el nom sigui vàlid
		if (!contingutHelper.isNomValid(nom)) {
			throw new ValidationException(
					"<creacio>",
					DocumentEntity.class,
					"El nom del document no és vàlid (no pot començar amb \".\")");
		}
		// Comprova el permís de modificació de l'expedient superior
		ExpedientEntity expedientSuperior = contingutHelper.getExpedientSuperior(
				contingut,
				true,
				false,
				false);
		if (expedientSuperior != null) {
			contingutHelper.comprovarPermisosContingut(
					expedientSuperior,
					false,
					true,
					false);
			if (metaDocument != null) {
				// Comprova que el meta-document estigui actiu
				if (!metaDocument.isActiu()) {
					Authentication auth = SecurityContextHolder.getContext().getAuthentication();
					logger.error("No es pot crear un document amb un meta-document desactivat ("
							+ "entitatId=" + entitatId + ", "
							+ "contingutId=" + contingutId + ", "
							+ "metaDocumentId=" + metaDocumentId + ", "
							+ "usuari=" + auth.getName() + ")");
					throw new SecurityException("No es pot crear un document amb un meta-document desactivat");
				}
				if (metaDocument.isGlobalExpedient()) {
					// Comprova que es pugui crear segons la multiplicitat
					List<DocumentEntity> documents = documentRepository.findByExpedientAndMetaNodeAndEsborrat(
							expedientSuperior,
							metaDocument,
							0);
					if (documents.size() > 0 && (metaDocument.getGlobalMultiplicitat().equals(MultiplicitatEnumDto.M_1) || metaDocument.getGlobalMultiplicitat().equals(MultiplicitatEnumDto.M_0_1))) {
						Authentication auth = SecurityContextHolder.getContext().getAuthentication();
						logger.error("La multiplicitat especificada per al meta-document no permet crear nous documents ("
								+ "entitatId=" + entitatId + ", "
								+ "contingutId=" + contingutId + ", "
								+ "metaDocumentId=" + metaDocumentId + ", "
								+ "usuari=" + auth.getName() + ")");
						throw new SecurityException("La multiplicitat especificada per al meta-document no permet crear nous documents");
					}
				} else {
					// Comprova que el meta-document es pugui crear a dins l'expedient
					MetaExpedientMetaDocumentEntity metaExpedientMetaDocument = metaExpedientMetaDocumentRepository.findByMetaExpedientAndMetaDocument(
							expedientSuperior.getMetaExpedient(),
							metaDocument);
					if (metaExpedientMetaDocument == null) {
						Authentication auth = SecurityContextHolder.getContext().getAuthentication();
						logger.error("No es pot crear un document amb un meta-document no disponible ("
								+ "entitatId=" + entitatId + ", "
								+ "contingutId=" + contingutId + ", "
								+ "metaDocumentId=" + metaDocumentId + ", "
								+ "usuari=" + auth.getName() + ")");
						throw new SecurityException("No es pot crear un document amb un meta-document no disponible");
					}
					// Comprova que es pugui crear segons la multiplicitat
					List<DocumentEntity> documents = documentRepository.findByExpedientAndMetaNodeAndEsborrat(
							expedientSuperior,
							metaDocument,
							0);
					if (documents.size() > 0 && (metaExpedientMetaDocument.getMultiplicitat().equals(MultiplicitatEnumDto.M_1) || metaExpedientMetaDocument.getMultiplicitat().equals(MultiplicitatEnumDto.M_0_1))) {
						Authentication auth = SecurityContextHolder.getContext().getAuthentication();
						logger.error("La multiplicitat especificada per al meta-document no permet crear nous documents ("
								+ "entitatId=" + entitatId + ", "
								+ "contingutId=" + contingutId + ", "
								+ "metaDocumentId=" + metaDocumentId + ", "
								+ "usuari=" + auth.getName() + ")");
						throw new SecurityException("La multiplicitat especificada per al meta-document no permet crear nous documents");
					}
				}
			}
		} else {
			if (metaDocument != null) {
				Authentication auth = SecurityContextHolder.getContext().getAuthentication();
				logger.error("No es pot crear un document amb meta-document fora d'un expedient ("
						+ "entitatId=" + entitatId + ", "
						+ "contingutId=" + contingutId + ", "
						+ "usuari=" + auth.getName() + ")");
				throw new SecurityException("No es pot crear un document amb meta-document fora d'un expedient");
			}
		}
		DocumentEntity document = DocumentEntity.getBuilder(
				tipus,
				DocumentEstatEnumDto.ESBORRANY,
				nom,
				data,
				expedientSuperior,
				metaDocument,
				contingut,
				entitat).
				ubicacio(ubicacio).
				build();
		documentRepository.save(document);
		if (DocumentTipusEnumDto.DIGITAL.equals(tipus)) {
			int versio = 1;
			DocumentVersioEntity documentVersio = documentHelper.crearVersioAmbFitxerAssociat(
					document,
					versio,
					arxiuNom,
					arxiuContentType,
					arxiuContingut);
			documentVersioRepository.save(documentVersio);
			document.updateVersioDarrera(documentVersio);
		}
		if (expedientSuperior != null) {
			cacheHelper.evictErrorsValidacioPerNode(expedientSuperior);
		}
		// Registra al log la creació del document
		contingutLogHelper.log(
				document,
				LogTipusEnumDto.CREACIO,
				null,
				null,
				true,
				true);
		return toDocumentDto(document);
	}

	@Transactional
	@Override
	public DocumentDto update(
			Long entitatId,
			Long id,
			DocumentTipusEnumDto tipus,
			Long metaDocumentId,
			String nom,
			Date data,
			String arxiuNom,
			String arxiuContentType,
			byte[] arxiuContingut,
			String ubicacio) {
		logger.debug("Actualitzant el document (" +
				"entitatId=" + entitatId + ", " +
				"id=" + id + ", " +
				"tipus=" + tipus + ", " +
				"nom=" + nom + ", " +
				"data=" + data + ", " +
				"arxiuNom=" + arxiuNom + ", " +
				"arxiuContentType=" + arxiuContentType + ", " +
				"arxiuContingut=" + arxiuContingut + ", " +
				"ubicacio=" + ubicacio + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				true,
				false,
				false);
		DocumentEntity document = entityComprovarHelper.comprovarDocument(
				entitat,
				null,
				id,
				false,
				true,
				false);
		// Comprova que el contenidor arrel és l'escriptori de l'usuari actual
		contingutHelper.comprovarContingutArrelEsEscriptoriUsuariActual(
				entitat,
				document);
		// Comprova l'accés al path del document
		contingutHelper.comprovarPermisosPathContingut(
				document,
				true,
				false,
				false,
				true);
		// Comprova que el nom sigui vàlid
		if (!contingutHelper.isNomValid(nom)) {
			throw new ValidationException(
					id,
					DocumentEntity.class,
					"El nom del document no és vàlid (no pot començar amb \".\")");
		}
		// Comprova el permís de modificació a l'expedient superior
		ExpedientEntity expedientSuperior = contingutHelper.getExpedientSuperior(
				document,
				true,
				false,
				false);
		if (expedientSuperior != null) {
			contingutHelper.comprovarPermisosContingut(
					expedientSuperior,
					false,
					true,
					false);
		}
		// Comprova el permís de modificació del document
		contingutHelper.comprovarPermisosContingut(
				document,
				false,
				true,
				false);
		// Comprova el meta-expedient
		MetaDocumentEntity metaDocument = null;
		if (metaDocumentId != null) {
			metaDocument = entityComprovarHelper.comprovarMetaDocument(
					entitat,
					metaDocumentId,
					true);
			cacheHelper.evictErrorsValidacioPerNode(document);
		}
		document.update(
				metaDocument,
				nom,
				data,
				ubicacio);
		if (arxiuNom != null && arxiuContingut != null) {
			int versio = document.getVersioDarrera().getVersio() + 1;
			DocumentVersioEntity documentVersio = documentHelper.crearVersioAmbFitxerAssociat(
					document,
					versio,
					arxiuNom,
					arxiuContentType,
					arxiuContingut);
			documentVersioRepository.save(documentVersio);
			document.updateVersioDarrera(documentVersio);
		}
		// Registra al log la modificació del document
		contingutLogHelper.log(
				document,
				LogTipusEnumDto.MODIFICACIO,
				null,
				null,
				false,
				false);
		return toDocumentDto(document);
	}

	@Transactional
	@Override
	@CacheEvict(value = "errorsValidacioNode", key = "#id")
	public DocumentDto delete(
			Long entitatId,
			Long id) {
		logger.debug("Actualitzant el document ("
				+ "entitatId=" + entitatId + ", "
				+ "id=" + id + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				true,
				false,
				false);
		DocumentEntity document = entityComprovarHelper.comprovarDocument(
				entitat,
				null,
				id,
				false,
				false,
				true);
		// Comprova que el contenidor arrel és l'escriptori de l'usuari actual
		contingutHelper.comprovarContingutArrelEsEscriptoriUsuariActual(
				entitat,
				document);
		// Comprova l'accés al path del document
		contingutHelper.comprovarPermisosPathContingut(
				document,
				true,
				false,
				false,
				true);
		// Comprova el permís de modificació de l'expedient superior
		ExpedientEntity expedientSuperior = contingutHelper.getExpedientSuperior(
				document,
				false,
				false,
				false);
		if (expedientSuperior != null) {
			contingutHelper.comprovarPermisosContingut(
					expedientSuperior,
					false,
					true,
					false);
		}
		// Comprova el permís d'esborrar del document actual
		contingutHelper.comprovarPermisosContingut(
				document,
				false,
				false,
				true);
		// Esborra els arxius de la gestió documental
		List<DocumentVersioEntity> versions = documentVersioRepository.findByDocumentOrderByVersioDesc(document);
		for (DocumentVersioEntity versio: versions) {
			documentHelper.deleteFitxerAssociatSiNhiHa(versio);
		}
		// Esborra el document
		documentRepository.delete(document);
		if (expedientSuperior != null)
			cacheHelper.evictErrorsValidacioPerNode(expedientSuperior);
		// Registra al log l'eliminació del document
		contingutLogHelper.log(
				document,
				LogTipusEnumDto.ELIMINACIO,
				null,
				null,
				true,
				true);
		return toDocumentDto(document);
	}

	@Transactional(readOnly = true)
	@Override
	public DocumentDto findById(
			Long entitatId,
			Long id) {
		logger.debug("Obtenint el document ("
				+ "entitatId=" + entitatId + ", "
				+ "id=" + id + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				true,
				false,
				false);
		DocumentEntity document = entityComprovarHelper.comprovarDocument(
				entitat,
				null,
				id,
				true,
				false,
				false);
		// Per a consultes no es comprova el contenidor arrel
		// Comprova l'accés al path del document
		contingutHelper.comprovarPermisosPathContingut(
				document,
				true,
				false,
				false,
				true);
		return toDocumentDto(document);
	}

	@Transactional(readOnly = true)
	@Override
	public List<DocumentVersioDto> findVersionsByDocument(
			Long entitatId,
			Long id) {
		logger.debug("Obtenint les versions del document ("
				+ "entitatId=" + entitatId + ", "
				+ "id=" + id + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				true,
				false,
				false);
		DocumentEntity document = entityComprovarHelper.comprovarDocument(
				entitat,
				null,
				id,
				true,
				false,
				false);
		// Per a consultes no es comprova el contenidor arrel
		// Comprova l'accés al path del document
		contingutHelper.comprovarPermisosPathContingut(
				document,
				true,
				false,
				false,
				true);
		List<DocumentVersioDto> resposta = conversioTipusHelper.convertirList(
				documentVersioRepository.findByDocumentOrderByVersioDesc(document),
				DocumentVersioDto.class);
		for (DocumentVersioDto documentVersio: resposta) {
			emplenarDadesPortafirmes(
					documentVersio,
					documentPortafirmesRepository.findByDocument(document));
		}
		return resposta;
	}

	@Transactional(readOnly = true)
	@Override
	public DocumentVersioDto findDarreraVersio(
			Long entitatId,
			Long id) {
		return findVersio(entitatId, id, -1);
	}

	@Transactional(readOnly = true)
	@Override
	public DocumentVersioDto findVersio(
			Long entitatId,
			Long id,
			int versio) {
		logger.debug("Obtenint la versió del document ("
				+ "entitatId=" + entitatId + ", "
				+ "id=" + id + ", "
				+ "versio=" + versio + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				true,
				false,
				false);
		DocumentEntity document = entityComprovarHelper.comprovarDocument(
				entitat,
				null,
				id,
				true,
				false,
				false);
		// Per a consultes no es comprova el contenidor arrel
		// Comprova l'accés al path del document
		contingutHelper.comprovarPermisosPathContingut(
				document,
				true,
				false,
				false,
				true);
		DocumentVersioEntity documentVersio;
		if (versio == -1) {
			documentVersio = document.getVersioDarrera();
		} else {
			documentVersio = documentVersioRepository.findByDocumentAndVersio(
					document,
					versio);
		}
		DocumentVersioDto dto = conversioTipusHelper.convertir(
				documentVersio,
				DocumentVersioDto.class);
		emplenarDadesPortafirmes(
				dto,
				documentPortafirmesRepository.findByDocument(document));
		return dto;
	}

	@Transactional(readOnly = true)
	@Override
	public FitxerDto descarregar(
			Long entitatId,
			Long id,
			int versio) {
		logger.debug("Descarregant contingut del document ("
				+ "entitatId=" + entitatId + ", "
				+ "id=" + id + ", "
				+ "versio=" + versio + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				true,
				false,
				false);
		DocumentEntity document = entityComprovarHelper.comprovarDocument(
				entitat,
				null,
				id,
				true,
				false,
				false);
		// Per a consultes no es comprova el contenidor arrel
		// Comprova l'accés al path del document
		contingutHelper.comprovarPermisosPathContingut(
				document,
				true,
				false,
				false,
				true);
		DocumentVersioEntity documentVersio = documentVersioRepository.findByDocumentAndVersio(
				document,
				versio);
		if (documentVersio != null) {
			return documentHelper.getFitxerAssociat(
					documentVersio);
		} else {
			throw new NotFoundException(
					"(documentId=" + document.getId() + ", versio=" + versio + ")",
					DocumentVersioEntity.class);
		}
	}

	@Transactional
	@Override
	public void portafirmesEnviar(
			Long entitatId,
			Long id,
			String motiu,
			PortafirmesPrioritatEnumDto prioritat,
			Date dataCaducitat) {
		logger.debug("Enviant document a portafirmes (" +
				"entitatId=" + entitatId + ", " +
				"id=" + id + ", " +
				"motiu=" + motiu + ", " +
				"prioritat=" + prioritat + ", " +
				"dataCaducitat=" + dataCaducitat + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				true,
				false,
				false);
		DocumentEntity document = entityComprovarHelper.comprovarDocument(
				entitat,
				null,
				id,
				true,
				false,
				false);
		// Per a consultes no es comprova el contenidor arrel
		// Comprova l'accés al path del document
		contingutHelper.comprovarPermisosPathContingut(
				document,
				true,
				false,
				false,
				true);
		if (!DocumentTipusEnumDto.DIGITAL.equals(document.getDocumentTipus())) {
			throw new ValidationException(
					document.getId(),
					DocumentEntity.class,
					"El document a enviar al portafirmes no és de tipus " + DocumentTipusEnumDto.DIGITAL);
		}
		DocumentPortafirmesEntity documentPortafirmesDarrer = findDocumentPortafirmesDarrer(
				entitat,
				document);
		if (documentPortafirmesDarrer != null && PortafirmesEstatEnumDto.PENDENT.equals(documentPortafirmesDarrer.getPortafirmesEstat())) {
			logger.error(
					"Hi ha enviaments a portafirmes en estat pendent pel document (" +
					"entitatId=" + entitatId + ", " +
					"id=" + id + ")");
			throw new ValidationException(
					document.getId(),
					DocumentEntity.class,
					"Hi ha enviaments a portafirmes en estat pendent pel document");
		}
		DocumentVersioEntity documentVersio = document.getVersioDarrera();
		if (documentVersio == null) {
			throw new NotFoundException(
					"(documentId=" + id + ", versio=darrera)",
					DocumentVersioEntity.class);
		}
		// TODO Verificar permisos de l'usuari per fer aquesta acció
		if (pluginHelper.portafirmesEnviarDocumentEstampat()) {
			String custodiaUrl = pluginHelper.custodiaReservarUrl(document);
			document.updateCustodiaUrl(custodiaUrl);
		}
		long portafirmesId = pluginHelper.portafirmesUpload(
				document,
				motiu,
				PortafirmesPrioritatEnum.valueOf(prioritat.name()),
				dataCaducitat,
				null);
		DocumentPortafirmesEntity documentPortafirmes = DocumentPortafirmesEntity.getBuilder(
				document,
				document.getVersioDarrera().getVersio(),
				motiu,
				prioritat,
				dataCaducitat,
				portafirmesId,
				PortafirmesEstatEnumDto.PENDENT).build();
		documentPortafirmesRepository.save(documentPortafirmes);
		// Registra al log l'enviament al portafirmes
		contingutLogHelper.log(
				document,
				LogTipusEnumDto.PFIRMA_ENVIAMENT,
				null,
				null,
				new Integer(documentVersio.getVersio()).toString(),
				null,
				true,
				true);
	}

	@Transactional
	@Override
	public void portafirmesCancelar(
			Long entitatId,
			Long id) {
		logger.debug("Enviant document a portafirmes (" +
				"entitatId=" + entitatId + ", " +
				"id=" + id + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				true,
				false,
				false);
		DocumentEntity document = entityComprovarHelper.comprovarDocument(
				entitat,
				null,
				id,
				true,
				false,
				false);
		// Per a consultes no es comprova el contenidor arrel
		// Comprova l'accés al path del document
		contingutHelper.comprovarPermisosPathContingut(
				document,
				true,
				false,
				false,
				true);
		DocumentVersioEntity documentVersio = document.getVersioDarrera();
		if (documentVersio == null) {
			logger.error("No s'ha trobat la darrera versió del document (" +
					"documentId=" + id + ")");
			throw new NotFoundException(
					"(documentId=" + id + ", versio=darrera)",
					DocumentVersioEntity.class);
		}
		DocumentPortafirmesEntity documentPortafirmesDarrer = findDocumentPortafirmesDarrer(
				entitat,
				document);
		if (documentPortafirmesDarrer == null) {
			logger.error(
					"No s'ha trobat cap enviament a portafirmes pel document (" +
					"entitatId=" + entitat.getId() + ", " +
					"id=" + document.getId() + ")");
			throw new ValidationException(
					document.getId(),
					DocumentEntity.class,
					"No s'ha trobat cap enviament a portafirmes pel document)");
		}
		if (!PortafirmesEstatEnumDto.PENDENT.equals(documentPortafirmesDarrer.getPortafirmesEstat())) {
			logger.error(
					"No s'ha trobat cap enviament a portafirmes en estat pendent pel document (" +
					"entitatId=" + entitatId + ", " +
					"id=" + id + ")");
			throw new ValidationException(
					document.getId(),
					DocumentEntity.class,
					"No s'ha trobat cap enviament a portafirmes en estat pendent pel document");
		}

		// TODO Verificar permisos de l'usuari per fer aquesta acció

		pluginHelper.portafirmesDelete(
				documentVersio,
				documentPortafirmesDarrer);
		documentPortafirmesDarrer.updateEstat(
				PortafirmesEstatEnumDto.CANCELAT);
		// Registra al log la cancelació de la firma del document
		contingutLogHelper.log(
				document,
				LogTipusEnumDto.PFIRMA_CANCELACIO,
				null,
				null,
				new Integer(documentVersio.getVersio()).toString(),
				null,
				true,
				true);
	}

	@Transactional
	@Override
	public Exception portafirmesCallback(
			long portafirmesId,
			PortafirmesCallbackEstatEnum estat) {
		logger.debug("Processant petició del callback ("
				+ "portafirmesId=" + portafirmesId + ", "
				+ "estat=" + estat + ")");
		DocumentPortafirmesEntity documentPortafirmes = documentPortafirmesRepository.findByPortafirmesId(
				portafirmesId);
		if (documentPortafirmes == null) {
			logger.error("No s'ha trobat el document de portafirmes (portafirmesId=" + portafirmesId + ")");
			return new NotFoundException(
					"(portafirmesId=" + portafirmesId + ")",
					DocumentPortafirmesEntity.class);
		}
		DocumentEntity document = documentPortafirmes.getDocument();
		documentPortafirmes.updateNouCallback();
		if (!documentPortafirmes.getPortafirmesEstat().equals(PortafirmesEstatEnumDto.PENDENT)) {
			return new ValidationException(
					documentPortafirmes.getId(),
					DocumentPortafirmesEntity.class,
					"L'enviament a portafirmes no es troba en estat pendent (" +
					"estatActual=" + documentPortafirmes.getPortafirmesEstat() + ", " +
					"estatRebut=" + estat + ")");
		}
		// Establim l'usuari actual com a "portafirmesCallback" per a
		// que apareixi als logs
		usuariHelper.generarUsuariAutenticat(
				"portafirmesCallback",
				true);
		switch (estat) {
		case DOCUMENT_PAUSAT:
		case DOCUMENT_PENDENT:
			break;
		case DOCUMENT_FIRMAT:
			// Registra al log la firma del document
			contingutLogHelper.log(
					documentPortafirmes.getDocument(),
					LogTipusEnumDto.PFIRMA_FIRMA,
					null,
					null,
					new Integer(document.getVersioDarrera().getVersio()).toString(),
					null,
					true,
					true);
			documentPortafirmes.updateEstat(
					PortafirmesEstatEnumDto.FIRMAT);
			processarCallbackEstatFirmat(
					document,
					documentPortafirmes);
			break;
		case DOCUMENT_REBUTJAT:
			// Registra al log el rebuig de la firma del document
			contingutLogHelper.log(
					documentPortafirmes.getDocument(),
					LogTipusEnumDto.PFIRMA_REBUIG,
					null,
					null,
					new Integer(document.getVersioDarrera().getVersio()).toString(),
					null,
					true,
					true);
			documentPortafirmes.updateEstat(
					PortafirmesEstatEnumDto.REBUTJAT);
		default:
			return new ValidationException(
					documentPortafirmes.getId(),
					DocumentPortafirmesEntity.class,
					"S'ha rebut un estat de callback desconegut");
		}
		return null;
	}

	@Transactional
	@Override
	public FitxerDto convertirPdfPerFirma(
			Long entitatId,
			Long id) {
		logger.debug("Convertint document a format PDF per firmar ("
				+ "entitatId=" + entitatId + ", "
				+ "id=" + id + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				true,
				false,
				false);
		DocumentEntity document = entityComprovarHelper.comprovarDocument(
				entitat,
				null,
				id,
				true,
				false,
				false);
		// Per a consultes no es comprova el contenidor arrel
		// Comprova l'accés al path del document
		contingutHelper.comprovarPermisosPathContingut(
				document,
				true,
				false,
				false,
				true);
		DocumentVersioEntity documentVersio = document.getVersioDarrera();
		if (documentVersio == null) {
			logger.error("No s'ha trobat la darrera versió del document (" +
					"documentId=" + id + ")");
			throw new NotFoundException(
					"(documentId=" + id + ", versio=darrera)",
					DocumentVersioEntity.class);
		}
		return pluginHelper.conversioConvertirPdfIEstamparUrl(
				documentHelper.getFitxerAssociat(
						documentVersio),
				null);
	}

	@Transactional
	@Override
	public String generarIdentificadorFirmaClient(
			Long entitatId,
			Long id) {
		logger.debug("Generar identificador firma applet ("
				+ "entitatId=" + entitatId + ", "
				+ "id=" + id + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				true,
				false,
				false);
		DocumentEntity document = entityComprovarHelper.comprovarDocument(
				entitat,
				null,
				id,
				true,
				false,
				false);
		// Per a consultes no es comprova el contenidor arrel
		// Comprova l'accés al path del document
		contingutHelper.comprovarPermisosPathContingut(
				document,
				true,
				false,
				false,
				true);
		DocumentVersioEntity documentVersio = document.getVersioDarrera();
		if (documentVersio == null) {
			logger.error("No s'ha trobat la darrera versió del document (" +
					"documentId=" + id + ")");
			throw new NotFoundException(
					"(documentId=" + id + ", versio=darrera)",
					DocumentVersioEntity.class);
		}
		int versio = documentVersio.getVersio();
		try {
			return firmaAppletXifrar(
					new ObjecteFirmaApplet( 
							new Long(System.currentTimeMillis()),
							entitatId,
							id,
							versio));
		} catch (Exception ex) {
			logger.error(
					"Error al generar l'identificador per la firma via applet (" +
					"documentId=" + id + ", " +
					"versio=" + versio + ")",
					ex);
			throw new RuntimeException(
					"Error al generar l'identificador per la firma via applet (" +
					"documentId=" + id + ", " +
					"versio=" + versio + ")",
					ex);
		}
	}

	@Transactional
	@Override
	public void custodiarDocumentFirmaClient(
			String identificador,
			String arxiuNom,
			byte[] arxiuContingut) {
		logger.debug("Custodiar identificador firma applet ("
				+ "identificador=" + identificador + ")");
		ObjecteFirmaApplet objecte = null;
		try {
			objecte = firmaAppletDesxifrar(
					identificador,
					CLAU_SECRETA);
		} catch (Exception ex) {
			throw new RuntimeException(
					"Error al desxifrar l'identificador per la firma via applet (" +
					"identificador=" + identificador + ")",
					ex);
		}
		if (objecte != null) {
			EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
					objecte.getEntitatId(),
					true,
					false,
					false);
			DocumentEntity document = entityComprovarHelper.comprovarDocument(
					entitat,
					null,
					objecte.getDocumentId(),
					true,
					false,
					false);
			// Per a consultes no es comprova el contenidor arrel
			// Comprova l'accés al path del document
			contingutHelper.comprovarPermisosPathContingut(
					document,
					true,
					false,
					false,
					true);
			// Registra al log la firma del document
			contingutLogHelper.log(
					document,
					LogTipusEnumDto.APPLET_FIRMA,
					null,
					null,
					new Integer(document.getVersioDarrera().getVersio()).toString(),
					null,
					true,
					true);
			String custodiaDocumentId = pluginHelper.custodiaCustodiarDocumentFirmat(
					document,
					document.getMetaDocument().getFirmaPassarelaCustodiaTipus(),
					arxiuNom,
					arxiuContingut);
			document.updateCustodiaEstat(
					true,
					new Date(),
					custodiaDocumentId);
			// Registra al log la custòdia de la firma del document
			contingutLogHelper.log(
					document,
					LogTipusEnumDto.CUSTODIA,
					null,
					null,
					new Integer(document.getVersioDarrera().getVersio()).toString(),
					null,
					true,
					true);
		} else {
			logger.error(
					"No s'han trobat les dades del document amb identificador applet (" +
					"identificador=" + identificador + ")");
			throw new RuntimeException(
					"No s'han trobat les dades del document amb identificador applet (" +
					"identificador=" + identificador + ")");
		}
	}

	@Transactional
	@Override
	public void custodiaPortafirmesReintentar(
			Long entitatId,
			Long id) {
		logger.debug("Reintentant custòdia de document firmat amb portafirmes que ha donat error ("
				+ "entitatId=" + entitatId + ", "
				+ "id=" + id + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				true,
				false,
				false);
		DocumentEntity document = entityComprovarHelper.comprovarDocument(
				entitat,
				null,
				id,
				true,
				false,
				false);
		// Per a consultes no es comprova el contenidor arrel
		// Comprova l'accés al path del document
		contingutHelper.comprovarPermisosPathContingut(
				document,
				true,
				false,
				false,
				true);
		DocumentPortafirmesEntity documentPortafirmesDarrer = findDocumentPortafirmesDarrer(
				entitat,
				document);
		if (documentPortafirmesDarrer == null) {
			logger.error(
					"No s'ha trobat cap enviament a portafirmes pel document (" +
					"entitatId=" + entitat.getId() + ", " +
					"id=" + document.getId() + ")");
			throw new ValidationException(
					document.getId(),
					DocumentEntity.class,
					"No s'ha trobat cap enviament a portafirmes pel document");
		}
		if (!PortafirmesEstatEnumDto.FIRMAT.equals(documentPortafirmesDarrer.getPortafirmesEstat())) {
			logger.error(
					"El darrer enviament a portafirmes no està pendent de custodiar pel document (" +
					"entitatId=" + entitatId + ", " +
					"id=" + id + ")");
			throw new ValidationException(
					documentPortafirmesDarrer.getId(),
					DocumentPortafirmesEntity.class,
					"El darrer enviament a portafirmes no està pendent de custodiar");
		}

		// TODO Verificar permisos de l'usuari per fer aquesta acció

		SistemaExternException ex = processarCallbackEstatFirmat(
				document,
				documentPortafirmesDarrer);
		if (ex == null) {
			// Neteja l'error de l'enviament al portafirmes
			documentPortafirmesDarrer.updateError(null);
			// Registra al log la custòdia del document
			contingutLogHelper.log(
					document,
					LogTipusEnumDto.CUSTODIA,
					null,
					null,
					new Integer(document.getVersioDarrera().getVersio()).toString(),
					null,
					true,
					true);
		} else {
			throw ex;
		}
	}

	@Transactional
	@Override
	public void custodiaEsborrar(
			Long entitatId,
			Long id) {
		logger.debug("Esborrar document de custòdia ("
				+ "entitatId=" + entitatId + ", "
				+ "id=" + id + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				true,
				false,
				false);
		DocumentEntity document = entityComprovarHelper.comprovarDocument(
				entitat,
				null,
				id,
				true,
				false,
				false);
		// Per a consultes no es comprova el contenidor arrel
		// Comprova l'accés al path del document
		contingutHelper.comprovarPermisosPathContingut(
				document,
				true,
				false,
				false,
				true);
		// Esborra el document de la custòdia
		pluginHelper.custodiaEsborrar(document);
		// Actualitza l'estat de custòdia del document
		document.updateCustodiaEstat(
				false,
				document.getCustodiaData(),
				document.getCustodiaId());
		DocumentPortafirmesEntity documentPortafirmesDarrer = findDocumentPortafirmesDarrer(
				entitat,
				document);
		if (documentPortafirmesDarrer != null && PortafirmesEstatEnumDto.CUSTODIAT.equals(documentPortafirmesDarrer.getPortafirmesEstat())) {
			documentPortafirmesDarrer.updateEstat(PortafirmesEstatEnumDto.ESBORRAT);
		}
	}



	private DocumentDto toDocumentDto(
			DocumentEntity document) {
		return (DocumentDto)contingutHelper.toContingutDto(
				document,
				false,
				false,
				false,
				false,
				false,
				false);
	}

	private SistemaExternException processarCallbackEstatFirmat(
			DocumentEntity document,
			DocumentPortafirmesEntity documentPortafirmes) {
		SistemaExternException exception = null;
		// Descarrega el document firmat del protafirmes
		PortafirmesDocument portafirmesDocument = null;
		try {
			portafirmesDocument = pluginHelper.portafirmesDownload(
					document,
					documentPortafirmes);
		} catch (SistemaExternException ex) {
			documentPortafirmes.updateError(
					ex.getMessage());
			exception = ex;
		}
		// Si el document s'ha descarregat correctament intenta la custòdia
		if (portafirmesDocument != null) {
			if (portafirmesDocument.isCustodiat()) {
				// Potser el document ja ha estat custodiat pel portafirmes
				document.updateCustodiaUrl(
						portafirmesDocument.getCustodiaUrl());
				document.updateCustodiaEstat(
						true,
						new Date(),
						portafirmesDocument.getCustodiaId());
				documentPortafirmes.updateEstat(PortafirmesEstatEnumDto.CUSTODIAT);
			} else {
				// Si el document no s'ha custodiat des del portafirmes l'envia
				// a la custòdia
				try {
					String custodiaDocumentId = pluginHelper.custodiaCustodiarDocumentFirmat(
							document,
							document.getMetaDocument().getPortafirmesCustodiaTipus(),
							portafirmesDocument.getArxiuNom(),
							portafirmesDocument.getArxiuContingut());
					document.updateCustodiaEstat(
							true,
							new Date(),
							custodiaDocumentId);
					documentPortafirmes.updateEstat(PortafirmesEstatEnumDto.CUSTODIAT);
					// Registra al log la custòdia del document
					contingutLogHelper.log(
							documentPortafirmes.getDocument(),
							LogTipusEnumDto.CUSTODIA,
							null,
							null,
							custodiaDocumentId,
							null,
							true,
							true);
				} catch (SistemaExternException ex) {
					documentPortafirmes.updateError(
							ex.getMessage());
					exception = ex;
				}
			}
		}
		return exception;
	}

	private void emplenarDadesPortafirmes(
			DocumentVersioDto documentVersio,
			List<DocumentPortafirmesEntity> documentsPortafirmes) {
		documentVersio.setPortafirmesConversioArxiuNom(
				pluginHelper.conversioConvertirPdfArxiuNom(
						documentVersio.getArxiuNom()));
		for (DocumentPortafirmesEntity documentPortafirmes: documentsPortafirmes) {
			if (documentVersio.getVersio() == documentPortafirmes.getVersio()) {
				PortafirmesEnviamentDto portafirmesEnviament = new PortafirmesEnviamentDto();
				portafirmesEnviament.setMotiu(
						documentPortafirmes.getMotiu());
				portafirmesEnviament.setPrioritat(
						PortafirmesPrioritatEnumDto.valueOf(
								documentPortafirmes.getPrioritat().name()));
				portafirmesEnviament.setDataEnviament(
						documentPortafirmes.getCreatedDate().toDate());
				portafirmesEnviament.setDataCaducitat(
						documentPortafirmes.getDataCaducitat());
				portafirmesEnviament.setPortafirmesId(
						documentPortafirmes.getPortafirmesId());
				portafirmesEnviament.setPortafirmesEstat(
						PortafirmesEstatEnumDto.valueOf(
								documentPortafirmes.getPortafirmesEstat().name()));
				portafirmesEnviament.setCallbackDarrer(
						documentPortafirmes.getCallbackDarrer());
				portafirmesEnviament.setCallbackCount(
						documentPortafirmes.getCallbackCount());
				portafirmesEnviament.setErrorDescripcio(
						documentPortafirmes.getErrorDescripcio());
				documentVersio.addPortafirmesEnviament(portafirmesEnviament);
			}
		}
	}

	private DocumentPortafirmesEntity findDocumentPortafirmesDarrer(
			EntitatEntity entitat,
			DocumentEntity document) {
		List<DocumentPortafirmesEntity> documentsPortafirmes = documentPortafirmesRepository.findByDocumentOrderByCreatedDateDesc(
				document);
		DocumentPortafirmesEntity documentPortafirmesDarrer = null;
		if (!documentsPortafirmes.isEmpty()) {
			documentPortafirmesDarrer = documentsPortafirmes.get(0);
		}
		return documentPortafirmesDarrer;
	}

	private static final String CLAU_SECRETA = "R1p3AR1p3AR1p3AR";
	private String firmaAppletXifrar(
			ObjecteFirmaApplet objecte) throws Exception {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream os = new ObjectOutputStream(baos);
		Long[] array = new Long[] {
				objecte.getSysdate(),
				objecte.getEntitatId(),
				objecte.getDocumentId(),
				new Long(objecte.getVersio())};
		os.writeObject(array);
		os.close();
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(
				Cipher.ENCRYPT_MODE,
				buildKey(CLAU_SECRETA));
		byte[] xifrat = cipher.doFinal(baos.toByteArray());
		return new String(Base64.encode(xifrat));
	}
	private ObjecteFirmaApplet firmaAppletDesxifrar(
			String missatge,
			String key) throws Exception {
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(
				Cipher.DECRYPT_MODE,
				buildKey(key));
		ByteArrayInputStream bais = new ByteArrayInputStream(
				cipher.doFinal(
						Base64.decode(missatge.getBytes())));
		ObjectInputStream is = new ObjectInputStream(bais);
		Long[] array = (Long[])is.readObject();
		ObjecteFirmaApplet objecte = new ObjecteFirmaApplet(
				array[0],
				array[1],
				array[2],
				array[3].intValue());
		is.close();
		return objecte;
	}
	private SecretKeySpec buildKey(String message) throws Exception {
		MessageDigest sha = MessageDigest.getInstance("SHA-1");
		byte[] key = sha.digest(message.getBytes());
		key = Arrays.copyOf(key, 16);
		return new SecretKeySpec(key, "AES");
	}

	public class ObjecteFirmaApplet implements Serializable {
		private Long sysdate;
		private Long entitatId;
		private Long documentId;
		private int versio;
		public ObjecteFirmaApplet(
				Long sysdate,
				Long entitatId,
				Long documentId,
				int versio) {
			this.sysdate = sysdate;
			this.entitatId = entitatId;
			this.documentId = documentId;
			this.versio = versio;
		}
		public Long getSysdate() {
			return sysdate;
		}
		public void setSysdate(Long sysdate) {
			this.sysdate = sysdate;
		}
		public Long getEntitatId() {
			return entitatId;
		}
		public void setEntitatId(Long entitatId) {
			this.entitatId = entitatId;
		}
		public Long getDocumentId() {
			return documentId;
		}
		public void setDocumentId(Long documentId) {
			this.documentId = documentId;
		}
		public int getVersio() {
			return versio;
		}
		public void setVersio(int versio) {
			this.versio = versio;
		}
		private static final long serialVersionUID = -6929597339153341365L;
	}

	private static final Logger logger = LoggerFactory.getLogger(DocumentServiceImpl.class);

}
