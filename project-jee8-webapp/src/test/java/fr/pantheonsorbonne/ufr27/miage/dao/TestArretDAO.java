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
import fr.pantheonsorbonne.ufr27.miage.jpa.Train;
import fr.pantheonsorbonne.ufr27.miage.tests.utils.TestPersistenceProducer;

@EnableWeld
public class TestArretDAO {
	@WeldSetup
	private WeldInitiator weld = WeldInitiator
			.from(TrainDAO.class, TestPersistenceProducer.class, HeureDePassageDAO.class, ArretDAO.class)
			.activate(RequestScoped.class).build();

	@Inject
	EntityManager em;

	@Inject
	ArretDAO dao;
	@Inject
	TrainDAO daoTrain;

	Arret arret1;
	Train train1;
	Arret arretDirection;

	@BeforeEach
	public void setup() {
		System.out.println("\n== SetUp");

		System.out.println("****************** " + dao.toString());

		em.getTransaction().begin();

		arret1 = new Arret();
		arret1.setNom("Caen");
		em.persist(arret1);
		arretDirection = new Arret();
		arretDirection.setNom("Paris");
		em.persist(arretDirection);
        train1 = new Train();
        train1.setNom("Bordeaux - Paris");
		train1.setDirectionType("forward");
		train1.setDirection(arretDirection);
		train1.setStatut("enmarche");
		train1.setNumeroTrain(8541);
		train1.setReseau("SNCF");
		train1.setStatut("en marche");
		train1.setBaseDepartTemps(LocalDateTime.now().plusMinutes(10));
		train1.setBaseArriveeTemps(LocalDateTime.now().plusMinutes(30));
		train1.setReelDepartTemps(LocalDateTime.now().plusMinutes(10));
		train1.setReelArriveeTemps(LocalDateTime.now().plusMinutes(30));
		em.persist(train1);
		em.getTransaction().commit();

	}

	@AfterEach
	public void tearDown() {
		System.out.println("\n== TearDown");
		em.getTransaction().begin();
		em.remove(arret1);
		arret1 = null;
		em.remove(arretDirection);
		arretDirection = null;
		em.remove(train1);
		train1=null;
		em.getTransaction().commit();
	}

	@Test
	public void testCreateArret() {

		assertFalse(dao.isArretCreated(arret1.getId()));

		em.getTransaction().begin();
		arret1.setCreated(true);
		em.merge(arret1);
		em.getTransaction().commit();
      
		assertTrue(dao.isArretCreated(arret1.getId()));
	
	}

	@Test
	public void testGetArretFromId() {
		assertEquals(arret1, dao.getArretFromId(arret1.getId()));
	}

	@Test
	public void testGetAllArret() {
		List<Arret> arrets = dao.getAllArret();

		assertEquals(2, arrets.size());
		assertEquals(arret1, arrets.get(0));
		assertEquals(arretDirection, arrets.get(1));
	}

	@Test
	public void testDeleteArret() {
		em.getTransaction().begin();
		dao.deleteArret(arret1.getId());
		em.getTransaction().commit();
		assertNull(dao.getArretFromId(arret1.getId()));
	}

	@Test public void testGetAllArretByTrain() {
		em.getTransaction().begin();
	    daoTrain.addArret(train1, arret1, LocalDateTime.now().plusMinutes(30));
	    em.getTransaction().commit();
	    List<Arret> Arrets = dao.getAllArretByTrain(train1.getId()); 
	    assertEquals(1, Arrets.size()); assertEquals(arret1, Arrets.get(0));
	    em.getTransaction().begin();
		daoTrain.removeArret(train1,arret1.getId());
		em.getTransaction().commit();
	 
	} 
	 

}
