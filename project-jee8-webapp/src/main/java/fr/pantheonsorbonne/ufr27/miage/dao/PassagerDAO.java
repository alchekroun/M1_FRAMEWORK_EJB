package fr.pantheonsorbonne.ufr27.miage.dao;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.TreeSet;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import fr.pantheonsorbonne.ufr27.miage.jpa.Passager;
import fr.pantheonsorbonne.ufr27.miage.jpa.Train;
import fr.pantheonsorbonne.ufr27.miage.jpa.Arret;
import fr.pantheonsorbonne.ufr27.miage.jpa.HeureDePassage;

public class PassagerDAO {
	@Inject
	EntityManager em;

	@Inject
	TrainDAO trainDAO;

	@Inject
	HeureDePassageDAO hdpDAO;

	/**
	 * Méthode permettant de récupérer un passager à partir de son id
	 * 
	 * @param passagerId
	 * @return Passager
	 */
	public Passager getPassagerFromId(int passagerId) {
		return em.find(Passager.class, passagerId);
	}

	/**
	 * Méthode permettant de modifier les caractéristiques d'un passager
	 * 
	 * @param passagerOriginal
	 * @param passagerUpdate
	 * @return Passager
	 */
	public Passager updatePassager(Passager passagerOriginal,
			fr.pantheonsorbonne.ufr27.miage.model.jaxb.Passager passagerUpdate) {

		passagerOriginal.setArrive(em.find(Arret.class, passagerUpdate.getArrive().getId()));
		passagerOriginal.setDepart(em.find(Arret.class, passagerUpdate.getDepart().getId()));
		passagerOriginal.setNom(passagerUpdate.getNom());

		return passagerOriginal;
	}

	/**
	 * Méthode permettant de supprimer un passager de la base de données
	 * 
	 * @param passager
	 */
	public void deletePassager(Passager passager) {
		if (passager.getTrain() != null) {
			trainDAO.removePassager(passager.getTrain(), passager);
		}
		em.remove(passager);
	}

	/**
	 * Méthode permettant de récupérer tous les passagers de la base de données
	 * 
	 * @return
	 */
	public List<Passager> getAllPassager() {
		return em.createNamedQuery("getAllPassager").getResultList();
	}

	/**
	 * Méthode permettant de récupérer tous les passagers d'un train
	 * 
	 * @param trainId
	 * @return
	 */
	public List<Passager> getAllPassagerByTrain(int trainId) {
		return em.createNamedQuery("findAllPassagerByTrain").setParameter("trainId", trainId).getResultList();
	}

	/**
	 * Méthode permettant de récupérer tous les passagers au départ du train
	 * 
	 * @param arretId
	 * @return List<Passager>
	 */
	public List<Passager> getAllPassagerByDepart(int arretId) {
		return em.createNamedQuery("findPassagerByDepart").setParameter("idArretDepart", arretId).getResultList();
	}

	/**
	 * Méthode permettant de récupérer tous les passagers à l'arrivée du train
	 * 
	 * @param arretId
	 * @return List<Passager>
	 */
	public List<Passager> getAllPassagerByArrivee(int arretId) {
		return em.createNamedQuery("findPassagerByArrivee").setParameter("idArretArrivee", arretId).getResultList();
	}

	/**
	 * Méthode permettant de récupérer tous les passagers pour une correspondance
	 * 
	 * @param arretId
	 * @return List<Passager>
	 */
	public List<Passager> getAllPassagerByCorrespondance(int arretId) {
		return em.createNamedQuery("findPassagerByCorrespondance").setParameter("arretId", arretId).getResultList();
	}

	/**
	 * Méthode permettant de savoir si un passager a bien été créé
	 * 
	 * @param passagerId
	 * @return true si le passager a été correctement créé, false sinon
	 */
	public boolean isPassagerCreated(int passagerId) {

		Passager p = em.find(Passager.class, passagerId);
		if (p == null) {
			throw new NoSuchElementException("No passager");
		}
		return p.isCreated();

	}

	public boolean isTrainTakeMeWhereIWant(int passagerId, int trainId) {
		// TODO
		return false;
	}

