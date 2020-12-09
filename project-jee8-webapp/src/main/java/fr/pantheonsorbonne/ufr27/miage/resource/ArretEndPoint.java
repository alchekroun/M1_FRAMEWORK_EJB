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

import fr.pantheonsorbonne.ufr27.miage.exception.CantCreateException;
import fr.pantheonsorbonne.ufr27.miage.exception.EmptyListException;
import fr.pantheonsorbonne.ufr27.miage.exception.NoSuchArretException;
import fr.pantheonsorbonne.ufr27.miage.exception.NoSuchTrainException;
import fr.pantheonsorbonne.ufr27.miage.model.jaxb.Arret;
import fr.pantheonsorbonne.ufr27.miage.service.ArretService;

@Path("/arret/")
public class ArretEndPoint {

	@Inject
	ArretService service;

	@POST
	@Consumes(value = { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response createArret(Arret arret) throws URISyntaxException {
		try {
			int arretId = service.createArret(arret);

			return Response.created(new URI("/arret/" + arretId)).build();
		} catch (CantCreateException e) {
			throw new WebApplicationException(404);
		}
	}

	@GET
	@Path("{arretId}")
	@Produces(value = { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response getArret(@PathParam("arretId") int arretId) {
		try {
			return Response.ok(service.getArretFromId(arretId)).build();
		} catch (NoSuchArretException e) {
			throw new WebApplicationException(404);
		}
	}

	@DELETE
	@Path("delete/{arretId}")
	@Consumes(value = { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response delete(@PathParam("arretId") int arretId) throws URISyntaxException {
		try {
			service.deleteArret(arretId);
			return Response.ok().build();
		} catch (NoSuchArretException e) {
			throw new WebApplicationException(404);
		}
	}

	@PUT
	@Path("update/{arretId}")
	@Consumes(value = { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response update() throws URISyntaxException {
		return Response.created(new URI("/arret/")).build();
	}

	@GET
	@Path("all")
	@Produces(value = { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response getAllArret() {
		try {
			return Response.ok(service.getAllArret()).build();
		} catch (EmptyListException e) {
			throw new WebApplicationException(404);
		}

	}

	@GET
	@Path("/byTrain/{trainId}")
	@Produces(value = { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response getAllArretByTrain(@PathParam("trainId") int trainId) {
		try {
			return Response.ok(service.getAllArretByTrain(trainId)).build();
		} catch (NoSuchTrainException e) {
			throw new WebApplicationException(404);
		}
	}

}
