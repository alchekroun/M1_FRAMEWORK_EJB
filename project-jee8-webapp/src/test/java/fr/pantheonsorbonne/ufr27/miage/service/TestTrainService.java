package fr.pantheonsorbonne.ufr27.miage.service;

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
import fr.pantheonsorbonne.ufr27.miage.dao.TrainDAO;
import fr.pantheonsorbonne.ufr27.miage.exception.CantCreateException;
import fr.pantheonsorbonne.ufr27.miage.exception.CantDeleteException;
import fr.pantheonsorbonne.ufr27.miage.exception.CantUpdateException;
import fr.pantheonsorbonne.ufr27.miage.exception.EmptyListException;
import fr.pantheonsorbonne.ufr27.miage.exception.NoSuchArretException;
import fr.pantheonsorbonne.ufr27.miage.exception.NoSuchHdpException;
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
import fr.pantheonsorbonne.ufr27.miage.mapper.TrainMapper;
import fr.pantheonsorbonne.ufr27.miage.mapper.ArretMapper;

@EnableWeld
class TestTrainService {
	@WeldSetup
	private WeldInitiator weld = WeldInitiator
			.from(ArretMapper.class, PassagerMapper.class, PassagerService.class, PassagerServiceImpl.class,
					PassagerEndPoint.class, TrainService.class, TrainEndPoint.class, TrainServiceImpl.class,
					ArretService.class, ArretEndPoint.class, ArretServiceImpl.class, TrainDAO.class, ArretDAO.class,
					HeureDePassageDAO.class, PassagerDAO.class, TestPersistenceProducer.class)
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
	TrainDAO dao;

	@Inject
	ArretDAO dao2;

	@Inject
	HeureDePassageDAO hdpDao;

	Train train1;
	Arret arret1;
	Arret arretDirection;
	int idArretDirection;

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

