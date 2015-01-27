/**
 * 
 */
package es.caib.ripea.core.helper;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import es.caib.ripea.core.api.exception.EntitatNotFoundException;
import es.caib.ripea.core.entity.EntitatEntity;
import es.caib.ripea.core.repository.EntitatRepository;
import es.caib.ripea.core.security.ExtendedPermission;

/**
 * Utilitat per omplir els permisos de les entitats.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@Component
public class PermisosComprovacioHelper {

	@Resource
	private EntitatRepository entitatRepository;

	@Resource
	private PermisosHelper permisosHelper;



	public EntitatEntity comprovarEntitat(
			Long entitatId,
			boolean comprovarPermisUsuari,
			boolean comprovarPermisAdmin,
			boolean comprovarPermisUsuariOrAdmin) throws EntitatNotFoundException {
		EntitatEntity entitat = entitatRepository.findOne(entitatId);
		if (entitat == null) {
			logger.error("No s'ha trobat l'entitat (entitatId=" + entitatId + ")");
			throw new EntitatNotFoundException();
		}
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (comprovarPermisUsuari) {
			boolean esLectorEntitat = permisosHelper.isGrantedAll(
					entitatId,
					EntitatEntity.class,
					new Permission[] {ExtendedPermission.READ},
					auth);
			if (!esLectorEntitat) {
				logger.error("Aquest usuari no té permisos d'accés sobre l'entitat ("
						+ "id=" + entitatId + ", "
						+ "usuari=" + auth.getName() + ")");
				throw new SecurityException("Sense permisos d'accés sobre aquesta entitat");
			}
		}
		if (comprovarPermisAdmin) {
			boolean esAdministradorEntitat = permisosHelper.isGrantedAll(
					entitatId,
					EntitatEntity.class,
					new Permission[] {ExtendedPermission.ADMINISTRATION},
					auth);
			if (!esAdministradorEntitat) {
				logger.error("Aquest usuari no té permisos d'administrador sobre l'entitat ("
						+ "id=" + entitatId + ", "
						+ "usuari=" + auth.getName() + ")");
				throw new SecurityException("Sense permisos d'administrador sobre aquesta entitat");
			}
		}
		if (comprovarPermisUsuariOrAdmin) {
			boolean esAdministradorOLectorEntitat = permisosHelper.isGrantedAny(
					entitatId,
					EntitatEntity.class,
					new Permission[] {
						ExtendedPermission.ADMINISTRATION,
						ExtendedPermission.READ},
					auth);
			if (!esAdministradorOLectorEntitat) {
				logger.error("Aquest usuari no té permisos d'administrador o d'accés sobre l'entitat ("
						+ "id=" + entitatId + ", "
						+ "usuari=" + auth.getName() + ")");
				throw new SecurityException("Sense permisos d'administrador o d'acces sobre aquesta entitat");
			}
		}
		return entitat;
	}

	public boolean isEntitatAdministrador(
			Long entitatId) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return permisosHelper.isGrantedAll(
				entitatId,
				EntitatEntity.class,
				new Permission[] {ExtendedPermission.ADMINISTRATION},
				auth);
	}

	private static final Logger logger = LoggerFactory.getLogger(PermisosComprovacioHelper.class);

}
