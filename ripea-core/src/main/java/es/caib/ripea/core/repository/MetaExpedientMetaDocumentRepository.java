/**
 * 
 */
package es.caib.ripea.core.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import es.caib.ripea.core.entity.MetaDocumentEntity;
import es.caib.ripea.core.entity.MetaExpedientEntity;
import es.caib.ripea.core.entity.MetaExpedientMetaDocumentEntity;

/**
 * Definició dels mètodes necessaris per a gestionar una entitat de base
 * de dades del tipus meta-expedient-meta-document.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public interface MetaExpedientMetaDocumentRepository extends JpaRepository<MetaExpedientMetaDocumentEntity, Long> {

	@Query("from MetaExpedientMetaDocumentEntity where metaExpedient = ?1 order by ordre asc")
	List<MetaExpedientMetaDocumentEntity> findByMetaExpedient(MetaExpedientEntity metaExpedient);
	@Query("from MetaExpedientMetaDocumentEntity where metaExpedient.id in ?1 order by metaExpedient.id asc, ordre asc")
	List<MetaExpedientMetaDocumentEntity> findByMetaExpedientIdIn(List<Long> metaExpedientIds);

	@Query(	"from " +
			"    MetaExpedientMetaDocumentEntity " +
			"where " +
			"    metaExpedient = ?1 " +
			"and metaDocument.actiu = true " +
			"order by " +
			"    metaDocument.nom asc")
	List<MetaExpedientMetaDocumentEntity> findByMetaExpedientAndMetaDocumentActiuTrueOrderByMetaDocumentNomAsc(
			MetaExpedientEntity metaExpedient);

	MetaExpedientMetaDocumentEntity findByMetaExpedientAndMetaDocument(
			MetaExpedientEntity metaExpedient,
			MetaDocumentEntity metaDocument);
}
