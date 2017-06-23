/**
 * 
 */
package es.caib.ripea.core.repository;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import es.caib.ripea.core.entity.ExecucioMassivaContingutEntity;

/**
 * Definició dels mètodes necessaris per a gestionar una entitat de base
 * de dades del tipus contingut.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public interface ExecucioMassivaContingutRepository extends JpaRepository<ExecucioMassivaContingutEntity, Long> {
	
	@Query("select min(e.id) " +
			"from	ExecucioMassivaContingut e " +
			"where	e.execucioMassiva.id = :nextMassiu " +
			"   and	e.dataFi is null ")
	public Long findNextExecucioMassivaContingut(@Param("nextMassiu") Long nextMassiu);
	
	@Query("select min(e.id) " +
			"from	ExecucioMassivaContingut e " +
			"where	e.execucioMassiva.id =	" +
			"			(select min(id) " +
			"			 from 	ExecucioMassiva " +
			"			 where 	dataInici <= :ara " +
			"					and dataFi is null) " +
			"	and	e.dataFi is null ")
	public Long findExecucioMassivaContingutId(@Param("ara") Date ara);
}
