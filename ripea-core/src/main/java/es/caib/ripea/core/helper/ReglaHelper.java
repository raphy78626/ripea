/**
 * 
 */
package es.caib.ripea.core.helper;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.namespace.QName;

import org.apache.axis.encoding.Base64;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import es.caib.ripea.core.api.dto.BackofficeTipusEnumDto;
import es.caib.ripea.core.api.dto.LogTipusEnumDto;
import es.caib.ripea.core.api.dto.ReglaTipusEnumDto;
import es.caib.ripea.core.api.exception.ScheduledTaskException;
import es.caib.ripea.core.api.registre.RegistreAnotacio;
import es.caib.ripea.core.api.registre.RegistreProcesEstatEnum;
import es.caib.ripea.core.api.service.bantel.wsClient.v2.BantelFacadeWsClient;
import es.caib.ripea.core.api.service.ws.RipeaBackofficeResultatProces;
import es.caib.ripea.core.api.service.ws.RipeaBackofficeWsService;
import es.caib.ripea.core.api.service.bantel.wsClient.v2.BantelFacadeException;
import es.caib.ripea.core.api.service.bantel.wsClient.v2.model.ReferenciaEntrada;
import es.caib.ripea.core.api.service.bantel.wsClient.v2.model.ReferenciasEntrada;
import es.caib.ripea.core.entity.BustiaEntity;
import es.caib.ripea.core.entity.ContingutMovimentEntity;
import es.caib.ripea.core.entity.EntitatEntity;
import es.caib.ripea.core.entity.ExpedientEntity;
import es.caib.ripea.core.entity.RegistreEntity;
import es.caib.ripea.core.entity.ReglaEntity;
import es.caib.ripea.core.repository.ExpedientRepository;
import es.caib.ripea.core.repository.RegistreRepository;
import es.caib.ripea.core.repository.ReglaRepository;

