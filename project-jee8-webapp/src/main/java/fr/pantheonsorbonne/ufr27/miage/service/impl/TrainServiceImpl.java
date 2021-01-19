package fr.pantheonsorbonne.ufr27.miage.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import fr.pantheonsorbonne.ufr27.miage.dao.ArretDAO;
import fr.pantheonsorbonne.ufr27.miage.dao.HeureDePassageDAO;
import fr.pantheonsorbonne.ufr27.miage.dao.PassagerDAO;
import fr.pantheonsorbonne.ufr27.miage.dao.PerturbationDAO;
import fr.pantheonsorbonne.ufr27.miage.dao.TrainDAO;
import fr.pantheonsorbonne.ufr27.miage.exception.CantCreateException;
import fr.pantheonsorbonne.ufr27.miage.exception.CantUpdateException;
import fr.pantheonsorbonne.ufr27.miage.exception.EmptyListException;
import fr.pantheonsorbonne.ufr27.miage.exception.NoSuchArretException;
import fr.pantheonsorbonne.ufr27.miage.exception.NoSuchHdpException;
import fr.pantheonsorbonne.ufr27.miage.exception.NoSuchTrainException;
import fr.pantheonsorbonne.ufr27.miage.mapper.HeureDePassageMapper;
import fr.pantheonsorbonne.ufr27.miage.mapper.PassagerMapper;
import fr.pantheonsorbonne.ufr27.miage.mapper.TrainMapper;
import fr.pantheonsorbonne.ufr27.miage.model.jaxb.HeureDePassage;
import fr.pantheonsorbonne.ufr27.miage.model.jaxb.Passager;
import fr.pantheonsorbonne.ufr27.miage.model.jaxb.Perturbation;
import fr.pantheonsorbonne.ufr27.miage.model.jaxb.Train;

import fr.pantheonsorbonne.ufr27.miage.service.TrainService;

public class TrainServiceImpl implements TrainService {

	@Inject
	EntityManager em;

	@Inject
	TrainDAO dao;

	@Inject
	ArretDAO arretDAO;

	@Inject
	HeureDePassageDAO hdpDAO;

	@Inject
	PerturbationDAO perturbationDAO;

	@Inject
	PassagerDAO passagerDAO;

	// Create
	@Override
	public int createTrain(Train trainDTO) throws CantCreateException {
		em.getTransaction().begin();
		try {

			fr.pantheonsorbonne.ufr27.miage.jpa.Train train = new fr.pantheonsorbonne.ufr27.miage.jpa.TrainAvecResa();

			train.setNom(trainDTO.getNom());
			train.setDirectionType(trainDTO.getDirectionType());
			train.setNumero(trainDTO.getNumeroTrain());
			train.setReseau(trainDTO.getReseau());
			train.setStatut(trainDTO.getStatut());

			em.persist(train);

			em.getTransaction().commit();

			return train.getId();
		} catch (org.eclipse.persistence.exceptions.DatabaseException e) {
			em.getTransaction().rollback();
			throw new CantCreateException();
		}
	}

	// Read
	@Override
	public Train getTrainFromId(int trainId) throws NoSuchTrainException {
		fr.pantheonsorbonne.ufr27.miage.jpa.Train train = dao.getTrainFromId(trainId);
		if (train == null) {
			throw new NoSuchTrainException();
		}
		return TrainMapper.trainDTOMapper(train);
	}

	// Update
	@Override
	public void updateTrain(Train trainUpdate) throws NoSuchTrainException, CantUpdateException {
		em.getTransaction().begin();
		try {
			fr.pantheonsorbonne.ufr27.miage.jpa.Train trainOriginal = dao.getTrainFromId(trainUpdate.getId());
			if (trainOriginal == null) {
				throw new NoSuchTrainException();
			}

			em.merge(dao.updateTrain(trainOriginal, trainUpdate));
			em.getTransaction().commit();
		} catch (org.eclipse.persistence.exceptions.DatabaseException e) {
			em.getTransaction().rollback();
			throw new CantUpdateException();
		}
	}

