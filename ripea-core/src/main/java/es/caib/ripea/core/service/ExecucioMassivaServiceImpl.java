/**
 * 
 */
package es.caib.ripea.core.service;

import java.security.Principal;
import java.util.Date;

import javax.annotation.Resource;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import es.caib.ripea.core.api.dto.ExecucioMassivaDto;
import es.caib.ripea.core.api.dto.ExecucioMassivaDto.ExecucioMassivaTipusDto;
import es.caib.ripea.core.api.exception.ExecucioMassivaException;
import es.caib.ripea.core.api.exception.NotFoundException;
import es.caib.ripea.core.api.exception.ValidationException;
import es.caib.ripea.core.api.service.DocumentService;
import es.caib.ripea.core.api.service.ExecucioMassivaService;
import es.caib.ripea.core.entity.ContingutEntity;
import es.caib.ripea.core.entity.ExecucioMassivaContingutEntity;
import es.caib.ripea.core.entity.ExecucioMassivaEntity;
import es.caib.ripea.core.entity.ExecucioMassivaEntity.ExecucioMassivaTipus;
import es.caib.ripea.core.helper.EmailHelper;
import es.caib.ripea.core.repository.ContingutRepository;
import es.caib.ripea.core.repository.ExecucioMassivaContingutRepository;
import es.caib.ripea.core.repository.ExecucioMassivaRepository;

