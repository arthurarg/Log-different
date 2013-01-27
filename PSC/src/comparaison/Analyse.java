package comparaison;
import java.util.LinkedList;
import flanagan.math.Minimisation;

import objets.DonneesPoint;
import objets.Signature;


/* Classe Analyse
 * ---------------
 * Role : ensemble des fonctions auxiliaires utilisees pour la comparaison de signatures
 * --------------
 */



public class Analyse {
	//Definition de seuils decomparaisons
	//TODO Definir seuils et méthode dans la classe Comparaison
	public static final double angleSeuil = Math.PI/2;
	public static final double ecartRelatifVitesseMoyenne = 0.50;
	public static final double ScorePositionsSeuil = 0.9;
	public static final double ScoreVitessesSeuil = 3E-4; // En cours de test, pas de raison que la valeur soit entre 0 et 1;
	

	
	//Releve la liste des points particuliers
	public static LinkedList<DonneesPoint> listeMinuties(Signature s) {
		//Liste retournee
		LinkedList<DonneesPoint> l = new LinkedList<DonneesPoint>();
		
		//Minutie caracterisee si l'angle entre les vecteurs vitesses est trop grand
		for(int i=0;i<s.donnees.length-1;i++) {
			if (s.donnees[i].angleVecteurVitesse() != Double.POSITIVE_INFINITY 
			    && s.donnees[i+1].angleVecteurVitesse() != Double.POSITIVE_INFINITY
				&& Math.abs(s.donnees[i].angleVecteurVitesse() - s.donnees[i+1].angleVecteurVitesse()) > angleSeuil)
				l.add(s.donnees[i+1]);
		}
		return l;
	}
	
	//Calcule les vitesses moyennes entre deux minuties; renvoie un tableau de ces vitesses
	public static double[] vitessesMoyennes(Signature a, LinkedList<DonneesPoint> u) {
		double[] tab = new double[u.size()+1];
		DonneesPoint tamp = u.poll();
		double vitesse = 0;
		int marqueur = 0, k =0;
		
		for (int j=0; j<a.donnees.length;j++) {
			if (tamp!=null && a.donnees[j].equals(tamp)) {
				tab[k] = vitesse/(j-marqueur);
				vitesse = 0;
				k++;
				marqueur = j;
				tamp = u.poll();
			}
			else
				vitesse+=a.donnees[j].normeVitesse();	
		}
		tab[k]=vitesse/(a.donnees.length-marqueur);
		
		return tab;
	}
	
	
	//Compare les vitesses moyennes de deux signatures (si elles ont le meme nombre de minuties)
	public static boolean compareVitessesMoyennes(Signature sTest, Signature sRef) {
		
		LinkedList<DonneesPoint> u = listeMinuties(sTest);
		LinkedList<DonneesPoint> v = listeMinuties(sRef);
		
		if (u.size() != v.size()) {
			System.out.println("nombre de minuties different!");
			return false;
		}
		double[] Vtest = vitessesMoyennes (sTest,u);
		double[] Vref = vitessesMoyennes (sRef,v);
		
		for(int k=0;k<Vtest.length;k++) {
			if (Math.signum(Vtest[k]/Vref[k] - (1- ecartRelatifVitesseMoyenne)) == -1.0
					|| Math.signum(Vtest[k]/Vref[k] - (1 + ecartRelatifVitesseMoyenne)) == 1.0) {
				System.out.println("Vitesses trop differentes : " + Vtest[k]/Vref[k] + " " + Vtest[k] + " " + Vref[k]);
				return false;
			}
		}
		
		return true;
	}
	
	
	//Calcule le score de comparaison point par point
	public static double scorePositions (Signature sTest, Signature sRef) {
		double d = 0;
		int taille = sTest.donnees.length;

		for (int j=0; j<sTest.donnees.length;j++)
			d+= sTest.donnees[j].distance(sRef.donnees[j])/(taille*Math.sqrt(2));
		
		return 1-d;
	}
	
	//Compare le score des positions au seuil defini
	public static boolean comparePositions (Signature sTest, Signature sRef) {
		if (scorePositions (sTest,sRef) < ScorePositionsSeuil)
			return false;
		else
			return true;
	}
	
	
	//Calcule le score de vitesse point a point
	public static double scoreVitesses (Signature sTest, Signature sRef) {
		double d = 0;
		int taille = sTest.donnees.length;
		
		for (int j=0; j<sTest.donnees.length;j++)
			d+= sTest.donnees[j].differenceVitesses(sRef.donnees[j])/(sTest.donnees[j].normeVitesse()+sRef.donnees[j].normeVitesse());
		
		return 1-d/taille;
	}
	