	// Delete
	@Override
	public void deleteTrain(int trainId) throws NoSuchTrainException {
		em.getTransaction().begin();
		fr.pantheonsorbonne.ufr27.miage.jpa.Train train = dao.getTrainFromId(trainId);
		if (train == null) {
			throw new NoSuchTrainException();
		}
		/*
		 * List<fr.pantheonsorbonne.ufr27.miage.jpa.Perturbation> listePerturbations =
		 * perturbationDAO.getPerturbationByTrain(train);
		 * for(fr.pantheonsorbonne.ufr27.miage.jpa.Perturbation pertu :
		 * listePerturbations) { perturbationDAO.deletePerturbation(pertu); }
		 */
		dao.deleteTrain(train);

		em.getTransaction().commit();
	}

	@Override
	public List<Train> getAllTrain() throws EmptyListException {
		List<fr.pantheonsorbonne.ufr27.miage.jpa.Train> listeTrains = dao.getAllTrain();
		if (listeTrains == null) {
			throw new EmptyListException();
		}
		return TrainMapper.trainAllDTOMapper(listeTrains);
	}

	@Override
	public void addArret(int trainId, int arretId, String passage, boolean desservi, boolean terminus)
			throws NoSuchTrainException, NoSuchArretException {
		em.getTransaction().begin();

		fr.pantheonsorbonne.ufr27.miage.jpa.Train train = dao.getTrainFromId(trainId);
		if (train == null) {
			em.getTransaction().rollback();
			throw new NoSuchTrainException();
		}
		fr.pantheonsorbonne.ufr27.miage.jpa.Arret arret = arretDAO.getArretFromId(arretId);
		if (arret == null) {
			em.getTransaction().rollback();
			throw new NoSuchArretException();
		}

		String[] horraires = passage.split(" ");

		dao.addArret(train, arret, LocalDateTime.parse(horraires[0]), LocalDateTime.parse(horraires[1]), desservi,
				terminus);

		em.getTransaction().commit();
	}

	@Override
	public void removeArret(int trainId, int arretId) throws NoSuchTrainException, NoSuchArretException {
		em.getTransaction().begin();
		fr.pantheonsorbonne.ufr27.miage.jpa.Train train = dao.getTrainFromId(trainId);
		if (train == null) {
			em.getTransaction().rollback();
			throw new NoSuchTrainException();
		}
		fr.pantheonsorbonne.ufr27.miage.jpa.Arret arret = arretDAO.getArretFromId(arretId);
		if (arret == null) {
			em.getTransaction().rollback();
			throw new NoSuchArretException();
		}

		dao.removeArret(train, arret);

		em.getTransaction().commit();

	}

	@Override
	public void changeParameterDesservi(int trainId, int arretId, boolean newDesservi)
			throws NoSuchTrainException, NoSuchArretException, NoSuchHdpException {
		em.getTransaction().begin();
		fr.pantheonsorbonne.ufr27.miage.jpa.Train train = dao.getTrainFromId(trainId);
		if (train == null) {
			em.getTransaction().rollback();
			throw new NoSuchTrainException();
		}
		fr.pantheonsorbonne.ufr27.miage.jpa.Arret arret = arretDAO.getArretFromId(arretId);
		if (arret == null) {
			em.getTransaction().rollback();
			throw new NoSuchArretException();
		}

		fr.pantheonsorbonne.ufr27.miage.jpa.HeureDePassage hdp = hdpDAO.getHdpFromTrainIdAndArretId(trainId, arretId);
		if (hdp == null) {
			em.getTransaction().rollback();
			throw new NoSuchHdpException();
		}

		hdpDAO.changeParameterDesservi(hdp, newDesservi);

		em.merge(hdp);
		em.merge(train);
		em.merge(arret);
		em.getTransaction().commit();

	}

