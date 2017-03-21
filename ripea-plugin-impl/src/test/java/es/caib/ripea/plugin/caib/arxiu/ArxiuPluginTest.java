/**
 * 
 */
package es.caib.ripea.plugin.caib.arxiu;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import es.caib.ripea.plugin.SistemaExternException;
import es.caib.ripea.plugin.SistemaExternNoTrobatException;
import es.caib.ripea.plugin.arxiu.ArxiuCapsalera;
import es.caib.ripea.plugin.arxiu.ArxiuCarpeta;
import es.caib.ripea.plugin.arxiu.ArxiuDocument;
import es.caib.ripea.plugin.arxiu.ArxiuDocumentVersio;
import es.caib.ripea.plugin.arxiu.ArxiuEstatElaboracio;
import es.caib.ripea.plugin.arxiu.ArxiuExpedient;
import es.caib.ripea.plugin.arxiu.ArxiuExpedientEstat;
import es.caib.ripea.plugin.arxiu.ArxiuFormatExtensio;
import es.caib.ripea.plugin.arxiu.ArxiuFormatNom;
import es.caib.ripea.plugin.arxiu.ArxiuOrigenContingut;
import es.caib.ripea.plugin.arxiu.ArxiuPlugin;
import es.caib.ripea.plugin.arxiu.ArxiuTipusDocumental;

