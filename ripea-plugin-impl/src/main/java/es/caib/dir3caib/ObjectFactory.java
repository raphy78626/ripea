
package es.caib.dir3caib;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the es.caib.dir3caib package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _ObtenerArbolUnidadesResponse_QNAME = new QName("http://www.caib.es/dir3caib", "obtenerArbolUnidadesResponse");
    private final static QName _ObtenerUnidadResponse_QNAME = new QName("http://www.caib.es/dir3caib", "obtenerUnidadResponse");
    private final static QName _ObtenerUnidad_QNAME = new QName("http://www.caib.es/dir3caib", "obtenerUnidad");
    private final static QName _ObtenerArbolUnidades_QNAME = new QName("http://www.caib.es/dir3caib", "obtenerArbolUnidades");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: es.caib.dir3caib
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ObtenerUnidadResponse }
     * 
     */
    public ObtenerUnidadResponse createObtenerUnidadResponse() {
        return new ObtenerUnidadResponse();
    }

    /**
     * Create an instance of {@link UnidadTF }
     * 
     */
    public UnidadTF createUnidadTF() {
        return new UnidadTF();
    }

    /**
     * Create an instance of {@link ObtenerUnidad }
     * 
     */
    public ObtenerUnidad createObtenerUnidad() {
        return new ObtenerUnidad();
    }

    /**
     * Create an instance of {@link ObtenerArbolUnidades }
     * 
     */
    public ObtenerArbolUnidades createObtenerArbolUnidades() {
        return new ObtenerArbolUnidades();
    }

    /**
     * Create an instance of {@link ObtenerArbolUnidadesResponse }
     * 
     */
    public ObtenerArbolUnidadesResponse createObtenerArbolUnidadesResponse() {
        return new ObtenerArbolUnidadesResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ObtenerArbolUnidadesResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.caib.es/dir3caib", name = "obtenerArbolUnidadesResponse")
    public JAXBElement<ObtenerArbolUnidadesResponse> createObtenerArbolUnidadesResponse(ObtenerArbolUnidadesResponse value) {
        return new JAXBElement<ObtenerArbolUnidadesResponse>(_ObtenerArbolUnidadesResponse_QNAME, ObtenerArbolUnidadesResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ObtenerUnidadResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.caib.es/dir3caib", name = "obtenerUnidadResponse")
    public JAXBElement<ObtenerUnidadResponse> createObtenerUnidadResponse(ObtenerUnidadResponse value) {
        return new JAXBElement<ObtenerUnidadResponse>(_ObtenerUnidadResponse_QNAME, ObtenerUnidadResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ObtenerUnidad }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.caib.es/dir3caib", name = "obtenerUnidad")
    public JAXBElement<ObtenerUnidad> createObtenerUnidad(ObtenerUnidad value) {
        return new JAXBElement<ObtenerUnidad>(_ObtenerUnidad_QNAME, ObtenerUnidad.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ObtenerArbolUnidades }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.caib.es/dir3caib", name = "obtenerArbolUnidades")
    public JAXBElement<ObtenerArbolUnidades> createObtenerArbolUnidades(ObtenerArbolUnidades value) {
        return new JAXBElement<ObtenerArbolUnidades>(_ObtenerArbolUnidades_QNAME, ObtenerArbolUnidades.class, null, value);
    }

}
