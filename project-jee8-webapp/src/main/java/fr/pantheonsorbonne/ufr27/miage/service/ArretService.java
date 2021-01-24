package fr.pantheonsorbonne.ufr27.miage.service;

import java.util.List;

import fr.pantheonsorbonne.ufr27.miage.exception.CantCreateException;
import fr.pantheonsorbonne.ufr27.miage.exception.CantDeleteException;
import fr.pantheonsorbonne.ufr27.miage.exception.CantUpdateException;
import fr.pantheonsorbonne.ufr27.miage.exception.EmptyListException;
import fr.pantheonsorbonne.ufr27.miage.exception.NoSuchArretException;
import fr.pantheonsorbonne.ufr27.miage.exception.NoSuchTrainException;
import fr.pantheonsorbonne.ufr27.miage.model.jaxb.Arret;

public interface ArretService {

	/**
	 * Méthode permettant de créer un arret dans la base de données.
	 * 
	 * @param arretDTO
	 * @return id de l'arret
	 */
	public int createArret(Arret arretDTO) throws CantCreateException;

	/**
	 * Méthode permettant de récupérer un arret via son Id.
	 * 
	 * @param arretId
	 * @return Arret
	 */
	public Arret getArretFromId(int arretId) throws NoSuchArretException;

	/**
	 * Méthode permettant de mettre à jour un arret dans la base de données.
	 * 
	 * @param arretUpdate
	 */
	public void updateArret(Arret arretUpdate) throws NoSuchArretException, CantUpdateException;

	/**
	 * Méthode permettant de supprimer un arret de la base de données.
	 * 
	 * @param arretId
	 */
	public void deleteArret(int arretId) throws NoSuchArretException, CantDeleteException;

	/**
	 * Méthode permettant de récupérer tous les arrets de la base de données
	 * 
	 * @return
	 */
	public List<Arret> getAllArret() throws EmptyListException;

	/**
	 * Méthode permettant de récupéter tous les arrets qu'un train parcourt
	 * 
	 * @param trainId
	 * @return Liste contenant tous les arrets parcouru par un train
	 */
	public List<Arret> getAllArretByTrain(int trainId) throws NoSuchTrainException;

}
