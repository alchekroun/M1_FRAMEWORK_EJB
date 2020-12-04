//
// Ce fichier a été généré par l'implémentation de référence JavaTM Architecture for XML Binding (JAXB), v2.2.8-b130911.1802 
// Voir <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Toute modification apportée à ce fichier sera perdue lors de la recompilation du schéma source. 
// Généré le : 2020.12.04 à 08:47:24 AM CET 
//


package generated;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java pour train complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="train">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="idTrain" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="nomTrain" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="direction" type="{}arret"/>
 *         &lt;element name="direction_type" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="numeroTrain" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="reseau" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="statut" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="base_depart_temps" type="{}SimpleDate"/>
 *         &lt;element name="base_arrivee_temps" type="{}SimpleDate"/>
 *         &lt;element name="reel_depart_temps" type="{}SimpleDate"/>
 *         &lt;element name="reel_arrivee_temps" type="{}SimpleDate"/>
 *         &lt;element name="listeArrets" type="{}arret" maxOccurs="unbounded"/>
 *         &lt;element name="listePassager" type="{}passager" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "train", propOrder = {
    "idTrain",
    "nomTrain",
    "direction",
    "directionType",
    "numeroTrain",
    "reseau",
    "statut",
    "baseDepartTemps",
    "baseArriveeTemps",
    "reelDepartTemps",
    "reelArriveeTemps",
    "listeArrets",
    "listePassager"
})
@XmlSeeAlso({
    TrainAvecResa.class,
    TrainSansResa.class
})
public class Train {

    protected int idTrain;
    @XmlElement(required = true)
    protected String nomTrain;
    @XmlElement(required = true)
    protected Arret direction;
    @XmlElement(name = "direction_type", required = true)
    protected String directionType;
    protected int numeroTrain;
    @XmlElement(required = true)
    protected String reseau;
    @XmlElement(required = true)
    protected String statut;
    @XmlElement(name = "base_depart_temps", required = true)
    protected String baseDepartTemps;
    @XmlElement(name = "base_arrivee_temps", required = true)
    protected String baseArriveeTemps;
    @XmlElement(name = "reel_depart_temps", required = true)
    protected String reelDepartTemps;
    @XmlElement(name = "reel_arrivee_temps", required = true)
    protected String reelArriveeTemps;
    @XmlElement(required = true)
    protected List<Arret> listeArrets;
    @XmlElement(required = true)
    protected List<Passager> listePassager;

    /**
     * Obtient la valeur de la propriété idTrain.
     * 
     */
    public int getIdTrain() {
        return idTrain;
    }

    /**
     * Définit la valeur de la propriété idTrain.
     * 
     */
    public void setIdTrain(int value) {
        this.idTrain = value;
    }

    /**
     * Obtient la valeur de la propriété nomTrain.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNomTrain() {
        return nomTrain;
    }

    /**
     * Définit la valeur de la propriété nomTrain.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNomTrain(String value) {
        this.nomTrain = value;
    }

    /**
     * Obtient la valeur de la propriété direction.
     * 
     * @return
     *     possible object is
     *     {@link Arret }
     *     
     */
    public Arret getDirection() {
        return direction;
    }

    /**
     * Définit la valeur de la propriété direction.
     * 
     * @param value
     *     allowed object is
     *     {@link Arret }
     *     
     */
    public void setDirection(Arret value) {
        this.direction = value;
    }

    /**
     * Obtient la valeur de la propriété directionType.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDirectionType() {
        return directionType;
    }

    /**
     * Définit la valeur de la propriété directionType.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDirectionType(String value) {
        this.directionType = value;
    }

    /**
     * Obtient la valeur de la propriété numeroTrain.
     * 
     */
    public int getNumeroTrain() {
        return numeroTrain;
    }

    /**
     * Définit la valeur de la propriété numeroTrain.
     * 
     */
    public void setNumeroTrain(int value) {
        this.numeroTrain = value;
    }

    /**
     * Obtient la valeur de la propriété reseau.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReseau() {
        return reseau;
    }

    /**
     * Définit la valeur de la propriété reseau.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReseau(String value) {
        this.reseau = value;
    }

    /**
     * Obtient la valeur de la propriété statut.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStatut() {
        return statut;
    }

    /**
     * Définit la valeur de la propriété statut.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStatut(String value) {
        this.statut = value;
    }

    /**
     * Obtient la valeur de la propriété baseDepartTemps.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBaseDepartTemps() {
        return baseDepartTemps;
    }

    /**
     * Définit la valeur de la propriété baseDepartTemps.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBaseDepartTemps(String value) {
        this.baseDepartTemps = value;
    }

    /**
     * Obtient la valeur de la propriété baseArriveeTemps.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBaseArriveeTemps() {
        return baseArriveeTemps;
    }

    /**
     * Définit la valeur de la propriété baseArriveeTemps.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBaseArriveeTemps(String value) {
        this.baseArriveeTemps = value;
    }

    /**
     * Obtient la valeur de la propriété reelDepartTemps.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReelDepartTemps() {
        return reelDepartTemps;
    }

    /**
     * Définit la valeur de la propriété reelDepartTemps.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReelDepartTemps(String value) {
        this.reelDepartTemps = value;
    }

    /**
     * Obtient la valeur de la propriété reelArriveeTemps.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReelArriveeTemps() {
        return reelArriveeTemps;
    }

    /**
     * Définit la valeur de la propriété reelArriveeTemps.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReelArriveeTemps(String value) {
        this.reelArriveeTemps = value;
    }

    /**
     * Gets the value of the listeArrets property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the listeArrets property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getListeArrets().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Arret }
     * 
     * 
     */
    public List<Arret> getListeArrets() {
        if (listeArrets == null) {
            listeArrets = new ArrayList<Arret>();
        }
        return this.listeArrets;
    }

    /**
     * Gets the value of the listePassager property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the listePassager property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getListePassager().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Passager }
     * 
     * 
     */
    public List<Passager> getListePassager() {
        if (listePassager == null) {
            listePassager = new ArrayList<Passager>();
        }
        return this.listePassager;
    }

}
