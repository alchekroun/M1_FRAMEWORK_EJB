package fr.pantheonsorbonne.ufr27.miage.mapper;

import fr.pantheonsorbonne.ufr27.miage.model.jaxb.Passager;

import java.util.ArrayList;
import java.util.List;

import fr.pantheonsorbonne.ufr27.miage.model.jaxb.Arret;
import fr.pantheonsorbonne.ufr27.miage.model.jaxb.ObjectFactory;

public class PassagerMapper {
	public static Passager passagerDTOMapper(fr.pantheonsorbonne.ufr27.miage.jpa.Passager passager) {

		Passager passagerDTO = new ObjectFactory().createPassager();
		Arret arretDepart = ArretMapper.arretDTOMapper(passager.getDepart());
		Arret arretArrive = ArretMapper.arretDTOMapper(passager.getArrive());
		passagerDTO.setId(passager.getId());
		passagerDTO.setNom(passager.getNom());
		passagerDTO.setDepart(arretDepart);
		passagerDTO.setArrive(arretArrive);

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