package fr.pantheonsorbonne.ufr27.miage.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;

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
import fr.pantheonsorbonne.ufr27.miage.exception.CantCreateException;
import fr.pantheonsorbonne.ufr27.miage.exception.CantDeleteException;
import fr.pantheonsorbonne.ufr27.miage.exception.CantUpdateException;
import fr.pantheonsorbonne.ufr27.miage.exception.EmptyListException;
import fr.pantheonsorbonne.ufr27.miage.exception.NoSuchArretException;
import fr.pantheonsorbonne.ufr27.miage.exception.NoSuchTrainException;
import fr.pantheonsorbonne.ufr27.miage.model.jaxb.Arret;
import fr.pantheonsorbonne.ufr27.miage.model.jaxb.ObjectFactory;
import fr.pantheonsorbonne.ufr27.miage.model.jaxb.Train;
import fr.pantheonsorbonne.ufr27.miage.resource.ArretEndPoint;
import fr.pantheonsorbonne.ufr27.miage.resource.TrainEndPoint;
import fr.pantheonsorbonne.ufr27.miage.service.impl.ArretServiceImpl;
import fr.pantheonsorbonne.ufr27.miage.service.impl.TrainServiceImpl;
import fr.pantheonsorbonne.ufr27.miage.tests.utils.TestPersistenceProducer;

@EnableWeld
class TestArrretService {
	@WeldSetup
	private WeldInitiator weld = WeldInitiator
			.from(TrainService.class, TrainServiceImpl.class, TrainEndPoint.class, ArretService.class,
					ArretEndPoint.class, ArretServiceImpl.class, TrainDAO.class, ArretDAO.class,
					HeureDePassageDAO.class, PassagerDAO.class, TestPersistenceProducer.class)
			.activate(RequestScoped.class).build();

	@Inject
	EntityManager em;

	@Inject
	ArretService arretService;

	@Inject
	TrainService trainService;

	@Inject
	TrainDAO trainDao;

	@Inject
	ArretDAO dao;

	static ObjectFactory factory;

	Arret arret1;
	Train train1;
	Arret arretArrivee;

	int idArretA;
	int idTrain;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {

		factory = new ObjectFactory();
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {

	}

	@BeforeEach
	void setUp() throws Exception {
		arret1 = factory.createArret();
		arret1.setNom("Deauville");

		arretArrivee = factory.createArret();
		arretArrivee.setNom("Marseille");
		idArretA = arretService.createArret(arretArrivee);
		arretArrivee.setId(idArretA);

		train1 = factory.createTrainAvecResa();
		train1.setNom("Bordeaux - Paris");
		train1.setDirectionType("forward");
		train1.setStatut("enmarche");
		train1.setNumeroTrain(8541);
		train1.setReseau("SNCF");
		train1.setStatut("en marche");
		idTrain = trainService.createTrain(train1);
		train1.setId(idTrain);

	}

	@AfterEach
	void tearDown() throws Exception {
		trainService.deleteTrain(idTrain);
		arretService.deleteArret(idArretA);

	}

	@Test
	void testCreateArret() throws CantCreateException, NoSuchArretException, CantDeleteException {
		int idArret = arretService.createArret(arret1);
		assertEquals(arret1.getNom(), arretService.getArretFromId(idArret).getNom());
		arretService.deleteArret(idArret);
	}

	@Test
	void testGetArretFromId() throws NoSuchArretException, CantCreateException, CantDeleteException {
		int idArret = arretService.createArret(arret1);
		Arret arretResultat = arretService.getArretFromId(idArret);
		assertEquals(arret1.getNom(), arretResultat.getNom());
		arretService.deleteArret(idArret);
	}

	@Test
	void testGetAllArret() throws EmptyListException, CantCreateException, NoSuchArretException, CantDeleteException {
		List<Arret> arrets = arretService.getAllArret();
		assertEquals(arrets.size(), 1);
		int idArret = arretService.createArret(arret1);
		arrets = arretService.getAllArret();
		assertFalse(arrets.isEmpty());
		assertEquals(arrets.size(), 2);
		assertEquals(arrets.get(1).getNom(), "Deauville");
		arretService.deleteArret(idArret);

	}

	@Test
	void testUpdateArret() throws CantCreateException, NoSuchArretException, CantUpdateException, CantDeleteException {
		int idArret = arretService.createArret(arret1);
		arret1.setId(idArret);
		assertEquals(arret1.getNom(), arretService.getArretFromId(idArret).getNom());
		arret1.setNom("Lyon");
		assertNotEquals(arret1.getNom(), arretService.getArretFromId(idArret).getNom());
		arretService.updateArret(arret1);
		assertEquals(arret1.getNom(), arretService.getArretFromId(idArret).getNom());
		arretService.deleteArret(idArret);

	}

	@Test
	void testDeleteArret() throws CantCreateException, NoSuchArretException, CantDeleteException {
		int idArret = arretService.createArret(arret1);
		arret1.setId(idArret);
		assertEquals(idArret, dao.getArretFromId(idArret).getId());
		arretService.deleteArret(idArret);
		assertNull(dao.getArretFromId(idArret));

	}

	@Test
	void testGetAllArretByTrain()
			throws CantCreateException, NoSuchTrainException, NoSuchArretException, CantDeleteException {
		int idArret = arretService.createArret(arret1);
		arret1.setId(idArret);
		em.getTransaction().begin();
		trainDao.addArret(trainDao.getTrainFromId(idTrain), dao.getArretFromId(idArret), LocalDateTime.now(),
				LocalDateTime.now().plusMinutes(10), true, false);
		em.getTransaction().commit();
		List<Arret> arrets = arretService.getAllArretByTrain(train1.getId());
		assertEquals(arrets.size(), 1);
		assertEquals(arrets.get(0).getNom(), "Deauville");
		arretService.deleteArret(idArret);
	}

}
