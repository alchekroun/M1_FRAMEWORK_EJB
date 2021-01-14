
package fr.pantheonsorbonne.ufr27.miage.dao;


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
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.pantheonsorbonne.ufr27.miage.jpa.Arret;
import fr.pantheonsorbonne.ufr27.miage.jpa.HeureDePassage;
import fr.pantheonsorbonne.ufr27.miage.jpa.HeureDePassageKey;
import fr.pantheonsorbonne.ufr27.miage.jpa.Passager;
import fr.pantheonsorbonne.ufr27.miage.jpa.Train;
import fr.pantheonsorbonne.ufr27.miage.jpa.TrainAvecResa;
import fr.pantheonsorbonne.ufr27.miage.model.jaxb.ObjectFactory;
import fr.pantheonsorbonne.ufr27.miage.tests.utils.TestPersistenceProducer;

@EnableWeld
class TestPassagerDAO {
	@WeldSetup
	private WeldInitiator weld = WeldInitiator.from(PassagerDAO.class, TestPersistenceProducer.class, TrainDAO.class,
			HeureDePassageDAO.class, ArretDAO.class, PerturbationDAO.class).activate(RequestScoped.class).build();

	@Inject
	EntityManager em;

	@Inject
	PassagerDAO dao;
	
	@Inject
	ArretDAO arretDao;
	
	@Inject
	TrainDAO trainDao;

	@Inject
	HeureDePassageDAO hdpDao;

	Passager passager1;
	Train train1;
	Train train2;
	Train train3;
	Arret arretDepart;
	Arret arretArrivee;
	
	
	Passager passager2;
	Passager passager3; 
	Arret arretTours;
	Arret arretLyon;
	Arret arretMarseille;
	Arret arretLille;
	HeureDePassageKey hdpKeyDepart1;
	HeureDePassageKey hdpKeyArrive1;
	HeureDePassage heureDePassageDepart1;
	HeureDePassage heureDePassageArrivee1;

