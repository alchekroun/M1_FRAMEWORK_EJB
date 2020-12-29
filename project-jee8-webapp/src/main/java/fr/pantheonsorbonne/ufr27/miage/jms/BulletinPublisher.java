package fr.pantheonsorbonne.ufr27.miage.jms;

import java.io.Closeable;
import java.io.IOException;
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

public class BulletinPublisher implements Closeable {

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
}
