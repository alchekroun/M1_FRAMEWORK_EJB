package fr.pantheonsorbonne.ufr27.miage.dao;

import java.util.List;
import java.util.NoSuchElementException;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import fr.pantheonsorbonne.ufr27.miage.jpa.Arret;

public class ArretDAO {
	@Inject
	EntityManager em;

	public Arret getArretFromId(int arretId) {
		fr.pantheonsorbonne.ufr27.miage.jpa.Arret arret = em.find(fr.pantheonsorbonne.ufr27.miage.jpa.Arret.class,
				arretId);
		return arret;
	}

	public List<Arret> getAllArret() {
		return em.createNamedQuery("getAllArret").getResultList();
	}

	public void deleteArret(int arretId) {
		em.remove(em.find(Arret.class, arretId));
	}

	public List<Arret> getAllArretByTrain(int trainId) {
		return em.createNamedQuery("findAllArretByTrain").setParameter("id", trainId).getResultList();
	}

	public boolean isArretCreated(int ArretId) {

		Arret a = em.find(Arret.class, ArretId);
		if (a == null) {
			throw new NoSuchElementException("No Arret");
		}
		return a.isCreated();

	}

}
