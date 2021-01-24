package fr.pantheonsorbonne.ufr27.miage.jpa;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

@Entity

@NamedQueries({ @NamedQuery(name = "getAllArret", query = "SELECT a FROM Arret a ORDER BY a.id ASC"),
		@NamedQuery(name = "findArretByTrain", query = "SELECT a FROM Arret a  JOIN a.listeHeureDePassage h WHERE h.train.id = :id") })
public class Arret {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	protected int id;

	protected String nom;

	// ce sont les heures de passages des trains qui ont cet arret sur leur parcours
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "arret")
	protected List<HeureDePassage> listeHeureDePassage;

	boolean isCreated;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public List<HeureDePassage> getListeHeureDePassage() {
		return listeHeureDePassage;
	}

	public void setListeHeureDePassage(List<HeureDePassage> listeHeureDePassage) {
		this.listeHeureDePassage = listeHeureDePassage;
	}

	/**
	 * Méthode permettant d'ajouter une heure de passage à un arrêt
	 * 
	 * @param hdp
	 */
	public void addArretHeureDePassage(HeureDePassage hdp) {
		this.listeHeureDePassage.add(hdp);
	}

	/**
	 * Méthode permettant de supprimer une heure de passage à un arrêt
	 * 
	 * @param hdp
	 */
	public void removeArretHeureDePassage(HeureDePassage hdp) {
		this.listeHeureDePassage.remove(hdp);
	}

	public boolean isCreated() {
		return isCreated;
	}

	public void setCreated(boolean isCreated) {
		this.isCreated = isCreated;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Arret) {
			Arret a = (Arret) obj;
			if (this.id == a.getId() && this.nom.equals(a.getNom())) {
				return true;
			}
			return false;
		}
		return false;
	}

}
