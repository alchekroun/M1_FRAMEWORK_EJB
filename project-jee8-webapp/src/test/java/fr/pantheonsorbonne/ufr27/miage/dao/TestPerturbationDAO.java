package fr.pantheonsorbonne.ufr27.miage.dao;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
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

import fr.pantheonsorbonne.ufr27.miage.jpa.Arret;
import fr.pantheonsorbonne.ufr27.miage.jpa.HeureDePassage;
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

	@Inject
	TrainDAO trainDAO;

	@Inject
	HeureDePassageDAO hdpDAO;

	static ObjectFactory factory;

	Train train1;
	Arret arret1;
	Arret arret2;
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

		arret1 = new Arret();
		arret1.setNom("Paris");
		em.persist(arret1);

		arret2 = new Arret();
		arret2.setNom("Bordeaux");
		em.persist(arret2);

		fr.pantheonsorbonne.ufr27.miage.model.jaxb.Perturbation perturbationTmp = factory.createPerturbation();

		perturbationTmp.setId(1);
		perturbationTmp.setMotif("chevreuil");
		perturbationTmp.setTrain(TrainMapper.trainDTOMapper(train1));
		perturbationTmp.setDureeEnPlus(10);

		perturbation1 = dao.createPerturbation(perturbationTmp);

		em.getTransaction().commit();

	}

	@AfterEach
	void tearDown() throws Exception {
		em.getTransaction().begin();
		em.remove(train1);
		train1 = null;
		em.remove(perturbation1);
		perturbation1 = null;
		em.remove(arret1);
		arret1 = null;
		em.remove(arret2);
		arret2 = null;
		em.getTransaction().commit();
	}

	@Test
	void testCreatePerturbation() {
		em.getTransaction().begin();
		fr.pantheonsorbonne.ufr27.miage.model.jaxb.Perturbation perturbationTmp = factory.createPerturbation();
		perturbationTmp.setMotif("chevreuil");
		perturbationTmp.setTrain(TrainMapper.trainDTOMapper(train1));
		perturbationTmp.setDureeEnPlus(10);

		perturbation1 = dao.createPerturbation(perturbationTmp);
		em.getTransaction().commit();

		Perturbation p = em.find(Perturbation.class, perturbation1.getId());
		assertEquals(p.getMotif(), perturbationTmp.getMotif());
		assertEquals(p.getTrain().getId(), perturbationTmp.getTrain().getId());
		assertEquals(p.getDureeEnPlus(), perturbationTmp.getDureeEnPlus());
	}

	@Test
	void testGetPerturbationFromId() {
		assertEquals(perturbation1, dao.getPerturbationFromId(perturbation1.getId()));
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
		assertEquals(1, listPerturbations.size());
		assertTrue(listPerturbations.contains(perturbation1));
	}

	@Test
	void testDeletePerturbation() {
		fail("Not yet implemented");
	}

	@Test
	void testImpacterTrafic() {
		em.getTransaction().begin();
		trainDAO.addArret(train1, arret1, LocalDateTime.now().plusMinutes(10), LocalDateTime.now().plusMinutes(0), true,
				false);
		trainDAO.addArret(train1, arret2, LocalDateTime.now().plusMinutes(0), LocalDateTime.now().plusMinutes(20), true,
				true);
		HeureDePassage hdp1 = hdpDAO.getHdpFromTrainIdAndArretId(train1.getId(), arret1.getId());
		HeureDePassage hdp2 = hdpDAO.getHdpFromTrainIdAndArretId(train1.getId(), arret2.getId());
		assertEquals(hdp2.getBaseArriveeTemps(), hdp2.getReelArriveeTemps());
		dao.impacterTrafic(perturbation1);

		// On vérifie que le premier arret n'est pas impacté CAR dans le passé
		assertTrue(hdp1.getBaseArriveeTemps().equals(hdp1.getReelArriveeTemps()));

		// On vérifie que le dernier arret est impacté CAR dans le futur
		assertFalse(hdp2.getBaseArriveeTemps().equals(hdp2.getReelArriveeTemps()));
		em.getTransaction().commit();
	}

}
