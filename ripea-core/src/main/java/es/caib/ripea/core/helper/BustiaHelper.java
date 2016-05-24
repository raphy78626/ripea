/**
 * 
 */
package es.caib.ripea.core.helper;

import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import es.caib.ripea.core.entity.BustiaEntity;
import es.caib.ripea.core.entity.EntitatEntity;

/**
 * Mètodes comuns per a gestionar bústies.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@Component
public class BustiaHelper {

	@Resource
	private CacheHelper cacheHelper;
	@Resource
	private ContenidorHelper contenidorHelper;



	public void evictElementsPendentsBustia(
			EntitatEntity entitat,
			BustiaEntity bustia) {
		Set<String> usuaris = contenidorHelper.findUsuarisAmbPermisReadPerContenidor(bustia);
		if (usuaris != null) {
			for (String usuari: usuaris)
				cacheHelper.evictElementsPendentsBustiesUsuari(
						entitat,
						usuari);
		}
	}

}
