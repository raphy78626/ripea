/**
 * 
 */
package es.caib.ripea.core.api.service;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;

import es.caib.ripea.core.api.dto.ExecucioMassivaContingutDto;
import es.caib.ripea.core.api.dto.ExecucioMassivaDto;
import es.caib.ripea.core.api.dto.UsuariDto;
import es.caib.ripea.core.api.exception.ExecucioMassivaException;
import es.caib.ripea.core.api.exception.NotFoundException;
import es.caib.ripea.core.api.exception.ValidationException;

/**
 * Declaració dels mètodes per a gestionar documents.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public interface ExecucioMassivaService {
	
	/**
	 * Crea una nova execució massiva
	 * 
	 * @param dto
	 *            Dto amb la informació de l'execució massiva a programar
	 * @throws NotFoundException
	 *             Si no s'ha trobat l'objecte amb l'id especificat.
	 * @throws ValidationException
	 *             Si el nom del contenidor conté caràcters invàlids.
	 */
	@PreAuthorize("hasRole('tothom')")
	public void crearExecucioMassiva(Long entitatId, ExecucioMassivaDto dto) throws NotFoundException, ValidationException;
	
	/**
	 * Retorna l'ID de la pròxima execució massiva activa
	 * 
	 * @param ultimaExecucioMassiva
	 *            Dto amb la informació de l'execució massiva a programar
	 */
	public Long getExecucionsMassivesActiva(Long ultimaExecucioMassiva);
	
	/**
	 * Executa un contingut massiu
	 * 
	 * @param cmasiu_id id del contingut massiu a executar
	 */
	public void executarExecucioMassiva(Long cmasiu_id) throws NotFoundException, ValidationException;
	
	
	public void generaInformeError(Long emc_id, String error) throws NotFoundException;
	
	public void actualitzaUltimaOperacio(Long ome_id) throws NotFoundException, ExecucioMassivaException;
	
	public List<ExecucioMassivaDto> findExecucionsMassivesPerUsuari(Long entitatId, UsuariDto usuari, int pagina) throws NotFoundException;
	
	public List<ExecucioMassivaDto> findExecucionsMassivesGlobals() throws NotFoundException;
	
	public List<ExecucioMassivaContingutDto> findContingutPerExecucioMassiva(Long exm_id) throws NotFoundException, ExecucioMassivaException;
}
