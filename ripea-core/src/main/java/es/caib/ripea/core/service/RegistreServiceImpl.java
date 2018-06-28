/**
 * 
 */
package es.caib.ripea.core.service;

import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.caib.plugins.arxiu.api.Document;
import es.caib.plugins.arxiu.api.DocumentContingut;
import es.caib.plugins.arxiu.api.DocumentMetadades;
import es.caib.plugins.arxiu.api.Firma;
import es.caib.plugins.arxiu.api.FirmaTipus;
import es.caib.ripea.core.api.dto.ApuntAccioModel;
import es.caib.ripea.core.api.dto.ApuntInteressatModel;
import es.caib.ripea.core.api.dto.ApuntMovimentModel;
import es.caib.ripea.core.api.dto.ArxiuFirmaDto;
import es.caib.ripea.core.api.dto.ArxiuFirmaTipusEnumDto;
import es.caib.ripea.core.api.dto.ContingutLogDto;
import es.caib.ripea.core.api.dto.ContingutMovimentDto;
import es.caib.ripea.core.api.dto.EntitatDto;
import es.caib.ripea.core.api.dto.FitxerDto;
import es.caib.ripea.core.api.dto.RegistreAnnexDetallDto;
import es.caib.ripea.core.api.dto.RegistreAnotacioDto;
import es.caib.ripea.core.api.exception.NotFoundException;
import es.caib.ripea.core.api.exception.ValidationException;
import es.caib.ripea.core.api.registre.RegistreInteressatTipusEnum;
import es.caib.ripea.core.api.registre.RegistreProcesEstatEnum;
import es.caib.ripea.core.api.registre.RegistreProcesEstatSistraEnum;
import es.caib.ripea.core.api.service.RegistreService;
import es.caib.ripea.core.entity.BustiaEntity;
import es.caib.ripea.core.entity.ContingutEntity;
import es.caib.ripea.core.entity.EntitatEntity;
import es.caib.ripea.core.entity.RegistreAnnexEntity;
import es.caib.ripea.core.entity.RegistreAnnexFirmaEntity;
import es.caib.ripea.core.entity.RegistreEntity;
import es.caib.ripea.core.entity.RegistreInteressatEntity;
import es.caib.ripea.core.helper.AlertaHelper;
import es.caib.ripea.core.helper.BustiaHelper;
import es.caib.ripea.core.helper.ContingutHelper;
import es.caib.ripea.core.helper.ContingutLogHelper;
import es.caib.ripea.core.helper.ConversioTipusHelper;
import es.caib.ripea.core.helper.EntityComprovarHelper;
import es.caib.ripea.core.helper.MessageHelper;
import es.caib.ripea.core.helper.PlantillaHelper;
import es.caib.ripea.core.helper.PluginHelper;
import es.caib.ripea.core.helper.PropertiesHelper;
import es.caib.ripea.core.helper.RegistreHelper;
import es.caib.ripea.core.helper.ReglaHelper;
import es.caib.ripea.core.repository.BustiaRepository;
import es.caib.ripea.core.repository.ExpedientRepository;
import es.caib.ripea.core.repository.RegistreAnnexRepository;
import es.caib.ripea.core.repository.RegistreRepository;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;

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

	@Override
	@Transactional
	@Async
	@Scheduled(fixedRateString = "60000")
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
						try {
							registreHelper.distribuirAnotacioPendent(pendent.getId());
						} catch (Exception e) {
							registreHelper.actualitzarEstatError(
									pendent.getId(), 
									e);
						}
					}
				} catch (Exception e) {
					alertaHelper.crearAlerta(
							messageHelper.getMessage(
									"alertes.segon.pla.aplicar.regles.backoffice.sistra.error",
									new Object[] {pendent.getId()}),
							e,
							pendent.getId());
				}
			}
		} else {
			logger.debug("No hi ha registres pendents de processar");
		}
	}
	
	@Value("${config:es.caib.ripea.tasca.dist.anotacio.asincrona}")
    private boolean isDistAsincEnabled;
	
	@Override
	@Transactional
	@Scheduled(fixedDelayString = "${config:es.caib.ripea.tasca.dist.anotacio.pendent.periode.execucio}")
	public void distribuirAnotacionsPendents() {
		
		if (isDistAsincEnabled) {
		
			logger.debug("Distribuïnt anotacions de registere pendents");
			try {
				String maxReintents = PropertiesHelper.getProperties().getProperty("es.caib.ripea.tasca.dist.anotacio.pendent.max.reintents");
				List<RegistreEntity> pendents = registreRepository.findPendentsDistribuir(Integer.parseInt(maxReintents));
				
				logger.debug("Distribuïnt " + pendents.size() + " anotacion pendents");
				if (!pendents.isEmpty()) {
					for (RegistreEntity pendent: pendents) {
						try {
							registreHelper.distribuirAnotacioPendent(pendent.getId());
						} catch (Exception e) {
							registreHelper.actualitzarEstatError(
									pendent.getId(), 
									e);
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
			
			try {
				registreHelper.distribuirAnotacioPendent(anotacio.getId());
				return true;
			} catch (Exception e) {
				registreHelper.actualitzarEstatError(
						anotacio.getId(), 
						e);
				return false;
			}
			
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
		if (RegistreProcesEstatEnum.PENDENT.equals(anotacio.getProcesEstat()) ||
			RegistreProcesEstatEnum.ERROR.equals(anotacio.getProcesEstat())) {
			try {
				registreHelper.distribuirAnotacioPendent(anotacio.getId());
				return true;
			} catch (Exception e) {
				registreHelper.actualitzarEstatError(
						anotacio.getId(), 
						e);
				return false;
			}
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
		document = pluginHelper.arxiuDocumentConsultar(annex.getRegistre(), annex.getFitxerArxiuUuid(), null, true, true);
		
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
				RegistreAnnexFirmaEntity firmaEntity = annex.getFirmes().get(indexFirma);
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
			RegistreAnnexDetallDto annex = conversioTipusHelper.convertir(
					annexEntity,
					RegistreAnnexDetallDto.class);
			if (annex.getFitxerArxiuUuid() != null && !annex.getFitxerArxiuUuid().isEmpty()) {
				Document document = pluginHelper.arxiuDocumentConsultar(
						contingut,
						annex.getFitxerArxiuUuid(),
						null,
						true);
				annex.setAmbDocument(true);
				
				if (document.getFirmes() != null && document.getFirmes().size() > 0) {
					List<ArxiuFirmaDto> firmes = registreHelper.convertirFirmesAnnexToArxiuFirmaDto(annexEntity, null);
					Iterator<Firma> it = document.getFirmes().iterator();
					
					int firmaIndex = 0;
					while (it.hasNext()) {
						Firma arxiuFirma = it.next();
						if (!FirmaTipus.CSV.equals(arxiuFirma.getTipus())) {
							ArxiuFirmaDto firma = firmes.get(firmaIndex);
							if (pluginHelper.isValidaSignaturaPluginActiu()) {
								byte[] documentContingut = document.getContingut().getContingut();
								byte[] firmaContingut = arxiuFirma.getContingut();
								if (	ArxiuFirmaTipusEnumDto.XADES_DET.equals(firma.getTipus()) ||
										ArxiuFirmaTipusEnumDto.CADES_DET.equals(firma.getTipus())) {
									firmaContingut = arxiuFirma.getContingut();
								}
								firma.setDetalls(
										pluginHelper.validaSignaturaObtenirDetalls(
												documentContingut,
												firmaContingut));
							}
							firmaIndex++;
						} else {
							it.remove();
						}
					}
					annex.setFirmes(firmes);
					annex.setAmbFirma(true);
 				}
				
			}
			annexos.add(annex);
		}
		return annexos;
	}

	@Transactional(readOnly = true)
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

	@SuppressWarnings("unused")
	@Transactional (readOnly = true)
	@Override
	public FitxerDto informeRegistre(Long entitatId, Long registreId) throws Exception {

		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				true,
				false,
				false);
		ContingutEntity contingut = entityComprovarHelper.comprovarContingut(
				entitat,
				registreId,
				null);
		
		InputStream in = getClass().getResourceAsStream("/es/caib/ripea/core/plantilles/plantilla_informe_apunt_v.odt");
		byte[] plantilla = IOUtils.toByteArray(in);
		
		Map<String, Object> model = getModelRegistre(registreId);
		
		EntitatDto entitatDto = conversioTipusHelper.convertir(
				entitat,
				EntitatDto.class);
		
		model.put("entitat", entitatDto);
		
		byte[] informe = PlantillaHelper.generatePDF(plantilla, false, model, true);
		
		FitxerDto fitxer = new FitxerDto();
		fitxer.setContingut(informe);
		fitxer.setContentType("application/pdf");
		fitxer.setNom("Informe registre.pdf");
		
		return fitxer;
	}
	
	private Map<String, Object> getModelRegistre(Long registreId) {
		
		Map<String, Object> model = new HashMap<String, Object>();
		
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm");
		RegistreEntity registreEntity = registreRepository.findOne(registreId);
		Map<String, String> registre = new HashMap<String, String>();
		registre.put("tipus", messageHelper.getMessage("registre.anotacio.tipus.enum." + registreEntity.getRegistreTipus().name()));
		registre.put("tipusAssumpte", registreEntity.getAssumpteTipusDescripcio() != null ? registreEntity.getAssumpteTipusDescripcio() : "" + 
									" (" + registreEntity.getAssumpteTipusCodi() + ")");
		registre.put("codiAssumpte", registreEntity.getAssumpteDescripcio() + " (" + registreEntity.getAssumpteCodi() + ")");
		registre.put("numero", registreEntity.getNumero());
		registre.put("data", df.format(registreEntity.getData()));
		registre.put("llibre", registreEntity.getLlibreDescripcio() + " (" + registreEntity.getLlibreCodi() + ")");
		registre.put("oficina", registreEntity.getOficinaDescripcio() + " (" + registreEntity.getOficinaCodi() + ")");
		registre.put("organ", registreEntity.getUnitatAdministrativaDescripcio() + " (" + registreEntity.getUnitatAdministrativa() + ")");
		registre.put("extracte", registreEntity.getExtracte());
		
		
//		List<InteressatDto> interessats = conversioTipusHelper.convertirList(
//				registreEntity.getInteressats(),
//				InteressatDto.class);
		List<ApuntInteressatModel> interessats = new ArrayList<ApuntInteressatModel>();
		List<ApuntAccioModel> accions = new ArrayList<ApuntAccioModel>();
		List<ApuntMovimentModel> moviments = new ArrayList<ApuntMovimentModel>();
		
		for (RegistreInteressatEntity registreInteressat: registreEntity.getInteressats()) {
			ApuntInteressatModel interessat = new ApuntInteressatModel();
			interessat.setTipus(messageHelper.getMessage("registre.interessat.tipus.enum." + registreInteressat.getTipus().name()));
			interessat.setDocument(registreInteressat.getDocumentNum());
			
			if (registreInteressat.getTipus().equals(RegistreInteressatTipusEnum.PERSONA_FIS)) {
				StringBuilder sb = new StringBuilder();
				if (registreInteressat.getNom() != null) {
					sb.append(registreInteressat.getNom());
				}
				if (registreInteressat.getLlinatge1() != null) {
					sb.append(" ");
					sb.append(registreInteressat.getLlinatge1());
					if (registreInteressat.getLlinatge2() != null) {
						sb.append(" ");
						sb.append(registreInteressat.getLlinatge2());
					}
				}
				interessat.setNomComplet(sb.toString());
			} else {
				interessat.setNomComplet(registreInteressat.getRaoSocial());
			}
			if (registreInteressat.getRepresentant() != null) {
				interessat.setRepresentantDocument(registreInteressat.getRepresentant().getDocumentNum());
			} else {
				interessat.setRepresentantDocument("");
			}
			interessats.add(interessat);
		}
		
		List<ContingutLogDto> contingutLogs = contingutLogHelper.findLogsContingut(registreEntity);
		for (ContingutLogDto contingutLog: contingutLogs) {
			ApuntAccioModel accio = new ApuntAccioModel();
			accio.setData(contingutLog.getCreatedDate());
			accio.setUsuari(contingutLog.getCreatedBy() != null ? contingutLog.getCreatedBy().getNom() : "");
			String strAccio = "";
			if (contingutLog.isSecundari()) {
				if (contingutLog.getObjecteLogTipus() != null) {
					strAccio = messageHelper.getMessage("log.tipus.enum." + contingutLog.getObjecteLogTipus().name()) + " ";
				}
				strAccio += messageHelper.getMessage("log.objecte.tipus.enum." + contingutLog.getObjecteTipus().name());
				strAccio += "#" + contingutLog.getObjecteId();
				accio.setAccio(strAccio);
			} else {
				strAccio = messageHelper.getMessage("log.tipus.enum." + contingutLog.getTipus().name());
			}
			accio.setAccio(strAccio);
			accio.setParam1(contingutLog.getParam1() != null ? contingutLog.getParam1() : "");
			accio.setParam2(contingutLog.getParam2() != null ? contingutLog.getParam2() : "");
			accions.add(accio);
		}
		
		List<ContingutMovimentDto> contingutMoviments = contingutLogHelper.findMovimentsContingut(registreEntity);
		for (ContingutMovimentDto contingutMoviment: contingutMoviments) {
			ApuntMovimentModel moviment = new ApuntMovimentModel();
			moviment.setData(contingutMoviment.getData());
			moviment.setUsuari(contingutMoviment.getRemitent().getNom());
			if (contingutMoviment.getOrigen() != null) {
				moviment.setOrigen(
						messageHelper.getMessage("contingut.tipus.enum." + contingutMoviment.getOrigen().getTipus().name()) + 
						"#" + contingutMoviment.getOrigen().getId());
			} else {
				moviment.setOrigen("");
			}
			moviment.setDesti(
					messageHelper.getMessage("contingut.tipus.enum." + contingutMoviment.getDesti().getTipus().name()) + 
					"#" + contingutMoviment.getDesti().getId());
			moviment.setComentari(contingutMoviment.getComentari() != null ? contingutMoviment.getComentari() : "");
			moviments.add(moviment);
		}
		
		model.put("registre", registre);
		
		model.put("interessats", interessats);
		model.put("accions", accions);
		model.put("moviments", moviments);
		
		// Interessats
		FieldsMetadata metadataInteressats = new FieldsMetadata();
		metadataInteressats.addFieldAsList("interessats.tipus");
		metadataInteressats.addFieldAsList("interessats.document");
		metadataInteressats.addFieldAsList("interessats.nomComplet");
		metadataInteressats.addFieldAsList("interessats.representantDocument");
		model.put("interessatsMetadata", metadataInteressats);
				
		// Accions
		FieldsMetadata metadataAccions = new FieldsMetadata();
		metadataAccions.addFieldAsList("accions.data");
		metadataAccions.addFieldAsList("accions.usuari");
		metadataAccions.addFieldAsList("accions.accio");
		metadataAccions.addFieldAsList("accions.param1");
		metadataAccions.addFieldAsList("accions.param2");
		model.put("accionsMetadata", metadataAccions);
		
		// Moviments
		FieldsMetadata metadataMoviments = new FieldsMetadata();
		metadataMoviments.addFieldAsList("moviments.data");
		metadataMoviments.addFieldAsList("moviments.usuari");
		metadataMoviments.addFieldAsList("moviments.origen");
		metadataMoviments.addFieldAsList("moviments.desti");
		metadataMoviments.addFieldAsList("moviments.comentari");
		model.put("movimentsMetadata", metadataMoviments);
		
		return model;
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
