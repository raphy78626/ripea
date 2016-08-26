package es.caib.ripea.core.api.dto;

public class PaisDto implements Comparable<PaisDto>{
	private String codi_numeric;
	private String alpha2;
	private String alpha3;
	private String nom;
	
	public PaisDto() {
		
	}
	
	public PaisDto(
			String codi_numeric,
			String alpha2,
			String alpha3,
			String nom) {
		this.codi_numeric = codi_numeric;
		this.alpha2 = alpha2;
		this.alpha3 = alpha3;
		this.nom = nom;
	}

	public String getCodi_numeric() {
		return codi_numeric;
	}
	public void setCodi_numeric(String codi_numeric) {
		this.codi_numeric = codi_numeric;
	}
	public String getAlpha2() {
		return alpha2;
	}
	public void setAlpha2(String alpha2) {
		this.alpha2 = alpha2;
	}
	public String getAlpha3() {
		return alpha3;
	}
	public void setAlpha3(String alpha3) {
		this.alpha3 = alpha3;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}

	@Override
	public int compareTo(PaisDto o) {
		return nom.compareToIgnoreCase(o.getNom());
	}
	
}
