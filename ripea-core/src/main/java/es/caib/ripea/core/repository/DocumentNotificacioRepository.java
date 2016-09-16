/**
 * 
 */
package es.caib.ripea.core.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import es.caib.ripea.core.entity.DocumentNotificacioEntity;
import es.caib.ripea.core.entity.ExpedientEntity;

/**
 * Definició dels mètodes necessaris per a gestionar una entitat de base
 * de dades del tipus documentNotificacio.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public interface DocumentNotificacioRepository extends JpaRepository<DocumentNotificacioEntity, Long> {

	List<DocumentNotificacioEntity> findByExpedientOrderByDestinatariDocumentNumAscDataEnviamentAsc(
			ExpedientEntity expedient);

}
