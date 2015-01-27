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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.caib.ripea.core.api.dto.DocumentDto;
import es.caib.ripea.core.api.dto.DocumentVersioDto;
import es.caib.ripea.core.api.dto.FitxerDto;
import es.caib.ripea.core.api.dto.PortafirmesEnviamentDto;
import es.caib.ripea.core.api.dto.PortafirmesEstatEnumDto;
import es.caib.ripea.core.api.dto.PortafirmesPrioritatEnumDto;
import es.caib.ripea.core.api.exception.ContenidorNotFoundException;
import es.caib.ripea.core.api.exception.DocumentNotFoundException;
import es.caib.ripea.core.api.exception.EntitatNotFoundException;
import es.caib.ripea.core.api.exception.MetaDocumentNotFoundException;
import es.caib.ripea.core.api.exception.NomInvalidException;
import es.caib.ripea.core.api.exception.PluginException;
import es.caib.ripea.core.api.service.DocumentService;
import es.caib.ripea.core.entity.ContenidorEntity;
import es.caib.ripea.core.entity.DocumentEntity;
import es.caib.ripea.core.entity.DocumentPortafirmesEntity;
import es.caib.ripea.core.entity.DocumentPortafirmesEstatEnum;
import es.caib.ripea.core.entity.DocumentPortafirmesPrioritatEnum;
import es.caib.ripea.core.entity.DocumentVersioEntity;
import es.caib.ripea.core.entity.EntitatEntity;
import es.caib.ripea.core.entity.ExpedientEntity;
import es.caib.ripea.core.entity.LogTipusEnum;
import es.caib.ripea.core.entity.MetaDocumentEntity;
import es.caib.ripea.core.entity.MetaExpedientMetaDocumentEntity;
import es.caib.ripea.core.entity.MetaNodeEntity;
import es.caib.ripea.core.entity.MultiplicitatEnum;
import es.caib.ripea.core.helper.CacheHelper;
import es.caib.ripea.core.helper.ContenidorHelper;
import es.caib.ripea.core.helper.ContenidorLogHelper;
import es.caib.ripea.core.helper.ConversioTipusHelper;
import es.caib.ripea.core.helper.DocumentHelper;
import es.caib.ripea.core.helper.PermisosComprovacioHelper;
import es.caib.ripea.core.helper.PermisosHelper;
import es.caib.ripea.core.helper.PluginHelper;
import es.caib.ripea.core.repository.ContenidorRepository;
import es.caib.ripea.core.repository.DocumentPortafirmesRepository;
import es.caib.ripea.core.repository.DocumentRepository;
import es.caib.ripea.core.repository.DocumentVersioRepository;
import es.caib.ripea.core.repository.EntitatRepository;
import es.caib.ripea.core.repository.MetaDocumentRepository;
import es.caib.ripea.core.repository.MetaExpedientMetaDocumentRepository;
import es.caib.ripea.core.security.ExtendedPermission;
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
	private ContenidorRepository contenidorRepository;
	@Resource
	private MetaDocumentRepository metaDocumentRepository;
	@Resource
	private DocumentRepository documentRepository;
	@Resource
	private DocumentVersioRepository documentVersioRepository;
	@Resource
	private DocumentPortafirmesRepository documentPortafirmesRepository;
	@Resource
	private MetaExpedientMetaDocumentRepository metaExpedientMetaDocumentRepository;

	@Resource
	private ConversioTipusHelper conversioTipusHelper;
	@Resource
	private PermisosHelper permisosHelper;
	@Resource
	private ContenidorHelper contenidorHelper;
	@Resource
	private DocumentHelper documentHelper;
	@Resource
	private PluginHelper pluginHelper;
	@Resource
	private PermisosComprovacioHelper permisosComprovacioHelper;
	@Resource
	private CacheHelper cacheHelper;
	@Resource
	private ContenidorLogHelper contenidorLogHelper;



	@Transactional
	@Override
	public DocumentDto create(
			Long entitatId,
			Long contenidorId,
			Long metaDocumentId,
			String nom,
			Date data,
			String arxiuNom,
			String arxiuContentType,
			byte[] arxiuContingut) throws EntitatNotFoundException, ContenidorNotFoundException, MetaDocumentNotFoundException, NomInvalidException {
		logger.debug("Creant nou document (" +
				"entitatId=" + entitatId + ", " +
				"contenidorId=" + contenidorId + ", " +
				"metaDocumentId=" + metaDocumentId + ", " +
				"nom=" + nom + ", " +
				"data=" + data + ", " +
				"arxiuNom=" + arxiuNom + ", " +
				"arxiuContentType=" + arxiuContentType + ", " +
				"arxiuContingut=" + arxiuContingut + ")");
		EntitatEntity entitat = permisosComprovacioHelper.comprovarEntitat(
				entitatId,
				true,
				false,
				false);
		ContenidorEntity contenidor = comprovarContenidor(
				entitat,
				contenidorId);
		// Comprova el meta-document
		MetaDocumentEntity metaDocument = null;
		if (metaDocumentId != null) {
			metaDocument = comprovarMetaDocument(
					entitat,
					metaDocumentId,
					true);
		}
		// Comprova que el contenidor arrel és l'escriptori de l'usuari actual
		contenidorHelper.comprovarContenidorArrelEsEscriptoriUsuariActual(
				entitat,
				contenidor);
		// Comprova l'accés al path del contenidor pare
		contenidorHelper.comprovarPermisosPathContenidor(
				contenidor,
				true,
				false,
				false,
				true);
		// Comprova que el nom sigui vàlid
		if (!contenidorHelper.isNomValid(nom)) {
			throw new NomInvalidException();
		}
		// Comprova el permís de modificació de l'expedient superior
		ExpedientEntity expedientSuperior = contenidorHelper.getExpedientSuperior(
				contenidor,
				true);
		if (expedientSuperior != null) {
			contenidorHelper.comprovarPermisosContenidor(
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
							+ "contenidorId=" + contenidorId + ", "
							+ "metaDocumentId=" + metaDocumentId + ", "
							+ "usuari=" + auth.getName() + ")");
					throw new SecurityException("No es pot crear un document amb un meta-document desactivat");
				}
				if (metaDocument.isGlobalExpedient()) {
					// Comprova que es pugui crear segons la multiplicitat
					List<DocumentEntity> documents = documentRepository.findByExpedientAndMetaNode(
							expedientSuperior,
							metaDocument);
					if (documents.size() > 0 && (metaDocument.getGlobalMultiplicitat().equals(MultiplicitatEnum.M_1) || metaDocument.getGlobalMultiplicitat().equals(MultiplicitatEnum.M_0_1))) {
						Authentication auth = SecurityContextHolder.getContext().getAuthentication();
						logger.error("La multiplicitat especificada per al meta-document no permet crear nous documents ("
								+ "entitatId=" + entitatId + ", "
								+ "contenidorId=" + contenidorId + ", "
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
								+ "contenidorId=" + contenidorId + ", "
								+ "metaDocumentId=" + metaDocumentId + ", "
								+ "usuari=" + auth.getName() + ")");
						throw new SecurityException("No es pot crear un document amb un meta-document no disponible");
					}
					// Comprova que es pugui crear segons la multiplicitat
					List<DocumentEntity> documents = documentRepository.findByExpedientAndMetaNode(
							expedientSuperior,
							metaDocument);
					if (documents.size() > 0 && (metaExpedientMetaDocument.getMultiplicitat().equals(MultiplicitatEnum.M_1) || metaExpedientMetaDocument.getMultiplicitat().equals(MultiplicitatEnum.M_0_1))) {
						Authentication auth = SecurityContextHolder.getContext().getAuthentication();
						logger.error("La multiplicitat especificada per al meta-document no permet crear nous documents ("
								+ "entitatId=" + entitatId + ", "
								+ "contenidorId=" + contenidorId + ", "
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
						+ "contenidorId=" + contenidorId + ", "
						+ "usuari=" + auth.getName() + ")");
				throw new SecurityException("No es pot crear un document amb meta-document fora d'un expedient");
			}
		}
		DocumentEntity document = DocumentEntity.getBuilder(
				nom,
				data,
				expedientSuperior,
				metaDocument,
				contenidor,
				entitat).build();
		documentRepository.save(document);
		int versio = 1;
		DocumentVersioEntity documentVersio = documentHelper.crearVersioAmbFitxerAssociat(
				document,
				versio,
				arxiuNom,
				arxiuContentType,
				arxiuContingut);
		documentVersioRepository.save(documentVersio);
		document.updateDarreraVersio(versio);
		if (expedientSuperior != null)
			cacheHelper.evictErrorsValidacioPerNode(expedientSuperior);
		// Registra al log la creació del document
		contenidorLogHelper.log(
				document,
				LogTipusEnum.CREACIO,
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
			Long metaDocumentId,
			String nom,
			Date data,
			String arxiuNom,
			String arxiuContentType,
			byte[] arxiuContingut) throws EntitatNotFoundException, DocumentNotFoundException, NomInvalidException {
		logger.debug("Actualitzant el document ("
				+ "entitatId=" + entitatId + ", "
				+ "id=" + id + ", "
				+ "nom=" + nom + ", "
				+ "data=" + data + ")");
		EntitatEntity entitat = permisosComprovacioHelper.comprovarEntitat(
				entitatId,
				true,
				false,
				false);
		DocumentEntity document = comprovarDocument(
				entitat,
				id,
				false,
				true,
				false);
		// Comprova que el contenidor arrel és l'escriptori de l'usuari actual
		contenidorHelper.comprovarContenidorArrelEsEscriptoriUsuariActual(
				entitat,
				document);
		// Comprova l'accés al path del document
		contenidorHelper.comprovarPermisosPathContenidor(
				document,
				true,
				false,
				false,
				true);
		// Comprova que el nom sigui vàlid
		if (!contenidorHelper.isNomValid(nom)) {
			throw new NomInvalidException();
		}
		// Comprova el permís de modificació a l'expedient superior
		ExpedientEntity expedientSuperior = contenidorHelper.getExpedientSuperior(
				document,
				true);
		if (expedientSuperior != null) {
			contenidorHelper.comprovarPermisosContenidor(
					expedientSuperior,
					false,
					true,
					false);
		}
		// Comprova el permís de modificació del document
		contenidorHelper.comprovarPermisosContenidor(
				document,
				false,
				true,
				false);
		// Comprova el meta-expedient
		MetaDocumentEntity metaDocument = null;
		if (metaDocumentId != null) {
			metaDocument = comprovarMetaDocument(
					entitat,
					metaDocumentId,
					true);
			cacheHelper.evictErrorsValidacioPerNode(document);
		}
		document.update(
				metaDocument,
				nom,
				data
				);
		if (arxiuNom != null && arxiuContingut != null) {
			int versio = document.getDarreraVersio() + 1;
			DocumentVersioEntity documentVersio = documentHelper.crearVersioAmbFitxerAssociat(
					document,
					versio,
					arxiuNom,
					arxiuContentType,
					arxiuContingut);
			documentVersioRepository.save(documentVersio);
			document.updateDarreraVersio(versio);
		}
		// Registra al log la modificació del document
		contenidorLogHelper.log(
				document,
				LogTipusEnum.MODIFICACIO,
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
			Long id) throws EntitatNotFoundException, DocumentNotFoundException {
		logger.debug("Actualitzant el document ("
				+ "entitatId=" + entitatId + ", "
				+ "id=" + id + ")");
		EntitatEntity entitat = permisosComprovacioHelper.comprovarEntitat(
				entitatId,
				true,
				false,
				false);
		DocumentEntity document = comprovarDocument(
				entitat,
				id,
				false,
				false,
				true);
		// Comprova que el contenidor arrel és l'escriptori de l'usuari actual
		contenidorHelper.comprovarContenidorArrelEsEscriptoriUsuariActual(
				entitat,
				document);
		// Comprova l'accés al path del document
		contenidorHelper.comprovarPermisosPathContenidor(
				document,
				true,
				false,
				false,
				true);
		// Comprova el permís de modificació de l'expedient superior
		ExpedientEntity expedientSuperior = contenidorHelper.getExpedientSuperior(
				document,
				false);
		if (expedientSuperior != null) {
			contenidorHelper.comprovarPermisosContenidor(
					expedientSuperior,
					false,
					true,
					false);
		}
		// Comprova el permís d'esborrar del document actual
		contenidorHelper.comprovarPermisosContenidor(
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
		contenidorLogHelper.log(
				document,
				LogTipusEnum.ELIMINACIO,
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
			Long id) throws EntitatNotFoundException, DocumentNotFoundException {
		logger.debug("Obtenint el document ("
				+ "entitatId=" + entitatId + ", "
				+ "id=" + id + ")");
		EntitatEntity entitat = permisosComprovacioHelper.comprovarEntitat(
				entitatId,
				true,
				false,
				false);
		DocumentEntity document = comprovarDocument(
				entitat,
				id,
				true,
				false,
				false);
		// Per a consultes no es comprova el contenidor arrel
		// Comprova l'accés al path del document
		contenidorHelper.comprovarPermisosPathContenidor(
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
			Long id) throws EntitatNotFoundException, DocumentNotFoundException {
		logger.debug("Obtenint les versions del document ("
				+ "entitatId=" + entitatId + ", "
				+ "id=" + id + ")");
		EntitatEntity entitat = permisosComprovacioHelper.comprovarEntitat(
				entitatId,
				true,
				false,
				false);
		DocumentEntity document = comprovarDocument(
				entitat,
				id,
				true,
				false,
				false);
		// Per a consultes no es comprova el contenidor arrel
		// Comprova l'accés al path del document
		contenidorHelper.comprovarPermisosPathContenidor(
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
			Long id) throws EntitatNotFoundException, DocumentNotFoundException {
		return findVersio(entitatId, id, -1);
	}

	@Transactional(readOnly = true)
	@Override
	public DocumentVersioDto findVersio(
			Long entitatId,
			Long id,
			int versio) throws EntitatNotFoundException, DocumentNotFoundException {
		logger.debug("Obtenint la versió del document ("
				+ "entitatId=" + entitatId + ", "
				+ "id=" + id + ", "
				+ "versio=" + versio + ")");
		EntitatEntity entitat = permisosComprovacioHelper.comprovarEntitat(
				entitatId,
				true,
				false,
				false);
		DocumentEntity document = comprovarDocument(
				entitat,
				id,
				true,
				false,
				false);
		// Per a consultes no es comprova el contenidor arrel
		// Comprova l'accés al path del document
		contenidorHelper.comprovarPermisosPathContenidor(
				document,
				true,
				false,
				false,
				true);
		DocumentVersioEntity documentVersio = documentVersioRepository.findByDocumentAndVersio(
				document,
				(versio == -1) ? document.getDarreraVersio() : versio);
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
			int versio) throws EntitatNotFoundException, DocumentNotFoundException {
		logger.debug("Descarregant contingut del document ("
				+ "entitatId=" + entitatId + ", "
				+ "id=" + id + ", "
				+ "versio=" + versio + ")");
		EntitatEntity entitat = permisosComprovacioHelper.comprovarEntitat(
				entitatId,
				true,
				false,
				false);
		DocumentEntity document = comprovarDocument(
				entitat,
				id,
				true,
				false,
				false);
		// Per a consultes no es comprova el contenidor arrel
		// Comprova l'accés al path del document
		contenidorHelper.comprovarPermisosPathContenidor(
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
			throw new DocumentNotFoundException();
		}
	}

	@Transactional
	@Override
	public void portafirmesEnviar(
			Long entitatId,
			Long id,
			int versio,
			String motiu,
			PortafirmesPrioritatEnumDto prioritat,
			Date dataCaducitat) throws EntitatNotFoundException, DocumentNotFoundException, IllegalStateException, PluginException {
		logger.debug("Enviant document a portafirmes (" +
				"entitatId=" + entitatId + ", " +
				"id=" + id + ", " +
				"versio=" + versio + ", " +
				"motiu=" + motiu + ", " +
				"prioritat=" + prioritat + ", " +
				"dataCaducitat=" + dataCaducitat + ")");
		EntitatEntity entitat = permisosComprovacioHelper.comprovarEntitat(
				entitatId,
				true,
				false,
				false);
		DocumentEntity document = comprovarDocument(
				entitat,
				id,
				true,
				false,
				false);
		// Per a consultes no es comprova el contenidor arrel
		// Comprova l'accés al path del document
		contenidorHelper.comprovarPermisosPathContenidor(
				document,
				true,
				false,
				false,
				true);
		DocumentPortafirmesEntity documentPortafirmesDarrer = findDocumentPortafirmesDarrer(
				entitat,
				document,
				versio);
		if (documentPortafirmesDarrer != null && DocumentPortafirmesEstatEnum.PENDENT.equals(documentPortafirmesDarrer.getPortafirmesEstat())) {
			logger.error(
					"Hi ha enviaments a portafirmes en estat pendent pel document (" +
					"entitatId=" + entitatId + ", " +
					"id=" + id + ", " +
					"versio=" + versio + ")");
			throw new IllegalStateException("Hi ha enviaments a portafirmes en estat pendent pel document");
		}
		DocumentVersioEntity documentVersio = documentVersioRepository.findByDocumentAndVersio(
				document,
				versio);

		// TODO Verificar permisos de l'usuari per fer aquesta acció

		long portafirmesId = pluginHelper.portafirmesUpload(
				documentVersio,
				motiu,
				PortafirmesPrioritatEnum.valueOf(prioritat.name()),
				dataCaducitat);
		DocumentPortafirmesEntity documentPortafirmes = DocumentPortafirmesEntity.getBuilder(
				document,
				versio,
				motiu,
				DocumentPortafirmesPrioritatEnum.valueOf(prioritat.name()),
				dataCaducitat,
				portafirmesId,
				DocumentPortafirmesEstatEnum.PENDENT).build();
		documentPortafirmesRepository.save(documentPortafirmes);
		// Registra al log l'enviament al portafirmes
		contenidorLogHelper.log(
				document,
				LogTipusEnum.PFIRMA_ENVIAMENT,
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
			Long id,
			int versio) throws EntitatNotFoundException, DocumentNotFoundException, IllegalStateException, PluginException {
		logger.debug("Enviant document a portafirmes (" +
				"entitatId=" + entitatId + ", " +
				"id=" + id + ", " +
				"versio=" + versio + ")");
		EntitatEntity entitat = permisosComprovacioHelper.comprovarEntitat(
				entitatId,
				true,
				false,
				false);
		DocumentEntity document = comprovarDocument(
				entitat,
				id,
				true,
				false,
				false);
		// Per a consultes no es comprova el contenidor arrel
		// Comprova l'accés al path del document
		contenidorHelper.comprovarPermisosPathContenidor(
				document,
				true,
				false,
				false,
				true);
		DocumentPortafirmesEntity documentPortafirmesDarrer = findDocumentPortafirmesDarrer(
				entitat,
				document,
				versio);
		if (documentPortafirmesDarrer == null) {
			logger.error(
					"No s'ha trobat cap enviament a portafirmes pel document (" +
					"entitatId=" + entitat.getId() + ", " +
					"id=" + document.getId() + ", " +
					"versio=" + versio + ")");
			throw new IllegalStateException("No s'ha trobat cap enviament a portafirmes pel document)");
		}
		if (!DocumentPortafirmesEstatEnum.PENDENT.equals(documentPortafirmesDarrer.getPortafirmesEstat())) {
			logger.error(
					"No s'ha trobat cap enviament a portafirmes en estat pendent pel document (" +
					"entitatId=" + entitatId + ", " +
					"id=" + id + ", " +
					"versio=" + versio + ")");
			throw new IllegalStateException("No s'ha trobat cap enviament a portafirmes en estat pendent pel document");
		}
		DocumentVersioEntity documentVersio = documentVersioRepository.findByDocumentAndVersio(
				document,
				versio);

		// TODO Verificar permisos de l'usuari per fer aquesta acció

		pluginHelper.portafirmesDelete(
				documentVersio,
				documentPortafirmesDarrer);
		documentPortafirmesDarrer.updateEstat(
				DocumentPortafirmesEstatEnum.CANCELAT);
		// Registra al log la cancelació de la firma del document
		contenidorLogHelper.log(
				document,
				LogTipusEnum.PFIRMA_CANCELACIO,
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
			int documentId,
			int estat) throws DocumentNotFoundException {
		logger.debug("Processant petició del callback ("
				+ "documentId=" + documentId + ", "
				+ "estat=" + estat + ")");
		DocumentPortafirmesEntity documentPortafirmes = documentPortafirmesRepository.findByPortafirmesId(
				new Integer(documentId).longValue());
		if (documentPortafirmes == null) {
			logger.error("No s'ha trobat el document de portafirmes (documentId=" + documentId + ")");
			throw new DocumentNotFoundException();
		}
		DocumentVersioEntity documentVersio = documentVersioRepository.findByDocumentAndVersio(
				documentPortafirmes.getDocument(),
				documentPortafirmes.getVersio());
		if (documentVersio == null) {
			logger.error("No s'ha trobat la versió del document (" +
					"documentId=" + documentPortafirmes.getDocument().getId() + ", " +
					"versio=" + documentPortafirmes.getVersio() + ")");
			throw new DocumentNotFoundException();
		}

		// TODO Verificar permisos de l'usuari per fer aquesta acció

		documentPortafirmes.updateNouCallback();
		if (documentPortafirmes.getPortafirmesEstat().equals(DocumentPortafirmesEstatEnum.PENDENT)) {
			return new IllegalStateException(
					"L'enviament a portafirmes no es troba en estat pendent (" +
					"documentId=" + documentId + ", " +
					"estatActual=" + documentPortafirmes.getPortafirmesEstat() + ", " +
					"estatRebut=" + estat + ")");
		}
		/*private static final int DOCUMENT_BLOQUEJAT = 0;
		private static final int DOCUMENT_PENDENT = 1;
		private static final int DOCUMENT_FIRMAT = 2;
		private static final int DOCUMENT_REBUTJAT = 3;*/
		try {
			switch (estat) {
			case 0:
			case 1:
				break;
			case 2:
				// Registra al log la firma del document
				contenidorLogHelper.log(
						documentPortafirmes.getDocument(),
						LogTipusEnum.PFIRMA_FIRMA,
						null,
						null,
						new Integer(documentVersio.getVersio()).toString(),
						null,
						true,
						true);
				pluginHelper.portafirmesProcessarCallbackEstatFirmat(
						documentVersio,
						documentPortafirmes);
				// Registra al log la custòdia del document
				contenidorLogHelper.log(
						documentPortafirmes.getDocument(),
						LogTipusEnum.CUSTODIA,
						null,
						null,
						new Integer(documentVersio.getVersio()).toString(),
						null,
						true,
						true);
				documentPortafirmes.updateEstat(
						DocumentPortafirmesEstatEnum.FIRMAT);
				break;
			case 3:
				// Registra al log el rebuig de la firma del document
				contenidorLogHelper.log(
						documentPortafirmes.getDocument(),
						LogTipusEnum.PFIRMA_REBUIG,
						null,
						null,
						new Integer(documentVersio.getVersio()).toString(),
						null,
						true,
						true);
				documentPortafirmes.updateEstat(
						DocumentPortafirmesEstatEnum.REBUTJAT);
			}
			return null;
		} catch (PluginException ex) {
			documentPortafirmes.updateEstatError(
					ex.getMessage());
			return ex;
		}
	}

	@Transactional
	@Override
	public FitxerDto convertirPdf(
			Long entitatId,
			Long id,
			int versio) {
		logger.debug("Convertint document a format PDF ("
				+ "entitatId=" + entitatId + ", "
				+ "id=" + id + ", "
				+ "versio=" + versio + ")");
		EntitatEntity entitat = permisosComprovacioHelper.comprovarEntitat(
				entitatId,
				true,
				false,
				false);
		DocumentEntity document = comprovarDocument(
				entitat,
				id,
				true,
				false,
				false);
		// Per a consultes no es comprova el contenidor arrel
		// Comprova l'accés al path del document
		contenidorHelper.comprovarPermisosPathContenidor(
				document,
				true,
				false,
				false,
				true);
		DocumentVersioEntity documentVersio = documentVersioRepository.findByDocumentAndVersio(
				document,
				versio);
		if (documentVersio == null) {
			logger.error("No s'ha trobat la versió del document (" +
					"documentId=" + id + ", " +
					"versio=" + versio + ")");
			throw new DocumentNotFoundException();
		}
		String url = pluginHelper.custodiaReservarUrl(documentVersio);
		return pluginHelper.conversioConvertirPdf(
				documentHelper.getFitxerAssociat(
						documentVersio),
				url);
	}

	@Transactional
	@Override
	public String generarIdentificadorFirmaApplet(
			Long entitatId,
			Long id,
			int versio) throws EntitatNotFoundException, DocumentNotFoundException {
		logger.debug("Generar identificador firma applet ("
				+ "entitatId=" + entitatId + ", "
				+ "id=" + id + ", "
				+ "versio=" + versio + ")");
		EntitatEntity entitat = permisosComprovacioHelper.comprovarEntitat(
				entitatId,
				true,
				false,
				false);
		DocumentEntity document = comprovarDocument(
				entitat,
				id,
				true,
				false,
				false);
		// Per a consultes no es comprova el contenidor arrel
		// Comprova l'accés al path del document
		contenidorHelper.comprovarPermisosPathContenidor(
				document,
				true,
				false,
				false,
				true);
		DocumentVersioEntity documentVersio = documentVersioRepository.findByDocumentAndVersio(
				document,
				versio);
		if (documentVersio == null) {
			logger.error("No s'ha trobat la versió del document (" +
					"documentId=" + id + ", " +
					"versio=" + versio + ")");
			throw new DocumentNotFoundException();
		}
		try {
			return firmaAppletXifrar(
					new ObjecteFirmaApplet( 
							new Long(System.currentTimeMillis()),
							entitatId,
							id,
							versio),
					CLAU_SECRETA);
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
	public void custodiarFirmaApplet(
			String identificador,
			String arxiuNom,
			byte[] arxiuContingut) throws DocumentNotFoundException, PluginException {
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
			EntitatEntity entitat = permisosComprovacioHelper.comprovarEntitat(
					objecte.getEntitatId(),
					true,
					false,
					false);
			DocumentEntity document = comprovarDocument(
					entitat,
					objecte.getDocumentId(),
					true,
					false,
					false);
			// Per a consultes no es comprova el contenidor arrel
			// Comprova l'accés al path del document
			contenidorHelper.comprovarPermisosPathContenidor(
					document,
					true,
					false,
					false,
					true);
			DocumentVersioEntity documentVersio = documentVersioRepository.findByDocumentAndVersio(
					document,
					objecte.getVersio());
			if (documentVersio == null) {
				logger.error("No s'ha trobat la versió del document (" +
						"documentId=" + objecte.getDocumentId() + ", " +
						"versio=" + objecte.getVersio() + ")");
				throw new DocumentNotFoundException();
			}
			// Registra al log la firma del document
			contenidorLogHelper.log(
					document,
					LogTipusEnum.APPLET_FIRMA,
					null,
					null,
					new Integer(documentVersio.getVersio()).toString(),
					null,
					true,
					true);
			FitxerDto pdfFirmat = new FitxerDto();
			pdfFirmat.setNom(arxiuNom);
			pdfFirmat.setContingut(arxiuContingut);
			pluginHelper.custodiaCustodiarPdfFirmat(
					documentVersio,
					pdfFirmat);
			// Registra al log la custòdia de la firma del document
			contenidorLogHelper.log(
					document,
					LogTipusEnum.CUSTODIA,
					null,
					null,
					new Integer(documentVersio.getVersio()).toString(),
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



	private DocumentDto toDocumentDto(
			DocumentEntity document) {
		return (DocumentDto)contenidorHelper.toContenidorDto(
				document,
				false,
				false,
				false,
				false,
				false,
				false);
	}

	private ContenidorEntity comprovarContenidor(
			EntitatEntity entitat,
			Long id) throws ContenidorNotFoundException {
		ContenidorEntity contenidor = contenidorRepository.findOne(id);
		if (contenidor == null) {
			logger.error("No s'ha trobat el contenidor (contenidorId=" + id + ")");
			throw new ContenidorNotFoundException();
		}
		if (!contenidor.getEntitat().equals(entitat)) {
			logger.error("L'entitat del contenidor no coincideix ("
					+ "entitatId1=" + entitat.getId() + ", "
					+ "entitatId2=" + contenidor.getEntitat().getId() + ")");
			throw new ContenidorNotFoundException();
		}
		return contenidor;
	}
	private MetaDocumentEntity comprovarMetaDocument(
			EntitatEntity entitat,
			Long id,
			boolean comprovarPermisCreate) throws MetaDocumentNotFoundException {
		MetaDocumentEntity metaDocument = metaDocumentRepository.findOne(id);
		if (metaDocument == null) {
			logger.error("No s'ha trobat el meta-document (id=" + id + ")");
			throw new MetaDocumentNotFoundException();
		}
		if (!entitat.equals(metaDocument.getEntitat())) {
			logger.error("L'entitat especificada no coincideix amb l'entitat del meta-document ("
					+ "entitatId1=" + entitat.getId() + ", "
					+ "entitatId2=" + metaDocument.getEntitat().getId() + ")");
			throw new MetaDocumentNotFoundException();
		}
		if (comprovarPermisCreate) {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			boolean granted = permisosHelper.isGrantedAll(
					metaDocument.getId(),
					MetaNodeEntity.class,
					new Permission[] {ExtendedPermission.CREATE},
					auth);
			if (!granted) {
				logger.error("No es tenen permisos per a crear documents amb el meta-document (id=" + id + ")");
				throw new SecurityException("No es tenen permisos per a crear documents amb aquest meta-document");
			}
		}
		return metaDocument;
	}
	private DocumentEntity comprovarDocument(
			EntitatEntity entitat,
			Long documentId,
			boolean comprovarPermisRead,
			boolean comprovarPermisWrite,
			boolean comprovarPermisDelete) throws DocumentNotFoundException {
		DocumentEntity document = documentRepository.findOne(documentId);
		if (document == null) {
			logger.error("No s'ha trobat el document (documentId=" + documentId + ")");
			throw new DocumentNotFoundException();
		}
		if (!document.getEntitat().equals(entitat)) {
			logger.error("L'entitat del document no coincideix ("
					+ "entitatId1=" + entitat.getId() + ", "
					+ "entitatId2=" + document.getEntitat().getId() + ")");
			throw new DocumentNotFoundException();
		}
		if (document.getMetaDocument() != null && (comprovarPermisRead || comprovarPermisWrite || comprovarPermisDelete)) {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			List<Permission> permisos = new ArrayList<Permission>();
			if (comprovarPermisRead)
				permisos.add(ExtendedPermission.READ);
			if (comprovarPermisWrite)
				permisos.add(ExtendedPermission.WRITE);
			if (comprovarPermisDelete)
				permisos.add(ExtendedPermission.DELETE);
			boolean granted = permisosHelper.isGrantedAll(
					document.getMetaDocument().getId(),
					MetaNodeEntity.class,
					permisos.toArray(new Permission[permisos.size()]),
					auth);
			if (!granted) {
				logger.error("No es tenen permisos per a modificar el document (documentId=" + documentId + ")");
				throw new SecurityException("No es tenen permisos per a modificar el document");
			}
		}
		return document;
	}

	private void emplenarDadesPortafirmes(
			DocumentVersioDto documentVersio,
			List<DocumentPortafirmesEntity> documentsPortafirmes) {
		documentVersio.setPortafirmesConversioArxiuNom(
				pluginHelper.portafirmesConversioArxiuNom(
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
			DocumentEntity document,
			int versio) {
		List<DocumentPortafirmesEntity> documentsPortafirmes = documentPortafirmesRepository.findByDocumentAndVersio(
				document,
				versio);
		DocumentPortafirmesEntity documentPortafirmesDarrer = null;
		for (DocumentPortafirmesEntity documentPortafirmes: documentsPortafirmes) {
			if (documentPortafirmesDarrer == null || documentPortafirmes.getCreatedDate().toDate().after(documentPortafirmesDarrer.getCreatedDate().toDate())) {
				documentPortafirmesDarrer = documentPortafirmes;
			}
		}
		return documentPortafirmesDarrer;
	}

	private static final String CLAU_SECRETA = "R1p3AR1p3AR1p3AR";
	private String firmaAppletXifrar(
			ObjecteFirmaApplet objecte,
			String key) throws Exception {
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
				buildKey(key));
		byte[] xifrat = cipher.doFinal(baos.toByteArray());
		String xifratBase64 = new String(Base64.encode(xifrat));
		System.out.println(">>> Dades del xifrat (" +
				"original=" + baos.size() + ", " +
				"xifrat=" + xifrat.length + ", " +
				"xifratB64=" + xifratBase64.length() + ")");
		return xifratBase64;
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
