package fr.pantheonsorbonne.ufr27.miage.jms;

import java.io.Closeable;
import java.io.IOException;
import java.io.StringReader;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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

import fr.pantheonsorbonne.ufr27.miage.model.jaxb.HeureDePassage;
import fr.pantheonsorbonne.ufr27.miage.model.jaxb.HeureDePassageWrapper;
import fr.pantheonsorbonne.ufr27.miage.model.jaxb.Train;

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

	public boolean isInterest(List<HeureDePassage> listHdp) {
		for (HeureDePassage hdp : listHdp) {
			if (hdp.getArret().getNom().equals(this.arret) && hdp.isDesservi()) {
				return true;
			}
		}
		return false;
	}

	// Cette fonction ne devrait pas être là
	public HeureDePassage getTerminus(List<HeureDePassage> listHdp, Train t) {

		for (HeureDePassage hdp : listHdp) {

			if (hdp.getTrain().getId() == t.getId() && hdp.isTerminus()) {
				return hdp;
			}
		}
		return null;
	}

	/**
	 * Méthode permettant de recevoir le message de l'infoCentre
	 * 
	 * @param message
	 */
	public void receiveBulletin(TextMessage message) throws JAXBException, JMSException {
		JAXBContext context = JAXBContext.newInstance(HeureDePassageWrapper.class);
		StringReader reader = new StringReader(message.getText());

		HeureDePassageWrapper hdpWrapper = (HeureDePassageWrapper) context.createUnmarshaller().unmarshal(reader);
		List<HeureDePassage> listHdp = hdpWrapper.getHdps();

		if (isInterest(listHdp)) {

			StringBuilder toShow = new StringBuilder();
			toShow.append("\nO = O = O = O\n");
			toShow.append("Gare de : " + this.arret + "\n");
			toShow.append("------------------------INFO TRAINS---------------------\n");
			toShow.append("DEPARTS\n");
			for (HeureDePassage hdp : listHdp) {
				if (hdp.getArret().getNom().equals(this.arret) && hdp.isDesservi() && !hdp.isTerminus()) {
					Train t = hdp.getTrain();
					toShow.append("##\n" + t.getReseau() + " - " + t.getNumeroTrain() + "\t| ");
					toShow.append(hdp.getReelDepartTemps().toString() + "\t| ");
					toShow.append(getTerminus(listHdp, t).getArret().getNom() + "\t| ");
					if (hdp.getBaseDepartTemps().equals(hdp.getReelDepartTemps())) {
						toShow.append("A l'heure");
					} else {
						toShow.append("Retardé de "
								+ conversionDiffBetweenTwoDates(hdp.getBaseDepartTemps(), hdp.getReelDepartTemps()));
					}
					toShow.append("\n##\n");
				}
			}
			toShow.append("ARRIVEES\n");
			for (HeureDePassage hdp : listHdp) {

				if (hdp.getArret().getNom().equals(this.arret) && hdp.isDesservi()) {
					Train t = hdp.getTrain();
					toShow.append("##\n" + t.getReseau() + " - " + t.getNumeroTrain() + "\t| ");
					toShow.append(hdp.getReelArriveeTemps().toString() + "\t| ");
					toShow.append(getTerminus(listHdp, t).getArret().getNom() + "\t| ");
					if (hdp.getBaseArriveeTemps().equals(hdp.getReelArriveeTemps())) {
						toShow.append("A l'heure");
					} else {
						toShow.append("Retardé de "
								+ conversionDiffBetweenTwoDates(hdp.getBaseDepartTemps(), hdp.getReelDepartTemps()));
					}
					toShow.append("\n##\n");
				}

			}
			toShow.append("------------------------FIN INFO TRAINS---------------------");
			System.out.println(toShow);

		}

	}

	public String conversionDiffBetweenTwoDates(LocalDateTime dateFrom, LocalDateTime dateTo) {
		StringBuilder timeDisplay = new StringBuilder();
		float timeDiff = (float) ChronoUnit.SECONDS.between(dateFrom, dateTo);

		if (timeDiff >= 3600) {
			int hours = (int) timeDiff / 3600;
			timeDiff = timeDiff % 3600;
			timeDisplay.append(hours + " h ");
		}
		if (timeDiff >= 60) {
			int minutes = (int) timeDiff / 60;
			timeDiff = timeDiff % 60;
			timeDisplay.append(minutes + " min ");
		}
		int seconds = (int) timeDiff;
		timeDisplay.append(seconds + " sec ");

		return timeDisplay.toString();
	}
  
  /**
	 * Méthode permettant aux infoGares de consommer le message des infoCentres
	 * auxquels ils sont abonnés
	 */
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
