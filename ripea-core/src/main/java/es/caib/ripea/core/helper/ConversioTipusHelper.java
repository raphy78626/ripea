/**
 * 
 */
package es.caib.ripea.core.helper;

import java.util.Date;
import java.util.List;
import java.util.Set;

import ma.glasnost.orika.CustomConverter;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import ma.glasnost.orika.metadata.Type;

import org.joda.time.DateTime;
import org.springframework.stereotype.Component;

/**
 * Helper per a convertir entre diferents formats de documents.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@Component
public class ConversioTipusHelper {

	private MapperFactory mapperFactory;

	public ConversioTipusHelper() {
		mapperFactory = new DefaultMapperFactory.Builder().build();
		mapperFactory.getConverterFactory().registerConverter(
				new CustomConverter<DateTime, Date>() {
					public Date convert(DateTime source, Type<? extends Date> destinationClass) {
						return source.toDate();
					}
				});
		/*mapperFactory.getConverterFactory().registerConverter(
				new CustomConverter<ArxiuEntity, ArxiuDto>() {
					public ArxiuDto convert(ArxiuEntity source, Type<? extends ArxiuDto> destinationClass) {
						ArxiuDto target = new ArxiuDto();
						target.setId(source.getId());
						target.setNom(source.getNom());
						target.setUnitatCodi(source.getUnitatCodi());
						target.setActiu(source.isActiu());
						return target;
					}
				});
		mapperFactory.getConverterFactory().registerConverter(
				new CustomConverter<ArxiuEntity, ArxiuDto>() {
					public ArxiuDto convert(ArxiuEntity source, Type<? extends ArxiuDto> destinationClass) {
						ArxiuDto target = new ArxiuDto();
						target.setId(source.getId());
						target.setNom(source.getNom());
						target.setUnitatCodi(source.getUnitatCodi());
						target.setActiu(source.isActiu());
						return target;
					}
				});*/
	}

	public <T> T convertir(Object source, Class<T> targetType) {
		if (source == null)
			return null;
		return getMapperFacade().map(source, targetType);
	}
	public <T> List<T> convertirList(List<?> items, Class<T> targetType) {
		if (items == null)
			return null;
		return getMapperFacade().mapAsList(items, targetType);
	}
	public <T> Set<T> convertirSet(Set<?> items, Class<T> targetType) {
		if (items == null)
			return null;
		return getMapperFacade().mapAsSet(items, targetType);
	}



	private MapperFacade getMapperFacade() {
		return mapperFactory.getMapperFacade();
	}

}
