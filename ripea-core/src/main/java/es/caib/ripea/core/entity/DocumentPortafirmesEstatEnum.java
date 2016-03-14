/**
 * 
 */
package es.caib.ripea.core.entity;


/**
 * Enumeració amb els possibles estats d'un enviament a
 * portafirmes.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public enum DocumentPortafirmesEstatEnum {
	PENDENT,
	FIRMAT,
	REBUTJAT,
	CANCELAT,
	ERROR_CUSTODIA
}
