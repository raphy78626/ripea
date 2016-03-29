/**
 * 
 */
package es.caib.ripea.core.api.dto;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * Enumeració amb els possibles valors de l'origen d'un
 * annex del registre.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public enum RegistreAnnexOrigenEnumDto {

	CIUTADA("0"),
	ADMINISTRACIO("1");

	private final String valor;
	private RegistreAnnexOrigenEnumDto(String valor) {
		this.valor = valor;
	}
	public String getValor() {
		return valor;
	}
	private static final Map<String, RegistreAnnexOrigenEnumDto> lookup;
	static {
		lookup = new HashMap<String, RegistreAnnexOrigenEnumDto>();
		for (RegistreAnnexOrigenEnumDto s: EnumSet.allOf(RegistreAnnexOrigenEnumDto.class))
			lookup.put(s.getValor(), s);
	}
	public static RegistreAnnexOrigenEnumDto valorAsEnum(String valor) {
		if (valor == null)
			return null;
        return lookup.get(valor); 
    }
	
}
