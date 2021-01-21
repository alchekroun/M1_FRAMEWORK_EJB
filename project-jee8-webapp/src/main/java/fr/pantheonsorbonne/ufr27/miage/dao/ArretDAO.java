package fr.pantheonsorbonne.ufr27.miage.dao;

import java.util.List;
import java.util.NoSuchElementException;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import fr.pantheonsorbonne.ufr27.miage.jpa.Arret;

public class ArretDAO {
	@Inject
	EntityManager em;

	@Inject
	HeureDePassageDAO hdpDAO;

	public Arret getArretFromId(int arretId) {
		return em.find(Arret.class, arretId);
	}

	public Arret updateArret(Arret arretOriginal, fr.pantheonsorbonne.ufr27.miage.model.jaxb.Arret arretUpdate) {
		arretOriginal.setNom(arretUpdate.getNom());
		return arretOriginal;
	}

	public void deleteArret(Arret arret) {
		if (!arret.getListeHeureDePassage().isEmpty()) {
			hdpDAO.deleteHeureDePassageByArret(arret);
		}
		em.remove(arret);
	}

	public List<Arret> getAllArret() {
		return em.createNamedQuery("getAllArret").getResultList();
	}

	public List<Arret> getAllArretByTrain(int trainId) {
		return em.createNamedQuery("findArretByTrain").setParameter("id", trainId).getResultList();
	}

	public boolean isArretCreated(int ArretId) {

		Arret a = em.find(Arret.class, ArretId);
		if (a == null) {
			throw new NoSuchElementException("No Arret");
		}
		return a.isCreated();

	}

}
