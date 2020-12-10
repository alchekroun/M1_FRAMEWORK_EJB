package fr.pantheonsorbonne.ufr27.miage.dao;

import java.time.LocalDateTime;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import fr.pantheonsorbonne.ufr27.miage.jpa.HeureDePassage;
import fr.pantheonsorbonne.ufr27.miage.jpa.HeureDePassageKey;
import fr.pantheonsorbonne.ufr27.miage.jpa.Train;

public class HeureDePassageDAO {
	@Inject
	EntityManager em;

	public HeureDePassage createHeureDePassage(int trainId, int arretId, LocalDateTime passage) {
		HeureDePassageKey hdpKey = new HeureDePassageKey();
		hdpKey.setArretId(arretId);
		hdpKey.setTrainId(trainId);
		HeureDePassage hdp = new HeureDePassage();
		hdp.setId(hdpKey);
		hdp.setArret(em.find(fr.pantheonsorbonne.ufr27.miage.jpa.Arret.class, arretId));
		hdp.setTrain(em.find(fr.pantheonsorbonne.ufr27.miage.jpa.Train.class, trainId));
		hdp.setPassage(passage);
		em.persist(hdp);
		return hdp;
	}

	public HeureDePassage getHeureFromId(int heureId) {
		fr.pantheonsorbonne.ufr27.miage.jpa.HeureDePassage heureDePassage = em
				.find(fr.pantheonsorbonne.ufr27.miage.jpa.HeureDePassage.class, heureId);
		return heureDePassage;
	}

	public void insertArretAndTrain(int trainId, int arretId) {
		em.createNamedQuery("INSERT INTO HeureDePassage (train_id, arret_id) VALUES (:trainId, :arretId)")
				.setParameter("trainId", trainId).setParameter("arretId", arretId);
	}

	public HeureDePassage findHeureByTrainIdAndArretId(int trainId, int arretId) {
		// TODO peut etre ameliorer avec getHeureFromId au lieu de return une List
		return (HeureDePassage) em.createNamedQuery("findHeureByTrainIdAndArretId").getSingleResult();

	}

	public void deleteHeureDePassage(HeureDePassageKey hdpId) {
		em.createNamedQuery("deleteHeureDePassage").setParameter("id", hdpId);
	}

	public HeureDePassage getHeureDePassageRemoved(int trainId, int arretId, LocalDateTime passage) {
		HeureDePassage h = findHeureByTrainIdAndArretId(trainId, arretId);
		deleteHeureDePassage(h.getId());
		return h;
	}
}
