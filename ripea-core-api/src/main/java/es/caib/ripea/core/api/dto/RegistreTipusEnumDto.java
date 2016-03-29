/**
 * 
 */
package es.caib.ripea.core.api.dto;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * Enumeració amb els possibles valors del tipus d'una anotació
 * de registre.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public enum RegistreTipusEnumDto {

	ENTRADA("0"),
	SORTIDA("1");

	private final String valor;
	private RegistreTipusEnumDto(String valor) {
		this.valor = valor;
	}
	public String getValor() {
		return valor;
	}
	private static final Map<String, RegistreTipusEnumDto> lookup;
	static {
		lookup = new HashMap<String, RegistreTipusEnumDto>();
		for (RegistreTipusEnumDto s: EnumSet.allOf(RegistreTipusEnumDto.class))
			lookup.put(s.getValor(), s);
	}
	public static RegistreTipusEnumDto valorAsEnum(String valor) {
		if (valor == null)
			return null;
        return lookup.get(valor); 
    }

}
