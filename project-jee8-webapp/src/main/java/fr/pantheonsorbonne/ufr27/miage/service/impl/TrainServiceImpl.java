package fr.pantheonsorbonne.ufr27.miage.service.impl;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import fr.pantheonsorbonne.ufr27.miage.dao.ArretDAO;
import fr.pantheonsorbonne.ufr27.miage.dao.HeureDePassageDAO;
import fr.pantheonsorbonne.ufr27.miage.dao.PassagerDAO;
import fr.pantheonsorbonne.ufr27.miage.dao.PerturbationDAO;
import fr.pantheonsorbonne.ufr27.miage.dao.TrainDAO;
import fr.pantheonsorbonne.ufr27.miage.exception.CantCreateException;
import fr.pantheonsorbonne.ufr27.miage.exception.CantUpdateException;
import fr.pantheonsorbonne.ufr27.miage.exception.EmptyListException;
import fr.pantheonsorbonne.ufr27.miage.exception.NoSuchArretException;
import fr.pantheonsorbonne.ufr27.miage.exception.NoSuchHdpException;
import fr.pantheonsorbonne.ufr27.miage.exception.NoSuchTrainException;
import fr.pantheonsorbonne.ufr27.miage.mapper.HeureDePassageMapper;
import fr.pantheonsorbonne.ufr27.miage.mapper.PassagerMapper;
import fr.pantheonsorbonne.ufr27.miage.mapper.TrainMapper;
import fr.pantheonsorbonne.ufr27.miage.model.jaxb.HeureDePassage;
import fr.pantheonsorbonne.ufr27.miage.model.jaxb.Passager;
import fr.pantheonsorbonne.ufr27.miage.model.jaxb.Perturbation;
import fr.pantheonsorbonne.ufr27.miage.model.jaxb.Train;
import fr.pantheonsorbonne.ufr27.miage.model.jaxb.TrainAvecResa;
import fr.pantheonsorbonne.ufr27.miage.service.TrainService;

public class TrainServiceImpl implements TrainService {

	@Inject
	EntityManager em;

	@Inject
	TrainDAO dao;

	@Inject
	ArretDAO arretDAO;

	@Inject
	HeureDePassageDAO hdpDAO;

	@Inject
	PerturbationDAO perturbationDAO;

	@Inject
	PassagerDAO passagerDAO;

	// Create
	@Override
	public int createTrain(Train trainDTO) throws CantCreateException {
		em.getTransaction().begin();
		try {

			fr.pantheonsorbonne.ufr27.miage.jpa.Train train = new fr.pantheonsorbonne.ufr27.miage.jpa.TrainAvecResa();

			train.setNom(trainDTO.getNom());
			train.setDirectionType(trainDTO.getDirectionType());
			train.setNumero(trainDTO.getNumeroTrain());
			train.setReseau(trainDTO.getReseau());
			train.setStatut(trainDTO.getStatut());

			em.persist(train);

			em.getTransaction().commit();

			return train.getId();
		} catch (org.eclipse.persistence.exceptions.DatabaseException e) {
			em.getTransaction().rollback();
			throw new CantCreateException();
		}
	}

	// Read
	@Override
	public Train getTrainFromId(int trainId) throws NoSuchTrainException {
		fr.pantheonsorbonne.ufr27.miage.jpa.Train train = dao.getTrainFromId(trainId);
		if (train == null) {
			throw new NoSuchTrainException();
		}
		return TrainMapper.trainDTOMapper(train);
	}

	// Update
	@Override
	public void updateTrain(Train trainUpdate) throws NoSuchTrainException, CantUpdateException {
		em.getTransaction().begin();
		try {
			fr.pantheonsorbonne.ufr27.miage.jpa.Train trainOriginal = dao.getTrainFromId(trainUpdate.getId());
			if (trainOriginal == null) {
				throw new NoSuchTrainException();
			}

			em.merge(dao.updateTrain(trainOriginal, trainUpdate));
			em.getTransaction().commit();
		} catch (org.eclipse.persistence.exceptions.DatabaseException e) {
			em.getTransaction().rollback();
			throw new CantUpdateException();
		}
	}

