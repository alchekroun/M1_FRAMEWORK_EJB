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
		return arretDTO;
	}

	public static List<Arret> arretAllDTOMapper(List<fr.pantheonsorbonne.ufr27.miage.jpa.Arret> listeArrets) {
		List<Arret> ListeArrets = new ArrayList<Arret>();
		for (fr.pantheonsorbonne.ufr27.miage.jpa.Arret arret : listeArrets) {
			ListeArrets.add(arretDTOMapper(arret));
		}
		return ListeArrets;
	}
}