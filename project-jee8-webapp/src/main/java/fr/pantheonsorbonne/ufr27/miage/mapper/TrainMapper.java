package fr.pantheonsorbonne.ufr27.miage.mapper;

import java.time.LocalDateTime;
import java.time.ZoneId;

import fr.pantheonsorbonne.ufr27.miage.model.jaxb.ObjectFactory;
import fr.pantheonsorbonne.ufr27.miage.model.jaxb.Train;

public class TrainMapper {
	public static Train trainDTOMapper(fr.pantheonsorbonne.ufr27.miage.jpa.Train train) {
		Train trainDTO = new ObjectFactory().createTrain();

		trainDTO.setDirectionType(train.getDirectionType());
		trainDTO.setIdTrain(train.getId());

		trainDTO.setReelArriveeTemps(
				LocalDateTime.ofInstant(train.getReelArriveeTemps().toInstant(), ZoneId.systemDefault()));
		return trainDTO;
	}
}
