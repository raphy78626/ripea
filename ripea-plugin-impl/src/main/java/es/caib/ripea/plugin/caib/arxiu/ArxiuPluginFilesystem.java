/**
 * 
 */
package es.caib.ripea.plugin.caib.arxiu;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.IOUtils;

import es.caib.ripea.plugin.SistemaExternException;
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
import es.caib.ripea.plugin.utils.PropertiesHelper;

/**
 * Implementació del plugin d'arxiu per a emmagatzemar els arxius
 * a dins una carpeta del servidor.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public class ArxiuPluginFilesystem implements ArxiuPlugin {

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
		throw new SistemaExternException("Mètode no suportat");
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
		throw new SistemaExternException("Mètode no suportat");
	}

	@Override
	public void expedientEsborrar(
			String nodeId,
			ArxiuCapsalera capsalera) throws SistemaExternException {
		throw new SistemaExternException("Mètode no suportat");
	}

	@Override
	public ArxiuExpedient expedientObtenirAmbId(
			String nodeId,
			ArxiuCapsalera capsalera) throws SistemaExternException {
		throw new SistemaExternException("Mètode no suportat");
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
			ArxiuDocument arxiuDocument = new ArxiuDocument();
			String id = saveWithNewId(contingut);
			arxiuDocument.setNodeId(id);
			return arxiuDocument;
		} catch (Exception ex) {
			throw new SistemaExternException(
					"No s'ha pogut crear l'arxiu",
					ex);
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
			if (!updateWithId(nodeId, contingut)) {
				throw new SistemaExternException(
						"No s'ha trobat l'arxiu (id=" + nodeId + ")");
			}
		} catch (Exception ex) {
			throw new SistemaExternException(
					"No s'ha pogut modificar l'arxiu",
					ex);
		}
	}

	@Override
	public void documentEsborrar(
			String nodeId,
			ArxiuCapsalera capsalera) throws SistemaExternException {
		try {
			if (!deleteWithId(nodeId)) {
				throw new SistemaExternException(
						"No s'ha trobat l'arxiu (id=" + nodeId + ")");
			}
		} catch (Exception ex) {
			throw new SistemaExternException(
					"No s'ha pogut esborrar l'arxiu",
					ex);
		}
	}

	@Override
	public ArxiuDocument documentObtenirAmbId(
			String nodeId,
			boolean ambContingut,
			ArxiuCapsalera capsalera) throws SistemaExternException {
		try {
			ArxiuDocument document = readWithId(nodeId);
			if (document == null) {
				throw new SistemaExternException(
						"No s'ha trobat l'arxiu (id=" + nodeId + ")");
			} else {
				return document;
			}
		} catch (Exception ex) {
			throw new SistemaExternException(
					"No s'ha pogut obtenir l'arxiu",
					ex);
		}
	}

	@Override
	public String documentGenerarCsv(
			String nodeId,
			ArxiuCapsalera capsalera) throws SistemaExternException {
		throw new SistemaExternException("Mètode no suportat");
	}

	@Override
	public ArxiuDocument documentObtenirAmbCsv(
			String csv,
			boolean ambContingut,
			ArxiuCapsalera capsalera) throws SistemaExternException {
		throw new SistemaExternException("Mètode no suportat");
	}

	@Override
	public List<ArxiuDocumentVersio> documentObtenirVersions(
			String nodeId,
			ArxiuCapsalera capsalera) throws SistemaExternException {
		throw new SistemaExternException("Mètode no suportat");
	}

	@Override
	public ArxiuCarpeta carpetaCrear(
			String nom,
			String pareNodeId,
			ArxiuCapsalera capsalera) throws SistemaExternException {
		throw new SistemaExternException("Mètode no suportat");
	}

	@Override
	public void carpetaModificar(
			String nodeId,
			String nom,
			ArxiuCapsalera capsalera) throws SistemaExternException {
		throw new SistemaExternException("Mètode no suportat");
	}

	@Override
	public void carpetaEsborrar(
			String nodeId,
			ArxiuCapsalera capsalera) throws SistemaExternException {
		throw new SistemaExternException("Mètode no suportat");
	}

	@Override
	public ArxiuCarpeta carpetaObtenirAmbId(
			String nodeId,
			ArxiuCapsalera capsalera) throws SistemaExternException {
		throw new SistemaExternException("Mètode no suportat");
	}



	private String saveWithNewId(
			InputStream contingut) throws Exception {
		String id = new Long(System.currentTimeMillis()).toString();
		File fContent = new File(getBaseDir() + "/" + id);
		while (fContent.exists()) {
			try {
				Thread.sleep(1);
			} catch (Exception ignored) {}
			id = new Long(System.currentTimeMillis()).toString();
			fContent = new File(getBaseDir() + "/" + id);
		}
		FileOutputStream outContent = new FileOutputStream(fContent);
		IOUtils.copy(contingut, outContent);
		outContent.close();
		return id;
	}

	private boolean updateWithId(
			String id,
			InputStream contingut) throws Exception {
		File fContent = new File(getBaseDir() + "/" + id);
		if (fContent.exists()) {
			FileOutputStream outContent = new FileOutputStream(fContent, false);
			IOUtils.copy(contingut, outContent);
			outContent.close();
			return true;
		} else {
			return false;
		}
	}

	private boolean deleteWithId(
			String id) throws Exception {
		File fContent = new File(getBaseDir() + "/" + id);
		if (fContent.exists()) {
			fContent.delete();
			return true;
		} else {
			return false;
		}
	}

	private ArxiuDocument readWithId(
			String id) throws Exception {
		File fContent = new File(getBaseDir() + "/" + id);
		if (fContent.exists()) {
			FileInputStream inContent = new FileInputStream(fContent);
			byte fileContent[] = new byte[(int)fContent.length()];
			inContent.read(fileContent);
			inContent.close();
			ArxiuDocument arxiuDocument = new ArxiuDocument();
			arxiuDocument.setNodeId(id);
			ArxiuDocumentContingut contingut = new ArxiuDocumentContingut();
			contingut.setContingut(fileContent);
			arxiuDocument.setContingut(contingut);
			return arxiuDocument;
		} else {
			return null;
		}
	}

	private String getBaseDir() {
		return PropertiesHelper.getProperties().getProperty(
				"es.caib.ripea.plugin.arxiu.filesystem.base.dir");
	}

}
