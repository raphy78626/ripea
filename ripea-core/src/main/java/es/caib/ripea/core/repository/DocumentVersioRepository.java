/**
 * 
 */
package es.caib.ripea.core.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import es.caib.ripea.core.entity.DocumentEntity;
import es.caib.ripea.core.entity.DocumentVersioEntity;

/**
 * Definició dels mètodes necessaris per a gestionar una entitat de base
 * de dades del tipus document versió.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public interface DocumentVersioRepository extends JpaRepository<DocumentVersioEntity, Long> {

	DocumentVersioEntity findByDocumentAndVersio(DocumentEntity document, int versio);
	
	List<DocumentVersioEntity> findByDocumentOrderByVersioDesc(DocumentEntity document);

}
