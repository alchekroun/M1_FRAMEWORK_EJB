package fr.pantheonsorbonne.ufr27.miage.dao;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import fr.pantheonsorbonne.ufr27.miage.jpa.Arret;
import fr.pantheonsorbonne.ufr27.miage.jpa.HeureDePassage;
import fr.pantheonsorbonne.ufr27.miage.jpa.HeureDePassageKey;
import fr.pantheonsorbonne.ufr27.miage.jpa.Train;

public class HeureDePassageDAO {

	@Inject
	public HeureDePassageDAO(EntityManager em) {
		this.em = em;
	}

	EntityManager em;

	public HeureDePassage createHeureDePassage(Train train, Arret arret, LocalDateTime departTemps,
			LocalDateTime arriveeTemps, boolean terminus) {
		HeureDePassageKey hdpKey = new HeureDePassageKey();
		hdpKey.setArretId(arret.getId());
		hdpKey.setTrainId(train.getId());
		HeureDePassage hdp = new HeureDePassage();
		hdp.setId(hdpKey);
		hdp.setArret(arret);
		hdp.setTrain(train);
		hdp.setBaseDepartTemps(departTemps);
		hdp.setReelDepartTemps(departTemps);
		hdp.setBaseArriveeTemps(arriveeTemps);
		hdp.setReelArriveeTemps(arriveeTemps);
		hdp.setTerminus(terminus);
		em.persist(hdp);
		train.addArretHeureDePassage(hdp);
		arret.addArretHeureDePassage(hdp);
		return hdp;
	}

	public HeureDePassage updateHeureDePassage(Train train, Arret arret, LocalDateTime newBaseDepartTemps,
			LocalDateTime newBaseArriveeTemps, LocalDateTime newReelDepartTemps, LocalDateTime newReelArriveeTemps,
			boolean newTerminus) {
		HeureDePassage hdp = getHdpFromTrainIdAndArretId(train.getId(), arret.getId());
		hdp.setArret(arret);
		hdp.setTrain(train);
		hdp.setBaseArriveeTemps(newBaseArriveeTemps);
		hdp.setBaseDepartTemps(newBaseDepartTemps);
		hdp.setReelArriveeTemps(newReelArriveeTemps);
		hdp.setReelDepartTemps(newReelDepartTemps);
		hdp.setTerminus(newTerminus);
		return hdp;
	}

	public void deleteHeureDePassage(Train train, Arret arret) {
		HeureDePassage hdp = getHdpFromTrainIdAndArretId(train.getId(), arret.getId());
		train.removeArretHeureDePassage(hdp);
		arret.removeArretHeureDePassage(hdp);
		em.remove(hdp);
	}

	// Obliger d'utiliser des fonctions comme celles ci pour eviter les erreurs :
	// ConcurentModificationException
	public void deleteHeureDePassageByTrain(Train train) {
		List<HeureDePassage> listHdp = getAllHeureDePassage();
		for (HeureDePassage hdp : listHdp) {
			if (hdp.getTrain() == train) {
				deleteHeureDePassage(train, hdp.getArret());
			}
		}
	}

	public void deleteHeureDePassageByArret(Arret arret) {
		List<HeureDePassage> listHdp = getAllHeureDePassage();
		for (HeureDePassage hdp : listHdp) {
			if (hdp.getArret() == arret) {
				deleteHeureDePassage(hdp.getTrain(), arret);
			}
		}
	}

	public HeureDePassage getHeureDePassageFromKey(HeureDePassageKey key) {
		return em.find(HeureDePassage.class, key);
	}

	public HeureDePassage getHdpFromTrainIdAndArretId(int trainId, int arretId) {
		return (HeureDePassage) em.createNamedQuery("findHeureByTrainIdAndArretId").setParameter("trainId", trainId)
				.setParameter("arretId", arretId).getSingleResult();
	}

	public List<HeureDePassage> getAllHeureDePassage() {
		return em.createNamedQuery("getAllHeureDePassage").getResultList();
	}

	public boolean isHeureDePassageCreated(HeureDePassageKey key) {

		HeureDePassage h = em.find(HeureDePassage.class, key);
		if (h == null) {
			throw new NoSuchElementException("No HeureDePassage");
		}
		return h.isCreated();

	}

	public HeureDePassage findHeureByTrainAndArret(Train train, Arret arret) {
		// replace by something better
		return em.find(Train.class, train.getId()).getListeHeureDePassage().stream()
				.filter(hdp -> hdp.getArret().equals(arret)).findFirst().orElseThrow(null);
	}
}
