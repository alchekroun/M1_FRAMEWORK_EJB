package fr.pantheonsorbonne.ufr27.miage.dao;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import fr.pantheonsorbonne.ufr27.miage.jpa.Arret;
import fr.pantheonsorbonne.ufr27.miage.jpa.HeureDePassage;
import fr.pantheonsorbonne.ufr27.miage.jpa.Train;

public class TrainDAO {

	@Inject
	EntityManager em;

	@Inject
	HeureDePassageDAO hdpDAO;

	@Inject
	ArretDAO arretDAO;

	public Train getTrainFromId(int trainId) {
		return em.find(Train.class, trainId);
	}

	public List<Train> getAllTrain() {
		return em.createNamedQuery("getAllTrain").getResultList();
	}

	public void deleteTrain(int trainId) {
		em.remove(em.find(Train.class, trainId));
	}

	public void addArret(Train train, Arret arret, LocalDateTime passage) {
		HeureDePassage hdp = hdpDAO.createHeureDePassage(train, arret, passage);
		train.addArretHeureDePassage(hdp);
		arret.addArretHeureDePassage(hdp);
	}

	public List<Train> findTrainByDirection(int arretId) {
		return em.createNamedQuery("findTrainByDirection").setParameter("arretId", arretId).getResultList();
	}

	public List<Train> findTrainByArret(int arretId) {
		return em.createNamedQuery("findTrainByArret").setParameter("arredId", arretId).getResultList();
	}

	public void removeArret(Train train, int arretId) {
		HeureDePassage hdp = hdpDAO.getHdpFromTrainIdAndArretId(train.getId(), arretId);
		train.removeArretHeureDePassage(hdp);
		em.find(Arret.class, arretId).removeArretHeureDePassage(hdp);
		em.remove(hdp);
	}

	public boolean isTrainCreated(int trainId) {

		Train t = em.find(Train.class, trainId);
		if (t == null) {
			throw new NoSuchElementException("No train");
		}
		return t.isCreated();

	}

//	public void addNewDirection(Train train, int arretId) {
//		train.setDirection()
//	}
//	
//	public findTrainByArret(Train train, int arretId) {
//		train.getListeHeureDePassage
//	}
}
