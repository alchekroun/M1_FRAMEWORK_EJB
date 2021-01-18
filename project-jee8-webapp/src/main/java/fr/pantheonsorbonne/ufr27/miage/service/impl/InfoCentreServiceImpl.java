package fr.pantheonsorbonne.ufr27.miage.service.impl;

import java.util.List;
import javax.inject.Inject;
import javax.jms.JMSException;
import javax.persistence.EntityManager;
import javax.xml.bind.JAXBException;

import fr.pantheonsorbonne.ufr27.miage.dao.TrainDAO;
import fr.pantheonsorbonne.ufr27.miage.dao.ArretDAO;
import fr.pantheonsorbonne.ufr27.miage.dao.HeureDePassageDAO;
import fr.pantheonsorbonne.ufr27.miage.exception.NoSuchTrainException;
import fr.pantheonsorbonne.ufr27.miage.jms.InfoCentrePublisher;
import fr.pantheonsorbonne.ufr27.miage.model.jaxb.Train;
import fr.pantheonsorbonne.ufr27.miage.service.InfoCentreService;

public class InfoCentreServiceImpl implements InfoCentreService {

	@Inject
	EntityManager em;

	@Inject
	TrainDAO daoTrain;

	@Inject
	ArretDAO daoArret;

	@Inject
	HeureDePassageDAO daoHdp;

	@Inject
	InfoCentrePublisher infoCentrePublisher;

	@Override
	public void sendInfo(Train trainInput) throws NoSuchTrainException {
		fr.pantheonsorbonne.ufr27.miage.jpa.Train train = daoTrain.getTrainFromId(trainInput.getId());
		if (train == null) {
			throw new NoSuchTrainException();
		}
		// TODO point to point
	}

	@Override
	public void periodicBulletin(List<Train> listTrainsDTO) {
		/*
		 * List<fr.pantheonsorbonne.ufr27.miage.jpa.Train> listTrains = new
		 * ArrayList<fr.pantheonsorbonne.ufr27.miage.jpa.Train>(); for (Train t :
		 * listTrainsDTO) {
		 * listTrains.add(em.find(fr.pantheonsorbonne.ufr27.miage.jpa.Train.class,
		 * t.getId())); } infoCentrePublisher.sendBulletin(listTrains);
		 */

	}

	@Override
	public void periodicBulletin() {
		try {
			infoCentrePublisher.publishBulletinByArret(daoHdp.getAllHeureDePassage());
		} catch (JAXBException | JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// infoCentrePublisher.sendBulletin(daoTrain.getAllTrain());

	}

}
