//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.01.22 at 04:05:05 PM EST 
//


package org.hl7.greencda.c32;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.hl7.greencda.c32 package. 
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

    private final static QName _VitalSign_QNAME = new QName("urn:hl7-org:greencda:c32", "vitalSign");
    private final static QName _AddressStreet_QNAME = new QName("urn:hl7-org:greencda:c32", "street");
    private final static QName _AddressPostalCode_QNAME = new QName("urn:hl7-org:greencda:c32", "postalCode");
    private final static QName _AddressCity_QNAME = new QName("urn:hl7-org:greencda:c32", "city");
    private final static QName _AddressState_QNAME = new QName("urn:hl7-org:greencda:c32", "state");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.hl7.greencda.c32
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Telecom }
     * 
     */
    public Telecom createTelecom() {
        return new Telecom();
    }

    /**
     * Create an instance of {@link Code }
     * 
     */
    public Code createCode() {
        return new Code();
    }

    /**
     * Create an instance of {@link Organization }
     * 
     */
    public Organization createOrganization() {
        return new Organization();
    }

    /**
     * Create an instance of {@link Interval }
     * 
     */
    public Interval createInterval() {
        return new Interval();
    }

    /**
     * Create an instance of {@link Address }
     * 
     */
    public Address createAddress() {
        return new Address();
    }

    /**
     * Create an instance of {@link Provider }
     * 
     */
    public Provider createProvider() {
        return new Provider();
    }

    /**
     * Create an instance of {@link Entry }
     * 
     */
    public Entry createEntry() {
        return new Entry();
    }

    /**
     * Create an instance of {@link SimpleCode }
     * 
     */
    public SimpleCode createSimpleCode() {
        return new SimpleCode();
    }

    /**
     * Create an instance of {@link Person }
     * 
     */
    public Person createPerson() {
        return new Person();
    }

    /**
     * Create an instance of {@link Result }
     * 
     */
    public Result createResult() {
        return new Result();
    }

    /**
     * Create an instance of {@link Timestamp }
     * 
     */
    public Timestamp createTimestamp() {
        return new Timestamp();
    }

    /**
     * Create an instance of {@link PersonalName }
     * 
     */
    public PersonalName createPersonalName() {
        return new PersonalName();
    }

    /**
     * Create an instance of {@link Quantity }
     * 
     */
    public Quantity createQuantity() {
        return new Quantity();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Result }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:hl7-org:greencda:c32", name = "vitalSign")
    public JAXBElement<Result> createVitalSign(Result value) {
        return new JAXBElement<Result>(_VitalSign_QNAME, Result.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Object }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:hl7-org:greencda:c32", name = "street", scope = Address.class)
    public JAXBElement<Object> createAddressStreet(Object value) {
        return new JAXBElement<Object>(_AddressStreet_QNAME, Object.class, Address.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Object }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:hl7-org:greencda:c32", name = "postalCode", scope = Address.class)
    public JAXBElement<Object> createAddressPostalCode(Object value) {
        return new JAXBElement<Object>(_AddressPostalCode_QNAME, Object.class, Address.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Object }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:hl7-org:greencda:c32", name = "city", scope = Address.class)
    public JAXBElement<Object> createAddressCity(Object value) {
        return new JAXBElement<Object>(_AddressCity_QNAME, Object.class, Address.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Object }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:hl7-org:greencda:c32", name = "state", scope = Address.class)
    public JAXBElement<Object> createAddressState(Object value) {
        return new JAXBElement<Object>(_AddressState_QNAME, Object.class, Address.class, value);
    }

}