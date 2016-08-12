/**
 * 
 */
package es.caib.ripea.core.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import es.caib.ripea.core.entity.EntitatEntity;
import es.caib.ripea.core.entity.ExpedientEntity;
import es.caib.ripea.core.entity.InteressatAdministracioEntity;
import es.caib.ripea.core.entity.InteressatEntity;
import es.caib.ripea.core.entity.InteressatPersonaFisicaEntity;
import es.caib.ripea.core.entity.InteressatPersonaJuridicaEntity;

/**
 * Definició dels mètodes necessaris per a gestionar una entitat de base
 * de dades del tipus interessat.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public interface InteressatRepository extends JpaRepository<InteressatEntity, Long> {

	@Query(	  "select "
			+ "    inter "
			+ "from "
			+ "    InteressatEntity inter "
			+ "where "
			+ "    inter.entitat = :entitat "
			+ "and inter.expedient = :expedient "
			+ "order by "
			+ "    inter.identificador desc")
	List<InteressatEntity> findByEntitatAndExpedient(
			@Param("entitat") EntitatEntity entitat,
			@Param("expedient") ExpedientEntity expedient);

	@Query(	  "select "
			+ "    distinct inter "
			+ "from "
			+ "    InteressatPersonaFisicaEntity inter "
			+ "where "
			+ "    inter.entitat = :entitat "
			+ "and (:esNullNom = true or inter.nom = :nom) "
			+ "and (:esNullDocumentNum = true or inter.documentNum = :documentNum) "
			+ "and (:esNullLlinatge1 = true or inter.llinatge1 = :llinatge1) "
			+ "and (:esNullLlinatge2 = true or inter.llinatge2 = :llinatge2) "
			+ "order by "
			+ "    inter.identificador desc")
	List<InteressatPersonaFisicaEntity> findByFiltrePersonaFisica(
			@Param("entitat") EntitatEntity entitat,
			@Param("esNullNom") boolean esNullNom,
			@Param("nom") String nom,
			@Param("esNullDocumentNum") boolean esNullDocumentNum,
			@Param("documentNum") String documentNum,
			@Param("esNullLlinatge1") boolean esNullLlinatge1,
			@Param("llinatge1") String llinatge1,
			@Param("esNullLlinatge2") boolean esNullLlinatge2,
			@Param("llinatge2") String llinatge2);

	@Query(	  "select "
			+ "    distinct inter "
			+ "from "
			+ "    InteressatPersonaJuridicaEntity inter "
			+ "where "
			+ "    inter.entitat = :entitat "
			+ "and (:esNullDocumentNum = true or inter.documentNum = :documentNum) "
			+ "and (:esNullRaoSocial = true or inter.raoSocial = :raoSocial) "
			+ "order by "
			+ "    inter.identificador desc")
	List<InteressatPersonaJuridicaEntity> findByFiltrePersonaJuridica(
			@Param("entitat") EntitatEntity entitat,
			@Param("esNullDocumentNum") boolean esNullDocumentNum,
			@Param("documentNum") String documentNum,
			@Param("esNullRaoSocial") boolean esNullRaoSocial,
			@Param("raoSocial") String raoSocial);
	
	@Query(	  "select "
			+ "    distinct inter "
			+ "from "
			+ "    InteressatAdministracioEntity inter "
			+ "where "
			+ "    inter.entitat = :entitat "
			+ "and (:esNullOrganCodi = true or inter.organCodi = :organCodi) "
			+ "order by "
			+ "    inter.identificador desc")
	List<InteressatAdministracioEntity> findByFiltreAdministracio(
			@Param("entitat") EntitatEntity entitat,
			@Param("esNullOrganCodi") boolean esNullOrganCodi,
			@Param("organCodi") String organCodi);
	
}
