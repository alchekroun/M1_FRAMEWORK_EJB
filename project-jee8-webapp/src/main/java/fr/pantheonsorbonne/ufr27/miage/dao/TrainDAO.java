package fr.pantheonsorbonne.ufr27.miage.dao;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import fr.pantheonsorbonne.ufr27.miage.mapper.TrainMapper;
import fr.pantheonsorbonne.ufr27.miage.model.jaxb.Train;

public class TrainDAO {

	@Inject
	EntityManager em;

	public Train getTrainFromId(int trainId) {
		fr.pantheonsorbonne.ufr27.miage.jpa.Train train = em.find(fr.pantheonsorbonne.ufr27.miage.jpa.Train.class,
				trainId);

		Train trainDTO = TrainMapper.trainDTOMapper(train);
		return trainDTO;
	}

}
