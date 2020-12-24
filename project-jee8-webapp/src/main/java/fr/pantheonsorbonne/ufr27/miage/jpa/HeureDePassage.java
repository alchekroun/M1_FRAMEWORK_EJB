package fr.pantheonsorbonne.ufr27.miage.jpa;

import java.time.LocalDateTime;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@Entity
@NamedQueries({
		@NamedQuery(name = "getAllHeureDePassage", query = "SELECT h FROM HeureDePassage h"),
		@NamedQuery(name = "findHeureByTrainIdAndArretId", query = "SELECT h FROM HeureDePassage h WHERE h.train.id = :trainId AND h.arret.id = :arretId") })
public class HeureDePassage {

	@EmbeddedId
	HeureDePassageKey id;

	@ManyToOne
	@MapsId("trainId")
	@JoinColumn(name = "train_id")
	Train train;

	@ManyToOne
	@MapsId("arretId")
	@JoinColumn(name = "arret_id")
	Arret arret;

	protected LocalDateTime passage;
	
	boolean isCreated;

	public HeureDePassageKey getId() {
		return id;
	}

	public void setId(HeureDePassageKey id) {
		this.id = id;
	}

	public Train getTrain() {
		return train;
	}

	public void setTrain(Train train) {
		this.train = train;
	}

	public Arret getArret() {
		return arret;
	}

	public void setArret(Arret arret) {
		this.arret = arret;
	}

	public LocalDateTime getPassage() {
		return passage;
	}

	public void setPassage(LocalDateTime passage) {
		this.passage = passage;
	}

	public boolean isCreated() {
		return isCreated;
	}

	public void setCreated(boolean isCreated) {
		this.isCreated = isCreated;
	}

}
