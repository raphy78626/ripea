/**
 * 
 */
package es.caib.ripea.core.service;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import es.caib.ripea.core.api.dto.EntitatDto;
import es.caib.ripea.core.api.dto.PermisDto;
import es.caib.ripea.core.api.dto.PrincipalTipusEnumDto;
import es.caib.ripea.core.api.service.EntitatService;

/**
 * Tests per al servei d'entitats.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/es/caib/ripea/core/context/application-context-test.xml"})
@Transactional
public class EntitatServiceTest extends BaseServiceTest {

	@Autowired
	private EntitatService entitatService;

	private EntitatDto entitatCreate;
	private EntitatDto entitatUpdate;
	private PermisDto permisUserRead;

	@Before
	public void setUp() {
		entitatCreate = new EntitatDto();
		entitatCreate.setCodi("LIMIT");
		entitatCreate.setNom("Limit Tecnologies");
		entitatCreate.setCif("12345678Z");
		entitatCreate.setUnitatArrel("LIM000001");
		entitatUpdate = new EntitatDto();
		entitatUpdate.setId(new Long(1));
		entitatUpdate.setCodi("LIMITE");
		entitatUpdate.setNom("Limit Tecnologies 0");
		entitatUpdate.setCif("12345678T");
		entitatUpdate.setUnitatArrel("LIM000002");
		permisUserRead = new PermisDto();
		permisUserRead.setRead(true);
		permisUserRead.setPrincipalTipus(PrincipalTipusEnumDto.USUARI);
		permisUserRead.setPrincipalNom("user");
	}

	@Test
    public void create() {
		autenticarUsuari("super");
		assertNotNull(entitatCreate);
		assertNull(entitatCreate.getId());
		EntitatDto entitatCreada = entitatService.create(entitatCreate);
		assertNotNull(entitatCreada);
		assertNotNull(entitatCreada.getId());
	}
	@Test
	public void findById() {
		autenticarUsuari("super");
		EntitatDto entitatCreada = entitatService.create(entitatCreate);
		assertNotNull(entitatService.findById(entitatCreada.getId()));
		assertThat(
				entitatCreate.getCodi(),
				is(entitatCreada.getCodi()));
		assertThat(
				entitatCreate.getNom(),
				is(entitatCreada.getNom()));
		assertThat(
				entitatCreate.getCif(),
				is(entitatCreada.getCif()));
    }
	@Test
	public void update() {
		autenticarUsuari("super");
		EntitatDto entitatCreada = entitatService.create(entitatCreate);
		assertNotNull(entitatService.findById(entitatCreada.getId()));
		assertThat(
				entitatCreate.getCodi(),
				not(entitatUpdate.getCodi()));
		assertThat(
				entitatCreate.getNom(),
				not(entitatUpdate.getNom()));
		assertThat(
				entitatCreate.getCif(),
				not(entitatUpdate.getCif()));
		entitatUpdate.setId(entitatCreada.getId());
		EntitatDto entitatModificada = entitatService.update(entitatUpdate);
		assertThat(
				entitatUpdate.getCodi(),
				is(entitatModificada.getCodi()));
		assertThat(
				entitatUpdate.getNom(),
				is(entitatModificada.getNom()));
		assertThat(
				entitatUpdate.getCif(),
				is(entitatModificada.getCif()));
    }
	@Test
	public void delete() {
		autenticarUsuari("super");
		EntitatDto entitatCreada = entitatService.create(entitatCreate);
		assertNotNull(entitatService.findById(entitatCreada.getId()));
		EntitatDto entitatEsborrada = entitatService.delete(entitatCreada.getId());
		assertThat(
				entitatCreate.getCodi(),
				is(entitatEsborrada.getCodi()));
		assertThat(
				entitatCreate.getNom(),
				is(entitatEsborrada.getNom()));
		assertThat(
				entitatCreate.getCif(),
				is(entitatEsborrada.getCif()));
		assertNull(entitatService.findById(entitatCreada.getId()));
	}

	@Test
	public void updateActiva() {
		autenticarUsuari("super");
		EntitatDto entitatCreada = entitatService.create(entitatCreate);
		assertNotNull(entitatCreada.getId());
		entitatService.updateActiva(entitatCreada.getId(), false);
		EntitatDto entitatRecuperada = entitatService.findById(entitatCreada.getId());
		assertFalse(entitatRecuperada.isActiva());
		entitatService.updateActiva(entitatCreada.getId(), true);
		entitatRecuperada = entitatService.findById(entitatCreada.getId());
		assertTrue(entitatRecuperada.isActiva());
	}

	@Test
	public void findByCodi() {
		autenticarUsuari("super");
		EntitatDto entitatCreada = entitatService.create(entitatCreate);
		assertNotNull(entitatService.findById(entitatCreada.getId()));
		EntitatDto entitatAmbCodi = entitatService.findByCodi(entitatCreada.getCodi());
		assertNotNull(entitatAmbCodi);
		assertNotNull(entitatAmbCodi.getId());
		assertThat(
				entitatCreada.getId(),
				is(entitatAmbCodi.getId()));
	}

	/*@Test
	@Rollback
	public void findAccessiblesUsuariActual() {
		autenticarUsuari("user");
		List<EntitatDto> entitatsAccessibles = entitatService.findAccessiblesUsuariActual();
		assertThat(
				entitatsAccessibles.size(),
				is(0));
		autenticarUsuari("super");
		EntitatDto entitatCreada = entitatService.create(entitatCreate);
		entitatService.updatePermisSuper(
				entitatCreada.getId(),
				permisUserRead);
		autenticarUsuari("user");
		entitatsAccessibles = entitatService.findAccessiblesUsuariActual();
		assertThat(
				entitatsAccessibles.size(),
				is(1));
	}*/

	@Test
	public void managePermisSuper() {
		autenticarUsuari("user");
		List<EntitatDto> entitatsAccessibles = entitatService.findAccessiblesUsuariActual();
		assertThat(
				entitatsAccessibles.size(),
				is(0));
		autenticarUsuari("super");
		EntitatDto entitatCreada = entitatService.create(entitatCreate);
		List<PermisDto> permisos = entitatService.findPermisSuper(entitatCreada.getId());
		assertThat(
				permisos.size(),
				is(0));
		entitatService.updatePermisSuper(
				entitatCreada.getId(),
				permisUserRead);
		permisos = entitatService.findPermisSuper(entitatCreada.getId());
		assertThat(
				permisos.size(),
				is(1));
		assertThat(
				permisos.get(0).getPrincipalNom(),
				is(permisUserRead.getPrincipalNom()));
		assertThat(
				permisos.get(0).getPrincipalTipus(),
				is(permisUserRead.getPrincipalTipus()));
		autenticarUsuari("user");
		entitatsAccessibles = entitatService.findAccessiblesUsuariActual();
		assertThat(
				entitatsAccessibles.size(),
				is(1));
		assertThat(
				entitatsAccessibles.get(0).getId(),
				is(entitatCreada.getId()));
		autenticarUsuari("super");
		entitatService.deletePermisSuper(
				entitatCreada.getId(),
				permisos.get(0).getId());
		permisos = entitatService.findPermisSuper(entitatCreada.getId());
		assertThat(
				permisos.size(),
				is(0));
		autenticarUsuari("user");
		entitatsAccessibles = entitatService.findAccessiblesUsuariActual();
		assertThat(
				entitatsAccessibles.size(),
				is(0));
	}

	@Test
	public void managePermisAdmin() {
		autenticarUsuari("user");
		List<EntitatDto> entitatsAccessibles = entitatService.findAccessiblesUsuariActual();
		assertThat(
				entitatsAccessibles.size(),
				is(0));
		autenticarUsuari("super");
		EntitatDto entitatCreada = entitatService.create(entitatCreate);
		PermisDto permisAdminAdmin = new PermisDto();
		permisAdminAdmin.setAdministration(true);
		permisAdminAdmin.setPrincipalTipus(PrincipalTipusEnumDto.USUARI);
		permisAdminAdmin.setPrincipalNom("admin");
		entitatService.updatePermisSuper(
				entitatCreada.getId(),
				permisAdminAdmin);
		autenticarUsuari("admin");
		List<PermisDto> permisos = entitatService.findPermisAdmin(entitatCreada.getId());
		assertThat(
				permisos.size(),
				is(1));
		entitatService.updatePermisAdmin(
				entitatCreada.getId(),
				permisUserRead);
		permisos = entitatService.findPermisAdmin(entitatCreada.getId());
		PermisDto permisCreatPerAdmin = null;
		for (PermisDto permis: permisos) {
			if (permis.isRead()) {
				permisCreatPerAdmin = permis;
				break;
			}
		}
		assertNotNull(permisCreatPerAdmin);
		assertThat(
				permisos.size(),
				is(2));
		assertThat(
				permisCreatPerAdmin.getPrincipalNom(),
				is(permisUserRead.getPrincipalNom()));
		assertThat(
				permisCreatPerAdmin.getPrincipalTipus(),
				is(permisUserRead.getPrincipalTipus()));
		autenticarUsuari("user");
		entitatsAccessibles = entitatService.findAccessiblesUsuariActual();
		assertThat(
				entitatsAccessibles.size(),
				is(1));
		assertThat(
				entitatsAccessibles.get(0).getId(),
				is(entitatCreada.getId()));
		autenticarUsuari("admin");
		entitatService.deletePermisAdmin(
				entitatCreada.getId(),
				permisCreatPerAdmin.getId());
		permisos = entitatService.findPermisAdmin(entitatCreada.getId());
		assertThat(
				permisos.size(),
				is(1));
		autenticarUsuari("user");
		entitatsAccessibles = entitatService.findAccessiblesUsuariActual();
		assertThat(
				entitatsAccessibles.size(),
				is(0));
	}

	@Test(expected = DataIntegrityViolationException.class)
	public void errorSiCodiDuplicat() {
		autenticarUsuari("super");
		entitatService.create(entitatCreate);
		entitatService.create(entitatCreate);
	}

	@Test(expected = AccessDeniedException.class)
	public void errorSiAccesAdminCreate() {
		autenticarUsuari("admin");
		entitatService.create(entitatCreate);
	}
	@Test(expected = AccessDeniedException.class)
	public void errorSiAccesUserCreate() {
		autenticarUsuari("user");
		entitatService.create(entitatCreate);
	}
	@Test(expected = AccessDeniedException.class)
	public void errorSiAccesAdminUpdate() {
		autenticarUsuari("admin");
		entitatService.update(entitatCreate);
	}
	@Test(expected = AccessDeniedException.class)
	public void errorSiAccesUserUpdate() {
		autenticarUsuari("user");
		entitatService.update(entitatCreate);
	}
	@Test(expected = AccessDeniedException.class)
	public void errorSiAccesAdminDelete() {
		autenticarUsuari("admin");
		entitatService.delete(new Long(1));
	}
	@Test(expected = AccessDeniedException.class)
	public void errorSiAccesUserDelete() {
		autenticarUsuari("user");
		entitatService.delete(new Long(1));
	}

}
