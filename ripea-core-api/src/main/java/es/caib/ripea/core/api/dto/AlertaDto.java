/**
 * 
 */
package es.caib.ripea.core.api.dto;

import java.util.Date;

/**
 * Informació d'una alerta.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public class AlertaDto {
	
	private Long id;
	private String text;
	private boolean llegida;
	private Long contingutId;
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	
	public boolean isLlegida() {
		return llegida;
	}
	public void setLlegida(boolean llegida) {
		this.llegida = llegida;
	}
	
	public Long getContingutId() {
		return contingutId;
	}
	public void setContingutId(Long contingutId) {
		this.contingutId = contingutId;
	}
	
	
	public String getTextResum() {
		return text.length() < 90 ? text + " ..." : text.substring(0, 90) + " ...";
	}
	
	
	private static final long serialVersionUID = -2299453443943600172L;
	
}
