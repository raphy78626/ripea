/**
 * 
 */
package es.caib.ripea.plugin.caib.arxiu;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.commons.io.IOUtils;

import es.caib.arxiudigital.apirest.ApiArchivoDigital;
import es.caib.arxiudigital.apirest.CSGD.entidades.comunes.DocumentNode;
import es.caib.arxiudigital.apirest.CSGD.entidades.comunes.RespuestaGenerica;
import es.caib.arxiudigital.apirest.CSGD.entidades.resultados.CreateDraftDocumentResult;
import es.caib.arxiudigital.apirest.constantes.Aspectos;
import es.caib.arxiudigital.apirest.constantes.EstadosElaboracion;
import es.caib.arxiudigital.apirest.constantes.OrigenesContenido;
import es.caib.arxiudigital.apirest.constantes.TiposDocumentosENI;
import es.caib.arxiudigital.apirest.facade.pojos.CabeceraPeticion;
import es.caib.arxiudigital.apirest.facade.pojos.Documento;
import es.caib.arxiudigital.apirest.facade.pojos.Expediente;
import es.caib.arxiudigital.apirest.facade.pojos.FirmaDocumento;
import es.caib.arxiudigital.apirest.facade.resultados.Resultado;
import es.caib.arxiudigital.apirest.facade.resultados.ResultadoSimple;
import es.caib.arxiudigital.apirest.utils.MetadataUtils;
import es.caib.arxiudigital.apirest.utils.UtilidadesFechas;

