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
 * <p>Classe Java pour arret complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="arret">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="idArret" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="nomArret" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "arret", propOrder = {
    "idArret",
    "nomArret"
})
public class Arret {

    protected int idArret;
    @XmlElement(required = true)
    protected String nomArret;

    /**
     * Obtient la valeur de la propriété idArret.
     * 
     */
    public int getIdArret() {
        return idArret;
    }

    /**
     * Définit la valeur de la propriété idArret.
     * 
     */
    public void setIdArret(int value) {
        this.idArret = value;
    }

    /**
     * Obtient la valeur de la propriété nomArret.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNomArret() {
        return nomArret;
    }

    /**
     * Définit la valeur de la propriété nomArret.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNomArret(String value) {
        this.nomArret = value;
    }

}
