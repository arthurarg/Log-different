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
	
	static LinkedList<Integer> lissage(LinkedList<Integer> l, double[] t, double niv){
		double m=0;
		int last=0, cur, size=0;
		for(ListIterator<Integer> i = l.listIterator(1); i.hasNext();){
			cur=i.next();
			m=m+Math.abs(t[cur]-t[last]);
			last=cur;
			size++;
		}
		m=m/size;
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
	
	static double moyenne(double[] t){
		double m=0;
		for(int i=0;i<t.length;i++)
			m=m+t[i];
		return m/t.length;
	}
	
	public static void main(String[] args) {
		Signature s=new Signature();
		Fenetre f=new Fenetre();
		double t[]=new double[s.donnees.length];
		for(int i=0;i<s.donnees.length;i++)
			t[i]=s.donnees[i].normeVitesse();
		LinkedList<Integer> l=lissage(extremats(t), t, 0.7);
		//System.out

		f.afficherTableau(t);
		f.afficherTableau(t, l, 0xffff0000);
		//f.afficherV(s);
	}
		
}
