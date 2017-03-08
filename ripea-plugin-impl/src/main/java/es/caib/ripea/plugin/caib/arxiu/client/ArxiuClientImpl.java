/**
 * 
 */
package es.caib.ripea.plugin.caib.arxiu.client;

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

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;

import es.caib.arxiudigital.apirest.CSGD.entidades.comunes.Content;
import es.caib.arxiudigital.apirest.CSGD.entidades.comunes.DocumentId;
import es.caib.arxiudigital.apirest.CSGD.entidades.comunes.DocumentNode;
import es.caib.arxiudigital.apirest.CSGD.entidades.comunes.FileAuditInfo;
import es.caib.arxiudigital.apirest.CSGD.entidades.comunes.FileNode;
import es.caib.arxiudigital.apirest.CSGD.entidades.comunes.FolderNode;
import es.caib.arxiudigital.apirest.CSGD.entidades.comunes.Metadata;
import es.caib.arxiudigital.apirest.CSGD.entidades.comunes.PersonIdentAuditInfo;
import es.caib.arxiudigital.apirest.CSGD.entidades.comunes.ProceedingsAuditInfo;
import es.caib.arxiudigital.apirest.CSGD.entidades.comunes.PublicServantAuditInfo;
import es.caib.arxiudigital.apirest.CSGD.entidades.comunes.ServiceAuditInfo;
import es.caib.arxiudigital.apirest.CSGD.entidades.comunes.ServiceHeader;
import es.caib.arxiudigital.apirest.CSGD.entidades.comunes.ServiceSecurityInfo;
import es.caib.arxiudigital.apirest.CSGD.entidades.comunes.SummaryInfoNode;
import es.caib.arxiudigital.apirest.CSGD.entidades.comunes.VersionNode;
import es.caib.arxiudigital.apirest.CSGD.entidades.parametrosLlamada.ParamCreateDocument;
import es.caib.arxiudigital.apirest.CSGD.entidades.parametrosLlamada.ParamCreateDraftDocument;
import es.caib.arxiudigital.apirest.CSGD.entidades.parametrosLlamada.ParamCreateFile;
import es.caib.arxiudigital.apirest.CSGD.entidades.parametrosLlamada.ParamCreateFolder;
import es.caib.arxiudigital.apirest.CSGD.entidades.parametrosLlamada.ParamGetDocument;
import es.caib.arxiudigital.apirest.CSGD.entidades.parametrosLlamada.ParamNodeId;
import es.caib.arxiudigital.apirest.CSGD.entidades.parametrosLlamada.ParamSetDocument;
import es.caib.arxiudigital.apirest.CSGD.entidades.parametrosLlamada.ParamSetFile;
import es.caib.arxiudigital.apirest.CSGD.entidades.parametrosLlamada.ParamSetFolder;
import es.caib.arxiudigital.apirest.CSGD.entidades.resultados.CreateDocumentResult;
import es.caib.arxiudigital.apirest.CSGD.entidades.resultados.CreateDraftDocumentResult;
import es.caib.arxiudigital.apirest.CSGD.entidades.resultados.CreateFileResult;
import es.caib.arxiudigital.apirest.CSGD.entidades.resultados.CreateFolderResult;
import es.caib.arxiudigital.apirest.CSGD.entidades.resultados.ExceptionResult;
import es.caib.arxiudigital.apirest.CSGD.entidades.resultados.GenerateDocCSVResult;
import es.caib.arxiudigital.apirest.CSGD.entidades.resultados.GetDocVersionListResult;
import es.caib.arxiudigital.apirest.CSGD.entidades.resultados.GetDocumentResult;
import es.caib.arxiudigital.apirest.CSGD.entidades.resultados.GetFileResult;
import es.caib.arxiudigital.apirest.CSGD.entidades.resultados.GetFolderResult;
import es.caib.arxiudigital.apirest.CSGD.peticiones.CreateDocument;
import es.caib.arxiudigital.apirest.CSGD.peticiones.CreateDraftDocument;
import es.caib.arxiudigital.apirest.CSGD.peticiones.CreateFile;
import es.caib.arxiudigital.apirest.CSGD.peticiones.CreateFolder;
import es.caib.arxiudigital.apirest.CSGD.peticiones.GenerateDocCSV;
import es.caib.arxiudigital.apirest.CSGD.peticiones.GetDocVersionList;
import es.caib.arxiudigital.apirest.CSGD.peticiones.GetDocument;
import es.caib.arxiudigital.apirest.CSGD.peticiones.GetFile;
import es.caib.arxiudigital.apirest.CSGD.peticiones.GetFolder;
import es.caib.arxiudigital.apirest.CSGD.peticiones.RemoveDocument;
import es.caib.arxiudigital.apirest.CSGD.peticiones.RemoveFile;
import es.caib.arxiudigital.apirest.CSGD.peticiones.RemoveFolder;
import es.caib.arxiudigital.apirest.CSGD.peticiones.Request;
import es.caib.arxiudigital.apirest.CSGD.peticiones.SetDocument;
import es.caib.arxiudigital.apirest.CSGD.peticiones.SetFile;
import es.caib.arxiudigital.apirest.CSGD.peticiones.SetFolder;
import es.caib.arxiudigital.apirest.constantes.Aspectos;
import es.caib.arxiudigital.apirest.constantes.EstadosElaboracion;
import es.caib.arxiudigital.apirest.constantes.EstadosExpediente;
import es.caib.arxiudigital.apirest.constantes.ExtensionesFichero;
import es.caib.arxiudigital.apirest.constantes.FormatosFichero;
import es.caib.arxiudigital.apirest.constantes.MetadatosDocumento;
import es.caib.arxiudigital.apirest.constantes.MetadatosExpediente;
import es.caib.arxiudigital.apirest.constantes.MetadatosFirma;
import es.caib.arxiudigital.apirest.constantes.OrigenesContenido;
import es.caib.arxiudigital.apirest.constantes.TiposDocumentosENI;
import es.caib.arxiudigital.apirest.facade.pojos.Documento;
import es.caib.arxiudigital.apirest.facade.pojos.Expediente;
import es.caib.arxiudigital.apirest.facade.pojos.FirmaDocumento;
import es.caib.arxiudigital.apirest.facade.pojos.Nodo;
import es.caib.arxiudigital.apirest.utils.MetadataUtils;

