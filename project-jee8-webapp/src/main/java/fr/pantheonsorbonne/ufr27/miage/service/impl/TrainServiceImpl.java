package fr.pantheonsorbonne.ufr27.miage.service.impl;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import fr.pantheonsorbonne.ufr27.miage.model.jaxb.Train;

import fr.pantheonsorbonne.ufr27.miage.service.TrainService;

public class TrainServiceImpl implements TrainService {

	@Inject
	EntityManager em;

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

}
