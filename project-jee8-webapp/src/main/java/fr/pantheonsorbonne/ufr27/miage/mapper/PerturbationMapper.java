package fr.pantheonsorbonne.ufr27.miage.mapper;

import java.util.ArrayList;
import java.util.List;

import fr.pantheonsorbonne.ufr27.miage.model.jaxb.Perturbation;
import fr.pantheonsorbonne.ufr27.miage.model.jaxb.ObjectFactory;

public class PerturbationMapper {

	/**
	 * Mappage d'une perturbation
	 * 
	 * @param perturbation
	 * @return Perturbation
	 */
	public static Perturbation perturbationDTOMapper(fr.pantheonsorbonne.ufr27.miage.jpa.Perturbation perturbation) {
		Perturbation perturbationDTO = new ObjectFactory().createPerturbation();

		perturbationDTO.setId(perturbation.getId());
		perturbationDTO.setMotif(perturbation.getMotif());
		perturbationDTO.setTrain(TrainMapper.trainDTOMapper(perturbation.getTrain()));
		perturbationDTO.setDureeEnPlus(perturbation.getDureeEnPlus());

		return perturbationDTO;

	}

	/**
	 * Mappage de toutes les perturbations
	 * 
	 * @param listePerturbations
	 * @return List<Perturbation>
	 */
	public static List<Perturbation> perturbationAllDTOMapper(
			List<fr.pantheonsorbonne.ufr27.miage.jpa.Perturbation> listePerturbations) {
		List<Perturbation> ListePerturbations = new ArrayList<Perturbation>();
		for (fr.pantheonsorbonne.ufr27.miage.jpa.Perturbation perturbation : listePerturbations) {
			ListePerturbations.add(perturbationDTOMapper(perturbation));
		}
		return ListePerturbations;
	}
}