/**
 * Interfície del client per a accedir a la funcionalitat de
 * l'arxiu digital.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public class ArxiuClientImpl implements ArxiuClient {

	private String url;
	private String aplicacioCodi;
	private String serveiVersio = "1.0";
	private String usuariSgd;
	private String contrasenyaSgd;

	private Client jerseyClient;
	private ObjectMapper mapper;



	public ArxiuClientImpl(
			String url,
			String aplicacioCodi,
			String serveiVersio,
			String usuariHttp,
			String contrasenyaHttp,
			String usuariSgd,
			String contrasenyaSgd) {
		super();
		this.url = url;
		this.aplicacioCodi = aplicacioCodi;
		this.usuariSgd = usuariSgd;
		this.contrasenyaSgd = contrasenyaSgd;
		jerseyClient = new Client();
		if (usuariHttp != null) {
			jerseyClient.addFilter(new HTTPBasicAuthFilter(usuariHttp, contrasenyaHttp));
		}
		mapper = new ObjectMapper();
		// Permite recibir un solo objeto donde debía haber una lista.
		// Lo transforma a una lista con un objeto
		mapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
		// Feature that determines standard deserialization mechanism used for Enum values: if enabled, Enums are assumed to have been 
		// serialized using return value of Enum.toString();
		mapper.enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING);
		// To suppress serializing properties with null values
		mapper.setSerializationInclusion(Include.NON_NULL);
	}

	public ArxiuClientImpl(
			String url,
			String aplicacioCodi,
			String serveiVersio,
			String usuariSgd,
			String contrasenyaSgd) {
		this(	url,
				aplicacioCodi,
				serveiVersio,
				null,
				null,
				usuariSgd,
				contrasenyaSgd);
	}

	@Override
	public ArxiuFile fileCreate(
			String titol,
			OrigenesContenido origen,
			Date dataObertura,
			String classificacio,
			EstadosExpediente estat,
			List<String> organs,
			List<String> interessats,
			String serieDocumental,
			ArxiuHeader capsalera) throws ArxiuException {
		String metode = ArxiuMetodes.FILE_CREATE;
		logger.debug("Invocant mètode " + metode + " amb paràmetres (" +
				"titol=" + titol + ", " +
				"origen=" + origen + ", " +
				"dataObertura=" + dataObertura + ", " +
				"classificacio=" + classificacio + ", " +
				"estat=" + estat + ", " +
				"organs=" + organs + ", " +
				"interessats=" + interessats + ", " +
				"serieDocumental=" + serieDocumental + ", " +
				"capsalera=" + capsalera + ")");
		try {
			Map<String, Object> metadades = crearMetadadesExpedient(
					origen,
					dataObertura,
					classificacio,
					estat,
					organs,
					interessats,
					serieDocumental);
			CreateFile createFile = new CreateFile();
			Request<ParamCreateFile> request = new Request<ParamCreateFile>();
			request.setServiceHeader(generarServiceHeader(capsalera));
			ParamCreateFile param = new ParamCreateFile();
			param.setRetrieveNode(new Boolean(true).toString());
			Expediente expedient = new Expediente();
			expedient.expedienteParaCrear(true);
			expedient.setName(titol);
			expedient.setMetadataCollection(metadades);
			param.setFile(toNodeFile(expedient));
			request.setParam(param);
			createFile.setCreateFileRequest(request);
			JerseyResponse resposta = enviarPeticioRest(
					metode,
					createFile);
			logger.debug("Rebuda resposta mètode " + metode + " (" +
					"status=" + resposta.getStatus() + ", " +
					"contingut=" + resposta.getJson() + ")");
			if (resposta.getStatus() == 200) {
				CreateFileResult result = mapper.readValue(
						resposta.getJson(),
						CreateFileResult.class);
				return toArxiuFile(
						result.getCreateFileResult().getResParam(),
						resposta.getJson());
			} else {
				throw generarExcepcioJson(
						metode,
						resposta);
			}
		} catch (ArxiuException aex) {
			throw aex;
		} catch (Exception ex) {
			throw new ArxiuException(
					metode,
					"S'ha produit un error cridant el mètode " + metode + ": " + ex.getMessage(),
					ex);
		}
	}

	@Override
	public void fileUpdate(
			String nodeId,
			String titol,
			OrigenesContenido origen,
			Date dataObertura,
			String classificacio,
			EstadosExpediente estat,
			List<String> organs,
			List<String> interessats,
			String serieDocumental,
			ArxiuHeader capsalera) {
		String metode = ArxiuMetodes.FILE_SET;
		logger.debug("Invocant mètode " + metode + " amb paràmetres (" +
				"nodeId=" + nodeId + ", " +
				"titol=" + titol + ", " +
				"origen=" + origen + ", " +
				"dataObertura=" + dataObertura + ", " +
				"classificacio=" + classificacio + ", " +
				"estat=" + estat + ", " +
				"organs=" + organs + ", " +
				"interessats=" + interessats + ", " +
				"serieDocumental=" + serieDocumental + ", " +
				"capsalera=" + capsalera + ")");
		try {
			Map<String, Object> metadades = crearMetadadesExpedient(
					origen,
					dataObertura,
					classificacio,
					estat,
					organs,
					interessats,
					serieDocumental);
			SetFile setFile = new SetFile();
			Request<ParamSetFile> request = new Request<ParamSetFile>();
			request.setServiceHeader(generarServiceHeader(capsalera));
			ParamSetFile param = new ParamSetFile();
			Expediente expedient = new Expediente();
			expedient.expedienteParaCrear(true);
			expedient.setName(titol);
			expedient.setMetadataCollection(metadades);
			param.setFile(toNodeFile(expedient));
			request.setParam(param);
			setFile.setSetFileRequest(request);
			JerseyResponse resposta = enviarPeticioRest(
					metode,
					setFile);
			logger.debug("Rebuda resposta mètode " + metode + " (" +
					"status=" + resposta.getStatus() + ", " +
					"contingut=" + resposta.getJson() + ")");
			if (resposta.getStatus() != 200) {
				throw generarExcepcioJson(
						metode,
						resposta);
			}
		} catch (ArxiuException aex) {
			throw aex;
		} catch (Exception ex) {
			throw new ArxiuException(
					metode,
					"S'ha produit un error cridant el mètode " + metode,
					ex);
		}
	}

	@Override
	public void fileDelete(
			String nodeId,
			ArxiuHeader capsalera) {
		String metode = ArxiuMetodes.FILE_REMOVE;
		logger.debug("Invocant mètode " + metode + " amb paràmetres (" +
				"nodeId=" + nodeId + ", " +
				"capsalera=" + capsalera + ")");
		try {
			RemoveFile removeFile = new RemoveFile();
			Request<ParamNodeId> request = new Request<ParamNodeId>();
			request.setServiceHeader(generarServiceHeader(capsalera));
			ParamNodeId paramNodeId = new ParamNodeId();
			paramNodeId.setNodeId(nodeId);
			request.setParam(paramNodeId);
			removeFile.setRemoveFileRequest(request);
			JerseyResponse resposta = enviarPeticioRest(
					metode,
					removeFile);
			logger.debug("Rebuda resposta mètode " + metode + " (" +
					"status=" + resposta.getStatus() + ", " +
					"contingut=" + resposta.getJson() + ")");
			if (resposta.getStatus() != 200) {
				throw generarExcepcioJson(
						metode,
						resposta);
			}
		} catch (ArxiuException aex) {
			throw aex;
		} catch (Exception ex) {
			throw new ArxiuException(
					metode,
					"S'ha produit un error cridant el mètode " + metode,
					ex);
		}
	}

	@Override
	public ArxiuFile fileGet(
			String nodeId,
			ArxiuHeader capsalera) {
		String metode = ArxiuMetodes.FILE_GET;
		logger.debug("Invocant mètode " + metode + " amb paràmetres (" +
				"nodeId=" + nodeId + ", " +
				"capsalera=" + capsalera + ")");
		try {
			GetFile getFile = new GetFile();
			Request<ParamNodeId> request = new Request<ParamNodeId>();
			request.setServiceHeader(generarServiceHeader(capsalera));
			ParamNodeId paramNodeId = new ParamNodeId();
			paramNodeId.setNodeId(nodeId);
			request.setParam(paramNodeId);
			getFile.setGetFileRequest(request);
			JerseyResponse resposta = enviarPeticioRest(
					metode,
					getFile);
			logger.debug("Rebuda resposta mètode " + metode + " (" +
					"status=" + resposta.getStatus() + ", " +
					"contingut=" + resposta.getJson() + ")");
			if (resposta.getStatus() == 200) {
				GetFileResult result = mapper.readValue(
						resposta.getJson(),
						GetFileResult.class);
				return toArxiuFile(
						result.getGetFileResult().getResParam(),
						resposta.getJson());
			} else {
				throw generarExcepcioJson(
						metode,
						resposta);
			}
		} catch (ArxiuException aex) {
			throw aex;
		} catch (Exception ex) {
			throw new ArxiuException(
					metode,
					"S'ha produit un error cridant el mètode " + metode,
					ex);
		}
	}

	@Override
	public void fileClose() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void fileReopen() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void fileExport() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ArxiuDocument documentDraftCreate(
			String titol,
			OrigenesContenido origen,
			Date dataCaptura,
			EstadosElaboracion estatElaboracio,
			TiposDocumentosENI documentTipus,
			FormatosFichero formatNom,
			ExtensionesFichero formatExtensio,
			List<String> organs,
			String serieDocumental,
			InputStream contingut,
			String tipusMime,
			String pareNodeId,
			ArxiuHeader capsalera) {
		String metode = ArxiuMetodes.DOCUMENT_CREATE_DRAFT;
		logger.debug("Invocant mètode " + metode + " amb paràmetres (" +
				"titol=" + titol + ", " +
				"origen=" + origen + ", " +
				"dataCaptura=" + dataCaptura + ", " +
				"estatElaboracio=" + estatElaboracio + ", " +
				"documentTipus=" + documentTipus + ", " +
				"formatNom=" + formatNom + ", " +
				"formatExtensio=" + formatExtensio + ", " +
				"organs=" + organs + ", " +
				"serieDocumental=" + serieDocumental + ", " +
				"tipusMime=" + tipusMime + ", " +
				"pareNodeId=" + pareNodeId + ", " +
				"capsalera=" + capsalera + ")");
		try {
			Map<String, Object> metadades = crearMetadadesDocument(
					origen,
					dataCaptura,
					estatElaboracio,
					documentTipus,
					formatNom,
					formatExtensio,
					organs,
					serieDocumental,
					metode);
			CreateDraftDocument createDraftDocument = new CreateDraftDocument();
			Request<ParamCreateDraftDocument> request = new Request<ParamCreateDraftDocument>();
			request.setServiceHeader(generarServiceHeader(capsalera));
			ParamCreateDraftDocument param = new ParamCreateDraftDocument();
			param.setRetrieveNode(new Boolean(true).toString());
			param.setParent(pareNodeId);
			param.setDocument(toDocumentNode(
					null,
					titol,
					metadades,
					contingut,
					tipusMime));
			request.setParam(param);
			createDraftDocument.setCreateDraftDocumentRequest(request);
			JerseyResponse resposta = enviarPeticioRest(
					metode,
					createDraftDocument);
			logger.debug("Rebuda resposta mètode " + metode + " (" +
					"status=" + resposta.getStatus() + ", " +
					"contingut=" + resposta.getJson() + ")");
			if (resposta.getStatus() == 200) {
				CreateDraftDocumentResult result = mapper.readValue(
						resposta.getJson(),
						CreateDraftDocumentResult.class);
				return toArxiuDocument(
						result.getCreateDraftDocumentResult().getResParam(),
						resposta.getJson());
			} else {
				throw generarExcepcioJson(
						metode,
						resposta);
			}
		} catch (ArxiuException aex) {
			throw aex;
		} catch (Exception ex) {
			throw new ArxiuException(
					metode,
					"S'ha produit un error cridant el mètode " + metode,
					ex);
		}
	}

	@Override
	public ArxiuDocument documentCreate(
			String titol,
			OrigenesContenido origen,
			Date dataCaptura,
			EstadosElaboracion estatElaboracio,
			TiposDocumentosENI documentTipus,
			FormatosFichero formatNom,
			ExtensionesFichero formatExtensio,
			List<String> organs,
			String serieDocumental,
			InputStream contingut,
			String tipusMime,
			String pareNodeId,
			ArxiuHeader capsalera) {
		String metode = ArxiuMetodes.DOCUMENT_CREATE;
		logger.debug("Invocant mètode " + metode + " amb paràmetres (" +
				"titol=" + titol + ", " +
				"origen=" + origen + ", " +
				"dataCaptura=" + dataCaptura + ", " +
				"estatElaboracio=" + estatElaboracio + ", " +
				"documentTipus=" + documentTipus + ", " +
				"formatNom=" + formatNom + ", " +
				"formatExtensio=" + formatExtensio + ", " +
				"organs=" + organs + ", " +
				"serieDocumental=" + serieDocumental + ", " +
				"tipusMime=" + tipusMime + ", " +
				"pareNodeId=" + pareNodeId + ", " +
				"capsalera=" + capsalera + ")");
		try {
			Map<String, Object> metadades = crearMetadadesDocument(
					origen,
					dataCaptura,
					estatElaboracio,
					documentTipus,
					formatNom,
					formatExtensio,
					organs,
					serieDocumental,
					metode);
			CreateDocument createDocument = new CreateDocument();
			Request<ParamCreateDocument> request = new Request<ParamCreateDocument>();
			request.setServiceHeader(generarServiceHeader(capsalera));
			ParamCreateDocument param = new ParamCreateDocument();
			param.setRetrieveNode(new Boolean(true).toString());
			param.setParent(pareNodeId);
			param.setDocument(toDocumentNode(
					null,
					titol,
					metadades,
					contingut,
					tipusMime));
			request.setParam(param);
			createDocument.setCreateDocumentRequest(request);
			JerseyResponse resposta = enviarPeticioRest(
					metode,
					createDocument);
			logger.debug("Rebuda resposta mètode " + metode + " (" +
					"status=" + resposta.getStatus() + ", " +
					"contingut=" + resposta.getJson() + ")");
			if (resposta.getStatus() == 200) {
				CreateDocumentResult result = mapper.readValue(
						resposta.getJson(),
						CreateDocumentResult.class);
				return toArxiuDocument(
						result.getCreateDocumentResult().getResParam(),
						resposta.getJson());
			} else {
				throw generarExcepcioJson(
						metode,
						resposta);
			}
		} catch (ArxiuException aex) {
			throw aex;
		} catch (Exception ex) {
			throw new ArxiuException(
					metode,
					"S'ha produit un error cridant el mètode " + metode,
					ex);
		}
	}

	@Override
	public void documentUpdate(
			String nodeId,
			String titol,
			OrigenesContenido origen,
			Date dataCaptura,
			EstadosElaboracion estatElaboracio,
			TiposDocumentosENI documentTipus,
			FormatosFichero formatNom,
			ExtensionesFichero formatExtensio,
			List<String> organs,
			String serieDocumental,
			InputStream contingut,
			String tipusMime,
			ArxiuHeader capsalera) {
		String metode = ArxiuMetodes.DOCUMENT_SET;
		logger.debug("Invocant mètode " + metode + " amb paràmetres (" +
				"nodeId=" + nodeId + ", " +
				"titol=" + titol + ", " +
				"origen=" + origen + ", " +
				"dataCaptura=" + dataCaptura + ", " +
				"estatElaboracio=" + estatElaboracio + ", " +
				"documentTipus=" + documentTipus + ", " +
				"formatNom=" + formatNom + ", " +
				"formatExtensio=" + formatExtensio + ", " +
				"organs=" + organs + ", " +
				"serieDocumental=" + serieDocumental + ", " +
				"tipusMime=" + tipusMime + ", " +
				"capsalera=" + capsalera + ")");
		try {
			Map<String, Object> metadades = crearMetadadesDocument(
					origen,
					dataCaptura,
					estatElaboracio,
					documentTipus,
					formatNom,
					formatExtensio,
					organs,
					serieDocumental,
					metode);
			SetDocument setDocument = new SetDocument();
			Request<ParamSetDocument> request = new Request<ParamSetDocument>();
			request.setServiceHeader(generarServiceHeader(capsalera));
			ParamSetDocument param = new ParamSetDocument();
			param.setDocument(toDocumentNode(
					nodeId,
					titol,
					metadades,
					contingut,
					tipusMime));
			request.setParam(param);
			setDocument.setSetDocumentRequest(request);
			JerseyResponse resposta = enviarPeticioRest(
					metode,
					setDocument);
			logger.debug("Rebuda resposta mètode " + metode + " (" +
					"status=" + resposta.getStatus() + ", " +
					"contingut=" + resposta.getJson() + ")");
			if (resposta.getStatus() != 200) {
				throw generarExcepcioJson(
						metode,
						resposta);
			}
		} catch (ArxiuException aex) {
			throw aex;
		} catch (Exception ex) {
			throw new ArxiuException(
					metode,
					"S'ha produit un error cridant el mètode " + metode,
					ex);
		}
	}

	@Override
	public void documentDelete(
			String nodeId,
			ArxiuHeader capsalera) {
		String metode = ArxiuMetodes.DOCUMENT_REMOVE;
		logger.debug("Invocant mètode " + metode + " amb paràmetres (" +
				"nodeId=" + nodeId + ", " +
				"capsalera=" + capsalera + ")");
		try {
			RemoveDocument removeDocument = new RemoveDocument();
			Request<ParamNodeId> request = new Request<ParamNodeId>();
			request.setServiceHeader(generarServiceHeader(capsalera));
			ParamNodeId paramNodeId = new ParamNodeId();
			paramNodeId.setNodeId(nodeId);
			request.setParam(paramNodeId);
			removeDocument.setRemoveDocumentRequest(request);
			JerseyResponse resposta = enviarPeticioRest(
					metode,
					removeDocument);
			logger.debug("Rebuda resposta mètode " + metode + " (" +
					"status=" + resposta.getStatus() + ", " +
					"contingut=" + resposta.getJson() + ")");
			if (resposta.getStatus() != 200) {
				throw generarExcepcioJson(
						metode,
						resposta);
			}
		} catch (ArxiuException aex) {
			throw aex;
		} catch (Exception ex) {
			throw new ArxiuException(
					metode,
					"S'ha produit un error cridant el mètode " + metode,
					ex);
		}
	}

	@Override
	public ArxiuDocument documentGet(
			String nodeId,
			String csv,
			boolean ambContingut,
			ArxiuHeader capsalera) {
		String metode = ArxiuMetodes.DOCUMENT_GET;
		logger.debug("Invocant mètode " + metode + " amb paràmetres (" +
				"nodeId=" + nodeId + ", " +
				"capsalera=" + capsalera + ")");
		try {
			GetDocument getDocument = new GetDocument();
			Request<ParamGetDocument> request = new Request<ParamGetDocument>();
			request.setServiceHeader(generarServiceHeader(capsalera));
			ParamGetDocument param = new ParamGetDocument();
			DocumentId documentId = new DocumentId();
			documentId.setNodeId(nodeId);
			documentId.setCsv(csv);
			param.setDocumentId(documentId);
			param.setContent(new Boolean(ambContingut).toString());
			request.setParam(param);
			getDocument.setGetDocumentRequest(request);
			JerseyResponse resposta = enviarPeticioRest(
					metode,
					getDocument);
			logger.debug("Rebuda resposta mètode " + metode + " (" +
					"status=" + resposta.getStatus() + ", " +
					"contingut=" + resposta.getJson() + ")");
			if (resposta.getStatus() == 200) {
				GetDocumentResult result = mapper.readValue(
						resposta.getJson(),
						GetDocumentResult.class);
				return toArxiuDocument(
						result.getGetDocumentResult().getResParam(),
						resposta.getJson());
			} else {
				throw generarExcepcioJson(
						metode,
						resposta);
			}
		} catch (ArxiuException aex) {
			throw aex;
		} catch (Exception ex) {
			throw new ArxiuException(
					metode,
					"S'ha produit un error cridant el mètode " + metode,
					ex);
		}
	}

	@Override
	public List<ArxiuDocumentVersio> documentVersionList(
			String nodeId,
			ArxiuHeader capsalera) {
		String metode = ArxiuMetodes.DOCUMENT_GET_VERSION;
		logger.debug("Invocant mètode " + metode + " amb paràmetres (" +
				"nodeId=" + nodeId + ", " +
				"capsalera=" + capsalera + ")");
		try {
			GetDocVersionList getDocVersionList = new GetDocVersionList();
			Request<ParamNodeId> request = new Request<ParamNodeId>();
			request.setServiceHeader(generarServiceHeader(capsalera));
			ParamNodeId param = new ParamNodeId();
			param.setNodeId(nodeId);
			request.setParam(param);
			getDocVersionList.setGetDocVersionListRequest(request);
			JerseyResponse resposta = enviarPeticioRest(
					metode,
					getDocVersionList);
			logger.debug("Rebuda resposta mètode " + metode + " (" +
					"status=" + resposta.getStatus() + ", " +
					"contingut=" + resposta.getJson() + ")");
			if (resposta.getStatus() == 200) {
				GetDocVersionListResult result = mapper.readValue(
						resposta.getJson(),
						GetDocVersionListResult.class);
				List<ArxiuDocumentVersio> versions = new ArrayList<ArxiuDocumentVersio>();
				List<VersionNode> vns = result.getGetDocVersionListResult().getResParam();
				for (VersionNode vs: vns) {
					ArxiuDocumentVersio versio = new ArxiuDocumentVersio();
					versio.setId(vs.getId());
					versio.setData(getMetadataValueAsDate(vs.getDate()));
					versions.add(versio);
				}
				return versions;
			} else {
				throw generarExcepcioJson(
						metode,
						resposta);
			}
		} catch (ArxiuException aex) {
			throw aex;
		} catch (Exception ex) {
			throw new ArxiuException(
					metode,
					"S'ha produit un error cridant el mètode " + metode,
					ex);
		}
	}

	@Override
	public String documentCsvGenerate(
			String nodeId,
			ArxiuHeader capsalera) {
		String metode = ArxiuMetodes.DOCUMENT_GENERATE_CSV;
		logger.debug("Invocant mètode " + metode + " amb paràmetres (" +
				"nodeId=" + nodeId + ", " +
				"capsalera=" + capsalera + ")");
		try {
			GenerateDocCSV generateDocCSV = new GenerateDocCSV();
			Request<Object> request = new Request<Object>();
			request.setServiceHeader(generarServiceHeader(capsalera));
			generateDocCSV.setGenerateDocCSVRequest(request);
			JerseyResponse resposta = enviarPeticioRest(
					metode,
					generateDocCSV);
			logger.debug("Rebuda resposta mètode " + metode + " (" +
					"status=" + resposta.getStatus() + ", " +
					"contingut=" + resposta.getJson() + ")");
			if (resposta.getStatus() == 200) {
				GenerateDocCSVResult result = mapper.readValue(
						resposta.getJson(),
						GenerateDocCSVResult.class);
				return result.getGenerateDocCSVResult().getResParam();
			} else {
				throw generarExcepcioJson(
						metode,
						resposta);
			}
		} catch (ArxiuException aex) {
			throw aex;
		} catch (Exception ex) {
			throw new ArxiuException(
					metode,
					"S'ha produit un error cridant el mètode " + metode,
					ex);
		}
	}

	@Override
	public void documentEniGet(
			String nodeId,
			ArxiuHeader capsalera) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void documentSearch() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void documentCopy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void documentMove() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void documentLink() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void documentLock() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void documentUnlock() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void documentPermissionsGrant() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void documentPermissionsCancel() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void documentValidate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void documentDispatch() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void documentFinalSet() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ArxiuFolder folderCreate(
			String name,
			String pareNodeId,
			ArxiuHeader capsalera) {
		String metode = ArxiuMetodes.FOLDER_CREATE;
		logger.debug("Invocant mètode " + metode + " amb paràmetres (" +
				"name=" + name + ", " +
				"capsalera=" + capsalera + ")");
		try {
			CreateFolder createFolder = new CreateFolder();
			Request<ParamCreateFolder> request = new Request<ParamCreateFolder>();
			request.setServiceHeader(generarServiceHeader(capsalera));
			ParamCreateFolder param = new ParamCreateFolder();
			FolderNode folder = new FolderNode();
			folder.setName(name);
			param.setFolder(folder);
			param.setParent(pareNodeId);
			param.setRetrieveNode(Boolean.TRUE.toString());
			request.setParam(param);
			createFolder.setCreateFolderRequest(request);
			JerseyResponse resposta = enviarPeticioRest(
					metode,
					createFolder);
			logger.debug("Rebuda resposta mètode " + metode + " (" +
					"status=" + resposta.getStatus() + ", " +
					"contingut=" + resposta.getJson() + ")");
			if (resposta.getStatus() == 200) {
				CreateFolderResult result = mapper.readValue(
						resposta.getJson(),
						CreateFolderResult.class);
				return toArxiuFolder(
						result.getCreateFolderResult().getResParam(),
						resposta.getJson());
			} else {
				throw generarExcepcioJson(
						metode,
						resposta);
			}
		} catch (ArxiuException aex) {
			throw aex;
		} catch (Exception ex) {
			throw new ArxiuException(
					metode,
					"S'ha produit un error cridant el mètode " + metode,
					ex);
		}
	}

	@Override
	public void folderUpdate(
			String nodeId,
			String name,
			ArxiuHeader capsalera) {
		String metode = ArxiuMetodes.FOLDER_SET;
		logger.debug("Invocant mètode " + metode + " amb paràmetres (" +
				"nodeId=" + nodeId + ", " +
				"name=" + name + ", " +
				"capsalera=" + capsalera + ")");
		try {
			SetFolder setFolder = new SetFolder();
			Request<ParamSetFolder> request = new Request<ParamSetFolder>();
			request.setServiceHeader(generarServiceHeader(capsalera));
			ParamSetFolder param = new ParamSetFolder();
			FolderNode folder = new FolderNode();
			folder.setId(nodeId);
			folder.setName(name);
			param.setFolder(folder);
			request.setParam(param);
			setFolder.setSetFolderRequest(request);
			JerseyResponse resposta = enviarPeticioRest(
					metode,
					setFolder);
			logger.debug("Rebuda resposta mètode " + metode + " (" +
					"status=" + resposta.getStatus() + ", " +
					"contingut=" + resposta.getJson() + ")");
			if (resposta.getStatus() != 200) {
				throw generarExcepcioJson(
						metode,
						resposta);
			}
		} catch (ArxiuException aex) {
			throw aex;
		} catch (Exception ex) {
			throw new ArxiuException(
					metode,
					"S'ha produit un error cridant el mètode " + metode,
					ex);
		}
	}

	@Override
	public void folderDelete(
			String nodeId,
			ArxiuHeader capsalera) {
		String metode = ArxiuMetodes.FOLDER_REMOVE;
		logger.debug("Invocant mètode " + metode + " amb paràmetres (" +
				"nodeId=" + nodeId + ", " +
				"capsalera=" + capsalera + ")");
		try {
			RemoveFolder removeFolder = new RemoveFolder();
			Request<ParamNodeId> request = new Request<ParamNodeId>();
			request.setServiceHeader(generarServiceHeader(capsalera));
			ParamNodeId paramNodeId = new ParamNodeId();
			paramNodeId.setNodeId(nodeId);
			request.setParam(paramNodeId);
			removeFolder.setRemoveFolderRequest(request);
			JerseyResponse resposta = enviarPeticioRest(
					metode,
					removeFolder);
			logger.debug("Rebuda resposta mètode " + metode + " (" +
					"status=" + resposta.getStatus() + ", " +
					"contingut=" + resposta.getJson() + ")");
			if (resposta.getStatus() != 200) {
				throw generarExcepcioJson(
						metode,
						resposta);
			}
		} catch (ArxiuException aex) {
			throw aex;
		} catch (Exception ex) {
			throw new ArxiuException(
					metode,
					"S'ha produit un error cridant el mètode " + metode,
					ex);
		}
	}

	@Override
	public ArxiuFolder folderGet(
			String nodeId,
			ArxiuHeader capsalera) {
		String metode = ArxiuMetodes.FOLDER_GET;
		logger.debug("Invocant mètode " + metode + " amb paràmetres (" +
				"nodeId=" + nodeId + ", " +
				"capsalera=" + capsalera + ")");
		try {
			GetFolder getFolder = new GetFolder();
			Request<ParamNodeId> request = new Request<ParamNodeId>();
			request.setServiceHeader(generarServiceHeader(capsalera));
			ParamNodeId paramNodeId = new ParamNodeId();
			paramNodeId.setNodeId(nodeId);
			request.setParam(paramNodeId);
			getFolder.setGetFolderRequest(request);
			JerseyResponse resposta = enviarPeticioRest(
					metode,
					getFolder);
			logger.debug("Rebuda resposta mètode " + metode + " (" +
					"status=" + resposta.getStatus() + ", " +
					"contingut=" + resposta.getJson() + ")");
			if (resposta.getStatus() == 200) {
				GetFolderResult result = mapper.readValue(
						resposta.getJson(),
						GetFolderResult.class);
				return toArxiuFolder(
						result.getGetFolderResult().getResParam(),
						resposta.getJson());
			} else {
				throw generarExcepcioJson(
						metode,
						resposta);
			}
		} catch (ArxiuException aex) {
			throw aex;
		} catch (Exception ex) {
			throw new ArxiuException(
					metode,
					"S'ha produit un error cridant el mètode " + metode,
					ex);
		}
	}

	@Override
	public void folderMove() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void folderLink() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void folderLock() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void folderUnlock() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void folderPermissionsGrant() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void folderPermissionsCancel() {
		// TODO Auto-generated method stub
		
	}



	private JerseyResponse enviarPeticioRest(
			String metode,
			Object peticio) throws UniformInterfaceException, ClientHandlerException, JsonProcessingException {
		String urlAmbMetode = (url.endsWith("/")) ? url + "services/" + metode : url + "/services/" + metode;
		String body = mapper.writeValueAsString(peticio);
		logger.debug("Enviant petició POST a l'arxiu (" +
				"url=" + urlAmbMetode + ", " +
				"tipus=application/json, " +
				"body=" + body + ")");
		/*System.out.println(">>> Enviant petició POST a l'arxiu (" +
				"url=" + urlAmbMetode + ", " +
				"tipus=application/json, " +
				"body=" + body + ")");*/
		ClientResponse response = jerseyClient.
				resource(urlAmbMetode).
				type("application/json").
				post(ClientResponse.class, body);
		String json = response.getEntity(String.class);
		logger.debug("Rebuda resposta de l'arxiu (" +
				"status=" + response.getStatus() + ", " +
				"body=" + json + ")");
		/*System.out.println(">>> Rebuda resposta de l'arxiu (" +
				"status=" + response.getStatus() + ", " +
				"body=" + json + ")");*/
		return new JerseyResponse(
				json,
				response.getStatus());
	}

	private FileNode toNodeFile(Expediente expedient) {
		FileNode nodo = new FileNode();
		nodo.setId(expedient.getId());
		nodo.setType(expedient.getType());
		nodo.setName(expedient.getName());
		nodo.setMetadataCollection(
				generarMetadadesAmbMap(expedient.getMetadataCollection()));
		nodo.setAspects(expedient.getAspects());
		nodo.setChildObjects(
				convetertirListSummaryInfoNode(
						expedient.getChilds()));
		return nodo;
	}

	private List<Metadata> generarMetadadesAmbMap(Map<String, Object> metadadesMap){
		List<Metadata> metadades = null;
		if (metadadesMap != null) {
			metadades = new ArrayList<Metadata>();
			for (Map.Entry<String, Object> entry: metadadesMap.entrySet()) {
				Metadata metadata = new Metadata();
				metadata.setQname(entry.getKey());
				metadata.setValue(entry.getValue());
				metadades.add(metadata);
			}
		}
		return metadades;
	}

	private Map<String, Object> crearMetadadesExpedient(
			OrigenesContenido origen,
			Date dataObertura,
			String classificacio,
			EstadosExpediente estat,
			List<String> organs,
			List<String> interessats,
			String serieDocumental) {
		Map<String, Object> metadades  = new HashMap<String, Object>();
		metadades.put(
				MetadatosExpediente.CODIGO_APLICACION_TRAMITE,
				aplicacioCodi);
		metadades.put(
				MetadatosExpediente.CODIGO_CLASIFICACION,
				serieDocumental);
		metadades.put(
				MetadatosExpediente.IDENTIFICADOR_PROCEDIMIENTO,
				classificacio);
		if (origen != null) {
			metadades.put(
					MetadatosExpediente.ORIGEN,
					origen);
		}
		if (estat != null) {
			metadades.put(
					MetadatosExpediente.ESTADO_EXPEDIENTE,
					estat);
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
			OrigenesContenido origen,
			Date dataCaptura,
			EstadosElaboracion estatElaboracio,
			TiposDocumentosENI documentTipus,
			FormatosFichero formatNom,
			ExtensionesFichero formatExtensio,
			List<String> organs,
			String serieDocumental,
			String metode) {
		Map<String, Object> metadades  = new HashMap<String, Object>();
		metadades.put(
				MetadatosDocumento.CODIGO_APLICACION_TRAMITE,
				aplicacioCodi);
		metadades.put(
				MetadatosDocumento.CODIGO_CLASIFICACION,
				serieDocumental);
		metadades.put(
				MetadatosDocumento.ORIGEN,
				origen);
		metadades.put(
				MetadatosDocumento.ESTADO_ELABORACION,
				estatElaboracio);
		metadades.put(
				MetadatosDocumento.TIPO_DOC_ENI,
				documentTipus);
		if (formatNom != null) {
			metadades.put(
					MetadatosDocumento.NOMBRE_FORMATO,
					formatNom);
		}
		if (formatExtensio != null) {
			metadades.put(
					MetadatosDocumento.EXTENSION_FORMATO,
					formatExtensio);
		}
		if (organs != null) {
			metadades.put(
					MetadatosDocumento.ORGANO,
					organs);
		}
		if (dataCaptura != null){
			metadades.put(
					MetadatosDocumento.FECHA_INICIO,
					formatDateIso8601(dataCaptura));
		}
		return metadades;
	}

	private List<SummaryInfoNode> convetertirListSummaryInfoNode(List<Nodo> listaEntrada) {
		List<SummaryInfoNode> listaInfoNode = null;
		if (listaEntrada != null){
			listaInfoNode = new ArrayList<SummaryInfoNode>();
			for (Nodo nodo: listaEntrada){
				SummaryInfoNode obj = new SummaryInfoNode();
				obj.setId(nodo.getId());
				obj.setName(nodo.getName());
				obj.setType(nodo.getType());
				listaInfoNode.add(obj);
			}
		}
		return listaInfoNode;
	}

	public ServiceHeader generarServiceHeader(ArxiuHeader capsalera) {
		ServiceHeader serviceHeader = new ServiceHeader();
		ServiceAuditInfo auditInfo = new ServiceAuditInfo();
		PersonIdentAuditInfo interessat = new PersonIdentAuditInfo();
		interessat.setName(capsalera.getInteressatNom());
		interessat.setDocument(capsalera.getInteressatNif());
		auditInfo.setApplicant(interessat);
		PublicServantAuditInfo publicServant = new PublicServantAuditInfo();
		PersonIdentAuditInfo funcionari = new PersonIdentAuditInfo();
		funcionari.setName(capsalera.getFuncionariNom());
		funcionari.setDocument(capsalera.getFuncionariNif());
		publicServant.setIdentificationData(funcionari);
		publicServant.setOrganization(capsalera.getFuncionariOrgan());
		auditInfo.setPublicServant(publicServant);
		FileAuditInfo expedient = new FileAuditInfo();
		expedient.setId(capsalera.getExpedientId());
		ProceedingsAuditInfo procediment = new ProceedingsAuditInfo();
		procediment.setId(capsalera.getProcedimentId());
		procediment.setName(capsalera.getProcedimentNom());
		expedient.setProceedings(procediment);
		auditInfo.setFile(expedient);
		auditInfo.setApplication(aplicacioCodi);
		serviceHeader.setAuditInfo(auditInfo);
		serviceHeader.setServiceVersion(serveiVersio);
		ServiceSecurityInfo securityInfo = new ServiceSecurityInfo();
		securityInfo.setUser(usuariSgd);
		securityInfo.setPassword(contrasenyaSgd);
		serviceHeader.setSecurityInfo(securityInfo);
		return serviceHeader;
	}

	private static final String DOCUMENT_ENCODING = "UTF-8";
	private Documento crearDocumento(
			String id,
			String titol,
			Map<String, Object> metadades,
			InputStream contingut,
			String tipusMime) throws IOException {
		Documento documento = new Documento();
		documento.setId(id);
		documento.setName(titol);
		List<Aspectos> aspects = new ArrayList<Aspectos>();
		aspects.add(Aspectos.INTEROPERABLE);
		aspects.add(Aspectos.TRANSFERIBLE);
		documento.setAspects(aspects);
		documento.setMetadataCollection(metadades);
		if (contingut != null) {
			String contingutBase64 = new String(
					Base64.encodeBase64(IOUtils.toByteArray(contingut)));
			documento.setContent(contingutBase64);
			documento.setEncoding(DOCUMENT_ENCODING);
			documento.setMimetype(tipusMime);
		}
		return documento;
	}
	private DocumentNode toDocumentNode(
			String id,
			String titol,
			Map<String, Object> metadades,
			InputStream contingut,
			String tipusMime) throws IOException {
		Documento doc = crearDocumento(
				id,
				titol,
				metadades,
				contingut,
				tipusMime);
		DocumentNode nodo = new DocumentNode();
		Content contenido = new Content();
		Content contenidofirma = new Content();
		List<Content> listaContenidos = new ArrayList<Content>();
		FirmaDocumento firma = doc.getFirma();
		// Si no se envía el contenido del documento, no se añade el objeto CONTENT
		// del documento
		if (doc != null && doc.getContent() != null) {
			contenido.setBinaryType(doc.getBinarytype());
			contenido.setContent(doc.getContent());
			contenido.setEncoding(doc.getEncoding());
			contenido.setMimetype(doc.getMimetype());
			listaContenidos.add(contenido);
		}
		// Si no se envía el contenido del documento, no se añade el objeto CONTENT
		// del documento
		if (firma != null && firma.getContent() != null) {
			contenidofirma.setBinaryType(firma.getBinarytype());
			contenidofirma.setContent(firma.getContent());
			contenidofirma.setEncoding(firma.getEncoding());
			// contenido.setMimetype(firma.getMimetype());
			listaContenidos.add(contenidofirma);
			// Añadimos el Aspecto Firmado al documento
			doc.getAspects().add(Aspectos.FIRMADO);
			// Añadimos los metadatos sobre la firma al documento
			doc.getMetadataCollection().put(MetadatosFirma.PERFIL_FIRMA, firma.getPerfil_firma());
			doc.getMetadataCollection().put(MetadatosFirma.TIPO_FIRMA, firma.getTipoFirma());
		}
		nodo.setId(doc.getId());
		nodo.setName(doc.getName());
		nodo.setType(doc.getType());
		nodo.setMetadataCollection(MetadataUtils.generarListaMetadatos(doc.getMetadataCollection()));
		nodo.setAspects(doc.getAspects());
		nodo.setBinaryContents(listaContenidos);
		return nodo;
	}

	private ArxiuFile toArxiuFile(
			FileNode fileNode,
			String respostaJson) throws ParseException {
		ArxiuFile file = new ArxiuFile();
		file.setNodeId(fileNode.getId());
		file.setTitol(fileNode.getName());
		if (fileNode.getMetadataCollection() != null) {
			for (Metadata metaData: fileNode.getMetadataCollection()) {
				if ("eni:v_nti".equals(metaData.getQname())) {
					file.setEniVersio(getMetadataValueAsString(getMetadataValueAsString(metaData.getValue())));
				} else if (MetadatosExpediente.DESCRIPCION.equals(metaData.getQname())) {
					file.setDescripcio(getMetadataValueAsString(metaData.getValue()));
				} else if ("eni:id".equals(metaData.getQname())) {
					file.setEniIdentificador(getMetadataValueAsString(getMetadataValueAsString(metaData.getValue())));
				} else if (MetadatosExpediente.ORIGEN.equals(metaData.getQname())) {
					if (OrigenesContenido.CIUDADANO.getValue().equals(getMetadataValueAsString(metaData.getValue()))) {
						file.setEniOrigen(OrigenesContenido.CIUDADANO);
					} else if (OrigenesContenido.ADMINISTRACION.getValue().equals(getMetadataValueAsString(metaData.getValue()))) {
						file.setEniOrigen(OrigenesContenido.ADMINISTRACION);
					}
				} else if (MetadatosExpediente.ORGANO.equals(metaData.getQname())) {
					file.setEniOrgans(getMetadataValueAsListString(metaData.getValue()));
				} else if (MetadatosExpediente.FECHA_INICIO.equals(metaData.getQname())) {
					file.setEniDataObertura(getMetadataValueAsDate(metaData.getValue()));
				} else if (MetadatosExpediente.IDENTIFICADOR_PROCEDIMIENTO.equals(metaData.getQname())) {
					file.setEniClassificacio(getMetadataValueAsString(metaData.getValue()));
				} else if (MetadatosExpediente.ESTADO_EXPEDIENTE.equals(metaData.getQname())) {
					if (EstadosExpediente.ABIERTO.getValue().equals(getMetadataValueAsString(metaData.getValue()))) {
						file.setEniEstat(EstadosExpediente.ABIERTO);
					} else if (EstadosExpediente.CERRADO.getValue().equals(getMetadataValueAsString(metaData.getValue()))) {
						file.setEniEstat(EstadosExpediente.CERRADO);
					} else if (EstadosExpediente.INDICE_REMISION.getValue().equals(getMetadataValueAsString(metaData.getValue()))) {
						file.setEniEstat(EstadosExpediente.INDICE_REMISION);
					}
				} else if (MetadatosExpediente.INTERESADOS.equals(metaData.getQname())) {
					file.setEniInteressats(getMetadataValueAsListString(metaData.getValue()));
				} else if ("eni:tipoFirma".equals(metaData.getQname())) {
					file.setEniFirmaTipus(getMetadataValueAsListString(metaData.getValue()));
				} else if ("eni:csv".equals(metaData.getQname())) {
					file.setEniFirmaCsv(getMetadataValueAsListString(metaData.getValue()));
				} else if ("eni:def_csv".equals(metaData.getQname())) {
					file.setEniFirmaCsvDefinicio(getMetadataValueAsListString(metaData.getValue()));
				} else if (MetadatosExpediente.CODIGO_CLASIFICACION.equals(metaData.getQname())) {
					file.setSerieDocumental(getMetadataValueAsString(metaData.getValue()));
				} else if (MetadatosExpediente.CODIGO_APLICACION_TRAMITE.equals(metaData.getQname())) {
					file.setAplicacioCreacio(getMetadataValueAsString(metaData.getValue()));
				} else if ("eni:soporte".equals(metaData.getQname())) {
					file.setSuport(getMetadataValueAsString(metaData.getValue()));
				}
			}
		}
		file.setJson(extreureResParamJson(respostaJson));
		return file;
	}

	private ArxiuDocument toArxiuDocument(
			DocumentNode documentNode,
			String respostaJson) throws ParseException {
		ArxiuDocument document = new ArxiuDocument();
		document.setNodeId(documentNode.getId());
		document.setTitol(documentNode.getName());
		document.setContinguts(documentNode.getBinaryContents());
		if (documentNode.getMetadataCollection() != null) {
			for (Metadata metaData: documentNode.getMetadataCollection()) {
				if ("eni:v_nti".equals(metaData.getQname())) {
					document.setEniVersio(getMetadataValueAsString(getMetadataValueAsString(metaData.getValue())));
				} else if (MetadatosDocumento.DESCRIPCION_DOCUMENTO.equals(metaData.getQname())) {
					document.setDescripcio(getMetadataValueAsString(metaData.getValue()));
				} else if (MetadatosDocumento.ID_ENI.equals(metaData.getQname())) {
					document.setEniIdentificador(getMetadataValueAsString(getMetadataValueAsString(metaData.getValue())));
				} else if (MetadatosDocumento.ORIGEN.equals(metaData.getQname())) {
					if (OrigenesContenido.CIUDADANO.getValue().equals(getMetadataValueAsString(metaData.getValue()))) {
						document.setEniOrigen(OrigenesContenido.CIUDADANO);
					} else if (OrigenesContenido.ADMINISTRACION.getValue().equals(getMetadataValueAsString(metaData.getValue()))) {
						document.setEniOrigen(OrigenesContenido.ADMINISTRACION);
					}
				} else if (MetadatosDocumento.ESTADO_ELABORACION.equals(metaData.getQname())) {
					if (EstadosElaboracion.ORIGINAL.getValue().equals(getMetadataValueAsString(metaData.getValue()))) {
						document.setEniEstatElaboracio(EstadosElaboracion.ORIGINAL);
					} else if (EstadosElaboracion.COPIA_AUTENTICA_PAPEL.getValue().equals(getMetadataValueAsString(metaData.getValue()))) {
						document.setEniEstatElaboracio(EstadosElaboracion.COPIA_AUTENTICA_PAPEL);
					} else if (EstadosElaboracion.COPIA_AUTENTICA_FORMATO.getValue().equals(getMetadataValueAsString(metaData.getValue()))) {
						document.setEniEstatElaboracio(EstadosElaboracion.COPIA_AUTENTICA_FORMATO);
					} else if (EstadosElaboracion.COPIA_AUTENTICA_PARCIAL.getValue().equals(getMetadataValueAsString(metaData.getValue()))) {
						document.setEniEstatElaboracio(EstadosElaboracion.COPIA_AUTENTICA_PARCIAL);
					} else if (EstadosElaboracion.OTROS.getValue().equals(getMetadataValueAsString(metaData.getValue()))) {
						document.setEniEstatElaboracio(EstadosElaboracion.OTROS);
					}
				} else if (MetadatosDocumento.TIPO_DOC_ENI.equals(metaData.getQname())) {
					if (TiposDocumentosENI.RESOLUCION.getValue().equals(getMetadataValueAsString(metaData.getValue()))) {
						document.setEniTipusDocumental(TiposDocumentosENI.RESOLUCION);
					} else if (TiposDocumentosENI.ACUERDO.getValue().equals(getMetadataValueAsString(metaData.getValue()))) {
						document.setEniTipusDocumental(TiposDocumentosENI.ACUERDO);
					} else if (TiposDocumentosENI.CONTRATO.getValue().equals(getMetadataValueAsString(metaData.getValue()))) {
						document.setEniTipusDocumental(TiposDocumentosENI.CONTRATO);
					} else if (TiposDocumentosENI.CONVENIO.getValue().equals(getMetadataValueAsString(metaData.getValue()))) {
						document.setEniTipusDocumental(TiposDocumentosENI.CONVENIO);
					} else if (TiposDocumentosENI.NOTIFICACION.getValue().equals(getMetadataValueAsString(metaData.getValue()))) {
						document.setEniTipusDocumental(TiposDocumentosENI.NOTIFICACION);
					} else if (TiposDocumentosENI.SOLICITUD.getValue().equals(getMetadataValueAsString(metaData.getValue()))) {
						document.setEniTipusDocumental(TiposDocumentosENI.SOLICITUD);
					} else if (TiposDocumentosENI.OTROS.getValue().equals(getMetadataValueAsString(metaData.getValue()))) {
						document.setEniTipusDocumental(TiposDocumentosENI.OTROS);
					}
				} else if (MetadatosDocumento.NOMBRE_FORMATO.equals(metaData.getQname())) {
					if (FormatosFichero.GML.getValue().equals(getMetadataValueAsString(metaData.getValue()))) {
						document.setEniFormatNom(FormatosFichero.GML);
					} else if (FormatosFichero.WFS.getValue().equals(getMetadataValueAsString(metaData.getValue()))) {
						document.setEniFormatNom(FormatosFichero.WFS);
					} else if (FormatosFichero.WMS.getValue().equals(getMetadataValueAsString(metaData.getValue()))) {
						document.setEniFormatNom(FormatosFichero.WMS);
					} else if (FormatosFichero.GZIP.getValue().equals(getMetadataValueAsString(metaData.getValue()))) {
						document.setEniFormatNom(FormatosFichero.GZIP);
					} else if (FormatosFichero.ZIP.getValue().equals(getMetadataValueAsString(metaData.getValue()))) {
						document.setEniFormatNom(FormatosFichero.ZIP);
					} else if (FormatosFichero.AVI.getValue().equals(getMetadataValueAsString(metaData.getValue()))) {
						document.setEniFormatNom(FormatosFichero.AVI);
					} else if (FormatosFichero.MP4A.getValue().equals(getMetadataValueAsString(metaData.getValue()))) {
						document.setEniFormatNom(FormatosFichero.MP4A);
					} else if (FormatosFichero.CSV.getValue().equals(getMetadataValueAsString(metaData.getValue()))) {
						document.setEniFormatNom(FormatosFichero.CSV);
					} else if (FormatosFichero.HTML.getValue().equals(getMetadataValueAsString(metaData.getValue()))) {
						document.setEniFormatNom(FormatosFichero.HTML);
					} else if (FormatosFichero.CSS.getValue().equals(getMetadataValueAsString(metaData.getValue()))) {
						document.setEniFormatNom(FormatosFichero.CSS);
					} else if (FormatosFichero.JPEG.getValue().equals(getMetadataValueAsString(metaData.getValue()))) {
						document.setEniFormatNom(FormatosFichero.JPEG);
					} else if (FormatosFichero.MHTML.getValue().equals(getMetadataValueAsString(metaData.getValue()))) {
						document.setEniFormatNom(FormatosFichero.MHTML);
					} else if (FormatosFichero.OASIS12.getValue().equals(getMetadataValueAsString(metaData.getValue()))) {
						document.setEniFormatNom(FormatosFichero.OASIS12);
					} else if (FormatosFichero.SOXML.getValue().equals(getMetadataValueAsString(metaData.getValue()))) {
						document.setEniFormatNom(FormatosFichero.SOXML);
					} else if (FormatosFichero.PDF.getValue().equals(getMetadataValueAsString(metaData.getValue()))) {
						document.setEniFormatNom(FormatosFichero.PDF);
					} else if (FormatosFichero.PDFA.getValue().equals(getMetadataValueAsString(metaData.getValue()))) {
						document.setEniFormatNom(FormatosFichero.PDFA);
					} else if (FormatosFichero.PNG.getValue().equals(getMetadataValueAsString(metaData.getValue()))) {
						document.setEniFormatNom(FormatosFichero.PNG);
					} else if (FormatosFichero.RTF.getValue().equals(getMetadataValueAsString(metaData.getValue()))) {
						document.setEniFormatNom(FormatosFichero.RTF);
					} else if (FormatosFichero.SVG.getValue().equals(getMetadataValueAsString(metaData.getValue()))) {
						document.setEniFormatNom(FormatosFichero.SVG);
					} else if (FormatosFichero.TIFF.getValue().equals(getMetadataValueAsString(metaData.getValue()))) {
						document.setEniFormatNom(FormatosFichero.TIFF);
					} else if (FormatosFichero.TXT.getValue().equals(getMetadataValueAsString(metaData.getValue()))) {
						document.setEniFormatNom(FormatosFichero.TXT);
					} else if (FormatosFichero.XHTML.getValue().equals(getMetadataValueAsString(metaData.getValue()))) {
						document.setEniFormatNom(FormatosFichero.XHTML);
					} else if (FormatosFichero.MP3.getValue().equals(getMetadataValueAsString(metaData.getValue()))) {
						document.setEniFormatNom(FormatosFichero.MP3);
					} else if (FormatosFichero.OGG.getValue().equals(getMetadataValueAsString(metaData.getValue()))) {
						document.setEniFormatNom(FormatosFichero.OGG);
					} else if (FormatosFichero.MP4V.getValue().equals(getMetadataValueAsString(metaData.getValue()))) {
						document.setEniFormatNom(FormatosFichero.MP4V);
					} else if (FormatosFichero.WEBM.getValue().equals(getMetadataValueAsString(metaData.getValue()))) {
						document.setEniFormatNom(FormatosFichero.WEBM);
					}
				} else if (MetadatosDocumento.EXTENSION_FORMATO.equals(metaData.getQname())) {
					if (ExtensionesFichero.GML.getValue().equals(getMetadataValueAsString(metaData.getValue()))) {
						document.setEniFormatExtensio(ExtensionesFichero.GML);
					} else if (ExtensionesFichero.GZ.getValue().equals(getMetadataValueAsString(metaData.getValue()))) {
						document.setEniFormatExtensio(ExtensionesFichero.GZ);
					} else if (ExtensionesFichero.ZIP.getValue().equals(getMetadataValueAsString(metaData.getValue()))) {
						document.setEniFormatExtensio(ExtensionesFichero.ZIP);
					} else if (ExtensionesFichero.AVI.getValue().equals(getMetadataValueAsString(metaData.getValue()))) {
						document.setEniFormatExtensio(ExtensionesFichero.AVI);
					} else if (ExtensionesFichero.CSV.getValue().equals(getMetadataValueAsString(metaData.getValue()))) {
						document.setEniFormatExtensio(ExtensionesFichero.CSV);
					} else if (ExtensionesFichero.HTML.getValue().equals(getMetadataValueAsString(metaData.getValue()))) {
						document.setEniFormatExtensio(ExtensionesFichero.HTML);
					} else if (ExtensionesFichero.HTM.getValue().equals(getMetadataValueAsString(metaData.getValue()))) {
						document.setEniFormatExtensio(ExtensionesFichero.HTM);
					} else if (ExtensionesFichero.CSS.getValue().equals(getMetadataValueAsString(metaData.getValue()))) {
						document.setEniFormatExtensio(ExtensionesFichero.CSS);
					} else if (ExtensionesFichero.JPG.getValue().equals(getMetadataValueAsString(metaData.getValue()))) {
						document.setEniFormatExtensio(ExtensionesFichero.JPG);
					} else if (ExtensionesFichero.JPEG.getValue().equals(getMetadataValueAsString(metaData.getValue()))) {
						document.setEniFormatExtensio(ExtensionesFichero.JPEG);
					} else if (ExtensionesFichero.MHTML.getValue().equals(getMetadataValueAsString(metaData.getValue()))) {
						document.setEniFormatExtensio(ExtensionesFichero.MHTML);
					} else if (ExtensionesFichero.MHT.getValue().equals(getMetadataValueAsString(metaData.getValue()))) {
						document.setEniFormatExtensio(ExtensionesFichero.MHT);
					} else if (ExtensionesFichero.ODT.getValue().equals(getMetadataValueAsString(metaData.getValue()))) {
						document.setEniFormatExtensio(ExtensionesFichero.ODT);
					} else if (ExtensionesFichero.ODS.getValue().equals(getMetadataValueAsString(metaData.getValue()))) {
						document.setEniFormatExtensio(ExtensionesFichero.ODS);
					} else if (ExtensionesFichero.ODP.getValue().equals(getMetadataValueAsString(metaData.getValue()))) {
						document.setEniFormatExtensio(ExtensionesFichero.ODP);
					} else if (ExtensionesFichero.ODG.getValue().equals(getMetadataValueAsString(metaData.getValue()))) {
						document.setEniFormatExtensio(ExtensionesFichero.ODG);
					} else if (ExtensionesFichero.DOCX.getValue().equals(getMetadataValueAsString(metaData.getValue()))) {
						document.setEniFormatExtensio(ExtensionesFichero.DOCX);
					} else if (ExtensionesFichero.XLSX.getValue().equals(getMetadataValueAsString(metaData.getValue()))) {
						document.setEniFormatExtensio(ExtensionesFichero.XLSX);
					} else if (ExtensionesFichero.PPTX.getValue().equals(getMetadataValueAsString(metaData.getValue()))) {
						document.setEniFormatExtensio(ExtensionesFichero.PPTX);
					} else if (ExtensionesFichero.PDF.getValue().equals(getMetadataValueAsString(metaData.getValue()))) {
						document.setEniFormatExtensio(ExtensionesFichero.PDF);
					} else if (ExtensionesFichero.PNG.getValue().equals(getMetadataValueAsString(metaData.getValue()))) {
						document.setEniFormatExtensio(ExtensionesFichero.PNG);
					} else if (ExtensionesFichero.RTF.getValue().equals(getMetadataValueAsString(metaData.getValue()))) {
						document.setEniFormatExtensio(ExtensionesFichero.RTF);
					} else if (ExtensionesFichero.SVG.getValue().equals(getMetadataValueAsString(metaData.getValue()))) {
						document.setEniFormatExtensio(ExtensionesFichero.SVG);
					} else if (ExtensionesFichero.TIFF.getValue().equals(getMetadataValueAsString(metaData.getValue()))) {
						document.setEniFormatExtensio(ExtensionesFichero.TIFF);
					} else if (ExtensionesFichero.TXT.getValue().equals(getMetadataValueAsString(metaData.getValue()))) {
						document.setEniFormatExtensio(ExtensionesFichero.TXT);
					} else if (ExtensionesFichero.MP3.getValue().equals(getMetadataValueAsString(metaData.getValue()))) {
						document.setEniFormatExtensio(ExtensionesFichero.MP3);
					} else if (ExtensionesFichero.OGG.getValue().equals(getMetadataValueAsString(metaData.getValue()))) {
						document.setEniFormatExtensio(ExtensionesFichero.OGG);
					} else if (ExtensionesFichero.OGA.getValue().equals(getMetadataValueAsString(metaData.getValue()))) {
						document.setEniFormatExtensio(ExtensionesFichero.OGA);
					} else if (ExtensionesFichero.MPEG.getValue().equals(getMetadataValueAsString(metaData.getValue()))) {
						document.setEniFormatExtensio(ExtensionesFichero.MPEG);
					} else if (ExtensionesFichero.MP4.getValue().equals(getMetadataValueAsString(metaData.getValue()))) {
						document.setEniFormatExtensio(ExtensionesFichero.MP4);
					} else if (ExtensionesFichero.WEBM.getValue().equals(getMetadataValueAsString(metaData.getValue()))) {
						document.setEniFormatExtensio(ExtensionesFichero.WEBM);
					}
				} else if (MetadatosDocumento.ORGANO.equals(metaData.getQname())) {
					document.setEniOrgans(getMetadataValueAsListString(metaData.getValue()));
				} else if (MetadatosDocumento.FECHA_INICIO.equals(metaData.getQname())) {
					document.setEniDataCaptura(getMetadataValueAsDate(metaData.getValue()));
				} else if (MetadatosDocumento.TIPO_FIRMA.equals(metaData.getQname())) {
					document.setEniFirmaTipus(getMetadataValueAsListString(metaData.getValue()));
				} else if (MetadatosDocumento.CSV.equals(metaData.getQname())) {
					document.setEniFirmaCsv(getMetadataValueAsListString(metaData.getValue()));
				} else if (MetadatosDocumento.DEF_CSV.equals(metaData.getQname())) {
					document.setEniFirmaCsvDefinicio(getMetadataValueAsListString(metaData.getValue()));
				} else if (MetadatosDocumento.CODIGO_CLASIFICACION.equals(metaData.getQname())) {
					document.setSerieDocumental(getMetadataValueAsString(metaData.getValue()));
				} else if (MetadatosDocumento.CODIGO_APLICACION_TRAMITE.equals(metaData.getQname())) {
					document.setAplicacioCreacio(getMetadataValueAsString(metaData.getValue()));
				} else if (MetadatosDocumento.SOPORTE.equals(metaData.getQname())) {
					document.setSuport(getMetadataValueAsString(metaData.getValue()));
				}
			}
		}
		document.setJson(extreureResParamJson(respostaJson));
		return document;
	}

	private ArxiuFolder toArxiuFolder(
			FolderNode folderNode,
			String respostaJson) throws ParseException {
		ArxiuFolder folder = new ArxiuFolder();
		folder.setNodeId(folderNode.getId());
		folder.setName(folderNode.getName());
		folder.setJson(extreureResParamJson(respostaJson));
		return folder;
	}

	private static final String JSON_RES_PARAM = "\"resParam\":";
	private String extreureResParamJson(String respostaJson) {
		int indexResParam = respostaJson.indexOf(JSON_RES_PARAM);
		if (indexResParam != -1) {
			int indexClau = respostaJson.lastIndexOf("}");
			indexClau = respostaJson.lastIndexOf("}", indexClau);
			return respostaJson.substring(
					indexResParam + JSON_RES_PARAM.length(),
					indexClau - 1);
		}
		return respostaJson;
	}

	private String getMetadataValueAsString(Object value) {
		if (value == null) {
			return null;
		}
		return value.toString();
	}

	private Date getMetadataValueAsDate(Object value) throws ParseException {
		if (value == null) {
			return null;
		}
		return parseDateIso8601((String)value);
	}

	@SuppressWarnings("unchecked")
	private List<String> getMetadataValueAsListString(Object value) throws ParseException {
		if (value == null) {
			return null;
		}
		if (value instanceof List) {
			return (List<String>)value;
		} else {
			List<String> resultat = new ArrayList<String>();
			resultat.add(getMetadataValueAsString(value));
			return resultat;
		}
	}

	private String formatDateIso8601(Date date) {
		TimeZone tz = TimeZone.getTimeZone("UTC");
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		df.setTimeZone(tz);
		return df.format(date);
	}
	private Date parseDateIso8601(String date) throws ParseException {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX");
		return df.parse(date);
	}

	private ArxiuException generarExcepcioJson(
			String metode,
			JerseyResponse resposta)
		      throws JsonParseException, JsonMappingException, IOException {
		ExceptionResult exceptionResult = mapper.readValue(
				resposta.getJson(),
				ExceptionResult.class);
		String code = exceptionResult.getException().getCode();
		String description = exceptionResult.getException().getDescription();
		if ("COD_021".equals(code) && description.contains("not found")) {
			return new ArxiuNotFoundException(
					metode,
					resposta.getStatus(),
					code,
					description);
		} else {
			return new ArxiuException(
					metode,
					resposta.getStatus(),
					code,
					description);
		}
	}

	private class JerseyResponse {
		String json;
		int status;
		public JerseyResponse(String json, int status) {
			this.json = json;
			this.status = status;
		}
		public String getJson() {
			return json;
		}
		public int getStatus() {
			return status;
		}
	}

	private static final Logger logger = LoggerFactory.getLogger(ArxiuClientImpl.class);

}
