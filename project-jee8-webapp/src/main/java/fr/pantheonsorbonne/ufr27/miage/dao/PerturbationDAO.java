package fr.pantheonsorbonne.ufr27.miage.dao;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import fr.pantheonsorbonne.ufr27.miage.jpa.Perturbation;
import fr.pantheonsorbonne.ufr27.miage.jpa.Train;

public class PerturbationDAO {

	@Inject
	EntityManager em;

	public Perturbation createPerturbation(fr.pantheonsorbonne.ufr27.miage.model.jaxb.Perturbation perturbationDTO) {
		Perturbation perturbation = new Perturbation();

		perturbation.setMotif(perturbationDTO.getMotif());
		perturbation.setTrain(em.find(Train.class, perturbation.getTrain().getId()));
		perturbation.setDureeEnPlus(perturbationDTO.getDureeEnPlus());

		return null;
	}

	public Perturbation getPerturbationFromId(int perturbationId) {
		return em.find(Perturbation.class, perturbationId);
	}

	public Perturbation updatePerturbation(Perturbation perturbationOriginal,
			fr.pantheonsorbonne.ufr27.miage.model.jaxb.Perturbation perturbationUpdate) {
		perturbationOriginal.setMotif(perturbationUpdate.getMotif());
		perturbationOriginal.setDureeEnPlus(perturbationUpdate.getDureeEnPlus());
		perturbationOriginal.setTrain(em.find(Train.class, perturbationUpdate.getTrain().getId()));

		return perturbationOriginal;
	}

	public List<Perturbation> getAllPerturbation() {
		return em.createNamedQuery("getAllPerturbation").getResultList();
	}

	public void deletePerturbation(Perturbation perturbation) {
		em.remove(perturbation);
	}

}
