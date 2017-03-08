/**
 * 
 */
package es.caib.ripea.plugin.caib.arxiu;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * @author josepg
 *
 */
public class DateFormatTest {

	public static void main(String[] args) {
		try {
			/*TimeZone tz = TimeZone.getTimeZone("UTC");
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
			df.setTimeZone(tz);
			String txt = df.format(new Date());
			System.out.println(">>> " + txt);
			System.out.println(">>> " + df.parse(txt));*/
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX");
			System.out.println(">>> " + df.parse("2017-02-16T00:00:00.000+01:00"));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
