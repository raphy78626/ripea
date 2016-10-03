/**
 * 
 */
package es.caib.ripea.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import es.caib.ripea.core.entity.DocumentPublicacioEntity;

/**
 * Definició dels mètodes necessaris per a gestionar una entitat de base
 * de dades del tipus documentPublicacio.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public interface DocumentPublicacioRepository extends JpaRepository<DocumentPublicacioEntity, Long> {

}
