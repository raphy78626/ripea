/**
 * 
 */
package es.caib.ripea.core.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import es.caib.ripea.core.entity.DocumentEnviamentEntity;
import es.caib.ripea.core.entity.ExpedientEntity;

/**
 * Definició dels mètodes necessaris per a gestionar una entitat de base
 * de dades del tipus documentEnviament.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public interface DocumentEnviamentRepository extends JpaRepository<DocumentEnviamentEntity, Long> {

	List<DocumentEnviamentEntity> findByExpedientOrderByDataEnviamentAsc(
			ExpedientEntity expedient);

}
