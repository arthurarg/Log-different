package minuties;
import java.util.LinkedList;
import java.util.ListIterator;

import objets.Signature;

import affichageEtTests.Fenetre;

public class Minuties {
	
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
		
		LinkedList<Integer> l2=new LinkedList<Integer>();
		last=0;
		for(int x:l){
			if(Math.abs(t[x]-t[last])>niv*m){
				l2.add(x);
				last=x;
			}
		}
		return l2;
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
		int l=s.donnees.length;
		
		if(i==2){ // ANGLE
			t=new double[l-1];
			for(int k=0;k<l-1;k++)
				t[k]=s.donnees[k+1].angleVecteurVitesse()-s.donnees[k].angleVecteurVitesse();
		}
		if(i==1){ // ACCELERATION
			t=new double[l-1];
			double ax, ay;
			for(int k=0;k<l-1;k++){
				ax=Math.pow(s.donnees[k+1].vx-s.donnees[k].vx, 2);
				ay=Math.pow(s.donnees[k+1].vy-s.donnees[k].vy, 2);
				t[k]=Math.pow(ax+ay, 1/2)/(s.donnees[k].t-s.donnees[k+1].t);
			}
		}
		else{ // VITESSE
			t=new double[l];
			for(int k=0;k<l;k++)
				t[k]=s.donnees[k].normeVitesse();
		}
		
		return t;
	}
	
	public static void main(String[] args) {
		Signature s;
		Fenetre f=new Fenetre();
		double t[];LinkedList<Integer> l;
		while(true){
			s=new Signature();
			t=donnees(s,2);
			l=minuties(t, 0.5);

			f.afficherTableau(t);
			f.afficherTableau(t, l, 0xffff0000);
		}
	}
		
}
