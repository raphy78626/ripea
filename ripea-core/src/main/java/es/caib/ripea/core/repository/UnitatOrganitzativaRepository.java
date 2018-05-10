/**
 * 
 */
package es.caib.ripea.core.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import es.caib.ripea.core.entity.UnitatOrganitzativaEntity;

/**
 * Definició dels mètodes necessaris per a gestionar una entitat de base
 * de dades del tipus unitat organitzativa.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public interface UnitatOrganitzativaRepository extends JpaRepository<UnitatOrganitzativaEntity, Long> {

	
	@Query(	"from " +
			"    UnitatOrganitzativaEntity uo " +
			"where " +
			"    uo.codiUnitatArrel = :codiUnitatArrel " +
			"and (:esNullFiltreCodi = true or uo.codi = :codi) " +
			"and (:esNullFiltreDenominacio = true or lower(uo.denominacio) like lower('%'||:denominacio||'%')) ")
	Page<UnitatOrganitzativaEntity> findByEntitatAndUnitatCodiAndUnitatDenominacioFiltrePaginat(
			@Param("codiUnitatArrel") String codiUnitatArrel,
			@Param("esNullFiltreCodi") boolean esNullFiltreCodi,
			@Param("codi") String codi, 
			@Param("esNullFiltreDenominacio") boolean esNullFiltreDenominacio,
			@Param("denominacio") String denominacio,		
			Pageable pageable);
	
}
