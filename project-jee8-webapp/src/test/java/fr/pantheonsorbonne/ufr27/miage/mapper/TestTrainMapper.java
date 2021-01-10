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

import fr.pantheonsorbonne.ufr27.miage.jpa.Train;
import fr.pantheonsorbonne.ufr27.miage.jpa.TrainAvecResa;
import fr.pantheonsorbonne.ufr27.miage.dao.HeureDePassageDAO;
import fr.pantheonsorbonne.ufr27.miage.dao.TrainDAO;
import fr.pantheonsorbonne.ufr27.miage.jpa.Arret;
import fr.pantheonsorbonne.ufr27.miage.jpa.Passager;
import fr.pantheonsorbonne.ufr27.miage.tests.utils.TestPersistenceProducer;

@EnableWeld
class TestTrainMapper {
	@WeldSetup
	private WeldInitiator weld = WeldInitiator.from(Passager.class,Train.class,TrainDAO.class,HeureDePassageDAO.class, TrainMapper.class, TestPersistenceProducer.class)
			.activate(RequestScoped.class).build();

	@Inject
	EntityManager em;

	// JPA
	Arret arretJPA1;
	Arret arretJPA2;
	Train trainJPA1;
	Passager passagerJPA1;
	List<Train> listTrain = new ArrayList<Train>();

	@BeforeEach
	void setUp() throws Exception {
		em.getTransaction().begin();

		arretJPA1 = new Arret();
		arretJPA1.setNom("Paris");
		em.persist(arretJPA1);

		arretJPA2 = new Arret();
		arretJPA2.setNom("Rouen");
		em.persist(arretJPA2);

		passagerJPA1 = new Passager();
		passagerJPA1.setNom("Alex");
		passagerJPA1.setDepart(arretJPA1);
		passagerJPA1.setArrive(arretJPA2);
		em.persist(passagerJPA1);

		trainJPA1 = new TrainAvecResa();
		trainJPA1.setNom("Nom");
		trainJPA1.setDirectionType("forward");
		trainJPA1.setReseau("SNCF");
		trainJPA1.setStatut("En marche");
		trainJPA1.addPassager(passagerJPA1);
		em.persist(trainJPA1);
		listTrain.add(trainJPA1);

		em.getTransaction().commit();

	}

	@AfterEach
	void tearDown() throws Exception {
		em.getTransaction().begin();
		em.remove(passagerJPA1);
		em.remove(trainJPA1);
		em.remove(arretJPA1);
		em.remove(arretJPA2);
		em.getTransaction().commit();
	}

	@Test
	void testTrainDTOMapper() {
		fr.pantheonsorbonne.ufr27.miage.model.jaxb.Train trainJAXBtest = TrainMapper.trainDTOMapper(trainJPA1);

		assertEquals(trainJAXBtest.getId(), trainJPA1.getId());
		assertEquals(trainJAXBtest.getNom(), trainJPA1.getNom());
		assertEquals(trainJAXBtest.getDirectionType(), trainJPA1.getDirectionType());
		assertEquals(trainJAXBtest.getReseau(), trainJPA1.getReseau());
		assertEquals(trainJAXBtest.getListePassagers().get(0).getNom(), trainJPA1.getListePassagers().get(0).getNom());

	}

	@Test
	void testTrainAllDTOMapper() {
		List<fr.pantheonsorbonne.ufr27.miage.model.jaxb.Train> listTrainJAXBtest = TrainMapper
				.trainAllDTOMapper(listTrain);
		assertEquals(listTrainJAXBtest.size(), listTrain.size());
		for (int i = 0; i < listTrain.size(); i++) {
			assertEquals(listTrainJAXBtest.get(i).getId(), listTrain.get(i).getId());
			assertEquals(listTrainJAXBtest.get(i).getNom(), listTrain.get(i).getNom());
			assertEquals(listTrainJAXBtest.get(i).getDirectionType(), listTrain.get(i).getDirectionType());
			assertEquals(listTrainJAXBtest.get(i).getReseau(), listTrain.get(i).getReseau());
			assertEquals(listTrainJAXBtest.get(i).getListePassagers().get(0).getNom(),
					listTrain.get(i).getListePassagers().get(0).getNom());
		}

	}

}
