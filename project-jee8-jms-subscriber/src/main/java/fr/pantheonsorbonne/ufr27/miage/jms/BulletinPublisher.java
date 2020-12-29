package fr.pantheonsorbonne.ufr27.miage.jms;

import java.io.Closeable;
import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;

public class BulletinPublisher implements Closeable {

	@Inject
	@Named("bulletin")
	private Topic topic;

	@Inject
	private TopicConnectionFactory connectionFactory;

	private TopicConnection connection;

	Session session;
	MessageProducer messagePublisher;

	@PostConstruct
	private void init() {
		try {
			this.connection = connectionFactory.createTopicConnection("alex", "alex");
			connection.start();
			this.session = connection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
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
			System.out.println("Failed to close JMS resources");
		}

	}

}
