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

import fr.pantheonsorbonne.ufr27.miage.jpa.Perturbation;
import fr.pantheonsorbonne.ufr27.miage.jpa.Train;
import fr.pantheonsorbonne.ufr27.miage.jpa.TrainAvecResa;
import fr.pantheonsorbonne.ufr27.miage.tests.utils.TestPersistenceProducer;

@EnableWeld
class TestPerturbationMapper {
	@WeldSetup
	private WeldInitiator weld = WeldInitiator.from(PerturbationMapper.class, TestPersistenceProducer.class)
			.activate(RequestScoped.class).build();

	@Inject
	EntityManager em;

	// JPA
	Perturbation perturbationJPA1;
	Perturbation perturbationJPA2;
	Train trainJPA;
	List<Perturbation> listPerturbation = new ArrayList<Perturbation>();

	@BeforeEach
	void setUp() throws Exception {
		em.getTransaction().begin();

		trainJPA = new TrainAvecResa();
		trainJPA.setNom("Nom");
		trainJPA.setDirectionType("forward");
		trainJPA.setReseau("SNCF");
		trainJPA.setStatut("En marche");
		em.persist(trainJPA);

		perturbationJPA1 = new Perturbation();
		perturbationJPA1.setMotif("covid");
		perturbationJPA1.setDureeEnPlus(10);
		perturbationJPA1.setTrain(trainJPA);
		em.persist(perturbationJPA1);
		listPerturbation.add(perturbationJPA1);

		perturbationJPA2 = new Perturbation();
		perturbationJPA1.setMotif("chevrueil");
		perturbationJPA1.setDureeEnPlus(20);
		perturbationJPA1.setTrain(trainJPA);
		em.persist(perturbationJPA2);
		listPerturbation.add(perturbationJPA2);

		em.getTransaction().commit();
	}

	@AfterEach
	void tearDown() throws Exception {
		em.getTransaction().begin();
		em.remove(perturbationJPA1);
		em.remove(perturbationJPA2);
		em.getTransaction().commit();
	}

	@Test
	void testPerturbationDTOMapper() {
		fr.pantheonsorbonne.ufr27.miage.model.jaxb.Perturbation perturbationJAXBtest = PerturbationMapper
				.perturbationDTOMapper(perturbationJPA1);

		assertEquals(perturbationJAXBtest.getId(), perturbationJPA1.getId());
		assertEquals(perturbationJAXBtest.getMotif(), perturbationJPA1.getMotif());
		assertEquals(perturbationJAXBtest.getDureeEnPlus(), perturbationJPA1.getDureeEnPlus());
		assertEquals(perturbationJAXBtest.getTrain().getId(), perturbationJPA1.getTrain().getId());

	}

	@Test
	void testPerturbationAllDTOMapper() {
		List<fr.pantheonsorbonne.ufr27.miage.model.jaxb.Perturbation> listPerturbationJAXBtest = PerturbationMapper
				.perturbationAllDTOMapper(listPerturbation);
		assertEquals(listPerturbationJAXBtest.size(), listPerturbation.size());
		for (int i = 0; i < listPerturbation.size(); i++) {
			assertEquals(listPerturbationJAXBtest.get(i).getId(), listPerturbation.get(i).getId());
			assertEquals(listPerturbationJAXBtest.get(i).getMotif(), listPerturbation.get(i).getMotif());
			assertEquals(listPerturbationJAXBtest.get(i).getDureeEnPlus(), listPerturbation.get(i).getDureeEnPlus());
			assertEquals(listPerturbationJAXBtest.get(i).getTrain().getId(),
					listPerturbation.get(i).getTrain().getId());
		}
	}

}
