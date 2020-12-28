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

	// C
	public int createArret(Arret arretDTO) throws CantCreateException;

	// R
	public Arret getArretFromId(int arretId) throws NoSuchArretException;

	// U
	public void updateArret(Arret arretUpdate) throws NoSuchArretException, CantUpdateException;

	// D
	public void deleteArret(int arretId) throws NoSuchArretException, CantDeleteException;

	public List<Arret> getAllArret() throws EmptyListException;

	public List<Arret> getAllArretByTrain(int trainId) throws NoSuchTrainException;

}
