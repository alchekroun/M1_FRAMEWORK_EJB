package fr.pantheonsorbonne.ufr27.miage;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import fr.pantheonsorbonne.ufr27.miage.model.jaxb.Arret;
import fr.pantheonsorbonne.ufr27.miage.model.jaxb.ObjectFactory;
import fr.pantheonsorbonne.ufr27.miage.model.jaxb.Train;

/**
 * Hello world!
 *
 */
public class RestClientApp

{

	private static Arret getArret() {
		ObjectFactory factory = new ObjectFactory();
		Arret arretLille = factory.createArret();
		arretLille.setNomArret("Lille");
		return arretLille;
	}

	private static Train getTrain() {
		ObjectFactory factory = new ObjectFactory();
		Train train = factory.createTrain();
		Arret arretParis = factory.createArret();
		arretParis.setNomArret("Paris");
		List<Arret> lA = new ArrayList<Arret>();
		lA.add(arretParis);

		train.setNomTrain("Perigueux-Bordeaux");
		train.setDirection(getArret());
		train.setDirectionType("forward");
		train.setNumeroTrain(8541);
		train.setReseau("SNCF");
		train.setStatut("en marche");
		train.setListeArrets(lA);
		train.setBaseDepartTemps(LocalDateTime.now().plusMinutes(10));
		train.setBaseArriveeTemps(LocalDateTime.now().plusMinutes(30));
		train.setReelDepartTemps(LocalDateTime.now().plusMinutes(10));
		train.setReelArriveeTemps(LocalDateTime.now().plusMinutes(30));

		return train;

	}

	public static void main(String[] args) throws InterruptedException {
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target("http://localhost:8082");

		// Création du train

		Response respTrain = target.path("train").request().accept(MediaType.APPLICATION_JSON)
				.post(Entity.json(getTrain()));

		System.out.println(respTrain + " ****************************");

		System.out.println("Creating a train");

		URI trainLocation = null;
		trainLocation = respTrain.getLocation();

		System.out.println(trainLocation);

		Response response = client.target(trainLocation).request().accept(MediaType.APPLICATION_JSON)
				.get(Response.class);
		Train train = response.readEntity(Train.class);

		System.out.println(train);
		System.out.println(train.toString());

		// Création de l'arret

		Response respArret = target.path("arret").request().accept(MediaType.APPLICATION_JSON)
				.post(Entity.json(getArret()));

		System.out.println(respArret + " ****************************");

		System.out.println("Creating an arret");

		Arret arret = client.target(respArret.getLocation()).request().accept(MediaType.APPLICATION_JSON)
				.get(Response.class).readEntity(Arret.class);

		// Ajout de l'arret dans la liste du train

		Response respAjout = client.target(train.getIdTrain() + "/addArret/" + arret.getIdArret()).request()
				.accept(MediaType.APPLICATION_JSON).put(Entity.json(train));

		System.out.println(respAjout + "******************");

		System.out.println("Adding an arret to train");

		System.out.println(client.target(trainLocation).request().accept(MediaType.APPLICATION_JSON).get(Response.class)
				.readEntity(Train.class).toString());

		/*
		 * Response resp1 = target.path(train.getIdTrain() + "/addArret/" +
		 * getArret().getIdArret()).request()
		 * .accept(MediaType.APPLICATION_JSON).put(Entity.json(train));
		 * 
		 * System.out.println(resp1 + "******************************");
		 * 
		 * URI trainUpdateLocation = resp1.getLocation(); Response response1 =
		 * client.target(trainUpdateLocation).request().accept(MediaType.
		 * APPLICATION_JSON) .get(Response.class); Train trainUpdate =
		 * response1.readEntity(Train.class);
		 * 
		 * System.out.println(trainUpdate.toString());
		 */

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
