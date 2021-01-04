package fr.pantheonsorbonne.ufr27.miage.mapper;

import fr.pantheonsorbonne.ufr27.miage.model.jaxb.ObjectFactory;

import java.util.ArrayList;
import java.util.List;

import fr.pantheonsorbonne.ufr27.miage.model.jaxb.Arret;

public class ArretMapper {
	public static Arret arretDTOMapper(fr.pantheonsorbonne.ufr27.miage.jpa.Arret arret) {
		Arret arretDTO = new ObjectFactory().createArret();

		arretDTO.setId(arret.getId());
		arretDTO.setNom(arret.getNom());

		for (fr.pantheonsorbonne.ufr27.miage.jpa.HeureDePassage hdp : arret.getListeHeureDePassage()) {
			arretDTO.getListeHeureDePassages().add(HeureDePassageMapper.heureDePassageDTOMapper(hdp));
		}

		for (fr.pantheonsorbonne.ufr27.miage.jpa.Train train : arret.getTrainsArrivants()) {
			arretDTO.getTrainsArrivants().add(TrainMapper.trainDTOMapper(train));
		}

		return arretDTO;
	}

	public static List<Arret> arretAllDTOMapper(List<fr.pantheonsorbonne.ufr27.miage.jpa.Arret> listeArrets) {
		List<Arret> ListeArretsDTO = new ArrayList<Arret>();
		for (fr.pantheonsorbonne.ufr27.miage.jpa.Arret arret : listeArrets) {
			ListeArretsDTO.add(arretDTOMapper(arret));
		}
		return ListeArretsDTO;
	}
}