package fr.pantheonsorbonne.ufr27.miage.resource;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import fr.pantheonsorbonne.ufr27.miage.exception.NoSuchTrainException;
import fr.pantheonsorbonne.ufr27.miage.model.jaxb.Train;
import fr.pantheonsorbonne.ufr27.miage.model.jaxb.TrainWrapper;
import fr.pantheonsorbonne.ufr27.miage.service.InfoCentreService;

@Path("/infoCentre/")
public class InfoCentreEndPoint {

	@Inject
	InfoCentreService service;

	@POST
	@Path("/sendInfo")
	@Consumes(value = { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response sendInfo(Train train) {
		try {
			service.sendInfo(train);
			return Response.status(200, "infomartion sended").build();
		} catch (NoSuchTrainException e) {
			throw new WebApplicationException("This train is not registered", 404);
		}
	}

	@POST
	@Path("/periodicBulletin")
	@Consumes(value = { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response sendPeriodicBulletin(TrainWrapper trains) {
		// try {
		service.periodicBulletin(trains.getTrains());
		return Response.status(200, "bulletin sended").build();
		// } catch() {
		// throw new WebApplicationException(404);
		// }
	}

	@POST
	@Path("/nhe")
	public Response launchPeriodicBulletin() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				while (true) {
					service.periodicBulletin();
					try {
						Thread.sleep(60000); // 1mn
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
