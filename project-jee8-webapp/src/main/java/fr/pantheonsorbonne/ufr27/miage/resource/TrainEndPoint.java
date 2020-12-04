package fr.pantheonsorbonne.ufr27.miage.resource;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import fr.pantheonsorbonne.ufr27.miage.dao.TrainDAO;
import fr.pantheonsorbonne.ufr27.miage.model.jaxb.Train;
import fr.pantheonsorbonne.ufr27.miage.service.TrainService;

@Path("/train/")
public class TrainEndPoint {

	@Inject
	TrainService service;

	@Inject
	TrainDAO dao;

	@Consumes(value = { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@POST
	public Response createTrain(Train train) throws URISyntaxException {
		int trainId = service.createTrain(train);

		return Response.created(new URI("/train/" + trainId)).build();

	}

	@Produces(value = { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@GET
	@Path("{trainId}")
	public Response getTrain(@PathParam("trainId") int trainId) {

		Train trainDTO = dao.getTrainFromId(trainId);

		return Response.ok(trainDTO).build();

	}

	@Produces(value = { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@GET
	@Path("all")
	public Response getAllTrain() {
		List<Train> allTrainDTO = new ArrayList<>();
		for (Train t : dao.getAllTrain()) {
			allTrainDTO.add(t);
		}

		return Response.ok(allTrainDTO).build();

	}

	@Consumes(value = { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@DELETE
	@Path("delete/{trainId}")
	public Response delete(@PathParam("trainId") int trainId) throws URISyntaxException {
		dao.deleteTrain(trainId);
		return Response.created(new URI("/train/")).build();
	}

	@Consumes(value = { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@PUT
	@Path("update/{trainId}")
	public Response update() throws URISyntaxException {
		return Response.created(new URI("/train/")).build();
	}

}
