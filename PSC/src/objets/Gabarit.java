package objets;

import objets.Signature.Complexite;

import comparaison.Analyse;
import comparaison.Comparaison;


/* Classe Gabarit
 * ---------------
 * Role : Cree un nouveau gabarit
 *         Un gabarit est un "modele" utilise dans les comparaisons futures
 * --------------
 */


public class Gabarit {
	// Constante : nb de signatures requis pour un nouveau gabarit (doit etre superieur a 2)
	public final static int nbSign = 10;
	
	// Tableau de DonneesPoint caracteristique d'une signature
	public Signature sRef;
	public Signature[] tab;	
	public Signature s;

	//Tableau d'etat, utilise pour l'interface graphique
	// Gris : 0, Orange : 1, Vert : 2, Rouge : -1
	public int[] etat;
	
	
	public Gabarit() {
		s = new Signature (false);
		this.tab = new Signature[nbSign];
		this.etat = new int[nbSign];
		init();
	}
	
	public Gabarit(boolean b) {
		s = new Signature (false);
		this.tab = new Signature[nbSign];
		this.etat = new int[nbSign];
		if (b)
			init();			
	}
	
	public Gabarit(Signature[] tab) {
		if (tab.length==nbSign) {
			this.tab = tab;
			constructGabarit();
		}
		else
			System.err.println("Tableau envoyé au constructeur non correct");
			
	}
	
	public void init() {
		for (int i=0; i<nbSign; i++) {
			
			s = new Signature(false);
			s.init();
			
			if (s.terminate)
				return;
					
			tab[i] = new Signature(s.getDonnees());
			etat[i] = 1;
		}
		constructGabarit();
	}
	
	private void constructGabarit() {
		//Tableau temp du futur gabarit
		DonneesPoint[] temp;
		temp = new DonneesPoint[this.tab[0].getDonnees().length];
		
		int marq = this.tab.length;
		int essai = 0;
		do {
			//Le cas échéant, on modifie une signature qui ne vas pas avec le gabarit
			if (marq>=0 && marq<this.tab.length) {
				s = new Signature(false);
				s.init();
				if (s.terminate)
					return;
				
				this.tab[marq] = new Signature(s.getDonnees());
				for(int i=0; i<tab.length;i++){
					etat[i] = 1;
				}
				
			}

			
			//Initialisation
			for (int i=0; i<temp.length; i++)
				temp[i] =  new DonneesPoint(0,0,0,0,0,0);
			
			//Remplis le tableau temp (moyenne des dix signatures)
			for (int i=0; i<temp.length; i++) 
				for (int j=0; j<nbSign; j++)
					temp[i] = temp[i].add(this.tab[j].getDonnees()[i], nbSign);
			//1er jet de sRef
			sRef = new Signature(temp);
			
			
			//Modifie les differentes signatures saisies en les rapprochant par similitudes a la signature moyenne
			for (int i=0; i<nbSign; i++) 
				tab[i] = new Signature(Analyse.similitudes(sRef, this.tab[i])[1]);
	
			
			
			// On redefinit la signature reference a partir des signatures saisies et modifiees
			for (int i=0; i<temp.length; i++)
				temp[i] =  new DonneesPoint(0,0,0,0,0,0);
			for (int i=0; i<temp.length; i++) 
				for (int j=0; j<nbSign; j++)
					temp[i] = temp[i].add(this.tab[j].getDonnees()[i], nbSign);
			
			//2eme jet
			sRef = new Signature (temp);
			essai++;
		} while ((marq = validationGabarit()) != -1 || essai > 5);
		
		if (marq!= -1) {
			this.sRef = null;
			System.out.println("Echec de l'enregistrement du gabarit : signatures trop diffèrentes");
		}		
	}
	
	public int validationGabarit() {
		int marqueur = -1;
		double scoreMin = 1;
		for (int w=0; w<this.tab.length; w++) {
			if (!Comparaison.comparer(this.sRef,this.tab[w])) {
				etat[w] = -1;
				if (Comparaison.mesureEcart(this.sRef, this.tab[w]) < scoreMin) {
					marqueur = w;
					scoreMin = Comparaison.mesureEcart(this.sRef, this.tab[w]);	
				}
			}
			else
				etat[w] = 2;
		}
		if (marqueur != -1) {
		System.out.println("Echec de la validation pour la signature n°" + (marqueur+1));
		System.out.println(Comparaison.enumProblemes(this.sRef, this.tab[marqueur])); 
		}
		return marqueur;
	}
}
