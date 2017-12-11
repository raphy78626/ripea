package es.caib.ripea.plugin.notificacio;

import java.util.Calendar;

import javax.xml.datatype.XMLGregorianCalendar;


/**
 * Informació del pagador per a interactuar amb el Notib.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public class NotificacioPagadorPostal {
	
	private XMLGregorianCalendar contracteDataVigencia;
    private String contracteNum;
    private String dir3Codi;
    private String facturacioClientCodi;
    
    
	public NotificacioPagadorPostal(
			XMLGregorianCalendar contracteDataVigencia,
			String contracteNum,
			String dir3Codi,
			String facturacioClientCodi) {
		super();
		this.contracteDataVigencia = contracteDataVigencia;
		this.contracteNum = contracteNum;
		this.dir3Codi = dir3Codi;
		this.facturacioClientCodi = facturacioClientCodi;
	}


	public XMLGregorianCalendar getContracteDataVigencia() {
		return contracteDataVigencia;
	}
	public void setContracteDataVigencia(XMLGregorianCalendar contracteDataVigencia) {
		this.contracteDataVigencia = contracteDataVigencia;
	}
	
	public String getContracteNum() {
		return contracteNum;
	}
	public void setContracteNum(String contracteNum) {
		this.contracteNum = contracteNum;
	}
	
	public String getDir3Codi() {
		return dir3Codi;
	}
	public void setDir3Codi(String dir3Codi) {
		this.dir3Codi = dir3Codi;
	}
	
	public String getFacturacioClientCodi() {
		return facturacioClientCodi;
	}
	public void setFacturacioClientCodi(String facturacioClientCodi) {
		this.facturacioClientCodi = facturacioClientCodi;
	}
	
    
}
