package fr.pantheonsorbonne.ufr27.miage;

import java.io.IOException;
import javax.enterprise.inject.se.SeContainer;
import javax.enterprise.inject.se.SeContainerInitializer;
import javax.jms.JMSException;
import javax.naming.NamingException;

import fr.pantheonsorbonne.ufr27.miage.jms.InfoGareSubscriber;

public class AppSubscriber {
	public static void main(String[] args) throws JMSException, NamingException, InterruptedException, IOException {

		// initialize CDI 2.0 SE container
		SeContainerInitializer initializer = SeContainerInitializer.newInstance();

		try (SeContainer container = initializer.disableDiscovery().addPackages(true, AppSubscriber.class)
				.initialize()) {

			final InfoGareSubscriber infoGareParis = container.select(InfoGareSubscriber.class).get();
			infoGareParis.setArret("Paris");

			final InfoGareSubscriber infoGareLille = container.select(InfoGareSubscriber.class).get();
			infoGareLille.setArret("Lille");

			final InfoGareSubscriber infoGareChantilly = container.select(InfoGareSubscriber.class).get();
			infoGareLille.setArret("Chantilly");

			final InfoGareSubscriber infoGareArras = container.select(InfoGareSubscriber.class).get();
			infoGareLille.setArret("Arras");
			while (true) {
				// check if arret is concerned by this message before printing it. Now
				// everything is printed!
				infoGareParis.consume();
				infoGareLille.consume();
				infoGareChantilly.consume();
				infoGareArras.consume();
				// System.out.println(infoGareParis.getArret() + " :\t" +
				// infoGareParis.consume());
			}
			// infoGareParis.close();

		}

		// System.exit(0);

	}

}
