/**
 * 
 */
package es.caib.ripea.plugin.caib.arxiu;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.apache.axis.encoding.Base64;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.caib.arxiudigital.apirest.constantes.EstadosElaboracion;
import es.caib.arxiudigital.apirest.constantes.EstadosExpediente;
import es.caib.arxiudigital.apirest.constantes.ExtensionesFichero;
import es.caib.arxiudigital.apirest.constantes.FormatosFichero;
import es.caib.arxiudigital.apirest.constantes.MetadatosDocumento;
import es.caib.arxiudigital.apirest.constantes.MetadatosExpediente;
import es.caib.arxiudigital.apirest.constantes.OrigenesContenido;
import es.caib.arxiudigital.apirest.constantes.PerfilesFirma;
import es.caib.arxiudigital.apirest.constantes.TiposDocumentosENI;
import es.caib.arxiudigital.apirest.constantes.TiposFirma;
import es.caib.ripea.plugin.SistemaExternCodiMissatgeException;
import es.caib.ripea.plugin.SistemaExternException;
import es.caib.ripea.plugin.SistemaExternNoTrobatException;
import es.caib.ripea.plugin.arxiu.ArxiuCapsalera;
import es.caib.ripea.plugin.arxiu.ArxiuCarpeta;
import es.caib.ripea.plugin.arxiu.ArxiuContingut;
import es.caib.ripea.plugin.arxiu.ArxiuContingutTipusEnum;
import es.caib.ripea.plugin.arxiu.ArxiuDocument;
import es.caib.ripea.plugin.arxiu.ArxiuDocumentContingut;
import es.caib.ripea.plugin.arxiu.ArxiuDocumentVersio;
import es.caib.ripea.plugin.arxiu.ArxiuEstatElaboracio;
import es.caib.ripea.plugin.arxiu.ArxiuExpedient;
import es.caib.ripea.plugin.arxiu.ArxiuExpedientEstat;
import es.caib.ripea.plugin.arxiu.ArxiuFill;
import es.caib.ripea.plugin.arxiu.ArxiuFillTipusEnum;
import es.caib.ripea.plugin.arxiu.ArxiuFormatExtensio;
import es.caib.ripea.plugin.arxiu.ArxiuFormatNom;
import es.caib.ripea.plugin.arxiu.ArxiuOrigenContingut;
import es.caib.ripea.plugin.arxiu.ArxiuPlugin;
import es.caib.ripea.plugin.arxiu.ArxiuTipusDocumental;
import es.caib.ripea.plugin.caib.arxiu.client.ArxiuClient;
import es.caib.ripea.plugin.caib.arxiu.client.ArxiuClientImpl;
import es.caib.ripea.plugin.caib.arxiu.client.ArxiuContentChild;
import es.caib.ripea.plugin.caib.arxiu.client.ArxiuContentVersion;
import es.caib.ripea.plugin.caib.arxiu.client.ArxiuDocumentContent;
import es.caib.ripea.plugin.caib.arxiu.client.ArxiuDocumentContentTypeEnum;
import es.caib.ripea.plugin.caib.arxiu.client.ArxiuException;
import es.caib.ripea.plugin.caib.arxiu.client.ArxiuFile;
import es.caib.ripea.plugin.caib.arxiu.client.ArxiuFolder;
import es.caib.ripea.plugin.caib.arxiu.client.ArxiuHeader;
import es.caib.ripea.plugin.caib.arxiu.client.ArxiuNotFoundException;
import es.caib.ripea.plugin.utils.PropertiesHelper;

