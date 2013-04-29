package gestionIO;

import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;

import objets.DonneesPoint;

import com.alderstone.multitouch.mac.touchpad.Finger;
import com.alderstone.multitouch.mac.touchpad.FingerState;
import com.alderstone.multitouch.mac.touchpad.TouchpadObservable;

/* Classe PaveGLULOGIC
 * ---------------
 * Role : Implemente l'acquisition de signature sous Mac OS X
 * --------------
 */

public class PaveGLULOGIC  implements Observer{
	public DonneesPoint[] donnees;
	public LinkedList<DonneesPoint> ListeDoi=new LinkedList<DonneesPoint>();
	public double t0;
	public int y=0;
	public int z=0;
	TouchpadObservable tpo;
	
	//Renvoie la valeur de y
	public int getY(){
		return this.y;
	}
     
	public void run() {
          tpo = TouchpadObservable.getInstance();
          tpo.addObserver(this);
          y = 1;
    }
	
	//Renvoie le tableau de DonneesPoint releve lors de la saisie
	public DonneesPoint[] getSignature(){
			return this.donnees;
		}
	
	public void update(Observable obj, Object arg){
		
		if (z==-1){}
		if (z==0){
			Finger f = (Finger) arg;
			t0=(double)f.getTimestamp()*1000;
			z=1;
		}
		Finger f = (Finger) arg;
		DonneesPoint milieu = new DonneesPoint((double)f.getX(),f.getY(),
				(double)f.getTimestamp()*1000-t0,(double)f.getXVelocity(),(double)f.getYVelocity(), (double)f.getSize());
		ListeDoi.addLast(milieu);
		
		if (y == 1 && f.getState()==FingerState.PRESSED)
			y = 0;
		if (f.getState()==FingerState.RELEASED){
			tpo.deleteObservers();
			int Nbrepoints = ListeDoi.size();
			System.out.println("Nbre de points "+Nbrepoints+ListeDoi);
			this.donnees=new DonneesPoint[Nbrepoints];
				for (int i=0; i<this.donnees.length;i++) {
					this.donnees[i] = new DonneesPoint(ListeDoi.get(i).x,1 - ListeDoi.get(i).y,
						              ListeDoi.get(i).t, ListeDoi.get(i).vx,ListeDoi.get(i).vy,ListeDoi.get(i).s);
				}
				y=1;
				z=-1;
		}
	}
	
	
	
}