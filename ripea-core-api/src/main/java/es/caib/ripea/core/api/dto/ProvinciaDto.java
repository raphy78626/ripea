package es.caib.ripea.core.api.dto;

public class ProvinciaDto implements Comparable<ProvinciaDto>{
	private String codi;
	private String nom;
	
	public ProvinciaDto() {
		
	}
	
	public ProvinciaDto(
			String codi,
			String nom) {
		this.codi = codi;
		this.nom = nom;
	}

	public String getCodi() {
		return codi;
	}
	public void setCodi(String codi) {
		this.codi = codi;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}

	@Override
	public int compareTo(ProvinciaDto o) {
		return nom.compareToIgnoreCase(o.getNom());
	}
	
}
