/**
 * 
 */
package es.caib.ripea.core.helper;

import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import es.caib.ripea.core.entity.BustiaEntity;
import es.caib.ripea.core.entity.EntitatEntity;
import es.caib.ripea.core.entity.RegistreEntity;
import es.caib.ripea.core.repository.BustiaRepository;


/**
 * Helper per a operacions amb bústies.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@Component
public class BustiaHelper {

	@Resource
	private BustiaRepository bustiaRepository;

	@Resource
	private CacheHelper cacheHelper;
	@Resource
	private ContenidorHelper contenidorHelper;



	public void evictElementsPendentsBustiaPerRegistre(
			EntitatEntity entitat,
			RegistreEntity registre) {
		Long contenidorOrigenId = registre.getContenidor().getId();
		BustiaEntity bustia = bustiaRepository.findOne(contenidorOrigenId);
		if (bustia != null) {
			evictElementsPendentsBustia(
					entitat,
					bustia);
		}
	}
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
