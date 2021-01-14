package fr.pantheonsorbonne.ufr27.miage.dao;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import fr.pantheonsorbonne.ufr27.miage.jpa.Arret;
import fr.pantheonsorbonne.ufr27.miage.jpa.Passager;
import fr.pantheonsorbonne.ufr27.miage.jpa.Train;

public class TrainDAO {

	@Inject
	EntityManager em;
	
	@Inject
	PerturbationDAO perturbationDao;

	@Inject
	HeureDePassageDAO hdpDAO;

	public Train getTrainFromId(int trainId) {
		return em.find(Train.class, trainId);
	}

	public Train updateTrain(Train trainOriginal, fr.pantheonsorbonne.ufr27.miage.model.jaxb.Train trainUpdate) {
		trainOriginal.setNom(trainUpdate.getNom());
		trainOriginal.setDirectionType(trainUpdate.getDirectionType());
		trainOriginal.setNumero(trainUpdate.getNumeroTrain());
		trainOriginal.setReseau(trainUpdate.getReseau());

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
		if (!perturbationDao.getPerturbationByTrain(train).isEmpty()) {
			List<fr.pantheonsorbonne.ufr27.miage.jpa.Perturbation> listePerturbations = perturbationDao.getPerturbationByTrain(train);
			for ( fr.pantheonsorbonne.ufr27.miage.jpa.Perturbation perturbation : listePerturbations) {
		    perturbationDao.deletePerturbation(perturbation);
		}
	 }
		em.remove(train);
	}

	public List<Train> findTrainByArret(int arretId) {
		return em.createNamedQuery("findTrainByArret").setParameter("arretId", arretId).getResultList();
	}
	
	public List<Train> findTrainByArretAndDepartAfterDate(int arretId, LocalDateTime date) {
		return em.createNamedQuery("findTrainByArretAndDepartAfterDate").setParameter("arretId", arretId).setParameter("temps", date).getResultList();
	}

	public void addArret(Train train, Arret arret, LocalDateTime departTemps, LocalDateTime arriveeTemps,
			boolean desservi, boolean terminus) {
		hdpDAO.createHeureDePassage(train, arret, departTemps, arriveeTemps, desservi, terminus);
	}

	public void removeArret(Train train, Arret arret) {
		hdpDAO.deleteHeureDePassage(train, arret);
	}

	public void addPassager(Train train, Passager p) {
		p.setTrain(train);
		em.merge(p);
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

}
