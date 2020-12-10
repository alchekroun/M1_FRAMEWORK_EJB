package fr.pantheonsorbonne.ufr27.miage.dao;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import fr.pantheonsorbonne.ufr27.miage.mapper.PassagerMapper;
import fr.pantheonsorbonne.ufr27.miage.model.jaxb.Passager;

public class PassagerDAO {
	@Inject
	EntityManager em;

	public Passager getPassagerFromId(int passagerId) {
		fr.pantheonsorbonne.ufr27.miage.jpa.Passager passager = em
				.find(fr.pantheonsorbonne.ufr27.miage.jpa.Passager.class, passagerId);

		Passager passagerDTO = PassagerMapper.passagerDTOMapper(passager);
		return passagerDTO;
	}

	public void deletePassager(int passagerId) {
		em.remove(em.find(Passager.class, passagerId));
	}
}
