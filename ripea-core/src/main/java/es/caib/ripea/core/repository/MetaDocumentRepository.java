/**
 * 
 */
package es.caib.ripea.core.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import es.caib.ripea.core.entity.EntitatEntity;
import es.caib.ripea.core.entity.MetaDadaEntity;
import es.caib.ripea.core.entity.MetaDocumentEntity;

/**
 * Definició dels mètodes necessaris per a gestionar una entitat de base
 * de dades del tipus meta-document.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public interface MetaDocumentRepository extends JpaRepository<MetaDocumentEntity, Long> {

	MetaDocumentEntity findByEntitatAndCodi(EntitatEntity entitat, String codi);

	List<MetaDocumentEntity> findByEntitat(EntitatEntity entitat, Sort sort);
	List<MetaDocumentEntity> findByEntitat(EntitatEntity entitat, Pageable pageable);
	List<MetaDocumentEntity> findByEntitatOrderByNomAsc(EntitatEntity entitat);

	List<MetaDocumentEntity> findByEntitatAndGlobalExpedientTrueAndActiuTrueOrderByNomAsc(EntitatEntity entitat);

	List<MetaDocumentEntity> findByEntitatAndActiuTrueOrderByNomAsc(EntitatEntity entitat);

	@Query(	"from " +
			"    MetaDocumentEntity md " +
			"where " +
			"    md.entitat = ?1 " +
			"and md.actiu = true " +
			"and (md.globalExpedient = false or (?2 = true and md.globalExpedient = true)) " +
			"order by " +
			"    md.nom asc")
	List<MetaDadaEntity> findByEntitatAndActiuTrueAndGlobalsOrderByNomAsc(
			EntitatEntity entitat,
			boolean incloureGlobalsExpedient);

	/*@Query(	"select " +
			"    md " +
			"from " +
			"    MetaExpedientMetaDocumentEntity med " +
			"    join med.metaDocument md " +
			"where " +
			"    med.metaExpedient = :metaExpedient " +
			"and md.entitat = :entitat " +
			"and md.actiu = true " +
			"order by " +
			"    md.nom asc")
	List<MetaDocumentEntity> findByEntitatAndMetaExpedientAndActiuTrueOrderByNomAsc(
			@Param("entitat") EntitatEntity entitat,
			@Param("metaExpedient") MetaExpedientEntity metaExpedient);*/

}
