/**
 * 
 */
package es.caib.ripea.core.api.dto;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * Enumeració amb els possibles valors de la validesa d'un document
 * de registre.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public enum RegistreDocumentValidesaEnumDto {

	COPIA("01"),
	COPIA_COMPULSADA("02"),
	COPIA_ORIGINAL("03"),
	ORIGINAL("04");

	private final String valor;
	private RegistreDocumentValidesaEnumDto(String valor) {
		this.valor = valor;
	}
	public String getValor() {
		return valor;
	}
	private static final Map<String, RegistreDocumentValidesaEnumDto> lookup;
	static {
		lookup = new HashMap<String, RegistreDocumentValidesaEnumDto>();
		for (RegistreDocumentValidesaEnumDto s: EnumSet.allOf(RegistreDocumentValidesaEnumDto.class))
			lookup.put(s.getValor(), s);
	}
	public static RegistreDocumentValidesaEnumDto valorAsEnum(String valor) {
		if (valor == null)
			return null;
        return lookup.get(valor); 
    }

}
