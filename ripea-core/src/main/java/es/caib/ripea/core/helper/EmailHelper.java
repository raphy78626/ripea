/**
 * 
 */
package es.caib.ripea.core.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailMessage;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import es.caib.ripea.core.api.dto.ArbreDto;
import es.caib.ripea.core.api.dto.ArbreNodeDto;
import es.caib.ripea.core.api.dto.UnitatOrganitzativaDto;
import es.caib.ripea.core.entity.BustiaEntity;
import es.caib.ripea.core.entity.CarpetaEntity;
import es.caib.ripea.core.entity.ContenidorEntity;
import es.caib.ripea.core.entity.ContenidorMovimentEntity;
import es.caib.ripea.core.entity.DocumentEntity;
import es.caib.ripea.core.entity.EntitatEntity;
import es.caib.ripea.core.entity.ExpedientEntity;
import es.caib.ripea.core.entity.RegistreEntity;
import es.caib.ripea.core.entity.UsuariEntity;
import es.caib.ripea.plugin.usuari.DadesUsuari;

/**
 * Mètodes per a l'enviament de correus.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@Component
public class EmailHelper {

	private static final String PREFIX_RIPEA = "[RIPEA]";

	@Resource
	private JavaMailSender mailSender;

	@Resource
	private CacheHelper cacheHelper;
	@Resource
	private PluginHelper pluginHelper;
	@Resource
	private ContenidorHelper contenidorHelper;
	@Resource
	private PermisosHelper permisosHelper;



	public void emailUsuariContenidorAgafatSensePermis(
			ContenidorEntity contenidor,
			UsuariEntity usuari) {
		logger.debug("Enviament emails contenidor agafat sense permis (" +
				"contenidorId=" + contenidor.getId() + ")");
		SimpleMailMessage missatge = new SimpleMailMessage();
		missatge.setFrom(getRemitent());
		String tipus = "desconegut";
		if (contenidor instanceof ExpedientEntity) {
			tipus = "expedient";
		} else if (contenidor instanceof DocumentEntity) {
			tipus = "document";
		} else if (contenidor instanceof CarpetaEntity) {
			tipus = "carpeta";
		}
		missatge.setSubject(PREFIX_RIPEA + " Element de l'escriptori agafat per un altre usuari: (" + tipus + ") " + contenidor.getNom());
		missatge.setText(
				"Informació de l'element de l'escriptori:\n" +
				"\tEntitat: " + contenidor.getEntitat().getNom() + "\n" +
				"\tTipus: " + tipus + "\n" +
				"\tNom: " + contenidor.getNom() + "\n\n" + 
				"\tPersona que ho ha agafat: " + usuari.getNom() + "(" + usuari.getCodi() + ").");
		mailSender.send(missatge);
	}

	public void emailBustiaPendentContenidor(
			BustiaEntity bustia,
			ContenidorEntity contenidor,
			ContenidorMovimentEntity contenidorMoviment) {
		logger.debug("Enviament emails nou contenidor a la bústia (" +
				"bustiaId=" + bustia.getId() + ")" +
				"contenidorId=" + contenidor.getId() + ")");
		SimpleMailMessage missatge = new SimpleMailMessage();
		if (emplenarDestinataris(
				missatge,
				bustia)) {
			missatge.setFrom(getRemitent());
			String unitatOrganitzativa = getUnitatOrganitzativaNom(
					bustia.getEntitat(),
					bustia.getUnitatCodi());
			missatge.setSubject(PREFIX_RIPEA + " Nou element rebut a la bústia: " + bustia.getNom());
			String tipus = "desconegut";
			if (contenidor instanceof ExpedientEntity) {
				tipus = "expedient";
			} else if (contenidor instanceof DocumentEntity) {
				tipus = "document";
			} else if (contenidor instanceof CarpetaEntity) {
				tipus = "carpeta";
			}
			missatge.setText(
					"Nou element rebut a la bústia:\n" +
					"\tEntitat: " + bustia.getEntitat().getNom() + "\n" +
					"\tUnitat organitzativa: " + unitatOrganitzativa + "\n" +
					"\tBústia: " + bustia.getNom() + "\n\n" +
					"Dades de l'element: \n" +
					"\tTipus: " + tipus + "\n" +
					"\tNom: " + contenidor.getNom() + "\n" +
					"\tRemitent: " + contenidorMoviment.getRemitent().getNom() + "\n" +
					"\tComentari: " + contenidorMoviment.getComentari() + "\n");
			mailSender.send(missatge);
		}
	}

	public void emailBustiaPendentRegistre(
			BustiaEntity bustia,
			RegistreEntity registre) {
		logger.debug("Enviament emails nova anotació de registre a la bústia (" +
				"bustiaId=" + bustia.getId() + ")" +
				"registreId=" + registre.getId() + ")");
		SimpleMailMessage missatge = new SimpleMailMessage();
		if (emplenarDestinataris(
				missatge,
				bustia)) {
			missatge.setFrom(getRemitent());
			String unitatOrganitzativa = getUnitatOrganitzativaNom(
					bustia.getEntitat(),
					bustia.getUnitatCodi());
			missatge.setSubject(PREFIX_RIPEA + " Nova anotació de registre rebuda a la bústia: " + bustia.getNom());
			missatge.setText(
					"Nova anotació de registre pendent de processar a la bústia:\n" +
					"\tEntitat: " + bustia.getEntitat().getNom() + "\n" +
					"\tUnitat organitzativa: " + unitatOrganitzativa + "\n" +
					"\tBústia: " + bustia.getNom() + "\n\n" +
					"Dades de l'anotació: \n" +
					"\tAcció: " + registre.getAccio() + "\n" +
					"\tTipus: " + registre.getTipus() + "\n" +
					"\tNúmero: " + registre.getNumero() + "\n" +
					"\tData: " + registre.getData() + "\n" +
					"\tAssumpte: " + registre.getAssumpteResum() + "\n");
			mailSender.send(missatge);
		}
	}



	private boolean emplenarDestinataris(
			MailMessage mailMessage,
			BustiaEntity bustia) {
		List<String> destinataris = new ArrayList<String>();
		Set<String> usuaris = contenidorHelper.findUsuarisAmbPermisReadPerContenidor(bustia);
		for (String usuari: usuaris) {
			DadesUsuari dadesUsuari = pluginHelper.dadesUsuariConsultarAmbUsuariCodi(
					usuari);
			if (dadesUsuari != null && dadesUsuari.getEmail() != null)
				destinataris.add(dadesUsuari.getEmail());
		}
		if (!destinataris.isEmpty()) {
			mailMessage.setTo(destinataris.get(0));
			destinataris.remove(0);
			if (!destinataris.isEmpty()) {
				mailMessage.setCc(destinataris.toArray(new String[destinataris.size()]));
			}
			return true;
		} else {
			return false;
		}
	}
	private String getUnitatOrganitzativaNom(
			EntitatEntity entitat,
			String unitatOrganitzativaCodi) {
		ArbreDto<UnitatOrganitzativaDto> arbreUnitats = cacheHelper.findUnitatsOrganitzativesPerEntitat(
				entitat.getCodi());
		for (ArbreNodeDto<UnitatOrganitzativaDto> node: arbreUnitats.toList()) {
			UnitatOrganitzativaDto unitat = node.getDades();
			if (unitat.getCodi().equals(unitatOrganitzativaCodi)) {
				return unitat.getDenominacio();
			}
		}
		return null;
	}
	private String getRemitent() {
		return PropertiesHelper.getProperties().getProperty("es.caib.ripea.email.remitent");
	}

	private static final Logger logger = LoggerFactory.getLogger(EmailHelper.class);

}
