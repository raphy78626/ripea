/**
 * 
 */
package es.caib.ripea.core.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import es.caib.ripea.core.entity.EntitatEntity;
import es.caib.ripea.core.entity.MetaNodeEntity;

/**
 * Definició dels mètodes necessaris per a gestionar una entitat de base
 * de dades del tipus meta-node.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public interface MetaNodeRepository extends JpaRepository<MetaNodeEntity, Long> {

	List<MetaNodeEntity> findByEntitat(EntitatEntity entitat);

}
