package fr.pantheonsorbonne.ufr27.miage.ressource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.URISyntaxException;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.weld.junit5.EnableWeld;
import org.jboss.weld.junit5.WeldInitiator;
import org.jboss.weld.junit5.WeldSetup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.pantheonsorbonne.ufr27.miage.exception.NoSuchArretException;
import fr.pantheonsorbonne.ufr27.miage.jpa.Arret;
import fr.pantheonsorbonne.ufr27.miage.model.jaxb.ObjectFactory;
import fr.pantheonsorbonne.ufr27.miage.resource.ArretEndPoint;
import fr.pantheonsorbonne.ufr27.miage.resource.InfoGareEndPoint;
import fr.pantheonsorbonne.ufr27.miage.resource.PassagerEndPoint;
import fr.pantheonsorbonne.ufr27.miage.resource.TrainEndPoint;
import fr.pantheonsorbonne.ufr27.miage.service.ArretService;
import fr.pantheonsorbonne.ufr27.miage.tests.utils.TestPersistenceProducer;

@EnableWeld
public class TestArretEndPoint {
	@WeldSetup
	private WeldInitiator weld = WeldInitiator.from(ArretEndPoint.class, TestPersistenceProducer.class,
			InfoGareEndPoint.class, TrainEndPoint.class, PassagerEndPoint.class).activate(RequestScoped.class).build();

	@Inject
	EntityManager em;
	
	@Inject
	ArretEndPoint arretRes;
	
	ArretService service;
	
	Arret arret1;
	Arret arret2;

	private Response rep;

	@BeforeEach
	public void setup() {
		arret2 = new Arret();
		arret2.setId(1);
		
	}

	
	@Test
	public void testDeleteArret() throws NoSuchArretException, URISyntaxException {
		/*Client client = ClientBuilder.newClient();
		WebTarget target = client.target("http://localhost:8080");

		Arret arret1 = new Arret();
		arret1.setId(1);

		Response responseCreationArret1 = target.path("arret/arretId").request().accept(MediaType.APPLICATION_JSON)
				.post(Entity.json(arret1));
		responseCreationArret1 = Response.ok(service.getArretFromId(arret1.getId())).build();
		
		assertEquals(responseCreationArret1, endPoint.getArret(arret2.getId()));*/
		
		//Response arretRep = arretRes.delete(1);
		
		
		
		/*Client client = ClientBuilder.newClient();
		WebTarget target = client.target("http://localhost:8080");
		
		Response arretRep = target.path("arret/1").request().delete();*/

		arretRes = new ArretEndPoint();
		Response arretRep1 = arretRes.delete(1);
		
		assertEquals(arretRep1.getStatus(), 200);
		
		
		
	}
}
