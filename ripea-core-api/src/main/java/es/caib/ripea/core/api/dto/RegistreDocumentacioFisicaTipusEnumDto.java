/**
 * 
 */
package es.caib.ripea.core.api.dto;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * Enumeració amb els possibles valors del tipus de documentació
 * física d'una anotació de registre.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public enum RegistreDocumentacioFisicaTipusEnumDto {

	AMB_REQUERIDA("1"),
	AMB_COMPLEM("2"),
	SENSE("3");

	private final String valor;
	private RegistreDocumentacioFisicaTipusEnumDto(String valor) {
		this.valor = valor;
	}
	public String getValor() {
		return valor;
	}
	private static final Map<String, RegistreDocumentacioFisicaTipusEnumDto> lookup;
	static {
		lookup = new HashMap<String, RegistreDocumentacioFisicaTipusEnumDto>();
		for (RegistreDocumentacioFisicaTipusEnumDto s: EnumSet.allOf(RegistreDocumentacioFisicaTipusEnumDto.class))
			lookup.put(s.getValor(), s);
	}
	public static RegistreDocumentacioFisicaTipusEnumDto valorAsEnum(String valor) {
		if (valor == null)
			return null;
        return lookup.get(valor); 
    }

}
