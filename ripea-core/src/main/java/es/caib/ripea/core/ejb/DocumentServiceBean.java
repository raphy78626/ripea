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
import es.caib.ripea.core.api.dto.DocumentVersioDto;
import es.caib.ripea.core.api.dto.FitxerDto;
import es.caib.ripea.core.api.dto.PortafirmesPrioritatEnumDto;
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
			Long metaDocumentId,
			String nom,
			Date data,
			String arxiuNom,
			String arxiuContentType,
			byte[] arxiuContingut) {
		return delegate.create(
				entitatId, contenidorId,
				metaDocumentId,
				nom,
				data,
				arxiuNom,
				arxiuContentType,
				arxiuContingut);
	}

	@Override
	@RolesAllowed("tothom")
	public DocumentDto update(
			Long entitatId,
			Long id,
			Long metaDocumentId,
			String nom,
			Date data,
			String arxiuNom,
			String arxiuContentType,
			byte[] arxiuContingut) {
		return delegate.update(
				entitatId,
				id,
				metaDocumentId,
				nom,
				data,
				arxiuNom,
				arxiuContentType,
				arxiuContingut);
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
			int versio,
			String motiu,
			PortafirmesPrioritatEnumDto prioritat,
			Date dataCaducitat) {
		delegate.portafirmesEnviar(
				entitatId,
				id,
				versio,
				motiu,
				prioritat,
				dataCaducitat);
	}

	@Override
	@RolesAllowed("tothom")
	public void portafirmesCancelar(
			Long entitatId,
			Long id,
			int versio) {
		delegate.portafirmesCancelar(
				entitatId,
				id,
				versio);
	}

	@Override
	public Exception portafirmesCallback(int documentId, int estat) {
		return delegate.portafirmesCallback(documentId, estat);
	}

	@Override
	@RolesAllowed("tothom")
	public FitxerDto convertirPdf(
			Long entitatId,
			Long id,
			int versio) {
		return convertirPdf(
				entitatId,
				id,
				versio);
	}

	@Override
	@RolesAllowed("tothom")
	public String generarIdentificadorFirmaApplet(
			Long entitatId,
			Long id,
			int versio) {
		return delegate.generarIdentificadorFirmaApplet(
				entitatId,
				id,
				versio);
	}

	@Override
	@RolesAllowed("tothom")
	public void custodiaEnviarDocumentFirmat(
			String identificador,
			String arxiuNom,
			byte[] arxiuContingut) {
		delegate.custodiaEnviarDocumentFirmat(
				identificador,
				arxiuNom,
				arxiuContingut);
	}

	@Override
	@RolesAllowed("tothom")
	public void custodiaPortafirmesReintentar(
			Long entitatId,
			Long id,
			int versio) {
		delegate.custodiaPortafirmesReintentar(
				entitatId,
				id,
				versio);
	}

}
