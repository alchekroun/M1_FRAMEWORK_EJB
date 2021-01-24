package fr.pantheonsorbonne.ufr27.miage.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
import fr.pantheonsorbonne.ufr27.miage.dao.PerturbationDAO;
import fr.pantheonsorbonne.ufr27.miage.dao.TrainDAO;
import fr.pantheonsorbonne.ufr27.miage.exception.CantCreateException;
import fr.pantheonsorbonne.ufr27.miage.exception.CantUpdateException;
import fr.pantheonsorbonne.ufr27.miage.exception.EmptyListException;
import fr.pantheonsorbonne.ufr27.miage.exception.NoSuchPassagerException;
import fr.pantheonsorbonne.ufr27.miage.exception.NoSuchTrainException;
import fr.pantheonsorbonne.ufr27.miage.mapper.ArretMapper;
import fr.pantheonsorbonne.ufr27.miage.mapper.PassagerMapper;
import fr.pantheonsorbonne.ufr27.miage.model.jaxb.Arret;
import fr.pantheonsorbonne.ufr27.miage.model.jaxb.ObjectFactory;
import fr.pantheonsorbonne.ufr27.miage.model.jaxb.Passager;
import fr.pantheonsorbonne.ufr27.miage.model.jaxb.Train;
import fr.pantheonsorbonne.ufr27.miage.resource.ArretEndPoint;
import fr.pantheonsorbonne.ufr27.miage.resource.PassagerEndPoint;
import fr.pantheonsorbonne.ufr27.miage.resource.TrainEndPoint;
import fr.pantheonsorbonne.ufr27.miage.service.impl.ArretServiceImpl;
import fr.pantheonsorbonne.ufr27.miage.service.impl.PassagerServiceImpl;
import fr.pantheonsorbonne.ufr27.miage.service.impl.TrainServiceImpl;
import fr.pantheonsorbonne.ufr27.miage.tests.utils.TestPersistenceProducer;

@EnableWeld
class TestPassagerService {
	@WeldSetup
	private WeldInitiator weld = WeldInitiator
			.from(ArretMapper.class, PassagerMapper.class, PassagerService.class, PassagerServiceImpl.class,
					PassagerEndPoint.class, TrainService.class, TrainEndPoint.class, TrainServiceImpl.class,
					ArretService.class, ArretEndPoint.class, ArretServiceImpl.class, TrainDAO.class, ArretDAO.class,
					HeureDePassageDAO.class, PassagerDAO.class, PerturbationDAO.class, TestPersistenceProducer.class)
			.activate(RequestScoped.class).build();

	@Inject
	EntityManager em;

	@Inject
	PassagerService passagerService;

	@Inject
	ArretService arretService;

	@Inject
	TrainService trainService;

	@Inject
	PassagerDAO dao;

	@Inject
	ArretDAO arretDao;

	@Inject
	TrainDAO trainDao;

	Passager passager1;
	Arret arretDepart;
	Arret arretArrivee;

	int idArretD;
	int idArretA;
	int idTrain;

	static ObjectFactory factory;

	Arret arret1;
	Train train1;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {

		factory = new ObjectFactory();
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {

	}

	@BeforeEach
	void setUp() throws Exception {
		arretArrivee = factory.createArret();
		arretArrivee.setNom("Marseille");
		idArretA = arretService.createArret(arretArrivee);
		arretArrivee.setId(idArretA);

		arretDepart = factory.createArret();
		arretDepart.setNom("Paris");
		idArretD = arretService.createArret(arretDepart);
		arretDepart.setId(idArretD);

		train1 = factory.createTrainAvecResa();
		train1.setNom("Bordeaux - Paris");
		train1.setStatut("on");
		train1.setNumeroTrain(8541);
		train1.setReseau("SNCF");
		idTrain = trainService.createTrain(train1);
		train1.setId(idTrain);

		passager1 = factory.createPassager();
		passager1.setNom("David Serruya");
		passager1.setArrive(arretArrivee);
		passager1.setDepart(arretDepart);

	}

	@AfterEach
	void tearDown() throws Exception {
		trainService.deleteTrain(idTrain);
		/*
		 * arretService.deleteArret(idArretA); arretService.deleteArret(idArretD);
		 */
	}

	@Test
	void testCreatePassager() {
		try {
			int idPassager = passagerService.createPassager(passager1);
			fr.pantheonsorbonne.ufr27.miage.jpa.Passager pJPA = dao.getPassagerFromId(idPassager);
			assertEquals(pJPA.getNom(), passager1.getNom());
			assertEquals(pJPA.getArrive().getNom(), passager1.getArrive().getNom());
			assertEquals(pJPA.getDepart().getNom(), passager1.getDepart().getNom());
			assertEquals(pJPA.getCorrespondance(), passager1.getCorrespondance()); // Null
			passagerService.deletePassager(idPassager);
		} catch (CantCreateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPassagerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	void testGetPassagerFromId() throws CantCreateException, NoSuchPassagerException {
		try {
			int idPassager = passagerService.createPassager(passager1);
			assertEquals(dao.getPassagerFromId(idPassager).getNom(), passager1.getNom());
			passagerService.deletePassager(idPassager);
		} catch (CantCreateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPassagerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	void testGetAllPassager() {
		try {
			List<Passager> listP = passagerService.getAllPassager();
			assertEquals(listP.size(), 0);
			assertTrue(listP.isEmpty());
			int idPassager = passagerService.createPassager(passager1);
			listP = passagerService.getAllPassager();
			assertEquals(listP.size(), 1);
			assertEquals(listP.get(0).getNom(), passager1.getNom());
			passagerService.deletePassager(idPassager);
		} catch (CantCreateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPassagerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (EmptyListException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Test
	void testDeletePassager() {
		try {
			int idPassager = passagerService.createPassager(passager1);
			assertEquals(dao.getPassagerFromId(idPassager).getNom(), passager1.getNom());
			passagerService.deletePassager(idPassager);
			assertNull(dao.getPassagerFromId(idPassager));
		} catch (CantCreateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPassagerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	void testGetAllPassagerByTrain() {
		try {
			List<Passager> listPassager = passagerService.getAllPassagerByTrain(train1.getId());
			assertEquals(listPassager.size(), 0);
			assertTrue(listPassager.isEmpty());
			int idPassager = passagerService.createPassager(passager1);
			em.getTransaction().begin();
			trainDao.addPassager(trainDao.getTrainFromId(idTrain), dao.getPassagerFromId(idPassager));
			em.getTransaction().commit();
			listPassager = passagerService.getAllPassagerByTrain(idTrain);
			assertEquals(listPassager.size(), 1);
			assertEquals(listPassager.get(0).getNom(), "David Serruya");
			passagerService.deletePassager(idPassager);
		} catch (CantCreateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPassagerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchTrainException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	void testUpdatePassager() {
		try {
			int idPassager = passagerService.createPassager(passager1);
			passager1.setId(idPassager);
			assertEquals(passager1.getNom(), passagerService.getPassagerFromId(idPassager).getNom());
			passager1.setNom("Alexandre Chekroun");
			assertNotEquals(passager1.getNom(), passagerService.getPassagerFromId(idPassager).getNom());
			passagerService.updatePassager(passager1);
			assertEquals("Alexandre Chekroun", passagerService.getPassagerFromId(idPassager).getNom());
			assertEquals(passager1.getNom(), passagerService.getPassagerFromId(idPassager).getNom());
			passagerService.deletePassager(idPassager);
		} catch (CantCreateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPassagerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CantUpdateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
