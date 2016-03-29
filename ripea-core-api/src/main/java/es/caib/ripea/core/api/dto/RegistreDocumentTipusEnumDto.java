/**
 * 
 */
package es.caib.ripea.core.api.dto;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * Enumeració amb els possibles valors del tipus d'un document
 * de registre.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public enum RegistreDocumentTipusEnumDto {

	FORM("01"),
	FORM_ADJUNT("02"),
	INTERN("03");

	private final String valor;
	private RegistreDocumentTipusEnumDto(String valor) {
		this.valor = valor;
	}
	public String getValor() {
		return valor;
	}
	private static final Map<String, RegistreDocumentTipusEnumDto> lookup;
	static {
		lookup = new HashMap<String, RegistreDocumentTipusEnumDto>();
		for (RegistreDocumentTipusEnumDto s: EnumSet.allOf(RegistreDocumentTipusEnumDto.class))
			lookup.put(s.getValor(), s);
	}
	public static RegistreDocumentTipusEnumDto valorAsEnum(String valor) {
		if (valor == null)
			return null;
        return lookup.get(valor); 
    }

}
