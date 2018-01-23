/**
 * 
 */
package es.caib.ripea.core.helper;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.joda.time.DateTime;
import org.springframework.stereotype.Component;

import es.caib.ripea.core.api.dto.AlertaDto;
import es.caib.ripea.core.api.dto.DocumentDto;
import es.caib.ripea.core.api.dto.ExecucioMassivaContingutDto;
import es.caib.ripea.core.api.dto.ExecucioMassivaContingutDto.ExecucioMassivaEstatDto;
import es.caib.ripea.core.api.dto.ExecucioMassivaDto;
import es.caib.ripea.core.entity.AlertaEntity;
import es.caib.ripea.core.entity.DocumentEntity;
import es.caib.ripea.core.entity.ExecucioMassivaContingutEntity;
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
		mapperFactory.getConverterFactory().registerConverter(
				new CustomConverter<ExecucioMassivaContingutEntity, ExecucioMassivaContingutDto>() {
					public ExecucioMassivaContingutDto convert(ExecucioMassivaContingutEntity source, Type<? extends ExecucioMassivaContingutDto> destinationClass) {
						ExecucioMassivaContingutDto target = new ExecucioMassivaContingutDto();
						target.setDataInici(source.getDataInici());
						target.setDataFi(source.getDataFi());
						target.setEstat(ExecucioMassivaEstatDto.valueOf(source.getEstat().name()));
						target.setError(source.getError());
						target.setOrdre(source.getOrdre());
						target.setExecucioMassiva(convertir(source.getExecucioMassiva(), ExecucioMassivaDto.class));
						
						if (source.getContingut() instanceof DocumentEntity)
							target.setContingut(convertir((DocumentEntity)source.getContingut(), DocumentDto.class));
						
						return target;
					}
				});
		mapperFactory.getConverterFactory().registerConverter(
				new CustomConverter<AlertaEntity, AlertaDto>() {
					public AlertaDto convert(AlertaEntity source, Type<? extends AlertaDto> destinationClass) {
						AlertaDto target = new AlertaDto();
						target.setId(source.getId());
						target.setText(source.getText());
						target.setError(source.getError());
						target.setLlegida(source.getLlegida().booleanValue());
						target.setContingutId(source.getContingut().getId());
						
						return target;
					}
				});
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
