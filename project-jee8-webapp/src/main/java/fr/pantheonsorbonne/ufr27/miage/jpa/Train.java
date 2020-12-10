package fr.pantheonsorbonne.ufr27.miage.jpa;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@NamedQueries({ @NamedQuery(name = "getAllTrain", query = "SELECT t FROM Train t"),
		@NamedQuery(name = "deleteTrain", query = "DELETE FROM Train t WHERE t.id = :id") })
public class Train {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int id;

	protected String nom;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "direction_arret_id", nullable = false)
	protected Arret direction;

	protected String directionType;

	protected int numero;

	protected String reseau;

	protected String statut;

	@Temporal(TemporalType.TIMESTAMP)
	protected Date baseDepartTemps;

	@Temporal(TemporalType.TIMESTAMP)
	protected Date baseArriveeTemps;

	@Temporal(TemporalType.TIMESTAMP)
	protected Date reelDepartTemps;

	@Temporal(TemporalType.TIMESTAMP)
	protected Date reelArriveeTemps;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "train")
	protected List<HeureDePassage> listeHeureDePassage;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "train")
	protected List<Passager> listePassagers;

	public Date getBaseDepartTemps() {
		return baseDepartTemps;
	}

	public void setBaseDepartTemps(Date baseDepartTemps) {
		this.baseDepartTemps = baseDepartTemps;
	}

	public Date getBaseArriveeTemps() {
		return baseArriveeTemps;
	}

	public void setBaseArriveeTemps(Date baseArriveeTemps) {
		this.baseArriveeTemps = baseArriveeTemps;
	}

	public Date getReelDepartTemps() {
		return reelDepartTemps;
	}

	public void setReelDepartTemps(Date reelDepartTemps) {
		this.reelDepartTemps = reelDepartTemps;
	}

	public Date getReelArriveeTemps() {
		return reelArriveeTemps;
	}

	public void setReelArriveeTemps(Date reelArriveeTemps) {
		this.reelArriveeTemps = reelArriveeTemps;
	}

	public String getNomTrain() {
		return nom;
	}

	public void setNomTrain(String nom) {
		this.nom = nom;
	}

	public String getDirectionType() {
		return directionType;
	}

	public void setDirectionType(String directionType) {
		this.directionType = directionType;
	}

	public int getNumeroTrain() {
		return numero;
	}

	public void setNumeroTrain(int numero) {
		this.numero = numero;
	}

	public String getReseau() {
		return reseau;
	}

	public void setReseau(String reseau) {
		this.reseau = reseau;
	}

	public String getStatut() {
		return statut;
	}

	public void setStatut(String statut) {
		this.statut = statut;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public Arret getDirection() {
		return direction;
	}

	public void setDirection(Arret direction) {
		this.direction = direction;
	}

	public List<Passager> getListePassagers() {
		return listePassagers;
	}

	public void setListePassagers(List<Passager> listePassagers) {
		this.listePassagers = listePassagers;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public int getNumero() {
		return numero;
	}

	public void setNumero(int numero) {
		this.numero = numero;
	}

	public List<HeureDePassage> getListeHeureDePassage() {
		return listeHeureDePassage;
	}

	public void setListeHeureDePassage(List<HeureDePassage> listeHeureDePassage) {
		this.listeHeureDePassage = listeHeureDePassage;
	}

	public void addArretHeureDePassage(HeureDePassage hdp) {
		this.listeHeureDePassage.add(hdp);
	}

}