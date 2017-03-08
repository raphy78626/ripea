/**
 * 
 */
package es.caib.ripea.plugin.caib.arxiu;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.axis.encoding.Base64;

import es.caib.arxiudigital.apirest.CSGD.entidades.comunes.Content;
import es.caib.arxiudigital.apirest.constantes.EstadosElaboracion;
import es.caib.arxiudigital.apirest.constantes.EstadosExpediente;
import es.caib.arxiudigital.apirest.constantes.ExtensionesFichero;
import es.caib.arxiudigital.apirest.constantes.FormatosFichero;
import es.caib.arxiudigital.apirest.constantes.OrigenesContenido;
import es.caib.arxiudigital.apirest.constantes.TiposContenidosBinarios;
import es.caib.arxiudigital.apirest.constantes.TiposDocumentosENI;
import es.caib.ripea.plugin.SistemaExternCodiMissatgeException;
import es.caib.ripea.plugin.SistemaExternException;
import es.caib.ripea.plugin.SistemaExternNoTrobatException;
import es.caib.ripea.plugin.arxiu.ArxiuCapsalera;
import es.caib.ripea.plugin.arxiu.ArxiuCarpeta;
import es.caib.ripea.plugin.arxiu.ArxiuDocument;
import es.caib.ripea.plugin.arxiu.ArxiuDocumentContingut;
import es.caib.ripea.plugin.arxiu.ArxiuDocumentVersio;
import es.caib.ripea.plugin.arxiu.ArxiuEstatElaboracio;
import es.caib.ripea.plugin.arxiu.ArxiuExpedient;
import es.caib.ripea.plugin.arxiu.ArxiuExpedientEstat;
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
public class ArxiuPluginImpl implements ArxiuPlugin {

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
	public ArxiuExpedient expedientObtenirAmbId(
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
					toExtensionesFichero(formatExtensio),
					organs,
					serieDocumental,
					contingut,
					tipusMime,
					pareNodeId,
					toArxiuHeader(capsalera));
			return toArxiuDocument(document);
		} catch (ArxiuException aex) {
			throw processarArxiuException(aex);
		}
	}

	@Override
	public void documentModificar(
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
					toExtensionesFichero(formatExtensio),
					organs,
					serieDocumental,
					contingut,
					tipusMime,
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
	public ArxiuDocument documentObtenirAmbId(
			String nodeId,
			boolean ambContingut,
			ArxiuCapsalera capsalera) throws SistemaExternException {
		try {
			es.caib.ripea.plugin.caib.arxiu.client.ArxiuDocument document = getArxiuClient().documentGet(
					nodeId,
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
	public String documentGenerarCsv(
			String nodeId,
			ArxiuCapsalera capsalera) throws SistemaExternException {
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
	public ArxiuDocument documentObtenirAmbCsv(
			String csv,
			boolean ambContingut,
			ArxiuCapsalera capsalera) throws SistemaExternException {
		try {
			es.caib.ripea.plugin.caib.arxiu.client.ArxiuDocument document = getArxiuClient().documentGet(
					null,
					csv,
					ambContingut,
					toArxiuHeader(capsalera));
			return toArxiuDocument(document);
		} catch (ArxiuNotFoundException nfex) {
			throw new SistemaExternNoTrobatException(
					"csv:" + csv,
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
	public ArxiuCarpeta carpetaObtenirAmbId(
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
		ArxiuExpedient expedient = new ArxiuExpedient();
		expedient.setNodeId(arxiuFile.getNodeId());
		expedient.setTitol(arxiuFile.getTitol());
		expedient.setDescripcio(arxiuFile.getDescripcio());
		expedient.setEniVersio(arxiuFile.getEniVersio());
		expedient.setEniIdentificador(arxiuFile.getEniIdentificador());
		if (OrigenesContenido.CIUDADANO.equals(arxiuFile.getEniOrigen())) {
			expedient.setEniOrigen(ArxiuOrigenContingut.CIUTADA);
		} else if (OrigenesContenido.ADMINISTRACION.equals(arxiuFile.getEniOrigen())) {
			expedient.setEniOrigen(ArxiuOrigenContingut.ADMINISTRACIO);
		}
		expedient.setEniOrgans(arxiuFile.getEniOrgans());
		expedient.setEniDataObertura(arxiuFile.getEniDataObertura());
		expedient.setEniClassificacio(arxiuFile.getEniClassificacio());
		if (EstadosExpediente.ABIERTO.equals(arxiuFile.getEniEstat())) {
			expedient.setEniEstat(ArxiuExpedientEstat.OBERT);
		} else if (EstadosExpediente.CERRADO.equals(arxiuFile.getEniEstat())) {
			expedient.setEniEstat(ArxiuExpedientEstat.TANCAT);
		} else if (EstadosExpediente.INDICE_REMISION.equals(arxiuFile.getEniEstat())) {
			expedient.setEniEstat(ArxiuExpedientEstat.INDEX_REMISSIO_TANCAT);
		}
		expedient.setEniInteressats(arxiuFile.getEniInteressats());
		expedient.setEniFirmaTipus(arxiuFile.getEniFirmaTipus());
		expedient.setEniFirmaCsv(arxiuFile.getEniFirmaCsv());
		expedient.setEniFirmaCsvDefinicio(arxiuFile.getEniFirmaCsvDefinicio());
		expedient.setSerieDocumental(arxiuFile.getSerieDocumental());
		expedient.setAplicacioCreacio(arxiuFile.getAplicacioCreacio());
		expedient.setSuport(arxiuFile.getSuport());
		expedient.setJson(arxiuFile.getJson());
		return expedient;
	}

	private ArxiuDocument toArxiuDocument(
			es.caib.ripea.plugin.caib.arxiu.client.ArxiuDocument arxiuDocument) {
		ArxiuDocument document = new ArxiuDocument();
		document.setNodeId(arxiuDocument.getNodeId());
		document.setTitol(arxiuDocument.getTitol());
		document.setDescripcio(arxiuDocument.getDescripcio());
		document.setEniVersio(arxiuDocument.getEniVersio());
		document.setEniIdentificador(arxiuDocument.getEniIdentificador());
		if (OrigenesContenido.CIUDADANO.equals(arxiuDocument.getEniOrigen())) {
			document.setEniOrigen(ArxiuOrigenContingut.CIUTADA);
		} else if (OrigenesContenido.ADMINISTRACION.equals(arxiuDocument.getEniOrigen())) {
			document.setEniOrigen(ArxiuOrigenContingut.ADMINISTRACIO);
		}
		document.setEniDataCaptura(arxiuDocument.getEniDataCaptura());
		if (EstadosElaboracion.ORIGINAL.equals(arxiuDocument.getEniEstatElaboracio())) {
			document.setEniEstatElaboracio(ArxiuEstatElaboracio.ORIGINAL);
		} else if (EstadosElaboracion.COPIA_AUTENTICA_PAPEL.equals(arxiuDocument.getEniEstatElaboracio())) {
			document.setEniEstatElaboracio(ArxiuEstatElaboracio.COPIA_AUTENTICA_PAPER);
		} else if (EstadosElaboracion.COPIA_AUTENTICA_PARCIAL.equals(arxiuDocument.getEniEstatElaboracio())) {
			document.setEniEstatElaboracio(ArxiuEstatElaboracio.COPIA_AUTENTICA_PARCIAL);
		} else if (EstadosElaboracion.COPIA_AUTENTICA_FORMATO.equals(arxiuDocument.getEniEstatElaboracio())) {
			document.setEniEstatElaboracio(ArxiuEstatElaboracio.COPIA_AUTENTICA_FORMAT);
		} else if (EstadosElaboracion.OTROS.equals(arxiuDocument.getEniEstatElaboracio())) {
			document.setEniEstatElaboracio(ArxiuEstatElaboracio.ALTRES);
		}
		if (TiposDocumentosENI.RESOLUCION.equals(arxiuDocument.getEniTipusDocumental())) {
			document.setEniTipusDocumental(ArxiuTipusDocumental.RESOLUCIO);
		} else if (TiposDocumentosENI.ACUERDO.equals(arxiuDocument.getEniTipusDocumental())) {
			document.setEniTipusDocumental(ArxiuTipusDocumental.ACORD);
		} else if (TiposDocumentosENI.CONTRATO.equals(arxiuDocument.getEniTipusDocumental())) {
			document.setEniTipusDocumental(ArxiuTipusDocumental.CONTRACTE);
		} else if (TiposDocumentosENI.CONVENIO.equals(arxiuDocument.getEniTipusDocumental())) {
			document.setEniTipusDocumental(ArxiuTipusDocumental.CONVENI);
		} else if (TiposDocumentosENI.NOTIFICACION.equals(arxiuDocument.getEniTipusDocumental())) {
			document.setEniTipusDocumental(ArxiuTipusDocumental.NOTIFICACIO);
		} else if (TiposDocumentosENI.SOLICITUD.equals(arxiuDocument.getEniTipusDocumental())) {
			document.setEniTipusDocumental(ArxiuTipusDocumental.SOLICITUD);
		} else if (TiposDocumentosENI.OTROS.equals(arxiuDocument.getEniTipusDocumental())) {
			document.setEniTipusDocumental(ArxiuTipusDocumental.ALTRES);
		}
		if (FormatosFichero.GML.equals(arxiuDocument.getEniFormatNom())) {
			document.setEniFormatNom(ArxiuFormatNom.GML);
		} else if (FormatosFichero.WFS.equals(arxiuDocument.getEniFormatNom())) {
			document.setEniFormatNom(ArxiuFormatNom.WFS);
		} else if (FormatosFichero.WMS.equals(arxiuDocument.getEniFormatNom())) {
			document.setEniFormatNom(ArxiuFormatNom.WMS);
		} else if (FormatosFichero.GZIP.equals(arxiuDocument.getEniFormatNom())) {
			document.setEniFormatNom(ArxiuFormatNom.GZIP);
		} else if (FormatosFichero.ZIP.equals(arxiuDocument.getEniFormatNom())) {
			document.setEniFormatNom(ArxiuFormatNom.ZIP);
		} else if (FormatosFichero.AVI.equals(arxiuDocument.getEniFormatNom())) {
			document.setEniFormatNom(ArxiuFormatNom.AVI);
		} else if (FormatosFichero.MP4A.equals(arxiuDocument.getEniFormatNom())) {
			document.setEniFormatNom(ArxiuFormatNom.MP4A);
		} else if (FormatosFichero.CSV.equals(arxiuDocument.getEniFormatNom())) {
			document.setEniFormatNom(ArxiuFormatNom.CSV);
		} else if (FormatosFichero.HTML.equals(arxiuDocument.getEniFormatNom())) {
			document.setEniFormatNom(ArxiuFormatNom.HTML);
		} else if (FormatosFichero.CSS.equals(arxiuDocument.getEniFormatNom())) {
			document.setEniFormatNom(ArxiuFormatNom.CSS);
		} else if (FormatosFichero.JPEG.equals(arxiuDocument.getEniFormatNom())) {
			document.setEniFormatNom(ArxiuFormatNom.JPEG);
		} else if (FormatosFichero.MHTML.equals(arxiuDocument.getEniFormatNom())) {
			document.setEniFormatNom(ArxiuFormatNom.MHTML);
		} else if (FormatosFichero.OASIS12.equals(arxiuDocument.getEniFormatNom())) {
			document.setEniFormatNom(ArxiuFormatNom.OASIS12);
		} else if (FormatosFichero.SOXML.equals(arxiuDocument.getEniFormatNom())) {
			document.setEniFormatNom(ArxiuFormatNom.SOXML);
		} else if (FormatosFichero.PDF.equals(arxiuDocument.getEniFormatNom())) {
			document.setEniFormatNom(ArxiuFormatNom.PDF);
		} else if (FormatosFichero.PDFA.equals(arxiuDocument.getEniFormatNom())) {
			document.setEniFormatNom(ArxiuFormatNom.PDFA);
		} else if (FormatosFichero.PNG.equals(arxiuDocument.getEniFormatNom())) {
			document.setEniFormatNom(ArxiuFormatNom.PNG);
		} else if (FormatosFichero.RTF.equals(arxiuDocument.getEniFormatNom())) {
			document.setEniFormatNom(ArxiuFormatNom.RTF);
		} else if (FormatosFichero.SVG.equals(arxiuDocument.getEniFormatNom())) {
			document.setEniFormatNom(ArxiuFormatNom.SVG);
		} else if (FormatosFichero.TIFF.equals(arxiuDocument.getEniFormatNom())) {
			document.setEniFormatNom(ArxiuFormatNom.TIFF);
		} else if (FormatosFichero.TXT.equals(arxiuDocument.getEniFormatNom())) {
			document.setEniFormatNom(ArxiuFormatNom.TXT);
		} else if (FormatosFichero.XHTML.equals(arxiuDocument.getEniFormatNom())) {
			document.setEniFormatNom(ArxiuFormatNom.XHTML);
		} else if (FormatosFichero.MP3.equals(arxiuDocument.getEniFormatNom())) {
			document.setEniFormatNom(ArxiuFormatNom.MP3);
		} else if (FormatosFichero.OGG.equals(arxiuDocument.getEniFormatNom())) {
			document.setEniFormatNom(ArxiuFormatNom.OGG);
		} else if (FormatosFichero.MP4V.equals(arxiuDocument.getEniFormatNom())) {
			document.setEniFormatNom(ArxiuFormatNom.MP4V);
		} else if (FormatosFichero.WEBM.equals(arxiuDocument.getEniFormatNom())) {
			document.setEniFormatNom(ArxiuFormatNom.WEBM);
		}
		if (ExtensionesFichero.GML.equals(arxiuDocument.getEniFormatExtensio())) {
			document.setEniFormatExtensio(ArxiuFormatExtensio.GML);
		} else if (ExtensionesFichero.GZ.equals(arxiuDocument.getEniFormatExtensio())) {
			document.setEniFormatExtensio(ArxiuFormatExtensio.GZ);
		} else if (ExtensionesFichero.ZIP.equals(arxiuDocument.getEniFormatExtensio())) {
			document.setEniFormatExtensio(ArxiuFormatExtensio.ZIP);
		} else if (ExtensionesFichero.AVI.equals(arxiuDocument.getEniFormatExtensio())) {
			document.setEniFormatExtensio(ArxiuFormatExtensio.AVI);
		} else if (ExtensionesFichero.CSV.equals(arxiuDocument.getEniFormatExtensio())) {
			document.setEniFormatExtensio(ArxiuFormatExtensio.CSV);
		} else if (ExtensionesFichero.HTML.equals(arxiuDocument.getEniFormatExtensio())) {
			document.setEniFormatExtensio(ArxiuFormatExtensio.HTML);
		} else if (ExtensionesFichero.HTM.equals(arxiuDocument.getEniFormatExtensio())) {
			document.setEniFormatExtensio(ArxiuFormatExtensio.HTM);
		} else if (ExtensionesFichero.CSS.equals(arxiuDocument.getEniFormatExtensio())) {
			document.setEniFormatExtensio(ArxiuFormatExtensio.CSS);
		} else if (ExtensionesFichero.JPG.equals(arxiuDocument.getEniFormatExtensio())) {
			document.setEniFormatExtensio(ArxiuFormatExtensio.JPG);
		} else if (ExtensionesFichero.JPEG.equals(arxiuDocument.getEniFormatExtensio())) {
			document.setEniFormatExtensio(ArxiuFormatExtensio.JPEG);
		} else if (ExtensionesFichero.MHTML.equals(arxiuDocument.getEniFormatExtensio())) {
			document.setEniFormatExtensio(ArxiuFormatExtensio.MHTML);
		} else if (ExtensionesFichero.MHT.equals(arxiuDocument.getEniFormatExtensio())) {
			document.setEniFormatExtensio(ArxiuFormatExtensio.MHT);
		} else if (ExtensionesFichero.ODT.equals(arxiuDocument.getEniFormatExtensio())) {
			document.setEniFormatExtensio(ArxiuFormatExtensio.ODT);
		} else if (ExtensionesFichero.ODS.equals(arxiuDocument.getEniFormatExtensio())) {
			document.setEniFormatExtensio(ArxiuFormatExtensio.ODS);
		} else if (ExtensionesFichero.ODP.equals(arxiuDocument.getEniFormatExtensio())) {
			document.setEniFormatExtensio(ArxiuFormatExtensio.ODP);
		} else if (ExtensionesFichero.ODG.equals(arxiuDocument.getEniFormatExtensio())) {
			document.setEniFormatExtensio(ArxiuFormatExtensio.ODG);
		} else if (ExtensionesFichero.DOCX.equals(arxiuDocument.getEniFormatExtensio())) {
			document.setEniFormatExtensio(ArxiuFormatExtensio.DOCX);
		} else if (ExtensionesFichero.XLSX.equals(arxiuDocument.getEniFormatExtensio())) {
			document.setEniFormatExtensio(ArxiuFormatExtensio.XLSX);
		} else if (ExtensionesFichero.PPTX.equals(arxiuDocument.getEniFormatExtensio())) {
			document.setEniFormatExtensio(ArxiuFormatExtensio.PPTX);
		} else if (ExtensionesFichero.PDF.equals(arxiuDocument.getEniFormatExtensio())) {
			document.setEniFormatExtensio(ArxiuFormatExtensio.PDF);
		} else if (ExtensionesFichero.PNG.equals(arxiuDocument.getEniFormatExtensio())) {
			document.setEniFormatExtensio(ArxiuFormatExtensio.PNG);
		} else if (ExtensionesFichero.RTF.equals(arxiuDocument.getEniFormatExtensio())) {
			document.setEniFormatExtensio(ArxiuFormatExtensio.RTF);
		} else if (ExtensionesFichero.SVG.equals(arxiuDocument.getEniFormatExtensio())) {
			document.setEniFormatExtensio(ArxiuFormatExtensio.SVG);
		} else if (ExtensionesFichero.TIFF.equals(arxiuDocument.getEniFormatExtensio())) {
			document.setEniFormatExtensio(ArxiuFormatExtensio.TIFF);
		} else if (ExtensionesFichero.TXT.equals(arxiuDocument.getEniFormatExtensio())) {
			document.setEniFormatExtensio(ArxiuFormatExtensio.TXT);
		} else if (ExtensionesFichero.MP3.equals(arxiuDocument.getEniFormatExtensio())) {
			document.setEniFormatExtensio(ArxiuFormatExtensio.MP3);
		} else if (ExtensionesFichero.OGG.equals(arxiuDocument.getEniFormatExtensio())) {
			document.setEniFormatExtensio(ArxiuFormatExtensio.OGG);
		} else if (ExtensionesFichero.OGA.equals(arxiuDocument.getEniFormatExtensio())) {
			document.setEniFormatExtensio(ArxiuFormatExtensio.OGA);
		} else if (ExtensionesFichero.MPEG.equals(arxiuDocument.getEniFormatExtensio())) {
			document.setEniFormatExtensio(ArxiuFormatExtensio.MPEG);
		} else if (ExtensionesFichero.MP4.equals(arxiuDocument.getEniFormatExtensio())) {
			document.setEniFormatExtensio(ArxiuFormatExtensio.MP4);
		} else if (ExtensionesFichero.WEBM.equals(arxiuDocument.getEniFormatExtensio())) {
			document.setEniFormatExtensio(ArxiuFormatExtensio.WEBM);
		}
		document.setEniOrgans(arxiuDocument.getEniOrgans());
		document.setEniFirmaTipus(arxiuDocument.getEniFirmaTipus());
		document.setEniFirmaCsv(arxiuDocument.getEniFirmaCsv());
		document.setEniFirmaCsvDefinicio(arxiuDocument.getEniFirmaCsvDefinicio());
		document.setSerieDocumental(arxiuDocument.getSerieDocumental());
		document.setAplicacioCreacio(arxiuDocument.getAplicacioCreacio());
		document.setSuport(arxiuDocument.getSuport());
		document.setJson(arxiuDocument.getJson());
		if (arxiuDocument.getContinguts() != null) {
			for (Content content: arxiuDocument.getContinguts()) {
				if (TiposContenidosBinarios.CONTENT.equals(content.getBinaryType())) {
					ArxiuDocumentContingut contingut = new ArxiuDocumentContingut();
					if (content.getMimetype() != null) {
						contingut.setContentType(content.getMimetype());
					}
					contingut.setContingut(Base64.decode(content.getContent()));
					document.setContingut(contingut);
					break;
				}
			}
		}
		return document;
	}

	private ArxiuCarpeta toArxiuCarpeta(
			es.caib.ripea.plugin.caib.arxiu.client.ArxiuFolder arxiuFolder) {
		ArxiuCarpeta carpeta = new ArxiuCarpeta();
		carpeta.setNodeId(arxiuFolder.getNodeId());
		carpeta.setNom(arxiuFolder.getName());
		carpeta.setJson(arxiuFolder.getJson());
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
		case INDEX_REMISSIO_TANCAT:
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

}
