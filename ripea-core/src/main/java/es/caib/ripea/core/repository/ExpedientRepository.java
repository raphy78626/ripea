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

import es.caib.ripea.core.api.dto.ExpedientEstatEnumDto;
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
			"and (:esNullArxiu = true or e.arxiu = :arxiu) " +
			"and (:esNullArxiusPermesos = true or e.arxiu in (:arxiusPermesos)) " +
			"and (e.metaNode is null or e.metaNode in (:metaNodesPermesos)) " +
			"and (:esNullNumero = true or lower(e.sequencia||'/'||e.any) like lower('%'||:numero||'%')) " +
			"and (:esNullNom = true or lower(e.nom) like lower('%'||:nom||'%')) " +
			"and (:esNullMetaNode = true or e.metaNode = :metaNode) " +
			"and (:esNullCreacioInici = true or e.createdDate >= :creacioInici) " +
			"and (:esNullCreacioFi = true or e.createdDate <= :creacioFi) " +
			"and (:esNullTancatInici = true or e.createdDate >= :tancatInici) " +
			"and (:esNullTancatFi = true or e.createdDate <= :tancatFi) " +
			"and (:esNullEstat = true or e.estat = :estat)")
	Page<ExpedientEntity> findByEntitatAndArxiuFiltre(
			@Param("entitat") EntitatEntity entitat,
			@Param("esNullArxiu") boolean esNullArxiu,
			@Param("arxiu") ArxiuEntity arxiu,
			@Param("esNullArxiusPermesos") boolean esNullArxiusPermesos,
			@Param("arxiusPermesos") List<ArxiuEntity> arxiusPermesos,
			@Param("metaNodesPermesos") List<? extends MetaNodeEntity> metaNodesPermesos,
			@Param("esNullMetaNode") boolean esNullMetaNode,
			@Param("metaNode") MetaNodeEntity metaNode,			
			@Param("esNullNumero") boolean esNullNumero,
			@Param("numero") String numero,
			@Param("esNullNom") boolean esNullNom,
			@Param("nom") String nom,
			@Param("esNullCreacioInici") boolean esNullCreacioInici,
			@Param("creacioInici") Date creacioInici,
			@Param("esNullCreacioFi") boolean esNullCreacioFi,
			@Param("creacioFi") Date creacioFi,
			@Param("esNullTancatInici") boolean esNullTancatInici,
			@Param("tancatInici") Date tancatInici,
			@Param("esNullTancatFi") boolean esNullTancatFi,
			@Param("tancatFi") Date tancatFi,
			@Param("esNullEstat") boolean esNullEstat,
			@Param("estat") ExpedientEstatEnumDto estat,
			Pageable pageable);

}
