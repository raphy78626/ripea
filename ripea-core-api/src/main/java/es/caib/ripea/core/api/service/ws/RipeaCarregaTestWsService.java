/**
 * 
 */
package es.caib.ripea.core.api.service.ws;

import java.util.Date;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlElement;

import es.caib.ripea.core.api.dto.DocumentDto;
import es.caib.ripea.core.api.dto.ExpedientDto;


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
	
	public Long getEntitatIdByCodi(String codi);
	
	public void assignaPermisEntitat(
			Long entitatId,
			String nom,
			boolean usuari,
			boolean read,
			boolean write,
			boolean create,
			boolean delete,
			boolean administration);
	
	public Long getEscriptoriId(
			Long entitatId,
			String usuariCodi);
	
	public Long getArxiuIdByNom(
			Long entitatId,
			String unitatCodi,
			String arxiuNom);
	
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
	
	public Long crearExpedientMetadata(
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
	
	public Long crearDocumentMetadata(
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
	
	public ExpedientDto crearExpedient(
			Long entitatId, 
			Long pareId, 
			Long metaExpedientCodi,
			Long arxiuId, 
			Integer any,
			String nom);
	
	public void modificarMetadadaExpedient(
			Long entitatId,
			Long expedientId,
			String expedientMetadadaCodi,
			Object expedientMetadadaValor);
	
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
			String documentNtiTipusDoc);
	
	public void modificarMetadadaDocument(
			Long entitatId,
			Long documentId,
			String documentMetadadaCodi,
			Object documentMetadadaValor);
	
	public void tancarExpedient(
			Long entitatId, 
			Long expedientId, 
			String missatge);
	
	public void alliberarExpedient(
			Long entitatId, 
			Long expedientId);
	
	
	/**
	 * Crea un expedient.
	 * 
	 * @param registreEntrada
	 *            Dades de l'anotació al registre d'entrada.
	 * @return l'identificador de l'expedient creat.
	 */
	@WebMethod
	public Long crearExpedientRendiment(
			@WebParam(name="entitatId") 				@XmlElement(required=true) 	Long entitatId,
			@WebParam(name="pareId") 					@XmlElement(required=false) Long pareId,
			@WebParam(name="metaExpedientCodi") 		@XmlElement(required=true) 	Long metaExpedientCodi,
			@WebParam(name="arxiuId") 					@XmlElement(required=true) 	Long arxiuId,
			@WebParam(name="any") 						@XmlElement(required=true)	Integer any,
			@WebParam(name="nom") 						@XmlElement(required=true)	String nom,
			@WebParam(name="expedientMetadata1Codi") 	@XmlElement(required=true) 	String expedientMetadata1Codi, 
			@WebParam(name="expedientMetadada1Valor")	@XmlElement(required=false)	Object expedientMetadada1Valor,
			@WebParam(name="expedientMetadata2Codi")	@XmlElement(required=true) 	String expedientMetadata2Codi, 
			@WebParam(name="expedientMetadada2Valor") 	@XmlElement(required=false)	Object expedientMetadada2Valor,
			@WebParam(name="documentTipusCodi") 		@XmlElement(required=true) 	String documentTipusCodi,
			@WebParam(name="documentTipus") 			@XmlElement(required=true) 	String documentTipus,
			@WebParam(name="fitxerNomOriginal") 		@XmlElement(required=false)	String fitxerNomOriginal,
			@WebParam(name="fitxerContentType") 		@XmlElement(required=false)	String fitxerContentType,
			@WebParam(name="fitxerContingut") 			@XmlElement(required=false)	byte[] fitxerContingut,
			@WebParam(name="documentTitol") 			@XmlElement(required=true) 	String documentTitol,
			@WebParam(name="documentData") 				@XmlElement(required=true) 	Date documentData,
			@WebParam(name="documentUbicacio") 			@XmlElement(required=false)	String documentUbicacio,
			@WebParam(name="documentNtiOrgan") 			@XmlElement(required=false)	String documentNtiOrgan,
			@WebParam(name="documentNtiOrigen") 		@XmlElement(required=false)	String documentNtiOrigen,
			@WebParam(name="documentNtiEstat") 			@XmlElement(required=false)	String documentNtiEstat,
			@WebParam(name="documentNtiTipusDoc") 		@XmlElement(required=false)	String documentNtiTipusDoc,
			@WebParam(name="documentMetadada1Codi") 	@XmlElement(required=true) 	String documentMetadada1Codi,
			@WebParam(name="documentMetadada1Valor") 	@XmlElement(required=false)	Object documentMetadada1Valor,
			@WebParam(name="documentMetadada2Codi") 	@XmlElement(required=true) 	String documentMetadada2Codi,
			@WebParam(name="documentMetadada2Valor") 	@XmlElement(required=false)	Object documentMetadada2Valor);
	
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
