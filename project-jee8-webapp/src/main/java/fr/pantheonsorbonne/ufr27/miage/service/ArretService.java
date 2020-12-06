package fr.pantheonsorbonne.ufr27.miage.service;

import java.util.List;

import fr.pantheonsorbonne.ufr27.miage.exception.NoSuchArretException;
import fr.pantheonsorbonne.ufr27.miage.model.jaxb.Arret;

public interface ArretService {
	public int createArret(Arret arretDTO);

	public Arret getArretFromId(int arretId) throws NoSuchArretException;

	public List<Arret> getAllArret() throws NoSuchArretException;

	public void deleteArret(int arretId) throws NoSuchArretException;

}
