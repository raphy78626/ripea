/**
 * 
 */
package es.caib.ripea.core.helper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.xml.datatype.XMLGregorianCalendar;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import es.caib.plugins.arxiu.api.Carpeta;
import es.caib.plugins.arxiu.api.ContingutArxiu;
import es.caib.plugins.arxiu.api.ContingutOrigen;
import es.caib.plugins.arxiu.api.Document;
import es.caib.plugins.arxiu.api.DocumentContingut;
import es.caib.plugins.arxiu.api.DocumentEstat;
import es.caib.plugins.arxiu.api.DocumentEstatElaboracio;
import es.caib.plugins.arxiu.api.DocumentExtensio;
import es.caib.plugins.arxiu.api.DocumentFormat;
import es.caib.plugins.arxiu.api.DocumentMetadades;
import es.caib.plugins.arxiu.api.DocumentTipus;
import es.caib.plugins.arxiu.api.Expedient;
import es.caib.plugins.arxiu.api.ExpedientEstat;
import es.caib.plugins.arxiu.api.ExpedientMetadades;
import es.caib.plugins.arxiu.api.Firma;
import es.caib.plugins.arxiu.api.FirmaPerfil;
import es.caib.plugins.arxiu.api.FirmaTipus;
import es.caib.plugins.arxiu.api.IArxiuPlugin;
import es.caib.ripea.core.api.dto.ArbreDto;
import es.caib.ripea.core.api.dto.ArbreNodeDto;
import es.caib.ripea.core.api.dto.ComunitatDto;
import es.caib.ripea.core.api.dto.DocumentEstatEnumDto;
import es.caib.ripea.core.api.dto.DocumentNtiEstadoElaboracionEnumDto;
import es.caib.ripea.core.api.dto.DocumentNtiTipoDocumentalEnumDto;
import es.caib.ripea.core.api.dto.DocumentNtiTipoFirmaEnumDto;
import es.caib.ripea.core.api.dto.DocumentTipusEnumDto;
import es.caib.ripea.core.api.dto.ExpedientEstatEnumDto;
import es.caib.ripea.core.api.dto.FitxerDto;
import es.caib.ripea.core.api.dto.IntegracioAccioTipusEnumDto;
import es.caib.ripea.core.api.dto.InteressatDocumentTipusEnumDto;
import es.caib.ripea.core.api.dto.InteressatIdiomaEnumDto;
import es.caib.ripea.core.api.dto.LocalitatDto;
import es.caib.ripea.core.api.dto.MetaDocumentFirmaFluxTipusEnumDto;
import es.caib.ripea.core.api.dto.NivellAdministracioDto;
import es.caib.ripea.core.api.dto.NtiOrigenEnumDto;
import es.caib.ripea.core.api.dto.PaisDto;
import es.caib.ripea.core.api.dto.PortafirmesDocumentTipusDto;
import es.caib.ripea.core.api.dto.ProvinciaDto;
import es.caib.ripea.core.api.dto.ProvinciaRw3Dto;
import es.caib.ripea.core.api.dto.TipusViaDto;
import es.caib.ripea.core.api.dto.UnitatOrganitzativaD3Dto;
import es.caib.ripea.core.api.dto.UnitatOrganitzativaDto;
import es.caib.ripea.core.api.exception.SistemaExternException;
import es.caib.ripea.core.api.exception.ValidationException;
import es.caib.ripea.core.entity.CarpetaEntity;
import es.caib.ripea.core.entity.ContingutEntity;
import es.caib.ripea.core.entity.DocumentEntity;
import es.caib.ripea.core.entity.DocumentPortafirmesEntity;
import es.caib.ripea.core.entity.EscriptoriEntity;
import es.caib.ripea.core.entity.ExpedientEntity;
import es.caib.ripea.core.entity.InteressatAdministracioEntity;
import es.caib.ripea.core.entity.InteressatEntity;
import es.caib.ripea.core.entity.InteressatPersonaFisicaEntity;
import es.caib.ripea.core.entity.InteressatPersonaJuridicaEntity;
import es.caib.ripea.core.entity.MetaExpedientEntity;
import es.caib.ripea.plugin.ciutada.CiutadaDocument;
import es.caib.ripea.plugin.ciutada.CiutadaExpedientInformacio;
import es.caib.ripea.plugin.ciutada.CiutadaNotificacioEstat;
import es.caib.ripea.plugin.ciutada.CiutadaNotificacioResultat;
import es.caib.ripea.plugin.ciutada.CiutadaPersona;
import es.caib.ripea.plugin.ciutada.CiutadaPlugin;
import es.caib.ripea.plugin.conversio.ConversioArxiu;
import es.caib.ripea.plugin.conversio.ConversioPlugin;
import es.caib.ripea.plugin.dadesext.Comunitat;
import es.caib.ripea.plugin.dadesext.DadesExternesPlugin;
import es.caib.ripea.plugin.dadesext.Municipi;
import es.caib.ripea.plugin.dadesext.Pais;
import es.caib.ripea.plugin.dadesext.Provincia;
import es.caib.ripea.plugin.notificacio.NotificacioDocument;
import es.caib.ripea.plugin.notificacio.NotificacioEnviament;
import es.caib.ripea.plugin.notificacio.NotificacioEnviamentTipusEnum;
import es.caib.ripea.plugin.notificacio.NotificacioInformacioEnviament;
import es.caib.ripea.plugin.notificacio.NotificacioPagadorCie;
import es.caib.ripea.plugin.notificacio.NotificacioPagadorPostal;
import es.caib.ripea.plugin.notificacio.NotificacioParametresSeu;
import es.caib.ripea.plugin.notificacio.NotificacioPlugin;
import es.caib.ripea.plugin.portafirmes.PortafirmesDocument;
import es.caib.ripea.plugin.portafirmes.PortafirmesDocumentTipus;
import es.caib.ripea.plugin.portafirmes.PortafirmesFluxBloc;
import es.caib.ripea.plugin.portafirmes.PortafirmesPlugin;
import es.caib.ripea.plugin.portafirmes.PortafirmesPrioritatEnum;
import es.caib.ripea.plugin.registre.RegistreAnotacioResposta;
import es.caib.ripea.plugin.registre.RegistrePlugin;
import es.caib.ripea.plugin.unitat.UnitatOrganitzativa;
import es.caib.ripea.plugin.unitat.UnitatsOrganitzativesPlugin;
import es.caib.ripea.plugin.usuari.DadesUsuari;
import es.caib.ripea.plugin.usuari.DadesUsuariPlugin;

