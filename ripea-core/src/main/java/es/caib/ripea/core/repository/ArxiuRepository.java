/**
 * 
 */
package es.caib.ripea.core.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import es.caib.ripea.core.entity.ArxiuEntity;
import es.caib.ripea.core.entity.EntitatEntity;

/**
 * Definició dels mètodes necessaris per a gestionar una entitat de base
 * de dades del tipus arxiu.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public interface ArxiuRepository extends JpaRepository<ArxiuEntity, Long> {

	List<ArxiuEntity> findByEntitatAndPareNotNull(EntitatEntity entitat);

	List<ArxiuEntity> findByEntitatAndUnitatCodiAndPareNotNull(
			EntitatEntity entitat,
			String unitatCodi);

	ArxiuEntity findByEntitatAndUnitatCodiAndPareNull(
			EntitatEntity entitat,
			String unitatCodi);

}
