package fr.pantheonsorbonne.ufr27.miage.service.impl;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import fr.pantheonsorbonne.ufr27.miage.dao.ArretDAO;
import fr.pantheonsorbonne.ufr27.miage.dao.TrainDAO;
import fr.pantheonsorbonne.ufr27.miage.exception.CantCreateException;
import fr.pantheonsorbonne.ufr27.miage.exception.EmptyListException;
import fr.pantheonsorbonne.ufr27.miage.exception.NoSuchArretException;
import fr.pantheonsorbonne.ufr27.miage.exception.NoSuchTrainException;
import fr.pantheonsorbonne.ufr27.miage.mapper.ArretMapper;
import fr.pantheonsorbonne.ufr27.miage.model.jaxb.Arret;
import fr.pantheonsorbonne.ufr27.miage.service.ArretService;

public class ArretServiceImpl implements ArretService {

	@Inject
	EntityManager em;

	@Inject
	ArretDAO dao;

	@Inject
	TrainDAO daoTrain;

	@Override
	public int createArret(Arret arretDTO) throws CantCreateException {
		try {
			em.getTransaction().begin();

			fr.pantheonsorbonne.ufr27.miage.jpa.Arret arret = new fr.pantheonsorbonne.ufr27.miage.jpa.Arret();

			arret.setNom(arretDTO.getNom());

			em.persist(arret);
			em.getTransaction().commit();

			return arret.getId();
		} catch (org.eclipse.persistence.exceptions.DatabaseException e) {
			throw new CantCreateException();
		}
	}

	@Override
	public Arret getArretFromId(int arretId) throws NoSuchArretException {
		fr.pantheonsorbonne.ufr27.miage.jpa.Arret arret = dao.getArretFromId(arretId);
		if (arret == null) {
			throw new NoSuchArretException();
		}
		return ArretMapper.arretDTOMapper(arret);
	}

	@Override
	public List<Arret> getAllArret() throws EmptyListException {
		List<fr.pantheonsorbonne.ufr27.miage.jpa.Arret> listeArrets = dao.getAllArret();
		if (listeArrets == null) {
			throw new EmptyListException();
		}
		return ArretMapper.arretAllDTOMapper(listeArrets);
	}

	@Override
	public void deleteArret(int arretId) throws NoSuchArretException {
		em.getTransaction().begin();
		// Redondance pour vérifier que le arret existe bien
		fr.pantheonsorbonne.ufr27.miage.jpa.Arret arret = dao.getArretFromId(arretId);
		if (arret == null) {
			throw new NoSuchArretException();
		}
		dao.deleteArret(arret.getId());
		em.getTransaction().commit();

	}

	@Override
	public List<Arret> getAllArretByTrain(int trainId) throws NoSuchTrainException {
		fr.pantheonsorbonne.ufr27.miage.jpa.Train train = daoTrain.getTrainFromId(trainId);
		if (train == null) {
			throw new NoSuchTrainException();
		}
		return ArretMapper.arretAllDTOMapper(dao.getAllArretByTrain(trainId));
	}

	@Override
	public void updateArret(Arret arret) throws NoSuchArretException {
		// TODO Auto-generated method stub

	}

}
