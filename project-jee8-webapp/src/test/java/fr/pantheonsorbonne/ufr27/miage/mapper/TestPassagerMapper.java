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
import fr.pantheonsorbonne.ufr27.miage.jpa.Passager;
import fr.pantheonsorbonne.ufr27.miage.tests.utils.TestPersistenceProducer;

@EnableWeld
class TestPassagerMapper {
	@WeldSetup
	private WeldInitiator weld = WeldInitiator.from(PassagerMapper.class, TestPersistenceProducer.class)
			.activate(RequestScoped.class).build();

	@Inject
	EntityManager em;

	// JPA
	Arret arretJPA1;
	Arret arretJPA2;
	Arret arretJPA3;
	Passager passagerJPA1;
	List<Passager> listPassager = new ArrayList<Passager>();

	@BeforeEach
	void setUp() throws Exception {
		em.getTransaction().begin();

		arretJPA1 = new Arret();
		arretJPA1.setNom("Paris");
		em.persist(arretJPA1);

		arretJPA2 = new Arret();
		arretJPA2.setNom("Rouen");
		em.persist(arretJPA2);

		arretJPA3 = new Arret();
		arretJPA3.setNom("Defense");
		em.persist(arretJPA3);

		passagerJPA1 = new Passager();
		passagerJPA1.setNom("Alex");
		passagerJPA1.setDepart(arretJPA1);
		passagerJPA1.setArrive(arretJPA2);
		passagerJPA1.setCorrespondance(arretJPA3);
		em.persist(passagerJPA1);

		listPassager.add(passagerJPA1);

		em.getTransaction().commit();
	}

	@AfterEach
	void tearDown() throws Exception {
		em.getTransaction().begin();
		em.remove(passagerJPA1);
		em.remove(arretJPA1);
		em.remove(arretJPA2);
		em.remove(arretJPA3);
		em.getTransaction().commit();
	}

	@Test
	void testPassagerDTOMapper() {
		fr.pantheonsorbonne.ufr27.miage.model.jaxb.Passager passagerJAXBtest = PassagerMapper
				.passagerDTOMapper(passagerJPA1);

		assertEquals(passagerJAXBtest.getId(), passagerJPA1.getId());
		assertEquals(passagerJAXBtest.getNom(), passagerJPA1.getNom());
		assertEquals(passagerJAXBtest.getDepart().getNom(), passagerJPA1.getDepart().getNom());
		assertEquals(passagerJAXBtest.getArrive().getNom(), passagerJPA1.getArrive().getNom());
		assertEquals(passagerJAXBtest.getCorrespondance().getNom(), passagerJPA1.getCorrespondance().getNom());
		assertEquals(passagerJAXBtest.isArrived(), passagerJPA1.isArrived());

	}

	@Test
	void testPassagerAllDTOMapper() {
		List<fr.pantheonsorbonne.ufr27.miage.model.jaxb.Passager> listPassagerJAXBtest = PassagerMapper
				.passagerAllDTOMapper(listPassager);
		assertEquals(listPassagerJAXBtest.size(), listPassager.size());
		for (int i = 0; i < listPassager.size(); i++) {
			assertEquals(listPassagerJAXBtest.get(i).getId(), listPassager.get(i).getId());
			assertEquals(listPassagerJAXBtest.get(i).getNom(), listPassager.get(i).getNom());
			assertEquals(listPassagerJAXBtest.get(i).getDepart().getNom(), listPassager.get(i).getDepart().getNom());
			assertEquals(listPassagerJAXBtest.get(i).getArrive().getNom(), listPassager.get(i).getArrive().getNom());
			assertEquals(listPassagerJAXBtest.get(i).getCorrespondance().getNom(),
					listPassager.get(i).getCorrespondance().getNom());
			assertEquals(listPassagerJAXBtest.get(i).isArrived(), listPassager.get(i).isArrived());
		}
	}

}
