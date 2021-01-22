package fr.pantheonsorbonne.ufr27.miage;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status.Family;

import fr.pantheonsorbonne.ufr27.miage.model.jaxb.ObjectFactory;
import fr.pantheonsorbonne.ufr27.miage.model.jaxb.Train;
import fr.pantheonsorbonne.ufr27.miage.model.jaxb.TrainWrapper;

/**
 * Hello world!
 *
 */
public class RestClientApp

{

	public static void main(String[] args) throws InterruptedException {

		Client client = ClientBuilder.newClient();
		WebTarget target = client.target("http://localhost:8080");

		int NB_TRAIN = 2;

		ObjectFactory factory = new ObjectFactory();

		System.out.println("--------------Launch periodic bulletin--------------");

		TrainWrapper trains = new TrainWrapper();

		Response responseSendBulletin = target.path("infoCentre/nhe").request().accept(MediaType.APPLICATION_JSON)
				.post(Entity.json(trains));
		if (responseSendBulletin.getStatusInfo().getFamily().equals(Family.SUCCESSFUL)) {
			System.out.println("--------------Periodic bulletin launched succesfully--------------");
			System.out.println("--------------Lunch first train--------------");

			for (int i = 1; i < NB_TRAIN + 1; i++) {
				Response respTrain = target.path("train/" + i).request().get();
				Train t = respTrain.readEntity(Train.class);
				Response responseLunchFirstTrain = target.path("train/enmarche").request()
						.accept(MediaType.APPLICATION_JSON).post(Entity.json(t));
			}

		} else {
			throw new RuntimeException("failed to send bulletin : " + responseSendBulletin.getStatusInfo().toString());
		}
	}

