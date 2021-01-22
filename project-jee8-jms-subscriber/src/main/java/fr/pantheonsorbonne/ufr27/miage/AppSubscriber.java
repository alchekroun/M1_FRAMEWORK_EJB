package fr.pantheonsorbonne.ufr27.miage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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

			String[] nomArrets = { "Lyon Part Dieu", "Valence", "Avignon", "Aix-en-Provence", "Marseille", "Toulon",
					"Draguinan", "Saint-Raphael", "Cannes", "Antibes", "Nice-Ville", "Paris Gare de Lyon" };

			Map<String, InfoGareSubscriber> mapIgs = new HashMap<String, InfoGareSubscriber>();
			for (String nom : nomArrets) {
				final InfoGareSubscriber infoGare = container.select(InfoGareSubscriber.class).get();
				infoGare.setArret(nom);
				mapIgs.put(nom, infoGare);
			}

			while (true) {
				for (InfoGareSubscriber igS : mapIgs.values()) {
					igS.consume();
				}
			}
			// for (InfoGareSubscriber igS : mapIgs.values()) {
			// igS.close();
			// }

		}

		// System.exit(0);

	}

}
