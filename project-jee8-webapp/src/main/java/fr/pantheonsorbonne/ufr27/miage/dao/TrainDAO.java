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
		// TODO
	}

}
