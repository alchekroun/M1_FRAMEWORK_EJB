package fr.pantheonsorbonne.ufr27.miage.jpa;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;

@Entity
@NamedQueries({ @NamedQuery(name = "getAllInfoGare", query = "SELECT i FROM InfoGare i") })
public class InfoGare implements Serializable {

	/**
	* 
	*/
	private static final long serialVersionUID = -5825103100885399293L;

	@Id
	protected int id;

	@OneToOne
	@MapsId
	@JoinColumn(name = "localisation_arret_id")
	protected Arret localisation;

	boolean isCreated;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Arret getLocalisation() {
		return localisation;
	}

	public void setLocalisation(Arret localisation) {
		this.localisation = localisation;
	}

	public boolean isCreated() {
		return isCreated;
	}

	public void setCreated(boolean isCreated) {
		this.isCreated = isCreated;
	}
}