/**
 * Helper per a interactuar amb els plugins.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@Component
public class PluginHelper {

	private DadesUsuariPlugin dadesUsuariPlugin;
	private UnitatsOrganitzativesPlugin unitatsOrganitzativesPlugin;
	private PortafirmesPlugin portafirmesPlugin;
	private ConversioPlugin conversioPlugin;
	private RegistrePlugin registrePlugin;
	private CiutadaPlugin ciutadaPlugin;
	private DadesExternesPlugin dadesExternesPlugin;
	private IArxiuPlugin arxiuPlugin;
	
	private NotificacioPlugin notificacioPlugin;

	@Resource
	private ConversioTipusHelper conversioTipusHelper;
	@Resource
	private IntegracioHelper integracioHelper;
	@Resource
	private DocumentHelper documentHelper;

	private static final Pattern MOBIL_PATTERN = Pattern.compile("(\\+34|0034|34)?[ -]*(6|7)([0-9]){2}[ -]?(([0-9]){2}[ -]?([0-9]){2}[ -]?([0-9]){2}|([0-9]){3}[ -]?([0-9]){3})");


	public DadesUsuari dadesUsuariFindAmbCodi(
			String usuariCodi) {
		String accioDescripcio = "Consulta d'usuari amb codi";
		Map<String, String> accioParams = new HashMap<String, String>();
		accioParams.put("codi", usuariCodi);
		long t0 = System.currentTimeMillis();
		try {
			DadesUsuari dadesUsuari = getDadesUsuariPlugin().findAmbCodi(
					usuariCodi);
			integracioHelper.addAccioOk(
					IntegracioHelper.INTCODI_USUARIS,
					accioDescripcio,
					accioParams,
					IntegracioAccioTipusEnumDto.ENVIAMENT,
					System.currentTimeMillis() - t0);
			return dadesUsuari;
		} catch (Exception ex) {
			String errorDescripcio = "Error al accedir al plugin de dades d'usuari";
			integracioHelper.addAccioError(
					IntegracioHelper.INTCODI_USUARIS,
					accioDescripcio,
					accioParams,
					IntegracioAccioTipusEnumDto.ENVIAMENT,
					System.currentTimeMillis() - t0,
					errorDescripcio,
					ex);
			throw new SistemaExternException(
					IntegracioHelper.INTCODI_USUARIS,
					errorDescripcio,
					ex);
		}
	}
	public List<DadesUsuari> dadesUsuariFindAmbGrup(
			String grupCodi) {
		String accioDescripcio = "Consulta d'usuaris d'un grup";
		Map<String, String> accioParams = new HashMap<String, String>();
		accioParams.put("grup", grupCodi);
		long t0 = System.currentTimeMillis();
		try {
			List<DadesUsuari> dadesUsuari = getDadesUsuariPlugin().findAmbGrup(
					grupCodi);
			integracioHelper.addAccioOk(
					IntegracioHelper.INTCODI_USUARIS,
					accioDescripcio,
					accioParams,
					IntegracioAccioTipusEnumDto.ENVIAMENT,
					System.currentTimeMillis() - t0);
			return dadesUsuari;
		} catch (Exception ex) {
			String errorDescripcio = "Error al accedir al plugin de dades d'usuari";
			integracioHelper.addAccioError(
					IntegracioHelper.INTCODI_USUARIS,
					accioDescripcio,
					accioParams,
					IntegracioAccioTipusEnumDto.ENVIAMENT,
					System.currentTimeMillis() - t0,
					errorDescripcio,
					ex);
			throw new SistemaExternException(
					IntegracioHelper.INTCODI_USUARIS,
					errorDescripcio,
					ex);
		}
	}
	
	public List<UnitatOrganitzativaDto> unitatsOrganitzativesFindListByPare(
			String pareCodi) {
		String accioDescripcio = "Consulta llista d'unitats donat un pare";
		Map<String, String> accioParams = new HashMap<String, String>();
		accioParams.put("unitatPare", pareCodi);
		long t0 = System.currentTimeMillis();
		try {
			List<UnitatOrganitzativa> resposta = getUnitatsOrganitzativesPlugin().findAmbPare(
					pareCodi);
			return conversioTipusHelper.convertirList(resposta, UnitatOrganitzativaDto.class);
		} catch (Exception ex) {
			String errorDescripcio = "Error al accedir al plugin d'unitats organitzatives";
			integracioHelper.addAccioError(
					IntegracioHelper.INTCODI_UNITATS,
					accioDescripcio,
					accioParams,
					IntegracioAccioTipusEnumDto.ENVIAMENT,
					System.currentTimeMillis() - t0,
					errorDescripcio,
					ex);
			throw new SistemaExternException(
					IntegracioHelper.INTCODI_UNITATS,
					errorDescripcio,
					ex);
		}
	}

	public ArbreDto<UnitatOrganitzativaDto> unitatsOrganitzativesFindArbreByPare(
			String pareCodi) {
		String accioDescripcio = "Consulta de l'arbre d'unitats donat un pare";
		Map<String, String> accioParams = new HashMap<String, String>();
		accioParams.put("unitatPare", pareCodi);
		long t0 = System.currentTimeMillis();
		try {
			List<UnitatOrganitzativa> unitatsOrganitzatives = getUnitatsOrganitzativesPlugin().findAmbPare(
					pareCodi);
			ArbreDto<UnitatOrganitzativaDto> resposta = new ArbreDto<UnitatOrganitzativaDto>(false);
			// Cerca l'unitat organitzativa arrel
			UnitatOrganitzativa unitatOrganitzativaArrel = null;
			for (UnitatOrganitzativa unitatOrganitzativa: unitatsOrganitzatives) {
				if (pareCodi.equalsIgnoreCase(unitatOrganitzativa.getCodi())) {
					unitatOrganitzativaArrel = unitatOrganitzativa;
					break;
				}
			}
			if (unitatOrganitzativaArrel != null) {
				// Omple l'arbre d'unitats organitzatives
				resposta.setArrel(
						getNodeArbreUnitatsOrganitzatives(
								unitatOrganitzativaArrel,
								unitatsOrganitzatives,
								null));
				integracioHelper.addAccioOk(
						IntegracioHelper.INTCODI_UNITATS,
						accioDescripcio,
						accioParams,
						IntegracioAccioTipusEnumDto.ENVIAMENT,
						System.currentTimeMillis() - t0);
				return resposta;
			} else {
				String errorMissatge = "No s'ha trobat la unitat organitzativa arrel (codi=" + pareCodi + ")";
				integracioHelper.addAccioError(
						IntegracioHelper.INTCODI_UNITATS,
						accioDescripcio,
						accioParams,
						IntegracioAccioTipusEnumDto.ENVIAMENT,
						System.currentTimeMillis() - t0,
						errorMissatge);
				throw new SistemaExternException(
						IntegracioHelper.INTCODI_UNITATS,
						errorMissatge);
			}
		} catch (Exception ex) {
			String errorDescripcio = "Error al accedir al plugin d'unitats organitzatives";
			integracioHelper.addAccioError(
					IntegracioHelper.INTCODI_UNITATS,
					accioDescripcio,
					accioParams,
					IntegracioAccioTipusEnumDto.ENVIAMENT,
					System.currentTimeMillis() - t0,
					errorDescripcio,
					ex);
			throw new SistemaExternException(
					IntegracioHelper.INTCODI_UNITATS,
					errorDescripcio,
					ex);
		}
	}
	public UnitatOrganitzativaDto unitatsOrganitzativesFindByCodi(
			String codi) {
		String accioDescripcio = "Consulta d'unitat organitzativa donat el seu codi";
		Map<String, String> accioParams = new HashMap<String, String>();
		accioParams.put("codi", codi);
		long t0 = System.currentTimeMillis();
		try {
			UnitatOrganitzativaDto unitatOrganitzativa = conversioTipusHelper.convertir(
					getUnitatsOrganitzativesPlugin().findAmbCodi(codi),
					UnitatOrganitzativaDto.class);
			integracioHelper.addAccioOk(
					IntegracioHelper.INTCODI_UNITATS,
					accioDescripcio,
					accioParams,
					IntegracioAccioTipusEnumDto.ENVIAMENT,
					System.currentTimeMillis() - t0);
			return unitatOrganitzativa;
		} catch (Exception ex) {
			String errorDescripcio = "Error al accedir al plugin d'unitats organitzatives";
			integracioHelper.addAccioError(
					IntegracioHelper.INTCODI_UNITATS,
					accioDescripcio,
					accioParams,
					IntegracioAccioTipusEnumDto.ENVIAMENT,
					System.currentTimeMillis() - t0,
					errorDescripcio,
					ex);
			throw new SistemaExternException(
					IntegracioHelper.INTCODI_UNITATS,
					errorDescripcio,
					ex);
		}
	}
	public List<UnitatOrganitzativaD3Dto> unitatsOrganitzativesD3FindByFiltre(
			String codiUnitat, 
			String denominacioUnitat,
			String codiNivellAdministracio, 
			String codiComunitat, 
			String codiProvincia, 
			String codiLocalitat, 
			Boolean esUnitatArrel) {
		String accioDescripcio = "Consulta d'unitats organitzatives donat un filtre";
		Map<String, String> accioParams = new HashMap<String, String>();
		accioParams.put("codiUnitat", codiUnitat);
		accioParams.put("denominacioUnitat", denominacioUnitat);
		accioParams.put("codiNivellAdministracio", codiNivellAdministracio);
		accioParams.put("codiComunitat", codiComunitat);
		accioParams.put("codiProvincia", codiProvincia);
		accioParams.put("codiLocalitat", codiLocalitat);
		accioParams.put("esUnitatArrel", esUnitatArrel == null ? "null" : esUnitatArrel.toString() );
		long t0 = System.currentTimeMillis();
		try {
			List<UnitatOrganitzativaD3Dto> unitatsOrganitzatives = conversioTipusHelper.convertirList(
					getUnitatsOrganitzativesPlugin().cercaUnitatsD3(
							codiUnitat, 
							denominacioUnitat, 
							toLongValue(codiNivellAdministracio), 
							toLongValue(codiComunitat), 
							false, 
							esUnitatArrel, 
							toLongValue(codiProvincia), 
							codiLocalitat),
					UnitatOrganitzativaD3Dto.class);
			integracioHelper.addAccioOk(
					IntegracioHelper.INTCODI_UNITATS,
					accioDescripcio,
					accioParams,
					IntegracioAccioTipusEnumDto.ENVIAMENT,
					System.currentTimeMillis() - t0);
			return unitatsOrganitzatives;
		} catch (Exception ex) {
			String errorDescripcio = "Error al accedir al realitzar la cerca de unitats organitzatives";
			integracioHelper.addAccioError(
					IntegracioHelper.INTCODI_UNITATS,
					accioDescripcio,
					accioParams,
					IntegracioAccioTipusEnumDto.ENVIAMENT,
					System.currentTimeMillis() - t0,
					errorDescripcio,
					ex);
			throw new SistemaExternException(
					IntegracioHelper.INTCODI_UNITATS,
					errorDescripcio,
					ex);
		}
	}
	
	public List<UnitatOrganitzativaDto> unitatsOrganitzativesFindByFiltre(
			String codiUnitat, 
			String denominacioUnitat,
			String codiNivellAdministracio, 
			String codiComunitat, 
			String codiProvincia, 
			String codiLocalitat, 
			Boolean esUnitatArrel) {
		String accioDescripcio = "Consulta d'unitats organitzatives donat un filtre";
		Map<String, String> accioParams = new HashMap<String, String>();
		accioParams.put("codiUnitat", codiUnitat);
		accioParams.put("denominacioUnitat", denominacioUnitat);
		accioParams.put("codiNivellAdministracio", codiNivellAdministracio);
		accioParams.put("codiComunitat", codiComunitat);
		accioParams.put("codiProvincia", codiProvincia);
		accioParams.put("codiLocalitat", codiLocalitat);
		accioParams.put("esUnitatArrel", esUnitatArrel == null ? "null" : esUnitatArrel.toString() );
		long t0 = System.currentTimeMillis();
		try {
			List<UnitatOrganitzativaDto> unitatsOrganitzatives = conversioTipusHelper.convertirList(
					getUnitatsOrganitzativesPlugin().cercaUnitats(
							codiUnitat, 
							denominacioUnitat, 
							toLongValue(codiNivellAdministracio), 
							toLongValue(codiComunitat), 
							false, 
							esUnitatArrel, 
							toLongValue(codiProvincia), 
							codiLocalitat),
					UnitatOrganitzativaDto.class);
			integracioHelper.addAccioOk(
					IntegracioHelper.INTCODI_UNITATS,
					accioDescripcio,
					accioParams,
					IntegracioAccioTipusEnumDto.ENVIAMENT,
					System.currentTimeMillis() - t0);
			return unitatsOrganitzatives;
		} catch (Exception ex) {
			String errorDescripcio = "Error al accedir al realitzar la cerca de unitats organitzatives";
			integracioHelper.addAccioError(
					IntegracioHelper.INTCODI_UNITATS,
					accioDescripcio,
					accioParams,
					IntegracioAccioTipusEnumDto.ENVIAMENT,
					System.currentTimeMillis() - t0,
					errorDescripcio,
					ex);
			throw new SistemaExternException(
					IntegracioHelper.INTCODI_UNITATS,
					errorDescripcio,
					ex);
		}
	}
	
	public List<ProvinciaRw3Dto> findProvinciesPerComunitat(
			String codiComunitat) {
		String accioDescripcio = "Consulta de provincies per comunitat autònoma";
		Map<String, String> accioParams = new HashMap<String, String>();
		accioParams.put("codiComunitat", codiComunitat);
		long t0 = System.currentTimeMillis();
		try {
			List<ProvinciaRw3Dto> provincies = conversioTipusHelper.convertirList(
					getUnitatsOrganitzativesPlugin().getProvinciaPerComunitat(new Long(codiComunitat)),
					ProvinciaRw3Dto.class);
			integracioHelper.addAccioOk(
					IntegracioHelper.INTCODI_UNITATS,
					accioDescripcio,
					accioParams,
					IntegracioAccioTipusEnumDto.ENVIAMENT,
					System.currentTimeMillis() - t0);
			return provincies;
		} catch (Exception ex) {
			String errorDescripcio = "Error al accedir al realitzar la cerca de localitats";
			integracioHelper.addAccioError(
					IntegracioHelper.INTCODI_UNITATS,
					accioDescripcio,
					accioParams,
					IntegracioAccioTipusEnumDto.ENVIAMENT,
					System.currentTimeMillis() - t0,
					errorDescripcio,
					ex);
			throw new SistemaExternException(
					IntegracioHelper.INTCODI_UNITATS,
					errorDescripcio,
					ex);
		}
	}
	
	public List<LocalitatDto> findLocalitatsPerProvincia(
			String codiProvincia) {
		String accioDescripcio = "Consulta de localitats donada el codi de la provincia";
		Map<String, String> accioParams = new HashMap<String, String>();
		accioParams.put("codiProvincia", codiProvincia);
		long t0 = System.currentTimeMillis();
		try {
			List<LocalitatDto> localitats = conversioTipusHelper.convertirList(
					getUnitatsOrganitzativesPlugin().getLocalitatsPerProvincia(new Long(codiProvincia)),
					LocalitatDto.class);
			
			integracioHelper.addAccioOk(
					IntegracioHelper.INTCODI_UNITATS,
					accioDescripcio,
					accioParams,
					IntegracioAccioTipusEnumDto.ENVIAMENT,
					System.currentTimeMillis() - t0);
			return localitats;
		} catch (Exception ex) {
			String errorDescripcio = "Error al accedir al realitzar la cerca de localitats";
			integracioHelper.addAccioError(
					IntegracioHelper.INTCODI_UNITATS,
					accioDescripcio,
					accioParams,
					IntegracioAccioTipusEnumDto.ENVIAMENT,
					System.currentTimeMillis() - t0,
					errorDescripcio,
					ex);
			throw new SistemaExternException(
					IntegracioHelper.INTCODI_UNITATS,
					errorDescripcio,
					ex);
		}
	}
	
	public List<PaisDto> findPaisos() {
		String accioDescripcio = "Consulta paisos";
		Map<String, String> accioParams = new HashMap<String, String>();
		long t0 = System.currentTimeMillis();
		try {
			List<PaisDto> paisos = conversioTipusHelper.convertirList(
					getDadesExternesPlugin().paisFindAll(),
					PaisDto.class);
			integracioHelper.addAccioOk(
					IntegracioHelper.INTCODI_DADESEXT,
					accioDescripcio,
					accioParams,
					IntegracioAccioTipusEnumDto.RECEPCIO,
					System.currentTimeMillis() - t0);
			return paisos;
		} catch (Exception ex) {
			String errorDescripcio = "Error al accedir al plugin de dades externes";
			integracioHelper.addAccioError(
					IntegracioHelper.INTCODI_DADESEXT,
					accioDescripcio,
					accioParams,
					IntegracioAccioTipusEnumDto.RECEPCIO,
					System.currentTimeMillis() - t0,
					errorDescripcio,
					ex);
			throw new SistemaExternException(
					IntegracioHelper.INTCODI_DADESEXT,
					errorDescripcio,
					ex);
		}
	}
	public List<ComunitatDto> findComunitats() {
		String accioDescripcio = "Consulta comunitats";
		Map<String, String> accioParams = new HashMap<String, String>();
		long t0 = System.currentTimeMillis();
		try {
			List<ComunitatDto> comunitats = conversioTipusHelper.convertirList(
					getDadesExternesPlugin().comunitatFindAll(),
					ComunitatDto.class);
			integracioHelper.addAccioOk(
					IntegracioHelper.INTCODI_DADESEXT,
					accioDescripcio,
					accioParams,
					IntegracioAccioTipusEnumDto.RECEPCIO,
					System.currentTimeMillis() - t0);
			return comunitats;
		} catch (Exception ex) {
			String errorDescripcio = "Error al accedir al plugin de dades externes";
			integracioHelper.addAccioError(
					IntegracioHelper.INTCODI_DADESEXT,
					accioDescripcio,
					accioParams,
					IntegracioAccioTipusEnumDto.RECEPCIO,
					System.currentTimeMillis() - t0,
					errorDescripcio,
					ex);
			throw new SistemaExternException(
					IntegracioHelper.INTCODI_DADESEXT,
					errorDescripcio,
					ex);
		}
	}
	public List<ProvinciaDto> findProvincies() {
		String accioDescripcio = "Consulta provincies";
		Map<String, String> accioParams = new HashMap<String, String>();
		long t0 = System.currentTimeMillis();
		try {
			List<ProvinciaDto> provincies = conversioTipusHelper.convertirList(
					getDadesExternesPlugin().provinciaFindAll(),
					ProvinciaDto.class);
			integracioHelper.addAccioOk(
					IntegracioHelper.INTCODI_DADESEXT,
					accioDescripcio,
					accioParams,
					IntegracioAccioTipusEnumDto.RECEPCIO,
					System.currentTimeMillis() - t0);
			return provincies;
		} catch (Exception ex) {
			String errorDescripcio = "Error al accedir al plugin de dades externes";
			integracioHelper.addAccioError(
					IntegracioHelper.INTCODI_DADESEXT,
					accioDescripcio,
					accioParams,
					IntegracioAccioTipusEnumDto.RECEPCIO,
					System.currentTimeMillis() - t0,
					errorDescripcio,
					ex);
			throw new SistemaExternException(
					IntegracioHelper.INTCODI_DADESEXT,
					errorDescripcio,
					ex);
		}
	}
//	public List<MunicipiDto> findLocalitats() {
//		String accioDescripcio = "Consulta provincies";
//		Map<String, String> accioParams = new HashMap<String, String>();
//		long t0 = System.currentTimeMillis();
//		try {
//			List<MunicipiDto> localitats = conversioTipusHelper.convertirList(
//					getDadesExternesPlugin().findLocalitats(),
//					MunicipiDto.class);
//			integracioHelper.addAccioOk(
//					IntegracioHelper.INTCODI_DADESEXT,
//					accioDescripcio,
//					accioParams,
//					IntegracioAccioTipusEnumDto.RECEPCIO,
//					System.currentTimeMillis() - t0);
//			return localitats;
//		} catch (Exception ex) {
//			String errorDescripcio = "Error al accedir al plugin de dades externes";
//			integracioHelper.addAccioError(
//					IntegracioHelper.INTCODI_DADESEXT,
//					accioDescripcio,
//					accioParams,
//					IntegracioAccioTipusEnumDto.RECEPCIO,
//					System.currentTimeMillis() - t0,
//					errorDescripcio,
//					ex);
//			throw new SistemaExternException(
//					IntegracioHelper.INTCODI_DADESEXT,
//					errorDescripcio,
//					ex);
//		}
//	}
	public List<NivellAdministracioDto> findNivellAdministracio() {
		String accioDescripcio = "Consulta de nivells d'administració";
		Map<String, String> accioParams = new HashMap<String, String>();
		long t0 = System.currentTimeMillis();
		try {
			List<NivellAdministracioDto> nivellAdministracio = conversioTipusHelper.convertirList(
					getDadesExternesPlugin().nivellAdministracioFindAll(),
					NivellAdministracioDto.class);
			integracioHelper.addAccioOk(
					IntegracioHelper.INTCODI_DADESEXT,
					accioDescripcio,
					accioParams,
					IntegracioAccioTipusEnumDto.RECEPCIO,
					System.currentTimeMillis() - t0);
			return nivellAdministracio;
		} catch (Exception ex) {
			String errorDescripcio = "Error al accedir al plugin de dades externes";
			integracioHelper.addAccioError(
					IntegracioHelper.INTCODI_DADESEXT,
					accioDescripcio,
					accioParams,
					IntegracioAccioTipusEnumDto.RECEPCIO,
					System.currentTimeMillis() - t0,
					errorDescripcio,
					ex);
			throw new SistemaExternException(
					IntegracioHelper.INTCODI_DADESEXT,
					errorDescripcio,
					ex);
		}
	}
	public List<TipusViaDto> findTipusVia() {
		String accioDescripcio = "Consulta de tipus de via";
		Map<String, String> accioParams = new HashMap<String, String>();
		long t0 = System.currentTimeMillis();
		try {
			List<TipusViaDto> tipusVies = conversioTipusHelper.convertirList(
					getDadesExternesPlugin().tipusViaFindAll(),
					TipusViaDto.class);
			integracioHelper.addAccioOk(
					IntegracioHelper.INTCODI_DADESEXT,
					accioDescripcio,
					accioParams,
					IntegracioAccioTipusEnumDto.RECEPCIO,
					System.currentTimeMillis() - t0);
			return tipusVies;
		} catch (Exception ex) {
			String errorDescripcio = "Error al accedir al plugin de dades externes";
			integracioHelper.addAccioError(
					IntegracioHelper.INTCODI_DADESEXT,
					accioDescripcio,
					accioParams,
					IntegracioAccioTipusEnumDto.RECEPCIO,
					System.currentTimeMillis() - t0,
					errorDescripcio,
					ex);
			throw new SistemaExternException(
					IntegracioHelper.INTCODI_DADESEXT,
					errorDescripcio,
					ex);
		}
	}

	/*public boolean isGestioDocumentalPluginActiu() {
		return getGestioDocumentalPlugin() != null;
	}
	public String gestioDocumentalCreate(
			String arxiuNom,
			byte[] contingut) {
		String accioDescripcio = "Creació d'un arxiu";
		Map<String, String> accioParams = new HashMap<String, String>();
		accioParams.put("arxiuNom", arxiuNom);
		if (contingut != null)
			accioParams.put("contingut", new Integer(contingut.length).toString() + " bytes");
		else
			accioParams.put("contingut", "<null>");
		long t0 = System.currentTimeMillis();
		try {
			String gestioDocumentalId = getGestioDocumentalPlugin().create(
					new GestioDocumentalArxiu(
							arxiuNom,
							contingut));
			integracioHelper.addAccioOk(
					IntegracioHelper.INTCODI_GESDOC,
					accioDescripcio,
					accioParams,
					IntegracioAccioTipusEnumDto.ENVIAMENT,
					System.currentTimeMillis() - t0);
			return gestioDocumentalId;
		} catch (Exception ex) {
			String errorDescripcio = "Error al accedir al plugin de gestió documental";
			integracioHelper.addAccioError(
					IntegracioHelper.INTCODI_GESDOC,
					accioDescripcio,
					accioParams,
					IntegracioAccioTipusEnumDto.ENVIAMENT,
					System.currentTimeMillis() - t0,
					errorDescripcio,
					ex);
			throw new SistemaExternException(
					IntegracioHelper.INTCODI_GESDOC,
					errorDescripcio,
					ex);
		}
	}
	public void gestioDocumentalUpdate(
			String id,
			String arxiuNom,
			byte[] contingut) {
		String accioDescripcio = "Modificació d'un arxiu";
		Map<String, String> accioParams = new HashMap<String, String>();
		accioParams.put("id", id);
		accioParams.put("arxiuNom", arxiuNom);
		accioParams.put("contingut", new Integer(contingut.length).toString());
		long t0 = System.currentTimeMillis();
		try {
			getGestioDocumentalPlugin().update(
					id,
					new GestioDocumentalArxiu(
							arxiuNom,
							contingut));
			integracioHelper.addAccioOk(
					IntegracioHelper.INTCODI_GESDOC,
					accioDescripcio,
					accioParams,
					IntegracioAccioTipusEnumDto.ENVIAMENT,
					System.currentTimeMillis() - t0);
		} catch (Exception ex) {
			String errorDescripcio = "Error al accedir al plugin de gestió documental";
			integracioHelper.addAccioError(
					IntegracioHelper.INTCODI_GESDOC,
					accioDescripcio,
					accioParams,
					IntegracioAccioTipusEnumDto.ENVIAMENT,
					System.currentTimeMillis() - t0,
					errorDescripcio,
					ex);
			throw new SistemaExternException(
					IntegracioHelper.INTCODI_GESDOC,
					errorDescripcio,
					ex);
		}
	}
	public void gestioDocumentalDelete(
			String id) {
		String accioDescripcio = "Eliminació d'un arxiu";
		Map<String, String> accioParams = new HashMap<String, String>();
		accioParams.put("id", id);
		long t0 = System.currentTimeMillis();
		try {
			getGestioDocumentalPlugin().delete(
					id);
			integracioHelper.addAccioOk(
					IntegracioHelper.INTCODI_GESDOC,
					accioDescripcio,
					accioParams,
					IntegracioAccioTipusEnumDto.ENVIAMENT,
					System.currentTimeMillis() - t0);
		} catch (Exception ex) {
			String errorDescripcio = "Error al accedir al plugin de gestió documental";
			integracioHelper.addAccioError(
					IntegracioHelper.INTCODI_GESDOC,
					accioDescripcio,
					accioParams,
					IntegracioAccioTipusEnumDto.ENVIAMENT,
					System.currentTimeMillis() - t0,
					errorDescripcio,
					ex);
			throw new SistemaExternException(
					IntegracioHelper.INTCODI_GESDOC,
					errorDescripcio,
					ex);
		}
	}
	public FitxerDto gestioDocumentalGet(
			String id) {
		String accioDescripcio = "Obtenció d'un arxiu";
		Map<String, String> accioParams = new HashMap<String, String>();
		accioParams.put("id", id);
		long t0 = System.currentTimeMillis();
		try {
			GestioDocumentalArxiu gesdocArxiu = getGestioDocumentalPlugin().get(id);
			integracioHelper.addAccioOk(
					IntegracioHelper.INTCODI_GESDOC,
					accioDescripcio,
					accioParams,
					IntegracioAccioTipusEnumDto.ENVIAMENT,
					System.currentTimeMillis() - t0);
			FitxerDto fitxer = new FitxerDto();
			fitxer.setNom(gesdocArxiu.getFileName());
			fitxer.setContingut(gesdocArxiu.getContent());
			return fitxer;
		} catch (Exception ex) {
			String errorDescripcio = "Error al accedir al plugin de gestió documental";
			integracioHelper.addAccioError(
					IntegracioHelper.INTCODI_GESDOC,
					accioDescripcio,
					accioParams,
					IntegracioAccioTipusEnumDto.ENVIAMENT,
					System.currentTimeMillis() - t0,
					errorDescripcio,
					ex);
			throw new SistemaExternException(
					IntegracioHelper.INTCODI_GESDOC,
					errorDescripcio,
					ex);
		}
	}

	public String custodiaReservarUrl(
			DocumentEntity document) {
		if (document.getCustodiaUrl() != null)
			return document.getCustodiaUrl();
		String accioDescripcio = "Reservar URL";
		Map<String, String> accioParams = new HashMap<String, String>();
		accioParams.put(
				"documentId",
				document.getId().toString());
		long t0 = System.currentTimeMillis();
		try {
			String url = getCustodiaPlugin().reservarUrl(
					document.getId().toString());
			integracioHelper.addAccioOk(
					IntegracioHelper.INTCODI_CUSTODIA,
					accioDescripcio,
					accioParams,
					IntegracioAccioTipusEnumDto.ENVIAMENT,
					System.currentTimeMillis() - t0);
			return url;
		} catch (Exception ex) {
			String errorDescripcio = "Error al accedir al plugin de custòdia de documents";
			integracioHelper.addAccioError(
					IntegracioHelper.INTCODI_CUSTODIA,
					accioDescripcio,
					accioParams,
					IntegracioAccioTipusEnumDto.ENVIAMENT,
					System.currentTimeMillis() - t0,
					errorDescripcio,
					ex);
			throw new SistemaExternException(
					IntegracioHelper.INTCODI_CUSTODIA,
					errorDescripcio,
					ex);
		}
	}
	public String custodiaEnviarDocumentFirmat(
			DocumentEntity document,
			String custodiaTipus,
			String arxiuNom,
			byte[] arxiuContingut) {
		String accioDescripcio = "Custodiar document firmat";
		Map<String, String> accioParams = new HashMap<String, String>();
		accioParams.put(
				"documentId",
				document.getId().toString());
		accioParams.put(
				"arxiuNom",
				arxiuNom);
		accioParams.put(
				"arxiuTamany",
				new Integer(arxiuContingut.length).toString());
		long t0 = System.currentTimeMillis();
		try {
			getCustodiaPlugin().custodiarPdfFirmat(
					document.getId().toString(),
					custodiaTipus,
					arxiuNom,
					new ByteArrayInputStream(arxiuContingut));
			integracioHelper.addAccioOk(
					IntegracioHelper.INTCODI_CUSTODIA,
					accioDescripcio,
					accioParams,
					IntegracioAccioTipusEnumDto.ENVIAMENT,
					System.currentTimeMillis() - t0);
			return document.getId().toString();
		} catch (Exception ex) {
			String errorDescripcio = "Error al accedir al plugin de custòdia de documents";
			integracioHelper.addAccioError(
					IntegracioHelper.INTCODI_CUSTODIA,
					accioDescripcio,
					accioParams,
					IntegracioAccioTipusEnumDto.ENVIAMENT,
					System.currentTimeMillis() - t0,
					errorDescripcio,
					ex);
			throw new SistemaExternException(
					IntegracioHelper.INTCODI_CUSTODIA,
					errorDescripcio,
					ex);
		}
	}
	public void custodiaEsborrar(DocumentEntity document) {
		String accioDescripcio = "Esborrar document custodiat";
		Map<String, String> accioParams = new HashMap<String, String>();
		accioParams.put(
				"documentId",
				document.getId().toString());
		accioParams.put(
				"custodiaId",
				document.getCustodiaId());
		long t0 = System.currentTimeMillis();
		try {
			getCustodiaPlugin().esborrarDocumentCustodiat(
					document.getCustodiaId(),
					false);
			integracioHelper.addAccioOk(
					IntegracioHelper.INTCODI_CUSTODIA,
					accioDescripcio,
					accioParams,
					IntegracioAccioTipusEnumDto.ENVIAMENT,
					System.currentTimeMillis() - t0);
		} catch (Exception ex) {
			String errorDescripcio = "Error al accedir al plugin de custòdia de documents";
			integracioHelper.addAccioError(
					IntegracioHelper.INTCODI_CUSTODIA,
					accioDescripcio,
					accioParams,
					IntegracioAccioTipusEnumDto.ENVIAMENT,
					System.currentTimeMillis() - t0,
					errorDescripcio,
					ex);
			throw new SistemaExternException(
					IntegracioHelper.INTCODI_CUSTODIA,
					errorDescripcio,
					ex);
		}
	}*/

	public boolean isArxiuPluginActiu() {
		return getArxiuPlugin() != null;
	}
	public boolean arxiuSuportaVersionsExpedients() {
		return getArxiuPlugin().suportaVersionatExpedient();
	}
	public boolean arxiuSuportaVersionsDocuments() {
		return getArxiuPlugin().suportaVersionatDocument();
	}
	public boolean arxiuSuportaMetadades() {
		return getArxiuPlugin().suportaMetadadesNti();
	}

	public void arxiuEscriptoriCrear(
			EscriptoriEntity escriptori) {
		String accioDescripcio = "Creant un nou escriptori";
		Map<String, String> accioParams = new HashMap<String, String>();
		accioParams.put("id", escriptori.getId().toString());
		accioParams.put("nom", escriptori.getNom());
		accioParams.put("entitatId", escriptori.getEntitat().getId().toString());
		accioParams.put("entitatCodi", escriptori.getEntitat().getCodi());
		accioParams.put("entitatNom", escriptori.getEntitat().getNom());
		long t0 = System.currentTimeMillis();
		try {
			ContingutArxiu expedientCreat = getArxiuPlugin().expedientCrear(
					toArxiuExpedient(
							null,
							escriptori.getNom(),
							null,
							Arrays.asList(escriptori.getEntitat().getUnitatArrel()),
							escriptori.getCreatedDate().toDate(),
							getPropertyPluginArxiuEscriptoriClassificacio(),
							ExpedientEstatEnumDto.OBERT,
							null,
							getPropertyPluginArxiuEscriptoriSerieDocumental()));
			escriptori.updateArxiu(
					expedientCreat.getIdentificador());
			integracioHelper.addAccioOk(
					IntegracioHelper.INTCODI_ARXIU,
					accioDescripcio,
					accioParams,
					IntegracioAccioTipusEnumDto.ENVIAMENT,
					System.currentTimeMillis() - t0);
		} catch (Exception ex) {
			String errorDescripcio = "Error al accedir al plugin d'arxiu digital: " + ex.getMessage();
			integracioHelper.addAccioError(
					IntegracioHelper.INTCODI_ARXIU,
					accioDescripcio,
					accioParams,
					IntegracioAccioTipusEnumDto.ENVIAMENT,
					System.currentTimeMillis() - t0,
					errorDescripcio,
					ex);
			throw new SistemaExternException(
					IntegracioHelper.INTCODI_ARXIU,
					errorDescripcio,
					ex);
		}
	}

	public void arxiuExpedientActualitzar(
			ExpedientEntity expedient) {
		String accioDescripcio = "Actualització de les dades d'un expedient";
		Map<String, String> accioParams = new HashMap<String, String>();
		accioParams.put("id", expedient.getId().toString());
		accioParams.put("títol", expedient.getNom());
		accioParams.put("número", expedient.getNumero());
		accioParams.put("tipus", expedient.getMetaExpedient().getNom());
		long t0 = System.currentTimeMillis();
		try {
			MetaExpedientEntity metaExpedient = expedient.getMetaExpedient();
			String classificacio;
			if (metaExpedient.getClassificacioSia() != null) {
				classificacio = metaExpedient.getClassificacioSia();
			} else {
				classificacio = expedient.getArxiu().getUnitatCodi() + "_PRO_RIP" + String.format("%027d", metaExpedient.getId());
			}
			List<String> interessats = new ArrayList<String>();
			for (InteressatEntity interessat: expedient.getInteressats()) {
				if (interessat.getDocumentNum() != null) {
					interessats.add(interessat.getDocumentNum());
				}
			}
			if (expedient.getArxiuUuid() == null) {
				ContingutArxiu expedientCreat = getArxiuPlugin().expedientCrear(
						toArxiuExpedient(
								null,
								expedient.getNom(),
								null,
								Arrays.asList(expedient.getArxiu().getUnitatCodi()),
								expedient.getCreatedDate().toDate(),
								classificacio,
								expedient.getEstat(),
								interessats,
								metaExpedient.getClassificacioDocumental()));
				if (getArxiuPlugin().suportaMetadadesNti()) {
					Expedient expedientDetalls = getArxiuPlugin().expedientDetalls(
							expedientCreat.getIdentificador(),
							null);
					propagarMetadadesExpedient(
							expedientDetalls,
							expedient);
				}
				expedient.updateArxiu(
						expedientCreat.getIdentificador());
			} else {
				getArxiuPlugin().expedientModificar(
						toArxiuExpedient(
								expedient.getArxiuUuid(),
								expedient.getNom(),
								expedient.getNtiIdentificador(),
								Arrays.asList(expedient.getArxiu().getUnitatCodi()),
								expedient.getCreatedDate().toDate(),
								classificacio,
								expedient.getEstat(),
								interessats,
								metaExpedient.getClassificacioDocumental()));
				expedient.updateArxiu(null);
			}
			integracioHelper.addAccioOk(
					IntegracioHelper.INTCODI_ARXIU,
					accioDescripcio,
					accioParams,
					IntegracioAccioTipusEnumDto.ENVIAMENT,
					System.currentTimeMillis() - t0);
		} catch (Exception ex) {
			String errorDescripcio = "Error al accedir al plugin d'arxiu digital: " + ex.getMessage();
			integracioHelper.addAccioError(
					IntegracioHelper.INTCODI_ARXIU,
					accioDescripcio,
					accioParams,
					IntegracioAccioTipusEnumDto.ENVIAMENT,
					System.currentTimeMillis() - t0,
					errorDescripcio,
					ex);
			throw new SistemaExternException(
					IntegracioHelper.INTCODI_ARXIU,
					errorDescripcio,
					ex);
		}
	}

	public Expedient arxiuExpedientConsultar(
			ExpedientEntity expedient) {
		String accioDescripcio = "Consulta d'un expedient";
		Map<String, String> accioParams = new HashMap<String, String>();
		accioParams.put("id", expedient.getId().toString());
		accioParams.put("títol", expedient.getNom());
		accioParams.put("número", expedient.getNumero());
		accioParams.put("tipus", expedient.getMetaExpedient().getNom());
		long t0 = System.currentTimeMillis();
		try {
			Expedient arxiuExpedient = getArxiuPlugin().expedientDetalls(
					expedient.getArxiuUuid(),
					null);
			integracioHelper.addAccioOk(
					IntegracioHelper.INTCODI_ARXIU,
					accioDescripcio,
					accioParams,
					IntegracioAccioTipusEnumDto.ENVIAMENT,
					System.currentTimeMillis() - t0);
			return arxiuExpedient;
		} catch (Exception ex) {
			String errorDescripcio = "Error al accedir al plugin d'arxiu digital: " + ex.getMessage();
			integracioHelper.addAccioError(
					IntegracioHelper.INTCODI_ARXIU,
					accioDescripcio,
					accioParams,
					IntegracioAccioTipusEnumDto.ENVIAMENT,
					System.currentTimeMillis() - t0,
					errorDescripcio,
					ex);
			throw new SistemaExternException(
					IntegracioHelper.INTCODI_ARXIU,
					errorDescripcio,
					ex);
		}
	}

	public void arxiuExpedientEsborrar(
			ExpedientEntity expedient) {
		String accioDescripcio = "Eliminació d'un expedient";
		Map<String, String> accioParams = new HashMap<String, String>();
		accioParams.put("id", expedient.getId().toString());
		accioParams.put("títol", expedient.getNom());
		accioParams.put("número", expedient.getNumero());
		accioParams.put("tipus", expedient.getMetaExpedient().getNom());
		long t0 = System.currentTimeMillis();
		try {
			getArxiuPlugin().expedientEsborrar(
					expedient.getArxiuUuid());
			expedient.updateArxiuEsborrat();
			integracioHelper.addAccioOk(
					IntegracioHelper.INTCODI_ARXIU,
					accioDescripcio,
					accioParams,
					IntegracioAccioTipusEnumDto.ENVIAMENT,
					System.currentTimeMillis() - t0);
		} catch (Exception ex) {
			String errorDescripcio = "Error al accedir al plugin d'arxiu digital: " + ex.getMessage();
			integracioHelper.addAccioError(
					IntegracioHelper.INTCODI_ARXIU,
					accioDescripcio,
					accioParams,
					IntegracioAccioTipusEnumDto.ENVIAMENT,
					System.currentTimeMillis() - t0,
					errorDescripcio,
					ex);
			throw new SistemaExternException(
					IntegracioHelper.INTCODI_ARXIU,
					errorDescripcio,
					ex);
		}
	}

	public void arxiuExpedientTancar(
			ExpedientEntity expedient) {
		String accioDescripcio = "Tancament d'un expedient";
		Map<String, String> accioParams = new HashMap<String, String>();
		accioParams.put("id", expedient.getId().toString());
		accioParams.put("títol", expedient.getNom());
		accioParams.put("número", expedient.getNumero());
		accioParams.put("tipus", expedient.getMetaExpedient().getNom());
		long t0 = System.currentTimeMillis();
		try {
			getArxiuPlugin().expedientTancar(
					expedient.getArxiuUuid());
			expedient.updateArxiuEsborrat();
			integracioHelper.addAccioOk(
					IntegracioHelper.INTCODI_ARXIU,
					accioDescripcio,
					accioParams,
					IntegracioAccioTipusEnumDto.ENVIAMENT,
					System.currentTimeMillis() - t0);
		} catch (Exception ex) {
			String errorDescripcio = "Error al accedir al plugin d'arxiu digital: " + ex.getMessage();
			integracioHelper.addAccioError(
					IntegracioHelper.INTCODI_ARXIU,
					accioDescripcio,
					accioParams,
					IntegracioAccioTipusEnumDto.ENVIAMENT,
					System.currentTimeMillis() - t0,
					errorDescripcio,
					ex);
			throw new SistemaExternException(
					IntegracioHelper.INTCODI_ARXIU,
					errorDescripcio,
					ex);
		}
	}

	public void arxiuExpedientReobrir(
			ExpedientEntity expedient) {
		String accioDescripcio = "Reobertura d'un expedient";
		Map<String, String> accioParams = new HashMap<String, String>();
		accioParams.put("id", expedient.getId().toString());
		accioParams.put("títol", expedient.getNom());
		accioParams.put("número", expedient.getNumero());
		accioParams.put("tipus", expedient.getMetaExpedient().getNom());
		long t0 = System.currentTimeMillis();
		try {
			getArxiuPlugin().expedientReobrir(
					expedient.getArxiuUuid());
			expedient.updateArxiuEsborrat();
			integracioHelper.addAccioOk(
					IntegracioHelper.INTCODI_ARXIU,
					accioDescripcio,
					accioParams,
					IntegracioAccioTipusEnumDto.ENVIAMENT,
					System.currentTimeMillis() - t0);
		} catch (Exception ex) {
			String errorDescripcio = "Error al accedir al plugin d'arxiu digital: " + ex.getMessage();
			integracioHelper.addAccioError(
					IntegracioHelper.INTCODI_ARXIU,
					accioDescripcio,
					accioParams,
					IntegracioAccioTipusEnumDto.ENVIAMENT,
					System.currentTimeMillis() - t0,
					errorDescripcio,
					ex);
			throw new SistemaExternException(
					IntegracioHelper.INTCODI_ARXIU,
					errorDescripcio,
					ex);
		}
	}

	public String arxiuExpedientExportar(
			ExpedientEntity expedient) {
		String accioDescripcio = "Exportar expedient en format ENI";
		Map<String, String> accioParams = new HashMap<String, String>();
		accioParams.put("id", expedient.getId().toString());
		accioParams.put("títol", expedient.getNom());
		long t0 = System.currentTimeMillis();
		try {
			String exportacio = getArxiuPlugin().expedientExportarEni(
					expedient.getArxiuUuid());
			integracioHelper.addAccioOk(
					IntegracioHelper.INTCODI_ARXIU,
					accioDescripcio,
					accioParams,
					IntegracioAccioTipusEnumDto.ENVIAMENT,
					System.currentTimeMillis() - t0);
			return exportacio;
		} catch (Exception ex) {
			String errorDescripcio = "Error al accedir al plugin d'arxiu digital: " + ex.getMessage();
			integracioHelper.addAccioError(
					IntegracioHelper.INTCODI_ARXIU,
					accioDescripcio,
					accioParams,
					IntegracioAccioTipusEnumDto.ENVIAMENT,
					System.currentTimeMillis() - t0,
					errorDescripcio,
					ex);
			throw new SistemaExternException(
					IntegracioHelper.INTCODI_ARXIU,
					errorDescripcio,
					ex);
		}
	}

	public void arxiuDocumentActualitzar(
			DocumentEntity document,
			FitxerDto fitxer,
			ContingutEntity contingutPare,
			String classificacioDocumental) {
		String accioDescripcio = "Actualització de les dades d'un document";
		Map<String, String> accioParams = new HashMap<String, String>();
		accioParams.put("id", document.getId().toString());
		accioParams.put("títol", document.getNom());
		accioParams.put("contingutPareId", contingutPare.getId().toString());
		accioParams.put("contingutPareNom", contingutPare.getNom());
		accioParams.put("classificacioDocumental", classificacioDocumental);
		long t0 = System.currentTimeMillis();
		try {
			if (document.getArxiuUuid() == null) {
				ContingutArxiu documentCreat = getArxiuPlugin().documentCrear(
						toArxiuDocument(
								null,
								document.getNom(),
								fitxer,
								null,
								null,
								document.getNtiOrigen(),
								Arrays.asList(document.getNtiOrgano()),
								document.getDataCaptura(),
								document.getNtiEstadoElaboracion(),
								document.getNtiTipoDocumental(),
								classificacioDocumental,
								DocumentEstat.ESBORRANY,
								DocumentTipusEnumDto.FISIC.equals(document.getDocumentTipus())),
						contingutPare.getArxiuUuid());
				if (getArxiuPlugin().suportaMetadadesNti()) {
					Document documentDetalls = getArxiuPlugin().documentDetalls(
							documentCreat.getIdentificador(),
							null,
							false);
					propagarMetadadesDocument(
							documentDetalls,
							document);
				}
				document.updateArxiu(
						documentCreat.getIdentificador());
			} else {
				getArxiuPlugin().documentModificar(
						toArxiuDocument(
								document.getArxiuUuid(),
								document.getNom(),
								fitxer,
								null,
								null,
								document.getNtiOrigen(),
								Arrays.asList(document.getNtiOrgano()),
								document.getDataCaptura(),
								document.getNtiEstadoElaboracion(),
								document.getNtiTipoDocumental(),
								classificacioDocumental,
								DocumentEstat.ESBORRANY,
								DocumentTipusEnumDto.FISIC.equals(document.getDocumentTipus())));
				document.updateArxiu(null);
			}
			integracioHelper.addAccioOk(
					IntegracioHelper.INTCODI_ARXIU,
					accioDescripcio,
					accioParams,
					IntegracioAccioTipusEnumDto.ENVIAMENT,
					System.currentTimeMillis() - t0);
		} catch (Exception ex) {
			String errorDescripcio = "Error al accedir al plugin d'arxiu digital: " + ex.getMessage();
			integracioHelper.addAccioError(
					IntegracioHelper.INTCODI_ARXIU,
					accioDescripcio,
					accioParams,
					IntegracioAccioTipusEnumDto.ENVIAMENT,
					System.currentTimeMillis() - t0,
					errorDescripcio,
					ex);
			throw new SistemaExternException(
					IntegracioHelper.INTCODI_ARXIU,
					errorDescripcio,
					ex);
		}
	}

	public Document arxiuDocumentConsultar(
			ContingutEntity contingut,
			String nodeId,
			String versio,
			boolean ambContingut) {
		String accioDescripcio = "Consulta d'un document";
		Map<String, String> accioParams = new HashMap<String, String>();
		accioParams.put("contingutId", contingut.getId().toString());
		accioParams.put("contingutNom", contingut.getNom());
		accioParams.put("nodeId", nodeId);
		String arxiuUuid = null;
		if (contingut instanceof DocumentEntity) {
			arxiuUuid = contingut.getArxiuUuid();
		} else {
			arxiuUuid = nodeId;
		}
		accioParams.put("arxiuUuidCalculat", arxiuUuid);
		accioParams.put("versio", versio);
		accioParams.put("ambContingut", new Boolean(ambContingut).toString());
		long t0 = System.currentTimeMillis();
		try {
			Document documentDetalls = getArxiuPlugin().documentDetalls(
					arxiuUuid,
					versio,
					ambContingut);
			integracioHelper.addAccioOk(
					IntegracioHelper.INTCODI_ARXIU,
					accioDescripcio,
					accioParams,
					IntegracioAccioTipusEnumDto.ENVIAMENT,
					System.currentTimeMillis() - t0);
			return documentDetalls;
		} catch (Exception ex) {
			String errorDescripcio = "Error al accedir al plugin d'arxiu digital: " + ex.getMessage();
			integracioHelper.addAccioError(
					IntegracioHelper.INTCODI_ARXIU,
					accioDescripcio,
					accioParams,
					IntegracioAccioTipusEnumDto.ENVIAMENT,
					System.currentTimeMillis() - t0,
					errorDescripcio,
					ex);
			throw new SistemaExternException(
					IntegracioHelper.INTCODI_ARXIU,
					errorDescripcio,
					ex);
		}
	}

	public void arxiuDocumentEsborrar(
			DocumentEntity document) {
		String accioDescripcio = "Eliminació d'un document";
		Map<String, String> accioParams = new HashMap<String, String>();
		accioParams.put("id", document.getId().toString());
		accioParams.put("títol", document.getNom());
		long t0 = System.currentTimeMillis();
		try {
			getArxiuPlugin().documentEsborrar(
					document.getArxiuUuid());
			document.updateArxiuEsborrat();
			integracioHelper.addAccioOk(
					IntegracioHelper.INTCODI_ARXIU,
					accioDescripcio,
					accioParams,
					IntegracioAccioTipusEnumDto.ENVIAMENT,
					System.currentTimeMillis() - t0);
		} catch (Exception ex) {
			String errorDescripcio = "Error al accedir al plugin d'arxiu digital: " + ex.getMessage();
			integracioHelper.addAccioError(
					IntegracioHelper.INTCODI_ARXIU,
					accioDescripcio,
					accioParams,
					IntegracioAccioTipusEnumDto.ENVIAMENT,
					System.currentTimeMillis() - t0,
					errorDescripcio,
					ex);
			throw new SistemaExternException(
					IntegracioHelper.INTCODI_ARXIU,
					errorDescripcio,
					ex);
		}
	}

	public boolean arxiuDocumentExtensioPermesa(String extensio) {
		return getArxiuFormatExtensio(extensio) != null;
	}

	public List<ContingutArxiu> arxiuDocumentObtenirVersions(
			DocumentEntity document) {
		String accioDescripcio = "Obtenir versions del document";
		Map<String, String> accioParams = new HashMap<String, String>();
		accioParams.put("id", document.getId().toString());
		accioParams.put("títol", document.getNom());
		long t0 = System.currentTimeMillis();
		try {
			List<ContingutArxiu> versions = getArxiuPlugin().documentVersions(
					document.getArxiuUuid());
			integracioHelper.addAccioOk(
					IntegracioHelper.INTCODI_ARXIU,
					accioDescripcio,
					accioParams,
					IntegracioAccioTipusEnumDto.ENVIAMENT,
					System.currentTimeMillis() - t0);
			return versions;
		} catch (Exception ex) {
			String errorDescripcio = "Error al accedir al plugin d'arxiu digital: " + ex.getMessage();
			integracioHelper.addAccioError(
					IntegracioHelper.INTCODI_ARXIU,
					accioDescripcio,
					accioParams,
					IntegracioAccioTipusEnumDto.ENVIAMENT,
					System.currentTimeMillis() - t0,
					errorDescripcio,
					ex);
			throw new SistemaExternException(
					IntegracioHelper.INTCODI_ARXIU,
					errorDescripcio,
					ex);
		}
	}

	public String arxiuDocumentGuardarPdfFirmat(
			DocumentEntity document,
			FitxerDto fitxerPdfFirmat) {
		// El paràmetre custodiaTipus es reb sempre com a paràmetre però només te
		// sentit quan s'empra el plugin d'arxiu que accedeix a valcert.
		String accioDescripcio = "Guardar PDF firmat com a document definitiu";
		Map<String, String> accioParams = new HashMap<String, String>();
		accioParams.put("id", document.getId().toString());
		accioParams.put("títol", document.getNom());
		accioParams.put("fitxerPdfFirmatNom", fitxerPdfFirmat.getNom());
		accioParams.put("fitxerPdfFirmatTamany", new Long(fitxerPdfFirmat.getTamany()).toString());
		accioParams.put("fitxerPdfFirmatContentType", fitxerPdfFirmat.getContentType());
		long t0 = System.currentTimeMillis();
		try {
			getArxiuPlugin().documentModificar(
					toArxiuDocument(
							document.getArxiuUuid(),
							document.getNom(),
							null,
							fitxerPdfFirmat,
							null,
							document.getNtiOrigen(),
							Arrays.asList(document.getNtiOrgano()),
							document.getDataCaptura(),
							document.getNtiEstadoElaboracion(),
							document.getNtiTipoDocumental(),
							null,
							DocumentEstat.DEFINITIU,
							DocumentTipusEnumDto.FISIC.equals(document.getDocumentTipus())));
			integracioHelper.addAccioOk(
					IntegracioHelper.INTCODI_ARXIU,
					accioDescripcio,
					accioParams,
					IntegracioAccioTipusEnumDto.ENVIAMENT,
					System.currentTimeMillis() - t0);
			document.updateEstat(
					DocumentEstatEnumDto.CUSTODIAT);
			return document.getId().toString();
		} catch (Exception ex) {
			String errorDescripcio = "Error al accedir al plugin d'arxiu digital: " + ex.getMessage();
			integracioHelper.addAccioError(
					IntegracioHelper.INTCODI_ARXIU,
					accioDescripcio,
					accioParams,
					IntegracioAccioTipusEnumDto.ENVIAMENT,
					System.currentTimeMillis() - t0,
					errorDescripcio,
					ex);
			throw new SistemaExternException(
					IntegracioHelper.INTCODI_ARXIU,
					errorDescripcio,
					ex);
		}
	}

	public void arxiuDocumentCopiar(
			DocumentEntity document,
			String arxiuUuidDesti) {
		String accioDescripcio = "Copiar document";
		Map<String, String> accioParams = new HashMap<String, String>();
		accioParams.put("id", document.getId().toString());
		accioParams.put("títol", document.getNom());
		accioParams.put("arxiuUuidDesti", arxiuUuidDesti);
		long t0 = System.currentTimeMillis();
		try {
			getArxiuPlugin().documentCopiar(
					document.getArxiuUuid(),
					arxiuUuidDesti);
			integracioHelper.addAccioOk(
					IntegracioHelper.INTCODI_ARXIU,
					accioDescripcio,
					accioParams,
					IntegracioAccioTipusEnumDto.ENVIAMENT,
					System.currentTimeMillis() - t0);
		} catch (Exception ex) {
			String errorDescripcio = "Error al accedir al plugin d'arxiu digital: " + ex.getMessage();
			integracioHelper.addAccioError(
					IntegracioHelper.INTCODI_ARXIU,
					accioDescripcio,
					accioParams,
					IntegracioAccioTipusEnumDto.ENVIAMENT,
					System.currentTimeMillis() - t0,
					errorDescripcio,
					ex);
			throw new SistemaExternException(
					IntegracioHelper.INTCODI_ARXIU,
					errorDescripcio,
					ex);
		}
	}

	public void arxiuDocumentMoure(
			DocumentEntity document,
			String arxiuUuidDesti) {
		String accioDescripcio = "Moure document";
		Map<String, String> accioParams = new HashMap<String, String>();
		accioParams.put("id", document.getId().toString());
		accioParams.put("títol", document.getNom());
		accioParams.put("arxiuUuidDesti", arxiuUuidDesti);
		long t0 = System.currentTimeMillis();
		try {
			getArxiuPlugin().documentMoure(
					document.getArxiuUuid(),
					arxiuUuidDesti);
			integracioHelper.addAccioOk(
					IntegracioHelper.INTCODI_ARXIU,
					accioDescripcio,
					accioParams,
					IntegracioAccioTipusEnumDto.ENVIAMENT,
					System.currentTimeMillis() - t0);
		} catch (Exception ex) {
			String errorDescripcio = "Error al accedir al plugin d'arxiu digital: " + ex.getMessage();
			integracioHelper.addAccioError(
					IntegracioHelper.INTCODI_ARXIU,
					accioDescripcio,
					accioParams,
					IntegracioAccioTipusEnumDto.ENVIAMENT,
					System.currentTimeMillis() - t0,
					errorDescripcio,
					ex);
			throw new SistemaExternException(
					IntegracioHelper.INTCODI_ARXIU,
					errorDescripcio,
					ex);
		}
	}

	public String arxiuDocumentExportar(
			DocumentEntity document) {
		String accioDescripcio = "Exportar document en format ENI";
		Map<String, String> accioParams = new HashMap<String, String>();
		accioParams.put("id", document.getId().toString());
		accioParams.put("títol", document.getNom());
		long t0 = System.currentTimeMillis();
		try {
			String exportacio = getArxiuPlugin().documentExportarEni(
					document.getArxiuUuid());
			integracioHelper.addAccioOk(
					IntegracioHelper.INTCODI_ARXIU,
					accioDescripcio,
					accioParams,
					IntegracioAccioTipusEnumDto.ENVIAMENT,
					System.currentTimeMillis() - t0);
			return exportacio;
		} catch (Exception ex) {
			String errorDescripcio = "Error al accedir al plugin d'arxiu digital: " + ex.getMessage();
			integracioHelper.addAccioError(
					IntegracioHelper.INTCODI_ARXIU,
					accioDescripcio,
					accioParams,
					IntegracioAccioTipusEnumDto.ENVIAMENT,
					System.currentTimeMillis() - t0,
					errorDescripcio,
					ex);
			throw new SistemaExternException(
					IntegracioHelper.INTCODI_ARXIU,
					errorDescripcio,
					ex);
		}
	}

	public void arxiuCarpetaActualitzar(
			CarpetaEntity carpeta,
			ContingutEntity contingutPare) {
		String accioDescripcio = "Actualització de les dades d'una carpeta";
		Map<String, String> accioParams = new HashMap<String, String>();
		accioParams.put("id", carpeta.getId().toString());
		accioParams.put("nom", carpeta.getNom());
		accioParams.put("contingutPareId", contingutPare.getId().toString());
		accioParams.put("contingutPareNom", contingutPare.getNom());
		long t0 = System.currentTimeMillis();
		try {
			if (carpeta.getArxiuUuid() == null) {
				ContingutArxiu carpetaCreada = getArxiuPlugin().carpetaCrear(
						toArxiuCarpeta(
								null,
								carpeta.getNom()),
						contingutPare.getArxiuUuid());
				carpeta.updateArxiu(
						carpetaCreada.getIdentificador());
			} else {
				getArxiuPlugin().carpetaModificar(
						toArxiuCarpeta(
								carpeta.getArxiuUuid(),
								carpeta.getNom()));
				carpeta.updateArxiu(null);
			}
			integracioHelper.addAccioOk(
					IntegracioHelper.INTCODI_ARXIU,
					accioDescripcio,
					accioParams,
					IntegracioAccioTipusEnumDto.ENVIAMENT,
					System.currentTimeMillis() - t0);
		} catch (Exception ex) {
			String errorDescripcio = "Error al accedir al plugin d'arxiu digital: " + ex.getMessage();
			integracioHelper.addAccioError(
					IntegracioHelper.INTCODI_ARXIU,
					accioDescripcio,
					accioParams,
					IntegracioAccioTipusEnumDto.ENVIAMENT,
					System.currentTimeMillis() - t0,
					errorDescripcio,
					ex);
			throw new SistemaExternException(
					IntegracioHelper.INTCODI_ARXIU,
					errorDescripcio,
					ex);
		}
	}

	public Carpeta arxiuCarpetaConsultar(
			CarpetaEntity carpeta) {
		String accioDescripcio = "Consulta d'una carpeta";
		Map<String, String> accioParams = new HashMap<String, String>();
		accioParams.put("id", carpeta.getId().toString());
		accioParams.put("nom", carpeta.getNom());
		long t0 = System.currentTimeMillis();
		try {
			Carpeta carpetaDetalls = getArxiuPlugin().carpetaDetalls(
					carpeta.getArxiuUuid());
			integracioHelper.addAccioOk(
					IntegracioHelper.INTCODI_ARXIU,
					accioDescripcio,
					accioParams,
					IntegracioAccioTipusEnumDto.ENVIAMENT,
					System.currentTimeMillis() - t0);
			return carpetaDetalls;
		} catch (Exception ex) {
			String errorDescripcio = "Error al accedir al plugin d'arxiu digital: " + ex.getMessage();
			integracioHelper.addAccioError(
					IntegracioHelper.INTCODI_ARXIU,
					accioDescripcio,
					accioParams,
					IntegracioAccioTipusEnumDto.ENVIAMENT,
					System.currentTimeMillis() - t0,
					errorDescripcio,
					ex);
			throw new SistemaExternException(
					IntegracioHelper.INTCODI_ARXIU,
					errorDescripcio,
					ex);
		}
	}

	public void arxiuCarpetaEsborrar(
			CarpetaEntity carpeta) {
		String accioDescripcio = "Eliminació d'una carpeta";
		Map<String, String> accioParams = new HashMap<String, String>();
		accioParams.put("id", carpeta.getId().toString());
		accioParams.put("nom", carpeta.getNom());
		long t0 = System.currentTimeMillis();
		try {
			getArxiuPlugin().carpetaEsborrar(
					carpeta.getArxiuUuid());
			carpeta.updateArxiuEsborrat();
			integracioHelper.addAccioOk(
					IntegracioHelper.INTCODI_ARXIU,
					accioDescripcio,
					accioParams,
					IntegracioAccioTipusEnumDto.ENVIAMENT,
					System.currentTimeMillis() - t0);
		} catch (Exception ex) {
			String errorDescripcio = "Error al accedir al plugin d'arxiu digital: " + ex.getMessage();
			integracioHelper.addAccioError(
					IntegracioHelper.INTCODI_ARXIU,
					accioDescripcio,
					accioParams,
					IntegracioAccioTipusEnumDto.ENVIAMENT,
					System.currentTimeMillis() - t0,
					errorDescripcio,
					ex);
			throw new SistemaExternException(
					IntegracioHelper.INTCODI_ARXIU,
					errorDescripcio,
					ex);
		}
	}

	public void arxiuCarpetaCopiar(
			CarpetaEntity carpeta,
			String arxiuUuidDesti) {
		String accioDescripcio = "Copiar carpeta";
		Map<String, String> accioParams = new HashMap<String, String>();
		accioParams.put("id", carpeta.getId().toString());
		accioParams.put("nom", carpeta.getNom());
		accioParams.put("arxiuUuidDesti", arxiuUuidDesti);
		long t0 = System.currentTimeMillis();
		try {
			getArxiuPlugin().carpetaCopiar(
					carpeta.getArxiuUuid(),
					arxiuUuidDesti);
			integracioHelper.addAccioOk(
					IntegracioHelper.INTCODI_ARXIU,
					accioDescripcio,
					accioParams,
					IntegracioAccioTipusEnumDto.ENVIAMENT,
					System.currentTimeMillis() - t0);
		} catch (Exception ex) {
			String errorDescripcio = "Error al accedir al plugin d'arxiu digital: " + ex.getMessage();
			integracioHelper.addAccioError(
					IntegracioHelper.INTCODI_ARXIU,
					accioDescripcio,
					accioParams,
					IntegracioAccioTipusEnumDto.ENVIAMENT,
					System.currentTimeMillis() - t0,
					errorDescripcio,
					ex);
			throw new SistemaExternException(
					IntegracioHelper.INTCODI_ARXIU,
					errorDescripcio,
					ex);
		}
	}

	public void arxiuCarpetaMoure(
			CarpetaEntity carpeta,
			String arxiuUuidDesti) {
		String accioDescripcio = "Moure carpeta";
		Map<String, String> accioParams = new HashMap<String, String>();
		accioParams.put("id", carpeta.getId().toString());
		accioParams.put("nom", carpeta.getNom());
		accioParams.put("arxiuUuidDesti", arxiuUuidDesti);
		long t0 = System.currentTimeMillis();
		try {
			getArxiuPlugin().carpetaMoure(
					carpeta.getArxiuUuid(),
					arxiuUuidDesti);
			integracioHelper.addAccioOk(
					IntegracioHelper.INTCODI_ARXIU,
					accioDescripcio,
					accioParams,
					IntegracioAccioTipusEnumDto.ENVIAMENT,
					System.currentTimeMillis() - t0);
		} catch (Exception ex) {
			String errorDescripcio = "Error al accedir al plugin d'arxiu digital: " + ex.getMessage();
			integracioHelper.addAccioError(
					IntegracioHelper.INTCODI_ARXIU,
					accioDescripcio,
					accioParams,
					IntegracioAccioTipusEnumDto.ENVIAMENT,
					System.currentTimeMillis() - t0,
					errorDescripcio,
					ex);
			throw new SistemaExternException(
					IntegracioHelper.INTCODI_ARXIU,
					errorDescripcio,
					ex);
		}
	}

	public String portafirmesUpload(
			DocumentEntity document,
			String motiu,
			PortafirmesPrioritatEnum prioritat,
			Date dataCaducitat,
			String documentTipus,
			String[] responsables,
			MetaDocumentFirmaFluxTipusEnumDto fluxTipus,
			String fluxId,
			List<DocumentEntity> annexos) {
		String accioDescripcio = "Enviament de document a firmar";
		Map<String, String> accioParams = new HashMap<String, String>();
		accioParams.put(
				"documentId",
				document.getId().toString());
		accioParams.put(
				"documentTitol",
				document.getNom());
		accioParams.put("motiu", motiu);
		accioParams.put("prioritat", prioritat.toString());
		accioParams.put(
				"dataCaducitat",
				new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(dataCaducitat));
		long t0 = System.currentTimeMillis();
		if (annexos != null) {
			StringBuilder annexosIds = new StringBuilder();
			StringBuilder annexosTitols = new StringBuilder();
			boolean primer = true;
			for (DocumentEntity annex: annexos) {
				if (!primer) {
					annexosIds.append(", ");
					annexosTitols.append(", ");
				}
				annexosIds.append(annex.getId());
				annexosTitols.append(annex.getNom());
				primer = false;
			}
			accioParams.put("annexosIds", annexosIds.toString());
			accioParams.put("annexosTitols", annexosTitols.toString());
		}
		PortafirmesDocument portafirmesDocument = new PortafirmesDocument();
		portafirmesDocument.setTitol(document.getNom());
		portafirmesDocument.setFirmat(
				false);
		/*String urlCustodia = null;
		if (portafirmesEnviarDocumentEstampat()) {
			urlCustodia = arxiuDocumentGenerarUrlPerDocument(document);
		}
		FitxerDto fitxerOriginal = documentHelper.getFitxerAssociat(document);
		FitxerDto fitxerConvertit = conversioConvertirPdfIEstamparUrl(
				fitxerOriginal,
				urlCustodia);*/
		FitxerDto fitxerOriginal = documentHelper.getFitxerAssociat(document);
		FitxerDto fitxerConvertit = this.conversioConvertirPdf(
				fitxerOriginal,
				null);
		portafirmesDocument.setArxiuNom(
				fitxerConvertit.getNom());
		portafirmesDocument.setArxiuContingut(
				fitxerConvertit.getContingut());
		List<PortafirmesFluxBloc> flux = new ArrayList<PortafirmesFluxBloc>();
		if (MetaDocumentFirmaFluxTipusEnumDto.SERIE.equals(fluxTipus)) {
			for (String responsable: responsables) {
				PortafirmesFluxBloc bloc = new PortafirmesFluxBloc();
				bloc.setMinSignataris(1);
				bloc.setDestinataris(new String[] {responsable});
				bloc.setObligatorietats(new boolean[] {true});
				flux.add(bloc);
			}
		} else if (MetaDocumentFirmaFluxTipusEnumDto.PARALEL.equals(fluxTipus)) {
			PortafirmesFluxBloc bloc = new PortafirmesFluxBloc();
			bloc.setMinSignataris(responsables.length);
			bloc.setDestinataris(responsables);
			boolean[] obligatorietats = new boolean[responsables.length];
			Arrays.fill(obligatorietats, true);
			bloc.setObligatorietats(obligatorietats);
			flux.add(bloc);
		}
		try {
			Calendar dataCaducitatCal = Calendar.getInstance();
			dataCaducitatCal.setTime(dataCaducitat);
			if (	dataCaducitatCal.get(Calendar.HOUR_OF_DAY) == 0 &&
					dataCaducitatCal.get(Calendar.MINUTE) == 0 &&
					dataCaducitatCal.get(Calendar.SECOND) == 0 &&
					dataCaducitatCal.get(Calendar.MILLISECOND) == 0) {
				dataCaducitatCal.set(Calendar.HOUR_OF_DAY, 23);
				dataCaducitatCal.set(Calendar.MINUTE, 59);
				dataCaducitatCal.set(Calendar.SECOND, 59);
				dataCaducitatCal.set(Calendar.MILLISECOND, 999);
			}
			String portafirmesEnviamentId = getPortafirmesPlugin().upload(
					portafirmesDocument,
					documentTipus,
					motiu,
					"Aplicació RIPEA",
					prioritat,
					dataCaducitatCal.getTime(),
					flux,
					fluxId,
					null,
					false);
			integracioHelper.addAccioOk(
					IntegracioHelper.INTCODI_PFIRMA,
					accioDescripcio,
					accioParams,
					IntegracioAccioTipusEnumDto.ENVIAMENT,
					System.currentTimeMillis() - t0);
			return portafirmesEnviamentId;
		} catch (Exception ex) {
			String errorDescripcio = "Error al accedir al plugin de portafirmes";
			integracioHelper.addAccioError(
					IntegracioHelper.INTCODI_PFIRMA,
					accioDescripcio,
					accioParams,
					IntegracioAccioTipusEnumDto.ENVIAMENT,
					System.currentTimeMillis() - t0,
					errorDescripcio,
					ex);
			throw new SistemaExternException(
					IntegracioHelper.INTCODI_PFIRMA,
					errorDescripcio,
					ex);
		}
	}

	public PortafirmesDocument portafirmesDownload(
			DocumentPortafirmesEntity documentPortafirmes) {
		String accioDescripcio = "Descarregar document firmat";
		Map<String, String> accioParams = new HashMap<String, String>();
		DocumentEntity document = documentPortafirmes.getDocument();
		accioParams.put(
				"documentVersioId",
				document.getId().toString());
		accioParams.put(
				"documentPortafirmesId",
				documentPortafirmes.getId().toString());
		accioParams.put(
				"portafirmesId",
				new Long(documentPortafirmes.getPortafirmesId()).toString());
		long t0 = System.currentTimeMillis();
		PortafirmesDocument portafirmesDocument = null;
		try {
			portafirmesDocument = getPortafirmesPlugin().download(
					documentPortafirmes.getPortafirmesId());
			integracioHelper.addAccioOk(
					IntegracioHelper.INTCODI_PFIRMA,
					accioDescripcio,
					accioParams,
					IntegracioAccioTipusEnumDto.ENVIAMENT,
					System.currentTimeMillis() - t0);
			return portafirmesDocument;
		} catch (Exception ex) {
			String errorDescripcio = "Error al descarregar el document firmat";
			integracioHelper.addAccioError(
					IntegracioHelper.INTCODI_PFIRMA,
					accioDescripcio,
					accioParams,
					IntegracioAccioTipusEnumDto.ENVIAMENT,
					System.currentTimeMillis() - t0,
					errorDescripcio,
					ex);
			throw new SistemaExternException(
					IntegracioHelper.INTCODI_PFIRMA,
					errorDescripcio,
					ex);
		}
	}

	public void portafirmesDelete(
			DocumentPortafirmesEntity documentPortafirmes) {
		String accioDescripcio = "Esborrar document enviat a firmar";
		Map<String, String> accioParams = new HashMap<String, String>();
		DocumentEntity document = documentPortafirmes.getDocument();
		accioParams.put(
				"documentId",
				document.getId().toString());
		accioParams.put(
				"documentPortafirmesId",
				documentPortafirmes.getId().toString());
		accioParams.put(
				"portafirmesId",
				new Long(documentPortafirmes.getPortafirmesId()).toString());
		long t0 = System.currentTimeMillis();
		try {
			getPortafirmesPlugin().delete(
					documentPortafirmes.getPortafirmesId());
			integracioHelper.addAccioOk(
					IntegracioHelper.INTCODI_PFIRMA,
					accioDescripcio,
					accioParams,
					IntegracioAccioTipusEnumDto.ENVIAMENT,
					System.currentTimeMillis() - t0);
		} catch (Exception ex) {
			String errorDescripcio = "Error al accedir al plugin de portafirmes";
			integracioHelper.addAccioError(
					IntegracioHelper.INTCODI_PFIRMA,
					accioDescripcio,
					accioParams,
					IntegracioAccioTipusEnumDto.ENVIAMENT,
					System.currentTimeMillis() - t0,
					errorDescripcio,
					ex);
			throw new SistemaExternException(
					IntegracioHelper.INTCODI_PFIRMA,
					errorDescripcio,
					ex);
		}
	}

	public List<PortafirmesDocumentTipusDto> portafirmesFindDocumentTipus() {
		String accioDescripcio = "Consulta de tipus de document";
		long t0 = System.currentTimeMillis();
		try {
			List<PortafirmesDocumentTipus> tipus = getPortafirmesPlugin().findDocumentTipus();
			integracioHelper.addAccioOk(
					IntegracioHelper.INTCODI_PFIRMA,
					accioDescripcio,
					null,
					IntegracioAccioTipusEnumDto.ENVIAMENT,
					System.currentTimeMillis() - t0);
			if (tipus != null) {
				List<PortafirmesDocumentTipusDto> resposta = new ArrayList<PortafirmesDocumentTipusDto>();
				for (PortafirmesDocumentTipus t: tipus) {
					PortafirmesDocumentTipusDto dto = new PortafirmesDocumentTipusDto();
					dto.setId(t.getId());
					dto.setCodi(t.getCodi());
					dto.setNom(t.getNom());
					resposta.add(dto);
				}
				return resposta;
			} else {
				return null;
			}
		} catch (Exception ex) {
			String errorDescripcio = "Error al accedir al plugin de portafirmes";
			integracioHelper.addAccioError(
					IntegracioHelper.INTCODI_PFIRMA,
					accioDescripcio,
					null,
					IntegracioAccioTipusEnumDto.ENVIAMENT,
					System.currentTimeMillis() - t0,
					errorDescripcio,
					ex);
			throw new SistemaExternException(
					IntegracioHelper.INTCODI_PFIRMA,
					errorDescripcio,
					ex);
		}
	}

	public boolean portafirmesEnviarDocumentEstampat() {
		return !getPortafirmesPlugin().isCustodiaAutomatica();
	}

	public String conversioConvertirPdfArxiuNom(
			String nomOriginal) {
		return getConversioPlugin().getNomArxiuConvertitPdf(nomOriginal);
	}

	public FitxerDto conversioConvertirPdf(
			FitxerDto original,
			String urlPerEstampar) {
		String accioDescripcio = "Conversió de document a PDF";
		Map<String, String> accioParams = new HashMap<String, String>();
		accioParams.put("arxiuOriginalNom", original.getNom());
		accioParams.put("arxiuOriginalTamany", new Integer(original.getContingut().length).toString());
		long t0 = System.currentTimeMillis();
		try {
			ConversioArxiu convertit = getConversioPlugin().convertirPdfIEstamparUrl(
					new ConversioArxiu(
							original.getNom(),
							original.getContingut()),
					urlPerEstampar);
			accioParams.put("arxiuConvertitNom", convertit.getArxiuNom());
			accioParams.put("arxiuConvertitTamany", new Integer(convertit.getArxiuContingut().length).toString());
			integracioHelper.addAccioOk(
					IntegracioHelper.INTCODI_CONVERT,
					accioDescripcio,
					accioParams,
					IntegracioAccioTipusEnumDto.ENVIAMENT,
					System.currentTimeMillis() - t0);
			FitxerDto resposta = new FitxerDto();
			resposta.setNom(
					convertit.getArxiuNom());
			resposta.setContingut(
					convertit.getArxiuContingut());
			return resposta;
		} catch (Exception ex) {
			String errorDescripcio = "Error al accedir al plugin de conversió de documents";
			integracioHelper.addAccioError(
					IntegracioHelper.INTCODI_CONVERT,
					accioDescripcio,
					accioParams,
					IntegracioAccioTipusEnumDto.ENVIAMENT,
					System.currentTimeMillis() - t0,
					errorDescripcio,
					ex);
			throw new SistemaExternException(
					IntegracioHelper.INTCODI_CONVERT,
					errorDescripcio,
					ex);
		}
	}

	public RegistreAnotacioResposta registreEntradaConsultar(
			String identificador,
			String entitatCodi) {
		String accioDescripcio = "Consulta d'una anotació d'entrada";
		Map<String, String> accioParams = new HashMap<String, String>();
		accioParams.put("identificador", identificador);
		long t0 = System.currentTimeMillis();
		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			RegistreAnotacioResposta resposta = getRegistrePlugin().entradaConsultar(
					identificador,
					auth.getName(),
					entitatCodi);
			integracioHelper.addAccioOk(
					IntegracioHelper.INTCODI_REGISTRE,
					accioDescripcio,
					accioParams,
					IntegracioAccioTipusEnumDto.ENVIAMENT,
					System.currentTimeMillis() - t0);
			return resposta;
		} catch (Exception ex) {
			String errorDescripcio = "Error al accedir al plugin de registre";
			integracioHelper.addAccioError(
					IntegracioHelper.INTCODI_REGISTRE,
					accioDescripcio,
					accioParams,
					IntegracioAccioTipusEnumDto.ENVIAMENT,
					System.currentTimeMillis() - t0,
					errorDescripcio,
					ex);
			throw new SistemaExternException(
					IntegracioHelper.INTCODI_REGISTRE,
					errorDescripcio,
					ex);
		}
	}

	public CiutadaExpedientInformacio ciutadaExpedientCrear(
			ExpedientEntity expedient,
			InteressatEntity destinatari) {
		MetaExpedientEntity metaExpedient = expedient.getMetaExpedient();
		String accioDescripcio = "Creació d'un expedient a la zona personal del ciutadà";
		Map<String, String> accioParams = new HashMap<String, String>();
		accioParams.put("expedientId", expedient.getId().toString());
		accioParams.put("expedientNumero", expedient.getNumero());
		accioParams.put("expedientTitol", expedient.getNom());
		accioParams.put("expedientTipusId", expedient.getMetaNode().getId().toString());
		accioParams.put("expedientTipusNom", expedient.getMetaNode().getNom());
		accioParams.put("unitatAdministrativa", metaExpedient.getUnitatAdministrativa());
		String idioma = getIdiomaPerPluginCiutada(destinatari.getPreferenciaIdioma());
		accioParams.put("idioma", idioma);
		accioParams.put("destinatari", destinatari.getIdentificador());
		long t0 = System.currentTimeMillis();
		try {
			String descripcio = "[" + expedient.getNumero() + "] " + expedient.getNom();
			String interessatMobil = null;
			if (destinatari.getTelefon() != null && isTelefonMobil(destinatari.getTelefon())) {
				interessatMobil = destinatari.getTelefon();
			}
			CiutadaExpedientInformacio expedientInfo = getCiutadaPlugin().expedientCrear(
					expedient.getNtiIdentificador(),
					metaExpedient.getUnitatAdministrativa(),
					metaExpedient.getClassificacioDocumental(),
					idioma,
					descripcio,
					toPluginCiutadaPersona(destinatari),
					null,
					expedient.getSistraBantelNum(),
					destinatari.isNotificacioAutoritzat(),
					destinatari.getEmail(),
					interessatMobil);
			integracioHelper.addAccioOk(
					IntegracioHelper.INTCODI_CIUTADA,
					accioDescripcio,
					accioParams,
					IntegracioAccioTipusEnumDto.ENVIAMENT,
					System.currentTimeMillis() - t0);
			return expedientInfo;
		} catch (Exception ex) {
			String errorDescripcio = "Error al accedir al plugin de comunicació amb el ciutadà";
			integracioHelper.addAccioError(
					IntegracioHelper.INTCODI_CIUTADA,
					accioDescripcio,
					accioParams,
					IntegracioAccioTipusEnumDto.ENVIAMENT,
					System.currentTimeMillis() - t0,
					errorDescripcio,
					ex);
			throw new SistemaExternException(
					IntegracioHelper.INTCODI_CIUTADA,
					errorDescripcio,
					ex);
		}
	}

	public void ciutadaAvisCrear(
			ExpedientEntity expedient,
			String titol,
			String text,
			String textMobil) {
		MetaExpedientEntity metaExpedient = expedient.getMetaExpedient();
		String accioDescripcio = "Creació d'un avis a la zona personal del ciutadà";
		Map<String, String> accioParams = new HashMap<String, String>();
		accioParams.put("expedientId", expedient.getId().toString());
		accioParams.put("expedientNumero", expedient.getNumero());
		accioParams.put("expedientTitol", expedient.getNom());
		accioParams.put("expedientTipusId", expedient.getMetaNode().getId().toString());
		accioParams.put("expedientTipusNom", expedient.getMetaNode().getNom());
		accioParams.put("titol", titol);
		accioParams.put("text", text);
		accioParams.put("textMobil", textMobil);
		long t0 = System.currentTimeMillis();
		try {
			getCiutadaPlugin().avisCrear(
					expedient.getNtiIdentificador(),
					metaExpedient.getUnitatAdministrativa(),
					titol,
					text,
					textMobil,
					null);
			integracioHelper.addAccioOk(
					IntegracioHelper.INTCODI_CIUTADA,
					accioDescripcio,
					accioParams,
					IntegracioAccioTipusEnumDto.ENVIAMENT,
					System.currentTimeMillis() - t0);
		} catch (Exception ex) {
			String errorDescripcio = "Error al accedir al plugin de comunicació amb el ciutadà";
			integracioHelper.addAccioError(
					IntegracioHelper.INTCODI_CIUTADA,
					accioDescripcio,
					accioParams,
					IntegracioAccioTipusEnumDto.ENVIAMENT,
					System.currentTimeMillis() - t0,
					errorDescripcio,
					ex);
			throw new SistemaExternException(
					IntegracioHelper.INTCODI_CIUTADA,
					errorDescripcio,
					ex);
		}
	}

	public CiutadaNotificacioResultat ciutadaNotificacioEnviar(
			ExpedientEntity expedient,
			InteressatEntity destinatari,
			String oficiTitol,
			String oficiText,
			String avisTitol,
			String avisText,
			String avisTextMobil,
			InteressatIdiomaEnumDto idioma,
			boolean confirmarRecepcio,
			List<DocumentEntity> annexos) {
		MetaExpedientEntity metaExpedient = expedient.getMetaExpedient();
		String accioDescripcio = "Enviament d'una notificació electrònica al ciutadà";
		Map<String, String> accioParams = new HashMap<String, String>();
		accioParams.put("expedientId", expedient.getId().toString());
		accioParams.put("expedientNumero", expedient.getNumero());
		accioParams.put("expedientTitol", expedient.getNom());
		accioParams.put("expedientTipusId", expedient.getMetaNode().getId().toString());
		accioParams.put("expedientTipusNom", expedient.getMetaNode().getNom());
		accioParams.put("unitatAdministrativa", metaExpedient.getUnitatAdministrativa());
		accioParams.put("llibreCodi", metaExpedient.getNotificacioLlibreCodi());
		accioParams.put("organCodi", metaExpedient.getNotificacioOrganCodi());
		accioParams.put("destinatari", (destinatari != null) ? destinatari.getIdentificador() : "<null>");
		accioParams.put("idioma", idioma.name());
		accioParams.put("oficiTitol", oficiTitol);
		accioParams.put("avisTitol", avisTitol);
		accioParams.put("confirmarRecepcio", new Boolean(confirmarRecepcio).toString());
		if (annexos != null)
			accioParams.put("annexos (núm.)", new Integer(annexos.size()).toString());
		if (annexos != null) {
			StringBuilder annexosIds = new StringBuilder();
			StringBuilder annexosTitols = new StringBuilder();
			boolean primer = true;
			for (DocumentEntity annex: annexos) {
				if (!primer) {
					annexosIds.append(", ");
					annexosTitols.append(", ");
				}
				annexosIds.append(annex.getId());
				annexosTitols.append(annex.getNom());
				primer = false;
			}
			accioParams.put("annexosIds", annexosIds.toString());
			accioParams.put("annexosTitols", annexosTitols.toString());
		}
		long t0 = System.currentTimeMillis();
		try {
			List<CiutadaDocument> ciutadaAnnexos = null;
			if (annexos != null) {
				ciutadaAnnexos = new ArrayList<CiutadaDocument>();
				for (DocumentEntity annex: annexos) {
					if (DocumentTipusEnumDto.FISIC.equals(annex.getDocumentTipus())) {
						throw new ValidationException(
								annex.getId(),
								DocumentEntity.class,
								"No espoden emprar documents físics com annexos d'una notificació telemàtica");
					}
					CiutadaDocument cdoc = new CiutadaDocument();
					cdoc.setTitol(annex.getNom());
					FitxerDto fitxer = documentHelper.getFitxerAssociat(annex);
					cdoc.setArxiuNom(fitxer.getNom());
					cdoc.setArxiuContingut(fitxer.getContingut());
					ciutadaAnnexos.add(cdoc);
				}
			}
			CiutadaNotificacioResultat resultat = getCiutadaPlugin().notificacioCrear(
					expedient.getNtiIdentificador(),
					expedient.getSistraUnitatAdministrativa(),
					metaExpedient.getNotificacioLlibreCodi(),
					metaExpedient.getNotificacioOrganCodi(),
					toPluginCiutadaPersona(destinatari),
					null,
					getIdiomaPerPluginCiutada(idioma),
					oficiTitol,
					oficiText,
					avisTitol,
					avisText,
					avisTextMobil,
					confirmarRecepcio,
					ciutadaAnnexos);
			integracioHelper.addAccioOk(
					IntegracioHelper.INTCODI_CIUTADA,
					accioDescripcio,
					accioParams,
					IntegracioAccioTipusEnumDto.ENVIAMENT,
					System.currentTimeMillis() - t0);
			return resultat;
		} catch (Exception ex) {
			String errorDescripcio = "Error al accedir al plugin de comunicació amb el ciutadà";
			integracioHelper.addAccioError(
					IntegracioHelper.INTCODI_CIUTADA,
					accioDescripcio,
					accioParams,
					IntegracioAccioTipusEnumDto.ENVIAMENT,
					System.currentTimeMillis() - t0,
					errorDescripcio,
					ex);
			throw new SistemaExternException(
					IntegracioHelper.INTCODI_CIUTADA,
					errorDescripcio,
					ex);
		}
	}

	public CiutadaNotificacioEstat ciutadaNotificacioComprovarEstat(
			ExpedientEntity expedient,
			String registreNumero) {
		String accioDescripcio = "Comprovació de l'estat de la notificació";
		Map<String, String> accioParams = new HashMap<String, String>();
		accioParams.put("expedientId", expedient.getId().toString());
		accioParams.put("expedientNumero", expedient.getNumero());
		accioParams.put("expedientTitol", expedient.getNom());
		accioParams.put("expedientTipusId", expedient.getMetaNode().getId().toString());
		accioParams.put("expedientTipusNom", expedient.getMetaNode().getNom());
		accioParams.put("registreNumero", registreNumero);
		long t0 = System.currentTimeMillis();
		try {
			CiutadaNotificacioEstat justificant = getCiutadaPlugin().notificacioObtenirJustificantRecepcio(
					registreNumero);
			integracioHelper.addAccioOk(
					IntegracioHelper.INTCODI_CIUTADA,
					accioDescripcio,
					accioParams,
					IntegracioAccioTipusEnumDto.ENVIAMENT,
					System.currentTimeMillis() - t0);
			return justificant;
		} catch (Exception ex) {
			String errorDescripcio = "Error al accedir al plugin de comunicació amb el ciutadà";
			integracioHelper.addAccioError(
					IntegracioHelper.INTCODI_CIUTADA,
					accioDescripcio,
					accioParams,
					IntegracioAccioTipusEnumDto.ENVIAMENT,
					System.currentTimeMillis() - t0,
					errorDescripcio,
					ex);
			throw new SistemaExternException(
					IntegracioHelper.INTCODI_CIUTADA,
					errorDescripcio,
					ex);
		}
	}

	public List<Pais> dadesExternesPaisosFindAll() {
		String accioDescripcio = "Consulta de tots els paisos";
		long t0 = System.currentTimeMillis();
		try {
			List<Pais> paisos = getDadesExternesPlugin().paisFindAll();
			integracioHelper.addAccioOk(
					IntegracioHelper.INTCODI_DADESEXT,
					accioDescripcio,
					null,
					IntegracioAccioTipusEnumDto.ENVIAMENT,
					System.currentTimeMillis() - t0);
			return paisos;
		} catch (Exception ex) {
			String errorDescripcio = "Error al accedir al plugin de dades externes";
			integracioHelper.addAccioError(
					IntegracioHelper.INTCODI_DADESEXT,
					accioDescripcio,
					null,
					IntegracioAccioTipusEnumDto.ENVIAMENT,
					System.currentTimeMillis() - t0,
					errorDescripcio,
					ex);
			throw new SistemaExternException(
					IntegracioHelper.INTCODI_DADESEXT,
					errorDescripcio,
					ex);
		}
	}

	public List<Comunitat> dadesExternesComunitatsFindAll() {
		String accioDescripcio = "Consulta de totes les comunitats";
		long t0 = System.currentTimeMillis();
		try {
			List<Comunitat> comunitats = getDadesExternesPlugin().comunitatFindAll();
			integracioHelper.addAccioOk(
					IntegracioHelper.INTCODI_DADESEXT,
					accioDescripcio,
					null,
					IntegracioAccioTipusEnumDto.ENVIAMENT,
					System.currentTimeMillis() - t0);
			return comunitats;
		} catch (Exception ex) {
			String errorDescripcio = "Error al accedir al plugin de dades externes";
			integracioHelper.addAccioError(
					IntegracioHelper.INTCODI_DADESEXT,
					accioDescripcio,
					null,
					IntegracioAccioTipusEnumDto.ENVIAMENT,
					System.currentTimeMillis() - t0,
					errorDescripcio,
					ex);
			throw new SistemaExternException(
					IntegracioHelper.INTCODI_DADESEXT,
					errorDescripcio,
					ex);
		}
	}
	
	public List<Provincia> dadesExternesProvinciesFindAll() {
		String accioDescripcio = "Consulta de totes les províncies";
		long t0 = System.currentTimeMillis();
		try {
			List<Provincia> provincies = getDadesExternesPlugin().provinciaFindAll();
			integracioHelper.addAccioOk(
					IntegracioHelper.INTCODI_DADESEXT,
					accioDescripcio,
					null,
					IntegracioAccioTipusEnumDto.ENVIAMENT,
					System.currentTimeMillis() - t0);
			return provincies;
		} catch (Exception ex) {
			String errorDescripcio = "Error al accedir al plugin de dades externes";
			integracioHelper.addAccioError(
					IntegracioHelper.INTCODI_DADESEXT,
					accioDescripcio,
					null,
					IntegracioAccioTipusEnumDto.ENVIAMENT,
					System.currentTimeMillis() - t0,
					errorDescripcio,
					ex);
			throw new SistemaExternException(
					IntegracioHelper.INTCODI_DADESEXT,
					errorDescripcio,
					ex);
		}
	}

	public List<Provincia> dadesExternesProvinciesFindAmbComunitat(
			String comunitatCodi) {
		String accioDescripcio = "Consulta de les províncies d'una comunitat";
		Map<String, String> accioParams = new HashMap<String, String>();
		accioParams.put("comunitatCodi", comunitatCodi);
		long t0 = System.currentTimeMillis();
		try {
			List<Provincia> provincies = getDadesExternesPlugin().provinciaFindByComunitat(comunitatCodi);
			integracioHelper.addAccioOk(
					IntegracioHelper.INTCODI_DADESEXT,
					accioDescripcio,
					null,
					IntegracioAccioTipusEnumDto.ENVIAMENT,
					System.currentTimeMillis() - t0);
			return provincies;
		} catch (Exception ex) {
			String errorDescripcio = "Error al accedir al plugin de dades externes";
			integracioHelper.addAccioError(
					IntegracioHelper.INTCODI_DADESEXT,
					accioDescripcio,
					accioParams,
					IntegracioAccioTipusEnumDto.ENVIAMENT,
					System.currentTimeMillis() - t0,
					errorDescripcio,
					ex);
			throw new SistemaExternException(
					IntegracioHelper.INTCODI_DADESEXT,
					errorDescripcio,
					ex);
		}
	}

