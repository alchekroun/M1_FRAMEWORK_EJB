package fr.pantheonsorbonne.ufr27.miage.service;

import fr.pantheonsorbonne.ufr27.miage.model.jaxb.Arret;
import fr.pantheonsorbonne.ufr27.miage.model.jaxb.Train;

public interface TrainService {
	public int createTrain(Train trainDTO);

	public void addArret(Train trainDTO, Arret arretDTO);
}
