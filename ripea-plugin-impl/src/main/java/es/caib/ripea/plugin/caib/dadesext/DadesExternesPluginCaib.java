/**
 * 
 */
package es.caib.ripea.plugin.caib.dadesext;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

import es.caib.ripea.plugin.SistemaExternException;
import es.caib.ripea.plugin.dadesext.DadesExternesPlugin;
import es.caib.ripea.plugin.dadesext.Municipi;
import es.caib.ripea.plugin.dadesext.Pais;
import es.caib.ripea.plugin.dadesext.Provincia;
import es.caib.ripea.plugin.utils.PropertiesHelper;

/**
 * Implementació del plugin de dades externes que consulta la
 * informació a l'aplicació web de dades comunes.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public class DadesExternesPluginCaib implements DadesExternesPlugin {


	@Override
	public List<Pais> paisFindAll() throws SistemaExternException {
		String url = getBaseUrl() + "/services/paisos/format/JSON/idioma/ca";
		String errorDescripcio = "No s'ha pogut consultar la llista de paisos (" +
				"url=" + url + ")";
		try {
			HttpURLConnection httpConnection = (HttpURLConnection)new URL(url).openConnection();
			httpConnection.setRequestMethod("GET");
			httpConnection.setDoInput(true);
			httpConnection.setDoOutput(true);
			ObjectMapper mapper = new ObjectMapper();
			List<DadesComunesPais> paisosJson = mapper.readValue(
					httpConnection.getInputStream(), 
					TypeFactory.defaultInstance().constructCollectionType(
							List.class,  
							DadesComunesPais.class));
			Collections.sort(
					paisosJson,
					new Comparator<DadesComunesPais>() {
						@Override
						public int compare(DadesComunesPais p1, DadesComunesPais p2) {
							return p1.getNom().compareToIgnoreCase(p2.getNom());
						}
					});
			List<Pais> paisos = new ArrayList<Pais>();
			for (DadesComunesPais paisJson: paisosJson) {
				int codiNumeric = 0;
				if (paisJson.getCodi_numeric() != null) {
					codiNumeric = new Integer(paisJson.getCodi_numeric()).intValue();
				}
				Pais pais = new Pais(
						codiNumeric,
						paisJson.getAlpha2(),
						paisJson.getAlpha3(),
						paisJson.getNom());
				paisos.add(pais);
			}
			return paisos;
		} catch (Exception ex) {
			throw new SistemaExternException(
					errorDescripcio,
					ex);
		}
	}

	@Override
	public List<Provincia> provinciaFindAll() throws SistemaExternException {
		String url = getBaseUrl() + "/services/provincies/format/JSON/idioma/ca";
		String errorDescripcio = "No s'ha pogut consultar la llista de províncies (" +
				"url=" + url + ")";
		try {
			HttpURLConnection httpConnection = (HttpURLConnection)new URL(url).openConnection();
			httpConnection.setRequestMethod("GET");
			httpConnection.setDoInput(true);
			httpConnection.setDoOutput(true);
			ObjectMapper mapper = new ObjectMapper();
			List<Provincia> provincies = mapper.readValue(
					httpConnection.getInputStream(), 
					TypeFactory.defaultInstance().constructCollectionType(
							List.class,  
							Provincia.class));
			Collections.sort(
					provincies,
					new Comparator<Provincia>() {
						@Override
						public int compare(Provincia p1, Provincia p2) {
							return p1.getNom().compareToIgnoreCase(p2.getNom());
						}
					});
			
			return provincies;
		} catch (Exception ex) {
			throw new SistemaExternException(
					errorDescripcio,
					ex);
		}
	}

	@Override
	public List<Provincia> provinciaFindByComunitat(
			String comunitatCodi) throws SistemaExternException {
		String url = getBaseUrl() + "/services/provincies/" + comunitatCodi + "/format/JSON/idioma/ca";
		String errorDescripcio = "No s'ha pogut consultar la llista de províncies donada una comunitat (" +
				"url=" + url + ", " +
				"comunitatCodi=" + comunitatCodi + ")";
		try {
			HttpURLConnection httpConnection = (HttpURLConnection)new URL(url).openConnection();
			httpConnection.setRequestMethod("GET");
			httpConnection.setDoInput(true);
			httpConnection.setDoOutput(true);
			ObjectMapper mapper = new ObjectMapper();
			List<Provincia> provincies = mapper.readValue(
					httpConnection.getInputStream(), 
					TypeFactory.defaultInstance().constructCollectionType(
							List.class,  
							Provincia.class));
			Collections.sort(
					provincies,
					new Comparator<Provincia>() {
						@Override
						public int compare(Provincia p1, Provincia p2) {
							return p1.getNom().compareToIgnoreCase(p2.getNom());
						}
					});
			
			return provincies;
		} catch (Exception ex) {
			throw new SistemaExternException(
					errorDescripcio,
					ex);
		}
	}

	@Override
	public List<Municipi> municipiFindByProvincia(
			String provinciaCodi) throws SistemaExternException {
		String url = getBaseUrl() + "/services/municipis/" + provinciaCodi + "/format/JSON";
		String errorDescripcio = "No s'ha pogut consultar la llista de municipis donada una província (" +
				"url=" + url + ", " +
				"provinciaCodi=" + provinciaCodi + ")";
		try {
			HttpURLConnection httpConnection = (HttpURLConnection)new URL(url).openConnection();
			httpConnection.setRequestMethod("GET");
			httpConnection.setDoInput(true);
			httpConnection.setDoOutput(true);
			ObjectMapper mapper = new ObjectMapper();
			List<Municipi> municipis = mapper.readValue(
					httpConnection.getInputStream(), 
					TypeFactory.defaultInstance().constructCollectionType(
							List.class,  
							Municipi.class));
			Collections.sort(
					municipis,
					new Comparator<Municipi>() {
						@Override
						public int compare(Municipi m1, Municipi m2) {
							return m1.getNom().compareToIgnoreCase(m2.getNom());
						}
					});
			
			return municipis;
		} catch (Exception ex) {
			throw new SistemaExternException(
					errorDescripcio,
					ex);
		}
	}

	private String getBaseUrl() {
		String baseUrl = PropertiesHelper.getProperties().getProperty(
				"es.caib.ripea.plugin.dadesext.caib.base.url");
		if (baseUrl != null && baseUrl.length() > 0) {
			return baseUrl;
		} else {
			return "https://proves.caib.es/dadescomunsfront";
		}
	}

	public static class DadesComunesPais {
		private String codi_numeric;
		private String alpha2;
		private String alpha3;
		private String nom;
		public String getCodi_numeric() {
			return codi_numeric;
		}
		public void setCodi_numeric(String codi_numeric) {
			this.codi_numeric = codi_numeric;
		}
		public String getAlpha2() {
			return alpha2;
		}
		public void setAlpha2(String alpha2) {
			this.alpha2 = alpha2;
		}
		public String getAlpha3() {
			return alpha3;
		}
		public void setAlpha3(String alpha3) {
			this.alpha3 = alpha3;
		}
		public String getNom() {
			return nom;
		}
		public void setNom(String nom) {
			this.nom = nom;
		}
	}

}
