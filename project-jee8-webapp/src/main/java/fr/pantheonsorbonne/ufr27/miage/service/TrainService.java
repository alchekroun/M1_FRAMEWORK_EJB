package fr.pantheonsorbonne.ufr27.miage.service;

import java.util.List;

import fr.pantheonsorbonne.ufr27.miage.exception.CantCreateException;
import fr.pantheonsorbonne.ufr27.miage.exception.EmptyListException;
import fr.pantheonsorbonne.ufr27.miage.exception.NoSuchTrainException;
import fr.pantheonsorbonne.ufr27.miage.model.jaxb.Train;

public interface TrainService {

	// C
	public int createTrain(Train trainDTO) throws CantCreateException;

	// U
	public void deleteTrain(int trainId) throws NoSuchTrainException;

	// D
	public void updateTrain(Train train) throws NoSuchTrainException;

	public Train getTrainFromId(int trainId) throws NoSuchTrainException;

	public List<Train> getAllTrain() throws EmptyListException;

	public void addArret(int trainId, int arretId) throws NoSuchTrainException;
}
