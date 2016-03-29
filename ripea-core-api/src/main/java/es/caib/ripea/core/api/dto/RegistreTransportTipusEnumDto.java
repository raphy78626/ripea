/**
 * 
 */
package es.caib.ripea.core.api.dto;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * Enumeració amb els possibles valors del tipus de transport
 * d'una anotació de registre.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public enum RegistreTransportTipusEnumDto {

	MISSATGERIA("01"),
	CORREU("02"),
	CORREU_CERTIFICAT("03"),
	BUROFAX("04"),
	EN_MA("05"),
	FAX("06"),
	ALTRES("07");

	private final String valor;
	private RegistreTransportTipusEnumDto(String valor) {
		this.valor = valor;
	}
	public String getValor() {
		return valor;
	}
	private static final Map<String, RegistreTransportTipusEnumDto> lookup;
	static {
		lookup = new HashMap<String, RegistreTransportTipusEnumDto>();
		for (RegistreTransportTipusEnumDto s: EnumSet.allOf(RegistreTransportTipusEnumDto.class))
			lookup.put(s.getValor(), s);
	}
	public static RegistreTransportTipusEnumDto valorAsEnum(String valor) {
		if (valor == null)
			return null;
        return lookup.get(valor); 
    }

}
