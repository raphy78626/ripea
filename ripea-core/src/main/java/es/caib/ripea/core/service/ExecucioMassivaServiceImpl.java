/**
 * 
 */
package es.caib.ripea.core.service;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.caib.ripea.core.api.dto.ExecucioMassivaDto;
import es.caib.ripea.core.api.dto.ExecucioMassivaDto.ExecucioMassivaTipusDto;
import es.caib.ripea.core.api.exception.NotFoundException;
import es.caib.ripea.core.api.exception.ValidationException;
import es.caib.ripea.core.api.service.ExecucioMassivaService;
import es.caib.ripea.core.entity.ContingutEntity;
import es.caib.ripea.core.entity.ExecucioMassivaContingutEntity;
import es.caib.ripea.core.entity.ExecucioMassivaEntity;
import es.caib.ripea.core.entity.ExecucioMassivaEntity.ExecucioMassivaTipus;
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
		if (ome == null)
			throw new NoTrobatException(ExecucioMassivaExpedient.class, cmasiu_id);
		
		ExecucioMassiva exm = ome.getExecucioMassiva();
		ExecucioMassivaTipus tipus = exm.getTipus();
		Entorn entorn = entornHelper.getEntornComprovantPermisos(
				exm.getEntorn(),
				false); 
		
		Expedient expedient = null;
		if (ome.getExpedient() != null) {
			expedient = ome.getExpedient();
		} else if (tipus  != ExecucioMassivaTipus.ELIMINAR_VERSIO_DEFPROC
					&& tipus != ExecucioMassivaTipus.PROPAGAR_PLANTILLES
					&& tipus != ExecucioMassivaTipus.PROPAGAR_CONSULTES){
			expedient = expedientHelper.findExpedientByProcessInstanceId(ome.getProcessInstanceId());
		}
		
		ExpedientTipus expedientTipus;
		if (expedient == null 
				&& (tipus == ExecucioMassivaTipus.ELIMINAR_VERSIO_DEFPROC 
					|| tipus == ExecucioMassivaTipus.PROPAGAR_PLANTILLES
					|| tipus == ExecucioMassivaTipus.PROPAGAR_CONSULTES) )
			expedientTipus = exm.getExpedientTipus();
		else
			expedientTipus = expedient.getTipus();
		
		logger.debug(
				"Executant la acció massiva (" +
				"expedientTipusId=" + (expedientTipus != null ? expedientTipus.getId() : "") + ", " +
				"dataInici=" + ome.getDataInici() + ", " +
				"expedient=" + ome.getId() + ", " +
				"acció=" + exm.getTipus());
		
		final Timer timerTotal = metricRegistry.timer(
				MetricRegistry.name(
						ExecucioMassivaService.class,
						"executar"));
		final Timer.Context contextTotal = timerTotal.time();
		Counter countTotal = metricRegistry.counter(
				MetricRegistry.name(
						ExecucioMassivaService.class,
						"executar.count"));
		countTotal.inc();
		final Timer timerEntorn = metricRegistry.timer(
				MetricRegistry.name(
						ExecucioMassivaService.class,
						"executar",
						entorn.getCodi()));
		final Timer.Context contextEntorn = timerEntorn.time();
		Counter countEntorn = metricRegistry.counter(
				MetricRegistry.name(
						ExecucioMassivaService.class,
						"executar.count",
						entorn.getCodi()));
		countEntorn.inc();
		final Timer timerTipexp = metricRegistry.timer(
				MetricRegistry.name(
						ExecucioMassivaService.class,
						"completar",
						entorn.getCodi(),
						(expedientTipus != null ? expedientTipus.getCodi() : "")));
		final Timer.Context contextTipexp = timerTipexp.time();
		Counter countTipexp = metricRegistry.counter(
				MetricRegistry.name(
						ExecucioMassivaService.class,
						"completar.count",
						entorn.getCodi(),
						(expedientTipus != null ? expedientTipus.getCodi() : "")));
		countTipexp.inc();
		try {
			Authentication orgAuthentication = SecurityContextHolder.getContext().getAuthentication();
			
//			final String user = exm.getUsuari();
//	        Principal principal = new Principal() {
//				public String getName() {
//					return user;
//				}
//			};
			
			Authentication authentication =  new UsernamePasswordAuthenticationToken (
					ome.getExecucioMassiva().getAuthenticationPrincipal(),
					"N/A",	//	ome.getExecucioMassiva().getAuthenticationCredentials(),
					ome.getExecucioMassiva().getAuthenticationRoles());
			
	        SecurityContextHolder.getContext().setAuthentication(authentication);
			
			String expedient_s = null;
	        if (MesuresTemporalsHelper.isActiu())
        		expedient_s = (expedientTipus != null ? expedientTipus.getNom() : "");
	        
			if (tipus == ExecucioMassivaTipus.EXECUTAR_TASCA){
				gestioTasca(ome);
			} else if (tipus == ExecucioMassivaTipus.ACTUALITZAR_VERSIO_DEFPROC){
				mesuresTemporalsHelper.mesuraIniciar("Actualitzar", "massiva", expedient_s);
				actualitzarVersio(ome);
				mesuresTemporalsHelper.mesuraCalcular("Actualitzar", "massiva", expedient_s);
			} else if (tipus == ExecucioMassivaTipus.ELIMINAR_VERSIO_DEFPROC){
				mesuresTemporalsHelper.mesuraIniciar("Eliniar", "massiva", expedient_s);
				eliminarVersio(ome);
				mesuresTemporalsHelper.mesuraCalcular("Actualitzar", "massiva", expedient_s);
			} else if (tipus == ExecucioMassivaTipus.EXECUTAR_SCRIPT){
				mesuresTemporalsHelper.mesuraIniciar("Executar script", "massiva", expedient_s);
				executarScript(ome);
				mesuresTemporalsHelper.mesuraCalcular("Executar script", "massiva", expedient_s);
			} else if (tipus == ExecucioMassivaTipus.EXECUTAR_ACCIO){
				mesuresTemporalsHelper.mesuraIniciar("Executar accio", "massiva", expedient_s);
				executarAccio(ome);
				mesuresTemporalsHelper.mesuraCalcular("Executar accio", "massiva", expedient_s);
			} else if (tipus == ExecucioMassivaTipus.ATURAR_EXPEDIENT){
				mesuresTemporalsHelper.mesuraIniciar("Aturar expedient", "massiva", expedient_s);
				aturarExpedient(ome);
				mesuresTemporalsHelper.mesuraCalcular("Aturar expedient", "massiva", expedient_s);
			} else if (tipus == ExecucioMassivaTipus.MODIFICAR_VARIABLE){
				mesuresTemporalsHelper.mesuraIniciar("Modificar variable", "massiva", expedient_s);
				modificarVariable(ome);
				mesuresTemporalsHelper.mesuraCalcular("Modificar variable", "massiva", expedient_s);
			} else if (tipus == ExecucioMassivaTipus.MODIFICAR_DOCUMENT){
				mesuresTemporalsHelper.mesuraIniciar("Modificar document", "massiva", expedient_s);
				modificarDocument(ome);
				mesuresTemporalsHelper.mesuraCalcular("Modificar document", "massiva", expedient_s);
			} else if (tipus == ExecucioMassivaTipus.REINDEXAR){
				mesuresTemporalsHelper.mesuraIniciar("Reindexar", "massiva", expedient_s);
				reindexarExpedient(ome);
				mesuresTemporalsHelper.mesuraCalcular("Reindexar", "massiva", expedient_s);
			} else if (tipus == ExecucioMassivaTipus.BUIDARLOG){
				mesuresTemporalsHelper.mesuraIniciar("Buidar log", "massiva", expedient_s);
				buidarLogExpedient(ome);
				mesuresTemporalsHelper.mesuraCalcular("Buidar log", "massiva", expedient_s);
			} else if (tipus == ExecucioMassivaTipus.REPRENDRE_EXPEDIENT){
				mesuresTemporalsHelper.mesuraIniciar("desfer fi process instance", "massiva", expedient_s);
				reprendreExpedient(ome);
				mesuresTemporalsHelper.mesuraCalcular("desfer fi process instance", "massiva", expedient_s);
			} else if (tipus == ExecucioMassivaTipus.REPRENDRE){
				mesuresTemporalsHelper.mesuraIniciar("reprendre tramitació process instance", "massiva", expedient_s);
				reprendreTramitacio(ome);
				mesuresTemporalsHelper.mesuraCalcular("reprendre tramitació process instance", "massiva", expedient_s);
			} else if (tipus == ExecucioMassivaTipus.REASSIGNAR){
				mesuresTemporalsHelper.mesuraIniciar("Reassignar", "massiva", expedient_s);
				//reassignarExpedient(ome);
				reassignarTasca(ome);
				mesuresTemporalsHelper.mesuraCalcular("Reassignar", "massiva", expedient_s);
			} else if (tipus == ExecucioMassivaTipus.PROPAGAR_PLANTILLES) {
				mesuresTemporalsHelper.mesuraIniciar("Propagar plantilles", "massiva", expedient_s);
				propagarPlantilles(ome);
				mesuresTemporalsHelper.mesuraCalcular("Propagar plantilles", "massiva", expedient_s);
			} else if (tipus == ExecucioMassivaTipus.PROPAGAR_CONSULTES) {
				mesuresTemporalsHelper.mesuraIniciar("Propagar consultes", "massiva", expedient_s);
				propagarConsultes(ome);
				mesuresTemporalsHelper.mesuraCalcular("Propagar consultes", "massiva", expedient_s);
			}
			SecurityContextHolder.getContext().setAuthentication(orgAuthentication);
		} catch (Exception ex) {
			logger.error("Error al executar la acció massiva (expedientTipusId=" + (expedientTipus != null ? expedientTipus.getId() : "") + ", dataInici=" + ome.getDataInici() + ", expedient=" + (expedient == null ? null : expedient.getId()) + ", acció=" + ome, ex);
			
			Throwable excepcioRetorn = ex;
			if (tipus != ExecucioMassivaTipus.ELIMINAR_VERSIO_DEFPROC && ExceptionUtils.getRootCause(ex) != null) {
				excepcioRetorn = ExceptionUtils.getRootCause(ex);
			}
			
			TascaProgramadaServiceImpl.saveError(cmasiu_id, excepcioRetorn, exm.getTipus());
			throw new ExecucioMassivaException(
					entorn.getId(), 
					entorn.getCodi(), 
					entorn.getNom(), 
					expedient == null ? null : expedient.getId(), 
					expedient == null ? null : expedient.getTitol(), 
					expedient == null ? null : expedient.getNumero(), 
					expedientTipus == null ? null : expedientTipus.getId(),
					expedientTipus == null ? null : expedientTipus.getCodi(),
					expedientTipus == null ? null : expedientTipus.getNom(),
					ome.getExecucioMassiva().getId(), 
					ome.getId(), 
					"Error al executar la acció massiva", 
					ex);
		} finally {
			contextTotal.stop();
			contextEntorn.stop();
			contextTipexp.stop();
		}
	}
	
	private void enviarPortafirmes(ExecucioMassivaContingutEntity ome) throws Exception {
//		Expedient exp = ome.getExpedient();
//		try {
//			ome.setDataInici(new Date());
//			Object param2 = deserialize(ome.getExecucioMassiva().getParam2());
//			String script = "";
//			if (param2 instanceof Object[]) {
//				script = (String)((Object[])param2)[0];
//			} else {
//				script = (String)param2;
//			}
//			expedientService.procesScriptExec(
//					exp.getId(),
//					exp.getProcessInstanceId(),
//					script);
//			ome.setEstat(ExecucioMassivaEstat.ESTAT_FINALITZAT);
//			ome.setDataFi(new Date());
//			execucioMassivaExpedientRepository.save(ome);
//		} catch (Exception ex) {
//			logger.error("OPERACIO:" + ome.getId() + ". No s'ha pogut executar l'script", ex);
//			throw ex;
//		}
	}

}
