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
import fr.pantheonsorbonne.ufr27.miage.jpa.TrainAvecResa;
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
		train1.setStatut("on");
		train1.setNumero(8541);
		train1.setReseau("SNCF");

		em.persist(train1);

		arret1 = new Arret();
		arret1.setNom("Paris");
		em.persist(arret1);

		arret2 = new Arret();
		arret2.setNom("Bordeaux");
		em.persist(arret2);
		em.getTransaction().commit();

	}

	@AfterEach
	void tearDown() throws Exception {
		em.getTransaction().begin();
		em.remove(train1);
		train1 = null;
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

		Perturbation perturbation1 = dao.createPerturbation(perturbationTmp);
		em.getTransaction().commit();

		Perturbation p = em.find(Perturbation.class, perturbation1.getId());
		assertEquals(p.getMotif(), perturbationTmp.getMotif());
		assertEquals(p.getTrain().getId(), perturbationTmp.getTrain().getId());
		assertEquals(p.getDureeEnPlus(), perturbationTmp.getDureeEnPlus());

		em.getTransaction().begin();
		dao.deletePerturbation(perturbation1);
		em.getTransaction().commit();
	}

	@Test
	void testGetPerturbationFromId() {

		em.getTransaction().begin();
		fr.pantheonsorbonne.ufr27.miage.model.jaxb.Perturbation perturbationTmp = factory.createPerturbation();
		perturbationTmp.setMotif("chevreuil");
		perturbationTmp.setTrain(TrainMapper.trainDTOMapper(train1));
		perturbationTmp.setDureeEnPlus(10);

		Perturbation perturbation1 = dao.createPerturbation(perturbationTmp);
		em.getTransaction().commit();

		assertEquals(perturbation1, dao.getPerturbationFromId(perturbation1.getId()));
		assertEquals(perturbation1.getMotif(), dao.getPerturbationFromId(perturbation1.getId()).getMotif());

		em.getTransaction().begin();
		dao.deletePerturbation(perturbation1);
		em.getTransaction().commit();
	}

	@Test
	void testUpdatePerturbation() {
		em.getTransaction().begin();
		fr.pantheonsorbonne.ufr27.miage.model.jaxb.Perturbation perturbationTmp = factory.createPerturbation();
		perturbationTmp.setMotif("chevreuil");
		perturbationTmp.setTrain(TrainMapper.trainDTOMapper(train1));
		perturbationTmp.setDureeEnPlus(10);

		Perturbation perturbation1 = dao.createPerturbation(perturbationTmp);
		em.getTransaction().commit();

		fr.pantheonsorbonne.ufr27.miage.model.jaxb.Perturbation perturbationTmp2 = factory.createPerturbation();
		perturbationTmp2.setMotif("covid");
		perturbationTmp2.setTrain(TrainMapper.trainDTOMapper(train1));
		perturbationTmp2.setDureeEnPlus(20);

		dao.updatePerturbation(perturbation1, perturbationTmp2);

		assertEquals(perturbation1.getDureeEnPlus(), perturbationTmp2.getDureeEnPlus());
		assertEquals(perturbation1.getMotif(), perturbationTmp2.getMotif());

		em.getTransaction().begin();
		dao.deletePerturbation(perturbation1);
		em.getTransaction().commit();
	}

	@Test
	void testGetAllPerturbation() {

		em.getTransaction().begin();
		fr.pantheonsorbonne.ufr27.miage.model.jaxb.Perturbation perturbationTmp = factory.createPerturbation();
		perturbationTmp.setMotif("chevreuil");
		perturbationTmp.setTrain(TrainMapper.trainDTOMapper(train1));
		perturbationTmp.setDureeEnPlus(10);
		Perturbation perturbation1 = dao.createPerturbation(perturbationTmp);
		em.getTransaction().commit();

		List<Perturbation> listPerturbations = dao.getAllPerturbation();
		assertEquals(1, listPerturbations.size());
		assertTrue(listPerturbations.contains(perturbation1));
		assertEquals(perturbation1, listPerturbations.get(0));

		em.getTransaction().begin();
		dao.deletePerturbation(perturbation1);
		em.getTransaction().commit();

	}

	@Test
	void testDeletePerturbation() {

		em.getTransaction().begin();
		fr.pantheonsorbonne.ufr27.miage.model.jaxb.Perturbation perturbationTmp = factory.createPerturbation();
		perturbationTmp.setMotif("chevreuil");
		perturbationTmp.setTrain(TrainMapper.trainDTOMapper(train1));
		perturbationTmp.setDureeEnPlus(10);
		Perturbation perturbation1 = dao.createPerturbation(perturbationTmp);
		int id = perturbation1.getId();
		em.getTransaction().commit();

		em.getTransaction().begin();
		dao.deletePerturbation(perturbation1);
		em.getTransaction().commit();
		Perturbation perturbation3 = dao.getPerturbationFromId(id);
		assertNull(perturbation3);

	}

	@Test
	void testImpacterTrafic() {

		em.getTransaction().begin();
		trainDAO.addArret(train1, arret1, LocalDateTime.now().minusMinutes(30), LocalDateTime.now().minusMinutes(30),
				true, false);
		trainDAO.addArret(train1, arret2, LocalDateTime.now().plusMinutes(30), LocalDateTime.now().plusMinutes(30),
				true, true);
		em.getTransaction().commit();

		HeureDePassage hdp1 = hdpDAO.getHdpFromTrainIdAndArretId(train1.getId(), arret1.getId());
		HeureDePassage hdp2 = hdpDAO.getHdpFromTrainIdAndArretId(train1.getId(), arret2.getId());

		assertEquals(hdp2.getBaseArriveeTemps(), hdp2.getReelArriveeTemps());
		assertEquals(hdp1.getBaseArriveeTemps(), hdp1.getReelArriveeTemps());

		em.getTransaction().begin();
		fr.pantheonsorbonne.ufr27.miage.model.jaxb.Perturbation perturbationTmp = factory.createPerturbation();
		perturbationTmp.setMotif("chevreuil");
		perturbationTmp.setTrain(TrainMapper.trainDTOMapper(train1));
		perturbationTmp.setDureeEnPlus(10);
		Perturbation perturbation1 = dao.createPerturbation(perturbationTmp);
		int id = perturbation1.getId();
		em.getTransaction().commit();

		dao.impacterTrafic(perturbation1);

		// On vérifie que le premier arret n'est pas impacté CAR dans le passé
		assertTrue(hdp1.getBaseArriveeTemps().equals(hdp1.getReelArriveeTemps()));

		// On vérifie que le dernier arret est impacté CAR dans le futur
		assertFalse(hdp2.getBaseArriveeTemps().equals(hdp2.getReelArriveeTemps()));
		assertTrue(hdp2.getBaseArriveeTemps()
				.equals(hdp2.getReelArriveeTemps().minusMinutes(perturbationTmp.getDureeEnPlus())));

		em.getTransaction().begin();
		dao.deletePerturbation(perturbation1);
		em.getTransaction().commit();

	}

}
