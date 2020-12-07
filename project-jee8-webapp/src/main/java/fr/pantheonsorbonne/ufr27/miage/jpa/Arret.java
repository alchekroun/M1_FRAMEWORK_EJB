package fr.pantheonsorbonne.ufr27.miage.jpa;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

@Entity

@NamedQueries({ @NamedQuery(name = "getAllArret", query = "SELECT a FROM Arret a"),
		@NamedQuery(name = "deleteArret", query = "DELETE FROM Arret a WHERE a.id = :id") })
public class Arret {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	protected int id;
	protected String nom;
	@OneToMany
	protected List<HeureDePassage> listeHeureDePassage;

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
}
