package fr.pantheonsorbonne.ufr27.miage;

import java.net.URI;
import java.time.LocalDateTime;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status.Family;

import fr.pantheonsorbonne.ufr27.miage.model.jaxb.Arret;
import fr.pantheonsorbonne.ufr27.miage.model.jaxb.ObjectFactory;
import fr.pantheonsorbonne.ufr27.miage.model.jaxb.Train;
import fr.pantheonsorbonne.ufr27.miage.model.jaxb.TrainWrapper;

/**
 * Hello world!
 *
 */
public class RestClientApp

{

	private static Arret getArret(String nom, WebTarget target) {
		ObjectFactory factory = new ObjectFactory();
		Arret arretLille = factory.createArret();
		arretLille.setId(2);
		arretLille.setNom(nom);
		target.path("arret").request().accept(MediaType.APPLICATION_JSON).post(Entity.json(arretLille));
		return arretLille;
	}

	public static void main(String[] args) throws InterruptedException {
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target("http://localhost:8080");

		ObjectFactory factory = new ObjectFactory();

		// Creation arret
		System.out.println("--------------Creation Arret--------------");
		Arret arret1Paris = factory.createArret();
		arret1Paris.setId(1);
		arret1Paris.setNom("Paris");

		Response responseCreationArret1Paris = target.path("arret").request().accept(MediaType.APPLICATION_JSON)
				.post(Entity.json(arret1Paris));
		if (responseCreationArret1Paris.getStatusInfo().getFamily().equals(Family.SUCCESSFUL)) {
			System.out.println("--------------Arret Created Successfully--------------");

			// Creation train
			System.out.println("--------------Creation Train--------------");
			Train train1 = factory.createTrainAvecResa();
			train1.setId(1);
			train1.setNom("Paris - Lille");
			train1.setDirectionType("forward");
			train1.setStatut("enmarche");
			train1.setNumeroTrain(8541);
			train1.setReseau("SNCF");
			train1.setStatut("en marche");

			Response responseCreationTrain1 = target.path("train").request().accept(MediaType.APPLICATION_JSON)
					.post(Entity.json(train1));

			URI train1Location = null;
			if (responseCreationTrain1.getStatusInfo().getFamily().equals(Family.SUCCESSFUL)) {
				System.out.println("--------------Train Created Successfully--------------");

				System.out.println("--------------Add arret to train--------------");

				// URI du train récupuré
				train1Location = responseCreationTrain1.getLocation();

				// On récupère le Train
				Train train = client.target(train1Location).request().get(Response.class).readEntity(Train.class);
				Arret arretLille = getArret("Lille", target);

				// On ajoute l'arrêt au train sur son chemin

				String tempsDepartArrive = LocalDateTime.now().plusMinutes(10).toString() + " "
						+ LocalDateTime.now().plusMinutes(30).toString();

				Response responseAddArret = target
						.path("train/" + train.getId() + "/addarret/" + arretLille.getId() + "/true").request()
						.accept(MediaType.APPLICATION_JSON).put(Entity.json(tempsDepartArrive));

				if (responseAddArret.getStatusInfo().getFamily().equals(Family.SUCCESSFUL)) {
					System.out.println("--------------Arret added successfully--------------");

					TrainWrapper trains = new TrainWrapper();
					trains.getTrains().add(train);

					System.out.println("--------------Launch periodic bulletin--------------");

					Response responseSendBulletin = target.path("infoCentre/nhe").request()
							.accept(MediaType.APPLICATION_JSON).post(Entity.json(trains));
					if (responseSendBulletin.getStatusInfo().getFamily().equals(Family.SUCCESSFUL)) {
						System.out.println("--------------Periodic bulletin launched succesfully--------------");
					} else {
						throw new RuntimeException(
								"failed to send bulletin : " + responseSendBulletin.getStatusInfo().toString());
					}

				} else {
					throw new RuntimeException(
							"failed to add arret : " + responseCreationTrain1.getStatusInfo().toString() + "\nURI :\t"
									+ "train/" + train.getId() + "/addarret/" + arretLille.getId());
				}
			} else {
				throw new RuntimeException(
						"failed to create train : " + responseCreationTrain1.getStatusInfo().toString());
			}

		} else {
			throw new RuntimeException(
					"failed to create Arret : " + responseCreationArret1Paris.getStatusInfo().toString());
		}

	}