/**
 * Test de l'arxiu digital.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public class ArxiuDigitalApiTest {

	public static final String BASE_URL = "https://afirmades.caib.es:4430/esb";

	private static final String VERSION_SERVICIO = "1.0";
	private static final String APLICACION_CLIENTE = "RIPEA";
	private static final String NOMBRE_USUARIO_CONEXION = "app1";
	private static final String PASSWORD_USUARIO_CONEXION = "app1";
	private static final String ORGANIZACION = "CAIB";

	private static final String NOMBRE_SOLICITANTE =  "Limit Tecnologies";
	private static final String DOCUMENTO_SOLICITANTE = "123456789Z";
	private static final String NOMBRE_USUARIO = "u101334";
	private static final String NOMBRE_PROCEDIMIENTO = "Proves amb RIPEA";

	private static final String SERIE_DOCUMENTAL = "S0001";
	//private static final String CODIGO_PROCEDIMIENTO = "organo1_PRO_123456789";
	private static final String ORGANO = "A04013511";
	private static final String ENCODING = "UTF-8";
	
	//private static final String EXPEDIENT_ID = "9cf1dc75-470a-4151-b3fc-94a1489d6f1e";

	public static void main(String[] args) {
		ArxiuDigitalApiTest apiTest = new ArxiuDigitalApiTest();
		String expedientId = null;
		try {
			expedientId = apiTest.crearExpedientTest();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (expedientId != null) {
				try {
					apiTest.esborrarExpedientTest(expedientId);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}
		/*String documentId = null;
		try {
			documentId = apiTest.crearDocumentTest(EXPEDIENT_ID);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (documentId != null) {
				try {
					apiTest.esborrarDocumentTest(documentId);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}*/
	}

	private String crearExpedientTest() throws IOException {
		List<String> organs = new ArrayList<String>();
		organs.add(ORGANO);
		String nombreExpediente = "ExpedientTestRipea_" + System.currentTimeMillis();
		Map<String, Object> metadades = MetadataUtils.CrearListaMetadatosExpediente(
				APLICACION_CLIENTE,
				SERIE_DOCUMENTAL,
				null, //CODIGO_PROCEDIMIENTO,
				UtilidadesFechas.fechaActualEnISO8601(),
				organs,
		        OrigenesContenido.ADMINISTRACION);
		Expediente expedientNou = new Expediente();
		expedientNou.expedienteParaCrear(true);
		expedientNou.setName(nombreExpediente);
		expedientNou.setMetadataCollection(metadades);
		Resultado<Expediente> resultado = getApiArxiu().crearExpediente(
				expedientNou,
				true);
		System.out.println(">>> Expedient creat (" +
				"resultatCodi=" + resultado.getCodigoResultado() + ", " +
				"resultatMissatge=" + resultado.getMsjResultado() + ", " +
				((resultado.getElementoDevuelto() != null) ?
				"expedientNom=" + resultado.getElementoDevuelto().getName() + ", " +
				"expedientId=" + resultado.getElementoDevuelto().getId() + ")" : ""));
		Expediente expedientCreat = resultado.getElementoDevuelto();
		/*if (expedientCreat != null) {
			if (expedientCreat.getMetadataCollection() != null) {
				System.out.println(">>>    Metadades:");
				for (String metadada: expedientCreat.getMetadataCollection().keySet()) {
					System.out.println(">>>       " + metadada + ": " + expedientCreat.getMetadataCollection().get(metadada));
				}
			}
			if (expedientCreat.getAspects() != null) {
				System.out.println(">>>    Aspectes:");
				for (Aspectos aspect: expedientCreat.getAspects()) {
					System.out.println(">>>       " + aspect);
				}
			}
		}*/
		return (expedientCreat != null) ? expedientCreat.getId() : null;
	}
	private void esborrarExpedientTest(
			String expedientId) throws IOException {
		ResultadoSimple resultado = getApiArxiu().eliminarExpediente(expedientId);
		System.out.println(">>> Expedient esborrat (" +
				"resultatCodi=" + resultado.getCodigoResultado() + ", " +
				"resultatMissatge=" + resultado.getMsjResultado() + ")");
	}

	@SuppressWarnings("unused")
	private String crearDocumentTest(
			String expedientId) throws IOException {
		String documentNom = "DocumentTestRipea_" + System.currentTimeMillis();
		List<String> organs = new ArrayList<String>();
		organs.add(ORGANO);
		Map<String, Object> metadades = MetadataUtils.CrearListaMetadatosDocumentoMinima(
				APLICACION_CLIENTE,
				SERIE_DOCUMENTAL,
				TiposDocumentosENI.SOLICITUD,
				organs,
		        EstadosElaboracion.ORIGINAL,
				OrigenesContenido.ADMINISTRACION);
		String contingutBase64 = IOUtils.toString(
				new Base64InputStream(getDocumentContingutPerTest(), true),
				ENCODING);
		
		Documento documentNou = generarObjetoDocumento(
				null,
				documentNom,
				contingutBase64,
				ENCODING,
				"application/pdf",
				metadades,
				null);
		CreateDraftDocumentResult res = getApiArxiu().crearDraftDocument(
				expedientId,
				documentNou,
				false);
		RespuestaGenerica<DocumentNode> result = res.getCreateDraftDocumentResult();
		System.out.println(">>> Document creat (" +
				"resultatCodi=" + result.getResult().getCode() + ", " +
				"resultatMissatge=" + result.getResult().getDescription() + ", " +
				((result.getResParam() != null) ?
				"documentNom=" + result.getResParam().getName() + ", " +
				"documentId=" + result.getResParam().getId() + ")" : ""));
		DocumentNode documentCreat = result.getResParam();
		/*if (expedientCreat != null) {
			if (expedientCreat.getMetadataCollection() != null) {
				System.out.println(">>>    Metadades:");
				for (String metadada: expedientCreat.getMetadataCollection().keySet()) {
					System.out.println(">>>       " + metadada + ": " + expedientCreat.getMetadataCollection().get(metadada));
				}
			}
			if (expedientCreat.getAspects() != null) {
				System.out.println(">>>    Aspectes:");
				for (Aspectos aspect: expedientCreat.getAspects()) {
					System.out.println(">>>       " + aspect);
				}
			}
		}*/
		return (documentCreat != null) ? documentCreat.getId() : null;
	}

	@SuppressWarnings("unused")
	private void esborrarDocumentTest(String documentId) throws IOException {
		ResultadoSimple resultado = getApiArxiu().eliminarDocumento(documentId);
		System.out.println(">>> Document esborrat (" +
				"resultatCodi=" + resultado.getCodigoResultado() + ", " +
				"resultatMissatge=" + resultado.getMsjResultado() + ")");
	}

	private Documento generarObjetoDocumento(
		String id,
		String name,
		String content,
		String encoding,
		String mimetype,
		Map<String, Object> metadataCollection,
		FirmaDocumento firma) {
		Documento doc = new Documento();
		List<Aspectos> aspects = new ArrayList<Aspectos>();
		aspects.add(Aspectos.INTEROPERABLE);
		aspects.add(Aspectos.TRANSFERIBLE);
		doc.setId(id);
		doc.setName(name);
		doc.setAspects(aspects);
		doc.setMetadataCollection(metadataCollection);
		doc.setContent(content);
		doc.setEncoding(encoding);
		doc.setMimetype(mimetype);
		if (firma != null) {
		  doc.setListaFirmas(Arrays.asList(firma));
		}
		return doc;
	}

	private ApiArchivoDigital getApiArxiu() {
		CabeceraPeticion cabecera = new CabeceraPeticion();
		cabecera.setServiceVersion(VERSION_SERVICIO);
		cabecera.setCodiAplicacion(APLICACION_CLIENTE);
		cabecera.setUsuarioSeguridad(NOMBRE_USUARIO_CONEXION);
		cabecera.setPasswordSeguridad(PASSWORD_USUARIO_CONEXION);
		cabecera.setOrganizacion(ORGANIZACION);
		// info login
		cabecera.setNombreSolicitante(NOMBRE_SOLICITANTE);
		cabecera.setDocumentoSolicitante(DOCUMENTO_SOLICITANTE);
		cabecera.setNombreUsuario(NOMBRE_USUARIO);
		// info peticio
		cabecera.setNombreProcedimiento(NOMBRE_PROCEDIMIENTO);
		return new ApiArchivoDigital(
				BASE_URL,
				cabecera);
	}

	private InputStream getDocumentContingutPerTest() {
		return getClass().getResourceAsStream(
        		"/es/caib/ripea/plugin/caib/document_test.pdf");
	}

}
