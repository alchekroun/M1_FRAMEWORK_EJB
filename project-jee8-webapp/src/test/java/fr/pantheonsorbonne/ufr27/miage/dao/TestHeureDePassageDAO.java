
package fr.pantheonsorbonne.ufr27.miage.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
import fr.pantheonsorbonne.ufr27.miage.jpa.Train;
import fr.pantheonsorbonne.ufr27.miage.jpa.TrainAvecResa;
import fr.pantheonsorbonne.ufr27.miage.tests.utils.TestPersistenceProducer;

@EnableWeld
class TestHeureDePassageDAO {
	@WeldSetup
	private WeldInitiator weld = WeldInitiator.from(HeureDePassageDAO.class, TestPersistenceProducer.class)
			.activate(RequestScoped.class).build();

	@Inject
	EntityManager em;

	@Inject
	HeureDePassageDAO dao;

	HeureDePassage heureDePassage1;
	Train train1;
	Arret arretArrivee;
	Arret arretDepart;
	HeureDePassageKey key;

	@BeforeEach
	void setUp() throws Exception {
		System.out.println("\n== SetUp");

		System.out.println("****************** " + dao.toString());
		em.getTransaction().begin();

		arretDepart = new Arret();
		arretDepart.setNom("Bordeaux");
		em.persist(arretDepart);

		arretArrivee = new Arret();
		arretArrivee.setNom("Paris");
		em.persist(arretArrivee);

		train1 = new TrainAvecResa();
		train1.setNom("Bordeaux - Paris");
		train1.setStatut("on");
		train1.setNumero(8541);
		train1.setReseau("SNCF");
		em.persist(train1);

		key = new HeureDePassageKey();
		key.setArretId(arretArrivee.getId());
		key.setTrainId(train1.getId());

		heureDePassage1 = new HeureDePassage();
		heureDePassage1.setId(key);
		heureDePassage1.setArret(arretArrivee);
		heureDePassage1.setTrain(train1);
		heureDePassage1.setBaseArriveeTemps(LocalDateTime.now().plusMinutes(100));
		heureDePassage1.setReelArriveeTemps(LocalDateTime.now().plusMinutes(100));
		heureDePassage1.setBaseDepartTemps(LocalDateTime.now().plusMinutes(100));
		heureDePassage1.setReelDepartTemps(LocalDateTime.now().plusMinutes(100));
		heureDePassage1.setDesservi(true);
		heureDePassage1.setTerminus(true);
		em.persist(heureDePassage1);

		em.getTransaction().commit();

	}

	@AfterEach
	void tearDown() throws Exception {
		System.out.println("\n== TearDown");
		em.getTransaction().begin();
		em.remove(heureDePassage1);
		heureDePassage1 = null;
		em.remove(train1);
		train1 = null;
		em.remove(arretArrivee);
		arretArrivee = null;
		em.remove(arretDepart);
		arretDepart = null;
		em.getTransaction().commit();
	}

	@Test
	public void testExistHeureDePassage() {

		assertFalse(dao.isHeureDePassageCreated(heureDePassage1.getId()));

		em.getTransaction().begin();
		heureDePassage1.setCreated(true);
		em.merge(heureDePassage1);
		em.getTransaction().commit();

		assertTrue(dao.isHeureDePassageCreated(heureDePassage1.getId()));

	}

	@Test
	public void testGetHeureDePassageFromkey() {
		assertEquals(heureDePassage1, dao.getHeureDePassageFromKey(key));
	}

	@Test
	public void testCreateHeureDePassage() {
		em.getTransaction().begin();
		HeureDePassage heureDePassage2 = dao.createHeureDePassage(train1, arretDepart,
				LocalDateTime.now().plusMinutes(30), LocalDateTime.now().plusMinutes(10), true, false);
		em.getTransaction().commit();

		assertNotNull(heureDePassage2);

		em.getTransaction().begin();
		em.remove(heureDePassage2);
		heureDePassage2 = null;
		em.getTransaction().commit();
	}

	@Test
	public void testGetHdpFromTrainIdAndArretId() {
		assertEquals(
				dao.getHdpFromTrainIdAndArretId(heureDePassage1.getTrain().getId(), heureDePassage1.getArret().getId()),
				heureDePassage1);
	}

