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
import fr.pantheonsorbonne.ufr27.miage.exception.NoSuchArretException;
import fr.pantheonsorbonne.ufr27.miage.exception.NoSuchHdpException;
import fr.pantheonsorbonne.ufr27.miage.exception.NoSuchTrainException;
import fr.pantheonsorbonne.ufr27.miage.model.jaxb.Perturbation;
import fr.pantheonsorbonne.ufr27.miage.model.jaxb.Train;
import fr.pantheonsorbonne.ufr27.miage.service.TrainService;

@Path("/train/")
public class TrainEndPoint {

	@Inject
	TrainService service;

	@POST
	@Consumes(value = { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response createTrain(Train train) throws URISyntaxException {
		try {
			int trainId = service.createTrain(train);
			return Response.created(new URI("/train/" + trainId)).build();
		} catch (CantCreateException e) {
			throw new WebApplicationException("Can\'t create train", 400);
		}

	}

	@GET
	@Path("{trainId}")
	@Produces(value = { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response getTrain(@PathParam("trainId") int trainId) {
		try {
			Train train = service.getTrainFromId(trainId);
			return Response.ok(train).build();
		} catch (NoSuchTrainException e) {
			throw new WebApplicationException("No such train", 404);
		}

	}

	/*
	 * TODO Revoir la suppresion : Pour supprimer un train il faut vérifier qu'il ne
	 * soit pas inclu dans les listes suivantes Arret.trainsArrivants et
	 * Arret.listeHeureDePassage
	 * 
	 * De plus s'il possède des passagers il faut les supprimer avec.
	 */
	@DELETE
	@Path("delete/{trainId}")
	public Response delete(@PathParam("trainId") int trainId) throws URISyntaxException {
		try {
			service.deleteTrain(trainId);
			return Response.status(200, "train deleted").build();
		} catch (NoSuchTrainException e) {
			throw new WebApplicationException("No such train", 404);
		}
	}

	@PUT
	@Path("update/{trainId}")
	@Consumes(value = { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response update(Train train) throws URISyntaxException {
		try {
			service.updateTrain(train);
			return Response.status(200, "train updated").build();
		} catch (NoSuchTrainException e) {
			throw new WebApplicationException("No such train", 404);
		} catch (CantUpdateException e) {
			throw new WebApplicationException("Can\'t update train", 400);
		}
	}

	@PUT
	@Path("{trainId}/addarret/{arretId}/{desservi}/{terminus}")
	@Consumes(value = { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response addArret(@PathParam("trainId") int trainId, @PathParam("arretId") int arretId,
			@PathParam("terminus") String desservi, @PathParam("terminus") String terminus, String passage)
			throws URISyntaxException {
		try {
			service.addArret(trainId, arretId, passage, Boolean.parseBoolean(desservi), Boolean.parseBoolean(terminus));
			return Response.status(200, "arret added to train").build();
		} catch (NoSuchTrainException e) {
			throw new WebApplicationException("No such train", 404);
		} catch (NoSuchArretException e) {
			throw new WebApplicationException("No such arret", 404);
		}

	}

	@DELETE
	@Path("{trainId}/removearret/{arretId}")
	public Response removeArret(@PathParam("trainId") int trainId, @PathParam("arretId") int arretId)
			throws URISyntaxException {
		try {
			service.removeArret(trainId, arretId);
			return Response.status(200, "arret removed from train").build();
		} catch (NoSuchTrainException e) {
			throw new WebApplicationException("No such train", 404);
		} catch (NoSuchArretException e) {
			throw new WebApplicationException("No such arret", 404);
		}
	}

	@GET
	@Path("all")
	@Produces(value = { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response getAllTrain() {
		try {
			return Response.ok(service.getAllTrain()).build();
		} catch (EmptyListException e) {
			throw new WebApplicationException("No train yet", 404);
		}

	}

	@PUT
	@Path("/changeParameterDesservi/{traindId}/{arretId}/{newDesservi}")
	public Response changeParamaterDesservi(@PathParam("trainId") int trainId, @PathParam("arretId") int arretId,
			@PathParam("newDesservi") String newDesservi) {
		try {
			service.changeParameterDesservi(trainId, arretId, Boolean.parseBoolean(newDesservi));
			return Response.status(200, "desservi parameter changed").build();
		} catch (NoSuchTrainException e) {
			throw new WebApplicationException("No such train", 404);
		} catch (NoSuchArretException e) {
			throw new WebApplicationException("No such arret", 404);
		} catch (NoSuchHdpException e) {
			throw new WebApplicationException("There is no link between train & arret", 404);
		}
	}

	@POST
	@Path("/createPerturbation")
	@Consumes(value = { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response createPerturbation(Perturbation perturbation) {
		try {
			service.createPerturbation(perturbation);
			return Response.status(200, "arret removed from train").build();
		} catch (NoSuchTrainException e) {
			throw new WebApplicationException("No such train", 404);
		}
	}

	@POST
	@Path("/enmarche")
	@Consumes(value = { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response enMarche(Train train) {

		// TODO Voir comment on peut exploiter les erreurs qui remontent d'un thread

		new Thread(new Runnable() {

			@Override
			public void run() {
				int statut = 0;

				while (statut != -1) {
					try {
						statut = service.enMarche(train);
						Thread.sleep(60000); // 1mn
					} catch (NoSuchTrainException e) {
						break;
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}).start();

		return Response.accepted().build();
	}

}
