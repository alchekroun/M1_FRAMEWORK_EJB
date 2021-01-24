package fr.pantheonsorbonne.ufr27.miage.dao;

import java.util.List;
import java.util.NoSuchElementException;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import fr.pantheonsorbonne.ufr27.miage.jpa.Arret;

public class ArretDAO {
	@Inject
	EntityManager em;

	@Inject
	HeureDePassageDAO hdpDAO;

	/**
	 * Méthode permettant de récupérer un arrêt avec son id
	 * 
	 * @param arretId
	 * @return arret
	 */
	public Arret getArretFromId(int arretId) {
		return em.find(Arret.class, arretId);
	}

	/**
	 * Méthode permettant de modifier un arrêt
	 * 
	 * @param arretOriginal
	 * @param arretUpdate
	 * @return arret
	 */
	public Arret updateArret(Arret arretOriginal, fr.pantheonsorbonne.ufr27.miage.model.jaxb.Arret arretUpdate) {
		arretOriginal.setNom(arretUpdate.getNom());
		return arretOriginal;
	}

	/**
	 * Methode permettant de supprimer un arrêt
	 * 
	 * @param arret
	 */
	public void deleteArret(Arret arret) {
		if (!arret.getListeHeureDePassage().isEmpty()) {
			hdpDAO.deleteHeureDePassageByArret(arret);
		}
		em.remove(arret);
	}

	/**
	 * Méthode permettant de récuperer tous les arrêts
	 */
	public List<Arret> getAllArret() {
		return em.createNamedQuery("getAllArret").getResultList();
	}

	/**
	 * Méthode permettant de récupérer tous les arrêts d'un train
	 * 
	 * @param trainId
	 * @return
	 */
	public List<Arret> getAllArretByTrain(int trainId) {
		return em.createNamedQuery("findArretByTrain").setParameter("id", trainId).getResultList();
	}

	/**
	 * Méthode permettant de savoir si un arrêt a bien été créé
	 * 
	 * @param ArretId
	 * @return true si l'arrêt a été correctement créé, false sinon
	 */
	public boolean isArretCreated(int ArretId) {

		Arret a = em.find(Arret.class, ArretId);
		if (a == null) {
			throw new NoSuchElementException("No Arret");
		}
		return a.isCreated();

	}

}
