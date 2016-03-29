/**
 * 
 */
package es.caib.ripea.core.api.dto;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * Enumeració amb els possibles valors del tipus d'un interessat
 * del registre.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public enum RegistreInteressatTipusEnumDto {

	PERSONA_FIS("2"),
	PERSONA_JUR("3"),
	ADMINISTRACIO("1");

	private final String valor;
	private RegistreInteressatTipusEnumDto(String valor) {
		this.valor = valor;
	}
	public String getValor() {
		return valor;
	}
	private static final Map<String, RegistreInteressatTipusEnumDto> lookup;
	static {
		lookup = new HashMap<String, RegistreInteressatTipusEnumDto>();
		for (RegistreInteressatTipusEnumDto s: EnumSet.allOf(RegistreInteressatTipusEnumDto.class))
			lookup.put(s.getValor(), s);
	}
	public static RegistreInteressatTipusEnumDto valorAsEnum(String valor) {
		if (valor == null)
			return null;
        return lookup.get(valor); 
    }

}
