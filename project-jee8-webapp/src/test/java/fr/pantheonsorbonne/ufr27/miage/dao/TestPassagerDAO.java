
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
import fr.pantheonsorbonne.ufr27.miage.jpa.Passager;
import fr.pantheonsorbonne.ufr27.miage.jpa.Train;
import fr.pantheonsorbonne.ufr27.miage.jpa.TrainAvecResa;
import fr.pantheonsorbonne.ufr27.miage.model.jaxb.ObjectFactory;
import fr.pantheonsorbonne.ufr27.miage.tests.utils.TestPersistenceProducer;

@EnableWeld
class TestPassagerDAO {
	@WeldSetup
	private WeldInitiator weld = WeldInitiator.from(PassagerDAO.class, TestPersistenceProducer.class, TrainDAO.class,
			HeureDePassageDAO.class, ArretDAO.class, PerturbationDAO.class).activate(RequestScoped.class).build();

	@Inject
	EntityManager em;

	@Inject
	PassagerDAO dao;

	@Inject
	ArretDAO arretDao;

	@Inject
	TrainDAO trainDao;

	@Inject
	HeureDePassageDAO hdpDao;

	Train train1;
	Train train2;
	Train train3;
	Train train4;
	Train train5;
	Passager passager1;
	Passager passager2;
	Passager passager3;
	Passager passager4;
	Passager passager5;
	Passager passager6;
	Passager passager7;
	// Arret Bordeaux
	Arret arretDepart;
	// Arret Paris
	Arret arretArrivee;
	Arret arretTours;
	Arret arretLyon;
	Arret arretMarseille;
	Arret arretLille;
	Arret arretBretagne;
	Arret arretNice;

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

		arretTours = new Arret();
		arretTours.setNom("Tours");
		em.persist(arretTours);

		arretLyon = new Arret();
		arretLyon.setNom("Lyon");
		em.persist(arretLyon);

		arretLille = new Arret();
		arretLille.setNom("Lille");
		em.persist(arretLille);

		arretMarseille = new Arret();
		arretMarseille.setNom("Marseille");
		em.persist(arretMarseille);

		arretBretagne = new Arret();
		arretBretagne.setNom("Bretagne");
		em.persist(arretBretagne);

		arretNice = new Arret();
		arretNice.setNom("Nice");
		em.persist(arretNice);

		train1 = new TrainAvecResa();
		train1.setNom("Bordeaux - Paris - Bretagne");
		train1.setStatut("on");
		train1.setNumero(8541);
		train1.setReseau("SNCF");
		em.persist(train1);

		train2 = new TrainAvecResa();
		train2.setNom("Paris - Tours - Lille");
		train2.setStatut("on");
		train2.setNumero(7400);
		train2.setReseau("SNCF");
		em.persist(train2);

		train3 = new TrainAvecResa();
		train3.setNom("Bordeaux - Marseille");
		train3.setStatut("on");
		train3.setNumero(6020);
		train3.setReseau("SNCF");
		em.persist(train3);

		train4 = new TrainAvecResa();
		train4.setNom("Bretagne - Lille");
		train4.setStatut("on");
		train4.setNumero(5350);
		train4.setReseau("SNCF");
		em.persist(train4);

		train5 = new TrainAvecResa();
		train5.setNom("Bordeaux - Lyon - Paris");
		train5.setStatut("on");
		train5.setNumero(3410);
		train5.setReseau("SNCF");
		em.persist(train5);

		passager1 = new Passager();
		passager1.setNom("David");
		passager1.setArrive(arretArrivee);
		passager1.setDepart(arretDepart);
		passager1.setTrain(train1);
		em.persist(passager1);

		passager2 = new Passager();
		passager2.setNom("Julien");
		passager2.setArrive(arretLille);
		passager2.setDepart(arretDepart);
		passager2.setTrain(train3);
		em.persist(passager2);

		passager3 = new Passager();
		passager3.setNom("Hanna");
		passager3.setArrive(arretTours);
		passager3.setDepart(arretDepart);
		passager3.setTrain(train2);
		em.persist(passager3);

		passager4 = new Passager();
		passager4.setNom("Alexandre");
		passager4.setArrive(arretNice);
		passager4.setDepart(arretDepart);
		passager4.setTrain(train2);
		em.persist(passager4);

		passager5 = new Passager();
		passager5.setNom("Frank");
		passager5.setArrive(arretLyon);
		passager5.setDepart(arretDepart);
		em.persist(passager5);

		passager6 = new Passager();
		passager6.setNom("Lucie");
		passager6.setArrive(arretDepart);
		passager6.setDepart(arretDepart);
		em.persist(passager6);

		passager7 = new Passager();
		passager7.setNom("Paul");
		passager7.setArrive(arretArrivee);
		passager7.setDepart(arretLyon);
		em.persist(passager7);

