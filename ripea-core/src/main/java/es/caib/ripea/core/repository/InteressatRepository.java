/**
 * 
 */
package es.caib.ripea.core.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import es.caib.ripea.core.entity.EntitatEntity;
import es.caib.ripea.core.entity.ExpedientEntity;
import es.caib.ripea.core.entity.InteressatAdministracioEntity;
import es.caib.ripea.core.entity.InteressatCiutadaEntity;
import es.caib.ripea.core.entity.InteressatEntity;

/**
 * Definició dels mètodes necessaris per a gestionar una entitat de base
 * de dades del tipus interessat.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public interface InteressatRepository extends JpaRepository<InteressatEntity, Long> {

	@Query(	  "select "
			+ "    inter "
			+ "from "
			+ "    InteressatEntity inter "
			+ "join inter.expedients ex "
			+ "where "
			+ "    ex.entitat = :entitat "
			+ "and ex = :expedient "
			+ "order by "
			+ "    inter.nom desc")
	List<InteressatEntity> findByEntitatAndExpedient(
			@Param("entitat") EntitatEntity entitat,
			@Param("expedient") ExpedientEntity expedient);

	@Query(	  "select "
			+ "    distinct inter "
			+ "from "
			+ "    InteressatEntity inter "
			+ "join inter.expedients ex "
			+ "where "
			+ "    ex.entitat = :entitat "
			+ "and (:esNullNom = true or inter.nom = :nom) "
			+ "and (:esNullNif = true or inter.nif = :nif) "
			+ "and (:esNullLlinatges = true or inter.llinatges = :llinatges) "
			+ "order by "
			+ "    inter.nom desc")
	List<InteressatCiutadaEntity> findByFiltreCiutada(
			@Param("entitat") EntitatEntity entitat,
			@Param("esNullNom") boolean esNullNom,
			@Param("nom") String nom,
			@Param("esNullNif") boolean esNullNif,
			@Param("nif") String nif,
			@Param("esNullLlinatges") boolean esNullLlinatges,
			@Param("llinatges") String llinatges);

	@Query(	  "select "
			+ "    distinct inter "
			+ "from "
			+ "    InteressatEntity inter "
			+ "join inter.expedients ex "
			+ "where "
			+ "    ex.entitat = :entitat "
			+ "and (:esNullNom = true or inter.nom = :nom) "
			+ "and (:esNullIdentificador = true or inter.identificador = :identificador) "
			+ "order by "
			+ "    inter.nom desc")
	List<InteressatAdministracioEntity> findByFiltreAdministracio(
			@Param("entitat") EntitatEntity entitat,
			@Param("esNullNom") boolean esNullNom,
			@Param("nom") String nom,
			@Param("esNullIdentificador") boolean esNullIdentificador,
			@Param("identificador") String identificador);

}
