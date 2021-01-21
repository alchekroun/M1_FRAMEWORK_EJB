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

	/**
	 * Méthode permettant de créer un train dans la base donnée.
	 * 
	 * @param trainDTO
	 * @return id du train
	 */
	public int createTrain(Train trainDTO) throws CantCreateException;

	/**
	 * Méthode permettant de récupérer un train via son Id.
	 * 
	 * @param trainId
	 * @return train
	 */
	public Train getTrainFromId(int trainId) throws NoSuchTrainException;

	/**
	 * Méthode permettant de supprimer un train de la base de donnée.
	 * 
	 * @param trainId
	 */
	public void deleteTrain(int trainId) throws NoSuchTrainException;

	/**
	 * Méthode permettant de mettre à jour un train dans la base de donnée.
	 * 
	 * @param train
	 */
	public void updateTrain(Train train) throws NoSuchTrainException, CantUpdateException;

	/**
	 * Méthode permettant de récupérer tous les trains de la base de donnée.
	 * 
	 * @return
	 */
	public List<Train> getAllTrain() throws EmptyListException;

	/**
	 * Méthode permettant de retirer un arret de l'itinéraire d'un train.
	 * 
	 * @param trainId
	 * @param arretId
	 */
	public void removeArret(int trainId, int arretId) throws NoSuchTrainException, NoSuchArretException;

	/**
	 * Méthode permettant d'ajouter un arret sur l'itinéraire d'un train.
	 * 
	 * @param trainId
	 * @param arretId
	 * @param passage
	 * @param desservi
	 * @param terminus
	 */
	void addArret(int trainId, int arretId, String passage, boolean desservi, boolean terminus)
			throws NoSuchTrainException, NoSuchArretException;

	/**
	 * Méthode permettant de changer si un arret est ou n'est pas desservi.
	 * 
	 * @param trainId
	 * @param arretId
	 * @param newDesservi
	 */
	void changeParameterDesservi(int trainId, int arretId, boolean newDesservi)
			throws NoSuchTrainException, NoSuchArretException, NoSuchHdpException;

	/**
	 * Méthode permettant de créer une pertubation dans la base de donnée.
	 * 
	 * @param perturbation
	 */
	public void createPerturbation(Perturbation perturbation) throws NoSuchTrainException;

	/**
	 * Méthode permettant de mettre en marche un train.
	 * 
	 * @param train
	 * @return 1 = OK ; -1 = KO
	 */
	public int enMarche(Train train) throws NoSuchTrainException;

}
