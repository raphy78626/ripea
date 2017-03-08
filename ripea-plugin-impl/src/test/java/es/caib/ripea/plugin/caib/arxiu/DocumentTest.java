/**
 * 
 */
package es.caib.ripea.plugin.caib.arxiu;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

/**
 * Classe de proves amb la lectura i conversio d'arxius.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public class DocumentTest {

	@Test
	public void inputStream() throws IOException {
		InputStream is = getClass().getResourceAsStream(
        		"/es/caib/ripea/plugin/caib/document_test.odt");
		byte[] bytes = IOUtils.toByteArray(is);
		Base64InputStream isb64 = new Base64InputStream(new ByteArrayInputStream(bytes), true);
		String contingutBase64 = IOUtils.toString(isb64, "UTF-8");
		System.out.println(">>> contingutBase64: " + contingutBase64);
	}

}
