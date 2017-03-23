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
import es.caib.ripea.plugin.caib.custodia.ClienteCustodia;
import es.caib.ripea.plugin.caib.custodia.ClienteCustodia.CustodiaResponse;
import es.caib.ripea.plugin.utils.PropertiesHelper;

/**
 * Implementació del plugin d'arxiu per a emmagatzemar els arxius
 * a dins una carpeta del servidor i per a emmagatzemar els documents
 * firmats a dins l'aplicació de custòdia de la CAIB (ValCert).
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public class ArxiuPluginFilesystemCustodia implements ArxiuPlugin {

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
	public ArxiuExpedient expedientConsultar(
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
	public ArxiuDocument documentConsultar(
			String nodeId,
			String versio,
			boolean ambContingut,
			ArxiuCapsalera capsalera) throws SistemaExternException {
		if (versio != null) {
			throw new SistemaExternException("Versionat de documents no suportat");
		}
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
	public List<ArxiuDocumentVersio> documentObtenirVersions(
			String nodeId,
			ArxiuCapsalera capsalera) throws SistemaExternException {
		throw new SistemaExternException("Mètode no suportat");
	}

	@Override
	public String documentGenerarCsv(
			String nodeId,
			ArxiuCapsalera capsalera,
			String valcertDocumentId) throws SistemaExternException {
		String errorDescripcio = "No s'ha pogut reservar la URL (" +
				"nodeId=" + nodeId + ", " +
				"valcertDocumentId=" + valcertDocumentId + ")";
		try {
			CustodiaResponse response = getClienteCustodia().reservarDocumento(
					valcertDocumentId);
			detectarErrorCustodiaResponse(
					response,
					errorDescripcio,
					null);
			return response.getBytesAsString();
		} catch (SistemaExternException sex) {
			throw sex;
		} catch (Exception ex) {
			throw new SistemaExternException(
					errorDescripcio,
					ex);
		}
	}

	@Override
	public void documentDefinitiuGuardarPdfFirmat(
			String nodeId,
			InputStream firmaContingut,
			String csv,
			ArxiuCapsalera capsalera,
			String valcertArxiuNom,
			String valcertDocumentId,
			String valcertDocumentTipus) throws SistemaExternException {
		String errorDescripcio = "No s'ha pogut custodiar el PDF firmat (" +
				"valcertDocumentId=" + valcertDocumentId + ", " +
				"valcertDocumentTipus=" + valcertDocumentTipus + ", " +
				"valcertArxiuNom=" + valcertArxiuNom + ")";
		if (valcertDocumentTipus == null || valcertDocumentTipus.isEmpty()) {
			throw new SistemaExternException(
					errorDescripcio + ": no s'ha especificat el tipus de document");
		}
		try {
			CustodiaResponse response = getClienteCustodia().custodiarPDFFirmado(
					firmaContingut,
					valcertArxiuNom,
					valcertDocumentId,
					valcertDocumentTipus);
			detectarErrorCustodiaResponse(
					response,
					errorDescripcio,
					null);
		} catch (SistemaExternException sex) {
			throw sex;
		} catch (Exception ex) {
			throw new SistemaExternException(
					errorDescripcio,
					ex);
		}
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
	public ArxiuCarpeta carpetaConsultar(
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

	private ClienteCustodia clienteCustodia;
	private ClienteCustodia getClienteCustodia() {
		if (clienteCustodia == null) {
			clienteCustodia = new ClienteCustodia(
					getServiceUrl(),
					getUsername(),
					getPassword());
		}
		return clienteCustodia;
	}

	private void detectarErrorCustodiaResponse(
			CustodiaResponse response,
			String errorDescripcio,
			String[] resulMinorIgnorats) throws SistemaExternException {
		if (response.isError()) {
			boolean ignorar = false;
			if (resulMinorIgnorats != null) {
				for (String rmi: resulMinorIgnorats) {
					if (rmi.equals(response.getResultMinor())) {
						ignorar = true;
						break;
					}
				}
			}
			if (!ignorar) {
				throw new SistemaExternException(
						errorDescripcio + ": " + response.errorToString());
			}
		}
	}

	private String getServiceUrl() {
		return PropertiesHelper.getProperties().getProperty(
				"es.caib.ripea.plugin.arxiu.custodia.service.url");
	}
	private String getUsername() {
		return PropertiesHelper.getProperties().getProperty(
				"es.caib.ripea.plugin.arxiu.custodia.username");
	}
	private String getPassword() {
		return PropertiesHelper.getProperties().getProperty(
				"es.caib.ripea.plugin.arxiu.custodia.password");
	}

}
