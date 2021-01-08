package fr.pantheonsorbonne.ufr27.miage.jpa;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;

@Entity
@NamedQueries({ @NamedQuery(name = "getAllPerturbation", query = "SELECT p FROM Perturbation p") })
public class Perturbation {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	protected int id;

	@OneToOne(cascade = CascadeType.ALL)
	protected Train train;

	protected String motif;

	protected int dureeEnPlus;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Train getTrain() {
		return train;
	}

	public void setTrain(Train train) {
		this.train = train;
	}

	public String getMotif() {
		return motif;
	}

	public void setMotif(String motif) {
		this.motif = motif;
	}

	public int getDureeEnPlus() {
		return dureeEnPlus;
	}

	public void setDureeEnPlus(int dureeEnPlus) {
		this.dureeEnPlus = dureeEnPlus;
	}

}
