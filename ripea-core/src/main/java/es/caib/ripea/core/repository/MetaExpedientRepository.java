/**
 * 
 */
package es.caib.ripea.core.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import es.caib.ripea.core.entity.EntitatEntity;
import es.caib.ripea.core.entity.MetaExpedientEntity;

/**
 * Definició dels mètodes necessaris per a gestionar una entitat de base
 * de dades del tipus meta-expedient.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public interface MetaExpedientRepository extends JpaRepository<MetaExpedientEntity, Long> {

	MetaExpedientEntity findByEntitatAndCodi(EntitatEntity entitat, String codi);

	List<MetaExpedientEntity> findByEntitatOrderByNomAsc(EntitatEntity entitat);
	List<MetaExpedientEntity> findByEntitat(EntitatEntity entitat, Sort sort);
	List<MetaExpedientEntity> findByEntitat(EntitatEntity entitat, Pageable pageable);
	List<MetaExpedientEntity> findByEntitatAndActiuTrueOrderByNomAsc(EntitatEntity entitat);

	/** Compta el número d'arxius per a cada meta-expedient de la entitat. */
	@Query(	"select " +
			"    id, " +
			"    size(arxius) " +
			"from " +
			"    MetaExpedientEntity " +
			"where " +
			"    entitat = :entitat " +
			"group by id ")
	List<Object[]> countArxius(
			@Param("entitat") EntitatEntity entitat);


}
