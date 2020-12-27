package fr.pantheonsorbonne.ufr27.miage.dao;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.jboss.weld.junit5.EnableWeld;
import org.jboss.weld.junit5.WeldInitiator;
import org.jboss.weld.junit5.WeldSetup;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import fr.pantheonsorbonne.ufr27.miage.jpa.Arret;
import fr.pantheonsorbonne.ufr27.miage.jpa.Train;
import fr.pantheonsorbonne.ufr27.miage.jpa.HeureDePassage;
import fr.pantheonsorbonne.ufr27.miage.tests.utils.TestPersistenceProducer;

@EnableWeld
public class TestTrainDAO {
	@WeldSetup
	private WeldInitiator weld = WeldInitiator
			.from(TrainDAO.class, TestPersistenceProducer.class, HeureDePassageDAO.class, ArretDAO.class)
			.activate(RequestScoped.class).build();

	@Inject
	EntityManager em;

	@Inject
	TrainDAO dao;

	Train train1;
	Arret arret1;
	Arret arretDirection;

	@BeforeEach
	public void setup() {
		System.out.println("\n== SetUp");

		System.out.println("****************** " + dao.toString());

		em.getTransaction().begin();

		arretDirection = new Arret();
		arretDirection.setNom("Paris");
		em.persist(arretDirection);

		train1 = new Train();
		train1.setNom("Bordeaux - Paris");
		train1.setDirection(arretDirection);
		train1.setDirectionType("forward");
		train1.setStatut("enmarche");
		train1.setNumeroTrain(8541);
		train1.setReseau("SNCF");
		train1.setStatut("en marche");
		train1.setBaseDepartTemps(LocalDateTime.now().plusMinutes(10));
		train1.setBaseArriveeTemps(LocalDateTime.now().plusMinutes(30));
		train1.setReelDepartTemps(LocalDateTime.now().plusMinutes(10));
		train1.setReelArriveeTemps(LocalDateTime.now().plusMinutes(30));
		em.persist(train1);

		arret1 = new Arret();
		arret1.setNom("Lille");
		em.persist(arret1);

		em.getTransaction().commit();

	}

	@AfterEach
	public void tearDown() {
		System.out.println("\n== TearDown");
		em.getTransaction().begin();
		em.remove(train1);
		train1 = null;
		em.remove(arret1);
		arret1 = null;
		em.remove(arretDirection);
		arretDirection = null;
		em.getTransaction().commit();
	}

	@Test
	public void testCreateTrain() {

		assertFalse(dao.isTrainCreated(train1.getId()));

		em.getTransaction().begin();
		train1.setCreated(true);
		em.merge(train1);
		em.getTransaction().commit();

		assertTrue(dao.isTrainCreated(train1.getId()));

	}

	@Test
	public void testGetTrainFromId() {
		assertEquals(train1, dao.getTrainFromId(train1.getId()));
	}

	@Test
	public void testGetAllTrain() {
		List<Train> trains = dao.getAllTrain();

		assertEquals(1, trains.size());
		assertEquals(train1, trains.get(0));
	}

	@Test
	public void testAddArret() {
		em.getTransaction().begin();
		dao.addArret(train1, arret1, LocalDateTime.now().plusMinutes(30));
		em.getTransaction().commit();
		List<HeureDePassage> listHdp = train1.getListeHeureDePassage();
		assertEquals(1, listHdp.size());
		assertEquals(arret1, listHdp.get(0).getArret());
	}

	@Test
	public void testFindTrainByDirection() {
		List<Train> trains = dao.findTrainByDirection(arretDirection.getId());
		assertEquals(1, trains.size());
		assertEquals(train1, trains.get(0));
	}

	@Test
	public void testFindTrainByArret() {
		// TODO
		fail("Not yet implemented");
	}

	@Test
	public void testRemoveArret() {
		// TODO
		fail("Not yet implemented");
	}

	@Test
	public void testDeleteTrain() {
		em.getTransaction().begin();
		dao.deleteTrain(train1);
		em.getTransaction().commit();
		assertNull(dao.getTrainFromId(train1.getId()));
	}

}
