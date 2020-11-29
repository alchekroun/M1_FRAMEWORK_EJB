package fr.pantheonsorbonne.ufr27.miage.jpa;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Train {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	int id;
	
	public int getId() {
		return id;
	}
}
