package fr.pantheonsorbonne.ufr27.miage.dao;

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
}
