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
@NamedQueries({ @NamedQuery(name = "getAllHeureDePassage", query = "SELECT h FROM HeureDePassage h"),
		@NamedQuery(name = "findHeureByTrainIdAndArretId", query = "SELECT h FROM HeureDePassage h WHERE h.train.id = :trainId AND h.arret.id = :arretId"),
		@NamedQuery(name = "findHeureByTrainId", query = "SELECT h FROM HeureDePassage h WHERE h.train.id = :trainId"),
		@NamedQuery(name = "findHeureByArretId", query = "SELECT h FROM HeureDePassage h WHERE h.arret.id = :arretId") })
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

	protected LocalDateTime baseDepartTemps;

	protected LocalDateTime baseArriveeTemps;

	protected LocalDateTime reelDepartTemps;

	protected LocalDateTime reelArriveeTemps;

	protected boolean terminus;

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

	public LocalDateTime getBaseDepartTemps() {
		return baseDepartTemps;
	}

	public void setBaseDepartTemps(LocalDateTime baseDepartTemps) {
		this.baseDepartTemps = baseDepartTemps;
	}

	public LocalDateTime getBaseArriveeTemps() {
		return baseArriveeTemps;
	}

	public void setBaseArriveeTemps(LocalDateTime baseArriveeTemps) {
		this.baseArriveeTemps = baseArriveeTemps;
	}

	public LocalDateTime getReelDepartTemps() {
		return reelDepartTemps;
	}

	public void setReelDepartTemps(LocalDateTime reelDepartTemps) {
		this.reelDepartTemps = reelDepartTemps;
	}

	public LocalDateTime getReelArriveeTemps() {
		return reelArriveeTemps;
	}

	public void setReelArriveeTemps(LocalDateTime reelArriveeTemps) {
		this.reelArriveeTemps = reelArriveeTemps;
	}

	public boolean isTerminus() {
		return terminus;
	}

	public void setTerminus(boolean terminus) {
		this.terminus = terminus;
	}

	public boolean isCreated() {
		return isCreated;
	}

	public void setCreated(boolean isCreated) {
		this.isCreated = isCreated;
	}

}
