package fr.pantheonsorbonne.ufr27.miage.dao;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import fr.pantheonsorbonne.ufr27.miage.jpa.Train;

public class TrainDAO {

	@Inject
	EntityManager em;

	@Inject
	ArretDAO arretDAO;

	@Inject
	HeureDePassageDAO heureDAO;


	public Train getTrainFromId(int trainId) {
		return em.find(Train.class, trainId);
	}

	public List<Train> getAllTrain() {
		return em.createNamedQuery("getAllTrain").getResultList();
	}

	public void deleteTrain(int trainId) {
		em.createNamedQuery("deleteTrain").setParameter("id", trainId).executeUpdate();
	}

	public void addArret(Train train, int arretId) {
		
		heureDAO.insertArretAndTrain(train.getId(), arretId);
		//arretDAO.getArretFromId(arretId)
		train.getListeHeureDePassage().add(heureDAO.findHeureByTrainIdAndArretId(train.getId(), arretId ).get(0));
		//ajouter train arrivant aussi sur larret???
	}
	
//	public void addNewDirection(Train train, int arretId) {
//		train.setDirection()
//	}
//	
//	public findTrainByArret(Train train, int arretId) {
//		train.getListeHeureDePassage
//	}
}
