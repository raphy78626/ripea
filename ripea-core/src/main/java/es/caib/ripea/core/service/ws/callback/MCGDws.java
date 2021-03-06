
package es.caib.ripea.core.service.ws.callback;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.1.6 in JDK 6
 * Generated source version: 2.1
 * 
 */
@WebService(name = "MCGDws", targetNamespace = "http://www.indra.es/portafirmasmcgdws/mcgdws")
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
@XmlSeeAlso({
    ObjectFactory.class
})
public interface MCGDws {


    /**
     * 
     * @param callbackRequest
     * @return
     *     returns es.caib.portafib.indra.callback.server.CallbackResponse
     */
    @WebMethod(operationName = "Callback", action = "Callback")
    @WebResult(name = "callback-response", targetNamespace = "http://www.indra.es/portafirmasmcgdws/mcgdws", partName = "callback-response")
    public CallbackResponse callback(
        @WebParam(name = "callback-request", targetNamespace = "http://www.indra.es/portafirmasmcgdws/mcgdws", partName = "callback-request")
        CallbackRequest callbackRequest);

}
