package objets;

import comparaison.Analyse;


/* Classe Gabarit
 * ---------------
 * Role : Cree un nouveau gabarit
 *         Un gabarit est un "modele" utilise dans les comparaisons futures
 * --------------
 */


public class Gabarit {
	// Constante : nb de signatures requis pour un nouveau gabarit (doit etre superieur a 2)
	final static int nbSign = 10;
	
	// Tableau de DonneesPoint caracteristique d'une signature
	public Signature sRef;
	public Signature[] tab = new Signature[nbSign];
	

	
	public Gabarit() {
		DonneesPoint[] temp;
		
		// Enregistrement de nbSign signatures, la moyenne sera le gabarit
		// Grace au reparsing, chacune des signatures a le mm nombre de points
		for (int i=0; i<nbSign; i++) 
			tab[i]=new Signature();
		
		//Initialise le tableau temp
		temp = new DonneesPoint[tab[0].getDonnees().length];
		for (int i=0; i<temp.length; i++)
			temp[i] =  new DonneesPoint(0,0,0,0,0,0);
		
		//Remplis le tableau temp (moyenne des dix signatures)
		for (int i=0; i<temp.length; i++) 
			for (int j=0; j<nbSign; j++)
				temp[i] = temp[i].add(tab[j].getDonnees()[i], nbSign);
		
		
		//Modifie les differentes signatures saisies en les rapprochant par similitudes a la signature moyenne
		for (int i=0; i<nbSign; i++)  {
			//On evite de modifier sRef en la coupant; on passe donc par s;
			Signature s = new Signature(temp);
			tab[i] = new Signature(Analyse.similitudes(s, tab[i])[1]);
		}
		// On redefinit la signature reference a partir des signatures saisies et modifiees
		for (int i=0; i<temp.length; i++)
			temp[i] =  new DonneesPoint(0,0,0,0,0,0);
		for (int i=0; i<temp.length; i++) 
			for (int j=0; j<nbSign; j++)
				temp[i] = temp[i].add(tab[j].getDonnees()[i], nbSign);
		
		//Exclut les 2 (deux) pires signatures 
		//TODO inclure la minimisation ?? don't think so
		double min = 1;
		int marqueur = 0;
		
		sRef = new Signature(temp);
		for (int w=0; w<2; w++) {
			for (int j=0; j<nbSign; j++) {
				if (tab[j]!= null && Analyse.scorePositions(tab[j], sRef) < min) {
					min = Analyse.scorePositions(tab[j], sRef);
					marqueur = j;
				}
			}
			tab[marqueur] = null;
			min = 1;
		}
		
		//Remplis le tableau donnees avec les huit signatures differentes
		for (int i=0; i<temp.length; i++)
			temp[i] =  new DonneesPoint(0,0,0,0,0,0);
		
		for (int i=0; i<temp.length; i++) {
			for (int j=0; j<nbSign; j++) {
				if (tab[j] != null)
					temp[i] = temp[i].add(tab[j].getDonnees()[i], nbSign - 2);	
		}}
		
		sRef = new Signature (temp);
	}
}
