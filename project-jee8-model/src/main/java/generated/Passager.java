//
// Ce fichier a été généré par l'implémentation de référence JavaTM Architecture for XML Binding (JAXB), v2.2.8-b130911.1802 
// Voir <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Toute modification apportée à ce fichier sera perdue lors de la recompilation du schéma source. 
// Généré le : 2020.12.04 à 08:47:24 AM CET 
//


package generated;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java pour passager complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="passager">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="idPassager" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="nomPassager" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="depart" type="{}arret"/>
 *         &lt;element name="arrive" type="{}arret"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "passager", propOrder = {
    "idPassager",
    "nomPassager",
    "depart",
    "arrive"
})
public class Passager {

    protected int idPassager;
    @XmlElement(required = true)
    protected String nomPassager;
    @XmlElement(required = true)
    protected Arret depart;
    @XmlElement(required = true)
    protected Arret arrive;

    /**
     * Obtient la valeur de la propriété idPassager.
     * 
     */
    public int getIdPassager() {
        return idPassager;
    }

    /**
     * Définit la valeur de la propriété idPassager.
     * 
     */
    public void setIdPassager(int value) {
        this.idPassager = value;
    }

    /**
     * Obtient la valeur de la propriété nomPassager.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNomPassager() {
        return nomPassager;
    }

    /**
     * Définit la valeur de la propriété nomPassager.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNomPassager(String value) {
        this.nomPassager = value;
    }

    /**
     * Obtient la valeur de la propriété depart.
     * 
     * @return
     *     possible object is
     *     {@link Arret }
     *     
     */
    public Arret getDepart() {
        return depart;
    }

    /**
     * Définit la valeur de la propriété depart.
     * 
     * @param value
     *     allowed object is
     *     {@link Arret }
     *     
     */
    public void setDepart(Arret value) {
        this.depart = value;
    }

    /**
     * Obtient la valeur de la propriété arrive.
     * 
     * @return
     *     possible object is
     *     {@link Arret }
     *     
     */
    public Arret getArrive() {
        return arrive;
    }

    /**
     * Définit la valeur de la propriété arrive.
     * 
     * @param value
     *     allowed object is
     *     {@link Arret }
     *     
     */
    public void setArrive(Arret value) {
        this.arrive = value;
    }

}
