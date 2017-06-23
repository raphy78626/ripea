/**
 * 
 */
package es.caib.ripea.core.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import es.caib.ripea.core.entity.ArxiuEntity;
import es.caib.ripea.core.entity.EntitatEntity;

/**
 * Definició dels mètodes necessaris per a gestionar una entitat de base
 * de dades del tipus arxiu.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public interface ArxiuRepository extends JpaRepository<ArxiuEntity, Long> {

	List<ArxiuEntity> findByEntitatAndActiuTrue(EntitatEntity entitat);
	List<ArxiuEntity> findByEntitatAndPareNotNull(EntitatEntity entitat);
	List<ArxiuEntity> findByEntitatAndUnitatCodiAndNom(
			EntitatEntity entitat,
			String unitatCodi,
			String arxiuNom);
	
	List<ArxiuEntity> findByEntitatAndUnitatCodiAndPareNotNull(
			EntitatEntity entitat,
			String unitatCodi);

	ArxiuEntity findByEntitatAndUnitatCodiAndPareNull(
			EntitatEntity entitat,
			String unitatCodi);
	
	/** Compta el número de meta expedients per a cada arxiu de la entitat. */
	@Query(	"select " +
			"    id, " +
			"    size(metaExpedients) " +
			"from " +
			"    ArxiuEntity " +
			"where " +
			"    entitat = :entitat " +
			"group by id ")
	List<Object[]> countMetaExpedients(
			@Param("entitat") EntitatEntity entitat);

}
