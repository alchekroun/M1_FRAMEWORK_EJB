package fr.pantheonsorbonne.ufr27.miage.jpa;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.glassfish.grizzly.utils.ArraySet;
import org.jboss.weld.junit5.EnableWeld;
import org.jboss.weld.junit5.WeldInitiator;
import org.jboss.weld.junit5.WeldSetup;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.pantheonsorbonne.ufr27.miage.tests.utils.TestPersistenceProducer;


@EnableWeld
class TestArret {
	

	@WeldSetup
	private WeldInitiator weld = WeldInitiator
			.from(Arret.class, HeureDePassage.class, HeureDePassageKey.class, InfoCentre.class, InfoGare.class, Passager.class, Train.class, Perturbation.class,TestPersistenceProducer.class)
			.activate(RequestScoped.class).build();

	@Inject
	EntityManager em;

	
	List<HeureDePassage> listeHeureDePassage;
	Set<Train> trainsArrivants;
	List<HeureDePassage> listeHeureDePassage2;
	Set<Train> trainsArrivants2;
	
	Train train1;
	Arret arret1;
	
	Arret arretDirection;
	Passager passager1;
	
	HeureDePassageKey hdpkey;
	HeureDePassage hdp;

	
	LocalDateTime passage1;
	LocalDateTime passage2;
	
	int indexTrain = 0;
	int indexArret = 0;
	int indexPassager = 0;

	@BeforeEach
	void setUp() throws Exception {
		System.out.println("\n== SetUp");
		
		listeHeureDePassage2 = new ArrayList<HeureDePassage>();
		trainsArrivants2 = new HashSet<Train>();
		
		arret1 = new Arret();
		arret1.setId(indexArret++);
		arret1.setNom("Bordeaux");
		arret1.setListeHeureDePassage(listeHeureDePassage2);
		arret1.setTrainsArrivants(trainsArrivants2);
	
		arretDirection = new Arret();
		arretDirection.setId(indexArret++);
		arretDirection.setNom("Paris");
		
		train1= new Train();
		train1.setId(indexTrain++);
		train1.setNom("Bordeaux - Paris");
		train1.setDirection(arretDirection);
		train1.setDirectionType("forward");
		train1.setStatut("enmarche");
		train1.setNumeroTrain(8541);
		train1.setReseau("SNCF");
		train1.setStatut("en marche");
		train1.setBaseDepartTemps(LocalDateTime.now().plusMinutes(10));
		train1.setBaseArriveeTemps(LocalDateTime.now().plusMinutes(30));
		train1.setReelDepartTemps(LocalDateTime.now().plusMinutes(10));
		train1.setReelArriveeTemps(LocalDateTime.now().plusMinutes(30));
		
		//hdp1
		hdpkey = new HeureDePassageKey ();
		hdpkey.setArretId(arret1.getId());
		hdpkey.setTrainId(train1.getId());
		
		passage1 = LocalDateTime.now().plusMinutes(5);
		
		hdp = new HeureDePassage();;
		hdp.setId(hdpkey);
		hdp.setTrain(train1);
		hdp.setArret(arret1);
		hdp.setPassage(passage1);
		
		listeHeureDePassage = new ArrayList<HeureDePassage>();
		listeHeureDePassage.add(hdp);	
		arret1.addArretHeureDePassage(hdp);
		
		trainsArrivants = new HashSet<Train>();
		trainsArrivants.add(train1);
		arret1.addTrainArrivant(train1);
		
	}

	@AfterEach
	void tearDown() throws Exception {
		System.out.println("\n== TearDown");
	}
	
	@Test
	void testAddArretHeureDePassage() {
		assertEquals(arret1.getListeHeureDePassage(),listeHeureDePassage);
	}

	@Test
	void testRemoveArretHeureDePassage() {
		listeHeureDePassage.remove(hdp);
		arret1.removeArretHeureDePassage(hdp);
		assertEquals(arret1.getListeHeureDePassage(),listeHeureDePassage);
	}

	@Test
	void testAddTrainArrivant() {
		assertEquals(arret1.getTrainsArrivants(),trainsArrivants);
	}

	@Test
	void testRemoveTrainArrivant() {
		trainsArrivants.remove(train1);
		arret1.removeTrainArrivant(train1);
		assertEquals(arret1.getTrainsArrivants(),trainsArrivants);
	}

}
