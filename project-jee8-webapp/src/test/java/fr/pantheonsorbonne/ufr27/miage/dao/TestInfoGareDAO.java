package fr.pantheonsorbonne.ufr27.miage.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

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
import fr.pantheonsorbonne.ufr27.miage.jpa.InfoGare;
import fr.pantheonsorbonne.ufr27.miage.jpa.Train;
import fr.pantheonsorbonne.ufr27.miage.tests.utils.TestPersistenceProducer;

@EnableWeld
public class TestInfoGareDAO {
	@WeldSetup
	private WeldInitiator weld = WeldInitiator.from(InfoGareDAO.class, TestPersistenceProducer.class)
			.activate(RequestScoped.class).build();

	@Inject
	EntityManager em;

	@Inject
	InfoGareDAO dao;

	InfoGare infoGare1;
	Arret arret1;

	@BeforeEach
	public void setup() {
		System.out.println("\n== SetUp");

		System.out.println("****************** " + dao.toString());

		em.getTransaction().begin();

		arret1 = new Arret();
		arret1.setNom("Paris");
		em.persist(arret1);

		infoGare1 = new InfoGare();
		infoGare1.setLocalisation(arret1);

		em.persist(infoGare1);
		em.getTransaction().commit();

	}

	@AfterEach
	public void tearDown() {
		System.out.println("\n== TearDown");
		em.getTransaction().begin();
		em.remove(infoGare1);
		infoGare1 = null;
		em.remove(arret1);
		arret1 = null;
		em.getTransaction().commit();
	}

	@Test
	public void testCreateInfoGare() {

		assertFalse(dao.isInfoGareCreated(arret1.getId()));

		em.getTransaction().begin();
		infoGare1.setCreated(true);
		em.merge(infoGare1);
		em.getTransaction().commit();

		assertTrue(dao.isInfoGareCreated(infoGare1.getId()));

	}

	@Test
	public void testGetInfoGareFromId() {
		assertEquals(infoGare1, dao.getInfoGareFromId(arret1.getId()));
	}

	@Test
	public void testGetAllInfoGare() {
		List<InfoGare> infoGares = dao.getAllInfoGare();

		assertEquals(1, infoGares.size());
		assertEquals(infoGare1, infoGares.get(0));
	}

	@Test
	public void testDeleteTrain() {
		dao.deleteInfoGare(arret1.getId());

		assertNull(dao.getInfoGareFromId(infoGare1.getId()));
	}
}
