package fr.pantheonsorbonne.ufr27.miage.dao;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import fr.pantheonsorbonne.ufr27.miage.model.jaxb.Arret;
import fr.pantheonsorbonne.ufr27.miage.mapper.ArretMapper;

public class ArretDAO {
	@Inject
	EntityManager em;

	public Arret getArretFromId(int arretId) {
		fr.pantheonsorbonne.ufr27.miage.jpa.Arret arret = em.find(fr.pantheonsorbonne.ufr27.miage.jpa.Arret.class,
				arretId);

		Arret arretDTO = ArretMapper.arretDTOMapper(arret);
		return arretDTO;
	}

	public List<Arret> getAllArret() {
		return em.createNamedQuery("getAllArret").getResultList();
	}

	public void deleteArret(int arretId) {
		em.createNamedQuery("deleteArret").setParameter("id", arretId).executeUpdate();
	}

	public List<Arret> getAllTrainByArret(int arretId) {
		// TODO Faire la NAMED QUERY
		return em.createNamedQuery("getAllTrainByArret").setParameter("id", arretId).getResultList();
	}

}
