/**
 * 
 */
package es.caib.ripea.core.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import es.caib.ripea.core.entity.ArxiuEntity;
import es.caib.ripea.core.entity.EntitatEntity;
import es.caib.ripea.core.entity.ExpedientEntity;
import es.caib.ripea.core.entity.MetaNodeEntity;

/**
 * Definició dels mètodes necessaris per a gestionar una entitat de base
 * de dades del tipus expedient.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public interface ExpedientRepository extends JpaRepository<ExpedientEntity, Long> {

	@Query(	"select " +
			"    arxiu.id, " +
			"    count(*) " +
			"from " +
			"    ExpedientEntity " +
			"where " +
			"    entitat = :entitat " +
			"and esborrat = 0 " +
			"group by " +
			"    arxiu")
	List<Object[]> countByArxiu(
			@Param("entitat") EntitatEntity entitat);

	@Query(	"select " +
			"    arxiu.id, " +
			"    count(*) " +
			"from " +
			"    ExpedientEntity " +
			"where " +
			"    entitat = :entitat " +
			"and esborrat = 0 " +
			"and (metaNode is null or metaNode in (:metaNodes)) " +
			"group by " +
			"    arxiu")
	List<Object[]> countPermesosByArxiu(
			@Param("entitat") EntitatEntity entitat,
			@Param("metaNodes") List<? extends MetaNodeEntity> metanodes);

	@Query(	"from" +
			"    ExpedientEntity e " +
			"where " +
			"    e.esborrat = 0 " +
			"and e.entitat = :entitat " +
			"and e.arxiu = :arxiu " +
			"and (e.metaNode is null or e.metaNode in (:metaNodesPermesos)) " +
			"and (:esNullNom = true or lower(e.nom) like lower('%'||:nom||'%')) " +
			"and (:esNullMetaNode = true or e.metaNode = :metaNode) " +
			"and (:esNullCreacioInici = true or e.createdDate >= :creacioInici) " +
			"and (:esNullCreacioFi = true or e.createdDate <= :creacioFi)")
	Page<ExpedientEntity> findByEntitatAndArxiuFiltre(
			@Param("entitat") EntitatEntity entitat,
			@Param("arxiu") ArxiuEntity arxiu,
			@Param("metaNodesPermesos") List<? extends MetaNodeEntity> metaNodesPermesos,
			@Param("esNullMetaNode") boolean esNullMetaNode,
			@Param("metaNode") MetaNodeEntity metaNode,
			@Param("esNullNom") boolean esNullNom,
			@Param("nom") String nom,
			@Param("esNullCreacioInici") boolean esNullCreacioInici,
			@Param("creacioInici") Date creacioInici,
			@Param("esNullCreacioFi") boolean esNullCreacioFi,
			@Param("creacioFi") Date creacioFi,
			Pageable pageable);

}
