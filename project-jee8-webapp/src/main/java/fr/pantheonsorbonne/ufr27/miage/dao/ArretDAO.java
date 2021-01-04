package fr.pantheonsorbonne.ufr27.miage.dao;

import java.util.List;
import java.util.NoSuchElementException;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import fr.pantheonsorbonne.ufr27.miage.jpa.Arret;
import fr.pantheonsorbonne.ufr27.miage.jpa.InfoGare;

public class ArretDAO {
	@Inject
	EntityManager em;

	@Inject
	HeureDePassageDAO hdpDAO;

	public Arret getArretFromId(int arretId) {
		return em.find(Arret.class, arretId);
	}

	public List<Arret> getAllArret() {
		return em.createNamedQuery("getAllArret").getResultList();
	}

	public void deleteArret(Arret arret) {
		if (!arret.getListeHeureDePassage().isEmpty()) {
			hdpDAO.deleteHeureDePassageByArret(arret);
		}
		// On supprime l'infoGare associé car un infoGare n'existe qu'au travers de
		// l'arret
		em.remove(em.find(InfoGare.class, arret.getId()));
		em.remove(arret);
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
