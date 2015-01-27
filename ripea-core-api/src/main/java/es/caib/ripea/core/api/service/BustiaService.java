/**
 * 
 */
package es.caib.ripea.core.api.service;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;

import es.caib.ripea.core.api.dto.ArbreDto;
import es.caib.ripea.core.api.dto.BustiaDto;
import es.caib.ripea.core.api.dto.ContenidorDto;
import es.caib.ripea.core.api.dto.PermisDto;
import es.caib.ripea.core.api.dto.RegistreAnotacioDto;
import es.caib.ripea.core.api.dto.UnitatOrganitzativaDto;
import es.caib.ripea.core.api.exception.BustiaNotFoundException;
import es.caib.ripea.core.api.exception.ContenidorNotFoundException;
import es.caib.ripea.core.api.exception.EntitatNotFoundException;
import es.caib.ripea.core.api.exception.RegistreNotFoundException;
import es.caib.ripea.core.api.exception.UnitatNotFoundException;

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
	 * @throws EntitatNotFoundException
	 *             Si no s'ha trobat cap entitat amb l'id especificat.
	 * @throws UnitatNotFoundException
	 *             Si no s'ha trobat cap unitat amb el codi especificat.
	 */
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public BustiaDto create(
			Long entitatId,
			BustiaDto bustia) throws EntitatNotFoundException, UnitatNotFoundException;

	/**
	 * Actualitza la informació d'una bústia.
	 * 
	 * @param entitatId
	 *            Id de l'entitat.
	 * @param bustia
	 *            Informació de la bústia a modificar.
	 * @return La bústia modificada.
	 * @throws EntitatNotFoundException
	 *             Si no s'ha trobat cap entitat amb l'id especificat.
	 * @throws BustiaNotFoundException
	 *             Si no s'ha trobat cap bústia amb l'id especificat.
	 */
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public BustiaDto update(
			Long entitatId,
			BustiaDto bustia) throws EntitatNotFoundException, BustiaNotFoundException;

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
	 * @throws EntitatNotFoundException
	 *             Si no s'ha trobat cap entitat amb l'id especificat.
	 * @throws BustiaNotFoundException
	 *             Si no s'ha trobat cap bústia amb l'id especificat.
	 */
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public BustiaDto updateActiva(
			Long entitatId,
			Long id,
			boolean activa) throws EntitatNotFoundException, BustiaNotFoundException;

	/**
	 * Esborra la bústia amb l'id especificat.
	 * 
	 * @param entitatId
	 *            Id de l'entitat.
	 * @param id
	 *            Atribut id de la bústia a esborrar.
	 * @return La bústia esborrada.
	 * @throws EntitatNotFoundException
	 *             Si no s'ha trobat cap entitat amb l'id especificat.
	 * @throws BustiaNotFoundException
	 *             Si no s'ha trobat cap bústia amb l'id especificat.
	 */
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public BustiaDto delete(
			Long entitatId,
			Long id) throws EntitatNotFoundException, BustiaNotFoundException;

	/**
	 * Marca la bústia com a bústia per defecte dins la seva unitat.
	 * 
	 * @param entitatId
	 *            Id de l'entitat.
	 * @param id
	 *            Atribut id de la bústia a esborrar.
	 * @throws EntitatNotFoundException
	 *             Si no s'ha trobat cap entitat amb l'id especificat.
	 * @throws BustiaNotFoundException
	 *             Si no s'ha trobat cap bústia amb l'id especificat.
	 */
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public BustiaDto marcarPerDefecte(
			Long entitatId,
			Long id) throws EntitatNotFoundException, BustiaNotFoundException;

	/**
	 * Consulta una bústia donat el seu id.
	 * 
	 * @param entitatId
	 *            Id de l'entitat.
	 * @param id
	 *             Atribut id de la bústia a trobar.
	 * @return La bústia amb l'id especificat o null si no s'ha trobat.
	 * @throws EntitatNotFoundException
	 *             Si no s'ha trobat cap entitat amb l'id especificat.
	 */
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
	public BustiaDto findById(
			Long entitatId,
			Long id) throws EntitatNotFoundException;

	/**
	 * Consulta les bústies donat el codi de la seva unitat.
	 * 
	 * @param entitatId
	 *            Id de l'entitat.
	 * @param unitatCodi
	 *            Atribut unitatCodi de les bústies a trobar.
	 * @return Les bústies amb la unitat especificada.
	 * @throws EntitatNotFoundException
	 *             Si no s'ha trobat cap entitat amb l'id especificat.
	 * @throws UnitatNotFoundException
	 *             Si no s'ha trobat cap unitat amb el codi especificat.
	 */
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public List<BustiaDto> findByUnitatCodiAdmin(
			Long entitatId,
			String unitatCodi) throws EntitatNotFoundException, UnitatNotFoundException;

	/**
	 * Consulta les bústies donat el codi de la seva unitat.
	 * 
	 * @param entitatId
	 *            Id de l'entitat.
	 * @param unitatCodi
	 *            Atribut unitatCodi de les bústies a trobar.
	 * @return Les bústies amb la unitat especificada.
	 * @throws EntitatNotFoundException
	 *             Si no s'ha trobat cap entitat amb l'id especificat.
	 * @throws UnitatNotFoundException
	 *             Si no s'ha trobat cap unitat amb el codi especificat.
	 */
	@PreAuthorize("hasRole('ROLE_USER')")
	public List<BustiaDto> findByUnitatCodiUsuari(
			Long entitatId,
			String unitatCodi) throws EntitatNotFoundException, UnitatNotFoundException;

	/**
	 * Retorna una llista de les bústies actives de l'entitat.
	 * 
	 * @param entitatId
	 *            Id de l'entitat.
	 * @return La llista de bústies actives de l'entitat.
	 * @throws EntitatNotFoundException
	 *             Si no s'ha trobat cap entitat amb l'id especificat.
	 */
	@PreAuthorize("hasRole('ROLE_USER')")
	public List<BustiaDto> findByEntitatAndActivaTrue(
			Long entitatId) throws EntitatNotFoundException;

	/**
	 * Consulta les bústies d'una entitat a les quals l'usuari te accés.
	 * 
	 * @param entitatId
	 *            Id de l'entitat.
	 * @return Les bústies amb accés.
	 * @throws EntitatNotFoundException
	 *             Si no s'ha trobat cap entitat amb l'id especificat.
	 */
	@PreAuthorize("hasRole('ROLE_USER')")
	public List<BustiaDto> findPermesesPerUsuari(
			Long entitatId) throws EntitatNotFoundException;

	/**
	 * Consulta el contingut pendent d'una bústia.
	 * 
	 * @param entitatId
	 *            Id de l'entitat.
	 * @param id
	 *            Atribut id de la bústia que es vol consultar.
	 * @return El contingut pendent.
	 * @throws EntitatNotFoundException
	 *             Si no s'ha trobat cap entitat amb l'id especificat.
	 * @throws BustiaNotFoundException
	 *             Si no s'ha trobat cap bústia amb l'id especificat.
	 */
	@PreAuthorize("hasRole('ROLE_USER')")
	public List<ContenidorDto> findContingutPendent(
			Long entitatId,
			Long id) throws EntitatNotFoundException, BustiaNotFoundException;

	/**
	 * Consulta les anotacions de registre pendents d'una bústia.
	 * 
	 * @param entitatId
	 *            Id de l'entitat.
	 * @param id
	 *            Atribut id de la bústia que es vol consultar.
	 * @return Les anotacions de registre pendents.
	 * @throws EntitatNotFoundException
	 *             Si no s'ha trobat cap entitat amb l'id especificat.
	 * @throws BustiaNotFoundException
	 *             Si no s'ha trobat cap bústia amb l'id especificat.
	 */
	@PreAuthorize("hasRole('ROLE_USER')")
	public List<RegistreAnotacioDto> findRegistrePendent(
			Long entitatId,
			Long id) throws EntitatNotFoundException, BustiaNotFoundException;

	/**
	 * Consulta una anotació de registre pendent a una bústia.
	 * 
	 * @param entitatId
	 *            Id de l'entitat.
	 * @param id
	 *            Atribut id de la bústia que es vol consultar.
	 * @param registreId
	 *            Atribut id del registre que es vol obtenir.
	 * @return L'anotació de registre pendent o null si no s'ha trobada.
	 * @throws EntitatNotFoundException
	 *             Si no s'ha trobat cap entitat amb l'id especificat.
	 * @throws BustiaNotFoundException
	 *             Si no s'ha trobat cap bústia amb l'id especificat.
	 */
	@PreAuthorize("hasRole('ROLE_USER')")
	public RegistreAnotacioDto findOneRegistrePendent(
			Long entitatId,
			Long id,
			Long registreId) throws EntitatNotFoundException, BustiaNotFoundException;

	/**
	 * Consulta el nombre d'elements pendents (tant contenidors com registres)
	 * a dins totes les bústies accessibles per l'usuari.
	 * 
	 * @param entitatId
	 *            Id de l'entitat.
	 * @return El nombre d'elementsPendents.
	 * @throws EntitatNotFoundException
	 *             Si no s'ha trobat cap entitat amb l'id especificat.
	 */
	@PreAuthorize("hasRole('ROLE_USER')")
	public long countElementsPendentsBustiesAll(
			Long entitatId) throws EntitatNotFoundException;

	/**
	 * Refresca el comptador d'elements pendents (tant contenidors com
	 * registres) a dins totes les bústies accessibles per l'usuari.
	 * 
	 * @param entitatId
	 *            Id de l'entitat.
	 * @throws EntitatNotFoundException
	 *             Si no s'ha trobat cap entitat amb l'id especificat.
	 */
	@PreAuthorize("hasRole('ROLE_USER')")
	public void refreshCountElementsPendentsBustiesAll(
			Long entitatId) throws EntitatNotFoundException;

	/**
	 * Modifica els permisos d'un usuari o d'un rol per a accedir a una bústia.
	 * 
	 * @param entitatId
	 *            Id de l'entitat.
	 * @param id
	 *            Atribut id de la bústia a la qual es vol modificar el permís.
	 * @param permis
	 *            El permís que es vol modificar.
	 * @throws EntitatNotFoundException
	 *             Si no s'ha trobat cap entitat amb l'id especificat.
	 * @throws BustiaNotFoundException
	 *             Si no s'ha trobat cap bústia amb l'id especificat.
	 */
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public void updatePermis(
			Long entitatId,
			Long id,
			PermisDto permis) throws EntitatNotFoundException, BustiaNotFoundException;

	/**
	 * Esborra els permisos d'un usuari o d'un rol per a accedir a una bústia.
	 * 
	 * @param entitatId
	 *            Id de l'entitat.
	 * @param id
	 *            Atribut id de l'arxiu del qual es vol modificar el permís.
	 * @param permisId
	 *            Atribut id del permís que es vol esborrar.
	 * @throws EntitatNotFoundException
	 *             Si no s'ha trobat cap entitat amb l'id especificat.
	 * @throws BustiaNotFoundException
	 *             Si no s'ha trobat cap bústia amb l'id especificat.
	 */
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public void deletePermis(
			Long entitatId,
			Long id,
			Long permisId) throws EntitatNotFoundException, BustiaNotFoundException;

	/**
	 * Consulta l'arbre de les unitats organitzatives per a mostrar les
	 * bústies.
	 * 
	 * @param entitatId
	 *            Id de l'entitat.
	 * @param nomesBustiesPermeses
	 *            Indica si només han d'aparèixer les bústies a les que s'hi tengui permisos d'accés.
	 * @param comptarElementsPendents
	 *            Indica si s'ha de comptar els elements pendets a cada unitat.
	 * @return L'arbre de les unitats organitzatives.
	 * @throws EntitatNotFoundException
	 *             Si no s'ha trobat cap entitat amb l'id especificat.
	 */
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
	public ArbreDto<UnitatOrganitzativaDto> findArbreUnitatsOrganitzatives(
			Long entitatId,
			boolean nomesBustiesPermeses,
			boolean comptarElementsPendents) throws EntitatNotFoundException;

	/**
	 * Reenvia un contenidor a una altra bústia.
	 * 
	 * @param entitatId
	 *            Id de l'entitat.
	 * @param id
	 *            Id de la bústia orígen.
	 * @param contenidorId
	 *            Id del contenidor a reenviar.
	 * @param bustiaDestiId
	 *            Id de la bústia destí.
	 * @param comentari
	 *            Comentari per al reenviament.
	 * @throws EntitatNotFoundException
	 *             Si no s'ha trobat cap entitat amb l'id especificat.
	 * @throws BustiaNotFoundException
	 *             Si no s'ha trobat cap bústia amb l'id especificat.
	 * @throws ContenidorNotFoundException
	 *             Si no s'ha trobat cap contenidor amb l'id especificat.
	 */
	@PreAuthorize("hasRole('ROLE_USER')")
	public void forwardContenidor(
			Long entitatId,
			Long id,
			Long contenidorId,
			Long bustiaDestiId,
			String comentari) throws EntitatNotFoundException, BustiaNotFoundException, ContenidorNotFoundException;

	/**
	 * Reenvia un registre a una altra bústia.
	 * 
	 * @param entitatId
	 *            Id de l'entitat.
	 * @param id
	 *            Id de la bústia orígen.
	 * @param registreId
	 *            Id del registre a reenviar.
	 * @param bustiaDestiId
	 *            Id de la bústia destí.
	 * @param comentari
	 *            Comentari del reenviament.
	 * @throws EntitatNotFoundException
	 *             Si no s'ha trobat cap entitat amb l'id especificat.
	 * @throws BustiaNotFoundException
	 *             Si no s'ha trobat cap bústia amb l'id especificat.
	 * @throws RegistreNotFoundException
	 *             Si no s'ha trobat cap anotació de registre amb l'id especificat.
	 */
	@PreAuthorize("hasRole('ROLE_USER')")
	public void forwardRegistre(
			Long entitatId,
			Long id,
			Long registreId,
			Long bustiaDestiId,
			String comentari) throws EntitatNotFoundException, BustiaNotFoundException, RegistreNotFoundException;

}
