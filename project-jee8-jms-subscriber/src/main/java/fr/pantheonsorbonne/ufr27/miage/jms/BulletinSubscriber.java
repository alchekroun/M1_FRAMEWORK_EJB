package fr.pantheonsorbonne.ufr27.miage.jms;

import java.io.Closeable;
import java.io.IOException;

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
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;

import org.jgroups.util.UUID;

public class BulletinSubscriber implements Closeable {

	@Inject
	@Named("bulletin")
	private Topic topic;

	@Inject
	private TopicConnectionFactory connectionFactory;

	private TopicConnection connection;
	private TopicSubscriber messageConsumer;

	private TopicSession session;

	@PostConstruct
	private void init() {
		try {
			this.connection = connectionFactory.createTopicConnection("alex", "alex");
			this.connection.setClientID("Bulletin subscriber " + UUID.randomUUID());
			connection.start();
			this.session = connection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
			this.messageConsumer = session.createDurableSubscriber(topic, "bulletin-subscription");
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
