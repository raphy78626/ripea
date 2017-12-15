package es.caib.ripea.plugin.notificacio;

import java.util.List;

import javax.xml.datatype.XMLGregorianCalendar;

/**
 * Informació de la consulta de la notificació del Notib.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public class NotificacioInformacioEnviament {
	
	protected NotificacioCertificacio certificacio;
    protected String concepte;
    protected XMLGregorianCalendar dataCaducitat;
    protected XMLGregorianCalendar dataCreacio;
    protected XMLGregorianCalendar dataPostaDisposicio;
    protected String descripcio;
    protected String destiDir3Codi;
    protected String destiDir3Descripcio;
    protected List<NotificacioPersona> destinataris;
    protected String emisorArrelDir3Codi;
    protected String emisorArrelDir3Descripcio;
    protected String emisorDir3Codi;
    protected String emisorDir3Descripcio;
    protected NotificacioEntregaDeh entregaDeh;
    protected NotificacioEntregaPostal entregaPostal;
    protected NotificacioEnviamentTipusEnum enviamentTipus;
    protected NotificacioEnviamentEstatEnum estat;
    protected XMLGregorianCalendar estatData;
    protected String identificador;
    protected boolean notificaError;
    protected XMLGregorianCalendar notificaErrorData;
    protected String notificaErrorDescripcio;
    protected NotificacioEnviamentEstatEnum notificaEstat;
    protected XMLGregorianCalendar notificaEstatData;
    protected String procedimentCodi;
    protected String procedimentDescripcio;
    protected String referencia;
    protected Integer retard;
    protected boolean seuError;
    protected XMLGregorianCalendar seuErrorData;
    protected String seuErrorDescripcio;
    protected NotificacioEnviamentEstatEnum seuEstat;
    protected XMLGregorianCalendar seuEstatData;
    protected NotificacioPersona titular;
    
    
	public NotificacioInformacioEnviament(
			NotificacioCertificacio certificacio,
			String concepte,
			XMLGregorianCalendar dataCaducitat,
			XMLGregorianCalendar dataCreacio,
			XMLGregorianCalendar dataPostaDisposicio,
			String descripcio,
			String destiDir3Codi,
			String destiDir3Descripcio,
			List<NotificacioPersona> destinataris,
			String emisorArrelDir3Codi,
			String emisorArrelDir3Descripcio,
			String emisorDir3Codi,
			String emisorDir3Descripcio,
			NotificacioEntregaDeh entregaDeh,
			NotificacioEntregaPostal entregaPostal,
			NotificacioEnviamentTipusEnum enviamentTipus,
			NotificacioEnviamentEstatEnum estat,
			XMLGregorianCalendar estatData,
			String identificador,
			boolean notificaError,
			XMLGregorianCalendar notificaErrorData,
			String notificaErrorDescripcio,
			NotificacioEnviamentEstatEnum notificaEstat,
			XMLGregorianCalendar notificaEstatData,
			String procedimentCodi,
			String procedimentDescripcio,
			String referencia,
			Integer retard,
			boolean seuError,
			XMLGregorianCalendar seuErrorData,
			String seuErrorDescripcio,
			NotificacioEnviamentEstatEnum seuEstat,
			XMLGregorianCalendar seuEstatData,
			NotificacioPersona titular) {
		super();
		this.certificacio = certificacio;
		this.concepte = concepte;
		this.dataCaducitat = dataCaducitat;
		this.dataCreacio = dataCreacio;
		this.dataPostaDisposicio = dataPostaDisposicio;
		this.descripcio = descripcio;
		this.destiDir3Codi = destiDir3Codi;
		this.destiDir3Descripcio = destiDir3Descripcio;
		this.destinataris = destinataris;
		this.emisorArrelDir3Codi = emisorArrelDir3Codi;
		this.emisorArrelDir3Descripcio = emisorArrelDir3Descripcio;
		this.emisorDir3Codi = emisorDir3Codi;
		this.emisorDir3Descripcio = emisorDir3Descripcio;
		this.entregaDeh = entregaDeh;
		this.entregaPostal = entregaPostal;
		this.enviamentTipus = enviamentTipus;
		this.estat = estat;
		this.estatData = estatData;
		this.identificador = identificador;
		this.notificaError = notificaError;
		this.notificaErrorData = notificaErrorData;
		this.notificaErrorDescripcio = notificaErrorDescripcio;
		this.notificaEstat = notificaEstat;
		this.notificaEstatData = notificaEstatData;
		this.procedimentCodi = procedimentCodi;
		this.procedimentDescripcio = procedimentDescripcio;
		this.referencia = referencia;
		this.retard = retard;
		this.seuError = seuError;
		this.seuErrorData = seuErrorData;
		this.seuErrorDescripcio = seuErrorDescripcio;
		this.seuEstat = seuEstat;
		this.seuEstatData = seuEstatData;
		this.titular = titular;
	}


	public NotificacioCertificacio getCertificacio() {
		return certificacio;
	}
	public void setCertificacio(NotificacioCertificacio certificacio) {
		this.certificacio = certificacio;
	}
	
	public String getConcepte() {
		return concepte;
	}
	public void setConcepte(String concepte) {
		this.concepte = concepte;
	}
	
	public XMLGregorianCalendar getDataCaducitat() {
		return dataCaducitat;
	}
	public void setDataCaducitat(XMLGregorianCalendar dataCaducitat) {
		this.dataCaducitat = dataCaducitat;
	}
	
	public XMLGregorianCalendar getDataCreacio() {
		return dataCreacio;
	}
	public void setDataCreacio(XMLGregorianCalendar dataCreacio) {
		this.dataCreacio = dataCreacio;
	}
	
	public XMLGregorianCalendar getDataPostaDisposicio() {
		return dataPostaDisposicio;
	}
	public void setDataPostaDisposicio(XMLGregorianCalendar dataPostaDisposicio) {
		this.dataPostaDisposicio = dataPostaDisposicio;
	}
	
	public String getDescripcio() {
		return descripcio;
	}
	public void setDescripcio(String descripcio) {
		this.descripcio = descripcio;
	}
	
	public String getDestiDir3Codi() {
		return destiDir3Codi;
	}
	public void setDestiDir3Codi(String destiDir3Codi) {
		this.destiDir3Codi = destiDir3Codi;
	}
	
	public String getDestiDir3Descripcio() {
		return destiDir3Descripcio;
	}
	public void setDestiDir3Descripcio(String destiDir3Descripcio) {
		this.destiDir3Descripcio = destiDir3Descripcio;
	}
	
	public List<NotificacioPersona> getDestinataris() {
		return destinataris;
	}
	public void setDestinataris(List<NotificacioPersona> destinataris) {
		this.destinataris = destinataris;
	}
	
	public String getEmisorArrelDir3Codi() {
		return emisorArrelDir3Codi;
	}
	public void setEmisorArrelDir3Codi(String emisorArrelDir3Codi) {
		this.emisorArrelDir3Codi = emisorArrelDir3Codi;
	}
	
	public String getEmisorArrelDir3Descripcio() {
		return emisorArrelDir3Descripcio;
	}
	public void setEmisorArrelDir3Descripcio(String emisorArrelDir3Descripcio) {
		this.emisorArrelDir3Descripcio = emisorArrelDir3Descripcio;
	}
	
	public String getEmisorDir3Codi() {
		return emisorDir3Codi;
	}
	public void setEmisorDir3Codi(String emisorDir3Codi) {
		this.emisorDir3Codi = emisorDir3Codi;
	}
	
	public String getEmisorDir3Descripcio() {
		return emisorDir3Descripcio;
	}
	public void setEmisorDir3Descripcio(String emisorDir3Descripcio) {
		this.emisorDir3Descripcio = emisorDir3Descripcio;
	}
	
	public NotificacioEntregaDeh getEntregaDeh() {
		return entregaDeh;
	}
	public void setEntregaDeh(NotificacioEntregaDeh entregaDeh) {
		this.entregaDeh = entregaDeh;
	}
	
	public NotificacioEntregaPostal getEntregaPostal() {
		return entregaPostal;
	}
	public void setEntregaPostal(NotificacioEntregaPostal entregaPostal) {
		this.entregaPostal = entregaPostal;
	}
	
	public NotificacioEnviamentTipusEnum getEnviamentTipus() {
		return enviamentTipus;
	}
	public void setEnviamentTipus(NotificacioEnviamentTipusEnum enviamentTipus) {
		this.enviamentTipus = enviamentTipus;
	}
	
	public NotificacioEnviamentEstatEnum getEstat() {
		return estat;
	}
	public void setEstat(NotificacioEnviamentEstatEnum estat) {
		this.estat = estat;
	}
	
	public XMLGregorianCalendar getEstatData() {
		return estatData;
	}
	public void setEstatData(XMLGregorianCalendar estatData) {
		this.estatData = estatData;
	}
	
	public String getIdentificador() {
		return identificador;
	}
	public void setIdentificador(String identificador) {
		this.identificador = identificador;
	}
	
	public boolean isNotificaError() {
		return notificaError;
	}
	public void setNotificaError(boolean notificaError) {
		this.notificaError = notificaError;
	}
	
	public XMLGregorianCalendar getNotificaErrorData() {
		return notificaErrorData;
	}
	public void setNotificaErrorData(XMLGregorianCalendar notificaErrorData) {
		this.notificaErrorData = notificaErrorData;
	}
	
	public String getNotificaErrorDescripcio() {
		return notificaErrorDescripcio;
	}
	public void setNotificaErrorDescripcio(String notificaErrorDescripcio) {
		this.notificaErrorDescripcio = notificaErrorDescripcio;
	}
	
	public NotificacioEnviamentEstatEnum getNotificaEstat() {
		return notificaEstat;
	}
	public void setNotificaEstat(NotificacioEnviamentEstatEnum notificaEstat) {
		this.notificaEstat = notificaEstat;
	}
	
	public XMLGregorianCalendar getNotificaEstatData() {
		return notificaEstatData;
	}
	public void setNotificaEstatData(XMLGregorianCalendar notificaEstatData) {
		this.notificaEstatData = notificaEstatData;
	}
	
	public String getProcedimentCodi() {
		return procedimentCodi;
	}
	public void setProcedimentCodi(String procedimentCodi) {
		this.procedimentCodi = procedimentCodi;
	}
	
	public String getProcedimentDescripcio() {
		return procedimentDescripcio;
	}
	public void setProcedimentDescripcio(String procedimentDescripcio) {
		this.procedimentDescripcio = procedimentDescripcio;
	}
	
	public String getReferencia() {
		return referencia;
	}
	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}
	
	public Integer getRetard() {
		return retard;
	}
	public void setRetard(Integer retard) {
		this.retard = retard;
	}
	
	public boolean isSeuError() {
		return seuError;
	}
	public void setSeuError(boolean seuError) {
		this.seuError = seuError;
	}
	
	public XMLGregorianCalendar getSeuErrorData() {
		return seuErrorData;
	}
	public void setSeuErrorData(XMLGregorianCalendar seuErrorData) {
		this.seuErrorData = seuErrorData;
	}
	
	public String getSeuErrorDescripcio() {
		return seuErrorDescripcio;
	}
	public void setSeuErrorDescripcio(String seuErrorDescripcio) {
		this.seuErrorDescripcio = seuErrorDescripcio;
	}
	
	public NotificacioEnviamentEstatEnum getSeuEstat() {
		return seuEstat;
	}
	public void setSeuEstat(NotificacioEnviamentEstatEnum seuEstat) {
		this.seuEstat = seuEstat;
	}
	
	public XMLGregorianCalendar getSeuEstatData() {
		return seuEstatData;
	}
	public void setSeuEstatData(XMLGregorianCalendar seuEstatData) {
		this.seuEstatData = seuEstatData;
	}
	
	public NotificacioPersona getTitular() {
		return titular;
	}
	public void setTitular(NotificacioPersona titular) {
		this.titular = titular;
	}
    
	
}
