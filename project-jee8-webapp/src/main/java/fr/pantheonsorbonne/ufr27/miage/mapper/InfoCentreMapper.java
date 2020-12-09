package fr.pantheonsorbonne.ufr27.miage.mapper;

import java.util.ArrayList;
import java.util.List;

import fr.pantheonsorbonne.ufr27.miage.model.jaxb.InfoCentre;
import fr.pantheonsorbonne.ufr27.miage.model.jaxb.ObjectFactory;

public class InfoCentreMapper {
	public static InfoCentre infoCentreDTOMapper(fr.pantheonsorbonne.ufr27.miage.jpa.InfoCentre infoCentre) {
		return new ObjectFactory().createInfoCentre();
	}

	public static List<InfoCentre> infoCentreAllDTOMapper(List<fr.pantheonsorbonne.ufr27.miage.jpa.InfoCentre> listeInfoCentres) {
		List<InfoCentre> ListeInfoCentres = new ArrayList<InfoCentre>();
		for (fr.pantheonsorbonne.ufr27.miage.jpa.InfoCentre infoCentre : listeInfoCentres) {
			ListeInfoCentres.add(infoCentreDTOMapper(infoCentre));
		}
		return ListeInfoCentres;
	}
}
