package fr.pantheonsorbonne.ufr27.miage.mapper;

import java.util.ArrayList;
import java.util.List;

import fr.pantheonsorbonne.ufr27.miage.model.jaxb.ObjectFactory;
import fr.pantheonsorbonne.ufr27.miage.model.jaxb.HeureDePassage;

public class HeureDePassageMapper {
	public static HeureDePassage heureDePassageDTOMapper(
			fr.pantheonsorbonne.ufr27.miage.jpa.HeureDePassage heureDePassage) {
		HeureDePassage hdpDTO = new ObjectFactory().createHeureDePassage();

		hdpDTO.setArretId(heureDePassage.getArret().getId());
		hdpDTO.setPassage(heureDePassage.getPassage());
		hdpDTO.setTrainId(heureDePassage.getTrain().getId());

		return hdpDTO;
	}

	public static List<HeureDePassage> heureDePassageAllDTOMapper(
			List<fr.pantheonsorbonne.ufr27.miage.jpa.HeureDePassage> listeHeureDePassages) {
		List<HeureDePassage> ListeHeureDePassages = new ArrayList<HeureDePassage>();
		for (fr.pantheonsorbonne.ufr27.miage.jpa.HeureDePassage heureDePassage : listeHeureDePassages) {
			ListeHeureDePassages.add(heureDePassageDTOMapper(heureDePassage));
		}
		return ListeHeureDePassages;
	}
}
