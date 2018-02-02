/**
 * 
 */
package es.caib.ripea.war.helper;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import es.caib.ripea.core.api.dto.EntitatDto;
import es.caib.ripea.core.api.dto.MetaExpedientDto;
import es.caib.ripea.core.api.service.MetaExpedientService;

/**
 * Utilitat per a gestionar les expedients i metaexpedients de l'usuari actual.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public class ExpedientHelper {

	private static final String REQUEST_PARAMETER_ACCES_EXPEDIENTS = "isUsuariAccesExpedients";
	private static final String SESSION_ATTRIBUTE_ENTITAT_ACTUAL = "EntitatHelper.entitatActual";


	
	public static void accesUsuariExpedients(
			HttpServletRequest request,
			MetaExpedientService metaExpedientService) {
		
		request.getSession().setAttribute(
				REQUEST_PARAMETER_ACCES_EXPEDIENTS,
				isUsuariAccesExpedients(
						request,
						metaExpedientService));
	}
	
	public static boolean isUsuariAccesExpedients(
			HttpServletRequest request,
			MetaExpedientService metaExpedientService) {
		
		EntitatDto entitatActual = (EntitatDto)request.getSession().getAttribute(
				SESSION_ATTRIBUTE_ENTITAT_ACTUAL);
		
		if (entitatActual != null) {
			List<MetaExpedientDto> expedientsAccessibles =  metaExpedientService.findAmbEntitatPerLectura(entitatActual.getId());
			
			boolean isUsuariAccesExpedients = false;
			if (expedientsAccessibles != null && !expedientsAccessibles.isEmpty())
				isUsuariAccesExpedients = true;
			
			return isUsuariAccesExpedients;
		} else {
			return false;
		}
	}
}
