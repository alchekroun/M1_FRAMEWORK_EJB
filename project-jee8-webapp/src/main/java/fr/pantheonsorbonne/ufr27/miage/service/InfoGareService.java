package fr.pantheonsorbonne.ufr27.miage.service;

import java.util.List;

import fr.pantheonsorbonne.ufr27.miage.exception.CantCreateException;
import fr.pantheonsorbonne.ufr27.miage.exception.EmptyListException;
import fr.pantheonsorbonne.ufr27.miage.exception.NoSuchInfoGareException;
import fr.pantheonsorbonne.ufr27.miage.model.jaxb.InfoGare;

public interface InfoGareService {

	// R
	public InfoGare getInfoGareFromId(int infoGareId) throws NoSuchInfoGareException;

	public List<InfoGare> getAllInfoGare() throws EmptyListException;
}
