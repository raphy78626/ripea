/**
 * 
 */
package es.caib.ripea.core.api.service.ws;

import java.util.Date;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlElement;


/**
 * Declaració dels mètodes per al servei per a carregar dades per 
 * tests de rendiment mitjançant una aplicació externa.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@WebService(
		name = "RipeaCarregaTest",
		serviceName = "RipeaCarregaTest",
		portName = "RipeaCarregaTestPort",
		targetNamespace = "http://www.caib.es/ripea/ws/ripeacarregatest")
public interface RipeaCarregaTestWsService {

	public Long crearEntitat(
			String codi,
			String nom,
			String descripcio,
			String cif,
			String unitatArrel);
	
	public void assignaPermisEntitat(
			Long entitatId,
			String nom,
			boolean usuari,
			boolean read,
			boolean write,
			boolean create,
			boolean delete,
			boolean administration);
	
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
			Long pareId);
	
	public void crearExpedientMetadata(
			Long entitatId,
			Long metaExpedient,
			String codi,
			String nom,
			String descripcio,
			String tipus,								// TEXT, DATA, IMPORT, SENCER, FLOTANT, BOOLEA
			boolean globalExpedient,
			boolean globalDocument,
			String globalMultiplicitat,					// M_1, M_0_1, M_0_N, M_1_N
			boolean globalReadOnly,
			String expedientMultiplicitat,				// M_1, M_0_1, M_0_N, M_1_N);
			boolean readOnly);
	
	public Long crearDocumentTipus(
			Long entitatId,
			String codi,
			String nom,
			String descripcio,
			boolean globalExpedient,
			String globalMultiplicitat,					// M_1, M_0_1, M_0_N, M_1_N
			boolean globalReadOnly,
			boolean firmaPortafirmesActiva,
			String portafirmesDocumentTipus,
			String portafirmesFluxId,
			String[] portafirmesResponsables,
			String portafirmesFluxTipus,				// SERIE, PARALEL
			String portafirmesCustodiaTipus,
			boolean firmaPassarelaActiva,
			String firmaPassarelaCustodiaTipus,
			String plantillaNom,
			String plantillaContentType,
			byte[] plantillaContingut);
	
	public void crearDocumentMetadata(
			Long entitatId,
			Long documentTipus,
			String codi,
			String nom,
			String descripcio,
			String tipus,								// TEXT, DATA, IMPORT, SENCER, FLOTANT, BOOLEA
			boolean globalExpedient,
			boolean globalDocument,
			String globalMultiplicitat,					// M_1, M_0_1, M_0_N, M_1_N
			boolean globalReadOnly,
			String documentMultiplicitat,				// M_1, M_0_1, M_0_N, M_1_N);
			boolean readOnly);
	
	public Long crearArxiu(
			Long entitatId,
			String nom,
			String unitatCodi);
	
	/**
	 * Crea un expedient.
	 * 
	 * @param registreEntrada
	 *            Dades de l'anotació al registre d'entrada.
	 * @return l'identificador de l'expedient creat.
	 */
	@WebMethod
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
			Object documentMetadada2Valor);
	
	/**
	 * Crea una anotació de registre.
	 * 
	 * @param registreEntrada
	 *            Dades de l'anotació al registre d'entrada.
	 * @return l'identificador de l'anotació creada
	 */
	@WebMethod
	public Long crearAnotacio(
			@WebParam(name="oficinaCodi") 			@XmlElement(required=true) String oficinaCodi,
			@WebParam(name="llibreCodi") 			@XmlElement(required=true) String llibreCodi,
			@WebParam(name="destinatariCodi") 		@XmlElement(required=true) String destinatariCodi);

}
