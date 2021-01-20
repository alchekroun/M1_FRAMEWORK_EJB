package fr.pantheonsorbonne.ufr27.miage.dao;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import fr.pantheonsorbonne.ufr27.miage.jpa.Arret;
import fr.pantheonsorbonne.ufr27.miage.jpa.HeureDePassage;
import fr.pantheonsorbonne.ufr27.miage.jpa.HeureDePassageKey;
import fr.pantheonsorbonne.ufr27.miage.jpa.Train;

public class HeureDePassageDAO {

	@Inject
	public HeureDePassageDAO(EntityManager em) {
		this.em = em;
	}

	EntityManager em;

	public HeureDePassage createHeureDePassage(Train train, Arret arret, LocalDateTime departTemps,
			LocalDateTime arriveeTemps, boolean desservi, boolean terminus) {
		HeureDePassageKey hdpKey = new HeureDePassageKey();
		hdpKey.setArretId(arret.getId());
		hdpKey.setTrainId(train.getId());
		HeureDePassage hdp = new HeureDePassage();
		hdp.setId(hdpKey);
		hdp.setArret(arret);
		hdp.setTrain(train);
		hdp.setBaseDepartTemps(departTemps);
		hdp.setReelDepartTemps(departTemps);
		hdp.setBaseArriveeTemps(arriveeTemps);
		hdp.setReelArriveeTemps(arriveeTemps);
		hdp.setDesservi(desservi);
		hdp.setTerminus(terminus);
		em.persist(hdp);
		train.addArretHeureDePassage(hdp);
		arret.addArretHeureDePassage(hdp);
		em.merge(train);
		em.merge(arret);
		return hdp;
	}

	public HeureDePassage updateHeureDePassage(Train train, Arret arret, LocalDateTime newBaseDepartTemps,
			LocalDateTime newBaseArriveeTemps, LocalDateTime newReelDepartTemps, LocalDateTime newReelArriveeTemps,
			boolean newDesservi, boolean newTerminus) {
		HeureDePassage hdp = getHdpFromTrainIdAndArretId(train.getId(), arret.getId());
		hdp.setArret(arret);
		hdp.setTrain(train);
		hdp.setBaseArriveeTemps(newBaseArriveeTemps);
		hdp.setBaseDepartTemps(newBaseDepartTemps);
		hdp.setReelArriveeTemps(newReelArriveeTemps);
		hdp.setReelDepartTemps(newReelDepartTemps);
		hdp.setDesservi(newDesservi);
		hdp.setTerminus(newTerminus);
		return hdp;
	}

	public void deleteHeureDePassage(Train train, Arret arret) {
		HeureDePassage hdp = getHdpFromTrainIdAndArretId(train.getId(), arret.getId());
		train.removeArretHeureDePassage(hdp);
		arret.removeArretHeureDePassage(hdp);
		em.remove(hdp);
	}

	// Obliger d'utiliser des fonctions comme celles ci pour eviter les erreurs :
	// ConcurentModificationException
	public void deleteHeureDePassageByTrain(Train train) {
		List<HeureDePassage> listHdp = getAllHeureDePassage();
		for (HeureDePassage hdp : listHdp) {
			if (hdp.getTrain() == train) {
				deleteHeureDePassage(train, hdp.getArret());
			}
		}
	}

	public void deleteHeureDePassageByArret(Arret arret) {
		List<HeureDePassage> listHdp = getAllHeureDePassage();
		for (HeureDePassage hdp : listHdp) {
			if (hdp.getArret() == arret) {
				deleteHeureDePassage(hdp.getTrain(), arret);
			}
		}
	}

	public HeureDePassage getHeureDePassageFromKey(HeureDePassageKey key) {
		return em.find(HeureDePassage.class, key);
	}

	public HeureDePassage getHdpFromTrainIdAndArretId(int trainId, int arretId) {
		return (HeureDePassage) em.createNamedQuery("findHeureByTrainIdAndArretId").setParameter("trainId", trainId)
				.setParameter("arretId", arretId).getSingleResult();
	}

	public List<HeureDePassage> findHdpByTrain(int trainId) {
		return em.createNamedQuery("findHeureByTrainId").setParameter("trainId", trainId).getResultList();
	}

