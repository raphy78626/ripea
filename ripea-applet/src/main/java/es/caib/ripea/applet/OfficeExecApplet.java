/**
 * 
 */
package es.caib.ripea.applet;

import java.applet.Applet;
import java.security.AccessController;
import java.security.PrivilegedAction;

/**
 * Applet per a executar l'editor de document ofimàtics passant
 * com a paràmetre la URL del document al servidor WEBDAV.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public class OfficeExecApplet extends Applet {

	private static final String OS_NAME = System.getProperty("os.name").toLowerCase();
	private static final String WINDOWS_EXEC_COMMAND = "soffice.exe";
	private static final String DEFAULT_EXEC_COMMAND = "soffice";

	private Process officeProcess;

	public void init() {
		System.out.println("[OfficeExecApplet] Iniciat");
		System.out.println("[OfficeExecApplet] Sistema operatiu: " + OS_NAME);
		System.out.println("[OfficeExecApplet] Comanda office: " + getComandaOffice());
	}

	public void openWithOffice(final String url) {
		System.out.println("[OfficeExecApplet] Executant office...");
		officeProcess = AccessController.doPrivileged(new PrivilegedAction<Process>() {
		    @Override
		    public Process run() {
		    	try {
			    	Runtime rt = Runtime.getRuntime();
			    	return rt.exec(getComandaOffice() + " " + url);
		    	} catch (Exception ex) {
					ex.printStackTrace();
					return null;
				}
		    }
		});
		System.out.println("[OfficeExecApplet] ... office executat.");
	}

	public Integer getExitValue() {
		if (officeProcess != null) {
			try {
				return officeProcess.exitValue();
			} catch (IllegalThreadStateException ex) {
				return null;
			}
		} else {
			return null;
		}
	}

	public Integer waitForExitValue() {
		if (officeProcess != null) {
			int exitValue = -1;
			boolean finished = false;
			do {
				try {
					exitValue = officeProcess.waitFor();
					finished = true;
				} catch (InterruptedException ex) {
					System.out.println("[OfficeExecApplet] WaitFor interruted!");
				}
			} while (!finished);
			return new Integer(exitValue);
		} else {
			return null;
		}
	}

	public String[] getAllowedFileExtensions() {
		return new String[] {
			"odt",
			"ott",
			"odm",
			"ods",
			"ots",
			"odg",
			"otg",
			"odp",
			"otp",
			"odf",
			"odb",
			"oxt"
		};
	}



	private boolean isOsWindows() {
		return OS_NAME.indexOf("win") >= 0;
	}
	private String getComandaOffice() {
		if (isOsWindows())
    		return WINDOWS_EXEC_COMMAND;
    	else
    		return DEFAULT_EXEC_COMMAND;
	}

	private static final long serialVersionUID = -1466539285992617180L;

}
