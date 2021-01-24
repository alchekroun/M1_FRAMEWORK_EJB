package fr.pantheonsorbonne.ufr27.miage.resource;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import fr.pantheonsorbonne.ufr27.miage.service.InfoCentreService;

@Path("/infoCentre/")
public class InfoCentreEndPoint {

	@Inject
	InfoCentreService service;

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
