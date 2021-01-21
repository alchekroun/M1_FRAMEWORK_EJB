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
		@NamedQuery(name = "findAllPassagerByTrain", query = "SELECT p FROM Passager p WHERE p.train.id= :trainId"),
		@NamedQuery(name = "findPassagerByDepart", query = "SELECT p FROM Passager p WHERE p.depart.id= :idArretDepart"),
		@NamedQuery(name = "findPassagerByArrivee", query = "SELECT p FROM Passager p WHERE p.arrive.id= :idArretArrivee"),
		@NamedQuery(name = "findPassagerByCorrespondance", query = "SELECT p FROM Passager p WHERE p.correspondance.id= :arretId"),
		@NamedQuery(name = "getPassagerByTrainIdAndNotArrivalAtArretId", query = "SELECT p FROM Passager p WHERE p.train.id= :trainId AND p.arrive.id<> :idArret AND p.correspondance= :idArret ")})

public class Passager {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int id;

	protected String nom;
	@ManyToOne
	protected Arret depart;
	@ManyToOne
	protected Arret arrive;
	@ManyToOne
	protected Arret correspondance;

	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	@JoinColumn(name = "train_id")
	private Train train;

	boolean isCreated;

	boolean arrived;

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

	public Arret getCorrespondance() {
		return correspondance;
	}

	public void setCorrespondance(Arret correspondance) {
		this.correspondance = correspondance;
	}

	public void setArrive(Arret arrive) {
		this.arrive = arrive;
	}

	public boolean isCreated() {
		return isCreated;
	}

	public void setCreated(boolean isCreated) {
		this.isCreated = isCreated;
	}

	public Train getTrain() {
		return train;
	}

	public void setTrain(Train train) {
		this.train = train;
	}

	public boolean isArrived() {
		return arrived;
	}

	public void setArrived(boolean arrived) {
		this.arrived = arrived;
	}

}