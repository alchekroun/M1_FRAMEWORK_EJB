package fr.pantheonsorbonne.ufr27.miage.jpa;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Passager {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	int id;

	protected String nomPassager;
	@ManyToOne
	protected Arret depart;
	@ManyToOne
	protected Arret arrive;

	public int getId() {
		return id;
	}

	public void setid(int id) {
		this.id = id;
	}

	public String getNomPassager() {
		return nomPassager;
	}

	public void setNomPassager(String nomPassager) {
		this.nomPassager = nomPassager;
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