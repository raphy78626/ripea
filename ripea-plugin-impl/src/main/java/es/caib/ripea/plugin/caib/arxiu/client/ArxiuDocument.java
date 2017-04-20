/**
 * 
 */
package es.caib.ripea.plugin.caib.arxiu.client;

import java.util.List;
import java.util.Map;

/**
 * Estructura que representa un document de l'arxiu.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public class ArxiuDocument extends ArxiuContent {

	private List<ArxiuDocumentContent> contents;

	public ArxiuDocument(
			String nodeId,
			String name,
			List<ArxiuDocumentContent> contents,
			Map<String, Object> metata,
			List<String> aspects) {
		super(
				nodeId,
				name,
				metata,
				aspects);
		this.contents = contents;
	}

	public List<ArxiuDocumentContent> getContents() {
		return contents;
	}

}