/**
 * Implementació del plugin d'arxiu per a l'arxiu digital
 * de la CAIB.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public class ArxiuPluginCaib implements ArxiuPlugin {

	private ArxiuClient arxiuClient;

	@Override
	public ArxiuExpedient expedientCrear(
			String titol,
			ArxiuOrigenContingut origen,
			Date dataObertura,
			String classificacio,
			ArxiuExpedientEstat estat,
			List<String> organs,
			List<String> interessats,
			String serieDocumental,
			ArxiuCapsalera capsalera) throws SistemaExternException {
		try {
			ArxiuFile file = getArxiuClient().fileCreate(
					toArxiuFile(
							null,
							titol,
							origen,
							dataObertura,
							classificacio,
							estat,
							organs,
							interessats,
							serieDocumental),
					toArxiuHeader(capsalera));
			return toArxiuExpedient(file);
		} catch (ArxiuException aex) {
			throw processarArxiuException(aex);
		}
	}

	@Override
	public void expedientModificar(
			String nodeId,
			String titol,
			ArxiuOrigenContingut origen,
			Date dataObertura,
			String classificacio,
			ArxiuExpedientEstat estat,
			List<String> organs,
			List<String> interessats,
			String serieDocumental,
			ArxiuCapsalera capsalera) throws SistemaExternException {
		try {
			getArxiuClient().fileUpdate(
					toArxiuFile(
							nodeId,
							titol,
							origen,
							dataObertura,
							classificacio,
							estat,
							organs,
							interessats,
							serieDocumental),
					toArxiuHeader(capsalera));
		} catch (ArxiuNotFoundException nfex) {
			throw new SistemaExternNoTrobatException(
					nodeId,
					nfex.getArxiuCodi(),
					nfex.getArxiuDescripcio(),
					nfex);
		} catch (ArxiuException aex) {
			throw processarArxiuException(aex);
		}
	}

	@Override
	public void expedientEsborrar(
			String nodeId,
			ArxiuCapsalera capsalera) throws SistemaExternException {
		try {
			getArxiuClient().fileDelete(
					nodeId,
					toArxiuHeader(capsalera));
		} catch (ArxiuNotFoundException nfex) {
			throw new SistemaExternNoTrobatException(
					nodeId,
					nfex.getArxiuCodi(),
					nfex.getArxiuDescripcio(),
					nfex);
		} catch (ArxiuException aex) {
			throw processarArxiuException(aex);
		}
	}

	@Override
	public ArxiuExpedient expedientConsultar(
			String nodeId,
			ArxiuCapsalera capsalera) throws SistemaExternException {
		try {
			ArxiuFile file = getArxiuClient().fileGet(
					nodeId,
					toArxiuHeader(capsalera));
			return toArxiuExpedient(file);
		} catch (ArxiuNotFoundException nfex) {
			throw new SistemaExternNoTrobatException(
					nodeId,
					nfex.getArxiuCodi(),
					nfex.getArxiuDescripcio(),
					nfex);
		} catch (ArxiuException aex) {
			throw processarArxiuException(aex);
		}
	}

	@Override
	public void expedientTancar(
			String nodeId,
			ArxiuCapsalera capsalera) throws SistemaExternException {
		try {
			getArxiuClient().fileClose(
					nodeId,
					toArxiuHeader(capsalera));
		} catch (ArxiuNotFoundException nfex) {
			throw new SistemaExternNoTrobatException(
					nodeId,
					nfex.getArxiuCodi(),
					nfex.getArxiuDescripcio(),
					nfex);
		} catch (ArxiuException aex) {
			throw processarArxiuException(aex);
		}
	}

	@Override
	public void expedientReobrir(
			String nodeId,
			ArxiuCapsalera capsalera) throws SistemaExternException {
		try {
			getArxiuClient().fileReopen(
					nodeId,
					toArxiuHeader(capsalera));
		} catch (ArxiuNotFoundException nfex) {
			throw new SistemaExternNoTrobatException(
					nodeId,
					nfex.getArxiuCodi(),
					nfex.getArxiuDescripcio(),
					nfex);
		} catch (ArxiuException aex) {
			throw processarArxiuException(aex);
		}
	}

	@Override
	public String expedientExportar(
			String nodeId,
			ArxiuCapsalera capsalera) throws SistemaExternException {
		try {
			return getArxiuClient().fileExport(
					nodeId,
					toArxiuHeader(capsalera));
		} catch (ArxiuNotFoundException nfex) {
			throw new SistemaExternNoTrobatException(
					nodeId,
					nfex.getArxiuCodi(),
					nfex.getArxiuDescripcio(),
					nfex);
		} catch (ArxiuException aex) {
			throw processarArxiuException(aex);
		}
	}

	@Override
	public ArxiuDocument documentEsborranyCrear(
			String titol,
			ArxiuOrigenContingut origen,
			Date dataCaptura,
			ArxiuEstatElaboracio estatElaboracio,
			ArxiuTipusDocumental documentTipus,
			ArxiuFormatNom formatNom,
			ArxiuFormatExtensio formatExtensio,
			List<String> organs,
			String serieDocumental,
			InputStream contingut,
			String contentType,
			String pareNodeId,
			ArxiuCapsalera capsalera) throws SistemaExternException {
		try {
			es.caib.ripea.plugin.caib.arxiu.client.ArxiuDocument document = getArxiuClient().documentDraftCreate(
					pareNodeId,
					toArxiuDocument(
							null,
							titol,
							origen,
							dataCaptura,
							estatElaboracio,
							documentTipus,
							formatNom,
							formatExtensio,
							organs,
							null,
							null,
							null,
							contingut,
							contentType,
							serieDocumental),
					toArxiuHeader(capsalera));
			return toArxiuDocument(document);
		} catch (ArxiuException aex) {
			throw processarArxiuException(aex);
		}
	}

	@Override
	public void documentEsborranyModificar(
			String nodeId,
			String titol,
			ArxiuOrigenContingut origen,
			Date dataCaptura,
			ArxiuEstatElaboracio estatElaboracio,
			ArxiuTipusDocumental documentTipus,
			ArxiuFormatNom formatNom,
			ArxiuFormatExtensio formatExtensio,
			List<String> organs,
			String serieDocumental,
			InputStream contingut,
			String contentType,
			ArxiuCapsalera capsalera) throws SistemaExternException {
		try {
			getArxiuClient().documentUpdate(
					toArxiuDocument(
							nodeId,
							titol,
							origen,
							dataCaptura,
							estatElaboracio,
							documentTipus,
							formatNom,
							formatExtensio,
							organs,
							null,
							null,
							null,
							contingut,
							contentType,
							serieDocumental),
					toArxiuHeader(capsalera));
		} catch (ArxiuNotFoundException nfex) {
			throw new SistemaExternNoTrobatException(
					nodeId,
					nfex.getArxiuCodi(),
					nfex.getArxiuDescripcio(),
					nfex);
		} catch (ArxiuException aex) {
			throw processarArxiuException(aex);
		}
	}

	@Override
	public void documentEsborrar(
			String nodeId,
			ArxiuCapsalera capsalera) throws SistemaExternException {
		try {
			getArxiuClient().documentDelete(
					nodeId,
					toArxiuHeader(capsalera));
		} catch (ArxiuNotFoundException nfex) {
			throw new SistemaExternNoTrobatException(
					nodeId,
					nfex.getArxiuCodi(),
					nfex.getArxiuDescripcio(),
					nfex);
		} catch (ArxiuException aex) {
			throw processarArxiuException(aex);
		}
	}

	@Override
	public ArxiuDocument documentConsultar(
			String nodeId,
			String versio,
			boolean ambContingut,
			ArxiuCapsalera capsalera) throws SistemaExternException {
		try {
			String nodeIdAmbVersio = (versio != null) ? versio + "@" + nodeId : nodeId;
			es.caib.ripea.plugin.caib.arxiu.client.ArxiuDocument document = getArxiuClient().documentGet(
					nodeIdAmbVersio,
					null,
					ambContingut,
					toArxiuHeader(capsalera));
			return toArxiuDocument(document);
		} catch (ArxiuNotFoundException nfex) {
			throw new SistemaExternNoTrobatException(
					nodeId,
					nfex.getArxiuCodi(),
					nfex.getArxiuDescripcio(),
					nfex);
		} catch (ArxiuException aex) {
			throw processarArxiuException(aex);
		}
	}

	@Override
	public List<ArxiuDocumentVersio> documentObtenirVersions(
			String nodeId,
			ArxiuCapsalera capsalera) throws SistemaExternException {
		try {
			List<ArxiuDocumentVersio> versions = new ArrayList<ArxiuDocumentVersio>();
			List<ArxiuContentVersion> vs = getArxiuClient().documentVersionList(
					nodeId,
					toArxiuHeader(capsalera));
			for (ArxiuContentVersion v: vs) {
				ArxiuDocumentVersio versio = new ArxiuDocumentVersio();
				versio.setId(v.getId());
				versio.setNodeId(v.getId() + "@" + nodeId);
				versio.setData(v.getData());
				versions.add(versio);
			}
			return versions;
		} catch (ArxiuNotFoundException nfex) {
			throw new SistemaExternNoTrobatException(
					nodeId,
					nfex.getArxiuCodi(),
					nfex.getArxiuDescripcio(),
					nfex);
		} catch (ArxiuException aex) {
			throw processarArxiuException(aex);
		}
	}

	@Override
	public String documentGenerarCsv(
			String nodeId,
			ArxiuCapsalera capsalera,
			String valcertDocumentId) throws SistemaExternException {
		try {
			return getArxiuClient().documentCsvGenerate(
					toArxiuHeader(capsalera));
		} catch (ArxiuNotFoundException nfex) {
			throw new SistemaExternNoTrobatException(
					nodeId,
					nfex.getArxiuCodi(),
					nfex.getArxiuDescripcio(),
					nfex);
		} catch (ArxiuException aex) {
			throw processarArxiuException(aex);
		}
	}

	@Override
	public void documentDefinitiuGuardarPdfFirmat(
			String nodeId,
			InputStream firmaPdfContingut,
			String csv,
			ArxiuCapsalera capsalera,
			String valcertArxiuNom,
			String valcertDocumentId,
			String valcertDocumentTipus) throws SistemaExternException {
		try {
			getArxiuClient().documentFinalSet(
					toArxiuDocument(
							nodeId,
							null, // titol,
							null, // toOrigenesContenido(origen),
							null, // dataCaptura,
							null, // toEstadosElaboracion(estatElaboracio),
							null, // toTiposDocumentosEni(documentTipus),
							ArxiuFormatNom.PDF, // toFormatosFichero(formatNom),
							ArxiuFormatExtensio.PDF, // toExtensionesFichero(formatExtensio),
							null, // organs,
							TiposFirma.PADES.getValue(),
							PerfilesFirma.EPES.getValue(),
							csv,
							firmaPdfContingut,
							"application/pdf",
							null // serieDocumental
							),
					toArxiuHeader(capsalera));
		} catch (ArxiuNotFoundException nfex) {
			throw new SistemaExternNoTrobatException(
					nodeId,
					nfex.getArxiuCodi(),
					nfex.getArxiuDescripcio(),
					nfex);
		} catch (ArxiuException aex) {
			throw processarArxiuException(aex);
		}
	}

	@Override
	public void documentCopiar(
			String nodeId,
			String nodeDestiId,
			ArxiuCapsalera capsalera) throws SistemaExternException {
		try {
			getArxiuClient().documentCopy(
					nodeId,
					nodeDestiId,
					toArxiuHeader(capsalera));
		} catch (ArxiuNotFoundException nfex) {
			throw new SistemaExternNoTrobatException(
					nodeId,
					nfex.getArxiuCodi(),
					nfex.getArxiuDescripcio(),
					nfex);
		} catch (ArxiuException aex) {
			throw processarArxiuException(aex);
		}
	}

	@Override
	public void documentMoure(
			String nodeId,
			String nodeDestiId,
			ArxiuCapsalera capsalera) throws SistemaExternException {
		try {
			getArxiuClient().documentMove(
					nodeId,
					nodeDestiId,
					toArxiuHeader(capsalera));
		} catch (ArxiuNotFoundException nfex) {
			throw new SistemaExternNoTrobatException(
					nodeId,
					nfex.getArxiuCodi(),
					nfex.getArxiuDescripcio(),
					nfex);
		} catch (ArxiuException aex) {
			throw processarArxiuException(aex);
		}
	}

	@Override
	public String documentExportar(
			String nodeId,
			ArxiuCapsalera capsalera) throws SistemaExternException {
		try {
			return getArxiuClient().documentEniGet(
					nodeId,
					toArxiuHeader(capsalera));
		} catch (ArxiuNotFoundException nfex) {
			throw new SistemaExternNoTrobatException(
					nodeId,
					nfex.getArxiuCodi(),
					nfex.getArxiuDescripcio(),
					nfex);
		} catch (ArxiuException aex) {
			throw processarArxiuException(aex);
		}
	}

	@Override
	public ArxiuCarpeta carpetaCrear(
			String nom,
			String pareNodeId,
			ArxiuCapsalera capsalera) throws SistemaExternException {
		try {
			ArxiuFolder carpeta = getArxiuClient().folderCreate(
					pareNodeId,
					toArxiuFolder(
							null,
							nom),
					toArxiuHeader(capsalera));
			return toArxiuCarpeta(carpeta);
		} catch (ArxiuException aex) {
			throw processarArxiuException(aex);
		}
	}

	@Override
	public void carpetaModificar(
			String nodeId,
			String nom,
			ArxiuCapsalera capsalera) throws SistemaExternException {
		try {
			getArxiuClient().folderUpdate(
					toArxiuFolder(
							nodeId,
							nom),
					toArxiuHeader(capsalera));
		} catch (ArxiuNotFoundException nfex) {
			throw new SistemaExternNoTrobatException(
					nodeId,
					nfex.getArxiuCodi(),
					nfex.getArxiuDescripcio(),
					nfex);
		} catch (ArxiuException aex) {
			throw processarArxiuException(aex);
		}
	}

	@Override
	public void carpetaEsborrar(
			String nodeId,
			ArxiuCapsalera capsalera) throws SistemaExternException {
		try {
			getArxiuClient().folderDelete(
					nodeId,
					toArxiuHeader(capsalera));
		} catch (ArxiuNotFoundException nfex) {
			throw new SistemaExternNoTrobatException(
					nodeId,
					nfex.getArxiuCodi(),
					nfex.getArxiuDescripcio(),
					nfex);
		} catch (ArxiuException aex) {
			throw processarArxiuException(aex);
		}
	}

	@Override
	public ArxiuCarpeta carpetaConsultar(
			String nodeId,
			ArxiuCapsalera capsalera) throws SistemaExternException {
		try {
			ArxiuFolder carpeta = getArxiuClient().folderGet(
					nodeId,
					toArxiuHeader(capsalera));
			return toArxiuCarpeta(carpeta);
		} catch (ArxiuNotFoundException nfex) {
			throw new SistemaExternNoTrobatException(
					nodeId,
					nfex.getArxiuCodi(),
					nfex.getArxiuDescripcio(),
					nfex);
		} catch (ArxiuException aex) {
			throw processarArxiuException(aex);
		}
	}

	@Override
	public void carpetaCopiar(
			String nodeId,
			String nodeDestiId,
			ArxiuCapsalera capsalera) throws SistemaExternException {
		try {
			getArxiuClient().folderCopy(
					nodeId,
					nodeDestiId,
					toArxiuHeader(capsalera));
		} catch (ArxiuNotFoundException nfex) {
			throw new SistemaExternNoTrobatException(
					nodeId,
					nfex.getArxiuCodi(),
					nfex.getArxiuDescripcio(),
					nfex);
		} catch (ArxiuException aex) {
			throw processarArxiuException(aex);
		}
	}

	@Override
	public void carpetaMoure(
			String nodeId,
			String nodeDestiId,
			ArxiuCapsalera capsalera) throws SistemaExternException {
		try {
			getArxiuClient().folderMove(
					nodeId,
					nodeDestiId,
					toArxiuHeader(capsalera));
		} catch (ArxiuNotFoundException nfex) {
			throw new SistemaExternNoTrobatException(
					nodeId,
					nfex.getArxiuCodi(),
					nfex.getArxiuDescripcio(),
					nfex);
		} catch (ArxiuException aex) {
			throw processarArxiuException(aex);
		}
	}



	private ArxiuHeader toArxiuHeader(ArxiuCapsalera capsalera) {
		ArxiuHeader header = new ArxiuHeader();
		header.setInteressatNom(capsalera.getInteressatNom());
		header.setInteressatNif(capsalera.getInteressatNif());
		header.setFuncionariNom(capsalera.getFuncionariNom());
		header.setFuncionariNif(capsalera.getFuncionariNif());
		header.setFuncionariOrgan(capsalera.getFuncionariOrgan());
		header.setProcedimentId(capsalera.getProcedimentId());
		header.setProcedimentNom(capsalera.getProcedimentNom());
		header.setExpedientId(capsalera.getExpedientId());
		return header;
	}

	private ArxiuFile toArxiuFile(
			String nodeId,
			String titol,
			ArxiuOrigenContingut origen,
			Date dataObertura,
			String classificacio,
			ArxiuExpedientEstat estat,
			List<String> organs,
			List<String> interessats,
			String serieDocumental) throws SistemaExternException {
		return new ArxiuFile(
				nodeId,
				titol,
				null,
				crearMetadadesExpedient(
						origen,
						dataObertura,
						classificacio,
						estat,
						organs,
						interessats,
						serieDocumental),
				null);
	}

	private es.caib.ripea.plugin.caib.arxiu.client.ArxiuDocument toArxiuDocument(
			String nodeId,
			String titol,
			ArxiuOrigenContingut origen,
			Date dataCaptura,
			ArxiuEstatElaboracio estatElaboracio,
			ArxiuTipusDocumental documentTipus,
			ArxiuFormatNom formatNom,
			ArxiuFormatExtensio formatExtensio,
			List<String> organs,
			String tipoFirma,
			String perfilFirma,
			String csv,
			InputStream contingut,
			String contentType,
			String serieDocumental) throws SistemaExternException {
		List<ArxiuDocumentContent> contents = null;
		if (contingut != null) {
			contents = new ArrayList<ArxiuDocumentContent>();
			ArxiuDocumentContent content = new ArxiuDocumentContent();
			try {
				content.setContentBase64(
						new String(
								Base64.encode(IOUtils.toByteArray(contingut))));
			} catch (IOException ex) {
				throw new SistemaExternException(
						"No s'ha pogut convertir el contingut del document a Base64",
						ex);
			}
			content.setContentType(contentType);
			content.setEncoding("UTF-8");
			if (tipoFirma != null) {
				content.setType(ArxiuDocumentContentTypeEnum.SIGNATURE);
			} else {
				content.setType(ArxiuDocumentContentTypeEnum.CONTENT);
			}
			
			contents.add(content);
		}
		es.caib.ripea.plugin.caib.arxiu.client.ArxiuDocument arxiuDocument = new es.caib.ripea.plugin.caib.arxiu.client.ArxiuDocument(
				nodeId,
				titol,
				contents,
				crearMetadadesDocument(
						origen,
						dataCaptura,
						estatElaboracio,
						documentTipus,
						formatNom,
						formatExtensio,
						organs,
						tipoFirma,
						perfilFirma,
						csv,
						serieDocumental),
				null);
		if (contingut != null) {
			
		}
		return arxiuDocument;
	}

	private ArxiuFolder toArxiuFolder(
			String nodeId,
			String nom) {
		return new ArxiuFolder(
				nodeId,
				nom,
				null);
	}

	private Map<String, Object> crearMetadadesExpedient(
			ArxiuOrigenContingut origen,
			Date dataObertura,
			String classificacio,
			ArxiuExpedientEstat estat,
			List<String> organs,
			List<String> interessats,
			String serieDocumental) throws SistemaExternException {
		Map<String, Object> metadades  = new HashMap<String, Object>();
		metadades.put(
				MetadatosExpediente.CODIGO_APLICACION_TRAMITE,
				getAplicacioCodi());
		metadades.put(
				MetadatosExpediente.CODIGO_CLASIFICACION,
				serieDocumental);
		metadades.put(
				MetadatosExpediente.IDENTIFICADOR_PROCEDIMIENTO,
				classificacio);
		if (origen != null) {
			metadades.put(
					MetadatosExpediente.ORIGEN,
					toOrigenesContenido(origen));
		}
		if (estat != null) {
			metadades.put(
					MetadatosExpediente.ESTADO_EXPEDIENTE,
					toEstadosExpediente(estat));
		}
		if (organs != null) {
			metadades.put(
					MetadatosExpediente.ORGANO,
					organs);
		}
		if (interessats != null) {
			metadades.put(
					MetadatosExpediente.INTERESADOS,
					interessats);
		}
		if (dataObertura != null){
			metadades.put(
					MetadatosExpediente.FECHA_INICIO,
					formatDateIso8601(dataObertura));
		}
		return metadades;
	}

	private Map<String, Object> crearMetadadesDocument(
			ArxiuOrigenContingut origen,
			Date dataCaptura,
			ArxiuEstatElaboracio estatElaboracio,
			ArxiuTipusDocumental documentTipus,
			ArxiuFormatNom formatNom,
			ArxiuFormatExtensio formatExtensio,
			List<String> organs,
			String tipoFirma,
			String perfilFirma,
			String csv,
			String serieDocumental) throws SistemaExternException {
		Map<String, Object> metadades  = new HashMap<String, Object>();
		metadades.put(
				MetadatosDocumento.CODIGO_APLICACION_TRAMITE,
				getAplicacioCodi());
		if (origen != null) {
			metadades.put(
					MetadatosDocumento.ORIGEN,
					toOrigenesContenido(origen));
		}
		if (dataCaptura != null){
			metadades.put(
					MetadatosDocumento.FECHA_INICIO,
					formatDateIso8601(dataCaptura));
		}
		if (estatElaboracio != null) {
			metadades.put(
					MetadatosDocumento.ESTADO_ELABORACION,
					toEstadosElaboracion(estatElaboracio));
		}
		if (documentTipus != null) {
			metadades.put(
					MetadatosDocumento.TIPO_DOC_ENI,
					toTiposDocumentosEni(documentTipus));
		}
		if (formatNom != null) {
			metadades.put(
					MetadatosDocumento.NOMBRE_FORMATO,
					toFormatosFichero(formatNom));
		}
		if (formatExtensio != null) {
			metadades.put(
					MetadatosDocumento.EXTENSION_FORMATO,
					toExtensionesFichero(formatExtensio));
		}
		if (organs != null) {
			metadades.put(
					MetadatosDocumento.ORGANO,
					organs);
		}
		if (tipoFirma != null) {
			metadades.put(
					MetadatosDocumento.TIPO_FIRMA,
					tipoFirma);
		}
		if (perfilFirma != null) {
			metadades.put(
					MetadatosDocumento.PERFIL_FIRMA,
					perfilFirma);
		}
		if (csv != null) {
			metadades.put(
					MetadatosDocumento.CSV,
					csv);
		}
		if (serieDocumental != null) {
			metadades.put(
					MetadatosDocumento.CODIGO_CLASIFICACION,
					serieDocumental);
		}
		return metadades;
	}

	private String formatDateIso8601(Date date) {
		TimeZone tz = TimeZone.getTimeZone("UTC");
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		df.setTimeZone(tz);
		return df.format(date);
	}
	
	private ArxiuExpedient toArxiuExpedient(ArxiuFile arxiuFile) {
		Map<String, Object> metadades = arxiuFile.getMetadata();
		ArxiuExpedient expedient = new ArxiuExpedient(
				arxiuFile.getNodeId(),
				arxiuFile.getName(),
				toFillsPerRetornar(arxiuFile.getChildren()),
				metadades,
				arxiuFile.getAspects(),
				null);
		expedient.setEniVersio(
				getMetadataValueAsString(
						metadades,
						"eni:v_nti"));
		expedient.setEniIdentificador(
				getMetadataValueAsString(
						metadades,
						"eni:id"));
		expedient.setEniOrigen(
				ArxiuOrigenContingut.valorAsEnum(
						getMetadataValueAsString(
								metadades,
								MetadatosExpediente.ORIGEN)));
		try {
			expedient.setEniDataObertura(
					getMetadataValueAsDate(
							metadades,
							MetadatosExpediente.FECHA_INICIO));
		} catch (ParseException ex) {
			logger.error("Error al obtenir la data d'obertura de l'expedient", ex);
		}
		expedient.setEniClassificacio(
				getMetadataValueAsString(
						metadades,
						MetadatosExpediente.IDENTIFICADOR_PROCEDIMIENTO));
		expedient.setEniEstat(
				ArxiuExpedientEstat.valorAsEnum(
						getMetadataValueAsString(
								metadades,
								MetadatosExpediente.ESTADO_EXPEDIENTE)));
		expedient.setEniOrgans(
				getMetadataValueAsListString(
						metadades,
						MetadatosExpediente.ORGANO));
		expedient.setEniInteressats(
				getMetadataValueAsListString(
						metadades,
						MetadatosExpediente.INTERESADOS));
		expedient.setEniFirmaTipus(
				getMetadataValueAsListString(
						metadades,
						"eni:tipoFirma"));
		expedient.setEniFirmaCsv(
				getMetadataValueAsListString(
						metadades,
						"eni:csv"));
		expedient.setEniFirmaCsvDefinicio(
				getMetadataValueAsListString(
						metadades,
						"eni:def_csv"));
		expedient.setDescripcio(
				getMetadataValueAsString(
						metadades,
						MetadatosExpediente.DESCRIPCION));
		expedient.setSerieDocumental(
				getMetadataValueAsString(
						metadades,
						MetadatosExpediente.CODIGO_CLASIFICACION));
		expedient.setAplicacio(
				getMetadataValueAsString(
						metadades,
						MetadatosExpediente.CODIGO_APLICACION_TRAMITE));
		emplenarCodiFontComunicacioArxiu(expedient);
		return expedient;
	}

	private ArxiuDocument toArxiuDocument(
			es.caib.ripea.plugin.caib.arxiu.client.ArxiuDocument arxiuDocument) {
		Map<String, Object> metadades = arxiuDocument.getMetadata();
		ArxiuDocument document = new ArxiuDocument(
				arxiuDocument.getNodeId(),
				arxiuDocument.getName(),
				toContingutsPerRetornar(arxiuDocument.getContents()),
				metadades,
				arxiuDocument.getAspects(),
				null);
		document.setEniVersio(
				getMetadataValueAsString(
						metadades,
						"eni:v_nti"));
		document.setEniIdentificador(
				getMetadataValueAsString(
						metadades,
						"eni:id"));
		document.setEniOrigen(
				ArxiuOrigenContingut.valorAsEnum(
						getMetadataValueAsString(
								metadades,
								MetadatosDocumento.ORIGEN)));
		try {
			document.setEniDataCaptura(
					getMetadataValueAsDate(
							metadades,
							MetadatosDocumento.FECHA_INICIO));
		} catch (ParseException ex) {
			logger.error("Error al obtenir la data de captura del document", ex);
		}
		document.setEniEstatElaboracio(
				ArxiuEstatElaboracio.valorAsEnum(
						getMetadataValueAsString(
								metadades,
								MetadatosDocumento.ESTADO_ELABORACION)));
		document.setEniTipusDocumental(
				ArxiuTipusDocumental.valorAsEnum(
						getMetadataValueAsString(
								metadades,
								MetadatosDocumento.TIPO_DOC_ENI)));
		document.setEniFormatNom(
				ArxiuFormatNom.valorAsEnum(
						getMetadataValueAsString(
								metadades,
								MetadatosDocumento.NOMBRE_FORMATO)));
		document.setEniFormatExtensio(
				ArxiuFormatExtensio.valorAsEnum(
						getMetadataValueAsString(
								metadades,
								MetadatosDocumento.EXTENSION_FORMATO)));
		document.setEniOrgans(
				getMetadataValueAsListString(
						metadades,
						MetadatosDocumento.ORGANO));
		document.setEniFirmaTipus(
				getMetadataValueAsListString(
						metadades,
						"eni:tipoFirma"));
		document.setEniFirmaCsv(
				getMetadataValueAsListString(
						metadades,
						"eni:csv"));
		document.setEniFirmaCsvDefinicio(
				getMetadataValueAsListString(
						metadades,
						"eni:def_csv"));
		document.setDescripcio(
				getMetadataValueAsString(
						metadades,
						MetadatosExpediente.DESCRIPCION));
		document.setSerieDocumental(
				getMetadataValueAsString(
						metadades,
						MetadatosExpediente.CODIGO_CLASIFICACION));
		document.setAplicacio(
				getMetadataValueAsString(
						metadades,
						MetadatosExpediente.CODIGO_APLICACION_TRAMITE));
		emplenarCodiFontComunicacioArxiu(document);
		return document;
	}

	private ArxiuCarpeta toArxiuCarpeta(
			ArxiuFolder arxiuFolder) {
		ArxiuCarpeta carpeta = new ArxiuCarpeta(
				arxiuFolder.getNodeId(),
				arxiuFolder.getName(),
				toFillsPerRetornar(arxiuFolder.getChildren()),
				null);
		emplenarCodiFontComunicacioArxiu(carpeta);
		return carpeta;
	}

	private List<ArxiuFill> toFillsPerRetornar(
			List<ArxiuContentChild> children) {
		List<ArxiuFill> fills = null;
		if (children != null) {
			fills = new ArrayList<ArxiuFill>();
			for (ArxiuContentChild child: children) {
				ArxiuFillTipusEnum tipus = null;
				if (child.getTipus() != null) {
					switch (child.getTipus()) {
					case EXPEDIENT:
						tipus = ArxiuFillTipusEnum.EXPEDIENT;
						break;
					case DOCUMENT:
						tipus = ArxiuFillTipusEnum.DOCUMENT;
						break;
					case CARPETA:
						tipus = ArxiuFillTipusEnum.CARPETA;
						break;
					case DOCUMENT_MIGRAT:
						tipus = ArxiuFillTipusEnum.DOCUMENT_MIGRAT;
						break;
					}
				}
				fills.add(
						new ArxiuFill(
								child.getNodeId(),
								tipus,
								child.getName()));
			}
		}
		return fills;
	}

	private List<ArxiuDocumentContingut> toContingutsPerRetornar(
			List<ArxiuDocumentContent> contents) {
		List<ArxiuDocumentContingut> continguts = null;
		if (contents != null) {
			continguts = new ArrayList<ArxiuDocumentContingut>();
			for (ArxiuDocumentContent content: contents) {
				ArxiuContingutTipusEnum tipus = null;
				if (content.getType() != null) {
					switch (content.getType()) {
					case CONTENT:
						tipus = ArxiuContingutTipusEnum.CONTINGUT;
						break;
					case SIGNATURE:
						tipus = ArxiuContingutTipusEnum.FIRMA;
						break;
					case SIGNATURE_VALCERT:
						tipus = ArxiuContingutTipusEnum.FIRMA_VALCERT;
						break;
					case MIGRATION_SIGNATURE:
						tipus = ArxiuContingutTipusEnum.MIGRACIO_FIRMA;
						break;
					case MIGRATION_ZIP:
						tipus = ArxiuContingutTipusEnum.MIGRACIO_ZIP;
						break;
					}
				}
				continguts.add(
						new ArxiuDocumentContingut(
								tipus,
								content.getContentType(),
								Base64.decode(content.getContentBase64())));
			}
		}
		return continguts;
	}

	private OrigenesContenido toOrigenesContenido(
			ArxiuOrigenContingut origen) throws SistemaExternException {
		if (origen == null) {
			return null;
		}
		switch (origen) {
		case ADMINISTRACIO:
			return OrigenesContenido.ADMINISTRACION;
		case CIUTADA:
			return OrigenesContenido.CIUDADANO;
		default:
			throw new SistemaExternException(
					"No s'ha pogut convertir el valor per l'enumeració ArxiuOrigenContingut (" +
					"valor=" + origen + ")");
		}
	}

	private EstadosExpediente toEstadosExpediente(
			ArxiuExpedientEstat estat) throws SistemaExternException {
		if (estat == null) {
			return null;
		}
		switch (estat) {
		case INDEX_REMISSIO:
			return EstadosExpediente.INDICE_REMISION;
		case OBERT:
			return EstadosExpediente.ABIERTO;
		case TANCAT:
			return EstadosExpediente.CERRADO;
		default:
			throw new SistemaExternException(
					"No s'ha pogut convertir el valor per l'enumeració ArxiuExpedientEstat (" +
					"valor=" + estat + ")");
		}
	}

	private EstadosElaboracion toEstadosElaboracion(
			ArxiuEstatElaboracio estatElaboracio) throws SistemaExternException {
		if (estatElaboracio == null) {
			return null;
		}
		switch (estatElaboracio) {
		case ALTRES:
			return EstadosElaboracion.OTROS;
		case COPIA_AUTENTICA_FORMAT:
			return EstadosElaboracion.COPIA_AUTENTICA_FORMATO;
		case COPIA_AUTENTICA_PAPER:
			return EstadosElaboracion.COPIA_AUTENTICA_PAPEL;
		case COPIA_AUTENTICA_PARCIAL:
			return EstadosElaboracion.COPIA_AUTENTICA_PARCIAL;
		case ORIGINAL:
			return EstadosElaboracion.ORIGINAL;
		default:
			throw new SistemaExternException(
					"No s'ha pogut convertir el valor per l'enumeració ArxiuEstatElaboracio (" +
					"valor=" + estatElaboracio + ")");
		}
	}

	private TiposDocumentosENI toTiposDocumentosEni(
			ArxiuTipusDocumental documentTipus) throws SistemaExternException {
		if (documentTipus == null) {
			return null;
		}
		switch (documentTipus) {
		case ACORD:
			return TiposDocumentosENI.ACUERDO;
		case ACTA:
			return TiposDocumentosENI.ACTA;
		case ALEGACIO:
			return TiposDocumentosENI.ALEGACION;
		case ALTRES:
			return TiposDocumentosENI.OTROS;
		case ALTRES_INCAUTATS:
			return TiposDocumentosENI.OTROS_INCAUTADOS;
		case CERTIFICAT:
			return TiposDocumentosENI.CERTIFICADO;
		case COMUNICACIO:
			return TiposDocumentosENI.COMUNICACION;
		case COMUNICACIO_CIUTADA:
			return TiposDocumentosENI.COMUNICACION_CIUDADANO;
		case CONTRACTE:
			return TiposDocumentosENI.CONTRATO;
		case CONVENI:
			return TiposDocumentosENI.CONVENIO;
		case DECLARACIO:
			return TiposDocumentosENI.DECLARACION;
		case DENUNCIA:
			return TiposDocumentosENI.DENUNCIA;
		case DILIGENCIA:
			return TiposDocumentosENI.DILIGENCIA;
		case FACTURA:
			return TiposDocumentosENI.FACTURA;
		case INFORME:
			return TiposDocumentosENI.INFORME;
		case JUSTIFICANT_RECEPCIO:
			return TiposDocumentosENI.ACUSE_DE_RECIBO;
		case NOTIFICACIO:
			return TiposDocumentosENI.NOTIFICACION;
		case PUBLICACIO:
			return TiposDocumentosENI.PUBLICACION;
		case RECURSOS:
			return TiposDocumentosENI.RECURSOS;
		case RESOLUCIO:
			return TiposDocumentosENI.RESOLUCION;
		case SOLICITUD:
			return TiposDocumentosENI.SOLICITUD;
		default:
			throw new SistemaExternException(
					"No s'ha pogut convertir el valor per l'enumeració ArxiuDocumentTipus (" +
					"valor=" + documentTipus + ")");
		}
	}

	private FormatosFichero toFormatosFichero(
			ArxiuFormatNom formatNom) throws SistemaExternException {
		if (formatNom == null) {
			return null;
		}
		switch (formatNom) {
		case AVI:
			return FormatosFichero.AVI;
		case CSS:
			return FormatosFichero.CSS;
		case CSV:
			return FormatosFichero.CSV;
		case GML:
			return FormatosFichero.GML;
		case GZIP:
			return FormatosFichero.GZIP;
		case HTML:
			return FormatosFichero.HTML;
		case JPEG:
			return FormatosFichero.JPEG;
		case MHTML:
			return FormatosFichero.MHTML;
		case MP3:
			return FormatosFichero.MP3;
		case MP4A:
			return FormatosFichero.MP4A;
		case MP4V:
			return FormatosFichero.MP4V;
		case OASIS12:
			return FormatosFichero.OASIS12;
		case OGG:
			return FormatosFichero.OGG;
		case PDF:
			return FormatosFichero.PDF;
		case PDFA:
			return FormatosFichero.PDFA;
		case PNG:
			return FormatosFichero.PNG;
		case RTF:
			return FormatosFichero.RTF;
		case SOXML:
			return FormatosFichero.SOXML;
		case SVG:
			return FormatosFichero.SVG;
		case TIFF:
			return FormatosFichero.TIFF;
		case TXT:
			return FormatosFichero.TXT;
		case WEBM:
			return FormatosFichero.WEBM;
		case WFS:
			return FormatosFichero.WFS;
		case WMS:
			return FormatosFichero.WMS;
		case XHTML:
			return FormatosFichero.XHTML;
		case ZIP:
			return FormatosFichero.ZIP;
		default:
			throw new SistemaExternException(
					"No s'ha pogut convertir el valor per l'enumeració ArxiuFormatNom (" +
					"valor=" + formatNom + ")");
		}
	}

	private ExtensionesFichero toExtensionesFichero(
			ArxiuFormatExtensio formatExtensio) throws SistemaExternException {
		if (formatExtensio == null) {
			return null;
		}
		switch (formatExtensio) {
		case AVI:
			return ExtensionesFichero.AVI;
		case CSS:
			return ExtensionesFichero.CSS;
		case CSV:
			return ExtensionesFichero.CSV;
		case DOCX:
			return ExtensionesFichero.DOCX;
		case GML:
			return ExtensionesFichero.GML;
		case GZ:
			return ExtensionesFichero.GZ;
		case HTM:
			return ExtensionesFichero.HTM;
		case HTML:
			return ExtensionesFichero.HTML;
		case JPEG:
			return ExtensionesFichero.JPEG;
		case JPG:
			return ExtensionesFichero.JPG;
		case MHT:
			return ExtensionesFichero.MHT;
		case MHTML:
			return ExtensionesFichero.MHTML;
		case MP3:
			return ExtensionesFichero.MP3;
		case MP4:
			return ExtensionesFichero.MP4;
		case MPEG:
			return ExtensionesFichero.MPEG;
		case ODG:
			return ExtensionesFichero.ODG;
		case ODP:
			return ExtensionesFichero.ODP;
		case ODS:
			return ExtensionesFichero.ODS;
		case ODT:
			return ExtensionesFichero.ODT;
		case OGA:
			return ExtensionesFichero.OGA;
		case OGG:
			return ExtensionesFichero.OGG;
		case PDF:
			return ExtensionesFichero.PDF;
		case PNG:
			return ExtensionesFichero.PNG;
		case PPTX:
			return ExtensionesFichero.PPTX;
		case RTF:
			return ExtensionesFichero.RTF;
		case SVG:
			return ExtensionesFichero.SVG;
		case TIFF:
			return ExtensionesFichero.TIFF;
		case TXT:
			return ExtensionesFichero.TXT;
		case WEBM:
			return ExtensionesFichero.WEBM;
		case XLSX:
			return ExtensionesFichero.XLSX;
		case ZIP:
			return ExtensionesFichero.ZIP;
		default:
			throw new SistemaExternException(
					"No s'ha pogut convertir el valor per l'enumeració ArxiuFormatExtensio (" +
					"valor=" + formatExtensio + ")");
		}
	}

	private String getMetadataValueAsString(
			Map<String, Object> metadades,
			String qname) {
		if (metadades == null) {
			return null;
		}
		if (metadades.get(qname) == null) {
			return null;
		}
		return metadades.get(qname).toString();
	}
	private Date getMetadataValueAsDate(
			Map<String, Object> metadades,
			String qname) throws ParseException {
		if (metadades == null) {
			return null;
		}
		if (metadades.get(qname) == null) {
			return null;
		}
		return parseDateIso8601(metadades.get(qname).toString());
	}
	@SuppressWarnings("unchecked")
	private List<String> getMetadataValueAsListString(
			Map<String, Object> metadades,
			String qname) {
		if (metadades == null) {
			return null;
		}
		if (metadades.get(qname) == null) {
			return null;
		}
		if (metadades.get(qname) instanceof List) {
			return (List<String>)metadades.get(qname);
		} else {
			List<String> resultat = new ArrayList<String>();
			resultat.add(metadades.get(qname).toString());
			return resultat;
		}
	}

	private Date parseDateIso8601(String date) throws ParseException {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX");
		return df.parse(date);
	}

	private ArxiuClient getArxiuClient() {
		if (arxiuClient == null) {
			arxiuClient = new ArxiuClientImpl(
					getBaseUrl(),
					getAplicacioCodi(),
					getUsuari(),
					getContrasenya());
		}
		return arxiuClient;
	}

	private SistemaExternException processarArxiuException(
			ArxiuException aex) {
		if (aex.getArxiuCodi() != null) {
			return new SistemaExternCodiMissatgeException(
					aex.getArxiuCodi(),
					aex.getArxiuDescripcio(),
					aex);
		} else {
			return new SistemaExternException(
					aex.getMessage(),
					aex);
		}
	}

	private void emplenarCodiFontComunicacioArxiu(ArxiuContingut arxiuContingut) {
		ArxiuClient arxiuClient = getArxiuClient();
		if (arxiuClient instanceof ArxiuClientImpl) {
			arxiuContingut.setCodiFontPeticio(
					((ArxiuClientImpl)arxiuClient).getLastJsonRequest());
			arxiuContingut.setCodiFontResposta(
					((ArxiuClientImpl)arxiuClient).getLastJsonResponse());
		}
	}

	private String getBaseUrl() {
		return PropertiesHelper.getProperties().getProperty("es.caib.ripea.plugin.arxiu.caib.base.url");
	}
	private String getAplicacioCodi() {
		return PropertiesHelper.getProperties().getProperty("es.caib.ripea.plugin.arxiu.caib.aplicacio.codi");
	}
	private String getUsuari() {
		return PropertiesHelper.getProperties().getProperty("es.caib.ripea.plugin.arxiu.caib.usuari");
	}
	private String getContrasenya() {
		return PropertiesHelper.getProperties().getProperty("es.caib.ripea.plugin.arxiu.caib.contrasenya");
	}

	private static final Logger logger = LoggerFactory.getLogger(ArxiuPluginCaib.class);

}
