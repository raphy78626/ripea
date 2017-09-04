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

import es.caib.notib.ws.notificacio.NotificacioEstatEnum;
import es.caib.ripea.core.api.dto.DocumentEnviamentDto;
import es.caib.ripea.core.api.dto.DocumentNotificacioDto;
import es.caib.ripea.core.api.dto.DocumentPublicacioDto;
import es.caib.ripea.core.api.exception.NotFoundException;
import es.caib.ripea.core.api.service.ExpedientEnviamentService;

/**
 * Implementació de ContenidorService com a EJB que empra una clase
 * delegada per accedir a la funcionalitat del servei.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@Stateless
@Interceptors(SpringBeanAutowiringInterceptor.class)
public class ExpedientEnviamentServiceBean implements ExpedientEnviamentService {

	@Autowired
	ExpedientEnviamentService delegate;

	@Override
	@RolesAllowed("tothom")
	public DocumentNotificacioDto notificacioCreate(
			Long entitatId,
			Long documentId,
			Long interessatId,
			DocumentNotificacioDto notificacio) {
		return delegate.notificacioCreate(
				entitatId,
				documentId,
				interessatId,
				notificacio);
	}

	@Override
	@RolesAllowed("tothom")
	public DocumentNotificacioDto notificacioUpdate(
			Long entitatId,
			Long expedientId,
			DocumentNotificacioDto notificacio) {
		return delegate.notificacioUpdate(
				entitatId,
				expedientId,
				notificacio);
	}

	@Override
	@RolesAllowed("tothom")
	public DocumentNotificacioDto notificacioDelete(
			Long entitatId,
			Long expedientId,
			Long notificacioId) {
		return delegate.notificacioDelete(
				entitatId,
				expedientId,
				notificacioId);
	}

	@Override
	@RolesAllowed("tothom")
	public boolean notificacioRetry(
			Long entitatId,
			Long expedientId,
			Long notificacioId) {
		return delegate.notificacioRetry(
				entitatId,
				expedientId,
				notificacioId);
	}

	@Override
	@RolesAllowed("tothom")
	public DocumentNotificacioDto notificacioFindAmbId(
			Long entitatId,
			Long expedientId,
			Long notificacioId) {
		return delegate.notificacioFindAmbId(
				entitatId,
				expedientId,
				notificacioId);
	}

	@Override
	public void notificacioProcessarPendents() {
		delegate.notificacioProcessarPendents();
	}

	@Override
	@RolesAllowed("tothom")
	public DocumentPublicacioDto publicacioCreate(
			Long entitatId,
			Long documentId,
			DocumentPublicacioDto publicacio) {
		return delegate.publicacioCreate(
				entitatId,
				documentId,
				publicacio);
	}

	@Override
	@RolesAllowed("tothom")
	public DocumentPublicacioDto publicacioUpdate(
			Long entitatId,
			Long expedientId,
			DocumentPublicacioDto publicacio) {
		return delegate.publicacioUpdate(
				entitatId,
				expedientId,
				publicacio);
	}

	@Override
	@RolesAllowed("tothom")
	public DocumentPublicacioDto publicacioFindAmbId(
			Long entitatId,
			Long expedientId,
			Long publicacioId) {
		return delegate.publicacioFindAmbId(
				entitatId,
				expedientId,
				publicacioId);
	}
	
	@Override
	@RolesAllowed("tothom")
	public DocumentPublicacioDto publicacioDelete(
			Long entitatId,
			Long expedientId,
			Long publicacioId) {
		return delegate.publicacioDelete(
				entitatId,
				expedientId,
				publicacioId);
	}

	@Override
	@RolesAllowed("tothom")
	public List<DocumentEnviamentDto> enviamentFindAmbExpedient(
			Long entitatId,
			Long expedientId) {
		return delegate.enviamentFindAmbExpedient(
				entitatId,
				expedientId);
	}

	@Override
	@RolesAllowed("tothom")
	public DocumentNotificacioDto notificacioUpdatePerReferencia(String referencia, NotificacioEstatEnum estat,
			Date data) throws NotFoundException {
		return delegate.notificacioUpdatePerReferencia(referencia, estat, data);
	}

}
