package fr.pantheonsorbonne.ufr27.miage.mapper;

import fr.pantheonsorbonne.ufr27.miage.model.jaxb.ObjectFactory;
import fr.pantheonsorbonne.ufr27.miage.model.jaxb.Arret;

public class ArretMapper {
	public static Arret arretDTOMapper(fr.pantheonsorbonne.ufr27.miage.jpa.Arret arret) {
		
		Arret arretDTO = new ObjectFactory().createArret();

	
		arretDTO.setIdArret(arret.getId());
		arretDTO.setNomArret(arret.getNom());

		return arretDTO;
	}
}
