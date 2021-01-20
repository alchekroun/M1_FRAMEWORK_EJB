package fr.pantheonsorbonne.ufr27.miage.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.ManagedBean;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import fr.pantheonsorbonne.ufr27.miage.dao.TrainDAO;
import fr.pantheonsorbonne.ufr27.miage.jpa.Arret;
import fr.pantheonsorbonne.ufr27.miage.jpa.HeureDePassage;
import fr.pantheonsorbonne.ufr27.miage.jpa.HeureDePassageKey;
import fr.pantheonsorbonne.ufr27.miage.jpa.Passager;
import fr.pantheonsorbonne.ufr27.miage.jpa.Train;
import fr.pantheonsorbonne.ufr27.miage.jpa.TrainAvecResa;
import fr.pantheonsorbonne.ufr27.miage.service.DataInitializerService;

@ApplicationScoped
@ManagedBean
public class DataInitializerServiceImpl implements DataInitializerService {

	private EntityManager em;

	public DataInitializerServiceImpl(EntityManager em) {
		this.em = em;
	}

	@Override
	public void fulfilBdd() {

		// Création des arrets
		em.getTransaction().begin();

		String[] nomArrets = { "Lyon Part Dieu", "Valence", "Avignon", "Aix-en-Provence", "Marseille", "Toulon",
				"Draguinan", "Saint-Raphael", "Cannes", "Antibes", "Nice-Ville", "Paris Gare de Lyon" };

		Map<String, Arret> mapArrets = new HashMap<String, Arret>();
		for (String nom : nomArrets) {
			Arret a = new Arret();
			a.setNom(nom);
			mapArrets.put(nom, a);
			em.persist(a);
		}

		em.getTransaction().commit();

		// Création des trains
		em.getTransaction().begin();

		Train train1 = new TrainAvecResa();
		train1.setNom("Lyon Part Dieu - Nice-Ville");
		train1.setDirectionType("forward");
		train1.setNumero(6801);
		train1.setReseau("SNCF");
		train1.setStatut("en marche");
		em.persist(train1);

		Train train2 = new TrainAvecResa();
		train2.setNom("Paris Gare de Lyon - Marseille");
		train2.setDirectionType("forward");
		train2.setNumero(8541);
		train2.setReseau("SNCF");
		train2.setStatut("en marche");
		em.persist(train2);

		em.getTransaction().commit();

		// Création des itineraire
		em.getTransaction().begin();

		LocalDateTime base = LocalDateTime.now();

		// - Lyon Nice
		addArret(train1, mapArrets.get("Lyon Part Dieu"), base.plusMinutes(10), base, true, false);
		addArret(train1, mapArrets.get("Valence"), base.plusMinutes(30), base.plusMinutes(20), true, false);
		addArret(train1, mapArrets.get("Avignon"), base.plusMinutes(50), base.plusMinutes(40), false, false);
		addArret(train1, mapArrets.get("Aix-en-Provence"), base.plusMinutes(70), base.plusMinutes(60), false, false);
		addArret(train1, mapArrets.get("Marseille"), base.plusMinutes(90), base.plusMinutes(80), true, false);
		addArret(train1, mapArrets.get("Toulon"), base.plusMinutes(110), base.plusMinutes(100), true, false);
		addArret(train1, mapArrets.get("Draguinan"), base.plusMinutes(130), base.plusMinutes(120), true, false);
		addArret(train1, mapArrets.get("Saint-Raphael"), base.plusMinutes(150), base.plusMinutes(140), true, false);
		addArret(train1, mapArrets.get("Cannes"), base.plusMinutes(170), base.plusMinutes(160), true, false);
		addArret(train1, mapArrets.get("Antibes"), base.plusMinutes(190), base.plusMinutes(180), true, false);
		addArret(train1, mapArrets.get("Nice-Ville"), base.plusMinutes(210), base.plusMinutes(200), true, true);

		// - Paris Marseille
		addArret(train2, mapArrets.get("Paris Gare de Lyon"), base.plusMinutes(10), base, true, false);
		addArret(train2, mapArrets.get("Avignon"), base.plusMinutes(50), base.plusMinutes(40), true, false);
		addArret(train2, mapArrets.get("Aix-en-Provence"), base.plusMinutes(70), base.plusMinutes(60), true, false);
		addArret(train2, mapArrets.get("Marseille"), base.plusMinutes(70), base.plusMinutes(60), true, false);

		em.getTransaction().commit();

		// Création des passagers
		em.getTransaction().begin();

		String[] nomPassagers = { "Alex", "Hannah", "Julien", "David", "Robert", "Norbert", "Patrick", "Roger", "Homer",
				"Frédéric", "Albert", "Didier", "Nicolas", "Georges", "Walter", "Louis", "Augustin", "Stéphanie",
				"Coralie", "Salomé", "Lea", "Corinne", "Annick" }; // 23

		for (int i = 0; i < nomPassagers.length; i++) {
			Passager p = new Passager();
			p.setNom(nomPassagers[i]);
			if (i < 15) {
				p.setDepart(mapArrets.get("Paris Gare de Lyon"));
				if (i < 5) {
					p.setArrive(mapArrets.get("Marseille"));
				} else if (i < 10) {
					p.setArrive(mapArrets.get("Avignon"));
				} else {
					p.setArrive(mapArrets.get("Nice-Ville")); // Correspondance
				}
			} else {
				p.setDepart(mapArrets.get("Lyon Part Dieu"));
				if (i < 17) {
					p.setArrive(mapArrets.get("Cannes"));
				} else if (i < 20) {
					p.setArrive(mapArrets.get("Antibes"));
				} else {
					p.setArrive(mapArrets.get("Toulon"));
				}
			}
			em.persist(p);
		}

		em.getTransaction().commit();
	}

	void addArret(Train train, Arret arret, LocalDateTime departTemps, LocalDateTime arriveeTemps, boolean desservi,
			boolean terminus) {
		HeureDePassageKey hdpKey = new HeureDePassageKey();
		hdpKey.setArretId(arret.getId());
		hdpKey.setTrainId(train.getId());
		HeureDePassage hdp = new HeureDePassage();
		hdp.setId(hdpKey);
		hdp.setArret(arret);
		hdp.setTrain(train);
		hdp.setBaseDepartTemps(departTemps);
		hdp.setReelDepartTemps(departTemps);
		hdp.setBaseArriveeTemps(arriveeTemps);
		hdp.setReelArriveeTemps(arriveeTemps);
		hdp.setDesservi(desservi);
		hdp.setTerminus(terminus);
		em.persist(hdp);
		train.addArretHeureDePassage(hdp);
		arret.addArretHeureDePassage(hdp);
		em.merge(train);
		em.merge(arret);
	}

}
