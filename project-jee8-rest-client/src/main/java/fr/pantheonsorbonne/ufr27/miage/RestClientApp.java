package fr.pantheonsorbonne.ufr27.miage;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status.Family;

import fr.pantheonsorbonne.ufr27.miage.model.jaxb.Arret;
import fr.pantheonsorbonne.ufr27.miage.model.jaxb.InfoGare;
import fr.pantheonsorbonne.ufr27.miage.model.jaxb.ObjectFactory;
import fr.pantheonsorbonne.ufr27.miage.model.jaxb.Train;

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
		System.out.println("#\t#Creation Arret#\t#");
		Arret arret1Paris = factory.createArret();
		arret1Paris.setId(1);
		arret1Paris.setNom("Paris");

		Response responseCreationArret1Paris = target.path("arret").request().accept(MediaType.APPLICATION_JSON)
				.post(Entity.json(arret1Paris));

		URI arret1ParisLocation = null;
		if (responseCreationArret1Paris.getStatusInfo().getFamily().equals(Family.SUCCESSFUL)) {
			System.out.println("Arret Created Successfully");
			arret1ParisLocation = responseCreationArret1Paris.getLocation();
		} else {
			throw new RuntimeException(
					"failed to create Arret : " + responseCreationArret1Paris.getStatusInfo().toString());
		}

		// Creation infoGare associé
		System.out.println("#\t#Creation InfoGare#\t#");
		InfoGare infoGare1Paris = factory.createInfoGare();
		infoGare1Paris.setLocalisationArretId(arret1Paris.getId());

		Response responseCreationInfoGare1Paris = target.path("infoGare").request().accept(MediaType.APPLICATION_JSON)
				.post(Entity.json(infoGare1Paris));

		URI infoGare1ParisLocation = null;
		if (responseCreationInfoGare1Paris.getStatusInfo().getFamily().equals(Family.SUCCESSFUL)) {
			System.out.println("InfoGare Created Successfully");
			infoGare1ParisLocation = responseCreationInfoGare1Paris.getLocation();
		} else {
			throw new RuntimeException(
					"failed to create infoGare : " + responseCreationInfoGare1Paris.getStatusInfo().toString());
		}

		// Creation train
		System.out.println("#\t#Creation Train#\t#");
		Train train1 = factory.createTrainAvecResa();
		train1.setId(1);
		train1.setNom("Bordeaux - Paris");
		train1.setDirection(arret1Paris);
		train1.setDirectionType("forward");
		train1.setStatut("enmarche");
		train1.setNumeroTrain(8541);
		train1.setReseau("SNCF");
		train1.setStatut("en marche");
		train1.setBaseDepartTemps(LocalDateTime.now().plusMinutes(10));
		train1.setBaseArriveeTemps(LocalDateTime.now().plusMinutes(30));
		train1.setReelDepartTemps(LocalDateTime.now().plusMinutes(10));
		train1.setReelArriveeTemps(LocalDateTime.now().plusMinutes(30));

		Response responseCreationTrain1 = target.path("train").request().accept(MediaType.APPLICATION_JSON)
				.post(Entity.json(train1));

		URI train1Location = null;
		if (responseCreationTrain1.getStatusInfo().getFamily().equals(Family.SUCCESSFUL)) {
			System.out.println("\nTrain Created Successfully");
			train1Location = responseCreationTrain1.getLocation();
		} else {
			throw new RuntimeException("failed to create train : " + responseCreationTrain1.getStatusInfo().toString());
		}

		Train train = client.target(train1Location).request().get(Response.class).readEntity(Train.class);
		DateTimeFormatter formatTransfer = DateTimeFormatter.ofPattern("dd-MM-yyyy-HH-mm-ss");
		Arret arretLille = getArret("Lille", target);
		Response responseAddArret = target.path("train/" + train.getId() + "/addarret/" + arretLille.getId()).request()
				.accept(MediaType.APPLICATION_JSON).put(Entity.json(LocalDateTime.now().plusMinutes(20).toString()));

		if (responseAddArret.getStatusInfo().getFamily().equals(Family.SUCCESSFUL)) {
			System.out.println("Arret added successfully");
		} else {
			throw new RuntimeException("failed to add arret : " + responseCreationTrain1.getStatusInfo().toString()
					+ "\n" + "train/" + train.getId() + "/addarret/" + arretLille.getId());
		}

	}
	/*
	 * private static FreeTrialPlan getPlan() { ObjectFactory factory = new
	 * ObjectFactory(); FreeTrialPlan trial = factory.createFreeTrialPlan(); User
	 * user = factory.createUser(); user.setFname("Nicolas");
	 * user.setLname("Herbaut"); user.setMembershipId(1234);
	 * 
	 * Address addresse = factory.createAddress(); addresse.setCountry("France");
	 * addresse.setStreetName("rue de Tolbiac"); addresse.setStreetNumber(90);
	 * addresse.setZipCode("75014");
	 * 
	 * trial.setUser(user); trial.setAddress(addresse); return trial; }
	 * 
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
