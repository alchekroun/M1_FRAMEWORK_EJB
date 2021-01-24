package fr.pantheonsorbonne.ufr27.miage.service.impl;

import javax.annotation.ManagedBean;
import javax.inject.Inject;
import javax.jms.JMSException;
import javax.persistence.EntityManager;
import javax.xml.bind.JAXBException;

import fr.pantheonsorbonne.ufr27.miage.dao.TrainDAO;
import fr.pantheonsorbonne.ufr27.miage.dao.ArretDAO;
import fr.pantheonsorbonne.ufr27.miage.dao.HeureDePassageDAO;
import fr.pantheonsorbonne.ufr27.miage.jms.InfoCentrePublisher;
import fr.pantheonsorbonne.ufr27.miage.service.InfoCentreService;

@ManagedBean
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
