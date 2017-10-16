/**
 * 
 */
package es.caib.ripea.core.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import es.caib.ripea.core.api.registre.RegistreProcesEstatEnum;
import es.caib.ripea.core.api.registre.RegistreProcesEstatSistraEnum;
import es.caib.ripea.core.entity.ContingutEntity;
import es.caib.ripea.core.entity.RegistreEntity;

/**
 * Definició dels mètodes necessaris per a gestionar una entitat de base
 * de dades del tipus registre.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public interface RegistreRepository extends JpaRepository<RegistreEntity, Long> {

	List<RegistreEntity> findByReglaNotNullAndProcesEstatOrderByCreatedDateAsc(
			RegistreProcesEstatEnum procesEstat);

	
	/** Consulta les anotacions de registre pendents de processar amb regles que no
	 * siguin de tipus backoffice sistra.
	 * @return
	 */
	@Query("from RegistreEntity r " +
		    "where r.procesEstat = es.caib.ripea.core.api.registre.RegistreProcesEstatEnum.PENDENT " +
			"	and r.regla is not null " +
		    "	and r.regla.backofficeTipus <> es.caib.ripea.core.api.dto.BackofficeTipusEnumDto.SISTRA " +
		    "order by r.data asc")
	List<RegistreEntity> findAmbReglaPendentProcessar();
	
	/** Consulta les anotacions de registre pendents de processar amb regles per a tipus backoffice sistra que
	 * estiguin en estat pendent o error de sistra i que no hagin superat el nombre de reintents.
	 * @return
	 */
	@Query("from RegistreEntity r " +
		    "where r.regla.backofficeTipus = es.caib.ripea.core.api.dto.BackofficeTipusEnumDto.SISTRA " +
		    "	and r.procesEstatSistra in ( es.caib.ripea.core.api.registre.RegistreProcesEstatSistraEnum.PENDENT, " +
		    "						   es.caib.ripea.core.api.registre.RegistreProcesEstatSistraEnum.ERROR) " +
			"	and (r.regla.backofficeIntents is null or  r.procesIntents < r.regla.backofficeIntents) " +
		    "order by r.data asc")
	List<RegistreEntity> findAmbReglaPendentProcessarBackofficeSistra();

	
	RegistreEntity findByPareAndId(
			ContingutEntity pare,
			Long id);

	/*List<RegistreEntity> findByPareAndMotiuRebuigNullOrderByDataAsc(
			ContingutEntity pare);*/

	@Query(	"select " +
			"    pare.id, " +
			"    count(*) " +
			"from " +
			"    RegistreEntity " +
			"where " +
			"    motiuRebuig is null " +
			"and (pare is not null and pare in (:pares)) " +
			"group by " +
			"    pare.id")
	List<Object[]> countByParesAndNotRebutjat(
			@Param("pares") List<? extends ContingutEntity> pares);

	RegistreEntity findByEntitatCodiAndLlibreCodiAndRegistreTipusAndNumeroAndData(
			String entitatCodi,
			String llibreCodi,
			String registreTipus,
			String numero,
			Date data);


	/** Troba l'anotació de registre per identificador. */
	RegistreEntity findByIdentificador(String identificador);

	/** Consulta els identificadors pel backoffice sistra segons els paràmetres de filtre. 
	 * @param b 
	 * @param procesEstatSistra */
	@Query("select r.identificador " +
			"from RegistreEntity r " +
			"where r.regla.backofficeTipus = es.caib.ripea.core.api.dto.BackofficeTipusEnumDto.SISTRA " +
			"	and r.identificadorProcedimentSistra = :identificadorProcediment " +
			"	and r.identificadorTramitSistra = :identificadorTramit " +
			"	and (:esNullProcesEstatSistra = true or r.procesEstatSistra = :estatSistra) " +
			"	and (:esNullDesde = true or r.data >= :desde) " +
			"	and (:esNullFins = true  or r.data <= :fins) " +
		    "order by r.data asc")
	List<String> findPerBackofficeSistra(
			@Param("identificadorProcediment") String identificadorProcediment,
			@Param("identificadorTramit") String identificadorTramit,
			@Param("esNullProcesEstatSistra") boolean esNullProcesEstatSistra, 
			@Param("estatSistra") RegistreProcesEstatSistraEnum estatSistra,
			@Param("esNullDesde") boolean esNullDesde,
			@Param("desde") Date desde,
			@Param("esNullFins") boolean esNullFins,
			@Param("fins") Date fins
		);
}
