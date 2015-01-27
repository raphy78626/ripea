/**
 * 
 */
package es.caib.ripea.core.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import es.caib.ripea.core.entity.BustiaEntity;
import es.caib.ripea.core.entity.EntitatEntity;

/**
 * Definició dels mètodes necessaris per a gestionar una entitat de base
 * de dades del tipus bústia.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public interface BustiaRepository extends JpaRepository<BustiaEntity, Long> {

	List<BustiaEntity> findByEntitatAndActivaTrueAndPareNotNull(EntitatEntity entitat);

	List<BustiaEntity> findByEntitatAndPareNotNull(EntitatEntity entitat);

	List<BustiaEntity> findByEntitatAndUnitatCodiAndPareNotNull(
			EntitatEntity entitat,
			String unitatCodi);

	BustiaEntity findByEntitatAndUnitatCodiAndPareNull(
			EntitatEntity entitat,
			String unitatCodi);

	BustiaEntity findByEntitatAndUnitatCodiAndPerDefecteTrue(
			EntitatEntity entitat,
			String unitatCodi);

}