	public List<HeureDePassage> findHdpByArret(int arretId) {
		return em.createNamedQuery("findHeureByArretId").setParameter("arretId", arretId).getResultList();

	}

	public List<HeureDePassage> getAllHeureDePassage() {
		return em.createNamedQuery("getAllHeureDePassage").getResultList();
	}

	public void changeParameterDesservi(HeureDePassage hdp, boolean newDesservi) {
		updateHeureDePassage(hdp.getTrain(), hdp.getArret(), hdp.getBaseDepartTemps(), hdp.getBaseArriveeTemps(),
				hdp.getReelDepartTemps(), hdp.getReelArriveeTemps(), newDesservi, hdp.isTerminus());
	}

	public void retarderHdp(HeureDePassage hdp, int dureeEnPlus) {
		updateHeureDePassage(hdp.getTrain(), hdp.getArret(), hdp.getBaseDepartTemps(), hdp.getBaseArriveeTemps(),
				hdp.getReelDepartTemps().plusMinutes(dureeEnPlus), hdp.getReelArriveeTemps().plusMinutes(dureeEnPlus),
				hdp.isDesservi(), hdp.isTerminus());
	}

	public boolean isHeureDePassageCreated(HeureDePassageKey key) {

		HeureDePassage h = em.find(HeureDePassage.class, key);
		if (h == null) {
			throw new NoSuchElementException("No HeureDePassage");
		}
		return h.isCreated();

	}

	public HeureDePassage findHeureByTrainAndArret(Train train, Arret arret) {
		// replace by something better
		return em.find(Train.class, train.getId()).getListeHeureDePassage().stream()
				.filter(hdp -> hdp.getArret().equals(arret)).findFirst().orElseThrow(null);
	}

	public HeureDePassage getHdpByTrainAndDateNow(int trainId) {
		return (HeureDePassage) em.createNamedQuery("findHeureByDateNowAndTrain")
				.setParameter("temps", LocalDateTime.now()).setParameter("trainId", trainId).getSingleResult();
	}

	public List<HeureDePassage> getHdpFromTrainIdAndArretIdAndBetweenDate1AndDate2(int trainId, int arretId,
			LocalDateTime date1, LocalDateTime date2) {
		return em.createNamedQuery("findHeureTrainIdAndArretIdAndBetweenDate1AndDate2").setParameter("trainId", trainId)
				.setParameter("arretId", arretId).setParameter("date1", date1).setParameter("date2", date2)
				.getResultList();
	}

	public List<HeureDePassage> findHeureByDepartAfterDateAndTrainIdAndArretIdAndSorted(int trainId, int arretId,
			LocalDateTime date) {
		return em.createNamedQuery("findHeureByDepartAfterDateAndTrainIdAndArretIdAndSorted")
				.setParameter("trainId", trainId).setParameter("arretId", arretId).setParameter("temps", date)
				.getResultList();
	}

	public List<HeureDePassage> findHdpByTrainAfterDateAndSorted(int trainId, LocalDateTime date) {
		return em.createNamedQuery("findHdpByTrainAfterDateAndSorted").setParameter("trainId", trainId)
				.setParameter("temps", date).getResultList();
	}

	public HeureDePassage findNextHdp(int trainId) {
		return findHdpByTrainAfterDateAndSorted(trainId, LocalDateTime.now()).get(0);
	}

	// retourne une liste des hdp des trains partant de arretId au plus tôt juste
	// après une date
//	public List<HeureDePassage> findHeureMoreRecentByArretIdAfterDate(int arretId, LocalDateTime date){
//		List<Object[]> list = em.createNamedQuery("findHeureMoreRecentByArretIdAfterDate")
//				.setParameter("arretId", arretId).setParameter("temps",date).getResultList();
//		List<HeureDePassage> listHdp = new ArrayList<>();
//		for (Object[] obj : list) {
//			listHdp.add((HeureDePassage) em.createNamedQuery("findHeureById").setParameter("idHdp", (HeureDePassageKey) obj[0]).getSingleResult());
//		}
//		
//		return listHdp;
//	}

}
