package fr.pantheonsorbonne.ufr27.miage.dao;

import static org.junit.jupiter.api.Assertions.*;

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

import fr.pantheonsorbonne.ufr27.miage.jpa.Perturbation;
import fr.pantheonsorbonne.ufr27.miage.jpa.Train;
import fr.pantheonsorbonne.ufr27.miage.mapper.TrainMapper;
import fr.pantheonsorbonne.ufr27.miage.model.jaxb.ObjectFactory;
import fr.pantheonsorbonne.ufr27.miage.tests.utils.TestPersistenceProducer;

@EnableWeld
class TestPerturbationDAO {
	@WeldSetup
	private WeldInitiator weld = WeldInitiator.from(TrainDAO.class, TestPersistenceProducer.class,
			HeureDePassageDAO.class, ArretDAO.class, PassagerDAO.class, PerturbationDAO.class)
			.activate(RequestScoped.class).build();

	@Inject
	EntityManager em;

	@Inject
	PerturbationDAO dao;

	static ObjectFactory factory;

	Train train1;
	Perturbation perturbation1;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		factory = new ObjectFactory();
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
		em.getTransaction().begin();

		train1 = new Train(); // TrainAvecResa
		train1.setNom("Bordeaux - Paris");
		train1.setDirectionType("forward");
		train1.setStatut("enmarche");
		train1.setNumero(8541);
		train1.setReseau("SNCF");
		train1.setStatut("en marche");
		em.persist(train1);

		fr.pantheonsorbonne.ufr27.miage.model.jaxb.Perturbation perturbationTmp = factory.createPerturbation();

		perturbationTmp.setId(1);
		perturbationTmp.setMotif("chevreuil");
		perturbationTmp.setTrain(TrainMapper.trainDTOMapper(train1));
		perturbationTmp.setDureeEnPlus(10);

		dao.createPerturbation(perturbationTmp);

		em.getTransaction().commit();
	}

	@AfterEach
	void tearDown() throws Exception {
		em.getTransaction().begin();
		em.remove(train1);
		train1 = null;
		em.remove(perturbation1);
		perturbation1 = null;
		em.getTransaction().commit();
	}

	@Test
	void testCreatePerturbation() {
		fail("Not yet implemented");
	}

	@Test
	void testGetPerturbationFromId() {
		Perturbation p = dao.getPerturbationFromId(perturbation1.getId());
		assertEquals(p.getId(), perturbation1.getId());
		assertEquals(p.getMotif(), perturbation1.getMotif());
		assertEquals(p.getTrain(), perturbation1.getTrain());
		assertEquals(p.getDureeEnPlus(), perturbation1.getDureeEnPlus());
	}

	@Test
	void testUpdatePerturbation() {
		em.getTransaction().begin();
		Perturbation p = dao.getPerturbationFromId(perturbation1.getId());
		assertEquals(p.getMotif(), perturbation1.getMotif());
		p.setMotif("covid");
		em.merge(p);
		em.getTransaction().commit();
		p = dao.getPerturbationFromId(perturbation1.getId());
		assertEquals(p.getMotif(), "covid");
		assertEquals(p.getDureeEnPlus(), perturbation1.getDureeEnPlus());
		assertEquals(p.getTrain(), perturbation1.getTrain());
	}

	@Test
	void testGetAllPerturbation() {
		List<Perturbation> listPerturbations = dao.getAllPerturbation();
		assertTrue(listPerturbations.contains(perturbation1));
	}

	@Test
	void testDeletePerturbation() {
		fail("Not yet implemented");
	}

	@Test
	void testImpacterTrafic() {
		fail("Not yet implemented");
	}

}
