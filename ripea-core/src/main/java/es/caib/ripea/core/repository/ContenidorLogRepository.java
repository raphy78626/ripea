/**
 * 
 */
package es.caib.ripea.core.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import es.caib.ripea.core.entity.ContenidorLogEntity;

/**
 * Definició dels mètodes necessaris per a gestionar una entitat de base
 * de dades del tipus ContenidorLog.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public interface ContenidorLogRepository extends JpaRepository<ContenidorLogEntity, Long> {

	List<ContenidorLogEntity> findByContenidorIdOrderByDataAsc(Long contenidorId);

}
