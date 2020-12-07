package fr.pantheonsorbonne.ufr27.miage.mapper;

import java.util.ArrayList;
import java.util.List;

import fr.pantheonsorbonne.ufr27.miage.model.jaxb.Perturbation;
import fr.pantheonsorbonne.ufr27.miage.model.jaxb.ObjectFactory;

public class PertubationMapper {
	
	public static Perturbation perturbationDTOMapper(fr.pantheonsorbonne.ufr27.miage.jpa.Perturbation perturbation) {

		return new ObjectFactory().createPerturbation();
	}

	public static List<Perturbation> perturbationAllDTOMapper(List<fr.pantheonsorbonne.ufr27.miage.jpa.Perturbation> listePerturbations) {
		List<Perturbation> ListePerturbations = new ArrayList<Perturbation>();
		for (fr.pantheonsorbonne.ufr27.miage.jpa.Perturbation perturbation : listePerturbations) {
			ListePerturbations.add(perturbationDTOMapper(perturbation));
		}
		return ListePerturbations;
	}
}
