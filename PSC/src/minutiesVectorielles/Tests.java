package minutiesVectorielles;
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
		DonneesAnglesSortedLinkedList l = Minuties.construireListeAngles(s, 0.001);
		for(int i=0; i<l.size(); i++){
			System.out.println(l.get(i).angle+"   "+l.get(i).j+","+l.get(i).i+","+l.get(i).k);
		}
		Fenetre f=new Fenetre();
		f.ajouterSignatureEtMinuties(s, l, 4);

		}
	}


