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
import fr.pantheonsorbonne.ufr27.miage.exception.CantUpdateException;
import fr.pantheonsorbonne.ufr27.miage.exception.EmptyListException;
import fr.pantheonsorbonne.ufr27.miage.exception.NoSuchPassagerException;
import fr.pantheonsorbonne.ufr27.miage.exception.NoSuchTrainException;
import fr.pantheonsorbonne.ufr27.miage.model.jaxb.Passager;
import fr.pantheonsorbonne.ufr27.miage.service.PassagerService;

@Path("/passager/")
public class PassagerEndPoint {

	@Inject
	PassagerService service;

	@POST
	@Consumes(value = { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response createPassager(Passager passager) throws URISyntaxException {
		try {
			int passagerId = service.createPassager(passager);
			return Response.created(new URI("/passager/" + passagerId)).build();
		} catch (CantCreateException e) {
			throw new WebApplicationException("Can\'t create passager", 404);
		}

	}

	@GET
	@Path("{passagerId}")
	@Produces(value = { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON, MediaType.TEXT_HTML })
	public Response getPassager(@PathParam("passagerId") int passagerId) {
		try {
			return Response.ok(service.getPassagerFromId(passagerId)).build();
		} catch (NoSuchPassagerException e) {
			throw new WebApplicationException("No such passager", 404);
		}

	}

	/*
	 * TODO Revoir la suppresion Pour supprimer un passager il faut vérifier qu'il
	 * ne soit pas inclu dans un train, si oui le retirer de la liste
	 */
	@DELETE
	@Path("delete/{passagerId}")
	public Response delete(@PathParam("passagerId") int passagerId) throws URISyntaxException {
		try {
			service.deletePassager(passagerId);
			return Response.status(200, "passager deleted").build();
		} catch (NoSuchPassagerException e) {
			throw new WebApplicationException("No such passager", 404);
		}
	}

	/*
	 * TODO Agrémenter l'update d'un passager
	 * 
	 */
	@PUT
	@Path("update/{passagerId}")
	@Consumes(value = { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response update(Passager passager) throws URISyntaxException {
		try {
			service.updatePassager(passager);
			return Response.status(200, "passager updated").build();
		} catch (NoSuchPassagerException e) {
			throw new WebApplicationException("No such passager", 404);
		} catch (CantUpdateException e) {
			throw new WebApplicationException("Can\'t update passager", 400);
		}
	}

	@GET
	@Path("all")
	@Produces(value = { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response getAllPassager() {
		try {
			return Response.ok(service.getAllPassager()).build();
		} catch (EmptyListException e) {
			throw new WebApplicationException("No passager yet", 404);
		}
	}

	@GET
	@Path("/byTrain/{trainId}")
	@Produces(value = { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response getAllArretByTrain(@PathParam("trainId") int trainId) {
		try {
			return Response.ok(service.getAllPassagerByTrain(trainId)).build();
		} catch (NoSuchTrainException e) {
			throw new WebApplicationException("No such train", 404);
		}
	}

	@GET
	@Path("/initAllTrajet")
	public Response initAllTrajet() {
		service.iniTrajetForAllPassager();
		return Response.status(200, "all trajet initiated").build();
	}

}
