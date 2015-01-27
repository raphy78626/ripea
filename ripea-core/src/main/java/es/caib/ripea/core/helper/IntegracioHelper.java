/**
 * 
 */
package es.caib.ripea.core.helper;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.stereotype.Component;

import es.caib.ripea.core.api.dto.IntegracioAccioDto;
import es.caib.ripea.core.api.dto.IntegracioAccioEstatEnumDto;
import es.caib.ripea.core.api.dto.IntegracioAccioTipusEnumDto;
import es.caib.ripea.core.api.dto.IntegracioDto;

/**
 * Mètodes per a la gestió d'integracions.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@Component
public class IntegracioHelper {

	public static final int DEFAULT_MAX_ACCIONS = 10;

	public static final String INTCODI_USUARIS = "USUARIS";
	public static final String INTCODI_UNITATS = "UNITATS";
	public static final String INTCODI_SISTRA = "SISTRA";
	public static final String INTCODI_PFIRMA = "PFIRMA";
	public static final String INTCODI_CUSTODIA = "CUSTODIA";
	public static final String INTCODI_REGISTRE = "REGISTRE";
	public static final String INTCODI_GESDOC = "GESDOC";
	public static final String INTCODI_CONVERT = "CONVERT";
	public static final String INTCODI_CALLBACK = "CALLBACK";

	private Map<String, LinkedList<IntegracioAccioDto>> accionsIntegracio = new HashMap<String, LinkedList<IntegracioAccioDto>>();
	private Map<String, Integer> maxAccionsIntegracio = new HashMap<String, Integer>();



	public List<IntegracioDto> findAll() {
		List<IntegracioDto> integracions = new ArrayList<IntegracioDto>();
		integracions.add(
				novaIntegracio(
						INTCODI_REGISTRE));
		integracions.add(
				novaIntegracio(
						INTCODI_UNITATS));
		integracions.add(
				novaIntegracio(
						INTCODI_PFIRMA));
		integracions.add(
				novaIntegracio(
						INTCODI_CALLBACK));
		integracions.add(
				novaIntegracio(
						INTCODI_CUSTODIA));
		integracions.add(
				novaIntegracio(
						INTCODI_GESDOC));
		integracions.add(
				novaIntegracio(
						INTCODI_SISTRA));
		integracions.add(
				novaIntegracio(
						INTCODI_USUARIS));
		integracions.add(
				novaIntegracio(
						INTCODI_CONVERT));
		return integracions;
	}

	public List<IntegracioAccioDto> findAccionsByIntegracioCodi(
			String integracioCodi) {
		return getLlistaAccions(integracioCodi);
	}

	public void addAccioOk(
			String integracioCodi,
			String descripcio,
			Map<String, String> parametres,
			IntegracioAccioTipusEnumDto tipus) {
		IntegracioAccioDto accio = new IntegracioAccioDto();
		accio.setIntegracio(novaIntegracio(integracioCodi));
		accio.setData(new Date());
		accio.setDescripcio(descripcio);
		accio.setParametres(parametres);
		accio.setTipus(tipus);
		accio.setEstat(IntegracioAccioEstatEnumDto.OK);
		addAccio(
				integracioCodi,
				accio);
	}
	public void addAccioError(
			String integracioCodi,
			String descripcio,
			Map<String, String> parametres,
			IntegracioAccioTipusEnumDto tipus,
			String errorDescripcio) {
		IntegracioAccioDto accio = new IntegracioAccioDto();
		accio.setIntegracio(novaIntegracio(integracioCodi));
		accio.setData(new Date());
		accio.setDescripcio(descripcio);
		accio.setParametres(parametres);
		accio.setTipus(tipus);
		accio.setEstat(IntegracioAccioEstatEnumDto.ERROR);
		accio.setErrorDescripcio(errorDescripcio);
		addAccio(
				integracioCodi,
				accio);
	}
	public void addAccioError(
			String integracioCodi,
			String descripcio,
			Map<String, String> parametres,
			IntegracioAccioTipusEnumDto tipus,
			String errorDescripcio,
			Throwable throwable) {
		IntegracioAccioDto accio = new IntegracioAccioDto();
		accio.setIntegracio(novaIntegracio(integracioCodi));
		accio.setData(new Date());
		accio.setDescripcio(descripcio);
		accio.setParametres(parametres);
		accio.setTipus(tipus);
		accio.setEstat(IntegracioAccioEstatEnumDto.ERROR);
		accio.setErrorDescripcio(errorDescripcio);
		accio.setExcepcioMessage(
				ExceptionUtils.getMessage(throwable));
		accio.setExcepcioStacktrace(
				ExceptionUtils.getStackTrace(throwable));
		addAccio(
				integracioCodi,
				accio);
	}



	private LinkedList<IntegracioAccioDto> getLlistaAccions(
			String integracioCodi) {
		LinkedList<IntegracioAccioDto> accions = accionsIntegracio.get(integracioCodi);
		if (accions == null) {
			accions = new LinkedList<IntegracioAccioDto>();
			accionsIntegracio.put(
					integracioCodi,
					accions);
		} else {
			int index = 0;
			for (IntegracioAccioDto accio: accions) {
				accio.setIndex(new Long(index++));
			}
		}
		return accions;
	}
	private int getMaxAccions(
			String integracioCodi) {
		Integer max = maxAccionsIntegracio.get(integracioCodi);
		if (max == null) {
			max = new Integer(DEFAULT_MAX_ACCIONS);
			maxAccionsIntegracio.put(
					integracioCodi,
					max);
		}
		return max.intValue();
	}

	private void addAccio(
			String integracioCodi,
			IntegracioAccioDto accio) {
		LinkedList<IntegracioAccioDto> accions = getLlistaAccions(integracioCodi);
		int max = getMaxAccions(integracioCodi);
		while (accions.size() >= max) {
			accions.poll();
		}
		accions.add(accio);
	}

	private IntegracioDto novaIntegracio(
			String codi) {
		IntegracioDto integracio = new IntegracioDto();
		integracio.setCodi(codi);
		if (INTCODI_REGISTRE.equals(codi)) {
			integracio.setNom("Registre");
		} else if (INTCODI_PFIRMA.equals(codi)) {
			integracio.setNom("Portafirmes");
		} else if (INTCODI_CUSTODIA.equals(codi)) {
			integracio.setNom("Custòdia doc.");
		} else if (INTCODI_GESDOC.equals(codi)) {
			integracio.setNom("Gestió doc.");
		} else if (INTCODI_CONVERT.equals(codi)) {
			integracio.setNom("Conversió doc.");
		} else if (INTCODI_USUARIS.equals(codi)) {
			integracio.setNom("Usuaris");
		} else if (INTCODI_UNITATS.equals(codi)) {
			integracio.setNom("Unitats admin.");
		} else if (INTCODI_SISTRA.equals(codi)) {
			integracio.setNom("Tramitació");
		} else if (INTCODI_CALLBACK.equals(codi)) {
			integracio.setNom("Callback PF");
		}
		return integracio;
	}

	//private static final Logger logger = LoggerFactory.getLogger(IntegracioHelper.class);

}
