package fr.pantheonsorbonne.ufr27.miage.jms;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import javax.jms.JMSException;
import javax.jms.Session;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;

import fr.pantheonsorbonne.ufr27.miage.jpa.Train;

public class InfoCentrePublisher implements Closeable {

	@Inject
	@Named("bulletin")
	private Topic topic;

	@Inject
	private TopicConnectionFactory topicConnectionFactory;

	private TopicConnection connection;
	private TopicPublisher messagePublisher;

	private TopicSession session;

	@PostConstruct
	void init() {
		try {
			this.connection = topicConnectionFactory.createTopicConnection("alex", "alex");
			connection.start();
			this.session = connection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
			this.messagePublisher = session.createPublisher(topic);
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
