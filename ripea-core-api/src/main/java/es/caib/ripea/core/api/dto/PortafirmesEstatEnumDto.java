/**
 * 
 */
package es.caib.ripea.core.api.dto;


/**
 * Enumeració amb els possibles estats d'un enviament a
 * portafirmes.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public enum PortafirmesEstatEnumDto {
	PENDENT,
	FIRMAT,
	REBUTJAT,
	CANCELAT,
	ERROR
}
