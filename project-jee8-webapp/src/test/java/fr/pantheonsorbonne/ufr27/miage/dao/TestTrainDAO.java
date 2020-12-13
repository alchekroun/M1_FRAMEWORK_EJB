package fr.pantheonsorbonne.ufr27.miage.dao;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.jboss.weld.junit5.EnableWeld;
import org.jboss.weld.junit5.WeldInitiator;
import org.jboss.weld.junit5.WeldSetup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.pantheonsorbonne.ufr27.miage.jpa.Arret;
import fr.pantheonsorbonne.ufr27.miage.jpa.Train;

import fr.pantheonsorbonne.ufr27.miage.tests.utils.TestPersistenceProducer;

@EnableWeld
public class TestTrainDAO {
	@WeldSetup
	private WeldInitiator weld = WeldInitiator.from(TrainDAO.class, TestPersistenceProducer.class)
			.activate(RequestScoped.class).build();

	@Inject
	EntityManager em;

	@Inject
	TrainDAO dao;

	Train train1;

	@BeforeEach
	public void setup() {

		System.out.println("****************** " + dao.toString());

		em.getTransaction().begin();
		
		Arret arret1Paris = new Arret();
		arret1Paris.setNom("Paris");
		em.persist(arret1Paris);

		train1 = new Train();
		train1.setNom("Bordeaux - Paris");
		train1.setDirection(arret1Paris);
		train1.setDirectionType("forward");
		train1.setStatut("enmarche");
		train1.setNumeroTrain(8541);
		train1.setReseau("SNCF");
		train1.setStatut("en marche");
		train1.setBaseDepartTemps(LocalDateTime.now().plusMinutes(10));
		train1.setBaseArriveeTemps(LocalDateTime.now().plusMinutes(30));
		train1.setReelDepartTemps(LocalDateTime.now().plusMinutes(10));
		train1.setReelArriveeTemps(LocalDateTime.now().plusMinutes(30));
		em.persist(train1);

		em.getTransaction().commit();

	}

	@Test
	public void testTrainDAO() {

		assertFalse(dao.isTrainCreated(train1.getId()));

		em.getTransaction().begin();
		train1.setCreated(true);
		em.merge(train1);
		em.getTransaction().commit();

		assertTrue(dao.isTrainCreated(train1.getId()));

	}

}