/**
 * Implementació dels mètodes per a gestionar documents.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@Service
public class ExecucioMassivaServiceImpl implements ExecucioMassivaService {

	@Resource
	private ContingutRepository contingutRepository;
	@Resource
	private ExecucioMassivaRepository execucioMassivaRepository;
	@Resource
	private ExecucioMassivaContingutRepository execucioMassivaContingutRepository;
	@Resource
	private EmailHelper mailHelper;
	
	@Autowired
	private DocumentService documentService;

	@Transactional
	@Override
	public void crearExecucioMassiva(ExecucioMassivaDto dto) throws NotFoundException, ValidationException {
		ExecucioMassivaEntity execucioMassiva = null;
		
		Date dataInici;
		if (dto.getDataInici() == null) {
			dataInici = new Date();
		} else {
			dataInici = dto.getDataInici();
		}
		
		if (dto.getTipus() == ExecucioMassivaTipusDto.PORTASIGNATURES) {
			execucioMassiva = ExecucioMassivaEntity.getBuilder(
					ExecucioMassivaTipus.valueOf(dto.getTipus().toString()),
					dataInici,
					dto.getMotiu(), 
					dto.getPrioritat(), 
					dto.getDataCaducitat(), 
					false).build();
		}
		
		int ordre = 0;
		for (Long contingutId: dto.getContingutIds()) {
			ContingutEntity contingut = contingutRepository.findOne(contingutId);
			ExecucioMassivaContingutEntity emc = ExecucioMassivaContingutEntity.getBuilder(
					execucioMassiva, 
					contingut, 
					ordre++).build();
			
			execucioMassiva.addContingut(emc);
		}
		
		execucioMassivaRepository.save(execucioMassiva);
	}
	
	@Override
	public Long getExecucionsMassivesActiva(Long ultimaExecucioMassiva) {
		Date ara = new Date();
		Long execucioMassivaContingutId = null;
		Boolean nextFound = false;
		if (ultimaExecucioMassiva != null) {
			Long nextMassiu = execucioMassivaRepository.getNextMassiu(ultimaExecucioMassiva, ara);
			if (nextMassiu != null) {
				nextFound = true;
				execucioMassivaContingutId = execucioMassivaContingutRepository.findNextExecucioMassivaContingut(nextMassiu);
			}
		}
		
		if (execucioMassivaContingutId == null && !nextFound)
			execucioMassivaContingutId = execucioMassivaContingutRepository.findExecucioMassivaContingutId(ara);
		
		if (execucioMassivaContingutId == null) {
			// Comprobamos si es una ejecución masiva sin expedientes asociados. En ese caso actualizamos la fecha de fin
			Long mas = execucioMassivaRepository.getMinExecucioMassiva(ara);
			if (mas != null) {
				ExecucioMassivaEntity massiva = execucioMassivaRepository.findOne(mas);
				if (massiva != null) {
					massiva.updateDataFi(new Date());
					execucioMassivaRepository.saveAndFlush(massiva);
				}
			}
		}
		return execucioMassivaContingutId;
	}
	
	@Override
	public void executarExecucioMassiva(Long cmasiu_id) {
		ExecucioMassivaContingutEntity emc = execucioMassivaContingutRepository.findOne(cmasiu_id);
		if (emc == null)
			throw new NotFoundException(cmasiu_id, ExecucioMassivaContingutEntity.class);
		
		ExecucioMassivaEntity exm = emc.getExecucioMassiva();
		ExecucioMassivaTipus tipus = exm.getTipus();
		
		try {
			Authentication orgAuthentication = SecurityContextHolder.getContext().getAuthentication();
			
			final String user = exm.getCreatedBy().getCodi();
	        Principal principal = new Principal() {
				public String getName() {
					return user;
				}
			};
			
			Authentication authentication =  new UsernamePasswordAuthenticationToken(
					principal, 
					"N/A");
			
	        SecurityContextHolder.getContext().setAuthentication(authentication);
			
	        
			if (tipus == ExecucioMassivaTipus.PORTASIGNATURES){
				enviarPortafirmes(emc);
			}
			
			SecurityContextHolder.getContext().setAuthentication(orgAuthentication);
		} catch (Exception ex) {
			
			Throwable excepcioRetorn = ExceptionUtils.getRootCause(ex);
			
			SegonPlaServiceImpl.saveError(cmasiu_id, excepcioRetorn, exm.getTipus());
			throw new ExecucioMassivaException(
					emc.getContingut().getId(),
					emc.getContingut().getNom(),
					emc.getContingut().getTipus(),
					exm.getId(),
					emc.getId(),
					"Error al executar la acció massiva", 
					ex);
		}
	}
	
	@Override
	public void actualitzaUltimaOperacio(Long emc_id) {
		ExecucioMassivaContingutEntity emc = execucioMassivaContingutRepository.findOne(emc_id);
		if (emc == null)
			throw new NotFoundException(emc_id, ExecucioMassivaContingutEntity.class);
		
		if (emc.getExecucioMassiva().getContinguts().size() == emc.getOrdre() + 1) {
			try {
				ExecucioMassivaEntity em = emc.getExecucioMassiva();
				em.updateDataFi(new Date());
				execucioMassivaRepository.save(em);
			} catch (Exception ex) {
				throw new ExecucioMassivaException(
						emc.getContingut().getId(),
						emc.getContingut().getNom(),
						emc.getContingut().getTipus(),
						emc.getExecucioMassiva().getId(),
						emc.getId(),
						"CONTINGUT MASSIU: No s'ha pogut processar l'execució massiva d'aquest contingut", 
						ex);
			}
			try {
				if (emc.getExecucioMassiva().getEnviarCorreu()) {
					mailHelper.emailExecucioMassivaFinalitzada(emc.getExecucioMassiva());
				}
			} catch (Exception ex) {
				logger.error("EXPEDIENTMASSIU: No s'ha pogut enviar el correu de finalització", ex);
				
				throw new ExecucioMassivaException(
						emc.getContingut().getId(),
						emc.getContingut().getNom(),
						emc.getContingut().getTipus(),
						emc.getExecucioMassiva().getId(),
						emc.getId(),
						"CONTINGUT MASSIU: No s'ha pogut enviar el correu de finalització", 
						ex);
			}
		}
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void generaInformeError(Long emc_id, String error) {
		ExecucioMassivaContingutEntity emc = execucioMassivaContingutRepository.findOne(emc_id);
		if (emc == null)
			throw new NotFoundException(emc_id, ExecucioMassivaContingutEntity.class);
		
		
		Date ara = new Date();
		
		emc.updateError(
				ara, 
				error);
		
		execucioMassivaContingutRepository.save(emc);
	}
	
	private void enviarPortafirmes(ExecucioMassivaContingutEntity emc) throws Exception {
		ContingutEntity contingut = emc.getContingut();
		
		try {
			emc.updateDataInici(new Date());
			
			ExecucioMassivaEntity em = emc.getExecucioMassiva();
			documentService.portafirmesEnviar(
					contingut.getEntitat().getId(),
					contingut.getId(),
					em.getMotiu(),
					em.getPrioritat(),
					em.getDataCaducitat());
				
			emc.updateFinalitzat(new Date());
			execucioMassivaContingutRepository.save(emc);
		} catch (Exception ex) {
			logger.error("CONTINGUT MASSIU:" + emc.getId() + ". No s'ha pogut enviar el document al portasignatures", ex);
			throw ex;
		}
	}

	private static final Logger logger = LoggerFactory.getLogger(EntitatServiceImpl.class);
	
}