	//Calcule le score de temps point par point
	public static double scoreTemps (Signature sTest, Signature sRef) {
		double d = 0, a,b;
		int taille = sTest.donnees.length;
		
		for (int j=1; j<sTest.donnees.length;j++){
			a=sTest.donnees[j].t-sTest.donnees[j-1].t;
			b=sRef.donnees[j].t-sRef.donnees[j-1].t;
			d+= max((b-a)/b, (a-b)/a);
		}
		return 1-d/taille;
	}
	
	//Compare le score des vitesses au seuil defini
	public static boolean compareVitesses (Signature sTest, Signature sRef) {
		if (scoreVitesses (sTest,sRef) > ScoreVitessesSeuil) 
			return false;
		else
			return true;
	}
	

	//Modifie sRef (coupe des points) et sTest (coupe des points, homothetie, rotation, translation) de sorte que scorePositions soit minimis��
	public static double[] similitudes(Signature sRef, Signature sTest){
		sRef.calculs();
		sTest.calculs();
		double angleMax=45*Math.PI/180;
		double rapportMax=5;
		double pourcentagePointsAvantApresCoupes = 0.1;
		
	    //Create instance of Minimisation
	    Minimisation min = new Minimisation();
	    // Create instace of class holding function to be minimised
	    MinimFunct funct = new MinimFunct();
	    // Set value of the signatures
	    funct.setsRef(sRef);
	    funct.setsTest(sTest);
	    // initial estimates
	    double[] start = {0, 1, 0, 0, 0, 0};
	    // convergence tolerance
	    double ftol = 1e-10;
	    // initial step sizes
        double[] step = {0.2, 0.1, 0.05, 0.05, 0.05, 0.05};
        
	    // Nelder and Mead minimisation procedure
	    min.addConstraint(0, -1, -angleMax);
	    min.addConstraint(0, 1, angleMax);
	    min.addConstraint(1, -1, 1/rapportMax);
	    min.addConstraint(1, 1, rapportMax);
	    min.addConstraint(2, -1, -1);
	    min.addConstraint(2, 1, 1);
	    min.addConstraint(3, -1, -1);
	    min.addConstraint(3, 1, 1);
	    min.addConstraint(4, -1, -pourcentagePointsAvantApresCoupes);
	    min.addConstraint(4, 1, pourcentagePointsAvantApresCoupes);
	    min.addConstraint(5, -1, -pourcentagePointsAvantApresCoupes);
	    min.addConstraint(5, 1, pourcentagePointsAvantApresCoupes);
	    
	    min.nelderMead(funct, start, step, ftol);
	    
	    
	    // get the minimum value
	    double minimum = min.getMinimum();	
	    // valeur des param���tres au minimum
	    double[] param = min.getParamValues();
	    // resultat
	    double[] params = {minimum, param[0], param[1], param[2], param[3], param[4], param[5]};
	    
	    
		int n1avant = Math.max((int) Math.floor(params[5]*sRef.donnees.length), 0);
		int n1apres = Math.max((int) Math.floor(params[6]*sRef.donnees.length), 0);
		int n2avant = Math.max((int) Math.floor(-params[5]*sTest.donnees.length), 0);
		int n2apres = Math.max((int) Math.floor(-params[6]*sTest.donnees.length), 0);
	    
	    DonneesPoint[] tab2 = new DonneesPoint[sTest.donnees.length-n2avant-n2apres];
		for(int i=0;i<sTest.donnees.length-n2avant-n2apres;i++){
			tab2[i] = new DonneesPoint(Math.cos(params[1])*params[2]*sTest.donnees[i+n2avant].x - Math.sin(params[1])*params[2]*sTest.donnees[i+n2avant].y + params[3],Math.sin(params[1])*params[2]*sTest.donnees[i+n2avant].x + Math.cos(params[1])*params[2]*sTest.donnees[i+n2avant].y + params[4],sTest.donnees[i+n2avant].t, Math.cos(params[1])*params[2]*sTest.donnees[i+n2avant].vx - Math.sin(params[1])*params[2]*sTest.donnees[i+n2avant].vy, Math.sin(params[1])*params[2]*sTest.donnees[i+n2avant].vx + Math.cos(params[1])*params[2]*sTest.donnees[i+n2avant].vy);
		}
		
		DonneesPoint[] tab1 = new DonneesPoint[sRef.donnees.length-n1avant-n1apres];
		for(int i=0;i<sRef.donnees.length-n1avant-n1apres;i++){
			tab1[i] = new DonneesPoint(sRef.donnees[i+n1avant].x, sRef.donnees[i+n1avant].y,sRef.donnees[i+n1avant].t,sRef.donnees[i+n1avant].vx,sRef.donnees[i+n1avant].vy);
		}
		
		sTest.donnees=tab2;
		sRef.donnees=tab1;
		sRef.calculs();
		sTest.calculs();
		double[] res = {params[1],params[2], param[4], param[5]};
		return res;
	}
	
	
	//Renvoi max(a,b)
	static double max( double a, double b){
		if(a>=b)
			return a;
		else return b;
	}

	
}