	// Delete
	@Override
	public void deleteTrain(int trainId) throws NoSuchTrainException {
		em.getTransaction().begin();
		fr.pantheonsorbonne.ufr27.miage.jpa.Train train = dao.getTrainFromId(trainId);
		if (train == null) {
			throw new NoSuchTrainException();
		}
		/*
		 * List<fr.pantheonsorbonne.ufr27.miage.jpa.Perturbation> listePerturbations =
		 * perturbationDAO.getPerturbationByTrain(train);
		 * for(fr.pantheonsorbonne.ufr27.miage.jpa.Perturbation pertu :
		 * listePerturbations) { perturbationDAO.deletePerturbation(pertu); }
		 */
		dao.deleteTrain(train);

		em.getTransaction().commit();
	}

	@Override
	public List<Train> getAllTrain() throws EmptyListException {
		List<fr.pantheonsorbonne.ufr27.miage.jpa.Train> listeTrains = dao.getAllTrain();
		if (listeTrains == null) {
			throw new EmptyListException();
		}
		return TrainMapper.trainAllDTOMapper(listeTrains);
	}

	@Override
	public void addArret(int trainId, int arretId, String passage, boolean desservi, boolean terminus)
			throws NoSuchTrainException, NoSuchArretException {
		em.getTransaction().begin();

		fr.pantheonsorbonne.ufr27.miage.jpa.Train train = dao.getTrainFromId(trainId);
		if (train == null) {
			em.getTransaction().rollback();
			throw new NoSuchTrainException();
		}
		fr.pantheonsorbonne.ufr27.miage.jpa.Arret arret = arretDAO.getArretFromId(arretId);
		if (arret == null) {
			em.getTransaction().rollback();
			throw new NoSuchArretException();
		}

		String[] horraires = passage.split(" ");

		dao.addArret(train, arret, LocalDateTime.parse(horraires[0]), LocalDateTime.parse(horraires[1]), desservi,
				terminus);

		em.getTransaction().commit();
	}

	@Override
	public void removeArret(int trainId, int arretId) throws NoSuchTrainException, NoSuchArretException {
		em.getTransaction().begin();
		fr.pantheonsorbonne.ufr27.miage.jpa.Train train = dao.getTrainFromId(trainId);
		if (train == null) {
			em.getTransaction().rollback();
			throw new NoSuchTrainException();
		}
		fr.pantheonsorbonne.ufr27.miage.jpa.Arret arret = arretDAO.getArretFromId(arretId);
		if (arret == null) {
			em.getTransaction().rollback();
			throw new NoSuchArretException();
		}

		dao.removeArret(train, arret);

		em.getTransaction().commit();

	}

	@Override
	public void changeParameterDesservi(int trainId, int arretId, boolean newDesservi)
			throws NoSuchTrainException, NoSuchArretException, NoSuchHdpException {
		em.getTransaction().begin();
		fr.pantheonsorbonne.ufr27.miage.jpa.Train train = dao.getTrainFromId(trainId);
		if (train == null) {
			em.getTransaction().rollback();
			throw new NoSuchTrainException();
		}
		fr.pantheonsorbonne.ufr27.miage.jpa.Arret arret = arretDAO.getArretFromId(arretId);
		if (arret == null) {
			em.getTransaction().rollback();
			throw new NoSuchArretException();
		}

		fr.pantheonsorbonne.ufr27.miage.jpa.HeureDePassage hdp = hdpDAO.getHdpFromTrainIdAndArretId(trainId, arretId);
		if (hdp == null) {
			em.getTransaction().rollback();
			throw new NoSuchHdpException();
		}

		hdpDAO.changeParameterDesservi(hdp, newDesservi);

		em.merge(hdp);
		em.merge(train);
		em.merge(arret);
		em.getTransaction().commit();

	}

	@Override
	public void createPerturbation(Perturbation perturbation) throws NoSuchTrainException {
		em.getTransaction().begin();
		fr.pantheonsorbonne.ufr27.miage.jpa.Train train = dao.getTrainFromId(perturbation.getTrain().getId());
		if (train == null) {
			em.getTransaction().rollback();
			throw new NoSuchTrainException();
		}

		perturbationDAO.impacterTrafic(perturbationDAO.createPerturbation(perturbation));

		em.getTransaction().commit();
	}

