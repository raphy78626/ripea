/**
 * 
 */
package es.caib.ripea.core.repository;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import es.caib.ripea.core.entity.ExecucioMassivaEntity;

/**
 * Definició dels mètodes necessaris per a gestionar una entitat de base
 * de dades del tipus contingut.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public interface ExecucioMassivaRepository extends JpaRepository<ExecucioMassivaEntity, Long> {
	
	@Query("select min(id) " +
			"from 	ExecucioMassivaEntity " +
			"where 	dataInici <= :ara " +
			"	and dataFi is null " +
			"	and id > :lastMassiu ")
	Long getNextMassiu(@Param("lastMassiu") Long lastMassiu, @Param("ara") Date ara);
	
	@Query("select min(id) " +
			"	from 	ExecucioMassivaEntity " +
			"	where 	dataInici <= :ara " +
			"	and dataFi is null")
	Long getMinExecucioMassiva(@Param("ara") Date ara);
}
