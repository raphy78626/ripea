/**
 * 
 */
package es.caib.ripea.core.service.ws.registre;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.jws.WebService;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import es.caib.ripea.core.api.dto.RegistreAccioEnumDto;
import es.caib.ripea.core.api.dto.RegistreAnotacioDto;
import es.caib.ripea.core.api.dto.RegistreDocumentDto;
import es.caib.ripea.core.api.dto.RegistreDocumentTipusEnumDto;
import es.caib.ripea.core.api.dto.RegistreDocumentValidesaEnumDto;
import es.caib.ripea.core.api.dto.RegistreDocumentacioFisicaTipusEnumDto;
import es.caib.ripea.core.api.dto.RegistreInteressatCanalEnumDto;
import es.caib.ripea.core.api.dto.RegistreInteressatDocumentTipusEnumDto;
import es.caib.ripea.core.api.dto.RegistreInteressatDto;
import es.caib.ripea.core.api.dto.RegistreTipusEnumDto;
import es.caib.ripea.core.api.dto.RegistreTransportTipusEnumDto;
import es.caib.ripea.core.api.service.RegistreService;

/**
 * Implementació dels mètodes per al servei de registre de RIPEA.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@Component
@WebService(
		name = "Registre",
		serviceName = "RegistreService",
		portName = "RegistreServicePort",
		endpointInterface = "es.caib.ripea.core.service.ws.registre.Registre",
		targetNamespace = "http://www.caib.es/ripea/ws/registre")
public class RegistreImpl implements Registre {

	@Resource
	private RegistreService registreService;



	@Override
	public boolean avisAnotacio(AnotacionRegistro anotacio) {
		String numRegistre = null;
		String dataEntrada = null;
		if (anotacio.getOrigen() != null) {
			numRegistre = anotacio.getOrigen().getNumeroRegistroEntrada();
			dataEntrada = anotacio.getOrigen().getFechaHoraEntrada();
		}
		String tipusAnotacio = null;
		if (anotacio.getOrigen() != null)
			tipusAnotacio = ("0".equals(anotacio.getControl().getTipoRegistro())) ? "entrada" : "sortida";
		logger.debug("Processant avís d'anotació al servei web de registre (" +
				"accio:" + anotacio.getAccion() + ", " +
				"numRegistre:" + numRegistre + ", " +
				"dataEntrada:" + dataEntrada + ", " +
				"tipusAnotacio:" + tipusAnotacio + ")");
		// Comprova els camps obligatoris
		if (anotacio.getAccion() == null) {
			throw new RegistreException(
					"Error al processar anotació: Falta element obligatori 'accion'");
		}
		if (anotacio.getControl() != null) {
			Control control = anotacio.getControl();
			if (control.getTipoRegistro() == null)
				throw new RegistreException(
						"Error al processar anotació: Falta camp obligatori 'control.tipoRegistro'");
		} else {
			throw new RegistreException(
					"Error al processar anotació: Falta element obligatori 'control'");
		}
		if (anotacio.getOrigen() != null) {
			Origen origen = anotacio.getOrigen();
			if (origen.getCodigoEntidadRegistralOrigen() == null)
				throw new RegistreException(
						"Error al processar anotació: Falta camp obligatori 'origen.codigoEntidadRegistralOrigen'");
			if (origen.getNumeroRegistroEntrada() == null)
				throw new RegistreException(
						"Error al processar anotació: Falta camp obligatori 'origen.numeroRegistroEntrada'");
			if (origen.getFechaHoraEntrada() == null)
				throw new RegistreException(
						"Error al processar anotació: Falta camp obligatori 'origen.fechaHoraEntrada'");
		} else {
			throw new RegistreException(
					"Error al processar anotació: Falta element obligatori 'origen'");
		}
		if (anotacio.getAsunto() != null) {
			Asunto asunto = anotacio.getAsunto();
			if (asunto.getResumen() == null)
				throw new RegistreException(
						"Error al processar anotació: Falta camp obligatori 'asunto.resumen'");
		} else {
			throw new RegistreException(
					"Error al processar anotació: Falta element obligatori 'asunto'");
		}
		try {
			RegistreAnotacioDto registreAnotacioDto = toRegistreAnotacioDto(anotacio);
			registreService.create(
					new Long(1),
					registreAnotacioDto);
			return true;
		} catch (Exception ex) {
			logger.error("Error al processar anotació (" +
				"numRegistre:" + numRegistre + ", " +
				"dataEntrada:" + dataEntrada + ", " +
				"tipusAnotacio:" + tipusAnotacio + ")", ex);
			throw new RegistreException(
					"Error al processar anotació: " + ExceptionUtils.getStackTrace(ex));
		}
	}



	private RegistreAnotacioDto toRegistreAnotacioDto(AnotacionRegistro anotacio) throws Exception {
		RegistreAnotacioDto registreAnotacio = new RegistreAnotacioDto();
		if (anotacio.getAccion() != null) {
			String accion = anotacio.getAccion();
			if ("0".equals(accion))
				registreAnotacio.setAccio(RegistreAccioEnumDto.CREATE);
			else if ("1".equals(accion))
				registreAnotacio.setAccio(RegistreAccioEnumDto.UPDATE);
			else if ("2".equals(accion))
				registreAnotacio.setAccio(RegistreAccioEnumDto.DELETE);
		}
		if (anotacio.getOrigen() != null) {
			Origen origen = anotacio.getOrigen();
			registreAnotacio.setEntitatCodi(origen.getCodigoEntidadRegistralOrigen());
			registreAnotacio.setEntitatNom(origen.getDecodificacionEntidadRegistralOrigen());
			registreAnotacio.setNumero(origen.getNumeroRegistroEntrada());
			DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
			registreAnotacio.setData(df.parse(origen.getFechaHoraEntrada()));
		}
		if (anotacio.getAsunto() != null) {
			Asunto asunto = anotacio.getAsunto();
			registreAnotacio.setAssumpteResum(asunto.getResumen());
			registreAnotacio.setAssumpteCodi(asunto.getCodigoAsuntoSegunDestino());
			registreAnotacio.setAssumpteReferencia(asunto.getReferenciaExterna());
			registreAnotacio.setAssumpteNumExpedient(asunto.getNumeroExpediente());
		}
		if (anotacio.getControl() != null) {
			Control control = anotacio.getControl();
			if ("0".equals(control.getTipoRegistro()))
				registreAnotacio.setTipus(RegistreTipusEnumDto.ENTRADA);
			else if ("1".equals(control.getTipoRegistro()))
				registreAnotacio.setTipus(RegistreTipusEnumDto.SORTIDA);
			if ("01".equals(control.getTipoTransporteEntrada()))
				registreAnotacio.setTransportTipus(RegistreTransportTipusEnumDto.MISSATGERIA);
			else if ("02".equals(control.getTipoRegistro()))
				registreAnotacio.setTransportTipus(RegistreTransportTipusEnumDto.CORREU);
			else if ("03".equals(control.getTipoRegistro()))
				registreAnotacio.setTransportTipus(RegistreTransportTipusEnumDto.CORREU_CERTIFICAT);
			else if ("04".equals(control.getTipoRegistro()))
				registreAnotacio.setTransportTipus(RegistreTransportTipusEnumDto.BUROFAX);
			else if ("05".equals(control.getTipoRegistro()))
				registreAnotacio.setTransportTipus(RegistreTransportTipusEnumDto.EN_PERSONA);
			else if ("06".equals(control.getTipoRegistro()))
				registreAnotacio.setTransportTipus(RegistreTransportTipusEnumDto.FAX);
			else if ("07".equals(control.getTipoRegistro()))
				registreAnotacio.setTransportTipus(RegistreTransportTipusEnumDto.ALTRES);
			registreAnotacio.setTransportNumero(control.getNumeroTransporteEntrada());
			registreAnotacio.setUsuariNom(control.getNombreUsuario());
			registreAnotacio.setUsuariContacte(control.getContactoUsuario());
			registreAnotacio.setAplicacioEmissora(control.getAplicacionVersionEmisora());
			if ("1".equals(control.getDocumentacionFisica()))
				registreAnotacio.setDocumentacioFisica(RegistreDocumentacioFisicaTipusEnumDto.AMB_REQUERIDA);
			else if ("2".equals(control.getDocumentacionFisica()))
				registreAnotacio.setDocumentacioFisica(RegistreDocumentacioFisicaTipusEnumDto.AMB_COMPLEM);
			else if ("3".equals(control.getDocumentacionFisica()))
				registreAnotacio.setDocumentacioFisica(RegistreDocumentacioFisicaTipusEnumDto.SENSE);
			registreAnotacio.setObservacions(control.getObservacionesApunte());
			if ("0".equals(control.getIndicadorPrueba()))
				registreAnotacio.setProva(false);
			else if ("1".equals(control.getIndicadorPrueba()))
				registreAnotacio.setProva(true);
		}
		if (anotacio.getInteresados() != null) {
			List<RegistreInteressatDto> registreInteressats = new ArrayList<RegistreInteressatDto>();
			for (Interesado interesado: anotacio.getInteresados()) {
				RegistreInteressatDto interessat = new RegistreInteressatDto();
				interessat.setDocumentTipus(
						getDocumentTipusEnum(
								interesado.getTipoDocumentoIdentificacionInteresado()));
				interessat.setDocumentNum(interesado.getDocumentoIdentificacionInteresado());
				interessat.setNom(interesado.getNombreInteresado());
				interessat.setLlinatge1(interesado.getPrimerApellidoInteresado());
				interessat.setLlinatge2(interesado.getSegundoApellidoInteresado());
				interessat.setRaoSocial(interesado.getRazonSocialInteresado());
				interessat.setPais(interesado.getPaisInteresado());
				interessat.setProvincia(interesado.getProvinciaInteresado());
				interessat.setMunicipi(interesado.getMunicipioInteresado());
				interessat.setAdresa(interesado.getDireccionInteresado());
				interessat.setCodiPostal(interesado.getCodigoPostalInteresado());
				interessat.setEmail(interesado.getCorreoElectronicoInteresado());
				interessat.setTelefon(interesado.getTelefonoContactoInteresado());
				interessat.setEmailHabilitat(interesado.getDireccionElectronicaHabilitadaInteresado());
				interessat.setCanalPreferent(
						getCanalEnum(
								interesado.getCanalPreferenteComunicacionInteresado()));
				interessat.setObservacions(interesado.getObservaciones());
				interessat.setRepresentantDocumentTipus(
						getDocumentTipusEnum(
								interesado.getTipoDocumentoIdentificacionRepresentante()));
				interessat.setRepresentantDocumentNum(interesado.getDocumentoIdentificacionRepresentante());
				interessat.setRepresentantNom(interesado.getNombreRepresentante());
				interessat.setRepresentantLlinatge1(interesado.getPrimerApellidoRepresentante());
				interessat.setRepresentantLlinatge2(interesado.getSegundoApellidoRepresentante());
				interessat.setRepresentantRaoSocial(interesado.getRazonSocialRepresentante());
				interessat.setRepresentantPais(interesado.getPaisRepresentante());
				interessat.setRepresentantProvincia(interesado.getProvinciaRepresentante());
				interessat.setRepresentantMunicipi(interesado.getMunicipioRepresentante());
				interessat.setRepresentantAdresa(interesado.getDireccionRepresentante());
				interessat.setRepresentantCodiPostal(interesado.getCodigoPostalRepresentante());
				interessat.setRepresentantEmail(interesado.getCorreoElectronicoRepresentante());
				interessat.setRepresentantTelefon(interesado.getTelefonoContactoRepresentante());
				interessat.setRepresentantEmailHabilitat(interesado.getDireccionElectronicaHabilitadaRepresentante());
				interessat.setRepresentantCanalPreferent(
						getCanalEnum(
								interesado.getCanalPreferenteComunicacionRepresentante()));
				registreInteressats.add(interessat);
			}
			registreAnotacio.setInteressats(registreInteressats);
		}
		if (anotacio.getAnexos() != null) {
			List<RegistreDocumentDto> registreAnnexos = new ArrayList<RegistreDocumentDto>();
			for (Anexo anexo: anotacio.getAnexos()) {
				RegistreDocumentDto annex = new RegistreDocumentDto();
				annex.setFitxerNom(anexo.getNombreFichero());
				annex.setIdentificador(anexo.getIdentificador());
				if ("01".equals(anexo.getValidez()))
					annex.setValidesa(RegistreDocumentValidesaEnumDto.COPIA);
				else if ("02".equals(anexo.getValidez()))
					annex.setValidesa(RegistreDocumentValidesaEnumDto.COMPULSA);
				else if ("03".equals(anexo.getValidez()))
					annex.setValidesa(RegistreDocumentValidesaEnumDto.COPIA_ORIGINAL);
				else if ("04".equals(anexo.getValidez()))
					annex.setValidesa(RegistreDocumentValidesaEnumDto.ORIGINAL);
				if ("01".equals(anexo.getTipo()))
					annex.setTipus(RegistreDocumentTipusEnumDto.FORM);
				else if ("02".equals(anexo.getTipo()))
					annex.setTipus(RegistreDocumentTipusEnumDto.FORM_ADJUNT);
				else if ("03".equals(anexo.getTipo()))
					annex.setTipus(RegistreDocumentTipusEnumDto.INTERN);
				annex.setGestioDocumentalId(anexo.getGestionDocumentalId());
				annex.setIndentificadorDocumentFirmat(anexo.getIdentificadorDocumentoFirmado());
				annex.setObservacions(anexo.getObservaciones());
				registreAnnexos.add(annex);
			}
			registreAnotacio.setAnnexos(registreAnnexos);
		}
		return registreAnotacio;
	}

	private RegistreInteressatDocumentTipusEnumDto getDocumentTipusEnum(String documentTipus) {
		if ("N".equals(documentTipus))
			return RegistreInteressatDocumentTipusEnumDto.NIF;
		else if ("C".equals(documentTipus))
			return RegistreInteressatDocumentTipusEnumDto.CIF;
		else if ("P".equals(documentTipus))
			return RegistreInteressatDocumentTipusEnumDto.PASSAPORT;
		else if ("E".equals(documentTipus))
			return RegistreInteressatDocumentTipusEnumDto.ESTRANGER;
		else if ("X".equals(documentTipus))
			return RegistreInteressatDocumentTipusEnumDto.PERSONA_FIS;
		else if ("O".equals(documentTipus))
			return RegistreInteressatDocumentTipusEnumDto.CODI_ORIGEN;
		return null;
	}
	private RegistreInteressatCanalEnumDto getCanalEnum(String canal) {
		if ("01".equals(canal))
			return RegistreInteressatCanalEnumDto.POSTAL;
		else if ("02".equals(canal))
			return RegistreInteressatCanalEnumDto.EMAIL;
		else if ("03".equals(canal))
			return RegistreInteressatCanalEnumDto.COMP_ELEC;
		return null;
	}

	private static final Logger logger = LoggerFactory.getLogger(RegistreImpl.class);

}
