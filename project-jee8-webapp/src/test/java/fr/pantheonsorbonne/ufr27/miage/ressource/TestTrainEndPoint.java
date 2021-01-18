package fr.pantheonsorbonne.ufr27.miage.ressource;

import static org.junit.jupiter.api.Assertions.*;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.weld.junit5.EnableWeld;
import org.jboss.weld.junit5.WeldInitiator;
import org.jboss.weld.junit5.WeldSetup;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.pantheonsorbonne.ufr27.miage.dao.ArretDAO;
import fr.pantheonsorbonne.ufr27.miage.dao.HeureDePassageDAO;
import fr.pantheonsorbonne.ufr27.miage.dao.PassagerDAO;
import fr.pantheonsorbonne.ufr27.miage.dao.TrainDAO;
import fr.pantheonsorbonne.ufr27.miage.model.jaxb.Arret;
import fr.pantheonsorbonne.ufr27.miage.model.jaxb.ObjectFactory;
import fr.pantheonsorbonne.ufr27.miage.model.jaxb.Passager;
import fr.pantheonsorbonne.ufr27.miage.model.jaxb.Train;
import fr.pantheonsorbonne.ufr27.miage.resource.TrainEndPoint;
import fr.pantheonsorbonne.ufr27.miage.service.TrainService;
import fr.pantheonsorbonne.ufr27.miage.service.impl.TrainServiceImpl;
import fr.pantheonsorbonne.ufr27.miage.tests.utils.TestPersistenceProducer;

@EnableWeld
class TestTrainEndPoint {

	@WeldSetup
	private WeldInitiator weld = WeldInitiator
			.from(TrainService.class, TrainServiceImpl.class, TrainEndPoint.class, TrainDAO.class,
					HeureDePassageDAO.class, ArretDAO.class, PassagerDAO.class, TestPersistenceProducer.class)
			.activate(RequestScoped.class).build();

	@Inject
	EntityManager em;

	static Client client;
	static WebTarget target;
	static ObjectFactory factory;

	// JAXB
	Train train1;
	Arret arret1;
	Arret arretDirection;
	Passager passager1;

	// Reponses
	Response respTrain1;
	Response respArret1;
	Response respArretDirection;
	Response respPassager1;

	int indexTrain = 0;
	int indexArret = 0;
	int indexPassager = 0;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		client = ClientBuilder.newClient();
		target = client.target("http://localhost:8080");

		factory = new ObjectFactory();
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
		client.close();
	}

	@BeforeEach
	void setUp() throws Exception {
		System.out.println("\n== SetUp");

		arretDirection = factory.createArret();
		arretDirection.setId(indexArret++);
		arretDirection.setNom("Paris");

		respArretDirection = target.path("arret").request().accept(MediaType.APPLICATION_JSON)
				.post(Entity.json(arretDirection));

		train1 = factory.createTrainAvecResa();
		train1.setId(indexTrain++);
		train1.setNom("Bordeaux - Paris");
		train1.setDirectionType("forward");
		train1.setStatut("enmarche");
		train1.setNumeroTrain(8541);
		train1.setReseau("SNCF");
		train1.setStatut("en marche");

		respTrain1 = target.path("train").request().accept(MediaType.APPLICATION_JSON).post(Entity.json(train1));
	}

	@AfterEach
	void tearDown() throws Exception {
		client.target(respArretDirection.getLocation()).request().delete();
		client.target(respTrain1.getLocation()).request().delete();
	}

	@Test
	void testCreateTrain() {
		fail("Not yet implemented");
	}

	@Test
	void testGetTrain() {
		Train train = client.target(respTrain1.getLocation()).request().get(Response.class).readEntity(Train.class);
		assertEquals(train.getId(), train1.getId());
	}

	@Test
	void testDelete() {
		fail("Not yet implemented");
	}

	@Test
	void testUpdate() {
		fail("Not yet implemented");
	}

	@Test
	void testAddArret() {
		fail("Not yet implemented");
	}

	@Test
	void testRemoveArret() {
		fail("Not yet implemented");
	}

	@Test
	void testGetAllTrain() {
		fail("Not yet implemented");
	}

}
