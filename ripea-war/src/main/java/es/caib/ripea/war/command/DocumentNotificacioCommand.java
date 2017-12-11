/**
 * 
 */
package es.caib.ripea.war.command;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.validator.constraints.NotEmpty;

import es.caib.ripea.core.api.dto.DocumentDto;
import es.caib.ripea.core.api.dto.DocumentEnviamentEstatEnumDto;
import es.caib.ripea.core.api.dto.DocumentNotificacioDto;
import es.caib.ripea.core.api.dto.DocumentNotificacioTipusEnumDto;
import es.caib.ripea.core.api.dto.InteressatIdiomaEnumDto;
import es.caib.ripea.core.api.dto.NotificacioEnviamentTipusEnumDto;
import es.caib.ripea.core.entity.ServeiTipusEnum;
import es.caib.ripea.war.helper.ConversioTipusHelper;

/**
 * Command per a gestionar les notificacions de documents.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public class DocumentNotificacioCommand {
	
	private Long id;
	@NotNull
	private DocumentNotificacioTipusEnumDto tipus;
	private DocumentEnviamentEstatEnumDto estat;
	@NotEmpty(groups = {Create.class, Update.class})
	@Size(max = 64, groups = {Create.class, Update.class})
	private String assumpte;
	@Size(groups = {Create.class, Update.class}, max = 256)
	private String observacions;
	
	private Date caducitat;
	@NotEmpty(groups = {Electronica.class})
	@Size(max = 50, groups = {Electronica.class})
	private String concepte;
	@Size(max = 100, groups = {Electronica.class})
	private String descripcio;
	@NotEmpty(groups = {Electronica.class})
	@Size(max = 9, groups = {Electronica.class})
	private String emisorDir3Codi;
	private Date enviamentDataProgramada;
	@NotEmpty(groups = {Electronica.class})
	private NotificacioEnviamentTipusEnumDto enviamentTipus;
	
	@NotNull(groups = {Electronica.class})
	private Long interessatId;
	
	private boolean dehObligat;
	@Size(max = 9, groups = {Electronica.class})
	private String dehNif;
	
	@Size(max = 20, groups = {Electronica.class})
	private String notificaReferencia;
	private ServeiTipusEnum serveiTipus;
    
	@Size(max = 100, groups = {Electronica.class})
	private String titularNom;
	@Size(max = 100, groups = {Electronica.class})
	private String titularLlinatge1;
	@Size(max = 100, groups = {Electronica.class})
	private String titularLlinatge2;
	@NotEmpty(groups = {Electronica.class})
	@Size(max = 9, groups = {Electronica.class})
	private String titularNif;
	@Size(max = 16, groups = {Electronica.class})
	private String titularTelefon;
	@Size(max = 100, groups = {Electronica.class})
	private String titularEmail;
	
	@Size(max = 9, groups = {Electronica.class})
	private String pagadorCieCodiDir3;
	private Date pagadorCieDataVigencia;
	
	@Size(max = 9, groups = {Electronica.class})
	private String pagadorCorreusCodiDir3;
	@Size(max = 20, groups = {Electronica.class})
	private String pagadorCorreusContracteNum;
	@Size(max = 20, groups = {Electronica.class})
	private String pagadorCorreusCodiClientFacturacio;
	private Date pagadorCorreusDataVigencia;
	
	@NotEmpty(groups = {Electronica.class}) @Size(max = 10)
	private String seuExpedientSerieDocumental;
	@NotEmpty(groups = {Electronica.class}) @Size(max = 10)
	private String seuExpedientUnitatOrganitzativa;
	@NotEmpty(groups = {Electronica.class}) @Size(max = 52)
	private String seuExpedientIdentificadorEni;
	@NotEmpty(groups = {Electronica.class}) @Size(max = 256)
	private String seuExpedientTitol;
	@NotEmpty(groups = {Electronica.class}) @Size(max = 256)
	private String seuRegistreOficina;
	@NotEmpty(groups = {Electronica.class}) @Size(max = 256)
	private String seuRegistreLlibre;
	@NotEmpty(groups = {Electronica.class}) @Size(max = 256)
	private String seuIdioma;
	@NotEmpty(groups = {Electronica.class}) @Size(max = 256)
	private String seuAvisTitol;
	@NotEmpty(groups = {Electronica.class}) @Size(max = 256)
	private String seuAvisText;
	@Size(max = 256, groups = {Electronica.class})
	private String seuAvisTextMobil;
	@NotEmpty(groups = {Electronica.class}) @Size(max = 256)
	private String seuOficiTitol;
	@NotEmpty(groups = {Electronica.class}) @Size(max = 256)
	private String seuOficiText;
	
	@NotEmpty(groups = {Electronica.class})
	@Size(max = 6, groups = {Electronica.class})
	private String procedimentCodiSia;
	private Integer retardPostal;
	
	@NotEmpty(groups = {Electronica.class})
	@Size(max = 40, groups = {Electronica.class})
	private String documentHash;
	@NotEmpty(groups = {Electronica.class})
	private boolean documentNormalitzat;
	@NotEmpty(groups = {Electronica.class})
	private boolean documentGenerarCsv;
	
	private List<Long> annexos;
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public DocumentNotificacioTipusEnumDto getTipus() {
		return tipus;
	}
	public void setTipus(DocumentNotificacioTipusEnumDto tipus) {
		this.tipus = tipus;
	}
	
	public DocumentEnviamentEstatEnumDto getEstat() {
		return estat;
	}
	public void setEstat(DocumentEnviamentEstatEnumDto estat) {
		this.estat = estat;
	}
	
	public String getAssumpte() {
		return assumpte;
	}
	public void setAssumpte(String assumpte) {
		this.assumpte = assumpte;
	}
	
	public String getObservacions() {
		return observacions;
	}
	public void setObservacions(String observacions) {
		this.observacions = observacions;
	}
	
	public Date getCaducitat() {
		return caducitat;
	}
	public void setCaducitat(Date caducitat) {
		this.caducitat = caducitat;
	}
	
	public String getConcepte() {
		return concepte;
	}
	public void setConcepte(String concepte) {
		this.concepte = concepte;
	}
	
	public String getDescripcio() {
		return descripcio;
	}
	public void setDescripcio(String descripcio) {
		this.descripcio = descripcio;
	}
	
	public String getEmisorDir3Codi() {
		return emisorDir3Codi;
	}
	public void setEmisorDir3Codi(String emisorDir3Codi) {
		this.emisorDir3Codi = emisorDir3Codi;
	}
	
	public Date getEnviamentDataProgramada() {
		return enviamentDataProgramada;
	}
	public void setEnviamentDataProgramada(Date enviamentDataProgramada) {
		this.enviamentDataProgramada = enviamentDataProgramada;
	}
	
	public NotificacioEnviamentTipusEnumDto getEnviamentTipus() {
		return enviamentTipus;
	}
	public void setEnviamentTipus(NotificacioEnviamentTipusEnumDto enviamentTipus) {
		this.enviamentTipus = enviamentTipus;
	}
	
	public Long getInteressatId() {
		return interessatId;
	}
	public void setInteressatId(Long interessatId) {
		this.interessatId = interessatId;
	}
	
	public boolean isDehObligat() {
		return dehObligat;
	}
	public void setDehObligat(boolean dehObligat) {
		this.dehObligat = dehObligat;
	}
	
	public String getDehNif() {
		return dehNif;
	}
	public void setDehNif(String dehNif) {
		this.dehNif = dehNif;
	}
	
	public String getNotificaReferencia() {
		return notificaReferencia;
	}
	public void setNotificaReferencia(String notificaReferencia) {
		this.notificaReferencia = notificaReferencia;
	}
	
	public ServeiTipusEnum getServeiTipus() {
		return serveiTipus;
	}
	public void setServeiTipus(ServeiTipusEnum serveiTipus) {
		this.serveiTipus = serveiTipus;
	}
	
	public String getTitularNom() {
		return titularNom;
	}
	public void setTitularNom(String titularNom) {
		this.titularNom = titularNom;
	}
	
	public String getTitularLlinatge1() {
		return titularLlinatge1;
	}
	public void setTitularLlinatge1(String titularLlinatge1) {
		this.titularLlinatge1 = titularLlinatge1;
	}
	
	public String getTitularLlinatge2() {
		return titularLlinatge2;
	}
	public void setTitularLlinatge2(String titularLlinatge2) {
		this.titularLlinatge2 = titularLlinatge2;
	}
	
	public String getTitularNif() {
		return titularNif;
	}
	public void setTitularNif(String titularNif) {
		this.titularNif = titularNif;
	}
	
	public String getTitularTelefon() {
		return titularTelefon;
	}
	public void setTitularTelefon(String titularTelefon) {
		this.titularTelefon = titularTelefon;
	}
	
	public String getTitularEmail() {
		return titularEmail;
	}
	public void setTitularEmail(String titularEmail) {
		this.titularEmail = titularEmail;
	}
	
	public String getPagadorCieCodiDir3() {
		return pagadorCieCodiDir3;
	}
	public void setPagadorCieCodiDir3(String pagadorCieCodiDir3) {
		this.pagadorCieCodiDir3 = pagadorCieCodiDir3;
	}
	
	public Date getPagadorCieDataVigencia() {
		return pagadorCieDataVigencia;
	}
	public void setPagadorCieDataVigencia(Date pagadorCieDataVigencia) {
		this.pagadorCieDataVigencia = pagadorCieDataVigencia;
	}
	
	public String getPagadorCorreusCodiDir3() {
		return pagadorCorreusCodiDir3;
	}
	public void setPagadorCorreusCodiDir3(String pagadorCorreusCodiDir3) {
		this.pagadorCorreusCodiDir3 = pagadorCorreusCodiDir3;
	}
	
	public String getPagadorCorreusContracteNum() {
		return pagadorCorreusContracteNum;
	}
	public void setPagadorCorreusContracteNum(String pagadorCorreusContracteNum) {
		this.pagadorCorreusContracteNum = pagadorCorreusContracteNum;
	}
	
	public String getPagadorCorreusCodiClientFacturacio() {
		return pagadorCorreusCodiClientFacturacio;
	}
	public void setPagadorCorreusCodiClientFacturacio(String pagadorCorreusCodiClientFacturacio) {
		this.pagadorCorreusCodiClientFacturacio = pagadorCorreusCodiClientFacturacio;
	}
	
	public Date getPagadorCorreusDataVigencia() {
		return pagadorCorreusDataVigencia;
	}
	public void setPagadorCorreusDataVigencia(Date pagadorCorreusDataVigencia) {
		this.pagadorCorreusDataVigencia = pagadorCorreusDataVigencia;
	}
	
	public String getSeuExpedientSerieDocumental() {
		return seuExpedientSerieDocumental;
	}
	public void setSeuExpedientSerieDocumental(String seuExpedientSerieDocumental) {
		this.seuExpedientSerieDocumental = seuExpedientSerieDocumental;
	}
	
	public String getSeuExpedientUnitatOrganitzativa() {
		return seuExpedientUnitatOrganitzativa;
	}
	public void setSeuExpedientUnitatOrganitzativa(String seuExpedientUnitatOrganitzativa) {
		this.seuExpedientUnitatOrganitzativa = seuExpedientUnitatOrganitzativa;
	}
	
	public String getSeuExpedientIdentificadorEni() {
		return seuExpedientIdentificadorEni;
	}
	public void setSeuExpedientIdentificadorEni(String seuExpedientIdentificadorEni) {
		this.seuExpedientIdentificadorEni = seuExpedientIdentificadorEni;
	}
	
	public String getSeuExpedientTitol() {
		return seuExpedientTitol;
	}
	public void setSeuExpedientTitol(String seuExpedientTitol) {
		this.seuExpedientTitol = seuExpedientTitol;
	}
	
	public String getSeuRegistreOficina() {
		return seuRegistreOficina;
	}
	public void setSeuRegistreOficina(String seuRegistreOficina) {
		this.seuRegistreOficina = seuRegistreOficina;
	}
	
	public String getSeuRegistreLlibre() {
		return seuRegistreLlibre;
	}
	public void setSeuRegistreLlibre(String seuRegistreLlibre) {
		this.seuRegistreLlibre = seuRegistreLlibre;
	}
	
	public String getSeuIdioma() {
		return seuIdioma;
	}
	public void setSeuIdioma(String seuIdioma) {
		this.seuIdioma = seuIdioma;
	}
	
	public String getSeuAvisTitol() {
		return seuAvisTitol;
	}
	public void setSeuAvisTitol(String seuAvisTitol) {
		this.seuAvisTitol = seuAvisTitol;
	}
	
	public String getSeuAvisText() {
		return seuAvisText;
	}
	public void setSeuAvisText(String seuAvisText) {
		this.seuAvisText = seuAvisText;
	}
	
	public String getSeuAvisTextMobil() {
		return seuAvisTextMobil;
	}
	public void setSeuAvisTextMobil(String seuAvisTextMobil) {
		this.seuAvisTextMobil = seuAvisTextMobil;
	}
	
	public String getSeuOficiTitol() {
		return seuOficiTitol;
	}
	public void setSeuOficiTitol(String seuOficiTitol) {
		this.seuOficiTitol = seuOficiTitol;
	}
	
	public String getSeuOficiText() {
		return seuOficiText;
	}
	public void setSeuOficiText(String seuOficiText) {
		this.seuOficiText = seuOficiText;
	}
	
	public String getProcedimentCodiSia() {
		return procedimentCodiSia;
	}
	public void setProcedimentCodiSia(String procedimentCodiSia) {
		this.procedimentCodiSia = procedimentCodiSia;
	}
	
	public Integer getRetardPostal() {
		return retardPostal;
	}
	public void setRetardPostal(Integer retardPostal) {
		this.retardPostal = retardPostal;
	}
	
	public String getDocumentHash() {
		return documentHash;
	}
	public void setDocumentHash(String documentHash) {
		this.documentHash = documentHash;
	}
	
	public boolean isDocumentNormalitzat() {
		return documentNormalitzat;
	}
	public void setDocumentNormalitzat(boolean documentNormalitzat) {
		this.documentNormalitzat = documentNormalitzat;
	}
	
	public boolean isDocumentGenerarCsv() {
		return documentGenerarCsv;
	}
	public void setDocumentGenerarCsv(boolean documentGenerarCsv) {
		this.documentGenerarCsv = documentGenerarCsv;
	}
	
	public List<Long> getAnnexos() {
		return annexos;
	}
	public void setAnnexos(List<Long> annexos) {
		this.annexos = annexos;
	}
	
	
	public static DocumentNotificacioCommand asCommand(DocumentNotificacioDto dto) {
		return ConversioTipusHelper.convertir(
				dto,
				DocumentNotificacioCommand.class);
	}
	public static DocumentNotificacioDto asDto(DocumentNotificacioCommand command) {
		DocumentNotificacioDto dto = ConversioTipusHelper.convertir(
				command,
				DocumentNotificacioDto.class);
		dto.setSeuIdioma(InteressatIdiomaEnumDto.valueOf(command.seuIdioma));
		if (command.getAnnexos() != null) {
			List<DocumentDto> annexos = new ArrayList<DocumentDto>();
			for (Long annexId: command.getAnnexos()) {
				DocumentDto annex = new DocumentDto();
				annex.setId(annexId);
				annexos.add(annex);
			}
			dto.setAnnexos(annexos);
		}
		return dto;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	public interface Electronica {}
	public interface Create {}
	public interface Update {}

}
