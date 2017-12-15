/**
 * 
 */
package es.caib.ripea.plugin.caib.gesdoc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import es.caib.ripea.plugin.SistemaExternException;
import es.caib.ripea.plugin.gesdoc.GestioDocumentalArxiu;
import es.caib.ripea.plugin.gesdoc.GestioDocumentalPlugin;
import es.caib.ripea.plugin.utils.PropertiesHelper;

/**
 * Implementació del plugin de gestió documental que
 * emmagatzema els arxius a un directori del sistema
 * de fitxers.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public class GestioDocumentalPluginFilesystem implements GestioDocumentalPlugin {

	@Override
	public String create(
			GestioDocumentalArxiu arxiu) throws SistemaExternException {
		try {
			return saveWithNewId(arxiu);
		} catch (Exception ex) {
			throw new SistemaExternException(
					"No s'ha pogut crear l'arxiu",
					ex);
		}
	}

	@Override
	public void update(
			String id,
			GestioDocumentalArxiu arxiu) throws SistemaExternException {
		try {
			if (!updateWithId(id, arxiu)) {
				throw new SistemaExternException(
						"No s'ha trobat l'arxiu (id=" + id + ")");
			}
		} catch (Exception ex) {
			throw new SistemaExternException(
					"No s'ha pogut actualitzar l'arxiu (id=" + id + ")",
					ex);
		}
	}

	@Override
	public void delete(
			String id) throws SistemaExternException {
		try {
			if (!deleteWithId(id)) {
				throw new SistemaExternException(
						"No s'ha trobat l'arxiu (id=" + id + ")");
			}
		} catch (Exception ex) {
			throw new SistemaExternException(
					"No s'ha pogut esborrar l'arxiu (id=" + id + ")",
					ex);
		}
	}

	@Override
	public GestioDocumentalArxiu get(
			String id) throws SistemaExternException {
		try {
			GestioDocumentalArxiu arxiu = readWithId(id);
			if (arxiu == null)
				throw new SistemaExternException(
						"No s'ha trobat l'arxiu (id=" + id + ")");
			return arxiu;
		} catch (Exception ex) {
			throw new SistemaExternException(
					"No s'ha pogut llegir l'arxiu (id=" + id + ")",
					ex);
		}
	}



	private String saveWithNewId(
			GestioDocumentalArxiu arxiu) throws Exception {
		String id = new Long(System.currentTimeMillis()).toString();
		File fContent = new File(getBaseDir() + "/" + id);
		while (fContent.exists()) {
			try {
				Thread.sleep(1);
			} catch (Exception ignored) {}
			id = new Long(System.currentTimeMillis()).toString();
			fContent = new File(getBaseDir() + "/" + id);
		}
		FileOutputStream outContent = new FileOutputStream(fContent);
		outContent.write(arxiu.getContent());
		outContent.close();
		File fName = new File(getBaseDir() + "/" + id + ".name");
		FileOutputStream outName = new FileOutputStream(fName);
		outName.write(arxiu.getFileName().getBytes());
		outName.close();
		return id;
	}

	private boolean updateWithId(
			String id,
			GestioDocumentalArxiu arxiu) throws Exception {
		File fContent = new File(getBaseDir() + "/" + id);
		if (fContent.exists()) {
			FileOutputStream outContent = new FileOutputStream(fContent, false);
			outContent.write(arxiu.getContent());
			outContent.close();
			File fName = new File(getBaseDir() + "/" + id + ".name");
			FileOutputStream outName = new FileOutputStream(fName);
			outName.write(arxiu.getFileName().getBytes());
			outName.close();
			return true;
		} else {
			return false;
		}
	}

	private boolean deleteWithId(
			String id) throws Exception {
		File fContent = new File(getBaseDir() + "/" + id);
		if (fContent.exists()) {
			fContent.delete();
			File fName = new File(getBaseDir() + "/" + id + ".name");
			fName.delete();
			return true;
		} else {
			return false;
		}
	}

	private GestioDocumentalArxiu readWithId(
			String id) throws Exception {
		File fContent = new File(getBaseDir() + "/" + id);
		if (fContent.exists()) {
			FileInputStream inContent = new FileInputStream(fContent);
			byte fileContent[] = new byte[(int)fContent.length()];
			inContent.read(fileContent);
			inContent.close();
			File fName = new File(getBaseDir() + "/" + id + ".name");
			FileInputStream inName = new FileInputStream(fName);
			byte fileName[] = new byte[(int)fName.length()];
			inName.read(fileName);
			inName.close();
			return new GestioDocumentalArxiu(
					new String(fileName),
					fileContent);
		} else {
			return null;
		}
	}

	private String getBaseDir() {
		return PropertiesHelper.getProperties().getProperty(
				"es.caib.ripea.plugin.gesdoc.filesystem.base.dir");
	}

}
