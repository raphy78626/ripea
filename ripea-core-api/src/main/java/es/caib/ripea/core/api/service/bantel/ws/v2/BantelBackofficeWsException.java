
package es.caib.ripea.core.api.service.bantel.ws.v2;

import javax.xml.ws.WebFault;


/**
 * This class was generated by Apache CXF 2.2.7
 * Mon Apr 30 17:35:22 CEST 2012
 * Generated source version: 2.2.7
 * 
 */

@WebFault(name = "fault", targetNamespace = "urn:es:caib:bantel:ws:v2:model:BackofficeFacade")
public class BantelBackofficeWsException extends Exception {
    public static final long serialVersionUID = 20120430173522L;
    
    private es.caib.ripea.core.api.service.bantel.ws.v2.model.BackofficeFacadeException fault;

    public BantelBackofficeWsException() {
        super();
    }
    
    public BantelBackofficeWsException(String message) {
        super(message);
    }
    
    public BantelBackofficeWsException(String message, Throwable cause) {
        super(message, cause);
    }

    public BantelBackofficeWsException(String message, es.caib.ripea.core.api.service.bantel.ws.v2.model.BackofficeFacadeException fault) {
        super(message);
        this.fault = fault;
    }

    public BantelBackofficeWsException(String message, es.caib.ripea.core.api.service.bantel.ws.v2.model.BackofficeFacadeException fault, Throwable cause) {
        super(message, cause);
        this.fault = fault;
    }

    public es.caib.ripea.core.api.service.bantel.ws.v2.model.BackofficeFacadeException getFaultInfo() {
        return this.fault;
    }
}