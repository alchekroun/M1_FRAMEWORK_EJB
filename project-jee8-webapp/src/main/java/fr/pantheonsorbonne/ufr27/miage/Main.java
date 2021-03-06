package fr.pantheonsorbonne.ufr27.miage;

import java.io.IOException;
import java.net.URI;
import java.util.Locale;
import javax.inject.Singleton;
import javax.jms.ConnectionFactory;
import javax.jms.Queue;
import javax.jms.Topic;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.internal.inject.AbstractBinder;
import org.glassfish.jersey.linking.DeclarativeLinkingFeature;
import org.glassfish.jersey.process.internal.RequestScoped;
import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.bridge.SLF4JBridgeHandler;

import fr.pantheonsorbonne.ufr27.miage.conf.EMFFactory;
import fr.pantheonsorbonne.ufr27.miage.conf.EMFactory;
import fr.pantheonsorbonne.ufr27.miage.conf.PersistenceConf;
import fr.pantheonsorbonne.ufr27.miage.dao.ArretDAO;
import fr.pantheonsorbonne.ufr27.miage.dao.HeureDePassageDAO;
import fr.pantheonsorbonne.ufr27.miage.dao.PassagerDAO;
import fr.pantheonsorbonne.ufr27.miage.dao.PerturbationDAO;
import fr.pantheonsorbonne.ufr27.miage.dao.TrainDAO;
import fr.pantheonsorbonne.ufr27.miage.exception.ExceptionMapper;
import fr.pantheonsorbonne.ufr27.miage.jms.InfoCentrePublisher;
import fr.pantheonsorbonne.ufr27.miage.jms.conf.BulletinTopicSupplier;
import fr.pantheonsorbonne.ufr27.miage.jms.conf.ConnectionFactorySupplier;
import fr.pantheonsorbonne.ufr27.miage.jms.conf.JMSProducer;
import fr.pantheonsorbonne.ufr27.miage.jms.conf.PaymentAckQueueSupplier;
import fr.pantheonsorbonne.ufr27.miage.jms.conf.PaymentQueueSupplier;
import fr.pantheonsorbonne.ufr27.miage.jms.utils.BrokerUtils;
import fr.pantheonsorbonne.ufr27.miage.service.ArretService;
import fr.pantheonsorbonne.ufr27.miage.service.DataInitializerService;
import fr.pantheonsorbonne.ufr27.miage.service.InfoCentreService;
import fr.pantheonsorbonne.ufr27.miage.service.PassagerService;
import fr.pantheonsorbonne.ufr27.miage.service.TrainService;
import fr.pantheonsorbonne.ufr27.miage.service.impl.ArretServiceImpl;
import fr.pantheonsorbonne.ufr27.miage.service.impl.DataInitializerServiceImpl;
import fr.pantheonsorbonne.ufr27.miage.service.impl.InfoCentreServiceImpl;
import fr.pantheonsorbonne.ufr27.miage.service.impl.PassagerServiceImpl;
import fr.pantheonsorbonne.ufr27.miage.service.impl.TrainServiceImpl;

/**
 * Main class.
 *
 */
public class Main {

	public static final String BASE_URI = "http://localhost:8080/";

	public static HttpServer startServer() {

		final ResourceConfig rc = new ResourceConfig()//
				.packages(true, "fr.pantheonsorbonne.ufr27.miage")//
				.register(DeclarativeLinkingFeature.class)//
				.register(JMSProducer.class).register(ExceptionMapper.class).register(PersistenceConf.class)
				.register(new AbstractBinder() {

					@Override
					protected void configure() {

						// Déclarer les classes pouvant être injectée

						// DAO
						bind(TrainDAO.class).to(TrainDAO.class);
						bind(PassagerDAO.class).to(PassagerDAO.class);
						bind(ArretDAO.class).to(ArretDAO.class);
						bind(HeureDePassageDAO.class).to(HeureDePassageDAO.class);
						bind(PerturbationDAO.class).to(PerturbationDAO.class);

						// SERVICE

						bind(ArretServiceImpl.class).to(ArretService.class);
						bind(TrainServiceImpl.class).to(TrainService.class);
						bind(PassagerServiceImpl.class).to(PassagerService.class);
						bind(InfoCentreServiceImpl.class).to(InfoCentreService.class);
						bind(DataInitializerServiceImpl.class).to(DataInitializerService.class);

						// AUTRE

						bindFactory(EMFFactory.class).to(EntityManagerFactory.class).in(Singleton.class);
						bindFactory(EMFactory.class).to(EntityManager.class).in(RequestScoped.class);
						bindFactory(ConnectionFactorySupplier.class).to(ConnectionFactory.class).in(Singleton.class);
						bindFactory(BulletinTopicSupplier.class).to(Topic.class).named("bulletin").in(Singleton.class);
						bindFactory(PaymentAckQueueSupplier.class).to(Queue.class).named("PaymentAckQueue")
								.in(Singleton.class);
						bindFactory(PaymentQueueSupplier.class).to(Queue.class).named("PaymentQueue")
								.in(Singleton.class);

						bind(InfoCentrePublisher.class).to(InfoCentrePublisher.class).in(Singleton.class);

					}

				});

		return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
	}

	/**
	 * Main method.beanbeanbeanbean
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {

		Locale.setDefault(Locale.ENGLISH);
		SLF4JBridgeHandler.removeHandlersForRootLogger();
		SLF4JBridgeHandler.install();
		final HttpServer server = startServer();

		BrokerUtils.startBroker();

		PersistenceConf pc = new PersistenceConf();
		EntityManager em = pc.getEM();
		pc.launchH2WS();

		initBdd(em);

		System.out.println(String.format(
				"Jersey app started with WADL available at " + "%sapplication.wadl\nHit enter to stop it...",
				BASE_URI));
		System.in.read();
		server.stop();

	}

	public static void initBdd(EntityManager em) {
		DataInitializerService di = new DataInitializerServiceImpl(em);
		di.fulfilBdd();
	}

}