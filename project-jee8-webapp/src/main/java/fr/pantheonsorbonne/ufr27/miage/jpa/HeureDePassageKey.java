package fr.pantheonsorbonne.ufr27.miage.jpa;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class HeureDePassageKey implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1672615368672146835L;

	@Column(name = "train_id")
	int trainId;

	@Column(name = "arret_id")
	int arretId;

	public int getTrainId() {
		return trainId;
	}

	public void setTrainId(int trainId) {
		this.trainId = trainId;
	}

	public int getArretId() {
		return arretId;
	}

	public void setArretId(int arretId) {
		this.arretId = arretId;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof HeureDePassageKey) {
			HeureDePassageKey h = (HeureDePassageKey) obj;
			if (this.arretId == h.arretId && this.trainId == h.trainId) {
				return true;
			}
			return false;

		}
		return false;
	}

	@Override
	public int hashCode() {
		int hash = 1;
		hash = hash * 17 + trainId;
		hash = hash * 31 + arretId;
		return hash;
	}

}
