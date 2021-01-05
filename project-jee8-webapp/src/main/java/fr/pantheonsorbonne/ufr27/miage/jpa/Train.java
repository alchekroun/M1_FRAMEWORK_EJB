package fr.pantheonsorbonne.ufr27.miage.jpa;

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

@Entity
@NamedQueries({ @NamedQuery(name = "getAllTrain", query = "SELECT t FROM Train t"),
		@NamedQuery(name = "findTrainByArret", query = "SELECT t FROM Train t LEFT JOIN t.listeHeureDePassage h WHERE h.arret.id = :arretId"),
		@NamedQuery(name = "findTrainByHdp", query = "SELECT t FROM Train t LEFT JOIN t.listeHeureDePassage h WHERE h.id = :hdp") })
public class Train {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int id;

	protected String nom;

	protected String directionType;

	protected int numero;

	protected String reseau;

	protected String statut;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "train")
	protected List<HeureDePassage> listeHeureDePassage;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "train")
	protected List<Passager> listePassagers;

	boolean isCreated;

	public String getDirectionType() {
		return directionType;
	}

	public void setDirectionType(String directionType) {
		this.directionType = directionType;
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

	public void removeArretHeureDePassage(HeureDePassage hdp) {
		this.listeHeureDePassage.remove(hdp);
	}

	public List<Passager> getListePassagers() {
		return listePassagers;
	}

	public void setListePassagers(List<Passager> listePassagers) {
		this.listePassagers = listePassagers;
	}

	public void addPassager(Passager p) {
		this.listePassagers.add(p);
	}

	public void removePassager(Passager p) {
		this.listePassagers.remove(p);
	}

	public boolean isCreated() {
		return isCreated;
	}

	public void setCreated(boolean isCreated) {
		this.isCreated = isCreated;
	}

}