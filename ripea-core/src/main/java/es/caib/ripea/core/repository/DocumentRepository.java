/**
 * 
 */
package es.caib.ripea.core.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import es.caib.ripea.core.entity.DocumentEntity;
import es.caib.ripea.core.entity.ExpedientEntity;
import es.caib.ripea.core.entity.MetaNodeEntity;

/**
 * Definició dels mètodes necessaris per a gestionar una entitat de base
 * de dades del tipus document.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public interface DocumentRepository extends JpaRepository<DocumentEntity, Long> {

	List<DocumentEntity> findByExpedient(ExpedientEntity expedient);
	List<DocumentEntity> findByExpedientAndMetaNode(
			ExpedientEntity expedient,
			MetaNodeEntity metaNode);

}
