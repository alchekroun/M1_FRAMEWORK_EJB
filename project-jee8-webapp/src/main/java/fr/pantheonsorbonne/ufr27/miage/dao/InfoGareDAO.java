package fr.pantheonsorbonne.ufr27.miage.dao;

import java.util.List;
import java.util.NoSuchElementException;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import fr.pantheonsorbonne.ufr27.miage.jpa.InfoGare;

public class InfoGareDAO {

	@Inject
	EntityManager em;

	public InfoGare getInfoGareFromId(int infoGareId) {
		return em.find(InfoGare.class, infoGareId);
	}

	public List<InfoGare> getAllInfoGare() {
		return em.createNamedQuery("getAllInfoGare").getResultList();
	}

	public boolean isInfoGareCreated(int infoGareId) {

		InfoGare i = em.find(InfoGare.class, infoGareId);
		if (i == null) {
			throw new NoSuchElementException("No InfoGare");
		}
		return i.isCreated();

	}

}
