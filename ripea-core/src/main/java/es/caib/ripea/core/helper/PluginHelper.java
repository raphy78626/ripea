/**
 * 
 */
package es.caib.ripea.core.helper;

import java.io.ByteArrayInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import es.caib.ripea.core.api.dto.ArbreDto;
import es.caib.ripea.core.api.dto.ArbreNodeDto;
import es.caib.ripea.core.api.dto.FitxerDto;
import es.caib.ripea.core.api.dto.IntegracioAccioTipusEnumDto;
import es.caib.ripea.core.api.dto.PortafirmesDocumentTipusDto;
import es.caib.ripea.core.api.dto.UnitatOrganitzativaDto;
import es.caib.ripea.core.api.exception.PluginException;
import es.caib.ripea.core.entity.DocumentEntity;
import es.caib.ripea.core.entity.DocumentPortafirmesEntity;
import es.caib.ripea.core.entity.DocumentVersioEntity;
import es.caib.ripea.core.entity.MetaDocumentEntity;
import es.caib.ripea.plugin.SistemaExternException;
import es.caib.ripea.plugin.conversio.ConversioArxiu;
import es.caib.ripea.plugin.conversio.ConversioPlugin;
import es.caib.ripea.plugin.custodia.CustodiaPlugin;
import es.caib.ripea.plugin.gesdoc.GestioDocumentalArxiu;
import es.caib.ripea.plugin.gesdoc.GestioDocumentalPlugin;
import es.caib.ripea.plugin.portafirmes.PortafirmesDocument;
import es.caib.ripea.plugin.portafirmes.PortafirmesDocumentTipus;
import es.caib.ripea.plugin.portafirmes.PortafirmesPlugin;
import es.caib.ripea.plugin.portafirmes.PortafirmesPrioritatEnum;
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

	@Resource
	private ConversioTipusHelper conversioTipusHelper;
	@Resource
	private IntegracioHelper integracioHelper;
	@Resource
	private DocumentHelper documentHelper;



	public DadesUsuari dadesUsuariConsultarAmbUsuariCodi(
			String usuariCodi) throws PluginException {
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
			throw new PluginException(errorDescripcio, ex);
		}
	}
	public DadesUsuari dadesUsuariConsultarAmbUsuariNif(
			String usuariNif) throws PluginException {
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
			throw new PluginException(errorDescripcio, ex);
		}
	}
	public List<DadesUsuari> dadesUsuariConsultarUsuarisAmbGrup(
			String grupCodi) throws PluginException {
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
			throw new PluginException(errorDescripcio, ex);
		}
	}

	public ArbreDto<UnitatOrganitzativaDto> unitatsOrganitzativesFindArbreByPare(
			String pareCodi) throws PluginException {
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
				String errorMissatge = "No s'ha trobat cap unitat organitzativa arrel";
				integracioHelper.addAccioError(
						IntegracioHelper.INTCODI_UNITATS,
						accioDescripcio,
						accioParams,
						IntegracioAccioTipusEnumDto.ENVIAMENT,
						errorMissatge,
						null);
				throw new PluginException(errorMissatge);
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
			throw new PluginException(errorDescripcio, ex);
		}
	}
	public UnitatOrganitzativaDto unitatsOrganitzativesFindByCodi(
			String codi) throws PluginException {
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
			throw new PluginException(errorDescripcio, ex);
		}
	}

	public boolean isGestioDocumentalPluginActiu() {
		return getGestioDocumentalPlugin() != null;
	}
	public String gestioDocumentalCreate(
			String arxiuNom,
			byte[] contingut) throws PluginException {
		try {
			if (!isGestioDocumentalPluginActiu())
				throw new PluginException(
						"La classe del plugin de gestió documental no està configurada");
			return getGestioDocumentalPlugin().create(
					new GestioDocumentalArxiu(
							arxiuNom,
							contingut));
		} catch (Exception ex) {
			throw new PluginException(
					"Error en el plugin de gestió documental",
					ex);
		}
	}
	public void gestioDocumentalUpdate(
			String id,
			String arxiuNom,
			byte[] contingut) throws PluginException {
		try {
			if (!isGestioDocumentalPluginActiu())
				throw new PluginException(
						"La classe del plugin de gestió documental no està configurada");
			getGestioDocumentalPlugin().update(
					id,
					new GestioDocumentalArxiu(
							arxiuNom,
							contingut));
		} catch (Exception ex) {
			throw new PluginException(
					"Error en el plugin de gestió documental",
					ex);
		}
	}
	public void gestioDocumentalDelete(
			String id) throws PluginException {
		try {
			if (!isGestioDocumentalPluginActiu())
				throw new PluginException(
						"La classe del plugin de gestió documental no està configurada");
			getGestioDocumentalPlugin().delete(
					id);
		} catch (Exception ex) {
			throw new PluginException(
					"Error en el plugin de gestió documental",
					ex);
		}
	}
	public FitxerDto gestioDocumentalGet(
			String id) throws PluginException {
		try {
			if (!isGestioDocumentalPluginActiu())
				throw new PluginException(
						"La classe del plugin de gestió documental no està configurada");
			GestioDocumentalArxiu gesdocArxiu = getGestioDocumentalPlugin().get(id);
			FitxerDto fitxer = new FitxerDto();
			fitxer.setNom(gesdocArxiu.getFileName());
			fitxer.setContingut(gesdocArxiu.getContent());
			return fitxer;
		} catch (Exception ex) {
			throw new PluginException(
					"Error en el plugin de gestió documental",
					ex);
		}
	}

	public long portafirmesUpload(
			DocumentVersioEntity documentVersio,
			String motiu,
			PortafirmesPrioritatEnum prioritat,
			Date dataCaducitat) throws PluginException {
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
			if (metaDocument.getPortafirmesFluxId() == null || metaDocument.getPortafirmesFluxId().isEmpty())
				metaDocumentConfigurat = false;
			if (metaDocumentConfigurat) {
				portafirmesDocument.setFirmat(
						false);
				String urlCustodia = null;
				if (!getPortafirmesPlugin().isCustodiaAutomatica()) {
					urlCustodia = custodiaReservarUrl(documentVersio);
				}
				FitxerDto fitxerOriginal = documentHelper.getFitxerAssociat(documentVersio);
				FitxerDto fitxerConvertit = conversioConvertirPdf(
						fitxerOriginal,
						urlCustodia);
				portafirmesDocument.setArxiuNom(
						fitxerConvertit.getNom());
				portafirmesDocument.setArxiuContingut(
						fitxerConvertit.getContingut());
				/*List<PortafirmesFluxBloc> passos = new ArrayList<PortafirmesFluxBloc>();
				PortafirmesFluxBloc bloc = new PortafirmesFluxBloc();
				bloc.setMinSignataris(1);
				bloc.setDestinataris(new String[] {"12345678Z"});
				bloc.setObligatorietats(new boolean[] {true});*/
				try {
					long portafirmesEnviamentId = getPortafirmesPlugin().upload(
							portafirmesDocument,
							Long.parseLong(metaDocument.getPortafirmesDocumentTipus()),
							motiu,
							"Aplicació RIPEA",
							prioritat,
							dataCaducitat,
							null,
							new Long(metaDocument.getPortafirmesFluxId()),
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
					throw new PluginException(errorDescripcio, ex);
				}
			} else {
				String errorMissatge = "El meta-document associat no està correctament configurat per a fer enviaments al portafirmes (" +
						"documentVersioId=" + documentVersio.getId().toString() + ")";
				integracioHelper.addAccioError(
						IntegracioHelper.INTCODI_PFIRMA,
						accioDescripcio,
						accioParams,
						IntegracioAccioTipusEnumDto.ENVIAMENT,
						errorMissatge,
						null);
				throw new PluginException(errorMissatge);
			}
		} else {
			String errorMissatge = "El document que s'intenta enviar no te meta-document associat (" +
							"documentVersioId=" + documentVersio.getId().toString() + ")";
			integracioHelper.addAccioError(
					IntegracioHelper.INTCODI_USUARIS,
					accioDescripcio,
					accioParams,
					IntegracioAccioTipusEnumDto.ENVIAMENT,
					errorMissatge,
					null);
			throw new PluginException(errorMissatge);
		}
	}

	public void portafirmesDelete(
			DocumentVersioEntity documentVersio,
			DocumentPortafirmesEntity documentPortafirmes) throws PluginException {
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
			throw new PluginException(errorDescripcio, ex);
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
			throw new PluginException(errorDescripcio, ex);
		}
	}

	public void portafirmesProcessarCallbackEstatFirmat(
			DocumentVersioEntity documentVersio,
			DocumentPortafirmesEntity documentPortafirmes) {
		String accioDescripcio = "Descarregar document firmat";
		Map<String, String> accioParams = new HashMap<String, String>();
		accioParams.put(
				"documentPortafirmesId",
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
		} catch (SistemaExternException ex) {
			String errorDescripcio = "Error al descarregar el document firmat";
			integracioHelper.addAccioError(
					IntegracioHelper.INTCODI_PFIRMA,
					accioDescripcio,
					accioParams,
					IntegracioAccioTipusEnumDto.ENVIAMENT,
					errorDescripcio,
					ex);
			throw new PluginException(errorDescripcio, ex);
		}
		if (portafirmesDocument.isCustodiat()) {
			documentVersio.updateCustodiaUrl(
					portafirmesDocument.getCustodiaUrl());
			documentVersio.updateCustodiaDades(
					true,
					portafirmesDocument.getCustodiaId());
		} else {
			custodiaCustodiarPdfFirmat(
					documentVersio,
					portafirmesDocument.getArxiuNom(),
					portafirmesDocument.getArxiuContingut());
		}
	}

	public String portafirmesConversioArxiuNom(
			String nomOriginal) {
		return getConversioPlugin().getNomArxiuConvertitPdf(nomOriginal);
	}

	public FitxerDto conversioConvertirPdf(
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
			throw new PluginException(errorDescripcio, ex);
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
			documentVersio.updateCustodiaUrl(url);
			integracioHelper.addAccioOk(
					IntegracioHelper.INTCODI_CUSTODIA,
					accioDescripcio,
					accioParams,
					IntegracioAccioTipusEnumDto.ENVIAMENT);
			return url;
		} catch (SistemaExternException ex) {
			String errorDescripcio = "Error al accedir al plugin de custòdia de documents";
			integracioHelper.addAccioError(
					IntegracioHelper.INTCODI_CUSTODIA,
					accioDescripcio,
					accioParams,
					IntegracioAccioTipusEnumDto.ENVIAMENT,
					errorDescripcio,
					ex);
			throw new PluginException(errorDescripcio, ex);
		}
	}

	public void custodiaCustodiarPdfFirmat(
			DocumentVersioEntity documentVersio,
			String arxiuNom,
			byte[] arxiuContingut) {
		String accioDescripcio = "Custodiar PDF firmat";
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
					documentVersio.getDocument().getMetaDocument().getCustodiaPolitica(),
					arxiuNom,
					new ByteArrayInputStream(arxiuContingut));
			integracioHelper.addAccioOk(
					IntegracioHelper.INTCODI_CUSTODIA,
					accioDescripcio,
					accioParams,
					IntegracioAccioTipusEnumDto.ENVIAMENT);
			documentVersio.updateCustodiaDades(
					true,
					documentVersio.getCustodiaId());
		} catch (SistemaExternException ex) {
			String errorDescripcio = "Error al accedir al plugin de custòdia de documents";
			integracioHelper.addAccioError(
					IntegracioHelper.INTCODI_CUSTODIA,
					accioDescripcio,
					accioParams,
					IntegracioAccioTipusEnumDto.ENVIAMENT,
					errorDescripcio,
					ex);
			throw new PluginException(errorDescripcio, ex);
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
					throw new PluginException(
							"Error al crear la instància del plugin de dades d'usuari",
							ex);
				}
			} else {
				throw new PluginException(
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
					throw new PluginException(
							"Error al crear la instància del plugin d'unitats organitzatives",
							ex);
				}
			} else {
				throw new PluginException(
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
					throw new PluginException(
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
					throw new PluginException(
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
					throw new PluginException(
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
					throw new PluginException(
							"Error al crear la instància del plugin de custòdia de documents",
							ex);
				}
			}
		}
		return custodiaPlugin;
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

}
