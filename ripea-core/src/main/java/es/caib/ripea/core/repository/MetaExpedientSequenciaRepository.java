/**
 * 
 */
package es.caib.ripea.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import es.caib.ripea.core.entity.MetaExpedientEntity;
import es.caib.ripea.core.entity.MetaExpedientSequenciaEntity;

/**
 * Definició dels mètodes necessaris per a gestionar una entitat de base
 * de dades del tipus meta-expedient-seqüencia.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public interface MetaExpedientSequenciaRepository extends JpaRepository<MetaExpedientSequenciaEntity, Long> {

	MetaExpedientSequenciaEntity findByMetaExpedientAndAny(
			MetaExpedientEntity metaExpedient,
			int any);

}
