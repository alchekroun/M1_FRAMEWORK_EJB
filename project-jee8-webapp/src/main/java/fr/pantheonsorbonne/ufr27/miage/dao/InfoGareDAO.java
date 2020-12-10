package fr.pantheonsorbonne.ufr27.miage.dao;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import fr.pantheonsorbonne.ufr27.miage.jpa.InfoGare;

public class InfoGareDAO {

	@Inject
	EntityManager em;

	public InfoGare getInfoGareFromId(int infoGareId) {
		fr.pantheonsorbonne.ufr27.miage.jpa.InfoGare infoGare = em
				.find(fr.pantheonsorbonne.ufr27.miage.jpa.InfoGare.class, infoGareId);
		return infoGare;
	}

	public List<InfoGare> getAllInfoGare() {
		return em.createNamedQuery("getAllInfoGare").getResultList();
	}

	public void deleteInfoGare(int infoGareId) {
		em.remove(em.find(InfoGare.class, infoGareId));
	}

}