	@Override
	public void createPerturbation(Perturbation perturbation) throws NoSuchTrainException {
		em.getTransaction().begin();
		fr.pantheonsorbonne.ufr27.miage.jpa.Train train = dao.getTrainFromId(perturbation.getTrain().getId());
		if (train == null) {
			em.getTransaction().rollback();
			throw new NoSuchTrainException();
		}

		perturbationDAO.impacterTrafic(perturbationDAO.createPerturbation(perturbation));

		em.getTransaction().commit();
	}

	@Override
	public void enMarche(Train train) throws NoSuchTrainException, NoSuchArretException {
		fr.pantheonsorbonne.ufr27.miage.jpa.Train trainJPA = dao.getTrainFromId(train.getId());
		if (trainJPA == null) {
			throw new NoSuchTrainException();
		}
		if (hdpDAO.findHdpByTrain(trainJPA.getId()) != null) {
			HeureDePassage hdpActuel = verifIfExistArretNow(trainJPA.getId());
			if (hdpActuel != null) {
				// Fait descendre les gens qui doivent descendre
				// Arrivee
				descendreListPassager(
						PassagerMapper.passagerAllDTOMapper(
								passagerDAO.getAllPassagerByArrivee(hdpActuel.getArret().getId())),
						trainJPA, hdpActuel.getArret().getId());
				// Correspondance
				descendreListPassager(
						PassagerMapper.passagerAllDTOMapper(
								passagerDAO.getAllPassagerByCorrespondance(hdpActuel.getArret().getId())),
						trainJPA, hdpActuel.getArret().getId());

				if (!hdpActuel.isTerminus()) {
					// Fait monter les gens qui attendent sur le quai

					// TODO Rajouter une fonction qui vérifie que les gens qui montent dans le train
					// ont bien leur arret qui soit desservi par ce même train

					monterListPassager(PassagerMapper.passagerAllDTOMapper(
							passagerDAO.getAllPassagerByDepart(hdpActuel.getArret().getId())), trainJPA);

				} else {
					// interupt le thread car nous sommes arriver au terminus
				}
			} else {
				// Le train n'a plus d'arrêt à desservir. Il faut interupt le thread.
			}
		} else {
			// Le train n'a aucun arrêt à desservir
			throw new NoSuchArretException();
		}
	}

	protected void descendreListPassager(List<Passager> listPassager, fr.pantheonsorbonne.ufr27.miage.jpa.Train train,
			int arretId) {
		em.getTransaction().begin();
		fr.pantheonsorbonne.ufr27.miage.jpa.Arret aJPA = em.find(fr.pantheonsorbonne.ufr27.miage.jpa.Arret.class,
				arretId);
		for (Passager p : listPassager) {
			fr.pantheonsorbonne.ufr27.miage.jpa.Passager pJPA = em
					.find(fr.pantheonsorbonne.ufr27.miage.jpa.Passager.class, p.getId());
			if (train.getListePassagers().contains(pJPA)) {
				pJPA.setDepart(aJPA);
				dao.removePassager(train, pJPA);
			}
		}
		em.getTransaction().commit();
	}

	protected void monterListPassager(List<Passager> listPassager, fr.pantheonsorbonne.ufr27.miage.jpa.Train train) {
		for (Passager p : listPassager) {
			em.getTransaction().begin();
			fr.pantheonsorbonne.ufr27.miage.jpa.Passager pJPA = em
					.find(fr.pantheonsorbonne.ufr27.miage.jpa.Passager.class, p.getId());
			if (!train.getListePassagers().contains(pJPA)) {
				dao.addPassager(train, pJPA);
			}
			em.getTransaction().commit();
		}
	}

	protected HeureDePassage verifIfExistArretNow(int trainId) {
		return HeureDePassageMapper.heureDePassageDTOMapper(hdpDAO.getHdpByTrainAndDateNow(trainId));
	}

}
