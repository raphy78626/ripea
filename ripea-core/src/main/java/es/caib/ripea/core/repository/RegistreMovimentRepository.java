/**
 * 
 */
package es.caib.ripea.core.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import es.caib.ripea.core.entity.RegistreEntity;
import es.caib.ripea.core.entity.RegistreMovimentEntity;

/**
 * Definició dels mètodes necessaris per a gestionar una entitat de base
 * de dades del tipus RegistreMoviment.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public interface RegistreMovimentRepository extends JpaRepository<RegistreMovimentEntity, Long> {

	List<RegistreMovimentEntity> findByRegistreOrderByDataAsc(
			RegistreEntity registre);

}
