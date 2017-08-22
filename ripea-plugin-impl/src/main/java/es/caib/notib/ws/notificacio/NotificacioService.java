
package es.caib.notib.ws.notificacio;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceFeature;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.9-b14002
 * Generated source version: 2.2
 * 
 */
@WebServiceClient(name = "NotificacioService", targetNamespace = "http://www.caib.es/notib/ws/notificacio", wsdlLocation = "file:/C:/Users/PereP/git/ripea/ripea-plugin-impl/wsdl/notib-notificacio/notificacio.wsdl")
public class NotificacioService
    extends Service
{

    private final static URL NOTIFICACIOSERVICE_WSDL_LOCATION;
    private final static WebServiceException NOTIFICACIOSERVICE_EXCEPTION;
    private final static QName NOTIFICACIOSERVICE_QNAME = new QName("http://www.caib.es/notib/ws/notificacio", "NotificacioService");

    static {
        URL url = null;
        WebServiceException e = null;
        try {
            url = new URL("file:/C:/Users/PereP/git/ripea/ripea-plugin-impl/wsdl/notib-notificacio/notificacio.wsdl");
        } catch (MalformedURLException ex) {
            e = new WebServiceException(ex);
        }
        NOTIFICACIOSERVICE_WSDL_LOCATION = url;
        NOTIFICACIOSERVICE_EXCEPTION = e;
    }

    public NotificacioService() {
        super(__getWsdlLocation(), NOTIFICACIOSERVICE_QNAME);
    }

    public NotificacioService(WebServiceFeature... features) {
        super(__getWsdlLocation(), NOTIFICACIOSERVICE_QNAME, features);
    }

    public NotificacioService(URL wsdlLocation) {
        super(wsdlLocation, NOTIFICACIOSERVICE_QNAME);
    }

    public NotificacioService(URL wsdlLocation, WebServiceFeature... features) {
        super(wsdlLocation, NOTIFICACIOSERVICE_QNAME, features);
    }

    public NotificacioService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public NotificacioService(URL wsdlLocation, QName serviceName, WebServiceFeature... features) {
        super(wsdlLocation, serviceName, features);
    }

    /**
     * 
     * @return
     *     returns Notificacio
     */
    @WebEndpoint(name = "NotificacioServicePort")
    public Notificacio getNotificacioServicePort() {
        return super.getPort(new QName("http://www.caib.es/notib/ws/notificacio", "NotificacioServicePort"), Notificacio.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns Notificacio
     */
    @WebEndpoint(name = "NotificacioServicePort")
    public Notificacio getNotificacioServicePort(WebServiceFeature... features) {
        return super.getPort(new QName("http://www.caib.es/notib/ws/notificacio", "NotificacioServicePort"), Notificacio.class, features);
    }

    private static URL __getWsdlLocation() {
        if (NOTIFICACIOSERVICE_EXCEPTION!= null) {
            throw NOTIFICACIOSERVICE_EXCEPTION;
        }
        return NOTIFICACIOSERVICE_WSDL_LOCATION;
    }

}
