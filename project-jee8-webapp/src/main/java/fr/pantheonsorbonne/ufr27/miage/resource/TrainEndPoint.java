package fr.pantheonsorbonne.ufr27.miage.resource;

import java.net.URI;
import java.net.URISyntaxException;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import fr.pantheonsorbonne.ufr27.miage.model.jaxb.Train;
import fr.pantheonsorbonne.ufr27.miage.service.TrainService;

@Path("/train/")
public class TrainEndPoint {
	
	@Inject
	TrainService service;

	@Produces(value = { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes(value = { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@POST
	public Response createTrain(Train train) throws URISyntaxException {
		int trainId = service.createTrain(train);

		return Response.created(new URI("/train/" + trainId)).build();

	}
	
	
}
