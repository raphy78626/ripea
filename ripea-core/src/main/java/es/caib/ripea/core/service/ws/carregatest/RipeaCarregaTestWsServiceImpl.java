/**
 * 
 */
package es.caib.ripea.core.service.ws.carregatest;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.jws.WebService;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import es.caib.ripea.core.api.dto.ArxiuDto;
import es.caib.ripea.core.api.dto.BustiaContingutPendentTipusEnumDto;
import es.caib.ripea.core.api.dto.DocumentDto;
import es.caib.ripea.core.api.dto.DocumentNtiEstadoElaboracionEnumDto;
import es.caib.ripea.core.api.dto.DocumentNtiOrigenEnumDto;
import es.caib.ripea.core.api.dto.DocumentNtiTipoDocumentalEnumDto;
import es.caib.ripea.core.api.dto.DocumentTipusEnumDto;
import es.caib.ripea.core.api.dto.EntitatDto;
import es.caib.ripea.core.api.dto.EscriptoriDto;
import es.caib.ripea.core.api.dto.ExpedientDto;
import es.caib.ripea.core.api.dto.FitxerDto;
import es.caib.ripea.core.api.dto.MetaDadaDto;
import es.caib.ripea.core.api.dto.MetaDadaTipusEnumDto;
import es.caib.ripea.core.api.dto.MetaDocumentDto;
import es.caib.ripea.core.api.dto.MetaExpedientDto;
import es.caib.ripea.core.api.dto.MultiplicitatEnumDto;
import es.caib.ripea.core.api.dto.PermisDto;
import es.caib.ripea.core.api.dto.PrincipalTipusEnumDto;
import es.caib.ripea.core.api.exception.NotFoundException;
import es.caib.ripea.core.api.service.ArxiuService;
import es.caib.ripea.core.api.service.ContingutService;
import es.caib.ripea.core.api.service.DocumentService;
import es.caib.ripea.core.api.service.EntitatService;
import es.caib.ripea.core.api.service.ExpedientService;
import es.caib.ripea.core.api.service.MetaDadaService;
import es.caib.ripea.core.api.service.MetaDocumentService;
import es.caib.ripea.core.api.service.MetaExpedientService;
import es.caib.ripea.core.api.service.ws.RipeaCarregaTestWsService;
import es.caib.ripea.core.entity.ArxiuEntity;
import es.caib.ripea.core.entity.EntitatEntity;
import es.caib.ripea.core.helper.UsuariHelper;

