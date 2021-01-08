package fr.pantheonsorbonne.ufr27.miage.service;

import java.util.List;

import fr.pantheonsorbonne.ufr27.miage.exception.CantCreateException;
import fr.pantheonsorbonne.ufr27.miage.exception.CantUpdateException;
import fr.pantheonsorbonne.ufr27.miage.exception.EmptyListException;
import fr.pantheonsorbonne.ufr27.miage.exception.NoSuchArretException;
import fr.pantheonsorbonne.ufr27.miage.exception.NoSuchHdpException;
import fr.pantheonsorbonne.ufr27.miage.exception.NoSuchTrainException;
import fr.pantheonsorbonne.ufr27.miage.model.jaxb.Perturbation;
import fr.pantheonsorbonne.ufr27.miage.model.jaxb.Train;

public interface TrainService {

	// C
	public int createTrain(Train trainDTO) throws CantCreateException;

	// R
	public Train getTrainFromId(int trainId) throws NoSuchTrainException;

	// U
	public void deleteTrain(int trainId) throws NoSuchTrainException;

	// D
	public void updateTrain(Train train) throws NoSuchTrainException, CantUpdateException;

	public List<Train> getAllTrain() throws EmptyListException;

	public void removeArret(int trainId, int arretId) throws NoSuchTrainException, NoSuchArretException;

	void addArret(int trainId, int arretId, String passage, boolean desservi, boolean terminus)
			throws NoSuchTrainException, NoSuchArretException;

	void changeParameterDesservi(int trainId, int arretId, boolean newDesservi)
			throws NoSuchTrainException, NoSuchArretException, NoSuchHdpException;

	public void createPerturbation(Perturbation perturbation) throws NoSuchTrainException;
}
