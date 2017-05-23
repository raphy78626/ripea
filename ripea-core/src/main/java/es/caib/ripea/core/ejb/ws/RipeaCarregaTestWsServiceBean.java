/**
 * 
 */
package es.caib.ripea.core.ejb.ws;

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
		targetNamespace = "http://www.caib.es/ripea/ws/carregatest")
@WebContext(
		contextRoot = "/ripea/ws",
		urlPattern = "/carregaTest",
		authMethod = "WSBASIC",
		transportGuarantee = "NONE",
		secureWSDLAccess = false)
@RolesAllowed({"IPA_TESTWS"})
@SecurityDomain("seycon")
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
		usuariHelper.generarUsuariAutenticatEjb(
				sessionContext,
				true);
		return delegate.crearExpedientTipus(
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
		usuariHelper.generarUsuariAutenticatEjb(
				sessionContext,
				true);
		delegate.crearExpedientMetadata(
				entitatId, 
				expedientTipus, 
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
		usuariHelper.generarUsuariAutenticatEjb(
				sessionContext,
				true);
		return delegate.crearExpedient(
				entitatId, 
				pareId, 
				expedientTipusCodi, 
				arxiuId, 
				any, 
				nom, 
				expedientMetadata1, 
				expedientMetadata2, 
				documentTipusCodi, 
				documentTipus, 
				documentMetadata1, 
				documentMetadata2);
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
