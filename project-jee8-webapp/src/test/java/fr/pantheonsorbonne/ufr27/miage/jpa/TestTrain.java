package fr.pantheonsorbonne.ufr27.miage.jpa;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TestTrain extends Train {

	List<HeureDePassage> listeHeureDePassage;
	List<Passager> listePassagers;
	List<HeureDePassage> listeHeureDePassage2;
	List<Passager> listePassagers2;

	Train train1;
	Arret arretDepart;

	Arret arretArrivee;
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
		listePassagers2 = new ArrayList<Passager>();

		arretDepart = new Arret();
		arretDepart.setId(indexArret++);
		arretDepart.setNom("Bordeaux");

		arretArrivee = new Arret();
		arretArrivee.setId(indexArret++);
		arretArrivee.setNom("Paris");

		train1 = new Train();
		train1.setId(indexTrain++);
		train1.setNom("Bordeaux - Paris");
		train1.setDirectionType("forward");
		train1.setStatut("enmarche");
		train1.setNumero(8541);
		train1.setReseau("SNCF");
		train1.setStatut("en marche");
		train1.setListeHeureDePassage(listeHeureDePassage2);
		train1.setListePassagers(listePassagers2);

		hdpkey = new HeureDePassageKey();
		hdpkey.setArretId(arretDepart.getId());
		hdpkey.setTrainId(train1.getId());

		passage1 = LocalDateTime.now().plusMinutes(5);

		hdp = new HeureDePassage();
		hdp.setId(hdpkey);
		hdp.setTrain(train1);
		hdp.setArret(arretDepart);

		listeHeureDePassage = new ArrayList<HeureDePassage>();
		listeHeureDePassage.add(hdp);
		train1.addArretHeureDePassage(hdp);

		passager1 = new Passager();
		passager1.setNom("Hanna Naccache");
		passager1.setid(indexPassager++);
		passager1.setArrive(arretArrivee);
		passager1.setDepart(arretDepart);
		passager1.setTrain(train1);

		listePassagers = new ArrayList<Passager>();
		listePassagers.add(passager1);
		train1.addPassager(passager1);

	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testAddArretHeureDePassage() {
		assertEquals(train1.getListeHeureDePassage(), listeHeureDePassage);
	}

	@Test
	void testRemoveArretHeureDePassage() {
		listeHeureDePassage.remove(hdp);
		train1.removeArretHeureDePassage(hdp);
		assertEquals(train1.getListeHeureDePassage(), listeHeureDePassage);
	}

	@Test
	void testAddPassager() {
		assertEquals(train1.getListePassagers(), listePassagers);
	}

	@Test
	void testRemovePassager() {
		listePassagers.remove(passager1);
		train1.removePassager(passager1);
		assertEquals(train1.getListePassagers(), listePassagers);
	}
}
