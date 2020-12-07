package fr.pantheonsorbonne.ufr27.miage.jpa;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class HeureDePassage {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	protected int idTrain;
	   
	protected LocalDateTime passage;

	public int getIdTrain() {
		return idTrain;
	}

	public void setIdTrain(int idTrain) {
		this.idTrain = idTrain;
	}

	public LocalDateTime getPassage() {
		return passage;
	}

	public void setPassage(LocalDateTime passage) {
		this.passage = passage;
	}
	  
}
