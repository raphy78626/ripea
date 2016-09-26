package es.caib.ripea.war.passarelafirma;

import java.util.Date;

import org.fundaciobit.plugins.signature.api.CommonInfoSignature;
import org.fundaciobit.plugins.signature.api.FileInfoSignature;
import org.fundaciobit.plugins.signatureweb.api.SignaturesSetWeb;

/**
 * Bean amb informació sobre un o varis documents a firmar amb
 * la passarel·la.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public class PassarelaFirmaSignaturesSet extends SignaturesSetWeb {


	protected Long pluginId = null;
	
	protected final String urlFinalCalculada;

	public PassarelaFirmaSignaturesSet(
			String signaturesSetId,
			Date expiryDate,
			CommonInfoSignature commonInfoSignature,
			FileInfoSignature[] fileInfoSignatureArray,
			String urlFinalCalculada, String urlFinal) {
		super(	signaturesSetId,
				expiryDate,
				commonInfoSignature,
				fileInfoSignatureArray,urlFinalCalculada);
		this.urlFinalCalculada = urlFinalCalculada; 
		
	}

	public Long getPluginId() {
		return pluginId;
	}
	public void setPluginId(Long pluginId) {
		this.pluginId = pluginId;
	}

  public String getUrlFinalCalculada() {
    return urlFinalCalculada;
  }

	
	
}
