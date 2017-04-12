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
import es.caib.arxiudigital.apirest.CSGD.entidades.parametrosLlamada.ParamCreateDraftDocument;
import es.caib.arxiudigital.apirest.CSGD.entidades.parametrosLlamada.ParamCreateFile;
import es.caib.arxiudigital.apirest.CSGD.entidades.parametrosLlamada.ParamCreateFolder;
import es.caib.arxiudigital.apirest.CSGD.entidades.parametrosLlamada.ParamGetDocument;
import es.caib.arxiudigital.apirest.CSGD.entidades.parametrosLlamada.ParamNodeID_TargetParent;
import es.caib.arxiudigital.apirest.CSGD.entidades.parametrosLlamada.ParamNodeId;
import es.caib.arxiudigital.apirest.CSGD.entidades.parametrosLlamada.ParamSetDocument;
import es.caib.arxiudigital.apirest.CSGD.entidades.parametrosLlamada.ParamSetFile;
import es.caib.arxiudigital.apirest.CSGD.entidades.parametrosLlamada.ParamSetFolder;
import es.caib.arxiudigital.apirest.CSGD.entidades.resultados.CreateDraftDocumentResult;
import es.caib.arxiudigital.apirest.CSGD.entidades.resultados.CreateFileResult;
import es.caib.arxiudigital.apirest.CSGD.entidades.resultados.CreateFolderResult;
import es.caib.arxiudigital.apirest.CSGD.entidades.resultados.ExceptionResult;
import es.caib.arxiudigital.apirest.CSGD.entidades.resultados.ExportFileResult;
import es.caib.arxiudigital.apirest.CSGD.entidades.resultados.GenerateDocCSVResult;
import es.caib.arxiudigital.apirest.CSGD.entidades.resultados.GetDocVersionListResult;
import es.caib.arxiudigital.apirest.CSGD.entidades.resultados.GetDocumentResult;
import es.caib.arxiudigital.apirest.CSGD.entidades.resultados.GetENIDocumentResult;
import es.caib.arxiudigital.apirest.CSGD.entidades.resultados.GetFileResult;
import es.caib.arxiudigital.apirest.CSGD.entidades.resultados.GetFolderResult;
import es.caib.arxiudigital.apirest.CSGD.peticiones.CloseFile;
import es.caib.arxiudigital.apirest.CSGD.peticiones.CopyDocument;
import es.caib.arxiudigital.apirest.CSGD.peticiones.CopyFolder;
import es.caib.arxiudigital.apirest.CSGD.peticiones.CreateDraftDocument;
import es.caib.arxiudigital.apirest.CSGD.peticiones.CreateFile;
import es.caib.arxiudigital.apirest.CSGD.peticiones.CreateFolder;
import es.caib.arxiudigital.apirest.CSGD.peticiones.ExportFile;
import es.caib.arxiudigital.apirest.CSGD.peticiones.GenerateDocCSV;
import es.caib.arxiudigital.apirest.CSGD.peticiones.GetDocVersionList;
import es.caib.arxiudigital.apirest.CSGD.peticiones.GetDocument;
import es.caib.arxiudigital.apirest.CSGD.peticiones.GetENIDocument;
import es.caib.arxiudigital.apirest.CSGD.peticiones.GetFile;
import es.caib.arxiudigital.apirest.CSGD.peticiones.GetFolder;
import es.caib.arxiudigital.apirest.CSGD.peticiones.MoveDocument;
import es.caib.arxiudigital.apirest.CSGD.peticiones.MoveFolder;
import es.caib.arxiudigital.apirest.CSGD.peticiones.RemoveDocument;
import es.caib.arxiudigital.apirest.CSGD.peticiones.RemoveFile;
import es.caib.arxiudigital.apirest.CSGD.peticiones.RemoveFolder;
import es.caib.arxiudigital.apirest.CSGD.peticiones.ReopenFile;
import es.caib.arxiudigital.apirest.CSGD.peticiones.Request;
import es.caib.arxiudigital.apirest.CSGD.peticiones.SetDocument;
import es.caib.arxiudigital.apirest.CSGD.peticiones.SetFile;
import es.caib.arxiudigital.apirest.CSGD.peticiones.SetFinalDocument;
import es.caib.arxiudigital.apirest.CSGD.peticiones.SetFolder;
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
import es.caib.arxiudigital.apirest.facade.pojos.Documento;
import es.caib.arxiudigital.apirest.facade.pojos.Expediente;
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
			Map<String, Object> metadadesAddicionals,
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
			if (metadadesAddicionals != null) {
				metadades.putAll(metadadesAddicionals);
			}
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
				FileNode fileNode = result.getCreateFileResult().getResParam();
				return new ArxiuFile(
						fileNode.getId(),
						fileNode.getName(),
						fileNode.getChildObjects(),
						fileNode.getMetadataCollection(),
						fileNode.getAspects(),
						extreureResParamJson(resposta.getJson()));
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
			Map<String, Object> metadadesAddicionals,
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
			if (metadadesAddicionals != null) {
				metadades.putAll(metadadesAddicionals);
			}
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
				FileNode fileNode = result.getGetFileResult().getResParam();
				return new ArxiuFile(
						fileNode.getId(),
						fileNode.getName(),
						fileNode.getChildObjects(),
						fileNode.getMetadataCollection(),
						fileNode.getAspects(),
						extreureResParamJson(resposta.getJson()));
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
	public void fileClose(
			String nodeId,
			ArxiuHeader capsalera) {
		String metode = ArxiuMetodes.FILE_CLOSE;
		logger.debug("Invocant mètode " + metode + " amb paràmetres (" +
				"nodeId=" + nodeId + ", " +
				"capsalera=" + capsalera + ")");
		try {
			CloseFile closeFile = new CloseFile();
			Request<ParamNodeId> request = new Request<ParamNodeId>();
			request.setServiceHeader(generarServiceHeader(capsalera));
			ParamNodeId paramNodeId = new ParamNodeId();
			paramNodeId.setNodeId(nodeId);
			request.setParam(paramNodeId);
			closeFile.setCloseFileRequest(request);
			JerseyResponse resposta = enviarPeticioRest(
					metode,
					closeFile);
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
	public void fileReopen(
			String nodeId,
			ArxiuHeader capsalera) {
		String metode = ArxiuMetodes.FILE_REOPEN;
		logger.debug("Invocant mètode " + metode + " amb paràmetres (" +
				"nodeId=" + nodeId + ", " +
				"capsalera=" + capsalera + ")");
		try {
			ReopenFile reopenFile = new ReopenFile();
			Request<ParamNodeId> request = new Request<ParamNodeId>();
			request.setServiceHeader(generarServiceHeader(capsalera));
			ParamNodeId paramNodeId = new ParamNodeId();
			paramNodeId.setNodeId(nodeId);
			request.setParam(paramNodeId);
			reopenFile.setReopenFileRequest(request);
			JerseyResponse resposta = enviarPeticioRest(
					metode,
					reopenFile);
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
	public String fileExport(
			String nodeId,
			ArxiuHeader capsalera) {
		String metode = ArxiuMetodes.FILE_EXPORT;
		logger.debug("Invocant mètode " + metode + " amb paràmetres (" +
				"nodeId=" + nodeId + ", " +
				"capsalera=" + capsalera + ")");
		try {
			ExportFile exportFile = new ExportFile();
			Request<ParamNodeId> request = new Request<ParamNodeId>();
			request.setServiceHeader(generarServiceHeader(capsalera));
			ParamNodeId paramNodeId = new ParamNodeId();
			paramNodeId.setNodeId(nodeId);
			request.setParam(paramNodeId);
			exportFile.setExportFileRequest(request);
			JerseyResponse resposta = enviarPeticioRest(
					metode,
					exportFile);
			logger.debug("Rebuda resposta mètode " + metode + " (" +
					"status=" + resposta.getStatus() + ", " +
					"contingut=" + resposta.getJson() + ")");
			if (resposta.getStatus() == 200) {
				ExportFileResult result = mapper.readValue(
						resposta.getJson(),
						ExportFileResult.class);
				String exportBase64 = result.getExportFileResult().getResParam();
				if (exportBase64 != null) {
					return new String(
							Base64.decodeBase64(exportBase64));
				} else {
					return null;
				}
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
	public void fileEasySearch() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void fileLink() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void fileLock() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void fileUnlock() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void fileVersionList() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void filePermissionsGrant() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void filePermissionsCancel() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void fileIndexGenerate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void fileChildCreate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void fileChildMove() {
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
			List<String> organs,
			String serieDocumental,
			InputStream contingut,
			String pareNodeId,
			ExtensionesFichero formatExtensio,
			String tipusMime,
			Map<String, Object> metadadesAddicionals,
			ArxiuHeader capsalera) {
		String metode = ArxiuMetodes.DOCUMENT_CREATE_DRAFT;
		logger.debug("Invocant mètode " + metode + " amb paràmetres (" +
				"titol=" + titol + ", " +
				"origen=" + origen + ", " +
				"dataCaptura=" + dataCaptura + ", " +
				"estatElaboracio=" + estatElaboracio + ", " +
				"documentTipus=" + documentTipus + ", " +
				"formatNom=" + formatNom + ", " +
				"organs=" + organs + ", " +
				"serieDocumental=" + serieDocumental + ", " +
				"pareNodeId=" + pareNodeId + ", " +
				"formatExtensio=" + formatExtensio + ", " +
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
					null,
					null,
					null,
					metode);
			if (metadadesAddicionals != null) {
				metadades.putAll(metadadesAddicionals);
			}
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
				DocumentNode documentNode = result.getCreateDraftDocumentResult().getResParam();
				return new ArxiuDocument(
						documentNode.getId(),
						documentNode.getName(),
						documentNode.getBinaryContents(),
						documentNode.getMetadataCollection(),
						documentNode.getAspects(),
						extreureResParamJson(resposta.getJson()));
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
			/*String titol,
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
			ArxiuHeader capsalera*/) {
		/*String metode = ArxiuMetodes.DOCUMENT_CREATE;
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
		}*/
		return null;
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
			List<String> organs,
			String serieDocumental,
			InputStream contingut,
			ExtensionesFichero formatExtensio,
			String tipusMime,
			Map<String, Object> metadadesAddicionals,
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
				"organs=" + organs + ", " +
				"serieDocumental=" + serieDocumental + ", " +
				"formatExtensio=" + formatExtensio + ", " +
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
					null,
					null,
					null,
					metode);
			if (metadadesAddicionals != null) {
				metadades.putAll(metadadesAddicionals);
			}
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
				DocumentNode documentNode = result.getGetDocumentResult().getResParam();
				return new ArxiuDocument(
						documentNode.getId(),
						documentNode.getName(),
						documentNode.getBinaryContents(),
						documentNode.getMetadataCollection(),
						documentNode.getAspects(),
						extreureResParamJson(resposta.getJson()));
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
	public void documentFinalSet(
			String nodeId,
			String titol,
			OrigenesContenido origen,
			Date dataCaptura,
			EstadosElaboracion estatElaboracio,
			TiposDocumentosENI documentTipus,
			FormatosFichero formatNom,
			List<String> organs,
			String serieDocumental,
			InputStream contingut,
			TiposFirma firmaTipus,
			PerfilesFirma firmaPerfil,
			String csv,
			ExtensionesFichero formatExtensio,
			String tipusMime,
			Map<String, Object> metadadesAddicionals,
			ArxiuHeader capsalera) {
		String metode = ArxiuMetodes.DOCUMENT_SET_FINAL;
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
				"firmaTipus=" + firmaTipus + ", " +
				"firmaPerfil=" + firmaPerfil + ", " +
				"csv=" + csv + ", " +
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
					firmaTipus,
					firmaPerfil,
					csv,
					metode);
			if (metadadesAddicionals != null) {
				metadades.putAll(metadadesAddicionals);
			}
			SetFinalDocument setFinalDocument = new SetFinalDocument();
			Request<ParamSetDocument> request = new Request<ParamSetDocument>();
			ParamSetDocument param = new ParamSetDocument();
			param.setDocument(toDocumentNode(
					nodeId,
					titol,
					metadades,
					contingut,
					tipusMime));
			request.setParam(param);
			request.setServiceHeader(generarServiceHeader(capsalera));
			setFinalDocument.setSetFinalDocumentRequest(request);
			JerseyResponse resposta = enviarPeticioRest(
					metode,
					setFinalDocument);
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
	public String documentEniGet(
			String nodeId,
			ArxiuHeader capsalera) {
		String metode = ArxiuMetodes.DOCUMENT_GET_ENIDOC;
		logger.debug("Invocant mètode " + metode + " amb paràmetres (" +
				"nodeId=" + nodeId + ", " +
				"capsalera=" + capsalera + ")");
		try {
			GetENIDocument getEniDocument = new GetENIDocument();
			Request<ParamNodeId> request = new Request<ParamNodeId>();
			request.setServiceHeader(generarServiceHeader(capsalera));
			ParamNodeId param = new ParamNodeId();
			param.setNodeId(nodeId);
			request.setParam(param);
			request.setServiceHeader(generarServiceHeader(capsalera));
			getEniDocument.setGetENIDocRequest(request);
			JerseyResponse resposta = enviarPeticioRest(
					metode,
					getEniDocument);
			logger.debug("Rebuda resposta mètode " + metode + " (" +
					"status=" + resposta.getStatus() + ", " +
					"contingut=" + resposta.getJson() + ")");
			if (resposta.getStatus() == 200) {
				GetENIDocumentResult result = mapper.readValue(
						resposta.getJson(),
						GetENIDocumentResult.class);
				String exportBase64 = result.getGetENIDocResult().getResParam();
				if (exportBase64 != null) {
					return new String(
							Base64.decodeBase64(exportBase64));
				} else {
					return null;
				}
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
	public void documentCopy(
			String nodeId,
			String nodeDestiId,
			ArxiuHeader capsalera) {
		String metode = ArxiuMetodes.DOCUMENT_COPY;
		logger.debug("Invocant mètode " + metode + " amb paràmetres (" +
				"nodeId=" + nodeId + ", " +
				"capsalera=" + capsalera + ")");
		try {
			CopyDocument copyDocument = new CopyDocument();
			Request<ParamNodeID_TargetParent> request = new Request<ParamNodeID_TargetParent>();
			ParamNodeID_TargetParent paramTarget = new ParamNodeID_TargetParent();
			paramTarget.setNodeId(nodeId);
			paramTarget.setTargetParent(nodeDestiId);
			request.setParam(paramTarget);
			request.setServiceHeader(generarServiceHeader(capsalera));
			copyDocument.setCopyDocumentRequest(request);
			JerseyResponse resposta = enviarPeticioRest(
					metode,
					copyDocument);
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
	public void documentMove(
			String nodeId,
			String nodeDestiId,
			ArxiuHeader capsalera) {
		String metode = ArxiuMetodes.DOCUMENT_MOVE;
		logger.debug("Invocant mètode " + metode + " amb paràmetres (" +
				"nodeId=" + nodeId + ", " +
				"capsalera=" + capsalera + ")");
		try {
			MoveDocument moveDocument = new MoveDocument();
			Request<ParamNodeID_TargetParent> request = new Request<ParamNodeID_TargetParent>();
			ParamNodeID_TargetParent paramTarget = new ParamNodeID_TargetParent();
			paramTarget.setNodeId(nodeId);
			paramTarget.setTargetParent(nodeDestiId);
			request.setParam(paramTarget);
			request.setServiceHeader(generarServiceHeader(capsalera));
			moveDocument.setMoveDocumentRequest(request);
			JerseyResponse resposta = enviarPeticioRest(
					metode,
					moveDocument);
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
	public void documentSearch() {
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
				FolderNode folderNode = result.getCreateFolderResult().getResParam();
				return new ArxiuFolder(
						folderNode.getId(),
						folderNode.getName(),
						folderNode.getChildObjects(),
						extreureResParamJson(resposta.getJson()));
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
				FolderNode folderNode = result.getGetFolderResult().getResParam();
				return new ArxiuFolder(
						folderNode.getId(),
						folderNode.getName(),
						folderNode.getChildObjects(),
						extreureResParamJson(resposta.getJson()));
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
	public void folderCopy(
			String nodeId,
			String nodeDestiId,
			ArxiuHeader capsalera) {
		String metode = ArxiuMetodes.FOLDER_COPY;
		logger.debug("Invocant mètode " + metode + " amb paràmetres (" +
				"nodeId=" + nodeId + ", " +
				"capsalera=" + capsalera + ")");
		try {
			CopyFolder copyFolder = new CopyFolder();
			Request<ParamNodeID_TargetParent> request = new Request<ParamNodeID_TargetParent>();
			ParamNodeID_TargetParent paramTarget = new ParamNodeID_TargetParent();
			paramTarget.setNodeId(nodeId);
			paramTarget.setTargetParent(nodeDestiId);
			request.setParam(paramTarget);
			request.setServiceHeader(generarServiceHeader(capsalera));
			copyFolder.setCopyDocumentFolder(request);
			JerseyResponse resposta = enviarPeticioRest(
					metode,
					copyFolder);
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
	public void folderMove(
			String nodeId,
			String nodeDestiId,
			ArxiuHeader capsalera) {
		String metode = ArxiuMetodes.FOLDER_MOVE;
		logger.debug("Invocant mètode " + metode + " amb paràmetres (" +
				"nodeId=" + nodeId + ", " +
				"capsalera=" + capsalera + ")");
		try {
			MoveFolder moveFolder = new MoveFolder();
			Request<ParamNodeID_TargetParent> request = new Request<ParamNodeID_TargetParent>();
			ParamNodeID_TargetParent paramTarget = new ParamNodeID_TargetParent();
			paramTarget.setNodeId(nodeId);
			paramTarget.setTargetParent(nodeDestiId);
			request.setParam(paramTarget);
			request.setServiceHeader(generarServiceHeader(capsalera));
			moveFolder.setMoveFolderRequest(request);
			JerseyResponse resposta = enviarPeticioRest(
					metode,
					moveFolder);
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
		System.out.println(">>> Enviant petició POST a l'arxiu (" +
				"url=" + urlAmbMetode + ", " +
				"tipus=application/json, " +
				"body=" + body + ")");
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
			TiposFirma firmaTipus,
			PerfilesFirma firmaPerfil,
			String csv,
			String metode) {
		Map<String, Object> metadades  = new HashMap<String, Object>();
		metadades.put(
				MetadatosDocumento.CODIGO_APLICACION_TRAMITE,
				aplicacioCodi);
		if (origen != null) {
			metadades.put(
					MetadatosDocumento.ORIGEN,
					origen);
		}
		if (dataCaptura != null){
			metadades.put(
					MetadatosDocumento.FECHA_INICIO,
					formatDateIso8601(dataCaptura));
		}
		if (estatElaboracio != null) {
			metadades.put(
					MetadatosDocumento.ESTADO_ELABORACION,
					estatElaboracio);
		}
		if (documentTipus != null) {
			metadades.put(
					MetadatosDocumento.TIPO_DOC_ENI,
					documentTipus);
		}
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
		if (serieDocumental != null) {
			metadades.put(
					MetadatosDocumento.CODIGO_CLASIFICACION,
					serieDocumental);
		}
		if (firmaTipus != null) {
			metadades.put(
					MetadatosDocumento.TIPO_FIRMA,
					firmaTipus);
		}
		if (firmaPerfil != null) {
			metadades.put(
					MetadatosDocumento.PERFIL_FIRMA,
					firmaPerfil);
		}
		if (csv != null) {
			metadades.put(
					MetadatosDocumento.CSV,
					csv);
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
		List<Content> listaContenidos = new ArrayList<Content>();
		// Si no se envía el contenido del documento, no se añade el objeto CONTENT
		// del documento
		if (doc != null && doc.getContent() != null) {
			contenido.setBinaryType(doc.getBinarytype());
			contenido.setContent(doc.getContent());
			contenido.setEncoding(doc.getEncoding());
			contenido.setMimetype(doc.getMimetype());
			listaContenidos.add(contenido);
		}
		/*if (firma != null && firma.getContent() != null) {
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
		}*/
		nodo.setId(doc.getId());
		nodo.setName(doc.getName());
		nodo.setType(doc.getType());
		nodo.setMetadataCollection(MetadataUtils.generarListaMetadatos(doc.getMetadataCollection()));
		nodo.setAspects(doc.getAspects());
		nodo.setBinaryContents(listaContenidos);
		return nodo;
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

	private Date getMetadataValueAsDate(Object value) throws ParseException {
		if (value == null) {
			return null;
		}
		return parseDateIso8601((String)value);
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
