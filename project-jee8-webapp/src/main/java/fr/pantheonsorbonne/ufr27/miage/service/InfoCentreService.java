package fr.pantheonsorbonne.ufr27.miage.service;

import fr.pantheonsorbonne.ufr27.miage.exception.NoSuchTrainException;
import fr.pantheonsorbonne.ufr27.miage.model.jaxb.Train;

public interface InfoCentreService {

	void sendInfo(Train train) throws NoSuchTrainException;

}
