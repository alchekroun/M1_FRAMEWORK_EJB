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
import fr.pantheonsorbonne.ufr27.miage.jpa.InfoGare;
import fr.pantheonsorbonne.ufr27.miage.jpa.Train;
import fr.pantheonsorbonne.ufr27.miage.jpa.TrainAvecResa;
import fr.pantheonsorbonne.ufr27.miage.model.jaxb.ObjectFactory;
import fr.pantheonsorbonne.ufr27.miage.tests.utils.TestPersistenceProducer;

@EnableWeld
public class TestArretDAO {
	@WeldSetup
	private WeldInitiator weld = WeldInitiator.from(TrainDAO.class, TestPersistenceProducer.class,
			HeureDePassageDAO.class, ArretDAO.class, PassagerDAO.class).activate(RequestScoped.class).build();

	@Inject
	EntityManager em;

	@Inject
	ArretDAO dao;
	@Inject
	TrainDAO daoTrain;

	Arret arret1;
	InfoGare infoGare1;
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
		infoGare1 = new InfoGare();
		infoGare1.setLocalisation(arret1);
		em.persist(infoGare1);
		train1 = new TrainAvecResa();
		train1.setNom("Bordeaux - Paris");
		train1.setDirectionType("forward");
		train1.setStatut("enmarche");
		train1.setNumero(8541);
		train1.setReseau("SNCF");
		train1.setStatut("en marche");
		em.persist(train1);
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
		em.remove(arretDirection);
		arretDirection = null;
		em.remove(train1);
		train1 = null;
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
		assertTrue(arrets.contains(arret1));
		assertTrue(arrets.contains(arretDirection));
	}

	@Test
	public void testDeleteArret() {
		em.getTransaction().begin();
		if (arret1 != null) {
			dao.deleteArret(arret1);
		}
		em.getTransaction().commit();
		assertNull(dao.getArretFromId(arret1.getId()));
	}

	@Test
	public void testUpdateArret() {
		fr.pantheonsorbonne.ufr27.miage.model.jaxb.Arret arretUpdate = new ObjectFactory().createArret();
		arretUpdate.setId(arret1.getId());
		arretUpdate.setNom("Rouen");

		Arret arretOriginalNonModif = arret1;

		em.getTransaction().begin();
		em.merge(dao.updateArret(arret1, arretUpdate));
		em.getTransaction().commit();
		arret1 = em.find(Arret.class, arret1.getId());
		assertEquals(arret1.getNom(), arretUpdate.getNom());
		assertEquals(arretOriginalNonModif.getListeHeureDePassage(), arret1.getListeHeureDePassage());
	}

	@Test
	public void testGetAllArretByTrain() {
		em.getTransaction().begin();
		daoTrain.addArret(train1, arret1, LocalDateTime.now().plusMinutes(30), LocalDateTime.now().plusMinutes(10),
				false);
		em.getTransaction().commit();
		List<Arret> Arrets = dao.getAllArretByTrain(train1.getId());
		assertEquals(1, Arrets.size());
		assertEquals(arret1, Arrets.get(0));
		em.getTransaction().begin();
		daoTrain.removeArret(train1, arret1);
		em.getTransaction().commit();

	}

}
