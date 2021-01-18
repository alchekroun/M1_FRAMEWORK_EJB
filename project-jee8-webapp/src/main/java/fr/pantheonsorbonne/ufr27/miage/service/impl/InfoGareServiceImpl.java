package fr.pantheonsorbonne.ufr27.miage.service.impl;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import fr.pantheonsorbonne.ufr27.miage.dao.InfoGareDAO;
import fr.pantheonsorbonne.ufr27.miage.exception.CantCreateException;
import fr.pantheonsorbonne.ufr27.miage.exception.EmptyListException;
import fr.pantheonsorbonne.ufr27.miage.exception.NoSuchInfoGareException;
import fr.pantheonsorbonne.ufr27.miage.mapper.InfoGareMapper;
import fr.pantheonsorbonne.ufr27.miage.model.jaxb.InfoGare;
import fr.pantheonsorbonne.ufr27.miage.service.InfoGareService;

public class InfoGareServiceImpl implements InfoGareService {

	@Inject
	EntityManager em;

	@Inject
	InfoGareDAO dao;

	@Override
	public InfoGare getInfoGareFromId(int infoGareId) throws NoSuchInfoGareException {
		fr.pantheonsorbonne.ufr27.miage.jpa.InfoGare infoGare = dao.getInfoGareFromId(infoGareId);
		if (infoGare == null) {
			throw new NoSuchInfoGareException();
		}
		return InfoGareMapper.infoGareDTOMapper(infoGare);
	}

	@Override
	public List<InfoGare> getAllInfoGare() throws EmptyListException {
		List<fr.pantheonsorbonne.ufr27.miage.jpa.InfoGare> listeInfoGares = dao.getAllInfoGare();
		if (listeInfoGares == null) {
			throw new EmptyListException();
		}
		return InfoGareMapper.infoGareAllDTOMapper(listeInfoGares);
	}

}
