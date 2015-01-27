/**
 * 
 */
package es.caib.ripea.core.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import es.caib.ripea.core.entity.ContenidorEntity;
import es.caib.ripea.core.entity.ContenidorMovimentEntity;

/**
 * Definició dels mètodes necessaris per a gestionar una entitat de base
 * de dades del tipus ContenidorMoviment.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public interface ContenidorMovimentRepository extends JpaRepository<ContenidorMovimentEntity, Long> {

	List<ContenidorMovimentEntity> findByContenidorOrderByDataAsc(
			ContenidorEntity contenidor);

}
