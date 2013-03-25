package comparaison;

import objets.DonneesPoint;
import objets.Signature;
import flanagan.math.MinimisationFunction;


/* Classe MinimFunct 
 * ---------------
 * Role : 
 * --------------
 */


public class MinimFunct implements MinimisationFunction{
    private Signature sRef;
    private Signature sTest;
    
    // evaluation function
    	public double function(double[] x){
    		int n1avant = Math.max((int) Math.floor(x[4]*sTest.size()), 0);
    		int n1apres = Math.max((int) Math.floor(x[5]*sTest.size()), 0);
    		int n2avant = Math.max((int) Math.floor(-x[4]*sTest.size()), 0);
    		int n2apres = Math.max((int) Math.floor(-x[5]*sTest.size()), 0);
    		
    		//int n1avant=0;
    		//int n2avant=0;
    		//int n1apres=0;
    		//int n2apres=0;
    		DonneesPoint[] donneestab2 = sTest.getDonnees();
    		DonneesPoint[] tab2 = new DonneesPoint[sTest.size()-n2avant-n2apres];
    		for(int i=0;i<sTest.size()-n2avant-n2apres;i++){
    			tab2[i] = new DonneesPoint(Math.cos(x[0])*x[1]*donneestab2[i+n2avant].x - Math.sin(x[0])*x[1]*donneestab2[i+n2avant].y + x[2],Math.sin(x[0])*x[1]*donneestab2[i+n2avant].x + Math.cos(x[0])*x[1]*donneestab2[i+n2avant].y + x[3],0,0,0,0);
    		}
    		Signature sTestApresSimilitude = new Signature(tab2);
    		
    		DonneesPoint[] donneestab1 = sRef.getDonnees();
    		DonneesPoint[] tab1 = new DonneesPoint[sRef.size()-n1avant-n1apres];
    		for(int i=0;i<donneestab1.length-n1avant-n1apres;i++){
    			tab1[i] = new DonneesPoint(donneestab1[i+n1avant].x, donneestab1[i+n1avant].y,0,0,0,0);
    		}
    		Signature sRefCoupee = new Signature(tab1);
    		
    		sRefCoupee.calculs();
    		sTestApresSimilitude.calculs();
    		return 1-Analyse.scorePositions(sTestApresSimilitude, sRefCoupee);

    }

    	
    // Method to set sRef
    public void setsRef(Signature sRef){
        this.sRef = sRef;
    }
 // Method to set sRef
    public void setsTest(Signature sTest){
        this.sTest = sTest;
    }
}
