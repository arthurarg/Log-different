package comparaison;

import java.util.LinkedList;

import objets.DonneesPoint;
import objets.Signature;


/* Classe Minuties
 * ---------------
 * Role : Recensement des differentes minuties presentes dans une signature
 * --------------
 */


public class Minuties {
	double vmax, vmin;
	Signature s;
	LinkedList<Integer> m;
	
	
	//Constructeur
	//TODO Placer cette classe minuties dans le package objets ? Ou pas ?
	Minuties(Signature s){
		this.s=new Signature(s.getDonnees());
		m=new LinkedList<Integer>();
		
		DonneesPoint[] tab = this.s.getDonnees();
		
		vmin=tab[0].normeVitesse();
		vmax=vmin;
		for(int i=0; i<tab.length-1; i++){
			if(tab[i].normeVitesse()<vmin)
				vmin=tab[i].normeVitesse();
			if(tab[i].normeVitesse()>vmax)
				vmax=tab[i].normeVitesse();
		}
		detection();
	}
	
	
	//Detecte les differentes minuties et les stocke dans this.m
	//TODO detecter les autres types de minuties
	public void detection(){
		double t;
		boolean b=false;
		DonneesPoint[] tab = s.getDonnees();
		
		for(int i=0; i<s.size()-1; i++){
			t=(tab[i].normeVitesse()-vmin)/(vmax-vmin);
			
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
