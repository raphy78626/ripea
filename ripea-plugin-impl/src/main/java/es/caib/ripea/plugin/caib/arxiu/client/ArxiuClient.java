/**
 * 
 */
package es.caib.ripea.plugin.caib.arxiu.client;

import java.util.List;

/**
 * Interfície del client per a accedir a la funcionalitat de
 * l'arxiu digital.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public interface ArxiuClient {

	public ArxiuFile fileCreate(
			ArxiuFile file,
			ArxiuHeader capsalera) throws ArxiuException;
	public void fileUpdate(
			ArxiuFile file,
			ArxiuHeader capsalera) throws ArxiuException;
	public void fileDelete(
			String nodeId,
			ArxiuHeader capsalera) throws ArxiuException;
	public ArxiuFile fileGet(
			String nodeId,
			ArxiuHeader capsalera) throws ArxiuException;
	public List<ArxiuContentVersion> fileVersionList(
			String nodeId,
			ArxiuHeader capsalera) throws ArxiuException;
	public void fileClose(
			String nodeId,
			ArxiuHeader capsalera) throws ArxiuException;
	public void fileReopen(
			String nodeId,
			ArxiuHeader capsalera) throws ArxiuException;
	public String fileExport(
			String nodeId,
			ArxiuHeader capsalera) throws ArxiuException;
	public String fileIndexGenerate(
			String nodeId,
			ArxiuHeader capsalera) throws ArxiuException;
	public ArxiuFile fileChildCreate(
			String pareNodeId,
			ArxiuFile file,
			ArxiuHeader capsalera);
	public void fileChildMove(
			String nodeId,
			String nodeDestiId,
			ArxiuHeader capsalera);
	/*public void fileSearch();
	public void fileEasySearch();*/
	public void fileLink(
			String nodeId,
			String nodeDestiId,
			ArxiuHeader capsalera) throws ArxiuException;
	public void fileLock(
			String nodeId,
			ArxiuHeader capsalera) throws ArxiuException;
	public void fileUnlock(
			String nodeId,
			ArxiuHeader capsalera) throws ArxiuException;
	public void filePermissionsGrant(
			List<String> nodeIds,
			List<String> authorities,
			ArxiuPermissionEnum permis,
			ArxiuHeader capsalera) throws ArxiuException;
	public void filePermissionsCancel(
			List<String> nodeIds,
			List<String> authorities,
			ArxiuHeader capsalera) throws ArxiuException;

	public ArxiuDocument documentDraftCreate(
			String pareNodeId,
			ArxiuDocument document,
			ArxiuHeader capsalera) throws ArxiuException;
	public ArxiuDocument documentFinalCreate(
			String pareNodeId,
			ArxiuDocument document,
			ArxiuHeader capsalera) throws ArxiuException;
	public void documentUpdate(
			ArxiuDocument document,
			ArxiuHeader capsalera) throws ArxiuException;
	public void documentDelete(
			String nodeId,
			ArxiuHeader capsalera) throws ArxiuException;
	public ArxiuDocument documentGet(
			String nodeId,
			String csv,
			boolean ambContingut,
			ArxiuHeader capsalera) throws ArxiuException;
	public List<ArxiuContentVersion> documentVersionList(
			String nodeId,
			ArxiuHeader capsalera) throws ArxiuException;
	public String documentCsvGenerate(
			ArxiuHeader capsalera) throws ArxiuException;
	public void documentFinalSet(
			ArxiuDocument document,
			ArxiuHeader capsalera) throws ArxiuException;
	public String documentEniGet(
			String nodeId,
			ArxiuHeader capsalera) throws ArxiuException;
	public void documentCopy(
			String nodeId,
			String targetNodeId,
			ArxiuHeader capsalera) throws ArxiuException;
	public void documentMove(
			String nodeId,
			String targetNodeId,
			ArxiuHeader capsalera) throws ArxiuException;
	public String documentDispatch(
			String nodeId,
			String targetNodeId,
			String targetNodeType,
			String classificationSerie,
			String classificationType,
			ArxiuHeader capsalera);
	/*public void documentValidate(
			String nodeId,
			String csv,
			ArxiuHeader capsalera);
	public void documentSearch();
	public void documentEasySearch();*/
	public void documentLink(
			String nodeId,
			String nodeDestiId,
			ArxiuHeader capsalera) throws ArxiuException;
	public void documentLock(
			String nodeId,
			ArxiuHeader capsalera) throws ArxiuException;
	public void documentUnlock(
			String nodeId,
			ArxiuHeader capsalera) throws ArxiuException;
	public void documentPermissionsGrant(
			List<String> nodeIds,
			List<String> authorities,
			ArxiuPermissionEnum permis,
			ArxiuHeader capsalera) throws ArxiuException;
	public void documentPermissionsCancel(
			List<String> nodeIds,
			List<String> authorities,
			ArxiuHeader capsalera) throws ArxiuException;

	public ArxiuFolder folderCreate(
			String pareNodeId,
			ArxiuFolder folder,
			ArxiuHeader capsalera) throws ArxiuException;
	public void folderUpdate(
			ArxiuFolder folder,
			ArxiuHeader capsalera) throws ArxiuException;
	public void folderDelete(
			String nodeId,
			ArxiuHeader capsalera) throws ArxiuException;
	public ArxiuFolder folderGet(
			String nodeId,
			ArxiuHeader capsalera) throws ArxiuException;
	public void folderCopy(
			String nodeId,
			String nodeDestiId,
			ArxiuHeader capsalera) throws ArxiuException;
	public void folderMove(
			String nodeId,
			String nodeDestiId,
			ArxiuHeader capsalera) throws ArxiuException;
	public void folderLink(
			String nodeId,
			String nodeDestiId,
			ArxiuHeader capsalera) throws ArxiuException;
	public void folderLock(
			String nodeId,
			ArxiuHeader capsalera) throws ArxiuException;
	public void folderUnlock(
			String nodeId,
			ArxiuHeader capsalera) throws ArxiuException;
	public void folderPermissionsGrant(
			List<String> nodeIds,
			List<String> authorities,
			ArxiuPermissionEnum permis,
			ArxiuHeader capsalera) throws ArxiuException;
	public void folderPermissionsCancel(
			List<String> nodeIds,
			List<String> authorities,
			ArxiuHeader capsalera) throws ArxiuException;

}