/**
 * Implementació dels mètodes per al servei de callback del portafirmes.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@Component
@WebService(
		name = "RipeaCarregaTest",
		serviceName = "RipeaCarregaTest",
		portName = "RipeaCarregaTestPort",
		targetNamespace = "http://www.caib.es/ripea/ws/ripeacarregatest")
public class RipeaCarregaTestWsServiceImpl implements RipeaCarregaTestWsService {

//	@Resource
//	private MetaDadaRepository metaDadaRepository;
	
	@Resource
	private EntitatService entitatService;
	@Resource
	private MetaExpedientService metaExpedientService;
	@Resource
	private MetaDocumentService metaDocumentService;
	@Resource
	private MetaDadaService metaDadaService;
	@Resource
	private ExpedientService expedientService;
	@Resource
	private ContingutService contingutService;
	@Resource
	private ArxiuService arxiuService;
	@Resource
	private DocumentService documentService;
	@Resource
	private UsuariHelper usuariHelper;

	
	@Transactional
	@Override
	public Long crearEntitat(
			String codi, 
			String nom, 
			String descripcio, 
			String cif, 
			String unitatArrel) {
		
		EntitatDto entitat = entitatService.findByCodi(codi);
		
		if (entitat == null) {
			entitat = new EntitatDto();
			entitat.setCodi(codi);
			entitat.setNom(nom);
			entitat.setDescripcio(descripcio);
			entitat.setCif(cif);
			entitat.setUnitatArrel(unitatArrel);
			
			entitat = entitatService.create(entitat);
		}
		return entitat.getId();
	}
	
	@Transactional(readOnly=true)
	@Override
	public Long getEntitatIdByCodi(String codi) {
		
		EntitatDto entitat = entitatService.findByCodi(codi);
		
		if (entitat != null) {
			return entitat.getId();
		}
		
		throw new NotFoundException(
				codi,
				EntitatEntity.class);
	}

	@Transactional
	@Override
	public void assignaPermisEntitat(
			Long entitatId, 
			String nom, 
			boolean usuari, 
			boolean read, 
			boolean write,
			boolean create, 
			boolean delete, 
			boolean administration) {
		
		PermisDto permis = new PermisDto();
		permis.setPrincipalNom(nom);
		permis.setPrincipalTipus(usuari ? PrincipalTipusEnumDto.USUARI: PrincipalTipusEnumDto.ROL);
		permis.setRead(read);
		permis.setWrite(write);
		permis.setCreate(create);
		permis.setDelete(delete);
		permis.setAdministration(administration);
		
		entitatService.updatePermisSuper(
				entitatId, 
				permis);
	}
	
	@Transactional(readOnly=true)
	@Override
	public Long getEscriptoriId(
			Long entitatId,
			String usuariCodi) {
		return contingutService.getEscriptoriIdByUsuari(
				usuariCodi, 
				entitatId);
	}
	
	@Transactional(readOnly=true)
	@Override
	public Long getArxiuIdByNom(
			Long entitatId,
			String unitatCodi,
			String arxiuNom) {
		
		List<ArxiuDto> arxius = arxiuService.findByUnitatCodiAdmin(
				entitatId, 
				unitatCodi);
		
		for (ArxiuDto arxiu: arxius) {
			if (arxiu.getNom().equals(arxiuNom))
				return arxiu.getId();
		}

		throw new NotFoundException(
				arxiuNom,
				ArxiuEntity.class);
	}

	@Transactional
	@Override
	public Long crearMetaExpedient(
			Long entitatId, 
			String codi, 
			String nom, 
			String descripcio,
			String classificacioDocumental, 
			String classificacioSia, 
			String unitatAdministrativa,
			boolean notificacioActiva, 
			String notificacioOrganCodi, 
			String notificacioLlibreCodi,
			String notificacioAvisTitol, 
			String notificacioAvisText, 
			String notificacioAvisTextSms,
			String notificacioOficiTitol, 
			String notificacioOficiText, 
			Long pareId) {
		
//		// TODO: Eliminar aquesta línia!!!! -> Autentica un usuari en Tomcat
//		usuariHelper.autenticaTest();

		
		MetaExpedientDto metaExpedient = metaExpedientService.findByEntitatCodi(entitatId, codi);
		
		if (metaExpedient == null) {
			metaExpedient = new MetaExpedientDto();
			metaExpedient.setCodi(codi);
			metaExpedient.setNom(nom);
			metaExpedient.setDescripcio(descripcio);
			metaExpedient.setClassificacioDocumental(classificacioDocumental);
			metaExpedient.setClassificacioSia(classificacioSia);
			metaExpedient.setUnitatAdministrativa(unitatAdministrativa);
			metaExpedient.setNotificacioActiva(notificacioActiva);
			metaExpedient.setNotificacioOrganCodi(notificacioOrganCodi);
			metaExpedient.setNotificacioLlibreCodi(notificacioLlibreCodi);
			metaExpedient.setNotificacioAvisTitol(notificacioAvisTitol);
			metaExpedient.setNotificacioAvisText(notificacioAvisTextSms);
			metaExpedient.setNotificacioAvisTextSms(notificacioAvisTextSms);
			metaExpedient.setNotificacioOficiTitol(notificacioOficiTitol);
			metaExpedient.setNotificacioOficiText(notificacioOficiText);
			metaExpedient.setPareId(pareId);
			
			metaExpedient = metaExpedientService.create(entitatId, metaExpedient);
		}
		return metaExpedient.getId();
	}

	@Transactional
	@Override
	public Long crearExpedientMetadata(
			Long entitatId, 
			Long metaExpedient, 
			String codi, 
			String nom, 
			String descripcio,
			String tipus, 
			boolean globalExpedient, 
			boolean globalDocument, 
			String globalMultiplicitat,
			boolean globalReadOnly, 
			String expedientMultiplicitat,
			boolean readOnly) {

		MetaDadaDto metaDada = metaDadaService.findByEntitatCodi(entitatId, codi);
		
		if (metaDada == null) {
			metaDada = new MetaDadaDto();
			metaDada.setCodi(codi);
			metaDada.setNom(nom);
			metaDada.setDescripcio(descripcio);
			metaDada.setTipus(MetaDadaTipusEnumDto.valueOf(tipus));
			metaDada.setGlobalExpedient(globalExpedient);
			metaDada.setGlobalDocument(globalDocument);
			metaDada.setGlobalMultiplicitat(MultiplicitatEnumDto.valueOf(globalMultiplicitat));
			metaDada.setGlobalReadOnly(globalReadOnly);
			
			metaDada = metaDadaService.create(
					entitatId, 
					metaDada);
		}
		
		
		if (!metaExpedientService.metaDadaExists(entitatId, metaExpedient, metaDada.getId())) {
			metaExpedientService.metaDadaCreate(
					entitatId, 
					metaExpedient, 
					metaDada.getId(), 
					MultiplicitatEnumDto.valueOf(expedientMultiplicitat), 
					readOnly);
		}
		return metaDada.getId();
	}

	@Transactional
	@Override
	public Long crearDocumentTipus(
			Long entitatId, 
			String codi, 
			String nom, 
			String descripcio, 
			boolean globalExpedient,
			String globalMultiplicitat, 
			boolean globalReadOnly, 
			boolean firmaPortafirmesActiva,
			String portafirmesDocumentTipus, 
			String portafirmesFluxId, 
			String[] portafirmesResponsables,
			String portafirmesFluxTipus, 
			String portafirmesCustodiaTipus, 
			boolean firmaPassarelaActiva,
			String firmaPassarelaCustodiaTipus, 
			String plantillaNom, 
			String plantillaContentType,
			byte[] plantillaContingut) {

		MetaDocumentDto metaDocument = metaDocumentService.findByEntitatCodi(entitatId, codi);
		
		if (metaDocument == null) {
			metaDocument = new MetaDocumentDto();
			metaDocument.setCodi(codi);
			metaDocument.setNom(nom);
			metaDocument.setDescripcio(descripcio);
			metaDocument.setGlobalExpedient(globalExpedient);
			metaDocument.setGlobalMultiplicitat(MultiplicitatEnumDto.valueOf(globalMultiplicitat));
			metaDocument.setGlobalReadOnly(globalReadOnly);
			metaDocument.setFirmaPortafirmesActiva(firmaPortafirmesActiva);
			metaDocument.setPortafirmesDocumentTipus(portafirmesDocumentTipus);
			metaDocument.setPortafirmesFluxId(portafirmesFluxId);
			metaDocument.setPortafirmesResponsables(portafirmesResponsables);
			metaDocument.setPortafirmesCustodiaTipus(portafirmesCustodiaTipus);
			metaDocument.setFirmaPassarelaActiva(firmaPassarelaActiva);
			metaDocument.setFirmaPassarelaCustodiaTipus(firmaPassarelaCustodiaTipus);
			
			metaDocument = metaDocumentService.create(
					entitatId, 
					metaDocument, 
					plantillaNom, 
					plantillaContentType, 
					plantillaContingut);
		}
		
		return metaDocument.getId();
	}

	@Transactional
	@Override
	public Long crearDocumentMetadata(
			Long entitatId, 
			Long documentTipus, 
			String codi, 
			String nom, 
			String descripcio,
			String tipus, 
			boolean globalExpedient, 
			boolean globalDocument, 
			String globalMultiplicitat,
			boolean globalReadOnly, 
			String documentMultiplicitat,
			boolean readOnly) {
		
		MetaDadaDto metaDada = metaDadaService.findByEntitatCodi(entitatId, codi);
		
		if (metaDada == null) {
			metaDada = new MetaDadaDto();
			metaDada.setCodi(codi);
			metaDada.setNom(nom);
			metaDada.setDescripcio(descripcio);
			metaDada.setTipus(MetaDadaTipusEnumDto.valueOf(tipus));
			metaDada.setGlobalExpedient(globalExpedient);
			metaDada.setGlobalDocument(globalDocument);
			metaDada.setGlobalMultiplicitat(MultiplicitatEnumDto.valueOf(globalMultiplicitat));
			metaDada.setGlobalReadOnly(globalReadOnly);
			
			metaDada = metaDadaService.create(
					entitatId, 
					metaDada);
		}
		
		if (!metaDocumentService.metaDadaExists(entitatId, documentTipus, metaDada.getId())) {
			metaDocumentService.metaDadaCreate(
					entitatId, 
					documentTipus, 
					metaDada.getId(), 
					MultiplicitatEnumDto.valueOf(documentMultiplicitat), 
					readOnly);
		}
		
		return metaDada.getId();
	}

	@Transactional
	@Override
	public Long crearArxiu(
			Long entitatId,
			String nom,
			String unitatCodi){
		
		ArxiuDto arxiu = null;
		List<ArxiuDto> arxius = arxiuService.findByUnitatCodiAdmin(entitatId, unitatCodi);
		for (ArxiuDto a: arxius) {
			if (a.getNom().equals(nom)) {
				arxiu = a;
				break;
			}
		}
		if (arxiu == null) {
			arxiu = new ArxiuDto();
			arxiu.setNom(nom);
			arxiu.setUnitatCodi(unitatCodi);
			
			arxiu = arxiuService.create(
					entitatId, 
					arxiu);
		}
		return arxiu.getId();
	}
	
	@Transactional
	@Override
	public ExpedientDto crearExpedient(
			Long entitatId, 
			Long pareId, 
			Long metaExpedientCodi,
			Long arxiuId, 
			Integer any,
			String nom) {
		
		System.out.println("INICI obtenció d'escritori d'usuari actual.");
		Long inici = System.currentTimeMillis();
		
		// Si no indiquen un pare, ho crearem a l'escriptori
		if (pareId == null) {
			EscriptoriDto escriptori = contingutService.getEscriptoriPerUsuariActual(entitatId);
			pareId = escriptori.getId();
		}
		
		Long fi = System.currentTimeMillis();
		System.out.println("FI obtenció d'escritori d'usuari actual. (" + (fi - inici) + "ms)");
		
		System.out.println("INICI creació d'expedient.");
		inici = fi;
		
		// Cream l'expedient
		ExpedientDto expedient = expedientService.create(
				entitatId, 
				pareId, 
				metaExpedientCodi, 
				arxiuId, 
				any, 
				nom, 
				BustiaContingutPendentTipusEnumDto.EXPEDIENT, // contingutTipus, 
				null);	// contingutId);
		
		fi = System.currentTimeMillis();
		System.out.println("FI creació d'expedient. (" + (fi - inici) + "ms)");
		
		return expedient;
	}
	
	@Transactional
	@Override
	public void modificarMetadadaExpedient(
			Long entitatId,
			Long expedientId,
			String expedientMetadadaCodi,
			Object expedientMetadadaValor) {
		
		System.out.println("INICI  Modificació de la metadada 1.");
		Long inici = System.currentTimeMillis();
		
		Map<String, Object> valors = new HashMap<String, Object>();
		valors.put(expedientMetadadaCodi, expedientMetadadaValor);
		contingutService.dadaSave(
				entitatId,
				expedientId,
				valors);
		
		Long fi = System.currentTimeMillis();
		System.out.println("FI Modificació de la metadada 1. (" + (fi - inici) + "ms)");
		
	}
	
	@Transactional
	@Override
	public DocumentDto crearDocumentExpedient(
			Long entitatId,
			Long expedientId,
			String documentTipusCodi,
			String documentTipus, 
			String fitxerNomOriginal,
			String fitxerContentType,
			byte[] fitxerContingut,
			String documentTitol,
			Date documentData,
			String documentUbicacio,
			String documentNtiOrgan,
			String documentNtiOrigen,
			String documentNtiEstat,
			String documentNtiTipusDoc) {
		
		System.out.println("INICI Creació del document.");
		Long inici = System.currentTimeMillis();
		
		MetaDocumentDto metaDocument = metaDocumentService.findByEntitatCodi(
				entitatId, 
				documentTipusCodi);
		
		DocumentTipusEnumDto tipusDocument = DocumentTipusEnumDto.valueOf(documentTipus);
		FitxerDto fitxer = null;
		
		if (DocumentTipusEnumDto.DIGITAL.equals(tipusDocument)) {
			fitxer = new FitxerDto();
			fitxer.setNom(fitxerNomOriginal);
			fitxer.setContentType(fitxerContentType);
			fitxer.setContingut(fitxerContingut);
		}
		
		DocumentDto document = new DocumentDto();
		document.setDocumentTipus(DocumentTipusEnumDto.valueOf(documentTipus));
		document.setNom(documentTitol);
		document.setData(documentData);
		document.setDataCaptura(documentData);
		document.setNtiOrgano(documentNtiOrgan);
		document.setNtiOrigen((documentNtiOrigen != null && !documentNtiOrigen.isEmpty()) ? DocumentNtiOrigenEnumDto.valueOf(documentNtiOrigen) : null);
		document.setNtiEstadoElaboracion((documentNtiEstat != null && !documentNtiEstat.isEmpty()) ? DocumentNtiEstadoElaboracionEnumDto.valueOf(documentNtiEstat) : null);
		document.setNtiTipoDocumental((documentNtiTipusDoc != null && !documentNtiTipusDoc.isEmpty()) ? DocumentNtiTipoDocumentalEnumDto.valueOf(documentNtiTipusDoc) : null);
		document.setMetaNode(metaDocument);
		document.setUbicacio(documentUbicacio);
		
		document = documentService.create(
				entitatId,
				expedientId,
				document,
				fitxer);
		
		Long fi = System.currentTimeMillis();
		System.out.println("FI Creació del document. (" + (fi - inici) + "ms)");
		
		return document;
		
	}
	
	@Transactional
	@Override
	public void modificarMetadadaDocument(
			Long entitatId,
			Long documentId,
			String documentMetadadaCodi,
			Object documentMetadadaValor) {
	
		System.out.println("INICI Modificació de la metadada 1 del document.");
		Long inici = System.currentTimeMillis();
		
		Map<String, Object> valorsDoc = new HashMap<String, Object>();
		valorsDoc.put(documentMetadadaCodi, documentMetadadaValor);
		contingutService.dadaSave(
				entitatId,
				documentId,
				valorsDoc);
		
		Long fi = System.currentTimeMillis();
		System.out.println("FI Modificació de la metadada 1 del document. (" + (fi - inici) + "ms)");
		
	}
	
	@Transactional
	@Override
	public void tancarExpedient(
			Long entitatId, 
			Long expedientId, 
			String missatge) {
		
		System.out.println("INICI Tancar l’expedient.");
		Long inici = System.currentTimeMillis();
		
		expedientService.tancar(
				entitatId, 
				expedientId, 
				missatge);
		
		Long fi = System.currentTimeMillis();
		System.out.println("FI Tancar l’expedient. (" + (fi - inici) + "ms)");
		
	}
	
	@Transactional
	@Override
	public void alliberarExpedient(
			Long entitatId, 
			Long expedientId) {
		
		System.out.println("INICI Alliberar l’expedient.");
		Long inici = System.currentTimeMillis();
		
		expedientService.alliberarAdmin(
				entitatId, 
				expedientId);
		
		Long fi = System.currentTimeMillis();
		System.out.println("FI Alliberar l’expedient. (" + (fi - inici) + "ms)");
		
	}

	@Transactional
	@Override
	public Long crearExpedientRendiment(
			Long entitatId, 
			Long pareId, 
			Long metaExpedientCodi, 
			Long arxiuId, 
			Integer any,
			String nom, 
			String expedientMetadada1Codi,
			Object expedientMetadada1Valor,
			String expedientMetadada2Codi, 
			Object expedientMetadada2Valor,
			String documentTipusCodi,
			String documentTipus, 
			String fitxerNomOriginal,
			String fitxerContentType,
			byte[] fitxerContingut,
			String documentTitol,
			Date documentData,
			String documentUbicacio,
			String documentNtiOrgan,
			String documentNtiOrigen,
			String documentNtiEstat,
			String documentNtiTipusDoc,
			String documentMetadada1Codi,
			Object documentMetadada1Valor,
			String documentMetadada2Codi,
			Object documentMetadada2Valor) {
		
		// 1. Creació de l'expedient
		ExpedientDto expedient = crearExpedient(
				entitatId, 
				pareId, 
				metaExpedientCodi,
				arxiuId, 
				any,
				nom);
		
		// 2. Modificació de la metadada 1 de l’expedient.
		modificarMetadadaExpedient(
				entitatId,
				expedient.getId(),
				expedientMetadada1Codi,
				expedientMetadada1Valor);
		
		// 3. Modificació de la metadada 2 de l’expedient.
		modificarMetadadaExpedient(
				entitatId,
				expedient.getId(),
				expedientMetadada2Codi,
				expedientMetadada2Valor);
		
		// 4. Creació del document a dins l’expedient.
		DocumentDto document = crearDocumentExpedient(
				entitatId,
				expedient.getId(),
				documentTipusCodi,
				documentTipus, 
				fitxerNomOriginal,
				fitxerContentType,
				fitxerContingut,
				documentTitol,
				documentData,
				documentUbicacio,
				documentNtiOrgan,
				documentNtiOrigen,
				documentNtiEstat,
				documentNtiTipusDoc);
		
		// 5. Modificació de la metadada 1 del document.
		modificarMetadadaDocument(
				entitatId,
				document.getId(),
				documentMetadada1Codi,
				documentMetadada1Valor);
		
		// 6. Modificació de la metadada 2 del document.
		modificarMetadadaDocument(
				entitatId,
				document.getId(),
				documentMetadada2Codi,
				documentMetadada2Valor);
		
		// 7. Tancar l’expedient.
		tancarExpedient(
				entitatId, 
				expedient.getId(), 
				"Proves de rendiment");
		
		// 8. Alliberar l’expedient.
		alliberarExpedient(
				entitatId, 
				expedient.getId());
		
		return expedient.getId();
	}

	@Transactional
	@Override
	public Long crearAnotacio(
			String oficinaCodi, 
			String llibreCodi, 
			String destinatariCodi) {
		
		// TODO 
		return null;
	}

//	private static final Logger logger = LoggerFactory.getLogger(RipeaCarregaTestWsServiceImpl.class);

}
