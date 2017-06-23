/**
 * 
 */
package es.caib.ripea.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import es.caib.ripea.core.entity.ExecucioMassivaEntity;

/**
 * Definició dels mètodes necessaris per a gestionar una entitat de base
 * de dades del tipus contingut.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public interface ExecucioMassivaRepository extends JpaRepository<ExecucioMassivaEntity, Long> {
}
