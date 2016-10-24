/**
 * 
 */
package es.caib.ripea.core.ejb;

import java.util.Date;
import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;

import es.caib.ripea.core.api.dto.DocumentDto;
import es.caib.ripea.core.api.dto.DocumentPortafirmesDto;
import es.caib.ripea.core.api.dto.DocumentVersioDto;
import es.caib.ripea.core.api.dto.FitxerDto;
import es.caib.ripea.core.api.dto.PortafirmesPrioritatEnumDto;
import es.caib.ripea.core.api.exception.NotFoundException;
import es.caib.ripea.core.api.service.DocumentService;

/**
 * Implementació de ContenidorService com a EJB que empra una clase
 * delegada per accedir a la funcionalitat del servei.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@Stateless
@Interceptors(SpringBeanAutowiringInterceptor.class)
public class DocumentServiceBean implements DocumentService {

	@Autowired
	DocumentService delegate;



	@Override
	@RolesAllowed("tothom")
	public DocumentDto create(
			Long entitatId,
			Long contenidorId,
			DocumentDto document,
			FitxerDto fitxer) {
		return delegate.create(
				entitatId,
				contenidorId,
				document,
				fitxer);
	}

	@Override
	@RolesAllowed("tothom")
	public DocumentDto update(
			Long entitatId,
			Long id,
			DocumentDto document,
			FitxerDto fitxer) {
		return delegate.update(
				entitatId,
				id,
				document,
				fitxer);
	}

	@Override
	@RolesAllowed("tothom")
	public DocumentDto delete(
			Long entitatId,
			Long id) {
		return delegate.delete(
				entitatId,
				id);
	}

	@Override
	@RolesAllowed("tothom")
	public DocumentDto findById(
			Long entitatId,
			Long id) {
		return delegate.findById(entitatId, id);
	}

	@Override
	@RolesAllowed("tothom")
	public List<DocumentVersioDto> findVersionsByDocument(
			Long entitatId,
			Long id) {
		return delegate.findVersionsByDocument(entitatId, id);
	}

	@Override
	@RolesAllowed("tothom")
	public DocumentVersioDto findDarreraVersio(
			Long entitatId,
			Long id) {
		return delegate.findDarreraVersio(entitatId, id);
	}

	@Override
	@RolesAllowed("tothom")
	public DocumentVersioDto findVersio(
			Long entitatId,
			Long id,
			int versio) {
		return delegate.findVersio(entitatId, id, versio);
	}

	@Override
	public List<DocumentDto> findAmbExpedientIPermisRead(
			Long entitatId,
			Long expedientId) throws NotFoundException {
		return delegate.findAmbExpedientIPermisRead(
				entitatId,
				expedientId);
	}

	@Override
	@RolesAllowed("tothom")
	public FitxerDto descarregar(
			Long entitatId,
			Long id,
			int versio) {
		return delegate.descarregar(entitatId, id, versio);
	}

	@Override
	@RolesAllowed("tothom")
	public void portafirmesEnviar(
			Long entitatId,
			Long id,
			String motiu,
			PortafirmesPrioritatEnumDto prioritat,
			Date dataCaducitat) {
		delegate.portafirmesEnviar(
				entitatId,
				id,
				motiu,
				prioritat,
				dataCaducitat);
	}

	@Override
	@RolesAllowed("tothom")
	public void portafirmesCancelar(
			Long entitatId,
			Long id) {
		delegate.portafirmesCancelar(
				entitatId,
				id);
	}

	@Override
	public Exception portafirmesCallback(
			long documentId,
			PortafirmesCallbackEstatEnum estat) {
		return delegate.portafirmesCallback(documentId, estat);
	}

	@Override
	@RolesAllowed("tothom")
	public void portafirmesReintentar(
			Long entitatId,
			Long id) {
		delegate.portafirmesReintentar(
				entitatId,
				id);
	}

	@Override
	@RolesAllowed("tothom")
	public DocumentPortafirmesDto portafirmesInfo(
			Long entitatId,
			Long documentId) {
		return delegate.portafirmesInfo(
				entitatId,
				documentId);
	}

	@Override
	@RolesAllowed("tothom")
	public FitxerDto convertirPdfPerFirma(
			Long entitatId,
			Long id) {
		return delegate.convertirPdfPerFirma(
				entitatId,
				id);
	}

	@Override
	@RolesAllowed("tothom")
	public String generarIdentificadorFirmaClient(
			Long entitatId,
			Long id) {
		return delegate.generarIdentificadorFirmaClient(
				entitatId,
				id);
	}

	@Override
	@RolesAllowed("tothom")
	public void processarFirmaClient(
			String identificador,
			String arxiuNom,
			byte[] arxiuContingut) {
		delegate.processarFirmaClient(
				identificador,
				arxiuNom,
				arxiuContingut);
	}

	@Override
	@RolesAllowed("tothom")
	public void custodiaEsborrar(
			Long entitatId,
			Long id) {
		delegate.custodiaEsborrar(
				entitatId,
				id);
	}

}
