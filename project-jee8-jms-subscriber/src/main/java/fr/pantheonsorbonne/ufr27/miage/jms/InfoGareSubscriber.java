package fr.pantheonsorbonne.ufr27.miage.jms;

import java.io.Closeable;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.jms.Connection;
import org.jgroups.util.UUID;

import fr.pantheonsorbonne.ufr27.miage.model.jaxb.Train;
import fr.pantheonsorbonne.ufr27.miage.model.jaxb.TrainWrapper;

public class InfoGareSubscriber implements Closeable {

	@Inject
	@Named("bulletin")
	private Topic topic;

	@Inject
	private ConnectionFactory connectionFactory;

	private Connection connection;
	private MessageConsumer messageConsumer;

	private Session session;

	private String arret;

	@PostConstruct
	private void init() {
		try {
			this.connection = connectionFactory.createConnection("alex", "alex");
			this.connection.setClientID("Bulletin subscriber " + UUID.randomUUID());
			connection.start();
			this.session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			this.messageConsumer = session.createDurableSubscriber(topic, "bulletin-subscription");
		} catch (JMSException e) {
			throw new RuntimeException(e);
		}
	}

	public void receiveBulletin(TextMessage message) throws JAXBException, JMSException {
		JAXBContext context = JAXBContext.newInstance(TrainWrapper.class);
		StringReader reader = new StringReader(message.getText());

		if (this.arret.equals(message.getStringProperty("arret"))) {

			TrainWrapper trainWrapper = (TrainWrapper) context.createUnmarshaller().unmarshal(reader);
			List<Train> listTrains = trainWrapper.getTrains();

			StringBuilder toShow = new StringBuilder();
			toShow.append("------------------------------------------------\n");
			toShow.append("Gare de : " + this.arret + "\n");
			toShow.append("------------------------INFO TRAINS---------------------\n");
			for (Train t : listTrains) {
				toShow.append("##\nTrain n°: ");
				toShow.append(t.getNumeroTrain() + " - " + t.getReseau() + "\n");
				toShow.append("Destination : ");
				toShow.append(t.getDirection().getNom() + "\n");
				if (t.getBaseArriveeTemps().equals(t.getReelArriveeTemps())) {
					toShow.append("A l'heure\n");
				} else {
					toShow.append("En retard\n");
				}
				toShow.append("Arrivée prévue à : ");
				toShow.append(t.getReelArriveeTemps() + "\n##\n");
			}
			toShow.append("------------------------FIN INFO TRAINS---------------------");
			System.out.println(toShow);
		}
	}

	public void consume() {
		try {
			receiveBulletin((TextMessage) messageConsumer.receive());
		} catch (JMSException | JAXBException e) {
			System.out.println("failed to consume message");
			e.printStackTrace();
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

	public String getArret() {
		return arret;
	}

	public void setArret(String arret) {
		this.arret = arret;
	}

}
