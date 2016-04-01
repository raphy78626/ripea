/**
 * 
 */
package es.caib.ripea.war.security;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.mapping.MappableAttributesRetriever;

/**
 * Aconsegueix els rols que seran rellevants per a l'aplicació.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public class RolesBasedMappableAttributesRetriever implements MappableAttributesRetriever {

	/*@Autowired
	private AplicacioService aplicacioService;*/

	private Set<String> defaultMappableAttributes;
	private Set<String> mappableAttributes = new HashSet<String>();
	private boolean refrescat = false;



	public Set<String> getMappableAttributes() {
		refrescarMappableAttributes();
		return mappableAttributes;
	}

	public void setDefaultMappableAttributes(Set<String> defaultMappableAttributes) {
		this.defaultMappableAttributes = defaultMappableAttributes;
	}



	private void refrescarMappableAttributes() {
		if (!refrescat) {
			LOGGER.debug("Refrescant el llistat de rols per mapejar");
			mappableAttributes.clear();
			if (defaultMappableAttributes != null) {
				mappableAttributes.addAll(defaultMappableAttributes);
			}
			//List<String> rolsPermisos = aplicacioService.permisosFindRolsDistinctAll();
			//mappableAttributes.addAll(rolsPermisos);
		}
	}

	private static final Logger LOGGER = LoggerFactory.getLogger(RolesBasedMappableAttributesRetriever.class);

}
