package fr.pantheonsorbonne.ufr27.miage.service;

import java.util.List;

import fr.pantheonsorbonne.ufr27.miage.exception.EmptyListException;
import fr.pantheonsorbonne.ufr27.miage.exception.NoSuchInfoGareException;
import fr.pantheonsorbonne.ufr27.miage.model.jaxb.InfoGare;

public interface InfoGareService {

	// C
	public int createInfoGare(InfoGare infoGareDTO);

	// R
	public InfoGare getInfoGareFromId(int infoGareId) throws NoSuchInfoGareException;

	// U
	public void updateInfoGare(InfoGare infoGare) throws NoSuchInfoGareException;

	// D
	public void deleteInfoGare(int infoGareId) throws NoSuchInfoGareException;

	public List<InfoGare> getAllInfoGare() throws EmptyListException;
}
