/**
 * 
 */
package es.caib.ripea.core.api.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Informació d'un contenidor.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public abstract class ContenidorDto extends AuditoriaDto {

	protected Long id;
	protected String nom;
	protected List<ContenidorDto> fills;
	protected List<ContenidorDto> path;
	protected List<RegistreAnotacioDto> registres;
	protected EntitatDto entitat;
	protected int esborrat;
	protected Date darrerMovimentData;
	protected UsuariDto darrerMovimentUsuari;
	protected String darrerMovimentComentari;

	protected boolean perConvertirJson;



	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public List<ContenidorDto> getFills() {
		return fills;
	}
	public void setFills(List<ContenidorDto> fills) {
		this.fills = fills;
	}
	public List<ContenidorDto> getPath() {
		return path;
	}
	public void setPath(List<ContenidorDto> path) {
		this.path = path;
	}
	public List<RegistreAnotacioDto> getRegistres() {
		return registres;
	}
	public void setRegistres(List<RegistreAnotacioDto> registres) {
		this.registres = registres;
	}
	public EntitatDto getEntitat() {
		return entitat;
	}
	public void setEntitat(EntitatDto entitat) {
		this.entitat = entitat;
	}
	public boolean isEsborrat() {
		return esborrat > 0;
	}
	public void setEsborrat(int esborrat) {
		this.esborrat = esborrat;
	}
	public Date getDarrerMovimentData() {
		return darrerMovimentData;
	}
	public void setDarrerMovimentData(Date darrerMovimentData) {
		this.darrerMovimentData = darrerMovimentData;
	}
	public UsuariDto getDarrerMovimentUsuari() {
		return darrerMovimentUsuari;
	}
	public void setDarrerMovimentUsuari(UsuariDto darrerMovimentUsuari) {
		this.darrerMovimentUsuari = darrerMovimentUsuari;
	}
	public String getDarrerMovimentComentari() {
		return darrerMovimentComentari;
	}
	public void setDarrerMovimentComentari(String darrerMovimentComentari) {
		this.darrerMovimentComentari = darrerMovimentComentari;
	}
	public boolean isPerConvertirJson() {
		return perConvertirJson;
	}
	public void setPerConvertirJson(boolean perConvertirJson) {
		this.perConvertirJson = perConvertirJson;
		if (fills != null) {
			for (ContenidorDto fill: fills) {
				fill.setPerConvertirJson(perConvertirJson);
			}
		}
	}

	public ContenidorDto getPare() {
		if (getPath() != null && !getPath().isEmpty())
			return getPath().get(getPath().size() - 1);
		else
			return null;
	}

	public String getPathAsString() {
		if (getPath() == null)
			return null;
		StringBuilder pathString = new StringBuilder();
		for (ContenidorDto pathElement: getPath()) {
			pathString.append("/");
			if (pathElement instanceof EscriptoriDto) {
				if (entitat != null)
					pathString.append(entitat.getNom());
				else
					pathString.append(pathElement.getNom());
			} else {
				pathString.append(pathElement.getNom());
			}
		}
		return pathString.toString();
	}
	public String getPathAsStringAmbNomContenidor() {
		return getPathAsString() + "/" + nom;
	}

	public ExpedientDto getExpedientPare() {
		if (getPath() == null)
			return null;
		if (this instanceof ExpedientDto) {
			if (perConvertirJson)
				return (ExpedientDto)copiarContenidor(this);
			else
				return (ExpedientDto)this;
		}
		for (int i = getPath().size() - 1; i >= 0; i--) {
			ContenidorDto contenidor = getPath().get(i);
			if (contenidor instanceof ExpedientDto) {
				return (ExpedientDto)contenidor;
			}
		}
		return null;
	}

	public EscriptoriDto getEscriptoriPare() {
		if (getPath() == null)
			return null;
		if (this instanceof EscriptoriDto) {
			if (perConvertirJson)
				return (EscriptoriDto)copiarContenidor(this);
			else
				return (EscriptoriDto)this;
		}
		for (int i = getPath().size() - 1; i >= 0; i--) {
			ContenidorDto contenidor = getPath().get(i);
			if (contenidor instanceof EscriptoriDto) {
				return (EscriptoriDto)contenidor;
			}
		}
		return null;
	}
	public String getNomPropietariEscriptoriPare() {
		EscriptoriDto escriptoriPare = getEscriptoriPare();
		if (escriptoriPare != null) {
			return escriptoriPare.getCreatedBy().getNom();
		} else {
			return null;
		}
	}
	public String getCodiPropietariEscriptoriPare() {
		EscriptoriDto escriptoriPare = getEscriptoriPare();
		if (escriptoriPare != null) {
			return escriptoriPare.getCreatedBy().getCodi();
		} else {
			return null;
		}
	}

	public List<ContenidorDto> getFillsExpedients() {
		List<ContenidorDto> expedients = new ArrayList<ContenidorDto>();
		if (fills != null) {
			for (ContenidorDto contenidor: fills) {
				if (contenidor instanceof ExpedientDto)
					expedients.add(contenidor);
			}
		}
		return expedients;
	}
	public List<ContenidorDto> getFillsNoExpedients() {
		List<ContenidorDto> noExpedients = new ArrayList<ContenidorDto>();
		if (fills != null) {
			for (ContenidorDto contenidor: fills) {
				if (!(contenidor instanceof ExpedientDto))
					noExpedients.add(contenidor);
			}
		}
		return noExpedients;
	}
	
	public int getFillsCount() {
		return (fills == null) ? 0 : fills.size();
	}
	public int getFillsExpedientsCount() {
		if  (fills == null) {
			return 0;
		} else {
			int count = 0;
			for (ContenidorDto contenidor: fills) {
				if (contenidor instanceof ExpedientDto)
					count++;
			}
			return count;
		}
	}
	public int getFillsNoExpedientsCount() {
		if  (fills == null) {
			return 0;
		} else {
			int count = 0;
			for (ContenidorDto contenidor: fills) {
				if (!(contenidor instanceof ExpedientDto))
					count++;
			}
			return count;
		}
	}

	public int getRegistresCount() {
		return (registres == null) ? 0 : registres.size();
	}

	public void setContenidorArrelIdPerPath(Long contenidorArrelId) {
		if (path != null) {
			if (!id.equals(contenidorArrelId)) {
				Iterator<ContenidorDto> it = path.iterator();
				boolean trobat = false;
				while (it.hasNext()) {
					ContenidorDto pathElement = it.next();
					if (pathElement.getId().equals(contenidorArrelId))
						trobat = true;
					if (!trobat) {
						it.remove();
					}
				}
			} else {
				path = null;
			}
		}
	}

	public boolean isCrearExpedients() {
		return isEscriptori();
	}

	public boolean isExpedient() {
		return this instanceof ExpedientDto;
	}
	public boolean isDocument() {
		return this instanceof DocumentDto;
	}
	public boolean isNode() {
		return this instanceof NodeDto;
	}
	public boolean isCarpeta() {
		return this instanceof CarpetaDto;
	}
	public boolean isEscriptori() {
		return this instanceof EscriptoriDto;
	}
	public boolean isArxiv() {
		return this instanceof ArxiuDto;
	}
	public boolean isBustia() {
		return this instanceof BustiaDto;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	protected abstract ContenidorDto copiarContenidor(ContenidorDto original);

	private static final long serialVersionUID = -139254994389509932L;

}
