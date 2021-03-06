/**
 * 
 */
package es.caib.ripea.core.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import es.caib.ripea.core.entity.DadaEntity;
import es.caib.ripea.core.entity.MetaDadaEntity;
import es.caib.ripea.core.entity.NodeEntity;

/**
 * Definició dels mètodes necessaris per a gestionar una entitat de base
 * de dades del tipus dada.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public interface DadaRepository extends JpaRepository<DadaEntity, Long> {

	List<DadaEntity> findByNode(NodeEntity node);
	List<DadaEntity> findByNodeAndMetaDada(NodeEntity node, MetaDadaEntity metaDada);

}
