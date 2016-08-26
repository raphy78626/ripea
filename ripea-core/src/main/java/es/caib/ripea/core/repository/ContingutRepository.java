/**
 * 
 */
package es.caib.ripea.core.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import es.caib.ripea.core.entity.ContingutEntity;
import es.caib.ripea.core.entity.EntitatEntity;
import es.caib.ripea.core.entity.MetaNodeEntity;
import es.caib.ripea.core.entity.UsuariEntity;

/**
 * Definició dels mètodes necessaris per a gestionar una entitat de base
 * de dades del tipus contingut.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public interface ContingutRepository extends JpaRepository<ContingutEntity, Long> {

	List<ContingutEntity> findByPareAndEsborrat(
			ContingutEntity pare,
			int esborrat,
			Sort sort);

	List<ContingutEntity> findByPareAndNomOrderByEsborratAsc(
			ContingutEntity pare,
			String nom);

	ContingutEntity findByPareAndNomAndEsborrat(
			ContingutEntity pare,
			String nom,
			int esborrat);

	@Query(	"select " +
			"    c " +
			"from " +
			"    ContingutEntity c " +
			"where " +
			"    c.entitat = :entitat " +
			"and (c.pare is not null or (type(c) <> es.caib.ripea.core.entity.ArxiuEntity and type(c) <> es.caib.ripea.core.entity.BustiaEntity)) " +
			"and (:tipusArxiu = true or type(c) <> es.caib.ripea.core.entity.ArxiuEntity) " +
			"and (:tipusBustia = true or type(c) <> es.caib.ripea.core.entity.BustiaEntity) " +
			"and (:tipusCarpeta = true or type(c) <> es.caib.ripea.core.entity.CarpetaEntity) " +
			"and (:tipusDocument = true or type(c) <> es.caib.ripea.core.entity.DocumentEntity) " +
			"and (:tipusEscriptori = true or type(c) <> es.caib.ripea.core.entity.EscriptoriEntity) " +
			"and (:tipusExpedient = true or type(c) <> es.caib.ripea.core.entity.ExpedientEntity) " +
			"and (:tipusRegistre = true or type(c) <> es.caib.ripea.core.entity.RegistreEntity) " +
			"and (:esNullMetaNode = true or ((type(c) = es.caib.ripea.core.entity.ExpedientEntity or type(c) = es.caib.ripea.core.entity.DocumentEntity) and c.metaNode = :metaNode)) " +
			"and (:esNullNom = true or lower(c.nom) like lower('%'||:nom||'%')) " +
			"and (:esNullDataInici = true or c.lastModifiedDate >= :dataInici) " +
			"and (:esNullDataFi = true or c.lastModifiedDate <= :dataFi) " +
			"and ((:mostrarEsborrats = true and c.esborrat > 0) or (:mostrarNoEsborrats = true and c.esborrat = 0)) ")
	public Page<ContingutEntity> findByFiltrePaginat(
			@Param("entitat") EntitatEntity entitat,
			@Param("tipusArxiu") boolean tipusArxiu,
			@Param("tipusBustia") boolean tipusBustia,
			@Param("tipusCarpeta") boolean tipusCarpeta,
			@Param("tipusDocument") boolean tipusDocument,
			@Param("tipusEscriptori") boolean tipusEscriptori,
			@Param("tipusExpedient") boolean tipusExpedient,
			@Param("tipusRegistre") boolean tipusRegistre,
			@Param("esNullNom") boolean esNullNom,
			@Param("nom") String nom,
			@Param("esNullMetaNode") boolean esNullMetaNode,
			@Param("metaNode") MetaNodeEntity metaNode,
			@Param("esNullDataInici") boolean esNullDataInici,
			@Param("dataInici") Date dataInici,
			@Param("esNullDataFi") boolean esNullDataFi,
			@Param("dataFi") Date dataFi,
			@Param("mostrarEsborrats") boolean mostrarEsborrats,
			@Param("mostrarNoEsborrats") boolean mostrarNoEsborrats,
			Pageable pageable);

	@Query(	"select " +
			"    c " +
			"from " +
			"    ContingutEntity c " +
			"where " +
			"    c.entitat = :entitat " +
			"and (:esNullNom = true or lower(c.nom) like :nom) " +
			"and (:esNullUsuari = true or c.lastModifiedBy = :usuari) " +
			"and (:esNullDataInici = true or c.lastModifiedDate >= :dataInici) " +
			"and (:esNullDataFi = true or c.lastModifiedDate <= :dataFi) " +
			"and esborrat > 0")
	public Page<ContingutEntity> findEsborratsByFiltrePaginat(
			@Param("entitat") EntitatEntity entitat,
			@Param("esNullNom") boolean esNullNom,
			@Param("nom") String nom,
			@Param("esNullUsuari") boolean esNullUsuari,
			@Param("usuari") UsuariEntity usuari,
			@Param("esNullDataInici") boolean esNullDataInici,
			@Param("dataInici") Date dataInici,
			@Param("esNullDataFi") boolean esNullDataFi,
			@Param("dataFi") Date dataFi,
			Pageable pageable);

	@Query(	"select " +
			"    c.pare.id, " +
			"    count(*) " +
			"from " +
			"    ContingutEntity c " +
			"where " +
			"    c.entitat = :entitat " +
			"and c.pare in (:pares) " +
			"and (:incloureAnotacionsProcessades = true or (type(c) = es.caib.ripea.core.entity.RegistreEntity and (c.procesEstat = 'NO_PROCES' or c.procesEstat = 'ERROR'))) " +
			"and c.esborrat = 0 " +
			"group by " +
			"    c.pare")
	List<Object[]> countByPares(
			@Param("entitat") EntitatEntity entitat,
			@Param("pares") List<? extends ContingutEntity> pares,
			@Param("incloureAnotacionsProcessades") boolean incloureAnotacionsProcessades);

}