	@Override
	public void enMarche(Train train) throws NoSuchTrainException, NoSuchArretException {
		fr.pantheonsorbonne.ufr27.miage.jpa.Train trainJPA = dao.getTrainFromId(train.getId());
		if (trainJPA == null) {
			throw new NoSuchTrainException();
		}
		// TODO vérifier qu'il reste des arrêts
		if (hdpDAO.findHdpByTrain(trainJPA.getId()) != null) {
			HeureDePassage hdpActuel = verifIfExistArretNow(trainJPA.getId());
			if (hdpActuel != null) {
				// Fait descendre les gens qui doivent descendre
				descendreListPassager(PassagerMapper.passagerAllDTOMapper(
						passagerDAO.getAllPassagerByArrivee(hdpActuel.getArret().getId())), trainJPA);

				if (!hdpActuel.isTerminus()) {
					// Fait monter les gens qui attendent sur le quai

					// TODO Rajouter une fonction qui vérifie que les gens qui montent dans le train
					// ont bien leur arret qui soit desservi par ce même train

					monterListPassager(PassagerMapper.passagerAllDTOMapper(
							passagerDAO.getAllPassagerByDepart(hdpActuel.getArret().getId())), trainJPA);

				} else {
					// interupt le thread car nous sommes arriver au terminus
				}
			} else {
				// Le train n'a plus d'arrêt à desservir. Il faut interupt le thread.
			}
		} else {
			// Le train n'a aucun arrêt à desservir
			throw new NoSuchArretException();
		}
	}

	@Override
	public void descendreListPassager(List<Passager> listPassager, fr.pantheonsorbonne.ufr27.miage.jpa.Train train) {
		for (Passager p : listPassager) {
			fr.pantheonsorbonne.ufr27.miage.jpa.Passager pJPA = em
					.find(fr.pantheonsorbonne.ufr27.miage.jpa.Passager.class, p.getId());
			if (train.getListePassagers().contains(pJPA)) {
				dao.removePassager(train, pJPA);
			}
		}
	}

	@Override
	public void monterListPassager(List<Passager> listPassager, fr.pantheonsorbonne.ufr27.miage.jpa.Train train) {
		for (Passager p : listPassager) {
			fr.pantheonsorbonne.ufr27.miage.jpa.Passager pJPA = em
					.find(fr.pantheonsorbonne.ufr27.miage.jpa.Passager.class, p.getId());
			if (!train.getListePassagers().contains(pJPA)) {
				dao.addPassager(train, pJPA);
			}
		}
	}

	@Override
	public HeureDePassage verifIfExistArretNow(int trainId) {
		return HeureDePassageMapper.heureDePassageDTOMapper(hdpDAO.getHdpByTrainAndDateNow(trainId));
	}
	
	
	protected boolean trainGetMeWhereIWant(Train train, Passager passager) {
		List<HeureDePassage> listHdp = HeureDePassageMapper
				.heureDePassageAllDTOMapper(hdpDAO.findHdpByTrain(train.getId()));
		for (HeureDePassage hdp : listHdp) {
			if (hdp.getArret().equals(passager.getArrive())) {
				return true;
			}
			if (passager.getCorrespondance() != null) {
				if (hdp.getArret().equals(passager.getCorrespondance())) {
					return true;
				}
			}
		}
		return false;
	}
	
	protected HeureDePassage verifIfNextArretHasTrainEnRetard(HeureDePassage hdpBase) {
		List<HeureDePassage> listHdpAtArretId = HeureDePassageMapper
				.heureDePassageAllDTOMapper(hdpDAO.findHdpByArret(hdpBase.getArret().getId()));

		for (HeureDePassage hdp : listHdpAtArretId) {

			// Seuls les trains qui arrivent après ce train sont prit en compte
			if (hdp.getReelArriveeTemps().isAfter(hdpBase.getReelArriveeTemps())) {

				// On recherche les hdp en retard
				if (hdp.getBaseArriveeTemps().compareTo((hdp.getReelArriveeTemps())) != 0) {

					if (isRetard(hdp)) {
						return hdp;
					}
				}
			}
		}
		return null;
	}
	
