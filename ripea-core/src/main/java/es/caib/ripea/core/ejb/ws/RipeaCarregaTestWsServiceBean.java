/**
 * 
 */
package es.caib.ripea.core.ejb.ws;

import java.util.Date;

import javax.annotation.Resource;
import javax.annotation.security.RolesAllowed;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.jws.WebService;

import org.jboss.annotation.security.SecurityDomain;
import org.jboss.wsf.spi.annotation.WebContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;

import es.caib.ripea.core.api.service.ws.RipeaCarregaTestWsService;
import es.caib.ripea.core.helper.UsuariHelper;
import es.caib.ripea.core.service.ws.carregatest.RipeaCarregaTestWsServiceImpl;

/**
 * Implementació dels mètodes per al servei de bústies de RIPEA.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@Stateless
@WebService(
		name = "RipeaCarregaTest",
		serviceName = "RipeaCarregaTest",
		portName = "RipeaCarregaTestPort",
		targetNamespace = "http://www.caib.es/ripea/ws/ripeacarregatest")
@WebContext(
		contextRoot = "/ripea/ws",
		urlPattern = "/ripeaCarregaTest",
//		authMethod = "WSBASIC",
		transportGuarantee = "NONE",
		secureWSDLAccess = false)
//@RolesAllowed({"IPA_TESTWS"})
//@SecurityDomain("seycon")
@Interceptors(SpringBeanAutowiringInterceptor.class)
public class RipeaCarregaTestWsServiceBean implements RipeaCarregaTestWsService {

	@Autowired
	private RipeaCarregaTestWsServiceImpl delegate;
	
	@Resource
	private SessionContext sessionContext;
	@Autowired
	private UsuariHelper usuariHelper;


	@Override
	public Long crearEntitat(
			String codi, 
			String nom, 
			String descripcio, 
			String cif, 
			String unitatArrel) {
		usuariHelper.generarUsuariAutenticatEjb(
				sessionContext,
				true);
		return delegate.crearEntitat(
				codi, 
				nom, 
				descripcio, 
				cif, 
				unitatArrel);
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
		usuariHelper.generarUsuariAutenticatEjb(
				sessionContext,
				true);
		delegate.assignaPermisEntitat(
				entitatId, 
				nom, 
				usuari, 
				read, 
				write, 
				create, 
				delete, 
				administration);
	}

	@Override
	public Long crearmetaExpedient(
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
		usuariHelper.generarUsuariAutenticatEjb(
				sessionContext,
				true);
		return delegate.crearmetaExpedient(
				entitatId, 
				codi, 
				nom, 
				descripcio, 
				classificacioDocumental, 
				classificacioSia, 
				unitatAdministrativa, 
				notificacioActiva, 
				notificacioOrganCodi, 
				notificacioLlibreCodi, 
				notificacioAvisTitol, 
				notificacioAvisText, 
				notificacioAvisTextSms, 
				notificacioOficiTitol, 
				notificacioOficiText, 
				pareId);
	}

	@Override
	public void crearExpedientMetadata(
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
		usuariHelper.generarUsuariAutenticatEjb(
				sessionContext,
				true);
		delegate.crearExpedientMetadata(
				entitatId, 
				metaExpedient, 
				codi, 
				nom, 
				descripcio, 
				tipus, 
				globalExpedient, 
				globalDocument, 
				globalMultiplicitat, 
				globalReadOnly, 
				expedientMultiplicitat,
				readOnly);
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
		usuariHelper.generarUsuariAutenticatEjb(
				sessionContext,
				true);
		return delegate.crearDocumentTipus(
				entitatId, 
				codi, 
				nom, 
				descripcio, 
				globalExpedient, 
				globalMultiplicitat, 
				globalReadOnly, 
				firmaPortafirmesActiva, 
				portafirmesDocumentTipus, 
				portafirmesFluxId, 
				portafirmesResponsables, 
				portafirmesFluxTipus, 
				portafirmesCustodiaTipus, 
				firmaPassarelaActiva, 
				firmaPassarelaCustodiaTipus, 
				plantillaNom, 
				plantillaContentType, 
				plantillaContingut);
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
		usuariHelper.generarUsuariAutenticatEjb(
				sessionContext,
				true);
		delegate.crearDocumentMetadata(
				entitatId, 
				documentTipus, 
				codi, 
				nom, 
				descripcio, 
				tipus, 
				globalExpedient, 
				globalDocument, 
				globalMultiplicitat, 
				globalReadOnly, 
				documentMultiplicitat,
				readOnly);
	}
	
	@Override
	public Long crearArxiu(
			Long entitatId,
			String nom,
			String unitatCodi){
		usuariHelper.generarUsuariAutenticatEjb(
				sessionContext,
				true);
		return delegate.crearArxiu(
				entitatId,
				nom,
				unitatCodi);
	}

	@Override
	public Long crearExpedient(
			Long entitatId, 
			Long pareId, 
			Long metaExpedientCodi, 
			Long arxiuId, 
			Integer any,
			String nom, 
			String expedientMetadata1Codi, 
			Object expedientMetadada1Valor,
			String expedientMetadata2Codi, 
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
		usuariHelper.generarUsuariAutenticatEjb(
				sessionContext,
				true);
		return delegate.crearExpedient(
				entitatId, 
				pareId, 
				metaExpedientCodi, 
				arxiuId, 
				any, 
				nom, 
				expedientMetadata1Codi, 
				expedientMetadada1Valor,
				expedientMetadata2Codi, 
				expedientMetadada2Valor,
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
				documentNtiTipusDoc,
				documentMetadada1Codi,
				documentMetadada1Valor,
				documentMetadada2Codi,
				documentMetadada2Valor);
	}

	@Override
	public Long crearAnotacio(String oficinaCodi, String llibreCodi, String destinatariCodi) {
		usuariHelper.generarUsuariAutenticatEjb(
				sessionContext,
				true);
		return delegate.crearAnotacio(
				oficinaCodi, 
				llibreCodi, 
				destinatariCodi);
	}

}
