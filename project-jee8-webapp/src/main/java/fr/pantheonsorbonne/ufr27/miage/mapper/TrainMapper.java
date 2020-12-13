package fr.pantheonsorbonne.ufr27.miage.mapper;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import fr.pantheonsorbonne.ufr27.miage.model.jaxb.HeureDePassage;
import fr.pantheonsorbonne.ufr27.miage.model.jaxb.ObjectFactory;
import fr.pantheonsorbonne.ufr27.miage.model.jaxb.Passager;
import fr.pantheonsorbonne.ufr27.miage.model.jaxb.Train;

public class TrainMapper {

	public static Train trainDTOMapper(fr.pantheonsorbonne.ufr27.miage.jpa.Train train) {
		Train trainDTO = new ObjectFactory().createTrain();

		trainDTO.setId(train.getId());

		trainDTO.setNom(train.getNomTrain());

		trainDTO.setDirectionType(train.getDirectionType());

		trainDTO.setReseau(train.getReseau());

		trainDTO.setStatut(train.getStatut());

		trainDTO.setDirection(ArretMapper.arretDTOMapper(train.getDirection()));

		for (HeureDePassage hdp : HeureDePassageMapper.heureDePassageAllDTOMapper(train.getListeHeureDePassage())) {
			trainDTO.getListeHeureDePassages().add(hdp);
		}

		for (Passager p : PassagerMapper.passagerAllDTOMapper(train.getListePassagers())) {
			trainDTO.getListePassagers().add(p);
		}

		trainDTO.setReelArriveeTemps(
				LocalDateTime.ofInstant(train.getReelArriveeTemps().toInstant(), ZoneId.systemDefault()));
		trainDTO.setReelDepartTemps(
				LocalDateTime.ofInstant(train.getReelDepartTemps().toInstant(), ZoneId.systemDefault()));
		trainDTO.setBaseDepartTemps(
				LocalDateTime.ofInstant(train.getBaseDepartTemps().toInstant(), ZoneId.systemDefault()));
		trainDTO.setBaseArriveeTemps(
				LocalDateTime.ofInstant(train.getBaseArriveeTemps().toInstant(), ZoneId.systemDefault()));

		return trainDTO;
	}

	public static List<Train> trainAllDTOMapper(List<fr.pantheonsorbonne.ufr27.miage.jpa.Train> listeTrains) {
		List<Train> ListeTrains = new ArrayList<Train>();
		for (fr.pantheonsorbonne.ufr27.miage.jpa.Train train : listeTrains) {
			ListeTrains.add(trainDTOMapper(train));
		}
		return ListeTrains;
	}

}