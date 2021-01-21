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
import fr.pantheonsorbonne.ufr27.miage.dao.PerturbationDAO;
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
import fr.pantheonsorbonne.ufr27.miage.model.jaxb.HeureDePassage;
import fr.pantheonsorbonne.ufr27.miage.model.jaxb.ObjectFactory;
import fr.pantheonsorbonne.ufr27.miage.model.jaxb.Passager;
import fr.pantheonsorbonne.ufr27.miage.model.jaxb.Perturbation;
import fr.pantheonsorbonne.ufr27.miage.model.jaxb.Train;
import fr.pantheonsorbonne.ufr27.miage.model.jaxb.TrainAvecResa;
import fr.pantheonsorbonne.ufr27.miage.resource.ArretEndPoint;
import fr.pantheonsorbonne.ufr27.miage.resource.PassagerEndPoint;
import fr.pantheonsorbonne.ufr27.miage.resource.TrainEndPoint;
import fr.pantheonsorbonne.ufr27.miage.service.impl.ArretServiceImpl;
import fr.pantheonsorbonne.ufr27.miage.service.impl.PassagerServiceImpl;
import fr.pantheonsorbonne.ufr27.miage.service.impl.TrainServiceImpl;
import fr.pantheonsorbonne.ufr27.miage.tests.utils.TestPersistenceProducer;

