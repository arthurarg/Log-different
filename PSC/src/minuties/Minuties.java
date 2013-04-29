
package minuties;
import java.util.LinkedList;
import java.util.ListIterator;

import objets.DonneesPoint;
import objets.Signature;
import objets.Signature.Complexite;
import affichageEtTests.Fenetre;

/* Classe Minuties
 * ---------------
 * Role : Permet de détecter les minuties d'une signatures
 * --------------
 */

public class Minuties {
	
	static double seuilLissage=0.5;
	
	static LinkedList<Integer> extremats(double[] t){ // renvoit une liste des posititons des extremats d'un tableau
		LinkedList<Integer> l=new LinkedList<Integer>();
		l.add(0);
		boolean croit;
		if(t[0]<=t[1])
			croit=true;
		else croit=false;
		for(int i=1;i<t.length-1;i++){
			if(croit && t[i]>t[i+1]){ // detection d'un max
				l.add(i);
				croit=false;
			}
			else if(!croit && t[i]<t[i+1]){ // detection d'un min
				l.add(i);
				croit=true;
			}
		}
		l.add(t.length-1);
		return l;
	}
	
	static LinkedList<Integer> lissage(LinkedList<Integer> l, double[] t, double niv){ // élimine les extremats les moins pertinents
		double m=0;
		int last=0, cur, size=0;
		for(ListIterator<Integer> i = l.listIterator(1); i.hasNext();){
			cur=i.next();
			m=m+Math.abs(t[cur]-t[last]);
			last=cur;
			size++;
		}
		m=m/size; // moyenne des hauteurs de pics
		
		LinkedList<Integer> l2=new LinkedList<Integer>(); // 1er lissage fonction de la hauteur entre deux extremats
		last=0;
		for(int x:l){
			if(Math.abs(t[x]-t[last])>niv*m){
				l2.add(x);
				last=x;
			}
		}
		
		LinkedList<Integer> l3=new LinkedList<Integer>(); // 2eme lissage : supprime les points intermediaires
		last=0;
		int x=0, next=0, temp;
		for(ListIterator<Integer> i = l2.listIterator(); i.hasNext();){
			if(t[last]<t[x] && t[x]<t[next]){
			}
			else if(t[last]>t[x] && t[x]>t[next]){	
			}
			else{
				l3.add(x);
				last=x;
			}
			temp=next;
			next=i.next();
			x=temp;
		}
		l3.add(x);
		l3.add(next);
		return l3;
	}
	
	static LinkedList<Integer> minuties(double[] t, double niv){ // procède au repérage des extremats puis au lissage
		return lissage(extremats(t), t, niv);
	}
	
	static double moyenne(double[] t){
		double m=0;
		for(int i=0;i<t.length;i++)
			m=m+t[i];
		return m/t.length;
	}
	
	static double[] donnees(Signature s, int i){
		double[] t;
		DonneesPoint[] tab = s.getDonnees();
		
		int l=tab.length;
		
		
		if(i==3){ // COURBURE
			t=new double[l-1];
			double ax, ay;
			for(int k=0;k<l-1;k++){
				ax=Math.pow(tab[k+1].vx-tab[k].vx, 2);
				ay=Math.pow(tab[k+1].vy-tab[k].vy, 2);
				t[k]=angle(ax, ay)-angle(tab[k].vx, tab[k].vy);
			}
		}
		if(i==2){ // ANGLE
			t=new double[l-1];
			for(int k=0;k<l-1;k++)
				t[k]=tab[k+1].angleVecteurVitesse()-tab[k].angleVecteurVitesse();
		}
		if(i==1){ // ACCELERATION
			t=new double[l-1];
			double ax, ay;
			for(int k=0;k<l-1;k++){
				ax=Math.pow(tab[k+1].vx-tab[k].vx, 2);
				ay=Math.pow(tab[k+1].vy-tab[k].vy, 2);
				t[k]=Math.pow(ax+ay, 1/2)/(tab[k].t-tab[k+1].t);
			}
		}
		else{ // VITESSE
			t=new double[l];
			for(int k=0;k<l;k++)
				t[k]=tab[k].normeVitesse();
		}
		
		return t;
	}
	
	public static double comparaison(Signature s1, Signature s2, int n){ // n=type de minutie
		double[] t1=donnees(s1,n), t2=donnees(s2,n);
		LinkedList<Integer> l1=minuties(t1, seuilLissage), l2=minuties(t2, seuilLissage);
		
		double step=0.1, s=0, ss1=0, ss2=0;
		
		for(int i=0;i<Math.min(t1.length, t2.length)/step;i++){
			s=s+Math.abs(prolongementContinue(t1, i*step)-prolongementContinue(t2, i*step))*step;
			ss1=ss1+Math.abs(prolongementContinue(t1, i*step))*step;
			ss2=ss2+Math.abs(prolongementContinue(t2, i*step))*step;
		}
		
		return 1-s/(ss1+ss2);
	}
	
	public static int nombreMinuties(Signature s, int n){
		if(s==null)
			return 0;
		else return minuties(donnees(s, n), seuilLissage).size();
	}
	
	public static double prolongementContinue(double[] t, double x){
		if(x>t.length-1)
			return 0;
		if(x==t.length-1)
			return t[t.length-1];
		double y=x-(double)((int)x);
		return t[(int)x]+y*t[1+(int)x];
	}
	
	static double angle(double x, double y){
		return Math.atan(y/x);
	}
	
	public static void main(String[] args) {
		Signature s, sref;
		Fenetre f=new Fenetre();
		double[] t, t0;LinkedList<Integer> l0, l;
		int n=0;double moy=0;
		
		while(true){
			s=new Signature();
			n++;
			f.vider();
			t=donnees(s,3);
			l=minuties(t, seuilLissage);

			f.afficherTableau(t);
			//f.afficherTableau(t, l, 0xff0000ff);
			//f.afficherTableau(t0, 0xffff0000);
			//f.afficherTableau(t0, l0, 0xffff0000);
			
			//f.ajouter(sref, 0xFFFF0000);
			//f.ajouter(s);
			double m=0;
			for(int i=0;i<4;i++){
				m=m+nombreMinuties(s, i);
			}
			System.out.println("-->"+m/4+" - "+s.complexite());
			if(s.complexite()==Complexite.FAIBLE)
				System.out.println("FUCK");
			else if(s.complexite()==Complexite.FORTE)
				System.out.println("FAT");
			else System.out.println("OK BEBE");
		}
	}
		
}
