package fr.pantheonsorbonne.ufr27.miage.jms;

import java.io.Closeable;
import java.io.IOException;
import java.io.StringWriter;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import fr.pantheonsorbonne.ufr27.miage.jpa.Arret;
import fr.pantheonsorbonne.ufr27.miage.jpa.Train;
import fr.pantheonsorbonne.ufr27.miage.mapper.TrainMapper;
import fr.pantheonsorbonne.ufr27.miage.model.jaxb.TrainWrapper;

public class InfoCentrePublisher implements Closeable {

	@Inject
	@Named("bulletin")
	private Topic topic;

	@Inject
	private ConnectionFactory connectionFactory;

	private Connection connection;
	private MessageProducer messagePublisher;

	private Session session;

	@PostConstruct
	void init() {
		try {
			this.connection = connectionFactory.createConnection("alex", "alex");
			connection.start();
			this.session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			this.messagePublisher = session.createProducer(topic);
		} catch (JMSException e) {
			throw new RuntimeException(e);
		}

	}

	public String publish(String message) {
		try {
			this.messagePublisher.send(this.session.createTextMessage(message));
			return message;
		} catch (JMSException e) {
			System.out.println("Failed to send message to queue");
			return "Nothing sent";
		}
	}

	public String publishBulletinByArret(Set<Train> listTrains, Arret arret) throws JAXBException, JMSException {
		JAXBContext jaxbContext = JAXBContext.newInstance(TrainWrapper.class);
		Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

		TrainWrapper listeTrain = new TrainWrapper();

		for (Train t : listTrains) {
			listeTrain.getTrains().add(TrainMapper.trainDTOMapper(t));
		}

		StringWriter writer = new StringWriter();
		jaxbMarshaller.marshal(listeTrain, writer);
		TextMessage message = session.createTextMessage(writer.toString());
		message.setStringProperty("type", "info-bulletin");
		message.setStringProperty("arret", arret.getNom());

		this.messagePublisher.send(message);
		return message.toString();

	}

	@Override
	public void close() throws IOException {
		try {
			messagePublisher.close();
			session.close();
			connection.close();
		} catch (JMSException e) {
			System.out.println("failed to close JMS resources");
		}
	}
}
