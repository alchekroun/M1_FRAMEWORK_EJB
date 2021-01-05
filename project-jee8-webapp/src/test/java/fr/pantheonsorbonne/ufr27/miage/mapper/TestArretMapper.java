package fr.pantheonsorbonne.ufr27.miage.mapper;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
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
import fr.pantheonsorbonne.ufr27.miage.tests.utils.TestPersistenceProducer;

@EnableWeld
class TestArretMapper {
	@WeldSetup
	private WeldInitiator weld = WeldInitiator.from(ArretMapper.class, TestPersistenceProducer.class)
			.activate(RequestScoped.class).build();

	@Inject
	EntityManager em;

	// JPA
	Arret arretJPA1;
	Arret arretJPA2;
	List<Arret> listArret = new ArrayList<Arret>();

	@BeforeEach
	void setUp() throws Exception {
		em.getTransaction().begin();

		arretJPA1 = new Arret();
		arretJPA1.setNom("Paris");
		em.persist(arretJPA1);
		listArret.add(arretJPA1);

		arretJPA2 = new Arret();
		arretJPA2.setNom("Rouen");
		em.persist(arretJPA2);
		listArret.add(arretJPA2);

		em.getTransaction().commit();
	}

	@AfterEach
	void tearDownAfterClass() throws Exception {
		em.getTransaction().begin();
		em.remove(arretJPA1);
		em.remove(arretJPA2);
		em.getTransaction().commit();
	}

	@Test
	void testArretDTOMapper() {
		fr.pantheonsorbonne.ufr27.miage.model.jaxb.Arret arretJAXBtest = ArretMapper.arretDTOMapper(arretJPA1);

		assertEquals(arretJAXBtest.getId(), arretJPA1.getId());
		assertEquals(arretJAXBtest.getNom(), arretJPA1.getNom());
	}

	@Test
	void testArretAllDTOMapper() {
		List<fr.pantheonsorbonne.ufr27.miage.model.jaxb.Arret> listArretJAXBtest = ArretMapper
				.arretAllDTOMapper(listArret);
		assertEquals(listArretJAXBtest.size(), listArret.size());
		for (int i = 0; i < listArret.size(); i++) {
			assertEquals(listArretJAXBtest.get(i).getId(), listArret.get(i).getId());
			assertEquals(listArretJAXBtest.get(i).getNom(), listArret.get(i).getNom());
		}
	}

}
