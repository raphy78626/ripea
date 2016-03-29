/**
 * 
 */
package es.caib.ripea.core.api.service;

import java.util.Date;
import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;

import es.caib.ripea.core.api.dto.ContenidorDto;
import es.caib.ripea.core.api.dto.ContenidorFiltreDto;
import es.caib.ripea.core.api.dto.ContenidorLogDto;
import es.caib.ripea.core.api.dto.ContenidorMovimentDto;
import es.caib.ripea.core.api.dto.DadaDto;
import es.caib.ripea.core.api.dto.EscriptoriDto;
import es.caib.ripea.core.api.dto.PaginaDto;
import es.caib.ripea.core.api.dto.PaginacioParamsDto;
import es.caib.ripea.core.api.dto.ValidacioErrorDto;
import es.caib.ripea.core.api.exception.NotFoundException;
import es.caib.ripea.core.api.exception.ValidationException;

/**
 * Declaració dels mètodes per a gestionar contenidors.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public interface ContenidorService {

	/**
	 * Canvia el nom del contenidor.
	 * 
	 * @param entitatId
	 *            Atribut id de l'entitat a la qual pertany el contenidor.
	 * @param contenidorId
	 *            Atribut id del contenidor del qual es vol consultar el contingut.
	 * @param nom
	 *            El nom que es vol posar al contenidor.
	 * @return El contenidor modificat.
	 * @throws NotFoundException
	 *             Si no s'ha trobat l'objecte amb l'id especificat.
	 */
	@PreAuthorize("hasRole('tothom')")
	public ContenidorDto rename(
			Long entitatId,
			Long contenidorId,
			String nom) throws NotFoundException;

	/**
	 * Crea una nova dada a dins el node.
	 * 
	 * @param entitatId
	 *            Atribut id de l'entitat a la qual pertany el contenidor.
	 * @param contenidorId
	 *            Atribut id del contenidor del qual es vol consultar el contingut.
	 * @param metaDadaId
	 *            Atribut id de la meta-dada a partir del qual es vol crear aquesta dada.
	 * @param valor
	 *            Valor de la dada que es vol crear.
	 * @return La dada creada.
	 * @throws NotFoundException
	 *             Si no s'ha trobat l'objecte amb l'id especificat.
	 * @throws ValidationException
	 *             Si es volen afegir al node més dades de les permeses.
	 */
	@PreAuthorize("hasRole('tothom')")
	public DadaDto dadaCreate(
			Long entitatId,
			Long contenidorId,
			Long metaDadaId,
			Object valor) throws NotFoundException, ValidationException;

	/**
	 * Modifica una dada a dins el node.
	 * 
	 * @param entitatId
	 *            Atribut id de l'entitat a la qual pertany el contenidor.
	 * @param contenidorId
	 *            Atribut id del contenidor del qual es vol consultar el contingut.
	 * @param dadaId
	 *            Atribut id de la meta-dada a partir del qual es vol crear aquesta dada.
	 * @param valor
	 *            Valor de la dada que es vol modificar.
	 * @return La dada modificada.
	 * @throws NotFoundException
	 *             Si no s'ha trobat l'objecte amb l'id especificat.
	 */
	@PreAuthorize("hasRole('tothom')")
	public DadaDto dadaUpdate(
			Long entitatId,
			Long contenidorId,
			Long dadaId,
			Object valor) throws NotFoundException;

	/**
	 * Esborra una dada a dins el node.
	 * 
	 * @param entitatId
	 *            Atribut id de l'entitat a la qual pertany el contenidor.
	 * @param contenidorId
	 *            Atribut id del contenidor del qual es vol consultar el contingut.
	 * @param dadaId
	 *            Atribut id de la meta-dada a partir del qual es vol crear aquesta dada.
	 * @return La dada esborrada.
	 * @throws NotFoundException
	 *             Si no s'ha trobat l'objecte amb l'id especificat.
	 */
	@PreAuthorize("hasRole('tothom')")
	public DadaDto dadaDelete(
			Long entitatId,
			Long contenidorId,
			Long dadaId) throws NotFoundException;

	/**
	 * Obté una dada de dins el node.
	 * 
	 * @param entitatId
	 *            Atribut id de l'entitat a la qual pertany el contenidor.
	 * @param contenidorId
	 *            Atribut id del contenidor del qual es vol consultar el contingut.
	 * @param dadaId
	 *            Atribut id de la meta-dada a partir del qual es vol crear aquesta dada.
	 * @return La dada esborrada.
	 * @throws NotFoundException
	 *             Si no s'ha trobat l'objecte amb l'id especificat.
	 */
	@PreAuthorize("hasRole('tothom')")
	public DadaDto dadaFindById(
			Long entitatId,
			Long contenidorId,
			Long dadaId) throws NotFoundException;

	/**
	 * Marca un contenidor com a esborrat. Posteriorment un administrador
	 * el podria recuperar.
	 * 
	 * @param entitatId
	 *            Atribut id de l'entitat a la qual pertany el contenidor.
	 * @param contenidorId
	 *            Atribut id del contenidor que es vol esborrar.
	 * @param nomesMarcarEsborrat
	 *            Posar a true si es vol esborrar el contenidor definitivament
	 *            o false si només es vol marcar com a esborrat.
	 * @return El contenidor esborrat.
	 * @throws NotFoundException
	 *             Si no s'ha trobat l'objecte amb l'id especificat.
	 */
	@PreAuthorize("hasRole('tothom')")
	public ContenidorDto deleteReversible(
			Long entitatId,
			Long contenidorId) throws NotFoundException;

	/**
	 * Esborra un contenidor sense possibilitat de recuperar-lo.
	 * 
	 * @param entitatId
	 *            Atribut id de l'entitat a la qual pertany el contenidor.
	 * @param contenidorId
	 *            Atribut id del contenidor que es vol esborrar.
	 * @return El contenidor esborrat.
	 * @throws NotFoundException
	 *             Si no s'ha trobat l'objecte amb l'id especificat.
	 */
	@PreAuthorize("hasRole('IPA_ADMIN')")
	public ContenidorDto deleteDefinitiu(
			Long entitatId,
			Long contenidorId) throws NotFoundException;

	/**
	 * Recupera un contenidor marcat com a esborrat.
	 * 
	 * @param entitatId
	 *            Atribut id de l'entitat a la qual pertany el contenidor.
	 * @param contenidorId
	 *            Atribut id del contenidor que es vol esborrar.
	 * @return El contenidor recuperat.
	 * @throws NotFoundException
	 *             Si no s'ha trobat l'objecte amb l'id especificat.
	 * @throws ValidationException
	 *             Si ja existeix un altre contenidor amb el mateix nom
	 *             a dins el mateix pare.
	 */
	@PreAuthorize("hasRole('IPA_ADMIN')")
	public ContenidorDto undelete(
			Long entitatId,
			Long contenidorId) throws NotFoundException, ValidationException;

	/**
	 * Mou un contenidor al destí especificat.
	 * 
	 * @param entitatId
	 *            Atribut id de l'entitat a la qual pertany el contenidor.
	 * @param contenidorOrigenId
	 *            Atribut id del contenidor que es vol moure.
	 * @param contenidorDestiId
	 *            Atribut id del contenidor a on es vol moure l'origen.
	 * @return El contenidor mogut.
	 * @throws NotFoundException
	 *             Si no s'ha trobat l'objecte amb l'id especificat.
	 * @throws ValidationException
	 *             Si ja existeix un altre contenidor amb el mateix nom
	 *             a dins el destí.
	 */
	@PreAuthorize("hasRole('tothom')")
	public ContenidorDto move(
			Long entitatId,
			Long contenidorOrigenId,
			Long contenidorDestiId) throws NotFoundException, ValidationException;

	/**
	 * Copia un contenidor al destí especificat.
	 * 
	 * @param entitatId
	 *            Atribut id de l'entitat a la qual pertany el contenidor.
	 * @param contenidorOrigenId
	 *            Atribut id del contenidor que es vol copiar.
	 * @param contenidorDestiId
	 *            Atribut id del contenidor a on es vol posar la còpia.
	 * @param recursiu
	 *            Amb el valor 'true' indica que .
	 * @return El contenidor creat amb la còpia.
	 * @throws NotFoundException
	 *             Si no s'ha trobat l'objecte amb l'id especificat.
	 * @throws ValidationException
	 *             Si ja existeix un altre contenidor amb el mateix nom
	 *             a dins el destí.
	 */
	@PreAuthorize("hasRole('tothom')")
	public ContenidorDto copy(
			Long entitatId,
			Long contenidorOrigenId,
			Long contenidorDestiId,
			boolean recursiu) throws NotFoundException, ValidationException;

	/**
	 * Envia un contenidor a la bústia especificada.
	 * 
	 * @param entitatId
	 *            Atribut id de l'entitat a la qual pertany el contenidor.
	 * @param contenidorId
	 *            Atribut id del contenidor que es vol enviar.
	 * @param bustiaId
	 *            Atribut id de la bústia destinatària.
	 * @param comentari
	 *            Comentari per a l'enviament.
	 * @return El contenidor enviat.
	 * @throws NotFoundException
	 *             Si no s'ha trobat l'objecte amb l'id especificat.
	 */
	@PreAuthorize("hasRole('tothom')")
	public ContenidorDto send(
			Long entitatId,
			Long contenidorId,
			Long bustiaId,
			String comentari) throws NotFoundException;

	/**
	 * Reb un contenidor situat a la bústia especificada.
	 * 
	 * @param entitatId
	 *            Atribut id de l'entitat a la qual pertany el contenidor.
	 * @param bustiaId
	 *            Atribut id de la bústia destinatària.
	 * @param contenidorOrigenId
	 *            Atribut id del contenidor que es vol rebre.
	 * @param contenidorDestiId
	 *            Atribut id del contenidor a on es vol situar el contingut rebut.
	 * @return El contenidor rebut.
	 * @throws NotFoundException
	 *             Si no s'ha trobat l'objecte amb l'id especificat.
	 */
	@PreAuthorize("hasRole('tothom')")
	public ContenidorDto receive(
			Long entitatId,
			Long bustiaId,
			Long contenidorOrigenId,
			Long contenidorDestiId) throws NotFoundException;

	/**
	 * Obté el contingut de l'escriptori de l'usuari actual.
	 * 
	 * @param entitatId
	 *            Atribut id de l'entitat a la qual pertany el contenidor.
	 * @return l'objecte que representa l'escriptori.
	 * @throws NotFoundException
	 *             Si no s'ha trobat l'objecte amb l'id especificat.
	 */
	@PreAuthorize("hasRole('tothom')")
	public EscriptoriDto getEscriptoriPerUsuariActual(
			Long entitatId) throws NotFoundException;

	/**
	 * Obté la informació del contenidor especificat sense el seu contingut.
	 * 
	 * @param entitatId
	 *            Atribut id de l'entitat a la qual pertany el contenidor.
	 * @param contenidorId
	 *            Atribut id del contenidor del qual es vol consultar el contingut.
	 * @return El contenidor sense el seu contingut.
	 * @throws NotFoundException
	 *             Si no s'ha trobat l'objecte amb l'id especificat.
	 */
	@PreAuthorize("hasRole('tothom')")
	public ContenidorDto getContenidorSenseContingut(
			Long entitatId,
			Long contenidorId) throws NotFoundException;

	/**
	 * Obté la informació del contenidor especificat juntament amb el seu contingut.
	 * 
	 * @param entitatId
	 *            Atribut id de l'entitat a la qual pertany el contenidor.
	 * @param contenidorId
	 *            Atribut id del contenidor del qual es vol consultar el contingut.
	 * @return El contenidor i el seu contingut.
	 * @throws NotFoundException
	 *             Si no s'ha trobat l'objecte amb l'id especificat.
	 */
	@PreAuthorize("hasRole('tothom')")
	public ContenidorDto getContenidorAmbContingut(
			Long entitatId,
			Long contenidorId) throws NotFoundException;

	/**
	 * Obté la informació s'un contenidor juntament el seu contingut donat el path.
	 * 
	 * @param entitatId
	 *            Atribut id de l'entitat a la qual pertany el contenidor.
	 * @param path
	 *            Path del contenidor dins l'entitat.
	 * @return El contenidor i el seu contingut.
	 * @throws NotFoundException
	 *             Si no s'ha trobat l'objecte amb l'id especificat.
	 */
	@PreAuthorize("hasRole('tothom')")
	public ContenidorDto getContenidorAmbContingutPerPath(
			Long entitatId,
			String path) throws NotFoundException;

	/**
	 * Obté els errors de validació d'un contenidor.
	 * 
	 * @param entitatId
	 *            Atribut id de l'entitat a la qual pertany el contenidor.
	 * @param contenidorId
	 *            Atribut id del contenidor del qual es vol consultar el contingut.
	 * @return Els errors de validació del contenidor.
	 * @throws NotFoundException
	 *             Si no s'ha trobat l'objecte amb l'id especificat.
	 */
	@PreAuthorize("hasRole('tothom')")
	public List<ValidacioErrorDto> findErrorsValidacio(
			Long entitatId,
			Long contenidorId) throws NotFoundException;

	/**
	 * Obté el registre d'accions realitzades damunt un contenidor.
	 * 
	 * @param entitatId
	 *            Atribut id de l'entitat a la qual pertany el contenidor.
	 * @param contenidorId
	 *            Atribut id del contenidor del qual es vol consultar el registre.
	 * @return La llista d'accions realitzades damunt el contenidor.
	 * @throws NotFoundException
	 *             Si no s'ha trobat l'objecte amb l'id especificat.
	 */
	@PreAuthorize("hasRole('IPA_ADMIN')")
	public List<ContenidorLogDto> findLogsPerContenidorAdmin(
			Long entitatId,
			Long contenidorId) throws NotFoundException;

	/**
	 * Obté el registre d'accions realitzades damunt un contenidor.
	 * 
	 * @param entitatId
	 *            Atribut id de l'entitat a la qual pertany el contenidor.
	 * @param contenidorId
	 *            Atribut id del contenidor del qual es vol consultar el registre.
	 * @return La llista d'accions realitzades damunt el contenidor.
	 * @throws NotFoundException
	 *             Si no s'ha trobat l'objecte amb l'id especificat.
	 */
	@PreAuthorize("hasRole('tothom')")
	public List<ContenidorLogDto> findLogsPerContenidorUser(
			Long entitatId,
			Long contenidorId) throws NotFoundException;

	/**
	 * Obté el registre d'accions realitzades damunt un contenidor.
	 * 
	 * @param entitatId
	 *            Atribut id de l'entitat a la qual pertany el contenidor.
	 * @param contenidorId
	 *            Atribut id del contenidor del qual es vol consultar el registre.
	 * @return La llista de moviments realitzats damunt el contenidor.
	 * @throws NotFoundException
	 *             Si no s'ha trobat l'objecte amb l'id especificat.
	 */
	@PreAuthorize("hasRole('tothom')")
	public List<ContenidorMovimentDto> findMovimentsPerContenidorAdmin(
			Long entitatId,
			Long contenidorId) throws NotFoundException;

	/**
	 * Obté el registre d'accions realitzades damunt un contenidor.
	 * 
	 * @param entitatId
	 *            Atribut id de l'entitat a la qual pertany el contenidor.
	 * @param contenidorId
	 *            Atribut id del contenidor del qual es vol consultar el registre.
	 * @return La llista de moviments realitzats damunt el contenidor.
	 * @throws NotFoundException
	 *             Si no s'ha trobat l'objecte amb l'id especificat.
	 */
	@PreAuthorize("hasRole('tothom')")
	public List<ContenidorMovimentDto> findMovimentsPerContenidorUser(
			Long entitatId,
			Long contenidorId) throws NotFoundException;

	/**
	 * Obté una llista dels contenidors esborrats permetent especificar dades
	 * per al seu filtratge.
	 * 
	 * @param entitatId
	 *            Atribut id de l'entitat.
	 * @param filtre
	 *            El filtre de la consulta.
	 * @param paginacioParams
	 *            Paràmetres per a dur a terme la paginació del resultats.
	 * @return Una pàgina amb els contenidors trobats.
	 * @throws NotFoundException
	 *             Si no s'ha trobat l'objecte amb l'id especificat.
	 */
	@PreAuthorize("hasRole('IPA_ADMIN')")
	public PaginaDto<ContenidorDto> findAdmin(
			Long entitatId,
			ContenidorFiltreDto filtre,
			PaginacioParamsDto paginacioParams) throws NotFoundException;

	/**
	 * Obté una llista dels contenidors esborrats permetent especificar dades
	 * per al seu filtratge.
	 * 
	 * @param entitatId
	 *            Atribut id de l'entitat.
	 * @param nom
	 *            Nom per a filtrar la llista d'elements esborrats.
	 * @param usuariCodi
	 *            Usuari per a filtrar la llista d'elements esborrats.
	 * @param dataInici
	 *            Data d'inici per a filtrar la llista d'elements esborrats.
	 * @param dataFi
	 *            Data de fi per a filtrar la llista d'elements esborrats.
	 * @param paginacioParams
	 *            Paràmetres per a dur a terme la paginació del resultats.
	 * @return Una pàgina amb els contenidors trobats.
	 * @throws NotFoundException
	 *             Si no s'ha trobat l'objecte amb l'id especificat.
	 */
	@PreAuthorize("hasRole('IPA_ADMIN')")
	public PaginaDto<ContenidorDto> findEsborrats(
			Long entitatId,
			String nom,
			String usuariCodi,
			Date dataInici,
			Date dataFi,
			PaginacioParamsDto paginacioParams) throws NotFoundException;

}
