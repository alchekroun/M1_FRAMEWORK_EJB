package fr.pantheonsorbonne.ufr27.miage.service.impl;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import fr.pantheonsorbonne.ufr27.miage.dao.ArretDAO;
import fr.pantheonsorbonne.ufr27.miage.dao.TrainDAO;
import fr.pantheonsorbonne.ufr27.miage.exception.CantCreateException;
import fr.pantheonsorbonne.ufr27.miage.exception.CantDeleteException;
import fr.pantheonsorbonne.ufr27.miage.exception.CantUpdateException;
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
			fr.pantheonsorbonne.ufr27.miage.jpa.InfoGare infoGare = new fr.pantheonsorbonne.ufr27.miage.jpa.InfoGare();
			arret.setNom(arretDTO.getNom());
			infoGare.setLocalisation(arret);
			em.persist(infoGare);
			em.persist(arret);
			em.getTransaction().commit();

			return arret.getId();
		} catch (org.eclipse.persistence.exceptions.DatabaseException e) {
			em.getTransaction().rollback();
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
	public void updateArret(Arret arretUpdate) throws NoSuchArretException, CantUpdateException {
		em.getTransaction().begin();
		try {
			fr.pantheonsorbonne.ufr27.miage.jpa.Arret arretOriginal = dao.getArretFromId(arretUpdate.getId());
			arretOriginal.setNom(arretUpdate.getNom());

			em.merge(arretOriginal);
			em.getTransaction().commit();
		} catch (org.eclipse.persistence.exceptions.DatabaseException e) {
			em.getTransaction().rollback();
			throw new CantUpdateException();
		}
	}

	@Override
	public void deleteArret(int arretId) throws NoSuchArretException, CantDeleteException {
		em.getTransaction().begin();
		fr.pantheonsorbonne.ufr27.miage.jpa.Arret arret = dao.getArretFromId(arretId);
		if (arret != null) {
			if (arret.getTrainsArrivants().isEmpty()) {
				dao.deleteArret(arret);

				em.getTransaction().commit();
			} else {
				throw new CantDeleteException("L\'arret est la destination d\'un train");
			}
		} else {
			throw new NoSuchArretException();
		}

	}

	@Override
	public List<Arret> getAllArretByTrain(int trainId) throws NoSuchTrainException {
		fr.pantheonsorbonne.ufr27.miage.jpa.Train train = daoTrain.getTrainFromId(trainId);
		if (train == null) {
			throw new NoSuchTrainException();
		}
		return ArretMapper.arretAllDTOMapper(dao.getAllArretByTrain(trainId));
	}

}
