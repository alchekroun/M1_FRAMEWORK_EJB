package fr.pantheonsorbonne.ufr27.miage.mapper;

import java.util.ArrayList;
import java.util.List;

import fr.pantheonsorbonne.ufr27.miage.model.jaxb.ObjectFactory;
import fr.pantheonsorbonne.ufr27.miage.model.jaxb.Passager;
import fr.pantheonsorbonne.ufr27.miage.model.jaxb.Train;

public class TrainMapper {

	/**
	 * Mappage d'un train
	 * 
	 * @param train
	 * @return Train
	 */
	public static Train trainDTOMapper(fr.pantheonsorbonne.ufr27.miage.jpa.Train train) {
		Train trainDTO = new ObjectFactory().createTrain();

		trainDTO.setId(train.getId());
		trainDTO.setNom(train.getNom());
		trainDTO.setReseau(train.getReseau());
		trainDTO.setStatut(train.getStatut());
		trainDTO.setNumeroTrain(train.getNumero());
		for (Passager p : PassagerMapper.passagerAllDTOMapper(train.getListePassagers())) {
			trainDTO.getListePassagers().add(p);
		}

		return trainDTO;
	}

	/**
	 * Mappage de tous les trains
	 * 
	 * @param listeTrains
	 * @return List<Train>
	 */
	public static List<Train> trainAllDTOMapper(List<fr.pantheonsorbonne.ufr27.miage.jpa.Train> listeTrains) {
		List<Train> ListeTrains = new ArrayList<Train>();
		for (fr.pantheonsorbonne.ufr27.miage.jpa.Train train : listeTrains) {
			ListeTrains.add(trainDTOMapper(train));
		}
		return ListeTrains;
	}

}