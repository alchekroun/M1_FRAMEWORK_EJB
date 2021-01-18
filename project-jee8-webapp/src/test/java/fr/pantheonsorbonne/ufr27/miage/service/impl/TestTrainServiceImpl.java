package fr.pantheonsorbonne.ufr27.miage.service.impl;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
import fr.pantheonsorbonne.ufr27.miage.exception.CantDeleteException;
import fr.pantheonsorbonne.ufr27.miage.exception.NoSuchArretException;
import fr.pantheonsorbonne.ufr27.miage.exception.NoSuchPassagerException;
import fr.pantheonsorbonne.ufr27.miage.exception.NoSuchTrainException;
import fr.pantheonsorbonne.ufr27.miage.mapper.ArretMapper;
import fr.pantheonsorbonne.ufr27.miage.mapper.PassagerMapper;
import fr.pantheonsorbonne.ufr27.miage.model.jaxb.Arret;
import fr.pantheonsorbonne.ufr27.miage.model.jaxb.HeureDePassage;
import fr.pantheonsorbonne.ufr27.miage.model.jaxb.ObjectFactory;
import fr.pantheonsorbonne.ufr27.miage.model.jaxb.Passager;
import fr.pantheonsorbonne.ufr27.miage.model.jaxb.Train;
import fr.pantheonsorbonne.ufr27.miage.resource.ArretEndPoint;
import fr.pantheonsorbonne.ufr27.miage.resource.PassagerEndPoint;
import fr.pantheonsorbonne.ufr27.miage.resource.TrainEndPoint;
import fr.pantheonsorbonne.ufr27.miage.service.ArretService;
import fr.pantheonsorbonne.ufr27.miage.service.PassagerService;
import fr.pantheonsorbonne.ufr27.miage.service.TrainService;
import fr.pantheonsorbonne.ufr27.miage.tests.utils.TestPersistenceProducer;

@EnableWeld
class TestTrainServiceImpl  {
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
	TrainServiceImpl trainService;

	@Inject
	TrainDAO dao;

	@Inject
	PassagerDAO passagerDao;

	@Inject
	ArretDAO dao2;

	@Inject
	HeureDePassageDAO hdpDao;

	@Inject
	PerturbationDAO pertuDao;

	Train train1;
	Arret arret1;
	Arret arret2;
	Arret arretDirection;
	Passager passager1;
	Passager passager2;
	int idArretDirection;
	int idPassager1;
	int idPassager2;

	List<Passager> listePassagers;

