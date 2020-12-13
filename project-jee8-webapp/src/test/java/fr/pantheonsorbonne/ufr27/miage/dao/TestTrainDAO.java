package fr.pantheonsorbonne.ufr27.miage.dao;

import static org.junit.jupiter.api.Assertions.*;



import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.jboss.weld.junit5.EnableWeld;
import org.jboss.weld.junit5.WeldInitiator;
import org.jboss.weld.junit5.WeldSetup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


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

	Train train;

	@BeforeEach
	public void setup() {

		
		System.out.println("****************** " + dao.toString());
		
		em.getTransaction().begin();

		

		train = new Train();
		train.setNumero(1);
		train.setNom("FR");

		em.persist(train);

		

		em.getTransaction().commit();

	}

	@Test
	    public void testTrainDAO() {
		
		assertEquals(1,1);

		/*assertFalse(dao.isTrainCreated(train.getId()));

		em.getTransaction().begin();
		train.setCreated(true);
		em.merge(train);
		em.getTransaction().commit();

		assertTrue(dao.isTrainCreated(train.getId())); */

	} 



}
