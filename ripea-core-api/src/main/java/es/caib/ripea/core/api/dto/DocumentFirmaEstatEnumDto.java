/**
 * 
 */
package es.caib.ripea.core.api.dto;


/**
 * Enumeració amb els possibles estats de la firma
 * d'un document.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public enum DocumentFirmaEstatEnumDto {
	SENSE_FIRMA,
	PFIRMA_PENDENT,
	PFIRMA_FIRMAT,
	PFIRMA_REBUTJAT,
	PFIRMA_CANCELAT,
	PFIRMA_CUSTODIAT,
	CUSTODIA_ERROR,
	APPLET_FIRMAT,
	APPLET_CUSTODIAT,
	APPLET_ERROR
}
