package fr.pantheonsorbonne.ufr27.miage.service.impl;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import fr.pantheonsorbonne.ufr27.miage.dao.TrainDAO;
import fr.pantheonsorbonne.ufr27.miage.exception.NoSuchTrainException;
import fr.pantheonsorbonne.ufr27.miage.mapper.TrainMapper;
import fr.pantheonsorbonne.ufr27.miage.model.jaxb.Train;

import fr.pantheonsorbonne.ufr27.miage.service.TrainService;

public class TrainServiceImpl implements TrainService {

	@Inject
	EntityManager em;

	@Inject
	TrainDAO dao;

	private static Date getDate(LocalDateTime adate) {

		Date out = Date.from(adate.atZone(ZoneId.systemDefault()).toInstant());
		return out;
	}

	@Override
	public int createTrain(Train trainDTO) {
		em.getTransaction().begin();

		fr.pantheonsorbonne.ufr27.miage.jpa.Train train = new fr.pantheonsorbonne.ufr27.miage.jpa.Train();

		train.setNomTrain(trainDTO.getNomTrain());
		// train.setDirection(trainDTO.getDirection());
		// train.setDirectionType(trainDTO.getDirectionType());
		train.setNumeroTrain(trainDTO.getNumeroTrain());
		train.setReseau(trainDTO.getReseau());
		train.setBaseDepartTemps(getDate(trainDTO.getBaseDepartTemps()));
		train.setBaseArriveeTemps(getDate(trainDTO.getBaseArriveeTemps()));
		train.setReelDepartTemps(getDate(trainDTO.getReelDepartTemps()));
		train.setReelArriveeTemps(getDate(trainDTO.getReelArriveeTemps()));

		em.persist(train);
		em.getTransaction().commit();

		return train.getId();
	}

	@Override
	public void addArret(int trainId, int arretId) throws NoSuchTrainException {
		em.getTransaction().begin();
		fr.pantheonsorbonne.ufr27.miage.jpa.Train train = dao.getTrainFromId(trainId);
		if (train == null) {
			throw new NoSuchTrainException();
		}

		dao.addArret(train, arretId);

		em.persist(train);
		em.getTransaction().commit();
	}

	@Override
	public Train getTrainFromId(int trainId) throws NoSuchTrainException {
		fr.pantheonsorbonne.ufr27.miage.jpa.Train train = dao.getTrainFromId(trainId);
		if (train == null) {
			throw new NoSuchTrainException();
		}
		return TrainMapper.trainDTOMapper(train);
	}

	@Override
	public List<Train> getAllTrain() throws NoSuchTrainException {
		List<fr.pantheonsorbonne.ufr27.miage.jpa.Train> listeTrains = dao.getAllTrain();
		if (listeTrains == null) {
			throw new NoSuchTrainException();
		}
		return TrainMapper.trainAllDTOMapper(listeTrains);
	}

	@Override
	public void deleteTrain(int trainId) throws NoSuchTrainException {
		// Redondance pour vérifier que le train existe bien
		fr.pantheonsorbonne.ufr27.miage.jpa.Train train = dao.getTrainFromId(trainId);
		if (train == null) {
			throw new NoSuchTrainException();
		}
		dao.deleteTrain(train.getId());
	}

}
