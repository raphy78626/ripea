/**
 * 
 */
package es.caib.ripea.core.service;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.awt.Desktop;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.internal.AssumptionViolatedException;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.junit.runners.model.Statement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import es.caib.ripea.core.api.dto.ExpedientDto;
import es.caib.ripea.core.api.dto.ExpedientEstatEnumDto;
import es.caib.ripea.core.api.dto.ExpedientFiltreDto;
import es.caib.ripea.core.api.dto.PaginaDto;
import es.caib.ripea.core.api.dto.PaginacioParamsDto;
import es.caib.ripea.core.api.service.ExpedientService;
import es.caib.ripea.core.helper.PropertiesHelper;

/**
 * Tests per al servei d'entitats.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/es/caib/ripea/core/application-context-performance-test.xml"})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@Transactional
public class PerformanceServiceTest {
	
    private static File junitReport;
    private static BufferedWriter junitWriter;
    private static Long milis;
    
	private static final String CODI_ENTITAT = "es.caib.ripea.performance.entitat.id";
	private static final String CODI_USUARI = "es.caib.ripea.performance.usuari.codi";
	private static final String CODI_METAEXPEDIENT = "es.caib.ripea.performance.metaexpedient.codi";
	private static final String USER_DIR = "es.caib.ripea.performance.user.dir";
	
//	@Autowired
//	private ContingutService contingutService;
	@Autowired
	private ExpedientService expedientService;
//	@Autowired
//	private ArxiuService arxiuService;
	@Autowired
	private UserDetailsService userDetailsService;

	
//	private EntitatDto entitat;
//	private MetaDadaDto metaDada;
//	private MetaDocumentDto metaDocument;
//	private MetaExpedientDto metaExpedient;
//	private ArxiuDto arxiu;
//	private ExpedientDto expedientCreate;
//	private ExpedientDto expedientUpdate;
//	private PermisDto permisUserRead;
	private static Long entitatActualId;
	private static String codiUsuari;
	private static Long metaExpedientId;
	
//	private PropertiesHelper properties;

	@BeforeClass
	public static void setUp() throws IOException {

		PropertiesHelper.getProperties("classpath:es/caib/ripea/core/test.properties");
		
		entitatActualId = PropertiesHelper.getProperties().getAsLong(CODI_ENTITAT);
		codiUsuari = PropertiesHelper.getProperties().getProperty(CODI_USUARI);
//		metaExpedientId = PropertiesHelper.getProperties().getAsLong(CODI_METAEXPEDIENT);
		String userDir = PropertiesHelper.getProperties().getProperty(USER_DIR);
		
		String junitReportFile = userDir + "/junitReportFile.html";
	    DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
	    Date date = new Date();
	    junitReport = new File(junitReportFile);
	    junitWriter = new BufferedWriter(new FileWriter(junitReport, true));
	    junitWriter.write("<html><body>");
	    junitWriter.write("<h1>Test Execution Summary - " + dateFormat.format(date) + "</h1>");
	}
	
	@AfterClass
	public static void tearDown() throws IOException {

	    junitWriter.write("</body></html>");
	    junitWriter.close();
	    Desktop.getDesktop().browse(junitReport.toURI());

	}
	
	@Rule
	public TestRule watchman = new TestWatcher() {

	    @Override
	    public Statement apply(Statement base, Description description) {
	        return super.apply(base, description);
	    }

	    @Override
	    protected void succeeded(Description description) {
	        try {
	        	junitWriter.write("Test " + description.getDisplayName() + " passed");
	            junitWriter.write("<br/>");
	        } catch (Exception e1) {
	            System.out.println(e1.getMessage());
	        }
	    }

	    @Override
	    protected void failed(Throwable e, Description description) {
	        try {
	        	junitWriter.write("Test " + description.getDisplayName() + " failed");
	        	junitWriter.write("<br/>");
	            junitWriter.write("Error " + e.getClass().getSimpleName());
	            junitWriter.write("<br/>");
	        } catch (Exception e2) {
	            System.out.println(e2.getMessage());
	        }
	    }
	    
        @Override
        protected void skipped(AssumptionViolatedException e, Description description) {
        	try {
        		junitWriter.write("Test " + description.getDisplayName() + " skipped");
        		junitWriter.write("<br/>");
	        } catch (Exception e3) {
	            System.out.println(e3.getMessage());
	        }
        }

        @Override
        protected void starting(Description description) {
            super.starting(description);
            milis = System.currentTimeMillis();
            try {
        		junitWriter.write("Test " + description.getDisplayName() + " started");
        		junitWriter.write("<br/>");
	        } catch (Exception e4) {
	            System.out.println(e4.getMessage());
	        }
        }

        @Override
        protected void finished(Description description) {
            super.finished(description);
            
            try {
        		junitWriter.write("Test " + description.getDisplayName() + " finished");
        		junitWriter.write("<br/>");
        		junitWriter.write("Duració " + (System.currentTimeMillis() - milis) + "ms.");
        		junitWriter.write("<br/>");
	        } catch (Exception e5) {
	            System.out.println(e5.getMessage());
	        }
        }
	};
	
	
	@Test
	public void testA_consultaExpedientsFiltreBuid() {
		
		// Cream un filtre buid
		ExpedientFiltreDto filtre = new ExpedientFiltreDto();
		
		// Definim els maràmetres de paginació
		PaginacioParamsDto paginacio = new PaginacioParamsDto();
		paginacio.setPaginaNum(1);
		paginacio.setPaginaTamany(50);

		// Autenticam a l'usuari
		autenticarUsuari(codiUsuari);
		
		// Realitzam la consulta d'expedients, amb el filtre buid
		PaginaDto<ExpedientDto> pagina = expedientService.findAmbFiltreUser(
				entitatActualId,
				filtre,
				paginacio);
		
		assertNotNull(pagina);
		assertFalse(pagina.isBuida());
		
	}
	
	@Test
	public void testB_consultaExpedientsFiltreTipusExpedient() {
		ExpedientFiltreDto filtre = new ExpedientFiltreDto();
		filtre.setMetaExpedientId(metaExpedientId);
		PaginacioParamsDto paginacio = new PaginacioParamsDto();
		paginacio.setPaginaNum(1);
		paginacio.setPaginaTamany(50);
		
		autenticarUsuari(codiUsuari);
		
		PaginaDto<ExpedientDto> pagina = expedientService.findAmbFiltreUser(
				entitatActualId,
				filtre,
				paginacio);
		
		assertNotNull(pagina);
		assertFalse(pagina.isBuida());
	}
	
	@Test
	public void testC_consultaExpedientsFiltreEstat() {
		ExpedientFiltreDto filtre = new ExpedientFiltreDto();
		filtre.setEstat(ExpedientEstatEnumDto.OBERT);
		PaginacioParamsDto paginacio = new PaginacioParamsDto();
		paginacio.setPaginaNum(1);
		paginacio.setPaginaTamany(50);
		
		autenticarUsuari(codiUsuari);
		
		PaginaDto<ExpedientDto> pagina = expedientService.findAmbFiltreUser(
				entitatActualId,
				filtre,
				paginacio);
	}
	
	@Test
	public void testD_agafarExpedient() {
		ExpedientFiltreDto filtre = new ExpedientFiltreDto();
		filtre.setMetaExpedientId(metaExpedientId);
		filtre.setEstat(ExpedientEstatEnumDto.OBERT);
		List<Long> expedients = expedientService.findIdsAmbFiltre(
				entitatActualId, 
				filtre);
		if (!expedients.isEmpty()) {
			expedientService.agafarUser(
				entitatActualId,
				expedients.get(0));
		}
		
//		EntitatDto entitatCreada = (EntitatDto)elementsCreats.get(0);
//		ExpedientDto expedientCreat = (ExpedientDto)elementsCreats.get(5);
//		autenticarUsuari("user");
//		assertTrue(
//				expedientCreat.isAgafat());
//		assertEquals("user", expedientCreat.getAgafatUsuari());
//		expedientService.alliberarUser(
//				entitatCreada.getId(),
//				expedientCreat.getId());
//		ExpedientDto alliberat = expedientService.findById(
//				entitatCreada.getId(),
//				expedientCreat.getId());
//		assertTrue(
//				!alliberat.isAgafat());
//		assertNull(alliberat.getAgafatUsuari());
//		expedientService.agafarUser(
//				entitatCreada.getId(),
//				expedientCreat.getId());
//		ExpedientDto agafat = expedientService.findById(
//				entitatCreada.getId(),
//				expedientCreat.getId());
//		assertTrue(
//				agafat.isAgafat());
//		assertEquals("user", agafat.getAgafatUsuari());
		
	}
	
	@Test
	public void testE_consultaDetallTipusExpedient() {
		
	}
	
	@Test
	public void testF_modificarDadesExpedient() {
		
	}
	
	@Test
	public void testG_consultaHistoricAccionsExpedient() {
		
	}
	
	@Test
	public void testH_consultaDetallDocumentTipusExpedient() {
		
	}
	
	@Test
	public void testI_modificarDocument() {
		
	}
	
	@Test
	public void testJ_consultaHistoricAccionsDocument() {
		
	}
	
	@Test
	public void testK_crearCarpetaExpedient() {
		
	}
	
	@Test
	public void testL_EsborrarCarpetaExpedient() {
		
	}
	
	@Test
	public void testM_alliberarExpedient() {
		
	}
	
	@Test
	public void testN_consultaBustiesUsuari() {
		
	}
	
	@Test
	public void testO_consultaBustiaFiltreBuid() {
		
	}
	
	@Test
	public void testP_consultaBustiaFiltrePendent() {
		
	}
	
	@Test
	public void testQ_consultaDetallAnotacioRegistre() {
		
	}
	
	@Test
	public void testR_consultaHistoricAccionsAnotacioRegistre() {
		
	}
	

	private void autenticarUsuari(String usuariCodi) {
		UserDetails userDetails = userDetailsService.loadUserByUsername(usuariCodi);
		Authentication authToken = new UsernamePasswordAuthenticationToken(
				userDetails.getUsername(),
				userDetails.getPassword(),
				userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);
	}
	
//	@Test
//    public void create() {
//		testAmbElementsIExpedient(
//				new TestAmbElementsCreats() {
//					@Override
//					public void executar(List<Object> elementsCreats) {
//						ExpedientDto expedientCreat = (ExpedientDto)elementsCreats.get(5);
//						assertNotNull(expedientCreat);
//						assertNotNull(expedientCreat.getId());
//						comprovarExpedientCoincideix(
//								expedientCreate,
//								expedientCreat);
//					}
//				});
//	}
//
//	@Test
//	public void findById() {
//		testAmbElementsIExpedient(
//				new TestAmbElementsCreats() {
//					@Override
//					public void executar(List<Object> elementsCreats) {
//						EntitatDto entitatCreada = (EntitatDto)elementsCreats.get(0);
//						ExpedientDto expedientCreat = (ExpedientDto)elementsCreats.get(5);
//						ExpedientDto trobat = expedientService.findById(
//								entitatCreada.getId(),
//								expedientCreat.getId());
//						assertNotNull(trobat);
//						assertNotNull(trobat.getId());
//						comprovarExpedientCoincideix(
//								expedientCreat,
//								trobat);
//					}
//				});
//    }
//
//	@Test
//    public void update() {
//		testAmbElementsIExpedient(
//				new TestAmbElementsCreats() {
//					@Override
//					public void executar(List<Object> elementsCreats) {
//						EntitatDto entitatCreada = (EntitatDto)elementsCreats.get(0);
//						MetaExpedientDto metaExpedientCreat = (MetaExpedientDto)elementsCreats.get(3);
//						ArxiuDto arxiuCreat = (ArxiuDto)elementsCreats.get(4);
//						ExpedientDto expedientCreat = (ExpedientDto)elementsCreats.get(5);
//						ExpedientDto modificat = expedientService.update(
//								entitatCreada.getId(),
//								expedientCreat.getId(),
//								arxiuCreat.getId(),
//								metaExpedientCreat.getId(),
//								expedientUpdate.getNom());
//						assertNotNull(modificat);
//						assertNotNull(modificat.getId());
//						assertEquals(
//								expedientCreat.getId(),
//								modificat.getId());
//						comprovarExpedientCoincideix(
//								expedientUpdate,
//								modificat);
//					}
//				});
//	}
//
//	@Test
//    public void deleteReversible() {
//		testAmbElementsIExpedient(
//				new TestAmbElementsCreats() {
//					@Override
//					public void executar(List<Object> elementsCreats) {
//						EntitatDto entitatCreada = (EntitatDto)elementsCreats.get(0);
//						ExpedientDto expedientCreat = (ExpedientDto)elementsCreats.get(5);
//						try {
//							ContingutDto esborrat = contingutService.deleteReversible(
//									entitatCreada.getId(),
//									expedientCreat.getId());
//							assertTrue(esborrat instanceof ExpedientDto);
//							comprovarExpedientCoincideix(
//									expedientCreate,
//									(ExpedientDto)esborrat);
//							try {
//								autenticarUsuari("user");
//								expedientService.findById(
//										entitatCreada.getId(),
//										expedientCreat.getId());
//								fail("L'expedient esborrat no s'hauria d'haver trobat");
//							} catch (NotFoundException expected) {
//							}
//							elementsCreats.remove(expedientCreat);
//						} catch (IOException ex) {
//							fail("S'han produit errors inesperats: " + ex);
//						}
//					}
//				});
//	}
//
//	@Test
//    public void deleteDefinitiu() {
//		testAmbElementsIExpedient(
//				new TestAmbElementsCreats() {
//					@Override
//					public void executar(List<Object> elementsCreats) {
//						EntitatDto entitatCreada = (EntitatDto)elementsCreats.get(0);
//						ExpedientDto expedientCreat = (ExpedientDto)elementsCreats.get(5);
//						autenticarUsuari("admin");
//						ContingutDto esborrat = contingutService.deleteDefinitiu(
//								entitatCreada.getId(),
//								expedientCreat.getId());
//						assertTrue(esborrat instanceof ExpedientDto);
//						comprovarExpedientCoincideix(
//								expedientCreate,
//								(ExpedientDto)esborrat);
//						try {
//							autenticarUsuari("user");
//							expedientService.findById(
//									entitatCreada.getId(),
//									expedientCreat.getId());
//							fail("L'expedient esborrat no s'hauria d'haver trobat");
//						} catch (NotFoundException expected) {
//						}
//						elementsCreats.remove(expedientCreat);
//					}
//				});
//	}
//
//	@Test
//    public void tancarReobrir() {
//		testAmbElementsIExpedient(
//				new TestAmbElementsCreats() {
//					@Override
//					public void executar(List<Object> elementsCreats) {
//						EntitatDto entitatCreada = (EntitatDto)elementsCreats.get(0);
//						ExpedientDto expedientCreat = (ExpedientDto)elementsCreats.get(5);
//						autenticarUsuari("user");
//						assertEquals(
//								ExpedientEstatEnumDto.OBERT, expedientCreat.getEstat());
//						String motiu = "Motiu de tancament de test";
//						expedientService.tancar(
//								entitatCreada.getId(),
//								expedientCreat.getId(),
//								motiu);
//						ExpedientDto tancat = expedientService.findById(
//								entitatCreada.getId(),
//								expedientCreat.getId());
//						assertEquals(
//								ExpedientEstatEnumDto.TANCAT, tancat.getEstat());
//						assertEquals(motiu, tancat.getTancatMotiu());
//						expedientService.reobrir(
//								entitatCreada.getId(),
//								expedientCreat.getId());
//						ExpedientDto reobert = expedientService.findById(
//								entitatCreada.getId(),
//								expedientCreat.getId());
//						assertEquals(
//								ExpedientEstatEnumDto.OBERT, reobert.getEstat());
//						assertNull(reobert.getTancatMotiu());
//					}
//				});
//	}
//
//	@Test
//    public void alliberarAgafarUser() {
//		testAmbElementsIExpedient(
//				new TestAmbElementsCreats() {
//					@Override
//					public void executar(List<Object> elementsCreats) {
//						EntitatDto entitatCreada = (EntitatDto)elementsCreats.get(0);
//						ExpedientDto expedientCreat = (ExpedientDto)elementsCreats.get(5);
//						autenticarUsuari("user");
//						assertTrue(
//								expedientCreat.isAgafat());
//						assertEquals("user", expedientCreat.getAgafatUsuari());
//						expedientService.alliberarUser(
//								entitatCreada.getId(),
//								expedientCreat.getId());
//						ExpedientDto alliberat = expedientService.findById(
//								entitatCreada.getId(),
//								expedientCreat.getId());
//						assertTrue(
//								!alliberat.isAgafat());
//						assertNull(alliberat.getAgafatUsuari());
//						expedientService.agafarUser(
//								entitatCreada.getId(),
//								expedientCreat.getId());
//						ExpedientDto agafat = expedientService.findById(
//								entitatCreada.getId(),
//								expedientCreat.getId());
//						assertTrue(
//								agafat.isAgafat());
//						assertEquals("user", agafat.getAgafatUsuari());
//					}
//				});
//	}
//
//	@Test
//    public void alliberarAdminAgafarUser() {
//		testAmbElementsIExpedient(
//				new TestAmbElementsCreats() {
//					@Override
//					public void executar(List<Object> elementsCreats) {
//						EntitatDto entitatCreada = (EntitatDto)elementsCreats.get(0);
//						ExpedientDto expedientCreat = (ExpedientDto)elementsCreats.get(5);
//						assertTrue(
//								expedientCreat.isAgafat());
//						assertEquals("user", expedientCreat.getAgafatUsuari());
//						autenticarUsuari("admin");
//						expedientService.alliberarAdmin(
//								entitatCreada.getId(),
//								expedientCreat.getId());
//						autenticarUsuari("user");
//						ExpedientDto alliberat = expedientService.findById(
//								entitatCreada.getId(),
//								expedientCreat.getId());
//						assertTrue(
//								!alliberat.isAgafat());
//						assertNull(alliberat.getAgafatUsuari());
//						expedientService.agafarUser(
//								entitatCreada.getId(),
//								expedientCreat.getId());
//						ExpedientDto agafat = expedientService.findById(
//								entitatCreada.getId(),
//								expedientCreat.getId());
//						assertTrue(
//								agafat.isAgafat());
//						assertEquals("user", agafat.getAgafatUsuari());
//					}
//				});
//	}
//
//
//	private void donarPermisosArxiu(
//			EntitatDto entitat,
//			ArxiuDto arxiu,
//			MetaExpedientDto metaExpedient) {
//		autenticarUsuari("admin");
//		arxiuService.addMetaExpedient(
//				entitat.getId(),
//				arxiu.getId(),
//				metaExpedient.getId());
//		autenticarUsuari("user");
//	}
//
//	private void comprovarExpedientCoincideix(
//			ExpedientDto original,
//			ExpedientDto perComprovar) {
//		assertEquals(
//				original.getAny(),
//				perComprovar.getAny());
//		assertEquals(
//				original.getNom(),
//				perComprovar.getNom());
//	}
//
//	private void testAmbElementsIExpedient(
//			final TestAmbElementsCreats testAmbExpedientCreat) {
//		testCreantElements(
//				new TestAmbElementsCreats() {
//					@Override
//					public void executar(List<Object> elementsCreats) {
//						autenticarUsuari("user");
//						EntitatDto entitatCreada = (EntitatDto)elementsCreats.get(0);
//						//MetaDadaDto metaDadaCreada = (MetaDadaDto)elementsCreats.get(1);
//						//MetaDocumentDto metaDocumentCreat = (MetaDocumentDto)elementsCreats.get(2);
//						MetaExpedientDto metaExpedientCreat = (MetaExpedientDto)elementsCreats.get(3);
//						ArxiuDto arxiuCreat = (ArxiuDto)elementsCreats.get(4);
//						donarPermisosArxiu(
//								entitatCreada,
//								arxiuCreat,
//								metaExpedientCreat);
//						EscriptoriDto escriptori = contingutService.getEscriptoriPerUsuariActual(
//								entitatCreada.getId());
//						ExpedientDto creat = expedientService.create(
//								entitatCreada.getId(),
//								escriptori.getId(),
//								metaExpedientCreat.getId(),
//								arxiuCreat.getId(),
//								expedientCreate.getAny(),
//								expedientCreate.getNom(),
//								null,
//								null);
//						try {
//							elementsCreats.add(creat);
//							testAmbExpedientCreat.executar(
//									elementsCreats);
//						} catch (Exception ex) {
//							System.out.println("El test ha produït una excepció:");
//							ex.printStackTrace(System.out);
//						} finally {
//							for (Object element: elementsCreats) {
//								if (element instanceof ExpedientDto) {
//									autenticarUsuari("admin");
//									contingutService.deleteDefinitiu(
//											entitatCreada.getId(),
//											((ExpedientDto)element).getId());
//								}
//							}
//							elementsCreats.remove(creat);
//						}
//					}
//				},
//				entitat,
//				metaDada,
//				metaDocument,
//				metaExpedient,
//				arxiu);
//	}
//	class TestAmbElementsIExpedient extends TestAmbElementsCreats {
//		@Override
//		public void executar(List<Object> elementsCreats) {
//			
//		}
//	}

}
