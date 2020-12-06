package fr.pantheonsorbonne.ufr27.miage.resource;

import java.net.URI;
import java.net.URISyntaxException;
import javax.inject.Inject;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import fr.pantheonsorbonne.ufr27.miage.exception.NoSuchTrainException;
import fr.pantheonsorbonne.ufr27.miage.model.jaxb.Train;
import fr.pantheonsorbonne.ufr27.miage.service.TrainService;

@Path("/train/")
public class TrainEndPoint {

	@Inject
	TrainService service;

	@POST
	@Consumes(value = { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response createTrain(Train train) throws URISyntaxException {
		int trainId = service.createTrain(train);

		return Response.created(new URI("/train/" + trainId)).build();

	}

	@GET
	@Path("{trainId}")
	@Produces(value = { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response getTrain(@PathParam("trainId") int trainId) {
		try {
			Train train = service.getTrainFromId(trainId);
			return Response.ok(train).build();
		} catch (NoSuchTrainException e) {
			throw new WebApplicationException(404);
		}

	}

	@DELETE
	@Path("delete/{trainId}")
	@Consumes(value = { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response delete(@PathParam("trainId") int trainId) throws URISyntaxException {
		try {
			service.deleteTrain(trainId);
			return Response.noContent().build();
		} catch (NoSuchTrainException e) {
			throw new WebApplicationException(404);
		}
	}

	@PUT
	@Path("update/{trainId}")
	@Consumes(value = { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response update(Train train) throws URISyntaxException {
		try {
			service.updateTrain(train);
			return Response.noContent().build();
		} catch (NoSuchTrainException e) {
			throw new WebApplicationException(404);
		}
	}

	@PUT
	@Path("{trainId}/addArret/{arretId}")
	@Consumes(value = { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response addArret(@PathParam("trainId") int trainId, @PathParam("arretId") int arretId)
			throws URISyntaxException {
		try {
			service.addArret(trainId, arretId);
			return Response.noContent().build();
		} catch (NoSuchTrainException e) {
			throw new WebApplicationException(404);
		}

	}

	@GET
	@Path("all")
	@Produces(value = { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response getAllTrain() {
		try {
			return Response.ok(service.getAllTrain()).build();
		} catch (NoSuchTrainException e) {
			throw new WebApplicationException(404);
		}

	}
}
