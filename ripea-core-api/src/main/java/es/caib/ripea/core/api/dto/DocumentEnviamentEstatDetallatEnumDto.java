/**
 * 
 */
package es.caib.ripea.core.api.dto;

import java.io.Serializable;

public enum DocumentEnviamentEstatDetallatEnumDto implements Serializable {
	
	AUSENT,
    DESCONEGUT,
    ADRESA_INCORRECTA,
    EDITANT,
    ENVIADA_CENTRE_IMPRESSIO,
    ENVIADA_DEH,
    LLEGIDA,
    ERROR_ENVIAMENT,
    EXTRAVIADA,
    MORT,
    NOTIFICADA,
    PENDENT_ENVIAMENT,
    PENDENT_COMPAREIXENSA,
    REBUTJADA,
    DATA_ENVIAMENT_PROGRAMAT,
    SENSE_INFORMACIO;

    public String value() {
        return name();
    }

    public static DocumentEnviamentEstatDetallatEnumDto fromValue(String v) {
        return valueOf(v);
    }


}
