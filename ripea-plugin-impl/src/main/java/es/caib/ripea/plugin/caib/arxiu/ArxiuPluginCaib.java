/**
 * 
 */
package es.caib.ripea.plugin.caib.arxiu;

import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.axis.encoding.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.caib.arxiudigital.apirest.CSGD.entidades.comunes.Content;
import es.caib.arxiudigital.apirest.CSGD.entidades.comunes.Metadata;
import es.caib.arxiudigital.apirest.CSGD.entidades.comunes.SummaryInfoNode;
import es.caib.arxiudigital.apirest.constantes.Aspectos;
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
import es.caib.ripea.plugin.caib.arxiu.client.ArxiuException;
import es.caib.ripea.plugin.caib.arxiu.client.ArxiuFile;
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

	private static final String SERVEI_VERSIO = "1.0";

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
					titol,
					toOrigenesContenido(origen),
					dataObertura,
					classificacio,
					toEstadosExpediente(estat),
					organs,
					interessats,
					serieDocumental,
					null,
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
					nodeId,
					titol,
					toOrigenesContenido(origen),
					dataObertura,
					classificacio,
					toEstadosExpediente(estat),
					organs,
					interessats,
					serieDocumental,
					null,
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
			String tipusMime,
			String pareNodeId,
			ArxiuCapsalera capsalera) throws SistemaExternException {
		try {
			es.caib.ripea.plugin.caib.arxiu.client.ArxiuDocument document = getArxiuClient().documentDraftCreate(
					titol,
					toOrigenesContenido(origen),
					dataCaptura,
					toEstadosElaboracion(estatElaboracio),
					toTiposDocumentosEni(documentTipus),
					toFormatosFichero(formatNom),
					organs,
					serieDocumental,
					contingut,
					pareNodeId,
					toExtensionesFichero(formatExtensio),
					tipusMime,
					null,
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
			String tipusMime,
			ArxiuCapsalera capsalera) throws SistemaExternException {
		try {
			getArxiuClient().documentUpdate(
					nodeId,
					titol,
					toOrigenesContenido(origen),
					dataCaptura,
					toEstadosElaboracion(estatElaboracio),
					toTiposDocumentosEni(documentTipus),
					toFormatosFichero(formatNom),
					organs,
					serieDocumental,
					contingut,
					toExtensionesFichero(formatExtensio),
					tipusMime,
					null,
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
			List<es.caib.ripea.plugin.caib.arxiu.client.ArxiuDocumentVersio> vs = getArxiuClient().documentVersionList(
					nodeId,
					toArxiuHeader(capsalera));
			for (es.caib.ripea.plugin.caib.arxiu.client.ArxiuDocumentVersio v: vs) {
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
					nodeId,
					null, // titol,
					null, // toOrigenesContenido(origen),
					null, // dataCaptura,
					null, // toEstadosElaboracion(estatElaboracio),
					null, // toTiposDocumentosEni(documentTipus),
					null, // toFormatosFichero(formatNom),
					null, // organs,
					null, // serieDocumental,
					firmaPdfContingut,
					TiposFirma.CSV,
					PerfilesFirma.EPES,
					csv,
					null, // toExtensionesFichero(formatExtensio),
					"application/pdf", // tipusMime,
					null,
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
			es.caib.ripea.plugin.caib.arxiu.client.ArxiuFolder carpeta = getArxiuClient().folderCreate(
					nom,
					pareNodeId,
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
					nodeId,
					nom,
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
			es.caib.ripea.plugin.caib.arxiu.client.ArxiuFolder carpeta = getArxiuClient().folderGet(
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

	private ArxiuExpedient toArxiuExpedient(ArxiuFile arxiuFile) {
		Map<String, Object> metadades = toMetadadesPerRetornar(
				arxiuFile.getMetadades());
		ArxiuExpedient expedient = new ArxiuExpedient(
				arxiuFile.getNodeId(),
				arxiuFile.getTitol(),
				toFillsPerRetornar(arxiuFile.getFills()),
				metadades,
				toAspectesPerRetornar(arxiuFile.getAspectes()),
				arxiuFile.getJson());
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
		return expedient;
	}

	private ArxiuDocument toArxiuDocument(
			es.caib.ripea.plugin.caib.arxiu.client.ArxiuDocument arxiuDocument) {
		Map<String, Object> metadades = toMetadadesPerRetornar(
				arxiuDocument.getMetadades());
		ArxiuDocument document = new ArxiuDocument(
				arxiuDocument.getNodeId(),
				arxiuDocument.getTitol(),
				toContingutsPerRetornar(arxiuDocument.getContinguts()),
				metadades,
				toAspectesPerRetornar(arxiuDocument.getAspectes()),
				arxiuDocument.getJson());
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
		return document;
	}

	private ArxiuCarpeta toArxiuCarpeta(
			es.caib.ripea.plugin.caib.arxiu.client.ArxiuFolder arxiuFolder) {
		ArxiuCarpeta carpeta = new ArxiuCarpeta(
				arxiuFolder.getNodeId(),
				arxiuFolder.getTitol(),
				toFillsPerRetornar(arxiuFolder.getFills()),
				arxiuFolder.getJson());
		return carpeta;
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
		case ALTRES:
			return TiposDocumentosENI.OTROS;
		case CONTRACTE:
			return TiposDocumentosENI.CONTRATO;
		case CONVENI:
			return TiposDocumentosENI.CONVENIO;
		case NOTIFICACIO:
			return TiposDocumentosENI.NOTIFICACION;
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

	private Map<String, Object> toMetadadesPerRetornar(List<Metadata> ms) {
		Map<String, Object> metadades = null;
		if (ms != null) {
			metadades = new HashMap<String, Object>();
			for (Metadata m: ms) {
				metadades.put(
						m.getQname(),
						m.getValue());
			}
		}
		return metadades;
	}
	private List<String> toAspectesPerRetornar(List<Aspectos> as) {
		List<String> aspectes = null;
		if (as != null) {
			aspectes = new ArrayList<String>();
			for (Aspectos a: as) {
				aspectes.add(a.getValue());
			}
		}
		return aspectes;
	}
	private List<ArxiuFill> toFillsPerRetornar(List<SummaryInfoNode> ns) {
		List<ArxiuFill> fills = null;
		if (ns != null) {
			fills = new ArrayList<ArxiuFill>();
			for (SummaryInfoNode n: ns) {
				ArxiuFillTipusEnum tipus = null;
				if (n.getType() != null) {
					switch (n.getType()) {
					case EXPEDIENTE:
						tipus = ArxiuFillTipusEnum.EXPEDIENT;
						break;
					case DOCUMENTO:
						tipus = ArxiuFillTipusEnum.DOCUMENT;
						break;
					case DIRECTORIO:
						tipus = ArxiuFillTipusEnum.CARPETA;
						break;
					case DOCUMENTO_MIGRADO:
						tipus = ArxiuFillTipusEnum.DOCUMENT_MIGRAT;
						break;
					}
				}
				fills.add(
						new ArxiuFill(
								n.getId(),
								tipus,
								n.getName()));
			}
		}
		return fills;
	}
	private List<ArxiuDocumentContingut> toContingutsPerRetornar(List<Content> cs) {
		List<ArxiuDocumentContingut> continguts = null;
		if (cs != null) {
			continguts = new ArrayList<ArxiuDocumentContingut>();
			for (Content c: cs) {
				ArxiuContingutTipusEnum tipus = null;
				if (c.getBinaryType() != null) {
					switch (c.getBinaryType()) {
					case CONTENT:
						tipus = ArxiuContingutTipusEnum.CONTINGUT;
						break;
					case SIGNATURE:
						tipus = ArxiuContingutTipusEnum.FIRMA;
						break;
					case VALCERT_SIGNATURE:
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
								c.getMimetype(),
								Base64.decode(c.getContent())));
			}
		}
		return continguts;
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
					SERVEI_VERSIO,
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
