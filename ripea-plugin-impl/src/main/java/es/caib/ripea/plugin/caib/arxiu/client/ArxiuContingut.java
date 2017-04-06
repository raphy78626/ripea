/**
 * 
 */
package es.caib.ripea.plugin.caib.arxiu.client;

import java.util.List;

import es.caib.arxiudigital.apirest.CSGD.entidades.comunes.Metadata;
import es.caib.arxiudigital.apirest.constantes.Aspectos;

/**
 * Estructura que representa un contingut genèric de l'arxiu.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public abstract class ArxiuContingut {

	protected String nodeId;
	protected String titol;
	protected List<Metadata> metadades;
	protected List<Aspectos> aspectes;
	protected String json;

	public ArxiuContingut(
			String nodeId,
			String titol,
			List<Metadata> metadades,
			List<Aspectos> aspectes,
			String json) {
		this.nodeId = nodeId;
		this.titol = titol;
		this.metadades = metadades;
		this.aspectes = aspectes;
		this.json = json;
	}

	public String getNodeId() {
		return nodeId;
	}
	public String getTitol() {
		return titol;
	}
	/*public String getDescripcio() {
		return getMetadataValueAsString(
				metadades,
				MetadatosExpediente.DESCRIPCION);
	}
	public String getSerieDocumental() {
		return getMetadataValueAsString(
				metadades,
				MetadatosExpediente.CODIGO_CLASIFICACION);
	}
	public String getAplicacio() {
		return getMetadataValueAsString(
				metadades,
				MetadatosExpediente.CODIGO_APLICACION_TRAMITE);
	}*/
	public List<Metadata> getMetadades() {
		return metadades;
	}
	public List<Aspectos> getAspectes() {
		return aspectes;
	}
	public String getJson() {
		return json;
	}

	/*protected static String getMetadataValueAsString(
			Map<String, Object> metadades,
			String qname) {
		if (metadades == null) {
			return null;
		}
		if (metadades.get(qname) == null) {
			return null;
		}
		return metadades.get(qname).toString();
	}
	protected static Date getMetadataValueAsDate(
			Map<String, Object> metadades,
			String qname) throws ParseException {
		if (metadades == null) {
			return null;
		}
		if (metadades.get(qname) == null) {
			return null;
		}
		return parseDateIso8601(metadades.get(qname).toString());
	}
	@SuppressWarnings("unchecked")
	protected static List<String> getMetadataValueAsListString(
			Map<String, Object> metadades,
			String qname) {
		if (metadades == null) {
			return null;
		}
		if (metadades.get(qname) == null) {
			return null;
		}
		if (metadades.get(qname) instanceof List) {
			return (List<String>)metadades.get(qname);
		} else {
			List<String> resultat = new ArrayList<String>();
			resultat.add(metadades.get(qname).toString());
			return resultat;
		}
	}

	protected static Date parseDateIso8601(String date) throws ParseException {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX");
		return df.parse(date);
	}*/

}
