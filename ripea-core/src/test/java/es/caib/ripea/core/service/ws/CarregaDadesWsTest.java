/**
 * 
 */
package es.caib.ripea.core.service.ws;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

import es.caib.ripea.core.api.service.ws.RipeaCarregaTestWsService;

/**
 * Classe de proves pel servei web de callback de portafirmes MCGDws.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public class CarregaDadesWsTest extends CarregaDadesWsBase {

//	private RipeaCarregaTestWsService ripeaCarregaTestWsService;
	
	private static final String USER_DIR = "es.caib.ripea.performance.user.dir";
	
	public static void main(String[] args) {
		try {
			CarregaDadesWsTest carregaTest = new CarregaDadesWsTest();
			carregaTest.carregaProperties();
			Integer nombreExpedientsACrear = carregaTest.getNombreExpedientsACrear();
			carregaTest.carrega(nombreExpedientsACrear);
			carregaTest.updateNombreExpedientsACrear(nombreExpedientsACrear);
//			System.out.println(">>> Resposta: " + resposta);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private Integer getNombreExpedientsACrear() {
		String userDir = properties.getProperty(USER_DIR);
		String fileName = userDir + "/numexps.txt";
	    Integer numExps = 0;
	    Integer numExpsCrear = 0;
	    
        try {
        	File f = new File(fileName);
        	if(f.exists()){
        		BufferedReader bufferedReader = new BufferedReader(new FileReader(f));
                String line = bufferedReader.readLine();
                bufferedReader.close();

                if (line != null) {
                	line = line.trim();
                	if (!line.isEmpty())
                		numExps = Integer.parseInt(line.trim());
                }
      	  	}else{
      	  		f.createNewFile();
      	  		numExps = 0;
      	  	}
            
            switch (numExps) {
	            case 0:
	            	numExpsCrear = 100;
	            	break;
	            case 100:
	            	numExpsCrear = 400;
	            	break;
	            case 500:
	            	numExpsCrear = 500;
	            	break;
	            case 1000:
	            	numExpsCrear = 4000;
	            	break;
	            case 5000:
	            	numExpsCrear = 5000;
	            	break;
	            case 10000:
	            	numExpsCrear = 40000;
	            	break;
	            case 50000:
	            	numExpsCrear = 50000;
	            	break;
	            case 100000:
	            	numExpsCrear = 0;
	            	break;
	            default:
	            	numExpsCrear = 0;
            }
            
        } catch(FileNotFoundException ex) {
            System.out.println(
                "Unable to open file '" + 
                fileName + "'");                
        } catch(IOException ex) {
            System.out.println(
                "Error reading file '" 
                + fileName + "'");                  
        }
        
        return numExpsCrear;
	}
	
	private void updateNombreExpedientsACrear(Integer nombreExpedientsACrear) {
		String userDir = properties.getProperty(USER_DIR);
		String fileName = userDir + "/numexps.txt";
	    String numExpsTotal = "0";
	    
        try {
            
            switch (nombreExpedientsACrear) {
	            case 100:
	            	numExpsTotal = "100";
	            	break;
	            case 400:
	            	numExpsTotal = "500";
	            	break;
	            case 500:
	            	numExpsTotal = "1000";
	            	break;
	            case 4000:
	            	numExpsTotal = "5000";
	            	break;
	            case 5000:
	            	numExpsTotal = "10000";
	            	break;
	            case 40000:
	            	numExpsTotal = "50000";
	            	break;
	            case 50000:
	            	numExpsTotal = "100000";
	            	break;
	            case 0:
	            	numExpsTotal = "100000";
	            	break;
	            default:
	            	numExpsTotal = "0";
            }
            
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileName, false));
            bufferedWriter.write(numExpsTotal);
            bufferedWriter.close();
        }
        catch(FileNotFoundException ex) {
            System.out.println(
                "Unable to open file '" + 
                fileName + "'");                
        }
        catch(IOException ex) {
            System.out.println(
                "Error reading file '" 
                + fileName + "'");                  
        }
        
	}
	
	private void carrega(Integer nombreExpedientsACrear) throws Exception {
		
		
		RipeaCarregaTestWsService ripeaCarregaTestWsService = getCarregaDadesService();
		
		// Usuari
		String usuariCodi = properties.getProperty("es.caib.ripea.performance.ws.user");
		// Entitat
		String entitatCodi = properties.getProperty("es.caib.ripea.performance.entitat.codi");
		Long entitatId = ripeaCarregaTestWsService.getEntitatIdByCodi(entitatCodi);
		// PareId
		Long  pareId = ripeaCarregaTestWsService.getEscriptoriId(entitatId, usuariCodi);
		
		// Metaexpedient
		Long metaExpedientCodi = Long.parseLong(properties.getProperty("es.caib.ripea.performance.metaexpedient.codi"));
		
		// ArxiuId
		String arxiuNom = properties.getProperty("es.caib.ripea.performance.arxiu.nom");
		String arxiuUnitatCodi = properties.getProperty("es.caib.ripea.performance.arxiu.unitat.codi");
		Long arxiuId = ripeaCarregaTestWsService.getArxiuIdByNom(entitatId, arxiuUnitatCodi, arxiuNom );
		
		// Expedient
		Integer any = Integer.parseInt(properties.getProperty("es.caib.ripea.performance.expedient.any"));
		String nom = properties.getProperty("es.caib.ripea.performance.expedient.nom");
		
		// Metadades expedient
		String expedientMetadata1Codi = properties.getProperty("es.caib.ripea.performance.metadadaexpedient.1.codi");
		String expedientMetadata2Codi = properties.getProperty("es.caib.ripea.performance.metadadaexpedient.2.codi");
		
		// Metadocument
		String documentTipusCodi = properties.getProperty("es.caib.ripea.performance.metadocument.codi");
		
		// Document
		String fitxerNomOriginal = "FitxerTest.pdf";
		String fitxerContentType = "application/pdf";
		String fitxerContingut = "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bmN5L0NTL0RldmljZVJHQi9JIHRydWU+Pi9Db250ZW50cyAyIDAgUj4+CmVuZG9iagoKNCAwIG9iago8PC9UeXBlL1BhZ2VzCi9SZXNvdXJjZXMgMTEgMCBSCi9NZWRpYUJveFsgMCAwIDU5NSA4NDIgXQovS2lkc1sgMSAwIFIgXQovQ291bnQgMT4+CmVuZG9iagoKMTIgMCBvYmoKPDwvVHlwZS9DYXRhbG9nL1BhZ2VzIDQgMCBSCi9PcGVuQWN0aW9uWzEgMCBSIC9YWVogbnVsbCBudWxsIDBdCi9MYW5nKGNhLUVTKQo+PgplbmRvYmoKCjEzIDAgb2JqCjw8L0F1dGhvcjxGRUZGMDA3MzAwNjkwMDZGMDA2RTAwMjA+Ci9DcmVhdG9yPEZFRkYwMDU3MDA3MjAwNjkwMDc0MDA2NTAwNzI+Ci9Qcm9kdWNlcjxGRUZGMDA0QzAwNjkwMDYyMDA3MjAwNjUwMDRGMDA2NjAwNjYwMDY5MDA2MzAwNjUwMDIwMDAzNTAwMkUwMDMxPgovQ3JlYXRpb25EYXRlKEQ6MjAxNzA2MjcxMjI0NDQrMDInMDAnKT4+CmVuZG9iagoKeHJlZgowIDE0CjAwMDAwMDAwMDAgNjU1MzUgZiAKMDAwMDAwNjIwOCAwMDAwMCBuIAowMDAwMDAwMDE5IDAwMDAwIG4gCjAwMDAwMDAyMTEgMDAwMDAgbiAKMDAwMDAwNjM1MSAwMDAwMCBuIAowMDAwMDAwMjMxIDAwMDAwIG4gCjAwMDAwMDUzNzYgMDAwMDAgbiAKMDAwMDAwNTM5NyAwMDAwMCBuIAowMDAwMDA1NTkyIDAwMDAwIG4gCjAwMDAwMDU5MjYgMDAwMDAgbiAKMDAwMDAwNjEyMSAwMDAwMCBuIAowMDAwMDA2MTUzIDAwMDAwIG4gCjAwMDAwMDY0NTAgMDAwMDAgbiAKMDAwMDAwNjU0NyAwMDAwMCBuIAp0cmFpbGVyCjw8L1NpemUgMTQvUm9vdCAxMiAwIFIKL0luZm8gMTMgMCBSCi9JRCBbIDw3REM3MzhGNDdDMjU5RUFDQ0ExRERCMTVFRTFDOTk3OD4KPDdEQzczOEY0N0MyNTlFQUNDQTFEREIxNUVFMUM5OTc4PiBdCi9Eb2NDaGVja3N1bSAvMEI1MEE3RTg4NUVGODg4MDVEQzk3N0VGOTdDQzQzMDMKPj4Kc3RhcnR4cmVmCjY3NTYKJSVFT0YK";
		String documentTipus = "DIGITAL";
		String documentTitol = "Document";
		String documentUbicacio = null;
		String documentNtiOrgan = arxiuUnitatCodi;
		String documentNtiOrigen = "O0";
		String documentNtiEstat = "EE01";
		String documentNtiTipusDoc = "TD13";
		
		// Metadades document
		String documentMetadada1Codi = properties.getProperty("es.caib.ripea.performance.metadadaexpedient.1.codi");
		String documentMetadada2Codi = properties.getProperty("es.caib.ripea.performance.metadadaexpedient.2.codi");
				
		for (int i = 0; i < nombreExpedientsACrear; i++) {
			ripeaCarregaTestWsService.crearExpedientRendiment(
					entitatId, 
					pareId, 
					metaExpedientCodi, 
					arxiuId, 
					any, 
					nom + "_" + i, 
					expedientMetadata1Codi, 
					Integer.toString(i), 
					expedientMetadata2Codi, 
					i, 
					documentTipusCodi, 
					documentTipus, 
					fitxerNomOriginal, 
					fitxerContentType, 
					fitxerContingut.getBytes(), 
					documentTitol + "_" + i, 
					new Date(), 
					documentUbicacio, 
					documentNtiOrgan, 
					documentNtiOrigen, 
					documentNtiEstat, 
					documentNtiTipusDoc, 
					documentMetadada1Codi, 
					Integer.toString(i), 
					documentMetadada2Codi, 
					i);
		}
	}
	
}
