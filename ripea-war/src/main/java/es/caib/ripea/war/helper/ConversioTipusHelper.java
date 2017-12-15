/**
 * 
 */
package es.caib.ripea.war.helper;

import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import ma.glasnost.orika.CustomConverter;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import ma.glasnost.orika.metadata.Type;

/**
 * Helper per a convertir entre diferents formats de documents.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@Component
public class ConversioTipusHelper {

	private static MapperFactory mapperFactory;
	
	
	public ConversioTipusHelper() {
		mapperFactory = new DefaultMapperFactory.Builder().build();
	}
	
	
	public static <T> T convertir(Object source, Class<T> targetType) {
		if (source == null)
			return null;
		return getMapperFacade().map(source, targetType);
	}
	public static <T> List<T> convertirList(List<?> items, Class<T> targetType) {
		if (items == null)
			return null;
		return getMapperFacade().mapAsList(items, targetType);
	}
	public static <T> Set<T> convertirSet(Set<?> items, Class<T> targetType) {
		if (items == null)
			return null;
		return getMapperFacade().mapAsSet(items, targetType);
	}



	private static MapperFacade getMapperFacade() {
		if (mapperFactory == null) {
			mapperFactory = new DefaultMapperFactory.Builder().build();
		}
		
		return mapperFactory.getMapperFacade();
	}

}
