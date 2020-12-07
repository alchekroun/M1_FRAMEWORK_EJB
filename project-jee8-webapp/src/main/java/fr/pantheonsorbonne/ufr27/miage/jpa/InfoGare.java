package fr.pantheonsorbonne.ufr27.miage.jpa;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import fr.pantheonsorbonne.ufr27.miage.model.jaxb.Arret;

@Entity
@NamedQueries({ @NamedQuery(name = "getAllInfoGare", query = "SELECT i FROM InfoGare i"),
	@NamedQuery(name = "deleteInfoGare", query = "DELETE FROM InfoGare i WHERE i.id = :id") })
public class InfoGare {
	
		@Id
		@GeneratedValue(strategy = GenerationType.AUTO)
		protected int id;
	   
	    protected Arret localisation;

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public Arret getLocalisation() {
			return localisation;
		}

		public void setLocalisation(Arret localisation) {
			this.localisation = localisation;
		}
}
