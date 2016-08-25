/**
 * 
 */
package es.caib.ripea.core.api.dto;


/**
 * Enumeració amb els possibles tipus d'accions de log.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public enum LogTipusEnumDto {
	CREACIO,
	CONSULTA,
	MODIFICACIO,
	ELIMINACIO,
	RECUPERACIO,
	ELIMINACIODEF,
	RESERVA,
	ALLIBERACIO,
	COPIA,
	MOVIMENT,
	ENVIAMENT,
	PROCESSAMENT,
	TANCAMENT,
	REOBERTURA,
	ACUMULACIO,
	DISGREGACIO,
	APPLET_FIRMA,
	PFIRMA_ENVIAMENT,
	PFIRMA_CANCELACIO,
	PFIRMA_FIRMA,
	PFIRMA_REBUIG,
	CUSTODIA
//	,
//	RELACIO,	// Relació d'un expedient amb un altre
//	RELACIO_ESBORRAR
}
