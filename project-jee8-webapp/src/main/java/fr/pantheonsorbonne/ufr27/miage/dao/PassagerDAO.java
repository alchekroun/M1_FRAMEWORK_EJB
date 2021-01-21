package fr.pantheonsorbonne.ufr27.miage.dao;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import fr.pantheonsorbonne.ufr27.miage.jpa.Passager;
import fr.pantheonsorbonne.ufr27.miage.jpa.Train;
import fr.pantheonsorbonne.ufr27.miage.jpa.Arret;
import fr.pantheonsorbonne.ufr27.miage.jpa.HeureDePassage;
import fr.pantheonsorbonne.ufr27.miage.jpa.HeureDePassageKey;

public class PassagerDAO {
	@Inject
	EntityManager em;

	@Inject
	TrainDAO trainDAO;
	
	@Inject
	HeureDePassageDAO hdpDAO;

	public Passager getPassagerFromId(int passagerId) {
		return em.find(Passager.class, passagerId);
	}

	public Passager updatePassager(Passager passagerOriginal,
			fr.pantheonsorbonne.ufr27.miage.model.jaxb.Passager passagerUpdate) {

		passagerOriginal.setArrive(em.find(Arret.class, passagerUpdate.getArrive().getId()));
		passagerOriginal.setDepart(em.find(Arret.class, passagerUpdate.getDepart().getId()));
		passagerOriginal.setNom(passagerUpdate.getNom());

		return passagerOriginal;
	}

	public void deletePassager(Passager passager) {
		if (passager.getTrain() != null) {
			trainDAO.removePassager(passager.getTrain(), passager);
		}
		em.remove(passager);
	}

	public List<Passager> getAllPassager() {
		return em.createNamedQuery("getAllPassager").getResultList();
	}

	public List<Passager> getAllPassagerByTrain(int trainId) {
		return em.createNamedQuery("findAllPassagerByTrain").setParameter("trainId", trainId).getResultList();
	}

	public List<Passager> getAllPassagerByDepart(int arretId) {
		return em.createNamedQuery("findPassagerByDepart").setParameter("idArretDepart", arretId).getResultList();
	}

	public List<Passager> getAllPassagerByArrivee(int arretId) {
		return em.createNamedQuery("findPassagerByArrivee").setParameter("idArretArrivee", arretId).getResultList();
	}

	public boolean isPassagerCreated(int passagerId) {

		Passager p = em.find(Passager.class, passagerId);
		if (p == null) {
			throw new NoSuchElementException("No passager");
		}
		return p.isCreated();

	}
	
	public List<Passager> findPassagerByCorrespondance(int arretId){
		return em.createNamedQuery("findPassagerByCorrespondance").setParameter("arretId", arretId).getResultList();
	}
	
	public List<Passager> getPassagerByTrainIdAndNotArrivalAtArretId(int trainId, int arretId){
		return em.createNamedQuery("getPassagerByTrainIdAndNotArrivalAtArretId").setParameter("trainId", trainId).setParameter("arretId", arretId).getResultList();
	}
	
	
	public Train findTrajet(int passagerId) {
		
		class Etat{
			private HeureDePassage actuel;
			private HeureDePassage precedent=null;
			private int nombresChangements=0;
			private Etat etatPrecedent = null;
			
			public Etat(HeureDePassage actuel, HeureDePassage precedent, Etat etat, int nombreChangements) {
				this.actuel=actuel;
				this.precedent=precedent;
				this.etatPrecedent=etat;
				this.nombresChangements=nombreChangements;
			}
			
			public Etat(HeureDePassage actuel) {
				this.actuel=actuel;
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
				if(et1== null || et2 == null) {
					return 0;
				}
				return et1.getActuel().getReelArriveeTemps().compareTo(et2.getActuel().getReelArriveeTemps());
			}
		});
		
		
		TreeSet<Etat> listeEtatDirectPossible = new TreeSet<>(new Comparator<Etat>() {
			@Override
			public int compare(Etat et1, Etat et2) {
				if(et1== null || et2 == null) {
					return 0;
				}
				return et1.getActuel().getReelArriveeTemps().compareTo(et2.getActuel().getReelArriveeTemps());
			}
		});
		
		TreeSet<Etat> listeEtatCalculCorrespondance = new TreeSet<>(new Comparator<Etat>() {
			@Override
			public int compare(Etat et1, Etat et2) {
				if(et1== null || et2 == null) {
					return 0;
				}
				return et1.getActuel().getReelArriveeTemps().compareTo(et2.getActuel().getReelArriveeTemps());
			}
		});
		
		
		TreeSet<Etat> listeEtatsFinaux = new TreeSet<>(new Comparator<Etat>() {
			@Override
			public int compare(Etat et1, Etat et2) {
				if(et1== null || et2 == null) {
					return 0;
				}
				return et1.getActuel().getReelArriveeTemps().compareTo(et2.getActuel().getReelArriveeTemps());
			}
		});
		
