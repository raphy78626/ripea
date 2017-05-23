/**
 * 
 */
package es.caib.ripea.core.service.ws.carregatest;

import javax.annotation.Resource;
import javax.jws.WebService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import es.caib.ripea.core.api.dto.BustiaContingutPendentTipusEnumDto;
import es.caib.ripea.core.api.dto.EntitatDto;
import es.caib.ripea.core.api.dto.MetaDadaDto;
import es.caib.ripea.core.api.dto.MetaDadaTipusEnumDto;
import es.caib.ripea.core.api.dto.MetaDocumentDto;
import es.caib.ripea.core.api.dto.MetaExpedientDto;
import es.caib.ripea.core.api.dto.MetaNodeMetaDadaDto;
import es.caib.ripea.core.api.dto.MultiplicitatEnumDto;
import es.caib.ripea.core.api.dto.PermisDto;
import es.caib.ripea.core.api.dto.PrincipalTipusEnumDto;
import es.caib.ripea.core.api.service.EntitatService;
import es.caib.ripea.core.api.service.ExpedientService;
import es.caib.ripea.core.api.service.MetaDadaService;
import es.caib.ripea.core.api.service.MetaDocumentService;
import es.caib.ripea.core.api.service.MetaExpedientService;
import es.caib.ripea.core.api.service.ws.RipeaCarregaTestWsService;

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
		targetNamespace = "http://www.caib.es/ripea/ws/carregatest")
public class RipeaCarregaTestWsServiceImpl implements RipeaCarregaTestWsService {

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
//	@Resource
//	private DocumentService documentService;
//	@Resource
//	private IntegracioHelper integracioHelper;

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

	@Override
	public Long crearExpedientTipus(
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

	@Override
	public void crearExpedientMetadata(
			Long entitatId, 
			Long expedientTipus, 
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
		
		MetaNodeMetaDadaDto mde = metaExpedientService.metaDadaFind(entitatId, expedientTipus, metaDada.getId());
		if (mde == null) {
			metaExpedientService.metaDadaCreate(
					entitatId, 
					expedientTipus, 
					metaDada.getId(), 
					MultiplicitatEnumDto.valueOf(expedientMultiplicitat), 
					readOnly);
		}
	}

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

	@Override
	public void crearDocumentMetadata(
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
		
		MetaNodeMetaDadaDto mde = metaDocumentService.metaDadaFind(entitatId, documentTipus, metaDada.getId());
		if (mde == null) {
			metaDocumentService.metaDadaCreate(
					entitatId, 
					documentTipus, 
					metaDada.getId(), 
					MultiplicitatEnumDto.valueOf(documentMultiplicitat), 
					readOnly);
		}
		
	}

	@Override
	public Long crearExpedient(
			Long entitatId, 
			Long pareId, 
			Long expedientTipusCodi, 
			Long arxiuId, 
			Integer any,
			String nom, 
			String expedientMetadata1, 
			String expedientMetadata2, 
			String documentTipusCodi,
			String documentTipus, 
			String documentMetadata1, 
			String documentMetadata2) {
		// TODO 
		
		// 1. Creació de l'expedient
		expedientService.create(
				entitatId, 
				pareId, 
				expedientTipusCodi, 
				arxiuId, 
				any, 
				nom, 
				BustiaContingutPendentTipusEnumDto.EXPEDIENT, // contingutTipus, 
				null);	// contingutId);
		
		
		// 2. Modificació de la metadada 1 de l’expedient.
		
		
		// 3. Modificació de la metadada 2 de l’expedient.
		
		
		// 4. Creació del document a dins l’expedient.
		
		
		// 5. Modificació de la metadada 1 del document.
		
		
		// 6. Modificació de la metadada 2 del document.
		
		
		// 7. Tancar l’expedient.
		
		
		// 8. Alliberar l’expedient.
		
		
		return null;
	}

	@Override
	public Long crearAnotacio(
			String oficinaCodi, 
			String llibreCodi, 
			String destinatariCodi) {
		// TODO 
		return null;
	}

	private static final Logger logger = LoggerFactory.getLogger(RipeaCarregaTestWsServiceImpl.class);

}
