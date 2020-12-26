package fr.pantheonsorbonne.ufr27.miage.jpa;

import java.util.List;
import java.util.Set;

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
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;

@Entity

@NamedQueries({ @NamedQuery(name = "getAllArret", query = "SELECT a FROM Arret a"),
	@NamedQuery(name = "findAllArretByTrain", query = "SELECT a FROM Arret a LEFT JOIN a.listeHeureDePassage h WHERE h.train.id = :id")})
public class Arret {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	protected int id;

	protected String nom;

	// liste des trains qui ont pour terminus cet arret
	@OneToMany(mappedBy = "direction", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Set<Train> trainsArrivants;

	// ce sont les heures de passages des trains qui ont cet arret sur leur parcours
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "arret")
	protected List<HeureDePassage> listeHeureDePassage;

	@OneToOne(mappedBy = "localisation", cascade = CascadeType.ALL)
	@PrimaryKeyJoinColumn
	protected InfoGare infoGare;

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

	public void addArretHeureDePassage(HeureDePassage hdp) {
		this.listeHeureDePassage.add(hdp);
	}

	public void removeArretHeureDePassage(HeureDePassage hdp) {
		this.listeHeureDePassage.remove(hdp);
	}

	public boolean isCreated() {
		return isCreated;
	}

	public void setCreated(boolean isCreated) {
		this.isCreated = isCreated;
	}

}