	public void testFindHdpByTrainIdAndArretIdAndHeureReel() {
		assertEquals(dao.findHdpByTrainIdAndArretIdAndHeureReel(heureDePassage1.getTrain().getId(),
				heureDePassage1.getArret().getId(), heureDePassage1.getReelArriveeTemps(),
				heureDePassage1.getReelDepartTemps()), heureDePassage1);
		assertNull(dao.findHdpByTrainIdAndArretIdAndHeureReel(heureDePassage1.getTrain().getId(),
				heureDePassage1.getArret().getId(), LocalDateTime.now().plusMinutes(100),
				LocalDateTime.now().plusMinutes(120)));
	}

	@Test
	public void testChangeParameterDesservi() {

		Boolean desserviTest = false;
		em.getTransaction().begin();
		HeureDePassage hdp = dao.createHeureDePassage(train1, arretDepart, LocalDateTime.now().plusMinutes(30),
				LocalDateTime.now().plusMinutes(10), desserviTest, false);
		em.getTransaction().commit();

		assertEquals(desserviTest, hdp.isDesservi());

		em.getTransaction().begin();
		dao.changeParameterDesservi(hdp, !desserviTest);
		em.merge(hdp);
		em.merge(train1);
		em.merge(arretDepart);
		em.getTransaction().commit();
		assertEquals(!desserviTest, hdp.isDesservi());

		em.getTransaction().begin();
		em.remove(hdp);
		hdp = null;
		em.getTransaction().commit();

	}

	@Test
	public void testRetarderHdp() {
		LocalDateTime heureDepart = heureDePassage1.getReelDepartTemps();
		LocalDateTime heureArrivee = heureDePassage1.getReelArriveeTemps();

		em.getTransaction().begin();
		dao.retarderHdp(heureDePassage1, 10);
		em.getTransaction().commit();

		HeureDePassage hdp = dao.getHdpFromTrainIdAndArretId(train1.getId(), arretArrivee.getId());
		assertEquals(heureDepart.plusMinutes(10), hdp.getReelDepartTemps());
		assertEquals(heureArrivee.plusMinutes(10), hdp.getReelArriveeTemps());
	}

	@Test
	public void testGetHdpByTrainAndDateNow() {
		em.getTransaction().begin();
		HeureDePassage heureDePassage2 = dao.createHeureDePassage(train1, arretDepart,
				LocalDateTime.now().plusMinutes(10), LocalDateTime.now().minusMinutes(1), true, false);
		em.getTransaction().commit();

		HeureDePassage heureDePassage3 = dao.getHdpByTrainAndDateNow(train1.getId());
		assertEquals(heureDePassage2, heureDePassage3);

		em.getTransaction().begin();
		em.remove(heureDePassage2);
		heureDePassage2 = null;
		em.getTransaction().commit();
	}

	@Test
	public void testGetHdpFromTrainIdAndArretIdAndBetweenDate1AndDate2() {

		List<HeureDePassage> list1 = dao.getHdpFromTrainIdAndArretIdAndBetweenDate1AndDate2(train1.getId(),
				arretArrivee.getId(), LocalDateTime.now().plusMinutes(5), LocalDateTime.now().plusMinutes(8));

		List<HeureDePassage> list2 = dao.getHdpFromTrainIdAndArretIdAndBetweenDate1AndDate2(train1.getId(),
				arretArrivee.getId(), LocalDateTime.now().plusMinutes(20), LocalDateTime.now().plusMinutes(40));

		List<HeureDePassage> list3 = dao.getHdpFromTrainIdAndArretIdAndBetweenDate1AndDate2(train1.getId(),
				arretArrivee.getId(), LocalDateTime.now().plusMinutes(80), LocalDateTime.now().plusMinutes(120));

		assertTrue(list1.isEmpty());
		assertTrue(list2.isEmpty());
		assertEquals(list3.size(), 1);
		assertEquals(heureDePassage1.getId(), list3.get(0).getId());
	}

