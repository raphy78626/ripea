package es.caib.ripea.plugin.ciutada;

import java.util.List;

import es.caib.ripea.plugin.SistemaExternException;

/**
 * Plugin per a l'enviament d'avisos i notificacions al ciutadà.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public interface CiutadaPlugin {

	/**
	 * Crea un nou expedient a la zona personal del ciutadà. Es verifica si la
	 * zona personal està creada i la crea si no hi està.
	 * 
	 * @param expedientIdentificador
	 * @param unitatAdministrativa
	 * @param idioma
	 * @param descripcio
	 * @param destinatari
	 * @param representat
	 * @param bantelNumeroEntrada
	 * @param avisosHabilitats
	 * @param avisEmail
	 * @param avisMobil
	 * @throws SistemaExternException
	 *             Si hi ha hagut algun problema per a crear l'expedient
	 */
	public void expedientCrear(
			String expedientIdentificador,
			long unitatAdministrativa,
			String idioma,
			String descripcio,
			CiutadaPersona destinatari,
			CiutadaPersona representat,
			String bantelNumeroEntrada,
			boolean avisosHabilitats,
			String avisEmail,
			String avisMobil) throws SistemaExternException;

	/**
	 * Crea un nou avis per a un expedient de la zona personal del ciutadà.
	 * 
	 * @param expedientIdentificador
	 * @param unitatAdministrativa
	 * @param titol
	 * @param text
	 * @param textSms
	 * @param annexos
	 * @throws SistemaExternException
	 *             Si hi ha hagut algun problema per a crear l'avis
	 */
	public void avisCrear(
			String expedientIdentificador,
			long unitatAdministrativa,
			String titol,
			String text,
			String textSms,
			List<CiutadaDocument> annexos) throws SistemaExternException;

	/**
	 * Envia una notificació telemàtica al ciutadà.
	 * 
	 * @param expedientIdentificador
	 * @param unitatAdministrativa
	 * @param oficinaCodi
	 * @param oficinaOrganCodi
	 * @param destinatari
	 * @param destinatariPaisCodi
	 * @param destinatariPaisNom
	 * @param destinatariProvinciaCodi
	 * @param destinatariProvinciaNom
	 * @param destinatariLocalitatCodi
	 * @param destinatariLocalitatNom
	 * @param representat
	 * @param notificacioIdioma
	 * @param notificacioAssumpteTipus
	 * @param notificacioOficiTitol
	 * @param notificacioOficiText
	 * @param notificacioAvisTitol
	 * @param notificacioAvisText
	 * @param notificacioAvisTextSms
	 * @param notificacioConfirmarRecepcio
	 * @param annexos
	 * @return les dades de la notificació creada
	 * @throws SistemaExternException
	 *             Si hi ha hagut algun problema per a crear la notificació
	 */
	public CiutadaNotificacioResultat notificacioCrear(
			String expedientIdentificador,
			long unitatAdministrativa,
			String oficinaCodi,
			String oficinaOrganCodi,
			CiutadaPersona destinatari,
			String destinatariPaisCodi,
			String destinatariPaisNom,
			String destinatariProvinciaCodi,
			String destinatariProvinciaNom,
			String destinatariLocalitatCodi,
			String destinatariLocalitatNom,
			CiutadaPersona representat,
			String notificacioIdioma,
			String notificacioAssumpteTipus,
			String notificacioOficiTitol,
			String notificacioOficiText,
			String notificacioAvisTitol,
			String notificacioAvisText,
			String notificacioAvisTextSms,
			boolean notificacioConfirmarRecepcio,
			List<CiutadaDocument> annexos) throws SistemaExternException;

	/**
	 * Obté l'estat d'una notificació telemàtica enviada al ciutadà.
	 * 
	 * @param registreNumero
	 * @return la informació sobre l'estat de la notificació
	 * @throws SistemaExternException
	 *             Si hi ha hagut algun problema per a obtenir l'estat de la notificació
	 */
	public CiutadaJustificantRecepcio notificacioObtenirJustificantRecepcio(
			String registreNumero) throws SistemaExternException;

}
