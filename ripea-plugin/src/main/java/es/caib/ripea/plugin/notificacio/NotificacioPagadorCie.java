package es.caib.ripea.plugin.notificacio;

import javax.xml.datatype.XMLGregorianCalendar;

/**
 * Informació del pagador cie per a interactuar amb el Notib.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public class NotificacioPagadorCie {
	
	private XMLGregorianCalendar contracteDataVigencia;
    private String dir3Codi;
    
    
	public NotificacioPagadorCie(
			XMLGregorianCalendar contracteDataVigencia,
			String dir3Codi) {
		super();
		this.contracteDataVigencia = contracteDataVigencia;
		this.dir3Codi = dir3Codi;
	}


	public XMLGregorianCalendar getContracteDataVigencia() {
		return contracteDataVigencia;
	}
	public void setContracteDataVigencia(XMLGregorianCalendar contracteDataVigencia) {
		this.contracteDataVigencia = contracteDataVigencia;
	}
	
	public String getDir3Codi() {
		return dir3Codi;
	}
	public void setDir3Codi(String dir3Codi) {
		this.dir3Codi = dir3Codi;
	}   
		
	
}
