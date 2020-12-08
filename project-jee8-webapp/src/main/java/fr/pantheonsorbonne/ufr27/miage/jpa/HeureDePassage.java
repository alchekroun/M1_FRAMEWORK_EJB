package fr.pantheonsorbonne.ufr27.miage.jpa;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@Entity
@NamedQueries({ @NamedQuery(name = "getAllHeureDePassage", query = "SELECT h FROM HeureDePassage h"),
	@NamedQuery(name = "deleteHeureDePassage", query = "DELETE FROM HeureDePassage h WHERE h.id = :id") })
public class HeureDePassage {

	@Id
	protected int id;
	   
	protected LocalDateTime passage;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public LocalDateTime getPassage() {
		return passage;
	}

	public void setPassage(LocalDateTime passage) {
		this.passage = passage;
	}
	  
}