/**
 * Classe de proves per al plugin d'integració amb l'arxiu digital.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public class ArxiuPluginTest {

	private ArxiuPlugin plugin;

	private ArxiuCapsalera capsaleraTest;
	private List<String> organsTest;
	private List<String> interessatsTest;


	@Before
	public void setUp() throws Exception {
		System.setProperty(
				"es.caib.ripea.plugin.arxiu.caib.base.url",
				"https://afirmades.caib.es:4430/esb");
		System.setProperty(
				"es.caib.ripea.plugin.arxiu.caib.aplicacio.codi",
				"RIPEA");
		System.setProperty(
				"es.caib.ripea.plugin.arxiu.caib.usuari",
				"app1");
		System.setProperty(
				"es.caib.ripea.plugin.arxiu.caib.contrasenya",
				"app1");
		plugin = new ArxiuPluginImpl();
		capsaleraTest = new ArxiuCapsalera();
		capsaleraTest.setInteressatNom("Limit Tecnologies");
		capsaleraTest.setInteressatNif("123456789Z");
		capsaleraTest.setFuncionariNom("u101334");
		capsaleraTest.setFuncionariOrgan("CAIB");
		capsaleraTest.setProcedimentNom("Proves amb RIPEA");
		organsTest = new ArrayList<String>();
		organsTest.add("A04013511");
		interessatsTest = new ArrayList<String>();
		interessatsTest.add("12345678Z");
		interessatsTest.add("00000000T");
	}

	//@Test
	public void cicleCreacioExpedient() throws Exception {
		System.out.println("TEST: GESTIO EXPEDIENTS");
		String titol = "RIPEA_prova_exp_" + System.currentTimeMillis();
		ArxiuOrigenContingut origen = ArxiuOrigenContingut.ADMINISTRACIO;
		Date dataObertura = new Date();
		String classificacio = "organo1_PRO_123456789";
		ArxiuExpedientEstat estat = ArxiuExpedientEstat.OBERT;
		String serieDocumental = "S0001";
		String creatNodeId = null;
		try {
			System.out.print("1.- Crear expedient... ");
			ArxiuExpedient expedientCreat = plugin.expedientCrear(
					titol,
					origen,
					dataObertura,
					classificacio,
					estat,
					organsTest,
					interessatsTest,
					serieDocumental,
					capsaleraTest);
			assertNotNull(expedientCreat);
			creatNodeId = expedientCreat.getNodeId();
			System.out.println("Ok");
			System.out.print("2.- Comprovar dades expedient creat (nodeId=" + creatNodeId + ")... ");
			comprovarExpedient(
					expedientCreat,
					titol,
					origen,
					dataObertura,
					classificacio,
					estat,
					organsTest,
					interessatsTest,
					serieDocumental);
			System.out.println("Ok");
			System.out.print("3.- Obtenint l'expedient creat (nodeId=" + creatNodeId + ")... ");
			ArxiuExpedient expedientTrobat1 = plugin.expedientConsultar(
					expedientCreat.getNodeId(),
					capsaleraTest);
			assertNotNull(expedientTrobat1);
			System.out.println("Ok");
			System.out.print("4.- Comprovar dades expedient obtingut (nodeId=" + creatNodeId + ")... ");
			comprovarExpedient(
					expedientTrobat1,
					titol,
					origen,
					dataObertura,
					classificacio,
					estat,
					organsTest,
					interessatsTest,
					serieDocumental);
			System.out.println("Ok");
			System.out.print("5.- Esborrant expedient creat (nodeId=" + creatNodeId + ")... ");
			plugin.expedientEsborrar(
					expedientCreat.getNodeId(),
					capsaleraTest);
			creatNodeId = null;
			System.out.println("Ok");
			System.out.print("6.- Obtenint expedient esborrat per verificar que no existeix (nodeId=" + expedientCreat.getNodeId() + ")... ");
			try {
				plugin.expedientConsultar(
						expedientCreat.getNodeId(),
						capsaleraTest);
				fail("No s'hauria d'haver trobat l'expedient una vegada esborrat (nodeId=" + expedientCreat.getNodeId() + ")");
			} catch (SistemaExternNoTrobatException ex) {
				System.out.println("Ok");
			}
		} catch (Exception ex) {
			System.out.println("Error: " + ex.getLocalizedMessage());
			throw ex;
		} finally {
			if (creatNodeId != null) {
				System.out.print("  - Esborrant expedient degut a errors (nodeId=" + creatNodeId + ")... ");
				try {
					plugin.documentEsborrar(creatNodeId, capsaleraTest);
					System.out.println("Ok");
				} catch (Exception ex) {
					System.out.println("Error: " + ex.getLocalizedMessage());
				}
			}
		}
	}

	//@Test
	public void cicleCreacioDocument() throws Exception {
		System.out.println("TEST: GESTIO DOCUMENTS");
		String titolExp = "RIPEA_prova_exp_" + System.currentTimeMillis();
		String titolDoc = "RIPEA_prova_doc_" + System.currentTimeMillis();
		ArxiuOrigenContingut origen = ArxiuOrigenContingut.ADMINISTRACIO;
		Date dataCaptura = new Date();
		Date dataObertura = new Date();
		String classificacio = "organo1_PRO_123456789";
		ArxiuExpedientEstat estat = ArxiuExpedientEstat.OBERT;
		ArxiuEstatElaboracio estatElaboracio = ArxiuEstatElaboracio.ORIGINAL;
		ArxiuTipusDocumental tipusDocumental1 = ArxiuTipusDocumental.ALTRES;
		ArxiuTipusDocumental tipusDocumental2 = ArxiuTipusDocumental.RESOLUCIO;
		ArxiuFormatNom formatNom = ArxiuFormatNom.OASIS12;
		ArxiuFormatExtensio formatExtensio = ArxiuFormatExtensio.ODT;
		String serieDocumental = "S0001";
		String expCreatNodeId = null;
		String docCreatNodeId = null;
		try {
			System.out.print("1.- Crear expedient... ");
			ArxiuExpedient expedientCreat = plugin.expedientCrear(
					titolExp,
					origen,
					dataObertura,
					classificacio,
					estat,
					organsTest,
					interessatsTest,
					serieDocumental,
					capsaleraTest);
			assertNotNull(expedientCreat);
			expCreatNodeId = expedientCreat.getNodeId();
			System.out.println("Ok");
			System.out.print("2.- Crear document... ");
			ArxiuDocument documentCreat = plugin.documentEsborranyCrear(
					titolDoc,
					origen,
					dataCaptura,
					estatElaboracio,
					tipusDocumental1,
					formatNom,
					formatExtensio,
					organsTest,
					serieDocumental,
					getDocumentContingutPerTest(),
					"application/vnd.oasis.opendocument.text",
					expCreatNodeId,
					capsaleraTest);
			assertNotNull(documentCreat);
			docCreatNodeId = documentCreat.getNodeId();
			System.out.println("Ok");
			System.out.print("3.- Modificant document (nodeId=" + docCreatNodeId + ")... ");
			plugin.documentEsborranyModificar(
					docCreatNodeId,
					titolDoc,
					origen,
					dataCaptura,
					estatElaboracio,
					tipusDocumental2,
					formatNom,
					formatExtensio,
					organsTest,
					serieDocumental,
					getDocumentContingutPerTest(),
					"application/vnd.oasis.opendocument.text",
					capsaleraTest);
			System.out.println("Ok");
			System.out.print("4.- Comprovant les versions del document (nodeId=" + docCreatNodeId + ")... ");
			List<ArxiuDocumentVersio> versions = plugin.documentObtenirVersions(
					docCreatNodeId,
					capsaleraTest);
			assertEquals(2, versions.size());
			System.out.println("Ok");
			System.out.print("5.- Obtenint darrera versió del document (nodeId=" + docCreatNodeId + ")... ");
			ArxiuDocument documentTrobat1 = plugin.documentConsultar(
					docCreatNodeId,
					null,
					false,
					capsaleraTest);
			assertNotNull(documentTrobat1);
			System.out.println("Ok");
			System.out.print("6.- Comprovant dades del document obtingut (nodeId=" + docCreatNodeId + ")... ");
			comprovarDocument(
					documentTrobat1,
					titolDoc,
					origen,
					dataCaptura,
					estatElaboracio,
					tipusDocumental2,
					formatNom,
					formatExtensio,
					organsTest,
					null,
					serieDocumental);
			System.out.println("Ok");
			System.out.print("7.- Obtenint versió antiga del document (nodeId=" + docCreatNodeId + ")... ");
			ArxiuDocument documentTrobat2 = plugin.documentConsultar(
					versions.get(versions.size() - 1).getNodeId(),
					null,
					false,
					capsaleraTest);
			assertNotNull(documentTrobat2);
			System.out.println("Ok");
			System.out.print("8.- Comprovant dades del document obtingut (nodeId=" + docCreatNodeId + ")... ");
			comprovarDocument(
					documentTrobat2,
					titolDoc,
					origen,
					dataCaptura,
					estatElaboracio,
					tipusDocumental1,
					formatNom,
					formatExtensio,
					organsTest,
					null,
					serieDocumental);
			System.out.println("Ok");
			System.out.print("9.- Generant CSV pel document (nodeId=" + docCreatNodeId + ")... ");
			String csv1 = plugin.documentGenerarCsv(
					docCreatNodeId,
					capsaleraTest,
					null);
			plugin.documentConsultar(
					docCreatNodeId,
					null,
					false,
					capsaleraTest);
			assertNotNull(csv1);
			System.out.println("Ok (CSV=" + csv1 + ")");
			/*System.out.print("9a.- Tornant a generar CSV pel document (nodeId=" + docCreatNodeId + ")... ");
			String csv2 = plugin.documentGenerarCsv(
					docCreatNodeId,
					capsaleraTest);
			plugin.documentObtenirAmbId(
					docCreatNodeId,
					null,
					capsaleraTest);
			assertNotNull(csv2);
			System.out.println("Ok (CSV=" + csv2 + ")");
			System.out.print("9b.- comprovant que els CSV generats son iguals...");
			assertEquals(csv1, csv2);
			System.out.println("Ok");*/
			System.out.print("10.- Comprovant que no es pot crear document amb mateix nom (nodeId=" + docCreatNodeId + ")... ");
			try {
				plugin.documentEsborranyCrear(
						titolDoc,
						origen,
						dataCaptura,
						estatElaboracio,
						tipusDocumental1,
						formatNom,
						formatExtensio,
						organsTest,
						serieDocumental,
						getDocumentContingutPerTest(),
						"application/vnd.oasis.opendocument.text",
						expCreatNodeId,
						capsaleraTest);
				System.out.println("Error");
				fail("No s'hauria de poder crear un document amb el mateix nom");
			} catch (SistemaExternException ex) {
				System.out.println("Ok");
			}
			System.out.print("11.- Esborrant document creat (nodeId=" + docCreatNodeId + ")... ");
			plugin.documentEsborrar(
					documentCreat.getNodeId(),
					capsaleraTest);
			docCreatNodeId = null;
			System.out.println("Ok");
			System.out.print("12.- Obtenint document esborrat per verificar que no existeix (nodeId=" + documentCreat.getNodeId() + ")... ");
			try {
				plugin.documentConsultar(
						documentCreat.getNodeId(),
						null,
						false,
						capsaleraTest);
				System.out.println("Error");
				fail("No s'hauria d'haver trobat el document una vegada esborrat (nodeId=" + documentCreat.getNodeId() + ")");
			} catch (SistemaExternNoTrobatException ex) {
				System.out.println("Ok");
			}
			System.out.print("13.- Esborrant expedient creat (nodeId=" + expCreatNodeId + ")... ");
			plugin.expedientEsborrar(
					expedientCreat.getNodeId(),
					capsaleraTest);
			expCreatNodeId = null;
			System.out.println("Ok");
		} catch (Exception ex) {
			System.out.println("Error: " + ex.getLocalizedMessage());
			throw ex;
		} finally {
			if (docCreatNodeId != null) {
				System.out.print("  - Esborrant document degut a errors (nodeId=" + docCreatNodeId + ")... ");
				try {
					plugin.documentEsborrar(docCreatNodeId, capsaleraTest);
					System.out.println("Ok");
				} catch (Exception ex) {
					System.out.println("Error: " + ex.getLocalizedMessage());
				}
			}
			if (expCreatNodeId != null) {
				System.out.print("  - Esborrant expedient degut a errors (nodeId=" + expCreatNodeId + ")... ");
				try {
					plugin.expedientEsborrar(expCreatNodeId, capsaleraTest);
					System.out.println("Ok");
				} catch (Exception ex) {
					System.out.println("Error: " + ex.getLocalizedMessage());
				}
			}
		}
	}

	@Test
	public void documentFirmat() throws Exception {
		System.out.println("TEST: GESTIO DOCUMENTS FIRMATS");
		String titolExp = "RIPEA_prova_exp_" + System.currentTimeMillis();
		String titolDoc = "RIPEA_prova_doc_" + System.currentTimeMillis();
		ArxiuOrigenContingut origen = ArxiuOrigenContingut.ADMINISTRACIO;
		Date dataCaptura = new Date();
		Date dataObertura = new Date();
		String classificacio = "organo1_PRO_123456789";
		ArxiuExpedientEstat estat = ArxiuExpedientEstat.OBERT;
		ArxiuEstatElaboracio estatElaboracio = ArxiuEstatElaboracio.ORIGINAL;
		ArxiuTipusDocumental tipusDocumental1 = ArxiuTipusDocumental.ALTRES;
		ArxiuFormatNom formatNom = ArxiuFormatNom.OASIS12;
		ArxiuFormatExtensio formatExtensio = ArxiuFormatExtensio.ODT;
		String serieDocumental = "S0001";
		String expCreatNodeId = null;
		String docCreatNodeId = null;
		try {
			System.out.print("1.- Crear expedient... ");
			ArxiuExpedient expedientCreat = plugin.expedientCrear(
					titolExp,
					origen,
					dataObertura,
					classificacio,
					estat,
					organsTest,
					interessatsTest,
					serieDocumental,
					capsaleraTest);
			assertNotNull(expedientCreat);
			expCreatNodeId = expedientCreat.getNodeId();
			System.out.println("Ok");
			System.out.print("2.- Crear document... ");
			ArxiuDocument documentCreat = plugin.documentEsborranyCrear(
					titolDoc,
					origen,
					dataCaptura,
					estatElaboracio,
					tipusDocumental1,
					formatNom,
					formatExtensio,
					organsTest,
					serieDocumental,
					getDocumentContingutPerTest(),
					"application/vnd.oasis.opendocument.text",
					expCreatNodeId,
					capsaleraTest);
			assertNotNull(documentCreat);
			docCreatNodeId = documentCreat.getNodeId();
			System.out.println("Ok");
			System.out.print("3.- Generant CSV pel document (nodeId=" + docCreatNodeId + ")... ");
			String csv = plugin.documentGenerarCsv(
					docCreatNodeId,
					capsaleraTest,
					null);
			assertNotNull(csv);
			System.out.println("Ok (CSV=" + csv + ")");
			System.out.print("4.- Guardar firma PDF... ");
			plugin.documentDefinitiuGuardarPdfFirmat(
					documentCreat.getNodeId(),
					getPdfContingutPerTest(),
					csv,
					capsaleraTest,
					null,
					null,
					null);
			System.out.println("Ok");
			System.out.print("5.- Obtenint document firmat (nodeId=" + docCreatNodeId + ")... ");
			ArxiuDocument documentTrobat1 = plugin.documentConsultar(
					docCreatNodeId,
					null,
					true,
					capsaleraTest);
			assertNotNull(documentTrobat1);
			System.out.println("Ok");
			System.out.print("6.- Esborrant document creat (nodeId=" + docCreatNodeId + ")... ");
			try {
				plugin.documentEsborrar(
						documentCreat.getNodeId(),
						capsaleraTest);
				System.out.println("Error");
				fail("No s'hauria de poder esborrar un document firmat (nodeId=" + documentCreat.getNodeId() + ")");
			} catch (SistemaExternException ex) {
				System.out.println("Ok");
				docCreatNodeId = null;
			}
			System.out.print("7.- Esborrant expedient creat (nodeId=" + expCreatNodeId + ")... ");
			try {
				plugin.expedientEsborrar(
						expedientCreat.getNodeId(),
						capsaleraTest);
				System.out.println("Error");
				fail("No s'hauria de poder esborrar un expedient amb documents firmats (nodeId=" + expedientCreat.getNodeId() + ")");
			} catch (SistemaExternException ex) {
				System.out.println("Ok");
				expCreatNodeId = null;
			}
		} catch (Exception ex) {
			System.out.println("Error: " + ex.getLocalizedMessage());
			throw ex;
		} finally {
			if (docCreatNodeId != null) {
				System.out.print("  - Esborrant document degut a errors (nodeId=" + docCreatNodeId + ")... ");
				try {
					plugin.documentEsborrar(docCreatNodeId, capsaleraTest);
					System.out.println("Ok");
				} catch (Exception ex) {
					System.out.println("Error: " + ex.getLocalizedMessage());
				}
			}
			if (expCreatNodeId != null) {
				System.out.print("  - Esborrant expedient degut a errors (nodeId=" + expCreatNodeId + ")... ");
				try {
					plugin.expedientEsborrar(expCreatNodeId, capsaleraTest);
					System.out.println("Ok");
				} catch (Exception ex) {
					System.out.println("Error: " + ex.getLocalizedMessage());
				}
			}
		}
	}

	//@Test
	public void cicleCreacioCarpeta() throws Exception {
		System.out.println("TEST: GESTIO CARPETES");
		String titolExp = "RIPEA_prova_exp_" + System.currentTimeMillis();
		String titolCar = "RIPEA_prova_car_" + System.currentTimeMillis();
		ArxiuOrigenContingut origen = ArxiuOrigenContingut.ADMINISTRACIO;
		Date dataObertura = new Date();
		String classificacio = "organo1_PRO_123456789";
		ArxiuExpedientEstat estat = ArxiuExpedientEstat.OBERT;
		String serieDocumental = "S0001";
		String expCreatNodeId = null;
		String carCreadaNodeId = null;
		try {
			System.out.print("1.- Crear expedient... ");
			ArxiuExpedient expedientCreat = plugin.expedientCrear(
					titolExp,
					origen,
					dataObertura,
					classificacio,
					estat,
					organsTest,
					interessatsTest,
					serieDocumental,
					capsaleraTest);
			assertNotNull(expedientCreat);
			expCreatNodeId = expedientCreat.getNodeId();
			System.out.println("Ok");
			System.out.print("2.- Crear carpeta... ");
			ArxiuCarpeta carpetaCreada = plugin.carpetaCrear(
					titolCar,
					expCreatNodeId,
					capsaleraTest);
			assertNotNull(carpetaCreada);
			carCreadaNodeId = carpetaCreada.getNodeId();
			System.out.println("Ok");
			System.out.print("3.- Modificant carpeta (nodeId=" + carCreadaNodeId + ")... ");
			plugin.carpetaModificar(
					carCreadaNodeId,
					titolCar,
					capsaleraTest);
			System.out.println("Ok");
			System.out.print("4.- Obtenint la carpeta (nodeId=" + carCreadaNodeId + ")... ");
			ArxiuCarpeta carpetaTrobada = plugin.carpetaConsultar(
					carCreadaNodeId,
					capsaleraTest);
			assertNotNull(carpetaTrobada);
			System.out.println("Ok");
			System.out.print("5.- Esborrant carpeta creada (nodeId=" + carCreadaNodeId + ")... ");
			plugin.carpetaEsborrar(
					carCreadaNodeId,
					capsaleraTest);
			carCreadaNodeId = null;
			System.out.println("Ok");
			System.out.print("6.- Obtenint carpeta esborrada per verificar que no existeix (nodeId=" + carpetaCreada.getNodeId() + ")... ");
			try {
				plugin.carpetaConsultar(
						carpetaCreada.getNodeId(),
						capsaleraTest);
				fail("No s'hauria d'haver trobat la carpeta una vegada esborrada (nodeId=" + carpetaCreada.getNodeId() + ")");
			} catch (SistemaExternNoTrobatException ex) {
				System.out.println("Ok");
			}
			System.out.print("7.- Esborrant expedient creat (nodeId=" + expCreatNodeId + ")... ");
			plugin.expedientEsborrar(
					expedientCreat.getNodeId(),
					capsaleraTest);
			expCreatNodeId = null;
			System.out.println("Ok");
		} catch (Exception ex) {
			System.out.println("Error: " + ex.getLocalizedMessage());
			throw ex;
		} finally {
			if (carCreadaNodeId != null) {
				System.out.print("  - Esborrant carpeta degut a errors (nodeId=" + carCreadaNodeId + ")... ");
				try {
					plugin.carpetaEsborrar(carCreadaNodeId, capsaleraTest);
					System.out.println("Ok");
				} catch (Exception ex) {
					System.out.println("Error: " + ex.getLocalizedMessage());
				}
			}
			if (expCreatNodeId != null) {
				System.out.print("  - Esborrant expedient degut a errors (nodeId=" + expCreatNodeId + ")... ");
				try {
					plugin.expedientEsborrar(expCreatNodeId, capsaleraTest);
					System.out.println("Ok");
				} catch (Exception ex) {
					System.out.println("Error: " + ex.getLocalizedMessage());
				}
			}
		}
	}

	//@Test
	public void carpetaAmbContingutEsborrar() throws Exception {
		System.out.println("TEST: GESTIO CARPETES");
		String titolExp = "RIPEA_prova_exp_" + System.currentTimeMillis();
		String titolCar = "RIPEA_prova_car_" + System.currentTimeMillis();
		String titolDoc = "RIPEA_prova_doc_" + System.currentTimeMillis();
		ArxiuOrigenContingut origen = ArxiuOrigenContingut.ADMINISTRACIO;
		Date dataObertura = new Date();
		Date dataCaptura = new Date();
		String classificacio = "organo1_PRO_123456789";
		ArxiuExpedientEstat estat = ArxiuExpedientEstat.OBERT;
		ArxiuEstatElaboracio estatElaboracio = ArxiuEstatElaboracio.ORIGINAL;
		ArxiuTipusDocumental tipusDocumental1 = ArxiuTipusDocumental.ALTRES;
		ArxiuFormatNom formatNom = ArxiuFormatNom.OASIS12;
		ArxiuFormatExtensio formatExtensio = ArxiuFormatExtensio.ODT;
		String serieDocumental = "S0001";
		String expCreatNodeId = null;
		String carCreadaNodeId = null;
		String subcarCreadaNodeId = null;
		String docCreatNodeId = null;
		try {
			System.out.print("1.- Crear expedient... ");
			ArxiuExpedient expedientCreat = plugin.expedientCrear(
					titolExp,
					origen,
					dataObertura,
					classificacio,
					estat,
					organsTest,
					interessatsTest,
					serieDocumental,
					capsaleraTest);
			assertNotNull(expedientCreat);
			expCreatNodeId = expedientCreat.getNodeId();
			System.out.println("Ok");
			System.out.print("2.- Crear carpeta... ");
			ArxiuCarpeta carpetaCreada = plugin.carpetaCrear(
					titolCar,
					expCreatNodeId,
					capsaleraTest);
			assertNotNull(carpetaCreada);
			carCreadaNodeId = carpetaCreada.getNodeId();
			System.out.println("Ok");
			System.out.print("3.- Crear subcarpeta dins la carpeta... ");
			ArxiuCarpeta subcarpetaCreada = plugin.carpetaCrear(
					titolCar,
					carpetaCreada.getNodeId(),
					capsaleraTest);
			assertNotNull(subcarpetaCreada);
			subcarCreadaNodeId = subcarpetaCreada.getNodeId();
			System.out.println("Ok");
			System.out.print("4.- Crear document dins la subcarpeta... ");
			ArxiuDocument documentCreat = plugin.documentEsborranyCrear(
					titolDoc,
					origen,
					dataCaptura,
					estatElaboracio,
					tipusDocumental1,
					formatNom,
					formatExtensio,
					organsTest,
					serieDocumental,
					getDocumentContingutPerTest(),
					"application/vnd.oasis.opendocument.text",
					subcarCreadaNodeId,
					capsaleraTest);
			assertNotNull(documentCreat);
			docCreatNodeId = documentCreat.getNodeId();
			System.out.println("Ok");
			System.out.print("5.- Esborrar carpeta amb subcarpeta... ");
			plugin.carpetaEsborrar(
					carCreadaNodeId,
					capsaleraTest);
			carCreadaNodeId = null;
			System.out.println("Ok");
			System.out.print("6.- Obtenint la subcarpeta (nodeId=" + subcarCreadaNodeId + ")... ");
			try {
				plugin.carpetaConsultar(
						subcarCreadaNodeId,
						capsaleraTest);
				fail("No s'hauria d'haver trobat la subcarpeta una vegada esborrada la carpeta pare (nodeId=" + subcarCreadaNodeId + ")");
			} catch (SistemaExternNoTrobatException ex) {
				System.out.println("Ok");
				subcarCreadaNodeId = null;
			}
			System.out.print("6.- Obtenint el document (nodeId=" + docCreatNodeId + ")... ");
			try {
				plugin.documentConsultar(
						docCreatNodeId,
						null,
						false,
						capsaleraTest);
				fail("No s'hauria d'haver trobat el document una vegada esborrada la carpeta pare (nodeId=" + subcarCreadaNodeId + ")");
			} catch (SistemaExternNoTrobatException ex) {
				System.out.println("Ok");
				docCreatNodeId = null;
			}
			System.out.print("8.- Esborrant expedient creat (nodeId=" + expCreatNodeId + ")... ");
			plugin.expedientEsborrar(
					expedientCreat.getNodeId(),
					capsaleraTest);
			expCreatNodeId = null;
			System.out.println("Ok");
		} catch (Exception ex) {
			System.out.println("Error: " + ex.getLocalizedMessage());
			throw ex;
		} finally {
			if (docCreatNodeId != null) {
				System.out.print("  - Esborrant document degut a errors (nodeId=" + docCreatNodeId + ")... ");
				try {
					plugin.documentEsborrar(docCreatNodeId, capsaleraTest);
					System.out.println("Ok");
				} catch (Exception ex) {
					System.out.println("Error: " + ex.getLocalizedMessage());
				}
			}
			if (subcarCreadaNodeId != null) {
				System.out.print("  - Esborrant subcarpeta degut a errors (nodeId=" + subcarCreadaNodeId + ")... ");
				try {
					plugin.carpetaEsborrar(subcarCreadaNodeId, capsaleraTest);
					System.out.println("Ok");
				} catch (Exception ex) {
					System.out.println("Error: " + ex.getLocalizedMessage());
				}
			}
			if (carCreadaNodeId != null) {
				System.out.print("  - Esborrant carpeta degut a errors (nodeId=" + carCreadaNodeId + ")... ");
				try {
					plugin.carpetaEsborrar(carCreadaNodeId, capsaleraTest);
					System.out.println("Ok");
				} catch (Exception ex) {
					System.out.println("Error: " + ex.getLocalizedMessage());
				}
			}
			if (expCreatNodeId != null) {
				System.out.print("  - Esborrant expedient degut a errors (nodeId=" + expCreatNodeId + ")... ");
				try {
					plugin.expedientEsborrar(expCreatNodeId, capsaleraTest);
					System.out.println("Ok");
				} catch (Exception ex) {
					System.out.println("Error: " + ex.getLocalizedMessage());
				}
			}
		}
	}
	/*@Test
	public void arxiuTest() throws IOException {
		InputStream is = getClass().getResourceAsStream(
        		"/es/caib/ripea/plugin/caib/document_test.odt");
		byte[] bytes = IOUtils.toByteArray(is);
		String contingutBase64 = IOUtils.toString(
				new Base64InputStream(
						new ByteArrayInputStream(bytes),
						true),
				"UTF-8");
		System.out.println(">>> contingutBase64: " + contingutBase64);
	}*/



	private void comprovarExpedient(
			ArxiuExpedient expedient,
			String titol,
			ArxiuOrigenContingut origen,
			Date dataObertura,
			String classificacio,
			ArxiuExpedientEstat estat,
			List<String> organs,
			List<String> interessats,
			String serieDocumental) {
		assertEquals(titol, expedient.getTitol());
		assertEquals(origen, expedient.getEniOrigen());
		//assertEquals(dataObertura, expedient.getEniDataObertura());
		assertEquals(classificacio, expedient.getEniClassificacio());
		assertEquals(estat, expedient.getEniEstat());
		assertEquals(organs, expedient.getEniOrgans());
		assertEquals(interessats, expedient.getEniInteressats());
		assertEquals(serieDocumental, expedient.getSerieDocumental());
	}

	private void comprovarDocument(
			ArxiuDocument document,
			String titol,
			ArxiuOrigenContingut origen,
			Date dataCaptura,
			ArxiuEstatElaboracio estatElaboracio,
			ArxiuTipusDocumental tipusDocumental,
			ArxiuFormatNom formatNom,
			ArxiuFormatExtensio formatExtensio,
			List<String> organs,
			String eniDocumentOrigenId,
			String serieDocumental) {
		assertEquals(titol, document.getTitol());
		assertEquals(origen, document.getEniOrigen());
		//assertEquals(dataCaptura, expedient.getEniDataCaptura());
		assertEquals(estatElaboracio, document.getEniEstatElaboracio());
		assertEquals(tipusDocumental, document.getEniTipusDocumental());
		assertEquals(formatNom, document.getEniFormatNom());
		assertEquals(formatExtensio, document.getEniFormatExtensio());
		assertEquals(organs, document.getEniOrgans());
		assertEquals(eniDocumentOrigenId, document.getEniDocumentOrigenId());
		assertEquals(serieDocumental, document.getSerieDocumental());
	}

	private InputStream getDocumentContingutPerTest() {
		InputStream is = getClass().getResourceAsStream(
        		"/es/caib/ripea/plugin/caib/document_test.odt");
		return is;
	}

	private InputStream getPdfContingutPerTest() {
		InputStream is = getClass().getResourceAsStream(
        		"/es/caib/ripea/plugin/caib/firma_test.pdf");
		return is;
	}

}
