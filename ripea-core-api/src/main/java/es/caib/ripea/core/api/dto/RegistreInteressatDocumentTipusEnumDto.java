/**
 * 
 */
package es.caib.ripea.core.api.dto;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * Enumeració amb els possibles valors del tipus de document
 * d'identitat d'un interessat del registre.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public enum RegistreInteressatDocumentTipusEnumDto {

	NIF("N"),
	CIF("C"),
	PASSAPORT("P"),
	ESTRANGER("E"),
	ALTRES("X"),
	CODI_ORIGEN("O");

	private final String valor;
	private RegistreInteressatDocumentTipusEnumDto(String valor) {
		this.valor = valor;
	}
	public String getValor() {
		return valor;
	}
	private static final Map<String, RegistreInteressatDocumentTipusEnumDto> lookup;
	static {
		lookup = new HashMap<String, RegistreInteressatDocumentTipusEnumDto>();
		for (RegistreInteressatDocumentTipusEnumDto s: EnumSet.allOf(RegistreInteressatDocumentTipusEnumDto.class))
			lookup.put(s.getValor(), s);
	}
	public static RegistreInteressatDocumentTipusEnumDto valorAsEnum(String valor) {
		if (valor == null)
			return null;
        return lookup.get(valor); 
    }

}
