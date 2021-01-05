
package fr.pantheonsorbonne.ufr27.miage.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
import fr.pantheonsorbonne.ufr27.miage.model.jaxb.ObjectFactory;
import fr.pantheonsorbonne.ufr27.miage.tests.utils.TestPersistenceProducer;

@EnableWeld
class TestPassagerDAO {
	@WeldSetup
	private WeldInitiator weld = WeldInitiator.from(PassagerDAO.class, TestPersistenceProducer.class, TrainDAO.class,
			HeureDePassageDAO.class, ArretDAO.class).activate(RequestScoped.class).build();

	@Inject
	EntityManager em;

	@Inject
	PassagerDAO dao;

	Passager passager1;
	Train train1;
	Arret arretDepart;
	Arret arretArrivee;

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

		train1 = new Train();
		train1.setNom("Bordeaux - Paris");
		train1.setDirectionType("forward");
		train1.setStatut("enmarche");
		train1.setNumero(8541);
		train1.setReseau("SNCF");
		train1.setStatut("en marche");
		em.persist(train1);

		passager1 = new Passager();
		passager1.setNom("David Serruya");
		passager1.setArrive(arretArrivee);
		passager1.setDepart(arretDepart);
		passager1.setTrain(train1);
		em.persist(passager1);

		em.getTransaction().commit();

	}

	@AfterEach
	public void tearDown() {
		System.out.println("\n== TearDown");
		em.getTransaction().begin();
		em.remove(passager1);
		passager1 = null;
		em.remove(train1);
		train1 = null;
		em.remove(arretDepart);
		arretDepart = null;
		em.remove(arretArrivee);
		arretArrivee = null;
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

}
