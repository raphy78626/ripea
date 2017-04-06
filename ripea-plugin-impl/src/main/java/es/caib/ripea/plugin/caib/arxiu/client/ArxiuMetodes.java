/**
 * 
 */
package es.caib.ripea.plugin.caib.arxiu.client;

/**
 * Mètodes disponibles a l'arxiu digital.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public abstract class ArxiuMetodes {

	public static final String FILE_CLOSE 				= "closeFile";
	public static final String FILE_CANCEL_PERMISOS 	= "cancelPermissionsOnFiles";
	public static final String FILE_CREATE 				= "createFile";
	public static final String FILE_EASY_SEARH			= "easyFileSearch";
	public static final String FILE_EXPORT 				= "exportFile";
	public static final String FILE_GENERAR_INDEX		= "generateFileIndex";
	public static final String FILE_GET 				= "getFile";
	public static final String FILE_GET_VERSION 		= "getFileVersionList";
	public static final String FILE_GRANT_PERMISOS 		= "grantPermissionsOnFiles";
	public static final String FILE_LINK 				= "linkFile";
	public static final String FILE_LOCK 				= "lockFile";
	public static final String FILE_REMOVE 				= "removeFile";
	public static final String FILE_REOPEN 				= "reopenFile";
	public static final String FILE_SET 				= "setFile";
	public static final String FILE_UNLOCK 				= "unlockFile";
	public static final String FILE_CREATE_CHILD 		= "createChildFile";
	public static final String FILE_MOVE_CHILD 			= "moveChildFile";

	public static final String DOCUMENT_CANCEL_PERMISOS	= "cancelPermissionsOnDocs";
	public static final String DOCUMENT_COPY 			= "copyDocument";
	public static final String DOCUMENT_CREATE 			= "createDocument";
	public static final String DOCUMENT_CREATE_DRAFT 	= "createDraftDocument";
	public static final String DOCUMENT_DISPATCH 		= "dispatchDocument";
	public static final String DOCUMENT_EASY_SEARH		= "easyDocumentSearch";
	public static final String DOCUMENT_GET				= "getDocument";
	public static final String DOCUMENT_GET_VERSION		= "getDocVersionList";
	public static final String DOCUMENT_GET_ENIDOC		= "getENIDocument";
	public static final String DOCUMENT_GRANT_PERMISOS 	= "grantPermissionsOnDocs";
	public static final String DOCUMENT_LINK			= "linkDocument";
	public static final String DOCUMENT_LOCK			= "lockDocument";
	public static final String DOCUMENT_MOVE			= "moveDocument";
	public static final String DOCUMENT_SEARCH			= "documentSearch";
	public static final String DOCUMENT_SET				= "setDocument";
	public static final String DOCUMENT_SET_FINAL		= "setFinalDocument";
	public static final String DOCUMENT_UNLOCK			= "unlockDocument";
	public static final String DOCUMENT_VALIDATE		= "validateDocument";
	public static final String DOCUMENT_REMOVE			= "removeDocument";
	public static final String DOCUMENT_GENERATE_CSV 	= "generateDocCSV";

	public static final String FOLDER_CANCEL_PERMISOS 	= "cancelPermissionsOnFolders";
	public static final String FOLDER_CREATE			= "createFolder";
	public static final String FOLDER_LINK 				= "linkFolder";
	public static final String FOLDER_LOCK 				= "lockFolder";
	public static final String FOLDER_COPY				= "copyFolder";
	public static final String FOLDER_MOVE 				= "moveFolder";
	public static final String FOLDER_SET 				= "setFolder";
	public static final String FOLDER_GET				= "getFolder";
	public static final String FOLDER_GRANT_PERMISOS	= "grantPermissionsOnFolders";
	public static final String FOLDER_REMOVE 			= "removeFolder";
	public static final String FOLDER_UNLOCK 			= "unlockFolder";

}