	@BeforeEach
	public void setup() {
		System.out.println("\n== SetUp");

		System.out.println("****************** " + dao.toString());

		em.getTransaction().begin();

		arretDepart = new Arret();
		arretDepart.setNom("Bordeaux");
		em.persist(arretDepart);

		arretArrivee = new Arret();
		arretArrivee.setNom("Paris");
		em.persist(arretArrivee);
		
		
		arretTours = new Arret();
		arretTours.setNom("Tours");
		em.persist(arretTours);
		
		arretLyon = new Arret();
		arretLyon.setNom("Lyon");
		em.persist(arretLyon);
		
		arretLille = new Arret();
		arretLille.setNom("Lille");
		em.persist(arretLille);
		
		arretMarseille = new Arret();
		arretMarseille.setNom("Marseille");
		em.persist(arretMarseille);
		
		
		train1 = new TrainAvecResa();
		train1.setNom("Bordeaux - Paris");
		train1.setDirectionType("forward");
		train1.setStatut("enmarche");
		train1.setNumero(8541);
		train1.setReseau("SNCF");
		train1.setStatut("en marche");
		em.persist(train1);
		
		train2 = new TrainAvecResa();
		train2.setNom("Paris - Tours - Lille");
		train2.setDirectionType("forward");
		train2.setStatut("enmarche");
		train2.setNumero(7400);
		train2.setReseau("SNCF");
		train2.setStatut("en marche");
		em.persist(train2);
		
		
		train3 = new TrainAvecResa();
		train3.setNom("Bordeaux - Marseille");
		train3.setDirectionType("forward");
		train3.setStatut("enmarche");
		train3.setNumero(6020);
		train3.setReseau("SNCF");
		train3.setStatut("en marche");
		em.persist(train3);
		
		
		passager1 = new Passager();
		passager1.setNom("David Serruya");
		passager1.setArrive(arretArrivee);
		passager1.setDepart(arretDepart);
		passager1.setTrain(train1);
		em.persist(passager1);
		
		//////////////////////////////////////////////////////

		
		
		passager2 = new Passager();
		passager2.setNom("Julien");
		passager2.setArrive(arretLille);
		passager2.setDepart(arretDepart);
		passager2.setTrain(train3);
		em.persist(passager2);
		

//		
//		hdpKeyDepart1 = new HeureDePassageKey();
//		hdpKeyDepart1.setArretId(arretDepart.getId());
//		hdpKeyDepart1.setTrainId(train1.getId());
//		
//		hdpKeyArrive1 = new HeureDePassageKey();
//		hdpKeyArrive1.setArretId(arretArrivee.getId());
//		hdpKeyArrive1.setTrainId(train1.getId());
//
//		heureDePassageDepart1 = new HeureDePassage();
//		heureDePassageDepart1.setId(hdpKeyDepart1);
//		heureDePassageDepart1.setArret(arretDepart);
//		heureDePassageDepart1.setTrain(train1);
//		heureDePassageDepart1.setBaseArriveeTemps(LocalDateTime.now().plusMinutes(10));
//		heureDePassageDepart1.setReelArriveeTemps(LocalDateTime.now().plusMinutes(10));
//		heureDePassageDepart1.setBaseDepartTemps(LocalDateTime.now().plusMinutes(30));
//		heureDePassageDepart1.setReelDepartTemps(LocalDateTime.now().plusMinutes(30));
//		heureDePassageDepart1.setDesservi(true);
//		heureDePassageDepart1.setTerminus(false);
//		
//		em.persist(heureDePassageDepart1);
//		
//		heureDePassageArrivee1 = new HeureDePassage();
//		heureDePassageArrivee1.setId(hdpKeyArrive1);
//		heureDePassageArrivee1.setArret(arretArrivee);
//		heureDePassageArrivee1.setTrain(train1);
//		heureDePassageArrivee1.setBaseArriveeTemps(LocalDateTime.now().plusMinutes(40));
//		heureDePassageArrivee1.setReelArriveeTemps(LocalDateTime.now().plusMinutes(40));
//		heureDePassageArrivee1.setBaseDepartTemps(LocalDateTime.now().plusMinutes(60));
//		heureDePassageArrivee1.setReelDepartTemps(LocalDateTime.now().plusMinutes(60));
//		heureDePassageArrivee1.setDesservi(true);
//		heureDePassageArrivee1.setTerminus(false);
//		
//		em.persist(heureDePassageArrivee1);
		
		
		em.getTransaction().commit();
		
		LocalDateTime date = LocalDateTime.now();
		
		//Bordeaux - Paris
		em.getTransaction().begin();
		trainDao.addArret(train1, arretDepart, date.plusMinutes(30), date.plusMinutes(10), true, false);
		em.getTransaction().commit();
		
		em.getTransaction().begin();
		trainDao.addArret(train1, arretArrivee, date.plusMinutes(60), date.plusMinutes(40), true, false);
		em.getTransaction().commit();
		
		//Paris - Tours - Lille
		em.getTransaction().begin();
		trainDao.addArret(train2, arretArrivee, date.plusMinutes(130), date.plusMinutes(120), true, false);
		em.getTransaction().commit();
		
		em.getTransaction().begin();
		trainDao.addArret(train2, arretTours, date.plusMinutes(150), date.plusMinutes(140), true, false);
		em.getTransaction().commit();
		
		em.getTransaction().begin();
		trainDao.addArret(train2, arretLille, date.plusMinutes(180), date.plusMinutes(170), true, false);
		em.getTransaction().commit();
		
		//Bordeaux - Marseille
		em.getTransaction().begin();
		trainDao.addArret(train3, arretDepart, date.plusMinutes(30), date.plusMinutes(10), true, false);
		em.getTransaction().commit();
		
		em.getTransaction().begin();
		trainDao.addArret(train3, arretMarseille, date.plusMinutes(80), date.plusMinutes(60), true, false);
		em.getTransaction().commit();
		
	}

