package comparaison;
import java.util.LinkedList;

import objets.DonneesPoint;
import objets.Signature;
import flanagan.math.Minimisation;


/* Classe Analyse
 * ---------------
 * Role : Ensemble des fonctions auxiliaires utilisees pour la comparaison de signatures 
 *         et du traitement de l'image test (rotation, translation, etc)
 * --------------
 */



public class Analyse {
	//Definition de seuils
	public static final double angleSeuil = Math.PI/2;
	public static final double ecartRelatifVitesseMoyenne = 0.50;
	
	//Releve la liste des points particuliers
	public static LinkedList<DonneesPoint> listeMinuties(Signature s) {
		//Liste retournee
		LinkedList<DonneesPoint> l = new LinkedList<DonneesPoint>();
		
		//Minutie caracterisee si l'angle entre les vecteurs vitesses est trop grand
		for(int i=0;i<s.size()-1;i++) {
			if (s.getDonnees()[i].angleVecteurVitesse() != Double.POSITIVE_INFINITY 
			    && s.getDonnees()[i+1].angleVecteurVitesse() != Double.POSITIVE_INFINITY
				&& Math.abs(s.getDonnees()[i].angleVecteurVitesse() - s.getDonnees()[i+1].angleVecteurVitesse()) > angleSeuil)
				l.add(s.getDonnees()[i+1]);
		}
		return l;
	}
	
	//Calcule les vitesses moyennes entre deux minuties; renvoie un tableau de ces vitesses
	public static double[] vitessesMoyennes(Signature a, LinkedList<DonneesPoint> u) {
		double[] tab = new double[u.size()+1];
		DonneesPoint tamp = u.poll();
		double vitesse = 0;
		int marqueur = 0, k =0;
		
		for (int j=0; j<a.getDonnees().length;j++) {
			if (tamp!=null && a.getDonnees()[j].equals(tamp)) {
				tab[k] = vitesse/(j-marqueur);
				vitesse = 0;
				k++;
				marqueur = j;
				tamp = u.poll();
			}
			else
				vitesse+=a.getDonnees()[j].normeVitesse();	
		}
		tab[k]=vitesse/(a.getDonnees().length-marqueur);
		
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
		int taille = sTest.size();
		DonneesPoint[] tabRef = sRef.getDonnees();
		DonneesPoint[] tabTest = sTest.getDonnees();

		for (int j=0; j<taille;j++)
			d+= tabTest[j].distance(tabRef[j])/(taille*Math.sqrt(2));
		
		return 1.0 - d;
	}
	
	//Calcule le score de vitesse point a point
	public static double scoreVitesses (Signature sTest, Signature sRef) {
		double d = 0;
		int taille = sTest.size();
		DonneesPoint[] tabRef = sRef.getDonnees();
		DonneesPoint[] tabTest = sTest.getDonnees();
		
		for (int j=0; j<sTest.size();j++) {
			double temp = tabTest[j].differenceVitesses(tabRef[j])/(tabTest[j].normeVitesse()+tabRef[j].normeVitesse());
			if (!Double.isNaN(temp))
				d+= temp;
		}
		return 1.0 - d / taille;
	}
	
	//Calcule le score de pression point à point
	public static double scorePressions (Signature sTest, Signature sRef) {
		double d = 0;
		int taille = sTest.size();
		DonneesPoint[] tabRef = sRef.getDonnees();
		DonneesPoint[] tabTest = sTest.getDonnees();
		
		for (int j=0; j<taille;j++) {
			if ((tabTest[j].s + tabRef[j].s) != 0)
				d+=  Math.abs(tabTest[j].s - tabRef[j].s) / (tabTest[j].s + tabRef[j].s);
		}
		
		return 1.0 - d / taille;
	}
	
	//Calcule le score de temps point par point
	public static double scoreTemps (Signature sTest, Signature sRef) {
		double d = 0, a,b;
		int taille = sTest.size();
		DonneesPoint[] tabRef = sRef.getDonnees();
		DonneesPoint[] tabTest = sTest.getDonnees();
		
		for (int j=1; j<taille;j++){
			a=tabTest[j].t-tabTest[j-1].t;
			b=tabRef[j].t-tabRef[j-1].t;
			d+= max((b-a)/b, (a-b)/a);
		}
		return 1-d/taille;
	}

	//Modifie sRef (coupe des points) et sTest (coupe des points, homothetie, rotation, translation) de sorte que scorePositions soit minimisee
	public static DonneesPoint[][] similitudes(Signature sRef, Signature sTest){
		DonneesPoint[] tabRef = sRef.getDonnees();
		DonneesPoint[] tabTest = sTest.getDonnees();
		
		double angleMax=5*Math.PI/180;
		double rapportMax=2;
		double pourcentagePointsAvantApresCoupes = 0.05;
		
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
	    double ftol = 1e-7;
	    // initial step sizes
        double[] step = {0.1, 0.5, 0.4, 0.4, 0.03, 0.03};
        
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
	    // valeur des parametres au minimum
	    double[] param = min.getParamValues();
	    // resultat
	    double[] params = {minimum, param[0], param[1], param[2], param[3], param[4], param[5]};
	    
	    
		int n1avant = Math.max((int) Math.floor(params[5]*sRef.size()), 0);
		int n1apres = Math.max((int) Math.floor(params[6]*sRef.size()), 0);
		int n2avant = Math.max((int) Math.floor(-params[5]*sTest.size()), 0);
		int n2apres = Math.max((int) Math.floor(-params[6]*sTest.size()), 0);
	    
	    DonneesPoint[] tab2 = new DonneesPoint[sTest.size()-n2avant-n2apres];
		for(int i=0;i<sTest.size()-n2avant-n2apres;i++) {
			tab2[i] = new DonneesPoint(Math.cos(params[1])*params[2]*tabTest[i+n2avant].x - Math.sin(params[1])*params[2]*tabTest[i+n2avant].y + params[3],
										Math.sin(params[1])*params[2]*tabTest[i+n2avant].x + Math.cos(params[1])*params[2]*tabTest[i+n2avant].y + params[4],
										tabTest[i+n2avant].t, 
										Math.cos(params[1])*params[2]*tabTest[i+n2avant].vx - Math.sin(params[1])*params[2]*tabTest[i+n2avant].vy, 
										Math.sin(params[1])*params[2]*tabTest[i+n2avant].vx + Math.cos(params[1])*params[2]*tabTest[i+n2avant].vy, 
										tabTest[i+n2avant].s);
		}
		
		
		DonneesPoint[] tab1 = new DonneesPoint[sRef.size()-n1avant-n1apres];
		for(int i=0;i<sRef.size()-n1avant-n1apres;i++)
			tab1[i] = new DonneesPoint(tabRef[i+n1avant].x, tabRef[i+n1avant].y,tabRef[i+n1avant].t,tabRef[i+n1avant].vx,tabRef[i+n1avant].vy,tabRef[i+n1avant].s);
		
	
		//double[] res = {params[1],params[2], param[4], param[5]};
		
		DonneesPoint[][] aRetourner;
		aRetourner = new DonneesPoint[2][];
		aRetourner[0] = tab1; //sRef
		aRetourner[1] = tab2;  //sTest
		
		return aRetourner;
		
		//return res;
	}
	
	
	//Renvoi max(a,b)
	static double max( double a, double b){
		if(a>=b)
			return a;
		else return b;
	}

	
}