		em.getTransaction().commit();

		LocalDateTime date = LocalDateTime.now();

		// Bordeaux ( hdp dans le passé) - Paris (hdp dans le passé) - Bordeaux - Paris
		// - Bretagne

		em.getTransaction().begin();
		trainDao.addArret(train1, arretDepart, date.plusMinutes(30), date.plusMinutes(10), true, false);
		em.getTransaction().commit();

		em.getTransaction().begin();
		trainDao.addArret(train1, arretArrivee, date.plusMinutes(60), date.plusMinutes(40), true, false);
		em.getTransaction().commit();

		em.getTransaction().begin();
		trainDao.addArret(train1, arretBretagne, date.plusMinutes(80), date.plusMinutes(70), true, false);
		em.getTransaction().commit();

		// Nice - Paris - Tours - Lille

		em.getTransaction().begin();
		trainDao.addArret(train2, arretNice, date.minusMinutes(200), date.minusMinutes(230), true, false);
		em.getTransaction().commit();

		em.getTransaction().begin();
		trainDao.addArret(train2, arretArrivee, date.plusMinutes(130), date.plusMinutes(120), true, false);
		em.getTransaction().commit();

		em.getTransaction().begin();
		trainDao.addArret(train2, arretTours, date.plusMinutes(150), date.plusMinutes(140), true, false);
		em.getTransaction().commit();

		em.getTransaction().begin();
		trainDao.addArret(train2, arretLille, date.plusMinutes(180), date.plusMinutes(170), true, false);
		em.getTransaction().commit();

		// Paris (hdp dans le passé) - Bordeaux - Marseille

		em.getTransaction().begin();
		trainDao.addArret(train3, arretDepart, date.plusMinutes(30), date.plusMinutes(10), true, false);
		em.getTransaction().commit();

		em.getTransaction().begin();
		trainDao.addArret(train3, arretMarseille, date.plusMinutes(80), date.plusMinutes(60), true, false);
		em.getTransaction().commit();

		// Bretagne - Lille

		em.getTransaction().begin();
		trainDao.addArret(train4, arretBretagne, date.plusMinutes(100), null, true, false);
		em.getTransaction().commit();

		em.getTransaction().begin();
		trainDao.addArret(train4, arretLille, null, date.plusMinutes(150), true, false);
		em.getTransaction().commit();

		// Bordeaux - Lyon - Paris

		em.getTransaction().begin();
		trainDao.addArret(train5, arretDepart, date.plusMinutes(20), null, true, false);
		em.getTransaction().commit();

		em.getTransaction().begin();
		trainDao.addArret(train5, arretLyon, date.plusMinutes(180), date.plusMinutes(150), true, false);
		em.getTransaction().commit();

