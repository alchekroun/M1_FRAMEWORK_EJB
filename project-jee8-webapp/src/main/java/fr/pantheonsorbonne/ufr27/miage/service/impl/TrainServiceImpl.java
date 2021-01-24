package fr.pantheonsorbonne.ufr27.miage.service.impl;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

import java.util.List;

import javax.annotation.ManagedBean;
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
import fr.pantheonsorbonne.ufr27.miage.model.jaxb.Arret;
import fr.pantheonsorbonne.ufr27.miage.model.jaxb.HeureDePassage;
import fr.pantheonsorbonne.ufr27.miage.model.jaxb.Passager;
import fr.pantheonsorbonne.ufr27.miage.model.jaxb.Perturbation;
import fr.pantheonsorbonne.ufr27.miage.model.jaxb.Train;
import fr.pantheonsorbonne.ufr27.miage.model.jaxb.TrainAvecResa;
import fr.pantheonsorbonne.ufr27.miage.service.TrainService;

@ManagedBean
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

			// Nous avons remarqué que les train dans les hdp ne se mettaient pas à jour
			// Nous avons essayé cela mais ça ne change rien. Les trains restent OFF dans
			// les HPD alors qu'ils sont ON
			for (fr.pantheonsorbonne.ufr27.miage.jpa.HeureDePassage hdp : hdpDAO
					.findHdpByTrain(trainOriginal.getId())) {
				em.merge(hdp);
				em.merge(hdp.getTrain());
			}

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
	public int enMarche(Train train) throws NoSuchTrainException {
		fr.pantheonsorbonne.ufr27.miage.jpa.Train trainJPA = dao.getTrainFromId(train.getId());
		if (trainJPA == null) {
			// throw new NoSuchTrainException();
			return -1;
		}
		if (train.getStatut().equals("off")) {
			changeStatut(train, "on");
		}
		if (hdpDAO.findHdpByTrain(trainJPA.getId()) != null) {

			retarderCorrespondance(train);

			HeureDePassage hdpActuel = verifIfExistArretNow(trainJPA.getId());
			if (hdpActuel != null && hdpActuel.isDesservi()) {

				Arret arretActuel = hdpActuel.getArret();

				// Fait descendre les gens qui doivent descendre

				// Arrivee
				descendreListPassager(
						PassagerMapper.passagerAllDTOMapper(passagerDAO.getAllPassagerByArrivee(arretActuel.getId())),
						trainJPA, arretActuel.getId());

				// Correspondance
				descendreListPassager(
						PassagerMapper
								.passagerAllDTOMapper(passagerDAO.getAllPassagerByCorrespondance(arretActuel.getId())),
						trainJPA, arretActuel.getId());

				if (!hdpActuel.isTerminus()) {

					// Fait monter les gens qui attendent sur le quai

					List<Passager> listPassagerDescendre = new ArrayList<Passager>();

					for (Passager p : PassagerMapper
							.passagerAllDTOMapper(passagerDAO.getAllPassagerByDepart(arretActuel.getId()))) {
						if (trainGetMeWhereIWant(train, p)) {
							listPassagerDescendre.add(p);
						}
					}

					monterListPassager(listPassagerDescendre, trainJPA);

					return 1;

				}
				// interupt le thread car nous sommes arriver au terminus
				changeStatut(train, "off");
				return -1;

			}
			// Le train n'a pas d'arrêt à desservir pour le moment.
			HeureDePassage nextHdp = HeureDePassageMapper.heureDePassageDTOMapper(hdpDAO.findNextHdp(train.getId()));
			if (nextHdp == null) {
				// Le train n'a plus d'arrêt à desservir
				changeStatut(train, "off");
				return -1;
			}

			arretExceptionnel(nextHdp);
			return 1;

		}
		// Le train n'a aucun arrêt à desservir
		changeStatut(train, "off");
		return -1;

	}

	protected void changeStatut(Train t, String s) {
		if (s.equals("on") || s.equals("off")) {
			t.setStatut(s);
			try {
				updateTrain(t);
			} catch (NoSuchTrainException | CantUpdateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	protected void descendreListPassager(List<Passager> listPassager, fr.pantheonsorbonne.ufr27.miage.jpa.Train train,
			int arretId) {
		em.getTransaction().begin();
		fr.pantheonsorbonne.ufr27.miage.jpa.Arret aJPA = em.find(fr.pantheonsorbonne.ufr27.miage.jpa.Arret.class,
				arretId);
		for (Passager p : listPassager) {
			fr.pantheonsorbonne.ufr27.miage.jpa.Passager pJPA = em
					.find(fr.pantheonsorbonne.ufr27.miage.jpa.Passager.class, p.getId());
			if (train.getListePassagers().contains(pJPA)) {
				pJPA.setDepart(aJPA);
				dao.removePassager(train, pJPA);
			}
		}
		em.getTransaction().commit();
	}

	protected void monterListPassager(List<Passager> listPassager, fr.pantheonsorbonne.ufr27.miage.jpa.Train train) {
		for (Passager p : listPassager) {
			em.getTransaction().begin();
			fr.pantheonsorbonne.ufr27.miage.jpa.Passager pJPA = em
					.find(fr.pantheonsorbonne.ufr27.miage.jpa.Passager.class, p.getId());
			if (!train.getListePassagers().contains(pJPA)) {
				dao.addPassager(train, pJPA);
			}
			em.getTransaction().commit();
		}
	}

	protected HeureDePassage verifIfExistArretNow(int trainId) {
		return HeureDePassageMapper.heureDePassageDTOMapper(hdpDAO.getHdpByTrainAndDateNow(trainId));
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
		int cpt = 0;
		for (Passager p : listPassager) {
			if (trainGetMeWhereIWant(hdpTrain.getTrain(), p)) {
				cpt++;
			}
		}
		if (cpt > 50) {
			return true;
		}
		return false;
	}

	public void retarderCorrespondance(Train train) throws NoSuchTrainException {

		List<fr.pantheonsorbonne.ufr27.miage.jpa.HeureDePassage> listHdpTrain = hdpDAO
				.findHdpByTrainByDateAndSortedAndDesservi(train.getId(), LocalDateTime.now());
		List<HeureDePassage> listHdp = HeureDePassageMapper.heureDePassageAllDTOMapper(listHdpTrain);

		if (!listHdp.isEmpty()) {

			HeureDePassage hdp = listHdp.get(0);

			HeureDePassage hdpTrainEnRetard = verifIfNextArretHasTrainEnRetard(hdp);
			if (dao.getTrainAvecResa(train.getId()) != null && hdpTrainEnRetard != null) {

				List<Passager> listPassagerTrainPerturbeTakingCorrespondanceAtArret = PassagerMapper
						.passagerAllDTOMapper(passagerDAO.getPassagerByTrainIdAndNotArrivalAtArretId(
								hdpTrainEnRetard.getTrain().getId(), hdp.getArret().getId()));

				if (isWorthRetardTrain(listPassagerTrainPerturbeTakingCorrespondanceAtArret, hdp)) {

					fr.pantheonsorbonne.ufr27.miage.jpa.HeureDePassage hdpTrainARetarder = listHdpTrain.get(0);
					fr.pantheonsorbonne.ufr27.miage.jpa.HeureDePassage hdpTrainEnRetardJpa = hdpDAO
							.findHdpByTrainIdAndArretIdAndHeureReel(hdpTrainEnRetard.getTrain().getId(),
									hdpTrainEnRetard.getArret().getId(), hdpTrainEnRetard.getReelArriveeTemps(),
									hdpTrainEnRetard.getReelDepartTemps());
	
					int diff = (int) ChronoUnit.MINUTES.between(hdpTrainARetarder.getBaseDepartTemps(), hdpTrainEnRetardJpa.getReelArriveeTemps().minusMinutes(10));
					int diff2 = (int) ChronoUnit.MINUTES.between(LocalDateTime.now(), hdpTrainEnRetardJpa.getReelArriveeTemps());
					Perturbation perturbation1 = new Perturbation();
					perturbation1.setMotif("retard ligne");
					perturbation1.setTrain(train);
					perturbation1.setDureeEnPlus(diff+diff2);
					createPerturbation(perturbation1);
							
						
				}
			}
		}
	}

	protected void arretExceptionnel(HeureDePassage nextHdp) {
		HeureDePassage hdpAvecTrainEnRetard = verifIfNextArretHasTrainEnRetard2h(nextHdp);
		if (hdpAvecTrainEnRetard != null && dao.getTrainAvecResa(hdpAvecTrainEnRetard.getTrain().getId()) != null) {
			hdpDAO.changeParameterDesservi(
					hdpDAO.getHdpFromTrainIdAndArretId(nextHdp.getTrain().getId(), nextHdp.getArret().getId()), true);

		}
	}

	protected boolean trainGetMeWhereIWant(Train train, Passager passager) {
		List<HeureDePassage> listHdp = HeureDePassageMapper
				.heureDePassageAllDTOMapper(hdpDAO.findHdpByTrain(train.getId()));
		for (HeureDePassage hdp : listHdp) {
			// Faire gaffe aux corress
			if (hdp.getReelDepartTemps().isAfter(LocalDateTime.now())) {
				if (hdp.getArret().getId() == passager.getArrive().getId()) {
					return true;
				}
				if (passager.getCorrespondance() != null) {
					if (hdp.getArret().getId() == passager.getCorrespondance().getId()) {
						return true;
					}
				}
			}
		}
		return false;
	}

	protected HeureDePassage verifIfNextArretHasTrainEnRetard2h(HeureDePassage hdpBase) {
		List<HeureDePassage> listHdpAtArretId = HeureDePassageMapper
				.heureDePassageAllDTOMapper(hdpDAO.findHdpByArret(hdpBase.getArret().getId()));

		for (HeureDePassage hdp : listHdpAtArretId) {

			// Seuls les trains qui arrivent après ce train sont prit en compte
			if (hdp.getReelArriveeTemps().isAfter(hdpBase.getReelArriveeTemps())) {

				// On recherche les hdp en retard
				if (hdp.getBaseArriveeTemps().compareTo((hdp.getReelArriveeTemps())) != 0) {

					if (isRetardMoreThan2hours(hdp)) {
						return hdp;
					}
				}
			}
		}
		return null;
	}

	protected boolean isRetardMoreThan2hours(HeureDePassage hdp) {
		return hdp.getReelArriveeTemps().isAfter(hdp.getBaseArriveeTemps().plusHours(2));
	}

}
