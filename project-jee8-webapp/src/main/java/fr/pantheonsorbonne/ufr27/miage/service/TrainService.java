package fr.pantheonsorbonne.ufr27.miage.service;

import fr.pantheonsorbonne.ufr27.miage.model.jaxb.FreeTrialPlan;
import fr.pantheonsorbonne.ufr27.miage.model.jaxb.Train;

public interface TrainService {
	public int createTrain(Train trainDTO);
}
