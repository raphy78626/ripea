/**
 * 
 */
package es.caib.ripea.core.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Component;

import es.caib.ripea.core.api.dto.PaginaDto;
import es.caib.ripea.core.api.dto.PaginacioParamsDto;
import es.caib.ripea.core.api.dto.PaginacioParamsDto.OrdreDireccioDto;
import es.caib.ripea.core.api.dto.PaginacioParamsDto.OrdreDto;

/**
 * Helper per a convertir les dades de paginació entre el DTO
 * i Spring-Data.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@Component
public class PaginacioHelper {

	@Resource
	private ConversioTipusHelper conversioTipusHelper;



	public boolean esPaginacioActivada(PaginacioParamsDto dto) {
		return dto.getPaginaTamany() > 0;
	}

	public <T> Pageable toSpringDataPageable(
			PaginacioParamsDto dto,
			Map<String, String[]> mapeigPropietatsOrdenacio) {
		return new PageRequest(
				dto.getPaginaNum(),
				dto.getPaginaTamany(),
				toSpringDataSort(dto.getOrdres(), mapeigPropietatsOrdenacio));
	}
	public <T> Pageable toSpringDataPageable(
			PaginacioParamsDto dto) {
		return toSpringDataPageable(dto, null);
	}

	public <T> Sort toSpringDataSort(
			PaginacioParamsDto dto) {
		return toSpringDataSort(dto.getOrdres(), null);
	}
	public Sort toSpringDataSort(
			List<OrdreDto> ordres,
			Map<String, String[]> mapeigPropietatsOrdenacio) {
		List<Order> orders = new ArrayList<Order>();
		if (ordres != null) {
			for (OrdreDto ordre: ordres) {
				Direction direccio = OrdreDireccioDto.DESCENDENT.equals(ordre.getDireccio()) ? Sort.Direction.DESC : Sort.Direction.ASC;
				if (mapeigPropietatsOrdenacio != null) {
					String[] mapeig = mapeigPropietatsOrdenacio.get(ordre.getCamp());
					if (mapeig != null) {
						for (String prop: mapeig) {
							orders.add(new Order(
									direccio,
									prop));
						}
					} else {
						orders.add(new Order(
								direccio,
								ordre.getCamp()));
					}
				} else {
					orders.add(new Order(
							direccio,
							ordre.getCamp()));
				}
			}
		}
		if (!orders.isEmpty())
			return new Sort(orders);
		else
			return null;
	}

	public <S, T> PaginaDto<T> toPaginaDto(
			Page<S> page,
			Class<T> targetType) {
		return toPaginaDto(page, targetType, null);
	}
	public <S, T> PaginaDto<T> toPaginaDto(
			Page<S> page,
			Class<T> targetType,
			Converter<S, T> converter) {
		PaginaDto<T> dto = new PaginaDto<T>();
		dto.setNumero(page.getNumber());
		dto.setTamany(page.getSize());
		dto.setTotal(page.getTotalPages());
		dto.setElementsTotal(page.getTotalElements());
		dto.setAnteriors(page.hasPrevious());
		dto.setPrimera(page.isFirst());
		dto.setPosteriors(page.hasNext());
		dto.setDarrera(page.isLast());
		if (page.hasContent()) {
			if (converter == null) {
				dto.setContingut(
						conversioTipusHelper.convertirList(
								page.getContent(),
								targetType));
			} else {
				List<T> contingut = new ArrayList<T>();
				for (S element: page.getContent()) {
					contingut.add(
							converter.convert(element));
				}
				dto.setContingut(contingut);
			}
		}
		return dto;
	}
	public <T> PaginaDto<T> toPaginaDto(
			List<?> llista,
			Class<T> targetType) {
		PaginaDto<T> dto = new PaginaDto<T>();
		dto.setNumero(0);
		dto.setTamany(llista.size());
		dto.setTotal(1);
		dto.setElementsTotal(llista.size());
		dto.setAnteriors(false);
		dto.setPrimera(true);
		dto.setPosteriors(false);
		dto.setDarrera(true);
		if (targetType != null) {
			dto.setContingut(
					conversioTipusHelper.convertirList(
							llista,
							targetType));
		}
		return dto;
	}

	public <T> PaginaDto<T> getPaginaDtoBuida(
			Class<T> targetType) {
		PaginaDto<T> dto = new PaginaDto<T>();
		dto.setNumero(0);
		dto.setTamany(0);
		dto.setTotal(1);
		dto.setElementsTotal(0);
		dto.setAnteriors(false);
		dto.setPrimera(true);
		dto.setPosteriors(false);
		dto.setDarrera(true);
		return dto;
	}

	public interface Converter<S, T> {
	    T convert(S source);
	}

}
