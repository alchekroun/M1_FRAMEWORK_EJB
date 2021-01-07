package fr.pantheonsorbonne.ufr27.miage.dao;

import static org.junit.jupiter.api.Assertions.*;

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

		train1 = new TrainAvecResa();
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
		perturbationTmp.setDureeEnPlus();

		dao.createPerturbation(perturbationTmp);

		em.getTransaction().commit();
	}

	@AfterEach
	void tearDown() throws Exception {
		em.getTransaction().begin();

		em.getTransaction().commit();
	}

	@Test
	void testCreatePerturbation() {
		fail("Not yet implemented");
	}

	@Test
	void testGetPerturbationFromId() {
		fail("Not yet implemented");
	}

	@Test
	void testUpdatePerturbation() {
		fail("Not yet implemented");
	}

	@Test
	void testGetAllPerturbation() {
		fail("Not yet implemented");
	}

	@Test
	void testDeletePerturbation() {
		fail("Not yet implemented");
	}

}
