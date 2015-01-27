/**
 * 
 */
package es.caib.ripea.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import es.caib.ripea.core.entity.RegistreDocumentEntity;

/**
 * Definició dels mètodes necessaris per a gestionar una entitat de base
 * de dades del tipus registre document.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public interface RegistreDocumentRepository extends JpaRepository<RegistreDocumentEntity, Long> {

}
