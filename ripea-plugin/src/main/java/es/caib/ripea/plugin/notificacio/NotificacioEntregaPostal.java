package es.caib.ripea.plugin.notificacio;

/**
 * Informació de la entrega postal per a interactuar amb el Notib.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public class NotificacioEntregaPostal {
	
	private String apartatCorreus;
    private String bloc;
    private Integer cie;
    private String codiPostal;
    private String complement;
    private String escala;
    private String formatFulla;
    private String formatSobre;
    private String linea1;
    private String linea2;
    private String municipiCodi;
    private String numeroCasa;
    private String numeroQualificador;
    private String paisCodi;
    private String planta;
    private String poblacio;
    private String porta;
    private String portal;
    private String provinciaCodi;
    private String puntKm;
    private NotificacioEntregaPostalTipusEnum tipus;
    private String viaNom;
    private NotificacioEntregaPostalViaTipusEnum viaTipus;
    
    
	public NotificacioEntregaPostal(
			String apartatCorreus,
			String bloc,
			Integer cie,
			String codiPostal,
			String complement,
			String escala,
			String formatFulla,
			String formatSobre,
			String linea1,
			String linea2,
			String municipiCodi,
			String numeroCasa,
			String numeroQualificador,
			String paisCodi,
			String planta,
			String poblacio,
			String porta,
			String portal,
			String provinciaCodi,
			String puntKm,
			NotificacioEntregaPostalTipusEnum tipus,
			String viaNom,
			NotificacioEntregaPostalViaTipusEnum viaTipus) {
		super();
		this.apartatCorreus = apartatCorreus;
		this.bloc = bloc;
		this.cie = cie;
		this.codiPostal = codiPostal;
		this.complement = complement;
		this.escala = escala;
		this.formatFulla = formatFulla;
		this.formatSobre = formatSobre;
		this.linea1 = linea1;
		this.linea2 = linea2;
		this.municipiCodi = municipiCodi;
		this.numeroCasa = numeroCasa;
		this.numeroQualificador = numeroQualificador;
		this.paisCodi = paisCodi;
		this.planta = planta;
		this.poblacio = poblacio;
		this.porta = porta;
		this.portal = portal;
		this.provinciaCodi = provinciaCodi;
		this.puntKm = puntKm;
		this.tipus = tipus;
		this.viaNom = viaNom;
		this.viaTipus = viaTipus;
	}
	public String getApartatCorreus() {
		return apartatCorreus;
	}
	public void setApartatCorreus(String apartatCorreus) {
		this.apartatCorreus = apartatCorreus;
	}
	
	public String getBloc() {
		return bloc;
	}
	public void setBloc(String bloc) {
		this.bloc = bloc;
	}
	
	public Integer getCie() {
		return cie;
	}
	public void setCie(Integer cie) {
		this.cie = cie;
	}
	
	public String getCodiPostal() {
		return codiPostal;
	}
	public void setCodiPostal(String codiPostal) {
		this.codiPostal = codiPostal;
	}
	
	public String getComplement() {
		return complement;
	}
	public void setComplement(String complement) {
		this.complement = complement;
	}
	
	public String getEscala() {
		return escala;
	}
	public void setEscala(String escala) {
		this.escala = escala;
	}
	
	public String getFormatFulla() {
		return formatFulla;
	}
	public void setFormatFulla(String formatFulla) {
		this.formatFulla = formatFulla;
	}
	
	public String getFormatSobre() {
		return formatSobre;
	}
	public void setFormatSobre(String formatSobre) {
		this.formatSobre = formatSobre;
	}
	
	public String getLinea1() {
		return linea1;
	}
	public void setLinea1(String linea1) {
		this.linea1 = linea1;
	}
	
	public String getLinea2() {
		return linea2;
	}
	public void setLinea2(String linea2) {
		this.linea2 = linea2;
	}
	
	public String getMunicipiCodi() {
		return municipiCodi;
	}
	public void setMunicipiCodi(String municipiCodi) {
		this.municipiCodi = municipiCodi;
	}
	
	public String getNumeroCasa() {
		return numeroCasa;
	}
	public void setNumeroCasa(String numeroCasa) {
		this.numeroCasa = numeroCasa;
	}
	
	public String getNumeroQualificador() {
		return numeroQualificador;
	}
	public void setNumeroQualificador(String numeroQualificador) {
		this.numeroQualificador = numeroQualificador;
	}
	
	public String getPaisCodi() {
		return paisCodi;
	}
	public void setPaisCodi(String paisCodi) {
		this.paisCodi = paisCodi;
	}
	
	public String getPlanta() {
		return planta;
	}
	public void setPlanta(String planta) {
		this.planta = planta;
	}
	
	public String getPoblacio() {
		return poblacio;
	}
	public void setPoblacio(String poblacio) {
		this.poblacio = poblacio;
	}
	
	public String getPorta() {
		return porta;
	}
	public void setPorta(String porta) {
		this.porta = porta;
	}
	
	public String getPortal() {
		return portal;
	}
	public void setPortal(String portal) {
		this.portal = portal;
	}
	
	public String getProvinciaCodi() {
		return provinciaCodi;
	}
	public void setProvinciaCodi(String provinciaCodi) {
		this.provinciaCodi = provinciaCodi;
	}
	
	public String getPuntKm() {
		return puntKm;
	}
	public void setPuntKm(String puntKm) {
		this.puntKm = puntKm;
	}
	
	public NotificacioEntregaPostalTipusEnum getTipus() {
		return tipus;
	}
	public void setTipus(NotificacioEntregaPostalTipusEnum tipus) {
		this.tipus = tipus;
	}
	
	public String getViaNom() {
		return viaNom;
	}
	public void setViaNom(String viaNom) {
		this.viaNom = viaNom;
	}
	
	public NotificacioEntregaPostalViaTipusEnum getViaTipus() {
		return viaTipus;
	}
	public void setViaTipus(NotificacioEntregaPostalViaTipusEnum viaTipus) {
		this.viaTipus = viaTipus;
	}
    
}
