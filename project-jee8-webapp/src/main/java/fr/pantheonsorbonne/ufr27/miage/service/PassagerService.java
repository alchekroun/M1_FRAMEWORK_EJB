package fr.pantheonsorbonne.ufr27.miage.service;

import java.util.List;

import fr.pantheonsorbonne.ufr27.miage.exception.CantCreateException;
import fr.pantheonsorbonne.ufr27.miage.exception.CantUpdateException;
import fr.pantheonsorbonne.ufr27.miage.exception.EmptyListException;
import fr.pantheonsorbonne.ufr27.miage.exception.NoSuchPassagerException;
import fr.pantheonsorbonne.ufr27.miage.exception.NoSuchTrainException;
import fr.pantheonsorbonne.ufr27.miage.model.jaxb.Passager;

public interface PassagerService {

	/**
	 * Méthode permettant de créer un passager dans la base de données.
	 * 
	 * @param passagerDTO
	 * @return id du passager
	 */
	public int createPassager(Passager passagerDTO) throws CantCreateException;

	/**
	 * Méthode permettant de récupérer un passager via son Id.
	 * 
	 * @param passagerId
	 * @return passager
	 */
	public Passager getPassagerFromId(int passagerId) throws NoSuchPassagerException;

	/**
	 * Méthode permettant de mettre à jour un passager de la base de données.
	 * 
	 * @param passagerUpdate
	 */
	public void updatePassager(Passager passagerUpdate) throws NoSuchPassagerException, CantUpdateException;

	/**
	 * Méthode permettant de supprimer un passager de la base de données.
	 * 
	 * @param passagerId
	 */
	public void deletePassager(int passagerId) throws NoSuchPassagerException;

	/**
	 * Méthode permettant de récupérer tous les passagers de la base de données.
	 * 
	 * @return Liste de tous les passager de la base de données.
	 */
	public List<Passager> getAllPassager() throws EmptyListException;

	/**
	 * Méthode permettant de récupérer tous les passagers associés à un train.
	 * 
	 * @param trainId
	 * @return Liste de tous les passager ayant pour train trainId
	 */
	public List<Passager> getAllPassagerByTrain(int trainId) throws NoSuchTrainException;

}
