/**
 * CriteriaEnum.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.indra.www.portafirmasws.cws;

public class CriteriaEnum implements java.io.Serializable {
    private java.lang.String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected CriteriaEnum(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final java.lang.String _state = "state";
    public static final java.lang.String _importance = "importance";
    public static final java.lang.String _dateentry = "dateentry";
    public static final java.lang.String _searchtype = "searchtype";
    public static final java.lang.String _usercode = "usercode";
    public static final java.lang.String _applicationname = "applicationname";
    public static final CriteriaEnum state = new CriteriaEnum(_state);
    public static final CriteriaEnum importance = new CriteriaEnum(_importance);
    public static final CriteriaEnum dateentry = new CriteriaEnum(_dateentry);
    public static final CriteriaEnum searchtype = new CriteriaEnum(_searchtype);
    public static final CriteriaEnum usercode = new CriteriaEnum(_usercode);
    public static final CriteriaEnum applicationname = new CriteriaEnum(_applicationname);
    public java.lang.String getValue() { return _value_;}
    public static CriteriaEnum fromValue(java.lang.String value)
          throws java.lang.IllegalArgumentException {
        CriteriaEnum enumeration = (CriteriaEnum)
            _table_.get(value);
        if (enumeration==null) throw new java.lang.IllegalArgumentException();
        return enumeration;
    }
    public static CriteriaEnum fromString(java.lang.String value)
          throws java.lang.IllegalArgumentException {
        return fromValue(value);
    }
    public boolean equals(java.lang.Object obj) {return (obj == this);}
    public int hashCode() { return toString().hashCode();}
    public java.lang.String toString() { return _value_;}
    public java.lang.Object readResolve() throws java.io.ObjectStreamException { return fromValue(_value_);}
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new org.apache.axis.encoding.ser.EnumSerializer(
            _javaType, _xmlType);
    }
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new org.apache.axis.encoding.ser.EnumDeserializer(
            _javaType, _xmlType);
    }
    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(CriteriaEnum.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.indra.es/portafirmasws/cws", "CriteriaEnum"));
    }
    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

}
