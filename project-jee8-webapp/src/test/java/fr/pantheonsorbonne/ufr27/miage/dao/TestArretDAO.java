package fr.pantheonsorbonne.ufr27.miage.dao;

import static org.junit.Assert.*;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.jboss.weld.junit5.WeldInitiator;
import org.jboss.weld.junit5.WeldSetup;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

import fr.pantheonsorbonne.ufr27.miage.jpa.Arret;
import fr.pantheonsorbonne.ufr27.miage.tests.utils.TestPersistenceProducer;

public class TestArretDAO {
	@WeldSetup
	private WeldInitiator weld = WeldInitiator.from(ArretDAO.class, TestPersistenceProducer.class)
			.activate(RequestScoped.class).build();

	@Inject
	EntityManager em;

	@Inject
	ArretDAO dao;

	Arret train1;

	@BeforeEach
	public void setup() {

	}

	@Test
	public void testGetArretFromId() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetAllArret() {
		fail("Not yet implemented");
	}

	@Test
	public void testDeleteArret() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetAllArretByTrain() {
		fail("Not yet implemented");
	}

}
