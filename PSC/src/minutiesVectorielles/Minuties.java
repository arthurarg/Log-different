package minutiesVectorielles;

import java.util.LinkedList;

import objets.Coordonnees;
import objets.DonneesPoint;
import objets.Signature;
import affichageEtTests.Fenetre;
import affichageEtTests.Image;
import minutiesV1.Sommet;

public class Minuties {
public static DonneesAnglesSortedLinkedList construireListeAngles(Signature s, double dist){
	DonneesAnglesSortedLinkedList l=new DonneesAnglesSortedLinkedList();
	DonneesPoint[] tab = s.getDonnees();
	
	for(int i=0;i<tab.length-1;i++){
		
		int j=i;
		while(tab[i].distance(tab[j])<dist && j>0){
			j--;
		}
		int k=i;
		while(tab[i].distance(tab[k])<dist && k<tab.length-2){
			k++;
		}
		if(tab[i].distance(tab[j])>=dist && tab[i].distance(tab[k])>=dist){
			double ijx=tab[j].x-tab[i].x;
			double ijy=tab[j].y-tab[i].y;
			double normeij=Math.sqrt(Math.pow(ijx,2)+Math.pow(ijy,2));
			double ikx=tab[k].x-tab[i].x;
			double iky=tab[k].y-tab[i].y;
			double normeik=Math.sqrt(Math.pow(ikx,2)+Math.pow(iky,2));
			double angle=Math.acos((ijx*ikx+ijy*iky)/(normeij*normeik));
			l.add(new DonneesAngles(angle, j, i, k));	
		}
	}

	for(int i=0; i<l.size()-1; i++){
		for(int j=i+1; j<l.size(); j++){
			if(l.get(i).k>l.get(j).j && l.get(j).k>l.get(i).j){
				l.remove(j);
				j--;
			}
		}
	}
	return l;
}

public static LinkedList<DonneesAngles> trouverMinAngles(Signature s, int nombre, double dist){
	LinkedList<DonneesAngles> l=new LinkedList<DonneesAngles>();
	LinkedList<DonneesAngles> m = construireListeAngles(s, dist);
	return l;
}
}
