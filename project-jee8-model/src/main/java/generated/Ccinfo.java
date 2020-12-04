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
 * <p>Classe Java pour ccinfo complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="ccinfo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="number" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="validityDate" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ccv" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ccinfo", propOrder = {
    "number",
    "validityDate",
    "ccv"
})
public class Ccinfo {

    @XmlElement(required = true)
    protected String number;
    @XmlElement(required = true)
    protected String validityDate;
    protected int ccv;

    /**
     * Obtient la valeur de la propriété number.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumber() {
        return number;
    }

    /**
     * Définit la valeur de la propriété number.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumber(String value) {
        this.number = value;
    }

    /**
     * Obtient la valeur de la propriété validityDate.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getValidityDate() {
        return validityDate;
    }

    /**
     * Définit la valeur de la propriété validityDate.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setValidityDate(String value) {
        this.validityDate = value;
    }

    /**
     * Obtient la valeur de la propriété ccv.
     * 
     */
    public int getCcv() {
        return ccv;
    }

    /**
     * Définit la valeur de la propriété ccv.
     * 
     */
    public void setCcv(int value) {
        this.ccv = value;
    }

}
