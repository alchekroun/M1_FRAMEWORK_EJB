package fr.pantheonsorbonne.ufr27.miage.jpa;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@Entity
@NamedQueries({ @NamedQuery(name = "getAllInfoCentre", query = "SELECT i FROM InfoCentre i"),
	@NamedQuery(name = "deleteInfoCentre", query = "DELETE FROM InfoCentre i WHERE i.id = :id") })
public class InfoCentre {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	protected int id;
   
    protected String nom;

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
