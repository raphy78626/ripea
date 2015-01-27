/**
 * 
 */
package es.caib.ripea.core.service;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.caib.ripea.core.api.dto.IntegracioAccioDto;
import es.caib.ripea.core.api.dto.IntegracioDto;
import es.caib.ripea.core.api.exception.IntegracioNotFoundException;
import es.caib.ripea.core.api.service.IntegracioService;
import es.caib.ripea.core.helper.IntegracioHelper;

/**
 * Implementació dels mètodes per a gestionar les integracions.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@Service
public class IntegracioServiceImpl implements IntegracioService {

	@Resource
	private IntegracioHelper integracioHelper;



	@Transactional(readOnly = true)
	@Override
	public List<IntegracioDto> findAll() {
		logger.debug("Consultant les integracions");
		return integracioHelper.findAll();
	}

	@Transactional(readOnly = true)
	@Override
	public List<IntegracioAccioDto> findDarreresAccionsByIntegracio(String codi)
			throws IntegracioNotFoundException {
		logger.debug("Consultant les darreres accions per a la integració ("
				+ "codi=" + codi + ")");
		return integracioHelper.findAccionsByIntegracioCodi(codi);
	}



	private static final Logger logger = LoggerFactory.getLogger(IntegracioServiceImpl.class);

}
