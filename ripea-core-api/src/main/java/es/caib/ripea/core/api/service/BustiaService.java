/**
 * 
 */
package es.caib.ripea.core.api.service;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;

import es.caib.ripea.core.api.dto.ArbreDto;
import es.caib.ripea.core.api.dto.BustiaContingutPendentDto;
import es.caib.ripea.core.api.dto.BustiaContingutPendentTipusEnumDto;
import es.caib.ripea.core.api.dto.BustiaDto;
import es.caib.ripea.core.api.dto.ContenidorDto;
import es.caib.ripea.core.api.dto.PermisDto;
import es.caib.ripea.core.api.dto.UnitatOrganitzativaDto;
import es.caib.ripea.core.api.exception.NotFoundException;
import es.caib.ripea.core.api.registre.RegistreAnotacio;
import es.caib.ripea.core.api.registre.RegistreTipusEnum;

/**
 * Declaració dels mètodes per a gestionar bústies.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public interface BustiaService {

	/**
	 * Crea una nova bústia.
	 * 
	 * @param entitatId
	 *            Id de l'entitat.
	 * @param bustia
	 *            Informació de la bústia a crear.
	 * @return La bústia creada.
	 * @throws NotFoundException
	 *             Si no s'ha trobat l'objecte amb l'id especificat.
	 */
	@PreAuthorize("hasRole('IPA_ADMIN')")
	public BustiaDto create(
			Long entitatId,
			BustiaDto bustia) throws NotFoundException;

	/**
	 * Actualitza la informació d'una bústia.
	 * 
	 * @param entitatId
	 *            Id de l'entitat.
	 * @param bustia
	 *            Informació de la bústia a modificar.
	 * @return La bústia modificada.
	 * @throws NotFoundException
	 *             Si no s'ha trobat l'objecte amb l'id especificat.
	 */
	@PreAuthorize("hasRole('IPA_ADMIN')")
	public BustiaDto update(
			Long entitatId,
			BustiaDto bustia) throws NotFoundException;

	/**
	 * Activa o desactiva una bústia.
	 * 
	 * @param entitatId
	 *            Id de l'entitat.
	 * @param id
	 *            Id de la bústia a modificar.
	 * @param activa
	 *            Indica si activar o desactivar la bústia.
	 * @return La bústia modificada.
	 * @throws NotFoundException
	 *             Si no s'ha trobat l'objecte amb l'id especificat.
	 */
	@PreAuthorize("hasRole('IPA_ADMIN')")
	public BustiaDto updateActiva(
			Long entitatId,
			Long id,
			boolean activa) throws NotFoundException;

	/**
	 * Esborra la bústia amb l'id especificat.
	 * 
	 * @param entitatId
	 *            Id de l'entitat.
	 * @param id
	 *            Atribut id de la bústia a esborrar.
	 * @return La bústia esborrada.
	 * @throws NotFoundException
	 *             Si no s'ha trobat l'objecte amb l'id especificat.
	 */
	@PreAuthorize("hasRole('IPA_ADMIN')")
	public BustiaDto delete(
			Long entitatId,
			Long id) throws NotFoundException;

	/**
	 * Marca la bústia com a bústia per defecte dins la seva unitat.
	 * 
	 * @param entitatId
	 *            Id de l'entitat.
	 * @param id
	 *            Atribut id de la bústia a esborrar.
	 * @throws NotFoundException
	 *             Si no s'ha trobat l'objecte amb l'id especificat.
	 */
	@PreAuthorize("hasRole('IPA_ADMIN')")
	public BustiaDto marcarPerDefecte(
			Long entitatId,
			Long id) throws NotFoundException;

	/**
	 * Consulta una bústia donat el seu id.
	 * 
	 * @param entitatId
	 *            Id de l'entitat.
	 * @param id
	 *             Atribut id de la bústia a trobar.
	 * @return La bústia amb l'id especificat o null si no s'ha trobat.
	 * @throws NotFoundException
	 *             Si no s'ha trobat l'objecte amb l'id especificat.
	 */
	@PreAuthorize("hasRole('IPA_ADMIN') or hasRole('tothom')")
	public BustiaDto findById(
			Long entitatId,
			Long id) throws NotFoundException;

	/**
	 * Consulta les bústies donat el codi de la seva unitat.
	 * 
	 * @param entitatId
	 *            Id de l'entitat.
	 * @param unitatCodi
	 *            Atribut unitatCodi de les bústies a trobar.
	 * @return Les bústies amb la unitat especificada.
	 * @throws NotFoundException
	 *             Si no s'ha trobat l'objecte amb l'id especificat.
	 */
	@PreAuthorize("hasRole('IPA_ADMIN')")
	public List<BustiaDto> findByUnitatCodiAdmin(
			Long entitatId,
			String unitatCodi) throws NotFoundException;

	/**
	 * Consulta les bústies donat el codi de la seva unitat.
	 * 
	 * @param entitatId
	 *            Id de l'entitat.
	 * @param unitatCodi
	 *            Atribut unitatCodi de les bústies a trobar.
	 * @return Les bústies amb la unitat especificada.
	 * @throws NotFoundException
	 *             Si no s'ha trobat l'objecte amb l'id especificat.
	 */
	@PreAuthorize("hasRole('tothom')")
	public List<BustiaDto> findByUnitatCodiUsuari(
			Long entitatId,
			String unitatCodi) throws NotFoundException;

	/**
	 * Retorna una llista de les bústies actives de l'entitat.
	 * 
	 * @param entitatId
	 *            Id de l'entitat.
	 * @return La llista de bústies actives de l'entitat.
	 * @throws NotFoundException
	 *             Si no s'ha trobat l'objecte amb l'id especificat.
	 */
	@PreAuthorize("hasRole('tothom')")
	public List<BustiaDto> findByEntitatAndActivaTrue(
			Long entitatId) throws NotFoundException;

	/**
	 * Consulta les bústies d'una entitat a les quals l'usuari te accés.
	 * 
	 * @param entitatId
	 *            Id de l'entitat.
	 * @return Les bústies amb accés.
	 * @throws NotFoundException
	 *             Si no s'ha trobat l'objecte amb l'id especificat.
	 */
	@PreAuthorize("hasRole('tothom')")
	public List<BustiaDto> findPermesesPerUsuari(
			Long entitatId) throws NotFoundException;

	/**
	 * Envia un contenidor a una bústia.
	 * 
	 * @param entitatId
	 *            Id de l'entitat.
	 * @param bustiaId
	 *            Atribut id de la bústia de destí.
	 * @param contenidorId
	 *            Atribut id del contenidor que s'envia.
	 * @param comentari
	 *            Comentari per l'enviament.
	 * @return el contenidor enviat
	 * @throws NotFoundException
	 *             Si no s'ha trobat l'objecte amb l'id especificat.
	 */
	@PreAuthorize("hasRole('tothom')")
	public ContenidorDto enviarContenidor(
			Long entitatId,
			Long bustiaId,
			Long contenidorId,
			String comentari) throws NotFoundException;

	/**
	 * Envia una anotació de registre a una bústia.
	 * 
	 * @param entitatCodi
	 *            El codi de l'entitat.
	 * @param tipus
	 *            El tipus d'anotació (ENTRADA o SORTIDA).
	 * @param unitatAdministrativa
	 *            La unitat administrativa destinatària.
	 * @param anotacio
	 *            Les dades de l'anotació de registre.
	 * @return el contenidor enviat
	 * @throws NotFoundException
	 *             Si no s'ha trobat l'objecte amb l'id especificat.
	 */
	//@PreAuthorize("hasRole('IPA_BSTWS')")
	public void registreAnotacioCrear(
			String entitatCodi,
			RegistreTipusEnum tipus,
			String unitatAdministrativa,
			RegistreAnotacio anotacio) throws NotFoundException;

	/**
	 * Envia una anotació de registre a una bústia.
	 * 
	 * @param entitatCodi
	 *            El codi de l'entitat.
	 * @param referencia
	 *            La referència per a consultar l'anotació de registre.
	 * @return el contenidor enviat
	 * @throws NotFoundException
	 *             Si no s'ha trobat l'objecte amb l'id especificat.
	 */
	//@PreAuthorize("hasRole('IPA_BSTWS')")
	public void registreAnotacioCrear(
			String entitatCodi,
			String referencia) throws NotFoundException;

	/**
	 * Consulta el contingut pendent d'una bústia.
	 * 
	 * @param entitatId
	 *            Id de l'entitat.
	 * @param bustiaId
	 *            Atribut id de la bústia que es vol consultar.
	 * @return El contingut pendent.
	 * @throws NotFoundException
	 *             Si no s'ha trobat l'objecte amb l'id especificat.
	 */
	@PreAuthorize("hasRole('tothom')")
	public List<BustiaContingutPendentDto> contingutPendentFindByBustiaId(
			Long entitatId,
			Long bustiaId) throws NotFoundException;

	/**
	 * Obté la informació d'un contingut pendent d'una bústia.
	 * 
	 * @param entitatId
	 *            Id de l'entitat.
	 * @param bustiaId
	 *            Atribut id de la bústia que es vol consultar.
	 * @param contingutTipus
	 *            El tipus del contingut que es vol consultar.
	 * @param contingutId
	 *            Atribut id del contingut que es vol consultar.
	 * @return la informació del contingut pendent.
	 * @throws NotFoundException
	 *             Si no s'ha trobat l'objecte amb l'id especificat.
	 */
	@PreAuthorize("hasRole('tothom')")
	public BustiaContingutPendentDto contingutPendentFindOne(
			Long entitatId,
			Long bustiaId,
			BustiaContingutPendentTipusEnumDto contingutTipus,
			Long contingutId) throws NotFoundException;

	/**
	 * Consulta el nombre d'elements pendents (tant contenidors com registres)
	 * a dins totes les bústies accessibles per l'usuari.
	 * 
	 * @param entitatId
	 *            Id de l'entitat.
	 * @return El nombre d'elementsPendents.
	 * @throws NotFoundException
	 *             Si no s'ha trobat l'objecte amb l'id especificat.
	 */
	@PreAuthorize("hasRole('tothom')")
	public long contingutPendentBustiesAllCount(
			Long entitatId) throws NotFoundException;

	/**
	 * Reenvia un contingut pendent de la bústia.
	 * 
	 * @param entitatId
	 *            Id de l'entitat.
	 * @param bustiaOrigenId
	 *            Atribut id de la bústia d'origen.
	 * @param bustiaDestiId
	 *            Atribut id de la bústia de destí.
	 * @param contingutTipus
	 *            El tipus del contingut que es vol consultar.
	 * @param contingutId
	 *            Atribut id del contingut que es vol consultar.
	 * @param comentari
	 *            Comentari pel reenviament.
	 * @throws NotFoundException
	 *             Si no s'ha trobat l'objecte amb l'id especificat.
	 */
	@PreAuthorize("hasRole('tothom')")
	public void contingutPendentReenviar(
			Long entitatId,
			Long bustiaOrigenId,
			Long bustiaDestiId,
			BustiaContingutPendentTipusEnumDto contingutTipus,
			Long contingutId,
			String comentari) throws NotFoundException;

	/**
	 * Consulta l'arbre de les unitats organitzatives per a mostrar les
	 * bústies.
	 * 
	 * @param entitatId
	 *            Id de l'entitat.
	 * @param nomesBusties
	 *            Indica si només han d'aparèixer les únitats que contenen bústies.
	 * @param nomesBustiesPermeses
	 *            Indica si només han d'aparèixer les bústies a les que es tengui permisos d'accés.
	 * @param comptarElementsPendents
	 *            Indica si s'ha de comptar els elements pendets a cada unitat.
	 * @return L'arbre de les unitats organitzatives.
	 * @throws NotFoundException
	 *             Si no s'ha trobat l'objecte amb l'id especificat.
	 */
	@PreAuthorize("hasRole('IPA_ADMIN') or hasRole('tothom')")
	public ArbreDto<UnitatOrganitzativaDto> findArbreUnitatsOrganitzatives(
			Long entitatId,
			boolean nomesBusties,
			boolean nomesBustiesPermeses,
			boolean comptarElementsPendents) throws NotFoundException;

	/**
	 * Modifica els permisos d'un usuari o d'un rol per a accedir a una bústia.
	 * 
	 * @param entitatId
	 *            Id de l'entitat.
	 * @param id
	 *            Atribut id de la bústia a la qual es vol modificar el permís.
	 * @param permis
	 *            El permís que es vol modificar.
	 * @throws NotFoundException
	 *             Si no s'ha trobat l'objecte amb l'id especificat.
	 */
	@PreAuthorize("hasRole('IPA_ADMIN')")
	public void updatePermis(
			Long entitatId,
			Long id,
			PermisDto permis) throws NotFoundException;

	/**
	 * Esborra els permisos d'un usuari o d'un rol per a accedir a una bústia.
	 * 
	 * @param entitatId
	 *            Id de l'entitat.
	 * @param id
	 *            Atribut id de l'arxiu del qual es vol modificar el permís.
	 * @param permisId
	 *            Atribut id del permís que es vol esborrar.
	 * @throws NotFoundException
	 *             Si no s'ha trobat l'objecte amb l'id especificat.
	 */
	@PreAuthorize("hasRole('IPA_ADMIN')")
	public void deletePermis(
			Long entitatId,
			Long id,
			Long permisId) throws NotFoundException;

}