package comparaison;

import objets.Signature;


/* Classe Analyse
 * ---------------
 * Role : Regroupe les méthodes de comparaison, qui font appel aux fonctions auxilaires de la classe Analyse
 * --------------
 */


public class Comparaison {

	//Definition de seuils decomparaisons
	//TODO Definir seuils et méthode dans la classe Comparaison
	public static final double angleSeuil = Math.PI/2;
	public static final double ecartRelatifVitesseMoyenne = 0.50;
	public static final double ScorePositionsSeuil = 0.9;
	public static final double ScoreVitessesSeuil = 0.8;
	public static final double ScorePressionsSeuil = 0.8;
	
	
	
	
	// Méthode 1: Compare le score des positions au seuil defini
	public static boolean comparePositions (Signature sTest, Signature sRef) {
		if (Analyse.scorePositions (sTest,sRef) < ScorePositionsSeuil)
			return false;
		else
			return true;
	}
	
	//Méthode 2 : Compare le score des vitesses au seuil defini
	public static boolean compareVitesses (Signature sTest, Signature sRef) {
		if (Analyse.scoreVitesses (sTest,sRef) < ScoreVitessesSeuil) 
			return false;
		else
			return true;
	}
	
	//Méthode 3 : Compare le score des pressions au seuil defini
	public static boolean comparePressions (Signature sTest, Signature sRef) {
		if (Analyse.scoreVitesses (sTest,sRef) < ScorePressionsSeuil) 
			return false;
		else
			return true;
	}
}
