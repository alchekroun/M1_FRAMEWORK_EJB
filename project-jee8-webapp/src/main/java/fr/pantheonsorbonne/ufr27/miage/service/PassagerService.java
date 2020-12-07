package fr.pantheonsorbonne.ufr27.miage.service;

import java.util.List;

import fr.pantheonsorbonne.ufr27.miage.exception.EmptyListException;
import fr.pantheonsorbonne.ufr27.miage.exception.NoSuchPassagerException;
import fr.pantheonsorbonne.ufr27.miage.exception.NoSuchTrainException;
import fr.pantheonsorbonne.ufr27.miage.model.jaxb.Passager;

public interface PassagerService {

	// C
	public int createPassager(Passager passagerDTO);

	// U
	public void updatePassager(Passager passager) throws NoSuchPassagerException;

	// D
	public void deletePassager(int passagerId) throws NoSuchPassagerException;

	public Passager getPassagerFromId(int passagerId) throws NoSuchPassagerException;

	public List<Passager> getAllPassager() throws EmptyListException;

	public List<Passager> getAllPassagerByTrain(int trainId) throws NoSuchTrainException;

}
