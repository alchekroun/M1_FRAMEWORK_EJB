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

	/**
	 * Méthode permettant de récupérer un train à partir de son id
	 * 
	 * @param trainId
	 * @return Train
	 */
	public Train getTrainFromId(int trainId) {
		return em.find(Train.class, trainId);
	}

	/**
	 * Méthode permettant de mettre à jour un train
	 * 
	 * @param trainOriginal
	 * @param trainUpdate
	 * @return Train
	 */
	public Train updateTrain(Train trainOriginal, fr.pantheonsorbonne.ufr27.miage.model.jaxb.Train trainUpdate) {
		trainOriginal.setNom(trainUpdate.getNom());
		trainOriginal.setNumero(trainUpdate.getNumeroTrain());
		trainOriginal.setReseau(trainUpdate.getReseau());
		trainOriginal.setStatut(trainUpdate.getStatut());

		return trainOriginal;
	}

	/**
	 * Méthode permettant de récupérer la liste de tous les trains de la base de
	 * données
	 * 
	 * @return List<Train>
	 */
	public List<Train> getAllTrain() {
		return em.createNamedQuery("getAllTrain").getResultList();
	}

	/**
	 * Méthode permettant de supprimer un train de la base de données
	 * 
	 * @param train
	 */
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
			List<fr.pantheonsorbonne.ufr27.miage.jpa.Perturbation> listePerturbations = perturbationDao
					.getPerturbationByTrain(train);
			for (fr.pantheonsorbonne.ufr27.miage.jpa.Perturbation perturbation : listePerturbations) {
				perturbationDao.deletePerturbation(perturbation);
			}
		}
		em.remove(train);
	}

	/**
	 * Méthode permettant de récupérer les trains passant par un arrêt
	 * 
	 * @param arretId
	 * @return List<Train>
	 */
	public List<Train> findTrainByArret(int arretId) {
		return em.createNamedQuery("findTrainByArret").setParameter("arretId", arretId).getResultList();
	}

	/**
	 * Méthode permettant de récupérer les trains passant par un arrêt après la date
	 * de départ du train
	 * 
	 * @param arretId
	 * @param date
	 * @return List<Train>
	 */
	public List<Train> findTrainByArretAndDepartAfterDate(int arretId, LocalDateTime date) {
		return em.createNamedQuery("findTrainByArretAndDepartAfterDate").setParameter("arretId", arretId)
				.setParameter("temps", date).getResultList();
	}

	/**
	 * Méthode permettant de récupérer les trains passant par un arrêt desservi
	 * après la date de départ du train
	 * 
	 * @param arretId
	 * @param date
	 * @return List<Train>
	 */
	public List<Train> findTrainByArretAndDepartAfterDateAndDesservi(int arretId, LocalDateTime date) {
		return em.createNamedQuery("findTrainByArretAndDepartAfterDateAndDesservi").setParameter("arretId", arretId)
				.setParameter("temps", date).getResultList();
	}

	/**
	 * Méthode permettant de récupérer les trains passant par un arrêt avant la date
	 * d'arrivée du train
	 * 
	 * @param arretId
	 * @param date
	 * @return List<Train>
	 */
	public List<Train> findTrainByArretAndArriveeBeforeDate(int arretId, LocalDateTime date) {
		return em.createNamedQuery("findTrainByArretAndArriveeBeforeDate").setParameter("arretId", arretId)
				.setParameter("temps", date).getResultList();
	}

	/**
	 * Méthode permettant d'ajouter un arrêt à un train
	 * 
	 * @param train
	 * @param arret
	 * @param departTemps
	 * @param arriveeTemps
	 * @param desservi
	 * @param terminus
	 */
	public void addArret(Train train, Arret arret, LocalDateTime departTemps, LocalDateTime arriveeTemps,
			boolean desservi, boolean terminus) {
		hdpDAO.createHeureDePassage(train, arret, departTemps, arriveeTemps, desservi, terminus);
	}

	/**
	 * Méthode permettant de supprimer un arrêt à un train
	 * 
	 * @param train
	 * @param arret
	 */
	public void removeArret(Train train, Arret arret) {
		hdpDAO.deleteHeureDePassage(train, arret);
	}

	/**
	 * Méthode permettant d'ajouter un passager dans un train
	 * 
	 * @param train
	 * @param p
	 */
	public void addPassager(Train train, Passager p) {
		p.setTrain(train);
		em.merge(p);
		train.addPassager(p);
	}

	/**
	 * Méthode permettant de supprimer un passager dans un train
	 * 
	 * @param train
	 * @param p
	 */
	public void removePassager(Train train, Passager p) {
		p.setTrain(null);
		if (p.getArrive().equals(p.getDepart())) {
			p.setArrived(true);
		}
		em.merge(p);
		train.removePassager(p);
	}

	/**
	 * Méthode permettant de vérifier que le train est bien créé
	 * 
	 * @param trainId
	 * @return true si le train a été correctement créé, false sinon
	 */
	public boolean isTrainCreated(int trainId) {

		Train t = em.find(Train.class, trainId);
		if (t == null) {
			throw new NoSuchElementException("No train");
		}
		return t.isCreated();

	}

//	public int findNombrePassagerByTrain(int trainId) {
//		return (int) em.createNamedQuery("findNombrePassagerByTrain").setParameter("trainId", trainId).getSingleResult();
//	}

}
