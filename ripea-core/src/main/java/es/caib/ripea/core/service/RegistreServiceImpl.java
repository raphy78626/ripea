/**
 * 
 */
package es.caib.ripea.core.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.caib.plugins.arxiu.api.ContingutArxiu;
import es.caib.plugins.arxiu.api.Document;
import es.caib.plugins.arxiu.api.DocumentContingut;
import es.caib.plugins.arxiu.api.DocumentMetadades;
import es.caib.plugins.arxiu.api.Firma;
import es.caib.plugins.arxiu.api.FirmaTipus;
import es.caib.ripea.core.api.dto.BackofficeTipusEnumDto;
import es.caib.ripea.core.api.dto.FirmaDto;
import es.caib.ripea.core.api.dto.FitxerDto;
import es.caib.ripea.core.api.dto.LogTipusEnumDto;
import es.caib.ripea.core.api.dto.RegistreAnnexDetallDto;
import es.caib.ripea.core.api.dto.RegistreAnotacioDto;
import es.caib.ripea.core.api.dto.ReglaTipusEnumDto;
import es.caib.ripea.core.api.exception.BustiaServiceException;
import es.caib.ripea.core.api.exception.NotFoundException;
import es.caib.ripea.core.api.exception.ScheduledTaskException;
import es.caib.ripea.core.api.exception.ValidationException;
import es.caib.ripea.core.api.registre.RegistreProcesEstatEnum;
import es.caib.ripea.core.api.registre.RegistreProcesEstatSistraEnum;
import es.caib.ripea.core.api.service.RegistreService;
import es.caib.ripea.core.entity.BustiaEntity;
import es.caib.ripea.core.entity.ContingutEntity;
import es.caib.ripea.core.entity.EntitatEntity;
import es.caib.ripea.core.entity.FirmaEntity;
import es.caib.ripea.core.entity.RegistreAnnexEntity;
import es.caib.ripea.core.entity.RegistreEntity;
import es.caib.ripea.core.helper.AlertaHelper;
import es.caib.ripea.core.helper.BustiaHelper;
import es.caib.ripea.core.helper.ContingutHelper;
import es.caib.ripea.core.helper.ContingutLogHelper;
import es.caib.ripea.core.helper.ConversioTipusHelper;
import es.caib.ripea.core.helper.EntityComprovarHelper;
import es.caib.ripea.core.helper.ExpedientHelper;
import es.caib.ripea.core.helper.MessageHelper;
import es.caib.ripea.core.helper.PluginHelper;
import es.caib.ripea.core.helper.PropertiesHelper;
import es.caib.ripea.core.helper.RegistreHelper;
import es.caib.ripea.core.helper.ReglaHelper;
import es.caib.ripea.core.helper.XmlHelper;
import es.caib.ripea.core.repository.BustiaRepository;
import es.caib.ripea.core.repository.ExpedientRepository;
import es.caib.ripea.core.repository.RegistreAnnexRepository;
import es.caib.ripea.core.repository.RegistreRepository;

