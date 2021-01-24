package fr.pantheonsorbonne.ufr27.miage.dao;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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

	/**
	 * Méthode permettant de créer une heure de passage pour un train et un arrêt
	 * dans la base de données
	 * 
	 * @param train
	 * @param arret
	 * @param departTemps
	 * @param arriveeTemps
	 * @param desservi
	 * @param terminus
	 * @return HeureDePassage
	 */
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

	/**
	 * Méthode permettant de modifier  une heure de passage avec un nouvel
	 * horaire
	 * 
	 * @param train
	 * @param arret
	 * @param newBaseDepartTemps
	 * @param newBaseArriveeTemps
	 * @param newReelDepartTemps
	 * @param newReelArriveeTemps
	 * @param newDesservi
	 * @param newTerminus
	 * @return HeureDePassage
	 */
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

	/**
	 * Méthode permettant de supprimer une heure de passage de la base de données
	 * 
	 * @param train
	 * @param arret
	 */
	public void deleteHeureDePassage(Train train, Arret arret) {
		HeureDePassage hdp = getHdpFromTrainIdAndArretId(train.getId(), arret.getId());
		train.removeArretHeureDePassage(hdp);
		arret.removeArretHeureDePassage(hdp);
		em.remove(hdp);
	}

	// Obliger d'utiliser des fonctions comme celles ci pour eviter les erreurs :
	// ConcurentModificationException
	/**
	 * Méthode permettant de supprimer une heure de passage en passant par le train
	 * 
	 * @param train
	 */
	public void deleteHeureDePassageByTrain(Train train) {
		List<HeureDePassage> listHdp = getAllHeureDePassage();
		for (HeureDePassage hdp : listHdp) {
			if (hdp.getTrain() == train) {
				deleteHeureDePassage(train, hdp.getArret());
			}
		}
	}

	/**
	 * Méthode permettant de supprimer une heure de passage en passant par l'arrêt
	 * 
	 * @param arret
	 */
	public void deleteHeureDePassageByArret(Arret arret) {
		List<HeureDePassage> listHdp = getAllHeureDePassage();
		for (HeureDePassage hdp : listHdp) {
			if (hdp.getArret() == arret) {
				deleteHeureDePassage(hdp.getTrain(), arret);
			}
		}
	}

	/**
	 * Méthode permettant de récupérer une clé d'heure de passage, c'est à dire l'id
	 * du train et de l'arrêt de l'heure de passage
	 * 
	 * @param key
	 * @return HeureDePassage
	 */
	public HeureDePassage getHeureDePassageFromKey(HeureDePassageKey key) {
		return em.find(HeureDePassage.class, key);
	}

	/**
	 * Méthode permettant de récupérer une heure de passage à partir de l'id du
	 * train et de l'arrêt
	 * 
	 * @param trainId
	 * @param arretId
	 * @return HeureDePassage
	 */
	public HeureDePassage getHdpFromTrainIdAndArretId(int trainId, int arretId) {
		return (HeureDePassage) em.createNamedQuery("findHeureByTrainIdAndArretId").setParameter("trainId", trainId)
				.setParameter("arretId", arretId).getSingleResult();
	}

	/**
	 * Méthode permettant de récupérer une heure de passage à partir de l'id du
	 * train et de l'arrêt ainsi que la date de départ et d'arrivée
	 * 
	 * @param trainId
	 * @param arretId
	 * @param dateArrivee
	 * @param dateDepart
	 * @return HeureDePassage
	 */
	public HeureDePassage findHdpByTrainIdAndArretIdAndHeureReel(int trainId, int arretId, LocalDateTime dateArrivee,
			LocalDateTime dateDepart) {
		return (HeureDePassage) em.createNamedQuery("findHdpByTrainIdAndArretIdAndHeureReel")
				.setParameter("trainId", trainId).setParameter("arretId", arretId)
				.setParameter("dateArrivee", dateArrivee).setParameter("dateDepart", dateDepart).getSingleResult();
	}

	/**
	 * Méthode permettant de récupérer la liste des heures de passage d'un train
	 * 
	 * @param trainId
	 * @return List<HeureDePassage>
	 */
	public List<HeureDePassage> findHdpByTrain(int trainId) {
		return em.createNamedQuery("findHeureByTrainId").setParameter("trainId", trainId).getResultList();
	}

	/**
	 * Méthode permettant de récupérer la liste des heures de passage d'un arrêt
	 * 
	 * @param arretId
	 * @return List<HeureDePassage>
	 */
	public List<HeureDePassage> findHdpByArret(int arretId) {
		return em.createNamedQuery("findHeureByArretId").setParameter("arretId", arretId).getResultList();

	}

	/**
	 * Méthode permettant de récupérer toutes les heures de passages de la base de
	 * données
	 * 
	 * @return List<HeureDePassage>
	 */
	public List<HeureDePassage> getAllHeureDePassage() {
		return em.createNamedQuery("getAllHeureDePassage").getResultList();
	}

	/**
	 * Méthode permettant de changer le paramètre desservi d'une heure de passage
	 * 
	 * @param hdp
	 * @param newDesservi
	 */
	public void changeParameterDesservi(HeureDePassage hdp, boolean newDesservi) {
		updateHeureDePassage(hdp.getTrain(), hdp.getArret(), hdp.getBaseDepartTemps(), hdp.getBaseArriveeTemps(),
				hdp.getReelDepartTemps(), hdp.getReelArriveeTemps(), newDesservi, hdp.isTerminus());
	}

	/**
	 * Méthode permettant de retarder les heures de départ et d'arrivée d'une heure
	 * de passage
	 * 
	 * @param hdp
	 * @param dureeEnPlus
	 */
	public void retarderHdp(HeureDePassage hdp, int dureeEnPlus) {
		updateHeureDePassage(hdp.getTrain(), hdp.getArret(), hdp.getBaseDepartTemps(), hdp.getBaseArriveeTemps(),
				hdp.getReelDepartTemps().plusMinutes(dureeEnPlus), hdp.getReelArriveeTemps().plusMinutes(dureeEnPlus),
				hdp.isDesservi(), hdp.isTerminus());
	}

	/**
	 * Méthode permettant de retarder l'heure de départ d'une heure de passage
	 * 
	 * @param hdp
	 * @param dureeEnPlus
	 */
	public void retarderHdpDepart(HeureDePassage hdp, int dureeEnPlus) {
		updateHeureDePassage(hdp.getTrain(), hdp.getArret(), hdp.getBaseDepartTemps(), hdp.getBaseArriveeTemps(),
				hdp.getReelDepartTemps().plusMinutes(dureeEnPlus), hdp.getReelArriveeTemps(), hdp.isDesservi(),
				hdp.isTerminus());
	}

	/**
	 * Méthode permettant de retarder les heures de départ et d'arrivée d'une heure
	 * de passage
	 * 
	 * @param hdp
	 * @param dureeEnPlus
	 */
	public void retarderHdpArriveeAndDepart(HeureDePassage hdp, int dureeEnPlus) {
		updateHeureDePassage(hdp.getTrain(), hdp.getArret(), hdp.getBaseDepartTemps(), hdp.getBaseArriveeTemps(),
				hdp.getReelDepartTemps().plusMinutes(dureeEnPlus), hdp.getReelArriveeTemps().plusMinutes(dureeEnPlus),
				hdp.isDesservi(), hdp.isTerminus());
	}

	/**
	 * Méthode permettant de vérifier si une heure de passage est correctement créé
	 * dans la base de données
	 * 
	 * @param key
	 * @return true si l'heure de passage a été correctement créé, false sinon
	 */
	public boolean isHeureDePassageCreated(HeureDePassageKey key) {

		HeureDePassage h = em.find(HeureDePassage.class, key);
		if (h == null) {
			throw new NoSuchElementException("No HeureDePassage");
		}
		return h.isCreated();

	}

	/**
	 * méthode permettant de récupérer une heure de passage à partir d'un train et
	 * d'un arrêt
	 * 
	 * @param train
	 * @param arret
	 * @return HeureDePassage
	 */
	public HeureDePassage findHeureByTrainAndArret(Train train, Arret arret) {
		// replace by something better
		return em.find(Train.class, train.getId()).getListeHeureDePassage().stream()
				.filter(hdp -> hdp.getArret().equals(arret)).findFirst().orElseThrow(null);
	}

	/**
	 * Méthode permettant de récupérer une heure de passage à l'heure actuel à
	 * partir de l'id d'un train
	 * 
	 * @param trainId
	 * @return
	 */
	public HeureDePassage getHdpByTrainAndDateNow(int trainId) {
		return (HeureDePassage) em.createNamedQuery("findHeureByDateNowAndTrain")
				.setParameter("temps", LocalDateTime.now()).setParameter("trainId", trainId).getSingleResult();
	}

	/**
	 * Méthode permettant de récupérer les heures de passages d'un train et d'un
	 * arrêt entre deux dates
	 * 
	 * @param arretId
	 * @param date1
	 * @param date2
	 * @return List<HeureDePassage>
	 */
	public List<HeureDePassage> getHdpFromTrainIdAndArretIdAndBetweenDate1AndDate2(int trainId, int arretId,
			LocalDateTime date1, LocalDateTime date2) {
		return em.createNamedQuery("findHeureTrainIdAndArretIdAndBetweenDate1AndDate2").setParameter("trainId", trainId)
				.setParameter("arretId", arretId).setParameter("date1", date1).setParameter("date2", date2)
				.getResultList();
	}

	/**
	 * Méthode permettant de récupérer les heures de passage triés d'un train et
	 * d'un arrêt après la date de départ
	 * 
	 * @param trainId
	 * @param arretId
	 * @param date
	 * @return List<HeureDePassage>
	 */
	public List<HeureDePassage> findHeureByDepartAfterDateAndTrainIdAndArretIdAndSorted(int trainId, int arretId,
			LocalDateTime date) {
		return em.createNamedQuery("findHeureByDepartAfterDateAndTrainIdAndArretIdAndSorted")
				.setParameter("trainId", trainId).setParameter("arretId", arretId).setParameter("temps", date)
				.getResultList();
	}

	/**
	 * Méthode permettant de récupérer les heures de passage triés d'un train après
	 * la date d'arrivée
	 * 
	 * @param trainId
	 * @param date
	 * @return List<HeureDePassage>
	 */
	public List<HeureDePassage> findHdpByTrainAfterDateAndSorted(int trainId, LocalDateTime date) {
		return em.createNamedQuery("findHdpByTrainAfterDateAndSorted").setParameter("trainId", trainId)
				.setParameter("temps", date).getResultList();
	}

	/**
	 * Méthode permettant de récupérer les heures de passage triés d'un train et
	 * d'un arrêt après la date d'arrivée
	 * 
	 * @param trainId
	 * @param arretId
	 * @param date
	 * @return List<HeureDePassage>
	 */
	public List<HeureDePassage> findHdpByTrainIdAndArretIdBeforeDateAndSorted(int trainId, int arretId,
			LocalDateTime date) {
		return em.createNamedQuery("findHdpByTrainIdAndArretIdBeforeDateAndSorted").setParameter("trainId", trainId)
				.setParameter("arretId", arretId).setParameter("temps", date).getResultList();
	}

	/**
	 * Méthode permettant de récupérer les heures de passage triés d'un train après
	 * la date d'arrivée
	 * 
	 * @param trainId
	 * @param date
	 * @return List<HeureDePassage>
	 */
	public List<HeureDePassage> findHdpByTrainAfterDateAndSortedAndDesservi(int trainId, LocalDateTime date) {
		return em.createNamedQuery("findHdpByTrainAfterDateAndSortedAndDesservi").setParameter("trainId", trainId)
				.setParameter("temps", date).getResultList();
	}

	/**
	 * Méthode permettant de récupérer les heures de passage triés d'un train et
	 * d'un arrêt après la date de départ
	 * 
	 * @param trainId
	 * @param arretId
	 * @param date
	 * @return List<HeureDePassage>
	 */
	public List<HeureDePassage> findHeureByDepartAfterDateAndTrainIdAndArretIdAndSortedAndDesservi(int trainId,
			int arretId, LocalDateTime date) {
		return em.createNamedQuery("findHeureByDepartAfterDateAndTrainIdAndArretIdAndSortedAndDesservi")
				.setParameter("trainId", trainId).setParameter("arretId", arretId).setParameter("temps", date)
				.getResultList();
	}

	/**
	 * Méthode permettant de récupérer les heures de passage triés d'un arret en
	 * excluant celles du train en paramètre
	 * 
	 * @param arretId
	 * @param trainId
	 * @param date
	 * @return List<HeureDePassage>
	 */
	public List<HeureDePassage> findHdpByArretAndNotTrainIdAndSorted(int arretId, int trainId, LocalDateTime date) {
		return em.createNamedQuery("findHdpByArretAndNotTrainIdAndSorted").setParameter("arretId", trainId)
				.setParameter("trainId", trainId).setParameter("temps", date).getResultList();
	}

	/**
	 * Méthode permettant de récupérer la prochaine heure de passage d'un train
	 * 
	 * @param trainId
	 * @return HeureDePassage
	 */
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
