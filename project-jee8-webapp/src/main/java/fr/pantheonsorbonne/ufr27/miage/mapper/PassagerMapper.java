package fr.pantheonsorbonne.ufr27.miage.mapper;

import fr.pantheonsorbonne.ufr27.miage.model.jaxb.Passager;
import fr.pantheonsorbonne.ufr27.miage.model.jaxb.ObjectFactory;

public class PassagerMapper {
	public static Passager passagerDTOMapper(fr.pantheonsorbonne.ufr27.miage.jpa.Passager passager) {

		Passager passagerDTO = new ObjectFactory().createPassager();

		passagerDTO.setIdPassager(passager.getIdPassager());
		passagerDTO.setNomPassager(passager.getNomPassager());
		passagerDTO.setArrive(passager.getArrive());
		passagerDTO.setDepart(passager.getDepart());

		return passagerDTO;
	}
}