		arret1 = factory.createArret();
		arret1.setNom("Deauville");

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

	}

	@AfterEach
	void tearDown() throws Exception {
		arretService.deleteArret(idArretDirection);
	}

	@Test
	void testCreateTrain() throws CantCreateException, NoSuchTrainException {
		int idTrain = trainService.createTrain(train1);
		assertEquals(train1.getNom(), trainService.getTrainFromId(idTrain).getNom());
		trainService.deleteTrain(idTrain);
	}

	@Test
	void testGetTrainFromId() throws CantCreateException, NoSuchTrainException {
		int idTrain = trainService.createTrain(train1);
		assertEquals(train1.getNom(), trainService.getTrainFromId(idTrain).getNom());
		trainService.deleteTrain(idTrain);
	}

	@Test
	void testDeleteTrain() throws CantCreateException, NoSuchTrainException {
		int idTrain = trainService.createTrain(train1);
		assertEquals(train1.getNom(), trainService.getTrainFromId(idTrain).getNom());
		trainService.deleteTrain(idTrain);
		assertNull(dao.getTrainFromId(idTrain));
	}

	@Test
	void testUpdateTrain() throws NoSuchTrainException, CantCreateException, CantUpdateException {
		int idTrain = trainService.createTrain(train1);
		train1.setId(idTrain);
		assertEquals(train1.getNom(), trainService.getTrainFromId(idTrain).getNom());
		train1.setNom("Orleans");
		assertNotEquals(train1.getNom(), trainService.getTrainFromId(train1.getId()).getNom());
		trainService.updateTrain(train1);
		assertEquals("Orleans", trainService.getTrainFromId(idTrain).getNom());
		assertEquals(train1.getNom(), trainService.getTrainFromId(idTrain).getNom());
		trainService.deleteTrain(idTrain);
	}

	@Test
	void testGetAllTrain() throws EmptyListException, CantCreateException, NoSuchTrainException {
		List<Train> trains = trainService.getAllTrain();
		assertEquals(trains.size(), 0);
		int idTrain = trainService.createTrain(train1);
		assertFalse(trainService.getAllTrain().isEmpty());
		assertEquals(trainService.getAllTrain().size(), 1);
		assertEquals(trainService.getAllTrain().get(0).getNom(), "Bordeaux - Paris");
		trainService.deleteTrain(idTrain);
	}

	@Test
	void testAddArret() throws NoSuchTrainException, NoSuchArretException, CantCreateException, CantDeleteException {
		int idTrain = trainService.createTrain(train1);
		int idArret = arretService.createArret(arret1);
		arret1.setId(idArret);
		train1.setId(idTrain);
		String passage = LocalDateTime.now().toString() + " " + LocalDateTime.now().plusMinutes(10).toString();
		trainService.addArret(train1.getId(), arret1.getId(), passage, true, false);
		String[] passages = passage.split(" ");
		fr.pantheonsorbonne.ufr27.miage.jpa.HeureDePassage hdp1 = hdpDao.getHdpFromTrainIdAndArretId(train1.getId(),
				arret1.getId());
		fr.pantheonsorbonne.ufr27.miage.jpa.Arret arret2 = hdp1.getArret();
		fr.pantheonsorbonne.ufr27.miage.jpa.Train train2 = hdp1.getTrain();
		assertEquals(arret1.getId(), arret2.getId());
		assertEquals(train1.getId(), train2.getId());
		assertEquals(arret1.getNom(), arret2.getNom());
		assertEquals(train1.getDirectionType(), train2.getDirectionType());
		assertEquals(train1.getNumeroTrain(), train2.getNumero());
		assertEquals(passages[0], hdp1.getBaseDepartTemps().toString());
		assertEquals(passages[1], hdp1.getBaseArriveeTemps().toString());
		trainService.removeArret(trainService.getTrainFromId(idTrain).getId(),
				arretService.getArretFromId(idArret).getId());
		arretService.deleteArret(idArret);
		trainService.deleteTrain(idTrain);

	}

	@Test
	void testRemoveArret() throws CantCreateException, NoSuchTrainException, NoSuchArretException, CantDeleteException {
		int idTrain = trainService.createTrain(train1);
		int idArret = arretService.createArret(arret1);
		arret1.setId(idArret);
		train1.setId(idTrain);
		String passage = LocalDateTime.now().toString() + " " + LocalDateTime.now().plusMinutes(10).toString();
		trainService.addArret(train1.getId(), arret1.getId(), passage, true, false);
		List<fr.pantheonsorbonne.ufr27.miage.jpa.HeureDePassage> hdps = hdpDao.getAllHeureDePassage();
		int arret = 0;
		for (fr.pantheonsorbonne.ufr27.miage.jpa.HeureDePassage hdp : hdps) {
			if (hdp.getArret().getId() == idArret && hdp.getTrain().getId() == idTrain) {
				arret++;
			}
		}
		assertEquals(arret, 1);
		trainService.removeArret(trainService.getTrainFromId(idTrain).getId(),
				arretService.getArretFromId(idArret).getId());
		hdps.clear();
		arret = 0;
		for (fr.pantheonsorbonne.ufr27.miage.jpa.HeureDePassage hdp : hdps) {
			if (hdp.getArret().getId() == idArret && hdp.getTrain().getId() == idTrain) {
				arret++;
			}
		}
		assertEquals(arret, 0);
		arretService.deleteArret(idArret);
		trainService.deleteTrain(idTrain);
	}

	@Test
	void testChangeParameterDesservi() throws CantCreateException, NoSuchTrainException, NoSuchArretException,
			NoSuchHdpException, CantDeleteException {
		int idTrain = trainService.createTrain(train1);
		int idArret = arretService.createArret(arret1);
		arret1.setId(idArret);
		train1.setId(idTrain);
		String passage = LocalDateTime.now().toString() + " " + LocalDateTime.now().plusMinutes(10).toString();
		trainService.addArret(train1.getId(), arret1.getId(), passage, false, false);
		fr.pantheonsorbonne.ufr27.miage.jpa.HeureDePassage hdp1 = hdpDao.getHdpFromTrainIdAndArretId(train1.getId(),
				arret1.getId());
		assertEquals(hdp1.isDesservi(), false);
		trainService.changeParameterDesservi(train1.getId(), arret1.getId(), true);
		hdp1 = hdpDao.getHdpFromTrainIdAndArretId(train1.getId(), arret1.getId());
		assertEquals(hdp1.isDesservi(), true);
		arretService.deleteArret(idArret);
		trainService.deleteTrain(idTrain);
	}

}
