package comparaison;

import java.util.LinkedList;

import objets.Signature;

public class Minuties {
	double vmax, vmin;
	Signature s;
	LinkedList<Integer> m;
	
	
	//Constructeur
	Minuties(Signature s){
		this.s=s;
		m=new LinkedList<Integer>();
		
		vmin=s.donnees[0].normeVitesse();
		vmax=vmin;
		for(int i=0; i<s.donnees.length-1; i++){
			if(s.donnees[i].normeVitesse()<vmin)
				vmin=s.donnees[i].normeVitesse();
			if(s.donnees[i].normeVitesse()>vmax)
				vmax=s.donnees[i].normeVitesse();
		}
		detection();
	}
	
	
	//Détecte les différentes minuties et les stocke dans this.m
	//TODO détecter les autres types de minuties
	public void detection(){
		double t;
		boolean b=false;
		
		for(int i=0; i<s.donnees.length-1; i++){
			t=(s.donnees[i].normeVitesse()-vmin)/(vmax-vmin);
			
			if(t>0.85 && b==false){
				b=true;
				m.add(i);
			}
			if(t<0.85 && b==true){
				b=false;
				m.add(i);
			}
		}
	}
}
