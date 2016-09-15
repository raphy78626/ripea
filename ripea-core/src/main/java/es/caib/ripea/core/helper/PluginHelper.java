/**
 * 
 */
package es.caib.ripea.core.helper;

import java.io.ByteArrayInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import es.caib.ripea.core.api.dto.ArbreDto;
import es.caib.ripea.core.api.dto.ArbreNodeDto;
import es.caib.ripea.core.api.dto.FitxerDto;
import es.caib.ripea.core.api.dto.IntegracioAccioTipusEnumDto;
import es.caib.ripea.core.api.dto.MetaDocumentFirmaFluxTipusEnumDto;
import es.caib.ripea.core.api.dto.PortafirmesDocumentTipusDto;
import es.caib.ripea.core.api.dto.UnitatOrganitzativaDto;
import es.caib.ripea.core.api.exception.SistemaExternException;
import es.caib.ripea.core.entity.DocumentEntity;
import es.caib.ripea.core.entity.DocumentPortafirmesEntity;
import es.caib.ripea.core.entity.DocumentVersioEntity;
import es.caib.ripea.core.entity.ExpedientEntity;
import es.caib.ripea.core.entity.InteressatEntity;
import es.caib.ripea.core.entity.InteressatPersonaFisicaEntity;
import es.caib.ripea.core.entity.MetaDocumentEntity;
import es.caib.ripea.plugin.ciutada.CiutadaPersona;
import es.caib.ripea.plugin.ciutada.CiutadaPlugin;
import es.caib.ripea.plugin.conversio.ConversioArxiu;
import es.caib.ripea.plugin.conversio.ConversioPlugin;
import es.caib.ripea.plugin.custodia.CustodiaPlugin;
import es.caib.ripea.plugin.gesdoc.GestioDocumentalArxiu;
import es.caib.ripea.plugin.gesdoc.GestioDocumentalPlugin;
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
	boolean gestioDocumentalPluginConfigurat = false;
	private GestioDocumentalPlugin gestioDocumentalPlugin;
	private PortafirmesPlugin portafirmesPlugin;
	private ConversioPlugin conversioPlugin;
	private CustodiaPlugin custodiaPlugin;
	private RegistrePlugin registrePlugin;
	private CiutadaPlugin ciutadaPlugin;

	@Resource
	private ConversioTipusHelper conversioTipusHelper;
	@Resource
	private IntegracioHelper integracioHelper;
	@Resource
	private DocumentHelper documentHelper;



	public DadesUsuari dadesUsuariConsultarAmbCodi(
			String usuariCodi) {
		String accioDescripcio = "Consulta d'usuari amb codi";
		Map<String, String> accioParams = new HashMap<String, String>();
		accioParams.put("codi", usuariCodi);
		try {
			DadesUsuari dadesUsuari = getDadesUsuariPlugin().consultarAmbUsuariCodi(
					usuariCodi);
			integracioHelper.addAccioOk(
					IntegracioHelper.INTCODI_USUARIS,
					accioDescripcio,
					accioParams,
					IntegracioAccioTipusEnumDto.ENVIAMENT);
			return dadesUsuari;
		} catch (Exception ex) {
			String errorDescripcio = "Error al accedir al plugin de dades d'usuari";
			integracioHelper.addAccioError(
					IntegracioHelper.INTCODI_USUARIS,
					accioDescripcio,
					accioParams,
					IntegracioAccioTipusEnumDto.ENVIAMENT,
					errorDescripcio,
					ex);
			throw new SistemaExternException(
					IntegracioHelper.INTCODI_USUARIS,
					errorDescripcio,
					ex);
		}
	}
	public DadesUsuari dadesUsuariConsultarAmbNif(
			String usuariNif) {
		String accioDescripcio = "Consulta d'usuari amb NIF";
		Map<String, String> accioParams = new HashMap<String, String>();
		accioParams.put("nif", usuariNif);
		try {
			DadesUsuari dadesUsuari = getDadesUsuariPlugin().consultarAmbUsuariNif(
					usuariNif);
			integracioHelper.addAccioOk(
					IntegracioHelper.INTCODI_USUARIS,
					accioDescripcio,
					accioParams,
					IntegracioAccioTipusEnumDto.ENVIAMENT);
			return dadesUsuari;
		} catch (Exception ex) {
			String errorDescripcio = "Error al accedir al plugin de dades d'usuari";
			integracioHelper.addAccioError(
					IntegracioHelper.INTCODI_USUARIS,
					accioDescripcio,
					accioParams,
					IntegracioAccioTipusEnumDto.ENVIAMENT,
					errorDescripcio,
					ex);
			throw new SistemaExternException(
					IntegracioHelper.INTCODI_USUARIS,
					errorDescripcio,
					ex);
		}
	}
	public List<DadesUsuari> dadesUsuariConsultarAmbGrup(
			String grupCodi) {
		String accioDescripcio = "Consulta d'usuaris d'un grup";
		Map<String, String> accioParams = new HashMap<String, String>();
		accioParams.put("grup", grupCodi);
		try {
			List<DadesUsuari> dadesUsuari = getDadesUsuariPlugin().findUsuarisPerGrup(
					grupCodi);
			integracioHelper.addAccioOk(
					IntegracioHelper.INTCODI_USUARIS,
					accioDescripcio,
					accioParams,
					IntegracioAccioTipusEnumDto.ENVIAMENT);
			return dadesUsuari;
		} catch (Exception ex) {
			String errorDescripcio = "Error al accedir al plugin de dades d'usuari";
			integracioHelper.addAccioError(
					IntegracioHelper.INTCODI_USUARIS,
					accioDescripcio,
					accioParams,
					IntegracioAccioTipusEnumDto.ENVIAMENT,
					errorDescripcio,
					ex);
			throw new SistemaExternException(
					IntegracioHelper.INTCODI_USUARIS,
					errorDescripcio,
					ex);
		}
	}

	public ArbreDto<UnitatOrganitzativaDto> unitatsOrganitzativesFindArbreByPare(
			String pareCodi) {
		String accioDescripcio = "Consulta de l'arbre d'unitats donat un pare";
		Map<String, String> accioParams = new HashMap<String, String>();
		accioParams.put("unitatPare", pareCodi);
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
						IntegracioAccioTipusEnumDto.ENVIAMENT);
				return resposta;
			} else {
				String errorMissatge = "No s'ha trobat la unitat organitzativa arrel (codi=" + pareCodi + ")";
				integracioHelper.addAccioError(
						IntegracioHelper.INTCODI_UNITATS,
						accioDescripcio,
						accioParams,
						IntegracioAccioTipusEnumDto.ENVIAMENT,
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
		try {
			UnitatOrganitzativaDto unitatOrganitzativa = conversioTipusHelper.convertir(
					getUnitatsOrganitzativesPlugin().findAmbCodi(codi),
					UnitatOrganitzativaDto.class);
			integracioHelper.addAccioOk(
					IntegracioHelper.INTCODI_UNITATS,
					accioDescripcio,
					accioParams,
					IntegracioAccioTipusEnumDto.ENVIAMENT);
			return unitatOrganitzativa;
		} catch (Exception ex) {
			String errorDescripcio = "Error al accedir al plugin d'unitats organitzatives";
			integracioHelper.addAccioError(
					IntegracioHelper.INTCODI_UNITATS,
					accioDescripcio,
					accioParams,
					IntegracioAccioTipusEnumDto.ENVIAMENT,
					errorDescripcio,
					ex);
			throw new SistemaExternException(
					IntegracioHelper.INTCODI_UNITATS,
					errorDescripcio,
					ex);
		}
	}

	public boolean isGestioDocumentalPluginActiu() {
		return getGestioDocumentalPlugin() != null;
	}
	public String gestioDocumentalCreate(
			String arxiuNom,
			byte[] contingut) {
		String accioDescripcio = "Creació d'un arxiu";
		Map<String, String> accioParams = new HashMap<String, String>();
		accioParams.put("arxiuNom", arxiuNom);
		accioParams.put("contingut", new Integer(contingut.length).toString());
		try {
			String gestioDocumentalId = getGestioDocumentalPlugin().create(
					new GestioDocumentalArxiu(
							arxiuNom,
							contingut));
			integracioHelper.addAccioOk(
					IntegracioHelper.INTCODI_GESDOC,
					accioDescripcio,
					accioParams,
					IntegracioAccioTipusEnumDto.ENVIAMENT);
			return gestioDocumentalId;
		} catch (Exception ex) {
			String errorDescripcio = "Error al accedir al plugin de gestió documental";
			integracioHelper.addAccioError(
					IntegracioHelper.INTCODI_GESDOC,
					accioDescripcio,
					accioParams,
					IntegracioAccioTipusEnumDto.ENVIAMENT,
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
					IntegracioAccioTipusEnumDto.ENVIAMENT);
		} catch (Exception ex) {
			String errorDescripcio = "Error al accedir al plugin de gestió documental";
			integracioHelper.addAccioError(
					IntegracioHelper.INTCODI_GESDOC,
					accioDescripcio,
					accioParams,
					IntegracioAccioTipusEnumDto.ENVIAMENT,
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
		try {
			getGestioDocumentalPlugin().delete(
					id);
			integracioHelper.addAccioOk(
					IntegracioHelper.INTCODI_GESDOC,
					accioDescripcio,
					accioParams,
					IntegracioAccioTipusEnumDto.ENVIAMENT);
		} catch (Exception ex) {
			String errorDescripcio = "Error al accedir al plugin de gestió documental";
			integracioHelper.addAccioError(
					IntegracioHelper.INTCODI_GESDOC,
					accioDescripcio,
					accioParams,
					IntegracioAccioTipusEnumDto.ENVIAMENT,
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
		try {
			GestioDocumentalArxiu gesdocArxiu = getGestioDocumentalPlugin().get(id);
			integracioHelper.addAccioOk(
					IntegracioHelper.INTCODI_GESDOC,
					accioDescripcio,
					accioParams,
					IntegracioAccioTipusEnumDto.ENVIAMENT);
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
					errorDescripcio,
					ex);
			throw new SistemaExternException(
					IntegracioHelper.INTCODI_GESDOC,
					errorDescripcio,
					ex);
		}
	}

	public long portafirmesUpload(
			DocumentVersioEntity documentVersio,
			String motiu,
			PortafirmesPrioritatEnum prioritat,
			Date dataCaducitat) {
		String accioDescripcio = "Enviament de document a firmar";
		Map<String, String> accioParams = new HashMap<String, String>();
		accioParams.put(
				"documentVersioId",
				documentVersio.getId().toString());
		accioParams.put("motiu", motiu);
		accioParams.put("prioritat", prioritat.toString());
		accioParams.put(
				"dataCaducitat",
				new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(dataCaducitat));
		DocumentEntity document = documentVersio.getDocument();
		accioParams.put(
				"documentId",
				document.getId().toString());
		MetaDocumentEntity metaDocument = document.getMetaDocument();
		if (metaDocument != null) {
			accioParams.put(
					"metaDocumentId",
					metaDocument.getId().toString());
			boolean metaDocumentConfigurat = true;
			PortafirmesDocument portafirmesDocument = new PortafirmesDocument();
			portafirmesDocument.setTitol(document.getNom());
			if (metaDocument.getPortafirmesDocumentTipus() == null || metaDocument.getPortafirmesDocumentTipus().isEmpty()) {
				metaDocumentConfigurat = false;
			}
			if (metaDocument.getPortafirmesResponsables() == null || metaDocument.getPortafirmesResponsables().length == 0) {
				metaDocumentConfigurat = false;
			}
			if (metaDocumentConfigurat) {
				portafirmesDocument.setFirmat(
						false);
				String urlCustodia = null;
				if (portafirmesEnviarDocumentEstampat()) {
					urlCustodia = documentVersio.getCustodiaUrl();
				}
				FitxerDto fitxerOriginal = documentHelper.getFitxerAssociat(documentVersio);
				FitxerDto fitxerConvertit = conversioConvertirPdfIEstamparUrl(
						fitxerOriginal,
						urlCustodia);
				portafirmesDocument.setArxiuNom(
						fitxerConvertit.getNom());
				portafirmesDocument.setArxiuContingut(
						fitxerConvertit.getContingut());
				String[] responsables = metaDocument.getPortafirmesResponsables();
				List<PortafirmesFluxBloc> flux = new ArrayList<PortafirmesFluxBloc>();
				if (MetaDocumentFirmaFluxTipusEnumDto.SERIE.equals(metaDocument.getPortafirmesFluxTipus())) {
					for (String responsable: responsables) {
						PortafirmesFluxBloc bloc = new PortafirmesFluxBloc();
						bloc.setMinSignataris(1);
						bloc.setDestinataris(new String[] {responsable});
						bloc.setObligatorietats(new boolean[] {true});
						flux.add(bloc);
					}
				} else if (MetaDocumentFirmaFluxTipusEnumDto.PARALEL.equals(metaDocument.getPortafirmesFluxTipus())) {
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
					long portafirmesEnviamentId = getPortafirmesPlugin().upload(
							portafirmesDocument,
							Long.parseLong(metaDocument.getPortafirmesDocumentTipus()),
							motiu,
							"Aplicació RIPEA",
							prioritat,
							dataCaducitatCal.getTime(),
							flux,
							null, //new Long(metaDocument.getPortafirmesFluxId()),
							null,
							false);
					integracioHelper.addAccioOk(
							IntegracioHelper.INTCODI_PFIRMA,
							accioDescripcio,
							accioParams,
							IntegracioAccioTipusEnumDto.ENVIAMENT);
					return portafirmesEnviamentId;
				} catch (Exception ex) {
					String errorDescripcio = "Error al accedir al plugin de portafirmes";
					integracioHelper.addAccioError(
							IntegracioHelper.INTCODI_PFIRMA,
							accioDescripcio,
							accioParams,
							IntegracioAccioTipusEnumDto.ENVIAMENT,
							errorDescripcio,
							ex);
					throw new SistemaExternException(
							IntegracioHelper.INTCODI_PFIRMA,
							errorDescripcio,
							ex);
				}
			} else {
				String errorMissatge = "El meta-document associat no està correctament configurat per a fer enviaments al portafirmes (" +
						"documentVersioId=" + documentVersio.getId().toString() + ")";
				integracioHelper.addAccioError(
						IntegracioHelper.INTCODI_PFIRMA,
						accioDescripcio,
						accioParams,
						IntegracioAccioTipusEnumDto.ENVIAMENT,
						errorMissatge);
				throw new SistemaExternException(
						IntegracioHelper.INTCODI_PFIRMA,
						errorMissatge);
			}
		} else {
			String errorMissatge = "El document que s'intenta enviar no te meta-document associat (" +
							"documentVersioId=" + documentVersio.getId().toString() + ")";
			integracioHelper.addAccioError(
					IntegracioHelper.INTCODI_PFIRMA,
					accioDescripcio,
					accioParams,
					IntegracioAccioTipusEnumDto.ENVIAMENT,
					errorMissatge);
			throw new SistemaExternException(
					IntegracioHelper.INTCODI_PFIRMA,
					errorMissatge);
		}
	}

	public PortafirmesDocument portafirmesDownload(
			DocumentVersioEntity documentVersio,
			DocumentPortafirmesEntity documentPortafirmes) {
		String accioDescripcio = "Descarregar document firmat";
		Map<String, String> accioParams = new HashMap<String, String>();
		accioParams.put(
				"documentVersioId",
				documentVersio.getId().toString());
		DocumentEntity document = documentVersio.getDocument();
		accioParams.put(
				"documentId",
				document.getId().toString());
		accioParams.put(
				"documentPortafirmesId",
				documentPortafirmes.getId().toString());
		accioParams.put(
				"portafirmesId",
				new Long(documentPortafirmes.getPortafirmesId()).toString());
		PortafirmesDocument portafirmesDocument = null;
		try {
			portafirmesDocument = getPortafirmesPlugin().download(
					documentPortafirmes.getPortafirmesId());
			integracioHelper.addAccioOk(
					IntegracioHelper.INTCODI_PFIRMA,
					accioDescripcio,
					accioParams,
					IntegracioAccioTipusEnumDto.ENVIAMENT);
			return portafirmesDocument;
		} catch (Exception ex) {
			String errorDescripcio = "Error al descarregar el document firmat";
			integracioHelper.addAccioError(
					IntegracioHelper.INTCODI_PFIRMA,
					accioDescripcio,
					accioParams,
					IntegracioAccioTipusEnumDto.ENVIAMENT,
					errorDescripcio,
					ex);
			throw new SistemaExternException(
					IntegracioHelper.INTCODI_PFIRMA,
					errorDescripcio,
					ex);
		}
	}

	public void portafirmesDelete(
			DocumentVersioEntity documentVersio,
			DocumentPortafirmesEntity documentPortafirmes) {
		String accioDescripcio = "Esborrar document enviat a firmar";
		Map<String, String> accioParams = new HashMap<String, String>();
		accioParams.put(
				"documentVersioId",
				documentVersio.getId().toString());
		DocumentEntity document = documentVersio.getDocument();
		accioParams.put(
				"documentId",
				document.getId().toString());
		accioParams.put(
				"documentPortafirmesId",
				documentPortafirmes.getId().toString());
		accioParams.put(
				"portafirmesId",
				new Long(documentPortafirmes.getPortafirmesId()).toString());
		try {
			getPortafirmesPlugin().delete(
					documentPortafirmes.getPortafirmesId());
			integracioHelper.addAccioOk(
					IntegracioHelper.INTCODI_PFIRMA,
					accioDescripcio,
					accioParams,
					IntegracioAccioTipusEnumDto.ENVIAMENT);
		} catch (Exception ex) {
			String errorDescripcio = "Error al accedir al plugin de portafirmes";
			integracioHelper.addAccioError(
					IntegracioHelper.INTCODI_PFIRMA,
					accioDescripcio,
					accioParams,
					IntegracioAccioTipusEnumDto.ENVIAMENT,
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
		try {
			List<PortafirmesDocumentTipus> tipus = getPortafirmesPlugin().findDocumentTipus();
			integracioHelper.addAccioOk(
					IntegracioHelper.INTCODI_PFIRMA,
					accioDescripcio,
					null,
					IntegracioAccioTipusEnumDto.ENVIAMENT);
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

	public FitxerDto conversioConvertirPdfIEstamparUrl(
			FitxerDto original,
			String url) {
		String accioDescripcio = "Conversió de document a PDF";
		Map<String, String> accioParams = new HashMap<String, String>();
		accioParams.put("arxiuOriginalNom", original.getNom());
		accioParams.put("arxiuOriginalTamany", new Integer(original.getContingut().length).toString());
		try {
			ConversioArxiu convertit = getConversioPlugin().convertirPdfIEstamparUrl(
					new ConversioArxiu(
							original.getNom(),
							original.getContingut()),
							url);
			accioParams.put("arxiuConvertitNom", convertit.getArxiuNom());
			accioParams.put("arxiuConvertitTamany", new Integer(convertit.getArxiuContingut().length).toString());
			integracioHelper.addAccioOk(
					IntegracioHelper.INTCODI_CONVERT,
					accioDescripcio,
					accioParams,
					IntegracioAccioTipusEnumDto.ENVIAMENT);
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
					errorDescripcio,
					ex);
			throw new SistemaExternException(
					IntegracioHelper.INTCODI_CONVERT,
					errorDescripcio,
					ex);
		}
	}

	public String custodiaReservarUrl(
			DocumentVersioEntity documentVersio) {
		if (documentVersio.getCustodiaUrl() != null)
			return documentVersio.getCustodiaUrl();
		String accioDescripcio = "Reservar URL";
		Map<String, String> accioParams = new HashMap<String, String>();
		accioParams.put(
				"documentVersioId",
				documentVersio.getId().toString());
		try {
			String url = getCustodiaPlugin().reservarUrl(
					documentVersio.getId().toString());
			integracioHelper.addAccioOk(
					IntegracioHelper.INTCODI_CUSTODIA,
					accioDescripcio,
					accioParams,
					IntegracioAccioTipusEnumDto.ENVIAMENT);
			return url;
		} catch (Exception ex) {
			String errorDescripcio = "Error al accedir al plugin de custòdia de documents";
			integracioHelper.addAccioError(
					IntegracioHelper.INTCODI_CUSTODIA,
					accioDescripcio,
					accioParams,
					IntegracioAccioTipusEnumDto.ENVIAMENT,
					errorDescripcio,
					ex);
			throw new SistemaExternException(
					IntegracioHelper.INTCODI_CUSTODIA,
					errorDescripcio,
					ex);
		}
	}

	public String custodiaCustodiarDocumentFirmat(
			DocumentVersioEntity documentVersio,
			String custodiaTipus,
			String arxiuNom,
			byte[] arxiuContingut) {
		String accioDescripcio = "Custodiar document firmat";
		Map<String, String> accioParams = new HashMap<String, String>();
		accioParams.put(
				"documentVersioId",
				documentVersio.getId().toString());
		accioParams.put(
				"arxiuNom",
				arxiuNom);
		accioParams.put(
				"arxiuTamany",
				new Integer(arxiuContingut.length).toString());
		try {
			getCustodiaPlugin().custodiarPdfFirmat(
					documentVersio.getId().toString(),
					custodiaTipus,
					arxiuNom,
					new ByteArrayInputStream(arxiuContingut));
			integracioHelper.addAccioOk(
					IntegracioHelper.INTCODI_CUSTODIA,
					accioDescripcio,
					accioParams,
					IntegracioAccioTipusEnumDto.ENVIAMENT);
			return documentVersio.getId().toString();
		} catch (Exception ex) {
			String errorDescripcio = "Error al accedir al plugin de custòdia de documents";
			integracioHelper.addAccioError(
					IntegracioHelper.INTCODI_CUSTODIA,
					accioDescripcio,
					accioParams,
					IntegracioAccioTipusEnumDto.ENVIAMENT,
					errorDescripcio,
					ex);
			throw new SistemaExternException(
					IntegracioHelper.INTCODI_CUSTODIA,
					errorDescripcio,
					ex);
		}
	}

	public void custodiaEsborrar(DocumentVersioEntity documentVersio) {
		String accioDescripcio = "Esborrar document custodiat";
		Map<String, String> accioParams = new HashMap<String, String>();
		accioParams.put(
				"documentVersioId",
				documentVersio.getId().toString());
		accioParams.put(
				"custodiaId",
				documentVersio.getCustodiaId());
		try {
			getCustodiaPlugin().esborrarDocumentCustodiat(
					documentVersio.getCustodiaId(),
					false);
			integracioHelper.addAccioOk(
					IntegracioHelper.INTCODI_CUSTODIA,
					accioDescripcio,
					accioParams,
					IntegracioAccioTipusEnumDto.ENVIAMENT);
		} catch (Exception ex) {
			String errorDescripcio = "Error al accedir al plugin de custòdia de documents";
			integracioHelper.addAccioError(
					IntegracioHelper.INTCODI_CUSTODIA,
					accioDescripcio,
					accioParams,
					IntegracioAccioTipusEnumDto.ENVIAMENT,
					errorDescripcio,
					ex);
			throw new SistemaExternException(
					IntegracioHelper.INTCODI_CUSTODIA,
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
					IntegracioAccioTipusEnumDto.ENVIAMENT);
			return resposta;
		} catch (Exception ex) {
			String errorDescripcio = "Error al accedir al plugin de registre";
			integracioHelper.addAccioError(
					IntegracioHelper.INTCODI_REGISTRE,
					accioDescripcio,
					accioParams,
					IntegracioAccioTipusEnumDto.ENVIAMENT,
					errorDescripcio,
					ex);
			throw new SistemaExternException(
					IntegracioHelper.INTCODI_REGISTRE,
					errorDescripcio,
					ex);
		}
	}

	public void ciutadaExpedientCrear(
			ExpedientEntity expedient,
			long unitatAdministrativa,
			String idioma,
			String descripcio,
			InteressatEntity destinatari,
			InteressatEntity representat,
			String bantelNumeroEntrada,
			boolean avisosHabilitats,
			String avisText,
			String avisTextMobil) {
		String accioDescripcio = "Creació d'un expedient a la zona personal del ciutadà";
		Map<String, String> accioParams = new HashMap<String, String>();
		accioParams.put("expedientId", expedient.getId().toString());
		accioParams.put("unitatAdministrativa", new Long(unitatAdministrativa).toString());
		accioParams.put("idioma", idioma);
		accioParams.put("descripcio", descripcio);
		accioParams.put("destinatari", representat.getIdentificador());
		accioParams.put("representat", representat.getIdentificador());
		accioParams.put("bantelNumeroEntrada", bantelNumeroEntrada);
		accioParams.put("avisosHabilitats", new Boolean(avisosHabilitats).toString());
		accioParams.put("avisText", avisText);
		accioParams.put("avisTextMobil", avisTextMobil);
		try {
			CiutadaPersona personaDestinatari = new CiutadaPersona();
			personaDestinatari.setNif(destinatari.getDocumentNum());
			if (destinatari instanceof InteressatPersonaFisicaEntity) {
				personaDestinatari.setNom(((InteressatPersonaFisicaEntity)destinatari).getNom());
				personaDestinatari.setLlinatge1(((InteressatPersonaFisicaEntity)destinatari).getLlinatge1());
				personaDestinatari.setLlinatge2(((InteressatPersonaFisicaEntity)destinatari).getLlinatge2());
			} else {
				personaDestinatari.setNom(destinatari.getIdentificador());
				personaDestinatari.setLlinatge1("");
				personaDestinatari.setLlinatge2("");
			}
			CiutadaPersona personaRepresentat = null;
			if (representat != null) {
				personaRepresentat = new CiutadaPersona();
				personaDestinatari.setNif(representat.getDocumentNum());
				if (representat instanceof InteressatPersonaFisicaEntity) {
					personaDestinatari.setNom(((InteressatPersonaFisicaEntity)representat).getNom());
					personaDestinatari.setLlinatge1(((InteressatPersonaFisicaEntity)representat).getLlinatge1());
					personaDestinatari.setLlinatge2(((InteressatPersonaFisicaEntity)representat).getLlinatge2());
				} else {
					personaDestinatari.setNom(representat.getIdentificador());
					personaDestinatari.setLlinatge1("");
					personaDestinatari.setLlinatge2("");
				}
			}
			getCiutadaPlugin().expedientCrear(
					expedient.getId().toString(),
					unitatAdministrativa,
					idioma,
					descripcio,
					personaDestinatari,
					personaRepresentat,
					bantelNumeroEntrada,
					avisosHabilitats,
					avisText,
					avisTextMobil);
			integracioHelper.addAccioOk(
					IntegracioHelper.INTCODI_CIUTADA,
					accioDescripcio,
					accioParams,
					IntegracioAccioTipusEnumDto.ENVIAMENT);
		} catch (Exception ex) {
			String errorDescripcio = "Error al accedir al plugin de comunicació amb el ciutadà";
			integracioHelper.addAccioError(
					IntegracioHelper.INTCODI_CIUTADA,
					accioDescripcio,
					accioParams,
					IntegracioAccioTipusEnumDto.ENVIAMENT,
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
			long unitatAdministrativa,
			String titol,
			String text,
			String textMobil) {
		String accioDescripcio = "Creació d'un avis a la zona personal del ciutadà";
		Map<String, String> accioParams = new HashMap<String, String>();
		accioParams.put("expedientId", expedient.getId().toString());
		accioParams.put("unitatAdministrativa", new Long(unitatAdministrativa).toString());
		accioParams.put("titol", titol);
		accioParams.put("text", text);
		accioParams.put("textMobil", textMobil);
		try {
			getCiutadaPlugin().avisCrear(
					expedient.getId().toString(),
					unitatAdministrativa,
					titol,
					text,
					textMobil,
					null);
			integracioHelper.addAccioOk(
					IntegracioHelper.INTCODI_CIUTADA,
					accioDescripcio,
					accioParams,
					IntegracioAccioTipusEnumDto.ENVIAMENT);
		} catch (Exception ex) {
			String errorDescripcio = "Error al accedir al plugin de comunicació amb el ciutadà";
			integracioHelper.addAccioError(
					IntegracioHelper.INTCODI_CIUTADA,
					accioDescripcio,
					accioParams,
					IntegracioAccioTipusEnumDto.ENVIAMENT,
					errorDescripcio,
					ex);
			throw new SistemaExternException(
					IntegracioHelper.INTCODI_CIUTADA,
					errorDescripcio,
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
	private GestioDocumentalPlugin getGestioDocumentalPlugin() {
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
	private CustodiaPlugin getCustodiaPlugin() {
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
	}
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

	private String getPropertyPluginDadesUsuari() {
		return PropertiesHelper.getProperties().getProperty("es.caib.ripea.plugin.dades.usuari.class");
	}
	private String getPropertyPluginUnitatsOrganitzatives() {
		return PropertiesHelper.getProperties().getProperty("es.caib.ripea.plugin.unitats.organitzatives.class");
	}
	private String getPropertyPluginGestioDocumental() {
		return PropertiesHelper.getProperties().getProperty("es.caib.ripea.plugin.gesdoc.class");
	}
	private String getPropertyPluginPortafirmes() {
		return PropertiesHelper.getProperties().getProperty("es.caib.ripea.plugin.portafirmes.class");
	}
	private String getPropertyPluginConversio() {
		return PropertiesHelper.getProperties().getProperty("es.caib.ripea.plugin.conversio.class");
	}
	private String getPropertyPluginCustodia() {
		return PropertiesHelper.getProperties().getProperty("es.caib.ripea.plugin.custodia.class");
	}
	private String getPropertyPluginRegistre() {
		return PropertiesHelper.getProperties().getProperty("es.caib.ripea.plugin.registre.class");
	}
	private String getPropertyPluginCiutada() {
		return PropertiesHelper.getProperties().getProperty("es.caib.ripea.plugin.ciutada.class");
	}

}
