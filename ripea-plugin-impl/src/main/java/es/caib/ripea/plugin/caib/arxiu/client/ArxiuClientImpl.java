/**
 * 
 */
package es.caib.ripea.plugin.caib.arxiu.client;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
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
import es.caib.arxiudigital.apirest.CSGD.entidades.comunes.DocClassification;
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
import es.caib.arxiudigital.apirest.CSGD.entidades.comunes.TargetNode;
import es.caib.arxiudigital.apirest.CSGD.entidades.comunes.VersionNode;
import es.caib.arxiudigital.apirest.CSGD.entidades.parametrosLlamada.ParamCancelPermissions;
import es.caib.arxiudigital.apirest.CSGD.entidades.parametrosLlamada.ParamCreateChildFile;
import es.caib.arxiudigital.apirest.CSGD.entidades.parametrosLlamada.ParamCreateDocument;
import es.caib.arxiudigital.apirest.CSGD.entidades.parametrosLlamada.ParamCreateDraftDocument;
import es.caib.arxiudigital.apirest.CSGD.entidades.parametrosLlamada.ParamCreateFile;
import es.caib.arxiudigital.apirest.CSGD.entidades.parametrosLlamada.ParamCreateFolder;
import es.caib.arxiudigital.apirest.CSGD.entidades.parametrosLlamada.ParamDispatchDocument;
import es.caib.arxiudigital.apirest.CSGD.entidades.parametrosLlamada.ParamGetDocument;
import es.caib.arxiudigital.apirest.CSGD.entidades.parametrosLlamada.ParamGrantPermissions;
import es.caib.arxiudigital.apirest.CSGD.entidades.parametrosLlamada.ParamNodeID_TargetParent;
import es.caib.arxiudigital.apirest.CSGD.entidades.parametrosLlamada.ParamNodeId;
import es.caib.arxiudigital.apirest.CSGD.entidades.parametrosLlamada.ParamSetDocument;
import es.caib.arxiudigital.apirest.CSGD.entidades.parametrosLlamada.ParamSetFile;
import es.caib.arxiudigital.apirest.CSGD.entidades.parametrosLlamada.ParamSetFolder;
import es.caib.arxiudigital.apirest.CSGD.entidades.resultados.CreateChildFileResult;
import es.caib.arxiudigital.apirest.CSGD.entidades.resultados.CreateDraftDocumentResult;
import es.caib.arxiudigital.apirest.CSGD.entidades.resultados.CreateFileResult;
import es.caib.arxiudigital.apirest.CSGD.entidades.resultados.CreateFolderResult;
import es.caib.arxiudigital.apirest.CSGD.entidades.resultados.DispatchDocumentResult;
import es.caib.arxiudigital.apirest.CSGD.entidades.resultados.ExceptionResult;
import es.caib.arxiudigital.apirest.CSGD.entidades.resultados.ExportFileResult;
import es.caib.arxiudigital.apirest.CSGD.entidades.resultados.GenerateDocCSVResult;
import es.caib.arxiudigital.apirest.CSGD.entidades.resultados.GetDocVersionListResult;
import es.caib.arxiudigital.apirest.CSGD.entidades.resultados.GetDocumentResult;
import es.caib.arxiudigital.apirest.CSGD.entidades.resultados.GetENIDocumentResult;
import es.caib.arxiudigital.apirest.CSGD.entidades.resultados.GetFileResult;
import es.caib.arxiudigital.apirest.CSGD.entidades.resultados.GetFolderResult;
import es.caib.arxiudigital.apirest.CSGD.peticiones.CancelPermissionsOnDocs;
import es.caib.arxiudigital.apirest.CSGD.peticiones.CancelPermissionsOnFiles;
import es.caib.arxiudigital.apirest.CSGD.peticiones.CancelPermissionsOnFolders;
import es.caib.arxiudigital.apirest.CSGD.peticiones.CloseFile;
import es.caib.arxiudigital.apirest.CSGD.peticiones.CopyDocument;
import es.caib.arxiudigital.apirest.CSGD.peticiones.CopyFolder;
import es.caib.arxiudigital.apirest.CSGD.peticiones.CreateChildFile;
import es.caib.arxiudigital.apirest.CSGD.peticiones.CreateDocument;
import es.caib.arxiudigital.apirest.CSGD.peticiones.CreateDraftDocument;
import es.caib.arxiudigital.apirest.CSGD.peticiones.CreateFile;
import es.caib.arxiudigital.apirest.CSGD.peticiones.CreateFolder;
import es.caib.arxiudigital.apirest.CSGD.peticiones.DispatchDocument;
import es.caib.arxiudigital.apirest.CSGD.peticiones.ExportFile;
import es.caib.arxiudigital.apirest.CSGD.peticiones.GenerateDocCSV;
import es.caib.arxiudigital.apirest.CSGD.peticiones.GenerateFileIndex;
import es.caib.arxiudigital.apirest.CSGD.peticiones.GetDocVersionList;
import es.caib.arxiudigital.apirest.CSGD.peticiones.GetDocument;
import es.caib.arxiudigital.apirest.CSGD.peticiones.GetENIDocument;
import es.caib.arxiudigital.apirest.CSGD.peticiones.GetFile;
import es.caib.arxiudigital.apirest.CSGD.peticiones.GetFileVersionList;
import es.caib.arxiudigital.apirest.CSGD.peticiones.GetFolder;
import es.caib.arxiudigital.apirest.CSGD.peticiones.GrantPermissionsOnDocs;
import es.caib.arxiudigital.apirest.CSGD.peticiones.GrantPermissionsOnFiles;
import es.caib.arxiudigital.apirest.CSGD.peticiones.GrantPermissionsOnFolders;
import es.caib.arxiudigital.apirest.CSGD.peticiones.LinkDocument;
import es.caib.arxiudigital.apirest.CSGD.peticiones.LinkFile;
import es.caib.arxiudigital.apirest.CSGD.peticiones.LinkFolder;
import es.caib.arxiudigital.apirest.CSGD.peticiones.LockDocument;
import es.caib.arxiudigital.apirest.CSGD.peticiones.LockFile;
import es.caib.arxiudigital.apirest.CSGD.peticiones.LockFolder;
import es.caib.arxiudigital.apirest.CSGD.peticiones.MoveChildFile;
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
import es.caib.arxiudigital.apirest.CSGD.peticiones.UnlockDocument;
import es.caib.arxiudigital.apirest.CSGD.peticiones.UnlockFile;
import es.caib.arxiudigital.apirest.CSGD.peticiones.UnlockFolder;
import es.caib.arxiudigital.apirest.constantes.Aspectos;
import es.caib.arxiudigital.apirest.constantes.Permisos;
import es.caib.arxiudigital.apirest.constantes.TiposContenidosBinarios;
import es.caib.arxiudigital.apirest.constantes.TiposObjetoSGD;

