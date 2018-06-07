/**
 * 
 */
package es.caib.ripea.core.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import es.caib.ripea.core.api.dto.UnitatOrganitzativaDto;
import es.caib.ripea.core.entity.EntitatEntity;
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
			"and (:esNullFiltreCodi = true or lower(uo.codi) like lower('%'||:codi||'%')) " +
			"and (:esNullFiltreDenominacio = true or lower(uo.denominacio) like lower('%'||:denominacio||'%')) ")
	Page<UnitatOrganitzativaEntity> findByEntitatAndUnitatCodiAndUnitatDenominacioFiltrePaginat(
			@Param("codiUnitatArrel") String codiUnitatArrel,
			@Param("esNullFiltreCodi") boolean esNullFiltreCodi,
			@Param("codi") String codi, 
			@Param("esNullFiltreDenominacio") boolean esNullFiltreDenominacio,
			@Param("denominacio") String denominacio,		
			Pageable pageable);
	
	
	UnitatOrganitzativaEntity findByCodi(String codi);
	
	List<UnitatOrganitzativaEntity> findByCodiUnitatArrel(String codiUnitatArrel);
	
	@Query(	"from " +
			"    UnitatOrganitzativaEntity uo " +
			"where " +
			"    uo.codiUnitatArrel = :codiUnitatArrel " +
			"and uo.estat!='V') ")
	List<UnitatOrganitzativaEntity> findByCodiUnitatArrelAndEstatNotV(
			@Param("codiUnitatArrel") String codiUnitatArrel);
	
	UnitatOrganitzativaEntity findByCodiUnitatArrelAndCodi(String codiUnitatArrel, String codi);
	
	
}
