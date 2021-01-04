package fr.pantheonsorbonne.ufr27.miage.mapper;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
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

import fr.pantheonsorbonne.ufr27.miage.dao.HeureDePassageDAO;
import fr.pantheonsorbonne.ufr27.miage.dao.TrainDAO;
import fr.pantheonsorbonne.ufr27.miage.model.jaxb.Arret;
import fr.pantheonsorbonne.ufr27.miage.tests.utils.TestPersistenceProducer;

@EnableWeld
class TestArretMapper {
	@WeldSetup
	private WeldInitiator weld = WeldInitiator
			.from(ArretMapper.class, TrainDAO.class, HeureDePassageDAO.class, TestPersistenceProducer.class)
			.activate(RequestScoped.class).build();

	@Inject
	EntityManager em;

	@Inject
	TrainDAO trainDAO;

	// JPA
	fr.pantheonsorbonne.ufr27.miage.jpa.Arret arretJPA1;
	fr.pantheonsorbonne.ufr27.miage.jpa.Arret arretJPA2;
	List<fr.pantheonsorbonne.ufr27.miage.jpa.Arret> listArret = new ArrayList<fr.pantheonsorbonne.ufr27.miage.jpa.Arret>();

	fr.pantheonsorbonne.ufr27.miage.jpa.Train trainJPA1;

	@BeforeEach
	void setUp() throws Exception {
		em.getTransaction().begin();

		arretJPA1 = new fr.pantheonsorbonne.ufr27.miage.jpa.Arret();
		arretJPA1.setNom("Paris");
		em.persist(arretJPA1);
		listArret.add(arretJPA1);

		arretJPA2 = new fr.pantheonsorbonne.ufr27.miage.jpa.Arret();
		arretJPA2.setNom("Rouen");
		em.persist(arretJPA2);
		listArret.add(arretJPA2);

		trainJPA1 = new fr.pantheonsorbonne.ufr27.miage.jpa.Train();
		trainJPA1.setId(1);
		trainJPA1.setNom("Bordeaux - Paris");
		trainJPA1.setDirection(arretJPA1);
		trainJPA1.setDirectionType("forward");
		trainJPA1.setStatut("enmarche");
		trainJPA1.setNumero(8541);
		trainJPA1.setReseau("SNCF");
		trainJPA1.setStatut("en marche");
		trainJPA1.setBaseDepartTemps(LocalDateTime.now().plusMinutes(10));
		trainJPA1.setBaseArriveeTemps(LocalDateTime.now().plusMinutes(30));
		trainJPA1.setReelDepartTemps(LocalDateTime.now().plusMinutes(10));
		trainJPA1.setReelArriveeTemps(LocalDateTime.now().plusMinutes(30));
		em.persist(trainJPA1);

		trainDAO.addArret(trainJPA1, arretJPA2, LocalDateTime.now().plusMinutes(20));

		em.getTransaction().commit();
	}

	@AfterEach
	void tearDownAfterClass() throws Exception {
		em.getTransaction().begin();
		trainDAO.removeArret(trainJPA1, arretJPA2);
		em.remove(trainJPA1);
		em.remove(arretJPA1);
		em.remove(arretJPA2);
		em.getTransaction().commit();
	}

	@Test
	void testArretDTOMapper() {
		Arret arretJAXBtest = ArretMapper.arretDTOMapper(arretJPA1);

		assertEquals(arretJAXBtest.getId(), arretJPA1.getId());
		assertEquals(arretJAXBtest.getNom(), arretJPA1.getNom());
		assertEquals(arretJAXBtest.getListeHeureDePassages().size(), arretJPA1.getListeHeureDePassage().size());
		assertEquals(arretJAXBtest.getTrainsArrivants().size(), arretJPA1.getTrainsArrivants().size());
	}

	@Test
	void testArretAllDTOMapper() {
		List<Arret> listArretJAXBtest = ArretMapper.arretAllDTOMapper(listArret);
		assertEquals(listArretJAXBtest.size(), listArret.size());
		for (int i = 0; i < listArret.size(); i++) {
			assertEquals(listArretJAXBtest.get(i).getId(), listArret.get(i).getId());
			assertEquals(listArretJAXBtest.get(i).getNom(), listArret.get(i).getNom());
			assertEquals(listArretJAXBtest.get(i).getListeHeureDePassages().size(),
					arretJPA1.getListeHeureDePassage().size());
			assertEquals(listArretJAXBtest.get(i).getTrainsArrivants().size(), arretJPA1.getTrainsArrivants().size());
		}
	}

}
