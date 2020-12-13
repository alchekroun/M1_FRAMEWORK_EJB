package fr.pantheonsorbonne.ufr27.miage.dao;

import java.time.LocalDateTime;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import fr.pantheonsorbonne.ufr27.miage.jpa.Arret;
import fr.pantheonsorbonne.ufr27.miage.jpa.HeureDePassage;
import fr.pantheonsorbonne.ufr27.miage.jpa.HeureDePassageKey;
import fr.pantheonsorbonne.ufr27.miage.jpa.Train;

public class HeureDePassageDAO {
	@Inject
	EntityManager em;

	public HeureDePassage createHeureDePassage(Train train, Arret arret, LocalDateTime passage) {
		HeureDePassageKey hdpKey = new HeureDePassageKey();
		hdpKey.setArretId(arret.getId());
		hdpKey.setTrainId(train.getId());
		HeureDePassage hdp = new HeureDePassage();
		hdp.setId(hdpKey);
		hdp.setArret(arret);
		hdp.setTrain(train);
		hdp.setPassage(passage);
		em.persist(hdp);
		return hdp;
	}

	public HeureDePassage getHdpFromTrainIdAndArretId(int trainId, int arretId) {
		return (HeureDePassage) em.createNamedQuery("findHeureByTrainIdAndArretId").setParameter("trainId", trainId)
				.setParameter("arretId", arretId).getSingleResult();
	}

	public void deleteHeureDePassage(int trainId, int arretId) {
		em.remove(getHdpFromTrainIdAndArretId(trainId, arretId));
	}
}
