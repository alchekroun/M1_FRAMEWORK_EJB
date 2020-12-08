package fr.pantheonsorbonne.ufr27.miage.jpa;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class HeureDePassageKey implements Serializable {

	@Column(name = "train_id")
	int trainId;

	@Column(name = "arret_id")
	int courseId;

	public int getTrainId() {
		return trainId;
	}

	public void setTrainId(int trainId) {
		this.trainId = trainId;
	}

	public int getCourseId() {
		return courseId;
	}

	public void setCourseId(int courseId) {
		this.courseId = courseId;
	}

}
