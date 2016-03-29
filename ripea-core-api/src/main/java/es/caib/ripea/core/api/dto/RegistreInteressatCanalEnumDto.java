/**
 * 
 */
package es.caib.ripea.core.api.dto;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * Enumeració amb els possibles valors del canal de notificació
 * preferent d'un interessat del registre.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public enum RegistreInteressatCanalEnumDto {

	POSTAL("01"),
	ADRECA_ELEC_HAB("02"),
	COMPAREIXENCA_ELEC("03");

	private final String valor;
	private RegistreInteressatCanalEnumDto(String valor) {
		this.valor = valor;
	}
	public String getValor() {
		return valor;
	}
	private static final Map<String, RegistreInteressatCanalEnumDto> lookup;
	static {
		lookup = new HashMap<String, RegistreInteressatCanalEnumDto>();
		for (RegistreInteressatCanalEnumDto s: EnumSet.allOf(RegistreInteressatCanalEnumDto.class))
			lookup.put(s.getValor(), s);
	}
	public static RegistreInteressatCanalEnumDto valorAsEnum(String valor) {
		if (valor == null)
			return null;
        return lookup.get(valor); 
    }

}
