package fr.pantheonsorbonne.ufr27.miage.jpa;

import fr.pantheonsorbonne.ufr27.miage.model.jaxb.Arret;

public class Passager {

	protected int idPassager;

	protected String nomPassager;

	protected Arret depart;

	protected Arret arrive;

	public int getIdPassager() {
		return idPassager;
	}

	public void setIdPassager(int idPassager) {
		this.idPassager = idPassager;
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
