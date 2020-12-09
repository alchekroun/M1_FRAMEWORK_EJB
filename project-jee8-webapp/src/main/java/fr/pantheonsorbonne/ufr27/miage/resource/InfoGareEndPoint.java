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
import fr.pantheonsorbonne.ufr27.miage.exception.NoSuchInfoGareException;
import fr.pantheonsorbonne.ufr27.miage.model.jaxb.InfoGare;
import fr.pantheonsorbonne.ufr27.miage.service.InfoGareService;

@Path("/infoGare/")
public class InfoGareEndPoint {

	@Inject
	InfoGareService service;

	@POST
	@Consumes(value = { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response createInfoGare(InfoGare infoGare) throws URISyntaxException {
		try {
			int infoGareId = service.createInfoGare(infoGare);

			return Response.created(new URI("/infoGare/" + infoGareId)).build();
		} catch (CantCreateException e) {
			throw new WebApplicationException(404);
		}

	}

	@GET
	@Path("{infoGareId}")
	@Produces(value = { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response getInfoGare(@PathParam("infoGareId") int infoGareId) {
		try {
			return Response.ok(service.getInfoGareFromId(infoGareId)).build();
		} catch (NoSuchInfoGareException e) {
			throw new WebApplicationException(404);
		}
	}

	@DELETE
	@Path("delete/{infoGareId}")
	@Consumes(value = { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response delete(@PathParam("infoGareId") int infoGareId) throws URISyntaxException {
		try {
			service.deleteInfoGare(infoGareId);
			return Response.ok().build();
		} catch (NoSuchInfoGareException e) {
			throw new WebApplicationException(404);
		}
	}

	@PUT
	@Path("update/{infoGareId}")
	@Consumes(value = { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response update() throws URISyntaxException {
		return Response.created(new URI("/infoGare/")).build();
	}

	@GET
	@Path("all")
	@Produces(value = { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response getAllInfoGare() {
		try {
			return Response.ok(service.getAllInfoGare()).build();
		} catch (EmptyListException e) {
			throw new WebApplicationException(404);
		}

	}

}
