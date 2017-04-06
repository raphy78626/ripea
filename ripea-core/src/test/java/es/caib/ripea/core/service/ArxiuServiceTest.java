/**
 * 
 */
package es.caib.ripea.core.service;

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

import es.caib.ripea.core.api.dto.ArxiuDto;
import es.caib.ripea.core.api.dto.EntitatDto;
import es.caib.ripea.core.api.dto.PermisDto;
import es.caib.ripea.core.api.dto.PrincipalTipusEnumDto;
import es.caib.ripea.core.api.exception.NotFoundException;
import es.caib.ripea.core.api.service.ArxiuService;
import es.caib.ripea.core.helper.PropertiesHelper;

/**
 * Tests per al servei d'entitats.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/es/caib/ripea/core/application-context-test.xml"})
@Transactional
public class ArxiuServiceTest extends BaseServiceTest {

	@Autowired
	private ArxiuService arxiuService;

	private EntitatDto entitat;
	private ArxiuDto arxiuCreate;
	private ArxiuDto arxiuUpdate;
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
		arxiuCreate = new ArxiuDto();
		arxiuCreate.setNom("Arxiu de test");
		arxiuCreate.setUnitatCodi(CODI_UNITAT_ARREL);
		arxiuUpdate = new ArxiuDto();
		arxiuUpdate.setNom("Arxiu de test2");
		arxiuUpdate.setUnitatCodi(CODI_UNITAT_ARREL);
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
						ArxiuDto arxiuCreat = (ArxiuDto)elementsCreats.get(1);
						assertNotNull(arxiuCreat);
						assertNotNull(arxiuCreat.getId());
						comprovarArxiuCoincideix(
								arxiuCreate,
								arxiuCreat);
					}
				},
				entitat,
				arxiuCreate);
	}

	@Test
	public void findById() {
		testCreantElements(
				new TestAmbElementsCreats() {
					@Override
					public void executar(List<Object> elementsCreats) {
						autenticarUsuari("admin");
						EntitatDto entitatCreada = (EntitatDto)elementsCreats.get(0);
						ArxiuDto arxiuCreat = (ArxiuDto)elementsCreats.get(1);
						ArxiuDto trobat = arxiuService.findById(
								entitatCreada.getId(),
								arxiuCreat.getId());
						assertNotNull(trobat);
						assertNotNull(trobat.getId());
						comprovarArxiuCoincideix(
								arxiuCreat,
								trobat);
					}
				},
				entitat,
				arxiuCreate);
    }

	@Test
    public void update() {
		testCreantElements(
				new TestAmbElementsCreats() {
					@Override
					public void executar(List<Object> elementsCreats) {
						autenticarUsuari("admin");
						EntitatDto entitatCreada = (EntitatDto)elementsCreats.get(0);
						ArxiuDto arxiuCreat = (ArxiuDto)elementsCreats.get(1);
						arxiuUpdate.setId(arxiuCreat.getId());
						ArxiuDto modificat = arxiuService.update(
								entitatCreada.getId(),
								arxiuUpdate);
						assertNotNull(modificat);
						assertNotNull(modificat.getId());
						assertEquals(
								arxiuCreat.getId(),
								modificat.getId());
						comprovarArxiuCoincideix(
								arxiuUpdate,
								modificat);
						assertEquals(true, modificat.isActiu());
					}
				},
				entitat,
				arxiuCreate);
	}

	@Test
	public void delete() {
		testCreantElements(
				new TestAmbElementsCreats() {
					@Override
					public void executar(List<Object> elementsCreats) {
						autenticarUsuari("admin");
						EntitatDto entitatCreada = (EntitatDto)elementsCreats.get(0);
						ArxiuDto arxiuCreat = (ArxiuDto)elementsCreats.get(1);
						ArxiuDto esborrat = arxiuService.delete(
								entitatCreada.getId(),
								arxiuCreat.getId());
						comprovarArxiuCoincideix(
								arxiuCreate,
								esborrat);
						try {
							arxiuService.findById(
									entitatCreada.getId(),
									arxiuCreat.getId());
							fail("L'arxiu esborrat no s'hauria d'haver trobat");
						} catch (NotFoundException expected) {
						}
						elementsCreats.remove(arxiuCreat);
					}
				},
				entitat,
				arxiuCreate);
	}

	@Test
	public void updateActiu() {
		testCreantElements(
				new TestAmbElementsCreats() {
					@Override
					public void executar(List<Object> elementsCreats) {
						autenticarUsuari("admin");
						EntitatDto entitatCreada = (EntitatDto)elementsCreats.get(0);
						ArxiuDto arxiuCreat = (ArxiuDto)elementsCreats.get(1);
						ArxiuDto desactivat = arxiuService.updateActiu(
								entitatCreada.getId(),
								arxiuCreat.getId(),
								false);
						assertEquals(
								false,
								desactivat.isActiu());
						ArxiuDto activat = arxiuService.updateActiu(
								entitatCreada.getId(),
								arxiuCreat.getId(),
								true);
						assertEquals(
								true,
								activat.isActiu());
					}
				},
				entitat,
				arxiuCreate);
	}



	private void comprovarArxiuCoincideix(
			ArxiuDto original,
			ArxiuDto perComprovar) {
		assertEquals(
				original.getNom(),
				perComprovar.getNom());
	}

}
