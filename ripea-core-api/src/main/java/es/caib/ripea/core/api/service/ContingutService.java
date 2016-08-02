/**
 * 
 */
package es.caib.ripea.core.api.service;

import java.util.Date;
import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;

import es.caib.ripea.core.api.dto.ContingutFiltreDto;
import es.caib.ripea.core.api.dto.ContingutDto;
import es.caib.ripea.core.api.dto.ContingutLogDto;
import es.caib.ripea.core.api.dto.ContingutMovimentDto;
import es.caib.ripea.core.api.dto.DadaDto;
import es.caib.ripea.core.api.dto.EscriptoriDto;
import es.caib.ripea.core.api.dto.PaginaDto;
import es.caib.ripea.core.api.dto.PaginacioParamsDto;
import es.caib.ripea.core.api.dto.ValidacioErrorDto;
import es.caib.ripea.core.api.exception.NotFoundException;
import es.caib.ripea.core.api.exception.ValidationException;

/**
 * Declaració dels mètodes per a gestionar continguts.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public interface ContingutService {

	/**
	 * Canvia el nom del contingut.
	 * 
	 * @param entitatId
	 *            Atribut id de l'entitat a la qual pertany el contingut.
	 * @param contingutId
	 *            Atribut id del contingut del qual es vol consultar el contingut.
	 * @param nom
	 *            El nom que es vol posar al contingut.
	 * @return El contingut modificat.
	 * @throws NotFoundException
	 *             Si no s'ha trobat l'objecte amb l'id especificat.
	 */
	@PreAuthorize("hasRole('tothom')")
	public ContingutDto rename(
			Long entitatId,
			Long contingutId,
			String nom) throws NotFoundException;

	/**
	 * Crea una nova dada a dins el node.
	 * 
	 * @param entitatId
	 *            Atribut id de l'entitat a la qual pertany el contingut.
	 * @param contingutId
	 *            Atribut id del contingut del qual es vol consultar el contingut.
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
			Long contingutId,
			Long metaDadaId,
			Object valor) throws NotFoundException, ValidationException;

	/**
	 * Modifica una dada a dins el node.
	 * 
	 * @param entitatId
	 *            Atribut id de l'entitat a la qual pertany el contingut.
	 * @param contingutId
	 *            Atribut id del contingut del qual es vol consultar el contingut.
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
			Long contingutId,
			Long dadaId,
			Object valor) throws NotFoundException;

	/**
	 * Esborra una dada a dins el node.
	 * 
	 * @param entitatId
	 *            Atribut id de l'entitat a la qual pertany el contingut.
	 * @param contingutId
	 *            Atribut id del contingut del qual es vol consultar el contingut.
	 * @param dadaId
	 *            Atribut id de la meta-dada a partir del qual es vol crear aquesta dada.
	 * @return La dada esborrada.
	 * @throws NotFoundException
	 *             Si no s'ha trobat l'objecte amb l'id especificat.
	 */
	@PreAuthorize("hasRole('tothom')")
	public DadaDto dadaDelete(
			Long entitatId,
			Long contingutId,
			Long dadaId) throws NotFoundException;

	/**
	 * Obté una dada de dins el node.
	 * 
	 * @param entitatId
	 *            Atribut id de l'entitat a la qual pertany el contingut.
	 * @param contingutId
	 *            Atribut id del contingut del qual es vol consultar el contingut.
	 * @param dadaId
	 *            Atribut id de la meta-dada a partir del qual es vol crear aquesta dada.
	 * @return La dada esborrada.
	 * @throws NotFoundException
	 *             Si no s'ha trobat l'objecte amb l'id especificat.
	 */
	@PreAuthorize("hasRole('tothom')")
	public DadaDto dadaFindById(
			Long entitatId,
			Long contingutId,
			Long dadaId) throws NotFoundException;

	/**
	 * Marca un contingut com a esborrat. Posteriorment un administrador
	 * el podria recuperar.
	 * 
	 * @param entitatId
	 *            Atribut id de l'entitat a la qual pertany el contingut.
	 * @param contingutId
	 *            Atribut id del contingut que es vol esborrar.
	 * @param nomesMarcarEsborrat
	 *            Posar a true si es vol esborrar el contingut definitivament
	 *            o false si només es vol marcar com a esborrat.
	 * @return El contingut esborrat.
	 * @throws NotFoundException
	 *             Si no s'ha trobat l'objecte amb l'id especificat.
	 */
	@PreAuthorize("hasRole('tothom')")
	public ContingutDto deleteReversible(
			Long entitatId,
			Long contingutId) throws NotFoundException;

	/**
	 * Esborra un contingut sense possibilitat de recuperar-lo.
	 * 
	 * @param entitatId
	 *            Atribut id de l'entitat a la qual pertany el contingut.
	 * @param contingutId
	 *            Atribut id del contingut que es vol esborrar.
	 * @return El contingut esborrat.
	 * @throws NotFoundException
	 *             Si no s'ha trobat l'objecte amb l'id especificat.
	 */
	@PreAuthorize("hasRole('IPA_ADMIN')")
	public ContingutDto deleteDefinitiu(
			Long entitatId,
			Long contingutId) throws NotFoundException;

	/**
	 * Recupera un contingut marcat com a esborrat.
	 * 
	 * @param entitatId
	 *            Atribut id de l'entitat a la qual pertany el contingut.
	 * @param contingutId
	 *            Atribut id del contingut que es vol esborrar.
	 * @return El contingut recuperat.
	 * @throws NotFoundException
	 *             Si no s'ha trobat l'objecte amb l'id especificat.
	 * @throws ValidationException
	 *             Si ja existeix un altre contingut amb el mateix nom
	 *             a dins el mateix pare.
	 */
	@PreAuthorize("hasRole('IPA_ADMIN')")
	public ContingutDto undelete(
			Long entitatId,
			Long contingutId) throws NotFoundException, ValidationException;

	/**
	 * Mou un contingut al destí especificat.
	 * 
	 * @param entitatId
	 *            Atribut id de l'entitat a la qual pertany el contingut.
	 * @param contingutOrigenId
	 *            Atribut id del contingut que es vol moure.
	 * @param contingutDestiId
	 *            Atribut id del contingut a on es vol moure l'origen.
	 * @return El contingut mogut.
	 * @throws NotFoundException
	 *             Si no s'ha trobat l'objecte amb l'id especificat.
	 * @throws ValidationException
	 *             Si ja existeix un altre contingut amb el mateix nom
	 *             a dins el destí.
	 */
	@PreAuthorize("hasRole('tothom')")
	public ContingutDto move(
			Long entitatId,
			Long contingutOrigenId,
			Long contingutDestiId) throws NotFoundException, ValidationException;

	/**
	 * Copia un contingut al destí especificat.
	 * 
	 * @param entitatId
	 *            Atribut id de l'entitat a la qual pertany el contingut.
	 * @param contingutOrigenId
	 *            Atribut id del contingut que es vol copiar.
	 * @param contingutDestiId
	 *            Atribut id del contingut a on es vol posar la còpia.
	 * @param recursiu
	 *            Amb el valor 'true' indica que .
	 * @return El contingut creat amb la còpia.
	 * @throws NotFoundException
	 *             Si no s'ha trobat l'objecte amb l'id especificat.
	 * @throws ValidationException
	 *             Si ja existeix un altre contingut amb el mateix nom
	 *             a dins el destí.
	 */
	@PreAuthorize("hasRole('tothom')")
	public ContingutDto copy(
			Long entitatId,
			Long contingutOrigenId,
			Long contingutDestiId,
			boolean recursiu) throws NotFoundException, ValidationException;

	/**
	 * Reb un contingut situat a la bústia especificada.
	 * 
	 * @param entitatId
	 *            Atribut id de l'entitat a la qual pertany el contingut.
	 * @param bustiaId
	 *            Atribut id de la bústia destinatària.
	 * @param contingutOrigenId
	 *            Atribut id del contingut que es vol rebre.
	 * @param contingutDestiId
	 *            Atribut id del contingut a on es vol situar el contingut rebut.
	 * @return El contingut rebut.
	 * @throws NotFoundException
	 *             Si no s'ha trobat l'objecte amb l'id especificat.
	 */
	/*@PreAuthorize("hasRole('tothom')")
	public ContingutDto receivea(
			Long entitatId,
			Long bustiaId,
			Long contingutOrigenId,
			Long contingutDestiId) throws NotFoundException;*/

	/**
	 * Obté el contingut de l'escriptori de l'usuari actual.
	 * 
	 * @param entitatId
	 *            Atribut id de l'entitat a la qual pertany el contingut.
	 * @return l'objecte que representa l'escriptori.
	 * @throws NotFoundException
	 *             Si no s'ha trobat l'objecte amb l'id especificat.
	 */
	@PreAuthorize("hasRole('tothom')")
	public EscriptoriDto getEscriptoriPerUsuariActual(
			Long entitatId) throws NotFoundException;

	/**
	 * Obté la informació del contingut especificat sense el seu contingut.
	 * 
	 * @param entitatId
	 *            Atribut id de l'entitat a la qual pertany el contingut.
	 * @param contingutId
	 *            Atribut id del contingut del qual es vol consultar el contingut.
	 * @return El contingut sense el seu contingut.
	 * @throws NotFoundException
	 *             Si no s'ha trobat l'objecte amb l'id especificat.
	 */
	@PreAuthorize("hasRole('tothom')")
	public ContingutDto getContingutSenseFills(
			Long entitatId,
			Long contingutId) throws NotFoundException;

	/**
	 * Obté la informació del contingut especificat juntament amb el seu contingut.
	 * 
	 * @param entitatId
	 *            Atribut id de l'entitat a la qual pertany el contingut.
	 * @param contingutId
	 *            Atribut id del contingut del qual es vol consultar el contingut.
	 * @return El contingut i el seu contingut.
	 * @throws NotFoundException
	 *             Si no s'ha trobat l'objecte amb l'id especificat.
	 */
	@PreAuthorize("hasRole('tothom')")
	public ContingutDto getContingutAmbFills(
			Long entitatId,
			Long contingutId) throws NotFoundException;

	/**
	 * Obté la informació s'un contingut juntament el seu contingut donat el path.
	 * 
	 * @param entitatId
	 *            Atribut id de l'entitat a la qual pertany el contingut.
	 * @param path
	 *            Path del contingut dins l'entitat.
	 * @return El contingut i el seu contingut.
	 * @throws NotFoundException
	 *             Si no s'ha trobat l'objecte amb l'id especificat.
	 */
	@PreAuthorize("hasRole('tothom')")
	public ContingutDto getContingutAmbFillsPerPath(
			Long entitatId,
			String path) throws NotFoundException;

	/**
	 * Obté els errors de validació d'un contingut.
	 * 
	 * @param entitatId
	 *            Atribut id de l'entitat a la qual pertany el contingut.
	 * @param contingutId
	 *            Atribut id del contingut del qual es vol consultar el contingut.
	 * @return Els errors de validació del contingut.
	 * @throws NotFoundException
	 *             Si no s'ha trobat l'objecte amb l'id especificat.
	 */
	@PreAuthorize("hasRole('tothom')")
	public List<ValidacioErrorDto> findErrorsValidacio(
			Long entitatId,
			Long contingutId) throws NotFoundException;

	/**
	 * Obté el registre d'accions realitzades damunt un contingut.
	 * 
	 * @param entitatId
	 *            Atribut id de l'entitat a la qual pertany el contingut.
	 * @param contingutId
	 *            Atribut id del contingut del qual es vol consultar el registre.
	 * @return La llista d'accions realitzades damunt el contingut.
	 * @throws NotFoundException
	 *             Si no s'ha trobat l'objecte amb l'id especificat.
	 */
	@PreAuthorize("hasRole('IPA_ADMIN')")
	public List<ContingutLogDto> findLogsPerContingutAdmin(
			Long entitatId,
			Long contingutId) throws NotFoundException;

	/**
	 * Obté el registre d'accions realitzades damunt un contingut.
	 * 
	 * @param entitatId
	 *            Atribut id de l'entitat a la qual pertany el contingut.
	 * @param contingutId
	 *            Atribut id del contingut del qual es vol consultar el registre.
	 * @return La llista d'accions realitzades damunt el contingut.
	 * @throws NotFoundException
	 *             Si no s'ha trobat l'objecte amb l'id especificat.
	 */
	@PreAuthorize("hasRole('tothom')")
	public List<ContingutLogDto> findLogsPerContingutUser(
			Long entitatId,
			Long contingutId) throws NotFoundException;

	/**
	 * Obté el registre d'accions realitzades damunt un contingut.
	 * 
	 * @param entitatId
	 *            Atribut id de l'entitat a la qual pertany el contingut.
	 * @param contingutId
	 *            Atribut id del contingut del qual es vol consultar el registre.
	 * @return La llista de moviments realitzats damunt el contingut.
	 * @throws NotFoundException
	 *             Si no s'ha trobat l'objecte amb l'id especificat.
	 */
	@PreAuthorize("hasRole('tothom')")
	public List<ContingutMovimentDto> findMovimentsPerContingutAdmin(
			Long entitatId,
			Long contingutId) throws NotFoundException;

	/**
	 * Obté el registre d'accions realitzades damunt un contingut.
	 * 
	 * @param entitatId
	 *            Atribut id de l'entitat a la qual pertany el contingut.
	 * @param contingutId
	 *            Atribut id del contingut del qual es vol consultar el registre.
	 * @return La llista de moviments realitzats damunt el contingut.
	 * @throws NotFoundException
	 *             Si no s'ha trobat l'objecte amb l'id especificat.
	 */
	@PreAuthorize("hasRole('tothom')")
	public List<ContingutMovimentDto> findMovimentsPerContingutUser(
			Long entitatId,
			Long contingutId) throws NotFoundException;

	/**
	 * Obté una llista dels continguts esborrats permetent especificar dades
	 * per al seu filtratge.
	 * 
	 * @param entitatId
	 *            Atribut id de l'entitat.
	 * @param filtre
	 *            El filtre de la consulta.
	 * @param paginacioParams
	 *            Paràmetres per a dur a terme la paginació del resultats.
	 * @return Una pàgina amb els continguts trobats.
	 * @throws NotFoundException
	 *             Si no s'ha trobat l'objecte amb l'id especificat.
	 */
	@PreAuthorize("hasRole('IPA_ADMIN')")
	public PaginaDto<ContingutDto> findAdmin(
			Long entitatId,
			ContingutFiltreDto filtre,
			PaginacioParamsDto paginacioParams) throws NotFoundException;

	/**
	 * Obté una llista dels continguts esborrats permetent especificar dades
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
	 * @return Una pàgina amb els continguts trobats.
	 * @throws NotFoundException
	 *             Si no s'ha trobat l'objecte amb l'id especificat.
	 */
	@PreAuthorize("hasRole('IPA_ADMIN')")
	public PaginaDto<ContingutDto> findEsborrats(
			Long entitatId,
			String nom,
			String usuariCodi,
			Date dataInici,
			Date dataFi,
			PaginacioParamsDto paginacioParams) throws NotFoundException;

}