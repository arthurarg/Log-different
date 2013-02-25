package gestionIO;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import objets.DonneesPoint;
import objets.Signature;


/* Classe Enregistrement
 * ---------------
 * Role : Gestion du stockage des donnees sur le disque dur (ecriture et lecture)
 * --------------
 */


public class Enregistrement {
	public static final String NEW_LINE = System.getProperty("line.separator" );
	
	
	//Enregistre une signature sur le disque dur
	public static void enregistrer (String login, Signature s) {
		try {
			//Ouverture du fichier .txt
			FileWriter fichier = new FileWriter("gabarits/"+login + ".txt");
			
			//Enregistrement s.donnees (taille puis contenu)
			fichier.write("" + s.donnees.length);
			for(int j=0; j<s.donnees.length;j++) {
				fichier.write("" + NEW_LINE + s.donnees[j].x + " " + 
			                       s.donnees[j].y + " " +
			                       s.donnees[j].t + " " +
			                       s.donnees[j].vx + " " +
			                       s.donnees[j].vy);
			}
			fichier.close();
		}
		catch (IOException e) {
			System.out.println("Le fichier n'a pu etre ouvert, " +
					" ou un probleme d'ecriture a ete rencontre"); }

		
	}
	
	//Recupere une signature deja enregistree sur le disque dur
	public static Signature ouvrir (String login) {
		//Declaration du tableau et variable temporaire de lecture
		DonneesPoint[] tab;
		String[] nombres;
		
		//Ouverture du fichier .txt et initialisation tableau donnees
		try {
			FileReader file = new FileReader("gabarits/"+login+".txt");
			BufferedReader in = new BufferedReader(file);
			
			
			tab = new DonneesPoint[Integer.parseInt(in.readLine())];
			for (int j=0; j<tab.length;j++) {
				tab[j] = new DonneesPoint(0,0,0,0,0,0);
			}
			
			for (int j=0; j<tab.length;j++) {
				nombres = in.readLine().split(" ");
				if (nombres.length != 5)
					System.out.println("Erreur de lecture du fichier .txt");
				tab[j] = new DonneesPoint(Double.parseDouble(nombres[0]),
						                  Double.parseDouble(nombres[1]),
						                  Double.parseDouble(nombres[2]),
						                  Double.parseDouble(nombres[3]),
						                  Double.parseDouble(nombres[4]),
						                  Double.parseDouble(nombres[5]));
			}
			file.close();
			in.close();
		}
		catch (IOException e) {
			System.out.println("Le fichier n'existe pas"); 
			return null;
		}
		
		//Creation de la signature a partir du tableau
		return new Signature(tab);
	}
}
