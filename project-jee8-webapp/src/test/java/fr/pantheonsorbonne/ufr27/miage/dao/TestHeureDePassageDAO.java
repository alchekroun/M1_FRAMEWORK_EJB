
package fr.pantheonsorbonne.ufr27.miage.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
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
		train1.setDirectionType("forward");
		train1.setStatut("enmarche");
		train1.setNumero(8541);
		train1.setReseau("SNCF");
		train1.setStatut("en marche");
		em.persist(train1);

		key = new HeureDePassageKey();
		key.setArretId(arretArrivee.getId());
		key.setTrainId(train1.getId());

		heureDePassage1 = new HeureDePassage();
		heureDePassage1.setId(key);
		heureDePassage1.setArret(arretArrivee);
		heureDePassage1.setTrain(train1);
		heureDePassage1.setBaseArriveeTemps(LocalDateTime.now().plusMinutes(10));
		heureDePassage1.setReelArriveeTemps(LocalDateTime.now().plusMinutes(10));
		heureDePassage1.setBaseDepartTemps(LocalDateTime.now().plusMinutes(30));
		heureDePassage1.setReelDepartTemps(LocalDateTime.now().plusMinutes(30));
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

	@Test
	public void testChangeParameterDesservi() {
		Boolean desserviTest = false;
		HeureDePassage hdp = dao.createHeureDePassage(train1, arretDepart, LocalDateTime.now().plusMinutes(30),
				LocalDateTime.now().plusMinutes(10), desserviTest, false);
		assertEquals(desserviTest, hdp.isDesservi());
		em.getTransaction().begin();
		dao.changeParameterDesservi(hdp, !desserviTest);
		em.merge(hdp);
		em.merge(train1);
		em.merge(arretDepart);
		em.getTransaction().commit();
		assertEquals(!desserviTest, hdp.isDesservi());

	}

	@Test
	public void testRetarderHdp() {
		LocalDateTime heureDepart = heureDePassage1.getReelDepartTemps();
		LocalDateTime heureArrivee = heureDePassage1.getReelArriveeTemps();
		dao.retarderHdp(heureDePassage1, 10);
		HeureDePassage hdp=dao.getHdpFromTrainIdAndArretId(train1.getId(), arretArrivee.getId());
		assertEquals(heureDepart.plusMinutes(10), hdp.getReelDepartTemps());
		assertEquals(heureArrivee.plusMinutes(10),hdp.getReelArriveeTemps());
	}

	@Test
	public void testGetHdpByTrainAndDateNow() {
		em.getTransaction().begin();
		HeureDePassage heureDePassage2 = dao.createHeureDePassage(train1, arretDepart,
				LocalDateTime.now().minusMinutes(10), LocalDateTime.now().plusMinutes(30), true, false);
		em.getTransaction().commit();
		HeureDePassage heureDePassage3= dao.getHdpByTrainAndDateNow(train1.getId());
		assertEquals(heureDePassage2,heureDePassage3);
		em.getTransaction().begin();
		em.remove(heureDePassage2);
		heureDePassage2 = null;
		em.getTransaction().commit();
	}

}
