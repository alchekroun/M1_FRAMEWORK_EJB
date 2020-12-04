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

import fr.pantheonsorbonne.ufr27.miage.dao.ArretDAO;
import fr.pantheonsorbonne.ufr27.miage.model.jaxb.Arret;
import fr.pantheonsorbonne.ufr27.miage.service.ArretService;

@Path("/arret/")
public class ArretEndPoint {

	@Inject
	ArretService service;

	@Inject
	ArretDAO dao;

	@Consumes(value = { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@POST
	public Response createArret(Arret arret) throws URISyntaxException {
		int arretId = service.createArret(arret);

		return Response.created(new URI("/arret/" + arretId)).build();

	}

	@Produces(value = { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@GET
	@Path("{arretId}")
	public Response getArret(@PathParam("arretId") int arretId) {

		Arret arretDTO = dao.getArretFromId(arretId);

		return Response.ok(arretDTO).build();

	}

	@Produces(value = { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@GET
	@Path("all")
	public Response getAllArret() {
		List<Arret> allArretDTO = new ArrayList<>();
		for (Arret t : dao.getAllArret()) {
			allArretDTO.add(t);
		}

		return Response.ok(allArretDTO).build();

	}

	@Consumes(value = { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@DELETE
	@Path("delete/{arretId}")
	public Response delete(@PathParam("arretId") int arretId) throws URISyntaxException {
		dao.deleteArret(arretId);
		return Response.created(new URI("/arret/")).build();
	}

	@Consumes(value = { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@PUT
	@Path("update/{arretId}")
	public Response update() throws URISyntaxException {
		return Response.created(new URI("/arret/")).build();
	}

}