	static ObjectFactory factory;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {

		factory = new ObjectFactory();

	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {

	}

	@BeforeEach
	void setUp() throws Exception {
		System.out.println("\n== SetUp");

		listePassagers = new ArrayList<Passager>();

		arret1 = factory.createArret();
		arret1.setNom("Bordeaux");

		arret2 = factory.createArret();
		arret2.setNom("Paris");

		arretDirection = factory.createArret();
		arretDirection.setNom("Paris");
		idArretDirection = arretService.createArret(arretDirection);
		arretDirection.setId(idArretDirection);
		train1 = factory.createTrainAvecResa();
		train1.setNom("Bordeaux - Paris");
		train1.setDirectionType("forward");
		train1.setStatut("enmarche");
		train1.setNumeroTrain(8541);
		train1.setReseau("SNCF");
		train1.setStatut("en marche");

		passager1 = factory.createPassager();
		passager1.setNom("Hanna");
		passager1.setArrive(arret1);
		passager1.setDepart(arretDirection);

		passager2 = factory.createPassager();
		passager2.setNom("David");
		passager2.setArrive(arret1);
		passager2.setDepart(arretDirection);

	}

	@AfterEach
	void tearDown() throws NoSuchPassagerException {
		try {
			arretService.deleteArret(idArretDirection);
		} catch (NoSuchArretException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CantDeleteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	@Test
	public void testDescendreListPassager() throws CantCreateException, NoSuchPassagerException, NoSuchTrainException {

		idPassager1 = passagerService.createPassager(passager1);
		idPassager2 = passagerService.createPassager(passager2);
		int idTrain = trainService.createTrain(train1);

		train1.setId(idTrain);
		passager1.setId(idPassager1);
		passager2.setId(idPassager2);

		assertEquals(dao.getTrainFromId(idTrain).getListePassagers().size(), 0);

		List<Passager> passagers = new ArrayList<Passager>();
		passagers.add(passager1);
		passagers.add(passager2);

		em.getTransaction().begin();
		dao.addPassager(dao.getTrainFromId(idTrain), passagerDao.getPassagerFromId(idPassager1));
		dao.addPassager(dao.getTrainFromId(idTrain), passagerDao.getPassagerFromId(idPassager2));
		em.getTransaction().commit();

		assertEquals(passagerDao.getAllPassagerByTrain(idTrain).size(), 2);
		assertEquals(dao.getTrainFromId(idTrain).getListePassagers().size(), 2);

		em.getTransaction().begin();
		trainService.descendreListPassager(passagers, dao.getTrainFromId(idTrain));
		em.getTransaction().commit();

		assertEquals(dao.getTrainFromId(idTrain).getListePassagers().size(), 0);
		assertEquals(passagerDao.getAllPassagerByTrain(idTrain).size(), 0);

		passagerService.deletePassager(idPassager1);
		passagerService.deletePassager(idPassager2);
		trainService.deleteTrain(idTrain);

	}

	@Test
	public void testMonterListPassager() throws CantCreateException, NoSuchTrainException, NoSuchArretException,
			CantDeleteException, NoSuchPassagerException {

		idPassager1 = passagerService.createPassager(passager1);
		idPassager2 = passagerService.createPassager(passager2);
		int idTrain = trainService.createTrain(train1);

		train1.setId(idTrain);
		passager1.setId(idPassager1);
		passager2.setId(idPassager2);

		assertEquals(dao.getTrainFromId(idTrain).getListePassagers().size(), 0);
		assertEquals(passagerDao.getAllPassagerByTrain(idTrain).size(), 0);

		List<Passager> passagers = new ArrayList<Passager>();
		passagers.add(passager1);
		passagers.add(passager2);

		em.getTransaction().begin();
		trainService.monterListPassager(passagers, dao.getTrainFromId(idTrain));
		em.getTransaction().commit();

		assertEquals(passagerDao.getAllPassagerByTrain(idTrain).size(), 2);
		assertEquals(dao.getTrainFromId(idTrain).getListePassagers().size(), 2);

		passagerService.deletePassager(idPassager1);
		passagerService.deletePassager(idPassager2);
		trainService.deleteTrain(idTrain);

	}

	@Test
	public void testVerifIfExistArretNow()
			throws CantCreateException, NoSuchTrainException, NoSuchArretException, CantDeleteException {

		int idTrain = trainService.createTrain(train1);
		int idArret = arretService.createArret(arret1);
		arret1.setId(idArret);
		train1.setId(idTrain);
		LocalDateTime dt1 = LocalDateTime.now();
		LocalDateTime dt2 = LocalDateTime.now().plusMinutes(10);
		String passage = dt1.toString() + " " + dt2.toString();
		trainService.addArret(train1.getId(), arret1.getId(), passage, true, false);
		fr.pantheonsorbonne.ufr27.miage.jpa.HeureDePassage hdp1 = hdpDao.getHdpFromTrainIdAndArretId(train1.getId(),
				arret1.getId());
		HeureDePassage hdp = trainService.verifIfExistArretNow(idTrain);
		assertEquals(dt1, hdp.getReelDepartTemps());
		assertEquals(dt2, hdp.getReelArriveeTemps());

		arretService.deleteArret(idArret);
		trainService.deleteTrain(idTrain);

	}

}