		TreeSet<Etat> listeEtatDejaVisite = new TreeSet<>(new Comparator<Etat>() {
			@Override
			public int compare(Etat et1, Etat et2) {
				if(et1== null || et2 == null) {
					return 0;
				}
				return et1.getActuel().getReelArriveeTemps().compareTo(et2.getActuel().getReelArriveeTemps());
			}
		});
		
		if(p.getDepart().getId()!=p.getArrive().getId()) {
			Arret arretDepart = p.getDepart();
			LocalDateTime dateNow = LocalDateTime.now();
			listeTrainByArretDepartPassager = trainDAO.findTrainByArretAndDepartAfterDateAndDesservi(arretDepart.getId(),dateNow);
			for(fr.pantheonsorbonne.ufr27.miage.jpa.Train train:listeTrainByArretDepartPassager) {
				List<HeureDePassage> listHdpDepart = hdpDAO.findHeureByDepartAfterDateAndTrainIdAndArretIdAndSortedAndDesservi(train.getId(),arretDepart.getId(),dateNow);
				HeureDePassage hdpDepart = null;
				if(!listHdpDepart.isEmpty()) {
					hdpDepart = listHdpDepart.get(0);
					listeHdpAfterDepartByTrain = hdpDAO.findHdpByTrainAfterDateAndSortedAndDesservi(train.getId(),hdpDepart.getReelDepartTemps());
				}
				Etat depart = new Etat(hdpDepart);
				HeureDePassage hdpSuivante=null;
				boolean dernierArret = false;
				if(!listeHdpAfterDepartByTrain.isEmpty()) {
					hdpSuivante = listeHdpAfterDepartByTrain.get(0);
							//on ajoute à la liste des calculs pour les correspondances pour la suite de l'algo si pas de chemin direct trouve
							if(hdpSuivante.getTrain().getId()==depart.getActuel().getTrain().getId()) {
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
				
				boolean tousLesCheminsParcourus=false;
				while(((!listeEtatPossible.isEmpty()) && (!tousLesCheminsParcourus)) && (!dernierArret)) {
					Etat etatEnCours = listeEtatPossible.pollFirst();
					//récup les hdp des arrets parcouru par le train juste après le départ de l'arrêt en cours
					//getReelArriveeTemps to getReelDepartTemps??
					listeHdpAfterDepartArretEnCoursByTrain = hdpDAO.findHdpByTrainAfterDateAndSortedAndDesservi(etatEnCours.getActuel().getTrain().getId(),etatEnCours.getActuel().getReelDepartTemps());
					HeureDePassage hdpSuivanteFromArretEnCours=null;
					if(!listeHdpAfterDepartArretEnCoursByTrain.isEmpty()) {
						hdpSuivanteFromArretEnCours = listeHdpAfterDepartArretEnCoursByTrain.get(0);
						if(hdpSuivanteFromArretEnCours.getArret() != p.getDepart()) {
							listeEtatPossible.add(new Etat(hdpSuivanteFromArretEnCours, etatEnCours.getActuel() ,etatEnCours, etatEnCours.getNombreChangements()));
							if(hdpSuivanteFromArretEnCours.getArret().getId() == p.getArrive().getId()) {
								listeEtatDirectPossible.add(new Etat(hdpSuivanteFromArretEnCours, etatEnCours.getActuel() ,etatEnCours, etatEnCours.getNombreChangements()));
								dernierArret=true;
							}
						}
						else {
							tousLesCheminsParcourus=true;
						}
					}
					else {
						tousLesCheminsParcourus=true;
					}
				}
				
				
				listeHdpAfterDepartByTrain.clear();
			}
			//cas ou il existe un train qui emmene directement a destination sans correspondance
			if(!listeEtatDirectPossible.isEmpty()) {
				return listeEtatDirectPossible.pollFirst().getActuel().getTrain();
			}
			
			while((!listeEtatCalculCorrespondance.isEmpty())) {
				TreeSet<Etat> listeEtatDejaVisitePrecedente = new TreeSet<>(listeEtatDejaVisite);
				int size = listeEtatCalculCorrespondance.size();
				Etat etatEnCours = listeEtatCalculCorrespondance.pollFirst();
				//je recup liste des trains de l'arret en cours qui partent après larrivee du train provenant de larret precedent
				listeTrainByArretDepartPassager = trainDAO.findTrainByArretAndDepartAfterDateAndDesservi(etatEnCours.getActuel().getArret().getId(),etatEnCours.getActuel().getReelArriveeTemps());
				for(fr.pantheonsorbonne.ufr27.miage.jpa.Train train:listeTrainByArretDepartPassager) {
					//liste des hdp des(du) train(s) qui minteresse pour larret en cours cad que le train en question doit partir de larret apres que le train qui vient de larret precdent soit arrive
					List<HeureDePassage> listHdpDepart = hdpDAO.findHeureByDepartAfterDateAndTrainIdAndArretIdAndSortedAndDesservi(train.getId(),etatEnCours.getActuel().getArret().getId(),etatEnCours.getActuel().getReelArriveeTemps());
					HeureDePassage hdpDepart = null;
					if(!listHdpDepart.isEmpty()) {
						hdpDepart = listHdpDepart.get(0);
						//je veux recup hdp heure darrivee la plus tot du prochain arret et qui soit le plus tot par rapport à mon heure de depart 
						listeHdpAfterDepartArretEnCoursByTrain = hdpDAO.findHdpByTrainAfterDateAndSortedAndDesservi(train.getId(),hdpDepart.getReelDepartTemps());
						HeureDePassage hdpSuivanteFromArretEnCours=null;
						if(!listeHdpAfterDepartArretEnCoursByTrain.isEmpty()) {
							hdpSuivanteFromArretEnCours = listeHdpAfterDepartArretEnCoursByTrain.get(0);
							Etat newEtat = null;
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
			
	
			if(!listeEtatsFinaux.isEmpty()) {
				Etat meilleurEtatFinalCorrespondance = listeEtatsFinaux.pollFirst();
				while(meilleurEtatFinalCorrespondance.getEtatPrecedent().getEtatPrecedent()!=null ){
					if(meilleurEtatFinalCorrespondance.getEtatPrecedent().getNombreChangements()==0 && meilleurEtatFinalCorrespondance.getNombreChangements()==1) {
						break;
					}	
					meilleurEtatFinalCorrespondance = meilleurEtatFinalCorrespondance.getEtatPrecedent();
					
				}
				
				p.setCorrespondance(meilleurEtatFinalCorrespondance.getEtatPrecedent().getActuel().getArret());
				return meilleurEtatFinalCorrespondance.getEtatPrecedent().getActuel().getTrain();
				//p.setCorrespondance(meilleurEtatFinalCorrespondance.getActuel().getArret());
				//return meilleurEtatFinalCorrespondance.getActuel().getTrain();
			}
		}
		return null;
	}
}
	
////////////////////////////	
		/*	boolean cheminFinalTrouve=false;
		  if(!listeEtatsFinaux.isEmpty()) {
			int taille=listeEtatsFinaux.size();
			Etat meilleurEtatFinalCorrespondance = listeEtatsFinaux.pollFirst();
			taille--;

			int cpt=0;
			while(taille!=0 && !cheminFinalTrouve) {
				if(cpt>0) {
					meilleurEtatFinalCorrespondance = listeEtatsFinaux.pollFirst();
					taille--;
				}
				cpt++;
		*/
				// idee de creer une class Etat qui permet de recup heure de passage actuelle et precedente
				
				
				//LocalDateTime dateDepartArretPrecedent = meilleurEtatFinalCorrespondance.getEtatPrecedent().getActuel().getReelDepartTemps();
				//LocalDateTime dateArriveeArretActuelForTrainPrecedent = hdpDAO.getHdpFromTrainIdAndArretIdAndBetweenDate1AndDate2(meilleurEtatFinalCorrespondance.getEtatPrecedent().getActuel().getTrain().getId(), meilleurEtatFinalCorrespondance.getActuel().getArret().getId(),meilleurEtatFinalCorrespondance.getEtatPrecedent().getActuel().getReelDepartTemps(),meilleurEtatFinalCorrespondance.getActuel().getReelDepartTemps()).get(0).getReelArriveeTemps();
				
				//LocalDateTime dateArriveeArretActuelForTrainPrecedent = hdpDAO.getHdpFromTrainIdAndArretId(meilleurEtatFinalCorrespondance.getEtatPrecedent().getActuel().getTrain().getId(), meilleurEtatFinalCorrespondance.getActuel().getArret().getId()).getReelArriveeTemps();
				// VOIR SI ON A BIEN LES HDP SUPPRIME AU COURS DU TEMPS
			/*	while(meilleurEtatFinalCorrespondance.getEtatPrecedent().getEtatPrecedent()!=null && !hdpDAO.getHdpFromTrainIdAndArretIdAndBetweenDate1AndDate2(meilleurEtatFinalCorrespondance.getEtatPrecedent().getActuel().getTrain().getId(), meilleurEtatFinalCorrespondance.getActuel().getArret().getId(),meilleurEtatFinalCorrespondance.getEtatPrecedent().getActuel().getReelDepartTemps(),meilleurEtatFinalCorrespondance.getActuel().getReelDepartTemps()).isEmpty()) {//compareTo(dateArriveeArretActuelForTrainPrecedent)}//(dateDepartArretPrecedent+(dateArriveeArretActuel-dateDepartArretPrecedent)<)) {
					meilleurEtatFinalCorrespondance = meilleurEtatFinalCorrespondance.getEtatPrecedent();
				}
				
				if((!(meilleurEtatFinalCorrespondance.getEtatPrecedent().getEtatPrecedent()!=null)) && !hdpDAO.getHdpFromTrainIdAndArretIdAndBetweenDate1AndDate2(meilleurEtatFinalCorrespondance.getEtatPrecedent().getActuel().getTrain().getId(), meilleurEtatFinalCorrespondance.getActuel().getArret().getId(),meilleurEtatFinalCorrespondance.getEtatPrecedent().getActuel().getReelDepartTemps(),meilleurEtatFinalCorrespondance.getActuel().getReelDepartTemps()).isEmpty()) {
					cheminFinalTrouve=true;
				}
				//ICI SET CORRESPONDANCE
				
			}
			if(cheminFinalTrouve) {
				p.setCorrespondance(meilleurEtatFinalCorrespondance.getActuel().getArret());
			}
			return meilleurEtatFinalCorrespondance.getActuel().getTrain();
		}*/
		
//		return null;
//		
//		
//		
//	}


	
	//
	//listEtatPossibleDejaVisite
//////////////////////////////////
/*		boolean notDoneCalculatingEveryPath = true;
	//(!listeEtatPossible.isEmpty()) || notDoneCalculatingEveryPath
	while((!listeEtatPossible.isEmpty()) || notDoneCalculatingEveryPath) {
		notDoneCalculatingEveryPath = true;
		TreeSet<Etat> listeEtatPossibleCopy = new TreeSet<>(listeEtatPossible);
		TreeSet<Etat> listeEtatDejaVisitePrecedente = new TreeSet<>(listeEtatDejaVisite);
		int size = listeEtatPossible.size();
		//direct use listeEtatPossible
		//copy because can't poll() while iterating
		for(Etat etat : listeEtatPossibleCopy) {
			for(fr.pantheonsorbonne.ufr27.miage.jpa.HeureDePassage h:etat.getActuel().getArret().getListeHeureDePassage()) {
				
				System.out.println("ETAT EN COURS , arret: "+etat.getActuel().getArret().getNom() +" /  "+etat.getActuel().getArret().getId()+" //// Train: "+etat.getActuel().getTrain().getNom() +" /  "+etat.getActuel().getTrain().getId()+" //// HeureDePassage: "+etat.getActuel().getId()+" /  "+ etat.getActuel().getReelArriveeTemps());
				System.out.println("Size LISTE hdp pour ETAT: "+etat.getActuel().getArret().getListeHeureDePassage().size());

				//for
				
				// le pb peut etre que je set la meme hdp pour actuel et precedent
				
				Etat newEtat = new Etat(h,etat.getActuel(),etat);
				
				//verifie si deja dans la liste des deja visite
				//check si pas meme actuel et precedent sinon pas creer
				boolean contientEtat=false;
				if(!listeEtatDejaVisite.isEmpty()) {
					for(Etat e : listeEtatDejaVisite) { //listeEtatPossible
						//a voir legalite
						System.out.println("TEST contientEtat "+e.getActuel().getId() +" == "+ newEtat.getActuel().getId() +" //// "+e.getPrecedent().getId() +" == "+newEtat.getPrecedent().getId());
						System.out.println("premiere condition: "+ e.getActuel().getId() == newEtat.getActuel().getId()+" ");
						System.out.println("deuxieme condition: "+e.getPrecedent().getId() == newEtat.getPrecedent().getId()+" ");
						System.out.println("boolean valeur: "+contientEtat);
						if(e.getActuel().getId() == newEtat.getActuel().getId() && e.getPrecedent().getId() == newEtat.getPrecedent().getId()) {
							contientEtat=true;
						}
					}
				}
				
				if(!contientEtat) { //h.getId()!=hdp.getId() && 
					listeEtatPossible.add(newEtat);
					listeEtatDejaVisite.add(newEtat);
					if(h.getArret().getId() == p.getArrive().getId()) {
						listeEtatsFinaux.add(newEtat);
					}
				}
				// ne pas faire remove qd contient deja letat
				// je rajoute un if pour supprimer que qd contient pas
				listeEtatPossible.remove(etat);
			}
		}
		if(listeEtatPossible.size() == size && listeEtatDejaVisite.containsAll(listeEtatDejaVisitePrecedente)) {
			notDoneCalculatingEveryPath=false;
		}
	//listeTrainPossiblePrecedente.clear();
	}
	
	// on récupère les heures de passage quand on revient en arriere et on fait ordre decroissant en récupérant la liste des heures de passage pour un train avec un findHdpByTrain
*/
///////////////	
	
	
	
	
	
	

