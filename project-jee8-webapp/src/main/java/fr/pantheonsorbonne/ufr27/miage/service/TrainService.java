package fr.pantheonsorbonne.ufr27.miage.service;

import java.util.List;

import fr.pantheonsorbonne.ufr27.miage.exception.NoSuchTrainException;
import fr.pantheonsorbonne.ufr27.miage.model.jaxb.Train;

public interface TrainService {
	public int createTrain(Train trainDTO);

	public void addArret(int trainId, int arretId) throws NoSuchTrainException;

	public Train getTrainFromId(int trainId) throws NoSuchTrainException;

	public List<Train> getAllTrain() throws NoSuchTrainException;

	public void deleteTrain(int trainId) throws NoSuchTrainException;
}
