package fr.pantheonsorbonne.ufr27.miage.service.impl;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import fr.pantheonsorbonne.ufr27.miage.dao.ArretDAO;
import fr.pantheonsorbonne.ufr27.miage.exception.NoSuchArretException;
import fr.pantheonsorbonne.ufr27.miage.mapper.ArretMapper;
import fr.pantheonsorbonne.ufr27.miage.model.jaxb.Arret;
import fr.pantheonsorbonne.ufr27.miage.service.ArretService;

public class ArretServiceImpl implements ArretService {

	@Inject
	EntityManager em;

	@Inject
	ArretDAO dao;

	@Override
	public int createArret(Arret arretDTO) {
		em.getTransaction().begin();

		fr.pantheonsorbonne.ufr27.miage.jpa.Arret arret = new fr.pantheonsorbonne.ufr27.miage.jpa.Arret();

		arret.setNom(arretDTO.getNomArret());

		em.persist(arret);
		em.getTransaction().commit();

		return arret.getId();
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
	public List<Arret> getAllArret() throws NoSuchArretException {
		List<fr.pantheonsorbonne.ufr27.miage.jpa.Arret> listeArrets = dao.getAllArret();
		if (listeArrets == null) {
			throw new NoSuchArretException();
		}
		return ArretMapper.arretAllDTOMapper(listeArrets);
	}

	@Override
	public void deleteArret(int arretId) throws NoSuchArretException {
		// Redondance pour vérifier que le arret existe bien
		fr.pantheonsorbonne.ufr27.miage.jpa.Arret arret = dao.getArretFromId(arretId);
		if (arret == null) {
			throw new NoSuchArretException();
		}
		dao.deleteArret(arret.getId());

	}

}