	/*
	 * public static void main(String[] args) throws InterruptedException { Client
	 * client = ClientBuilder.newClient(); WebTarget target =
	 * client.target("http://localhost:8080");
	 * 
	 * FreeTrialPlan plan = getPlan();
	 * 
	 * Response resp =
	 * target.path("membership").request().accept(MediaType.APPLICATION_JSON)
	 * .post(Entity.json(getPlan()));
	 * 
	 * URI userLocation = null;
	 * 
	 * System.out.println("Creating a membership"); if
	 * (resp.getStatusInfo().getFamily().equals(Family.SUCCESSFUL)) {
	 * System.out.println("Trial Created Successfully");
	 * 
	 * userLocation = resp.getLocation(); } else {
	 * 
	 * throw new RuntimeException("failed to create membership" +
	 * resp.getStatusInfo().toString()); }
	 * 
	 * if (userLocation != null) { System.out.println("changing the address");
	 * Response userResponse =
	 * client.target(userLocation).request().get(Response.class); if
	 * (userResponse.getStatusInfo().getFamily().equals(Family.SUCCESSFUL)) { if
	 * (userResponse.hasEntity()) {
	 * 
	 * User user = userResponse.readEntity(User.class);
	 * 
	 * plan.getAddress().setZipCode("75013"); Response addressUpdateResponse =
	 * client.target(userLocation).path("address").request()
	 * .put(Entity.json(plan.getAddress()));
	 * 
	 * if
	 * (addressUpdateResponse.getStatusInfo().getFamily().equals(Family.SUCCESSFUL))
	 * { System.out.
	 * println("Address Sucessfully updated checking is user has been updated");
	 * Response respGetAddress =
	 * client.target(userLocation).path("address").request() .get(Response.class);
	 * 
	 * if (respGetAddress.getStatusInfo().getFamily().equals(Family.SUCCESSFUL)) {
	 * Address address = respGetAddress.readEntity(Address.class); if
	 * (!address.getZipCode().equals("75013")) { throw new
	 * RuntimeException("address was't updated"); } else {
	 * System.out.println("address updated successfully");
	 * 
	 * Ccinfo ccinfo = new Ccinfo(); ccinfo.setCcv(123);
	 * ccinfo.setNumber("1212126126216"); ccinfo.setValidityDate("10/22"); Response
	 * paymentResponse = target.path("payment").path("" + user.getId()).request()
	 * .post(Entity.xml(ccinfo)); if
	 * (!paymentResponse.getStatusInfo().getFamily().equals(Family.SUCCESSFUL)) {
	 * throw new RuntimeException("failed to pay"); }
	 * System.out.println("Payment sent");
	 * 
	 * allpaid: while (true) { boolean allPaid = true; Response invoicesResp =
	 * target.path("invoice").path("" + user.getId()).request() .get();
	 * 
	 * if (invoicesResp.getStatusInfo().getFamily().equals(Family.SUCCESSFUL)) {
	 * InvoiceWrapper invoices = invoicesResp.readEntity(InvoiceWrapper.class); for
	 * (Invoice invoice : invoices.getInvoices()) { if (!invoice.isPaid()) { allPaid
	 * = false; break; } System.out.println("contract " + invoice.getContractId() +
	 * " from " + invoice.getDate().toString() + " " + invoice.isPaid());
	 * 
	 * }
	 * 
	 * } else { throw new RuntimeException("failed to load invoices"); }
	 * 
	 * if (!allPaid) { System.out.println("all invoices are not paid"); } else {
	 * System.out.println("all invoices are paid"); break allpaid; }
	 * Thread.sleep(1000); }
	 * 
	 * } } else { throw new RuntimeException("failed to fetch updated address"); }
	 * 
	 * } else {
	 * 
	 * throw new RuntimeException("failed to update address" +
	 * addressUpdateResponse.toString()); }
	 * 
	 * }
	 * 
	 * } else { throw new RuntimeException("failed to get user after creation");
	 * 
	 * } } else {
	 * 
	 * throw new RuntimeException("failed to create user" +
	 * resp.getStatusInfo().toString()); }
	 * 
	 * }
	 */

}
