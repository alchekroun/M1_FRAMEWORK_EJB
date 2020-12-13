package fr.pantheonsorbonne.ufr27.miage.service.impl;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import fr.pantheonsorbonne.ufr27.miage.dao.PassagerDAO;
import fr.pantheonsorbonne.ufr27.miage.dao.TrainDAO;
import fr.pantheonsorbonne.ufr27.miage.exception.CantCreateException;
import fr.pantheonsorbonne.ufr27.miage.exception.CantUpdateException;
import fr.pantheonsorbonne.ufr27.miage.exception.EmptyListException;
import fr.pantheonsorbonne.ufr27.miage.exception.NoSuchPassagerException;
import fr.pantheonsorbonne.ufr27.miage.exception.NoSuchTrainException;
import fr.pantheonsorbonne.ufr27.miage.mapper.PassagerMapper;
import fr.pantheonsorbonne.ufr27.miage.model.jaxb.Passager;
import fr.pantheonsorbonne.ufr27.miage.service.PassagerService;

public class PassagerServiceImpl implements PassagerService {

	@Inject
	EntityManager em;

	@Inject
	PassagerDAO dao;

	@Inject
	TrainDAO daoTrain;

	@Override
	public int createPassager(Passager passagerDTO) throws CantCreateException {
		try {
			em.getTransaction().begin();

			fr.pantheonsorbonne.ufr27.miage.jpa.Passager passager = new fr.pantheonsorbonne.ufr27.miage.jpa.Passager();

			passager.setDepart(
					em.find(fr.pantheonsorbonne.ufr27.miage.jpa.Arret.class, passagerDTO.getDepart().getId()));
			passager.setArrive(
					em.find(fr.pantheonsorbonne.ufr27.miage.jpa.Arret.class, passagerDTO.getArrive().getId()));
			passager.setNom(passagerDTO.getNom());

			em.persist(passager);
			em.getTransaction().commit();

			return passager.getId();
		} catch (org.eclipse.persistence.exceptions.DatabaseException e) {
			em.getTransaction().rollback();
			throw new CantCreateException();
		}
	}

	@Override
	public Passager getPassagerFromId(int passagerId) throws NoSuchPassagerException {
		fr.pantheonsorbonne.ufr27.miage.jpa.Passager passager = dao.getPassagerFromId(passagerId);
		if (passager == null) {
			throw new NoSuchPassagerException();
		}
		return PassagerMapper.passagerDTOMapper(passager);
	}

	@Override
	public List<Passager> getAllPassager() throws EmptyListException {
		List<fr.pantheonsorbonne.ufr27.miage.jpa.Passager> listePassagers = dao.getAllPassager();
		if (listePassagers == null) {
			throw new EmptyListException();
		}
		return PassagerMapper.passagerAllDTOMapper(listePassagers);
	}

	@Override
	public void deletePassager(int passagerId) throws NoSuchPassagerException {
		em.getTransaction().begin();
		// Redondance pour vérifier que le passager existe bien
		fr.pantheonsorbonne.ufr27.miage.jpa.Passager passager = dao.getPassagerFromId(passagerId);
		if (passager == null) {
			throw new NoSuchPassagerException();
		}
		dao.deletePassager(passager.getId());
		em.getTransaction().commit();

	}

	@Override
	public List<Passager> getAllPassagerByTrain(int trainId) throws NoSuchTrainException {
		fr.pantheonsorbonne.ufr27.miage.jpa.Train train = daoTrain.getTrainFromId(trainId);
		if (train == null) {
			throw new NoSuchTrainException();
		}
		return PassagerMapper.passagerAllDTOMapper(dao.getAllPassagerByTrain(trainId));
	}

	@Override
	public void updatePassager(Passager passagerUpdate) throws NoSuchPassagerException, CantUpdateException {
		em.getTransaction().begin();
		try {
			fr.pantheonsorbonne.ufr27.miage.jpa.Passager passagerOriginal = dao
					.getPassagerFromId(passagerUpdate.getId());
			passagerOriginal.setArrive(
					em.find(fr.pantheonsorbonne.ufr27.miage.jpa.Arret.class, passagerUpdate.getArrive().getId()));
			passagerOriginal.setDepart(
					em.find(fr.pantheonsorbonne.ufr27.miage.jpa.Arret.class, passagerUpdate.getDepart().getId()));
			passagerOriginal.setNom(passagerUpdate.getNom());
		} catch (org.eclipse.persistence.exceptions.DatabaseException e) {
			em.getTransaction().rollback();
			throw new CantUpdateException();
		}
	}

}
