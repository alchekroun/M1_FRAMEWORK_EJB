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
	
	class SortHeureTrajet implements Comparator<HeureDePassage> { 
		public int compare(HeureDePassage hdp1, HeureDePassage hdp2) { 
			Long diff = ChronoUnit.SECONDS.between(hdp1.getReelArriveeTemps(),hdp2.getReelArriveeTemps());
			if(diff>0) {
				return 1;
			}
			else {
				return -1;
			}
		}
	}
	

    

			
	
	
	public Train findTrajet(int passagerId) {
		
		class Etat{
			private HeureDePassage actuel;
			private HeureDePassage precedent=null;
			private Etat etatPrecedent = null;
			
			public Etat(HeureDePassage actuel, HeureDePassage precedent, Etat etat) {
				this.actuel=actuel;
				this.precedent=precedent;
				this.etatPrecedent=etat;
			}
			
			public Etat(HeureDePassage actuel) {
				this.actuel=actuel;
			}
			
			public HeureDePassage getActuel() {
				return this.actuel;
			}

			public void setActuel(HeureDePassage actuel) {
				this.actuel = actuel;
			}

			public HeureDePassage getPrecedent() {
				return this.precedent;
			}

			public void setPrecedent(HeureDePassage precedent) {
				this.precedent = precedent;
			}
			
			public Etat getEtatPrecedent() {
				return this.etatPrecedent;
			}
		}
		
		
		
		Passager p = em.find(Passager.class, passagerId);
		//List<Arret> trajetPlusRapide;
		//List<Train> listeTrainEtatChemin;
		List<Train> listeTrainByArretDepartPassager;
		List<HeureDePassage> listeHdpAfterDepartByTrain;
		List<HeureDePassage> listeHdpAfterDepartArretEnCoursByTrain;
		//List<Train> listeTrainDirectPossible;
		//List<Train> listeTrainPossible;
		
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
		
		
		Arret arretDepart = p.getDepart();
		LocalDateTime dateNow = LocalDateTime.now();
		listeTrainByArretDepartPassager = trainDAO.findTrainByArretAndDepartAfterDate(arretDepart.getId(),dateNow);
		System.out.println( "Train depart: "+listeTrainByArretDepartPassager.get(0).getId() );
		System.out.println( "Train nom depart: "+listeTrainByArretDepartPassager.get(0).getNom() );
		for(fr.pantheonsorbonne.ufr27.miage.jpa.Train train:listeTrainByArretDepartPassager) {
			// on ajoute hdp de arret de depart
			//HeureDePassage hdpDepart = hdpDAO.getHdpFromTrainIdAndArretId(train.getId(),arretDepart.getId());
			List<HeureDePassage> listHdpDepart = hdpDAO.findHeureByDepartAfterDateAndTrainIdAndArretIdAndSorted(train.getId(),arretDepart.getId(),dateNow);
			HeureDePassage hdpDepart = null;
			if(!listHdpDepart.isEmpty()) {
				hdpDepart = listHdpDepart.get(0);
			}
			Etat depart = new Etat(hdpDepart);
			//listeEtatPossible.add(depart);
			System.out.println("****::::SIZE"+train.getListeHeureDePassage().size());
			//utiliser DAO findHdpByTrainid
			//récup les hdp des arrets parcouru par les trains juste après le départ de l'arrêt de départ du passager
			listeHdpAfterDepartByTrain = hdpDAO.findHdpByTrainAfterDateAndSorted(train.getId(),dateNow);
			System.out.println("****::::SIZE"+listeHdpAfterDepartByTrain.size());
			//for(fr.pantheonsorbonne.ufr27.miage.jpa.HeureDePassage hdp:train.getListeHeureDePassage()) {
			
			/*for(fr.pantheonsorbonne.ufr27.miage.jpa.HeureDePassage hdp:listeHdpAfterDepartByTrain) {
				System.out.println("Heure passage: TrainID: "+hdp.getTrain().getId() +" ArretID: "+hdp.getArret().getId());
				System.out.println(hdp.getArret() +" "+ p.getDepart() + " ID VERSIONS "+hdp.getArret() +" "+ p.getDepart());
				//cas possible quand depart 
				// voir aussi si hdp est apres depart
				if(hdp.getArret() != p.getDepart()) {
					listeEtatPossible.add(new Etat(hdp,hdpDepart, depart));
					if(hdp.getArret().getId() == p.getArrive().getId()) {
						//LocalDateTime hdpTmp = hdp.getReelArriveeTemps();
						listeEtatDirectPossible.add(new Etat(hdp,hdpDepart, depart));
					}
				}
			}*/
			HeureDePassage hdpSuivante=null;
			boolean dernierArret = false;
			if(!listeHdpAfterDepartByTrain.isEmpty()) {
				hdpSuivante = listeHdpAfterDepartByTrain.get(0);
				//if(hdpSuivante.getArret() != p.getDepart()) {
						listeEtatPossible.add(new Etat(hdpSuivante, depart.getActuel() ,depart));
						//on ajoute à la liste des calculs pour les correspondances pour la suite de l'algo si pas de chemin direct trouve
						listeEtatCalculCorrespondance.add(new Etat(hdpSuivante, depart.getActuel() ,depart));
						if(hdpSuivante.getArret().getId() == p.getArrive().getId()) {
							listeEtatDirectPossible.add(new Etat(hdpSuivante, depart.getActuel() ,depart));
							dernierArret=true;
						}
				//}
			}
			
			boolean tousLesCheminsParcourus=false;
			//int tailleListePrecedente=listeEtatPossible.size();
			while(((!listeEtatPossible.isEmpty()) && (!tousLesCheminsParcourus)) && (!dernierArret)) {
				//tailleListePrecedente=listeEtatPossible.size();
				Etat etatEnCours = listeEtatPossible.pollFirst();
				//on veut recup la liste des hdp du train partant de l'arret et on garde la plus recente par rapport à l'heure d'arrivee
				/*List<HeureDePassage> listHdpDepartArretEnCours = hdpDAO.findHeureByDepartAfterDateAndTrainIdAndArretIdAndSorted(etatEnCours.getActuel().getTrain().getId(),etatEnCours.getActuel().getArret().getId(),etatEnCours.getActuel().getReelArriveeTemps());
				HeureDePassage hdpDepartArretEnCours = null;
				if(!listHdpDepartArretEnCours.isEmpty()) {
					hdpDepartArretEnCours = listHdpDepartArretEnCours.get(0);
				}
				*/
				//récup les hdp des arrets parcouru par le train juste après le départ de l'arrêt en cours
				listeHdpAfterDepartArretEnCoursByTrain = hdpDAO.findHdpByTrainAfterDateAndSorted(etatEnCours.getActuel().getTrain().getId(),etatEnCours.getActuel().getReelArriveeTemps());
				HeureDePassage hdpSuivanteFromArretEnCours=null;
				if(!listeHdpAfterDepartArretEnCoursByTrain.isEmpty()) {
					hdpSuivanteFromArretEnCours = listeHdpAfterDepartArretEnCoursByTrain.get(0);
					if(hdpSuivanteFromArretEnCours.getArret() != p.getDepart()) {
						listeEtatPossible.add(new Etat(hdpSuivanteFromArretEnCours, etatEnCours.getActuel() ,etatEnCours));
						if(hdpSuivanteFromArretEnCours.getArret().getId() == p.getArrive().getId()) {
							listeEtatDirectPossible.add(new Etat(hdpSuivanteFromArretEnCours, etatEnCours.getActuel() ,etatEnCours));
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
			// faire la boucle for ici pour developper le train en cours et tout son chemin et ne pas mélanger ses etats avec ceux des autres train qui partent du départ aussi
			//et conserver dans la liste final letat final si on arrive au bout
			
			
 
		}
		//cas ou il existe un train qui emmene directement a destination sans correspondance
		if(!listeEtatDirectPossible.isEmpty()) {
			//on peut lui faire un setTrain() ?
			return listeEtatDirectPossible.pollFirst().getActuel().getTrain();
		}
		
		//listEtatPossibleDejaVisite
		boolean notDoneCalculatingEveryPath = true;
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

		if(!listeEtatsFinaux.isEmpty()) {
			boolean cheminFinalTrouve=false;
			Etat meilleurEtatFinalCorrespondance = listeEtatsFinaux.pollFirst();
			int cpt=0;
			while(!listeEtatsFinaux.isEmpty() && !cheminFinalTrouve) {
				if(cpt>0) {
					meilleurEtatFinalCorrespondance = listeEtatsFinaux.pollFirst();
				}
				cpt++;
				// idee de creer une class Etat qui permet de recup heure de passage actuelle et precedente
				
				
				//LocalDateTime dateDepartArretPrecedent = meilleurEtatFinalCorrespondance.getEtatPrecedent().getActuel().getReelDepartTemps();
				//LocalDateTime dateArriveeArretActuelForTrainPrecedent = hdpDAO.getHdpFromTrainIdAndArretIdAndBetweenDate1AndDate2(meilleurEtatFinalCorrespondance.getEtatPrecedent().getActuel().getTrain().getId(), meilleurEtatFinalCorrespondance.getActuel().getArret().getId(),meilleurEtatFinalCorrespondance.getEtatPrecedent().getActuel().getReelDepartTemps(),meilleurEtatFinalCorrespondance.getActuel().getReelDepartTemps()).get(0).getReelArriveeTemps();
				
				//LocalDateTime dateArriveeArretActuelForTrainPrecedent = hdpDAO.getHdpFromTrainIdAndArretId(meilleurEtatFinalCorrespondance.getEtatPrecedent().getActuel().getTrain().getId(), meilleurEtatFinalCorrespondance.getActuel().getArret().getId()).getReelArriveeTemps();
				// VOIR SI ON A BIEN LES HDP SUPPRIME AU COURS DU TEMPS
				while(meilleurEtatFinalCorrespondance.getEtatPrecedent().getEtatPrecedent()!=null && !hdpDAO.getHdpFromTrainIdAndArretIdAndBetweenDate1AndDate2(meilleurEtatFinalCorrespondance.getEtatPrecedent().getActuel().getTrain().getId(), meilleurEtatFinalCorrespondance.getActuel().getArret().getId(),meilleurEtatFinalCorrespondance.getEtatPrecedent().getActuel().getReelDepartTemps(),meilleurEtatFinalCorrespondance.getActuel().getReelDepartTemps()).isEmpty()) {//compareTo(dateArriveeArretActuelForTrainPrecedent)}//(dateDepartArretPrecedent+(dateArriveeArretActuel-dateDepartArretPrecedent)<)) {
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
		}
		
		return null;
		
		
		
	}


	
}
