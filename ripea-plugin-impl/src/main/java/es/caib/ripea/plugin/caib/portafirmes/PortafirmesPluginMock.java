/**
 * 
 */
package es.caib.ripea.plugin.caib.portafirmes;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import es.caib.ripea.plugin.SistemaExternException;
import es.caib.ripea.plugin.portafirmes.PortafirmesDocument;
import es.caib.ripea.plugin.portafirmes.PortafirmesDocumentTipus;
import es.caib.ripea.plugin.portafirmes.PortafirmesFluxBloc;
import es.caib.ripea.plugin.portafirmes.PortafirmesPlugin;
import es.caib.ripea.plugin.portafirmes.PortafirmesPrioritatEnum;

/**
 * Implementació del plugin de portafirmes per fer proves sense accés
 * a cap sistema extern.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public class PortafirmesPluginMock implements PortafirmesPlugin {

	@Override
	public long upload(
			PortafirmesDocument document,
			Long documentTipus,
			String motiu,
			String remitent,
			PortafirmesPrioritatEnum prioritat,
			Date dataCaducitat,
			List<PortafirmesFluxBloc> flux,
			Long plantillaFluxId,
			List<PortafirmesDocument> annexos,
			boolean signarAnnexos) throws SistemaExternException {
		return System.currentTimeMillis();
	}

	@Override
	public PortafirmesDocument download(
			long id) throws SistemaExternException {
		return null;
	}

	@Override
	public void delete(
			long id) throws SistemaExternException {
		
	}

	@Override
	public List<PortafirmesDocumentTipus> findDocumentTipus() throws SistemaExternException {
		
		List<PortafirmesDocumentTipus> resposta = new ArrayList<PortafirmesDocumentTipus>();
		PortafirmesDocumentTipus tipusDocument = new PortafirmesDocumentTipus();
		for(int i = 0; i < 5; i++) {
			tipusDocument = new PortafirmesDocumentTipus();
			tipusDocument.setId(new Long(i));
			tipusDocument.setCodi("T" + i);
			tipusDocument.setNom("Tipus " + i);
			resposta.add(tipusDocument);				
		}		
		return resposta;
	}

	@Override
	public boolean isCustodiaAutomatica() {
		return false;
	}

}