/**
 * Implementació dels mètodes per a gestionar anotacions
 * de registre.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@Service
public class RegistreServiceImpl implements RegistreService {

	@Resource
	private RegistreRepository registreRepository;
	@Resource
	private ExpedientRepository expedientRepository;
	@Resource
	private BustiaRepository bustiaRepository;
	@Resource
	private RegistreAnnexRepository registreAnnexRepository;

	@Resource
	private ContingutHelper contingutHelper;
	@Resource
	private ContingutLogHelper contingutLogHelper;
	@Resource
	private ConversioTipusHelper conversioTipusHelper;
	@Resource
	private EntityComprovarHelper entityComprovarHelper;
	@Resource
	private ReglaHelper reglaHelper;
	@Resource
	private BustiaHelper bustiaHelper;
	@Resource
	private RegistreHelper registreHelper;
	@Resource
	private PluginHelper pluginHelper;
	@Resource
	private ExpedientHelper expedientHelper;
	@Resource
	private MessageHelper messageHelper;
	@Resource
	private AlertaHelper alertaHelper;
	

	@Transactional(readOnly = true)
	@Override
	public RegistreAnotacioDto findOne(
			Long entitatId,
			Long contingutId,
			Long registreId) throws NotFoundException {
		logger.debug("Obtenint anotació de registre ("
				+ "entitatId=" + entitatId + ", "
				+ "contingutId=" + contingutId + ", "
				+ "registreId=" + registreId + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				true,
				false,
				false);
		ContingutEntity contingut = entityComprovarHelper.comprovarContingut(
				entitat,
				contingutId,
				null);
		if (contingut instanceof BustiaEntity) {
			entityComprovarHelper.comprovarBustia(
					entitat,
					contingutId,
					true);
		} else {
			// Comprova l'accés al path del contenidor pare
			contingutHelper.comprovarPermisosPathContingut(
					contingut,
					true,
					false,
					false,
					true);
		}
		RegistreEntity registre = registreRepository.findByPareAndId(
				contingut,
				registreId);
		RegistreAnotacioDto registreAnotacio = (RegistreAnotacioDto)contingutHelper.toContingutDto(
				registre);
		
		contingutHelper.tractarInteressats(registreAnotacio.getInteressats());
		
		if (registre.getJustificantArxiuUuid() != null && !registre.getJustificantArxiuUuid().isEmpty()) {
			RegistreAnnexDetallDto justificant = getJustificantPerRegistre(
					entitat, 
					contingut, 
					registre.getJustificantArxiuUuid(), 
					true);
			
			registreAnotacio.setJustificant(justificant);
		}
		
		return registreAnotacio;
	}

	@Transactional
	@Override
	public void rebutjar(
			Long entitatId,
			Long bustiaId,
			Long registreId,
			String motiu) {
		logger.debug("Rebutjar anotació de registre a la bústia ("
				+ "entitatId=" + entitatId + ", "
				+ "bustiaId=" + bustiaId + ", "
				+ "registreId=" + registreId + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				true,
				false,
				false);
		BustiaEntity bustia = entityComprovarHelper.comprovarBustia(
				entitat,
				bustiaId,
				true);
		RegistreEntity registre = entityComprovarHelper.comprovarRegistre(
				registreId,
				null);
		if (!registre.getPare().equals(bustia)) {
			logger.error("No s'ha trobat el registre a dins la bústia (" +
					"bustiaId=" + bustiaId + "," +
					"registreId=" + registreId + ")");
			throw new ValidationException(
					registreId,
					RegistreEntity.class,
					"La bústia especificada (id=" + bustiaId + ") no coincideix amb la bústia de l'anotació de registre");
		}
		registre.updateRebuig(motiu);
		// Refrescam cache usuaris bústia
		bustiaHelper.evictElementsPendentsBustia(
				entitat,
				bustia);
	}

//	@Override
//	@Transactional
//	@Async
//	@Scheduled(fixedRateString = "${config:es.caib.ripea.tasca.regla.pendent.periode.execucio}")
	public void reglaAplicarPendents() {
		logger.debug("Aplicant regles a les anotacions pendents");
		try {
			
			List<RegistreEntity> pendents = registreRepository.findAmbReglaPendentProcessar();
			logger.debug("Aplicant regles a " + pendents.size() + " registres pendents");
			if (!pendents.isEmpty()) {
				for (RegistreEntity pendent: pendents) {
					try {
						reglaAplicar(pendent);
					} catch (Exception e) {
						alertaHelper.crearAlerta(
								pendent.getId(),
								messageHelper.getMessage(
										"alertes.segon.pla.aplicar.regles.error",
										new Object[] {pendent.getId()}),
								e);
						throw e;
					}
				}
			} else {
				logger.debug("No hi ha registres pendents de processar");
			}
		} catch (Exception e) {
			logger.error("Error aplicant regles a les anotacions pendents.", e);
			e.printStackTrace();
		}
	}
	
//	@Override
//	@Transactional
//	@Async
//	@Scheduled(fixedRateString = "60000")
	public void reglaAplicarPendentsBackofficeSistra() {
		logger.debug("Aplicant regles a les anotacions pendents per a regles de backoffice tipus Sistra");
		List<RegistreEntity> pendents = registreRepository.findAmbReglaPendentProcessarBackofficeSistra();
		logger.debug("Aplicant regles a " + pendents.size() + " registres pendents");
		if (!pendents.isEmpty()) {
			Date ara;
			Date darrerProcessament;
			Integer minutsEntreReintents;
			Calendar properProcessamentCal = Calendar.getInstance();
			for (RegistreEntity pendent: pendents) {
				try {
					// Comprova si ha passat el temps entre reintents o ha d'esperar
					boolean esperar = false;
					darrerProcessament = pendent.getProcesData();
					minutsEntreReintents = pendent.getRegla().getBackofficeTempsEntreIntents();
					if (darrerProcessament != null && minutsEntreReintents != null) {
						// Calcula el temps pel proper intent
						properProcessamentCal.setTime(darrerProcessament);
						properProcessamentCal.add(Calendar.MINUTE, minutsEntreReintents);
						ara  = new Date();
						esperar = ara.before(properProcessamentCal.getTime());
					}
					if (!esperar) {
						reglaAplicar(pendent);
					}
				} catch (Exception e) {
					alertaHelper.crearAlerta(
							pendent.getId(),
							messageHelper.getMessage(
									"alertes.segon.pla.aplicar.regles.backoffice.sistra.error",
									new Object[] {pendent.getId()}),
							e);
				}
			}
		} else {
			logger.debug("No hi ha registres pendents de processar");
		}
	}
	
	
	@Override
	@Async
	@Transactional
	@Scheduled(fixedDelayString = "${config:es.caib.ripea.tasca.dist.anotacio.pendent.periode.execucio}")
	public void distribuirAnotacionsPendents() {
		logger.debug("Distribuïnt anotacions de registere pendents");
		try {
			String maxReintents = PropertiesHelper.getProperties().getProperty("es.caib.ripea.tasca.dist.anotacio.pendent.max.reintents");
			List<RegistreEntity> pendents = registreRepository.findPendentsDistribuir(Integer.parseInt(maxReintents));
			
			logger.debug("Distribuïnt " + pendents.size() + " anotacion pendents");
			if (!pendents.isEmpty()) {
				for (RegistreEntity pendent: pendents) {
					try {
						distribuirAnotacioPendent(pendent);
					} catch (Exception e) {
						registreHelper.processarErrorAsincron(pendent, e);
						throw e;
					}
				}
			} else {
				logger.debug("No hi ha anotacions pendents de distribuïr");
			}
		} catch (Exception e) {
			logger.error("Error distribuïnt anotacions pendents", e);
			e.printStackTrace();
		}
	}
	
	@Override
	@Transactional
	public boolean reglaReintentarAdmin(
			Long entitatId,
			Long bustiaId,
			Long registreId) {
		logger.debug("Reintentant aplicació de regla a anotació pendent (" +
				"entitatId=" + entitatId + ", " +
				"bustiaId=" + bustiaId + ", " +
				"registreId=" + registreId + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				false,
				false,
				true);
		BustiaEntity bustia = entityComprovarHelper.comprovarBustia(
				entitat,
				bustiaId,
				false);
		RegistreEntity anotacio = entityComprovarHelper.comprovarRegistre(
				registreId,
				bustia);
		if (	RegistreProcesEstatEnum.PENDENT.equals(anotacio.getProcesEstat()) ||
				RegistreProcesEstatEnum.ERROR.equals(anotacio.getProcesEstat())) {
			return reglaAplicar(anotacio);
		} else {
			throw new ValidationException(
					anotacio.getId(),
					RegistreEntity.class,
					"L'estat de l'anotació no és PENDENT o ERROR");
		}
	}

	@Override
	@Transactional
	public boolean reglaReintentarUser(
			Long entitatId,
			Long bustiaId,
			Long registreId) {
		logger.debug("Reintentant aplicació de regla a anotació pendent (" +
				"entitatId=" + entitatId + ", " +
				"bustiaId=" + bustiaId + ", " +
				"registreId=" + registreId + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				false,
				false,
				true);
		BustiaEntity bustia = entityComprovarHelper.comprovarBustia(
				entitat,
				bustiaId,
				true);
		RegistreEntity anotacio = entityComprovarHelper.comprovarRegistre(
				registreId,
				bustia);
		if (	RegistreProcesEstatEnum.PENDENT.equals(anotacio.getProcesEstat()) ||
				RegistreProcesEstatEnum.ERROR.equals(anotacio.getProcesEstat())) {
			return reglaAplicar(anotacio);
		} else {
			throw new ValidationException(
					anotacio.getId(),
					RegistreEntity.class,
					"L'estat de l'anotació no és PENDENT o ERROR");
		}
	}
	
	
	@Transactional(readOnly = true)
	@Override
	public FitxerDto getArxiuAnnex(
			Long annexId) {
		RegistreAnnexEntity annex = registreAnnexRepository.findOne(annexId);
		FitxerDto arxiu = new FitxerDto();
		
		Document document = null;
		document = pluginHelper.arxiuDocumentConsultar(annex.getRegistre(), annex.getFitxerArxiuUuid(), null, true);
		
		if (document != null) {
			DocumentContingut documentContingut = document.getContingut();
			if (documentContingut != null) {
				arxiu.setNom(annex.getFitxerNom());
				arxiu.setContentType(documentContingut.getTipusMime());
				arxiu.setContingut(documentContingut.getContingut());
				arxiu.setTamany(documentContingut.getContingut().length);
			}
		}
		return arxiu;
	}
	
	@Transactional(readOnly = true)
	@Override
	public FitxerDto getJustificant(
			Long registreId) {
		RegistreEntity registre = registreRepository.findOne(registreId);
		FitxerDto arxiu = new FitxerDto();
		
		Document document = null;
		document = pluginHelper.arxiuDocumentConsultar(registre, registre.getJustificantArxiuUuid(), null, true);
		
		if (document != null) {
			DocumentContingut documentContingut = document.getContingut();
			if (documentContingut != null) {
				arxiu.setNom(obtenirJustificantNom(document));
				arxiu.setContentType(documentContingut.getTipusMime());
				arxiu.setContingut(documentContingut.getContingut());
				arxiu.setTamany(documentContingut.getContingut().length);
			}
		}
		return arxiu;
	}
	
	@Transactional(readOnly = true)
	@Override
	public FitxerDto getAnnexFirmaContingut(
			Long annexId,
			int indexFirma) {
		RegistreAnnexEntity annex = registreAnnexRepository.findOne(annexId);
		FitxerDto arxiu = new FitxerDto();
		
		Document document = null;
		document = pluginHelper.arxiuDocumentConsultar(annex.getRegistre(), annex.getFitxerArxiuUuid(), null, true);
		
		if (document != null) {
			List<Firma> firmes = document.getFirmes();
			if (firmes != null && firmes.size() > 0) {
				
				Iterator<Firma> it = firmes.iterator();
				while (it.hasNext()) {
					Firma firma = it.next();
					if (firma.getTipus() == FirmaTipus.CSV) {
						it.remove();
					}
				}
				
				Firma firma = firmes.get(indexFirma);
				FirmaEntity firmaEntity = annex.getFirmes().get(indexFirma);
				if (firma != null && firmaEntity != null) {
					arxiu.setNom(firmaEntity.getFitxerNom());
					arxiu.setContentType(firmaEntity.getTipusMime());
					arxiu.setContingut(firma.getContingut());
					arxiu.setTamany(firma.getContingut().length);
				}
			}
		}
		return arxiu;
	}

	@Transactional(readOnly = true)
	@Override
	public List<RegistreAnnexDetallDto> getAnnexosAmbArxiu(
			Long entitatId,
			Long contingutId,
			Long registreId) throws NotFoundException {
		logger.debug("Obtenint anotació de registre ("
				+ "entitatId=" + entitatId + ", "
				+ "contingutId=" + contingutId + ", "
				+ "registreId=" + registreId + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				true,
				false,
				false);
		ContingutEntity contingut = entityComprovarHelper.comprovarContingut(
				entitat,
				contingutId,
				null);
		if (contingut instanceof BustiaEntity) {
			entityComprovarHelper.comprovarBustia(
					entitat,
					contingutId,
					true);
		} else {
			// Comprova l'accés al path del contenidor pare
			contingutHelper.comprovarPermisosPathContingut(
					contingut,
					true,
					false,
					false,
					true);
		}
		RegistreEntity registre = registreRepository.findByPareAndId(
				contingut,
				registreId);

		List<RegistreAnnexDetallDto> annexos = new ArrayList<RegistreAnnexDetallDto>();
		for (RegistreAnnexEntity annexEntity: registre.getAnnexos()) {
			RegistreAnnexDetallDto annex = conversioTipusHelper.convertir(annexEntity, RegistreAnnexDetallDto.class);
			if (annex.getFitxerArxiuUuid() != null && !annex.getFitxerArxiuUuid().isEmpty()) {
				Document document = pluginHelper.arxiuDocumentConsultar(contingut, annex.getFitxerArxiuUuid(), null, true);
				annex.setAmbDocument(true);
				
				if (document.getFirmes() != null && document.getFirmes().size() > 0) {
					annex.setFirmes(conversioTipusHelper.convertirList(document.getFirmes(), FirmaDto.class));
					annex.setAmbFirma(true);
					
					int j = 0;
					
					Iterator<FirmaDto> it = annex.getFirmes().iterator();
					while (it.hasNext()) {
						FirmaDto firma = it.next();
						if (firma.getTipus() == "TF01") {
							it.remove();
						} else if (j < annexEntity.getFirmes().size()) {
							firma.setFitxerNom(annexEntity.getFirmes().get(j).getFitxerNom());
							firma.setTipusMime(annexEntity.getFirmes().get(j).getTipusMime());
							firma.setAutofirma(annexEntity.getFirmes().get(j).getAutofirma());
							j++;
						}
					}
				}
			}
			annexos.add(annex);
		}
		
		return annexos;
	}
	
	
	
	@Transactional (readOnly = true)
	@Override
	public RegistreAnotacioDto findAmbIdentificador(String identificador) {
		RegistreAnotacioDto registreAnotacioDto;
		RegistreEntity registre = registreRepository.findByIdentificador(identificador);
		if (registre != null)
			registreAnotacioDto = (RegistreAnotacioDto) contingutHelper.toContingutDto(registre);
		else
			registreAnotacioDto = null;
		return registreAnotacioDto;
	}

	@Transactional
	@Override
	public void updateProces(
			Long registreId, 
			RegistreProcesEstatEnum procesEstat, 
			RegistreProcesEstatSistraEnum procesEstatSistra,
			String resultadoProcesamiento) {
		logger.debug("Actualitzar estat procés anotació de registre ("
				+ "registreId=" + registreId + ", "
				+ "procesEstat=" + procesEstat + ", "
				+ "procesEstatSistra=" + procesEstatSistra + ", "
				+ "resultadoProcesamiento=" + resultadoProcesamiento + ")");
		RegistreEntity registre = registreRepository.findOne(registreId);
		registre.updateProces(
				new Date(), 
				procesEstat, 
				resultadoProcesamiento);
		registre.updateProcesSistra(procesEstatSistra);
	}

	@Transactional (readOnly = true)
	@Override
	public List<String> findPerBackofficeSistra(
			String identificadorProcediment, 
			String identificadorTramit,
			RegistreProcesEstatSistraEnum procesEstatSistra, 
			Date desdeDate, 
			Date finsDate) {
		logger.debug("Consultant els numeros d'entrada del registre pel backoffice Sistra ("
				+ "identificadorProcediment=" + identificadorProcediment + ", "
				+ "identificadorTramit=" + identificadorTramit + ", "
				+ "procesEstatSistra=" + procesEstatSistra + ", "
				+ "desdeDate=" + desdeDate + ", "
				+ "finsDate=" + finsDate + ")");
		
		return registreRepository.findPerBackofficeSistra(
				identificadorProcediment,
				identificadorTramit,
				procesEstatSistra == null,
				procesEstatSistra,
				desdeDate == null,
				desdeDate,
				finsDate == null,
				finsDate);
	}
	
	private RegistreAnnexDetallDto getJustificantPerRegistre(
			EntitatEntity entitat,
			ContingutEntity contingut,
			String justificantUuid,
			boolean ambContingut) throws NotFoundException {
		logger.debug("Obtenint anotació de registre ("
				+ "entitatId=" + entitat.getId() + ", "
				+ "contingutId=" + contingut.getId() + ", "
				+ "justificantUuid=" + justificantUuid + ")");
		

		RegistreAnnexDetallDto annex = new RegistreAnnexDetallDto();
		Document document = pluginHelper.arxiuDocumentConsultar(contingut, justificantUuid, null, ambContingut);
		
		annex.setFitxerNom(obtenirJustificantNom(document));
		annex.setFitxerTamany(document.getContingut().getContingut().length);
		annex.setFitxerTipusMime(document.getContingut().getTipusMime());
		annex.setTitol(document.getNom());
		DocumentMetadades metadades = document.getMetadades();
		if (metadades != null) {
			annex.setDataCaptura(metadades.getDataCaptura());
			annex.setOrigenCiutadaAdmin(metadades.getOrigen().name());
			annex.setNtiElaboracioEstat(metadades.getEstatElaboracio().name());
			annex.setNtiTipusDocument(metadades.getTipusDocumental().name());
		}
		
		annex.setAmbDocument(true);
		
		return annex;
	}

	private boolean reglaAplicar(
			RegistreEntity anotacio) {
		contingutLogHelper.log(
				anotacio,
				LogTipusEnumDto.PROCESSAMENT,
				null,
				null,
				false,
				false);
		logger.debug("Aplicant regla a anotació de registre (" +
				"anotacioId=" + anotacio.getId() + ", " +
				"anotacioNum=" + anotacio.getIdentificador() + ", " +
				"reglaId=" + anotacio.getRegla().getId() + ", " +
				"reglaTipus=" + anotacio.getRegla().getTipus().name() + ", " +
				(anotacio.getRegla().getBackofficeTipus() != null ?
						"reglaBackofficeTipus=" + anotacio.getRegla().getBackofficeTipus().name() + ", " 
						: "") +
				"metaExpedient=" + anotacio.getRegla().getMetaExpedient() + ", " +
				"arxiu=" + anotacio.getRegla().getArxiu() + ", " +
				"bustia=" + anotacio.getRegla().getBustia() + ")");
		try {
			reglaHelper.aplicar(anotacio.getId());
			logger.debug("Processament anotació OK (id=" + anotacio.getId() + ", núm.=" + anotacio.getIdentificador() + ")");
			
			alertaHelper.crearAlerta(
					anotacio.getId(),
					messageHelper.getMessage(
							"alertes.segon.pla.aplicar.regles",
							new Object[] {anotacio.getId()}),
					null);
			
			return true;
		} catch (Exception ex) {
			String procesError;
			if (ex instanceof ScheduledTaskException) {
				procesError = ex.getMessage();
			} else {
				procesError = ExceptionUtils.getStackTrace(ExceptionUtils.getRootCause(ex));
			}
			reglaHelper.actualitzarEstatError(
					anotacio.getId(),
					procesError);
			logger.debug("Processament anotació ERROR (" +
					"id=" + anotacio.getId() + ", " +
					"núm.=" + anotacio.getIdentificador() + "): " +
					procesError);
			
			alertaHelper.crearAlerta(
					anotacio.getId(),
					messageHelper.getMessage(
							"alertes.segon.pla.aplicar.regles.error",
							new Object[] {anotacio.getId()}),
					ex);
			
			return false;
		}
	}
	
	private void distribuirAnotacioPendent(RegistreEntity anotacio) {
		
		BustiaEntity bustia = bustiaHelper.findBustiaDesti(
				anotacio.getEntitat(),
				anotacio.getUnitatAdministrativa());
		
		if (anotacio.getAnnexos() != null && anotacio.getAnnexos().size() > 0) {
			ContingutArxiu expedientCreat = crearExpedientArxiuTemporal(anotacio, bustia);
			anotacio.updateExpedientArxiuUuid(expedientCreat.getIdentificador());
			registreRepository.saveAndFlush(anotacio);
			processarAnnexos(anotacio, bustia, expedientCreat);
		}
		
		if (anotacio.getRegla() != null) {
			if (!reglaAplicar(anotacio))
				throw new ScheduledTaskException("Error aplicant regla en segon pla per a l'anotació " + anotacio.getId());
		} else {
			Date dataProcesAsincron = new Date();
			anotacio.updateProces(
					dataProcesAsincron,
					RegistreProcesEstatEnum.PROCESSAT,
					null);
			anotacio.updateDistribucioAsincrona(
					dataProcesAsincron, 
					null);
			registreRepository.save(anotacio);
		}
		
	}
	
	private ContingutArxiu crearExpedientArxiuTemporal(RegistreEntity anotacio, BustiaEntity bustia) {
		
		ContingutArxiu expedientCreat = pluginHelper.arxiuExpedientPerAnotacioCrear(
				anotacio, 
				bustia);
		
		return expedientCreat;
	}
	
	private void processarAnnexos(RegistreEntity anotacio, BustiaEntity bustia, ContingutArxiu expedientCreat) {
		boolean isExpedientFinal = false;
		try {
			for (RegistreAnnexEntity annex: anotacio.getAnnexos()) {
				//si tenim contingut de fitxer i també referència del registre, hem de tornar una excepció
				if (annex.getFitxerArxiuUuid() == null && annex.getFitxerContingut() == null) {
					throw new ValidationException(
							"S'ha d'especificar o bé la referència del document o el contingut del document"
							+ " per l'annex [" + annex.getTitol() + "]");
				} else {
					guardarDocumentAnnex (annex, bustia, expedientCreat);
					isExpedientFinal = true;
				}
				// Si l'anotació té una regla sistra llavors intenta extreure la informació dels identificadors
				// del tràmit i del procediment de l'annex
				if (anotacio.getRegla() != null 
						&& anotacio.getRegla().getTipus() == ReglaTipusEnumDto.BACKOFFICE
						&& anotacio.getRegla().getBackofficeTipus() == BackofficeTipusEnumDto.SISTRA) {
					if (annex.getFitxerNom().equals("DatosPropios.xml")
							|| annex.getFitxerNom().equals("Asiento.xml"))
						processarAnnexSistra(anotacio, annex);
				}
			}
		} catch (Exception ex) {
			try {
				if (isExpedientFinal)
					pluginHelper.arxiuExpedientTemporalTancar(anotacio);
				else
					eliminarContingutExistent(anotacio.getExpedientArxiuUuid());
			} catch (Exception ex2) {
				logger.error(
						"Error al eliminar o tancar l'expedient creat l'arxiu digital",
						ex2);
			}
			throw new BustiaServiceException(
					"Error al processar els annexos de l'anotació de registre",
					ex);
		}
	}
	
	
	/*
	 * Mètode privat per obrir el document annex de tipus sistra i extreure'n
	 * informació per a l'anotació de registre. La informació que es pot extreure
	 * depén del document:
	 * - Asiento.xml: ASIENTO_REGISTRAL.DATOS_ASUNTO.IDENTIFICADOR_TRAMITE (VARCHAR2(20))
	 * - DatosPropios.xml: DATOS_PROPIOS.INSTRUCCIONES.IDENTIFICADOR_PROCEDIMIENTO (VARCHAR2(100))
	 * 
	 * @param anotacio 
	 * 			Anotació del registre
	 * @param annex
	 * 			Document annex amb el contingut per a llegir.
	 */
	private void processarAnnexSistra(RegistreEntity anotacio, RegistreAnnexEntity annex) {
		try {
			org.w3c.dom.Document doc = XmlHelper.getDocumentFromContent(annex.getFitxerContingut());
			if (annex.getFitxerNom().equals("DatosPropios.xml")) {
				String identificadorProcediment = XmlHelper.getNodeValue(doc.getDocumentElement(), "INSTRUCCIONES.IDENTIFICADOR_PROCEDIMIENTO");
				anotacio.updateIdentificadorProcedimentSistra(identificadorProcediment);
			} else if (annex.getFitxerNom().equals("Asiento.xml")) {
				String identificadorTramit = XmlHelper.getNodeValue(doc.getDocumentElement(), "DATOS_ASUNTO.IDENTIFICADOR_TRAMITE");
				anotacio.updateIdentificadorTramitSistra(identificadorTramit);
			}		
		} catch (Exception e) {
			logger.error(
					"Error processant l'annex per l'anotació amb regla backoffice SISTRA " + annex.getFitxerNom(),
					e);
		}
	}

	private void guardarDocumentAnnex(
			RegistreAnnexEntity annex,
			BustiaEntity bustia,
			ContingutArxiu expedientCreat) throws IOException {
		byte[] contingut = null;
		if (annex.getFitxerContingut() != null) {
			contingut = annex.getFitxerContingut();
		} else {
			Document arxiuDocument = pluginHelper.arxiuDocumentConsultar(
					bustia,
					annex.getFitxerArxiuUuid(),
					null,
					true);
			if (arxiuDocument.getContingut() != null) {
				contingut = arxiuDocument.getContingut().getContingut();
			} else {
				throw new ValidationException(
						"No s'ha trobat cap contingut per l'annex (" +
						"uuid=" + annex.getFitxerArxiuUuid() + ")");
			}
		}
		
		FitxerDto fitxer = new FitxerDto();
		fitxer.setNom(annex.getFitxerNom());
		fitxer.setContingut(contingut);
		fitxer.setContentType(annex.getFitxerTipusMime());
		fitxer.setTamany(contingut.length);

		if (pluginHelper.signaturaSignarAnnexos() && annex.getFirmes().size() == 0) {
			// Ripea signa amb el plugin de signatures els annexos sense firmes
			FirmaEntity firma = pluginHelper.signaturaSignar(annex);
			annex.getFirmes().add(firma);
		}

		String uuidDocument = pluginHelper.arxiuDocumentAnnexCrear(annex, bustia, fitxer, expedientCreat);
		annex.updateFitxerArxiuUuid(uuidDocument);
	}

	private void eliminarContingutExistent(
			String expedientUuid) {
		pluginHelper.arxiuExpedientEsborrarPerUuid(expedientUuid);
	}
	
	
	@Transactional
	@Override
	public RegistreAnotacioDto marcarLlegida(
			Long entitatId,
			Long contingutId,
			Long registreId) {
		logger.debug("Marcan com a llegida l'anotació de registre ("
				+ "entitatId=" + entitatId + ", "
				+ "contingutId=" + contingutId + ", "
				+ "registreId=" + registreId + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				true,
				false,
				false);
		ContingutEntity contingut = entityComprovarHelper.comprovarContingut(
				entitat,
				contingutId,
				null);
		if (contingut instanceof BustiaEntity) {
			entityComprovarHelper.comprovarBustia(
					entitat,
					contingutId,
					true);
		} else {
			// Comprova l'accés al path del contenidor pare
			contingutHelper.comprovarPermisosPathContingut(
					contingut,
					true,
					false,
					false,
					true);
		}
		RegistreEntity registre = registreRepository.findByPareAndId(
					contingut,
					registreId);
		registre.updateLlegida(true);
		
		return (RegistreAnotacioDto) contingutHelper.toContingutDto(
				registre);		
	}
	
	
	
	private String obtenirJustificantNom(Document document) {
		String fileName = "";
		String fileExtension = "";
		
		if (document.getContingut() != null) { 
			if (document.getContingut().getTipusMime() != null)
				fileExtension = document.getContingut().getTipusMime();
			
			if (document.getContingut().getArxiuNom() != null && !document.getContingut().getArxiuNom().isEmpty()) {
				fileName = document.getContingut().getArxiuNom();
				fileExtension = document.getContingut().getTipusMime();
			} else {
				fileName = document.getNom();
			}
		} else {
			fileName = document.getNom();
		}
		
		String fragment = "";
    	if (fileName.length() > 4)
    		fragment = fileName.substring(fileName.length() -5);
    	
    	if (fragment.contains("."))
    		return fileName;
    	
    	if (!fileExtension.isEmpty()) {
    		if (fileExtension.contains("/")) {
    			fileName += ("." + fileExtension.split("/")[1]);
    		} else if (fileExtension.contains(".")) {
    			fileName += fileExtension;
    		} else {
    			fileName += "." + fileExtension;
    		}
    	}
    	
		return fileName;
	}
	

	private static final Logger logger = LoggerFactory.getLogger(RegistreServiceImpl.class);

}
