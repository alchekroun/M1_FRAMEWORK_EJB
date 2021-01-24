package fr.pantheonsorbonne.ufr27.miage.mapper;

import fr.pantheonsorbonne.ufr27.miage.model.jaxb.ObjectFactory;

import java.util.ArrayList;
import java.util.List;

import fr.pantheonsorbonne.ufr27.miage.model.jaxb.Arret;

public class ArretMapper {

	/**
	 * Mappage d'un arrêt
	 * 
	 * @param arret
	 * @return Arret
	 */
	public static Arret arretDTOMapper(fr.pantheonsorbonne.ufr27.miage.jpa.Arret arret) {
		Arret arretDTO = new ObjectFactory().createArret();

		arretDTO.setId(arret.getId());
		arretDTO.setNom(arret.getNom());

		return arretDTO;
	}

	/**
	 * Mappage de tous les arrêts
	 * 
	 * @param listeArrets
	 * @return Lsit<Arret>
	 */
	public static List<Arret> arretAllDTOMapper(List<fr.pantheonsorbonne.ufr27.miage.jpa.Arret> listeArrets) {
		List<Arret> ListeArretsDTO = new ArrayList<Arret>();
		for (fr.pantheonsorbonne.ufr27.miage.jpa.Arret arret : listeArrets) {
			ListeArretsDTO.add(arretDTOMapper(arret));
		}
		return ListeArretsDTO;
	}
}