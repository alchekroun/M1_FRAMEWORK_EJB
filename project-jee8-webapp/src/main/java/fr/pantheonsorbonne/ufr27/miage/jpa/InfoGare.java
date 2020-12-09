package fr.pantheonsorbonne.ufr27.miage.jpa;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;

@Entity
@NamedQueries({ @NamedQuery(name = "getAllInfoGare", query = "SELECT i FROM InfoGare i"),
		@NamedQuery(name = "deleteInfoGare", query = "DELETE FROM InfoGare i WHERE i.id = :id") })
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
}
