package minutiesVectorielles;

import java.util.LinkedList;

import objets.Coordonnees;
import objets.Signature;
import affichageEtTests.Fenetre;
import affichageEtTests.Image;
import minutiesV1.Sommet;

public class Minuties {
public static DonneesAnglesSortedLinkedList construireListeAngles(Signature s, double dist){
	DonneesAnglesSortedLinkedList l=new DonneesAnglesSortedLinkedList();
	for(int i=0;i<s.donnees.length-1;i++){
		
		int j=i;
		while(s.donnees[i].distance(s.donnees[j])<dist && j>0){
			j--;
		}
		int k=i;
		while(s.donnees[i].distance(s.donnees[k])<dist && k<s.donnees.length-2){
			k++;
		}
		if(s.donnees[i].distance(s.donnees[j])>=dist && s.donnees[i].distance(s.donnees[k])>=dist){
			double ijx=s.donnees[j].x-s.donnees[i].x;
			double ijy=s.donnees[j].y-s.donnees[i].y;
			double normeij=Math.sqrt(Math.pow(ijx,2)+Math.pow(ijy,2));
			double ikx=s.donnees[k].x-s.donnees[i].x;
			double iky=s.donnees[k].y-s.donnees[i].y;
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
