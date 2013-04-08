package comparaison;

import minuties.Minuties;
import objets.DonneesPoint;
import objets.Signature;


/* Classe Analyse
 * ---------------
 * Role : Regroupe les méthodes de comparaison, qui font appel aux fonctions auxilaires de la classe Analyse
 * --------------
 */


public class Comparaison {

	//Definition de seuils decomparaisons
	public static final double seuilPos = 0.975;
	public static final double seuilVit = 0.75455;
	public static final double seuilPre = 0.92955;
	public static final double seuilMin0 = 0.86155;
	public static final double seuilMin1 = 0.87175;
	public static final double seuilMin2 = 0.86200;
	
	//Nouvelle ligne
	public static final String NEW_LINE = System.getProperty("line.separator" );

	
	
	//Dis oui ou non l'authentification est ok
	public static boolean comparer (Signature sRef, Signature sTest) {
		// On procede systematiquement a un recalage, dans le doute
		DonneesPoint[][] analyse = Analyse.similitudes(sRef, sTest);
		Signature s1 = new Signature(analyse[0]);
		Signature s2 = new Signature(analyse[1]);
		
		return (Analyse.scorePositions(s1, s2)>=seuilPos
				&& Analyse.scoreVitesses(s1, s2)>=seuilVit
				&& Analyse.scorePressions(s1, s2)>=seuilPre
				&&  Minuties.comparaison(s1, s2, 0)>=seuilMin0
				&&  Minuties.comparaison(s1, s2, 1)>=seuilMin1
				&&  Minuties.comparaison(s1, s2, 2)>=seuilMin2);
	}
	
	//Renvoie une chaine de caractères pour relever les problèmes
	public static String enumProblemes (Signature sRef, Signature sTest) {
		// On procede systematiquement a un recalage, dans le doute
		DonneesPoint[][] analyse = Analyse.similitudes(sRef, sTest);
		Signature s1 = new Signature(analyse[0]);
		Signature s2 = new Signature(analyse[1]);
		
		String s = "";
		if (Analyse.scorePositions(s1, s2)<=seuilPos)
			s+= " Les POSITONS sont trop differentes" + NEW_LINE;
		if (Analyse.scoreVitesses(s1, s2)<=seuilVit)
			s+= " Les VITESSES sont trop differentes" + NEW_LINE;
		if (Analyse.scorePressions(s1, s2)<=seuilPre)
			s+= " La PRESSION au cours de la saisie n'est pas assez proche" + NEW_LINE;
		if (Minuties.comparaison(s1, s2, 0)<=seuilMin0)
			s+= " Les minuties de TYPE 0 ne sont pas les mêmes" + NEW_LINE;
		if (Minuties.comparaison(s1, s2, 1)<=seuilMin1)
			s+= " Les minuties de TYPE 1 ne sont pas les mêmes" + NEW_LINE;
		if (Minuties.comparaison(s1, s2, 2)<=seuilMin2)
			s+= " Les minuties de TYPE 2 ne sont pas les mêmes" + NEW_LINE;
		
		return s;
	}
	
	public static double mesureEcart (Signature sRef, Signature sTest) {
		// On procede systematiquement a un recalage, dans le doute
		DonneesPoint[][] analyse = Analyse.similitudes(sRef, sTest);
		Signature s1 = new Signature(analyse[0]);
		Signature s2 = new Signature(analyse[1]);
		
		return (Analyse.scorePositions(s1, s2)
				* Analyse.scoreVitesses(s1, s2)
				* Analyse.scorePressions(s1, s2)
				*  Minuties.comparaison(s1, s2, 0)
				*  Minuties.comparaison(s1, s2, 1)
				*  Minuties.comparaison(s1, s2, 2));
				
	}
	
}
