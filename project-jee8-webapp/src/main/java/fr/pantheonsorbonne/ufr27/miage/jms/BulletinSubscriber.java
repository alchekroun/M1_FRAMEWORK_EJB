package fr.pantheonsorbonne.ufr27.miage.jms;

import java.io.Closeable;
import java.io.IOException;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicSubscriber;

public class BulletinSubscriber implements Closeable {

	@Inject
	@Named("bulletin")
	private Topic topic;

	@Inject
	private TopicConnectionFactory topicConnectionFactory;

	private TopicConnection connection;
	private TopicSubscriber messageConsumer;

	private Session session;

	@PostConstruct
	void init() {
		try {
			connection = topicConnectionFactory.createTopicConnection("alex", "alex");
			connection.setClientID("Bulletin subscriber " + UUID.randomUUID());
			connection.start();
			session = connection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
			messageConsumer = session.createDurableSubscriber(topic, "bulletin-subscription");
		} catch (JMSException e) {
			throw new RuntimeException(e);
		}

	}

	public String consume() {
		try {
			Message message = messageConsumer.receive();
			return ((TextMessage) message).getText();

		} catch (JMSException e) {
			System.out.println("failed to consume message ");
			return "";
		}
	}

	@Override
	public void close() throws IOException {
		try {
			messageConsumer.close();
			session.close();
			connection.close();
		} catch (JMSException e) {
			System.out.println("Failed to close JMS resources");
		}

	}

}
