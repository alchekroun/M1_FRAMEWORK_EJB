package fr.pantheonsorbonne.ufr27.miage.mapper;

import fr.pantheonsorbonne.ufr27.miage.model.jaxb.Passager;

import java.util.ArrayList;
import java.util.List;

import fr.pantheonsorbonne.ufr27.miage.model.jaxb.ObjectFactory;

public class PassagerMapper {
	public static Passager passagerDTOMapper(fr.pantheonsorbonne.ufr27.miage.jpa.Passager passager) {

		Passager passagerDTO = new ObjectFactory().createPassager();
		passagerDTO.setId(passager.getId());
		passagerDTO.setNom(passager.getNom());
		passagerDTO.setDepart(ArretMapper.arretDTOMapper(passager.getDepart()));
		passagerDTO.setArrive(ArretMapper.arretDTOMapper(passager.getArrive()));
		if (passager.getCorrespondance() != null) {
			passagerDTO.setCorrespondance(ArretMapper.arretDTOMapper(passager.getCorrespondance()));
		}
		passagerDTO.setArrived(passager.isArrived());

		return passagerDTO;
	}

	public static List<Passager> passagerAllDTOMapper(
			List<fr.pantheonsorbonne.ufr27.miage.jpa.Passager> listePassagers) {
		List<Passager> ListePassagers = new ArrayList<Passager>();
		for (fr.pantheonsorbonne.ufr27.miage.jpa.Passager passager : listePassagers) {
			ListePassagers.add(passagerDTOMapper(passager));
		}
		return ListePassagers;
	}
}