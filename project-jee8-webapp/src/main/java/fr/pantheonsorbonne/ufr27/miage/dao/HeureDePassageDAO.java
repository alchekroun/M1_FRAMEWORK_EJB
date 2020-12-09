package fr.pantheonsorbonne.ufr27.miage.dao;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import fr.pantheonsorbonne.ufr27.miage.jpa.HeureDePassage;

public class HeureDePassageDAO {
	@Inject
	EntityManager em;

	public HeureDePassage getHeureFromId(int heureId) {
		fr.pantheonsorbonne.ufr27.miage.jpa.HeureDePassage heureDePassage = em.find(fr.pantheonsorbonne.ufr27.miage.jpa.HeureDePassage.class, heureId);
		return heureDePassage;
	}
	public void insertArretAndTrain(int trainId, int arretId) {
		em.createNamedQuery("INSERT INTO HeureDePassage (train_id, arret_id) VALUES (:trainId, :arretId)")
			.setParameter("trainId",trainId)
			.setParameter("arretId",arretId);
	}

	public List<HeureDePassage> findHeureByTrainIdAndArretId(int trainId, int arretId) {
		// TODO peut etre ameliorer avec getHeureFromId au lieu de return une List
		return em.createNamedQuery("findHeureByTrainIdAndArretId").getResultList();
	}
}
