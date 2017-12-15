/**
 * 
 */
package es.caib.ripea.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import es.caib.ripea.core.entity.CarpetaEntity;

/**
 * Definició dels mètodes necessaris per a gestionar una entitat de base
 * de dades del tipus carpeta.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public interface CarpetaRepository extends JpaRepository<CarpetaEntity, Long> {

}
