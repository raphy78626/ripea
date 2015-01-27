/**
 * 
 */
package es.caib.ripea.plugin.caib.portafirmes;

import static org.junit.Assert.assertTrue;

import java.util.Calendar;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;

import es.caib.ripea.plugin.portafirmes.PortafirmesDocument;
import es.caib.ripea.plugin.portafirmes.PortafirmesDocumentTipus;
import es.caib.ripea.plugin.portafirmes.PortafirmesPlugin;
import es.caib.ripea.plugin.portafirmes.PortafirmesPrioritatEnum;
import es.caib.ripea.plugin.utils.PropertiesHelper;

/**
 * Classe de proves pel portafirmes Portafib.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public class PortafirmesPluginPortafibTest {

	private PortafirmesPlugin plugin;

	private PortafirmesDocument uploadDocument;



	@Before
	public void setUp() throws Exception {
		PropertiesHelper.getProperties().setLlegirSystem(false);
		PropertiesHelper.getProperties().setProperty(
				"es.caib.ripea.plugin.portafirmes.portafib.service.url",
				"http://portafibcaib.fundaciobit.org/portafib/ws/v1/PortaFIBPeticioDeFirma");
		PropertiesHelper.getProperties().setProperty(
				"es.caib.ripea.plugin.portafirmes.portafib.username",
				"limit_app");
		PropertiesHelper.getProperties().setProperty(
				"es.caib.ripea.plugin.portafirmes.portafib.password",
				"limit_app");
		plugin = new PortafirmesPluginPortafib();
		uploadDocument = new PortafirmesDocument();
		uploadDocument.setTitol("(RIP) Document per firmar");
		uploadDocument.setArxiuNom("document_firma.pdf");
		uploadDocument.setArxiuContingut(
			IOUtils.toByteArray(getClass().getResource(
	        		"/es/caib/ripea/plugin/caib/document_firma.pdf")));
		uploadDocument.setFirmat(false);
	}

	@Test
	public void findTipusDocument() throws Exception {
		List<PortafirmesDocumentTipus> tipus = plugin.findDocumentTipus();
		assertTrue(tipus != null);
		assertTrue(tipus.size() > 0);
		/*for (PortafirmesDocumentTipus t: tipus)
			System.out.println(">>> " + t.getId() + ", " + t.getNom());
		long instanciaFluxId = ((PortafirmesPluginPortafib)plugin).instanciaFluxFirma(
				new Long(353));
		System.out.println(">>> instanciaFluxId: " + instanciaFluxId);*/
	}

	@Test
	public void uploadAndDelete() throws Exception {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, 1);
		long documentId = plugin.upload(
				uploadDocument,
				new Long(99),
				"Prova d'enviament RIPEA",
				"Aplicació RIPEA",
				PortafirmesPrioritatEnum.NORMAL,
				cal.getTime(),
				null,
				new Long(353),
				null,
				false);
		plugin.delete(documentId);
	}

	/*@Test
	public void download() throws Exception {
		long documentId = 30078; // 30078
		PortafirmesDocument downloadDocument = plugin.download(documentId);
		assertThat(
				downloadDocument.getTitol(),
				is(uploadDocument.getTitol()));
		assertThat(
				downloadDocument.getArxiuNom(),
				is(uploadDocument.getArxiuNom()));
		assertThat(
				downloadDocument.getArxiuContingut().length,
				is(uploadDocument.getArxiuContingut().length));
		assertThat(
				downloadDocument.isFirmat(),
				is(true));
	}*/

}
