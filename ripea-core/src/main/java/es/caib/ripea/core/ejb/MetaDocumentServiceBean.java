/**
 * 
 */
package es.caib.ripea.core.ejb;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;

import es.caib.ripea.core.api.dto.FitxerDto;
import es.caib.ripea.core.api.dto.MetaDocumentDto;
import es.caib.ripea.core.api.dto.MetaNodeMetaDadaDto;
import es.caib.ripea.core.api.dto.MultiplicitatEnumDto;
import es.caib.ripea.core.api.dto.PaginaDto;
import es.caib.ripea.core.api.dto.PaginacioParamsDto;
import es.caib.ripea.core.api.dto.PermisDto;
import es.caib.ripea.core.api.dto.PortafirmesDocumentTipusDto;
import es.caib.ripea.core.api.exception.ContenidorNotFoundException;
import es.caib.ripea.core.api.exception.EntitatNotFoundException;
import es.caib.ripea.core.api.exception.MetaDadaNotFoundException;
import es.caib.ripea.core.api.exception.MetaDocumentNotFoundException;
import es.caib.ripea.core.api.service.MetaDocumentService;

/**
 * Implementació de MetaDocumentService com a EJB que empra una clase
 * delegada per accedir a la funcionalitat del servei.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@Stateless
@Interceptors(SpringBeanAutowiringInterceptor.class)
public class MetaDocumentServiceBean implements MetaDocumentService {

	@Autowired
	MetaDocumentService delegate;



	@Override
	@RolesAllowed("IPA_ADMIN")
	public MetaDocumentDto create(
			Long entitatId,
			MetaDocumentDto metaDocument,
			String plantillaNom,
			String plantillaContentType,
			byte[] plantillaContingut) {
		return delegate.create(
				entitatId,
				metaDocument,
				plantillaNom,
				plantillaContentType,
				plantillaContingut);
	}

	@Override
	@RolesAllowed("IPA_ADMIN")
	public MetaDocumentDto update(
			Long entitatId,
			MetaDocumentDto metaDocument,
			String plantillaNom,
			String plantillaContentType,
			byte[] plantillaContingut) throws EntitatNotFoundException, MetaDocumentNotFoundException {
		return delegate.update(
				entitatId,
				metaDocument,
				plantillaNom,
				plantillaContentType,
				plantillaContingut);
	}

	@Override
	@RolesAllowed("IPA_ADMIN")
	public MetaDocumentDto updateActiu(
			Long entitatId,
			Long id,
			boolean actiu) throws EntitatNotFoundException, MetaDocumentNotFoundException {
		return delegate.updateActiu(entitatId, id, actiu);
	}

	@Override
	@RolesAllowed("IPA_ADMIN")
	public MetaDocumentDto delete(
			Long entitatId,
			Long metaDocumentId) throws EntitatNotFoundException, MetaDocumentNotFoundException {
		return delegate.delete(entitatId, metaDocumentId);
	}

	@Override
	@RolesAllowed("IPA_ADMIN")
	public MetaDocumentDto findById(
			Long entitatId,
			Long id) throws EntitatNotFoundException {
		return delegate.findById(
				entitatId,
				id);
	}

	@Override
	@RolesAllowed("IPA_ADMIN")
	public MetaDocumentDto findByEntitatCodi(
			Long entitatId,
			String codi) throws EntitatNotFoundException {
		return delegate.findByEntitatCodi(entitatId, codi);
	}

	@Override
	@RolesAllowed("IPA_ADMIN")
	public List<MetaDocumentDto> findByEntitat(
			Long entitatId) throws EntitatNotFoundException {
		return delegate.findByEntitat(entitatId);
	}

	@Override
	@RolesAllowed("IPA_ADMIN")
	public PaginaDto<MetaDocumentDto> findByEntitatPaginat(
			Long entitatId,
			PaginacioParamsDto paginacioParams) throws EntitatNotFoundException {
		return delegate.findByEntitatPaginat(entitatId, paginacioParams);
	}

	@Override
	@RolesAllowed("IPA_ADMIN")
	public List<MetaDocumentDto> findByEntitatAndActiveTrue(
			Long entitatId,
			boolean incloureGlobalsExpedient) throws EntitatNotFoundException {
		return delegate.findByEntitatAndActiveTrue(
				entitatId,
				incloureGlobalsExpedient);
	}

	@Override
	@RolesAllowed({"IPA_ADMIN", "tothom"})
	public FitxerDto getPlantilla(
			Long entitatId,
			Long id) throws EntitatNotFoundException, MetaDocumentNotFoundException {
		return delegate.getPlantilla(entitatId, id);
	}

	@Override
	@RolesAllowed("IPA_ADMIN")
	public void metaDadaCreate(
			Long entitatId,
			Long id,
			Long metaDadaId,
			MultiplicitatEnumDto multiplicitat,
			boolean readOnly) throws EntitatNotFoundException, MetaDocumentNotFoundException, MetaDadaNotFoundException {
		delegate.metaDadaCreate(entitatId, id, metaDadaId, multiplicitat, readOnly);
	}

	@Override
	@RolesAllowed("IPA_ADMIN")
	public void metaDadaUpdate(
			Long entitatId,
			Long id,
			Long metaNodeMetaDadaId,
			MultiplicitatEnumDto multiplicitat,
			boolean readOnly) throws EntitatNotFoundException, MetaDocumentNotFoundException, MetaDadaNotFoundException {
		delegate.metaDadaUpdate(entitatId, id, metaNodeMetaDadaId, multiplicitat, readOnly);
	}

	@Override
	@RolesAllowed("IPA_ADMIN")
	public void metaDadaDelete(
			Long entitatId,
			Long id,
			Long metaDadaId) throws EntitatNotFoundException, MetaDocumentNotFoundException, MetaDadaNotFoundException {
		delegate.metaDadaDelete(entitatId, id, metaDadaId);
	}

	@Override
	@RolesAllowed("IPA_ADMIN")
	public void metaDadaMove(
			Long entitatId,
			Long id,
			Long metaDadaId,
			int posicio) throws EntitatNotFoundException, MetaDocumentNotFoundException, MetaDadaNotFoundException {
		delegate.metaDadaMove(entitatId, id, metaDadaId, posicio);
	}

	@Override
	@RolesAllowed("IPA_ADMIN")
	public MetaNodeMetaDadaDto findMetaDada(
			Long entitatId,
			Long id,
			Long metaNodeMetaDadaId) throws EntitatNotFoundException, MetaDocumentNotFoundException, MetaDadaNotFoundException {
		return delegate.findMetaDada(entitatId, id, metaNodeMetaDadaId);
	}

	@Override
	@RolesAllowed("IPA_ADMIN")
	public List<PermisDto> findPermis(Long entitatId, Long id)
			throws EntitatNotFoundException, MetaDocumentNotFoundException {
		return delegate.findPermis(entitatId, id);
	}

	@Override
	@RolesAllowed("IPA_ADMIN")
	public void updatePermis(Long entitatId, Long id, PermisDto permis)
			throws EntitatNotFoundException, MetaDocumentNotFoundException {
		delegate.updatePermis(entitatId, id, permis);
	}

	@Override
	@RolesAllowed("IPA_ADMIN")
	public void deletePermis(Long entitatId, Long id, Long permisId)
			throws EntitatNotFoundException, MetaDocumentNotFoundException {
		delegate.deletePermis(entitatId, id, permisId);
	}

	@Override
	@RolesAllowed("tothom")
	public List<MetaDocumentDto> findActiveByEntitatAndContenidorPerCreacio(
			Long entitatId,
			Long contenidorId) throws EntitatNotFoundException, ContenidorNotFoundException {
		return delegate.findActiveByEntitatAndContenidorPerCreacio(
				entitatId,
				contenidorId);
	}

	@Override
	@RolesAllowed("IPA_ADMIN")
	public List<PortafirmesDocumentTipusDto> findPortafirmesDocumentTipus() {
		return delegate.findPortafirmesDocumentTipus();
	}

}
