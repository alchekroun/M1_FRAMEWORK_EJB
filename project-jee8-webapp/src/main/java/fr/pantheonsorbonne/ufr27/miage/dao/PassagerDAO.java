package fr.pantheonsorbonne.ufr27.miage.dao;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import fr.pantheonsorbonne.ufr27.miage.jpa.Passager;

public class PassagerDAO {
	@Inject
	EntityManager em;

	public Passager getPassagerFromId(int passagerId) {
		fr.pantheonsorbonne.ufr27.miage.jpa.Passager passager = em
				.find(fr.pantheonsorbonne.ufr27.miage.jpa.Passager.class, passagerId);
		return passager;
	}

	public void deletePassager(int passagerId) {
		em.remove(em.find(Passager.class, passagerId));
	}

	public List<Passager> getAllPassager() {
		return em.createNamedQuery("getAllPassager").getResultList();
	}

	public List<Passager> getAllPassagerByTrain(int trainId) {
		return em.createNamedQuery("findAllPassagerByTrain").setParameter("id", trainId).getResultList();
	}
}