	protected boolean isRetard(HeureDePassage hdp) {
		return hdp.getReelArriveeTemps().isAfter(hdp.getBaseArriveeTemps());
	}
	
	protected boolean isWorthRetardTrain(List<Passager> listPassager, HeureDePassage hdpTrain) {
		int cpt=0;
		for(Passager p : listPassager) {
			if(trainGetMeWhereIWant(hdpTrain.getTrain(),p)) {
				cpt++;
			}
		}
		if(cpt>50) {
			return true;
		}
		return false;
	}
	
	
	@Override
	public void retarderCorrespondance (Train train){
		
		List<fr.pantheonsorbonne.ufr27.miage.jpa.HeureDePassage> listHdpTrain = hdpDAO.findHdpByTrainAfterDateAndSorted(train.getId(), LocalDateTime.now());
		List<HeureDePassage> listHdp = HeureDePassageMapper.heureDePassageAllDTOMapper(listHdpTrain);
		
		if(!listHdp.isEmpty()) {			
			
			HeureDePassage hdp = listHdp.get(0);
			
			HeureDePassage hdpTrainEnRetard = verifIfNextArretHasTrainEnRetard(hdp);
			if(train instanceof TrainAvecResa && hdpTrainEnRetard!=null) {
				
				List<Passager> listPassagerTrainPerturbeTakingCorrespondanceAtArret = PassagerMapper.passagerAllDTOMapper(passagerDAO.getPassagerByTrainIdAndNotArrivalAtArretId(hdpTrainEnRetard.getTrain().getId(), hdp.getArret().getId()));
				
				if(isWorthRetardTrain(listPassagerTrainPerturbeTakingCorrespondanceAtArret, hdp)) {
					
					fr.pantheonsorbonne.ufr27.miage.jpa.HeureDePassage hdpTrainARetarder = listHdpTrain.get(0);
					fr.pantheonsorbonne.ufr27.miage.jpa.HeureDePassage hdpTrainEnRetardJpa = hdpDAO.findHdpByTrainIdAndArretIdAndHeureReel(hdpTrainEnRetard.getTrain().getId(),hdpTrainEnRetard.getArret().getId(),hdpTrainEnRetard.getReelArriveeTemps(),hdpTrainEnRetard.getReelDepartTemps());
					hdpDAO.updateHeureDePassage(hdpTrainARetarder.getTrain(),hdpTrainARetarder.getArret(),hdpTrainARetarder.getBaseDepartTemps(), hdpTrainARetarder.getBaseArriveeTemps(), hdpTrainEnRetardJpa.getReelDepartTemps().plusMinutes(10), hdpTrainEnRetardJpa.getReelArriveeTemps().plusMinutes(10), hdpTrainARetarder.isDesservi(), hdpTrainARetarder.isTerminus());
				}
			}
		}
	}
}			
//			
//			//liste des trains arrivant avant le train perturbe à l'arret
//			List<fr.pantheonsorbonne.ufr27.miage.jpa.HeureDePassage> listHdpTrainArrivingBeforeTrainPerturbe = hdpDAO.findHdpByArretAndNotTrainIdAndSorted(hdp.getArret().getId(),hdp.getTrain().getId(),hdp.getReelArriveeTemps());
//			List<Passager> listPassagerTrainPerturbeTakingCorrespondanceAtArret = PassagerMapper.passagerAllDTOMapper(passagerDAO.getPassagerByTrainIdAndNotArrivalAtArretId(hdp.getTrain().getId(), hdp.getArret().getId()));
//			
//			if(!listHdpTrainArrivingBeforeTrainPerturbe.isEmpty()) {
//				
//				List<HeureDePassage> listHdpTrainCheckARetarder = HeureDePassageMapper.heureDePassageAllDTOMapper(listHdpTrainArrivingBeforeTrainPerturbe);
//				
//				if(listHdpTrainCheckARetarder.get(0).getTrain() instanceof TrainAvecResa) {
//					
//					if(isWorthRetardTrain(listPassagerTrainPerturbeTakingCorrespondanceAtArret, listHdpTrainCheckARetarder.get(0))) {
//						
//						fr.pantheonsorbonne.ufr27.miage.jpa.HeureDePassage hdpARetarder = listHdpTrainArrivingBeforeTrainPerturbe.get(0);
//						hdpDAO.updateHeureDePassage(hdpARetarder.getTrain(),hdpARetarder.getArret(),hdpARetarder.getBaseDepartTemps(), hdpARetarder.getBaseArriveeTemps(), hdp.getReelDepartTemps().plusMinutes(10), hdp.getReelArriveeTemps().plusMinutes(10), hdpARetarder.isDesservi(), hdpARetarder.isTerminus());
//					}
//				}
//			}
//		}
//	}	
//}
	