	/*
	 * private static Arret getArret(String nom, Client client, WebTarget target) {
	 * ObjectFactory factory = new ObjectFactory(); Arret arretLille =
	 * factory.createArret(); arretLille.setNom(nom); Response resp =
	 * target.path("arret").request().accept(MediaType.APPLICATION_JSON).post(Entity
	 * .json(arretLille)); return
	 * client.target(resp.getLocation()).request().get(Response.class).readEntity(
	 * Arret.class); }
	 * 
	 * public static void main(String[] args) throws InterruptedException { Client
	 * client = ClientBuilder.newClient(); WebTarget target =
	 * client.target("http://localhost:8080");
	 * 
	 * ObjectFactory factory = new ObjectFactory();
	 * 
	 * // Creation arret
	 * System.out.println("--------------Creation Arret--------------"); Arret
	 * arret1Paris = factory.createArret(); arret1Paris.setId(1);
	 * arret1Paris.setNom("Paris");
	 * 
	 * Response responseCreationArret1Paris =
	 * target.path("arret").request().accept(MediaType.APPLICATION_JSON)
	 * .post(Entity.json(arret1Paris)); if
	 * (responseCreationArret1Paris.getStatusInfo().getFamily().equals(Family.
	 * SUCCESSFUL)) {
	 * System.out.println("--------------Arret Created Successfully--------------");
	 * 
	 * // Creation train
	 * System.out.println("--------------Creation Train--------------"); Train
	 * train1 = factory.createTrainAvecResa(); train1.setId(1);
	 * train1.setNom("Paris - Lille"); train1.setDirectionType("forward");
	 * train1.setNumeroTrain(8541); train1.setReseau("SNCF");
	 * train1.setStatut("en marche");
	 * 
	 * Response responseCreationTrain1 =
	 * target.path("train").request().accept(MediaType.APPLICATION_JSON)
	 * .post(Entity.json(train1));
	 * 
	 * URI train1Location = null; if
	 * (responseCreationTrain1.getStatusInfo().getFamily().equals(Family.SUCCESSFUL)
	 * ) {
	 * System.out.println("--------------Train Created Successfully--------------");
	 * 
	 * System.out.println("--------------Add arret to train--------------");
	 * 
	 * // URI du train récupuré train1Location =
	 * responseCreationTrain1.getLocation();
	 * 
	 * // On récupère le Train Train train =
	 * client.target(train1Location).request().get(Response.class).readEntity(Train.
	 * class); Arret arretLille = getArret("Lille", client, target);
	 * 
	 * // On ajoute l'arrêt au train sur son chemin
	 * 
	 * String tempsDepartArrive = LocalDateTime.now().plusMinutes(0).toString() +
	 * " " + LocalDateTime.now().plusMinutes(60).toString();
	 * 
	 * Response responseAddArretTerminus = target .path("train/" + train.getId() +
	 * "/addarret/" + arretLille.getId() + "/true/true").request()
	 * .accept(MediaType.APPLICATION_JSON).put(Entity.json(tempsDepartArrive));
	 * 
	 * if (responseAddArretTerminus.getStatusInfo().getFamily().equals(Family.
	 * SUCCESSFUL)) {
	 * 
	 * Arret arretChantilly = getArret("Chantilly", client, target);
	 * 
	 * tempsDepartArrive = LocalDateTime.now().plusMinutes(30).toString() + " " +
	 * LocalDateTime.now().plusMinutes(30).toString();
	 * 
	 * Response responseAddArretNonDesservi = target .path("train/" + train.getId()
	 * + "/addarret/" + arretChantilly.getId() + "/false/false")
	 * .request().accept(MediaType.APPLICATION_JSON).put(Entity.json(
	 * tempsDepartArrive));
	 * 
	 * if (responseAddArretNonDesservi.getStatusInfo().getFamily().equals(Family.
	 * SUCCESSFUL)) {
	 * 
	 * Arret arretArras = getArret("Arras", client, target);
	 * 
	 * tempsDepartArrive = LocalDateTime.now().plusMinutes(45).toString() + " " +
	 * LocalDateTime.now().plusMinutes(40).toString();
	 * 
	 * Response responseAddArretDesservi = target .path("train/" + train.getId() +
	 * "/addarret/" + arretArras.getId() + "/true/false")
	 * .request().accept(MediaType.APPLICATION_JSON).put(Entity.json(
	 * tempsDepartArrive));
	 * 
	 * if (responseAddArretDesservi.getStatusInfo().getFamily().equals(Family.
	 * SUCCESSFUL)) {
	 * 
	 * System.out.println("--------------Arret added successfully--------------");
	 * 
	 * TrainWrapper trains = new TrainWrapper(); trains.getTrains().add(train);
	 * 
	 * System.out.println("--------------Launch periodic bulletin--------------");
	 * 
	 * Response responseSendBulletin = target.path("infoCentre/nhe").request()
	 * .accept(MediaType.APPLICATION_JSON).post(Entity.json(trains)); if
	 * (responseSendBulletin.getStatusInfo().getFamily().equals(Family.SUCCESSFUL))
	 * { System.out
	 * .println("--------------Periodic bulletin launched succesfully--------------"
	 * ); } else { throw new RuntimeException( "failed to send bulletin : " +
	 * responseSendBulletin.getStatusInfo().toString()); } } else { throw new
	 * RuntimeException( "failed to add arret : " +
	 * responseCreationTrain1.getStatusInfo().toString());
	 * 
	 * } } else { throw new RuntimeException( "failed to add arret : " +
	 * responseCreationTrain1.getStatusInfo().toString());
	 * 
	 * }
	 * 
	 * } else { throw new RuntimeException( "failed to add arret : " +
	 * responseCreationTrain1.getStatusInfo().toString()); } } else { throw new
	 * RuntimeException( "failed to create train : " +
	 * responseCreationTrain1.getStatusInfo().toString()); }
	 * 
	 * } else { throw new RuntimeException( "failed to create Arret : " +
	 * responseCreationArret1Paris.getStatusInfo().toString()); }
	 * 
	 * }
	 */

}
