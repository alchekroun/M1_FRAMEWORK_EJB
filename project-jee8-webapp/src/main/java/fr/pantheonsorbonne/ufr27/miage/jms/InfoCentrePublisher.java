package fr.pantheonsorbonne.ufr27.miage.jms;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.Topic;
import fr.pantheonsorbonne.ufr27.miage.jpa.Train;

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

	public String prepareBulletinToPublish(List<Train> listTrains) {
		StringBuilder message = new StringBuilder();
		message.append("------------------------INFO TRAINS---------------------\n");
		for (Train t : listTrains) {
			message.append("Train n°: ");
			message.append(t.getNumero());
			message.append("\nDestination : ");
			message.append(t.getDirection());
			message.append("\n");
		}
		message.append("------------------------FIN INFO TRAINS---------------------");
		return message.toString();
	}

	public void sendBulletin(List<Train> listTrains) {
		String message = prepareBulletinToPublish(listTrains);
		System.out.println("Bulletin sent : " + publish(message));
	}
}