	public List<Passager> getPassagerByTrainIdAndNotArrivalAtArretId(int trainId, int arretId){
		return em.createNamedQuery("getPassagerByTrainIdAndNotArrivalAtArretId").setParameter("trainId", trainId).setParameter("idArret", arretId).getResultList();
    
	/**
	 * Méthode permettant de récupérer les passagers n'ayant pas l'arrêt arretId
	 * comme arrêt d'arrivée mais comme correspondance
	 * 
	 * @param trainId
	 * @param arretId
	 * @return List<Passager>
	 */
	public List<Passager> getPassagerByTrainIdAndNotArrivalAtArretId(int trainId, int arretId) {
		return em.createNamedQuery("getPassagerByTrainIdAndNotArrivalAtArretId").setParameter("trainId", trainId)
				.setParameter("arretId", arretId).getResultList();
	}

	/**
	 * Méthode permettant de savoir le trajet prévu pour un passager
	 * 
	 * @param passagerId
	 * @return Train
	 */
	public Train findTrajet(int passagerId) {

		class Etat {
			private HeureDePassage actuel;
			private HeureDePassage precedent = null;
			private int nombresChangements = 0;
			private Etat etatPrecedent = null;

			public Etat(HeureDePassage actuel, HeureDePassage precedent, Etat etat, int nombreChangements) {
				this.actuel = actuel;
				this.precedent = precedent;
				this.etatPrecedent = etat;
				this.nombresChangements = nombreChangements;
			}

			public Etat(HeureDePassage actuel) {
				this.actuel = actuel;
			}

			public HeureDePassage getActuel() {
				return this.actuel;
			}

			public HeureDePassage getPrecedent() {
				return this.precedent;
			}

			public Etat getEtatPrecedent() {
				return this.etatPrecedent;
			}

			public int getNombreChangements() {
				return this.nombresChangements;
			}
		}

		Passager p = em.find(Passager.class, passagerId);
		List<Train> listeTrainByArretDepartPassager;
		List<HeureDePassage> listeHdpAfterDepartByTrain = null;
		List<HeureDePassage> listeHdpAfterDepartArretEnCoursByTrain;

		TreeSet<Etat> listeEtatPossible = new TreeSet<>(new Comparator<Etat>() {
			@Override
			public int compare(Etat et1, Etat et2) {
				if (et1 == null || et2 == null) {
					return 0;
				}
				return et1.getActuel().getReelArriveeTemps().compareTo(et2.getActuel().getReelArriveeTemps());
			}
		});

		TreeSet<Etat> listeEtatDirectPossible = new TreeSet<>(new Comparator<Etat>() {
			@Override
			public int compare(Etat et1, Etat et2) {
				if (et1 == null || et2 == null) {
					return 0;
				}
				return et1.getActuel().getReelArriveeTemps().compareTo(et2.getActuel().getReelArriveeTemps());
			}
		});

		TreeSet<Etat> listeEtatCalculCorrespondance = new TreeSet<>(new Comparator<Etat>() {
			@Override
			public int compare(Etat et1, Etat et2) {
				if (et1 == null || et2 == null) {
					return 0;
				}
				return et1.getActuel().getReelArriveeTemps().compareTo(et2.getActuel().getReelArriveeTemps());
			}
		});

		TreeSet<Etat> listeEtatsFinaux = new TreeSet<>(new Comparator<Etat>() {
			@Override
			public int compare(Etat et1, Etat et2) {
				if (et1 == null || et2 == null) {
					return 0;
				}
				return et1.getActuel().getReelArriveeTemps().compareTo(et2.getActuel().getReelArriveeTemps());
			}
		});

		TreeSet<Etat> listeEtatDejaVisite = new TreeSet<>(new Comparator<Etat>() {
			@Override
			public int compare(Etat et1, Etat et2) {
				if (et1 == null || et2 == null) {
					return 0;
				}
				return et1.getActuel().getReelArriveeTemps().compareTo(et2.getActuel().getReelArriveeTemps());
			}
		});

		if (p.getDepart().getId() != p.getArrive().getId()) {
			Arret arretDepart = p.getDepart();
			LocalDateTime dateNow = LocalDateTime.now();
			listeTrainByArretDepartPassager = trainDAO
					.findTrainByArretAndDepartAfterDateAndDesservi(arretDepart.getId(), dateNow);
			for (fr.pantheonsorbonne.ufr27.miage.jpa.Train train : listeTrainByArretDepartPassager) {
				List<HeureDePassage> listHdpDepart = hdpDAO
						.findHeureByDepartAfterDateAndTrainIdAndArretIdAndSortedAndDesservi(train.getId(),
								arretDepart.getId(), dateNow);
				HeureDePassage hdpDepart = null;

				if(!listHdpDepart.isEmpty()) {
					hdpDepart = hdpDAO.returnFirstHdpDesservi(listHdpDepart);
					if(hdpDepart!=null) {
						listeHdpAfterDepartByTrain = hdpDAO.findHdpByTrainAfterDateAndSortedAndDesservi(train.getId(),hdpDepart.getReelDepartTemps());
					}
				}
				Etat depart = new Etat(hdpDepart);
				HeureDePassage hdpSuivante = null;
				boolean dernierArret = false;
          
				if(listeHdpAfterDepartByTrain!=null && !listeHdpAfterDepartByTrain.isEmpty()) {
					hdpSuivante = hdpDAO.returnFirstHdpDesservi(listeHdpAfterDepartByTrain);
							//on ajoute à la liste des calculs pour les correspondances pour la suite de l'algo si pas de chemin direct trouve
							if(hdpSuivante!=null && hdpSuivante.getTrain().getId()==depart.getActuel().getTrain().getId()) {
								listeEtatPossible.add(new Etat(hdpSuivante, depart.getActuel() ,depart, depart.getNombreChangements()));
								listeEtatCalculCorrespondance.add(new Etat(hdpSuivante, depart.getActuel() ,depart, depart.getNombreChangements()));
								if(hdpSuivante.getArret().getId() == p.getArrive().getId()) {
									listeEtatDirectPossible.add(new Etat(hdpSuivante, depart.getActuel() ,depart, depart.getNombreChangements()));
									dernierArret=true;
								}
							}
							else {
								listeEtatPossible.add(new Etat(hdpSuivante, depart.getActuel() ,depart, depart.getNombreChangements()+1));
								listeEtatCalculCorrespondance.add(new Etat(hdpSuivante, depart.getActuel() ,depart, depart.getNombreChangements()+1));
								if(hdpSuivante.getArret().getId() == p.getArrive().getId()) {
									listeEtatDirectPossible.add(new Etat(hdpSuivante, depart.getActuel() ,depart, depart.getNombreChangements()+1));
									dernierArret=true;
								}
								
							}
				}

				boolean tousLesCheminsParcourus = false;
				while (((!listeEtatPossible.isEmpty()) && (!tousLesCheminsParcourus)) && (!dernierArret)) {
					Etat etatEnCours = listeEtatPossible.pollFirst();
					// récup les hdp des arrets parcouru par le train juste après le départ de
					// l'arrêt en cours
					listeHdpAfterDepartArretEnCoursByTrain = hdpDAO.findHdpByTrainAfterDateAndSortedAndDesservi(
							etatEnCours.getActuel().getTrain().getId(), etatEnCours.getActuel().getReelDepartTemps());
					HeureDePassage hdpSuivanteFromArretEnCours = null;
					if (!listeHdpAfterDepartArretEnCoursByTrain.isEmpty()) {
						hdpSuivanteFromArretEnCours = listeHdpAfterDepartArretEnCoursByTrain.get(0);
						if (hdpSuivanteFromArretEnCours.getArret() != p.getDepart()) {
							listeEtatPossible.add(new Etat(hdpSuivanteFromArretEnCours, etatEnCours.getActuel(),
									etatEnCours, etatEnCours.getNombreChangements()));
							if (hdpSuivanteFromArretEnCours.getArret().getId() == p.getArrive().getId()) {
								listeEtatDirectPossible.add(new Etat(hdpSuivanteFromArretEnCours,
										etatEnCours.getActuel(), etatEnCours, etatEnCours.getNombreChangements()));
								dernierArret = true;
							}
						} else {
							tousLesCheminsParcourus = true;
						}
					} else {
						tousLesCheminsParcourus = true;
					}
				}
				if(listeHdpAfterDepartByTrain!=null) {
					listeHdpAfterDepartByTrain.clear();
				}
			}
			// cas ou il existe un train qui emmene directement a destination sans
			// correspondance
			if (!listeEtatDirectPossible.isEmpty()) {
				return listeEtatDirectPossible.pollFirst().getActuel().getTrain();
			}

			while ((!listeEtatCalculCorrespondance.isEmpty())) {
				TreeSet<Etat> listeEtatDejaVisitePrecedente = new TreeSet<>(listeEtatDejaVisite);
				int size = listeEtatCalculCorrespondance.size();
				Etat etatEnCours = listeEtatCalculCorrespondance.pollFirst();
				// je recup liste des trains de l'arret en cours qui partent après larrivee du
				// train provenant de larret precedent
				listeTrainByArretDepartPassager = trainDAO.findTrainByArretAndDepartAfterDateAndDesservi(
						etatEnCours.getActuel().getArret().getId(), etatEnCours.getActuel().getReelArriveeTemps());
				for (fr.pantheonsorbonne.ufr27.miage.jpa.Train train : listeTrainByArretDepartPassager) {
					// liste des hdp des(du) train(s) qui minteresse pour larret en cours cad que le
					// train en question doit partir de larret apres que le train qui vient de
					// larret precdent soit arrive
					List<HeureDePassage> listHdpDepart = hdpDAO
							.findHeureByDepartAfterDateAndTrainIdAndArretIdAndSortedAndDesservi(train.getId(),
									etatEnCours.getActuel().getArret().getId(),
									etatEnCours.getActuel().getReelArriveeTemps());
					HeureDePassage hdpDepart = null;

					if(!listHdpDepart.isEmpty()) {
						hdpDepart = hdpDAO.returnFirstHdpDesservi(listHdpDepart);
						if(hdpDepart!=null) {
						//je veux recup hdp heure darrivee la plus tot du prochain arret et qui soit le plus tot par rapport à mon heure de depart 
						listeHdpAfterDepartArretEnCoursByTrain = hdpDAO.findHdpByTrainAfterDateAndSortedAndDesservi(train.getId(),hdpDepart.getReelDepartTemps());
						HeureDePassage hdpSuivanteFromArretEnCours=null;
							if(!listeHdpAfterDepartArretEnCoursByTrain.isEmpty()) {
								hdpSuivanteFromArretEnCours = hdpDAO.returnFirstHdpDesservi(listeHdpAfterDepartArretEnCoursByTrain);
								Etat newEtat = null;
								if(hdpSuivanteFromArretEnCours!=null) {
									if(hdpSuivanteFromArretEnCours.getTrain().getId()==etatEnCours.getActuel().getTrain().getId()) {
										newEtat = new Etat(hdpSuivanteFromArretEnCours, etatEnCours.getActuel() ,etatEnCours, etatEnCours.getNombreChangements());
									}
									else {
										newEtat = new Etat(hdpSuivanteFromArretEnCours, etatEnCours.getActuel() ,etatEnCours, etatEnCours.getNombreChangements()+1);
									}
										
									boolean contientEtat=false;
										
									if(!listeEtatDejaVisite.isEmpty()) {
										for(Etat e : listeEtatDejaVisite) {
											if(e.getActuel().getId() == newEtat.getActuel().getId() && e.getPrecedent().getId() == newEtat.getPrecedent().getId()) {
													contientEtat=true;
											}
										}
									}
										
									if(!contientEtat) {
										listeEtatCalculCorrespondance.add(newEtat);
										listeEtatDejaVisite.add(newEtat);
										if(hdpSuivanteFromArretEnCours.getArret().getId() == p.getArrive().getId()) {
												listeEtatsFinaux.add(newEtat);
										}
									}
								}
							}
						}
					}
				}
			}

			if (!listeEtatsFinaux.isEmpty()) {
				Etat meilleurEtatFinalCorrespondance = listeEtatsFinaux.pollFirst();
				while (meilleurEtatFinalCorrespondance.getEtatPrecedent().getEtatPrecedent() != null) {
					if (meilleurEtatFinalCorrespondance.getEtatPrecedent().getNombreChangements() == 0
							&& meilleurEtatFinalCorrespondance.getNombreChangements() == 1) {
						break;
					}
					meilleurEtatFinalCorrespondance = meilleurEtatFinalCorrespondance.getEtatPrecedent();

				}

				p.setCorrespondance(meilleurEtatFinalCorrespondance.getEtatPrecedent().getActuel().getArret());
				return meilleurEtatFinalCorrespondance.getEtatPrecedent().getActuel().getTrain();
			}
		}
		return null;
	}
}


