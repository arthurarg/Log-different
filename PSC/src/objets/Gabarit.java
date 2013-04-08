package objets;

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
	public Signature[] tab = new Signature[nbSign];
	

	
	public Gabarit() {
		// Enregistrement de nbSign signatures
		// Grace au reparsing, chacune des signatures a le mm nombre de points
		for (int i=0; i<nbSign; i++) 
			tab[i]=new Signature();
		
		initGabarit();
	}
	
	public Gabarit(Signature[] tab) {
		if (tab.length==nbSign) {
			this.tab = tab;
			initGabarit();
		}
		else
			System.out.println("Tableau envoyé au constructeur non correct");
			
	}
	
	
	public void initGabarit() {
		//Tableau temp du futur gabarit
		DonneesPoint[] temp;
		temp = new DonneesPoint[this.tab[0].getDonnees().length];
		
		int marq = this.tab.length;
		do {
			//Le cas échéant, on modifie une signature qui ne vas pas avec le gabarit
			if (marq>=0 && marq<this.tab.length) 
				this.tab[marq] = new Signature();
			
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
		} while ((marq = validationGabarit()) != -1);
	}
	
	public int validationGabarit() {
		int marqueur = -1;
		double scoreMin = 1;
		for (int w=0; w<this.tab.length; w++) {
			if (!Comparaison.comparer(this.sRef,this.tab[w]) && Comparaison.mesureEcart(this.sRef, this.tab[w]) < scoreMin) {
				marqueur = w;
				scoreMin = Comparaison.mesureEcart(this.sRef, this.tab[w]);		
			}
		}
		if (marqueur != -1) {
		System.out.println("Echec de la validation pour la signature n°" + (marqueur+1));
		System.out.println(Comparaison.enumProblemes(this.sRef, this.tab[marqueur])); 
		}
		return marqueur;
	}
}
