package fr.pantheonsorbonne.ufr27.miage;

import java.io.IOException;
import javax.enterprise.inject.se.SeContainer;
import javax.enterprise.inject.se.SeContainerInitializer;
import javax.jms.JMSException;
import javax.naming.NamingException;

public class AppSubscriber {
	public static void main(String[] args) throws JMSException, NamingException, InterruptedException, IOException {

		// initialize CDI 2.0 SE container
		SeContainerInitializer initializer = SeContainerInitializer.newInstance();

		try (SeContainer container = initializer.disableDiscovery().addPackages(AppSubscriber.class).initialize()) {

			final InfoGareSubscriber infoGareParis = container.select(InfoGareSubscriber.class).get();
			infoGareParis.setArret("Paris");
			while (true) {
				infoGareParis.consume();
			}
			// infoGareParis.close();

		}

		// System.exit(0);

	}

}