/**
 * Mètodes comuns per a aplicar regles.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@Component
public class ReglaHelper {

	@Resource
	private ReglaRepository reglaRepository;
	@Resource
	private RegistreRepository registreRepository;
	@Resource
	private ExpedientRepository expedientRepository;

	@Resource
	private ContingutHelper contingutHelper;
	@Resource
	private ContingutLogHelper contingutLogHelper;
	@Resource
	private RegistreHelper registreHelper;
	@Resource
	private BustiaHelper bustiaHelper;
	@Resource
	private EmailHelper emailHelper;

	private final static String CLAU_XIFRAT = "3çS)ZX!3a94_*?S2";



	public ReglaEntity findAplicable(
			EntitatEntity entitat,
			String unitatAdministrativa,
			RegistreAnotacio anotacio) {
		List<ReglaEntity> regles = reglaRepository.findByEntitatAndActivaTrueOrderByOrdreAsc(entitat);
		ReglaEntity reglaAplicable = null;
		for (ReglaEntity regla: regles) {
			if (regla.getUnitatCodi() == null || regla.getUnitatCodi().equals(unitatAdministrativa)) {
				if (anotacio.getAssumpteCodi() != null && anotacio.getAssumpteCodi().equals(regla.getAssumpteCodi())) {
					reglaAplicable = regla;
					break;
				}
			}
		}
		return reglaAplicable;
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void aplicar(
			Long pendentId) {
		RegistreEntity pendent = registreRepository.findOne(pendentId);
		BustiaEntity pendentBustia = null;
		if (pendent.getPare() instanceof BustiaEntity) {
			pendentBustia = (BustiaEntity)pendent.getPare();
		}
		ReglaEntity regla = pendent.getRegla();
		String error = null;
		try {
			switch (regla.getTipus()) {
			case BACKOFFICE:
				if (BackofficeTipusEnumDto.SISTRA.equals(regla.getBackofficeTipus())) {
					// SISTRA
					
					BantelFacadeWsClient backofficeSistraClient = new WsClientHelper<BantelFacadeWsClient>().generarClientWs(
							getClass().getResource("/es/caib/ripea/core/service/ws/backofficeSistra/BantelFacade.wsdl"),
							regla.getBackofficeUrl(),
							new QName(
									"urn:es:caib:bantel:ws:v2:services",
									"BantelFacadeService"),
							regla.getBackofficeUsuari(),
							regla.getBackofficeContrasenya(),
							null,
							BantelFacadeWsClient.class);
					// Crea la llista de referències d'entrada
					ReferenciasEntrada referenciesEntrades = new ReferenciasEntrada();
					ReferenciaEntrada referenciaEntrada = new ReferenciaEntrada();
					referenciaEntrada.setNumeroEntrada(pendent.getIdentificador());
					referenciaEntrada.setClaveAcceso(ReglaHelper.encrypt(pendent.getIdentificador()));	
					referenciesEntrades.getReferenciaEntrada().add(referenciaEntrada);
					// Invoca el backoffice sistra
					try {
						backofficeSistraClient.avisoEntradas(referenciesEntrades);
					} catch (BantelFacadeException bfe) {
						error = "[" + bfe.getFaultInfo() + "] " + bfe.getLocalizedMessage();
					}
				} else {
					// RIPEA

					//System.out.println(">>> Processant anotacio de registre amb backoffice (id=" + pendent.getId() + ", identificador=" + pendent.getIdentificador() + ")");
					RipeaBackofficeWsService backofficeClient = new WsClientHelper<RipeaBackofficeWsService>().generarClientWs(
							getClass().getResource("/es/caib/ripea/core/service/ws/backoffice/RipeaBackoffice.wsdl"),
							regla.getBackofficeUrl(),
							new QName(
									"http://www.caib.es/ripea/ws/backoffice",
									"RipeaBackofficeService"),
							regla.getBackofficeUsuari(),
							regla.getBackofficeContrasenya(),
							null,
							RipeaBackofficeWsService.class);
					RipeaBackofficeResultatProces resultat = backofficeClient.processarAnotacio(
							registreHelper.fromRegistreEntity(pendent));
					if (resultat.isError()) {
						error = "[" + resultat.getErrorCodi() + "] " + resultat.getErrorDescripcio();
					}
				}
				break;
			case BUSTIA:
				//System.out.println(">>> Processant anotacio de registre movent a bústia (id=" + pendent.getId() + ", identificador=" + pendent.getIdentificador() + ")");
				ContingutMovimentEntity contingutMoviment = contingutHelper.ferIEnregistrarMoviment(
						pendent,
						regla.getBustia(),
						null);
				contingutLogHelper.log(
						pendent,
						LogTipusEnumDto.MOVIMENT,
						contingutMoviment,
						true,
						true);
				emailHelper.emailBustiaPendentContingut(
						regla.getBustia(),
						pendent,
						contingutMoviment);
				break;
			case EXP_CREAR:
				//System.out.println(">>> Processant anotacio de registre creant nou expedient (id=" + pendent.getId() + ", identificador=" + pendent.getIdentificador() + ")");
				ExpedientEntity expedientCreat = contingutHelper.crearNouExpedient(
						"Creat automàticament amb anotació " + pendent.getIdentificador(),
						regla.getMetaExpedient(),
						regla.getArxiu(),
						regla.getArxiu(),
						regla.getEntitat(),
						"1.0",
						regla.getArxiu().getUnitatCodi(),
						new Date(),
						null);
				contingutHelper.ferIEnregistrarMoviment(
						pendent,
						expedientCreat,
						null);
				break;
			case EXP_AFEGIR:
				//System.out.println(">>> Processant anotacio de registre afegint a un expedient existent (id=" + pendent.getId() + ", identificador=" + pendent.getIdentificador() + ", expedientNumero=" + pendent.getExpedientNumero() + ")");
				String expedientNumero = pendent.getExpedientNumero();
				int separadorIndex = expedientNumero.indexOf("/");
				if (separadorIndex == -1 || separadorIndex == 0 || separadorIndex == expedientNumero.length() - 1) {
					error = "El número d'expedient de l'anotació de registre \"" + expedientNumero + "\" no te el format correcte \"SEQ/ANY\"";
				} else {
					long sequencia = new Long(expedientNumero.substring(0, separadorIndex)).longValue();
					int any = new Integer(expedientNumero.substring(separadorIndex + 1)).intValue();
					ExpedientEntity expedientAfegir = expedientRepository.findByEntitatAndMetaNodeAndAnyAndSequencia(
							regla.getEntitat(),
							regla.getMetaExpedient(),
							any,
							sequencia);
					if (expedientAfegir != null) {
						contingutHelper.ferIEnregistrarMoviment(
								pendent,
								expedientAfegir,
								null);
					} else {
						error = "No s'ha trobat l'expedient \"" + expedientNumero + "\"";
					}
				}
				break;
			default:
				error = "Tipus de regla desconegut (" + regla.getTipus() + ")";
				break;
			}
		} catch (Exception ex) {
			Throwable t = ExceptionUtils.getRootCause(ex);
			if (t == null)
				t = ex.getCause();
			if (t == null)
				t = ex;
			error = ExceptionUtils.getStackTrace(t);
		}
		if (error != null) {
			throw new ScheduledTaskException(error);
		} else {
			if (pendentBustia != null) {
				bustiaHelper.evictElementsPendentsBustia(
						regla.getEntitat(),
						pendentBustia);
			}
			pendent.updateProces(
					new Date(),
					RegistreProcesEstatEnum.PROCESSAT, // (ReglaTipusEnumDto.BUSTIA.equals(regla.getTipus())) ? RegistreProcesEstatEnum.PROCESSAT_VISIBLE : RegistreProcesEstatEnum.PROCESSAT_OCULT,
					error);
			// Si la regla és del tipus backoffice marca com a esborrat el contingut una vegada processat
			if (ReglaTipusEnumDto.BACKOFFICE.equals(regla.getTipus())) {
				pendent.updateEsborrat(1);
			}
		}
	}

	@Transactional
	public void actualitzarEstatError(
			Long pendentId,
			String error) {
		RegistreEntity pendent = registreRepository.findOne(pendentId);
		ReglaEntity regla = pendent.getRegla();
		if (ReglaTipusEnumDto.BACKOFFICE.equals(regla.getTipus())) {
			Integer intentsRegla = regla.getBackofficeIntents();
			if (intentsRegla == null) {
				pendent.updateProces(
						new Date(),
						RegistreProcesEstatEnum.ERROR,
						error);
			} else {
				Integer intentsPendent = pendent.getProcesIntents();
				if (intentsPendent != null && intentsPendent.intValue() >= intentsRegla.intValue() - 1) {
					pendent.updateProces(
							new Date(),
							RegistreProcesEstatEnum.ERROR,
							error);
				} else {
					pendent.updateProces(
							new Date(),
							RegistreProcesEstatEnum.PENDENT,
							error);
				}
			}
		} else {
			pendent.updateProces(
					new Date(),
					RegistreProcesEstatEnum.ERROR,
					error);
		}
	}

	/** Mètode per encriptar un text per a generar la clau d'accès. */
	public static String encrypt(String input){
		   byte[] crypted = null;
		   try{
		     SecretKeySpec skey = new SecretKeySpec(CLAU_XIFRAT.getBytes(), "AES");
		       Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
		       cipher.init(Cipher.ENCRYPT_MODE, skey);
		       crypted = cipher.doFinal(input.getBytes());
		     }catch(Exception e){
		      System.out.println(e.toString());
		     }
		     return new String(Base64.encode(crypted));
		 }
	
}