	@Test
	public void testFindHeureByDepartAfterDateAndTrainIdAndArretIdAndSorted() {

		em.getTransaction().begin();
		HeureDePassage heureDePassage2 = dao.createHeureDePassage(train1, arretDepart,
				LocalDateTime.now().plusMinutes(40), LocalDateTime.now().plusMinutes(10), true, false);
		em.getTransaction().commit();

		List<HeureDePassage> list1 = dao.findHeureByDepartAfterDateAndTrainIdAndArretIdAndSorted(train1.getId(),
				arretDepart.getId(), LocalDateTime.now().plusMinutes(5));

		List<HeureDePassage> list2 = dao.findHeureByDepartAfterDateAndTrainIdAndArretIdAndSorted(train1.getId(),
				arretDepart.getId(), LocalDateTime.now().plusMinutes(50));

		assertEquals(heureDePassage2.getId(), list1.get(0).getId());
		assertEquals(1, list1.size());
		assertTrue(list2.isEmpty());

		em.getTransaction().begin();
		em.remove(heureDePassage2);
		heureDePassage2 = null;
		em.getTransaction().commit();

	}

	@Test
	public void testFindHdpByTrainAfterDateAndSortedAndDesservi() {
		em.getTransaction().begin();
		HeureDePassage heureDePassage2 = dao.createHeureDePassage(train1, arretDepart,
				LocalDateTime.now().plusMinutes(400), LocalDateTime.now().plusMinutes(120), true, false);
		em.getTransaction().commit();

		List<HeureDePassage> list1 = dao.findHdpByTrainAfterDateAndSortedAndDesservi(train1.getId(),
				LocalDateTime.now().plusMinutes(50));

		List<HeureDePassage> list2 = dao.findHdpByTrainAfterDateAndSortedAndDesservi(train1.getId(),
				LocalDateTime.now().plusMinutes(500));

		assertEquals(heureDePassage1.getId(), list1.get(0).getId());
		assertEquals(2, list1.size());
		assertTrue(list2.isEmpty());
		
		em.getTransaction().begin();
		dao.changeParameterDesservi(heureDePassage2, false);
		em.getTransaction().commit();
		
		list1 = dao.findHdpByTrainAfterDateAndSortedAndDesservi(train1.getId(), LocalDateTime.now().plusMinutes(119));

		// null car non desservi
		assertTrue(list1.isEmpty());

		em.getTransaction().begin();
		em.remove(heureDePassage2);
		heureDePassage2 = null;
		em.getTransaction().commit();

	}

	@Test

	public void testFindHdpByTrainAfterDateAndSorted() {

		em.getTransaction().begin();
		HeureDePassage heureDePassage2 = dao.createHeureDePassage(train1, arretDepart,
				LocalDateTime.now().plusMinutes(40), LocalDateTime.now().plusMinutes(10), true, false);
		em.getTransaction().commit();

		List<HeureDePassage> list1 = dao.findHdpByTrainAfterDateAndSorted(train1.getId(), LocalDateTime.now());

		List<HeureDePassage> list2 = dao.findHdpByTrainAfterDateAndSorted(train1.getId(),
				LocalDateTime.now().plusMinutes(110));

		assertEquals(heureDePassage2.getId(), list1.get(0).getId());
		assertEquals(2, list1.size());
		assertEquals(list1.get(0), heureDePassage2);
		assertTrue(list2.isEmpty());

		em.getTransaction().begin();

		em.remove(heureDePassage2);
		heureDePassage2 = null;
		em.getTransaction().commit();

	}
	
	@Test public void testFindHdpByTrainByDateAndSortedAndDesservi() {
		
		em.getTransaction().begin();
		HeureDePassage heureDePassage2 = dao.createHeureDePassage(train1, arretDepart,
				LocalDateTime.now().plusMinutes(400), LocalDateTime.now().plusMinutes(120), true, false);
		em.getTransaction().commit();

		List<HeureDePassage> list1 = dao.findHdpByTrainByDateAndSortedAndDesservi(train1.getId(),
				LocalDateTime.now().plusMinutes(50));

		List<HeureDePassage> list2 = dao.findHdpByTrainByDateAndSortedAndDesservi(train1.getId(),
				LocalDateTime.now().plusMinutes(500));

		assertEquals(heureDePassage1.getId(), list1.get(0).getId());
		assertEquals(2, list1.size());
		assertTrue(list2.isEmpty());
		
		
		list1 = dao.findHdpByTrainByDateAndSortedAndDesservi(train1.getId(), LocalDateTime.now().plusMinutes(110));
		assertEquals(heureDePassage2.getId(), list1.get(0).getId());
		
		em.getTransaction().begin();
		dao.changeParameterDesservi(heureDePassage2, false);
		em.getTransaction().commit();
		
		list1 = dao.findHdpByTrainByDateAndSortedAndDesservi(train1.getId(), LocalDateTime.now().plusMinutes(119));

		// null car non desservi
		assertTrue(list1.isEmpty());
		
		em.getTransaction().begin();
		em.remove(heureDePassage2);
		heureDePassage2 = null;
		em.getTransaction().commit();

	}

