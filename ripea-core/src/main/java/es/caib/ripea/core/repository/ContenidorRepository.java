/**
 * 
 */
package es.caib.ripea.core.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import es.caib.ripea.core.entity.ContenidorEntity;
import es.caib.ripea.core.entity.EntitatEntity;
import es.caib.ripea.core.entity.UsuariEntity;

/**
 * Definició dels mètodes necessaris per a gestionar una entitat de base
 * de dades del tipus contenidor.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public interface ContenidorRepository extends JpaRepository<ContenidorEntity, Long> {

	List<ContenidorEntity> findByPareAndEsborrat(
			ContenidorEntity pare,
			int esborrat,
			Sort sort);

	List<ContenidorEntity> findByPareAndNomOrderByEsborratAsc(
			ContenidorEntity pare,
			String nom);

	ContenidorEntity findByPareAndNomAndEsborrat(
			ContenidorEntity pare,
			String nom,
			int esborrat);

	@Query(	"select " +
			"    c " +
			"from " +
			"    ContenidorEntity c " +
			"where " +
			"    c.entitat = :entitat " +
			"and (type(c) = es.caib.ripea.core.entity.ExpedientEntity or type(c) = es.caib.ripea.core.entity.DocumentEntity or type(c) = es.caib.ripea.core.entity.CarpetaEntity)" +
			"and (:tipusExpedient = true or type(c) <> es.caib.ripea.core.entity.ExpedientEntity) " +
			"and (:tipusDocument = true or type(c) <> es.caib.ripea.core.entity.DocumentEntity) " +
			"and (:tipusCarpeta = true or type(c) <> es.caib.ripea.core.entity.CarpetaEntity) " +
			"and (:esNullNom = true or lower(c.nom) like :nom) " +
			"and (:esNullDataInici = true or c.lastModifiedDate >= :dataInici) " +
			"and (:esNullDataFi = true or c.lastModifiedDate <= :dataFi) " +
			"and ((:mostrarEsborrats = true and c.esborrat > 0) or (:mostrarNoEsborrats = true and c.esborrat = 0)) ")
	public Page<ContenidorEntity> findByFiltrePaginat(
			@Param("entitat") EntitatEntity entitat,
			@Param("tipusExpedient") boolean tipusExpedient,
			@Param("tipusDocument") boolean tipusDocument,
			@Param("tipusCarpeta") boolean tipusCarpeta,
			@Param("esNullNom") boolean esNullNom,
			@Param("nom") String nom,
			@Param("esNullDataInici") boolean esNullDataInici,
			@Param("dataInici") Date dataInici,
			@Param("esNullDataFi") boolean esNullDataFi,
			@Param("dataFi") Date dataFi,
			@Param("mostrarEsborrats") boolean mostrarEsborrats,
			@Param("mostrarNoEsborrats") boolean mostrarNoEsborrats,
			Pageable pageable);

	@Query(	"select " +
			"    c " +
			"from " +
			"    ContenidorEntity c " +
			"where " +
			"    c.entitat = :entitat " +
			"and (:esNullNom = true or lower(c.nom) like :nom) " +
			"and (:esNullUsuari = true or c.lastModifiedBy = :usuari) " +
			"and (:esNullDataInici = true or c.lastModifiedDate >= :dataInici) " +
			"and (:esNullDataFi = true or c.lastModifiedDate <= :dataFi) " +
			"and esborrat > 0")
	public Page<ContenidorEntity> findEsborratsByFiltrePaginat(
			@Param("entitat") EntitatEntity entitat,
			@Param("esNullNom") boolean esNullNom,
			@Param("nom") String nom,
			@Param("esNullUsuari") boolean esNullUsuari,
			@Param("usuari") UsuariEntity usuari,
			@Param("esNullDataInici") boolean esNullDataInici,
			@Param("dataInici") Date dataInici,
			@Param("esNullDataFi") boolean esNullDataFi,
			@Param("dataFi") Date dataFi,
			Pageable pageable);

	@Query(	"select " +
			"    pare.id, " +
			"    count(*) " +
			"from " +
			"    ContenidorEntity " +
			"where " +
			"    entitat = :entitat " +
			"and pare in (:pares) " +
			"and esborrat = 0 " +
			"group by " +
			"    pare")
	List<Object[]> countByPares(
			@Param("entitat") EntitatEntity entitat,
			@Param("pares") List<? extends ContenidorEntity> pares);

}