		em.getTransaction().begin();
		trainDao.addArret(train5, arretArrivee, date.plusMinutes(280), date.plusMinutes(200), true, false);
		em.getTransaction().commit();

	}

	@AfterEach
	public void tearDown() {
		em.getTransaction().begin();
		em.remove(passager1);
		passager1 = null;
		em.remove(passager2);
		passager2 = null;
		em.remove(passager3);
		passager3 = null;
		em.remove(passager4);
		passager4 = null;
		em.remove(passager5);
		passager5 = null;
		em.remove(passager6);
		passager6 = null;
		em.remove(passager7);
		passager7 = null;

		trainDao.removeArret(train1, arretDepart);
		trainDao.removeArret(train1, arretArrivee);
		trainDao.removeArret(train1, arretBretagne);

		trainDao.removeArret(train2, arretNice);
		trainDao.removeArret(train2, arretArrivee);
		trainDao.removeArret(train2, arretTours);
		trainDao.removeArret(train2, arretLille);

		trainDao.removeArret(train3, arretDepart);
		trainDao.removeArret(train3, arretMarseille);

		trainDao.removeArret(train4, arretBretagne);
		trainDao.removeArret(train4, arretLille);

		trainDao.removeArret(train5, arretDepart);
		trainDao.removeArret(train5, arretLyon);
		trainDao.removeArret(train5, arretArrivee);

		em.remove(train1);
		train1 = null;
		em.remove(train2);
		train2 = null;
		em.remove(train3);
		train3 = null;
		em.remove(train4);
		train4 = null;
		em.remove(train5);
		train5 = null;
		em.remove(arretDepart);
		arretDepart = null;
		em.remove(arretArrivee);
		arretArrivee = null;
		em.remove(arretLille);
		arretLille = null;
		em.remove(arretTours);
		arretTours = null;
		em.remove(arretLyon);
		arretLyon = null;
		em.remove(arretMarseille);
		arretMarseille = null;
		em.remove(arretBretagne);
		arretBretagne = null;
		em.remove(arretNice);
		arretNice = null;

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

		assertEquals(7, passagers.size());

		assertTrue(passagers.contains(passager1));
		assertTrue(passagers.contains(passager2));
		assertTrue(passagers.contains(passager3));
		assertTrue(passagers.contains(passager4));
		assertTrue(passagers.contains(passager5));
		assertTrue(passagers.contains(passager6));
	}

	@Test
	void testGetAllPassagerByTrain() {
		List<Passager> passagers = dao.getAllPassagerByTrain(train1.getId());
		assertEquals(1, passagers.size());
		assertEquals(passager1, passagers.get(0));
	}

	@Test
	void testgetAllPassagerByDepart() {
		List<Passager> passagers = dao.getAllPassagerByDepart(arretDepart.getId());
		assertEquals(6, passagers.size());

		assertTrue(passagers.contains(passager1));
		assertTrue(passagers.contains(passager2));
		assertTrue(passagers.contains(passager3));
		assertTrue(passagers.contains(passager4));
		assertTrue(passagers.contains(passager5));
		assertTrue(passagers.contains(passager6));
	}

	@Test
	void testGetAllPassagerByArrivee() {
		List<Passager> passagers = dao.getAllPassagerByArrivee(arretArrivee.getId());
		assertEquals(2, passagers.size());
		assertTrue(passagers.contains(passager1));

	}

	@Test
	void testFindTrajet() {

		// prend le train1 Bordeaux - Paris plutôt que Bordeaux - Lyon - Paris qui est
		// plus long
		// ne prend pas le train3 non plus car hdp de Paris dans le passé
		Train trainAttribue = dao.findTrajet(passager1.getId());
		assertEquals(train1.getId(), trainAttribue.getId());

		// prend le train1 Bordeaux - Paris - Bretagne pour ensuite prendre train4
		// Bretagne - Lille plutôt que Paris-Tours-Lille qui est plus long
		Train trainCorrespondance = dao.findTrajet(passager2.getId());
		assertEquals(train1.getId(), trainCorrespondance.getId());
		assertEquals(arretBretagne.getId(), passager2.getCorrespondance().getId());

		assertEquals(train1.getId(), dao.findTrajet(passager3.getId()).getId());
		// correspondance a Paris pour aller a Tours
		assertEquals(arretArrivee.getId(), passager3.getCorrespondance().getId());

		// renvoie null car pas de train pour Nice car l'hdp est déjà passée et plus hdp
		// existante dans le futur pour Nice
		assertNull(dao.findTrajet(passager4.getId()));

		// renvoie null car la personne est déjà à son arrêt d'arrivée
		assertNull(dao.findTrajet(passager6.getId()));

		Train trainAttribueLyon = dao.findTrajet(passager5.getId());
		assertEquals(train5.getId(), trainAttribueLyon.getId());

		assertEquals(train5.getId(), dao.findTrajet(passager7.getId()).getId());

		HeureDePassage newHdp = null;
		for (HeureDePassage hdp : train5.getListeHeureDePassage()) {
			if (hdp.getArret().getId() == arretLyon.getId()) {
				newHdp = hdp;
			}
		}
		hdpDao.changeParameterDesservi(newHdp, false);
		// renvoie null car maintenant arret non desservi
		assertNull(dao.findTrajet(passager5.getId()));

		assertNull(dao.findTrajet(passager7.getId()));

		newHdp = null;
		for (HeureDePassage hdp : train1.getListeHeureDePassage()) {
			if (hdp.getArret().getId() == arretBretagne.getId()) {
				newHdp = hdp;
			}
		}
		hdpDao.changeParameterDesservi(newHdp, false);
		trainCorrespondance = dao.findTrajet(passager2.getId());
		assertEquals(train1.getId(), trainCorrespondance.getId());
		assertEquals(arretArrivee.getId(), passager2.getCorrespondance().getId());

	}

	@Test
	void testFindPassagerByCorrespondance() {
		assertTrue(dao.getAllPassagerByCorrespondance(arretBretagne.getId()).isEmpty());
		assertTrue(dao.getAllPassagerByCorrespondance(arretArrivee.getId()).isEmpty());

		em.getTransaction().begin();
		em.merge(dao.findTrajet(passager2.getId()));
		em.getTransaction().commit();
		em.getTransaction().begin();
		dao.findTrajet(passager3.getId());
		em.getTransaction().commit();

		List<Passager> list1 = dao.getAllPassagerByCorrespondance(arretBretagne.getId());
		List<Passager> list2 = dao.getAllPassagerByCorrespondance(arretArrivee.getId());

		assertEquals(1, list1.size());
		assertEquals(passager2.getId(), list1.get(0).getId());

		assertEquals(1, list2.size());
		assertEquals(passager3.getId(), list2.get(0).getId());
	}

}
