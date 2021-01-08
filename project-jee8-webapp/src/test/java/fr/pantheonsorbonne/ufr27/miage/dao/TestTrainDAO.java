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
import fr.pantheonsorbonne.ufr27.miage.jpa.TrainAvecResa;
import fr.pantheonsorbonne.ufr27.miage.model.jaxb.ObjectFactory;
import fr.pantheonsorbonne.ufr27.miage.jpa.HeureDePassage;
import fr.pantheonsorbonne.ufr27.miage.jpa.Passager;
import fr.pantheonsorbonne.ufr27.miage.tests.utils.TestPersistenceProducer;

@EnableWeld
public class TestTrainDAO {
	@WeldSetup
	private WeldInitiator weld = WeldInitiator.from(TrainDAO.class, TestPersistenceProducer.class,
			HeureDePassageDAO.class, ArretDAO.class, PassagerDAO.class).activate(RequestScoped.class).build();

	@Inject
	EntityManager em;

	@Inject
	TrainDAO dao;

	Train train1;
	Arret arret1;
	Arret arretDirection;
	Passager passager1;

	@BeforeEach
	public void setup() {
		System.out.println("\n== SetUp");

		System.out.println("****************** " + dao.toString());

		em.getTransaction().begin();

		arretDirection = new Arret();
		arretDirection.setNom("Paris");
		em.persist(arretDirection);

		train1 = new TrainAvecResa();
		train1.setNom("Bordeaux - Paris");
		train1.setDirectionType("forward");
		train1.setStatut("enmarche");
		train1.setNumero(8541);
		train1.setReseau("SNCF");
		em.persist(train1);

		arret1 = new Arret();
		arret1.setNom("Lille");
		em.persist(arret1);

		passager1 = new Passager();
		passager1.setNom("David Serruya");
		passager1.setArrive(arretDirection);
		passager1.setDepart(arret1);
		em.persist(passager1);

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
		em.remove(passager1);
		passager1 = null;
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
		dao.addArret(train1, arretDirection, LocalDateTime.now().plusMinutes(10), LocalDateTime.now(), true, true);
		dao.addArret(train1, arret1, LocalDateTime.now().plusMinutes(10), LocalDateTime.now().plusMinutes(20), true,
				false);
		em.getTransaction().commit();
		List<HeureDePassage> listHdp = train1.getListeHeureDePassage();
		assertEquals(2, listHdp.size());
		for (HeureDePassage hdp : listHdp) {
			if (hdp.isTerminus()) {
				assertEquals(hdp.getArret().getNom(), arretDirection.getNom());
			} else {
				assertEquals(hdp.getArret().getNom(), arret1.getNom());
			}
		}
		em.getTransaction().begin();
		dao.removeArret(train1, arret1);
		dao.removeArret(train1, arretDirection);
		em.getTransaction().commit();
	}

	@Test
	public void testUpdateTrain() {
		ObjectFactory factory = new ObjectFactory();
		fr.pantheonsorbonne.ufr27.miage.model.jaxb.Train trainUpdate = factory.createTrain();
		trainUpdate.setId(train1.getId());
		trainUpdate.setNom(train1.getNom());
		trainUpdate.setNumeroTrain(train1.getNumero());
		trainUpdate.setStatut(train1.getStatut());
		trainUpdate.setReseau(train1.getReseau());

		fr.pantheonsorbonne.ufr27.miage.model.jaxb.Arret arretUpdate1 = factory.createArret();
		arretUpdate1.setId(arret1.getId());
		arretUpdate1.setNom(arret1.getNom());

		Train trainOriginalNonModif = train1;

		em.getTransaction().begin();
		em.merge(dao.updateTrain(train1, trainUpdate));
		em.getTransaction().commit();
		train1 = em.find(Train.class, train1.getId());
		assertEquals(train1.getId(), trainUpdate.getId());
		assertEquals(train1.getNom(), trainUpdate.getNom());
		assertEquals(train1.getNumero(), trainUpdate.getNumeroTrain());
		assertEquals(train1.getReseau(), trainUpdate.getReseau());
		assertEquals(train1.getStatut(), trainUpdate.getStatut());
		assertEquals(trainOriginalNonModif.getListeHeureDePassage(), train1.getListeHeureDePassage());
		assertEquals(trainOriginalNonModif.getListePassagers(), train1.getListePassagers());

	}

	@Test
	public void testFindTrainByArret() {
		em.getTransaction().begin();
		dao.addArret(train1, arret1, LocalDateTime.now().plusMinutes(20), LocalDateTime.now().plusMinutes(10), true,
				false);
		em.getTransaction().commit();
		List<Train> trains = dao.findTrainByArret(arret1.getId());
		assertEquals(1, trains.size());
		assertEquals(train1, trains.get(0));
		em.getTransaction().begin();
		dao.removeArret(train1, arret1);
		em.getTransaction().commit();

	}

	@Test
	public void testRemoveArret() {
		em.getTransaction().begin();
		dao.addArret(train1, arret1, LocalDateTime.now().plusMinutes(30), LocalDateTime.now().plusMinutes(10), true,
				false);
		em.getTransaction().commit();
		List<HeureDePassage> listHdp = train1.getListeHeureDePassage();
		em.getTransaction().begin();
		dao.removeArret(train1, arret1);
		em.getTransaction().commit();
		listHdp = train1.getListeHeureDePassage();
		assertTrue(dao.findTrainByArret(arret1.getId()).isEmpty());
		assertTrue(train1.getListeHeureDePassage().isEmpty());
	}

	@Test
	public void testAddPassager() {

		assertEquals(passager1.getTrain(), null);
		assertTrue(train1.getListePassagers().isEmpty());
		dao.addPassager(train1, passager1);
		assertFalse(train1.getListePassagers().isEmpty());
		assertEquals(train1.getListePassagers().get(0), passager1);
		assertEquals(passager1.getTrain(), train1);
		dao.removePassager(train1, passager1);

	}

	@Test
	public void testRemovePassager() {

		dao.addPassager(train1, passager1);
		dao.removePassager(train1, passager1);
		assertTrue(train1.getListePassagers().isEmpty());
		assertNull(passager1.getTrain());
	}

	// Vérifier bcp plus de chsoe sur la méthode :
	// Si le train a été bien enlevé des listes contenu dans l'Arret
	@Test
	public void testDeleteTrain() {
		em.getTransaction().begin();
		dao.addArret(train1, arret1, LocalDateTime.now().plusMinutes(30), LocalDateTime.now().plusMinutes(10), true,
				false);
		dao.addPassager(train1, passager1);
		dao.deleteTrain(train1);
		em.getTransaction().commit();
		assertNull(dao.getTrainFromId(train1.getId()));
		// assertFalse(passager1.getTrain().equals(train1));
		for (HeureDePassage hdp : arret1.getListeHeureDePassage()) {
			assertFalse(hdp.getTrain().equals(train1));
		}
	}

}
