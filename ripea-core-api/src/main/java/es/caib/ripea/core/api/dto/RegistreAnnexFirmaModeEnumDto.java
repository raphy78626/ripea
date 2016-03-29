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
public enum RegistreAnnexFirmaModeEnumDto {

	FIRMA_NO(0),
	FIRMA_AUTO_SI(1),
	FIRMA_AUTO_NO(2);

	private final int valor;
	private RegistreAnnexFirmaModeEnumDto(int valor) {
		this.valor = valor;
	}
	public int getValor() {
		return valor;
	}
	private static final Map<Integer, RegistreAnnexFirmaModeEnumDto> lookup;
	static {
		lookup = new HashMap<Integer, RegistreAnnexFirmaModeEnumDto>();
		for (RegistreAnnexFirmaModeEnumDto s: EnumSet.allOf(RegistreAnnexFirmaModeEnumDto.class))
			lookup.put(new Integer(s.getValor()), s);
	}
	public static RegistreAnnexFirmaModeEnumDto valorAsEnum(int valor) {
        return lookup.get(new Integer(valor)); 
    }

}