@EnableWeld
class TestTrainService {
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
	void testCreateTrain() {
		try {
			int idTrain = trainService.createTrain(train1);
			assertEquals(train1.getNom(), trainService.getTrainFromId(idTrain).getNom());
			trainService.deleteTrain(idTrain);
		} catch (CantCreateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchTrainException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	void testGetTrainFromId() {
		try {
			int idTrain = trainService.createTrain(train1);
			assertEquals(train1.getNom(), trainService.getTrainFromId(idTrain).getNom());
			trainService.deleteTrain(idTrain);
		} catch (CantCreateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchTrainException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	void testDeleteTrain() {
		try {
			int idTrain = trainService.createTrain(train1);
			assertEquals(train1.getNom(), trainService.getTrainFromId(idTrain).getNom());
			trainService.deleteTrain(idTrain);
			assertNull(dao.getTrainFromId(idTrain));
		} catch (CantCreateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchTrainException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	void testUpdateTrain() {
		try {
			int idTrain = trainService.createTrain(train1);
			train1.setId(idTrain);
			assertEquals(train1.getNom(), trainService.getTrainFromId(idTrain).getNom());
			train1.setNom("Orleans");
			assertNotEquals(train1.getNom(), trainService.getTrainFromId(train1.getId()).getNom());
			trainService.updateTrain(train1);
			assertEquals("Orleans", trainService.getTrainFromId(idTrain).getNom());
			assertEquals(train1.getNom(), trainService.getTrainFromId(idTrain).getNom());
			trainService.deleteTrain(idTrain);
		} catch (CantCreateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchTrainException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CantUpdateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	void testGetAllTrain() {
		try {
			List<Train> trains = trainService.getAllTrain();
			assertEquals(trains.size(), 0);
			int idTrain = trainService.createTrain(train1);
			assertFalse(trainService.getAllTrain().isEmpty());
			assertEquals(trainService.getAllTrain().size(), 1);
			assertEquals(trainService.getAllTrain().get(0).getNom(), "Bordeaux - Paris");
			trainService.deleteTrain(idTrain);
		} catch (CantCreateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchTrainException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (EmptyListException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	void testAddArret() {
		try {
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
		} catch (CantCreateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchTrainException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchArretException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CantDeleteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Test
	void testRemoveArret() throws CantCreateException, NoSuchTrainException, NoSuchArretException, CantDeleteException {
		try {
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
		} catch (CantCreateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchTrainException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchArretException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CantDeleteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	void testChangeParameterDesservi() {
		try {

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
		} catch (CantCreateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchTrainException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchArretException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchHdpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CantDeleteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	void testCreatePertubation()
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
		assertEquals(dt1, hdp1.getBaseDepartTemps());
		assertEquals(dt2, hdp1.getBaseArriveeTemps());
		assertEquals(dt1, hdp1.getReelDepartTemps());
		assertEquals(dt2, hdp1.getReelArriveeTemps());
		assertEquals(hdp1.getTrain().getId(), train1.getId());

		Perturbation perturbation1 = new Perturbation();
		fr.pantheonsorbonne.ufr27.miage.jpa.Train train = dao.getTrainFromId(idTrain);

		assertTrue(pertuDao.getPerturbationByTrain(train).isEmpty());

		perturbation1.setMotif("chevreuil");
		perturbation1.setTrain(train1);
		perturbation1.setDureeEnPlus(10);
		trainService.createPerturbation(perturbation1);

		assertEquals(pertuDao.getPerturbationByTrain(train).get(0).getId(),
				pertuDao.getPerturbationFromId(pertuDao.getPerturbationByTrain(train).get(0).getId()).getId());
		assertEquals("chevreuil",
				pertuDao.getPerturbationFromId(pertuDao.getPerturbationByTrain(train).get(0).getId()).getMotif());
		assertEquals(10,
				pertuDao.getPerturbationFromId(pertuDao.getPerturbationByTrain(train).get(0).getId()).getDureeEnPlus());
		assertEquals(idTrain, pertuDao.getPerturbationFromId(pertuDao.getPerturbationByTrain(train).get(0).getId())
				.getTrain().getId());

		assertEquals(dt1, hdp1.getBaseDepartTemps());
		assertEquals(dt2, hdp1.getBaseArriveeTemps());
		assertEquals(dt1.plusMinutes(10), hdp1.getReelDepartTemps());
		assertEquals(dt2.plusMinutes(10), hdp1.getReelArriveeTemps());
		assertEquals(hdp1.getTrain().getId(), train1.getId());

		arretService.deleteArret(idArret);
		trainService.deleteTrain(idTrain);

	}

//	@Test
//	public void testEnMarche() {
//		// TODO
//		fail("todo");
//	}

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
	
	
	@Test
	public void testRetarderCorrespondance() throws NoSuchTrainException, CantCreateException, NoSuchArretException, CantDeleteException, NoSuchPassagerException {
		
		Train train2 = factory.createTrainAvecResa();
		train2.setNom("Bordeaux - Paris - Marseille");
		train2.setDirectionType("forward");
		train2.setStatut("enmarche");
		train2.setNumeroTrain(8621);
		train2.setReseau("SNCF");
		train2.setStatut("en marche");
		
		Arret arret3 = factory.createArret();
		arret3.setNom("Marseille");
		
		List<Passager> listPassager = new ArrayList<Passager>();
		for(int i=0; i<=49; i++) {
			Passager p = factory.createPassager();
			p.setArrive(arret3);
			p.setDepart(arret1);
			listPassager.add(p);
		}
		
		List<Integer> listIdPassager = new ArrayList<Integer>();;
		for(Passager p : listPassager) {
			listIdPassager.add(passagerService.createPassager(p));
		}

		int idTrain1 = trainService.createTrain(train1);
		int idTrain2 = trainService.createTrain(train2);
		int idArret1 = arretService.createArret(arret1);
		int idArret2 = arretService.createArret(arret2);
		int idArret3 = arretService.createArret(arret3);
		
		arret1.setId(idArret1);
		arret2.setId(idArret2);
		arret3.setId(idArret3);
		train1.setId(idTrain1);
		train2.setId(idTrain2);
		
		for(int i=0; i<listIdPassager.size(); i++) {
			listPassager.get(i).setId(listIdPassager.get(i));
		}
		
		em.getTransaction().begin();
		trainService.monterListPassager(listPassager, dao.getTrainFromId(idTrain2));
		em.getTransaction().commit();
		
		LocalDateTime dt1 = LocalDateTime.now().minusMinutes(50);
		LocalDateTime dt2 = dt1.plusMinutes(10);
		String passage = dt1.toString() + " " + dt2.toString();
		String passage2 = dt1.plusMinutes(41).toString() + " " + dt2.plusMinutes(60).toString();
		String passage5 = dt1.plusMinutes(200).toString() + " " + dt2.plusMinutes(240).toString();
		trainService.addArret(train1.getId(), arret1.getId(), passage, true, false);
		fr.pantheonsorbonne.ufr27.miage.jpa.HeureDePassage hdp1 = hdpDao.getHdpFromTrainIdAndArretId(train1.getId(),
				arret1.getId());
		trainService.addArret(train1.getId(), arret2.getId(), passage2, true, false);
		fr.pantheonsorbonne.ufr27.miage.jpa.HeureDePassage hdp2 = hdpDao.getHdpFromTrainIdAndArretId(train1.getId(),
				arret2.getId());
		trainService.addArret(train1.getId(), arret3.getId(), passage5, true, false);
		fr.pantheonsorbonne.ufr27.miage.jpa.HeureDePassage hdp5 = hdpDao.getHdpFromTrainIdAndArretId(train1.getId(),
				arret3.getId());
		
		
		LocalDateTime dt3 = dt1.plusMinutes(20);
		LocalDateTime dt4 = dt1.plusMinutes(30);
		String passage3 = dt3.toString() + " " + dt4.toString();
		String passage4 = dt3.plusMinutes(40).toString() + " " + dt4.plusMinutes(80).toString();
		trainService.addArret(train2.getId(), arret1.getId(), passage3, true, false);
		fr.pantheonsorbonne.ufr27.miage.jpa.HeureDePassage hdp3 = hdpDao.getHdpFromTrainIdAndArretId(train2.getId(),
				arret1.getId());
		trainService.addArret(train2.getId(), arret2.getId(), passage4, true, false);
		fr.pantheonsorbonne.ufr27.miage.jpa.HeureDePassage hdp4 = hdpDao.getHdpFromTrainIdAndArretId(train2.getId(),
				arret2.getId());

		for(Passager p : listPassager) {
			passagerDao.findTrajet(p.getId());
		}
		
		assertTrue(hdp2.getReelArriveeTemps().compareTo(hdp4.getReelArriveeTemps())<0);
		assertTrue(hdp2.getReelDepartTemps().compareTo(hdp4.getReelDepartTemps())<0);
		
		Perturbation perturbation1 = new Perturbation();
		
		perturbation1.setMotif("chevreuil");
		perturbation1.setTrain(train2);
		perturbation1.setDureeEnPlus(100);
		trainService.createPerturbation(perturbation1);
		
		trainService.retarderCorrespondance(train1);
		
		fr.pantheonsorbonne.ufr27.miage.jpa.HeureDePassage hdpTrain1 = hdpDao.getHdpFromTrainIdAndArretId(train1.getId(),
				arret2.getId());
		
		fr.pantheonsorbonne.ufr27.miage.jpa.HeureDePassage hdpTrain2 = hdpDao.getHdpFromTrainIdAndArretId(train2.getId(),
				arret2.getId());
		
		assertTrue(hdpTrain1.getReelArriveeTemps().plusMinutes(10)!=hdpTrain1.getReelArriveeTemps());
		assertTrue(hdpTrain1.getReelDepartTemps().plusMinutes(10)!=hdpTrain1.getReelDepartTemps());
		
		Passager p = factory.createPassager();
		listPassager.add(p);
		p.setArrive(arret3);
		p.setDepart(arret1);
		listIdPassager.add(passagerService.createPassager(p));
		listPassager.get(51).setId(listIdPassager.get(51));
		
		passagerDao.findTrajet(p.getId());
		
		trainService.retarderCorrespondance(train1);
		assertEquals(hdpTrain1.getReelArriveeTemps().plusMinutes(10),hdpTrain1.getReelArriveeTemps());
		assertEquals(hdpTrain1.getReelDepartTemps().plusMinutes(10),hdpTrain1.getReelDepartTemps());
		
		for(int i=0; i<listIdPassager.size(); i++) {
			passagerService.deletePassager(listIdPassager.get(i));
		}
		
		arretService.deleteArret(idArret1);
		arretService.deleteArret(idArret2);
		arretService.deleteArret(idArret3);
		trainService.deleteTrain(idTrain1);
		trainService.deleteTrain(idTrain2);
	}
}
