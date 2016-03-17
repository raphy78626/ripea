/**
 * 
 */
package es.caib.ripea.core.api.service;

import java.util.Date;
import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;

import es.caib.ripea.core.api.dto.DocumentDto;
import es.caib.ripea.core.api.dto.DocumentVersioDto;
import es.caib.ripea.core.api.dto.FitxerDto;
import es.caib.ripea.core.api.dto.PortafirmesPrioritatEnumDto;
import es.caib.ripea.core.api.exception.NomInvalidException;
import es.caib.ripea.core.api.exception.NotFoundException;
import es.caib.ripea.core.api.exception.PluginException;

/**
 * Declaració dels mètodes per a gestionar documents.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public interface DocumentService {

	/**
	 * Crea un nou document a dins un contenidor.
	 * 
	 * @param entitatId
	 *            Atribut id de l'entitat a la qual pertany el contenidor.
	 * @param contenidorId
	 *            Atribut id del contenidor del qual es vol consultar el contingut.
	 * @param metaDocumentId
	 *            Atribut id del meta-document a partir del qual es vol crear el document.
	 * @param nom
	 *            Nom del document que es vol crear.
	 * @param data
	 *            Data del document que es vol crear.
	 * @param arxiuNom
	 *            Nom de l'arxiu del document.
	 * @param arxiuContentType
	 *            ContentType de l'arxiu del document.
	 * @param arxiuContingut
	 *            Contingut de l'arxiu del document.
	 * @return El document creat.
	 * @throws NotFoundException
	 *             Si no s'ha trobat l'objecte amb l'id especificat.
	 * @throws NomInvalidException
	 *             Si el nom del contenidor conté caràcters invàlids.
	 */
	@PreAuthorize("hasRole('ROLE_USER')")
	public DocumentDto create(
			Long entitatId,
			Long contenidorId,
			Long metaDocumentId,
			String nom,
			Date data,
			String arxiuNom,
			String arxiuContentType,
			byte[] arxiuContingut) throws NotFoundException, NomInvalidException;

	/**
	 * Modifica un document.
	 * 
	 * @param entitatId
	 *            Atribut id de l'entitat a la qual pertany el contenidor.
	 * @param id
	 *            Atribut id del document que es vol modificar.
	 * @param metaDocumentId
	 *            Atribut id del meta-document.
	 * @param nom
	 *            Nom del document.
	 * @param data
	 *            Data del document.
	 * @param arxiuNom
	 *            Nom de l'arxiu del document.
	 * @param arxiuContentType
	 *            ContentType de l'arxiu del document.
	 * @param arxiuContingut
	 *            Contingut de l'arxiu del document.
	 * @return El document modificat.
	 * @throws NotFoundException
	 *             Si no s'ha trobat l'objecte amb l'id especificat.
	 * @throws NomInvalidException
	 *             Si el nom del contenidor conté caràcters invàlids.
	 */
	@PreAuthorize("hasRole('ROLE_USER')")
	public DocumentDto update(
			Long entitatId,
			Long id,
			Long metaDocumentId,
			String nom,
			Date data,
			String arxiuNom,
			String arxiuContentType,
			byte[] arxiuContingut) throws NotFoundException, NomInvalidException;

	/**
	 * Esborra un document.
	 * 
	 * @param entitatId
	 *            Atribut id de l'entitat a la qual pertany el contenidor.
	 * @param id
	 *            Atribut id del document que es vol esborrar.
	 * @return El document esborrat.
	 * @throws NotFoundException
	 *             Si no s'ha trobat l'objecte amb l'id especificat.
	 */
	@PreAuthorize("hasRole('ROLE_USER')")
	public DocumentDto delete(
			Long entitatId,
			Long id) throws NotFoundException;

	/**
	 * Consulta un document donat el seu id.
	 * 
	 * @param entitatId
	 *            Atribut id de l'entitat a la qual pertany el contenidor.
	 * @param id
	 *            Atribut id del document que es vol trobar.
	 * @return El document.
	 * @throws NotFoundException
	 *             Si no s'ha trobat l'objecte amb l'id especificat.
	 */
	@PreAuthorize("hasRole('ROLE_USER')")
	public DocumentDto findById(
			Long entitatId,
			Long id) throws NotFoundException;

	/**
	 * Consulta les versions d'un document.
	 * 
	 * @param entitatId
	 *            Atribut id de l'entitat a la qual pertany el contenidor.
	 * @param id
	 *            Atribut id del document del qual es volen recuperar les versions.
	 * @return La llista de versions.
	 * @throws NotFoundException
	 *             Si no s'ha trobat l'objecte amb l'id especificat.
	 */
	@PreAuthorize("hasRole('ROLE_USER')")
	public List<DocumentVersioDto> findVersionsByDocument(
			Long entitatId,
			Long id) throws NotFoundException;

	/**
	 * Consulta la darrera versió del document.
	 * 
	 * @param entitatId
	 *            Atribut id de l'entitat a la qual pertany el contenidor.
	 * @param id
	 *            Atribut id del document del qual es vol descarregar el contingut.
	 * @return la darrera versió del document.
	 * @throws NotFoundException
	 *             Si no s'ha trobat l'objecte amb l'id especificat.
	 */
	@PreAuthorize("hasRole('ROLE_USER')")
	public DocumentVersioDto findDarreraVersio(
			Long entitatId,
			Long id) throws NotFoundException;

	/**
	 * Consulta una versió del document.
	 * 
	 * @param entitatId
	 *            Atribut id de l'entitat a la qual pertany el contenidor.
	 * @param id
	 *            Atribut id del document del qual es vol descarregar el contingut.
	 * @param versio
	 *            El número de versió del document que es vol descarregar.
	 * @return la versió del document.
	 * @throws NotFoundException
	 *             Si no s'ha trobat l'objecte amb l'id especificat.
	 */
	@PreAuthorize("hasRole('ROLE_USER')")
	public DocumentVersioDto findVersio(
			Long entitatId,
			Long id,
			int versio) throws NotFoundException;

	/**
	 * Descarrega el contingut d'un document.
	 * 
	 * @param entitatId
	 *            Atribut id de l'entitat a la qual pertany el contenidor.
	 * @param id
	 *            Atribut id del document del qual es vol descarregar el contingut.
	 * @param versio
	 *            El número de versió del document que es vol descarregar.
	 * @return el fitxer amb el contingut.
	 * @throws NotFoundException
	 *             Si no s'ha trobat l'objecte amb l'id especificat.
	 */
	@PreAuthorize("hasRole('ROLE_USER')")
	public FitxerDto descarregar(
			Long entitatId,
			Long id,
			int versio) throws NotFoundException;


	/**
	 * Envia un document a firmar al portafirmes.
	 * 
	 * @param entitatId
	 *            Atribut id de l'entitat a la qual pertany el contenidor.
	 * @param id
	 *            Atribut id del document que es vol enviar a firmar.
	 * @param versio
	 *            El número de versió del document que es vol enviar a firmar.
	 * @param motiu
	 *            El motiu de l'enviament.
	 * @param prioritat
	 *            La prioritat de l'enviament.
	 * @param dataCaducitat
	 *            La data màxima per a firmar el document.
	 * @throws NotFoundException
	 *             Si no s'ha trobat l'objecte amb l'id especificat.
	 * @throws IllegalStateException
	 *             Si hi ha enviaments a portafirmes pendents per aquest document.
	 * @throws PluginException
	 *             Hi ha hagut algun error en la comunicació amb el portafirmes.
	 */
	@PreAuthorize("hasRole('ROLE_USER')")
	public void portafirmesEnviar(
			Long entitatId,
			Long id,
			int versio,
			String motiu,
			PortafirmesPrioritatEnumDto prioritat,
			Date dataCaducitat) throws NotFoundException, IllegalStateException, PluginException;

	/**
	 * Cancela l'enviament d'un document a firmar al portafirmes.
	 * 
	 * @param entitatId
	 *            Atribut id de l'entitat a la qual pertany el contenidor.
	 * @param id
	 *            Atribut id del document que es vol enviar a firmar.
	 * @param versio
	 *            El número de versió del document que es vol enviar a firmar.
	 * @throws NotFoundException
	 *             Si no s'ha trobat l'objecte amb l'id especificat.
	 * @throws IllegalStateException
	 *             Si no s'ha trobat l'enviament al portafirmes pel document.
	 * @throws PluginException
	 *             Hi ha hagut algun error en la comunicació amb el portafirmes.
	 */
	@PreAuthorize("hasRole('ROLE_USER')")
	public void portafirmesCancelar(
			Long entitatId,
			Long id,
			int versio) throws NotFoundException, IllegalStateException, PluginException;

	/**
	 * Processa una petició del callback de portafirmes.
	 * 
	 * @param documentId
	 *            Atribut id del document del portafirmes.
	 * @param estat
	 *            Nou estat del document.
	 * @return null si tot ha anat bé o una excepció si s'ha produit algun error
	 *            al processar el document firmat.
	 * @throws NotFoundException
	 *             Si no s'ha trobat l'objecte amb l'id especificat.
	 */
	public Exception portafirmesCallback(
			int documentId,
			int estat) throws NotFoundException;

	/**
	 * Converteix el document a format PDF per a firmar-lo.
	 * 
	 * @param entitatId
	 *            Atribut id de l'entitat a la qual pertany el contenidor.
	 * @param id
	 *            Atribut id del document que es vol convertir.
	 * @param versio
	 *            El número de versió del document que es vol convertir.
	 * @return el fitxer convertit.
	 * @throws NotFoundException
	 *             Si no s'ha trobat l'objecte amb l'id especificat.
	 * @throws PluginException
	 *             Hi ha hagut algun error en la comunicació amb el portafirmes.
	 */
	@PreAuthorize("hasRole('ROLE_USER')")
	public FitxerDto convertirPdfPerFirma(
			Long entitatId,
			Long id,
			int versio) throws NotFoundException, PluginException;

	/**
	 * Genera un identificador del document per firmar via applet.
	 * 
	 * @param entitatId
	 *            Atribut id de l'entitat a la qual pertany el contenidor.
	 * @param id
	 *            Atribut id del document que es vol convertir.
	 * @param versio
	 *            El número de versió del document del qual es vol generar identif.
	 * @return l'identificador generat.
	 * @throws NotFoundException
	 *             Si no s'ha trobat l'objecte amb l'id especificat.
	 * @throws PluginException
	 *             Hi ha hagut algun error en la comunicació amb la custòdia.
	 */
	@PreAuthorize("hasRole('ROLE_USER')")
	public String generarIdentificadorFirmaApplet(
			Long entitatId,
			Long id,
			int versio) throws NotFoundException, PluginException;

	/**
	 * Envia a custòdia un document firmat al navegador.
	 * 
	 * @param identificador
	 *            Identificador del document generat amb anterioritat.
	 * @param arxiuNom
	 *            Nom de l'arxiu firmat.
	 * @param arxiuContingut
	 *            Contingut de l'arxiu firmat.
	 * @throws NotFoundException
	 *             Si no s'ha trobat l'objecte amb l'id especificat.
	 * @throws PluginException
	 *             Hi ha hagut algun error en la comunicació amb la custòdia.
	 */
	@PreAuthorize("hasRole('ROLE_USER')")
	public void custodiaEnviarDocumentFirmat(
			String identificador,
			String arxiuNom,
			byte[] arxiuContingut) throws NotFoundException, PluginException;

	/**
	 * Reintenta la custòdia d'un document firmat amb portafirmes que ha donat
	 * error al custodiar.
	 * 
	 * @param entitatId
	 *            Atribut id de l'entitat a la qual pertany el contenidor.
	 * @param id
	 *            Atribut id del document que es vol custodiar.
	 * @param versio
	 *            El número de versió del document que es vol custodiar.
	 * @throws NotFoundException
	 *             Si no s'ha trobat l'objecte amb l'id especificat.
	 * @throws PluginException
	 *             Hi ha hagut algun error en la comunicació amb la custòdia.
	 */
	@PreAuthorize("hasRole('ROLE_USER')")
	public void custodiaPortafirmesReintentar(
			Long entitatId,
			Long id,
			int versio) throws NotFoundException, PluginException;

	/**
	 * Esborrar un document custodiat.
	 * 
	 * @param entitatId
	 *            Atribut id de l'entitat a la qual pertany el contenidor.
	 * @param id
	 *            Atribut id del document que es vol custodiar.
	 * @param versio
	 *            El número de versió del document que es vol custodiar.
	 * @throws NotFoundException
	 *             Si no s'ha trobat l'objecte amb l'id especificat.
	 * @throws PluginException
	 *             Hi ha hagut algun error en la comunicació amb la custòdia.
	 */
	@PreAuthorize("hasRole('ROLE_USER')")
	public void custodiaEsborrar(
			Long entitatId,
			Long id,
			int versio) throws NotFoundException, PluginException;

}