	@AfterEach
	public void tearDown() {
		System.out.println("\n== TearDown");
		em.getTransaction().begin();
		em.remove(passager1);
		passager1 = null;
		em.remove(passager2);
		passager2=null;
//		hdpDao.deleteHeureDePassage(train1,arretDepart);
//		hdpDao.deleteHeureDePassage(train1,arretArrivee);
//		em.remove(heureDePassageDepart1);
//		heureDePassageDepart1 = null;
//		em.remove(heureDePassageArrivee1);
//		heureDePassageArrivee1=null;
		trainDao.removeArret(train1, arretDepart);
		trainDao.removeArret(train1, arretArrivee);
		
		trainDao.removeArret(train2, arretArrivee);
		trainDao.removeArret(train2, arretTours);
		trainDao.removeArret(train2, arretLille);
		
		trainDao.removeArret(train3, arretDepart);
		trainDao.removeArret(train3, arretMarseille);
		
		em.remove(train1);
		train1 = null;
		em.remove(train2);
		train2 = null;
		em.remove(train3);
		train3 = null;
		em.remove(arretDepart);
		arretDepart = null;
		em.remove(arretArrivee);
		arretArrivee = null;
		em.remove(arretLille);
		arretLille = null;
		em.remove(arretTours);
		arretTours = null;
		em.remove(arretLyon);
		arretLyon = null;
		em.remove(arretMarseille);
		arretMarseille = null;

		em.getTransaction().commit();
	}

	@Test
	public void testCreatePassager() {

		assertFalse(dao.isPassagerCreated(passager1.getId()));

		em.getTransaction().begin();
		passager1.setCreated(true);
		em.merge(passager1);
		em.getTransaction().commit();

		assertTrue(dao.isPassagerCreated(passager1.getId()));

	}

	@Test
	void testGetPassagerFromId() {
		assertEquals(passager1, dao.getPassagerFromId(passager1.getId()));
	}

	@Test
	void testUpdatePassager() {
		ObjectFactory factory = new ObjectFactory();
		fr.pantheonsorbonne.ufr27.miage.model.jaxb.Passager passagerUpdate = factory.createPassager();
		passagerUpdate.setId(passager1.getId());
		passagerUpdate.setNom("tiesto");
		fr.pantheonsorbonne.ufr27.miage.model.jaxb.Arret arretUpdate1 = factory.createArret();
		arretUpdate1.setId(arretArrivee.getId());
		arretUpdate1.setNom(arretArrivee.getNom());
		fr.pantheonsorbonne.ufr27.miage.model.jaxb.Arret arretUpdate2 = factory.createArret();
		arretUpdate2.setId(arretDepart.getId());
		arretUpdate2.setNom(arretDepart.getNom());
		passagerUpdate.setDepart(arretUpdate1);
		passagerUpdate.setArrive(arretUpdate2);
		Passager passagerOriginalNonModif = passager1;

		em.getTransaction().begin();
		em.merge(dao.updatePassager(passager1, passagerUpdate));
		em.getTransaction().commit();
		passager1 = em.find(Passager.class, passager1.getId());
		assertEquals(passager1.getNom(), passagerUpdate.getNom());
		assertEquals(passagerOriginalNonModif.getDepart(), passager1.getDepart());
		assertEquals(passagerOriginalNonModif.getArrive(), passager1.getArrive());
	}

	@Test
	void testDeletePassager() {
		em.getTransaction().begin();
		dao.deletePassager(passager1);
		em.getTransaction().commit();
		assertNull(dao.getPassagerFromId(passager1.getId()));
	}

	@Test
	void testGetAllPassager() {
		List<Passager> passagers = dao.getAllPassager();

		assertEquals(1, passagers.size());
		assertEquals(passager1, passagers.get(0));
	}

	@Test
	void testGetAllPassagerByTrain() {
		List<Passager> passagers = dao.getAllPassagerByTrain(train1.getId());
		assertEquals(1, passagers.size());
		assertEquals(passager1, passagers.get(0));
	}

	@Test
	void testgetAllPassagerByDepart() {
		List<Passager> passagers = dao.getAllPassagerByDepart(arretDepart.getId());
		assertEquals(1, passagers.size());
		assertEquals(passager1, passagers.get(0));
	}

	@Test
	void testGetAllPassagerByArrivee() {
		List<Passager> passagers = dao.getAllPassagerByArrivee(arretArrivee.getId());
		assertEquals(1, passagers.size());
		assertEquals(passager1, passagers.get(0));

	}
	
	@Test 
	void testFindTrajet(){

		
		Train trainAttribue =  dao.findTrajet(passager1.getId());
		assertEquals(train1.getId(),trainAttribue.getId());
//		
		// prend le train1 Bordeaux - Paris pour ensuite prendre train2 Paris-Tours-Lille
//		Train trainCorrespondance= dao.findTrajet(passager2.getId());
//		assertEquals(train1.getId(),trainCorrespondance.getId());
//		
		
	}

	
}
