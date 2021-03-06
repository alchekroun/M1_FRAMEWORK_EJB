package fr.pantheonsorbonne.ufr27.miage.mapper;

import java.util.ArrayList;
import java.util.List;

import fr.pantheonsorbonne.ufr27.miage.model.jaxb.ObjectFactory;
import fr.pantheonsorbonne.ufr27.miage.model.jaxb.HeureDePassage;

public class HeureDePassageMapper {

	/**
	 * Mappage d'une heure de passage
	 * 
	 * @param heureDePassage
	 * @return HeureDePassage
	 */
	public static HeureDePassage heureDePassageDTOMapper(
			fr.pantheonsorbonne.ufr27.miage.jpa.HeureDePassage heureDePassage) {
		HeureDePassage hdpDTO = new ObjectFactory().createHeureDePassage();

		hdpDTO.setArret(ArretMapper.arretDTOMapper(heureDePassage.getArret()));
		hdpDTO.setTrain(TrainMapper.trainDTOMapper(heureDePassage.getTrain()));
		hdpDTO.setReelArriveeTemps(heureDePassage.getReelArriveeTemps());
		hdpDTO.setReelDepartTemps(heureDePassage.getReelDepartTemps());
		hdpDTO.setBaseDepartTemps(heureDePassage.getBaseDepartTemps());
		hdpDTO.setBaseArriveeTemps(heureDePassage.getBaseArriveeTemps());
		hdpDTO.setDesservi(heureDePassage.isDesservi());
		hdpDTO.setTerminus(heureDePassage.isTerminus());

		return hdpDTO;
	}

	/**
	 * Mappage de toutes les heures de passage
	 * 
	 * @param listeHeureDePassages
	 * @return List<HeureDePassage>
	 */
	public static List<HeureDePassage> heureDePassageAllDTOMapper(
			List<fr.pantheonsorbonne.ufr27.miage.jpa.HeureDePassage> listeHeureDePassages) {
		List<HeureDePassage> ListeHeureDePassages = new ArrayList<HeureDePassage>();
		for (fr.pantheonsorbonne.ufr27.miage.jpa.HeureDePassage heureDePassage : listeHeureDePassages) {
			ListeHeureDePassages.add(heureDePassageDTOMapper(heureDePassage));
		}
		return ListeHeureDePassages;
	}
}
