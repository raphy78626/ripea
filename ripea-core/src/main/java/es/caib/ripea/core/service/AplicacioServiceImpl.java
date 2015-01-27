/**
 * 
 */
package es.caib.ripea.core.service;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.caib.ripea.core.api.dto.UsuariDto;
import es.caib.ripea.core.api.exception.UsuariNotFoundException;
import es.caib.ripea.core.api.service.AplicacioService;
import es.caib.ripea.core.entity.UsuariEntity;
import es.caib.ripea.core.helper.ConversioTipusHelper;
import es.caib.ripea.core.helper.PluginHelper;
import es.caib.ripea.core.repository.UsuariRepository;
import es.caib.ripea.plugin.usuari.DadesUsuari;

/**
 * Implementació dels mètodes per a gestionar la versió de l'aplicació.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@Service
public class AplicacioServiceImpl implements AplicacioService {

	private static final MajorVersion MAJOR_VERSION = MajorVersion.V0_7;
	private static final int MINOR_VERSION = 0;

	private enum MajorVersion {
		V0_1,
		V0_2,
		V0_3,
		V0_4,
		V0_5,
		V0_6,
		V0_7
	}

	@Resource
	private UsuariRepository usuariRepository;

	@Resource
	private PluginHelper pluginHelper;
	@Resource
	private ConversioTipusHelper conversioTipusHelper;



	@Override
	public String getVersioActual() {
		logger.debug("Obtenint versió actual de l'aplicació");
		String majorVersionActual = MAJOR_VERSION.name();
		return majorVersionActual.substring(1).replace("_", ".") + "." + MINOR_VERSION;
	}

	@Transactional
	@Override
	public void processarAutenticacioUsuari() throws UsuariNotFoundException {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		logger.debug("Processant autenticació (usuariCodi=" + auth.getName() + ")");
		UsuariEntity usuari = usuariRepository.findOne(auth.getName());
		if (usuari == null) {
			logger.debug("Consultant plugin de dades d'usuari (usuariCodi=" + auth.getName() + ")");
			DadesUsuari dadesUsuari = pluginHelper.dadesUsuariConsultarAmbUsuariCodi(auth.getName());
			if (dadesUsuari != null) {
				usuari = usuariRepository.save(
						UsuariEntity.getBuilder(
								dadesUsuari.getCodi(),
								dadesUsuari.getNom(),
								dadesUsuari.getNif(),
								dadesUsuari.getEmail()).build());
			} else {
				throw new UsuariNotFoundException();
			}
		} else {
			logger.debug("Consultant plugin de dades d'usuari (usuariCodi=" + auth.getName() + ")");
			DadesUsuari dadesUsuari = pluginHelper.dadesUsuariConsultarAmbUsuariCodi(auth.getName());
			if (dadesUsuari != null) {
				usuari.update(
						dadesUsuari.getNom(),
						dadesUsuari.getNif(),
						dadesUsuari.getEmail());
			} else {
				throw new UsuariNotFoundException();
			}
		}
	}

	@Transactional(readOnly = true)
	@Override
	public UsuariDto getUsuariActual() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		logger.debug("Obtenint usuari actual");
		return conversioTipusHelper.convertir(
				usuariRepository.findOne(auth.getName()),
				UsuariDto.class);
	}

	@Transactional(readOnly = true)
	@Override
	public UsuariDto findUsuariAmbCodi(String codi) {
		logger.debug("Obtenint usuari amb codi (codi=" + codi + ")");
		return conversioTipusHelper.convertir(
				usuariRepository.findOne(codi),
				UsuariDto.class);
	}

	@Transactional(readOnly = true)
	@Override
	public List<UsuariDto> findUsuariAmbText(String text) {
		logger.debug("Consultant usuaris amb text (text=" + text + ")");
		return conversioTipusHelper.convertirList(
				usuariRepository.findByText(text),
				UsuariDto.class);
	}



	private static final Logger logger = LoggerFactory.getLogger(AplicacioServiceImpl.class);

}
