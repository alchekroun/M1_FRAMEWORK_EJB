package fr.pantheonsorbonne.ufr27.miage.jpa;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@Entity
@NamedQueries({ @NamedQuery(name = "getAllPassager", query = "SELECT p FROM Passager p"),
		@NamedQuery(name = "deletePassager", query = "DELETE FROM Passager p WHERE p.id = :id") })
public class Passager {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	int id;

	protected String nom;
	@ManyToOne
	protected Arret depart;
	@ManyToOne
	protected Arret arrive;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "train_id")
	private Train train;

	public int getId() {
		return id;
	}

	public void setid(int id) {
		this.id = id;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public Arret getDepart() {
		return depart;
	}

	public void setDepart(Arret depart) {
		this.depart = depart;
	}

	public Arret getArrive() {
		return arrive;
	}

	public void setArrive(Arret arrive) {
		this.arrive = arrive;
	}
}