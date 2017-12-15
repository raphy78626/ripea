/**
 * 
 */
package es.caib.ripea.core.service;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import es.caib.ripea.core.api.dto.EntitatDto;
import es.caib.ripea.core.api.dto.MetaDocumentDto;
import es.caib.ripea.core.api.dto.MetaDocumentFirmaFluxTipusEnumDto;
import es.caib.ripea.core.api.dto.MultiplicitatEnumDto;
import es.caib.ripea.core.api.dto.PermisDto;
import es.caib.ripea.core.api.dto.PrincipalTipusEnumDto;
import es.caib.ripea.core.api.exception.NotFoundException;
import es.caib.ripea.core.api.service.MetaDocumentService;
import es.caib.ripea.core.helper.PropertiesHelper;

/**
 * Tests per al servei d'entitats.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/es/caib/ripea/core/application-context-test.xml"})
@Transactional
public class MetaDocumentServiceTest extends BaseServiceTest {

	@Autowired
	private MetaDocumentService metaDocumentService;

	private EntitatDto entitat;
	private MetaDocumentDto metaDocumentCreate;
	private MetaDocumentDto metaDocumentUpdate;
	private PermisDto permisUserRead;

	@Before
	public void setUp() {
		PropertiesHelper.getProperties("classpath:es/caib/ripea/core/test.properties");
		entitat = new EntitatDto();
		entitat.setCodi("LIMIT");
		entitat.setNom("Limit Tecnologies");
		entitat.setCif("00000000T");
		entitat.setUnitatArrel(CODI_UNITAT_ARREL);
		List<PermisDto> permisos = new ArrayList<PermisDto>();
		PermisDto permisAdminAdmin = new PermisDto();
		permisAdminAdmin.setAdministration(true);
		permisAdminAdmin.setPrincipalTipus(PrincipalTipusEnumDto.USUARI);
		permisAdminAdmin.setPrincipalNom("admin");
		permisos.add(permisAdminAdmin);
		entitat.setPermisos(permisos);
		metaDocumentCreate = new MetaDocumentDto();
		metaDocumentCreate.setCodi("TEST1");
		metaDocumentCreate.setNom("Metadocument de test");
		metaDocumentCreate.setDescripcio("Descripció de test");
		metaDocumentCreate.setGlobalExpedient(false);
		metaDocumentCreate.setGlobalMultiplicitat(MultiplicitatEnumDto.M_0_1);
		metaDocumentCreate.setGlobalReadOnly(false);
		metaDocumentCreate.setFirmaPortafirmesActiva(false);
		metaDocumentCreate.setPortafirmesDocumentTipus("1234");
		metaDocumentCreate.setPortafirmesFluxId("1234");
		metaDocumentCreate.setPortafirmesResponsables(new String[] {"123456789Z"});
		metaDocumentCreate.setPortafirmesFluxTipus(MetaDocumentFirmaFluxTipusEnumDto.SERIE);
		metaDocumentCreate.setPortafirmesCustodiaTipus("1234");
		metaDocumentCreate.setFirmaPassarelaCustodiaTipus("1234");
		metaDocumentUpdate = new MetaDocumentDto();
		metaDocumentUpdate.setCodi("TEST2");
		metaDocumentUpdate.setNom("Metadocument de test2");
		metaDocumentUpdate.setDescripcio("Descripció de test2");
		metaDocumentUpdate.setGlobalExpedient(true);
		metaDocumentUpdate.setGlobalMultiplicitat(MultiplicitatEnumDto.M_0_1);
		metaDocumentUpdate.setGlobalReadOnly(true);
		metaDocumentUpdate.setFirmaPortafirmesActiva(true);
		metaDocumentUpdate.setPortafirmesDocumentTipus("12341");
		metaDocumentUpdate.setPortafirmesFluxId("12342");
		metaDocumentUpdate.setPortafirmesResponsables(new String[] {"00000000T"});
		metaDocumentUpdate.setPortafirmesFluxTipus(MetaDocumentFirmaFluxTipusEnumDto.SERIE);
		metaDocumentUpdate.setPortafirmesCustodiaTipus("12343");
		metaDocumentUpdate.setFirmaPassarelaCustodiaTipus("12344");
		permisUserRead = new PermisDto();
		permisUserRead.setRead(true);
		permisUserRead.setPrincipalTipus(PrincipalTipusEnumDto.USUARI);
		permisUserRead.setPrincipalNom("user");
	}

	@Test
    public void create() {
		testCreantElements(
				new TestAmbElementsCreats() {
					@Override
					public void executar(List<Object> elementsCreats) {
						MetaDocumentDto metaDocumentCreat = (MetaDocumentDto)elementsCreats.get(1);
						assertNotNull(metaDocumentCreat);
						assertNotNull(metaDocumentCreat.getId());
						comprovarMetaDocumentCoincideix(
								metaDocumentCreate,
								metaDocumentCreat);
						assertEquals(true, metaDocumentCreat.isActiu());
					}
				},
				entitat,
				metaDocumentCreate);
	}

	@Test
	public void findById() {
		testCreantElements(
				new TestAmbElementsCreats() {
					@Override
					public void executar(List<Object> elementsCreats) {
						autenticarUsuari("admin");
						EntitatDto entitatCreada = (EntitatDto)elementsCreats.get(0);
						MetaDocumentDto metaDocumentCreat = (MetaDocumentDto)elementsCreats.get(1);
						MetaDocumentDto trobat = metaDocumentService.findById(
								entitatCreada.getId(),
								metaDocumentCreat.getId());
						assertNotNull(trobat);
						assertNotNull(trobat.getId());
						comprovarMetaDocumentCoincideix(
								metaDocumentCreat,
								trobat);
					}
				},
				entitat,
				metaDocumentCreate);
    }

	@Test
    public void update() {
		testCreantElements(
				new TestAmbElementsCreats() {
					@Override
					public void executar(List<Object> elementsCreats) {
						autenticarUsuari("admin");
						EntitatDto entitatCreada = (EntitatDto)elementsCreats.get(0);
						MetaDocumentDto metaDocumentCreat = (MetaDocumentDto)elementsCreats.get(1);
						metaDocumentUpdate.setId(metaDocumentCreat.getId());
						MetaDocumentDto modificat = metaDocumentService.update(
								entitatCreada.getId(),
								metaDocumentUpdate,
								null,
								null,
								null);
						assertNotNull(modificat);
						assertNotNull(modificat.getId());
						assertEquals(
								metaDocumentCreat.getId(),
								modificat.getId());
						comprovarMetaDocumentCoincideix(
								metaDocumentUpdate,
								modificat);
						assertEquals(true, modificat.isActiu());
					}
				},
				entitat,
				metaDocumentCreate);
	}

	@Test
	public void delete() {
		testCreantElements(
				new TestAmbElementsCreats() {
					@Override
					public void executar(List<Object> elementsCreats) {
						autenticarUsuari("admin");
						EntitatDto entitatCreada = (EntitatDto)elementsCreats.get(0);
						MetaDocumentDto metaDocumentCreat = (MetaDocumentDto)elementsCreats.get(1);
						MetaDocumentDto esborrat = metaDocumentService.delete(
								entitatCreada.getId(),
								metaDocumentCreat.getId());
						comprovarMetaDocumentCoincideix(
								metaDocumentCreate,
								esborrat);
						try {
							metaDocumentService.findById(
									entitatCreada.getId(),
									metaDocumentCreat.getId());
							fail("El meta-document esborrat no s'hauria d'haver trobat");
						} catch (NotFoundException expected) {
						}
						elementsCreats.remove(metaDocumentCreat);
					}
				},
				entitat,
				metaDocumentCreate);
	}

	@Test
	public void updateActiu() {
		testCreantElements(
				new TestAmbElementsCreats() {
					@Override
					public void executar(List<Object> elementsCreats) {
						autenticarUsuari("admin");
						EntitatDto entitatCreada = (EntitatDto)elementsCreats.get(0);
						MetaDocumentDto metaDocumentCreat = (MetaDocumentDto)elementsCreats.get(1);
						MetaDocumentDto desactivat = metaDocumentService.updateActiu(
								entitatCreada.getId(),
								metaDocumentCreat.getId(),
								false);
						assertEquals(
								false,
								desactivat.isActiu());
						MetaDocumentDto activat = metaDocumentService.updateActiu(
								entitatCreada.getId(),
								metaDocumentCreat.getId(),
								true);
						assertEquals(
								true,
								activat.isActiu());
					}
				},
				entitat,
				metaDocumentCreate);
	}



	private void comprovarMetaDocumentCoincideix(
			MetaDocumentDto original,
			MetaDocumentDto perComprovar) {
		assertEquals(
				original.getCodi(),
				perComprovar.getCodi());
		assertEquals(
				original.getNom(),
				perComprovar.getNom());
		assertEquals(
				original.getDescripcio(),
				perComprovar.getDescripcio());
		assertEquals(
				original.isGlobalExpedient(),
				perComprovar.isGlobalExpedient());
		assertEquals(
				original.getGlobalMultiplicitat(),
				perComprovar.getGlobalMultiplicitat());
		assertEquals(
				original.isGlobalReadOnly(),
				perComprovar.isGlobalReadOnly());
		assertEquals(
				original.isFirmaPortafirmesActiva(),
				perComprovar.isFirmaPortafirmesActiva());
		assertEquals(
				original.getPortafirmesDocumentTipus(),
				perComprovar.getPortafirmesDocumentTipus());
		assertEquals(
				original.getPortafirmesFluxId(),
				perComprovar.getPortafirmesFluxId());
		assertArrayEquals(
				original.getPortafirmesResponsables(),
				perComprovar.getPortafirmesResponsables());
		assertEquals(
				original.getPortafirmesFluxTipus(),
				perComprovar.getPortafirmesFluxTipus());
		assertEquals(
				original.getPortafirmesCustodiaTipus(),
				perComprovar.getPortafirmesCustodiaTipus());
	}

}
