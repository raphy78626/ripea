/**
 * 
 */
package es.caib.ripea.core.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import es.caib.ripea.core.entity.DocumentEntity;
import es.caib.ripea.core.entity.DocumentPortafirmesEntity;

/**
 * Definició dels mètodes necessaris per a gestionar una entitat de base
 * de dades del tipus document-portafirmes.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public interface DocumentPortafirmesRepository extends JpaRepository<DocumentPortafirmesEntity, Long> {

	List<DocumentPortafirmesEntity> findByDocument(DocumentEntity document);

	List<DocumentPortafirmesEntity> findByDocumentAndVersio(
			DocumentEntity document,
			int versio);

	DocumentPortafirmesEntity findByPortafirmesId(
			long portafirmesId);

}
