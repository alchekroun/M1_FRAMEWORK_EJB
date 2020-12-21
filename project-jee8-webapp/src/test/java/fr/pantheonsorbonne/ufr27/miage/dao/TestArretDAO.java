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
import fr.pantheonsorbonne.ufr27.miage.jpa.Passager;
import fr.pantheonsorbonne.ufr27.miage.jpa.Train;
import fr.pantheonsorbonne.ufr27.miage.tests.utils.TestPersistenceProducer;

@EnableWeld
public class TestArretDAO {
	@WeldSetup
	private WeldInitiator weld = WeldInitiator.from(ArretDAO.class, TestPersistenceProducer.class)
			.activate(RequestScoped.class).build();

	@Inject
	EntityManager em;

	@Inject
	ArretDAO dao;

	Arret arret1;
	Train train1;

	@BeforeEach
	public void setup() {
		System.out.println("\n== SetUp");

		System.out.println("****************** " + dao.toString());

		em.getTransaction().begin();

		arret1 = new Arret();
		arret1.setNom("Paris");
		em.persist(arret1);

		em.getTransaction().commit();

	}

	@AfterEach
	public void tearDown() {
		System.out.println("\n== TearDown");
		em.getTransaction().begin();
		em.remove(arret1);
		arret1 = null;
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

		assertEquals(1, arrets.size());
		assertEquals(arret1, arrets.get(0));
	}

	@Test
	public void testDeleteArret() {
		em.getTransaction().begin();
		dao.deleteArret(arret1.getId());
		em.getTransaction().commit();
		assertNull(dao.getArretFromId(arret1.getId()));
	}

	/*
	 * @Test public void testGetAllArretByTrain() {
	 * 
	 * List<Arret> arrets = dao.getAllArretByTrain(train1.getId()); assertEquals(1,
	 * arrets.size()); assertEquals(arret1, arrets.get(0)); }
	 */

}
