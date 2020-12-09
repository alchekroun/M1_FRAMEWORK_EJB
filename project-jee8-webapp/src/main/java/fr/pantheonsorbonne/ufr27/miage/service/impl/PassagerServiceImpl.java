package fr.pantheonsorbonne.ufr27.miage.service.impl;

import java.util.List;

import fr.pantheonsorbonne.ufr27.miage.exception.CantCreateException;
import fr.pantheonsorbonne.ufr27.miage.exception.EmptyListException;
import fr.pantheonsorbonne.ufr27.miage.exception.NoSuchPassagerException;
import fr.pantheonsorbonne.ufr27.miage.exception.NoSuchTrainException;
import fr.pantheonsorbonne.ufr27.miage.model.jaxb.Passager;
import fr.pantheonsorbonne.ufr27.miage.service.PassagerService;

public class PassagerServiceImpl implements PassagerService {

	@Override
	public int createPassager(Passager passagerDTO) throws CantCreateException {
		try {
			return 0;
		}  catch (org.eclipse.persistence.exceptions.DatabaseException e) {
			throw new CantCreateException();
		}
	}

	@Override
	public Passager getPassagerFromId(int passagerId) throws NoSuchPassagerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Passager> getAllPassager() throws EmptyListException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deletePassager(int passagerId) throws NoSuchPassagerException {
		// TODO Auto-generated method stub

	}

	@Override
	public List<Passager> getAllPassagerByTrain(int trainId) throws NoSuchTrainException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updatePassager(Passager passager) throws NoSuchPassagerException {
		// TODO Auto-generated method stub

	}

}
