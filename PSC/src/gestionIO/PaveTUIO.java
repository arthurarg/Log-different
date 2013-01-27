package gestionIO;

import java.util.LinkedList;

import objets.DonneesPoint;
import TUIO.TuioCursor;
import TUIO.TuioListener;
import TUIO.TuioTime;


/* Classe PaveTUIO
 * ---------------
 * Role : Recuperation des donnees lors de la saisie d'une signature
 * --------------
 */


	public class PaveTUIO implements TuioListener{
		public DonneesPoint[] donnees;
		public LinkedList<DonneesPoint> ListeDoi=new LinkedList<DonneesPoint>();
		public long t0;
		public int y=0;
		
		//Renvoie la valeur de y
		public int getY(){
			return this.y;
		}
		
		//Renvoie le tableau de DonneesPoint releve lors de la saisie
		public DonneesPoint[] getSignature(){
				return this.donnees;
			}
		
		//Recupere les donnees intiales de la saisie
		public void addTuioCursor(TuioCursor tcur) {
				t0=tcur.getStartTime().getTotalMilliseconds();
				DonneesPoint debut = new DonneesPoint((double)tcur.getX(),(double)tcur.getY(),
						(double)tcur.getTuioTime().getTotalMilliseconds()-t0,(double)tcur.getXSpeed(),(double)tcur.getYSpeed());
				ListeDoi.addLast(debut);
				
		}
		
		//Recupere les donnees lors de la saisie
		public void updateTuioCursor(TuioCursor tcur) {
				DonneesPoint milieu = new DonneesPoint((double)tcur.getX(),tcur.getY(),
						(double)tcur.getTuioTime().getTotalMilliseconds()-t0,(double)tcur.getXSpeed(),(double)tcur.getYSpeed());
				ListeDoi.addLast(milieu);
		}
		
		//Stocke toutes les donnees enregistrees lors de la saisie dans un tableau
		public void removeTuioCursor(TuioCursor tcur) {
				DonneesPoint fin = new DonneesPoint((double)tcur.getX(),(double)tcur.getY(),
						(double)tcur.getTuioTime().getTotalMilliseconds()-t0,(double)tcur.getXSpeed(),(double)tcur.getYSpeed());
				ListeDoi.addLast(fin);
				y=1;
				
				int Nbrepoints = ListeDoi.size();
				System.out.println("Nbre de points "+Nbrepoints+ListeDoi);
				this.donnees=new DonneesPoint[Nbrepoints];
					for (int i=0; i<this.donnees.length;i++) {
						this.donnees[i] = new DonneesPoint(ListeDoi.get(i).x,ListeDoi.get(i).y,
							              ListeDoi.get(i).t, ListeDoi.get(i).vx,ListeDoi.get(i).vy);
					}
					
				}
		
		
		//TODO supprimer ??
		public void refresh(TuioTime frameTime) {
			//System.out.println("refresh "+frameTime.getTotalMilliseconds());
		}
		
}
