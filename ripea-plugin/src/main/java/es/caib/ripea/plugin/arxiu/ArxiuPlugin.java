package es.caib.ripea.plugin.arxiu;

import java.io.InputStream;
import java.util.Date;
import java.util.List;

import es.caib.ripea.plugin.SistemaExternException;

/**
 * Plugin per a la integració amb un arxiu digital.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public interface ArxiuPlugin {

	/**
	 * Crea un nou expedient a l'arxiu digital.
	 * 
	 * @param titol
	 * @param origen
	 * @param dataObertura
	 * @param classificacio
	 * @param estat
	 * @param organs
	 * @param interessats
	 * @param serieDocumental
	 * @param capsalera
	 * @return
	 * @throws SistemaExternException
	 */
	public ArxiuExpedient expedientCrear(
			String titol,
			ArxiuOrigenContingut origen,
			Date dataObertura,
			String classificacio,
			ArxiuExpedientEstat estat,
			List<String> organs,
			List<String> interessats,
			String serieDocumental,
			ArxiuCapsalera capsalera) throws SistemaExternException;

	/**
	 * Modifica un expedient ja existent a l'arxiu digital.
	 * 
	 * @param nodeId
	 * @param titol
	 * @param origen
	 * @param dataObertura
	 * @param classificacio
	 * @param estat
	 * @param organs
	 * @param interessats
	 * @param serieDocumental
	 * @param capsalera
	 * @throws SistemaExternException
	 */
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
			ArxiuCapsalera capsalera) throws SistemaExternException;

	/**
	 * Esborra un expedient previament creat.
	 * 
	 * @param nodeId
	 * @param capsalera
	 * @throws SistemaExternException
	 */
	public void expedientEsborrar(
			String nodeId,
			ArxiuCapsalera capsalera) throws SistemaExternException;

	/**
	 * Consulta un expedient previament creat donat el seu id.
	 * 
	 * @param nodeId
	 * @param capsalera
	 * @throws SistemaExternException
	 */
	public ArxiuExpedient expedientObtenirAmbId(
			String nodeId,
			ArxiuCapsalera capsalera) throws SistemaExternException;

	/**
	 * Crea un nou esborrany de document a l'arxiu digital.
	 * 
	 * @param titol
	 * @param origen
	 * @param dataCaptura
	 * @param estatElaboracio
	 * @param tipusDocumental
	 * @param extensio
	 * @param organs
	 * @param serieDocumental
	 * @param contingut
	 * @param tipusMime
	 * @param pareNodeId
	 * @param capsalera
	 * @return
	 * @throws SistemaExternException
	 */
	public ArxiuDocument documentEsborranyCrear(
			String titol,
			ArxiuOrigenContingut origen,
			Date dataCaptura,
			ArxiuEstatElaboracio estatElaboracio,
			ArxiuTipusDocumental tipusDocumental,
			ArxiuFormatNom formatNom,
			ArxiuFormatExtensio formatExtensio,
			List<String> organs,
			String serieDocumental,
			InputStream contingut,
			String tipusMime,
			String pareNodeId,
			ArxiuCapsalera capsalera) throws SistemaExternException;

	/**
	 * Modifica un document ja existent a l'arxiu digital.
	 * 
	 * @param nodeId
	 * @param titol
	 * @param origen
	 * @param dataCaptura
	 * @param estatElaboracio
	 * @param documentTipus
	 * @param extensio
	 * @param organs
	 * @param serieDocumental
	 * @param contingut
	 * @param tipusMime
	 * @param expedientNodeId
	 * @param capsalera
	 * @throws SistemaExternException
	 */
	public void documentModificar(
			String nodeId,
			String titol,
			ArxiuOrigenContingut origen,
			Date dataCaptura,
			ArxiuEstatElaboracio estatElaboracio,
			ArxiuTipusDocumental tipusDocumental,
			ArxiuFormatNom formatNom,
			ArxiuFormatExtensio formatExtensio,
			List<String> organs,
			String serieDocumental,
			InputStream contingut,
			String tipusMime,
			ArxiuCapsalera capsalera) throws SistemaExternException;

	/**
	 * Esborra un document previament creat.
	 * 
	 * @param nodeId
	 * @param capsalera
	 * @throws SistemaExternException
	 */
	public void documentEsborrar(
			String nodeId,
			ArxiuCapsalera capsalera) throws SistemaExternException;

	/**
	 * Consulta un document previament creat donat el seu id.
	 * 
	 * @param nodeId
	 * @param ambContingut
	 * @param capsalera
	 * @throws SistemaExternException
	 */
	public ArxiuDocument documentObtenirAmbId(
			String nodeId,
			boolean ambContingut,
			ArxiuCapsalera capsalera) throws SistemaExternException;

	/**
	 * Genera el CSV associat a un document donat el seu id.
	 * 
	 * @param nodeId
	 * @param capsalera
	 * @return
	 * @throws SistemaExternException
	 */
	public String documentGenerarCsv(
			String nodeId,
			ArxiuCapsalera capsalera) throws SistemaExternException;

	/**
	 * Consulta un document previament creat donat el seu CSV.
	 * 
	 * @param csv
	 * @param ambContingut
	 * @param capsalera
	 * @throws SistemaExternException
	 */
	public ArxiuDocument documentObtenirAmbCsv(
			String csv,
			boolean ambContingut,
			ArxiuCapsalera capsalera) throws SistemaExternException;

	/**
	 * Consulta les versions disponibles d'un document
	 * 
	 * @param nodeId
	 * @param capsalera
	 * @return
	 * @throws SistemaExternException
	 */
	public List<ArxiuDocumentVersio> documentObtenirVersions(
			String nodeId,
			ArxiuCapsalera capsalera) throws SistemaExternException;

	/**
	 * Crea una nova carpeta a l'arxiu digital.
	 * 
	 * @param nom
	 * @param pareNodeId
	 * @param capsalera
	 * @return
	 * @throws SistemaExternException
	 */
	public ArxiuCarpeta carpetaCrear(
			String nom,
			String pareNodeId,
			ArxiuCapsalera capsalera) throws SistemaExternException;

	/**
	 * Modifica una carpeta ja existent a l'arxiu digital.
	 * 
	 * @param nodeId
	 * @param nom
	 * @param capsalera
	 * @throws SistemaExternException
	 */
	public void carpetaModificar(
			String nodeId,
			String nom,
			ArxiuCapsalera capsalera) throws SistemaExternException;

	/**
	 * Esborra una carpeta previament creada.
	 * 
	 * @param nodeId
	 * @param capsalera
	 * @throws SistemaExternException
	 */
	public void carpetaEsborrar(
			String nodeId,
			ArxiuCapsalera capsalera) throws SistemaExternException;

	/**
	 * Consulta una carpeta previament creada donat el seu id.
	 * 
	 * @param nodeId
	 * @param csv
	 * @param capsalera
	 * @throws SistemaExternException
	 */
	public ArxiuCarpeta carpetaObtenirAmbId(
			String nodeId,
			ArxiuCapsalera capsalera) throws SistemaExternException;

}
