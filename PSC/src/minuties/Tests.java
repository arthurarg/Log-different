package minuties;
import java.awt.image.BufferedImage;

import affichageEtTests.Fenetre;
import affichageEtTests.Image;

import objets.Signature;
import objets.Coordonnees;


import affichageEtTests.Image;
import java.awt.image.BufferedImage;
import java.util.LinkedList;


import javax.swing.JFrame;
import javax.swing.JLabel;


public class Tests {
	public static void main(String[] args) {

		Signature s = new Signature();
		Signature s2 = new Signature();
		DonneesAnglesSortedLinkedList l = MinutiesVectorielles.construireListeAngles(s, 0.05);
		DonneesAnglesSortedLinkedList l2 = MinutiesVectorielles.construireListeAngles(s2, 0.05);
		/*for(int i=0; i<l.size(); i++){
			System.out.println(l.get(i).angle+"   "+l.get(i).j+","+l.get(i).i+","+l.get(i).k);
		}*/
		Fenetre f=new Fenetre();
		f.ajouterSignatureEtMinuties(s, l, 5);
		Fenetre f2=new Fenetre();
		f2.ajouterSignatureEtMinuties(s2, l2, 5);
		System.out.print("score = "+MinutiesVectorielles.scoreMinutiesCourbure(s,s2));
		}
	}


