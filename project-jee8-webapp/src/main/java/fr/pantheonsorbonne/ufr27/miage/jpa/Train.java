package fr.pantheonsorbonne.ufr27.miage.jpa;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@NamedQueries({ @NamedQuery(name = "getAllTrain", query = "SELECT t FROM Train t"),
		@NamedQuery(name = "findTrainByArret", query = "SELECT t FROM Train t LEFT JOIN t.listeHeureDePassage h WHERE h.arret.id = :arretId"),
		@NamedQuery(name = "findTrainByHdp", query = "SELECT t FROM Train t LEFT JOIN t.listeHeureDePassage h WHERE h.id = :hdp"),
		@NamedQuery(name = "findTrainByArretAndDepartAfterDate", query = "SELECT t FROM Train t LEFT JOIN t.listeHeureDePassage h WHERE h.arret.id = :arretId AND h.reelDepartTemps > :temps"),
		@NamedQuery(name = "findTrainByArretAndDepartAfterDateAndDesservi", query = "SELECT t FROM Train t LEFT JOIN t.listeHeureDePassage h WHERE h.arret.id = :arretId AND h.reelDepartTemps > :temps AND h.desservi = true"),
		@NamedQuery(name = "findTrainByArretAndArriveeBeforeDate", query = "SELECT t FROM Train t LEFT JOIN t.listeHeureDePassage h WHERE h.arret.id = :arretId AND h.reelArriveeTemps < :temps ORDER BY h.reelArriveeTemps ASC")})
		//@NamedQuery(name="findNombrePassagerByTrain", query= "SELECT SIZE(t.listePassagers) FROM Train t WHERE t.id= :trainId")


public abstract class Train {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int id;

	protected String nom;

	protected int numero;

	protected String reseau;

	protected String statut;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "train")
	protected List<HeureDePassage> listeHeureDePassage;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "train")
	protected List<Passager> listePassagers;

	boolean isCreated;

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

	/**
	 * Méthode permettant d'ajouter une heure de passage à un train
	 * @param hdp
	 */
	public void addArretHeureDePassage(HeureDePassage hdp) {
		this.listeHeureDePassage.add(hdp);
	}

	/**
	 * Méthode permettant de supprimer une heure de passage à un train
	 * @param hdp
	 */
	public void removeArretHeureDePassage(HeureDePassage hdp) {
		this.listeHeureDePassage.remove(hdp);
	}

	public List<Passager> getListePassagers() {
		return listePassagers;
	}

	public void setListePassagers(List<Passager> listePassagers) {
		this.listePassagers = listePassagers;
	}

	/**
	 * Méthode permettant d'ajouter un passager à un train
	 * @param p
	 */
	public void addPassager(Passager p) {
		if (this.listePassagers == null) {
			this.listePassagers = new ArrayList<Passager>();
		}
		this.listePassagers.add(p);
	}

	/**
	 * Méthode permettant de supprimer un passager à un train
	 * @param p
	 */
	public void removePassager(Passager p) {
		this.listePassagers.remove(p);
	}

	public boolean isCreated() {
		return isCreated;
	}

	public void setCreated(boolean isCreated) {
		this.isCreated = isCreated;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Train) {
			Train t = (Train) obj;
			if (this.id == t.getId() && this.nom.equals(t.getNom()) && this.numero == t.getNumero()
					&& this.reseau.equals(t.getReseau()) && this.statut.equals(t.getStatut())) {
				return true;
			}
			return false;
		}
		return false;
	}

}