//
// Ce fichier a été généré par l'implémentation de référence JavaTM Architecture for XML Binding (JAXB), v2.2.8-b130911.1802 
// Voir <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Toute modification apportée à ce fichier sera perdue lors de la recompilation du schéma source. 
// Généré le : 2020.12.04 à 08:47:24 AM CET 
//


package generated;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the generated package. 
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

    private final static QName _Address_QNAME = new QName("", "address");
    private final static QName _TrainAvecResa_QNAME = new QName("", "trainAvecResa");
    private final static QName _InvoiceWrapper_QNAME = new QName("", "invoiceWrapper");
    private final static QName _Ccinfo_QNAME = new QName("", "ccinfo");
    private final static QName _Invoice_QNAME = new QName("", "invoice");
    private final static QName _TrainSansResa_QNAME = new QName("", "trainSansResa");
    private final static QName _Passager_QNAME = new QName("", "passager");
    private final static QName _Arret_QNAME = new QName("", "arret");
    private final static QName _User_QNAME = new QName("", "user");
    private final static QName _Train_QNAME = new QName("", "train");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: generated
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Address }
     * 
     */
    public Address createAddress() {
        return new Address();
    }

    /**
     * Create an instance of {@link TrainAvecResa }
     * 
     */
    public TrainAvecResa createTrainAvecResa() {
        return new TrainAvecResa();
    }

    /**
     * Create an instance of {@link FreeTrialPlan }
     * 
     */
    public FreeTrialPlan createFreeTrialPlan() {
        return new FreeTrialPlan();
    }

    /**
     * Create an instance of {@link User }
     * 
     */
    public User createUser() {
        return new User();
    }

    /**
     * Create an instance of {@link InvoiceWrapper }
     * 
     */
    public InvoiceWrapper createInvoiceWrapper() {
        return new InvoiceWrapper();
    }

    /**
     * Create an instance of {@link Ccinfo }
     * 
     */
    public Ccinfo createCcinfo() {
        return new Ccinfo();
    }

    /**
     * Create an instance of {@link Invoice }
     * 
     */
    public Invoice createInvoice() {
        return new Invoice();
    }

    /**
     * Create an instance of {@link TrainSansResa }
     * 
     */
    public TrainSansResa createTrainSansResa() {
        return new TrainSansResa();
    }

    /**
     * Create an instance of {@link Passager }
     * 
     */
    public Passager createPassager() {
        return new Passager();
    }

    /**
     * Create an instance of {@link Arret }
     * 
     */
    public Arret createArret() {
        return new Arret();
    }

    /**
     * Create an instance of {@link Train }
     * 
     */
    public Train createTrain() {
        return new Train();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Address }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "address")
    public JAXBElement<Address> createAddress(Address value) {
        return new JAXBElement<Address>(_Address_QNAME, Address.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TrainAvecResa }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "trainAvecResa")
    public JAXBElement<TrainAvecResa> createTrainAvecResa(TrainAvecResa value) {
        return new JAXBElement<TrainAvecResa>(_TrainAvecResa_QNAME, TrainAvecResa.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link InvoiceWrapper }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "invoiceWrapper")
    public JAXBElement<InvoiceWrapper> createInvoiceWrapper(InvoiceWrapper value) {
        return new JAXBElement<InvoiceWrapper>(_InvoiceWrapper_QNAME, InvoiceWrapper.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Ccinfo }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "ccinfo")
    public JAXBElement<Ccinfo> createCcinfo(Ccinfo value) {
        return new JAXBElement<Ccinfo>(_Ccinfo_QNAME, Ccinfo.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Invoice }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "invoice")
    public JAXBElement<Invoice> createInvoice(Invoice value) {
        return new JAXBElement<Invoice>(_Invoice_QNAME, Invoice.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TrainSansResa }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "trainSansResa")
    public JAXBElement<TrainSansResa> createTrainSansResa(TrainSansResa value) {
        return new JAXBElement<TrainSansResa>(_TrainSansResa_QNAME, TrainSansResa.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Passager }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "passager")
    public JAXBElement<Passager> createPassager(Passager value) {
        return new JAXBElement<Passager>(_Passager_QNAME, Passager.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Arret }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "arret")
    public JAXBElement<Arret> createArret(Arret value) {
        return new JAXBElement<Arret>(_Arret_QNAME, Arret.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "user")
    public JAXBElement<User> createUser(User value) {
        return new JAXBElement<User>(_User_QNAME, User.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Train }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "train")
    public JAXBElement<Train> createTrain(Train value) {
        return new JAXBElement<Train>(_Train_QNAME, Train.class, null, value);
    }

}
