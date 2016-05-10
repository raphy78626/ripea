package es.caib.ripea.war.passarelafirma;

import java.util.Date;

import org.fundaciobit.plugins.signatureweb.api.CommonInfoSignature;
import org.fundaciobit.plugins.signatureweb.api.FileInfoSignature;
import org.fundaciobit.plugins.signatureweb.api.SignaturesSet;

/**
 * Bean amb informació sobre un o varis documents a firmar amb
 * la passarel·la.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public class PassarelaFirmaSignaturesSet extends SignaturesSet {

	protected final String urlFinal;
	protected Long pluginId = null;

	public PassarelaFirmaSignaturesSet(
			String signaturesSetId,
			Date expiryDate,
			CommonInfoSignature commonInfoSignature,
			FileInfoSignature[] fileInfoSignatureArray,
			String urlFinal) {
		super(	signaturesSetId,
				expiryDate,
				commonInfoSignature,
				fileInfoSignatureArray);
		this.urlFinal = urlFinal;
	}
	public String getUrlFinal() {
		return urlFinal;
	}
	public Long getPluginId() {
		return pluginId;
	}
	public void setPluginId(Long pluginId) {
		this.pluginId = pluginId;
	}

}
