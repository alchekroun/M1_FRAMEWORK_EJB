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
import fr.pantheonsorbonne.ufr27.miage.exception.CantDeleteException;
import fr.pantheonsorbonne.ufr27.miage.exception.CantUpdateException;
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
			throw new WebApplicationException("Can\'t create train", 404);
		}
	}

	@GET
	@Path("{arretId}")
	@Produces(value = { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response getArret(@PathParam("arretId") int arretId) {
		try {
			return Response.ok(service.getArretFromId(arretId)).build();
		} catch (NoSuchArretException e) {
			throw new WebApplicationException("No such arret", 404);
		}
	}

	/*
	 * TODO Revoir la suppresion Pour supprimer un arret il faut vérifier qu'il ne
	 * soit pas rattaché à un infogare, si oui supprimer l'infoGare avec. Il faut
	 * vérifier qu'il ne soit pas une liste HeureDePassage d'un train ! Si oui le
	 * supprimer de la liste
	 */
	@DELETE
	@Path("delete/{arretId}")
	public Response delete(@PathParam("arretId") int arretId) throws URISyntaxException {
		try {
			service.deleteArret(arretId);
			return Response.status(200, "arret deleted").build();
		} catch (NoSuchArretException e) {
			throw new WebApplicationException("No such arret", 404);
		} catch (CantDeleteException e) {
			throw new WebApplicationException(e.getMessage(), 400);
		}
	}

	@PUT
	@Path("update/{arretId}")
	@Consumes(value = { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response update(Arret arret) throws URISyntaxException {
		try {
			service.updateArret(arret);
			return Response.status(200, "arret updated").build();
		} catch (NoSuchArretException e) {
			throw new WebApplicationException("No such arret", 404);
		} catch (CantUpdateException e) {
			throw new WebApplicationException("Can\'t udpate arret", 400);
		}
	}

	@GET
	@Path("all")
	@Produces(value = { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response getAllArret() {
		try {
			return Response.ok(service.getAllArret()).build();
		} catch (EmptyListException e) {
			throw new WebApplicationException("No arret yet", 404);
		}

	}

	@GET
	@Path("/byTrain/{trainId}")
	@Produces(value = { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response getAllArretByTrain(@PathParam("trainId") int trainId) {
		try {
			return Response.ok(service.getAllArretByTrain(trainId)).build();
		} catch (NoSuchTrainException e) {
			throw new WebApplicationException("No such train", 404);
		}
	}

}
