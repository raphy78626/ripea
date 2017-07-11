
package es.caib.bantel.ws.v2.model.firmaws;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para FirmasWS complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="FirmasWS">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="firmas" type="{urn:es:caib:bantel:ws:v2:model:FirmaWS}FirmaWS" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FirmasWS", propOrder = {
    "firmas"
})
public class FirmasWS {

    protected List<FirmaWS> firmas;

    /**
     * Gets the value of the firmas property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the firmas property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFirmas().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link FirmaWS }
     * 
     * 
     */
    public List<FirmaWS> getFirmas() {
        if (firmas == null) {
            firmas = new ArrayList<FirmaWS>();
        }
        return this.firmas;
    }

}
