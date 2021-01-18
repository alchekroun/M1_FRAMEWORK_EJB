package fr.pantheonsorbonne.ufr27.miage.resource;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import fr.pantheonsorbonne.ufr27.miage.exception.EmptyListException;
import fr.pantheonsorbonne.ufr27.miage.exception.NoSuchInfoGareException;
import fr.pantheonsorbonne.ufr27.miage.service.InfoGareService;

@Path("/infoGare/")
public class InfoGareEndPoint {

	@Inject
	InfoGareService service;

	@GET
	@Path("{infoGareId}")
	@Produces(value = { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response getInfoGare(@PathParam("infoGareId") int infoGareId) {
		try {
			return Response.ok(service.getInfoGareFromId(infoGareId)).build();
		} catch (NoSuchInfoGareException e) {
			throw new WebApplicationException("No such infoGare", 404);
		}
	}

	@GET
	@Path("all")
	@Produces(value = { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response getAllInfoGare() {
		try {
			return Response.ok(service.getAllInfoGare()).build();
		} catch (EmptyListException e) {
			throw new WebApplicationException("No infoGare yet", 404);
		}

	}

}
