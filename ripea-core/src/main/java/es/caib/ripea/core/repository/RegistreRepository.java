/**
 * 
 */
package es.caib.ripea.core.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import es.caib.ripea.core.entity.ContenidorEntity;
import es.caib.ripea.core.entity.RegistreEntity;

/**
 * Definició dels mètodes necessaris per a gestionar una entitat de base
 * de dades del tipus registre.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public interface RegistreRepository extends JpaRepository<RegistreEntity, Long> {

	RegistreEntity findByContenidorAndId(
			ContenidorEntity contenidor,
			Long id);

	List<RegistreEntity> findByContenidorAndMotiuRebuigNullOrderByDataAsc(
			ContenidorEntity contenidor);

	@Query(	"select " +
			"    contenidor.id, " +
			"    count(*) " +
			"from " +
			"    RegistreEntity " +
			"where " +
			"    motiuRebuig is null " +
			"and (contenidor is not null and contenidor in (:contenidors)) " +
			"group by " +
			"    contenidor.id")
	List<Object[]> countByContenidorsAndNotRebutjat(
			@Param("contenidors") List<? extends ContenidorEntity> contenidors);

}
