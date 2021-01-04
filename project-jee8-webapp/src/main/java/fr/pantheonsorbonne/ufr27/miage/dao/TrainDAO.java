package fr.pantheonsorbonne.ufr27.miage.dao;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import fr.pantheonsorbonne.ufr27.miage.jpa.Arret;
import fr.pantheonsorbonne.ufr27.miage.jpa.HeureDePassage;
import fr.pantheonsorbonne.ufr27.miage.jpa.Passager;
import fr.pantheonsorbonne.ufr27.miage.jpa.Train;

public class TrainDAO {

	@Inject
	EntityManager em;

	@Inject
	HeureDePassageDAO hdpDAO;

	public Train getTrainFromId(int trainId) {
		return em.find(Train.class, trainId);
	}

	public Train updateTrain(Train trainOriginal, fr.pantheonsorbonne.ufr27.miage.model.jaxb.Train trainUpdate) {
		trainOriginal.setNom(trainUpdate.getNom());
		trainOriginal.setDirection(em.find(Arret.class, trainUpdate.getDirection().getId()));
		trainOriginal.setDirectionType(trainUpdate.getDirectionType());
		trainOriginal.setNumero(trainUpdate.getNumeroTrain());
		trainOriginal.setReseau(trainUpdate.getReseau());
		trainOriginal.setBaseArriveeTemps(trainUpdate.getBaseArriveeTemps());
		trainOriginal.setBaseDepartTemps(trainUpdate.getBaseDepartTemps());
		trainOriginal.setReelDepartTemps(trainUpdate.getReelDepartTemps());
		trainOriginal.setReelArriveeTemps(trainUpdate.getReelArriveeTemps());

		// TODO Vérifier si l'on doit quand même ajouter les setter des list passager et
		// hdp
		return trainOriginal;
	}

	public List<Train> getAllTrain() {
		return em.createNamedQuery("getAllTrain").getResultList();
	}

	public void deleteTrain(Train train) {
		if (!train.getListePassagers().isEmpty()) {
			for (Passager p : train.getListePassagers()) {
				p.setTrain(null);
			}
		}
		if (!train.getListeHeureDePassage().isEmpty()) {
			hdpDAO.deleteHeureDePassageByTrain(train);
		}
		// TODO A revoir, étudié s'il faut utiliser le DAO arret
		train.getDirection().removeTrainArrivant(train);
		em.remove(train);
	}

	public List<Train> findTrainByDirection(int arretId) {
		return em.createNamedQuery("findTrainByDirection").setParameter("arretId", arretId).getResultList();
	}

	public List<Train> findTrainByArret(int arretId) {
		return em.createNamedQuery("findTrainByArret").setParameter("arretId", arretId).getResultList();
	}

	public void addArret(Train train, Arret arret, LocalDateTime passage) {
		hdpDAO.createHeureDePassage(train, arret, passage);
	}

	public void removeArret(Train train, Arret arret) {
		hdpDAO.deleteHeureDePassage(train, arret);
	}

	public void addPassager(Train train, Passager p) {
		p.setTrain(train);
		train.addPassager(p);
	}

	public void removePassager(Train train, Passager p) {
		p.setTrain(null);
		train.removePassager(p);
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