	@Test
	public void testFindNextHdp() {

		em.getTransaction().begin();

		HeureDePassage heureDePassage2 = dao.createHeureDePassage(train1, arretDepart,
				LocalDateTime.now().plusMinutes(40), LocalDateTime.now().plusMinutes(10), true, false);

		em.getTransaction().commit();

		HeureDePassage hdp1 = dao.findNextHdp(train1.getId());

		assertEquals(hdp1.getId(), heureDePassage2.getId());

		em.getTransaction().begin();
		heureDePassage2.setBaseDepartTemps(LocalDateTime.now().minusMinutes(10));
		heureDePassage2.setReelDepartTemps(LocalDateTime.now().minusMinutes(10));
		heureDePassage2.setBaseArriveeTemps(LocalDateTime.now().minusMinutes(40));
		heureDePassage2.setReelArriveeTemps(LocalDateTime.now().minusMinutes(40));
		em.merge(heureDePassage2);
		em.getTransaction().commit();

		hdp1 = dao.findNextHdp(train1.getId());
		assertEquals(hdp1.getId(), heureDePassage1.getId());

		em.getTransaction().begin();
		dao.changeParameterDesservi(heureDePassage2, false);
		em.getTransaction().commit();

		List<HeureDePassage> list1 = dao.findHeureByDepartAfterDateAndTrainIdAndArretIdAndSortedAndDesservi(
				train1.getId(), arretDepart.getId(), LocalDateTime.now().plusMinutes(10));

		// null car non desservi
		assertTrue(list1.isEmpty());

		em.getTransaction().begin();
		em.remove(heureDePassage2);
		heureDePassage2 = null;
		em.getTransaction().commit();

	}
	
	@Test
	public void testReturnFirstHdpDesservi() {
		
		em.getTransaction().begin();
		Arret arretN = new Arret();
		arretN.setNom("Nice");
		em.persist(arretN);
		em.getTransaction().commit();
		
		em.getTransaction().begin();
		HeureDePassage heureDePassage2 = dao.createHeureDePassage(train1, arretN,
				LocalDateTime.now().plusMinutes(40), LocalDateTime.now().plusMinutes(10), true, false);
		em.getTransaction().commit();
		
		em.getTransaction().begin();
		HeureDePassage heureDePassage3 = dao.createHeureDePassage(train1, arretDepart,
				LocalDateTime.now().plusMinutes(30), LocalDateTime.now().plusMinutes(5), true, false);
		em.getTransaction().commit();
		
		List<HeureDePassage> listHdp = new ArrayList<HeureDePassage>();
		listHdp.add(heureDePassage2);
		listHdp.add(heureDePassage3);
		
		em.getTransaction().begin();
		HeureDePassage hdpResult = dao.returnFirstHdpDesservi(listHdp);
		assertEquals(heureDePassage2.getId(),hdpResult.getId());
		em.getTransaction().commit();
		
		em.getTransaction().begin();
		dao.changeParameterDesservi(heureDePassage2, false);
		em.getTransaction().commit();
		
		em.getTransaction().begin();
		hdpResult = dao.returnFirstHdpDesservi(listHdp);
		assertEquals(heureDePassage3.getId(),hdpResult.getId());
		em.getTransaction().commit();
		
		em.getTransaction().begin();
		dao.changeParameterDesservi(heureDePassage3, false);
		em.getTransaction().commit();
		
		em.getTransaction().begin();
		assertNull(dao.returnFirstHdpDesservi(listHdp));
		em.getTransaction().commit();
		
		
		em.getTransaction().begin();
		em.remove(heureDePassage2);
		heureDePassage2 = null;
		em.remove(heureDePassage3);
		heureDePassage3 = null;
		em.remove(arretN);
		arretN=null;
		em.getTransaction().commit();
		
	}

}
