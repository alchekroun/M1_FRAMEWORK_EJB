package fr.pantheonsorbonne.ufr27.miage.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import fr.pantheonsorbonne.ufr27.miage.Main;
import fr.pantheonsorbonne.ufr27.miage.dao.TrainDAO;
import fr.pantheonsorbonne.ufr27.miage.exception.NoSuchTrainException;
import fr.pantheonsorbonne.ufr27.miage.model.jaxb.Train;
import fr.pantheonsorbonne.ufr27.miage.service.InfoCentreService;

public class InfoCentreServiceImpl implements InfoCentreService {

	@Inject
	EntityManager em;

	@Inject
	TrainDAO daoTrain;

	@Override
	public void sendInfo(Train trainInput) throws NoSuchTrainException {
		fr.pantheonsorbonne.ufr27.miage.jpa.Train train = daoTrain.getTrainFromId(trainInput.getId());
		if (train == null) {
			throw new NoSuchTrainException();
		}
	}

	@Override
	public void periodicBulletin(List<Train> listTrainsDTO) {
		try {
			System.out.println("\n ###### inServicesIMPL #######");
			List<fr.pantheonsorbonne.ufr27.miage.jpa.Train> listTrains = new ArrayList<fr.pantheonsorbonne.ufr27.miage.jpa.Train>();
			for (Train t : listTrainsDTO) {
				listTrains.add(em.find(fr.pantheonsorbonne.ufr27.miage.jpa.Train.class, t.getId()));
			}
			Main.periodicBulletin(listTrains);
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}

	}

}
