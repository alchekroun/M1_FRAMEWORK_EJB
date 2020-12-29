package fr.pantheonsorbonne.ufr27.miage;

import java.io.IOException;
import javax.enterprise.inject.se.SeContainer;
import javax.enterprise.inject.se.SeContainerInitializer;
import javax.jms.JMSException;
import javax.naming.NamingException;

import fr.pantheonsorbonne.ufr27.miage.jms.BulletinSubscriber;

public class AppSubscriber {
	public static void main(String[] args) throws JMSException, NamingException, InterruptedException, IOException {

		// initialize CDI 2.0 SE container
		SeContainerInitializer initializer = SeContainerInitializer.newInstance();

		try (SeContainer container = initializer.disableDiscovery().addPackages(AppSubscriber.class).initialize()) {

			final BulletinSubscriber bulletinSubscriber = container.select(BulletinSubscriber.class).get();

			while (true) {
				String message = bulletinSubscriber.consume();
				System.out.println("Message read from bulletinPublisher: " + message);
				if ("EXIT".equals(message)) {
					break;
				}
			}
			bulletinSubscriber.close();

		}

		System.exit(0);

	}

}
