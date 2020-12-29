package fr.pantheonsorbonne.ufr27.miage.service.impl;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.Topic;
import javax.persistence.EntityManager;

import fr.pantheonsorbonne.ufr27.miage.dao.TrainDAO;
import fr.pantheonsorbonne.ufr27.miage.exception.NoSuchTrainException;
import fr.pantheonsorbonne.ufr27.miage.model.jaxb.Train;
import fr.pantheonsorbonne.ufr27.miage.service.InfoCentreService;

public class InfoCentreServiceImpl implements InfoCentreService {

	@Inject
	EntityManager em;

	@Inject
	TrainDAO daoTrain;

	@Inject
	private ConnectionFactory connectionFactory;

	@Inject
	@Named("bulletin")
	private Topic topic;

	private Connection connection;
	private Session session;
	private MessageProducer messageProducer;

	@PostConstruct
	private void init() {

		try {
			connection = connectionFactory.createConnection("alex", "alex");
			connection.start();
			session = connection.createSession();
			messageProducer = session.createProducer(queue);
		} catch (JMSException e) {
			throw new RuntimeException("failed to create JMS Session", e);
		}
	}

	@Override
	public void sendInfo(Train trainInput) throws NoSuchTrainException {
		fr.pantheonsorbonne.ufr27.miage.jpa.Train train = daoTrain.getTrainFromId(trainInput.getId());
		if (train == null) {
			throw new NoSuchTrainException();
		}
	}

}