//	public List<Municipi> dadesExternesMunicipisFindAll() {
//		String accioDescripcio = "Consulta de tots els municipis";
//		long t0 = System.currentTimeMillis();
//		try {
//			List<Municipi> municipis = getDadesExternesPlugin().municipiFindAll();
//			integracioHelper.addAccioOk(
//					IntegracioHelper.INTCODI_DADESEXT,
//					accioDescripcio,
//					null,
//					IntegracioAccioTipusEnumDto.ENVIAMENT,
//					System.currentTimeMillis() - t0);
//			return municipis;
//		} catch (Exception ex) {
//			String errorDescripcio = "Error al accedir al plugin de dades externes";
//			integracioHelper.addAccioError(
//					IntegracioHelper.INTCODI_DADESEXT,
//					accioDescripcio,
//					null,
//					IntegracioAccioTipusEnumDto.ENVIAMENT,
//					System.currentTimeMillis() - t0,
//					errorDescripcio,
//					ex);
//			throw new SistemaExternException(
//					IntegracioHelper.INTCODI_DADESEXT,
//					errorDescripcio,
//					ex);
//		}
//	}

	public List<Municipi> dadesExternesMunicipisFindAmbProvincia(
			String provinciaCodi) {
		String accioDescripcio = "Consulta dels municipis d'una província";
		Map<String, String> accioParams = new HashMap<String, String>();
		accioParams.put("provinciaCodi", provinciaCodi);
		long t0 = System.currentTimeMillis();
		try {
			List<Municipi> municipis = getDadesExternesPlugin().municipiFindByProvincia(provinciaCodi);
			integracioHelper.addAccioOk(
					IntegracioHelper.INTCODI_DADESEXT,
					accioDescripcio,
					null,
					IntegracioAccioTipusEnumDto.ENVIAMENT,
					System.currentTimeMillis() - t0);
			return municipis;
		} catch (Exception ex) {
			String errorDescripcio = "Error al accedir al plugin de dades externes";
			integracioHelper.addAccioError(
					IntegracioHelper.INTCODI_DADESEXT,
					accioDescripcio,
					accioParams,
					IntegracioAccioTipusEnumDto.ENVIAMENT,
					System.currentTimeMillis() - t0,
					errorDescripcio,
					ex);
			throw new SistemaExternException(
					IntegracioHelper.INTCODI_DADESEXT,
					errorDescripcio,
					ex);
		}
	}
	
	//////////////////////////////////////////////////////
	//	PLUGIN DE NOTIB									//
	//////////////////////////////////////////////////////
	public String notibNotificacioEnviar(
			XMLGregorianCalendar caducitat,
			String concepte,
			String descripcio,
			NotificacioDocument document,
			String emisorDir3Codi,
			XMLGregorianCalendar enviamentDataProgramada,
			NotificacioEnviamentTipusEnum enviamentTipus,
			NotificacioEnviament enviament,
			NotificacioPagadorCie pagadorCie,
			NotificacioPagadorPostal pagadorPostal,
			NotificacioParametresSeu parametresSeu,
			String procedimentCodi,
		    Integer retard) {
		
		try {
			return getNotibPlugin().notificacioAlta(
					caducitat,
					concepte,
					descripcio,
					document,
					emisorDir3Codi,
					enviamentDataProgramada,
					enviamentTipus,
					enviament,
					pagadorCie,
					pagadorPostal,
					parametresSeu,
					procedimentCodi,
					retard);
		} catch (Exception ex) {
			throw new SistemaExternException(
					IntegracioHelper.INTCODI_NOTIB,
					"Error al donar de alta a una notificació",
					ex);
		}
	}
	
	public NotificacioInformacioEnviament notibNotificacioComprovarEstat(
			String registreNumero) {
		
		try {
			return getNotibPlugin().notificacioConsultaEstat(
					registreNumero);
		} catch (Exception ex) {
			throw new SistemaExternException(
					IntegracioHelper.INTCODI_NOTIB,
					"Error al consultata una notificació (Número de registre:" + registreNumero + ")",
					ex);
		}
	}


	private ArbreNodeDto<UnitatOrganitzativaDto> getNodeArbreUnitatsOrganitzatives(
			UnitatOrganitzativa unitatOrganitzativa,
			List<UnitatOrganitzativa> unitatsOrganitzatives,
			ArbreNodeDto<UnitatOrganitzativaDto> pare) {
		ArbreNodeDto<UnitatOrganitzativaDto> resposta = new ArbreNodeDto<UnitatOrganitzativaDto>(
				pare,
				conversioTipusHelper.convertir(
						unitatOrganitzativa,
						UnitatOrganitzativaDto.class));
		String codiUnitat = (unitatOrganitzativa != null) ? unitatOrganitzativa.getCodi() : null;
		for (UnitatOrganitzativa uo: unitatsOrganitzatives) {
			if (	(codiUnitat == null && uo.getCodiUnitatSuperior() == null) ||
					(uo.getCodiUnitatSuperior() != null && uo.getCodiUnitatSuperior().equals(codiUnitat))) {
				resposta.addFill(
						getNodeArbreUnitatsOrganitzatives(
								uo,
								unitatsOrganitzatives,
								resposta));
			}
		}
		return resposta;
	}

	private CiutadaPersona toPluginCiutadaPersona(
			InteressatEntity interessat) {
		if (interessat == null)
			return null;
		if (	!InteressatDocumentTipusEnumDto.NIF.equals(interessat.getDocumentTipus()) &&
				!InteressatDocumentTipusEnumDto.CIF.equals(interessat.getDocumentTipus())) {
			throw new ValidationException(
					interessat.getId(),
					InteressatEntity.class,
					"No es pot notificar a interessats amb el tipus de document " + interessat.getDocumentTipus());
		}
		CiutadaPersona persona = new CiutadaPersona();
		if (interessat instanceof InteressatPersonaFisicaEntity) {
			InteressatPersonaFisicaEntity interessatPf = (InteressatPersonaFisicaEntity)interessat;
			persona.setNif(interessatPf.getDocumentNum());
			persona.setNom(interessatPf.getNom());
			persona.setLlinatge1(interessatPf.getLlinatge1());
			persona.setLlinatge2(interessatPf.getLlinatge2());
			persona.setPaisCodi(interessat.getDomiciliPaisCodiIso());
			persona.setProvinciaCodi(interessat.getDomiciliProvinciaCodi());
			persona.setMunicipiCodi(interessat.getDomiciliMunicipiCodiIne());
		} else if (interessat instanceof InteressatPersonaJuridicaEntity) {
			InteressatPersonaJuridicaEntity interessatPj = (InteressatPersonaJuridicaEntity)interessat;
			persona.setNif(interessatPj.getDocumentNum());
			persona.setNom(interessatPj.getRaoSocial());
			persona.setPaisCodi(interessat.getDomiciliPaisCodiIso());
			persona.setProvinciaCodi(interessat.getDomiciliProvinciaCodi());
			persona.setMunicipiCodi(interessat.getDomiciliMunicipiCodiIne());
		} else if (interessat instanceof InteressatAdministracioEntity) {
			throw new ValidationException(
					interessat.getId(),
					InteressatEntity.class,
					"Els interessats de les notificacions només poden ser persones físiques o jurídiques");
		}
		return persona;
	}

	private String getIdiomaPerPluginCiutada(InteressatIdiomaEnumDto idioma) {
		switch (idioma) {
		case CA:
			return "ca";
		case ES:
			return "es";
		default:
			return "ca";
		}
	}

	private boolean isTelefonMobil(String telefon) {
		return MOBIL_PATTERN.matcher(telefon).matches();
	}

	private Long toLongValue(String text) {
		if (text == null || text.isEmpty())
			return null;
		return Long.parseLong(text);
	}

	private Expedient toArxiuExpedient(
			String identificador,
			String nom,
			String ntiIdentificador,
			List<String> ntiOrgans,
			Date ntiDataObertura,
			String ntiClassificacio,
			ExpedientEstatEnumDto ntiEstat,
			List<String> ntiInteressats,
			String serieDocumental) {
		Expedient expedient = new Expedient();
		expedient.setNom(nom);
		expedient.setIdentificador(identificador);
		ExpedientMetadades metadades = new ExpedientMetadades();
		metadades.setIdentificador(ntiIdentificador);
		metadades.setDataObertura(ntiDataObertura);
		metadades.setClassificacio(ntiClassificacio);
		if (ntiEstat != null) {
			switch (ntiEstat) {
			case OBERT:
				metadades.setEstat(ExpedientEstat.OBERT);
				break;
			case TANCAT:
				metadades.setEstat(ExpedientEstat.TANCAT);
				break;
			case INDEX_REMISSIO:
				metadades.setEstat(ExpedientEstat.INDEX_REMISSIO);
				break;
			}
		}
		metadades.setOrgans(ntiOrgans);
		metadades.setInteressats(ntiInteressats);
		metadades.setSerieDocumental(serieDocumental);
		expedient.setMetadades(metadades);
		return expedient;
	}

	private Document toArxiuDocument(
			String identificador,
			String nom,
			FitxerDto fitxer,
			FitxerDto firmaPdf,
			String ntiIdentificador,
			NtiOrigenEnumDto ntiOrigen,
			List<String> ntiOrgans,
			Date ntiDataCaptura,
			DocumentNtiEstadoElaboracionEnumDto ntiEstatElaboracio,
			DocumentNtiTipoDocumentalEnumDto ntiTipusDocumental,
			String serieDocumental,
			DocumentEstat estat,
			boolean enPaper) {
		Document document = new Document();
		document.setNom(nom);
		document.setIdentificador(identificador);
		DocumentMetadades metadades = new DocumentMetadades();
		metadades.setIdentificador(ntiIdentificador);
		if (ntiOrigen != null) {
			switch (ntiOrigen) {
			case O0:
				metadades.setOrigen(ContingutOrigen.CIUTADA);
				break;
			case O1:
				metadades.setOrigen(ContingutOrigen.ADMINISTRACIO);
				break;
			}
		}
		metadades.setDataCaptura(ntiDataCaptura);
		DocumentEstatElaboracio estatElaboracio = null;
		switch (ntiEstatElaboracio) {
		case EE01:
			estatElaboracio = DocumentEstatElaboracio.ORIGINAL;
			break;
		case EE02:
			estatElaboracio = DocumentEstatElaboracio.COPIA_CF;
			break;
		case EE03:
			estatElaboracio = DocumentEstatElaboracio.COPIA_DP;
			break;
		case EE04:
			estatElaboracio = DocumentEstatElaboracio.COPIA_PR;
			break;
		case EE99:
			estatElaboracio = DocumentEstatElaboracio.ALTRES;
			break;
		}
		metadades.setEstatElaboracio(estatElaboracio);
		DocumentTipus tipusDocumental = null;
		switch (ntiTipusDocumental) {
		case TD01:
			tipusDocumental = DocumentTipus.RESOLUCIO;
			break;
		case TD02:
			tipusDocumental = DocumentTipus.ACORD;
			break;
		case TD03:
			tipusDocumental = DocumentTipus.CONTRACTE;
			break;
		case TD04:
			tipusDocumental = DocumentTipus.CONVENI;
			break;
		case TD05:
			tipusDocumental = DocumentTipus.DECLARACIO;
			break;
		case TD06:
			tipusDocumental = DocumentTipus.COMUNICACIO;
			break;
		case TD07:
			tipusDocumental = DocumentTipus.NOTIFICACIO;
			break;
		case TD08:
			tipusDocumental = DocumentTipus.PUBLICACIO;
			break;
		case TD09:
			tipusDocumental = DocumentTipus.JUSTIFICANT_RECEPCIO;
			break;
		case TD10:
			tipusDocumental = DocumentTipus.ACTA;
			break;
		case TD11:
			tipusDocumental = DocumentTipus.CERTIFICAT;
			break;
		case TD12:
			tipusDocumental = DocumentTipus.DILIGENCIA;
			break;
		case TD13:
			tipusDocumental = DocumentTipus.INFORME;
			break;
		case TD14:
			tipusDocumental = DocumentTipus.SOLICITUD;
			break;
		case TD15:
			tipusDocumental = DocumentTipus.DENUNCIA;
			break;
		case TD16:
			tipusDocumental = DocumentTipus.ALEGACIO;
			break;
		case TD17:
			tipusDocumental = DocumentTipus.RECURS;
			break;
		case TD18:
			tipusDocumental = DocumentTipus.COMUNICACIO_CIUTADA;
			break;
		case TD19:
			tipusDocumental = DocumentTipus.FACTURA;
			break;
		case TD20:
			tipusDocumental = DocumentTipus.ALTRES_INCAUTATS;
			break;
		default:
			tipusDocumental = DocumentTipus.ALTRES;
			break;
		}
		metadades.setTipusDocumental(tipusDocumental);
		DocumentExtensio extensio = null;
		DocumentContingut contingut = null;
		if (fitxer != null && !enPaper) {
			String fitxerExtensio = fitxer.getExtensio();
			String extensioAmbPunt = (fitxerExtensio.startsWith(".")) ? fitxerExtensio.toLowerCase() : "." + fitxerExtensio.toLowerCase();
			extensio = DocumentExtensio.toEnum(extensioAmbPunt);
			contingut = new DocumentContingut();
			contingut.setArxiuNom(fitxer.getNom());
			contingut.setContingut(fitxer.getContingut());
			contingut.setTipusMime(fitxer.getContentType());
			document.setContingut(contingut);
		}
		if (firmaPdf != null) {
			Firma firmaPades = new Firma();
			firmaPades.setTipus(FirmaTipus.PADES);
			firmaPades.setPerfil(FirmaPerfil.EPES);
			firmaPades.setTipusMime("application/pdf");
			firmaPades.setFitxerNom(firmaPdf.getNom());
			firmaPades.setContingut(firmaPdf.getContingut());
			document.setFirmes(Arrays.asList(firmaPades));
			extensio = DocumentExtensio.PDF;
		}
		if (extensio != null) {
			metadades.setExtensio(extensio);
			DocumentFormat format = null;
			switch (extensio) {
			case AVI:
				format = DocumentFormat.AVI;
				break;
			case CSS:
				format = DocumentFormat.CSS;
				break;
			case CSV:
				format = DocumentFormat.CSV;
				break;
			case DOCX:
				format = DocumentFormat.SOXML;
				break;
			case GML:
				format = DocumentFormat.GML;
				break;
			case GZ:
				format = DocumentFormat.GZIP;
				break;
			case HTM:
				format = DocumentFormat.XHTML; // HTML o XHTML!!!
				break;
			case HTML:
				format = DocumentFormat.XHTML; // HTML o XHTML!!!
				break;
			case JPEG:
				format = DocumentFormat.JPEG;
				break;
			case JPG:
				format = DocumentFormat.JPEG;
				break;
			case MHT:
				format = DocumentFormat.MHTML;
				break;
			case MHTML:
				format = DocumentFormat.MHTML;
				break;
			case MP3:
				format = DocumentFormat.MP3;
				break;
			case MP4:
				format = DocumentFormat.MP4V; // MP4A o MP4V!!!
				break;
			case MPEG:
				format = DocumentFormat.MP4V; // MP4A o MP4V!!!
				break;
			case ODG:
				format = DocumentFormat.OASIS12;
				break;
			case ODP:
				format = DocumentFormat.OASIS12;
				break;
			case ODS:
				format = DocumentFormat.OASIS12;
				break;
			case ODT:
				format = DocumentFormat.OASIS12;
				break;
			case OGA:
				format = DocumentFormat.OGG;
				break;
			case OGG:
				format = DocumentFormat.OGG;
				break;
			case PDF:
				format = DocumentFormat.PDF; // PDF o PDFA!!!
				break;
			case PNG:
				format = DocumentFormat.PNG;
				break;
			case PPTX:
				format = DocumentFormat.SOXML;
				break;
			case RTF:
				format = DocumentFormat.RTF;
				break;
			case SVG:
				format = DocumentFormat.SVG;
				break;
			case TIFF:
				format = DocumentFormat.TIFF;
				break;
			case TXT:
				format = DocumentFormat.TXT;
				break;
			case WEBM:
				format = DocumentFormat.WEBM;
				break;
			case XLSX:
				format = DocumentFormat.SOXML;
				break;
			case ZIP:
				format = DocumentFormat.ZIP;
				break;
			}
			metadades.setFormat(format);
		}
		metadades.setOrgans(ntiOrgans);
		metadades.addMetadadaAddicional(
				"eni:cod_clasificacion",
				serieDocumental);
		document.setMetadades(metadades);
		document.setContingut(contingut);
		document.setEstat(estat);
		return document;
	}

	private Carpeta toArxiuCarpeta(
			String identificador,
			String nom) {
		Carpeta carpeta = new Carpeta();
		carpeta.setIdentificador(identificador);
		carpeta.setNom(nom);
		return carpeta;
	}

	private DocumentExtensio getArxiuFormatExtensio(String extensio) {
		String extensioAmbPunt = (extensio.startsWith(".")) ? extensio.toLowerCase() : "." + extensio.toLowerCase();
		return DocumentExtensio.toEnum(extensioAmbPunt);
	}

	/*private ArxiuCapsalera generarCapsaleraArxiu(
			ContingutEntity contingut) {
		ArxiuCapsalera capsaleraTest = new ArxiuCapsalera();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		capsaleraTest.setFuncionariNom(auth.getName());
		capsaleraTest.setFuncionariOrgan(
				contingut.getEntitat().getUnitatArrel());
		return capsaleraTest;
	}*/

	private void propagarMetadadesExpedient(
			Expedient expedientArxiu,
			ExpedientEntity expedientDb) {
		List<String> metadadaOrgans = expedientArxiu.getMetadades().getOrgans();
		String organs = null;
		if (expedientArxiu.getMetadades().getOrgans() != null) {
			StringBuilder organsSb = new StringBuilder();
			boolean primer = true;
			for (String organ: metadadaOrgans) {
				organsSb.append(organ);
				if (primer || metadadaOrgans.size() == 1) {
					primer = false;
				} else {
					organsSb.append(",");
				}
			}
			organs = organsSb.toString();
		}
		expedientDb.updateNti(
				obtenirNumeroVersioEniExpedient(
						expedientArxiu.getMetadades().getVersioNti()),
				expedientArxiu.getMetadades().getIdentificador(),
				organs,
				expedientArxiu.getMetadades().getDataObertura(),
				expedientArxiu.getMetadades().getClassificacio());
	}

	private static final String ENI_EXPEDIENT_PREFIX = "http://administracionelectronica.gob.es/ENI/XSD/v";
	private String obtenirNumeroVersioEniExpedient(String versio) {
		if (versio != null) {
			if (versio.startsWith(ENI_EXPEDIENT_PREFIX)) {
				int indexBarra = versio.indexOf("/", ENI_EXPEDIENT_PREFIX.length());
				return versio.substring(ENI_EXPEDIENT_PREFIX.length(), indexBarra);
			}
		}
		return null;
	}

	private void propagarMetadadesDocument(
			Document documentArxiu,
			DocumentEntity documentDb) {
		List<String> metadadaOrgans = documentArxiu.getMetadades().getOrgans();
		String organs = null;
		if (documentArxiu.getMetadades().getOrgans() != null) {
			StringBuilder organsSb = new StringBuilder();
			boolean primer = true;
			for (String organ: metadadaOrgans) {
				organsSb.append(organ);
				if (primer || metadadaOrgans.size() == 1) {
					primer = false;
				} else {
					organsSb.append(",");
				}
			}
			organs = organsSb.toString();
		}
		NtiOrigenEnumDto origen = null;
		ContingutOrigen metadadaOrigen = documentArxiu.getMetadades().getOrigen();
		if (metadadaOrigen != null) {
			switch (metadadaOrigen) {
			case CIUTADA:
				origen = NtiOrigenEnumDto.O0;
				break;
			case ADMINISTRACIO:
				origen = NtiOrigenEnumDto.O1;
				break;
			}
		}
		DocumentNtiEstadoElaboracionEnumDto ntiEstadoElaboracion = null;
		if (documentArxiu.getMetadades().getEstatElaboracio() != null) {
			switch (documentArxiu.getMetadades().getEstatElaboracio()) {
			case ORIGINAL:
				ntiEstadoElaboracion = DocumentNtiEstadoElaboracionEnumDto.EE01;
				break;
			case COPIA_CF:
				ntiEstadoElaboracion = DocumentNtiEstadoElaboracionEnumDto.EE02;
				break;
			case COPIA_DP:
				ntiEstadoElaboracion = DocumentNtiEstadoElaboracionEnumDto.EE03;
				break;
			case COPIA_PR:
				ntiEstadoElaboracion = DocumentNtiEstadoElaboracionEnumDto.EE04;
				break;
			case ALTRES:
				ntiEstadoElaboracion = DocumentNtiEstadoElaboracionEnumDto.EE99;
				break;
			}
		}
		DocumentNtiTipoDocumentalEnumDto ntiTipoDocumental = null;
		if (documentArxiu.getMetadades().getTipusDocumental() != null) {
			switch (documentArxiu.getMetadades().getTipusDocumental()) {
			case RESOLUCIO:
				ntiTipoDocumental = DocumentNtiTipoDocumentalEnumDto.TD01;
				break;
			case ACORD:
				ntiTipoDocumental = DocumentNtiTipoDocumentalEnumDto.TD02;
				break;
			case CONTRACTE:
				ntiTipoDocumental = DocumentNtiTipoDocumentalEnumDto.TD03;
				break;
			case CONVENI:
				ntiTipoDocumental = DocumentNtiTipoDocumentalEnumDto.TD04;
				break;
			case DECLARACIO:
				ntiTipoDocumental = DocumentNtiTipoDocumentalEnumDto.TD05;
				break;
			case COMUNICACIO:
				ntiTipoDocumental = DocumentNtiTipoDocumentalEnumDto.TD06;
				break;
			case NOTIFICACIO:
				ntiTipoDocumental = DocumentNtiTipoDocumentalEnumDto.TD07;
				break;
			case PUBLICACIO:
				ntiTipoDocumental = DocumentNtiTipoDocumentalEnumDto.TD08;
				break;
			case JUSTIFICANT_RECEPCIO:
				ntiTipoDocumental = DocumentNtiTipoDocumentalEnumDto.TD09;
				break;
			case ACTA:
				ntiTipoDocumental = DocumentNtiTipoDocumentalEnumDto.TD10;
				break;
			case CERTIFICAT:
				ntiTipoDocumental = DocumentNtiTipoDocumentalEnumDto.TD11;
				break;
			case DILIGENCIA:
				ntiTipoDocumental = DocumentNtiTipoDocumentalEnumDto.TD12;
				break;
			case INFORME:
				ntiTipoDocumental = DocumentNtiTipoDocumentalEnumDto.TD13;
				break;
			case SOLICITUD:
				ntiTipoDocumental = DocumentNtiTipoDocumentalEnumDto.TD14;
				break;
			case DENUNCIA:
				ntiTipoDocumental = DocumentNtiTipoDocumentalEnumDto.TD15;
				break;
			case ALEGACIO:
				ntiTipoDocumental = DocumentNtiTipoDocumentalEnumDto.TD16;
				break;
			case RECURS:
				ntiTipoDocumental = DocumentNtiTipoDocumentalEnumDto.TD17;
				break;
			case COMUNICACIO_CIUTADA:
				ntiTipoDocumental = DocumentNtiTipoDocumentalEnumDto.TD18;
				break;
			case FACTURA:
				ntiTipoDocumental = DocumentNtiTipoDocumentalEnumDto.TD19;
				break;
			case ALTRES_INCAUTATS:
				ntiTipoDocumental = DocumentNtiTipoDocumentalEnumDto.TD20;
				break;
			case ALTRES:
				ntiTipoDocumental = DocumentNtiTipoDocumentalEnumDto.TD99;
				break;
			}
		}
		DocumentNtiTipoFirmaEnumDto ntiTipoFirma = null;
		if (documentArxiu.getFirmes() != null && !documentArxiu.getFirmes().isEmpty()) {
			Firma firma = documentArxiu.getFirmes().get(0);
			switch (firma.getTipus()) {
			case CSV:
				ntiTipoFirma = DocumentNtiTipoFirmaEnumDto.TF01;
				break;
			case XADES_DET:
				ntiTipoFirma = DocumentNtiTipoFirmaEnumDto.TF02;
				break;
			case XADES_ENV:
				ntiTipoFirma = DocumentNtiTipoFirmaEnumDto.TF03;
				break;
			case CADES_DET:
				ntiTipoFirma = DocumentNtiTipoFirmaEnumDto.TF04;
				break;
			case CADES_ATT:
				ntiTipoFirma = DocumentNtiTipoFirmaEnumDto.TF05;
				break;
			case PADES:
				ntiTipoFirma = DocumentNtiTipoFirmaEnumDto.TF06;
				break;
			case SMIME:
				ntiTipoFirma = DocumentNtiTipoFirmaEnumDto.TF07;
				break;
			case ODT:
				ntiTipoFirma = DocumentNtiTipoFirmaEnumDto.TF08;
				break;
			case OOXML:
				ntiTipoFirma = DocumentNtiTipoFirmaEnumDto.TF09;
				break;
			
			}
		}
		documentDb.updateNti(
				obtenirNumeroVersioEniDocument(
						documentArxiu.getMetadades().getVersioNti()),
				documentArxiu.getMetadades().getIdentificador(),
				organs,
				origen,
				ntiEstadoElaboracion,
				ntiTipoDocumental,
				documentArxiu.getMetadades().getIdentificadorOrigen(),
				ntiTipoFirma,
				null,
				null);
	}
	private static final String ENI_DOCUMENT_PREFIX = "http://administracionelectronica.gob.es/ENI/XSD/v";
	private String obtenirNumeroVersioEniDocument(String versio) {
		if (versio != null) {
			if (versio.startsWith(ENI_DOCUMENT_PREFIX)) {
				int indexBarra = versio.indexOf("/", ENI_DOCUMENT_PREFIX.length());
				return versio.substring(ENI_DOCUMENT_PREFIX.length(), indexBarra);
			}
		}
		return null;
	}

	private DadesUsuariPlugin getDadesUsuariPlugin() {
		if (dadesUsuariPlugin == null) {
			String pluginClass = getPropertyPluginDadesUsuari();
			if (pluginClass != null && pluginClass.length() > 0) {
				try {
					Class<?> clazz = Class.forName(pluginClass);
					dadesUsuariPlugin = (DadesUsuariPlugin)clazz.newInstance();
				} catch (Exception ex) {
					throw new SistemaExternException(
							IntegracioHelper.INTCODI_USUARIS,
							"Error al crear la instància del plugin de dades d'usuari",
							ex);
				}
			} else {
				throw new SistemaExternException(
						IntegracioHelper.INTCODI_USUARIS,
						"La classe del plugin de dades d'usuari no està configurada");
			}
		}
		return dadesUsuariPlugin;
	}
	private UnitatsOrganitzativesPlugin getUnitatsOrganitzativesPlugin() {
		if (unitatsOrganitzativesPlugin == null) {
			String pluginClass = getPropertyPluginUnitatsOrganitzatives();
			if (pluginClass != null && pluginClass.length() > 0) {
				try {
					Class<?> clazz = Class.forName(pluginClass);
					unitatsOrganitzativesPlugin = (UnitatsOrganitzativesPlugin)clazz.newInstance();
				} catch (Exception ex) {
					throw new SistemaExternException(
							IntegracioHelper.INTCODI_UNITATS,
							"Error al crear la instància del plugin d'unitats organitzatives",
							ex);
				}
			} else {
				throw new SistemaExternException(
						IntegracioHelper.INTCODI_UNITATS,
						"La classe del plugin d'unitats organitzatives no està configurada");
			}
		}
		return unitatsOrganitzativesPlugin;
	}
	/*private GestioDocumentalPlugin getGestioDocumentalPlugin() {
		if (!gestioDocumentalPluginConfigurat) {
			String pluginClass = getPropertyPluginGestioDocumental();
			if (pluginClass != null && pluginClass.length() > 0) {
				try {
					Class<?> clazz = Class.forName(pluginClass);
					gestioDocumentalPlugin = (GestioDocumentalPlugin)clazz.newInstance();
				} catch (Exception ex) {
					throw new SistemaExternException(
							IntegracioHelper.INTCODI_GESDOC,
							"Error al crear la instància del plugin de gestió documental",
							ex);
				}
			}
			gestioDocumentalPluginConfigurat = true;
		}
		return gestioDocumentalPlugin;
	}
	private ArxiuPlugin getArxiuPlugin() {
		if (arxiuPlugin == null) {
			String pluginClass = getPropertyPluginArxiu();
			if (pluginClass != null && pluginClass.length() > 0) {
				try {
					Class<?> clazz = Class.forName(pluginClass);
					arxiuPlugin = (ArxiuPlugin)clazz.newInstance();
				} catch (Exception ex) {
					throw new SistemaExternException(
							IntegracioHelper.INTCODI_ARXIU,
							"Error al crear la instància del plugin d'arxiu digital",
							ex);
				}
			}
		}
		return arxiuPlugin;
	}*/
	private IArxiuPlugin getArxiuPlugin() {
		if (arxiuPlugin == null) {
			String pluginClass = getPropertyPluginArxiu();
			if (pluginClass != null && pluginClass.length() > 0) {
				try {
					Class<?> clazz = Class.forName(pluginClass);
					if (PropertiesHelper.getProperties().isLlegirSystem()) {
						arxiuPlugin = (IArxiuPlugin)clazz.getDeclaredConstructor(
								String.class).newInstance(
								"es.caib.ripea.");
					} else {
						arxiuPlugin = (IArxiuPlugin)clazz.getDeclaredConstructor(
								String.class,
								Properties.class).newInstance(
								"es.caib.ripea.",
								PropertiesHelper.getProperties().findAll());
					}
					
				} catch (Exception ex) {
					throw new SistemaExternException(
							IntegracioHelper.INTCODI_ARXIU,
							"Error al crear la instància del plugin d'arxiu digital",
							ex);
				}
			}
		}
		return arxiuPlugin;
	}
	private PortafirmesPlugin getPortafirmesPlugin() {
		if (portafirmesPlugin == null) {
			String pluginClass = getPropertyPluginPortafirmes();
			if (pluginClass != null && pluginClass.length() > 0) {
				try {
					Class<?> clazz = Class.forName(pluginClass);
					portafirmesPlugin = (PortafirmesPlugin)clazz.newInstance();
				} catch (Exception ex) {
					throw new SistemaExternException(
							IntegracioHelper.INTCODI_PFIRMA,
							"Error al crear la instància del plugin de portafirmes",
							ex);
				}
			}
		}
		return portafirmesPlugin;
	}
	private ConversioPlugin getConversioPlugin() {
		if (conversioPlugin == null) {
			String pluginClass = getPropertyPluginConversio();
			if (pluginClass != null && pluginClass.length() > 0) {
				try {
					Class<?> clazz = Class.forName(pluginClass);
					conversioPlugin = (ConversioPlugin)clazz.newInstance();
				} catch (Exception ex) {
					throw new SistemaExternException(
							IntegracioHelper.INTCODI_CONVERT,
							"Error al crear la instància del plugin de conversió de documents",
							ex);
				}
			}
		}
		return conversioPlugin;
	}
	/*private CustodiaPlugin getCustodiaPlugin() {
		if (custodiaPlugin == null) {
			String pluginClass = getPropertyPluginCustodia();
			if (pluginClass != null && pluginClass.length() > 0) {
				try {
					Class<?> clazz = Class.forName(pluginClass);
					custodiaPlugin = (CustodiaPlugin)clazz.newInstance();
				} catch (Exception ex) {
					throw new SistemaExternException(
							IntegracioHelper.INTCODI_CUSTODIA,
							"Error al crear la instància del plugin de custòdia de documents",
							ex);
				}
			}
		}
		return custodiaPlugin;
	}*/
	private RegistrePlugin getRegistrePlugin() {
		if (registrePlugin == null) {
			String pluginClass = getPropertyPluginRegistre();
			if (pluginClass != null && pluginClass.length() > 0) {
				try {
					Class<?> clazz = Class.forName(pluginClass);
					registrePlugin = (RegistrePlugin)clazz.newInstance();
				} catch (Exception ex) {
					throw new SistemaExternException(
							IntegracioHelper.INTCODI_REGISTRE,
							"Error al crear la instància del plugin de registre",
							ex);
				}
			}
		}
		return registrePlugin;
	}
	private CiutadaPlugin getCiutadaPlugin() {
		if (ciutadaPlugin == null) {
			String pluginClass = getPropertyPluginCiutada();
			if (pluginClass != null && pluginClass.length() > 0) {
				try {
					Class<?> clazz = Class.forName(pluginClass);
					ciutadaPlugin = (CiutadaPlugin)clazz.newInstance();
				} catch (Exception ex) {
					throw new SistemaExternException(
							IntegracioHelper.INTCODI_CIUTADA,
							"Error al crear la instància del plugin de comunicació amb el ciutadà",
							ex);
				}
			}
		}
		return ciutadaPlugin;
	}
	private DadesExternesPlugin getDadesExternesPlugin() {
		if (dadesExternesPlugin == null) {
			String pluginClass = getPropertyPluginDadesExternes();
			if (pluginClass != null && pluginClass.length() > 0) {
				try {
					Class<?> clazz = Class.forName(pluginClass);
					dadesExternesPlugin = (DadesExternesPlugin)clazz.newInstance();
				} catch (Exception ex) {
					throw new SistemaExternException(
							IntegracioHelper.INTCODI_CIUTADA,
							"Error al crear la instància del plugin de consulta de dades externes",
							ex);
				}
			}
		}
		return dadesExternesPlugin;
	}
	
	private NotificacioPlugin getNotibPlugin() {
		if (notificacioPlugin == null) {
			String pluginClass = getPropertyPluginNotificacio();
			if (pluginClass != null && pluginClass.length() > 0) {
				try {
					Class<?> clazz = Class.forName(pluginClass);
					notificacioPlugin = (NotificacioPlugin)clazz.newInstance();
				} catch (Exception ex) {
					throw new SistemaExternException(
							IntegracioHelper.INTCODI_NOTIB,
							"Error al crear la instància del plugin de notificació amb Notib (Plugin class: " + pluginClass + ")",
							ex);
				}
			}
		}
		return notificacioPlugin;
	}

	private String getPropertyPluginDadesUsuari() {
		return PropertiesHelper.getProperties().getProperty("es.caib.ripea.plugin.dades.usuari.class");
	}
	private String getPropertyPluginUnitatsOrganitzatives() {
		return PropertiesHelper.getProperties().getProperty("es.caib.ripea.plugin.unitats.organitzatives.class");
	}
	/*private String getPropertyPluginGestioDocumental() {
		return PropertiesHelper.getProperties().getProperty("es.caib.ripea.plugin.gesdoc.class");
	}
	private String getPropertyPluginCustodia() {
		return PropertiesHelper.getProperties().getProperty("es.caib.ripea.plugin.custodia.class");
	}*/
	private String getPropertyPluginArxiu() {
		return PropertiesHelper.getProperties().getProperty("es.caib.ripea.plugin.arxiu.class");
	}
	private String getPropertyPluginPortafirmes() {
		return PropertiesHelper.getProperties().getProperty("es.caib.ripea.plugin.portafirmes.class");
	}
	private String getPropertyPluginConversio() {
		return PropertiesHelper.getProperties().getProperty("es.caib.ripea.plugin.conversio.class");
	}
	private String getPropertyPluginRegistre() {
		return PropertiesHelper.getProperties().getProperty("es.caib.ripea.plugin.registre.class");
	}
	private String getPropertyPluginCiutada() {
		return PropertiesHelper.getProperties().getProperty("es.caib.ripea.plugin.ciutada.class");
	}
	private String getPropertyPluginDadesExternes() {
		return PropertiesHelper.getProperties().getProperty("es.caib.ripea.plugin.dadesext.class");
	}

	private String getPropertyPluginArxiuEscriptoriClassificacio() {
		return PropertiesHelper.getProperties().getProperty("es.caib.ripea.plugin.arxiu.escriptori.classificacio");
	}
	private String getPropertyPluginArxiuEscriptoriSerieDocumental() {
		return PropertiesHelper.getProperties().getProperty("es.caib.ripea.plugin.arxiu.escriptori.serie.documental");
	}
	
	private String getPropertyPluginNotificacio() {
		return PropertiesHelper.getProperties().getProperty("es.caib.ripea.plugin.notificacio.class");
	}

}