/**
 * Interfície del client per a accedir a la funcionalitat de
 * l'arxiu digital.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public class ArxiuClientImpl implements ArxiuClient {

	private static final String SERVEI_VERSIO = "1.0";

	private String url;
	private String aplicacioCodi;
	private String usuariSgd;
	private String contrasenyaSgd;

	private Client jerseyClient;
	private ObjectMapper mapper;
	private String lastJsonRequest;
	private String lastJsonResponse;



	public ArxiuClientImpl(
			String url,
			String aplicacioCodi,
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
			String usuariSgd,
			String contrasenyaSgd) {
		this(	url,
				aplicacioCodi,
				null,
				null,
				usuariSgd,
				contrasenyaSgd);
	}



	@Override
	public ArxiuFile fileCreate(
			ArxiuFile file,
			ArxiuHeader capsalera) throws ArxiuException {
		String metode = ArxiuMethods.FILE_CREATE;
		try {
			CreateFile createFile = new CreateFile();
			Request<ParamCreateFile> request = new Request<ParamCreateFile>();
			request.setServiceHeader(generarServiceHeader(capsalera));
			ParamCreateFile param = new ParamCreateFile();
			param.setFile(toFileNode(file, true));
			param.setRetrieveNode(Boolean.TRUE.toString());
			request.setParam(param);
			createFile.setCreateFileRequest(request);
			JerseyResponse resposta = enviarPeticioRest(
					metode,
					createFile);
			if (resposta.getStatus() == 200) {
				CreateFileResult result = mapper.readValue(
						resposta.getJson(),
						CreateFileResult.class);
				FileNode fileNode = result.getCreateFileResult().getResParam();
				ArxiuFile fileCreat = new ArxiuFile(
						fileNode.getId(),
						fileNode.getName(),
						generarChildrenPerRetornar(fileNode.getChildObjects()),
						generarMetadadesPerRetornar(fileNode.getMetadataCollection()),
						generarAspectesPerRetornar(fileNode.getAspects()));
				return fileCreat;
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
			ArxiuFile file,
			ArxiuHeader capsalera) throws ArxiuException {
		String metode = ArxiuMethods.FILE_CREATE;
		try {
			SetFile setFile = new SetFile();
			Request<ParamSetFile> request = new Request<ParamSetFile>();
			request.setServiceHeader(generarServiceHeader(capsalera));
			ParamSetFile param = new ParamSetFile();
			param.setFile(toFileNode(file, true));
			request.setParam(param);
			setFile.setSetFileRequest(request);
			JerseyResponse resposta = enviarPeticioRest(
					metode,
					setFile);
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
		String metode = ArxiuMethods.FILE_REMOVE;
		try {
			RemoveFile removeFile = new RemoveFile();
			Request<ParamNodeId> request = new Request<ParamNodeId>();
			request.setServiceHeader(generarServiceHeader(capsalera));
			ParamNodeId param = new ParamNodeId();
			param.setNodeId(nodeId);
			request.setParam(param);
			removeFile.setRemoveFileRequest(request);
			JerseyResponse resposta = enviarPeticioRest(
					metode,
					removeFile);
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
		String metode = ArxiuMethods.FILE_GET;
		try {
			GetFile getFile = new GetFile();
			Request<ParamNodeId> request = new Request<ParamNodeId>();
			request.setServiceHeader(generarServiceHeader(capsalera));
			ParamNodeId param = new ParamNodeId();
			param.setNodeId(nodeId);
			request.setParam(param);
			getFile.setGetFileRequest(request);
			JerseyResponse resposta = enviarPeticioRest(
					metode,
					getFile);
			if (resposta.getStatus() == 200) {
				GetFileResult result = mapper.readValue(
						resposta.getJson(),
						GetFileResult.class);
				FileNode fileNode = result.getGetFileResult().getResParam();
				ArxiuFile fileObtingut = new ArxiuFile(
						fileNode.getId(),
						fileNode.getName(),
						generarChildrenPerRetornar(fileNode.getChildObjects()),
						generarMetadadesPerRetornar(fileNode.getMetadataCollection()),
						generarAspectesPerRetornar(fileNode.getAspects()));
				return fileObtingut;
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
	public List<ArxiuContentVersion> fileVersionList(
			String nodeId,
			ArxiuHeader capsalera) throws ArxiuException {
		String metode = ArxiuMethods.FILE_GET_VERSION;
		try {
			GetFileVersionList getFileVersionList = new GetFileVersionList();
			Request<ParamNodeId> request = new Request<ParamNodeId>();
			request.setServiceHeader(generarServiceHeader(capsalera));
			ParamNodeId param = new ParamNodeId();
			param.setNodeId(nodeId);
			request.setParam(param);
			getFileVersionList.setGetFileVersionListRequest(request);
			JerseyResponse resposta = enviarPeticioRest(
					metode,
					getFileVersionList);
			if (resposta.getStatus() == 200) {
				GetDocVersionListResult result = mapper.readValue(
						resposta.getJson(),
						GetDocVersionListResult.class);
				List<ArxiuContentVersion> versions = new ArrayList<ArxiuContentVersion>();
				List<VersionNode> vns = result.getGetDocVersionListResult().getResParam();
				for (VersionNode vn: vns) {
					ArxiuContentVersion versio = new ArxiuContentVersion();
					versio.setId(vn.getId());
					versio.setData(getMetadataValueAsDate(vn.getDate()));
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
	public void fileClose(
			String nodeId,
			ArxiuHeader capsalera) {
		String metode = ArxiuMethods.FILE_CLOSE;
		try {
			CloseFile closeFile = new CloseFile();
			Request<ParamNodeId> request = new Request<ParamNodeId>();
			request.setServiceHeader(generarServiceHeader(capsalera));
			ParamNodeId param = new ParamNodeId();
			param.setNodeId(nodeId);
			request.setParam(param);
			closeFile.setCloseFileRequest(request);
			JerseyResponse resposta = enviarPeticioRest(
					metode,
					closeFile);
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
		String metode = ArxiuMethods.FILE_REOPEN;
		try {
			ReopenFile reopenFile = new ReopenFile();
			Request<ParamNodeId> request = new Request<ParamNodeId>();
			request.setServiceHeader(generarServiceHeader(capsalera));
			ParamNodeId param = new ParamNodeId();
			param.setNodeId(nodeId);
			request.setParam(param);
			reopenFile.setReopenFileRequest(request);
			JerseyResponse resposta = enviarPeticioRest(
					metode,
					reopenFile);
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
		String metode = ArxiuMethods.FILE_EXPORT;
		try {
			ExportFile exportFile = new ExportFile();
			Request<ParamNodeId> request = new Request<ParamNodeId>();
			request.setServiceHeader(generarServiceHeader(capsalera));
			ParamNodeId param = new ParamNodeId();
			param.setNodeId(nodeId);
			request.setParam(param);
			exportFile.setExportFileRequest(request);
			JerseyResponse resposta = enviarPeticioRest(
					metode,
					exportFile);
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
	public String fileIndexGenerate(
			String nodeId,
			ArxiuHeader capsalera) throws ArxiuException {
		String metode = ArxiuMethods.FILE_GENERAR_INDEX;
		try {
			GenerateFileIndex generateFileIndex = new GenerateFileIndex();
			Request<ParamNodeId> request = new Request<ParamNodeId>();
			request.setServiceHeader(generarServiceHeader(capsalera));
			ParamNodeId param = new ParamNodeId();
			param.setNodeId(nodeId);
			request.setParam(param);
			generateFileIndex.setGenerateFileIndexRequest(request);
			JerseyResponse resposta = enviarPeticioRest(
					metode,
					generateFileIndex);
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
	public ArxiuFile fileChildCreate(
			String pareNodeId,
			ArxiuFile file,
			ArxiuHeader capsalera) {
		String metode = ArxiuMethods.FILE_CREATE_CHILD;
		try {
			CreateChildFile createChildFile = new CreateChildFile();
			Request<ParamCreateChildFile> request = new Request<ParamCreateChildFile>();
			request.setServiceHeader(generarServiceHeader(capsalera));
			ParamCreateChildFile param = new ParamCreateChildFile();
			param.setParent(pareNodeId);
			param.setFile(toFileNode(file, true));
			param.setRetrieveNode(Boolean.TRUE.toString());
			request.setParam(param);
			createChildFile.setCreateChildFileRequest(request);
			JerseyResponse resposta = enviarPeticioRest(
					metode,
					createChildFile);
			if (resposta.getStatus() == 200) {
				CreateChildFileResult result = mapper.readValue(
						resposta.getJson(),
						CreateChildFileResult.class);
				FileNode fileNode = result.getCreateChildFileResult().getResParam();
				ArxiuFile fileCreat = new ArxiuFile(
						fileNode.getId(),
						fileNode.getName(),
						generarChildrenPerRetornar(fileNode.getChildObjects()),
						generarMetadadesPerRetornar(fileNode.getMetadataCollection()),
						generarAspectesPerRetornar(fileNode.getAspects()));
				return fileCreat;
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
	public void fileChildMove(
			String nodeId,
			String targetNodeId,
			ArxiuHeader capsalera) {
		String metode = ArxiuMethods.FILE_MOVE_CHILD;
		try {
			MoveChildFile moveChildFile = new MoveChildFile();
			Request<ParamNodeID_TargetParent> request = new Request<ParamNodeID_TargetParent>();
			ParamNodeID_TargetParent param = new ParamNodeID_TargetParent();
			param.setNodeId(nodeId);
			param.setTargetParent(targetNodeId);
			request.setParam(param);
			request.setServiceHeader(generarServiceHeader(capsalera));
			moveChildFile.setMoveChildFileRequest(request);
			JerseyResponse resposta = enviarPeticioRest(
					metode,
					moveChildFile);
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
	public void fileLink(
			String nodeId,
			String targetNodeId,
			ArxiuHeader capsalera) {
		String metode = ArxiuMethods.FILE_LINK;
		try {
			LinkFile linkFile = new LinkFile();
			Request<ParamNodeID_TargetParent> request = new Request<ParamNodeID_TargetParent>();
			ParamNodeID_TargetParent param = new ParamNodeID_TargetParent();
			param.setNodeId(nodeId);
			param.setTargetParent(targetNodeId);
			request.setParam(param);
			request.setServiceHeader(generarServiceHeader(capsalera));
			linkFile.setLinkFileRequest(request);
			JerseyResponse resposta = enviarPeticioRest(
					metode,
					linkFile);
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
	public void fileLock(
			String nodeId,
			ArxiuHeader capsalera) throws ArxiuException {
		String metode = ArxiuMethods.FILE_LOCK;
		try {
			LockFile lockFile = new LockFile();
			Request<ParamNodeId> request = new Request<ParamNodeId>();
			ParamNodeId param = new ParamNodeId();
			param.setNodeId(nodeId);
			request.setParam(param);
			request.setServiceHeader(generarServiceHeader(capsalera));
			lockFile.setLockFileRequest(request);
			JerseyResponse resposta = enviarPeticioRest(
					metode,
					lockFile);
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
	public void fileUnlock(
			String nodeId,
			ArxiuHeader capsalera) throws ArxiuException {
		String metode = ArxiuMethods.FILE_UNLOCK;
		try {
			UnlockFile unlockFile = new UnlockFile();
			Request<ParamNodeId> request = new Request<ParamNodeId>();
			ParamNodeId param = new ParamNodeId();
			param.setNodeId(nodeId);
			request.setParam(param);
			request.setServiceHeader(generarServiceHeader(capsalera));
			unlockFile.setUnlockFileRequest(request);
			JerseyResponse resposta = enviarPeticioRest(
					metode,
					unlockFile);
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
	public void filePermissionsGrant(
			List<String> nodeIds,
			List<String> authorities,
			ArxiuPermissionEnum permis,
			ArxiuHeader capsalera) throws ArxiuException {
		String metode = ArxiuMethods.FILE_GRANT_PERMISOS;
		try {
			GrantPermissionsOnFiles grantPermissionsFiles = new GrantPermissionsOnFiles();
			Request<ParamGrantPermissions> request = new Request<ParamGrantPermissions>();
			ParamGrantPermissions param = new ParamGrantPermissions();
			param.setNodeIds(nodeIds);
			param.setAuthorities(authorities);
			if (permis != null) {
				switch (permis) {
				case READ:
					param.setPermission(Permisos.READ);
					break;
				case WRITE:
					param.setPermission(Permisos.WRITE);
					break;
				}
			}
			request.setParam(param);
			request.setServiceHeader(generarServiceHeader(capsalera));
			grantPermissionsFiles.setGrantPermissionsOnFilesRequest(request);
			JerseyResponse resposta = enviarPeticioRest(
					metode,
					grantPermissionsFiles);
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
	public void filePermissionsCancel(
			List<String> nodeIds,
			List<String> authorities,
			ArxiuHeader capsalera) throws ArxiuException {
		String metode = ArxiuMethods.FILE_CANCEL_PERMISOS;
		try {
			CancelPermissionsOnFiles cancelPermissionsFiles = new CancelPermissionsOnFiles();
			Request<ParamCancelPermissions> request = new Request<ParamCancelPermissions>();
			ParamCancelPermissions param = new ParamCancelPermissions();
			param.setNodeIds(nodeIds);
			param.setAuthorities(authorities);
			request.setParam(param);
			request.setServiceHeader(generarServiceHeader(capsalera));
			cancelPermissionsFiles.setCancelPermissionsOnFilesRequest(request);
			JerseyResponse resposta = enviarPeticioRest(
					metode,
					cancelPermissionsFiles);
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
	public ArxiuDocument documentDraftCreate(
			String pareNodeId,
			ArxiuDocument document,
			ArxiuHeader capsalera) {
		String metode = ArxiuMethods.DOCUMENT_CREATE_DRAFT;
		try {
			CreateDraftDocument createDraftDocument = new CreateDraftDocument();
			Request<ParamCreateDraftDocument> request = new Request<ParamCreateDraftDocument>();
			request.setServiceHeader(generarServiceHeader(capsalera));
			ParamCreateDraftDocument param = new ParamCreateDraftDocument();
			param.setParent(pareNodeId);
			param.setDocument(toDocumentNode(document, true));
			param.setRetrieveNode(Boolean.TRUE.toString());
			request.setParam(param);
			createDraftDocument.setCreateDraftDocumentRequest(request);
			JerseyResponse resposta = enviarPeticioRest(
					metode,
					createDraftDocument);
			if (resposta.getStatus() == 200) {
				CreateDraftDocumentResult result = mapper.readValue(
						resposta.getJson(),
						CreateDraftDocumentResult.class);
				DocumentNode documentNode = result.getCreateDraftDocumentResult().getResParam();
				ArxiuDocument documentCreat = new ArxiuDocument(
						documentNode.getId(),
						documentNode.getName(),
						generarContentPerRetornar(documentNode.getBinaryContents()),
						generarMetadadesPerRetornar(documentNode.getMetadataCollection()),
						generarAspectesPerRetornar(documentNode.getAspects()));
				return documentCreat;
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
	public ArxiuDocument documentFinalCreate(
			String pareNodeId,
			ArxiuDocument document,
			ArxiuHeader capsalera) {
		String metode = ArxiuMethods.DOCUMENT_CREATE;
		try {
			CreateDocument createDocument = new CreateDocument();
			Request<ParamCreateDocument> request = new Request<ParamCreateDocument>();
			request.setServiceHeader(generarServiceHeader(capsalera));
			ParamCreateDocument param = new ParamCreateDocument();
			param.setParent(pareNodeId);
			param.setDocument(toDocumentNode(document, true));
			param.setRetrieveNode(Boolean.TRUE.toString());
			request.setParam(param);
			createDocument.setCreateDocumentRequest(request);
			JerseyResponse resposta = enviarPeticioRest(
					metode,
					createDocument);
			if (resposta.getStatus() == 200) {
				CreateDraftDocumentResult result = mapper.readValue(
						resposta.getJson(),
						CreateDraftDocumentResult.class);
				DocumentNode documentNode = result.getCreateDraftDocumentResult().getResParam();
				ArxiuDocument documentCreat = new ArxiuDocument(
						documentNode.getId(),
						documentNode.getName(),
						generarContentPerRetornar(documentNode.getBinaryContents()),
						generarMetadadesPerRetornar(documentNode.getMetadataCollection()),
						generarAspectesPerRetornar(documentNode.getAspects()));
				return documentCreat;
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
			ArxiuDocument document,
			ArxiuHeader capsalera) {
		String metode = ArxiuMethods.DOCUMENT_SET;
		try {
			SetDocument setDocument = new SetDocument();
			Request<ParamSetDocument> request = new Request<ParamSetDocument>();
			request.setServiceHeader(generarServiceHeader(capsalera));
			ParamSetDocument param = new ParamSetDocument();
			param.setDocument(toDocumentNode(document, false));
			request.setParam(param);
			setDocument.setSetDocumentRequest(request);
			JerseyResponse resposta = enviarPeticioRest(
					metode,
					setDocument);
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
		String metode = ArxiuMethods.DOCUMENT_REMOVE;
		try {
			RemoveDocument removeDocument = new RemoveDocument();
			Request<ParamNodeId> request = new Request<ParamNodeId>();
			request.setServiceHeader(generarServiceHeader(capsalera));
			ParamNodeId param = new ParamNodeId();
			param.setNodeId(nodeId);
			request.setParam(param);
			removeDocument.setRemoveDocumentRequest(request);
			JerseyResponse resposta = enviarPeticioRest(
					metode,
					removeDocument);
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
		String metode = ArxiuMethods.DOCUMENT_GET;
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
			if (resposta.getStatus() == 200) {
				GetDocumentResult result = mapper.readValue(
						resposta.getJson(),
						GetDocumentResult.class);
				DocumentNode documentNode = result.getGetDocumentResult().getResParam();
				ArxiuDocument documentObtingut = new ArxiuDocument(
						documentNode.getId(),
						documentNode.getName(),
						generarContentPerRetornar(documentNode.getBinaryContents()),
						generarMetadadesPerRetornar(documentNode.getMetadataCollection()),
						generarAspectesPerRetornar(documentNode.getAspects()));
				return documentObtingut;
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
	public List<ArxiuContentVersion> documentVersionList(
			String nodeId,
			ArxiuHeader capsalera) {
		String metode = ArxiuMethods.DOCUMENT_GET_VERSION;
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
			if (resposta.getStatus() == 200) {
				GetDocVersionListResult result = mapper.readValue(
						resposta.getJson(),
						GetDocVersionListResult.class);
				List<ArxiuContentVersion> versions = new ArrayList<ArxiuContentVersion>();
				List<VersionNode> vns = result.getGetDocVersionListResult().getResParam();
				for (VersionNode vn: vns) {
					ArxiuContentVersion versio = new ArxiuContentVersion();
					versio.setId(vn.getId());
					versio.setData(getMetadataValueAsDate(vn.getDate()));
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
			ArxiuHeader capsalera) {
		String metode = ArxiuMethods.DOCUMENT_GENERATE_CSV;
		try {
			GenerateDocCSV generateDocCSV = new GenerateDocCSV();
			Request<Object> request = new Request<Object>();
			request.setServiceHeader(generarServiceHeader(capsalera));
			generateDocCSV.setGenerateDocCSVRequest(request);
			JerseyResponse resposta = enviarPeticioRest(
					metode,
					generateDocCSV);
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
			ArxiuDocument document,
			ArxiuHeader capsalera) {
		String metode = ArxiuMethods.DOCUMENT_SET_FINAL;
		try {
			SetFinalDocument setFinalDocument = new SetFinalDocument();
			Request<ParamSetDocument> request = new Request<ParamSetDocument>();
			ParamSetDocument param = new ParamSetDocument();
			param.setDocument(toDocumentNode(document, false));
			request.setParam(param);
			request.setServiceHeader(generarServiceHeader(capsalera));
			setFinalDocument.setSetFinalDocumentRequest(request);
			JerseyResponse resposta = enviarPeticioRest(
					metode,
					setFinalDocument);
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
		String metode = ArxiuMethods.DOCUMENT_GET_ENIDOC;
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
			String targetNodeId,
			ArxiuHeader capsalera) {
		String metode = ArxiuMethods.DOCUMENT_COPY;
		try {
			CopyDocument copyDocument = new CopyDocument();
			Request<ParamNodeID_TargetParent> request = new Request<ParamNodeID_TargetParent>();
			ParamNodeID_TargetParent param = new ParamNodeID_TargetParent();
			param.setNodeId(nodeId);
			param.setTargetParent(targetNodeId);
			request.setParam(param);
			request.setServiceHeader(generarServiceHeader(capsalera));
			copyDocument.setCopyDocumentRequest(request);
			JerseyResponse resposta = enviarPeticioRest(
					metode,
					copyDocument);
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
			String targetNodeId,
			ArxiuHeader capsalera) {
		String metode = ArxiuMethods.DOCUMENT_MOVE;
		try {
			MoveDocument moveDocument = new MoveDocument();
			Request<ParamNodeID_TargetParent> request = new Request<ParamNodeID_TargetParent>();
			ParamNodeID_TargetParent param = new ParamNodeID_TargetParent();
			param.setNodeId(nodeId);
			param.setTargetParent(targetNodeId);
			request.setParam(param);
			request.setServiceHeader(generarServiceHeader(capsalera));
			moveDocument.setMoveDocumentRequest(request);
			JerseyResponse resposta = enviarPeticioRest(
					metode,
					moveDocument);
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
	public String documentDispatch(
			String nodeId,
			String targetNodeId,
			String targetNodeType,
			String classificationSerie,
			String classificationType,
			ArxiuHeader capsalera) {
		String metode = ArxiuMethods.DOCUMENT_DISPATCH;
		try {
			DispatchDocument dispatchDocument = new DispatchDocument();
			Request<ParamDispatchDocument> request = new Request<ParamDispatchDocument>();
			ParamDispatchDocument param = new ParamDispatchDocument();
			param.setSourceNodeId(nodeId);
			TargetNode targetNode = new TargetNode();
			targetNode.setId(targetNodeId);
			targetNode.setTargetType(targetNodeType);
			DocClassification docClassification = new DocClassification();
			docClassification.setSerie(classificationSerie);
			docClassification.setType(classificationType);
			targetNode.setDocClassification(docClassification);
			param.setTargetNode(targetNode);
			request.setParam(param);
			request.setServiceHeader(generarServiceHeader(capsalera));
			dispatchDocument.setDispatchDocumentRequest(request);
			JerseyResponse resposta = enviarPeticioRest(
					metode,
					dispatchDocument);
			if (resposta.getStatus() == 200) {
				DispatchDocumentResult result = mapper.readValue(
						resposta.getJson(),
						DispatchDocumentResult.class);
				return result.getDispatchDocumentResult().getResParam();
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

	/*@Override
	public void documentValidate(
			String nodeId,
			String csv,
			ArxiuHeader capsalera) {
		String metode = ArxiuMethods.DOCUMENT_VALIDATE;
		try {
			ValidateDocument validateDocument = new ValidateDocument();
			Request<ParamValidateDoc> request = new Request<ParamValidateDoc>();
			ParamValidateDoc param = new ParamValidateDoc();
			DocumentId documentId = new DocumentId();
			documentId.setNodeId(nodeId);
			documentId.setCsv(csv);
			param.setDocumentId(documentId);
			request.setParam(param);
			request.setServiceHeader(generarServiceHeader(capsalera));
			validateDocument.setValidateDocRequest(request);
			JerseyResponse resposta = enviarPeticioRest(
					metode,
					validateDocument);
			if (resposta.getStatus() == 200) {
				ValidateDocResult result = mapper.readValue(
						resposta.getJson(),
						ValidateDocResult.class);
				DocumentoYFirmas documentAmbFirmes = result.getValidateDocResult().getResParam();
				DSSResult dssResult = result.getValidateDocResult().getResult();
				
				return result.getValidateDocResult().getResParam();
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
	}*/

	@Override
	public void documentLink(
			String nodeId,
			String targetNodeId,
			ArxiuHeader capsalera) {
		String metode = ArxiuMethods.DOCUMENT_LINK;
		try {
			LinkDocument linkDocument = new LinkDocument();
			Request<ParamNodeID_TargetParent> request = new Request<ParamNodeID_TargetParent>();
			ParamNodeID_TargetParent param = new ParamNodeID_TargetParent();
			param.setNodeId(nodeId);
			param.setTargetParent(targetNodeId);
			request.setParam(param);
			request.setServiceHeader(generarServiceHeader(capsalera));
			linkDocument.setLinkDocumentRequest(request);
			JerseyResponse resposta = enviarPeticioRest(
					metode,
					linkDocument);
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
	public void documentLock(
			String nodeId,
			ArxiuHeader capsalera) {
		String metode = ArxiuMethods.DOCUMENT_LOCK;
		try {
			LockDocument lockDocument = new LockDocument();
			Request<ParamNodeId> request = new Request<ParamNodeId>();
			ParamNodeId param = new ParamNodeId();
			param.setNodeId(nodeId);
			request.setParam(param);
			request.setServiceHeader(generarServiceHeader(capsalera));
			lockDocument.setLockDocumentRequest(request);
			JerseyResponse resposta = enviarPeticioRest(
					metode,
					lockDocument);
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
	public void documentUnlock(
			String nodeId,
			ArxiuHeader capsalera) {
		String metode = ArxiuMethods.DOCUMENT_UNLOCK;
		try {
			UnlockDocument unlockDocument = new UnlockDocument();
			Request<ParamNodeId> request = new Request<ParamNodeId>();
			ParamNodeId param = new ParamNodeId();
			param.setNodeId(nodeId);
			request.setParam(param);
			request.setServiceHeader(generarServiceHeader(capsalera));
			unlockDocument.setUnlockDocumentRequest(request);
			JerseyResponse resposta = enviarPeticioRest(
					metode,
					unlockDocument);
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
	public void documentPermissionsGrant(
			List<String> nodeIds,
			List<String> authorities,
			ArxiuPermissionEnum permis,
			ArxiuHeader capsalera) {
		String metode = ArxiuMethods.DOCUMENT_GRANT_PERMISOS;
		try {
			GrantPermissionsOnDocs grantPermissionsDocs = new GrantPermissionsOnDocs();
			Request<ParamGrantPermissions> request = new Request<ParamGrantPermissions>();
			ParamGrantPermissions param = new ParamGrantPermissions();
			param.setNodeIds(nodeIds);
			param.setAuthorities(authorities);
			if (permis != null) {
				switch (permis) {
				case READ:
					param.setPermission(Permisos.READ);
					break;
				case WRITE:
					param.setPermission(Permisos.WRITE);
					break;
				}
			}
			request.setParam(param);
			request.setServiceHeader(generarServiceHeader(capsalera));
			grantPermissionsDocs.setGrantPermissionsOnDocsRequest(request);
			JerseyResponse resposta = enviarPeticioRest(
					metode,
					grantPermissionsDocs);
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
	public void documentPermissionsCancel(
			List<String> nodeIds,
			List<String> authorities,
			ArxiuHeader capsalera) {
		String metode = ArxiuMethods.FOLDER_CANCEL_PERMISOS;
		try {
			CancelPermissionsOnDocs cancelPermissionsDocs = new CancelPermissionsOnDocs();
			Request<ParamCancelPermissions> request = new Request<ParamCancelPermissions>();
			ParamCancelPermissions param = new ParamCancelPermissions();
			param.setNodeIds(nodeIds);
			param.setAuthorities(authorities);
			request.setParam(param);
			request.setServiceHeader(generarServiceHeader(capsalera));
			cancelPermissionsDocs.setCancelPermissionsOnDocsRequest(request);
			JerseyResponse resposta = enviarPeticioRest(
					metode,
					cancelPermissionsDocs);
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
	public ArxiuFolder folderCreate(
			String pareNodeId,
			ArxiuFolder folder,
			ArxiuHeader capsalera) {
		String metode = ArxiuMethods.FOLDER_CREATE;
		try {
			CreateFolder createFolder = new CreateFolder();
			Request<ParamCreateFolder> request = new Request<ParamCreateFolder>();
			request.setServiceHeader(generarServiceHeader(capsalera));
			ParamCreateFolder param = new ParamCreateFolder();
			param.setParent(pareNodeId);
			param.setFolder(toFolderNode(folder));
			param.setRetrieveNode(Boolean.TRUE.toString());
			request.setParam(param);
			createFolder.setCreateFolderRequest(request);
			JerseyResponse resposta = enviarPeticioRest(
					metode,
					createFolder);
			if (resposta.getStatus() == 200) {
				CreateFolderResult result = mapper.readValue(
						resposta.getJson(),
						CreateFolderResult.class);
				FolderNode folderNode = result.getCreateFolderResult().getResParam();
				return new ArxiuFolder(
						folderNode.getId(),
						folderNode.getName(),
						generarChildrenPerRetornar(folderNode.getChildObjects()));
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
			ArxiuFolder folder,
			ArxiuHeader capsalera) {
		String metode = ArxiuMethods.FOLDER_SET;
		try {
			SetFolder setFolder = new SetFolder();
			Request<ParamSetFolder> request = new Request<ParamSetFolder>();
			request.setServiceHeader(generarServiceHeader(capsalera));
			ParamSetFolder param = new ParamSetFolder();
			param.setFolder(toFolderNode(folder));
			request.setParam(param);
			setFolder.setSetFolderRequest(request);
			JerseyResponse resposta = enviarPeticioRest(
					metode,
					setFolder);
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
		String metode = ArxiuMethods.FOLDER_REMOVE;
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
		String metode = ArxiuMethods.FOLDER_GET;
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
			if (resposta.getStatus() == 200) {
				GetFolderResult result = mapper.readValue(
						resposta.getJson(),
						GetFolderResult.class);
				FolderNode folderNode = result.getGetFolderResult().getResParam();
				return new ArxiuFolder(
						folderNode.getId(),
						folderNode.getName(),
						generarChildrenPerRetornar(folderNode.getChildObjects()));
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
			String targetNodeId,
			ArxiuHeader capsalera) {
		String metode = ArxiuMethods.FOLDER_COPY;
		try {
			CopyFolder copyFolder = new CopyFolder();
			Request<ParamNodeID_TargetParent> request = new Request<ParamNodeID_TargetParent>();
			ParamNodeID_TargetParent paramTarget = new ParamNodeID_TargetParent();
			paramTarget.setNodeId(nodeId);
			paramTarget.setTargetParent(targetNodeId);
			request.setParam(paramTarget);
			request.setServiceHeader(generarServiceHeader(capsalera));
			copyFolder.setCopyDocumentFolder(request);
			JerseyResponse resposta = enviarPeticioRest(
					metode,
					copyFolder);
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
			String targetNodeId,
			ArxiuHeader capsalera) {
		String metode = ArxiuMethods.FOLDER_MOVE;
		try {
			MoveFolder moveFolder = new MoveFolder();
			Request<ParamNodeID_TargetParent> request = new Request<ParamNodeID_TargetParent>();
			ParamNodeID_TargetParent paramTarget = new ParamNodeID_TargetParent();
			paramTarget.setNodeId(nodeId);
			paramTarget.setTargetParent(targetNodeId);
			request.setParam(paramTarget);
			request.setServiceHeader(generarServiceHeader(capsalera));
			moveFolder.setMoveFolderRequest(request);
			JerseyResponse resposta = enviarPeticioRest(
					metode,
					moveFolder);
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
	public void folderLink(
			String nodeId,
			String targetNodeId,
			ArxiuHeader capsalera) {
		String metode = ArxiuMethods.FOLDER_LINK;
		try {
			LinkFolder linkFolder = new LinkFolder();
			Request<ParamNodeID_TargetParent> request = new Request<ParamNodeID_TargetParent>();
			ParamNodeID_TargetParent paramTarget = new ParamNodeID_TargetParent();
			paramTarget.setNodeId(nodeId);
			paramTarget.setTargetParent(targetNodeId);
			request.setParam(paramTarget);
			request.setServiceHeader(generarServiceHeader(capsalera));
			linkFolder.setLinkFolderRequest(request);
			JerseyResponse resposta = enviarPeticioRest(
					metode,
					linkFolder);
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
	public void folderLock(
			String nodeId,
			ArxiuHeader capsalera) {
		String metode = ArxiuMethods.FOLDER_LOCK;
		try {
			LockFolder lockFolder = new LockFolder();
			Request<ParamNodeId> request = new Request<ParamNodeId>();
			ParamNodeId paramNodeId = new ParamNodeId();
			paramNodeId.setNodeId(nodeId);
			request.setParam(paramNodeId);
			request.setServiceHeader(generarServiceHeader(capsalera));
			lockFolder.setLockFolderRequest(request);
			JerseyResponse resposta = enviarPeticioRest(
					metode,
					lockFolder);
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
	public void folderUnlock(
			String nodeId,
			ArxiuHeader capsalera) {
		String metode = ArxiuMethods.FOLDER_UNLOCK;
		try {
			UnlockFolder unlockFolder = new UnlockFolder();
			Request<ParamNodeId> request = new Request<ParamNodeId>();
			ParamNodeId paramNodeId = new ParamNodeId();
			paramNodeId.setNodeId(nodeId);
			request.setParam(paramNodeId);
			request.setServiceHeader(generarServiceHeader(capsalera));
			unlockFolder.setUnlockFolderRequest(request);
			JerseyResponse resposta = enviarPeticioRest(
					metode,
					unlockFolder);
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
	public void folderPermissionsGrant(
			List<String> nodeIds,
			List<String> authorities,
			ArxiuPermissionEnum permis,
			ArxiuHeader capsalera) {
		String metode = ArxiuMethods.FOLDER_GRANT_PERMISOS;
		try {
			GrantPermissionsOnFolders grantPermissionsFolders = new GrantPermissionsOnFolders();
			Request<ParamGrantPermissions> request = new Request<ParamGrantPermissions>();
			ParamGrantPermissions paramGrantPermissions = new ParamGrantPermissions();
			paramGrantPermissions.setNodeIds(nodeIds);
			paramGrantPermissions.setAuthorities(authorities);
			if (permis != null) {
				switch (permis) {
				case READ:
					paramGrantPermissions.setPermission(Permisos.READ);
					break;
				case WRITE:
					paramGrantPermissions.setPermission(Permisos.WRITE);
					break;
				}
			}
			request.setParam(paramGrantPermissions);
			request.setServiceHeader(generarServiceHeader(capsalera));
			grantPermissionsFolders.setGrantPermissionsOnFoldersRequest(request);
			JerseyResponse resposta = enviarPeticioRest(
					metode,
					grantPermissionsFolders);
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
	public void folderPermissionsCancel(
			List<String> nodeIds,
			List<String> authorities,
			ArxiuHeader capsalera) {
		String metode = ArxiuMethods.FOLDER_CANCEL_PERMISOS;
		try {
			CancelPermissionsOnFolders cancelPermissionsFolders = new CancelPermissionsOnFolders();
			Request<ParamCancelPermissions> request = new Request<ParamCancelPermissions>();
			ParamCancelPermissions paramCancelPermissions = new ParamCancelPermissions();
			paramCancelPermissions.setNodeIds(nodeIds);
			paramCancelPermissions.setAuthorities(authorities);
			request.setParam(paramCancelPermissions);
			request.setServiceHeader(generarServiceHeader(capsalera));
			cancelPermissionsFolders.setCancelPermissionsOnFoldersRequest(request);
			JerseyResponse resposta = enviarPeticioRest(
					metode,
					cancelPermissionsFolders);
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

	public String getLastJsonRequest() {
		return lastJsonRequest;
	}
	public String getLastJsonResponse() {
		return lastJsonResponse;
	}



	private FileNode toFileNode(
			ArxiuFile file,
			boolean creacio) {
		FileNode node = new FileNode();
		node.setId(file.getNodeId());
		node.setType(TiposObjetoSGD.EXPEDIENTE);
		node.setName(file.getName());
		node.setMetadataCollection(
				generarMetadataPerArxiu(file.getMetadata()));
		node.setAspects(
				generarAspectosPerArxiu(
						file.getAspects(),
						creacio));
		node.setChildObjects(
				generarSummaryInfoNodePerArxiu(
						file.getChildren()));
		return node;
	}

	private DocumentNode toDocumentNode(
			ArxiuDocument document,
			boolean creacio) {
		DocumentNode node = new DocumentNode();
		node.setId(document.getNodeId());
		node.setType(TiposObjetoSGD.DOCUMENTO);
		node.setName(document.getName());
		node.setMetadataCollection(
				generarMetadataPerArxiu(document.getMetadata()));
		node.setAspects(
				generarAspectosPerArxiu(
						document.getAspects(),
						creacio));
		List<Content> binaryContents = null;
		if (document.getContents() != null) {
			binaryContents = new ArrayList<Content>();
			for (ArxiuDocumentContent content: document.getContents()) {
				Content binaryContent = new Content();
				if (content.getType() != null) {
					switch (content.getType()) {
					case CONTENT:
						binaryContent.setBinaryType(TiposContenidosBinarios.CONTENT);
						break;
					case SIGNATURE:
						binaryContent.setBinaryType(TiposContenidosBinarios.SIGNATURE);
						break;
					case SIGNATURE_VALCERT:
						binaryContent.setBinaryType(TiposContenidosBinarios.VALCERT_SIGNATURE);
						break;
					case MIGRATION_SIGNATURE:
						binaryContent.setBinaryType(TiposContenidosBinarios.MIGRATION_SIGNATURE);
						break;
					case MIGRATION_ZIP:
						binaryContent.setBinaryType(TiposContenidosBinarios.MIGRATION_ZIP);
						break;
					}
				}
				binaryContent.setEncoding(content.getEncoding());
				binaryContent.setMimetype(content.getContentType());
				binaryContent.setContent(content.getContentBase64());
				binaryContents.add(binaryContent);
			}
		}
		node.setBinaryContents(binaryContents);
		return node;
	}

	private FolderNode toFolderNode(
			ArxiuFolder folder) {
		FolderNode node = new FolderNode();
		node.setId(folder.getNodeId());
		node.setType(TiposObjetoSGD.DIRECTORIO);
		node.setName(folder.getName());
		return node;
	}

	private ServiceHeader generarServiceHeader(ArxiuHeader capsalera) {
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
		serviceHeader.setServiceVersion(SERVEI_VERSIO);
		ServiceSecurityInfo securityInfo = new ServiceSecurityInfo();
		securityInfo.setUser(usuariSgd);
		securityInfo.setPassword(contrasenyaSgd);
		serviceHeader.setSecurityInfo(securityInfo);
		return serviceHeader;
	}

	private List<Metadata> generarMetadataPerArxiu(
			Map<String, Object> metadadesMap){
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
	private List<Aspectos> generarAspectosPerArxiu(
			List<String> aspectesList,
			boolean perCreacio) {
		List<Aspectos> aspectes = null;
		if (aspectesList != null) {
			aspectes = new ArrayList<Aspectos>();
			for (String aspecte: aspectesList) {
				aspectes.add(Aspectos.valueOf(aspecte));
			}
		}
		if (perCreacio) {
			if (aspectes == null) {
				aspectes = new ArrayList<Aspectos>();
			}
			if (aspectes.isEmpty()) {
				if (perCreacio) {
					aspectes.add(Aspectos.INTEROPERABLE);
					aspectes.add(Aspectos.TRANSFERIBLE);
				}
			}
		}
		return aspectes;
	}
	private List<SummaryInfoNode> generarSummaryInfoNodePerArxiu(
			List<ArxiuContentChild> children) {
		List<SummaryInfoNode> summaryInfoNodes = null;
		if (children != null){
			summaryInfoNodes = new ArrayList<SummaryInfoNode>();
			for (ArxiuContentChild child: children){
				SummaryInfoNode sin = new SummaryInfoNode();
				sin.setId(child.getNodeId());
				sin.setName(child.getName());
				if (child.getTipus() != null) {
					switch (child.getTipus()) {
					case EXPEDIENT:
						sin.setType(TiposObjetoSGD.EXPEDIENTE);
						break;
					case DOCUMENT:
						sin.setType(TiposObjetoSGD.DOCUMENTO);
						break;
					case CARPETA:
						sin.setType(TiposObjetoSGD.DIRECTORIO);
						break;
					case DOCUMENT_MIGRAT:
						sin.setType(TiposObjetoSGD.DOCUMENTO_MIGRADO);
						break;
					}
				}
				summaryInfoNodes.add(sin);
			}
		}
		return summaryInfoNodes;
	}

	private Map<String, Object> generarMetadadesPerRetornar(
			List<Metadata> metadataList){
		Map<String, Object> metadades = null;
		if (metadataList != null) {
			metadades = new HashMap<String, Object>();
			for (Metadata metadata: metadataList) {
				metadades.put(
						metadata.getQname(),
						metadata.getValue());
			}
		}
		return metadades;
	}
	private List<String> generarAspectesPerRetornar(
			List<Aspectos> aspectosList){
		List<String> aspectes = null;
		if (aspectosList != null) {
			aspectes = new ArrayList<String>();
			for (Aspectos aspecto: aspectosList) {
				aspectes.add(aspecto.getValue());
			}
		}
		return aspectes;
	}
	private List<ArxiuContentChild> generarChildrenPerRetornar(
			List<SummaryInfoNode> summaryInfoNodeList){
		List<ArxiuContentChild> children = null;
		if (summaryInfoNodeList != null) {
			children = new ArrayList<ArxiuContentChild>();
			for (SummaryInfoNode node: summaryInfoNodeList) {
				ArxiuContentChild child = new ArxiuContentChild();
				child.setNodeId(node.getId());
				if (node.getType() != null) {
					switch (node.getType()) {
					case EXPEDIENTE:
						child.setTipus(ArxiuContentChildTypeEnum.EXPEDIENT);
						break;
					case DOCUMENTO:
						child.setTipus(ArxiuContentChildTypeEnum.DOCUMENT);
						break;
					case DIRECTORIO:
						child.setTipus(ArxiuContentChildTypeEnum.CARPETA);
						break;
					case DOCUMENTO_MIGRADO:
						child.setTipus(ArxiuContentChildTypeEnum.DOCUMENT_MIGRAT);
						break;
					}
				}
				child.setName(node.getName());
				children.add(child);
			}
		}
		return children;
	}
	private List<ArxiuDocumentContent> generarContentPerRetornar(
			List<Content> contentList){
		List<ArxiuDocumentContent> contents = null;
		if (contentList != null) {
			contents = new ArrayList<ArxiuDocumentContent>();
			for (Content node: contentList) {
				ArxiuDocumentContent content = new ArxiuDocumentContent();
				if (node.getBinaryType() != null) {
					switch (node.getBinaryType()) {
					case CONTENT:
						content.setType(ArxiuDocumentContentTypeEnum.CONTENT);
						break;
					case SIGNATURE:
						content.setType(ArxiuDocumentContentTypeEnum.SIGNATURE);
						break;
					case VALCERT_SIGNATURE:
						content.setType(ArxiuDocumentContentTypeEnum.SIGNATURE_VALCERT);
						break;
					case MIGRATION_SIGNATURE:
						content.setType(ArxiuDocumentContentTypeEnum.MIGRATION_SIGNATURE);
						break;
					case MIGRATION_ZIP:
						content.setType(ArxiuDocumentContentTypeEnum.MIGRATION_ZIP);
						break;
					}
				}
				content.setEncoding(node.getEncoding());
				content.setContentType(node.getMimetype());
				content.setContentBase64(node.getContent());
				contents.add(content);
			}
		}
		return contents;
	}

	private Date getMetadataValueAsDate(Object value) throws ParseException {
		if (value == null) {
			return null;
		}
		return parseDateIso8601((String)value);
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

	private JerseyResponse enviarPeticioRest(
			String metode,
			Object peticio) throws UniformInterfaceException, ClientHandlerException, JsonProcessingException {
		String urlAmbMetode = (url.endsWith("/")) ? url + "services/" + metode : url + "/services/" + metode;
		String body = mapper.writeValueAsString(peticio);
		logger.debug("Enviant petició HTTP a l'arxiu (" +
				"url=" + urlAmbMetode + ", " +
				"tipus=application/json, " +
				"body=" + body + ")");
		System.out.println(">>> Enviant petició HTTP a l'arxiu (" +
				"url=" + urlAmbMetode + ", " +
				"tipus=application/json, " +
				"body=" + body + ")");
		lastJsonRequest = body;
		ClientResponse response = jerseyClient.
				resource(urlAmbMetode).
				type("application/json").
				post(ClientResponse.class, body);
		String json = response.getEntity(String.class);
		logger.debug("Rebuda resposta HTTP de l'arxiu (" +
				"status=" + response.getStatus() + ", " +
				"body=" + json + ")");
		lastJsonResponse = json;
		return new JerseyResponse(
				json,
				response.getStatus());
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
