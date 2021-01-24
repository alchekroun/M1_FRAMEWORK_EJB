package fr.pantheonsorbonne.ufr27.miage.dao;

import java.time.LocalDateTime;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import fr.pantheonsorbonne.ufr27.miage.jpa.HeureDePassage;
import fr.pantheonsorbonne.ufr27.miage.jpa.Perturbation;
import fr.pantheonsorbonne.ufr27.miage.jpa.Train;

public class PerturbationDAO {

	@Inject
	EntityManager em;

	@Inject
	HeureDePassageDAO hdpDAO;

	/**
	 * Méthode permettant de créer un passager dans la base de données
	 * 
	 * @param perturbationDTO
	 * @return Perturbation
	 */
	public Perturbation createPerturbation(fr.pantheonsorbonne.ufr27.miage.model.jaxb.Perturbation perturbationDTO) {
		Perturbation perturbation = new Perturbation();

		perturbation.setMotif(perturbationDTO.getMotif());
		perturbation.setTrain(em.find(Train.class, perturbationDTO.getTrain().getId()));
		perturbation.setDureeEnPlus(perturbationDTO.getDureeEnPlus());

		em.persist(perturbation);

		return perturbation;
	}

	/**
	 * Méthode permettant de récupérer une perturbation à partir de son id
	 * 
	 * @param perturbationId
	 * @return Perturbation
	 */
	public Perturbation getPerturbationFromId(int perturbationId) {
		return em.find(Perturbation.class, perturbationId);
	}

	/**
	 * Méthode permettant de modifier une perturbation
	 * 
	 * @param perturbationOriginal
	 * @param perturbationUpdate
	 * @return Perturbation
	 */
	public Perturbation updatePerturbation(Perturbation perturbationOriginal,
			fr.pantheonsorbonne.ufr27.miage.model.jaxb.Perturbation perturbationUpdate) {
		perturbationOriginal.setMotif(perturbationUpdate.getMotif());
		perturbationOriginal.setDureeEnPlus(perturbationUpdate.getDureeEnPlus());
		perturbationOriginal.setTrain(em.find(Train.class, perturbationUpdate.getTrain().getId()));

		return perturbationOriginal;
	}

	/**
	 * Méthode permettant de retarder une heure de passage d'un train si une
	 * perturbation impacte le trafic
	 * 
	 * @param perturbation
	 */
	public void impacterTrafic(Perturbation perturbation) {
		List<HeureDePassage> listHdp = hdpDAO.findHdpByTrain(perturbation.getTrain().getId());
		for (HeureDePassage hdp : listHdp) {
			// ne va vérifier seulement que les hdp qui sont a suivre sur le chemin
			if (hdp.getReelArriveeTemps().isAfter(LocalDateTime.now())) {
				hdpDAO.retarderHdp(hdp, perturbation.getDureeEnPlus());
			}
		}

	}
	
	

	/**
	 * Méthode permettant de récupérer toutes les perturbations dans la base de
	 * données
	 * 
	 * @return List<Perturbation>
	 */
	public List<Perturbation> getAllPerturbation() {
		return em.createNamedQuery("getAllPerturbation").getResultList();
	}

	/**
	 * Méthode permettant de récupérer toutes les perturbations d'un train
	 * 
	 * @param t
	 * @return List<Perturbation>
	 */
	public List<Perturbation> getPerturbationByTrain(Train t) {
		return em.createNamedQuery("getPerturbationByTrain").setParameter("idTrain", t.getId()).getResultList();
	}

	/**
	 * Méthode permettant de supprimer une perturbation
	 * 
	 * @param perturbation
	 */
	public void deletePerturbation(Perturbation perturbation) {
		em.remove(perturbation);
	}

}
