/**
 * 
 */
package es.caib.ripea.war.helper;

import javax.servlet.http.HttpServletRequest;

import es.caib.ripea.core.api.dto.EntitatDto;
import es.caib.ripea.core.api.service.BustiaService;

/**
 * Utilitat per a contar els elements pendents de les bústies
 * de l'usuari actual.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public class ElementsPendentsBustiaHelper {

	private static final String REQUEST_PARAMETER_COUNT = "ElementsPendentsBustiatHelper.countElementsPendentsBustia";



	public static void countElementsPendentsBustia(
			HttpServletRequest request,
			BustiaService bustiaService) {
		if (!RequestHelper.isError(request) && bustiaService != null && RolHelper.isUsuariActualUsuari(request)) {
			EntitatDto entitat = EntitatHelper.getEntitatActual(request);
			long count = bustiaService.contingutPendentBustiesAllCount(
					entitat.getId());
			request.setAttribute(REQUEST_PARAMETER_COUNT, new Long(count));
		}
	}

	public static Long getCount(HttpServletRequest request) {
		return (Long)request.getAttribute(REQUEST_PARAMETER_COUNT);
	}

}