//	@Override
//	public void retarderCorrespondance (Perturbation perturbation) {
//		List<fr.pantheonsorbonne.ufr27.miage.jpa.HeureDePassage> listHdp = hdpDAO.findHdpByTrainAfterDateAndSorted(perturbation.getTrain().getId(), LocalDateTime.now());
//		if(!listHdp.isEmpty()) {
//			fr.pantheonsorbonne.ufr27.miage.jpa.HeureDePassage hdp = listHdp.get(0);
//			//liste qui recup les trains qui ont un depart après l'arrivee du train qui est perturbe (calcul en retirant le temps de perturbation pour avoir le cas original sans perturbation)
//			List<fr.pantheonsorbonne.ufr27.miage.jpa.Train> listTrainCheckIfHaveToDelay= dao.findTrainByArretAndDepartAfterDate(hdp.getArret().getId(),hdp.getReelArriveeTemps().minusMinutes(perturbation.getDureeEnPlus()));
//			//liste qui recup les trains qui ont un depart après l'arrivee du train qui est perturbe (cette fois on compte le temps de perturbation)
//			List<fr.pantheonsorbonne.ufr27.miage.jpa.Train> listTrainCheckIfHaveToDelayAfterAddingPerturbation= dao.findTrainByArretAndDepartAfterDate(hdp.getArret().getId(),hdp.getReelArriveeTemps());
//			List<fr.pantheonsorbonne.ufr27.miage.jpa.Train> listTrainARetarder = null;
//			//les trains de la premiere liste et qui ne sont pas dans la seconde liste sont donc ceux qu'ils faut retarder
//			//on check qu'il y a plus de 50 passagers à recup + que le train qu'on va retarder est un train avec reservation
//			if(!listTrainCheckIfHaveToDelayAfterAddingPerturbation.isEmpty()) {
//				for(fr.pantheonsorbonne.ufr27.miage.jpa.Train t : listTrainCheckIfHaveToDelay) {
//					if(!listTrainCheckIfHaveToDelayAfterAddingPerturbation.contains(t)) {
//						if(passagerDAO.getNombrePassagerByTrainIdAndNotArrivalAtArretId(perturbation.getTrain().getId(),hdp.getArret().getId()).size()>50 && t instanceof fr.pantheonsorbonne.ufr27.miage.jpa.TrainAvecResa) {
//							listTrainARetarder.add(t);
//						}
//							
//					}
//				}
//				
//			}else {
//				for(fr.pantheonsorbonne.ufr27.miage.jpa.Train t : listTrainCheckIfHaveToDelay) {
//					if(passagerDAO.getNombrePassagerByTrainIdAndNotArrivalAtArretId(perturbation.getTrain().getId(),hdp.getArret().getId()).size()>50 && t.getClass().getName().equals("TrainAvecResa")) {
//						listTrainARetarder.add(t);
//					}	
//				}
//			}
//
//			for(fr.pantheonsorbonne.ufr27.miage.jpa.Train t :listTrainARetarder) {
//				fr.pantheonsorbonne.ufr27.miage.jpa.HeureDePassage hdpTrainARetarder = hdpDAO.findHeureByDepartAfterDateAndTrainIdAndArretIdAndSorted(t.getId(), hdp.getArret().getId(), hdp.getReelArriveeTemps().minusMinutes(perturbation.getDureeEnPlus())).get(0);
//				hdpDAO.retarderHdpDepart(hdpTrainARetarder, perturbation.getDureeEnPlus());//(int) ChronoUnit.MINUTES.between(hdp.getReelArriveeTemps().plusMinutes(5), hdpTrainARetarder.getReelDepartTemps()));
//			}
//			
//		}
//	}

