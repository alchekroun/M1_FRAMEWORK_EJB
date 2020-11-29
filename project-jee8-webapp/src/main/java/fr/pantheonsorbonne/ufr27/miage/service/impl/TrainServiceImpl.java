package fr.pantheonsorbonne.ufr27.miage.service.impl;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import fr.pantheonsorbonne.ufr27.miage.model.jaxb.Train;
import fr.pantheonsorbonne.ufr27.miage.service.TrainService;

public class TrainServiceImpl implements TrainService{

	@Inject
	EntityManager em;

	@Override
	public int createTrain(Train trainDTO) {
		em.getTransaction().begin();
		
		Train train=new Train();
		
		train.setNomTrain(trainDTO.getNomTrain());
		train.setDirection(trainDTO.getDirection());
		train.setDirectionType(trainDTO.getDirectionType());
		train.setNumeroTrain(trainDTO.getNumeroTrain());
		train.setReseau(trainDTO.getReseau());
		train.setBaseDepartTemps(trainDTO.getBaseDepartTemps());
		train.setBaseArriveeTemps(trainDTO.getBaseArriveeTemps());
		train.setReelDepartTemps(trainDTO.getReelDepartTemps());
		train.setReelArriveeTemps(trainDTO.getReelArriveeTemps());
		
		
		em.persist(train);
		em.getTransaction().commit();
		
		return train.getIdTrain();
	}

	

}
